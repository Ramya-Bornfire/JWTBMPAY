package com.bornfire.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bornfire.entity.EncryptionEntity;
import com.bornfire.entity.UserManagementEntity;
import com.bornfire.service.UserManagementService;

@RestController
@RequestMapping("/api")
public class UserManagementController {

	private static final Logger logger = LoggerFactory.getLogger(UserManagementController.class);

	@Autowired
	UserManagementService service;
	
	//Single User Management List
	@GetMapping("/AllUserManagementList")
	public List<UserManagementEntity> GetMerchantUnitAllUser(@RequestParam String merchant_user_id,
			@RequestParam String unit_id) {
		return service.getMerchantUnitAllUserService(merchant_user_id, unit_id);
	}

	// Merchant wise User Management List
	@GetMapping("/UserManagementList")
	public List<UserManagementEntity> GetAllUser(@RequestParam String merchant_user_id) {
		return service.getAllMerchantUser(merchant_user_id);
	}

	// Unit Wise User Management List
	@GetMapping("/UnitWiseUserManagementList")
	public List<UserManagementEntity> GetUnitAllUser(@RequestParam String merchant_user_id,
			@RequestParam String unit_id) {
		return service.getAllUnitUser(merchant_user_id, unit_id);
	}

	// Creation/Addition of new User
	@PostMapping("/AddUserData")
	public String postUserData(@RequestBody EncryptionEntity EncryptedString,
			@RequestHeader(value = "PSU_Device_ID", required = true) String psuDeviceID) throws Exception  {
		try {
			String response = service.CreateUser(EncryptedString, psuDeviceID);
			return response;
		} catch (Exception ex) {
			logger.debug("Failed to created Device---->" + ex.getMessage());
			return ex.getMessage();
		}
	}

	// Update existing user
	@RequestMapping(value = "UpdateUser", method = { RequestMethod.POST })
	public String UpdateExisitingUser(@RequestBody EncryptionEntity EncryptedString,
			@RequestHeader(value = "PSU_Device_ID", required = true) String psuDeviceID) throws Exception {
		try {
			String response = service.UpdateUserDetails(EncryptedString, psuDeviceID);
			return response;
		} catch (Exception e) {
			System.out.print("Exception-------" + e.getMessage());
			logger.debug("Failed to Update Device---->" + e.getMessage());
			return e.getMessage();
		}
	}
	// Update existing user
		@RequestMapping(value = "VerifyUserdetails", method = { RequestMethod.POST })
		public String VerifyExisitingUser(@RequestBody EncryptionEntity EncryptedString,
				@RequestHeader(value = "PSU_Device_ID", required = true) String psuDeviceID) throws Exception {
			return service.VerifyUserDetails(EncryptedString, psuDeviceID);
		}
		
		//Delete User
		@PostMapping("/DeleteUserData")
		public String DeleteUserData(@RequestParam String userid,@RequestParam String remark, @RequestParam String verifyuser) throws Exception {
			return service.DeleteUser(userid, remark,verifyuser);
		}
}
