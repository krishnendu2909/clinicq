package com.infy.utility;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ErrorInfo {
	
	private Integer errorCode;
	private String errorMessage;
	private LocalDateTime timestamp;

}
