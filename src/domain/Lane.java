package domain;

import java.util.ArrayList;
import java.util.List;

public class Lane {
    private int laneNr;
    private List<Participant> participants;

    public Lane(int laneNr) {
        this.laneNr = laneNr;
        this.participants = new ArrayList<Participant>();
    }
    
    private void addParticipant(Participant p) {
        participants.add(p);
    }
}
