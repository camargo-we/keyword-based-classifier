package classifier;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import similarity.StringSimilarity;
import similarity.SimilarityLevel;

public class GenericKeywordBasedTextClassifier {
	// Fixed params
	private static final String EMPTY_STRING = "";
	private static final String SPACE_CHAR = " ";
	private static final double MAX_LENGTH_DELTA_TO_CONSIDER_CONTENT_SIMILAR_KEYWORD = 0.25d;
	// Custom params
	public static int RANKING_SIZE = Integer.MAX_VALUE;
	public static int MINIMUM_SCORE_FOR_RANK = 3;
	public static boolean USE_TIEBREAKERS_CRITERIA = false;
	private static int MINIMUM_KEYWORD_LENGTH = 3;
	public static int POINTS_FOR_SINGLE_KEYWORD_MATCH = 1;
	public static int POINTS_FOR_COMPOUND_KEYWORD_MATCH = 2;
	public static int POINTS_FOR_PARTIAL_NAME_MATCH = 3;
	public static int POINTS_FOR_HIGH_RELEVANCE_MATCH = 5;
	public static int POINTS_FOR_NAME_TITLE_MATCH = 8;
	public static boolean PRINT_DEBUG = false;

	public static boolean NORMALIZE_STRING = true;
	public static boolean SMALLCASE_STRING = true;
	public static boolean FORCE_RANKING = false;

	// Used vars
	private Document document;
	private Set<Class> classes;
	private Map<Class, Score> standings;
	private List<Score> ranking;
	private Class assignedClass;
	private GenericKeywordBasedTextClassifier() {}
	private int totalScoreAssigned = 0;

	public GenericKeywordBasedTextClassifier(Set<Class> classes, Document document) {
		this();
		this.classes = classes;
		this.document = document;
		this.standings = new HashMap<Class, Score>();
		classify();
	}

	public void classify() {

		if (PRINT_DEBUG) {
			System.out.println("------------------------------ [ D O C U M E N T ] -----------------------------");
			System.out.println(String.format("document.title: %s", getNotNullLowerCaseNormalizedString(document.getTitle())));
			System.out.println(String.format("document.content: %s", getNotNullLowerCaseNormalizedString(document.getContent())));
		}

		if (classes != null && !classes.isEmpty()) {
			// Initialize standings with zero score.
			this.standings = new HashMap<Class, Score>();
			for (Class c : classes) {
				standings.put(c, new Score(c));
			}

			// Keywords comparisons procedures
			if (!getNotNullLowerCaseNormalizedString(document.getContent()).isEmpty()) {
				String content = getNotNullLowerCaseNormalizedString(document.getContent());
				for (Class classInstance : classes) {
					// Get keyword set
					String[] allKeywords = classInstance.getKeywords().split(Class.DEFAULT_KEYWORDS_BREAKER);
					Set<String> keywords = getValidWords(allKeywords, 0);

					// Separation of simple and compound keywords.
					Set<String> compoundKeywords = new HashSet<String>();
					Set<String> singleKeywords = new HashSet<String>();

					// Preprocess keywords to generate specific keywords sets (single, compound)
					for (String keyword : keywords ) {
						// Generate keywords sets (single, compound).
						if (keyword.contains(SPACE_CHAR)) {
							compoundKeywords.add(keyword);
						} else {
							singleKeywords.add(keyword);
						}

						// If the document content length resembling keyword length, it performs the high relevance comparison and ignores the keyword.
						if ((Math.abs(1-((double)keyword.length()/(double)content.length()))) <= MAX_LENGTH_DELTA_TO_CONSIDER_CONTENT_SIMILAR_KEYWORD) {
							compareBy_HIGH_RELEVANCE(content, keyword, classInstance);
						}

					}

					// Process compound keywords
					compareBy_COMPOUND_KEYWORD(content, compoundKeywords, classInstance);

					// Process single keywords
					compareBy_SINGLE_KEYWORD(content, singleKeywords, classInstance);

				}
			}

			// ClassName comparisons procedures
			if (!getNotNullLowerCaseNormalizedString(document.getTitle()).isEmpty()) {
				compareBy_PARTIAL_CLASSNAME(document.getTitle());
				compareBy_FULL_CLASSNAME(document.getTitle());
			}

		}

		if (PRINT_DEBUG) {
			System.out.println("\t........................................................................");
		}

		if (!standings.isEmpty()) {
			List<Score> completeStandings = new ArrayList<Score>(this.standings.values());
			ranking = new ArrayList<Score>(completeStandings.size());
			for (Score ranked : completeStandings) {
				if (FORCE_RANKING || ranked.getScore() >= MINIMUM_SCORE_FOR_RANK) {
					ranking.add(ranked);
				}
			}
			if (!ranking.isEmpty()) {
				Collections.sort(ranking);
				for (int i=0; (i<ranking.size() && i<RANKING_SIZE); i++ ) {
					totalScoreAssigned += ranking.get(i).getScore();
				}
				for (int i=0; (i<ranking.size() && i<RANKING_SIZE); i++ ) {
					ranking.get(i).setMatchRate((double)ranking.get(i).getScore() / (double) totalScoreAssigned);
				}
				if (FORCE_RANKING ||
						USE_TIEBREAKERS_CRITERIA ||
						ranking.size() == 1 ||
						ranking.get(0).getScore() > ranking.get(1).getScore()) {
					assignedClass = ranking.get(0).getInstanceClass();
				}
			}

			if (PRINT_DEBUG) {
				Collections.sort(completeStandings);
				System.out.println("\tRanking (Top" + getRanking().size() + "):");
				for (Score score : getRanking() ) {
					System.out.println("\t\t" + score.toString());
				}
				System.out.println("\tClass: " + (assignedClass != null ? assignedClass.getName() : "NOT CLASSIFIED") + "\n");
			}
		}

	}

