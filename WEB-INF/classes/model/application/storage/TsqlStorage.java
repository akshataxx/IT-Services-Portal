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
            //load all users
            connection = factory.getConnection();
            userStatement = connection.prepareStatement("SELECT * FROM Users");
            userSet = userStatement.executeQuery();
            while (userSet.next()) {
                UserBean user = UserBean.serialize(userSet.getString("uniqueId"), userSet.getString("username"), userSet.getString("surname"),
                        userSet.getString("email"), userSet.getString("username"), userSet.getString("password"), userSet.getString("contactno"), userSet.getInt("unread"), userSet.getString("role"));

                userCache.put(user.getUniqueId(),user);
            }

            //load all issues
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

            //load all comments
            commentStatement = connection.prepareStatement("SELECT * FROM Comments");
            commentSet = commentStatement.executeQuery();
            while(commentSet.next()) {
                UUID commenter = UUID.fromString(commentSet.getString("commenter"));
                UserBean user = userCache.get(commenter);
                IssueBean issue = issueCache.get(commentSet.getLong("issue"));
                CommentBean comment = CommentBean.serialize(commentSet.getString("id"),commentSet.getLong("timePosted"),commentSet.getString("comment"),user);
                issue.addComment(comment);
            }

            //load all user notifications
            notificationStatement = connection.prepareStatement("SELECT * FROM Notifications");
            notificationSet = notificationStatement.executeQuery();
            while (notificationSet.next()) {
                IssueBean issue = issueCache.get(notificationSet.getLong("issue"));
                UserBean user = userCache.get(UUID.fromString(notificationSet.getString("person")));
                NotificationBean notification = NotificationBean.serialize(notificationSet.getString("id"),notificationSet.getString("title"),notificationSet.getLong("sendTime"),
                        notificationSet.getString("content"),issue);
                user.addNotification(notification);
            }

            //load all issue solutions
            solutionStatement = connection.prepareStatement("SELECT * FROM Solutions");
            solutionSet = solutionStatement.executeQuery();
            while (solutionSet.next()) {
                IssueBean issueBean = issueCache.get(solutionSet.getLong("issue"));
                UserBean user = userCache.get(UUID.fromString(solutionSet.getString("staff")));
                SolutionBean solution = SolutionBean.serialize(solutionSet.getString("id"),solutionSet.getLong("postTime"),solutionSet.getString("details"),solutionSet.getString("status"),user,issueBean);
                issueBean.addSolution(solution);
            }

            //get which issue we are up to, so we can keep generating PK's
            issueCount = connection.prepareStatement("SELECT COUNT(*) AS issueCount FROM Issue");
            issueCountSet = issueCount.executeQuery();
            issueCountSet.next();
            IssueBean.setIssueCount(issueCountSet.getLong("issueCount"));

        } catch (SQLException | SerializationException e) {
            throw new StorageException(e);
        } finally {
            //close all SQL items
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
        Connection connection = null;
        PreparedStatement issueStatement = null;
        PreparedStatement userStatement = null;
        PreparedStatement commentStatement = null;
        PreparedStatement solutionStatement = null;
        PreparedStatement notificationStatement = null;
        try {
            connection = factory.getConnection();
            //save all users
            userStatement = connection.prepareStatement("UPDATE Users " +
                    "SET username=?, password=?,first_name=?,surname=?,email=?,contactno=?,unread=?,role=? " +
                    "WHERE uniqueId = ? " +
                    "IF @@ROWCOUNT = 0 " +
                    "BEGIN " +
                    "INSERT INTO Users VALUES(?,?,?,?,?,?,?,?,?); " +
                    "END");

            for(UserBean user : getAllUsers()) {
                userStatement.setString(1,user.getUsername());
                userStatement.setString(2,user.getPassword());
                userStatement.setString(3,user.getFirstName());
                userStatement.setString(4,user.getSurname());
                userStatement.setString(5,user.getEmail());
                userStatement.setString(6,user.getContactNo());
                userStatement.setInt(7,user.getUnreadNotifications());
                userStatement.setString(8,user.getRole().name());
                userStatement.setString(9,user.getUniqueId().toString());
                userStatement.setString(10,user.getUniqueId().toString());
                userStatement.setString(11,user.getUsername());
                userStatement.setString(12,user.getPassword());
                userStatement.setString(13,user.getFirstName());
                userStatement.setString(14,user.getSurname());
                userStatement.setString(15,user.getEmail());
                userStatement.setString(16,user.getContactNo());
                userStatement.setInt(17,user.getUnreadNotifications());
                userStatement.setString(18,user.getRole().name());
                userStatement.addBatch();
            }

            userStatement.executeBatch();

            //save all issues
            issueStatement = connection.prepareStatement("" +
                    "UPDATE Issue " +
                    "SET description=?, title=?,mainCategory=?,subCategory=?,dateReported=?,knowledgeBase=?,issueStatus=?,dateResolved=? " +
                    "WHERE uniqueId = ?; " +
                    "IF @@ROWCOUNT = 0 " +
                    "BEGIN " +
                    "    INSERT INTO Issue VALUES(?,?,?,?,?,?,?,?,?,?); " +
                    "END");
            for(IssueBean issue : getAllIssues()) {
                //set parameters
                issueStatement.setString(1,issue.getDescription());
                issueStatement.setString(2,issue.getTitle());
                issueStatement.setString(3,issue.getCategory().getMain().getName());
                issueStatement.setString(4,issue.getCategory().getSub().getName());
                issueStatement.setLong(5,issue.getReportDateTime());
                issueStatement.setBoolean(6,issue.isInKnowledgeBase());
                issueStatement.setString(7,issue.getState().name());
                issueStatement.setLong(8,issue.getResolveDateTime());
                issueStatement.setLong(9,issue.getUniqueId());
                issueStatement.setLong(10,issue.getUniqueId());
                issueStatement.setString(11,issue.getDescription());
                issueStatement.setString(12,issue.getTitle());
                issueStatement.setString(13,issue.getCategory().getMain().getName());
                issueStatement.setString(14,issue.getCategory().getSub().getName());
                issueStatement.setLong(15,issue.getReportDateTime());
                issueStatement.setBoolean(16,issue.isInKnowledgeBase());
                issueStatement.setString(17,issue.getState().name());
                issueStatement.setLong(18,issue.getResolveDateTime());
                issueStatement.setString(19,issue.getReporter().getUniqueId().toString());
                issueStatement.addBatch();
            }
            //execute issue batch
            issueStatement.executeBatch();


            //save all comments
            commentStatement = connection.prepareStatement("UPDATE Comments " +
                    "SET comment=?, timePosted=? " +
                    "WHERE id = ?; " +
                    "IF @@ROWCOUNT = 0 " +
                    "BEGIN " +
                    "    INSERT INTO Comments VALUES(?,?,?,?,?); " +
                    "END");

            for(IssueBean issue : getAllIssues()) {
                for(CommentBean comment : issue.getComments()) {
                    //set parameters
                    commentStatement.setString(1,comment.getText());
                    commentStatement.setLong(2,comment.getDateTime());
                    commentStatement.setString(3,comment.getUniqueId().toString());
                    commentStatement.setString(4,comment.getUniqueId().toString());
                    commentStatement.setString(5,comment.getText());
                    commentStatement.setLong(6,comment.getDateTime());
                    commentStatement.setString(7,comment.getAuthor().getUniqueId().toString());
                    commentStatement.setLong(8,issue.getUniqueId());
                    commentStatement.addBatch();
                }
            }

            //execute batch
            commentStatement.executeBatch();

            solutionStatement = connection.prepareStatement("UPDATE Solutions " +
                    "SET details=?, postTime=?, status=? " +
                    "WHERE id = ?; " +
                    "IF @@ROWCOUNT = 0 " +
                    "BEGIN " +
                    "    INSERT INTO Solutions VALUES(?,?,?,?,?,?); " +
                    "END");

            for(IssueBean issue : getAllIssues()) {
                for(SolutionBean solution : issue.getSolutions()) {
                    solutionStatement.setString(1,solution.getText());
                    solutionStatement.setLong(2,solution.getDateTime());
                    solutionStatement.setString(3,solution.getState().toString());
                    solutionStatement.setString(4,solution.getUniqueId().toString());
                    solutionStatement.setString(5,solution.getUniqueId().toString());
                    solutionStatement.setString(6,solution.getText());
                    solutionStatement.setLong(7,solution.getDateTime());
                    solutionStatement.setString(8,solution.getState().toString());
                    solutionStatement.setString(9,solution.getAuthor().getUniqueId().toString());
                    solutionStatement.setLong(10,issue.getUniqueId());
                    solutionStatement.addBatch();
                }
            }

            solutionStatement.executeBatch();

            notificationStatement = connection.prepareStatement("UPDATE Notifications " +
                    "SET title=?, content=?, sendTime=? " +
                    "WHERE id = ?; " +
                    "IF @@ROWCOUNT = 0 " +
                    "BEGIN " +
                    "    INSERT INTO Notifications VALUES(?,?,?,?,?,?); " +
                    "END");

            for(UserBean user : getAllUsers()) {
                for(NotificationBean notification : user.getNotifications()) {
                    notificationStatement.setString(1,notification.getTitle());
                    notificationStatement.setString(2,notification.getComment());
                    notificationStatement.setLong(3,notification.getPostTime());
                    notificationStatement.setString(4,notification.getUniqueId().toString());
                    notificationStatement.setString(5,notification.getUniqueId().toString());
                    notificationStatement.setString(6,notification.getTitle());
                    notificationStatement.setString(7,notification.getComment());
                    notificationStatement.setLong(8,notification.getPostTime());
                    notificationStatement.setString(9,user.getUniqueId().toString());
                    notificationStatement.setLong(10,notification.getIssueBean().getUniqueId());
                    notificationStatement.addBatch();
                }
            }

            notificationStatement.executeBatch();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeSqlItem(userStatement);
            closeSqlItem(issueStatement);
            closeSqlItem(commentStatement);
            closeSqlItem(solutionStatement);
            closeSqlItem(notificationStatement);
            closeSqlItem(connection);
        }
    }

}
