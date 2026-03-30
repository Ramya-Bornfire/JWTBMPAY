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

import com.bornfire.entity.DeviceManagementEntity;
import com.bornfire.entity.EncryptionEntity;

@Service
public class DeviceManagementService {
	private static final Logger logger = LoggerFactory.getLogger(DeviceManagementService.class);

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	Environment env;
	
	private static final String GENERIC_CREATE_FAILURE = "Device creation failed. Please try again.";
	private static final String GENERIC_UPDATE_FAILURE = "Device update failed. Please try again.";
	private static final String GENERIC_VERIFY_FAILURE = "Device verification failed. Please try again.";
	private static final String GENERIC_DELETE_FAILURE = "Device deletion failed. Please try again.";

	public List<DeviceManagementEntity> getAllDeviceDetails(String merchantId, String unitId) {
		String url = env.getProperty("intranetconnection") + "/api/AllDeviceList?merchant_user_id=" + merchantId
				+ "&unit_id=" + unitId;
		DeviceManagementEntity[] response = restTemplate.getForObject(url, DeviceManagementEntity[].class);
		if (response == null) {
			return Collections.emptyList();
		}
		return Arrays.asList(response);
	}

	// Merchant wise for Device Management list
	public List<DeviceManagementEntity> getAllMerchantDevice(String merchantId) {
		String url = env.getProperty("intranetconnection") + "/api/DeviceManagementList?merchant_user_id=" + merchantId;
		DeviceManagementEntity[] response = restTemplate.getForObject(url, DeviceManagementEntity[].class);
		if (response == null) {
			return Collections.emptyList();
		}
		return Arrays.asList(response);
	}

	// unit wise for Device Management list
	public List<DeviceManagementEntity> getAllUnitDevice(String merchantId, String unitId) {
		String url = env.getProperty("intranetconnection") + "/api/GetAllUnitDeviceList?merchant_user_id=" + merchantId
				+ "&unit_id=" + unitId;
		DeviceManagementEntity[] response = restTemplate.getForObject(url, DeviceManagementEntity[].class);
		if (response == null) {
			return Collections.emptyList();
		}
		return Arrays.asList(response);
	}

	public String getcheckDeviceDetailsService(String device_id) {
		String url = env.getProperty("intranetconnection") + "/api/CheckDeviceId?device_id=" + device_id;
		String response = restTemplate.getForObject(url, String.class);
		return response;
	}

	// Create Device Service
	public String CreateDevice(EncryptionEntity EncryptedString, String psuDeviceID) {
		String url = env.getProperty("intranetconnection") + "/api/AddDevice";
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("PSU_Device_ID", psuDeviceID);
		HttpEntity<EncryptionEntity> requestEntity = new HttpEntity<>(EncryptedString, headers);
		try {
			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
			String responsedata = response.getBody();
			return responsedata;
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug("Device Not able to Create" + e.getMessage());
			return GENERIC_CREATE_FAILURE;
		}
	}

	// Update Device Details Service
	public String UpdateDeviceDetails(EncryptionEntity EncryptedString, String psuDeviceID) {
		String updateUrl = env.getProperty("intranetconnection") + "/api/UpdateDeviceData";
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("PSU_Device_ID", psuDeviceID);
		HttpEntity<EncryptionEntity> requestEntity = new HttpEntity<>(EncryptedString, headers);

		try {
			ResponseEntity<String> response = restTemplate.exchange(updateUrl, HttpMethod.POST, requestEntity,
					String.class);
			String getlogindata = response.getBody();
			if (getlogindata != null) {
				return getlogindata.toString();
			} else {
				return "Error updating Device: No response body";
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug("Merchant Representative profile--->Not able to update" + e.getMessage());
			return GENERIC_UPDATE_FAILURE;
		}
	}

	public String VerifyDeviceDetails(EncryptionEntity EncryptedString, String psuDeviceID) {
		String updateUrl = env.getProperty("intranetconnection") + "/api/VerifyDevicedetails";
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("PSU_Device_ID", psuDeviceID);
		HttpEntity<EncryptionEntity> requestEntity = new HttpEntity<>(EncryptedString, headers);

		try {
			ResponseEntity<String> response = restTemplate.exchange(updateUrl, HttpMethod.POST, requestEntity,
					String.class);
			String getlogindata = response.getBody();
			if (getlogindata != null) {
				return getlogindata.toString();
			} else {
				return "Error in verify Device: No response body";
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug("Merchant Representative profile--->Not able to verify" + e.getMessage());
			return GENERIC_VERIFY_FAILURE;
		}
	}

	public String DeleteDeviceDataService(String userid, String remark, String verifyuser) {
		String url = env.getProperty("intranetconnection") + "/api/DeleteDeviceData?deviceid=" + userid + "&remark="
				+ remark + "&verifyuser=" + verifyuser;

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
