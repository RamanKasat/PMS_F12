package com.cts.pensionerDetails.Controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.hamcrest.Matchers;
//import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.cts.pensionerDetails.Exception.NotFoundException;
import com.cts.pensionerDetails.Model.BankDetails;
import com.cts.pensionerDetails.Model.PensionerDetails;
import com.cts.pensionerDetails.Service.PensionerDetailServiceImpl;
import com.cts.pensionerDetails.Util.DateUtil;

/**
 * Test cases for the pensioner Details controller
 *
 */
@WebMvcTest
class PensionDetailsControllerTest {
	
	@Value("${errorMessage}")
	private String AADHAAR_NUMBER_NOT_FOUND;
	
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private PensionerDetailServiceImpl service;
	
	/**
	 * Test Case for test To Get Correct Pensioner Details From Controller
	 * 
	 * @throws Exception .
	 */
	@Test
	void testToGetCorrectPensionerDetailsFromController() throws Exception {

		final String aadhaarNumber = "123456789012";
		PensionerDetails pensionerDetail = new PensionerDetails
				(
						"Vishnu",
						"123456789012",
						DateUtil.parseDate("14-09-1999"),
						"BRPPV3218K",
						27000,
						10000,
						"self",
						12345678,
						new BankDetails
								(
										"ICICI",
										12345678,
										"private"
								)
				);
		when(service.getPensionerDetailByAadhaar(aadhaarNumber)).thenReturn(pensionerDetail);
		mockMvc.perform(get("/pensionerDetailByAadhaar/{aadhaarNumber}", aadhaarNumber)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name", Matchers.equalTo("Vishnu")))
				.andExpect(jsonPath("$.aadhaarNumber", Matchers.equalTo("123456789012")))
				.andExpect(jsonPath("$.dateOfBirth", Matchers.equalTo("1999-09-14")))
				.andExpect(jsonPath("$.pan",Matchers.equalTo("BRPPV3218K")))
				.andExpect(jsonPath("$.salary",Matchers.equalTo(27000.0)))
				.andExpect(jsonPath("$.allowance",Matchers.equalTo(10000.0)))
				.andExpect(jsonPath("$.pensionType",Matchers.equalTo("self")))
				.andExpect(jsonPath("$.bank.bankName",Matchers.equalTo("ICICI")))
				.andExpect(jsonPath("$.bank.accountNumber",Matchers.equalTo(12345678)))
				.andExpect(jsonPath("$.bank.bankType",Matchers.equalTo("private")));
	}

	/**
	 * Test Case for the Aadhaar Number Not In CSV File
	 * @throws Exception .
	 */
	@Test
	void testForAadharNumberNotInCsvFile() throws Exception {
		
		final String aadhaarNumber = "12345678888";

		when(service.getPensionerDetailByAadhaar(ArgumentMatchers.any()))
				.thenThrow(new NotFoundException(AADHAAR_NUMBER_NOT_FOUND));
				
		mockMvc.perform(get("/pensionerDetailByAadhaar/{aadhaarNumber}", aadhaarNumber)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().is4xxClientError())
				.andExpect(jsonPath("$.message", Matchers.equalTo(AADHAAR_NUMBER_NOT_FOUND)));
	}

}
