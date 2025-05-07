public class EntropyScorer extends SentimentScorer {
    @Override
    public double calculateScore(SentimentResult results) {
        double pos = results.getClassifications().getFrequency("POSITIVE");
        double neg = results.getClassifications().getFrequency("NEGATIVE");
        double neu = results.getClassifications().getFrequency("NEUTRAL");
        double total = pos + neg + neu;

        if (total == 0) return 0;

        double[] probabilities = {pos / total, neg / total, neu / total};
        double entropy = 0;

        for (double p : probabilities) {
            if (p > 0) {
                entropy -= p * Math.log(p) / Math.log(2);
            }
        }

        return entropy;
    }

    @Override
    public String classify(double entropy) {
        if (entropy < 0.5) return "POLARIZED";
        if (entropy > 1.5) return "MIXED";
        return "NEUTRAL";
    }
}
