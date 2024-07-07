package com.atmmachine.impl;

import com.atmmachine.CardLockManager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CardLockManagerImpl implements CardLockManager {

	private static CardLockManager instance;
	private static final int MAX_FAILED_ATTEMPTS = 3;
	private static final long LOCK_DURATION = 24 * 60 * 60 * 1000;
	private static final String FAILED_ATTEMPTS_FILE = "failed_attempts.txt";
	private final Map<String, Integer> failedAttempts = new HashMap<>();
	private final Map<String, Long> lockTime = new HashMap<>();

	private CardLockManagerImpl() {
		loadFailedAttempts();
	}

	public static CardLockManager getInstance() {
		if (instance == null) {
			instance = new CardLockManagerImpl();
		}

		return instance;
	}

	@Override
	public void incrementFailedAttempts(String cardNumber) {
		if (isLocked(cardNumber)) {
			return;
		}
		int attempts = failedAttempts.getOrDefault(cardNumber, 0) + 1;
		if (attempts >= MAX_FAILED_ATTEMPTS) {
			lockTime.put(cardNumber, System.currentTimeMillis());
			attempts = MAX_FAILED_ATTEMPTS;
		}
		failedAttempts.put(cardNumber, attempts);
		saveFailedAttempts();
	}

	@Override
	public boolean isLocked(String cardNumber) {
		if (lockTime.containsKey(cardNumber)) {
			if ((System.currentTimeMillis() - lockTime.get(cardNumber)) >= LOCK_DURATION) {
				failedAttempts.put(cardNumber, 0);
				lockTime.remove(cardNumber);
				saveFailedAttempts();
				return false;
			}
			return true;
		}
		return false;
	}

	@Override
	public void resetFailedAttempts(String cardNumber) {
		failedAttempts.put(cardNumber, 0);
		lockTime.remove(cardNumber);
		saveFailedAttempts();
	}

	private void loadFailedAttempts() {
		try (BufferedReader br = new BufferedReader(new FileReader(FAILED_ATTEMPTS_FILE))) {
			String line;
			while ((line = br.readLine()) != null) {
				String[] parts = line.split("\\s+");
				String cardNumber = parts[0];
				int attempts = Integer.parseInt(parts[1]);
				long lockTimestamp = Long.parseLong(parts[2]);
				failedAttempts.put(cardNumber, attempts);
				if (lockTimestamp > 0) {
					lockTime.put(cardNumber, lockTimestamp);
				}
			}
		} catch (IOException e) {
			System.out.println("Ошибка при загрузке данных о попытках: " + e.getMessage());
		}
	}

	private void saveFailedAttempts() {
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(FAILED_ATTEMPTS_FILE))) {
			for (Map.Entry<String, Integer> entry : failedAttempts.entrySet()) {
				String cardNumber = entry.getKey();
				int attempts = entry.getValue();
				long lockTimestamp = lockTime.getOrDefault(cardNumber, 0L);
				bw.write(String.format("%s %d %d%n", cardNumber, attempts, lockTimestamp));
			}
		} catch (IOException e) {
			System.out.println("Ошибка при сохранении данных о попытках: " + e.getMessage());
		}
	}
}
