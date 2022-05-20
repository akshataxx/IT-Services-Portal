package model.domain;

import util.Preconditions;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

/**
 * Represents the solution to an {@link IssueBean}
 */
public class SolutionBean implements DatabaseSerializable {

    private final UUID uniqueId;
    private final long solutionDate;
    private String resolutionDetails;
    private SolutionState state;
    private final UserBean staffMember;
    private final IssueBean issue;

    //create solution
    public SolutionBean(String resolutionDetails, UserBean staffMember, IssueBean issue) {
        this.uniqueId = UUID.randomUUID();
        if(!staffMember.getRole().equals(UserRole.IT_STAFF))
            throw new IllegalArgumentException("Non IT staff making solution!");
        this.staffMember = staffMember;
        this.solutionDate = System.currentTimeMillis();
        setText(resolutionDetails);
        this.state = SolutionState.WAITING;
        this.issue = issue;
    }

    private SolutionBean(UUID uniqueId, long solutionDate, String resolutionDetails, SolutionState state, UserBean staffMember, IssueBean issue) {
        this.uniqueId = uniqueId;
        this.solutionDate = solutionDate;
        this.resolutionDetails = resolutionDetails;
        this.state = state;
        this.staffMember = staffMember;
        this.issue = issue;
    }

    //getters
    public long getDateTime() {
        return solutionDate;
    }

    public Date getDate() {
        return new Date(solutionDate);
    }

    public String getText() {
        return resolutionDetails;
    }

    //setters, make sure to keep with database constraints
    public void setText(String text) {
        Preconditions.validateLength(text,2000);
        Preconditions.validateNotNull(text);
        this.resolutionDetails = text;
    }

    public SolutionState getState() {
        return state;
    }

    public void setState(SolutionState state) {
        Objects.requireNonNull(state);
        this.state = state;
    }

    public UUID getUniqueId() {
        return uniqueId;
    }

    public UserBean getAuthor() {
        return staffMember;
    }

    //accepts this solution, the issue will now be resolved
    public void acceptSolution() {
        if(!getState().equals(SolutionState.WAITING))
            throw new IllegalStateException("Issue state has already been decided");

        issue.setState(IssueState.RESOLVED);
        setState(SolutionState.ACCEPTED);
        issue.setResolveDate(System.currentTimeMillis());
    }

    //reject the issue, it will now be in progress again, notify the it staff
    public void rejectSolution(String notificationTitle, String notificationComment) {
        if(issue.isResolved())
            throw new IllegalStateException("Issue has already been resolved");

        if(!getState().equals(SolutionState.WAITING))
            throw new IllegalStateException("Issue state has already been decided");

        issue.setState(IssueState.IN_PROGRESS);
        setState(SolutionState.REJECTED);
        this.staffMember.addUnreadNotification(new NotificationBean(notificationTitle,notificationComment,issue));
    }

    //serialize the solution from the database
    public static SolutionBean serialize(String uniqueId, long solutionDate, String resolutionDetails, String state, UserBean staffMember, IssueBean issue) throws SerializationException {
        UUID uuid;
        try {
            uuid = UUID.fromString(uniqueId);
        } catch (IllegalArgumentException e) {
            throw new SerializationException("Bad UUID '"+uniqueId+"'",e);
        }

        //get state
        SolutionState solutionState = null;
        try {
            solutionState = SolutionState.valueOf(state);
        } catch (IllegalArgumentException e) {
            throw new SerializationException("unknown solution state",e);
        }

        return new SolutionBean(uuid,solutionDate,resolutionDetails,solutionState,staffMember,issue);
    }

    @Override
    public String toString() {
        return "Solution[uuid="+uniqueId + ",state="+state+",content="+resolutionDetails+"]";
    }
}
