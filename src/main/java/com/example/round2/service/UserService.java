/**
 * 
 */
package com.example.round2.service;

import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

/**
 * 
 */

public interface UserService {

	String getBearerToken();

	JSONObject getUserDetails(String msisdn);

}
