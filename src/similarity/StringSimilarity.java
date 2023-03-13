package similarity;

public class StringSimilarity {

	// Custom param
	public static double MAX_LENGTH_DELTA_TO_CONSIDER_SIMILAR_LENGTH = 0.5;
	public static SimilarityLevel DEFAULT_MINIMUM_SIMILARITY_LEVEL_TO_CONSIDER_SIMILAR_WORDS = SimilarityLevel.VERY_HIGH;
	public static boolean PRINT_DEBUG = false;

	// Used vars
	private int[][] size;
	private char[][] path;
	private String one, other, reference;
	private String longestCommonSubsequence;
	private double weightedSimilarity;
	private SimilarityLevel similarityLevel;

	private StringSimilarity() {}
	
	public StringSimilarity(String one, String other) {
		this();
		this.one = one != null ? one : "";
		this.other = other != null ? other : "";
		this.reference = new String(one.length() >= other.length() ? one : other);

		if ( one.length() > 0
			&& other.length() > 0
			&& (Math.abs(1-((double)one.length()/(double)other.length()))) <= MAX_LENGTH_DELTA_TO_CONSIDER_SIMILAR_LENGTH ) {
			identifyLongestCommonSubsequence();
			calculateWeightedSimilarity();
			defineSimilarityLevel();
		} else {
			this.longestCommonSubsequence = "";
			this.weightedSimilarity = 0d;
			this.similarityLevel = SimilarityLevel.NONE;
		}
	}
	
	private void identifyLongestCommonSubsequence() {
		int rows = one.length()+1;
		int cols = other.length()+1;
		size = new int[rows][cols];
		path = new char[rows][cols];
		
		//init
		for (int r=0; r< rows; r++) {
			for (int c=0; c<cols; c++) {
				size[r][c] = 0;
				if ( r == 0 ^ c == 0){
					if (r>0)
						path[r][c] = one.charAt( r -1 );
					if (c>0)
						path[r][c] = other.charAt( c -1 );
				}else{
					path[r][c] = '_';
				}
			}
		}
		
		//proccess
		for (int i=1; i < rows; i++) {
			for (int j=1; j < cols; j++){
				if (one.substring(i-1, i).equals(other.substring(j-1, j))) {
					size[i][j] = size[i-1][j-1] + 1;
					path[i][j] = '\\';
				}else{
					if (size[i][j-1] > size[i-1][j]){
						size[i][j] = size[i][j-1];
						path[i][j] = '_';
					}else{
						size[i][j] = size[i-1][j];
						path[i][j] = '|';
					}
				}
			}
		}
		
		//solution
		char[] chars = new char[size[rows-1][cols-1]];
		int row = rows-1;
		int col = cols-1;
		int index = size[rows-1][cols-1] - 1;
		while (row > 0 && col > 0){
			if (path[row][col] == '\\'){
				chars[index--] = other.charAt(col-1);
				col--;
				row--;
			}else{
				if (path[row][col] == '|'){
					row--;
				}else{
					col--;
				}
			}
		}
		
		longestCommonSubsequence = new String(chars);
		
	}
	
	public String getLongestCommonSubsequence() {
		return longestCommonSubsequence;
	}
	
	public Double getWeightedSimilarity() {
		return weightedSimilarity;
	}

	public SimilarityLevel getSimilarityLevel() {
		return similarityLevel;
	}

	/**
	 * Percentage 3/4=75% -> Pondered 3/4 = 75*(1+((4-3)/100)) = 75*1.01 = 75.75%
	 * Percentage 6/8=75% -> Pondered 6/8 = 75*(1+((8-6)/100)) = 75*1.02 = 76.5%
	 */
	private void calculateWeightedSimilarity() {
		double lcs_similarity_value = ((double)longestCommonSubsequence.length() / reference.length());
		this.weightedSimilarity = lcs_similarity_value * (1 + ((double)(reference.length() - longestCommonSubsequence.length())/100));
	}
	
	private void defineSimilarityLevel() {
		similarityLevel = SimilarityLevel.NONE;
		for (SimilarityLevel level : SimilarityLevel.values()){
			if (this.weightedSimilarity >= level.getReferenceValue()){
				similarityLevel = level;
			} else {
				break;
			}
		}
	}
		
	public boolean isSimilar(SimilarityLevel minimumSimilarityLevel){
		if (minimumSimilarityLevel == null) {
			minimumSimilarityLevel = DEFAULT_MINIMUM_SIMILARITY_LEVEL_TO_CONSIDER_SIMILAR_WORDS;
		}
		boolean similar = this.weightedSimilarity >= minimumSimilarityLevel.getReferenceValue();
		if (PRINT_DEBUG) {
			System.out.println(String.format("Weighted Similarity by LCS [%6.2f%% similar]: \"%s\" + \"%s\" = \"%s\". [ %s ]", ((double) this.weightedSimilarity * 100), one, other, longestCommonSubsequence, (similar ? "IS SIMILAR" : "NOT SIMILAR")));
		}
		return similar;
	}

}

