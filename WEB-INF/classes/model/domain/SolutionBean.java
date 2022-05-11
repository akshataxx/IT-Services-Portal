package model.domain;

import util.Preconditions;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

public class SolutionBean implements DatabaseSerializable, TextElement {

    private final UUID uniqueId;
    private final long solutionDate;
    private String resolutionDetails;
    private SolutionState state;
    private final UserBean staffMember;
    private final IssueBean issue;

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

    @Override
    public long getDateTime() {
        return solutionDate;
    }

    @Override
    public Date getDate() {
        return new Date(solutionDate);
    }

    @Override
    public String getText() {
        return resolutionDetails;
    }

    @Override
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

    @Override
    public UserBean getAuthor() {
        return staffMember;
    }

    public void acceptSolution() {
        setState(SolutionState.ACCEPTED);
        issue.setState(IssueState.RESOLVED);
        issue.setResolveDate(System.currentTimeMillis());
    }

    public void rejectSolution(String notificationTitle, String notificationComment) {
        if(issue.isResolved())
            throw new IllegalStateException("Issue has already been resolved");

        setState(SolutionState.REJECTED);
        issue.setState(IssueState.IN_PROGRESS);
        this.staffMember.addNotification(new NotificationBean(notificationTitle,notificationComment,issue));
    }

    public static SolutionBean serialize(String uniqueId, long solutionDate, String resolutionDetails, String state, UserBean staffMember, IssueBean issue) throws SerializationException {
        UUID uuid;
        try {
            uuid = UUID.fromString(uniqueId);
        } catch (IllegalArgumentException e) {
            throw new SerializationException("Bad UUID '"+uniqueId+"'",e);
        }

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
