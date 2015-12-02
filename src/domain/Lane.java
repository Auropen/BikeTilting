package domain;

import java.util.ArrayList;
import java.util.List;

public class Lane {
    private int laneNr;
    private List<Participant> participants;

    public Lane(int laneNr) {
        this.setLaneNr(laneNr);
        this.participants = new ArrayList<Participant>();
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
}
