package com.example.round2.service;

import java.util.Map;

import org.json.simple.JSONObject;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.example.round2.constants.Constants;

@Service
public class UserServiceImpl implements UserService {

	private final RestTemplate restTemplate = new RestTemplate();

	@SuppressWarnings("unchecked")
	@Override
	public String getBearerToken() {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.set("Content-Type", "application/json");

			JSONObject requestBody = new JSONObject();
			requestBody.put("grant_type", Constants.GRANT_TYPE);
			requestBody.put("client_id", Constants.CLIENT_ID);
			requestBody.put("client_secret", Constants.CLIENT_SECRET);

			HttpEntity<JSONObject> request = new HttpEntity<>(requestBody, headers);

			ResponseEntity<JSONObject> response = restTemplate.exchange(Constants.TOKEN_URL, HttpMethod.POST, request,
					JSONObject.class);

			if (response.getStatusCode().is2xxSuccessful()) {
				return (String) response.getBody().get(Constants.ACCESS_TOKEN);
			} else {
				throw new RuntimeException("Failed to retrieve bearer token. Status Code: " + response.getStatusCode());
			}
		} catch (HttpClientErrorException.Unauthorized ex) {
			throw new RuntimeException("Invalid authentication credentials", ex);
		} catch (Exception e) {
			throw new RuntimeException("An error occurred while retrieving the bearer token", e);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public JSONObject getUserDetails(String msisdn) {
		try {
			String userUrl = Constants.USER_URL;
			String bearerToken = getBearerToken();
			final String url = userUrl + msisdn;

			HttpHeaders headers = new HttpHeaders();
			headers.set("Content-Type", "application/json");
			headers.set(Constants.Country_HEADER, Constants.UGANDA_COUNTRY_CODE);
			headers.set(Constants.Currency_HEADER, Constants.UGANDA_CURRENCY_CODE);
			headers.set("Authorization", "Bearer " + bearerToken);

			HttpEntity<?> requestEntity = new HttpEntity<>(headers);
			ResponseEntity<Map<String, Object>> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity,
					new ParameterizedTypeReference<Map<String, Object>>() {
					});

			if (response.getStatusCode().is2xxSuccessful()) {
				Map<String, Object> responseBody = response.getBody();
				Map<String, Object> data = (Map<String, Object>) responseBody.get("data");

				JSONObject finalResponse = new JSONObject();
				finalResponse.put("full_name", data.get("first_name") + " " + data.get("last_name"));

				Map<String, Object> registration = (Map<String, Object>) data.get("registration");
				finalResponse.put("registration_status", registration.get("status"));

				return finalResponse;
			} else {
				throw new RuntimeException("Failed to retrieve user details. Status Code: " + response.getStatusCode());
			}
		} catch (HttpClientErrorException.Unauthorized ex) {
			throw new RuntimeException("Unauthorized. Check  authentication credentials", ex);
		} catch (Exception e) {
			throw new RuntimeException("An error occurred while retrieving user details", e);
		}
	}

}
