package domain;

public class Score {
	private int scoreID;
	private int score;
	private String hitScore;

	public Score(int scoreID, int score, String hitScore) {
		this.setScoreID(scoreID);
		this.setScore(score);
		this.setHitScore(hitScore);
	}

	public int getScoreID() {
		return scoreID;
	}

	public void setScoreID(int scoreID) {
		this.scoreID = scoreID;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public String getHitScore() {
		return hitScore;
	}

	public void setHitScore(String hitScore) {
		this.hitScore = hitScore;
	}
}
