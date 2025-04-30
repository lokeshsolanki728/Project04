package com.rays.pro4.Util;

import java.util.Calendar;
import java.util.Date;

/**
 * This class validates input data.
 * 
 * @author Lokesh SOlanki
 *
 */
public class DataValidator {

	/**
	 * Checks if value is Null
	 * 
	 * @param val
	 * @return
	 */
	public static boolean isNull(String val) {
		if (val == null || val.trim().length() == 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Checks if value is NOT Null
	 * 
	 * @param val
	 * @return
	 */
	public static boolean isNotNull(String val) {
		return !isNull(val);

	}
		
	}
	public static boolean isInteger(String val){
		if(isNotNull(val)){
			try{
				int i=Integer.parseInt(val);
				return true;
			}catch(NumberFormatException e){
					return false;
			}
		}else{
			return false;
		}
	}
	
		/**
	 * Checks if value is Long
	 * 
	 * @param val
	 * @return
	 */
	public static boolean isLong(String val){
		if(isNotNull(val)){
			try{
				long i=Long.parseLong(val);
				return true;
			}catch(NumberFormatException e){
				return false;
			}
		}else{
			return false;
		}
	} 
	/**
	 * Checks if value is Email ID
	 * 
	 * @param val
	 * @return
	 */


	  public static boolean isEmail(String val) {
		  
	        String emailreg = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
                                    
	        if (isNotNull(val)) {
	            try {
	                return val.matches(emailreg);
	            } catch (NumberFormatException e) {
	                return false;
	            }

	        } else {
	            return false;
	        }
	    }

	
	/**
	 * Checks if value is Date
	 * 
	 * @param val
	 * @return
	 */

	public static boolean isDate(String val){
		Date d=null;
		if(isNotNull(val)){
			d=DataUtility.getDate(val);
		}
		return d!=null;
	}
	
	/**
	 * Checks if value is Name
	 * 
	 * @param val
	 * @return
	 */

	public static boolean isName(String val) {

		String namereg = "^[^-\\s][\\p{L} .'-]+$";

		if (isNotNull(val)) {
			try {
				return val.matches(namereg);
			} catch (NumberFormatException e) {
				return false;
			}

		} else {
			return false;
		}
	}
	
	/**
	 * Checks if value is Password
	 * 
	 * @param val
	 * @return
	 */
	public static boolean isPassword(String val) {

		String passreg = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[^a-zA-Z0-9])(?!.*\\s).{8,15}$";

		if (isNotNull(val)) {
			try {
				return val.matches(passreg);
			} catch (NumberFormatException e) {
				return false;
			}

		} else {
			return false;
		}
	}
	
	/**
	 * Checks if value is Password length
	 * 
	 * @param val
	 * @return
	 */
	public static boolean isPasswordLength(String val) {

		if (isNotNull(val) && val.length() >= 8 && val.length() <= 12) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Checks if value of Mobile No is 10
	 * 
	 * @param val
	 * @return
	 */
	public static boolean isPhoneLength(String val) {

		if (isNotNull(val) && val.length() == 10) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Checks if value is Roll No
	 * 
	 * @param val
	 * @return
	 */

	public static boolean isRollNo(String val) {
		
		

		String rollreg = "^[a-zA-Z]{2}[0-9]{3,5}$";

		if (isNotNull(val)) {
			try {
				return val.matches(rollreg);
			} catch (NumberFormatException e) {
				return false;
			}

		} else {
			return false;
		}
	}
	
	/**
	 * Checks if value is Age
	 * 
	 * @param val
	 * @return
	 */
	public static boolean isAge(String val) {
		if (isNotNull(val)) {
			Date today = new Date();
			Date birthDate = DataUtility.getDate(val);

			if (birthDate.after(today)) {
				return false; // Birth date is in the future
			}

			Calendar calToday = Calendar.getInstance();
			calToday.setTime(today);
			Calendar calBirthDate = Calendar.getInstance();
			calBirthDate.setTime(birthDate);

			int age = calToday.get(Calendar.YEAR) - calBirthDate.get(Calendar.YEAR);

			if (calToday.get(Calendar.MONTH) < calBirthDate.get(Calendar.MONTH)
					|| (calToday.get(Calendar.MONTH) == calBirthDate.get(Calendar.MONTH)
							&& calToday.get(Calendar.DAY_OF_MONTH) < calBirthDate.get(Calendar.DAY_OF_MONTH))) {
				age--;
			}

			return age >= 18;
		}
		return false;
	}
	/**
	 * Checks if value is Mobile No
	 * @param val
	 * @return
	 */
	 public static boolean isMobileNo(String val){
	    	
	    	String mobreg = "^[6-9][0-9]{9}$";
	    	
	    			if (isNotNull(val) && val.matches(mobreg)) {
						
							return true;
	    				}else{
	    					
	    					return false;
						}	
	    		}
	 	
	
	public static void main(String[] args) {
		System.out.println("Not Null 2"+isNotNull("ABC"));
		System.out.println("Not Null 3"+isNotNull(null));
		System.out.println("Not Null 4"+isNull("123"));
		
		
		System.out.println("is int"+isInteger(null));
		System.out.println("Is int"+isInteger("ABC1"));
		System.out.println("Is Int"+isInteger("123"));
		System.out.println("is Int"+isNotNull("123"));
	}

}
