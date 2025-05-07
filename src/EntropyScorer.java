/*
* The entropy scorer scores text based on
* the principles of entropy (more commonly known as the measure of
* uncertainty and disorder)
* It captures and classifies the distribution of sentiment (POSITIVE, NEGATIVE) in text
*  */

public class EntropyScorer extends SentimentScorer {
    @Override
    public double calculateScore(SentimentResult results) {
        Classifications classifications = results.getClassifications();

        // Querying the results
        double positive = classifications.getFrequency("POSITIVE");
        double negative = classifications.getFrequency("NEGATIVE");
        double neutral = classifications.getFrequency("NEUTRAL");

        double total = positive + negative + neutral;

        // avoid division by zero
        // return 0 if no sentiment or
        // emotionally charged words are found
        if (positive + negative == 0) return 0;

        double[] probabilities = {
            positive / total,
            negative / total,
            neutral / total
        };

        double entropy = 0;

        // This applies the entropy formula
        // H = -sum(p * log2(p));
        for (double p : probabilities) {
            // p > 0 check to prevent a mathematical error
            // from log(0)
            if (p > 0) {
                entropy -= p * Math.log(p) / Math.log(2);
            }
        }

        return entropy;
    }

    @Override
    public String classify(double entropy) {
        // interpret the entropy score into a classification
        // low entropy indicates strong polarity (mostly positive or negative)
        // which is equivalent to a stronger or more distinct
        // distribution of the sentiment in the text
        // high entropy indicates mixed sentiment
        // which is equivalent to a weaker and less distinct distribution of
        // sentiment in the text
        if (entropy < 0.5) return "POLARIZED";
        if (entropy > 1.5) return "MIXED";
        return "NEUTRAL";
    }
}
