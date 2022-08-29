package com.cts.pensionerDetails.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.cts.pensionerDetails.Model.PensionerDetails;
import com.cts.pensionerDetails.Service.PensionerDetailServiceImpl;

import lombok.extern.slf4j.Slf4j;

/**
 *         Pensioner Details Controller is to get the details of pensioner by
 *         passing the Aadhaar Number
 *
 */
@RestController
@Slf4j
@CrossOrigin
public class PensionerDetailsController {

	@Autowired
	private PensionerDetailServiceImpl pensionerDetailService;

	/**
	 * @URL: http://localhost:8083/pensionerDetailByAadhaar/123456789012 .
	 * 
	 * @return if Aadhaar Number then return the pensioner details else throws
	 *         Exception
	 *
	 * @Expceted: {
					  "aadhar": "123456789011",
					  "name": "Vishnu",
					  "dateOfBirth": "1999-09-14T18:30:00.000+00:00",
					  "pan": "BRRPPV3218K",
					  "salary": 32000,
					  "allowance": 10000,
					  "pensionType": "self",
	                  "accountNumber": 12345678,
					  "bank": {
						"bankName": "SBI",
						"accountNumber": 12345678,
						"bankType": "private"
					  }
				   }
	 *
	 */

	@GetMapping("/pensionerDetailByAadhaar/{aadhaarNumber}")
	public PensionerDetails getPensionerDetailByAadhaar(@PathVariable String aadhaarNumber) {
		log.info("START - getPensionerDetailByAadhaar()");
		return pensionerDetailService.getPensionerDetailByAadhaar(aadhaarNumber);
	}

}
