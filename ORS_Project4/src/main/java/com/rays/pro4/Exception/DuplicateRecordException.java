package com.rays.pro4.Exception;

import org.apache.log4j.Logger;

/**
 * DuplicateRecordException thrown when a duplicate record occurred.
 * 
 * @author Lokesh SOlanki
 *
 */

public class DuplicateRecordException extends Exception {

	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(DuplicateRecordException.class);
	
	public DuplicateRecordException(String msg){
		super(msg);
		log.debug("Duplicate Record Exception : ", this);
		printStackTrace();

	}
}
