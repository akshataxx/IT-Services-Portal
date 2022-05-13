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

public class Main {

    public static void main(String[] args) {
        /**
         * TODO
         *   - Notifications
         *   - Statistics
         *   - Knowledge Base
         *   - Knowledge Article
         *   - Save Database
         *
         *  - IT View for Issue
         *  - Add Solution
         *  - Accept/Reject Solution
         *  - Set to waiting for reporter
         *
         *  Suggestions
         *    - Make it look better
         *    - IssueIndex, have things spawn as nothing, then you just have the select rather than the checkboxes
         *    - Have a table for viewing the issues in list
         *    - Have solutions and comments be a little kinder when multiple users are working simultaneously
         */
        //categoryExample();
        //issueExample();
        /*
        System.out.println(System.currentTimeMillis());
        try {
            ConnectionFactory factory = new TestConnectionFactory("localhost\\SQLEXPRESS","jdbcUserseng2050","mypassword");
            StorageImplementation storage = new TsqlStorage(factory);
            ITPortal.getInstance().setStorageImplementation(storage);
            StatisticsReport report = new StatisticsReport();
            CountMap<Category> unsolvedCategory = report.unsolvedEachCategory();
            for(CategoryDefinition main : CategoryDefinition.values()) {
                System.out.println(main.getDisplayName()+": "+unsolvedCategory.get(new Category(main)));
                for(CategoryDefinition sub : main.getSubCategories()) {
                    System.out.println(" - " +sub.getDisplayName()+": "+unsolvedCategory.get(new Category(main,sub)));
                }
            }

            Instant instant = Instant.now();
            DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            System.out.println(format.format(instant.toEpochMilli()));
            System.out.println(unsolvedCategory.get(new Category(CategoryDefinition.HARDWARE,CategoryDefinition.COMP_WONT_TURN_ON)));
            storage.shutdown();
        } catch (SQLException e) {
            e.printStackTrace();
        }

         */
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
