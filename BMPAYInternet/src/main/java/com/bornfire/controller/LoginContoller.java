package com.bornfire.controller;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.bornfire.entity.EncryptionEntity;
import com.bornfire.entity.InfoTableEntity;
import com.bornfire.security.JwtUtil;
import com.bornfire.service.LoginService;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

@RestController
@RequestMapping("/api")
public class LoginContoller {

	private static final Logger logger = LoggerFactory.getLogger(LoginContoller.class);

	@Autowired
	LoginService loginService;
	
	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	Environment env;
	
	@GetMapping("/ws/infoForEveryScreen")
	public ResponseEntity<?> getInfo(@RequestParam String screen_id) {
	    String url = env.getProperty("intranetconnection") + "/api/ws/infoForEveryScreen?screen_id=" + screen_id;
	    InfoTableEntity response = restTemplate.getForObject(url, InfoTableEntity.class);

	    if (response == null) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No data found for this screen ID");
	    }else {
	    	 return ResponseEntity.ok(response);
	    }
	}

	// Update user profile of representative
	@PostMapping("UpdateRepresentativeProfile")
	public String UpdateRepDetails(@RequestBody EncryptionEntity EncryptedString,
			@RequestHeader(value = "PSU_Device_ID", required = true) String psuDeviceID) throws Exception {
		String response = loginService.UpdateUserDetailsProfile(EncryptedString,psuDeviceID);
		return response;
	}

