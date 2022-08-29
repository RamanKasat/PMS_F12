package com.cts.pensionerDetails.Model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * This is BankDetail class which contains the Bank details
 * like bankName, bankType, accountNumber
 *
 */

@AllArgsConstructor
@Getter
@ToString
public class BankDetails {
	private String bankName;
	private long accountNumber;
	private String bankType;
}
