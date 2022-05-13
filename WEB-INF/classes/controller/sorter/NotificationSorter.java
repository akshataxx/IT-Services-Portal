package controller.sorter;

import model.domain.NotificationBean;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

public class NotificationSorter implements Sorter<NotificationBean> {

    private final Comparator<NotificationBean> sorter;
    private final List<NotificationBean> sorted;

    public NotificationSorter(SortOption option, Collection<NotificationBean> notifications) {
        if(option.equals(SortOption.ASCENDING)) {
            sorter = ((o1, o2) -> Long.compare(o1.getPostTime(),o2.getPostTime()));
        } else {
            sorter = (o1,o2) -> Long.compare(o2.getPostTime(),o1.getPostTime());
        }
        this.sorted = new ArrayList<>(notifications);
    }

    @Override
    public void sort(List<NotificationBean> items) {
        items.sort(sorter);
    }

    public List<NotificationBean> getSorted() {
        sort(sorted);
        return sorted;
    }
}
