package com.bornfire.controller;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bornfire.entity.CIMMerchantQRRequestFormat;
import com.bornfire.entity.CimDynamicMaucasRequest;
import com.bornfire.entity.CustomerTransactionEntity;
import com.bornfire.service.QrCodeService;

@RestController
@RequestMapping("/api")
public class QrController {

	@Autowired
	QrCodeService service;

	// Generate Static QR Code
	@PostMapping(path = "/ws/StaticMaucas", produces = "application/json", consumes = "application/json")
	public String genMerchantQRcode(@RequestHeader(value = "P-ID", required = false) String p_id,
			@RequestHeader(value = "PSU-Device-ID", required = false) String psuDeviceID,
			@RequestHeader(value = "PSU-IP-Address", required = false) String psuIpAddress,
			@RequestHeader(value = "PSU-ID", required = false) String psuID,
			@RequestHeader(value = "PSU-Channel", required = false) String channelID,
			@RequestHeader(value = "Merchant_ID", required = true) String acct_num,
			@RequestHeader(value = "Device_ID", required = false) String deviceID,
			@RequestHeader(value = "Reference_Number", required = false) String referencenumber,
			@RequestHeader(value = "User-ID", required = true) String UserID,
			@RequestHeader(value = "Unit-ID", required = true) String unit_id,
			@RequestHeader(value = "PSU-Resv-Field2", required = false) String resvfield2) {
		String response = service.createMerchantQRConnection(p_id, psuDeviceID, psuIpAddress,psuID, channelID,
				acct_num, resvfield2,deviceID,referencenumber,UserID,unit_id);
		return response;
	}

	// Generate Dynamic QR Code
	@PostMapping(path = "/ws/DynamicMaucas", produces = "application/json", consumes = "application/json")
	public String genDynamicMerchantQRcode(@RequestHeader(value = "P-ID", required = false) String p_id,
			@RequestHeader(value = "PSU-Device-ID", required = false) String psuDeviceID,
			@RequestHeader(value = "PSU-IP-Address", required = false) String psuIpAddress,
			@RequestHeader(value = "Device-ID", required = false) String DeviceID,
			@RequestHeader(value = "PSU-ID", required = false) String psuID,
			@RequestHeader(value = "PSU-Channel", required = true) String channelID,
			@RequestHeader(value = "Merchant_ID", required = true) String acct_num,
			@RequestHeader(value = "User-ID", required = true) String UserID,
			@RequestHeader(value = "PSU-Resv-Field2", required = false) String resvfield2,
			@RequestHeader(value = "Unit-ID", required = true) String unit_id,
			@RequestBody CimDynamicMaucasRequest cimmaudynamic

	) {
		String response = service.createDynamicMerchantQRConnection(psuDeviceID, psuIpAddress,DeviceID, psuID, p_id, channelID,
				acct_num, resvfield2, cimmaudynamic,UserID,unit_id);
		return response;

	}

	
	// Scan Customer API
	@PostMapping(path = "/ws/scanCustomerQRcode", produces = "application/json", consumes = "application/json")
	public String getCustomerQRdata(
			@RequestHeader(value = "P-ID", required = true) @NotEmpty(message = "Required") String p_id,
			@RequestHeader(value = "PSU-Device-ID", required = true) @NotEmpty(message = "Required") String psuDeviceID,
			@RequestHeader(value = "PSU-IP-Address", required = true) String psuIpAddress,
			@RequestHeader(value = "PSU-ID", required = false) String psuID,
			@RequestHeader(value = "PSU-Channel", required = true) String channelID,
			@RequestHeader(value = "PSU-Resv-Field1", required = false) String resvfield1,
			@RequestHeader(value = "PSU-Resv-Field2", required = false) String resvfield2,
			@Valid @RequestBody CIMMerchantQRRequestFormat mcCreditTransferRequest) {
		
		String response = service.ScanCustomerQR(psuDeviceID, psuIpAddress, psuID,
				 p_id, channelID, resvfield1,resvfield2, mcCreditTransferRequest);
		return response;
	}
	
	//InitateTransactionfrommerchantside
	@PostMapping(path = "/ws/InitiateCustomerTransaction", produces = "application/json", consumes = "application/json")
	public String directCustomerFndInititate(
			@RequestHeader(value = "P-ID", required = true) @NotEmpty(message = "Required") String p_id,
			@RequestHeader(value = "PSU-Device-ID", required = true) @NotEmpty(message = "Required") String psuDeviceID,
			@RequestHeader(value = "PSU-IP-Address", required = true) String psuIpAddress,
			@RequestHeader(value = "PSU-ID", required = false) String psuID,
			@RequestHeader(value = "PSU-Channel", required = true) String channelID,
			@RequestHeader(value = "PSU-Resv-Field1", required = false) String resvfield1,
			@RequestHeader(value = "PSU-Resv-Field2", required = false) String resvfield2,
			@Valid @RequestBody CustomerTransactionEntity request)
			 {
           String response=service.InititeCustomerpaymentConnection(p_id,psuDeviceID,psuIpAddress,psuID,channelID,resvfield1, resvfield2,request);
		
		return "Successfully Saved";
	    }
	
	@GetMapping("/getTranAmountLimit")
	public String getTranAmountLimit(@RequestParam String merchant_id) {
		String Amount=service.getTranAmountLimit(merchant_id);
		return Amount;
	}
	
	@GetMapping("/getStaticPaydetails")
	public String getCustomerPayDetails(@RequestParam String merchant_id, @RequestParam String device_id,
			@RequestParam String userid){
		String response = service.getStaticNotificationService(merchant_id,device_id, userid);
		return response;
	}

	
}
