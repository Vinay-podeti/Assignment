import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ExpenseTracker {

	    private List<Transaction> transactions;
	    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	    private static final String FILE_PATH = "transactions.csv";

	    static class Transaction {
	        String type;
	        String category;
	        double amount;
	        LocalDate date;
	        String description;

	        Transaction(String type, String category, double amount, LocalDate date, String description) {
	            this.type = type;
	            this.category = category;
	            this.amount = amount;
	            this.date = date;
	            this.description = description;
	        }

	        public String toString() {
	            return String.format("%s,%s,%.2f,%s,%s", type, category, amount, date.format(DATE_FORMATTER), description);
	        }
	    }

	    public ExpenseTracker() {
	        transactions = new ArrayList<>();
	    }

	    public void addTransaction(Scanner scanner) {
	        System.out.println("Enter type (1 for Income, 2 for Expense): ");
	        int typeChoice = scanner.nextInt();
	        scanner.nextLine();

	        String type;
	        String[] categories;
	        if (typeChoice == 1) {
	            type = "Income";
	            categories = new String[]{"Salary", "Business"};
	        } else if (typeChoice == 2) {
	            type = "Expense";
	            categories = new String[]{"Food", "Rent", "Travel"};
	        } else {
	            System.out.println("Invalid choice!");
	            return;
	        }

	        System.out.println("Choose category:");
	        for (int i = 0; i < categories.length; i++) {
	            System.out.println((i + 1) + ". " + categories[i]);
	        }
	        int categoryChoice = scanner.nextInt();
	        scanner.nextLine();
	        if (categoryChoice < 1 || categoryChoice > categories.length) {
	            System.out.println("Invalid category!");
	            return;
	        }
	        String category = categories[categoryChoice - 1];

	        System.out.println("Enter amount: ");
	        double amount = scanner.nextDouble();
	        scanner.nextLine();

	        System.out.println("Enter date (YYYY-MM-DD): ");
	        String dateStr = scanner.nextLine();
	        LocalDate date;
	        try {
	            date = LocalDate.parse(dateStr, DATE_FORMATTER);
	        } catch (Exception e) {
	            System.out.println("Invalid date format!");
	            return;
	        }

	        System.out.println("Enter description: ");
	        String description = scanner.nextLine();

	        transactions.add(new Transaction(type, category, amount, date, description));
	        System.out.println("Transaction added!");
	    }

	    // View monthly summary
	    public void viewMonthlySummary(Scanner scanner) {
	        System.out.println("Enter year (e.g., 2025): ");
	        int year = scanner.nextInt();
	        System.out.println("Enter month (1-12): ");
	        int month = scanner.nextInt();
	        scanner.nextLine();

	        double totalIncome = 0;
	        double totalExpense = 0;

	        for (Transaction t : transactions) {
	            if (t.date.getYear() == year && t.date.getMonthValue() == month) {
	                if (t.type.equals("Income")) {
	                    totalIncome += t.amount;
	                } else {
	                    totalExpense += t.amount;
	                }
	            }
	        }

	        double balance = totalIncome - totalExpense;
	        System.out.printf("Summary for %d-%02d:%n", year, month);
	        System.out.printf("Total Income: $%.2f%n", totalIncome);
	        System.out.printf("Total Expenses: $%.2f%n", totalExpense);
	        System.out.printf("Balance: $%.2f%n", balance);
	    }

	    public void saveToFile() {
	        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
	            writer.write("type,category,amount,date,description\n");
	            for (Transaction t : transactions) {
	                writer.write(t.toString() + "\n");
	            }
	            System.out.println("Saved to " + FILE_PATH);
	        } catch (IOException e) {
	            System.out.println("Error saving file!");
	        }
	    }

	    public void loadFromFile() {
	        transactions.clear();
	        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
	            String line = reader.readLine(); // Skip header
	            while ((line = reader.readLine()) != null) {
	                String[] parts = line.split(",", -1);
	                if (parts.length != 5) {
	                    System.out.println("Skipping invalid line: " + line);
	                    continue;
	                }
	                try {
	                    String type = parts[0];
	                    String category = parts[1];
	                    double amount = Double.parseDouble(parts[2]);
	                    LocalDate date = LocalDate.parse(parts[3], DATE_FORMATTER);
	                    String description = parts[4];
	                    transactions.add(new Transaction(type, category, amount, date, description));
	                } catch (Exception e) {
	                    System.out.println("Error parsing line: " + line);
	                }
	            }
	            System.out.println("Loaded from " + FILE_PATH);
	        } catch (FileNotFoundException e) {
	            System.out.println("File not found!");
	        } catch (IOException e) {
	            System.out.println("Error loading file!");
	        }
	    }

	    public void run() {
	        Scanner scanner = new Scanner(System.in);
	        while (true) {
	            System.out.println("\nExpense Tracker:");
	            System.out.println("1. Add Transaction");
	            System.out.println("2. View Monthly Summary");
	            System.out.println("3. Save to File");
	            System.out.println("4. Load from File");
	            System.out.println("5. Exit");
	            System.out.println("Enter choice: ");
	            int choice = scanner.nextInt();
	            scanner.nextLine();

	            switch (choice) {
	                case 1:
	                    addTransaction(scanner);
	                    break;
	                case 2:
	                    viewMonthlySummary(scanner);
	                    break;
	                case 3:
	                    saveToFile();
	                    break;
	                case 4:
	                    loadFromFile();
	                    break;
	                case 5:
	                    System.out.println("Exiting...");
	                    scanner.close();
	                    return;
	                default:
	                    System.out.println("Invalid choice!");
	            }
	        }
	    }

	    public static void main(String[] args) {
	        new ExpenseTracker().run();
	    }
	}


