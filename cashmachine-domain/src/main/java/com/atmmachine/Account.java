package com.atmmachine;

import java.util.Objects;

public class Account {

	private String pinCode;
	private double balance;

	public Account(String pinCode, double balance) {
		this.pinCode = pinCode;
		this.balance = balance;
	}

	public String getPinCode() {
		return pinCode;
	}

	public void setPinCode(String pinCode) {
		this.pinCode = pinCode;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) return true;
		if (object == null || getClass() != object.getClass()) return false;
		Account account = (Account) object;
		return Double.compare(balance, account.balance) == 0 && Objects.equals(pinCode, account.pinCode);
	}

	@Override
	public int hashCode() {
		return Objects.hash(pinCode, balance);
	}

	@Override
	public String toString() {
		return "Account{" +
				"pinCode='" + pinCode + '\'' +
				", balance=" + balance +
				'}';
	}
}
