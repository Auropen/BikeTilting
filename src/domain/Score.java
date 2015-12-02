package domain;

public class Score {
	private int scoreID;

	public Score(int scoreID) {
		this.setScoreID(scoreID);
	}

	public int getScoreID() {
		return scoreID;
	}

	public void setScoreID(int scoreID) {
		this.scoreID = scoreID;
	}
}
