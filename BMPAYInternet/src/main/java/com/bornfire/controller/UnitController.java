package com.bornfire.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bornfire.entity.UnitManagementEntity;
import com.bornfire.service.UnitService;

@RestController
@RequestMapping("/api")
public class UnitController {
	@Autowired
	UnitService service;

	 @GetMapping("/UnitList")
		public List<UnitManagementEntity> UnitList(@RequestParam String merchant_id) {
			return service.getAllUnit(merchant_id);
		}
}
