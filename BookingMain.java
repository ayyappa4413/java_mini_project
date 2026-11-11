// Movie Ticket Booking Application
import java.util.*;

public class BookingMain {

    static class Theatre {

        private final boolean[][] booked;

        Theatre(int rows, int seatsPerRow) {
            booked = new boolean[rows][seatsPerRow];
        }

        void showSeats() {

            System.out.println("\nO = Available, X = Booked");

            for (int i = 0; i < booked.length; i++) {

                System.out.print("Row " + (i + 1) + ": ");

                for (int j = 0; j < booked[i].length; j++) {
                    if (booked[i][j]) {
                        System.out.print("X ");
                    } else {
                        System.out.print("O ");
                    }
                }

                System.out.println();
            }
        }

        boolean isAllSeatsBooked() {

            for (int i = 0; i < booked.length; i++) {
                for (int j = 0; j < booked[i].length; j++) {

                    if (!booked[i][j]) {
                        return false;
                    }
                }
            }

            return true;
        }

        boolean hasAnyBooking() {

            for (int i = 0; i < booked.length; i++) {
                for (int j = 0; j < booked[i].length; j++) {

                    if (booked[i][j]) {
                        return true;
                    }
                }
            }

            return false;
        }
        void validateSeat(int row, int seat) {
            checkSeat(row, seat);

            if (booked[row - 1][seat - 1]) {
                throw new IllegalStateException("This seat is already booked.");
            }
        }
        double bookTicket(int row, int seat, boolean weekend, double discount) {

            checkSeat(row, seat);

            if (booked[row - 1][seat - 1]) {
                throw new IllegalStateException("This seat is already booked.");
            }

            booked[row - 1][seat - 1] = true;

            double price = weekend ? 250 : 180;

            return price - (price * discount / 100);
        }

        void cancelTicket(int row, int seat) {

            checkSeat(row, seat);

            if (!booked[row - 1][seat - 1]) {
                throw new IllegalStateException("This seat is not booked.");
            }

            booked[row - 1][seat - 1] = false;

            System.out.println("Ticket cancelled successfully.");
        }

        private void checkSeat(int row, int seat) {

            if (row < 1 || row > booked.length ||
                seat < 1 || seat > booked[0].length) {

                throw new IllegalArgumentException("Invalid row or seat number.");
            }
        }
    }

    static Scanner sc = new Scanner(System.in);

    static int readNumber(String message) {

        while (true) {

            System.out.print(message);

            try {
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    public static void main(String[] args) {

        Theatre theatre = new Theatre(5, 6);

        while (true) {

            System.out.println("\n--- MOVIE TICKET BOOKING ---");
            System.out.println("1. Show Seats");
            System.out.println("2. Book Ticket");
            System.out.println("3. Cancel Ticket");
            System.out.println("0. Exit");

            int choice = readNumber("Enter your choice: ");

            try {

                switch (choice) {

                    case 1:

                        theatre.showSeats();
                        break;

                    case 2:

                        if (theatre.isAllSeatsBooked()) {
                            System.out.println("Sorry! All seats are already booked.");
                            break;
                        }

                        int row = readNumber("Enter row number: ");
                        int seat = readNumber("Enter seat number: ");

                        theatre.validateSeat(row, seat);

                        System.out.print("Is it a weekend? (yes/no): ");
                        boolean weekend = sc.nextLine().trim().equalsIgnoreCase("yes");

                        System.out.print("Enter coupon code (SAVE10 or none): ");
                        String coupon = sc.nextLine().trim();

                        double discount = coupon.equalsIgnoreCase("SAVE10") ? 10 : 0;

                        double total = theatre.bookTicket(row, seat, weekend, discount);

                        System.out.printf(
                            "Ticket booked successfully! Total amount: Rs. %.2f%n",
                            total
                        );

                        break;

                    case 3:

                        if (!theatre.hasAnyBooking()) {
                            System.out.println("No tickets are available to cancel.");
                            break;
                        }

                        int cancelRow = readNumber("Enter row number: ");
                        int cancelSeat = readNumber("Enter seat number: ");

                        theatre.cancelTicket(cancelRow, cancelSeat);
                        break;

                    case 0:

                        System.out.println("Thank you! Goodbye.");
                        return;

                    default:

                        System.out.println("Invalid choice. Please try again.");
                }

            } catch (RuntimeException e) {

                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}
