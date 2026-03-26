package com.bornfire.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bornfire.entity.EncryptionEntity;
import com.bornfire.entity.RateMaintainanceEntity;
import com.bornfire.service.RateMaintanenceService;

@RestController
@RequestMapping("/api")
public class RateMaintanceController {

	private static final Logger logger = LoggerFactory.getLogger(RateMaintanceController.class);

	@Autowired
	RateMaintanenceService service;

	// All Rate Maintenance list
	@GetMapping("/RateMaintenanceList")
	public List<RateMaintainanceEntity> getAllratedetails() {
		return service.getAllrate();
	}

	// Creation/Addition of new rate
	@PostMapping("/AddNewRate")
	public String CreateRateData(@RequestBody EncryptionEntity EncryptedString,
			@RequestHeader(value = "PSU_Device_ID", required = true) String psuDeviceID) throws Exception   {
		try {
			String response = service.CreateRate(EncryptedString, psuDeviceID);
			return response;

		} catch (Exception ex) {
			logger.debug("Failed to create rate" + ex.getMessage());
			return ex.getMessage();
		}

	}

	// UpdateRateData
	@RequestMapping(value = "UpdateExistRate", method = { RequestMethod.PUT })
	public String UpdateRate(@RequestBody EncryptionEntity EncryptedString,
			@RequestHeader(value = "PSU_Device_ID", required = true) String psuDeviceID) throws Exception  {
		try {
			String response = service.UpdateRateDetails(EncryptedString, psuDeviceID);
			return response;
		} catch (Exception e) {
			System.out.print("Exception------->" + e.getMessage());
			logger.debug("Failed to update rate" + e.getMessage());
			return e.getMessage();
		}

	}

}
