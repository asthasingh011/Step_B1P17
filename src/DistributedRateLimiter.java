import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;
public class DistributedRateLimiter {
    private static final int MAX_REQUESTS = 1000;
    private static final int REFILL_RATE = MAX_REQUESTS / 3600;
    private ConcurrentHashMap<String, TokenBucket> clientBuckets;
    public DistributedRateLimiter() {
        clientBuckets = new ConcurrentHashMap<>();
    }
    class TokenBucket {
        private AtomicLong tokens;
        private long lastRefillTime;
        public TokenBucket() {
            this.tokens = new AtomicLong(MAX_REQUESTS);
            this.lastRefillTime = System.currentTimeMillis();
        }
        private synchronized void refill() {
            long now = System.currentTimeMillis();
            long elapsedSeconds = (now - lastRefillTime) / 1000;
            if (elapsedSeconds > 0) {
                long refillAmount = elapsedSeconds * REFILL_RATE;
                long newTokenCount = Math.min(MAX_REQUESTS,
                        tokens.get() + refillAmount);

                tokens.set(newTokenCount);
                lastRefillTime = now;
            }
        }
        public synchronized boolean allowRequest() {
            refill();
            if (tokens.get() > 0) {
                tokens.decrementAndGet();
                return true;
            }
            return false;
        }
        public long getRemainingTokens() {
            refill();
            return tokens.get();
        }
    }
    public String checkRateLimit(String clientId) {
        clientBuckets.putIfAbsent(clientId, new TokenBucket());

        TokenBucket bucket = clientBuckets.get(clientId);

        if (bucket.allowRequest()) {
            return "Allowed (" + bucket.getRemainingTokens() + " remaining)";
        } else {
            return "Denied (Rate limit exceeded. Try later.)";
        }
    }
    public String getRateLimitStatus(String clientId) {

        TokenBucket bucket = clientBuckets.get(clientId);

        if (bucket == null) {
            return "Client not found";
        }

        long remaining = bucket.getRemainingTokens();
        long used = MAX_REQUESTS - remaining;

        return "{used: " + used +
                ", limit: " + MAX_REQUESTS +
                ", remaining: " + remaining + "}";
    }
    public static void main(String[] args) {

        DistributedRateLimiter limiter = new DistributedRateLimiter();

        for (int i = 0; i < 5; i++) {
            System.out.println(
                    limiter.checkRateLimit("abc123"));
        }
        System.out.println(
                limiter.getRateLimitStatus("abc123"));
    }
}