
package domain;

import java.util.List;

public class Lane {
    private int laneNr;
    private List<Participant> participants;
            
    
    private void addParticipant(Participant p) {
        participants.add(p);
    }
}
