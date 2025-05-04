package com.rays.pro4.Exception;

import org.apache.log4j.Logger;
/**
 * ApplicationException is propogated from Service classes when an business
 * logic exception occurered.
 * 
 * @author Lokesh SOlanki
 *
 */
public class ApplicationException extends Exception {
	
	private static final long serialVersionUID = 1L;
    private static Logger log = Logger.getLogger(ApplicationException.class);
	public ApplicationException(String msg){
        log.debug("ApplicationException : Started");
        printStackTrace();
		super(msg);
        log.debug("ApplicationException : End");
	}
	
}
