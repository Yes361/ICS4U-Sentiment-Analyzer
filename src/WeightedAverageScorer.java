public class WeightedAverageScorer extends SentimentScorer {
    @Override
    public double calculateScore(SentimentResult result) {
        double weightedSum = 0;

        // Retrieving data from the results
        Classifications classifications = result.getClassifications();
        String[] Classifiers = classifications.getClassifiers();

        // Average the intensities of all the classifiers
        for (String Classifier : Classifiers) {
            double intensity = classifications.getIntensity(Classifier);

            // Weighted calculation of the intensity of the classifier multiplied
            // by its frequency of occurrence
            weightedSum += intensity * classifications.getFrequency(Classifier) / classifications.getWords().size();
        }

        return weightedSum / Classifiers.length;
    }

    @Override
    public String classify(double score) {
        double classificationThreshold = 0.1;
        if (score > classificationThreshold) return "POSITIVE";
        if (score < -classificationThreshold) return "NEGATIVE";
        return "NEUTRAL";
    }
}
