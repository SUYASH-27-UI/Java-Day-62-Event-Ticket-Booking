
import java.util.*;
import java.io.*;

class Booking {
    int id;
    String customerName;
    int tickets;

    Booking(int id, String customerName, int tickets) {
        this.id = id;
        this.customerName = customerName;
        this.tickets = tickets;
    }

    void display() {
        System.out.println("Booking ID: " + id);
        System.out.println("Customer: " + customerName);
        System.out.println("Tickets: " + tickets);
        System.out.println("Amount: Rs. " + (tickets * 300));
        System.out.println("----------------------");
    }
}

public class Main {

    static ArrayList<Booking> bookings = new ArrayList<>();
    static Scanner sc = new Scanner(System.in);

    static final int TOTAL_SEATS = 50;
    static final int TICKET_PRICE = 300;
    static final String FILE_NAME = "bookings.txt";

    static void loadBookings() {
        try (BufferedReader br =
                 new BufferedReader(new FileReader(FILE_NAME))) {

            String line;

            while ((line = br.readLine()) != null) {
                String[] data = line.split("\\|", 3);

                if (data.length == 3) {
                    int id = Integer.parseInt(data[0]);
                    int tickets = Integer.parseInt(data[2]);

                    if (id > 0 && tickets > 0 &&
                        !data[1].isBlank() &&
                        !data[1].contains("|")) {

                        bookings.add(new Booking(
                            id, data[1], tickets
                        ));
                    }
                }
            }

        } catch (FileNotFoundException e) {
            // File will be created when the first booking is saved.
        } catch (IOException | NumberFormatException e) {
            System.out.println("Error loading booking records.");
        }
    }

    static void saveBookings() {
        try (PrintWriter writer =
                 new PrintWriter(new FileWriter(FILE_NAME))) {

            for (Booking b : bookings) {
                writer.println(
                    b.id + "|" + b.customerName + "|" + b.tickets
                );
            }

        } catch (IOException e) {
            System.out.println("Unable to save bookings!");
        }
    }

    static int bookedSeats() {
        int total = 0;

        for (Booking b : bookings) {
            total += b.tickets;
        }

        return total;
    }

    static int nextID() {
        int maxID = 0;

        for (Booking b : bookings) {
            if (b.id > maxID)
                maxID = b.id;
        }

        return maxID + 1;
    }

    static void bookTickets() {
        int available = TOTAL_SEATS - bookedSeats();

        if (available <= 0) {
            System.out.println("All seats are booked!");
            return;
        }

        System.out.print("Enter customer name: ");
        String name = sc.nextLine().trim();

        if (name.isEmpty() || name.contains("|")) {
            System.out.println("Invalid customer name!");
            return;
        }

        System.out.println("Available Seats: " + available);
        System.out.print("Enter number of tickets: ");

        int quantity = readNumber();

        if (quantity <= 0 || quantity > available) {
            System.out.println("Invalid ticket quantity!");
            return;
        }

        Booking booking = new Booking(
            nextID(), name, quantity
        );

        bookings.add(booking);
        saveBookings();

        System.out.println("\nBooking successful!");
        booking.display();
    }

    static void viewBookings() {
        if (bookings.isEmpty()) {
            System.out.println("No bookings found!");
            return;
        }

        System.out.println("\n===== ALL BOOKINGS =====");

        for (Booking b : bookings) {
            b.display();
        }
    }

    static void searchBooking() {
        System.out.print("Enter booking ID: ");
        int id = readNumber();

        for (Booking b : bookings) {
            if (b.id == id) {
                System.out.println("\nBooking Found!");
                b.display();
                return;
            }
        }

        System.out.println("Booking not found!");
    }

    static void cancelBooking() {
        System.out.print("Enter booking ID to cancel: ");
        int id = readNumber();

        for (int i = 0; i < bookings.size(); i++) {
            if (bookings.get(i).id == id) {
                bookings.remove(i);
                saveBookings();

                System.out.println("Booking cancelled successfully!");
                return;
            }
        }

        System.out.println("Booking not found!");
    }

    static void showSummary() {
        int seats = bookedSeats();
        int available = TOTAL_SEATS - seats;
        int revenue = seats * TICKET_PRICE;

        System.out.println("\n===== EVENT SUMMARY =====");
        System.out.println("Total Seats: " + TOTAL_SEATS);
        System.out.println("Booked Seats: " + seats);
        System.out.println("Available Seats: " + available);
        System.out.println("Total Bookings: " + bookings.size());
        System.out.println("Total Revenue: Rs. " + revenue);
    }

    static int readNumber() {
        try {
            return Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public static void main(String[] args) {

        loadBookings();

        int choice;

        do {
            System.out.println("\n===== EVENT TICKET BOOKING =====");
            System.out.println("1. Book Tickets");
            System.out.println("2. View All Bookings");
            System.out.println("3. Search Booking");
            System.out.println("4. Cancel Booking");
            System.out.println("5. Event Summary");
            System.out.println("6. Exit");

            System.out.print("Enter choice: ");
            choice = readNumber();

            switch (choice) {
                case 1:
                    bookTickets();
                    break;

                case 2:
                    viewBookings();
                    break;

                case 3:
                    searchBooking();
                    break;

                case 4:
                    cancelBooking();
                    break;

                case 5:
                    showSummary();
                    break;

                case 6:
                    System.out.println("Thank you!");
                    break;

                default:
                    System.out.println("Invalid choice!");
            }

        } while (choice != 6);

        sc.close();
    }
}
