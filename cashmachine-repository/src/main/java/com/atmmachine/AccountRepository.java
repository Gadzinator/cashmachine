package com.atmmachine;

public interface AccountRepository {

	void addAccount(String cardNumber, Account account);

	Account getAccount(String cardNumber);

	void updateAccount(String cardNumber, Account account);

	void saveData();

	void loadData();
}
