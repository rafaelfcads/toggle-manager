package com.it.togglemanager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;

import com.it.togglemanager.domain.user.CustomUserDetail;
import com.it.togglemanager.domain.user.UserRepository;

@SpringBootApplication
public class ToggleManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ToggleManagerApplication.class, args);
	}
	
	@Autowired
	public void authenticationManager(AuthenticationManagerBuilder builder, UserRepository repo) throws Exception {
		builder.userDetailsService(name -> new CustomUserDetail(repo.findByUserName(name)));
	}
}
