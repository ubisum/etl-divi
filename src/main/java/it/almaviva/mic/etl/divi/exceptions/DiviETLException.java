package it.almaviva.mic.etl.divi.exceptions;

import org.springframework.http.HttpStatus;

public class DiviETLException extends RuntimeException
{ 
	private static final long serialVersionUID = -647021387043820310L;
	private HttpStatus status;
	
	public DiviETLException(String message)
	{
		super(message);
	}
	
	public DiviETLException(String message, HttpStatus status)
	{
		super(message);
		this.setStatus(status);
	}

	public HttpStatus getStatus() {
		return status;
	}

	public void setStatus(HttpStatus status) {
		this.status = status;
	}
	
	
}
