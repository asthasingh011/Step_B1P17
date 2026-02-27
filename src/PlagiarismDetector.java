import java.util.*;
public class PlagiarismDetector {
    private HashMap<String, Set<String>> nGramIndex;
    private HashMap<String, List<String>> documentStore;
    private int N = 5;

    public PlagiarismDetector() {
        nGramIndex = new HashMap<>();
        documentStore = new HashMap<>();
    }

    public void addDocument(String docId, String content) {
        List<String> words = preprocess(content);
        documentStore.put(docId, words);

        List<String> ngrams = generateNGrams(words);

        for (String ngram : ngrams) {
            nGramIndex.putIfAbsent(ngram, new HashSet<>());
            nGramIndex.get(ngram).add(docId);
        }

        System.out.println("Indexed document: " + docId);
        System.out.println("Extracted " + ngrams.size() + " n-grams\n");
    }
    public void analyzeDocument(String docId, String content) {

        List<String> words = preprocess(content);
        List<String> ngrams = generateNGrams(words);

        HashMap<String, Integer> matchCount = new HashMap<>();

        for (String ngram : ngrams) {
            if (nGramIndex.containsKey(ngram)) {
                for (String matchedDoc : nGramIndex.get(ngram)) {
                    matchCount.put(matchedDoc,
                            matchCount.getOrDefault(matchedDoc, 0) + 1);
                }
            }
        }

        System.out.println("Analyzing: " + docId);
        System.out.println("Extracted " + ngrams.size() + " n-grams");

        for (String matchedDoc : matchCount.keySet()) {
            int matches = matchCount.get(matchedDoc);
            double similarity = (matches * 100.0) / ngrams.size();

            System.out.println("Matched with: " + matchedDoc);
            System.out.println("Matching n-grams: " + matches);
            System.out.printf("Similarity: %.2f%%\n", similarity);

            if (similarity > 60) {
                System.out.println("⚠ PLAGIARISM DETECTED\n");
            } else if (similarity > 15) {
                System.out.println("Suspicious similarity\n");
            } else {
                System.out.println("Low similarity\n");
            }
        }
    }
    private List<String> preprocess(String content) {
        content = content.toLowerCase().replaceAll("[^a-z ]", "");
        return Arrays.asList(content.split("\\s+"));
    }
    private List<String> generateNGrams(List<String> words) {
        List<String> ngrams = new ArrayList<>();

        for (int i = 0; i <= words.size() - N; i++) {
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < N; j++) {
                sb.append(words.get(i + j)).append(" ");
            }
            ngrams.add(sb.toString().trim());
        }

        return ngrams;
    }
    public static void main(String[] args) {
        PlagiarismDetector detector = new PlagiarismDetector();
        detector.addDocument("essay_089.txt",
                "Artificial intelligence is transforming the world through automation and innovation");

        detector.addDocument("essay_092.txt",
                "Artificial intelligence is transforming the world through automation and innovation rapidly across industries");

        detector.analyzeDocument("essay_123.txt",
                "Artificial intelligence is transforming the world through automation and innovation across industries");
    }
}