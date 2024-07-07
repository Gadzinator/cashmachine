package com.atmmachine.impl;

import com.atmmachine.Account;
import com.atmmachine.AccountRepository;
import com.atmmachine.AccountService;
import com.atmmachine.AtmRepository;
import com.atmmachine.CardLockManager;

public class AccountServiceImpl implements AccountService {

	private static final String CARD_PATTERN = "\\d{4}-\\d{4}-\\d{4}-\\d{4}";
	private static final int MAX_DEPOSIT = 1000000;
	private final AccountRepository accountRepository = AccountRepositoryImpl.getInstance();
	private final AtmRepository atmRepository = AtmRepositoryImpl.getInstance();
	private final CardLockManager cardLockManager = CardLockManagerImpl.getInstance();
	private String currentCardNumber;

	@Override
	public String getCurrentCardNumber() {
		return currentCardNumber;
	}

	@Override
	public void setCurrentCardNumber(String cartNumber) {
		currentCardNumber = cartNumber;
	}

	@Override
	public double getBalance(String cardNumber) {
		if (isCardLocked(cardNumber)) {
			System.out.println("Карта заблокирована. Попробуйте позже.");
			return -1;
		}
		Account account = accountRepository.getAccount(cardNumber);

		return account.getBalance();
	}

	@Override
	public void withdraw(String cardNumber, double amount) {
		if (isCardLocked(cardNumber)) {
			System.out.println("Карта заблокированна.");
			return;
		}
		final Account account = accountRepository.getAccount(cardNumber);

		if (amount > account.getBalance()) {
			System.out.println("Недостаточно средств на карте.");
		} else if (amount > atmRepository.getBalance()) {
			System.out.println("Недостаточно средств в банкомате.");
		} else {
			account.setBalance(account.getBalance() - amount);
			accountRepository.updateAccount(cardNumber, account);
			atmRepository.setBalance(atmRepository.getBalance() - amount);
			System.out.printf("Успешно снято: %.2f. Текущий баланс: %.2f%n", amount, account.getBalance());
		}
	}

	@Override
	public void deposit(String cardNumber, double amount) {
		if (isCardLocked(cardNumber)) {
			System.out.println("Карта заблокированна.");
			return;
		}
		if (amount > MAX_DEPOSIT) {
			System.out.println("Превышен лимит пополнения.");
		} else {
			Account account = accountRepository.getAccount(cardNumber);
			account.setBalance(account.getBalance() + amount);
			accountRepository.updateAccount(cardNumber, account);
			atmRepository.setBalance(atmRepository.getBalance() + amount);
			System.out.printf("Успешно пополнено: %.2f. Текущий баланс: %.2f%n", amount, account.getBalance());
		}
	}

	@Override
	public void saveData() {
		accountRepository.saveData();
	}

	@Override
	public void addAccount(String cardNumber, Account account) {
		accountRepository.addAccount(cardNumber, account);
	}

	@Override
	public boolean validateCardNumber(String cardNumber) {
		if (cardNumber.matches(CARD_PATTERN)) {
			try {
				final Account account = getAccountByCardNumber(cardNumber);
				if (account != null) {
					return true;
				} else {
					System.out.println("Истёк срок действия карты.");
				}
			} catch (NullPointerException e) {
				System.out.println("Истёк срок действия карты.");
			}
		} else {
			System.out.println("Не верный формат карты.");
		}

		return false;
	}

	@Override
	public boolean validateCredentials(String cardNumber, String pinCode) {
		final Account account = getAccountByCardNumber(cardNumber);
		if (account.getPinCode().equals(pinCode) && !(cardLockManager.isLocked(cardNumber))) {
			cardLockManager.resetFailedAttempts(cardNumber);
			return true;
		} else {
			incrementFailedAttempts(cardNumber);
		}
		System.out.println("Не правильный пароль.");

		return false;
	}

	@Override
	public boolean isCardLocked(String cardNumber) {
		return cardLockManager.isLocked(cardNumber);
	}

	private Account getAccountByCardNumber(String cardNumber) {
		return accountRepository.getAccount(cardNumber);
	}

	private void incrementFailedAttempts(String cardNumber) {
		cardLockManager.incrementFailedAttempts(cardNumber);
		if (isCardLocked(cardNumber)) {
			System.out.println("Карта заблокирована из-за трех неправильных попыток ввода ПИН-кода.");
		}
	}
}
