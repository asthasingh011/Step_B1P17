package week3_4;

public class Problem6 {

    static int floor(int[] arr, int target) {
        int low = 0, high = arr.length - 1, res = -1;

        while (low <= high) {
            int mid = (low + high) / 2;

            if (arr[mid] <= target) {
                res = arr[mid];
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return res;
    }

    static int ceil(int[] arr, int target) {
        int low = 0, high = arr.length - 1, res = -1;

        while (low <= high) {
            int mid = (low + high) / 2;

            if (arr[mid] >= target) {
                res = arr[mid];
                high = mid - 1;
            } else {
                low = mid + 1;
            }
        }
        return res;
    }

    public static void main(String[] args) {
        int[] arr = {10, 25, 50, 100};

        System.out.println("Floor: " + floor(arr, 30));
        System.out.println("Ceil: " + ceil(arr, 30));
    }
}