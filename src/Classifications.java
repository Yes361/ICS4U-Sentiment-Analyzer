import java.util.*;

public class Classifications {
    private final HashMap<String, ArrayList<String>> Classifiers = new HashMap<>();
    private final HashMap<String, Double> Intensities = new HashMap<>();
    private final HashMap<String, Integer> Frequency = new HashMap<>();
    private int totalWords = 0;

    public void insert(String classifier, String word, double intensity) {
        totalWords++;

        if (!Classifiers.containsKey(classifier)) {
            Classifiers.put(classifier, new ArrayList<String>());
            Intensities.put(classifier, 0.0);
        }

        Classifiers.get(classifier).add(word);
        Intensities.compute(classifier, (key, level ) -> level + intensity);

        if (!Frequency.containsKey(word)) {
            Frequency.put(word, 1);
        } else {
            Frequency.compute(word, (key, count) -> count + 1);
        }

        if (!Frequency.containsKey(classifier)) {
            Frequency.put(classifier, 1);
        } else {
            Frequency.compute(classifier, (key, count) -> count + 1);
        }
    }

    public ArrayList<String> getClassifier(String classifier) {
        return Classifiers.getOrDefault(classifier, new ArrayList<>());
    }

    public double getIntensity(String classifier) {
        return Intensities.getOrDefault(classifier, 0.0);
    }

    public ArrayList<String> getLabels() {
        return (ArrayList<String>) Classifiers.keySet().stream().toList();
    }

    public int getTotalWords() {
        return totalWords;
    }

    public void clear() {
        Classifiers.clear();
        Frequency.clear();
        totalWords = 0;
    }

    public Integer getFrequency(String word) {
        return Frequency.getOrDefault(word, 0);
    }

    public String toString() {
        return String.format("Classifications{data=%s, frequency=%s, intensities=%s}", Classifiers, Frequency, Intensities);
    }
}
