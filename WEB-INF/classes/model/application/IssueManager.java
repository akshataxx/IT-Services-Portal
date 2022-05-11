package model.application;

import model.application.storage.StorageImplementation;
import model.domain.IssueBean;

import java.util.*;

public class IssueManager {

    private final StorageImplementation implementation;

    public IssueManager(StorageImplementation storageImplementation) {
        this.implementation = storageImplementation;
    }

    public void reportIssue(IssueBean issueBean) {
        Objects.requireNonNull(issueBean);
        issueBean.getReporter().addIssue(issueBean);
        implementation.updateIssue(issueBean);
    }

    public Collection<IssueBean> getAllIssues() {
        return implementation.getAllIssues();
    }

    public Collection<IssueBean> getKnowledgeBase() {
        List<IssueBean> knowledgeBase = new ArrayList<>();
        for(IssueBean issueBean : getAllIssues()) {
            if(issueBean.isInKnowledgeBase())
                knowledgeBase.add(issueBean);
        }

        return Collections.unmodifiableList(knowledgeBase);
    }

    public IssueBean getIssueById(long uniqueId) {
        return implementation.getIssue(uniqueId);
    }

}
