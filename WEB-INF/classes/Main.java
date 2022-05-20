import model.application.ITPortal;
import model.application.StatisticsReport;
import model.application.storage.ConnectionFactory;
import model.application.storage.StorageImplementation;
import model.application.storage.TestConnectionFactory;
import model.application.storage.TsqlStorage;
import model.domain.*;
import util.CountMap;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Iterator;
import java.util.UUID;

public class Main {

    public static void main(String[] args) {

        System.out.println(System.currentTimeMillis());
        try {
            ConnectionFactory factory = new TestConnectionFactory("localhost\\SQLEXPRESS","jdbcUserseng2050","mypassword");
            StorageImplementation storage = new TsqlStorage(factory);
            ITPortal.getInstance().setStorageImplementation(storage);
            for(IssueBean issue : ITPortal.getInstance().getIssueManager().getAllIssues()) {
                issue.addComment(new CommentBean(ITPortal.getInstance().getUserManager().getUserById(UUID.fromString("f9d5d4dc-cc69-11ec-9d64-0242ac120002")),"Get Good"));
            }
            storage.shutdown();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void categoryExample() {
        //create category
        Category category = new Category(CategoryDefinition.ACCOUNT,CategoryDefinition.WRONG_DETAILS);
        System.out.println(category);
        //loop through all main categories
        for(CategoryDefinition definition : CategoryDefinition.values()) {
            System.out.println(definition.getDisplayName());
            //loop through all subcategories
            for(CategoryDefinition sub : definition.getSubCategories()) {
                System.out.println(" - "+sub.getDisplayName());
            }
        }

        System.out.println(CategoryDefinition.ACCOUNT.getSubCategories());

        //serialize
        CategoryDefinition definition = CategoryDefinition.valueOf("NETWORK");
        System.out.println(definition);
    }

    public static void issueExample() {
        //business rules aren't being enforced in the model, only in the model.application/controller
        UserBean bob = new UserBean(UserRole.USER,"Bob","Smith","bob","smith");
        UserBean staff = new UserBean(UserRole.IT_STAFF,"serious","man","serious","man");
        IssueBean issue = new IssueBean("Help","abcdefgh",bob,new Category(CategoryDefinition.ACCOUNT,CategoryDefinition.WRONG_DETAILS));
        issue.setState(IssueState.IN_PROGRESS);
        issue.setResolveDate(System.currentTimeMillis());
        issue.addComment(new CommentBean(bob,"helppppp!!!!"));
        issue.addComment(new CommentBean(staff,"calm down bro"));
        issue.addSolution(new SolutionBean("turn on and off computer",staff,issue));
        for(CommentBean commentBean : issue.getComments()) {
            System.out.println(commentBean.getText());
        }

        for(SolutionBean solutionBean : issue.getSolutions()) {
            System.out.println(solutionBean.getAuthor());
        }

        issue.addToKnowledgeBase();
        System.out.println(issue.isInKnowledgeBase());

        issue.setState(IssueState.WAITING_ON_REPORTER);

        bob.addNotification(new NotificationBean("Solution","Look at it",issue));
        for(NotificationBean notificationBean : bob.getNotifications()) {
            System.out.println(notificationBean);
        }
    }
}
