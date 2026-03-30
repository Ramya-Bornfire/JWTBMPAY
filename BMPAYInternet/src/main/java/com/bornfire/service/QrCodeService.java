package com.bornfire.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.bornfire.entity.CIMMerchantDirectFndRequest;
import com.bornfire.entity.CIMMerchantQRRequestFormat;
import com.bornfire.entity.CimDynamicMaucasRequest;
import com.bornfire.entity.CustomerTransactionEntity;

@Service
public class QrCodeService {
	private static final Logger logger = LoggerFactory.getLogger(DeviceManagementService.class);

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	Environment env;

	private static final String GENERIC_QR_FAILURE          = "QR code operation failed. Please try again.";
	private static final String GENERIC_TRANSACTION_FAILURE = "Transaction could not be initiated. Please try again.";
	private static final String GENERIC_SERVICE_FAILURE     = "The request could not be completed. Please try again.";
	
	// Static QR Code Service
	public String createMerchantQRConnection(String p_id, String psuDeviceID, String psuIpAddress,String psuID,
			String channelID, String acct_num, String resvfield2,String deviceID,String referencenumber,String UserID,String unit_id) {
		String url = env.getProperty("intranetconnection") + "/api/ws/StaticMaucas";
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("P-ID", p_id);
		headers.add("PSU-Device-ID", psuDeviceID);
		headers.add("PSU-IP-Address", psuIpAddress);
		headers.add("PSU-ID", psuID);
		headers.add("PSU-Channel", channelID);
		headers.add("Merchant_ID", acct_num);
		headers.add("PSU-Resv-Field2", resvfield2);
		headers.add("Device_ID", deviceID);
		headers.add("Reference_Number", referencenumber);
		headers.add("User-ID", UserID);
		headers.add("Unit-ID", unit_id);
		HttpEntity<String> requestEntity = new HttpEntity<>(headers);
		try {
			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
			return response.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug("Device Not able to Create" + e.getMessage());
			return GENERIC_QR_FAILURE;
		}
	}

	// Dynamic QR Code Service
	public String createDynamicMerchantQRConnection(String p_id, String psuDeviceID,String DeviceID, String psuIpAddress,String psuID,
			String channelID, String acct_num, String resvfield2, CimDynamicMaucasRequest cimmaudynamic,String UserID,String unit_id) {
		String url = env.getProperty("intranetconnection") + "/api/ws/DynamicMaucas";
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("P-ID", p_id);
		headers.add("PSU-Device-ID", psuDeviceID);
		headers.add("PSU-IP-Address", psuIpAddress);
		headers.add("Device-ID", DeviceID);
		headers.add("PSU-ID", psuID);
		headers.add("PSU-Channel", channelID);
		headers.add("Merchant_ID", acct_num);
		headers.add("User-ID", UserID);
		headers.add("PSU-Resv-Field2", resvfield2);
		headers.add("Unit-ID", unit_id);
		HttpEntity<CimDynamicMaucasRequest> requestEntity = new HttpEntity<>(cimmaudynamic, headers);
		try {
			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
			return response.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug("Device Not able to Create" + e.getMessage());
			return GENERIC_QR_FAILURE;
		}
	}

	// Scan Merchant QR Code
	public String createScanCustomerQRConnection(String p_id, String psuDeviceID, String psuIpAddress, String psuID,
			String channelID, String acct_num, String resvfield2, CIMMerchantQRRequestFormat cimmaudynamic) {
		String url = env.getProperty("intranetconnection") + "/api/ws/scanMerchantQRcode";
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("P-ID", p_id);
		headers.add("PSU-Device-ID", psuDeviceID);
		headers.add("PSU-IP-Address", psuIpAddress);
		headers.add("PSU-ID", psuID);
		headers.add("PSU-Channel", channelID);
		headers.add("PSU-Resv-Field1", acct_num);
		headers.add("PSU-Resv-Field2", resvfield2);
		HttpEntity<CIMMerchantQRRequestFormat> requestEntity = new HttpEntity<>(cimmaudynamic, headers);
		try {
			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
			return response.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug("Device Not able to Create" + e.getMessage());
			return GENERIC_QR_FAILURE;
		}
	}

