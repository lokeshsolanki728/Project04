package com.rays.pro4.Bean;

import javax.servlet.http.HttpServletRequest;

/**
 * DropdownList interface is implemented by Beans those are used to create drop
 * down list on HTML pages.
 * 
 * @author Lokesh SOlanki
 *
 */

public interface DropdownListBean {

	/**
	 * Returns key of list element
	 * @return key
	 */
	public abstract String getkey();

	/**
	 * Returns display text of list element
	 * 
	 * @return value
	 */
	public abstract String getValue();

	    /**
	     * Populate bean object from request parameters.
	     *
	     * @param request the request
	     */
	    public abstract void populate(HttpServletRequest request);

}
