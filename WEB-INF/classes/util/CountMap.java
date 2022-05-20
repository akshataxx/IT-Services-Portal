package util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * CountMap is a utility class that acts a dictionary for a key to a value that is being counted. Each key
 * has an integer attached to it. {@link #add(Object, int)} to add for the key. {@link #increment(Object)} to increment
 * for the value.
 *
 * @param <T>
 */
public class CountMap<T> {

    private final Map<T,Integer> countMap;

    public CountMap() {
        this.countMap = new HashMap<>();
    }

    public void increment(T t) {
        add(t,1);
    }

    public void add(T t, int amount) {
        //if it contains then add the amount
        if(countMap.containsKey(t)) {
            int curr = countMap.get(t);
            curr += amount;
            countMap.put(t,curr);
        } else {
            //otherwise this is new, set the amount
            countMap.put(t,amount);
        }
    }

    public int get(T t) {
        //get the count of the key
        Integer value = countMap.get(t);
        if(value==null)
            return 0;

        return value;
    }

    public boolean contains(T t) {
        return countMap.containsKey(t);
    }

    public Collection<Map.Entry<T,Integer>> entrySet() {
        return countMap.entrySet();
    }

    @Override
    public String toString() {
        return countMap.toString();
    }

}
