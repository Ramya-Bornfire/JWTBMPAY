package com.bornfire.controller;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Date;
import java.util.List;

import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bornfire.entity.AlertEntity;
import com.bornfire.entity.AllCustomTransaction;
import com.bornfire.entity.BIPS_Charge_Back_Entity;
import com.bornfire.entity.EncryptionEntity;
import com.bornfire.entity.FeesAndChargesEntity;
import com.bornfire.entity.NotificationEntity;
import com.bornfire.entity.OutwardTransactionMonitoringTable;
import com.bornfire.entity.ServiceReqEntity;
import com.bornfire.service.AdminModuleService;

@RestController
@RequestMapping("/api")
public class AdminModule {

	@Autowired
	private AdminModuleService service;
	
	@GetMapping("/uniqueUserId")
    public String uniqueUserId(@RequestParam String merchant_id) {
		return service.getUniqueUserId(merchant_id);
	}
	
	@GetMapping("/uniqueDeviceId")
    public String uniqueDeviceId(@RequestParam String merchant_id) {
		return service.getUniqueDeviceId(merchant_id);
	}
	
	@GetMapping("/ServiceRequestId")
    public String ServiceRequestId() {
		return service.getUniqueSrId();
	}
	
	@GetMapping("/NotifiParamId")
    public String NotifiParamId() {
		return service.getUniqueNpId();
	}
	
	
	//Reference code
	@GetMapping("/referenceMasterForDropdown")
	public List<String> referenceMasterForDropdown(@RequestParam String ref_type) {
		return service.getReferenceList(ref_type);
	}
	
	// Single Notification List
	@GetMapping("/AllNotificationList")
	public List<NotificationEntity> GetMerchantUnitAllUser(@RequestParam String merchant_id,
			@RequestParam String unit_id) {
		return service.getAllNotification(merchant_id, unit_id);
	}

	// Single All Customer Transaction list
	@GetMapping("/AllCustomerTransactionList")
	public List<AllCustomTransaction> getAllMerchantCustomerUnitDetails(@RequestParam String merchant_id,
			@RequestParam String unit_id, @RequestParam String fromdate,
			@RequestParam String todate,  @RequestParam String type) {
		return service.getAllCustomerTransaction(merchant_id, unit_id,fromdate,todate,type);
	}
	
	// User and Device Combination Transaction list
	@GetMapping("/AllTransactionListHistory")
	public List<AllCustomTransaction> getTransalltransaction(@RequestParam String user_id, @RequestParam String merchant_id,@RequestParam String fromdate,
			@RequestParam String todate, @RequestParam String type) {
		return service.getallMerchantTransaction(merchant_id,user_id,fromdate,todate,type);
	}
	
	// Unit Wise Fees and Charges List
		@GetMapping("/AllFeesAndChargesList")
		public List<FeesAndChargesEntity> getAllFeesDetails(@RequestParam String merchant_id,
				@RequestParam String unit_id,@RequestParam String fromdate,
				String todate,  @RequestParam String type) {
			return service.getAllFeesChargesTransactions(merchant_id, unit_id,fromdate,todate,type);
		}
		
		// Service Request List
		@GetMapping("/AllServiceRequestList")
		public List<ServiceReqEntity> gealltServiceDetails(@RequestParam String merchant_id,
				@RequestParam String unit_id) {
			//System.out.println("BMPAYY Controlerr----->"+merchant_id+unit_id);
			return service.getAllservice(merchant_id, unit_id);
		}
		
		@GetMapping("/AllChargeBackList")
		public List<BIPS_Charge_Back_Entity> getAllChargeBacks(@RequestParam String merchant_id,
				@RequestParam String unit_id,@RequestParam String fromdate,
				@RequestParam String todate) {
			return service.getAllListchargeback(merchant_id, unit_id,fromdate,todate);
		}
		@GetMapping("/AllMerchantPendingChargeBack")
		public List<BIPS_Charge_Back_Entity> getAllPendingChargeBacks(@RequestParam String merchant_id,
				@RequestParam String unit_id,@RequestParam String fromdate,@RequestParam String todate) {
			return service.getAllPendingTransaction(merchant_id, unit_id,fromdate,todate);
		}
		@GetMapping("/AllMerchantRevertedChargeBack")
		public List<BIPS_Charge_Back_Entity> getAllRevertedChargeBacks(@RequestParam String merchant_id,
				@RequestParam String unit_id,@RequestParam String fromdate,@RequestParam String todate) {
			return service.getAllRevertedTransaction(merchant_id, unit_id,fromdate,todate);
		}

	
	
	
	
