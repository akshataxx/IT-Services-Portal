import model.application.ITPortal;
import model.application.storage.ConnectionFactory;
import model.application.storage.StorageImplementation;
import model.application.storage.TestConnectionFactory;
import model.application.storage.TsqlStorage;
import model.domain.*;

import java.sql.SQLException;
import java.util.concurrent.ExecutionException;

public class Main {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        //categoryExample();
        //issueExample();
        try {
            ConnectionFactory factory = new TestConnectionFactory("localhost\\SQLEXPRESS","jdbcUserseng2050","mypassword");
            StorageImplementation storage = new TsqlStorage(factory);
            ITPortal.getInstance().setStorageImplementation(storage);
            System.out.println(ITPortal.getInstance().getIssueManager().getIssueById(0));
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
        UserBean bob = new UserBean(UserRole.USER,"Bob","Smith");
        UserBean staff = new UserBean(UserRole.IT_STAFF,"serious","man");
        IssueBean issue = new IssueBean("Help","abcdefgh",bob,new Category(CategoryDefinition.ACCOUNT,CategoryDefinition.WRONG_DETAILS));
        issue.setState(IssueState.IN_PROGRESS);
        issue.setResolveDate(System.currentTimeMillis());
        issue.addComment(new CommentBean(bob,"helppppp!!!!"));
        issue.addComment(new CommentBean(staff,"calm down bro"));
        issue.addSolution(new SolutionBean("turn on and off computer",staff));
        for(CommentBean commentBean : issue.getComments()) {
            System.out.println(commentBean.getComment());
        }

        for(SolutionBean solutionBean : issue.getSolutions()) {
            System.out.println(solutionBean.getResolutionDetails());
        }

        issue.setInKnowledgeBase(true);
        System.out.println(issue.isInKnowledgeBase());

        issue.setState(IssueState.WAITING_ON_REPORTER);

        bob.addNotification(new NotificationBean("Solution","Look at it",issue));
        for(NotificationBean notificationBean : bob.getNotifications()) {
            System.out.println(notificationBean);
        }
    }
}
