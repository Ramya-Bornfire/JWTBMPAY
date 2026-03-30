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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bornfire.entity.DeviceManagementEntity;
import com.bornfire.entity.EncryptionEntity;
import com.bornfire.service.DeviceManagementService;

@RestController
@RequestMapping("/api")
public class DeviceManagementController {
	private static final Logger logger = LoggerFactory.getLogger(DeviceManagementController.class);

	@Autowired
	DeviceManagementService deviceService;
	
	private static final String GENERIC_CREATE_FAILURE = "Device creation failed. Please try again.";
	private static final String GENERIC_UPDATE_FAILURE = "Device update failed. Please try again.";
	private static final String GENERIC_DELETE_FAILURE = "Device deletion failed. Please try again.";
	private static final String GENERIC_VERIFY_FAILURE = "Device verification failed. Please try again.";

	
	
	@GetMapping("/AllDeviceList")
	public List<DeviceManagementEntity> getAllDevice(@RequestParam String merchant_user_id,
			@RequestParam String unit_id) {
		return deviceService.getAllDeviceDetails(merchant_user_id, unit_id);
	}

	// Device Management list
	@GetMapping("/DeviceManagementList")
	public List<DeviceManagementEntity> getAllDeviceDetails(@RequestParam String merchant_user_id) {
		return deviceService.getAllMerchantDevice(merchant_user_id);
	}

	// Unit Device Management List
	@GetMapping("/GetAllUnitDeviceList")
	public List<DeviceManagementEntity> getAllUnitDeviceDetails(@RequestParam String merchant_user_id,
			@RequestParam String unit_id) {
		return deviceService.getAllUnitDevice(merchant_user_id, unit_id);
	}

	// Creation/Addition of new device
	@PostMapping("/AddDevice")
	public String AddDeviceData(@RequestBody EncryptionEntity EncryptedString,
			@RequestHeader(value = "PSU_Device_ID", required = true) String psuDeviceID) throws Exception {
		try {
			
			String response = deviceService.CreateDevice(EncryptedString,psuDeviceID);
			return response;
		} catch (Exception ex) {
			logger.debug("Failed to created Device---->" + ex.getMessage());
			return GENERIC_CREATE_FAILURE;
		}

	}

	// Update the Existing Device
	@RequestMapping(value = "UpdateDeviceData", method = { RequestMethod.POST })
	public String UpdateDevice(@RequestBody EncryptionEntity EncryptedString,
			@RequestHeader(value = "PSU_Device_ID", required = true) String psuDeviceID) throws Exception  {
		try {
			String response = deviceService.UpdateDeviceDetails(EncryptedString,psuDeviceID);
			return response;
		} catch (Exception e) {
			System.out.print("Exception-------" + e.getMessage());
			logger.debug("Failed to Update Device---->" + e.getMessage());
			Map<String, String> response = new HashMap<>();
			response.put("Status", "Failed");
			response.put("Message", "Not Updating");
			return GENERIC_UPDATE_FAILURE;
		}

	}
	// CheckDeviceID
		@GetMapping("/CheckDeviceId")
		public String getCheckDeviceDetails(@RequestParam String device_id) {
			return deviceService.getcheckDeviceDetailsService(device_id);
		}
	@RequestMapping(value = "VerifyDevicedetails", method = { RequestMethod.POST })
	public String VerifyExisitingUser(@RequestBody EncryptionEntity EncryptedString,
			@RequestHeader(value = "PSU_Device_ID", required = true) String psuDeviceID) throws Exception {
		return deviceService.VerifyDeviceDetails(EncryptedString, psuDeviceID);
	}

	@PostMapping("/DeleteDeviceData")
	public String DeleteUserData(@RequestParam String deviceid,@RequestParam String remark, @RequestParam String verifyuser) throws Exception {
		return deviceService.DeleteDeviceDataService(deviceid, remark,verifyuser);
	}
}
