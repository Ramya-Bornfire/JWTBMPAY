package com.bornfire.service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.bornfire.entity.UnitManagementEntity;

@Service
public class UnitService {

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	Environment env;

	// Merchant wise for Device Management list
	public List<UnitManagementEntity> getAllUnit(String merchantId) {
		String url = env.getProperty("intranetconnection") + "/api/UnitList?merchant_id=" + merchantId;
		UnitManagementEntity[] response = restTemplate.getForObject(url, UnitManagementEntity[].class);
		if (response == null) {
			return Collections.emptyList();
		}
		return Arrays.asList(response);
	}

}
