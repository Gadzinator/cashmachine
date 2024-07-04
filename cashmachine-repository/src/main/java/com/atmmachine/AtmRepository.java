package com.atmmachine;

public interface AtmRepository {

	double getBalance();

	void setBalance(double balance);

	void saveData();

	void loadData();
}
