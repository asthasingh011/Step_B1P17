import java.util.*;
public class ParkingLot {
    enum Status {
        EMPTY,
        OCCUPIED,
        DELETED
    }
    class ParkingSpot {
        String licensePlate;
        long entryTime;
        Status status;

        ParkingSpot() {
            status = Status.EMPTY;
        }
    }
    private ParkingSpot[] table;
    private int capacity;
    private int size;
    private int totalProbes;
    public ParkingLot(int capacity) {
        this.capacity = capacity;
        this.table = new ParkingSpot[capacity];
        for (int i = 0; i < capacity; i++) {
            table[i] = new ParkingSpot();
        }
        size = 0;
        totalProbes = 0;
    }
    private int hash(String licensePlate) {
        return Math.abs(licensePlate.hashCode()) % capacity;
    }
    public void parkVehicle(String licensePlate) {
        int index = hash(licensePlate);
        int probes = 0;
        while (table[index].status == Status.OCCUPIED) {
            index = (index + 1) % capacity;
            probes++;
        }
        table[index].licensePlate = licensePlate;
        table[index].entryTime = System.currentTimeMillis();
        table[index].status = Status.OCCUPIED;
        size++;
        totalProbes += probes;

        System.out.println("Vehicle " + licensePlate +
                " assigned spot #" + index +
                " (" + probes + " probes)");
    }
    public void exitVehicle(String licensePlate) {
        int index = hash(licensePlate);
        while (table[index].status != Status.EMPTY) {
            if (table[index].status == Status.OCCUPIED &&
                    table[index].licensePlate.equals(licensePlate)) {
                long durationMillis =
                        System.currentTimeMillis() - table[index].entryTime;

                double hours = durationMillis / (1000.0 * 60 * 60);
                double fee = hours * 5;

                table[index].status = Status.DELETED;
                size--;

                System.out.printf("Vehicle %s exited from spot #%d\n",
                        licensePlate, index);
                System.out.printf("Duration: %.2f hours, Fee: $%.2f\n",
                        hours, fee);

                return;
            }

            index = (index + 1) % capacity;
        }

        System.out.println("Vehicle not found!");
    }

    public void getStatistics() {
        double occupancy =
                (size * 100.0) / capacity;
        double avgProbes =
                size == 0 ? 0 : (double) totalProbes / size;
        System.out.println("\n===== Parking Statistics =====");
        System.out.printf("Occupancy: %.2f%%\n", occupancy);
        System.out.printf("Average Probes: %.2f\n", avgProbes);
        System.out.println("===============================\n");
    }
    public static void main(String[] args) throws InterruptedException {
        ParkingLot lot = new ParkingLot(500);
        lot.parkVehicle("ABC-1234");
        lot.parkVehicle("ABC-1235");
        lot.parkVehicle("XYZ-9999");
        Thread.sleep(2000);
        lot.exitVehicle("ABC-1234");
        lot.getStatistics();
    }
}