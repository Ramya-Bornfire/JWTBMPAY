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
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import com.bornfire.entity.CIMCustomerQRcodeRequest;
import com.bornfire.entity.CIMMerchantDirectFndRequest;
import com.bornfire.entity.CIMMerchantQRRequestFormat;
import com.bornfire.entity.EncryptionEntity;

@Service
public class CustomerService {
	private static final Logger logger = LoggerFactory.getLogger(DeviceManagementService.class);

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	Environment env;

	// Static QR Code Service
	public String createMerchantQRConnection(String p_id, String psuDeviceID, String psuIpAddress,String DeviceID, String psuID,
			String channelID,String resvfield2, String EncryptedString) {
		String url = env.getProperty("intranetconnection") + "/api/ws/generateCustomerQRcodeEnc";
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("P-ID", p_id);
		headers.add("PSU-Device-ID", psuDeviceID);
		headers.add("PSU-IP-Address", psuIpAddress);
		headers.add("Device-ID", DeviceID);
		headers.add("PSU-ID", psuID);
		headers.add("PSU-Channel", channelID);
		headers.add("PSU-Resv-Field2", resvfield2);
		HttpEntity<String> requestEntity = new HttpEntity<>(EncryptedString, headers);
		try {
			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
			return response.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}
	public String createMerchantQRConnection1(String p_id, String psuDeviceID, String psuIpAddress,String DeviceID, String psuID,
			String channelID,String resvfield2, CIMCustomerQRcodeRequest EncryptedString) {
		String url = env.getProperty("intranetconnection") + "/api/ws/generateCustomerQRcode";
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("P-ID", p_id);
		headers.add("PSU-Device-ID", psuDeviceID);
		headers.add("PSU-IP-Address", psuIpAddress);
		headers.add("Device-ID", DeviceID);
		headers.add("PSU-ID", psuID);
		headers.add("PSU-Channel", channelID);
		headers.add("PSU-Resv-Field2", resvfield2);
		HttpEntity<CIMCustomerQRcodeRequest> requestEntity = new HttpEntity<>(EncryptedString, headers);
		try {
			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
			return response.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}
	
	public String createScanMerchantQRConnection(String p_id, String psuDeviceID, String psuIpAddress, String psuID,
			String channelID, String acct_num, String resvfield2, String cimmaudynamic) {
		String url = env.getProperty("intranetconnection") + "/api/ws/scanMerchantQRcodeEnc";
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("P-ID", p_id);
		headers.add("PSU-Device-ID", psuDeviceID);
		headers.add("PSU-IP-Address", psuIpAddress);
		headers.add("PSU-ID", psuID);
		headers.add("PSU-Channel", channelID);
		headers.add("PSU-Resv-Field1", acct_num);
		headers.add("PSU-Resv-Field2", resvfield2);
		HttpEntity<String> requestEntity = new HttpEntity<>(cimmaudynamic, headers);
		try {
			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
			return response.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug("Device Not able to Create" + e.getMessage());
			return e.getMessage();
		}
	}
	
	public String createScanMerchantQRConnection1(String p_id, String psuDeviceID, String psuIpAddress, String psuID,
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
			return e.getMessage();
		}
	}
	
	public String InititeTransactionConnection(String p_id, String psuDeviceID, String psuIpAddress, String psuID,
			String channelID, String acct_num, String resvfield2,
			String cimmaudynamic) {
		
		String url = env.getProperty("intranetconnection") + "/api/ws/directMerchantFndTransferEnc";
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("P-ID", p_id);
		headers.add("PSU-Device-ID", psuDeviceID);
		headers.add("PSU-IP-Address", psuIpAddress);
		headers.add("PSU-ID", psuID);
		headers.add("PSU-Channel", channelID);
		headers.add("PSU-Resv-Field1", acct_num);
		headers.add("PSU-Resv-Field2", resvfield2);
		HttpEntity<String> requestEntity = new HttpEntity<>(cimmaudynamic, headers);
		try {
			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
			return response.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug("Device Not able to Create" + e.getMessage());
			return e.getMessage();
		}
	}
	public String InititeTransactionConnection1(String p_id, String psuDeviceID, String psuIpAddress, String psuID,
			String channelID, String acct_num, String resvfield2,
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
		HttpEntity<CIMMerchantDirectFndRequest> requestEntity = new HttpEntity<>(cimmaudynamic, headers);
		try {
			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
			return response.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug("Device Not able to Create" + e.getMessage());
			return e.getMessage();
		}
	}
	
	// Merchant wise for ChargeBacks

    public String getDetailsMerchant(String merchantId, String referenceNumber, String psuDeviceID) {
        String url = env.getProperty("intranetconnection") + "/api/ws/getMerchantTransactionDetailsEnc?customer_id=" + merchantId + "&reference_number=" + referenceNumber;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("PSU-Device-ID", psuDeviceID);

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);
            return response.getBody();
        } catch (HttpClientErrorException e) {
            // Handle client errors (4xx)
            System.err.println("Client error: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
        } catch (HttpServerErrorException e) {
       
            System.err.println("Server error: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
        } catch (Exception e) {
          
            e.printStackTrace();
        }

        return null;
    }

		
}
