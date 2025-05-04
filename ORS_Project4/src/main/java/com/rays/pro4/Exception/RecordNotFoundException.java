package com.rays.pro4.Exception;

import org.apache.log4j.Logger;

/**
 * RecordNotFoundException thrown when a record not found occurred.
 * 
 * @author Lokesh SOlanki
 * 
 */
public class RecordNotFoundException extends Exception {
    private static Logger log = Logger.getLogger(RecordNotFoundException.class);
    private static final long serialVersionUID = 1L;

    public RecordNotFoundException(String msg) {
        super(msg);
        log.debug("message--" + msg);
        printStackTrace();
        log.error(msg);
    }
}
