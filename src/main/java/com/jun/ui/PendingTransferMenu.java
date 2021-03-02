package com.jun.ui;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import com.jun.model.PendingTransfer;
import com.jun.services.TransactionService;

public class PendingTransferMenu implements Menu{

	public TransactionService transactionService;
	public PendingTransferMenu() {
		this.transactionService = new TransactionService();
	}

	@Override
	public void display() {
		System.out.println("=== Pending Transfers ===");
		System.out.println("Please select a pending transfer to review");
		ArrayList<PendingTransfer> pendingTransfers = new ArrayList<>();
		int choice = 0;
		do {
			try {
				pendingTransfers = transactionService.checkPendingTransfer(MainMenu.loginId);
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
			StringBuilder sb = new StringBuilder();
			sb.append("1.)Exit");
			sb.append(System.getProperty("line.separator"));
			//makes sure pendingtransfer actually has a list
			if (pendingTransfers.size() > 0) {
				//lambda expr
				pendingTransfers.forEach(withCounter((i, pt) -> {
					String fromAccount = pt.getFromAccountId();
					double amount = pt.getAmount();
					sb.append((i+2) + ".)" + "From account: " + fromAccount + "amount: " + amount);
					sb.append(System.getProperty("line.separator"));
				}));
				System.out.println(sb);
			try {
				choice = Integer.parseInt(Menu.sc.nextLine());
			} catch (NumberFormatException e) {};
			
			} else {
				System.out.println("There are no pending applications");
				choice = 1;
			}
			switch (choice) {
				case 1:
					break;
				default:
					int selected = 0;
					System.out.println("From account: " + pendingTransfers.get(choice - 2).getFromAccountId());
					System.out.println("Amount transfering into your account: " + pendingTransfers.get(choice - 2).getAmount());
					System.out.println("1.) Exit");
					System.out.println("2.) Approve");
					System.out.println("3.) Deny");
					try {
						selected = Integer.parseInt(Menu.sc.nextLine());
					}catch(NumberFormatException e) {};
					
					switch(selected) {
						case 1:
							break;
						case 2:
							System.out.println("approved");
							selected = 1;
							break;
						case 3:
							System.out.println("declined");
							selected = 1;
							break;
					} while(selected != 1);
			}
		} while(choice != 1);
	}
	//create consumer to keep track of counter and pass to a biconsumer
	//returns a new lambda, uses atomicinteger to keep track of counter and increments on each new item
	private static <T>Consumer<T> withCounter(BiConsumer<Integer, T> consumer) {
	    AtomicInteger counter = new AtomicInteger(0);
	    return item -> consumer.accept(counter.getAndIncrement(), item);
	}

}