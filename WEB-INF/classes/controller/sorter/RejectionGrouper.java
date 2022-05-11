package controller.sorter;

import model.domain.IssueBean;

import java.util.Iterator;
import java.util.List;

public class RejectionGrouper implements Sorter<IssueBean> {

    private final GroupOption option;

    public RejectionGrouper(GroupOption option) {
        this.option = option;
    }

    @Override
    public void sort(List<IssueBean> items) {
        Iterator<IssueBean> iterator = items.iterator();
        while (iterator.hasNext()) {
            IssueBean issue = iterator.next();
            if(option.equals(GroupOption.RESOLVED)) {
                if (!issue.isResolved())
                    iterator.remove();
            } else {
                if(!issue.isRejected())
                    iterator.remove();
            }
        }
    }

    public enum GroupOption {
        REJECTED,
        RESOLVED
        ;
    }
}
