package model.domain;

import model.application.ITPortal;

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
        Objects.requireNonNull(title);
        Objects.requireNonNull(description);
        Objects.requireNonNull(reporter);
        Objects.requireNonNull(category);

        if(!reporter.getRole().equals(UserRole.USER))
            throw new IllegalArgumentException("IT staff cannot report issues");

        this.title = title;
        this.description = description;
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
        if(state.equals(IssueState.RESOLVED))
            throw new IllegalStateException("Issue has already been resolved");

        Objects.requireNonNull(state);
        this.state = state;
    }

    public void setResolveDate(long resolveDate) {
        this.resolveDate = resolveDate;
    }

    public void setCategory(Category category) {
        Objects.requireNonNull(category);
        this.category = category;
    }

    public void setInKnowledgeBase(boolean inKnowledgeBase) {
        if(!(this.state.equals(IssueState.RESOLVED) || this.state.equals(IssueState.COMPLETED)))
            throw new IllegalArgumentException("Issue cannot be added to the knowledge base if it is not completed or resolved");

        this.inKnowledgeBase = inKnowledgeBase;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public long getReportDate() {
        return reportDate;
    }

    public long getResolveDate() {
        return resolveDate;
    }

    public IssueState getState() {
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
        Objects.requireNonNull(solution);
        if(this.state.equals(IssueState.RESOLVED))
            throw new IllegalArgumentException("Cannot add solutions when the issue has been resolved");
        this.solutions.add(solution);
    }

    public Collection<CommentBean> getComments() {
        return Collections.unmodifiableList(comments);
    }

    public void addComment(CommentBean comment) {
        Objects.requireNonNull(comments);
        this.comments.add(comment);
    }

    public long getUniqueId() {
        return uniqueId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
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
