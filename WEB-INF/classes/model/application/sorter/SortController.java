package model.application.sorter;

import model.domain.Category;
import model.domain.IssueBean;
import model.domain.IssueState;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class SortController {

    private final List<IssueBean> beans;
    private Sorter<IssueBean> sorter;
    private Sorter<IssueBean> grouper;

    public SortController(Collection<IssueBean> issues) {
        Objects.requireNonNull(issues);
        this.beans = new ArrayList<>(issues);
    }

    public void sortBy(Sorter<IssueBean> sorter) {
        Objects.requireNonNull(sorter);
        this.sorter = sorter;
    }

    public void groupBy(Sorter<IssueBean> grouper) {
        Objects.requireNonNull(grouper);
        this.grouper = grouper;
    }

    public void sortAlphabetical(AlphabeticalSorter.SortOption option) {
        this.sorter = new AlphabeticalSorter(option);
    }

    public void sortReportDate(PublishDateSorter.SortOption option) {
        this.sorter = new PublishDateSorter(option);
    }

    public void groupCategory(Category category) {
        this.grouper = new CategoryGrouper(category);
    }

    public void groupState(IssueState state) {
        this.grouper = new StateGrouper(state);
    }

    public void groupRejected(RejectionGrouper.GroupOption option) {
        this.grouper = new RejectionGrouper(option);
    }

    public List<IssueBean> getList() {
        List<IssueBean> list = new ArrayList<>(beans);
        if(grouper!=null)
            grouper.sort(list);

        if(sorter!=null)
            sorter.sort(list);

        return list;
    }
}
