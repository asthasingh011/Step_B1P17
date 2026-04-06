package week3_4;

class Client {
    String name;
    int risk;
    int balance;

    Client(String name, int risk, int balance) {
        this.name = name;
        this.risk = risk;
        this.balance = balance;
    }
}

public class Problem2 {

    // Bubble Sort (ascending risk)
    static void bubbleSort(Client[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            for (int j = 0; j < arr.length - i - 1; j++) {
                if (arr[j].risk > arr[j + 1].risk) {
                    Client temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                }
            }
        }
    }

    // Insertion Sort (DESC risk + balance)
    static void insertionSort(Client[] arr) {
        for (int i = 1; i < arr.length; i++) {
            Client key = arr[i];
            int j = i - 1;

            while (j >= 0 &&
                    (arr[j].risk < key.risk ||
                     (arr[j].risk == key.risk && arr[j].balance < key.balance))) {
                arr[j + 1] = arr[j];
                j--;
            }

            arr[j + 1] = key;
        }
    }

    public static void main(String[] args) {
        Client[] arr = {
            new Client("C", 80, 2000),
            new Client("A", 20, 1000),
            new Client("B", 50, 500)
        };

        bubbleSort(arr);
        System.out.println("Bubble (ASC):");
        for (Client c : arr) System.out.println(c.name + " " + c.risk);

        insertionSort(arr);
        System.out.println("\nInsertion (DESC):");
        for (Client c : arr) System.out.println(c.name + " " + c.risk);

        System.out.println("\nTop Risk Client: " + arr[0].name);
    }
}