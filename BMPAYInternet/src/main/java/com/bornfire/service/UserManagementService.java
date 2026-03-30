package com.bornfire.service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.bornfire.entity.EncryptionEntity;
import com.bornfire.entity.UserManagementEntity;

@Service
public class UserManagementService {
	private static final Logger logger = LoggerFactory.getLogger(DeviceManagementService.class);

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	Environment env;
		
	private static final String GENERIC_CREATE_FAILURE = "User creation failed. Please try again.";
	private static final String GENERIC_UPDATE_FAILURE = "User update failed. Please try again.";
	private static final String GENERIC_VERIFY_FAILURE = "User verification failed. Please try again.";
	private static final String GENERIC_DELETE_FAILURE = "User deletion failed. Please try again.";


	public List<UserManagementEntity> getMerchantUnitAllUserService(String merchantId, String unitId) {
		String url = env.getProperty("intranetconnection") + "/api/AllUserManagementList?merchant_user_id="
				+ merchantId + "&unit_id=" + unitId;
		UserManagementEntity[] response = restTemplate.getForObject(url, UserManagementEntity[].class);
		if (response == null) {
			return Collections.emptyList();
		}
		return Arrays.asList(response);
	}

	// Merchant wise for Device Management list
	public List<UserManagementEntity> getAllMerchantUser(String merchantId) {
		String url = env.getProperty("intranetconnection") + "/api/UserManagementList?merchant_user_id=" + merchantId;
		UserManagementEntity[] response = restTemplate.getForObject(url, UserManagementEntity[].class);
		if (response == null) {
			return Collections.emptyList();
		}
		return Arrays.asList(response);
	}

	// unit wise for Device Management list
	public List<UserManagementEntity> getAllUnitUser(String merchantId, String unitId) {
		String url = env.getProperty("intranetconnection") + "/api/UnitWiseUserManagementList?merchant_user_id="
				+ merchantId + "&unit_id=" + unitId;
		UserManagementEntity[] response = restTemplate.getForObject(url, UserManagementEntity[].class);
		if (response == null) {
			return Collections.emptyList();
		}
		return Arrays.asList(response);
	}

	// Create Device Service
	public String CreateUser(EncryptionEntity EncryptedString, String psuDeviceID) {
		String url = env.getProperty("intranetconnection") + "/api/AddUserData";
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("PSU_Device_ID", psuDeviceID);
		HttpEntity<EncryptionEntity> requestEntity = new HttpEntity<>(EncryptedString, headers);
		try {
			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
			return response.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug("Device Not able to Create" + e.getMessage());
			return GENERIC_CREATE_FAILURE;
		}
	}

	// Update Device Details Service
	public String UpdateUserDetails(EncryptionEntity EncryptedString, String psuDeviceID) {
		String updateurl = env.getProperty("intranetconnection") + "/api/UpdateUser";
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("PSU_Device_ID", psuDeviceID);
		HttpEntity<EncryptionEntity> requestEntity = new HttpEntity<>(EncryptedString, headers);
		try {
			ResponseEntity<String> response = restTemplate.exchange(updateurl, HttpMethod.POST, requestEntity,
					String.class);
			return response.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug("Merchant Representative profile--->Not able to update" + e.getMessage());
			return GENERIC_UPDATE_FAILURE;
		}
	}
	
	public String VerifyUserDetails(EncryptionEntity EncryptedString, String psuDeviceID) {
		String updateurl = env.getProperty("intranetconnection") + "/api/VerifyUserdetails";
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("PSU_Device_ID", psuDeviceID);
		HttpEntity<EncryptionEntity> requestEntity = new HttpEntity<>(EncryptedString, headers);
		try {
			ResponseEntity<String> response = restTemplate.exchange(updateurl, HttpMethod.POST, requestEntity,
					String.class);
			return response.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug("Merchant Representative profile--->Not able to update" + e.getMessage());
			return GENERIC_VERIFY_FAILURE;
		}
	}
	
	public String DeleteUser(String userid, String remark, String verifyuser) {
        String url = env.getProperty("intranetconnection") + "/api/DeleteUserData?userid=" + userid
                     + "&remark=" + remark
                     + "&verifyuser=" + verifyuser;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
            return response.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            logger.debug("User Not able to Delete" + e.getMessage());
            return GENERIC_DELETE_FAILURE;
        }
    }
}
