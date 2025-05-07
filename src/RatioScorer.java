/*
* RatioScorer is an implementation of the SentimentScorer that scores
* based on ratios
*
*  */

public class RatioScorer extends SentimentScorer {
    @Override
    public double calculateScore(SentimentResult results) {
        // Retrieving classification results
        Classifications classifications = results.getClassifications();

        // Querying Classifications
        int positiveCount = classifications.getFrequency("POSITIVE");
        int negativeCount = classifications.getFrequency("NEGATIVE");
        int totalWords = classifications.getTotalWords();

        // If the majority of the text is positive return a positive
        // confidence value and vice versa
        return positiveCount > negativeCount ? (double) positiveCount / totalWords : (double) -negativeCount / totalWords;
    }

    @Override
    public String classify(double score) {
        double classificationThreshold = 0.05;

        // Return NEGATIVE if the score is negative and POSITIVE if the score is positive
        // however to account for potential errors NEUTRAL is assigned to a minimal
        // classification threshold
        if (score < -classificationThreshold) {
            return "NEGATIVE";
        } else if (score > classificationThreshold) {
            return "POSITIVE";
        } else {
            return "NEUTRAL";
        }
    }
}