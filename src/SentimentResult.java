/*
* SentimentResults provides a way of interacting and querying the results
* of a sentiment analysis conducted in SentimenyAnalyzer
*  */

import java.io.File;
import java.util.HashMap;

public class SentimentResult {
    ///  Instance variables

    // Basic data about the results
    private final File file;
    private double score;
    private String sentiment;

    // Classifications contains the heart of the data and
    // the end user should have access to it
    private final Classifications classifications;

    // Custom fields that may be set by the scorer or used in future development
    private final HashMap<String, Object> CustomFields = new HashMap<>();

    public SentimentResult(File file, Classifications classifications) {
        this.file = file;
        this.classifications = classifications;
    }

    /// Getter and Setter Methods

    public HashMap<String, Object> getCustomFields() {
        return CustomFields;
    }

    public String getFileName() {
        return this.file.getName();
    }

    public double getScore() {
        return this.score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public String getSentiment() {
        return this.sentiment;
    }

    public void setSentiment(String sentiment) {
        this.sentiment = sentiment;
    }

    public Classifications getClassifications() {
        return classifications;
    }

    public String toString() {
        return String.format(
            "SentimentResults{filename=%s, score=%.2f, sentiment=%s, classes=%s, customFields=%s}",
            file.getName(),
            score,
            sentiment,
            classifications,
            CustomFields
        );
    }

    /**
     * ScoreResults accepts a SentimentScorer and using its methods to score the data
     *
     * @param Scorer the Scorer used to classify the results
     */
    public void ScoreResults(SentimentScorer Scorer) {
        this.score = Scorer.calculateScore(this);
        this.sentiment = Scorer.classify(this.score);

        // This is added to the custom fields which is included in the summary
        CustomFields.put("Scorer", Scorer.getName());
    }

    /**
     * GetSummary returns a summary of the report
     *
     * @return String representing the summary report
     */
    public String getSummary() {
        StringBuilder Report = new StringBuilder();

        // Add all the instance variables
        Report.append(String.format("""
        ====SUMMARY===
        File: %s
        Total Words: %d
        Score: %.2f
        Sentiment: %s
        """,
            file.getName(),
            classifications.getTotalWords(),
            score,
            sentiment
        ));

        // Add the Custom Fields key and value pair to the report
        for (String FieldName : CustomFields.keySet()) {
            Report
                .append(FieldName)
                .append(": ")
                .append(CustomFields.get(FieldName))
                .append("\n");
        }

        // Append the Words belonging to each classifications to the summary report
        for (String classifier : classifications.getClassifiers()) {
            Report
                .append(classifier)
                .append("\n")
                .append(classifications.getClassifier(classifier))
                .append("\n\n");
        }
        
        return Report.toString();
    }
}
