/**
 * 
 */
package com.example.round2.controller;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.round2.constants.Constants;
import com.example.round2.service.UserService;

/**
 * 
 */

@RestController
@RequestMapping("/kyc")
public class UseController {

	@Autowired
	UserService userService;

	@GetMapping("userdetails/{msisdn}")
	public JSONObject getUserDetails(@PathVariable(required = true) String msisdn) {

		return userService.getUserDetails(msisdn);

	}

}