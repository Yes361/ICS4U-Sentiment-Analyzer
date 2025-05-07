/*
* SentimentDictionary aggregates words with their assigned sentiment and intensities
* as opposed to having HashMaps strewn about the codebase
*  */

import java.util.HashMap;

public class SentimentDictionary {
    private final HashMap<String, String> Sentiments = new HashMap<>();
    private final HashMap<String, Double> Intensities = new HashMap<>();

    public void insert(String word, String sentiment, double intensity) {
        Sentiments.put(word, sentiment);
        Intensities.put(word, intensity);
    }

    public boolean ContainsWord(String word) {
        return Sentiments.containsKey(word);
    }

    public String getSentiment(String word) {
        return Sentiments.get(word);
    };

    public double getIntensity(String word) {
        return Intensities.getOrDefault(word, 0.0);
    }

    public boolean isEmpty() {
        return Sentiments.isEmpty();
    }

    public void clear() {
        Sentiments.clear();
        Intensities.clear();
    }

}
