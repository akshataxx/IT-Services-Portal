package model.domain;

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
