import java.util.*;
import java.util.concurrent.*;
public class RealTimeAnalytics {
    private ConcurrentHashMap<String, Integer> pageViews;
    private ConcurrentHashMap<String, Set<String>> uniqueVisitors;
    private ConcurrentHashMap<String, Integer> trafficSources;

    public RealTimeAnalytics() {
        pageViews = new ConcurrentHashMap<>();
        uniqueVisitors = new ConcurrentHashMap<>();
        trafficSources = new ConcurrentHashMap<>();

        startDashboardUpdater();
    }
    public void processEvent(String url, String userId, String source) {
        pageViews.merge(url, 1, Integer::sum);
        uniqueVisitors.putIfAbsent(url, ConcurrentHashMap.newKeySet());
        uniqueVisitors.get(url).add(userId);
        trafficSources.merge(source, 1, Integer::sum);
    }
    private List<Map.Entry<String, Integer>> getTopPages() {

        PriorityQueue<Map.Entry<String, Integer>> minHeap =
                new PriorityQueue<>(Map.Entry.comparingByValue());

        for (Map.Entry<String, Integer> entry : pageViews.entrySet()) {
            minHeap.offer(entry);
            if (minHeap.size() > 10) {
                minHeap.poll();
            }
        }
        List<Map.Entry<String, Integer>> result = new ArrayList<>(minHeap);
        result.sort((a, b) -> b.getValue() - a.getValue());
        return result;
    }
    public void getDashboard() {

        System.out.println("\n===== REAL-TIME DASHBOARD =====");

        System.out.println("\nTop Pages:");
        List<Map.Entry<String, Integer>> topPages = getTopPages();

        for (int i = 0; i < topPages.size(); i++) {
            String url = topPages.get(i).getKey();
            int views = topPages.get(i).getValue();
            int unique = uniqueVisitors.get(url).size();

            System.out.println((i + 1) + ". " + url +
                    " - " + views + " views (" + unique + " unique)");
        }

        System.out.println("\nTraffic Sources:");
        int total = trafficSources.values().stream().mapToInt(i -> i).sum();
        for (String source : trafficSources.keySet()) {
            int count = trafficSources.get(source);
            double percentage = (count * 100.0) / total;
            System.out.printf("%s: %.1f%%\n", source, percentage);
        }
        System.out.println("===============================\n");
    }
    private void startDashboardUpdater() {
        ScheduledExecutorService scheduler =
                Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(() -> {
            getDashboard();
        }, 5, 5, TimeUnit.SECONDS);
    }
    public static void main(String[] args) throws InterruptedException {
        RealTimeAnalytics analytics = new RealTimeAnalytics();
        analytics.processEvent("/article/breaking-news", "user_123", "google");
        analytics.processEvent("/article/breaking-news", "user_456", "facebook");
        analytics.processEvent("/sports/championship", "user_789", "google");
        analytics.processEvent("/sports/championship", "user_123", "direct");
        analytics.processEvent("/article/breaking-news", "user_123", "google");
        Thread.sleep(15000);
    }
}