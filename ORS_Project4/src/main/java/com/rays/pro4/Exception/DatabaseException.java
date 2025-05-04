package com.rays.pro4.Exception;

import org.apache.log4j.Logger;

/**
 * DatabaseException is propogated by DAO classes when an unhandled Database
 * exception occurred.
 * 
 * @author Lokesh SOlanki
 *
 */

public class DatabaseException extends Exception {

	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(DatabaseException.class);
	public DatabaseException(String msg){
		super(msg);
		log.debug(msg);
		log.error(this);
	}
	
}
