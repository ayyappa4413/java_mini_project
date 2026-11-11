
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

public class TrackerMain {

    enum Category {
        FOOD, TRAVEL, SHOPPING, BILLS,
        EDUCATION, HEALTH, OTHER
    }

        static class Expense {
        private final int id;
        private final String description;
        private final Category category;
        private final double amount;
        private final LocalDate date;

        Expense(int id, String description, Category category,
                double amount, LocalDate date) {

            this.id = id;
            this.description = description;
            this.category = category;
            this.amount = amount;
            this.date = date;
        }

        int getId() {
            return id;
        }

        Category getCategory() {
            return category;
        }

        double getAmount() {
            return amount;
        }

        public String toString() {
            return String.format(
                "ID: %d | %-20s | %-10s | Rs. %.2f | %s",
                id, description, category, amount, date
            );
        }
    }

    static class ExpenseService {

        private final List<Expense> expenses = new ArrayList<>();
        private int nextId = 1;

        void add(String description, Category category,
                 double amount, LocalDate date) {

            if (description.isBlank()) {
                throw new IllegalArgumentException(
                    "Description cannot be empty."
                );
            }

            if (!Double.isFinite(amount) || amount <= 0) {
                throw new IllegalArgumentException(
                    "Amount must be a valid positive number."
                );
            }

            expenses.add(
                new Expense(nextId++, description,
                            category, amount, date)
            );

            System.out.println("Expense added successfully.");
        }

        void showAll() {

            if (expenses.isEmpty()) {
                System.out.println("No expenses found.");
                return;
            }

            expenses.forEach(System.out::println);
        }

        void delete(int id) {
        
            if (expenses.isEmpty()) {
                System.out.println("No expenses available to delete.");
                return;
            }
        
            boolean removed = expenses.removeIf(
                e -> e.getId() == id
            );
        
            if (removed) {
                System.out.println("Expense deleted successfully.");
            } else {
                System.out.println("Expense ID not found.");
            }
        }

        boolean isEmpty() {
            return expenses.isEmpty();
        }

        void summary() {

            if (expenses.isEmpty()) {
                System.out.println("No expenses available to calculate summary.");
                return;
            }

            double total = expenses.stream()
            .mapToDouble(Expense::getAmount)
            .sum();

            System.out.printf("Total Expense: Rs. %.2f%n", total);

            expenses.stream()
                .max(Comparator.comparingDouble(
                    Expense::getAmount
                ))
                .ifPresent(e ->
                    System.out.println("Highest Expense: " + e)
                );

            Map<Category, Double> totals =
                expenses.stream().collect(
                    Collectors.groupingBy(
                        Expense::getCategory,
                        () -> new EnumMap<>(Category.class),
                        Collectors.summingDouble(
                            Expense::getAmount
                        )
                    )
                );

            System.out.println("\nCategory-wise Expenses:");

            totals.forEach((category, amount) ->
                System.out.printf("%s : Rs. %.2f%n", category, amount)
                );
        }

        void searchByCategory(Category category) {

            List<Expense> filtered = expenses.stream()
                .filter(e -> e.getCategory() == category)
                .toList();

            if (filtered.isEmpty()) {
                System.out.println("No expenses found in " + category + ".");
                return;
            }

            System.out.println("\nExpenses in " + category + ":");

            filtered.forEach(System.out::println);
        }
    }

    static final Scanner sc = new Scanner(System.in);

    static int readInt(String prompt) {

        while (true) {

            System.out.print(prompt);

            try {
                return Integer.parseInt(sc.nextLine().trim());

            } catch (NumberFormatException e) {
                System.out.println("Enter a valid integer.");
            }
        }
    }

    static double readDouble(String prompt) {

        while (true) {

            System.out.print(prompt);

            try {
                return Double.parseDouble(sc.nextLine().trim());

            } catch (NumberFormatException e) {
                System.out.println("Enter a valid number.");
            }
        }
    }

    static Category readCategory() {

        while (true) {

            System.out.println(
                "Categories: " + Arrays.toString(Category.values())
            );

            try {
                return Category.valueOf(
                    sc.nextLine().trim().toUpperCase()
                );

            } catch (IllegalArgumentException e) {
                System.out.print(
                    "Invalid category. Enter again: "
                );
            }
        }
    }

    static LocalDate readDate() {

        while (true) {

            try {
                System.out.print("Date (YYYY-MM-DD): ");

                return LocalDate.parse(
                    sc.nextLine().trim()
                );

            } catch (DateTimeParseException e) {
                System.out.println(
                    "Invalid date. Use YYYY-MM-DD format."
                );
            }
        }
    }

    public static void main(String[] args) {

        ExpenseService service = new ExpenseService();

        while (true) {

            System.out.println("\n--- PERSONAL EXPENSE TRACKER ---");

            System.out.println("1. Add Expense");
            System.out.println("2. Show All Expenses");
            System.out.println("3. Delete Expense");
            System.out.println("4. Expense Summary");
            System.out.println("5. Search by Category");
            System.out.println("0. Exit");

            int choice = readInt("Enter your choice: ");

            try {

                switch (choice) {

                    case 1:

                        System.out.print("Description: ");
                        String desc = sc.nextLine();

                        System.out.println("Select Category:");
                        Category cat = readCategory();

                        double amount = readDouble("Amount: ");

                        LocalDate date = readDate();

                        service.add(
                            desc, cat, amount, date
                        );

                        break;

                    case 2:

                        service.showAll();

                        break;

                    case 3:
                    
                        if (service.isEmpty()) {
                            System.out.println("No expenses available to delete.");
                            break;
                        }
                    
                        int id = readInt("Expense ID: ");
                    
                        service.delete(id);
                    
                        break;

                    case 4:

                        service.summary();

                        break;

                    case 5:
                    
                        if (service.isEmpty()) {
                            System.out.println("No expenses available to search.");
                            break;
                        }
                    
                        System.out.println("Select Category:");
                    
                        Category category = readCategory();
                    
                        service.searchByCategory(category);
                    
                        break;

                    case 0:

                        System.out.println("Goodbye!");

                        return;

                    default:
                        

                        System.out.println("Invalid choice.");

                }

            } catch (RuntimeException e) {

                System.out.println("Error: " + e.getMessage());

            }
        }
    }
}