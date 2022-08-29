package com.cts.authorization.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.cts.authorization.exception.InvalidCredentialsException;
import com.cts.authorization.model.UserRequest;
import com.cts.authorization.util.JwtUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * Authorization Controller to handle requests for logging in a valid user and
 * validating the JWT Token for other services.
 * 
 * 
 */
@RestController
@Slf4j
@CrossOrigin
public class AuthorizationController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private UserDetailsService userService;

	@Value("${userDetails.badCredentialsMessage}")
	private String BAD_CREDENTIALS;

	
	/**
	 * @URL: http://localhost:8081/login
	 * 
	 * @Data: [Admin] { "username": "Raman1", "password": "pass@1234" }
	 * 
	 * @param userRequest {username, password}
	 * 
	 */
	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestBody @Valid UserRequest userRequest) {
		log.info("START - login()");
		try {
			Authentication authenticate = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(userRequest.getUsername(), userRequest.getPassword()));
			if (authenticate.isAuthenticated()) {
				log.info("Valid User detected - logged in");
			}
		} catch (BadCredentialsException e) {
			throw new InvalidCredentialsException(BAD_CREDENTIALS);
		}

		String token = jwtUtil.generateToken(userRequest.getUsername());
		log.info("END - login()");
		return new ResponseEntity<>(token, HttpStatus.OK);
	}

	/**
	 * Checks if the token is a valid administrator token
	 * 
	 * @URL: http://localhost:8081/validate
	 * 
	 * @Header: [Authorization] = JWT Token
	 * 
	 * @param token
	 * @return
	 */
	@GetMapping("/validate")
	public ResponseEntity<Boolean> validateAdmin(@RequestHeader(name = "Authorization") String token) {
		log.info("START - validateAdmin()");

		// throws custom exception and response if token is invalid
		jwtUtil.isTokenExpiredOrInvalidFormat(token);

		// else the user is loaded and role is checked, if role is valid, access is
		// granted
		UserDetails user = userService.loadUserByUsername(jwtUtil.getUsernameFromToken(token));
		if (user.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN"))) {
			log.info("END - validateAdmin()");
			return new ResponseEntity<>(true, HttpStatus.OK);
		}

		return new ResponseEntity<>(false, HttpStatus.UNAUTHORIZED);

	}

}
