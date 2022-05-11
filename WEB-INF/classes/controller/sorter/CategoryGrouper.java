package controller.sorter;

import model.domain.Category;
import model.domain.IssueBean;

import java.util.Iterator;
import java.util.List;

public class CategoryGrouper implements Sorter<IssueBean> {

    private final Category category;

    public CategoryGrouper(Category category) {
        this.category = category;
    }

    @Override
    public void sort(List<IssueBean> items) {
        Iterator<IssueBean> iterator = items.iterator();
        while (iterator.hasNext()) {
            IssueBean issue = iterator.next();
            if(category.getSub()==null) {
                if(!issue.getCategory().getMain().equals(category.getMain()))
                    iterator.remove();
            } else {
                if(!issue.getCategory().equals(category))
                    iterator.remove();
            }
        }
    }
}
