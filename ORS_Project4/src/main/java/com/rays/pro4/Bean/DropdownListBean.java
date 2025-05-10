package com.rays.pro4.Bean;


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
	public abstract String getKey();

	/**
	 * Returns display text of list element
	 * 
	 * @return value
	 */
	public abstract String getValue();
	
}
