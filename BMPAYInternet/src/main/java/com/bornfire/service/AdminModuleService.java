package com.bornfire.service;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.bornfire.entity.AlertEntity;
import com.bornfire.entity.AllCustomTransaction;
import com.bornfire.entity.BIPS_Charge_Back_Entity;
import com.bornfire.entity.EncryptionEntity;
import com.bornfire.entity.FeesAndChargesEntity;
import com.bornfire.entity.NotificationEntity;
import com.bornfire.entity.OutwardTransactionMonitoringTable;
import com.bornfire.entity.ServiceReqEntity;

@Service
public class AdminModuleService {

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	Environment env;
	public String getUniqueUserId(String ref_type) {
		String url = env.getProperty("intranetconnection") + "/api/uniqueUserId?merchant_id="+ref_type;
		String response = restTemplate.getForObject(url, String.class);
		if (response == null) {
			return null;
		}
		return response;
	}
	public String getUniqueDeviceId(String ref_type) {
		String url = env.getProperty("intranetconnection") + "/api/uniqueDeviceId?merchant_id="+ref_type;
		String response = restTemplate.getForObject(url, String.class);
		if (response == null) {
			return null;
		}
		return response;
	}
	public String getUniqueSrId() {
		String url = env.getProperty("intranetconnection") + "/api/ServiceRequestId";
		String response = restTemplate.getForObject(url, String.class);
		if (response == null) {
			return null;
		}
		return response;
	}
	public String getUniqueNpId() {
		String url = env.getProperty("intranetconnection") + "/api/NotifiParamId";
		String response = restTemplate.getForObject(url, String.class);
		if (response == null) {
			return null;
		}
		return response;
	}
	
	public List<String> getReferenceList(String ref_type) {
		String url = env.getProperty("intranetconnection") + "/api/referenceMasterForDropdown?ref_type="+ref_type;
		String[] response = restTemplate.getForObject(url, String[].class);
		if (response == null) {
			return Collections.emptyList();
		}
		return Arrays.asList(response);
	}
	public List<AlertEntity> getAllAlert() {
		String url = env.getProperty("intranetconnection") + "/api/AlertListForAdmin";
		AlertEntity[] response = restTemplate.getForObject(url, AlertEntity[].class);
		if (response == null) {
			return Collections.emptyList();
		}
		return Arrays.asList(response);
	}

	public List<NotificationEntity> getAllNotification(String merchantId) {
		String url = env.getProperty("intranetconnection") + "/api/NotificationListforMerchant?merchant_id="
				+ merchantId;
		NotificationEntity[] response = restTemplate.getForObject(url, NotificationEntity[].class);
		if (response == null) {
			return Collections.emptyList();
		}
		return Arrays.asList(response);
	}

	public List<NotificationEntity> getUnitNotification(String merchantId, String unitId) {
		String url = env.getProperty("intranetconnection") + "/api/NotificationListforUnit?merchant_id=" + merchantId
				+ "&unit_id=" + unitId;
		NotificationEntity[] response = restTemplate.getForObject(url, NotificationEntity[].class);
		if (response == null) {
			return Collections.emptyList();
		}
		return Arrays.asList(response);
	}

	// Merchant wise for ChargeBacks
	public List<BIPS_Charge_Back_Entity> getAllChargeBacks(String merchantId) {
		String url = env.getProperty("intranetconnection") + "/api/ChargeBackList?merchant_id=" + merchantId;
		BIPS_Charge_Back_Entity[] response = restTemplate.getForObject(url, BIPS_Charge_Back_Entity[].class);
		if (response == null) {
			return Collections.emptyList();
		}
		return Arrays.asList(response);
	}

	public List<BIPS_Charge_Back_Entity> getPendingTransactionMerchantList(String merchantId) {
		String url = env.getProperty("intranetconnection") + "/api/AllPendingChargeBack?merchant_id=" + merchantId;
		BIPS_Charge_Back_Entity[] response = restTemplate.getForObject(url, BIPS_Charge_Back_Entity[].class);
		if (response == null) {
			return Collections.emptyList();
		}
		return Arrays.asList(response);
	}

	public List<BIPS_Charge_Back_Entity> getRevertedTransactionMerchantList(String merchantId) {
		String url = env.getProperty("intranetconnection") + "/api/AllRevertedChargeBack?merchant_id=" + merchantId;
		BIPS_Charge_Back_Entity[] response = restTemplate.getForObject(url, BIPS_Charge_Back_Entity[].class);
		if (response == null) {
			return Collections.emptyList();
		}
		return Arrays.asList(response);
	}

