package controller.sorter;

import model.domain.IssueBean;

import java.util.Comparator;
import java.util.List;

public class AlphabeticalSorter implements Sorter<IssueBean> {

    private final Comparator<IssueBean> alphabetical;

    public AlphabeticalSorter(SortOption option) {
        if(option.equals(SortOption.ASCENDING)) {
            alphabetical = (o1, o2) -> o1.getTitle().compareTo(o2.getTitle());
        } else {
            alphabetical = (o1,o2) -> o2.getTitle().compareTo(o1.getTitle());
        }
    }

    @Override
    public void sort(List<IssueBean> items) {
        items.sort(alphabetical);
    }

}
