package com.atmmachine.impl;

import com.atmmachine.AtmRepository;
import com.atmmachine.AtmService;

public class AtmServiceImpl implements AtmService {

	private final AtmRepository atmRepository = AtmRepositoryImpl.getInstance();

	@Override
	public double getBalance() {
		return atmRepository.getBalance();
	}

	@Override
	public void setBalance(double balance) {
		atmRepository.setBalance(balance);
	}
}