	// Unit wise for ChargeBacks
	public List<BIPS_Charge_Back_Entity> getAllUnitChargeBacks(String merchantId, String unitId) {
		String url = env.getProperty("intranetconnection") + "/api/UnitChargeBackList?merchant_id=" + merchantId
				+ "&unit_id=" + unitId;
		BIPS_Charge_Back_Entity[] response = restTemplate.getForObject(url, BIPS_Charge_Back_Entity[].class);
		if (response == null) {
			return Collections.emptyList();
		}
		return Arrays.asList(response);
	}

	public List<BIPS_Charge_Back_Entity> getPendingTransactionUnitList(String merchantId, String unitId) {
		String url = env.getProperty("intranetconnection") + "/api/UnitPendingChargeBack?merchant_id=" + merchantId
				+ "&unit_id=" + unitId;
		BIPS_Charge_Back_Entity[] response = restTemplate.getForObject(url, BIPS_Charge_Back_Entity[].class);
		if (response == null) {
			return Collections.emptyList();
		}
		return Arrays.asList(response);
	}

	public List<BIPS_Charge_Back_Entity> getRevertedTransactionUnitList(String merchantId, String unitId) {
		String url = env.getProperty("intranetconnection") + "/api/UnitRevertedChargeBack?merchant_id=" + merchantId
				+ "&unit_id=" + unitId;
		BIPS_Charge_Back_Entity[] response = restTemplate.getForObject(url, BIPS_Charge_Back_Entity[].class);
		if (response == null) {
			return Collections.emptyList();
		}
		return Arrays.asList(response);
	}

	// Merchant wise for all Customer transaction
	public List<AllCustomTransaction> getCustomerTransactionForMerchant(String merchantId) {
		String url = env.getProperty("intranetconnection") + "/api/CustomerTransactionList?merchant_id=" + merchantId;
		AllCustomTransaction[] response = restTemplate.getForObject(url, AllCustomTransaction[].class);
		if (response == null) {
			return Collections.emptyList();
		}
		return Arrays.asList(response);
	}

	// Unit wise for all Customer transaction
	public List<AllCustomTransaction> getCustomerTransactionForUnit(String merchantId, String unitId) {
		String url = env.getProperty("intranetconnection") + "/api/UnitCustomerTransactionList?merchant_id="
				+ merchantId + "&unit_id=" + unitId;
		AllCustomTransaction[] response = restTemplate.getForObject(url, AllCustomTransaction[].class);
		if (response == null) {
			return Collections.emptyList();
		}
		return Arrays.asList(response);
	}

	public List<OutwardTransactionMonitoringTable> getAllTransactions(String userId, String Merchant_id) {
		String url = env.getProperty("intranetconnection") + "/api/TransactionList?user_id=" + userId + "&Merchant_id="
				+ Merchant_id;
		OutwardTransactionMonitoringTable[] response = restTemplate.getForObject(url,
				OutwardTransactionMonitoringTable[].class);
		if (response == null) {
			return Collections.emptyList();
		}
		return Arrays.asList(response);
	}

	public List<OutwardTransactionMonitoringTable> getUserTransaction(String userId, String DeviceId,
			String Merchant_id) {
		String url = env.getProperty("intranetconnection") + "/api/TransactionListForUser?user_id=" + userId
				+ "&Device_id=" + DeviceId + "&Merchant_id=" + Merchant_id;
		OutwardTransactionMonitoringTable[] response = restTemplate.getForObject(url,
				OutwardTransactionMonitoringTable[].class);
		if (response == null) {
			return Collections.emptyList();
		}
		return Arrays.asList(response);
	}

	public List<FeesAndChargesEntity> getFeesChargesTransactions(String merchantId) {
		String url = env.getProperty("intranetconnection") + "/api/FeesAndChargesList?merchant_id=" + merchantId;
		FeesAndChargesEntity[] response = restTemplate.getForObject(url, FeesAndChargesEntity[].class);
		if (response == null) {
			return Collections.emptyList();
		}
		return Arrays.asList(response);
	}

	public List<FeesAndChargesEntity> getUnitFeesChargesTransactions(String merchantId, String unitId) {
		String url = env.getProperty("intranetconnection") + "/api/UnitFeesAndChargesList?merchant_id=" + merchantId
				+ "&unit_id=" + unitId;
		FeesAndChargesEntity[] response = restTemplate.getForObject(url, FeesAndChargesEntity[].class);
		if (response == null) {
			return Collections.emptyList();
		}
		return Arrays.asList(response);
	}

