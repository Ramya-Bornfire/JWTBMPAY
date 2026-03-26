package com.bornfire.controller;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bornfire.entity.CIMCustomerQRcodeRequest;
import com.bornfire.entity.CIMMerchantDirectFndRequest;
import com.bornfire.entity.CIMMerchantQRRequestFormat;
import com.bornfire.entity.EncryptionEntity;
import com.bornfire.service.CustomerService;

@RestController
@RequestMapping("/api")
public class CustomerController {
	@Autowired
	CustomerService service;

	//Customer Static QR Code
	@PostMapping(path = "/ws/generateCustomerQRcodeEnc", produces = "application/json", consumes = "application/text")
	public String genMerchantQRcode(
			@RequestHeader(value = "P-ID", required = true) @NotEmpty(message = "Required") String p_id,
			@RequestHeader(value = "PSU-Device-ID", required = true) @NotEmpty(message = "Required") String psuDeviceID,
			@RequestHeader(value = "PSU-IP-Address", required = true) String psuIpAddress,
			@RequestHeader(value = "PSU-ID", required = false) String psuID,
			@RequestHeader(value = "PSU-Channel", required = true) String channelID,
			@RequestHeader(value = "PSU-Resv-Field1", required = false) String resvfield1,
			@RequestHeader(value = "PSU-Resv-Field2", required = false) String resvfield2,
			@RequestBody String EncryptedString)
			throws DatatypeConfigurationException, JAXBException, KeyManagementException, UnrecoverableKeyException,
			KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException, Exception {

		String response = service.createMerchantQRConnection(p_id, psuDeviceID, psuIpAddress, psuID, channelID,
				resvfield1, resvfield2, EncryptedString);

		return response;
	}

	// Scan Merchant QR Code
	@PostMapping(path = "/ws/scanMerchantQRcodeEnc", produces = "application/json", consumes = "application/text")
	public String ScanCustomerQR(
			@RequestHeader(value = "P-ID", required = true) @NotEmpty(message = "Required") String p_id,
			@RequestHeader(value = "PSU-Device-ID", required = true) @NotEmpty(message = "Required") String psuDeviceID,
			@RequestHeader(value = "PSU-IP-Address", required = true) String psuIpAddress,
			@RequestHeader(value = "PSU-ID", required = false) String psuID,
			@RequestHeader(value = "PSU-Channel", required = true) String channelID,
			@RequestHeader(value = "PSU-Resv-Field1", required = false) String resvfield1,
			@RequestHeader(value = "PSU-Resv-Field2", required = false) String resvfield2,
			@RequestBody String EncryptedString) {

		String response = service.createScanMerchantQRConnection(p_id, psuDeviceID, psuIpAddress, psuID, channelID,
				resvfield1, resvfield2, EncryptedString);
		return response;
	}

	// Transaction Api
	@PostMapping(path = "/ws/directMerchantFndTransferEnc", produces = "application/json", consumes = "application/text")
	public String directMerchantFndTransfer(
			@RequestHeader(value = "P-ID", required = true) @NotEmpty(message = "Required") String p_id,
			@RequestHeader(value = "PSU-Device-ID", required = true) @NotEmpty(message = "Required") String psuDeviceID,
			@RequestHeader(value = "PSU-IP-Address", required = true) String psuIpAddress,
			@RequestHeader(value = "PSU-ID", required = false) String psuID,
			@RequestHeader(value = "PSU-Channel", required = true) String channelID,
			@RequestHeader(value = "PSU-Resv-Field1", required = false) String resvfield1,
			@RequestHeader(value = "PSU-Resv-Field2", required = false) String resvfield2,
			@Valid @RequestBody String EncryptedString) {
		String response = service.InititeTransactionConnection(p_id, psuDeviceID, psuIpAddress, psuID, channelID,
				resvfield1, resvfield2, EncryptedString);

		return response;
	}

	@GetMapping("/ws/getMerchantTransactionDetailsEnc")
	public String GetUnitAllUser(@RequestParam String customer_id, @RequestParam String reference_number, @RequestHeader(value = "PSU-Device-ID", required = true) @NotEmpty(message = "Required") String psuDeviceID) {
		String response = service.getDetailsMerchant(customer_id, reference_number,psuDeviceID);
		return response;
	}


	//Customer Static QR Code
	@PostMapping(path = "/ws/generateCustomerQRcode", produces = "application/json", consumes = "application/json")
	public String genMerchantQRcode1(
			@RequestHeader(value = "P-ID", required = true) @NotEmpty(message = "Required") String p_id,
			@RequestHeader(value = "PSU-Device-ID", required = true) @NotEmpty(message = "Required") String psuDeviceID,
			@RequestHeader(value = "PSU-IP-Address", required = true) String psuIpAddress,
			@RequestHeader(value = "PSU-ID", required = false) String psuID,
			@RequestHeader(value = "PSU-Channel", required = true) String channelID,
			@RequestHeader(value = "PSU-Resv-Field1", required = false) String resvfield1,
			@RequestHeader(value = "PSU-Resv-Field2", required = false) String resvfield2,
			@RequestBody CIMCustomerQRcodeRequest EncryptedString)
			throws DatatypeConfigurationException, JAXBException, KeyManagementException, UnrecoverableKeyException,
			KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException, Exception {

		String response = service.createMerchantQRConnection1(p_id, psuDeviceID, psuIpAddress, psuID, channelID,
				resvfield1, resvfield2, EncryptedString);

		return response;
	}

	// Scan Merchant QR Code
	@PostMapping(path = "/ws/scanMerchantQRcode", produces = "application/json", consumes = "application/json")
	public String ScanCustomerQR1(
			@RequestHeader(value = "P-ID", required = true) @NotEmpty(message = "Required") String p_id,
			@RequestHeader(value = "PSU-Device-ID", required = true) @NotEmpty(message = "Required") String psuDeviceID,
			@RequestHeader(value = "PSU-IP-Address", required = true) String psuIpAddress,
			@RequestHeader(value = "PSU-ID", required = false) String psuID,
			@RequestHeader(value = "PSU-Channel", required = true) String channelID,
			@RequestHeader(value = "PSU-Resv-Field1", required = false) String resvfield1,
			@RequestHeader(value = "PSU-Resv-Field2", required = false) String resvfield2,
			@RequestBody CIMMerchantQRRequestFormat EncryptedString) {

		String response = service.createScanMerchantQRConnection1(p_id, psuDeviceID, psuIpAddress, psuID, channelID,
				resvfield1, resvfield2, EncryptedString);
		return response;
	}

	// Transaction Api
	@PostMapping(path = "/ws/directMerchantFndTransfer", produces = "application/json", consumes = "application/json")
	public String directMerchantFndTransfer1(
			@RequestHeader(value = "P-ID", required = true) @NotEmpty(message = "Required") String p_id,
			@RequestHeader(value = "PSU-Device-ID", required = true) @NotEmpty(message = "Required") String psuDeviceID,
			@RequestHeader(value = "PSU-IP-Address", required = true) String psuIpAddress,
			@RequestHeader(value = "PSU-ID", required = false) String psuID,
			@RequestHeader(value = "PSU-Channel", required = true) String channelID,
			@RequestHeader(value = "PSU-Resv-Field1", required = false) String resvfield1,
			@RequestHeader(value = "PSU-Resv-Field2", required = false) String resvfield2,
			@Valid @RequestBody CIMMerchantDirectFndRequest EncryptedString) {
		String response = service.InititeTransactionConnection1(p_id, psuDeviceID, psuIpAddress, psuID, channelID,
				resvfield1, resvfield2, EncryptedString);

		return response;
	}
}



