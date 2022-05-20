package model.domain;

import util.Preconditions;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class IssueBean implements DatabaseSerializable {

    private final static AtomicLong issueCount = new AtomicLong(0);
    private final long uniqueId;
    private String title;
    private String description;
    private final long reportDate;
    private long resolveDate;
    private IssueState state;
    private Category category;
    private final UserBean reporter;
    private boolean inKnowledgeBase;
    private final List<SolutionBean> solutions;
    private final List<CommentBean> comments;

    public IssueBean(String title, String description, UserBean reporter, Category category) {
        this.uniqueId = issueCount.getAndIncrement();
        Preconditions.validateNotNull(title);
        Preconditions.validateNotNull(description);
        Preconditions.validateNotNull(reporter);
        Preconditions.validateNotNull(category);

        if(!reporter.getRole().equals(UserRole.USER))
            throw new IllegalArgumentException("IT staff cannot report issues");

        setTitle(title);
        setDescription(description);
        this.reportDate = System.currentTimeMillis();
        this.reporter = reporter;
        this.resolveDate = -1;
        this.state = IssueState.NEW;
        this.category = category;
        this.inKnowledgeBase = false;
        this.solutions = new ArrayList<>();
        this.comments = new ArrayList<>();
    }

    private IssueBean(long uniqueId, String title, String description, long reportDate, long resolveDate, IssueState state, Category category, boolean inKnowledgeBase, UserBean reporter) {
        this.uniqueId = uniqueId;
        this.title = title;
        this.description = description;
        this.reportDate = reportDate;
        this.state = state;
        this.category = category;
        this.inKnowledgeBase = inKnowledgeBase;
        this.reporter = reporter;
        this.resolveDate = resolveDate;
        this.solutions = new ArrayList<>();
        this.comments = new ArrayList<>();
    }


    public void setState(IssueState state) {
        if(this.state.equals(IssueState.RESOLVED))
            throw new IllegalStateException("Issue has already been resolved");

        if(!(state.equals(IssueState.RESOLVED) || state.equals(IssueState.COMPLETED)))
            inKnowledgeBase = false;

        Preconditions.validateNotNull(state);
        this.state = state;
    }

    public void setResolveDate(long resolveDate) {
        if(!state.equals(IssueState.RESOLVED))
            throw new IllegalStateException("Issue has not been resolved");

        this.resolveDate = resolveDate;
    }

    public void setCategory(Category category) {
        Preconditions.validateNotNull(category);
        this.category = category;
    }

    public void addToKnowledgeBase() {
        if(!canBeAddedToKnowledgeBase())
            throw new IllegalArgumentException("Issue cannot be added to the knowledge base if it is not completed or resolved");

        this.inKnowledgeBase = true;
    }

    public void removeFromKnowledgeBase() {
        this.inKnowledgeBase = false;
    }

    public boolean canBeAddedToKnowledgeBase() {
        if(inKnowledgeBase)
            return false;

        return this.state.equals(IssueState.RESOLVED) || this.state.equals(IssueState.COMPLETED);
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public long getReportDateTime() {
        return reportDate;
    }

    public long getResolveDateTime() {
        return resolveDate;
    }

    public Date getReportDate() {
        return new Date(reportDate);
    }

    public Date getResolveDate() {
        return new Date(resolveDate);
    }

    public IssueState getState() {
        autoAcceptSolutions();
        return state;
    }

    public Category getCategory() {
        return category;
    }

    public boolean isInKnowledgeBase() {
        return inKnowledgeBase;
    }

    public UserBean getReporter() {
        return reporter;
    }

    public Collection<SolutionBean> getSolutions() {
        return Collections.unmodifiableList(solutions);
    }

    public void addSolution(SolutionBean solution) {
        Preconditions.validateNotNull(solution);
        this.solutions.add(solution);
    }

    public Collection<CommentBean> getComments() {
        return Collections.unmodifiableList(comments);
    }

    public void addComment(CommentBean comment) {
        Preconditions.validateNotNull(comments);
        this.comments.add(comment);
    }

    public void commentOnIssue(CommentBean comment) {
        if(!comment.getAuthor().getRole().equals(UserRole.IT_STAFF))
            if(!comment.getAuthor().equals(this.reporter))
                throw new IllegalStateException("Illegal comment author");

        if(isResolved())
            throw new IllegalStateException("Cannot comment on issue in final state");

        addComment(comment);
    }

    public long getUniqueId() {
        return uniqueId;
    }

    public void setTitle(String title) {
        Preconditions.validateLength(title,0,100);
        Preconditions.validateNotNull(title);
        this.title = title;
    }

    public void setDescription(String description) {
        Preconditions.validateLength(description,0,2000);
        Preconditions.validateNotNull(description);
        this.description = description;
    }

    public SolutionBean getSolution(UUID uniqueId) {
        for(SolutionBean solution : solutions) {
            if(solution.getUniqueId().equals(uniqueId))
                return solution;
        }
        return null;
    }

    public void waitOnReporter(String notificationTitle, String notificationComment) {
        if(isResolved() || state.equals(IssueState.COMPLETED) || state.equals(IssueState.WAITING_ON_REPORTER))
            throw new IllegalStateException("Issue has already been resolved");

        setState(IssueState.WAITING_ON_REPORTER);
        reporter.addUnreadNotification(new NotificationBean(notificationTitle,notificationComment,this));
    }

    public void setInProgress() {
        if(isResolved() || state.equals(IssueState.COMPLETED) || state.equals(IssueState.IN_PROGRESS))
            throw new IllegalStateException("Issue has already been resolved");

        setState(IssueState.IN_PROGRESS);
    }

    public void waitOnThirdParty() {
        if(isResolved() || state.equals(IssueState.COMPLETED) || state.equals(IssueState.WAITING_ON_THIRD_PARTY))
            throw new IllegalStateException("Issue has already been resolved");

        setState(IssueState.WAITING_ON_THIRD_PARTY);
    }

    public void solveIssue(SolutionBean solution, String notificationTitle, String notificationComment) {
        if(isResolved() || state.equals(IssueState.COMPLETED))
            throw new IllegalStateException("Cannot add solutions when the issue has been resolved");

        setState(IssueState.COMPLETED);
        this.reporter.addUnreadNotification(new NotificationBean(notificationTitle,notificationComment,this));
        addSolution(solution);
    }

    public boolean isResolved() {
        autoAcceptSolutions();
        return state.equals(IssueState.RESOLVED);
    }

    private void autoAcceptSolutions() {
        if(state.equals(IssueState.RESOLVED))
            return;

        long oneWeek = Duration.of(7, ChronoUnit.DAYS).toMillis();
        for(SolutionBean solution : solutions) {
            if(!solution.getState().equals(SolutionState.WAITING))
                continue;
            long duration = System.currentTimeMillis()-solution.getDateTime();
            //forcibly accept solution if it has been longer than a week
            if(duration>=oneWeek) {
                solution.acceptSolution();
                break;
            }
        }
    }

    public boolean isRejected() {
        if(isResolved())
            return false;

        //get latest solution;
        if(solutions.size()==0)
            return false;
        SolutionBean latest = solutions.get(0);
        for(SolutionBean solution : getSolutions()) {
            if(solution.getDateTime()>latest.getDateTime())
                latest = solution;
        }

        return latest.getState().equals(SolutionState.REJECTED);
    }

    public static IssueBean serialize(long uniqueId, String title, String description, long reportDate, long resolveDate, String state, String category, String subCategory, boolean inKnowledgeBase, UserBean reporter) throws SerializationException {
        if(title==null)
            throw new SerializationException("Null title");

        if(description==null)
            throw new SerializationException("null description");

        IssueState issueState;
        try {
            issueState = IssueState.valueOf(state);
        } catch (IllegalArgumentException e) {
            throw new SerializationException("Unknown Issue State '"+state+"'",e);
        }

        CategoryDefinition sub;
        try {
            sub = CategoryDefinition.valueOf(subCategory);
        } catch (IllegalArgumentException e) {
            throw new SerializationException("Unknown Main Category '"+category+"'",e);
        }

        CategoryDefinition main;
        try {
            main = CategoryDefinition.valueOf(category);
        } catch (IllegalArgumentException e) {
            throw new SerializationException("Unknown subcategory '"+subCategory+"'",e);
        }

        Category cat = new Category(main,sub);
        return new IssueBean(uniqueId,title,description,reportDate,resolveDate,issueState,cat,inKnowledgeBase,reporter);
    }

    @Override
    public String toString() {
        return "Issue[title="+title+ ",state="+state+",id="+uniqueId+"]";
    }

    public static long getIssueCount() {
        return issueCount.get();
    }

    public static void setIssueCount(long issueCount) {
        IssueBean.issueCount.set(issueCount);
    }
}
