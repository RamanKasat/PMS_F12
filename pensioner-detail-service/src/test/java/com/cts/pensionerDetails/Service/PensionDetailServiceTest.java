package com.cts.pensionerDetails.Service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.text.ParseException;

//import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import com.cts.pensionerDetails.Exception.NotFoundException;
import com.cts.pensionerDetails.Model.BankDetails;
import com.cts.pensionerDetails.Model.PensionerDetails;
import com.cts.pensionerDetails.Util.DateUtil;

/**
 *         This is the service test class
 * 
 *         Test cases for the Pension Detail Service
 *
 */
@SpringBootTest
class PensionDetailServiceTest {

	@Autowired
	PensionerDetailService pds;

	@Value("${errorMessage}")
	private String AADHAAR_NUMBER_NOT_FOUND;

	/**
	 * Test the Pensioner Detail service is null or not
	 */

	@Test
	void testNotNullPensionDetailServiceObject() {
		assertNotNull(pds,"Pension Details Service Object is null");
	}

	/**
	 * Test Case for the Correct Details Returned From Service With Correct
	 * AadharNumber
	 */

	@Test
	void testCorrectDetailsReturnedFromServiceWithCorrectAadharNumber()
			throws IOException, NotFoundException, NumberFormatException,
			com.cts.pensionerDetails.Exception.NotFoundException, ParseException, NullPointerException {
		final String aadhaarNumber = "123456789012";

		PensionerDetails pensionerDetail = new PensionerDetails("Vishnu","123456789012", DateUtil.parseDate("14-09-1999"),
				"BRPPV3218K", 27000, 10000, "self",12345678, new BankDetails("ICICI", 12345678, "private"));
		assertEquals(pds.getPensionerDetailByAadhaar(aadhaarNumber).getPan(), pensionerDetail.getPan());
		assertEquals(pds.getPensionerDetailByAadhaar(aadhaarNumber).getBank().getAccountNumber(), pensionerDetail.getBank().getAccountNumber());
	}

	/**
	 * Test Case for the incorrect details returned From Service With Invalid
	 * Aadhaar Number
	 */

	@Test
	void testForIncorrectAadharNumber() {
		final String aadhaarNumber = "12345678";

		NotFoundException exception = assertThrows(NotFoundException.class,
				() -> pds.getPensionerDetailByAadhaar(aadhaarNumber));
		assertEquals(exception.getMessage(), AADHAAR_NUMBER_NOT_FOUND);
		assertNotNull(exception);
	}
}
