package controller.sorter;

import model.domain.IssueBean;

import java.util.Comparator;
import java.util.List;

public class PublishDateSorter implements Sorter<IssueBean> {
    private final Comparator<IssueBean> sorter;

    public PublishDateSorter(SortOption option) {
        if(option.equals(SortOption.ASCENDING)) {
            sorter = ((o1, o2) -> Long.compare(o1.getReportDateTime(),o2.getReportDateTime()));
        } else {
            sorter = (o1,o2) -> Long.compare(o2.getReportDateTime(),o1.getReportDateTime());
        }
    }

    @Override
    public void sort(List<IssueBean> items) {
        items.sort(sorter);
    }


}
