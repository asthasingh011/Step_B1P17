import java.util.*;
public class MultiLevelCacheSystem {
    private static final int L1_CAPACITY = 10000;
    private static final int L2_CAPACITY = 100000;
    private LinkedHashMap<String, String> L1;
    private LinkedHashMap<String, String> L2;
    private HashMap<String, String> L3;
    private int L1Hits = 0;
    private int L2Hits = 0;
    private int L3Hits = 0;
    private int totalRequests = 0;
    public MultiLevelCacheSystem() {
        L1 = new LinkedHashMap<String, String>(16, 0.75f, true) {
            protected boolean removeEldestEntry(Map.Entry<String, String> eldest) {
                return size() > L1_CAPACITY;
            }
        };

        L2 = new LinkedHashMap<String, String>(16, 0.75f, true) {
            protected boolean removeEldestEntry(Map.Entry<String, String> eldest) {
                return size() > L2_CAPACITY;
            }
        };
        L3 = new HashMap<>();
        for (int i = 1; i <= 200000; i++) {
            L3.put("video_" + i, "VideoData_" + i);
        }
    }
    public String getVideo(String videoId) {
        totalRequests++;
        if (L1.containsKey(videoId)) {
            L1Hits++;
            simulateDelay(1);
            return "L1 HIT → " + L1.get(videoId);
        }
        if (L2.containsKey(videoId)) {
            L2Hits++;
            simulateDelay(5);
            L1.put(videoId, L2.get(videoId));
            return "L2 HIT → Promoted to L1";
        }
        if (L3.containsKey(videoId)) {
            L3Hits++;
            simulateDelay(150);
            L2.put(videoId, L3.get(videoId));
            return "L3 HIT → Added to L2";
        }

        return "Video Not Found";
    }
    public void invalidate(String videoId) {
        L1.remove(videoId);
        L2.remove(videoId);
        L3.remove(videoId);
        System.out.println("Video invalidated: " + videoId);
    }
    public void getStatistics() {
        System.out.println("\n===== Cache Statistics =====");
        System.out.println("L1 Hit Rate: " +
                percentage(L1Hits) + "%");
        System.out.println("L2 Hit Rate: " +
                percentage(L2Hits) + "%");
        System.out.println("L3 Hit Rate: " +
                percentage(L3Hits) + "%");
        int overallHits = L1Hits + L2Hits;
        System.out.println("Overall Cache Hit Rate: " +
                percentage(overallHits) + "%");

        System.out.println("============================\n");
    }
    private double percentage(int hits) {
        if (totalRequests == 0) return 0;
        return (hits * 100.0) / totalRequests;
    }

    private void simulateDelay(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ignored) {}
    }

    public static void main(String[] args) {
        MultiLevelCacheSystem cache =
                new MultiLevelCacheSystem();
        System.out.println(cache.getVideo("video_123"));
        System.out.println(cache.getVideo("video_123"));
        System.out.println(cache.getVideo("video_999"));
        System.out.println(cache.getVideo("video_999"));

        cache.getStatistics();
    }
}