//	// Login for Tab
//	@PostMapping("LoginAndroid")
//	public String LoginAndroid(@RequestBody EncryptionEntity EncryptedString,
//			@RequestHeader(value = "PSU_Device_ID", required = true) String psuDeviceID) throws Exception {
//		try {
//			String response = loginService.RepAndroidLogin(EncryptedString,psuDeviceID);
//			String userId = extractUserId(response);
//
//			if (userId != null) {
//	            // ✅ Generate JWT with ANDROID_ID (not random PSU_Device_ID)
//	            String token = JwtUtil.generateToken(userId, psuDeviceID);
//	            return response + ",token=" + token; // Your existing format
//	        }
//	        
//	        return response;
//		} catch (Exception e) {
//			logger.debug("Login for TAB Exception: " + e.getMessage());
//			System.out.println("Exception: " + e.getMessage());
//			return  e.getMessage();
//		}
//	}
//	private String extractUserId(String response) {
//	    if (response == null) return null;
//
//	    try {
//	        String[] parts = response.split(",");
//
//	        for (String part : parts) {
//	            String[] keyValue = part.split("=");
//
//	            if (keyValue.length == 2 && keyValue[0].trim().equals("merchant_user_id")) {
//	                return keyValue[1].trim();
//	            }
//	        }
//	    } catch (Exception e) {
//	        e.printStackTrace();
//	    }
//
//	    return null;
//	}
	@PostMapping("LoginAndroid")
	public ResponseEntity<String> LoginAndroidTab(
	        @RequestBody EncryptionEntity request,
	        @RequestHeader(value = "PSU_Device_ID", required = true) String psuDeviceID) {
	    
	    try {
	        // 🔥 YOUR EXISTING BIPS CALL (KEEP IT)
	        String bipsportalResponse = loginService.RepAndroidLogin(request, psuDeviceID);
	        
	        // 🔥 EXTRACT USERID from BIPS response (no decryption needed here)
	        String userId = extractUserIdFromEncrypted(bipsportalResponse);
	        
	        if (userId != null && !userId.trim().isEmpty()) {
	            // ✅ Generate JWT with STABLE Android ID
	            String jwtToken = JwtUtil.generateToken(userId, userId, psuDeviceID);
	            
	            // 🔥 RETURN PLAIN JSON - NO ENCRYPTION!
	            String plainJsonResponse = String.format(
	                "{\"status\":\"Success\",\"message\":\"%s\",\"token\":\"%s\"}",
	                bipsportalResponse.replace("\"", "\\\""),  // Keep BIPS data as-is
	                jwtToken
	            );
	            return ResponseEntity.ok(plainJsonResponse);  // ← PLAIN TEXT!
	        }
	        
	        // Fallback: return original BIPS response as plain text
	        return ResponseEntity.ok(bipsportalResponse);
	        
	    } catch (Exception e) {
	        logger.error("LoginAndroid failed", e);
	        return ResponseEntity.status(500).body(
	            "{\"status\":\"Failed\",\"message\":\"Server Error\"}"
	        );
	    }
	}
	// ✅ Method 1: Check if login was successful
	private boolean isLoginSuccess(String response) {
	    if (response == null) return false;
	    String lower = response.toLowerCase();
	    return lower.contains("success") || 
	           lower.contains("merchant_user_id") || 
	           lower.contains("merchant_rep_id") ||
	           lower.contains("loginentity") ||
	           !lower.contains("error") && 
	           !lower.contains("failed");
	}

	// ✅ Method 2: Extract user ID from BIPS response
	private String extractUserIdFromEncrypted(String response) {
	    if (response == null) return null;
	    try {
	        Pattern pattern = Pattern.compile("(?i)(merchant_?user_?id|user_?id|merchant_?rep_?id)=(\\d+)", 
	                                         Pattern.CASE_INSENSITIVE);
	        Matcher matcher = pattern.matcher(response);
	        if (matcher.find()) {
	            return matcher.group(2);
	        }
	    } catch (Exception e) {
	        System.err.println("UserID extraction failed: " + e.getMessage());
	    }
	    return null;
	}

	// ✅ Method 3: Extract merchant ID from BIPS response  
	private String extractMerchantIdFromEncrypted(String response) {
	    if (response == null) return null;
	    try {
	        Pattern pattern = Pattern.compile("(?i)merchant_?id=(\\d+)", Pattern.CASE_INSENSITIVE);
	        Matcher matcher = pattern.matcher(response);
	        if (matcher.find()) {
	            return matcher.group(1);
	        }
	    } catch (Exception e) {
	        System.err.println("MerchantID extraction failed: " + e.getMessage());
	    }
	    return null;
	}

	@PostMapping("LoginForTab")
	public String LoginTab(@RequestBody EncryptionEntity EncryptedString,
			@RequestHeader(value = "PSU_Device_ID", required = true) String psuDeviceID) throws Exception {
		try {
			String response = loginService.RepLogin(EncryptedString,psuDeviceID);
			return response;
		} catch (Exception e) {
			logger.debug("Login for TAB Exception: " + e.getMessage());
			System.out.println("Exception: " + e.getMessage());
			return  e.getMessage();
		}
	}
	// Logout for Tab
	@PostMapping("LogoutForTab")
	public String LogoutforTab(@RequestBody EncryptionEntity EncryptedString,
			@RequestHeader(value = "PSU_Device_ID", required = true) String psuDeviceID) throws Exception{
		try {
			String response = loginService.Logoutfortab(EncryptedString, psuDeviceID);
			return response;
		} catch (Exception e) {
			logger.debug("Logout for TAB Exception: " + e.getMessage());
			System.out.println("Exception: " + e.getMessage());
			return e.getMessage();
		}
	}

	// Login for Mobile
	@PostMapping("LoginforMobile")
	public String MobileLogin(@RequestBody EncryptionEntity EncryptedString,
			@RequestHeader(value = "PSU_Device_ID", required = true) String psuDeviceID) throws Exception {
		try {
			String response = loginService.UserLogin(EncryptedString,psuDeviceID);
			return response;
		} catch (Exception e) {
			logger.debug("Login for Mobile:Failed" + e.getMessage());
			System.out.print("Exception: " + e.getMessage());
			return e.getMessage();

		}
	}

	// Logout for Mobile
	@PostMapping("LogoutforMobile")
	public String MobileLogout(@RequestBody EncryptionEntity EncryptedString,
			@RequestHeader(value = "PSU_Device_ID", required = true) String psuDeviceID) throws Exception{
		try {
			String response = loginService.LogoutMobile(EncryptedString,psuDeviceID);
			return response;
		} catch (Exception e) {
			System.out.print("Exception: " + e.getMessage());
			return e.getMessage();

		}
	}

	// Sent OTP for user
	@GetMapping("OtpForUser")
	public String SendOTPUser(@RequestParam String user_id,@RequestHeader(value = "PSU_Device_ID", required = true) String psuDeviceID) throws Exception  {
		try {
			String response = loginService.SentOTPforUser(user_id, psuDeviceID);
			return response;
		} catch (Exception e) {
			System.out.print("Exception: " + e.getMessage());
			logger.debug("OTP sent to User: " + e.getMessage());
			return e.getMessage();

		}
	}

	// Sent OTP for Merchant
	@GetMapping("OtpForMerchant")
	public String SendOTPMerchant(@RequestParam String merchant_rep_id, @RequestHeader(value = "PSU_Device_ID", required = true) String psuDeviceID) throws Exception  {
		try {
			String response = loginService.SentOTPforMerchant(merchant_rep_id, psuDeviceID);
			return response;
		} catch (Exception e) {
			System.out.print("Exception: " + e.getMessage());
			logger.debug("OTP sent to Merchant: " + e.getMessage());
			return e.getMessage();

		}
	}

	// Sent OTP for Merchant
		@GetMapping("OtpForAndroid")
		public String SendOTPAndroid(@RequestParam String merchant_rep_id, @RequestHeader(value = "PSU_Device_ID", required = true) String psuDeviceID) throws Exception  {
			try {
				String response = loginService.SentOTPforAndroid(merchant_rep_id, psuDeviceID);
				return response;
			} catch (Exception e) {
				System.out.print("Exception: " + e.getMessage());
				logger.debug("OTP sent to Merchant: " + e.getMessage());
				return e.getMessage();

			}
		}
	// Reset User Password
	@PostMapping("ResetUserPassword")
	public String ResetUserPassword(@RequestBody EncryptionEntity EncryptedString,
			@RequestHeader(value = "PSU_Device_ID", required = true) String psuDeviceID) throws Exception{
		try {
			String response = loginService.ChangeUserPassword(EncryptedString,psuDeviceID);
			return response;
		} catch (Exception e) {
			System.out.print("Exception: " + e.getMessage());
			logger.debug("Failed to Reset User" + e.getMessage());
			return  e.getMessage();
		}
	}

	// Reset Merchant Password
	@PostMapping("ResetMerchantPassword")
	public String ResetMerchantPassword(@RequestBody EncryptionEntity EncryptedString,
			@RequestHeader(value = "PSU_Device_ID", required = true) String psuDeviceID) throws Exception{
		try {
			String response = loginService.ChangeMerchantPassword(EncryptedString,psuDeviceID);
			return response;
		} catch (Exception e) {
			System.out.print("Exception: " + e.getMessage());
			logger.debug("Reset Represtatvie User" + e.getMessage());
			return e.getMessage();

		}
	}

	// Check password for tab for lock screen
	@GetMapping("CheckPasswordForTab")
	public String CheckPasswordForTab(@RequestParam String merchant_id, @RequestParam String password, @RequestHeader(value = "PSU_Device_ID", required = true) String psuDeviceID) throws Exception  {
		try {
			String response = loginService.CheckPasswordForTab(merchant_id, password, psuDeviceID);
			return response;
		} catch (Exception e) {
			System.out.print("Exception: " + e.getMessage());
			logger.debug("Check password for Merchant" + e.getMessage());
			return  e.getMessage();

		}
	}

	// Check Password for the Mobile
	@GetMapping("CheckPasswordForMobile")
	public String CheckPasswordForMobile(@RequestParam String merchant_id, @RequestParam String password, @RequestHeader(value = "PSU_Device_ID", required = true) String psuDeviceID) throws Exception  {
		try {
			String response = loginService.CheckPasswordForMobile(merchant_id, password, psuDeviceID);
			return response;
		} catch (Exception e) {
			System.out.print("Exception: " + e.getMessage());
			logger.debug("Check password for user" + e.getMessage());
			return e.getMessage();

		}
	}
	
	//Authentication for Mobile
	@PostMapping("AuthenticationForRep")
	public String Authentication(@RequestBody EncryptionEntity EncryptedString,
			@RequestHeader(value = "PSU_Device_ID", required = true) String psuDeviceID) throws Exception {
			return loginService.RepAuthentication(EncryptedString, psuDeviceID);
				
	}
	@GetMapping("CheckTwoFactorAnswer")
	public String checkanswer(@RequestParam String userId,@RequestParam int answerNumber, @RequestParam String answer,@RequestHeader(value = "PSU_Device_ID", required = true) String psuDeviceID) {
	   return loginService.TwoFa(userId,answerNumber, answer,psuDeviceID);
	}
	
	
	@PostMapping("ResetUserNewPassword")
	public String ResetUserNewPassword(@RequestBody EncryptionEntity EncryptedString,
			@RequestHeader(value = "PSU_Device_ID", required = true) String psuDeviceID) throws Exception {
		try {
			String response = loginService.ChangeUserNewPassword(EncryptedString,psuDeviceID);
			return response;
		} catch (Exception e) {
			System.out.print("Exception: " + e.getMessage());
			logger.debug("Reset Represtatvie User" + e.getMessage());
			return e.getMessage();

		}
	}
	
	
	@PostMapping("ResetMerchantNewPassword")
	public String ResetMerchantNewPassword(@RequestBody EncryptionEntity EncryptedString,
			@RequestHeader(value = "PSU_Device_ID", required = true) String psuDeviceID) throws Exception {
			try {
				String response = loginService.ChangeMerchantNewPassword(EncryptedString,psuDeviceID);
				return response;
			} catch (Exception e) {
				System.out.print("Exception: " + e.getMessage());
				logger.debug("Reset Represtatvie User" + e.getMessage());
				return e.getMessage();

			}
	}
	
	
}
