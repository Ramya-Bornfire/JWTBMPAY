package com.bornfire.service;

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
import org.springframework.web.util.UriComponentsBuilder;

import com.bornfire.entity.EncryptionEntity;

@Service
public class LoginService {
	private static final Logger logger = LoggerFactory.getLogger(LoginService.class);

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	Environment env;

	// Update Representative Profile
	public String UpdateUserDetailsProfile(EncryptionEntity EncryptedString, String psuDeviceID) {
		String updateurl = env.getProperty("intranetconnection") + "/api/UpdateRepresentativeProfile";
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
			return e.getMessage();
		}
	}

	// Login for Tab Service
	public String RepLogin(EncryptionEntity EncryptedString, String psuDeviceID) {
		String url = env.getProperty("intranetconnection") + "/api/LoginForTab";
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("PSU_Device_ID", psuDeviceID);
		HttpEntity<EncryptionEntity> requestEntity = new HttpEntity<>(EncryptedString, headers);
		try {
			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
			return response.getBody();

		} catch (Exception e) {
			e.printStackTrace();
			logger.debug("Merchant Representative Login--->Not able to update" + e.getMessage());
			return e.getMessage();
		}
	}

	// Login for Android Service
		public String RepAndroidLogin(EncryptionEntity EncryptedString, String psuDeviceID) {
			String url = env.getProperty("intranetconnection") + "/api/LoginAndroid";
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.set("PSU_Device_ID", psuDeviceID);
			HttpEntity<EncryptionEntity> requestEntity = new HttpEntity<>(EncryptedString, headers);
			try {
				ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
				return response.getBody();

			} catch (Exception e) {
				e.printStackTrace();
				logger.debug("Merchant Representative Login--->Not able to update" + e.getMessage());
				return e.getMessage();
			}
		}

	// Logout for Tab Service
	public String Logoutfortab(EncryptionEntity EncryptedString, String psuDeviceID) {
		String url = env.getProperty("intranetconnection") + "/api/LogoutForTab";
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("PSU_Device_ID", psuDeviceID);
		HttpEntity<EncryptionEntity> requestEntity = new HttpEntity<>(EncryptedString, headers);
		try {
			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
			return response.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug("Merchant Representative Logout--->Not able to update" + e.getMessage());
			return e.getMessage();
		}
	}

	// Login for Mobile Service
	public String UserLogin(EncryptionEntity EncryptedString, String psuDeviceID) {
		String url = env.getProperty("intranetconnection") + "/api/LoginforMobile";
		HttpHeaders headers = new HttpHeaders();
		headers.set("PSU_Device_ID", psuDeviceID);
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<EncryptionEntity> requestEntity = new HttpEntity<>(EncryptedString, headers);
		try {
			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
			return response.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug("User Login--->Not able to update" + e.getMessage());
			return e.getMessage();
		}
	}

	// Logout for Mobile Service
	public String LogoutMobile(EncryptionEntity EncryptedString, String psuDeviceID) {
		String url = env.getProperty("intranetconnection") + "/api/LogoutforMobile";
		HttpHeaders headers = new HttpHeaders();
		headers.set("PSU_Device_ID", psuDeviceID);
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<EncryptionEntity> requestEntity = new HttpEntity<>(EncryptedString, headers);
		try {
			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
			return response.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug("User Logout--->Not able to update" + e.getMessage());
			return e.getMessage();
		}
	}

	// OTP for Mobile Service
	public String SentOTPforUser(String userid, String psuDeviceID) {
		String url = env.getProperty("intranetconnection") + "/api/OtpForUser?user_id=" + userid;
		HttpHeaders headers = new HttpHeaders();
		headers.set("PSU_Device_ID", psuDeviceID);
		HttpEntity<String> entity = new HttpEntity<>(headers);
		try {
		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
		return response.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug("OTP" + e.getMessage());
			return e.getMessage();
		}
	}

	// OTP for Tab Service
	public String SentOTPforMerchant(String userid, String psuDeviceID) {
		String url = env.getProperty("intranetconnection") + "/api/OtpForMerchant?merchant_rep_id=" + userid;
		HttpHeaders headers = new HttpHeaders();
		headers.set("PSU_Device_ID", psuDeviceID);
		HttpEntity<String> entity = new HttpEntity<>(headers);
		try {
		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
		return response.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug("OTP" + e.getMessage());
			return e.getMessage();
		}
	}
	
	// OTP for Tab Service
	public String SentOTPforAndroid(String userid, String psuDeviceID) {
		String url = env.getProperty("intranetconnection") + "/api/OtpForAndroid?merchant_rep_id=" + userid;
		HttpHeaders headers = new HttpHeaders();
		headers.set("PSU_Device_ID", psuDeviceID);
		HttpEntity<String> entity = new HttpEntity<>(headers);
		try {
		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
		return response.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug("OTP" + e.getMessage());
			return e.getMessage();
		}
	}

	// Reset password for Mobile Service
	public String ChangeUserPassword(EncryptionEntity EncryptedString, String psuDeviceID) {
		String url = env.getProperty("intranetconnection") + "/api/ResetUserPassword";
		HttpHeaders headers = new HttpHeaders();
		headers.set("PSU_Device_ID", psuDeviceID);
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<EncryptionEntity> requestEntity = new HttpEntity<>(EncryptedString, headers);
		try {
			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
			return response.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug("User Logout--->Not able to update" + e.getMessage());
			return e.getMessage();
		}
	}

	// Reset password for Tab Service
	public String ChangeMerchantPassword(EncryptionEntity EncryptedString, String psuDeviceID) {
		String url = env.getProperty("intranetconnection") + "/api/ResetMerchantPassword";
		HttpHeaders headers = new HttpHeaders();
		headers.set("PSU_Device_ID", psuDeviceID);
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<EncryptionEntity> requestEntity = new HttpEntity<>(EncryptedString, headers);
		try {
			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
			return response.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug("User Logout--->Not able to update" + e.getMessage());
			return e.getMessage();
		}
	}

	
	// Reset password for Mobile Service
	public String ChangeUserNewPassword(EncryptionEntity EncryptedString, String psuDeviceID) {
		String url = env.getProperty("intranetconnection") + "/api/ResetUserNewPassword";
		HttpHeaders headers = new HttpHeaders();
		headers.set("PSU_Device_ID", psuDeviceID);
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<EncryptionEntity> requestEntity = new HttpEntity<>(EncryptedString, headers);
		try {
			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
			return response.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug("User Logout--->Not able to update" + e.getMessage());
			return e.getMessage();
		}
	}

	// Reset password for Tab Service
	public String ChangeMerchantNewPassword(EncryptionEntity EncryptedString, String psuDeviceID) {
		String url = env.getProperty("intranetconnection") + "/api/ResetMerchantNewPassword";
		HttpHeaders headers = new HttpHeaders();
		headers.set("PSU_Device_ID", psuDeviceID);
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<EncryptionEntity> requestEntity = new HttpEntity<>(EncryptedString, headers);
		try {
			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
			return response.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug("User Logout--->Not able to update" + e.getMessage());
			return e.getMessage();
		}
	}
	
	// Lock Password Check for Tab Service
	public String CheckPasswordForTab(String userid, String password, String psuDeviceID) {
		String url = env.getProperty("intranetconnection") + "/api/CheckPasswordForTab?merchant_id=" + userid
				+ "&password=" + password;
		HttpHeaders headers = new HttpHeaders();
		headers.set("PSU_Device_ID", psuDeviceID);
		HttpEntity<String> entity = new HttpEntity<>(headers);
		try {
		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
		return response.getBody();
	} catch (Exception e) {
		e.printStackTrace();
		logger.debug("ChangePassword" + e.getMessage());
		return e.getMessage();
	}
	}

	// Lock Password Check for Mobile Service
	public String CheckPasswordForMobile(String userid, String password, String psuDeviceID) {
	
		String url = env.getProperty("intranetconnection") + "/api/CheckPasswordForMobile?merchant_id=" + userid
				+ "&password=" + password;
		HttpHeaders headers = new HttpHeaders();
		headers.set("PSU_Device_ID", psuDeviceID);
		HttpEntity<String> entity = new HttpEntity<>(headers);
		try {
		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
		return response.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug("ChangePassword" + e.getMessage());
			return e.getMessage();
		}
	}
	
	// Lock Password Check for Mobile Service
	public String TwoFa(String userId, int answerNumber, String answer, String psuDeviceID) {
	    try {
	        HttpHeaders headers = new HttpHeaders();
	        headers.set("PSU_Device_ID", psuDeviceID);
	        headers.setContentType(MediaType.APPLICATION_JSON);
	        String url = env.getProperty("intranetconnection") + "/api/CheckTwoFactorAnswer?userId="+userId+"&answer="+answer+"&answerNumber="+answerNumber;
	        HttpEntity<String> entity = new HttpEntity<>(headers);
	        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
	        return response.getBody();
	    } catch (Exception e) {
	        logger.error("TwoFA Error: {}", e.getMessage(), e);
	        return "Error: " + e.getMessage();
	    }
	}
	
	// Reset password for Tab Service
	public String RepAuthentication(EncryptionEntity EncryptedString, String psuDeviceID) {
		String url = env.getProperty("intranetconnection") + "/api/AuthenticationForRep";
		HttpHeaders headers = new HttpHeaders();
		headers.set("PSU_Device_ID", psuDeviceID);
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<EncryptionEntity> requestEntity = new HttpEntity<>(EncryptedString, headers);
		try {
			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
			return response.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug("Authentication message" + e.getMessage());
			return e.getMessage();
		}
	}


}
