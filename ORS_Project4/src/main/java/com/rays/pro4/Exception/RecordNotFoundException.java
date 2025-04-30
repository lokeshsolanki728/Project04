package com.rays.pro4.Exception;
/**
 * RecordNotFoundException thrown when a record not found occurred.
 * 
 * @author Lokesh SOlanki
 * 
 */
public class RecordNotFoundException extends Exception {
	private static final long serialVersionUID = 1L;
	public RecordNotFoundException(String msg){
		super(msg);
	}
}
