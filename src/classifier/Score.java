package classifier;

public class Score implements Comparable<Score> {
	private Class instanceClass;
	private Integer score;
	private Double matchRate;
	private Integer occurrences;
	private Double accumulatedSimilarityValue;
	private Score(){}
	
	public Score(Class instanceClass) {
		this();
		this.instanceClass = instanceClass;
		this.score = 0;
		this.occurrences = 0;
		this.matchRate = 0d;
		this.accumulatedSimilarityValue = 0d;
	}
	
	@Override
	public int compareTo(Score other) {
		if (this.score.equals(other.score)) {
			if (this.occurrences.equals(other.occurrences)) {
				if (this.accumulatedSimilarityValue.equals(other.accumulatedSimilarityValue)) {
					return (this.getInstanceClass().getId().compareTo(other.getInstanceClass().getId()));
				} else {
					return (this.accumulatedSimilarityValue.compareTo(other.accumulatedSimilarityValue) * -1);
				}
			} else {
				return (this.occurrences.compareTo(other.occurrences) * -1);
			}
		}
		return (this.score.compareTo(other.score) * -1);
	}
	
	public void addOccurrencesCount() {
		this.occurrences++;
	}
	
	public void increaseAccumulatedSimilarityValue(double value) {
		this.accumulatedSimilarityValue = accumulatedSimilarityValue + value;
	}
		
	public Double getMatchRate() {
		return matchRate;
	}

	public void setMatchRate(Double matchRate) {
		this.matchRate = matchRate;
	}

	public void increaseScore(int points) {
		this.score = score + points;
	}
	
	public Class getInstanceClass() {
		return this.instanceClass;
	}
	
	public int getScore() {
		return this.score;
	}

	@Override
	public String toString() {
		return "Score: " + String.format("%3d",score) + "\t Occurrences: " + String.format("%3d",occurrences) + "\t Accumulated similarity value: " + String.format("%10.2f", (accumulatedSimilarityValue*100)) + "% " + "\t Match rate: " + String.format("%6.2f",(matchRate*100)) + "% = " + instanceClass.getName();
	}
	
}