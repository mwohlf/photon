package net.wohlfart.photon.events;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.annotation.Nonnull;

import net.wohlfart.photon.tools.ObjectPool.PoolableObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class PoolEventBus implements EventBus<PoolableObject> {
    protected static final Logger LOGGER = LoggerFactory.getLogger(PoolEventBus.class);
    private final ConcurrentLinkedQueue<PoolableObject> queue = new ConcurrentLinkedQueue<PoolableObject>();
    private final List<HandlerInfo> handlers = new CopyOnWriteArrayList<HandlerInfo>();


    public void setSubscriber(Collection<Object> subscribers) {
        LOGGER.debug("register called for {}", subscribers);
        for (Object subscriber : subscribers) {
            register(subscriber);
        }
    }

    @Override
    public void register(Object newSubscriber) {
        LOGGER.debug("register called for {}", newSubscriber);

        Class<? extends Object> clazz = newSubscriber.getClass();
        LOGGER.debug("checking class '{}'", clazz);
        for (Method superClazzMethod : clazz.getMethods()) {
            LOGGER.debug("checking method '{}' in class '{}'", superClazzMethod, clazz);
            if (superClazzMethod.isAnnotationPresent(Subscribe.class)) {
                checkAnnotation(newSubscriber, clazz, superClazzMethod);
            } else {
                LOGGER.debug("no subscribe annotation found for {}", superClazzMethod.getName());
            }
        }
    }

    private void checkAnnotation(Object subscriber, Class<? extends Object> clazz, Method method) {
        LOGGER.info("subscribe found for {} in class {}", method.getName(), clazz);
        Class<?>[] parameterTypes = method.getParameterTypes();
        if (parameterTypes.length != 1) {
            throw new IllegalArgumentException("Method " + method + " on class " + clazz
                    + " has @Subscribe annotation, but requires " + parameterTypes.length
                    + " arguments.  Event handler methods must require a single argument (the event).");
        }

        if (!PoolableObject.class.isAssignableFrom(parameterTypes[0])) {
            throw new IllegalArgumentException("Method " + method + " on class " + clazz
                    + " has parameter type " + parameterTypes[0]
                    + " which is not assignment-compatible with " + PoolableObject.class);
        }

        HandlerInfo info = new HandlerInfo(parameterTypes[0], method, subscriber);
        if (handlers.contains(info)) {
            throw new IllegalArgumentException("Method " + method + " on class " + clazz + " is registered twice.");
        }

        handlers.add(info);
    }

    @Override
    public void unregister(Object subscriber) {
        Iterator<HandlerInfo> iter = handlers.iterator();
        while (iter.hasNext()) {
            HandlerInfo handler = iter.next();
            if (handler.matchesSubscriber(subscriber)) {
                iter.remove();
            }
        }

    }

    @Override
    public void post(@Nonnull PoolableObject event) {
        assert event != null;
        queue.add(event);
    }

    @Override
    public boolean hasEvent() {
        return queue.size() > 0;
    }

    @Override
    public void fireEvent() {
        PoolableObject event = queue.poll();
        if (event == null) {
            LOGGER.warn("ignoring fireEvent() since no events are available, "
                    + "use hasEvent() before calling fireEvent()");
            return;
        }
        int invokeCount = 0;
        for (HandlerInfo handler : handlers) {
            if (handler.matchesEvent(event)) {
                LOGGER.debug("handler found for {}, subscriber is {} on method {}",
                        new Object[] {event, handler.subscriber.getClass().getName(), handler.method.getName()});
                handler.invoke(event);
                invokeCount++;
            }
        }
        LOGGER.debug("invoked {} handler for  event {}", invokeCount, event);
        event.reset();
    }

    private static class HandlerInfo {
        private final Object subscriber;
        private final Method method;
        private final Class<?> eventClass;

        HandlerInfo(Class<?> eventClass, Method method, Object subscriber) {
            this.eventClass = eventClass;
            this.method = method;
            this.subscriber = subscriber;
        }

        void invoke(Object event) {
            try {
                method.invoke(subscriber, event);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        boolean matchesEvent(Object event) {
            return event.getClass().equals(eventClass);
        }

        boolean matchesSubscriber(Object subscriber) {
            return this.subscriber == subscriber;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((eventClass == null) ? 0 : eventClass.hashCode());
            result = prime * result + ((method == null) ? 0 : method.hashCode());
            result = prime * result + ((subscriber == null) ? 0 : subscriber.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object object) {
            if (this == object)
                return true;
            if (object == null)
                return false;
            if (getClass() != object.getClass())
                return false;

            HandlerInfo that = (HandlerInfo) object;

            if (eventClass == null) {
                if (that.eventClass != null)
                    return false;
            } else if (!eventClass.equals(that.eventClass))
                return false;

            if (method == null) {
                if (that.method != null)
                    return false;
            } else if (!method.equals(that.method))
                return false;

            if (subscriber == null) {
                if (that.subscriber != null)
                    return false;
            } else if (!subscriber.equals(that.subscriber))
                return false;

            return true;
        }

    }

    @Override
    public void flush() {
        while (!queue.isEmpty()) {
            PoolableObject event = queue.poll();
            event.reset();
        }
    }

}
