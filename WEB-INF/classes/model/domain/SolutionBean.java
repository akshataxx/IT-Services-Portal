package model.domain;

import java.util.Objects;
import java.util.UUID;

public class SolutionBean implements DatabaseSerializable {

    private final UUID uniqueId;
    private final long solutionDate;
    private final String resolutionDetails;
    private SolutionState state;
    private final UserBean staffMember;

    public SolutionBean(String resolutionDetails, UserBean staffMember) {
        this.uniqueId = UUID.randomUUID();
        if(!staffMember.getRole().equals(UserRole.IT_STAFF))
            throw new IllegalArgumentException("Non IT staff making solution!");
        this.staffMember = staffMember;
        Objects.requireNonNull(resolutionDetails);
        this.solutionDate = System.currentTimeMillis();
        this.resolutionDetails = resolutionDetails;
        this.state = SolutionState.WAITING;
    }

    private SolutionBean(UUID uniqueId, long solutionDate, String resolutionDetails, SolutionState state, UserBean staffMember) {
        this.uniqueId = uniqueId;
        this.solutionDate = solutionDate;
        this.resolutionDetails = resolutionDetails;
        this.state = state;
        this.staffMember = staffMember;
    }

    public long getSolutionDate() {
        return solutionDate;
    }

    public String getResolutionDetails() {
        return resolutionDetails;
    }

    public SolutionState getState() {
        return state;
    }

    public void setState(SolutionState state) {
        Objects.requireNonNull(state);
        if(this.state==SolutionState.ACCEPTED)
            throw new IllegalArgumentException("The solution is already accepted");
        this.state = state;
    }

    public UUID getUniqueId() {
        return uniqueId;
    }

    public UserBean getStaffMember() {
        return staffMember;
    }

    public static SolutionBean serialize(String uniqueId, long solutionDate, String resolutionDetails, String state, UserBean staffMember) throws SerializationException {
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

        return new SolutionBean(uuid,solutionDate,resolutionDetails,solutionState,staffMember);
    }

    @Override
    public String toString() {
        return "Solution[uuid="+uniqueId + ",state="+state+",content="+resolutionDetails+"]";
    }
}