	/** Returns the Document's Assigned Class.
	 *  May return NULL if there is a tie in rank OR if no class has reached the MINIMUM_SCORE_FOR_CLASSIFICATION.
	 * */
	public Class getAssignedClass() {
		return this.assignedClass;
	}

	/** Returns the Ordered List of Ranked Classes that reached the MINIMUM_SCORE_FOR_CLASSIFICATION
	 * May return Empty List.
	 * */
	public List<Score> getRanking() {
		if (this.ranking.size() > RANKING_SIZE) {
			return this.ranking.subList(0, RANKING_SIZE-1);
		}
		return this.ranking;
	}

	private void compareBy_FULL_CLASSNAME(String text) {
		String documentTitle = getNotNullLowerCaseNormalizedString(text);
		for (Class classInstance : classes) {
			String className = getNotNullLowerCaseNormalizedString(classInstance.getName());
			StringSimilarity similarity = new StringSimilarity(className, documentTitle);
			if (similarity.isSimilar(SimilarityLevel.ALMOST_TOTAL)) {
				standings.get(classInstance).increaseScore(POINTS_FOR_NAME_TITLE_MATCH);
				standings.get(classInstance).addOccurrencesCount();
				standings.get(classInstance).increaseAccumulatedSimilarityValue(similarity.getWeightedSimilarity());
				if (PRINT_DEBUG) {
					System.out.println("\t +" + POINTS_FOR_NAME_TITLE_MATCH + " points " + classInstance.getName() + ": ClassName DocumentTitle Match [" + similarity.getLongestCommonSubsequence() + "].");
				}
			}
		}
	}

	private void compareBy_PARTIAL_CLASSNAME(String text) {
		String documentTitle = getNotNullLowerCaseNormalizedString(text);
		for (Class classInstance : classes) {
			// Set classname as keyword array
			String[] allClassNameKeywords = getNotNullLowerCaseNormalizedString(classInstance.getName()).split(SPACE_CHAR);
			Set<String> validatedClassNameKeywords = getValidWords(allClassNameKeywords, MINIMUM_KEYWORD_LENGTH);

			// Document Title to Bag of Words (BoW)
			String[] allTextWords = documentTitle.split(SPACE_CHAR);
			Set<String> validTextWords = getValidWords(allTextWords, MINIMUM_KEYWORD_LENGTH);

			// Comparisons
			String debugMessage = null;
			if (PRINT_DEBUG) {
				debugMessage = "\t +" + POINTS_FOR_PARTIAL_NAME_MATCH + " points " + classInstance.getName() + ": Partial Name Match";
			}
			doKeywordBowComparisons(standings.get(classInstance), validatedClassNameKeywords, validTextWords, SimilarityLevel.VERY_HIGH, POINTS_FOR_PARTIAL_NAME_MATCH, debugMessage);
		}
	}

	private void compareBy_HIGH_RELEVANCE(String text, String keyword, Class classInstance) {
		StringSimilarity similarity = new StringSimilarity(text, keyword);
		if (similarity.isSimilar(SimilarityLevel.ALMOST_TOTAL)) {
			standings.get(classInstance).increaseScore(POINTS_FOR_HIGH_RELEVANCE_MATCH);
			standings.get(classInstance).addOccurrencesCount();
			standings.get(classInstance).increaseAccumulatedSimilarityValue(similarity.getWeightedSimilarity());
			if (PRINT_DEBUG) {
				System.out.println("\t +" + POINTS_FOR_HIGH_RELEVANCE_MATCH + " points " + classInstance.getName() + ": The Document Content Look Like The Keyword [" + similarity.getLongestCommonSubsequence() + "].");
			}
		}
	}

