/*
* Classifications stores the processed words from a file into given sentiment categories
* referred to as classifiers. Each Classifier and word has an assigned frequency and summed overall intensity
*
* Reflections:
* Some potential changes to the class design and implementation was creating a SentimentLexicon class
* which would store various bits of information about a word such as its intensity, its original line position
* and row position in the original text. This would allow for a singular HashMap as opposed to breaking up the
* implementation into the HashMaps Classifiers and Intensity, an example of this implementation may look like
*
* public class SentimentLexicon {
*   private String word;
*   private int rowPosition;
*   private int lineColPosition;
*
*   public SentimentLexicon(String word, int rowPosition, int lineColPosition) {
*       this.word = word;
*       this.rowPosition = rowPosition;
*       this.lineColPosition = lineColPosition;
*   }
* }
*
* Sadly this was not implemented as to not stray too far away from the initial class implementations
* as outlined in the project instructions and UML Diagrams
*
*  */

import java.util.*;

public class Classifications {
    /// Instance Variables

    // Classifiers is a HashMap that contains a String value for the key to the pair value of the list of
    // words related to that classifier, i.e.("POSITIVE", ["happy", "cool", "awesome"]) might be a possible
    // key value pair
    private final HashMap<String, ArrayList<String>> Classifiers = new HashMap<>();

    // Similarly Intensities is a HashMap that correlates a classifier to the overall intensity of
    // classifier in the text, i.e. "POSITIVE" might have an intensity of 5.0 and "NEGATIVE" might have an
    // intensity of 10.0 indicating the text was more negative
    private final HashMap<String, Double> Intensities = new HashMap<>();

    // Frequency is a HashMap correlating Classifiers and Words to how frequent they are in the text
    // For example there may be 5 "POSITIVE"-ly classified Words and 2 instances of the word "Happy"
    private final HashMap<String, Integer> Frequency = new HashMap<>();

    // A simple ArrayList of all words added to keep track internally
    private final ArrayList<String> Words = new ArrayList<>();

    /**
     * Inserts a word into Classifications with its respective Classifier and Intensity value
     *
     * @param classifier The classifier or sentiment category the word belongs to
     * @param word The String value of the word
     * @param intensity The intensity/confidence of the word in relation to that sentiment
     */
    public void insert(String classifier, String word, double intensity) {
        // Add the Word
        Words.add(word);

        // If the Classifier doesn't exist add it to `Classifiers` and `Intensities`
        // Otherwise add them to the ArrayList for Classifiers and increase the intensity
        // level for that classification
        if (!Classifiers.containsKey(classifier)) {
            // Create a new ArrayList with the word
            ArrayList<String> ClassifierList = new ArrayList<>();
            ClassifierList.add(word);

            // Put the word and its intensity in their respective HashMaps
            Classifiers.put(classifier, ClassifierList);
            Intensities.put(classifier, intensity);
        } else {
            Classifiers.get(classifier).add(word);
            Intensities.compute(classifier, (key, level) -> level + intensity);
        }

        // If the word doesn't exist add it to the Frequencies HashMap or
        // update its current intensity value
        if (!Frequency.containsKey(word)) {
            Frequency.put(word, 1);
        } else {
            Frequency.compute(word, (key, count) -> count + 1);
        }

        // Same process but for Classifiers
        if (!Frequency.containsKey(classifier)) {
            Frequency.put(classifier, 1);
        } else {
            Frequency.compute(classifier, (key, count) -> count + 1);
        }
    }

    /**
     * Gets the list of words belonging to this classifier
     *
     * @param classifier the label or name of the classifier, case sensitivity does matter
     *
     * @return The list of words belonging to that category
     */
    public ArrayList<String> getClassifier(String classifier) {
        // Get the list of words belonging ot this classifier
        // or if it doesn't exist return an empty list
        return Classifiers.getOrDefault(classifier, new ArrayList<>());
    }

    /**
     * Gets the intensity of the classifier/category
     *
     * @param classifier the label or name of the classifier, case sensitivity does matter
     *
     * @return A double value representing the value of intensity
     */
    public double getIntensity(String classifier) {
        // Get the classifier or if it doesn't exist return 0.0
        return Intensities.getOrDefault(classifier, 0.0);
    }

    /**
     * Returns a list of all the classifiers currently stored in this Classification
     *
     * @return A list of the labels/names of the classifiers
     */
    public String[] getClassifiers() {
        return Classifiers.keySet().toArray(new String[0]);
    }

    public int getTotalWords() {
        return Words.size();
    }

    public ArrayList<String> getWords() {
        return Words;
    }

    public Integer getFrequency(String word) {
        return Frequency.getOrDefault(word, 0);
    }

    // Clears this Classifications
    public void clear() {
        Classifiers.clear();
        Frequency.clear();
        Intensities.clear();
        Words.clear();
    }

    public String toString() {
        return String.format(
            "Classifications{data=%s, frequency=%s, intensities=%s}",
            Classifiers,
            Frequency,
            Intensities
        );
    }
}
