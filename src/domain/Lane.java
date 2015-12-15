package domain;

import java.util.ArrayList;
import java.util.List;

public class Lane {
    private final int laneID;
    private int laneNr;
    private List<Participant> participants;
    private String ageGroup;

	public Lane(int laneID, int laneNr, String ageGroup) {
		this.laneID = laneID;
        this.laneNr = laneNr;
        this.participants = new ArrayList<Participant>();
        this.ageGroup = ageGroup;
    }
    
    public void addParticipant(Participant p) {
        participants.add(p);
    }

	public int getLaneNr() {
		return laneNr;
	}

	public void setLaneNr(int laneNr) {
		this.laneNr = laneNr;
	}

    public List<Participant> getParticipants() {
		return participants;
	}

	public void setParticipants(List<Participant> participants) {
		this.participants = participants;
	}

	public String getAgeGroup() {
		return ageGroup;
	}

	public void setAgeGroup(String ageGroup) {
		this.ageGroup = ageGroup;
	}

	public int getLaneID() {
		return laneID;
	}
}