	// Creation/Addition of new User
	@PostMapping("/AddNotification")
	public String postUserData(@RequestBody EncryptionEntity EncryptedString,
			@RequestHeader(value = "PSU_Device_ID", required = true) String psuDeviceID) throws Exception  {
		try {
			String response = service.AddNotification(EncryptedString, psuDeviceID);
			return response;
		} catch (Exception ex) {
			return ex.getMessage();
		}
	}
	
	@GetMapping("/NotificationListforMerchant")
	public List<NotificationEntity> getAllNotification(@RequestParam String merchant_id){
		return service.getAllNotification(merchant_id);
	}
	@GetMapping("/NotificationListforUnit")
	public List<NotificationEntity> getUnitNotification(@RequestParam String merchant_id, @RequestParam String unit_id){
		return service.getUnitNotification(merchant_id,unit_id);
	}
	// Service Request List
	/*
	 * @GetMapping("/ServiceRequestAllList") public List<ServiceReqEntity>
	 * getAllServiceDetails(@RequestParam String merchant_id) { return
	 * service.getAllservice(merchant_id); }
	 */
	
	// Service Request List
	@GetMapping("/ServiceRequestUnitList")
	public List<ServiceReqEntity> getunitServiceDetails(@RequestParam String merchant_id, @RequestParam String unit_id) {
		return service.getAllUnitservice(merchant_id,unit_id);
	}
  
    //Add Service Request
	@PostMapping("/AddServiceReq")
	public String postServiceData(@RequestBody EncryptionEntity EncryptedString,
			@RequestHeader(value = "PSU_Device_ID", required = true) String psuDeviceID) throws Exception {
		return service.AddService(EncryptedString, psuDeviceID);
	}
	
	@GetMapping("/AlertListForAdmin")
	public List<AlertEntity> getAllAlert(){
		return service.getAllAlert();
	}
	// Merchant Charge Back List
	@GetMapping("/ChargeBackList")
	public List<BIPS_Charge_Back_Entity> getAllChargeBacksDetails(@RequestParam String merchant_id) {
		return service.getAllChargeBacks(merchant_id);
	}

	// Unit-wise Charge Back List
	@GetMapping("/UnitChargeBackList")
	public List<BIPS_Charge_Back_Entity> getAllUnitChargeBacksDetails(@RequestParam String merchant_id,
			@RequestParam String unit_id) {
		return service.getAllUnitChargeBacks(merchant_id, unit_id);
	}

	@GetMapping("/AllPendingChargeBack")
    public List<BIPS_Charge_Back_Entity> getAllPendingChargeBacksDetails(@RequestParam String merchant_id) {
         return service.getPendingTransactionMerchantList(merchant_id);
    }
	
	//Unit pending chargebacklist
	@GetMapping("/UnitPendingChargeBack")
    public List<BIPS_Charge_Back_Entity> getUnitPendingChargeBacksDetails(@RequestParam String merchant_id, @RequestParam String unit_id) {
         return service.getPendingTransactionUnitList(merchant_id, unit_id);
    }
    
	@GetMapping("/AllRevertedChargeBack")
    public List<BIPS_Charge_Back_Entity> getAllRevertedChargeBacksDetails(@RequestParam String merchant_id) {
         return service.getRevertedTransactionMerchantList(merchant_id);
    }
    @GetMapping("/UnitRevertedChargeBack")
    public List<BIPS_Charge_Back_Entity> getUnitRevertedChargeBacksDetails(@RequestParam String merchant_id, @RequestParam String unit_id) {
         return service.getRevertedTransactionUnitList(merchant_id, unit_id);
    }
    
	// Initiated Charge Backs
	@RequestMapping(value = "InititedChargeBack", method = { RequestMethod.POST })
	public String UpdateChargeBack(@RequestParam String userid, @RequestParam String seqUniqueID, @RequestParam String merchant_id) throws Exception {
		try {
			ResponseEntity<String> response = service.updateChargeBack(userid,seqUniqueID,merchant_id);
			if (response.getStatusCode() != HttpStatus.OK) {
				return "Failed to Initiate chargeback";
			}
			return "Initiated Successfully.";
		} catch (Exception e) {
			System.out.println("Exception------->" + e.getMessage());
			return e.getMessage();
		}
	}
	
