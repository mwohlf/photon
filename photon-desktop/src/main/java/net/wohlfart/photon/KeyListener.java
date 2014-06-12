package net.wohlfart.photon;


import javax.inject.Inject;

import net.wohlfart.photon.events.CommandEvent;
import net.wohlfart.photon.events.MoveEvent;
import net.wohlfart.photon.events.PoolEventBus;
import net.wohlfart.photon.events.RotateEvent;
import net.wohlfart.photon.tools.ObjectPool.PoolableObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jogamp.newt.event.KeyAdapter;
import com.jogamp.newt.event.KeyEvent;

public class KeyListener extends KeyAdapter {
	private static final Logger LOGGER = LoggerFactory.getLogger(KeyListener.class);

	protected final PoolEventBus eventBus;

	@Inject
	public KeyListener(PoolEventBus eventBus) {
		this.eventBus = eventBus;
	}

	// TODO: use a set of keyId mapped factories to be configurable

	@Override
	public void keyPressed(KeyEvent keyEvent) {
		float time = 0.1f;
		PoolableObject evt;
		// FIXME: need a map to be able to configure keys...
		switch (keyEvent.getKeyCode()) {
		case KeyEvent.VK_ESCAPE:
			evt = CommandEvent.exit();
			break;
		case KeyEvent.VK_W:
			evt = MoveEvent.moveForward(time);
			break;
		case KeyEvent.VK_Y:
			evt = MoveEvent.moveBack(time);
			break;
		case KeyEvent.VK_A:
			evt = MoveEvent.moveLeft(time);
			break;
		case KeyEvent.VK_S:
			evt = MoveEvent.moveRight(time);
			break;
		case KeyEvent.VK_Q:
			evt = MoveEvent.moveUp(time);
			break;
		case KeyEvent.VK_X:
			evt = MoveEvent.moveDown(time);
			break;
		case KeyEvent.VK_LEFT:
			evt = RotateEvent.rotateLeft(time);
			break;
		case KeyEvent.VK_RIGHT:
			evt = RotateEvent.rotateRight(time);
			break;
		case KeyEvent.VK_UP:
			evt = RotateEvent.rotateUp(time);
			break;
		case KeyEvent.VK_DOWN:
			evt = RotateEvent.rotateDown(time);
			break;
		case KeyEvent.VK_PAGE_UP:
			evt = RotateEvent.rotateClockwise(time);
			break;
		case KeyEvent.VK_PAGE_DOWN:
			evt = RotateEvent.rotateCounterClockwise(time);
			break;
		case KeyEvent.VK_0:
			evt = CommandEvent.dumpScene();
			break;
		case KeyEvent.VK_9:
			evt = CommandEvent.debugRenderer();
			break;
		default:
			evt = null;
		}
		if (evt!= null) {
			LOGGER.debug("posting event: {}", evt);
			eventBus.post(evt);
		}
	}


	@Override
	public void keyReleased(KeyEvent e) {
	}
}
