package model.application.sorter;

import model.domain.IssueBean;

import java.util.Comparator;
import java.util.List;

public class PublishDateSorter implements Sorter<IssueBean> {
    private final Comparator<IssueBean> sorter;

    public PublishDateSorter(PublishDateSorter.SortOption option) {
        if(option.equals(PublishDateSorter.SortOption.ASCENDING)) {
            sorter = ((o1, o2) -> Long.compare(o1.getReportDate(),o2.getReportDate()));
        } else {
            sorter = (o1,o2) -> Long.compare(o2.getReportDate(),o1.getReportDate());
        }
    }

    @Override
    public void sort(List<IssueBean> items) {
        items.sort(sorter);
    }

    public enum SortOption {
        ASCENDING,
        DESCENDING
    }
}
