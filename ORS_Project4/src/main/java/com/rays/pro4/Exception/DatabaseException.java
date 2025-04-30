package com.rays.pro4.Exception;

/**
 * DatabaseException is propogated by DAO classes when an unhandled Database
 * exception occurred.
 * 
 * @author Lokesh SOlanki
 *
 */

public class DatabaseException extends Exception {

	private static final long serialVersionUID = 1L;
	public DatabaseException(String msg){
		super(msg);
	}
	
}
