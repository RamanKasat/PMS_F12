package com.cts.pensionerDetails.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.cts.pensionerDetails.Exception.NotFoundException;
import com.cts.pensionerDetails.Model.BankDetails;
import com.cts.pensionerDetails.Model.PensionerDetails;
import com.cts.pensionerDetails.Util.DateUtil;

import lombok.extern.slf4j.Slf4j;

/**
 *		Service class with implementations of PensionerDetailService
 *		Has method to get Pensioner details by aadhaar number
 */
@Service
@Slf4j
public class PensionerDetailServiceImpl implements PensionerDetailService {

	@Value("${errorMessage}")
	private String AADHAAR_NUMBER_NOT_FOUND;

	/**
	 * Loads pensioner from the CSV based on Aadhaar.
	 *
	 * @param aadhaarNumber .
	 * @return Pensioner Details if Aadhaar number is found
	 */
	public PensionerDetails getPensionerDetailByAadhaar(String aadhaarNumber) {

		String line = "";
		BufferedReader br = new BufferedReader(
				new InputStreamReader(Objects.requireNonNull(this.getClass().getResourceAsStream("/details.csv"))));
		try {
			while ((line = br.readLine()) != null) // returns a Boolean value
			{
				String[] pensionerDetail = line.split(",");

				if (aadhaarNumber.contentEquals(pensionerDetail[0])) {
					log.info("Details found");
					return new PensionerDetails
							(
								pensionerDetail[1],
								pensionerDetail[0],
								DateUtil.parseDate(pensionerDetail[2]),
								pensionerDetail[3],
								Double.parseDouble(pensionerDetail[4]),
								Double.parseDouble(pensionerDetail[5]),
								pensionerDetail[6],
								Long.parseLong(pensionerDetail[8]),
									new BankDetails(
										pensionerDetail[7],
										Long.parseLong(pensionerDetail[8]),
										pensionerDetail[9]
								)
							);
				}
			}
		} catch (NumberFormatException | IOException | ParseException e) {
			throw new NotFoundException(AADHAAR_NUMBER_NOT_FOUND);
		}
		throw new NotFoundException(AADHAAR_NUMBER_NOT_FOUND);
	}

}
