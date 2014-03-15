package net.wohlfart.photon.tools;

import gnu.trove.map.TObjectIntMap;
import gnu.trove.map.hash.TObjectIntHashMap;

public class EnumWeights {
    
    final TObjectIntMap<Class<?>> weights;

    public EnumWeights(Class<?>... clazzes) {  
        weights = new TObjectIntHashMap<Class<?>>(
                clazzes.length * 2, // initial size
                0.75f ,   // load factor for inc trigger
                0);   // not found value 0
        
        int lastDigits = 1;
        for (int i = clazzes.length - 1; i >= 0 ; i--) {
            @SuppressWarnings("unchecked")
            Class<Enum<?>> clazz = (Class<Enum<?>>) clazzes[i];
            int elems = clazz.getEnumConstants().length;
            weights.put(clazz, lastDigits);
            lastDigits = lastDigits * elems;    
        }
    }

    public int getWeightFor(Enum<?>... inums) {
        int result = 0;
        for (Enum<?> inum : inums) {
            result += weights.get(inum.getDeclaringClass()) * inum.ordinal();
        }
        return result;
    }

}
