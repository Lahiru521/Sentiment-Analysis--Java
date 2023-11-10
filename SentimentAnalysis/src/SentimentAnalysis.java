import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class SentimentAnalysis {

    private static final String DATASET_PATH = "dataset.txt";

    private Map<String, Integer> wordFrequencies;

    public SentimentAnalysis() {
        wordFrequencies = new HashMap<>();
    }

    /**
     * Trains the sentiment analysis model using the provided dataset.
     * Reads the dataset file, updates the word frequencies based on the labels,
     * and builds the word frequency-based data structure.
     *
     * @param datasetPath The path to the dataset file.
     */
    public void train(String datasetPath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(datasetPath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                //int lineLength = parts.length;
                if (parts.length == 2) {
                    String text = parts[0].toLowerCase();
                    //System.out.println(text);
                    int label = Integer.parseInt(parts[1]);
                    //System.out.println(label);
                    updateWordFrequencies(text, label);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates the word frequencies based on the given text and label.
     *
     * @param text  The text sample.
     * @param label The label indicating positive (1) or negative (-1) sentiment.
     */
    private void updateWordFrequencies(String text, int label) {
        String[] words = text.split(" ");
        for (String word : words) {
            int frequency = wordFrequencies.getOrDefault(word, 0);
            //System.out.println("old"+frequency);
            frequency += (label == 1) ? 1 : -1;
            //System.out.println("new"+ frequency);
            wordFrequencies.put(word, frequency);
            //System.out.println(wordFrequencies);
        }
    }

    /**
     * Classifies the given input text as positive or negative sentiment.
     *
     * @param inputText The input text to classify.
     * @return The sentiment label (Positive or Negative).
     */
    public String classify(String inputText) {
        String[] words = inputText.toLowerCase().split(" ");
        int score = 0;
        for (String word : words) {
            if (wordFrequencies.containsKey(word)) {
                score += wordFrequencies.get(word);
            }
        }
        return (score >= 0) ? "Positive" : "Negative";
    }

    public static void main(String[] args) {
        SentimentAnalysis sa = new SentimentAnalysis();
        sa.train(DATASET_PATH);
        boolean repeat = true;
        Scanner scanner = new Scanner(System.in);
        while (repeat) {
            // Get user input

            System.out.print("Enter a sentence: ");
            String inputText = scanner.nextLine();

            // Classify the user input
            String sentiment = sa.classify(inputText);
            System.out.println("Sentiment: " + sentiment);
            Scanner repeatScanner = new Scanner(System.in);
            System.out.print("Do you want to Repeat(Y/N): ");
            String input =scanner.nextLine().toUpperCase();

            if (input.equals("Y")){
                repeat=true;
            } else {
                repeat =false;
            }
        }
        scanner.close();
    }
}
