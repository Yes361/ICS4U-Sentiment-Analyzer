/*
* SentimentScorer is the base class for defining other Scorer's behaviors
* A potential design consideration was a classifyMultipleSentiments which would've returned
* Multiple sentiments
*  */

public abstract class SentimentScorer {
    public abstract double calculateScore(SentimentResult results);
    public abstract String classify(double score);
//    public abstract String[] classifyMultipleSentiments(double score);

    /**
     * getName() returns a simplistic identifier for the scorer
     *
     * @return A string representing the name of the Scorer
     */
    public String getName() {
        return this.getClass().getName();
    }

    public String toString() {
        return this.getName();
    }
}