	public List<ServiceReqEntity> getAllUnitservice(String merchantid, String unitid) {
		String url = env.getProperty("intranetconnection") + "/api/ServiceRequestUnitList?merchant_id=" + merchantid
				+ "&unit_id=" + unitid;
		ServiceReqEntity[] response = restTemplate.getForObject(url, ServiceReqEntity[].class);
		if (response == null) {
			return Collections.emptyList();
		}
		return Arrays.asList(response);
	}

	public List<Object> AccountStatementMerchantWise(String merchantid, String fromdate, String todate) {
		String url = env.getProperty("intranetconnection") + "/api/AccountStatementMerchantWise?merchant_id="
				+ merchantid + "&fromdate=" + fromdate + "&todate=" + todate;
		Object[] response = restTemplate.getForObject(url, Object[].class);
		if (response == null) {
			return Collections.emptyList();
		}
		return Arrays.asList(response);
	}

	public List<Object> AccountStatementMerchantUnitWise(String merchantid, String fromdate, String todate,
			String unitid) {
		String url = env.getProperty("intranetconnection") + "/api/AccountStatementMerchantUnitWise?merchant_id="
				+ merchantid + "&fromdate=" + fromdate + "&todate=" + todate + "&unit_id=" + unitid;
		Object[] response = restTemplate.getForObject(url, Object[].class);
		if (response == null) {
			return Collections.emptyList();
		}
		return Arrays.asList(response);
	}

	public List<Object> AccountStatementUserWise(String merchantid, String fromdate, String todate, String unitid,
			String userid) {
		String url = env.getProperty("intranetconnection") + "/api/AccountStatementUserWise?merchant_id=" + merchantid
				+ "&fromdate=" + fromdate + "&todate=" + todate + "&unit_id=" + unitid + "&user_id=" + userid;
		Object[] response = restTemplate.getForObject(url, Object[].class);
		if (response == null) {
			return Collections.emptyList();
		}
		return Arrays.asList(response);
	}

	public List<Object> AccountStatementDeviceWise(String merchantid, String fromdate, String todate, String unitid,
			String deviceid) {
		String url = env.getProperty("intranetconnection") + "/api/AccountStatementDeviceWise?merchant_id=" + merchantid
				+ "&fromdate=" + fromdate + "&todate=" + todate + "&unit_id=" + unitid + "&device_id=" + deviceid;
		Object[] response = restTemplate.getForObject(url, Object[].class);
		if (response == null) {
			return Collections.emptyList();
		}
		return Arrays.asList(response);
	}

	public String getCustomerPayDetails(String merchant_id, String device_id, String reference_number,
			String psuDeviceID) {
		String url = env.getProperty("intranetconnection") + "/api/getCustomerPaydetails?merchant_id=" + merchant_id
				+ "&device_id=" + device_id + "&reference_number=" + reference_number;
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("PSU_Device_ID", psuDeviceID);
		HttpEntity<EncryptionEntity> requestEntity = new HttpEntity<>(headers);
		try {
			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);
			return response.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}

