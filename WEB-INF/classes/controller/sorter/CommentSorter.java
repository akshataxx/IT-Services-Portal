package controller.sorter;

import model.domain.CommentBean;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

public class CommentSorter implements Sorter<CommentBean> {

    private final Comparator<CommentBean> sorter;
    private final List<CommentBean> sorted;

    public CommentSorter(SortOption option, Collection<CommentBean> solution) {
        if(option.equals(SortOption.ASCENDING)) {
            sorter = ((o1, o2) -> Long.compare(o1.getDateTime(),o2.getDateTime()));
        } else {
            sorter = (o1,o2) -> Long.compare(o2.getDateTime(),o1.getDateTime());
        }
        this.sorted = new ArrayList<>(solution);
    }

    @Override
    public void sort(List<CommentBean> items) {
        items.sort(sorter);
    }

    public List<CommentBean> getSorted() {
        sort(sorted);
        return sorted;
    }
}
