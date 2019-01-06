package com.it.togglemanager.config.kafka;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString
@EqualsAndHashCode
public class Greeting {
	
	private String msg;
    
	private String name;
	
}