	// Initiate charge backs
	public ResponseEntity<String> updateChargeBack(String userId, String sequenceid, String merchant_id) {
		String updateUrl = env.getProperty("intranetconnection") + "/api/InititedChargeBack";
		String urlWithParams = updateUrl + "?userid=" + userId + "&seqUniqueID=" + sequenceid + "&merchant_id="
				+ merchant_id;

		HttpHeaders headers = new HttpHeaders();
		HttpEntity<String> requestEntity = new HttpEntity<>(null, headers); // No body, but non-null headers

		try {
			ResponseEntity<String> response = restTemplate.exchange(urlWithParams, HttpMethod.POST, requestEntity,
					String.class);
			return response;
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

	// Approve charge backs
	public ResponseEntity<String> ApproveChargeBack(String userId, String sequenceid) {
		String updateUrl = env.getProperty("intranetconnection") + "/api/ws/revertMerchantFndTransfer";
		String urlWithParams = updateUrl + "?userid=" + userId + "&seqUniqueID=" + sequenceid;

		HttpHeaders headers = new HttpHeaders();
		HttpEntity<String> requestEntity = new HttpEntity<>(null, headers); // No body, but non-null headers

		try {
			ResponseEntity<String> response = restTemplate.exchange(urlWithParams, HttpMethod.POST, requestEntity,
					String.class);
			return response;
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

	// Create Device Service
	public String AddNotification(EncryptionEntity EncryptedString, String psuDeviceID) {
		String url = env.getProperty("intranetconnection") + "/api/AddNotification";
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("PSU_Device_ID", psuDeviceID);
		HttpEntity<EncryptionEntity> requestEntity = new HttpEntity<>(EncryptedString, headers);
		try {
			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
			return response.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}

	public String AddService(EncryptionEntity EncryptedString, String psuDeviceID) {
		String url = env.getProperty("intranetconnection") + "/api/AddServiceReq";
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("PSU_Device_ID", psuDeviceID);
		HttpEntity<EncryptionEntity> requestEntity = new HttpEntity<>(EncryptedString, headers);
		try {
			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
			return response.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}

	public List<NotificationEntity> getAllNotification(String merchantid, String unitid) {
		String url = env.getProperty("intranetconnection") + "/api/AllNotificationList?merchant_id=" + merchantid
				+ "&unit_id=" + unitid;
		NotificationEntity[] response = restTemplate.getForObject(url, NotificationEntity[].class);
		if (response == null) {
			return Collections.emptyList();
		}
		return Arrays.asList(response);
	}

	public List<AllCustomTransaction> getAllCustomerTransaction(String merchantid, String unitid,String fromdate,String todate,String type) {
		String url = env.getProperty("intranetconnection") + "/api/AllCustomerTransactionList?merchant_id=" + merchantid
				+ "&unit_id=" + unitid+"&fromdate="+fromdate+"&todate="+todate+"&type="+type;
		AllCustomTransaction[] response = restTemplate.getForObject(url, AllCustomTransaction[].class);
		if (response == null) {
			return Collections.emptyList();
		}
		return Arrays.asList(response);
	}

	public List<AllCustomTransaction> getallMerchantTransaction(String merchantid, String userid, String fromdate,String todate,String type) {
		String url = env.getProperty("intranetconnection") + "/api/AllTransactionListHistory?merchant_id=" + merchantid
				+ "&user_id=" + userid+"&fromdate="+fromdate+"&todate="+todate+"&type="+type;
		AllCustomTransaction[] response = restTemplate.getForObject(url,
				AllCustomTransaction[].class);
		if (response == null) {
			return Collections.emptyList();
		}
		return Arrays.asList(response);
	}

	public List<FeesAndChargesEntity> getAllFeesChargesTransactions(String merchantid, String unitid,String fromdate,String todate,String type) {
		String url = env.getProperty("intranetconnection") + "/api/AllFeesAndChargesList?merchant_id=" + merchantid
				+ "&unit_id=" + unitid+"&fromdate="+fromdate+"&todate="+todate+"&type="+type;
		FeesAndChargesEntity[] response = restTemplate.getForObject(url, FeesAndChargesEntity[].class);
		if (response == null) {
			return Collections.emptyList();
		}
		return Arrays.asList(response);
	}

	public List<ServiceReqEntity> getAllservice(String merchantid, String unitid) {
		String url = env.getProperty("intranetconnection") + "/api/AllServiceRequestList?merchant_id=" + merchantid
				+ "&unit_id=" + unitid;
		ServiceReqEntity[] response = restTemplate.getForObject(url, ServiceReqEntity[].class);
		if (response == null) {
			return Collections.emptyList();
		}
		return Arrays.asList(response);
	}

	public List<BIPS_Charge_Back_Entity> getAllListchargeback(String merchantid, String unitid,String fromdate,String todate) {
		String url = env.getProperty("intranetconnection") + "/api/AllChargeBackList?merchant_id=" + merchantid
				+ "&unit_id=" + unitid+"&fromdate="+fromdate+"&todate="+todate;
		BIPS_Charge_Back_Entity[] response = restTemplate.getForObject(url, BIPS_Charge_Back_Entity[].class);
		if (response == null) {
			return Collections.emptyList();
		}
		return Arrays.asList(response);
	}

	public List<BIPS_Charge_Back_Entity> getAllPendingTransaction(String merchantid, String unitid,String fromdate,String todate) {
		String url = env.getProperty("intranetconnection") + "/api/AllMerchantPendingChargeBack?merchant_id="
				+ merchantid + "&unit_id=" + unitid+"&fromdate="+fromdate+"&todate="+todate;
		BIPS_Charge_Back_Entity[] response = restTemplate.getForObject(url, BIPS_Charge_Back_Entity[].class);
		if (response == null) {
			return Collections.emptyList();
		}
		return Arrays.asList(response);
	}

	public List<BIPS_Charge_Back_Entity> getAllRevertedTransaction(String merchantid, String unitid,String fromdate,String todate) {
		String url = env.getProperty("intranetconnection") + "/api/AllMerchantRevertedChargeBack?merchant_id="
				+ merchantid + "&unit_id=" + unitid+"&fromdate="+fromdate+"&todate="+todate;
		BIPS_Charge_Back_Entity[] response = restTemplate.getForObject(url, BIPS_Charge_Back_Entity[].class);
		if (response == null) {
			return Collections.emptyList();
		}
		return Arrays.asList(response);
	}

}