	@PostMapping("/ws/revertMerchantFndTransfer")
	public String revertMerchantFndTransfer(@RequestParam String seqUniqueID,
			@RequestParam String userid)
			throws DatatypeConfigurationException, JAXBException, KeyManagementException, UnrecoverableKeyException,
			KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {
		try {
			ResponseEntity<String> response = service.ApproveChargeBack(userid,seqUniqueID);
			if (response.getStatusCode() != HttpStatus.OK) {
				return "Failed to Initiate chargeback";
			}
			return "Approved Successfully.";
		} catch (Exception e) {
			System.out.println("Exception------->" + e.getMessage());
			return e.getMessage();
		}
		
		
	}
	
	// Merchant All Customer Transaction list
	@GetMapping("/CustomerTransactionList")
	public List<AllCustomTransaction> getAllCustomerDetails(@RequestParam String merchant_id) {
		return service.getCustomerTransactionForMerchant(merchant_id);
	}

	// Unit All Customer Transaction list
	@GetMapping("/UnitCustomerTransactionList")
	public List<AllCustomTransaction> getAllCustomerUnitDetails(@RequestParam String merchant_id,
			@RequestParam String unit_id) {
		return service.getCustomerTransactionForUnit(merchant_id, unit_id);
	}

	// User All Transaction list
	@GetMapping("/TransactionList")
	public List<OutwardTransactionMonitoringTable> getAllTransactions(@RequestParam String user_id, @RequestParam String Merchant_id) {
		return service.getAllTransactions(user_id,Merchant_id);
	}

	// User and Device Combination Transaction list
	@GetMapping("/TransactionListForUser")
	public List<OutwardTransactionMonitoringTable> getTransForUser(@RequestParam String user_id,
			@RequestParam String Device_id, @RequestParam String Merchant_id) {
		return service.getUserTransaction(user_id, Device_id,Merchant_id);
	}

	// Fees and Charges List
	@GetMapping("/FeesAndChargesList")
	public List<FeesAndChargesEntity> getAllFeesDetails(@RequestParam String merchant_id) {
		return service.getFeesChargesTransactions(merchant_id);
	}

	// Unit Wise Fees and Charges List
	@GetMapping("/UnitFeesAndChargesList")
	public List<FeesAndChargesEntity> getAllUnitFeesDetails(@RequestParam String merchant_id,
			@RequestParam String unit_id) {
		return service.getUnitFeesChargesTransactions(merchant_id, unit_id);
	}

	// Account Statement Merchant wise
	@GetMapping("/AccountStatementMerchantWise")
	public List<Object> AccountStatementMerchant(@RequestParam String merchant_id, @RequestParam String fromdate,
			@RequestParam String todate) {
		List<Object> res = service.AccountStatementMerchantWise(merchant_id, fromdate, todate);
		return res;
	}

	// Account Statement Merchant Unit wise
	@GetMapping("/AccountStatementMerchantUnitWise")
	public List<Object> AccountStatementMerchantUnit(@RequestParam String merchant_id, @RequestParam String fromdate,
			@RequestParam String todate, @RequestParam String unit_id) {
		return service.AccountStatementMerchantUnitWise(merchant_id, fromdate, todate, unit_id);
	}

	// Account Statement User wise
	@GetMapping("/AccountStatementUserWise")
	public List<Object> AccountStatementUserWise(@RequestParam String merchant_id, @RequestParam String fromdate,
			@RequestParam String todate, @RequestParam String unit_id, @RequestParam String user_id) {
		return service.AccountStatementUserWise(merchant_id, fromdate, todate, unit_id, user_id);
	}

	// Account Statement User wise
	@GetMapping("/AccountStatementDeviceWise")
	public List<Object> AccountStatementDeviceWise(@RequestParam String merchant_id, @RequestParam String fromdate,
			@RequestParam String todate, @RequestParam String unit_id, @RequestParam String device_id) {
		return service.AccountStatementDeviceWise(merchant_id, fromdate, todate, unit_id, device_id);
	}
	@GetMapping("/getCustomerPaydetails")
	public String getCustomerPayDetails(@RequestParam String merchant_id, @RequestParam String device_id,@RequestParam String reference_number, 		@RequestHeader(value = "PSU_Device_ID", required = true) String psuDeviceID)
	{
		return service.getCustomerPayDetails(merchant_id,device_id,reference_number, psuDeviceID);
	}




	
}


