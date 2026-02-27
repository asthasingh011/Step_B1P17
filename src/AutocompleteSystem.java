import java.util.*;
public class AutocompleteSystem {
    class TrieNode {
        Map<Character, TrieNode> children = new HashMap<>();
        PriorityQueue<String> topQueries =
                new PriorityQueue<>((a, b) ->
                        frequencyMap.get(a) - frequencyMap.get(b));
    }
    private TrieNode root;
    private Map<String, Integer> frequencyMap;
    private static final int TOP_K = 10;
    public AutocompleteSystem() {
        root = new TrieNode();
        frequencyMap = new HashMap<>();
    }
    public void updateFrequency(String query) {
        frequencyMap.put(query,
                frequencyMap.getOrDefault(query, 0) + 1);
        insertIntoTrie(query);
    }
    private void insertIntoTrie(String query) {
        TrieNode node = root;
        for (char ch : query.toCharArray()) {
            node.children.putIfAbsent(ch, new TrieNode());
            node = node.children.get(ch);
            node.topQueries.offer(query);
            if (node.topQueries.size() > TOP_K) {
                node.topQueries.poll();
            }
        }
    }
    public List<String> search(String prefix) {

        TrieNode node = root;

        for (char ch : prefix.toCharArray()) {
            if (!node.children.containsKey(ch)) {
                return Collections.emptyList();
            }
            node = node.children.get(ch);
        }

        List<String> result = new ArrayList<>(node.topQueries);
        result.sort((a, b) ->
                frequencyMap.get(b) - frequencyMap.get(a));

        return result;
    }
    public List<String> suggestWithTypo(String prefix) {

        List<String> result = search(prefix);

        if (!result.isEmpty()) return result;
        for (int i = 0; i < prefix.length(); i++) {
            String modified =
                    prefix.substring(0, i) + prefix.substring(i + 1);

            result = search(modified);
            if (!result.isEmpty()) return result;
        }

        return Collections.emptyList();
    }
    public static void main(String[] args)
        AutocompleteSystem system = new AutocompleteSystem();
        system.updateFrequency("java tutorial");
        system.updateFrequency("javascript");
        system.updateFrequency("java download");
        system.updateFrequency("java tutorial");
        system.updateFrequency("java 21 features");
        system.updateFrequency("java 21 features");
        system.updateFrequency("java 21 features");

        System.out.println(system.search("jav"));
        System.out.println(system.suggestWithTypo("jaav"));
    }
}