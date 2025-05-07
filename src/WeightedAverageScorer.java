public class WeightedAverageScorer extends SentimentScorer {
    @Override
    public double calculateScore(SentimentResult result) {
        double totalWeight = 0;
        double weightedSum = 0;

        for (String Classifier : result.getClassifications().getClassifiers()) {
            double intensity = result.getClassifications().getIntensity(Classifier);
            double weight = 1.0;
            weightedSum += intensity * weight;
            totalWeight += weight;
        }

        return totalWeight == 0 ? 0 : weightedSum / totalWeight;
    }

    @Override
    public String classify(double score) {
        if (score > 0.5) return "POSITIVE";
        if (score < -0.5) return "NEGATIVE";
        return "NEUTRAL";
    }
}
