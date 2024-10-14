import java.text.DecimalFormat;
import java.util.InputMismatchException;
import java.util.Scanner;

public class LoanCalculator {

    // Method to calculate the monthly payment
    public static double calculatePayment(int loanAmount, int annualInterestRate, int numberOfMonths) {
        double monthlyInterestRate = (annualInterestRate / 100.0) / 12;
        return loanAmount * (monthlyInterestRate * Math.pow(1 + monthlyInterestRate, numberOfMonths)) /
                (Math.pow(1 + monthlyInterestRate, numberOfMonths) - 1);
    }

    // Method to generate the amortization table as a 2D array
    public static double[][] getTable(int loanAmount, int annualInterestRate, int numberOfMonths) {
        double[][] table = new double[numberOfMonths][5];
        double remainingBalance = loanAmount;
        double monthlyPayment = calculatePayment(loanAmount, annualInterestRate, numberOfMonths);
        double monthlyInterestRate = (annualInterestRate / 100.0) / 12;

        for (int month = 1; month <= numberOfMonths; month++) {
            double interest = remainingBalance * monthlyInterestRate;
            double principal = monthlyPayment - interest;
            remainingBalance -= principal;

            table[month - 1][0] = month;                  // Month number
            table[month - 1][1] = monthlyPayment;         // Monthly payment
            table[month - 1][2] = interest;               // Interest for the month
            table[month - 1][3] = principal;              // Principal paid
            table[month - 1][4] = Math.max(remainingBalance, 0); // Remaining balance
        }

        return table;
    }

    // Method to format the amortization table as a string without using StringBuilder
    public static String toString(double payment, double[][] table) {
        String result = "\nNumber  Payment     Interest    Loan       Balance\n";
        for (int i = 0; i < table.length; i++) {
            result += toString((int) table[i][0], table[i][1], table[i][2], table[i][3], table[i][4]) + "\n";
        }
        return result;
    }

    // Method to format a single row of the amortization table
    public static String toString(int number, double payment, double interest, double loan, double balance) {
        return String.format("%4d %10.2f %9.2f %8.2f %9.2f", number, payment, interest, loan, Math.abs(balance));
    }

    // Main method to run the program
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean quit = false;

        while (!quit) {
            // Display the menu and prompt for an option
            System.out.println("\nLoan Calculator - Please choose an option.");
            System.out.println("P - Payment");
            System.out.println("T - Table");
            System.out.println("Q - Quit");
            System.out.print("Option: ");
            String option = scanner.next().toUpperCase();

            if (option.equals("Q")) {
                quit = true;
                System.out.println("Exiting the loan calculator");
            } else if (option.equals("P") || option.equals("T")) {
                // Prompt for loan details if option is P or T
                int loanAmount = 0, interestRate = 0, loanTermMonths = 0;

                // Input validation for loan amount
                boolean validInput = true;
                try {
                    System.out.print("Enter loan amount (1000 - 1000000): ");
                    loanAmount = scanner.nextInt();
                    if (loanAmount < 1000 || loanAmount > 1000000) {
                        System.out.println("Invalid amount.");
                        validInput = false;
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Invalid amount. Please enter a valid integer.");
                    scanner.next(); // Clear the invalid input
                    validInput = false;
                }

                // If invalid input, go back to menu
                if (!validInput) {
                    continue; // Display menu again
                }

                // Input validation for interest rate
                validInput = true;
                try {
                    System.out.print("Enter interest rate (1 - 10): ");
                    interestRate = scanner.nextInt();
                    if (interestRate < 1 || interestRate > 10) {
                        System.out.println("Invalid rate.");
                        validInput = false;
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Invalid rate. Please enter a valid integer.");
                    scanner.next(); // Clear the invalid input
                    validInput = false;
                }

                // If invalid input, go back to menu
                if (!validInput) {
                    continue; // Display menu again
                }

                // Input validation for number of months
                validInput = true;
                try {
                    System.out.print("Enter number of months (2 - 360): ");
                    loanTermMonths = scanner.nextInt();
                    if (loanTermMonths < 2 || loanTermMonths > 360) {
                        System.out.println("Invalid months.");
                        validInput = false;
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Invalid months. Please enter a valid integer.");
                    scanner.next(); // Clear the invalid input
                    validInput = false;
                }

                // If invalid input, go back to menu
                if (!validInput) {
                    continue; // Display menu again
                }

                if (option.equals("P")) {
                    // Display monthly payment
                    DecimalFormat df = new DecimalFormat("0.00");
                    System.out.println("Monthly Payment: $" + df.format(calculatePayment(loanAmount, interestRate, loanTermMonths)));
                } else if (option.equals("T")) {
                    // Display amortization table
                    double[][] table = getTable(loanAmount, interestRate, loanTermMonths);
                    System.out.println(toString(calculatePayment(loanAmount, interestRate, loanTermMonths), table));
                }

            } else {
                System.out.println("Invalid option. Please try again.");
            }
        }

        scanner.close();
    }
}
