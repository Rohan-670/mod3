import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Simple console-based Banking Management System.
 * Features: deposit, withdraw, check balance, and transaction history.
 * Transactions are stored in memory and are cleared when the program exits.
 */
public class BankingManagementSystem {
    private static final Scanner SCANNER = new Scanner(System.in);
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    private static class Transaction {
        private final LocalDateTime time;
        private final String type;
        private final double amount;
        private final double balanceAfter;

        Transaction(String type, double amount, double balanceAfter) {
            this.time = LocalDateTime.now();
            this.type = type;
            this.amount = amount;
            this.balanceAfter = balanceAfter;
        }

        @Override
        public String toString() {
            return String.format("%-19s | %-10s | Amount: %10.2f | Balance: %10.2f",
                    time.format(TIME_FORMAT), type, amount, balanceAfter);
        }
    }

    private static class BankAccount {
        private final String accountHolder;
        private final String accountNumber;
        private double balance;
        private final List<Transaction> transactions = new ArrayList<>();

        BankAccount(String accountHolder, String accountNumber, double openingBalance) {
            this.accountHolder = accountHolder;
            this.accountNumber = accountNumber;
            this.balance = openingBalance;
            transactions.add(new Transaction("OPENING", openingBalance, openingBalance));
        }

        boolean deposit(double amount) {
            if (!isValidAmount(amount)) {
                System.out.println("Amount must be greater than zero.");
                return false;
            }
            balance += amount;
            transactions.add(new Transaction("DEPOSIT", amount, balance));
            System.out.printf("Deposit successful. Updated balance: %.2f%n", balance);
            return true;
        }

        boolean withdraw(double amount) {
            if (!isValidAmount(amount)) {
                System.out.println("Amount must be greater than zero.");
                return false;
            }
            if (amount > balance) {
                System.out.println("Withdrawal failed: insufficient balance.");
                return false;
            }
            balance -= amount;
            transactions.add(new Transaction("WITHDRAWAL", amount, balance));
            System.out.printf("Withdrawal successful. Updated balance: %.2f%n", balance);
            return true;
        }

        void showBalance() {
            System.out.println("\n--- Account Details ---");
            System.out.println("Account holder : " + accountHolder);
            System.out.println("Account number : " + accountNumber);
            System.out.printf("Current balance: %.2f%n", balance);
        }

        void showTransactionHistory() {
            System.out.println("\n--- Transaction History ---");
            if (transactions.isEmpty()) {
                System.out.println("No transactions found.");
                return;
            }
            System.out.printf("%-19s | %-10s | %-22s | %-10s%n",
                    "Date and time", "Type", "Amount", "Balance after");
            System.out.println("--------------------------------------------------------------------------");
            for (Transaction transaction : transactions) {
                System.out.println(transaction);
            }
        }

        private boolean isValidAmount(double amount) {
            return Double.isFinite(amount) && amount > 0;
        }
    }

    public static void main(String[] args) {
        System.out.println("====================================");
        System.out.println("      BANKING MANAGEMENT SYSTEM");
        System.out.println("====================================");

        String holder = readNonEmptyLine("Enter account holder name: ");
        String accountNumber = readNonEmptyLine("Enter account number: ");
        double openingBalance = readNonNegativeAmount("Enter opening balance: ");
        BankAccount account = new BankAccount(holder, accountNumber, openingBalance);

        boolean running = true;
        while (running) {
            printMenu();
            int choice = readInt("Choose an option: ");
            switch (choice) {
                case 1:
                    account.deposit(readPositiveAmount("Enter deposit amount: "));
                    break;
                case 2:
                    account.withdraw(readPositiveAmount("Enter withdrawal amount: "));
                    break;
                case 3:
                    account.showBalance();
                    break;
                case 4:
                    account.showTransactionHistory();
                    break;
                case 5:
                    running = false;
                    System.out.println("Thank you for using the Banking Management System.");
                    break;
                default:
                    System.out.println("Invalid option. Please choose a number from 1 to 5.");
            }
            System.out.println();
        }
        SCANNER.close();
    }

    private static void printMenu() {
        System.out.println("--- Main Menu ---");
        System.out.println("1. Deposit money");
        System.out.println("2. Withdraw money");
        System.out.println("3. Check balance");
        System.out.println("4. View transaction history");
        System.out.println("5. Exit");
    }

    private static String readNonEmptyLine(String prompt) {
        while (true) {
            System.out.print(prompt);
            String value = SCANNER.nextLine().trim();
            if (!value.isEmpty()) {
                return value;
            }
            System.out.println("Input cannot be empty. Please try again.");
        }
    }

    private static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = SCANNER.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid whole number.");
            }
        }
    }

    private static double readPositiveAmount(String prompt) {
        while (true) {
            double amount = readDouble(prompt);
            if (Double.isFinite(amount) && amount > 0) {
                return amount;
            }
            System.out.println("Enter an amount greater than zero.");
        }
    }

    private static double readNonNegativeAmount(String prompt) {
        while (true) {
            double amount = readDouble(prompt);
            if (Double.isFinite(amount) && amount >= 0) {
                return amount;
            }
            System.out.println("Opening balance cannot be negative.");
        }
    }

    private static double readDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = SCANNER.nextLine().trim();
            try {
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid numeric amount.");
            }
        }
    }
}
