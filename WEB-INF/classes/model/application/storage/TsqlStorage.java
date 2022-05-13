package model.application.storage;

import model.domain.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

public class TsqlStorage implements StorageImplementation {

    private final ConnectionFactory factory;
    private final ConcurrentMap<UUID,UserBean> userCache;
    private final ConcurrentMap<Long,IssueBean> issueCache;

    public TsqlStorage(ConnectionFactory factory) {
        this.factory = factory;
        userCache = new ConcurrentHashMap<>();
        issueCache = new ConcurrentHashMap<>();
    }

    public void initialise() throws StorageException {
        /**
         * Load the database into memory. I understand this is not very good practice, but it saves making a really complicated system with caching, database management, managing the dependencies of the data
         * etc. which is probably well out of the scope of this course
         */
        Connection connection = null;
        PreparedStatement userStatement = null;
        ResultSet userSet = null;
        PreparedStatement issueStatement = null;
        ResultSet issueSet = null;
        PreparedStatement commentStatement = null;
        ResultSet commentSet = null;
        PreparedStatement notificationStatement = null;
        ResultSet notificationSet = null;
        PreparedStatement solutionStatement = null;
        ResultSet solutionSet = null;
        PreparedStatement issueCount = null;
        ResultSet issueCountSet = null;
        try {
            connection = factory.getConnection();
            userStatement = connection.prepareStatement("SELECT * FROM Users");
            userSet = userStatement.executeQuery();
            while (userSet.next()) {
                UserBean user = UserBean.serialize(userSet.getString("uniqueId"), userSet.getString("username"), userSet.getString("surname"),
                        userSet.getString("email"), userSet.getString("username"), userSet.getString("password"), userSet.getString("contactno"), userSet.getInt("unread"), userSet.getString("role"));

                userCache.put(user.getUniqueId(),user);
            }

            issueStatement = connection.prepareStatement("SELECT * FROM Issue");
            issueSet = issueStatement.executeQuery();
            while (issueSet.next()) {
                UUID reporter = UUID.fromString(issueSet.getString("reporter"));
                UserBean user = userCache.get(reporter);
                IssueBean issue = IssueBean.serialize(issueSet.getLong("uniqueId"),issueSet.getString("title"),issueSet.getString("description"),issueSet.getLong("dateReported"),issueSet.getLong("dateResolved"),
                        issueSet.getString("issueStatus"),issueSet.getString("mainCategory"),issueSet.getString("subCategory"),issueSet.getBoolean("knowledgeBase"),user);
                user.addIssue(issue);
                issueCache.put(issue.getUniqueId(),issue);
            }

            commentStatement = connection.prepareStatement("SELECT * FROM Comments");
            commentSet = commentStatement.executeQuery();
            while(commentSet.next()) {
                UUID commenter = UUID.fromString(commentSet.getString("commenter"));
                UserBean user = userCache.get(commenter);
                IssueBean issue = issueCache.get(commentSet.getLong("issue"));
                CommentBean comment = CommentBean.serialize(commentSet.getString("id"),commentSet.getLong("timePosted"),commentSet.getString("comment"),user);
                issue.addComment(comment);
            }

            notificationStatement = connection.prepareStatement("SELECT * FROM Notifications");
            notificationSet = notificationStatement.executeQuery();
            while (notificationSet.next()) {
                IssueBean issue = issueCache.get(notificationSet.getLong("issue"));
                UserBean user = userCache.get(UUID.fromString(notificationSet.getString("person")));
                NotificationBean notification = NotificationBean.serialize(notificationSet.getString("id"),notificationSet.getString("title"),notificationSet.getLong("sendTime"),
                        notificationSet.getString("content"),issue);
                user.addNotification(notification);
            }

            solutionStatement = connection.prepareStatement("SELECT * FROM Solutions");
            solutionSet = solutionStatement.executeQuery();
            while (solutionSet.next()) {
                IssueBean issueBean = issueCache.get(solutionSet.getLong("issue"));
                UserBean user = userCache.get(UUID.fromString(solutionSet.getString("staff")));
                SolutionBean solution = SolutionBean.serialize(solutionSet.getString("id"),solutionSet.getLong("postTime"),solutionSet.getString("details"),solutionSet.getString("status"),user,issueBean);
                issueBean.addSolution(solution);
            }

            issueCount = connection.prepareStatement("SELECT COUNT(*) AS issueCount FROM Issue");
            issueCountSet = issueCount.executeQuery();
            issueCountSet.next();
            IssueBean.setIssueCount(issueCountSet.getLong("issueCount"));

        } catch (SQLException | SerializationException e) {
            throw new StorageException(e);
        } finally {
            closeSqlItem(userSet);
            closeSqlItem(userStatement);
            closeSqlItem(issueSet);
            closeSqlItem(issueStatement);
            closeSqlItem(commentSet);
            closeSqlItem(commentStatement);
            closeSqlItem(notificationSet);
            closeSqlItem(notificationStatement);
            closeSqlItem(solutionSet);
            closeSqlItem(solutionStatement);
            closeSqlItem(connection);
            closeSqlItem(issueCount);
            closeSqlItem(issueCountSet);
        }
    }

    @Override
    public Collection<IssueBean> getAllIssues() {
        return issueCache.values();
    }

    @Override
    public UserBean getUser(UUID uniqueId) {
        return userCache.get(uniqueId);
    }

    @Override
    public UserBean getUser(String username, String password) {
        for(UserBean userBean : userCache.values()) {
            if(userBean.getUsername().equals(username) && userBean.getPassword().equals(password)) {
                return userBean;
            }
        }
        return null;
    }

    @Override
    public Collection<UserBean> getAllUsers() {
        return Collections.unmodifiableCollection(userCache.values());
    }

    @Override
    public void updateUser(UserBean userBean) {
        this.userCache.put(userBean.getUniqueId(),userBean);
    }

    @Override
    public IssueBean getIssue(long id) {
        return issueCache.get(id);
    }

    @Override
    public void updateIssue(IssueBean issueBean) {
        this.issueCache.put(issueBean.getUniqueId(),issueBean);
    }

    private void closeSqlItem(AutoCloseable closeable) {
        if(closeable!=null) {
            try {
                closeable.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void shutdown() {
    }

}
