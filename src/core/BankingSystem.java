package core;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import database.db;

class sbs {
    private Connection conn = db.getConnection();
    private Scanner sc = new Scanner(System.in);
    int processingFee;
    String bank;

    public void selectBank() {
        System.out.println();
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM banks");
            System.out.println("Select your bank");
            int index = 0;
            String bank_name[] = new String[3];
            while (rs.next()) {
                bank_name[index] = rs.getString("bank_name");
                System.out.println((index + 1) + ". " + bank_name[index]);
                index++;
            }
            int opt = sc.nextInt();

            switch (opt) {
                case 1:
                    processingFee = 2;
                    bank = bank_name[opt - 1];
                    break;
                case 2:
                    processingFee = 4;
                    bank = bank_name[opt - 1];
                    break;
                case 3:
                    processingFee = 5;
                    bank = bank_name[opt - 1];
                    break;
                default:
                    System.out.println("Enter a valid choice");
            }
            System.out.println("Thanks for choosing " + bank);
            homePage();
        } catch (SQLException ex) {
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
    }

    public void createAccount() {
        System.out.println();
        String name;
        String phone;
        int pin;
        int balance = 0;
        System.out.println("Enter your name:");
        name = sc.next();
        System.out.println("Enter your phone number:");
        phone = sc.next();
        if (phone.length() != 10) {
            System.out.println("Enter a valid phone number");
            createAccount();
        }
        System.out.println("Choose your pin:");
        pin = sc.nextInt();
        if (pin < 1000 || pin > 9999) {
            System.out.println("Enter a valid pin");
            createAccount();
        }
        System.out.println("Do you want to deposit money? (y/n)");
        String opt = sc.next();
        if (opt.equals("y")) {
            System.out.println("Enter the amount to deposit");
            balance = sc.nextInt();
        }
        Statement stmt = null;
        System.out.println();

        System.out.println("Please check details and press 1 to confirm or 2 to edit: ");
        System.out.println();

        System.out.println("Name: " + name + "\nPhone number: " + phone + "\nPin: " + pin + "\nBalance: " + balance
                + "\nBank: " + bank);
        int choice = sc.nextInt();
        if (choice == 1) {
            try {
                stmt = conn.createStatement();
                stmt.execute("INSERT INTO user (name, phone_number, pin, balance, bank_name) VALUES ('" + name + "', '" + phone
                        + "', '" + pin + "', '" + balance + "', '" + bank + "')");
                System.out.println("Your account is created. \nPlease visit the bank for verification.\nThank you");
            } catch (SQLException ex) {
                // handle any errors
                System.out.println("SQLException: " + ex.getMessage());
                System.out.println("SQLState: " + ex.getSQLState());
                System.out.println("VendorError: " + ex.getErrorCode());
            }
        } else {
            createAccount();
        }

        homePage();
    }

    public void homePage() {
        System.out.println("Smart Bank Space welcomes you");
        System.out.println();

        System.out.println("Enter your choice: ");
        System.out.println("1. Login to your account");
        System.out.println("2. Open a new bank account");
        int opt = sc.nextInt();
        switch (opt) {
            case 1:
                login();
                break;
            case 2:
                createAccount();
                break;
            default:
                System.out.println("Enter a valid choice");
        }
    }

    public void login() {
        System.out.println("Enter your phone number: ");
        String phone = sc.next();
        if (phone.length() != 10) {
            System.out.println("Enter a valid phone number");
            login();
        }
        System.out.println("Enter your pin: ");
        int enteredPin = sc.nextInt();
        if (enteredPin < 1000 || enteredPin > 9999) {
            System.out.println("Enter a valid pin");
            login();
        }
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM user WHERE phone_number = " + phone);
            if (rs.next()) {
                int pin = rs.getInt("pin");
                if (pin == enteredPin) {
                    System.out.println("Login successful");
                    menu(phone, pin);
                } else {
                    System.out.println("Incorrect pin");
                    login();
                }
            } else {
                System.out.println("Phone number not found");
                login();
            }
        } catch (SQLException ex) {
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }

    }

    public void menu(String phone, int pin) {
        System.out.println();
        System.out.println("Enter your choice: ");
        System.out.println("1. Check account balance ");
        System.out.println("2. Withdraw money");
        System.out.println("3. Deposit money ");
        System.out.println("4. Exit ");
        int opt = sc.nextInt();
        switch (opt) {
            case 1:
                checkBalance(phone, pin);
                break;
            case 2:
                withdraw(phone, pin);
                break;
            case 3:
                deposit(phone, pin);
                break;
            case 4:
                System.out.println("Thank you for using Smart Bank Space");
                System.exit(0);
                break;
            default:
                System.out.println("Enter a valid choice");
        }
    }

    public void checkBalance(String phone, int pin) {
        System.out.println();
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM user WHERE phone_number = " + phone);
            if (rs.next()) {
                int validPin = rs.getInt("pin");
                if (pin == validPin) {
                    System.out.println("Login successful");
                    float balance = rs.getFloat("balance");
                    System.out.println("Your balance is: " + balance);
                    menu(phone, pin);
                } else {
                    System.out.println("Incorrect pin");
                    login();
                }
            } else {
                System.out.println("Phone number not found");
                login();
            }
        } catch (SQLException ex) {
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
        System.out.println();
        menu(phone, pin);
    }

    public void withdraw(String phone, int pin) {
        System.out.println();
        Statement stmt = null;
        ResultSet rs = null;

        System.out.println("Enter amount to withdraw, available denominations are 100, 500, 2000");
        float amount = sc.nextFloat();
        if (amount % 100 != 0) {
            System.out.println("Enter a valid amount");
            withdraw(phone, pin);
        }
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM user WHERE phone_number = " + phone);
            if (rs.next()) {
                int validPin = rs.getInt("pin");
                if (pin == validPin) {
                    float balance = rs.getFloat("balance");
                    if (balance >= amount + processingFee) {
                        balance -= amount + processingFee;
                        stmt.execute("UPDATE user SET balance = " + balance + " WHERE phone_number = " + phone);
                        System.out.println("Please collect your cash");
                        System.out.println("Your balance is: " + balance);
                    } else {
                        System.out.println("Insufficient balance");
                    }
                } else {
                    System.out.println("Incorrect pin");
                    login();
                }
            } else {
                System.out.println("Phone number not found");
                login();
            }
        } catch (SQLException ex) {
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }

        System.out.println();
        menu(phone, pin);
    }

    public void deposit(String phone, int pin) {
        System.out.println();
        System.out.println("Enter the amount: ");
        float amount = sc.nextFloat();
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM user WHERE phone_number = " + phone);
            if (rs.next()) {
                int validPin = rs.getInt("pin");
                if (pin == validPin) {
                    float balance = rs.getFloat("balance");
                    balance += amount;
                    stmt.execute("UPDATE user SET balance = " + balance + " WHERE phone_number = " + phone);
                    System.out.println("Your balance is: " + balance);
                    System.out.println("Money deposited successfully");
                } else {
                    System.out.println("Incorrect pin");
                    login();
                }
            } else {
                System.out.println("Phone number not found");
                login();
            }
        } catch (SQLException ex) {
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
        System.out.println();
        menu(phone, pin);
    }
}

public class BankingSystem {
    public static void main(String args[]) {
        sbs ob = new sbs();
        ob.selectBank();
    }
}