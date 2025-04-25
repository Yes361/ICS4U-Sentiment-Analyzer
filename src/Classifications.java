import java.util.*;

public class Classifications {
    private final HashMap<String, ArrayList<String>> data = new HashMap<>();
    private final HashMap<String, Integer> frequency = new HashMap<>();

    public void insert(String classifier, String word) {
        if (!data.containsKey(classifier)) {
            data.put(classifier, new ArrayList<String>());
        } else {
            data.get(classifier).add(word);
        }

        if (!frequency.containsKey(word)) {
            frequency.put(word, 1);
        } else {
            frequency.compute(word, (k, count) -> count + 1);
        }

        if (!frequency.containsKey(classifier)) {
            frequency.put(classifier, 1);
        } else {
            frequency.compute(classifier, (k, count) -> count + 1);
        }
    }

    public ArrayList<String> getClassifier(String classifier) {
        return data.get(classifier);
    }

    public void clear() {
        data.clear();
        frequency.clear();
    }

    public Integer getFrequency(String classifier) {
        return frequency.getOrDefault(classifier, 0);
    }

    public String toString() {
        return String.format("Classifications{data=%s, frequency=%s}", data, frequency);
    }
}
