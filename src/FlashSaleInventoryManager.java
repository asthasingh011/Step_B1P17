import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
public class FlashSaleInventoryManager {
    private ConcurrentHashMap<String, AtomicInteger> inventory;
    private ConcurrentHashMap<String, ConcurrentLinkedQueue<Integer>> waitingList;
    public FlashSaleInventoryManager() {
        inventory = new ConcurrentHashMap<>();
        waitingList = new ConcurrentHashMap<>();
    }
    public void addProduct(String productId, int stock) {
        inventory.put(productId, new AtomicInteger(stock));
        waitingList.put(productId, new ConcurrentLinkedQueue<>());
    }
    public int checkStock(String productId) {
        AtomicInteger stock = inventory.get(productId);
        return stock != null ? stock.get() : 0;
    }
    public String purchaseItem(String productId, int userId) {
        AtomicInteger stock = inventory.get(productId);
        if (stock == null) {
            return "Product not found";
        }
        while (true) {
            int currentStock = stock.get();
            if (currentStock <= 0) {
                ConcurrentLinkedQueue<Integer> queue = waitingList.get(productId);
                queue.add(userId);
                return "Out of stock. Added to waiting list. Position #" + queue.size();
            }
            if (stock.compareAndSet(currentStock, currentStock - 1)) {
                return "Success! Remaining stock: " + (currentStock - 1);
            }
        }
    }
    public int getWaitingPosition(String productId, int userId) {
        ConcurrentLinkedQueue<Integer> queue = waitingList.get(productId);
        return queue != null ? queue.size() : -1;
    }
    public static void main(String[] args) {
        FlashSaleInventoryManager manager = new FlashSaleInventoryManager();
        manager.addProduct("IPHONE15_256GB", 3);
        System.out.println("Stock: " + manager.checkStock("IPHONE15_256GB"));
        System.out.println(manager.purchaseItem("IPHONE15_256GB", 101));
        System.out.println(manager.purchaseItem("IPHONE15_256GB", 102));
        System.out.println(manager.purchaseItem("IPHONE15_256GB", 103));
        System.out.println(manager.purchaseItem("IPHONE15_256GB", 104));
        System.out.println(manager.purchaseItem("IPHONE15_256GB", 105));
    }
}