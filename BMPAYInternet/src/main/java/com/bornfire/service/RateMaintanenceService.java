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
import com.bornfire.entity.RateMaintainanceEntity;

@Service
public class RateMaintanenceService {
	private static final Logger logger = LoggerFactory.getLogger(DeviceManagementService.class);

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	Environment env;

	// Rate Maintenance list
	public List<RateMaintainanceEntity> getAllrate() {
		String url = env.getProperty("intranetconnection") + "/api/RateMaintenanceList";
		RateMaintainanceEntity[] response = restTemplate.getForObject(url, RateMaintainanceEntity[].class);
		if (response == null) {
			return Collections.emptyList();
		}
		return Arrays.asList(response);
	}

	// Create Rate Service
	public String CreateRate(EncryptionEntity EncryptedString, String psuDeviceID) {
		String url = env.getProperty("intranetconnection") + "/api/AddNewRate";
		HttpHeaders headers = new HttpHeaders();
		headers.set("PSU_Device_ID", psuDeviceID);
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<EncryptionEntity> requestEntity = new HttpEntity<>(EncryptedString, headers);
		try {
			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
			return response.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug("Device Not able to Create" + e.getMessage());
			return "Error in creating Device";
		}
	}

	// Update Rate Details Service
	public String UpdateRateDetails(EncryptionEntity EncryptedString, String psuDeviceID) {
		String updateurl = env.getProperty("intranetconnection") + "/api/UpdateExistRate";
		HttpHeaders headers = new HttpHeaders();
		headers.set("PSU_Device_ID", psuDeviceID);
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<EncryptionEntity> requestEntity = new HttpEntity<>(EncryptedString, headers);
		try {
			ResponseEntity<String> response = restTemplate.exchange(updateurl, HttpMethod.PUT, requestEntity,
					String.class);
			return response.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug("Merchant Representative profile--->Not able to update" + e.getMessage());
			return "Error updating Representative Profile";
		}
	}
}
