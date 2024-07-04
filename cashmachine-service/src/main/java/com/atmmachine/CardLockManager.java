package com.atmmachine;

public interface CardLockManager {

	void incrementFailedAttempts(String cardNumber);

	boolean isLocked(String cardNumber);

	void resetFailedAttempts(String cardNumber);
}
