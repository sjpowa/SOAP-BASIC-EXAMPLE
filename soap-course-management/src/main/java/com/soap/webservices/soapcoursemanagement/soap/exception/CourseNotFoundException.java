package com.soap.webservices.soapcoursemanagement.soap.exception;

import org.springframework.ws.soap.server.endpoint.annotation.FaultCode;
import org.springframework.ws.soap.server.endpoint.annotation.SoapFault;

// We extends the RuntimeException
//@SoapFault(faultCode = FaultCode.CLIENT) // with SOAP Fault we are going to set the Fault to the client
										 // when he/she will search for an
										 // id that doesn't exist
@SoapFault(faultCode = FaultCode.CUSTOM, customFaultCode = "{http://in28minutes.com/courses}Id_not_found") // in the {} we have defined a namespace
public class CourseNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CourseNotFoundException(String message) {
		super(message);
	}

}
