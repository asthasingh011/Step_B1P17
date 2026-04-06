package week3_4;

import java.util.*;

/*
 * Problem 1: Transaction Fee Sorting
 * Bubble Sort + Insertion Sort + Filtering
 */

class Transaction {
    String id;
    double fee;
    String timestamp;

    Transaction(String id, double fee, String timestamp) {
        this.id = id;
        this.fee = fee;
        this.timestamp = timestamp;
    }
}

public class Problem1 {

    // Bubble Sort (by fee)
    static void bubbleSort(List<Transaction> list) {
        for (int i = 0; i < list.size() - 1; i++) {
            boolean swapped = false;

            for (int j = 0; j < list.size() - i - 1; j++) {
                if (list.get(j).fee > list.get(j + 1).fee) {
                    Collections.swap(list, j, j + 1);
                    swapped = true;
                }
            }

            if (!swapped) break;
        }
    }

    // Insertion Sort (fee + timestamp)
    static void insertionSort(List<Transaction> list) {
        for (int i = 1; i < list.size(); i++) {
            Transaction key = list.get(i);
            int j = i - 1;

            while (j >= 0 &&
                    (list.get(j).fee > key.fee ||
                     (list.get(j).fee == key.fee &&
                      list.get(j).timestamp.compareTo(key.timestamp) > 0))) {

                list.set(j + 1, list.get(j));
                j--;
            }

            list.set(j + 1, key);
        }
    }

    public static void main(String[] args) {
        List<Transaction> list = new ArrayList<>();

        list.add(new Transaction("T1", 10.5, "10:00"));
        list.add(new Transaction("T2", 25.0, "09:30"));
        list.add(new Transaction("T3", 5.0, "10:15"));

        bubbleSort(list);
        System.out.println("Bubble Sorted:");
        list.forEach(t -> System.out.println(t.id + " -> " + t.fee));

        insertionSort(list);
        System.out.println("\nInsertion Sorted:");
        list.forEach(t -> System.out.println(t.id + " -> " + t.fee));

        System.out.println("\nHigh Fee (>50):");
        list.stream().filter(t -> t.fee > 50).forEach(t -> System.out.println(t.id));
    }
}