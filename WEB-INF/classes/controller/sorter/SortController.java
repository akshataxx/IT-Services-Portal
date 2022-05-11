package controller.sorter;

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
    private final List<Sorter<IssueBean>> grouper;

    public SortController(Collection<IssueBean> issues) {
        Objects.requireNonNull(issues);
        this.grouper = new ArrayList<>();
        this.beans = new ArrayList<>(issues);
    }

    public void sortBy(Sorter<IssueBean> sorter) {
        this.sorter = sorter;
    }

    public void groupBy(Sorter<IssueBean> grouper) {
        Objects.requireNonNull(grouper);
        this.grouper.add(grouper);
    }

    public void clearGroupers() {
        this.grouper.clear();
    }

    public void sortAlphabetical(SortOption option) {
        this.sorter = new AlphabeticalSorter(option);
    }

    public void sortReportDate(SortOption option) {
        this.sorter = new PublishDateSorter(option);
    }

    public void groupCategory(Category category) {
        this.grouper.add(new CategoryGrouper(category));
    }

    public void groupState(IssueState state) {
        this.grouper.add(new StateGrouper(state));
    }

    public void groupRejected(RejectionGrouper.GroupOption option) {
        this.grouper.add(new RejectionGrouper(option));
    }

    public List<IssueBean> getList() {
        List<IssueBean> list = new ArrayList<>(beans);
        for(Sorter<IssueBean> grouper : this.grouper) {
            grouper.sort(list);
        }

        if(sorter!=null)
            sorter.sort(list);

        return list;
    }
}
