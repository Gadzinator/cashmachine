package com.atmmachine;


public interface AccountService {

	String getCurrentCardNumber();

	void setCurrentCardNumber(String cartNumber);

	double getBalance(String cardNumber);

	void withdraw(String cardNumber, double amount);

	void deposit(String cardNumber, double amount);

	void saveData();

	void addAccount(String cardNumber, Account account);

	boolean validateCardNumber(String cardNumber);

	boolean validateCredentials(String cardNumber, String pinCode);

	boolean isCardLocked(String cardNumber);
}
