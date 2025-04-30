package com.rays.pro4.Exception;
/**
 * ApplicationException is propogated from Service classes when an business
 * logic exception occurered.
 * 
 * @author Lokesh SOlanki
 *
 */
public class ApplicationException extends Exception {
	
	private static final long serialVersionUID = 1L;

	public ApplicationException(String msg){
		super(msg);
	}
	
}
