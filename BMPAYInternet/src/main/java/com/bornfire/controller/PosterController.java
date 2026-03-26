package com.bornfire.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.bornfire.entity.InfoTableEntity;
import com.bornfire.entity.PosterEntity;

@RestController
@RequestMapping("/images")
public class PosterController {

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	Environment env;

	// Poster List
	@GetMapping("/PosterList")
	public List<PosterEntity> getAllPosterdetails(@RequestParam String merchant_user_id) {
		String url = env.getProperty("intranetconnection") + "/images/PosterList?merchant_user_id=" + merchant_user_id;
		PosterEntity[] response = restTemplate.getForObject(url, PosterEntity[].class);
		if (response == null) {
			return Collections.emptyList();
		}
		return Arrays.asList(response);
	}

	// Upload photos
	@PostMapping("/upload")
	public String uploadImage(@RequestParam("file") MultipartFile file, @RequestParam String unit_name,
			@RequestParam("date") @DateTimeFormat(pattern = "dd-MM-yyyy") Date date, @RequestParam String unit_id,
			@RequestParam String merchant_id, @RequestParam String merchant_rep_id, @RequestParam String frequency,
			@RequestParam("from_date") @DateTimeFormat(pattern = "dd-MM-yyyy") Date from_date,
			@RequestParam("to_date") @DateTimeFormat(pattern = "dd-MM-yyyy") Date to_date) throws IOException {
		String uploadUrl = env.getProperty("intranetconnection") + "/upload";

		// Format the dates
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		String formattedDate = dateFormat.format(date);
		String formattedFromDate = dateFormat.format(from_date);
		String formattedToDate = dateFormat.format(to_date);

		// Prepare the multipart request
		MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
		body.add("file", new ByteArrayResource(file.getBytes()) {
			@Override
			public String getFilename() {
				return file.getOriginalFilename();
			}
		});
		body.add("unit_name", unit_name);
		body.add("date", formattedDate);
		body.add("unit_id", unit_id);
		body.add("merchant_id", merchant_id);
		body.add("merchant_rep_id", merchant_rep_id);
		body.add("frequency", frequency);
		body.add("from_date", formattedFromDate);
		body.add("to_date", formattedToDate);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);

		HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

		ResponseEntity<String> response = restTemplate.exchange(uploadUrl, HttpMethod.POST, requestEntity,
				String.class);
		return response.getBody();

	}
	
	
	

}
