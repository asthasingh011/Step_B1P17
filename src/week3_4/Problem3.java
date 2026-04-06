package week3_4;

import java.util.*;

public class Problem3 {

    // MERGE SORT
    static void mergeSort(int[] arr, int l, int r) {
        if (l >= r) return;
        int mid = (l + r) / 2;
        mergeSort(arr, l, mid);
        mergeSort(arr, mid + 1, r);
        merge(arr, l, mid, r);
    }

    static void merge(int[] arr, int l, int m, int r) {
        int[] temp = new int[r - l + 1];
        int i = l, j = m + 1, k = 0;

        while (i <= m && j <= r)
            temp[k++] = (arr[i] <= arr[j]) ? arr[i++] : arr[j++];

        while (i <= m) temp[k++] = arr[i++];
        while (j <= r) temp[k++] = arr[j++];

        System.arraycopy(temp, 0, arr, l, temp.length);
    }

    // QUICK SORT (DESC)
    static void quickSort(int[] arr, int low, int high) {
        if (low < high) {
            int pi = partition(arr, low, high);
            quickSort(arr, low, pi - 1);
            quickSort(arr, pi + 1, high);
        }
    }

    static int partition(int[] arr, int low, int high) {
        int pivot = arr[high];
        int i = low - 1;

        for (int j = low; j < high; j++) {
            if (arr[j] > pivot) { // DESC
                i++;
                int temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
            }
        }

        int temp = arr[i + 1];
        arr[i + 1] = arr[high];
        arr[high] = temp;

        return i + 1;
    }

    public static void main(String[] args) {
        int[] arr = {500, 100, 300};

        mergeSort(arr, 0, arr.length - 1);
        System.out.println("Merge Sort: " + Arrays.toString(arr));

        quickSort(arr, 0, arr.length - 1);
        System.out.println("Quick Sort DESC: " + Arrays.toString(arr));

        int sum = Arrays.stream(arr).sum();
        System.out.println("Total Volume: " + sum);
    }
}