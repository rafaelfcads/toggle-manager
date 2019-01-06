package com.it.togglemanager.domain.user;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.it.togglemanager.domain.toggle.Toggle;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode
@ToString
@Document
public class User {
	
	@Id
	private String id;
	
	@Indexed(unique=true)
	private String userName;
	
	private String password;
	
	private String version;
	
	private String[] roles;
	
	@DBRef
	@Field("toggles")
	private List<Toggle> toggles;
	
}