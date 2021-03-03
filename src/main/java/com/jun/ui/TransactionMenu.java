package com.jun.ui;

import java.sql.SQLException;

import com.jun.exceptions.CardNotFoundException;
import com.jun.exceptions.InvalidAccountException;
import com.jun.exceptions.InvalidBalanceException;
import com.jun.exceptions.InvalidTransferRequestException;
import com.jun.services.AccountService;
import com.jun.services.TransactionService;

public class TransactionMenu implements Menu {

	public String cardNum;
	public String transactionType;
	public double balance;
	public int userId;
	public TransactionService transactionService;
	public AccountService cardService;
	
	public TransactionMenu(String cardNum, String transactionType, double balance, int userId) {
		this.cardNum = cardNum;
		this.transactionType = transactionType;
		this.balance = balance;
		this.userId = userId;
		this.transactionService = new TransactionService();
		this.cardService = new AccountService();
	}

	@Override
	public void display() {
		
		int choice = 0;
		
		do {
			System.out.println("1.) Exit");
			System.out.println("Current balance is: " + String.valueOf(balance));
			System.out.println("Please input the amount to " + transactionType.toLowerCase());
			double amount = Double.parseDouble(Menu.sc.nextLine());
			switch (choice) {
				case 1: 
					break;
				default: 
					if (balance < amount) {
						System.out.println("There is not enough balance on this account to make this transfer");
					} else { 
						if(transactionType != "Transfer") {
							try {
								transactionService.updateBalance(cardNum, transactionType, amount);
								try {
									System.out.println("the updated balance is : " + cardService.getAccountInfo(cardNum, userId).getBalance());
									AccountMenu am = new AccountMenu(cardNum, userId);
									am.display();
								} catch (CardNotFoundException e) {
									e.printStackTrace();
								}
							} catch (SQLException | NumberFormatException | InvalidBalanceException e) {
								System.out.println(e.getMessage());
							} 
						} else {
							try {
								//check if valid account somewhere?
								System.out.println("Please enter account number that you want to transfer to");
								String toAcc = Menu.sc.nextLine();
								String transfer = transactionService.transferBalanceToAccount(userId, toAcc, cardNum, amount);
								System.out.println(transfer);
								AccountMenu am = new AccountMenu(cardNum, userId);
								am.display();
							} catch (SQLException | InvalidBalanceException | InvalidTransferRequestException | InvalidAccountException e) {
								System.out.println(e.getMessage());
							} 
						}
					}
				}
				break;
		} while (choice != 1);
	}

}
