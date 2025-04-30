package com.rays.pro4.Exception;

/**
 * DuplicateRecordException thrown when a duplicate record occurred.
 * 
 * @author Lokesh SOlanki
 *
 */

public class DuplicateRecordException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public DuplicateRecordException(String msg){
		super(msg);
	}

	
}
