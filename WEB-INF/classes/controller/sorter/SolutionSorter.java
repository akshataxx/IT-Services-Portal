package controller.sorter;

import model.domain.SolutionBean;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

public class SolutionSorter implements Sorter<SolutionBean> {

    private final Comparator<SolutionBean> sorter;
    private final List<SolutionBean> sorted;

    public SolutionSorter(SortOption option, Collection<SolutionBean> solution) {
        if(option.equals(SortOption.ASCENDING)) {
            sorter = ((o1, o2) -> Long.compare(o1.getDateTime(),o2.getDateTime()));
        } else {
            sorter = (o1,o2) -> Long.compare(o2.getDateTime(),o1.getDateTime());
        }
        this.sorted = new ArrayList<>(solution);
    }

    @Override
    public void sort(List<SolutionBean> items) {
        items.sort(sorter);
    }

    public List<SolutionBean> getSorted() {
        sort(sorted);
        return sorted;
    }
}
