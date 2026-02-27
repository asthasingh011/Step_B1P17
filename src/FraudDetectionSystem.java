import java.util.*;
public class FraudDetectionSystem {
    static class Transaction {
        int id;
        double amount;
        String merchant;
        String account;
        long timestamp;
        public Transaction(int id, double amount, String merchant,
                           String account, long timestamp) {
            this.id = id;
            this.amount = amount;
            this.merchant = merchant;
            this.account = account;
            this.timestamp = timestamp;
        }
    }
    public static List<int[]> findTwoSum(List<Transaction> transactions,
                                         double target) {
        Map<Double, Transaction> map = new HashMap<>();
        List<int[]> result = new ArrayList<>();
        for (Transaction t : transactions) {
            double complement = target - t.amount;

            if (map.containsKey(complement)) {
                result.add(new int[]{
                        map.get(complement).id, t.id});
            }
            map.put(t.amount, t);
        }
        return result;
    }
    public static List<int[]> findTwoSumWithWindow(
            List<Transaction> transactions, double target) {
        List<int[]> result = new ArrayList<>();
        Map<Double, List<Transaction>> map = new HashMap<>();
        long ONE_HOUR = 60 * 60 * 1000;
        for (Transaction t : transactions) {
            double complement = target - t.amount;
            if (map.containsKey(complement)) {
                for (Transaction prev : map.get(complement)) {
                    if (Math.abs(t.timestamp - prev.timestamp)
                            <= ONE_HOUR) {
                        result.add(new int[]{prev.id, t.id});
                    }
                }
            }

            map.putIfAbsent(t.amount, new ArrayList<>());
            map.get(t.amount).add(t);
        }
        return result;
    }
    public static List<List<Integer>> findKSum(
            List<Transaction> transactions,
            int k, double target) {
        List<List<Integer>> result = new ArrayList<>();
        List<Double> amounts = new ArrayList<>();
        for (Transaction t : transactions) {
            amounts.add(t.amount);
        }
        Collections.sort(amounts);
        kSumHelper(amounts, k, target, 0,
                new ArrayList<>(), result);
        return result;
    }
    private static void kSumHelper(List<Double> nums,
                                   int k,
                                   double target,
                                   int start,
                                   List<Integer> path,
                                   List<List<Integer>> result) {
        if (k == 2) {
            Map<Double, Integer> map = new HashMap<>();
            for (int i = start; i < nums.size(); i++) {
                double complement = target - nums.get(i);
                if (map.containsKey(complement)) {
                    List<Integer> temp = new ArrayList<>(path);
                    temp.add(map.get(complement));
                    temp.add(i);
                    result.add(temp);
                }
                map.put(nums.get(i), i);
            }
            return;
        }

        for (int i = start; i < nums.size(); i++) {
            path.add(i);
            kSumHelper(nums, k - 1,
                    target - nums.get(i),
                    i + 1, path, result);
            path.remove(path.size() - 1);
        }
    }
    public static Map<String, List<Transaction>>
    detectDuplicates(List<Transaction> transactions) {

        Map<String, List<Transaction>> map = new HashMap<>();

        for (Transaction t : transactions) {
            String key = t.amount + "-" + t.merchant;

            map.putIfAbsent(key, new ArrayList<>());
            map.get(key).add(t);
        }

        Map<String, List<Transaction>> duplicates =
                new HashMap<>();

        for (String key : map.keySet()) {
            if (map.get(key).size() > 1) {
                duplicates.put(key, map.get(key));
            }
        }

        return duplicates;
    }
    public static void main(String[] args) {

        long now = System.currentTimeMillis();

        List<Transaction> transactions = Arrays.asList(
                new Transaction(1, 500, "Store A", "acc1", now),
                new Transaction(2, 300, "Store B", "acc2", now + 1000),
                new Transaction(3, 200, "Store C", "acc3", now + 2000),
                new Transaction(4, 500, "Store A", "acc4", now + 3000)
        );

        System.out.println("Two-Sum:");
        for (int[] pair :
                findTwoSum(transactions, 500)) {
            System.out.println(pair[0] + " & " + pair[1]);
        }

        System.out.println("\nDuplicates:");
        Map<String, List<Transaction>> dups =
                detectDuplicates(transactions);

        for (String key : dups.keySet()) {
            System.out.println(key +
                    " count=" + dups.get(key).size());
        }
    }
}