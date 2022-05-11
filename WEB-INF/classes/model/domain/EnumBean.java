package model.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class EnumBean {

    public EnumBean() {
    }

    public Collection<CategoryDefinition> getCategoryValues() {
        return Arrays.asList(CategoryDefinition.values());
    }

    public IssueState[] getIssueValues() {
        return IssueState.values();
    }

    public SolutionState[] getSolutionValues() {
        return SolutionState.values();
    }
}
