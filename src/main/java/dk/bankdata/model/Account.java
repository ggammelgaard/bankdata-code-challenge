package dk.bankdata.model;

import jakarta.persistence.*;

@Entity
public class Account {
    @Id
    @SequenceGenerator( // Allows for increment in import.sql as well as dynamic inserts
            name = "accountNumberSeq",
            sequenceName = "ACCOUNT_SEQ",
            allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "accountNumberSeq")
    private Long accountNumber;
    private double balance;
    private String firstName;
    private String lastName;


    public Long getAccountNumber() {
        return accountNumber;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public double getBalance() {
        return balance;
    }

    public void addToBalance(double amount) {
        this.balance += amount;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}