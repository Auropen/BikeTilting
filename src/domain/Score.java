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

	public Score(int scoreID) {
		this(scoreID, 0, "");
	}

	public void addHit() {
		score++;
		hitScore += "1";
	}

	public void addMiss() {
		hitScore += "0";
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
