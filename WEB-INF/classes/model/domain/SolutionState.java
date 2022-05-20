package model.domain;

//represents the state a solution of an issue can be
public enum SolutionState {
    WAITING("Waiting"),
    REJECTED("Rejected"),
    ACCEPTED("Accepted");


    private final String displayName;
    SolutionState(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
