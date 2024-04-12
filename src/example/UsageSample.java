package example;

import classifier.Document;
import classifier.GenericKeywordBasedTextClassifier;
import classifier.Score;

public class UsageSample {

    public static void main(String[] args) {

        GenericKeywordBasedTextClassifier.RANKING_SIZE = 5;
        GenericKeywordBasedTextClassifier.MINIMUM_SCORE_FOR_RANK = 1;
        GenericKeywordBasedTextClassifier.USE_TIEBREAKERS_CRITERIA = true;
        GenericKeywordBasedTextClassifier.FORCE_RANKING = false;
        GenericKeywordBasedTextClassifier.NORMALIZE_STRING = true;
        GenericKeywordBasedTextClassifier.LOWERCASE_STRING = true;
        GenericKeywordBasedTextClassifier.PRINT_DEBUG = true;

        for (Document doc : Data.getDocuments()) {
            GenericKeywordBasedTextClassifier classifier = new GenericKeywordBasedTextClassifier(Data.getPurposeClasses(), doc);

            if (classifier.getAssignedClass() != null) {
                System.out.println("[DIRECT OUTPUT] Classified as: " + classifier.getAssignedClass().getName());
            } else {
                System.out.println("[INTERPRETABLE OUTPUT] Not directly classified! Ranking: ");
                for (Score score : classifier.getRanking()) {
                    System.out.println("\t" + score.toString());
                }
            }

            System.out.println("\n\n");

        }
    }

}