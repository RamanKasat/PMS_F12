package com.cts.authorization.service;

import java.util.Collections;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.cts.authorization.exception.InvalidCredentialsException;
import com.cts.authorization.model.User;
import com.cts.authorization.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserServiceImpl implements UserDetailsService {

	@Value("${userDetails.errorMessage}")
	private String USER_DOES_NOT_EXISTS_MESSAGE;

	@Autowired
	private UserRepository userRepository;
	

	/**
	 * Loads user from the database if exists and compares with the given username.
	 *
	 * @param username
	 * @return UserDetails
	 */
	@Override
	public UserDetails loadUserByUsername(String username) {
		Optional<User> userOptional = userRepository.findById(username);
		if (userOptional.isEmpty()) {
			throw new InvalidCredentialsException(USER_DOES_NOT_EXISTS_MESSAGE);
		} else {
			log.info("Username: {} is valid", username);
			User user = userOptional.get();
			return new org.springframework.security.core.userdetails.User(username, user.getPassword(),
					Collections.singletonList(new SimpleGrantedAuthority(user.getRole())));
		}
	}

}