	public String InititeTransactionConnection(String p_id, String psuDeviceID, String psuIpAddress, String psuID,
			String channelID, String acct_num, String resvfield2, String userid, String unit_id, String unit_name,
			CIMMerchantDirectFndRequest cimmaudynamic) {
		String url = env.getProperty("intranetconnection") + "/api/ws/directMerchantFndTransfer";
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("P-ID", p_id);
		headers.add("PSU-Device-ID", psuDeviceID);
		headers.add("PSU-IP-Address", psuIpAddress);
		headers.add("PSU-ID", psuID);
		headers.add("PSU-Channel", channelID);
		headers.add("PSU-Resv-Field1", acct_num);
		headers.add("PSU-Resv-Field2", resvfield2);
		headers.add("User-ID", userid);
		headers.add("Unit-ID", unit_id);
		headers.add("Unit-Name", unit_name);
		HttpEntity<CIMMerchantDirectFndRequest> requestEntity = new HttpEntity<>(cimmaudynamic, headers);
		try {
			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
			return response.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug("Device Not able to Create" + e.getMessage());
			return GENERIC_TRANSACTION_FAILURE;
		}
	}
	
	//Scan Customer QR 
	public String ScanCustomerQR(String p_id, String psuDeviceID, String psuIpAddress, String psuID,
			String channelID, String resfield1, String resvfield2, CIMMerchantQRRequestFormat cimmaudynamic) {
		String url = env.getProperty("intranetconnection") + "/api/ws/scanCustomerQRcode";
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("P-ID", p_id);
		headers.add("PSU-Device-ID", psuDeviceID);
		headers.add("PSU-IP-Address", psuIpAddress);
		headers.add("PSU-ID", psuID);
		headers.add("PSU-Channel", channelID);
		headers.add("PSU-Resv-Field1", resfield1);
		headers.add("PSU-Resv-Field2", resvfield2);
		HttpEntity<CIMMerchantQRRequestFormat> requestEntity = new HttpEntity<>(cimmaudynamic, headers);
		try {
			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
			return response.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug("Device Not able to Create" + e.getMessage());
			return GENERIC_QR_FAILURE;
		}
	}

	//Initaite customer transaction
	public String InititeCustomerpaymentConnection(String p_id, String psuDeviceID, String psuIpAddress, String psuID,
			String channelID, String acct_num, String resvfield2,
			CustomerTransactionEntity cimmaudynamic) {
		String url = env.getProperty("intranetconnection") + "/api/ws/InitiateCustomerTransaction";
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("P-ID", p_id);
		headers.add("PSU-Device-ID", psuDeviceID);
		headers.add("PSU-IP-Address", psuIpAddress);
		headers.add("PSU-ID", psuID);
		headers.add("PSU-Channel", channelID);
		headers.add("PSU-Resv-Field1", acct_num);
		headers.add("PSU-Resv-Field2", resvfield2);
		HttpEntity<CustomerTransactionEntity> requestEntity = new HttpEntity<>(cimmaudynamic, headers);
		try {
			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
			return response.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug("Device Not able to Create" + e.getMessage());
			return GENERIC_TRANSACTION_FAILURE;
		}
	}
	
	
	public String getTranAmountLimit(String merchant_id) {
		try {
		String url = env.getProperty("intranetconnection") + "/api/getTranAmountLimit?merchant_id=" + merchant_id;
		String response = restTemplate.getForObject(url, String.class);
		return response;
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug("TranAmount" + e.getMessage());
			return GENERIC_SERVICE_FAILURE;
		}
	}
	
	public String getStaticNotificationService(String merchant_id,String device_id,String userid) {
		 String url = env.getProperty("intranetconnection") + "/api/getStaticPaydetails?merchant_id=" + merchant_id + "&device_id=" + device_id + "&userid=" + userid;
		  try {
		        String response = restTemplate.getForObject(url, String.class);
		        return response;
		    } catch (HttpClientErrorException e) {
		        if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
		            return "No details found for the given merchant ID, device ID, and user ID.";
		        } else {
		            return GENERIC_SERVICE_FAILURE;
		        }
		    }
	}	
	
	
}
