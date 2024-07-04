package com.atmmachine;


import com.atmmachine.impl.AccountServiceImpl;
import com.atmmachine.impl.AtmServiceImpl;

import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		AccountService accountService = new AccountServiceImpl();
		AtmService atmService = new AtmServiceImpl();
		atmService.setBalance(1000000);

		accountService.addAccount("1111-1111-1111-1111", new Account("1234", 1000.0));
		accountService.addAccount("2222-2222-2222-2222", new Account("5678", 2000.0));
		accountService.saveData();

		Scanner scanner = new Scanner(System.in);
		System.out.println("Введите номер карты: ");
		String cardNumber = scanner.nextLine();
		if (!accountService.validateCardNumber(cardNumber)) {
			return;
		}

		System.out.println("введите пин код: ");
		String pinCode = scanner.nextLine();

		if (accountService.validateCredentials(cardNumber, pinCode)) {
			while (true) {
				System.out.println("\nВыберите операцию:");
				System.out.println("1. Проверить баланс");
				System.out.println("2. Снять средства");
				System.out.println("3. Пополнить баланс");
				System.out.println("4. Выйти");
				System.out.print("Введите номер операции: ");
				int choice = scanner.nextInt();

				switch (choice) {
					case 1 -> {
						final double balance = accountService.getBalance(cardNumber);
						System.out.printf("Текущий баланс: %.2f%n", balance);
					}
					case 2 -> {
						System.out.print("Введите сумму для снятия: ");
						double amount = scanner.nextDouble();
						accountService.withdraw(cardNumber, amount);
					}
					case 3 -> {
						System.out.print("Введите сумму для пополнения: ");
						final double amount = scanner.nextDouble();
						accountService.deposit(cardNumber, amount);
					}
					case 4 -> {
						accountService.saveData();
						System.out.println("Выход из системы.");
						return;
					}
					default -> System.out.println("Неверный выбор. Попробуйте снова.");
				}
			}
		}
	}
}
