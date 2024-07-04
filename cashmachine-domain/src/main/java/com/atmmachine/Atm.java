package com.atmmachine;

public class Atm {

	private double balance;

	public Atm(double balance) {
		this.balance = balance;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	@Override
	public String toString() {
		return "Atm{" +
				"balance=" + balance +
				'}';
	}
}
