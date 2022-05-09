package model.application.storage;

import model.domain.IssueBean;
import model.domain.UserBean;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface StorageImplementation {

    Collection<IssueBean> getAllIssues();

    UserBean getUser(UUID uuid);

    UserBean getUser(String username, String password);

    void updateUser(UserBean userBean);

    IssueBean getIssue(long id);

    void updateIssue(IssueBean issueBean);

    void shutdown();
}
