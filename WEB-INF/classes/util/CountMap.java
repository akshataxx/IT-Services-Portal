package util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class CountMap<T> {

    private final Map<T,Integer> countMap;

    public CountMap() {
        this.countMap = new HashMap<>();
    }

    public void increment(T t) {
        add(t,1);
    }

    public void add(T t, int amount) {
        if(countMap.containsKey(t)) {
            int curr = countMap.get(t);
            curr += amount;
            countMap.put(t,curr);
        } else {
            countMap.put(t,amount);
        }
    }

    public int get(T t) {
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
