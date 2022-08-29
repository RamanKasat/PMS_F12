package com.cts.pensionerDetails.Service;

import com.cts.pensionerDetails.Model.PensionerDetails;

/**
 * Interface providing implementations for PensionerDetailService
 * 
 */
public interface PensionerDetailService {

	public PensionerDetails getPensionerDetailByAadhaar(String aadhaarNumber);

}
