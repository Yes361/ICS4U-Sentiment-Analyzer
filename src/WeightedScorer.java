/*
* This is similar to RatioScorer but instead adds the `intensities` of the
* classifiers
*  */

public class WeightedScorer extends SentimentScorer {

    @Override
    public double calculateScore(SentimentResult results) {
        Classifications classifications = results.getClassifications();

        double positiveIntensity = classifications.getIntensity("POSITIVE");
        double negativeIntensity = classifications.getIntensity("NEGATIVE");
        int totalWords = classifications.getTotalWords();

        return (positiveIntensity + negativeIntensity) / totalWords;
    }

    @Override
    public String classify(double score) {
        double classificationThreshold = 0.05;
        if (score < -classificationThreshold) {
            return "NEGATIVE";
        } else if (score > classificationThreshold) {
            return "POSITIVE";
        } else {
            return "NEUTRAL";
        }
    }
}