	private void compareBy_COMPOUND_KEYWORD(String text, Set<String> compoundKeywords, Class classInstance) {
		String content = getNotNullLowerCaseNormalizedString(text);
		for (String compoundKeyword : compoundKeywords ) {
			// Contains the compound keyword.
			if (content.contains(compoundKeyword)) {
				standings.get(classInstance).increaseScore(POINTS_FOR_COMPOUND_KEYWORD_MATCH);
				standings.get(classInstance).addOccurrencesCount();
				standings.get(classInstance).increaseAccumulatedSimilarityValue(1d);
				if (PRINT_DEBUG) {
					System.out.println("\t +" + POINTS_FOR_COMPOUND_KEYWORD_MATCH + " points " + classInstance.getName() + ": Document contains the Compound Keyword [" + compoundKeyword + "].");
				}
			}
			// Decompound keyword to process like single one.
			String[] spacedToSingleKeyword = compoundKeyword.split(SPACE_CHAR);
			Set<String> validDecompoundKeywords = getValidWords(spacedToSingleKeyword, MINIMUM_KEYWORD_LENGTH);
			compareBy_SINGLE_KEYWORD(content, validDecompoundKeywords, classInstance);
		}
	}

	private void compareBy_SINGLE_KEYWORD(String text, Set<String> keywords, Class classInstance) {
		// Document to Bag of Words (BoW)
		String[] allTextWords = text.split(SPACE_CHAR);
		Set<String> words = getValidWords(allTextWords, MINIMUM_KEYWORD_LENGTH);

		// Comparisons
		String debugMessage = null;
		if (PRINT_DEBUG) {
			debugMessage = "\t +" + POINTS_FOR_SINGLE_KEYWORD_MATCH + " points " + classInstance.getName() + ": Single Keyword Match Occurrence";
		}
		doKeywordBowComparisons(standings.get(classInstance), keywords, words, SimilarityLevel.VERY_HIGH, POINTS_FOR_SINGLE_KEYWORD_MATCH, debugMessage);
	}

	private void doKeywordBowComparisons(Score classScore, Set<String> keywords, Set<String> bow, SimilarityLevel minSimilarityLevel, int matchValue, String debugMessage ) {
		for (String keyword : keywords ) {
			for (String word : bow) {
				StringSimilarity similarity = new StringSimilarity(keyword, word);
				if (similarity.isSimilar(minSimilarityLevel)) {
					standings.get(classScore.getInstanceClass()).increaseScore(matchValue);
					standings.get(classScore.getInstanceClass()).addOccurrencesCount();
					standings.get(classScore.getInstanceClass()).increaseAccumulatedSimilarityValue(similarity.getWeightedSimilarity());
					if (PRINT_DEBUG && debugMessage != null) {
						System.out.println(debugMessage + " [" + similarity.getLongestCommonSubsequence() + "].");
					}
				}
			}
		}
	}

	private Set<String> getValidWords(String[] allWords, int minimumLength) {
		Set<String> validWords = new HashSet<String>(allWords.length);
		for (String w : allWords){
			String word = getNotNullLowerCaseNormalizedString(w);
			if (!word.isEmpty() && word.length() >= minimumLength){
				validWords.add(word);
			}
		}
		return validWords;
	}

	private String getNotNullLowerCaseNormalizedString(String string) {
		if (string != null && !string.isEmpty()){
			if (NORMALIZE_STRING) {
				string = Normalizer.normalize(string, Normalizer.Form.NFD);
				string = string.replaceAll("[^\\p{ASCII}]", EMPTY_STRING);
				string = string.replaceAll("[(;)|(,)|(:)|(!)|(?)|(.)|(*)|(\\)|(/)|(+)|(\\-)|(_)|(|)|(\n)|(\r)|(\t)|(\b)]", SPACE_CHAR);
				string = string.replaceAll("[^(a-z)|(A-Z)|(0-9)|( )]", EMPTY_STRING);
			}
			if (SMALLCASE_STRING) {
				string = string.toLowerCase();
			}
			// Remove multiples spaces
			string = string.trim().replaceAll("\\s\\s+", SPACE_CHAR);
			return string;
		}
		return EMPTY_STRING;
	}

}
