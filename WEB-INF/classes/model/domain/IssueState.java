package model.domain;

public enum IssueState {

    NEW("New"),
    IN_PROGRESS("In Progress"),
    COMPLETED("Completed"),
    RESOLVED("Resolved"),
    WAITING_ON_REPORTER("Waiting on Reporter"),
    WAITING_ON_THIRD_PARTY("Waiting on Third Party")
    ;

    private final String displayName;

    IssueState(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static IssueState[] getValues() {
        return values();
    }
}
