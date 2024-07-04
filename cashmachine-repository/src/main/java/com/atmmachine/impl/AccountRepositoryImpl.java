package com.atmmachine.impl;

import com.atmmachine.Account;
import com.atmmachine.AccountRepository;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AccountRepositoryImpl implements AccountRepository {

	private static AccountRepositoryImpl instance;
	private static final String DATA_FILE = "account_data.txt";
	private static final Map<String, Account> accounts = new HashMap<>();

	public static AccountRepositoryImpl getInstance() {
		if (instance == null) {
			instance = new AccountRepositoryImpl();
		}

		return instance;
	}

	private AccountRepositoryImpl() {
		loadData();
	}

	@Override
	public void addAccount(String cardNumber, Account account) {
		accounts.put(cardNumber, account);
	}

	@Override
	public Account getAccount(String cardNumber) {
		return accounts.get(cardNumber);
	}

	@Override
	public void updateAccount(String cardNumber, Account account) {
		accounts.put(cardNumber, account);
	}

	@Override
	public void saveData() {
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(DATA_FILE))) {
			for (Map.Entry<String, Account> entry : accounts.entrySet()) {
				final String cardNumber = entry.getKey();
				final Account account = entry.getValue();
				bw.write(String.format("%s %s %.2f%n", cardNumber, account.getPinCode(), account.getBalance()));
			}
		} catch (IOException e) {
			System.out.println("Ошибка при сохранении данных: " + e.getMessage());
		}
	}

	@Override
	public void loadData() {
		try (BufferedReader br = new BufferedReader(new FileReader(DATA_FILE))) {
			String line;
			while ((line = br.readLine()) != null) {
				String[] parts = line.split("\\s+");
				String cardNumber = parts[0];
				String pinCode = parts[1];
				double balance = Double.parseDouble(parts[2].replace(",", "."));
				accounts.put(cardNumber, new Account(pinCode, balance));
			}
		} catch (IOException e) {
			System.out.println("Ошибка при загрузке данных: " + e.getMessage());
		} catch (NumberFormatException e) {
			System.out.println("Ошибка преобразования числа: " + e.getMessage());
		}
	}
}
