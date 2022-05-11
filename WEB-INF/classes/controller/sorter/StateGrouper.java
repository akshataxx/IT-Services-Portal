package controller.sorter;

import model.domain.IssueBean;
import model.domain.IssueState;

import java.util.Iterator;
import java.util.List;

public class StateGrouper implements Sorter<IssueBean> {

    private final IssueState state;

    public StateGrouper(IssueState state) {
        this.state = state;
    }

    @Override
    public void sort(List<IssueBean> items) {
        Iterator<IssueBean> iterator = items.iterator();
        while (iterator.hasNext()) {
            IssueBean issue = iterator.next();
            if(!issue.getState().equals(state))
                iterator.remove();
        }
    }
}
