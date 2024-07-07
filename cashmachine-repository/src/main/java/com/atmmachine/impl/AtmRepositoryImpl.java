package com.atmmachine.impl;

import com.atmmachine.Atm;
import com.atmmachine.AtmRepository;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class AtmRepositoryImpl implements AtmRepository {

	private static AtmRepositoryImpl instance;
	private static final String DATA_FILE = "atm_data.txt";
	private Atm atm;

	public static AtmRepositoryImpl getInstance() {
		if (instance == null) {
			instance = new AtmRepositoryImpl();
		}

		return instance;
	}

	private AtmRepositoryImpl() {
		loadData();
	}

	@Override
	public double getBalance() {
		return atm.getBalance();
	}

	@Override
	public void setBalance(double balance) {
		atm.setBalance(balance);
		saveData();
	}

	@Override
	public void saveData() {
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(DATA_FILE))) {
			bw.write(String.format("%.2f%n", atm.getBalance()));
		} catch (IOException e) {
			System.out.println("Ошибка при сохранении данных: " + e.getMessage());
		}
	}

	@Override
	public void loadData() {
		try (BufferedReader br = new BufferedReader(new FileReader(DATA_FILE))) {
			String line = br.readLine();
			if (line != null) {
				double balance = Double.parseDouble(line.replace(",", "."));
				atm = new Atm(balance);
			} else {
				atm = new Atm(0);
			}
		} catch (IOException e) {
			System.out.println("Ошибка при загрузке данных: " + e.getMessage());
			atm = new Atm(0);
		}
	}
}
