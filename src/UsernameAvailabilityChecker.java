import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
public class UsernameAvailabilityChecker {
    private ConcurrentHashMap<String, Integer> usersMap;
    private ConcurrentHashMap<String, Integer> attemptCount;
    private String mostAttemptedUser;
    private int maxAttempts;
    public UsernameAvailabilityChecker() {
        usersMap = new ConcurrentHashMap<>();
        attemptCount = new ConcurrentHashMap<>();
        mostAttemptedUser = "";
        maxAttempts = 0;
    }
    public boolean checkAvailability(String username) {
        int count = attemptCount.getOrDefault(username, 0) + 1;
        attemptCount.put(username, count);
        if (count > maxAttempts) {
            maxAttempts = count;
            mostAttemptedUser = username;
        }
        return !usersMap.containsKey(username);
    }
    public void registerUser(String username, int userId) {
        usersMap.put(username, userId);
    }
    public List<String> suggestAlternatives(String username) {
        List<String> suggestions = new ArrayList<>();

        if (!usersMap.containsKey(username)) {
            suggestions.add(username);
            return suggestions;
        }
        for (int i = 1; i <= 5; i++) {
            String newUsername = username + i;
            if (!usersMap.containsKey(newUsername)) {
                suggestions.add(newUsername);
            }
        }

        String modified = username.replace("_", ".");
        if (!usersMap.containsKey(modified)) {
            suggestions.add(modified);
        }

        return suggestions;
    }

    public String getMostAttempted() {
        return mostAttemptedUser;
    }
    public static void main(String[] args) {

        UsernameAvailabilityChecker system = new UsernameAvailabilityChecker();

        system.registerUser("john_doe", 101);
        system.registerUser("admin", 1);
        System.out.println("Is john_doe available? " +
                system.checkAvailability("john_doe"));

        System.out.println("Is jane_smith available? " +
                system.checkAvailability("jane_smith"));
        System.out.println("Suggestions for john_doe: " +
                system.suggestAlternatives("john_doe"));
        system.checkAvailability("admin");
        system.checkAvailability("admin");
        system.checkAvailability("admin");
        System.out.println("Most Attempted Username: " +
                system.getMostAttempted());
    }
}