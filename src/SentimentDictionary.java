/*
* SentimentDictionary aggregates words with their assigned sentiment and intensities
* as opposed to having HashMaps strewn about the codebase
*  */

import java.util.HashMap;

public class SentimentDictionary {
    private final HashMap<String, String> Sentiments = new HashMap<>();
    private final HashMap<String, Double> Intensities = new HashMap<>();

    /**
     * Inserts the word into the dictionary alongside its sentiment and intensity
     */
    public void insert(String word, String sentiment, double intensity) {
        Sentiments.put(word, sentiment);
        Intensities.put(word, intensity);
    }

    /**
     * ContainsWords determines whether a word is in this
     * sentiment dictionary
     *
     * @param word Word to check against
     *
     * @return boolean value representing whether the value exists in
     * the dictionary
     */
    public boolean ContainsWord(String word) {
        return Sentiments.containsKey(word);
    }

    /// Getters and Setters

    public String getSentiment(String word) {
        return Sentiments.get(word);
    };

    public double getIntensity(String word) {
        return Intensities.getOrDefault(word, 0.0);
    }

    /**
     * Determines if the dictionary is empty
     *
     * @return boolean value for whether the dictionary is empty
     */
    public boolean isEmpty() {
        return Sentiments.isEmpty();
    }

    // Clears the dictionary
    public void clear() {
        Sentiments.clear();
        Intensities.clear();
    }

}
