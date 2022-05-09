package model.application;

import model.application.storage.StorageImplementation;
import model.domain.IssueBean;
import model.domain.SolutionBean;
import model.domain.UserBean;

import java.util.Collection;
import java.util.List;

public class IssueManager {

    private final StorageImplementation implementation;

    public IssueManager(StorageImplementation storageImplementation) {
        this.implementation = storageImplementation;
    }

    public void acceptSolution(IssueBean issueBean, SolutionBean solutionBean) {

    }

    public void rejectSolution(IssueBean issueBean, SolutionBean solutionBean) {

    }

    public void reportIssue(IssueBean issueBean) {
        implementation.updateIssue(issueBean);
    }


    public Collection<IssueBean> getAllIssues() {
        return implementation.getAllIssues();
    }

    public IssueBean getIssueById(long uniqueId) {
        return implementation.getIssue(uniqueId);
    }

}
