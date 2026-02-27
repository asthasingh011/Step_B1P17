import java.util.*;
public class DNSCache {
    private final int MAX_SIZE;
    private LinkedHashMap<String, DNSEntry> cache;
    private int hits = 0;
    private int misses = 0;
    public DNSCache(int maxSize) {
        this.MAX_SIZE = maxSize;
        cache = new LinkedHashMap<String, DNSEntry>(16, 0.75f, true) {
            protected boolean removeEldestEntry(Map.Entry<String, DNSEntry> eldest) {
                return size() > MAX_SIZE;
            }
        };
        startCleanupThread();
    }
    public synchronized String resolve(String domain) {
        long startTime = System.nanoTime();
        DNSEntry entry = cache.get(domain);
        if (entry != null) {
            if (!entry.isExpired()) {
                hits++;
                System.out.println("Cache HIT");
                return entry.ipAddress;
            } else {
                cache.remove(domain);
                System.out.println("Cache EXPIRED");
            }
        }
        misses++;
        System.out.println("Cache MISS");
        String ip = queryUpstreamDNS(domain);
        cache.put(domain, new DNSEntry(domain, ip, 10));
        long endTime = System.nanoTime();
        System.out.println("Lookup Time: " + (endTime - startTime) / 1_000_000.0 + " ms");
        return ip;
    }
    private String queryUpstreamDNS(String domain) {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "172.217.14." + new Random().nextInt(255);
    }
    private void startCleanupThread() {
        Thread cleaner = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(5000);
                    synchronized (this) {
                        Iterator<Map.Entry<String, DNSEntry>> it = cache.entrySet().iterator();
                        while (it.hasNext()) {
                            if (it.next().getValue().isExpired()) {
                                it.remove();
                            }
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        cleaner.setDaemon(true);
        cleaner.start();
    }
    public void getCacheStats() {
        int total = hits + misses;
        double hitRate = total == 0 ? 0 : (hits * 100.0) / total;

        System.out.println("Cache Hits: " + hits);
        System.out.println("Cache Misses: " + misses);
        System.out.println("Hit Rate: " + hitRate + "%");
    }
    public static void main(String[] args) throws InterruptedException {

        DNSCache dns = new DNSCache(3);

        System.out.println(dns.resolve("google.com"));
        System.out.println(dns.resolve("google.com"));

        Thread.sleep(11000);

        System.out.println(dns.resolve("google.com"));

        dns.getCacheStats();
    }
}