package com.rays.pro4.Util;

import java.util.HashMap;

/**
 *  Class that build Application Email messages.
 *
 * @author Lokesh SOlanki
 *
 */
public class EmailBuilder {

	/**
	 * Returns Successful User Registration Message
	 *
	 * @param map
	 *            : Message parameters
	 * @return String
	 */
    public static String getUserRegistrationMessage(HashMap<String, String> map) {
        StringBuilder msg = new StringBuilder();
       
        String orsUrl = PropertyReader.getValue("ors.url");
        String helplineNo = PropertyReader.getValue("ors.helplineNo");
        String email = PropertyReader.getValue("ors.email");
        String webUrl = PropertyReader.getValue("ors.webUrl");
        String company = PropertyReader.getValue("ors.companyName");
        msg.append("<HTML><BODY>");
        msg.append("Registration is successful for ORS Project "+company);
        msg.append("<H1>Hi! Greetings from "+company+"!</H1>");
        msg.append("<P>Congratulations for registering on ORS! You can now access your ORS account online - anywhere, anytime and enjoy the flexibility to check the Marksheet Details.</P>");
        msg.append("<P>Log in today at <a href='"+orsUrl+"'>"+orsUrl+"</a> with your following credentials:</P>");
        msg.append("<P><B>Login Id : " + map.get("login") + "<BR>"
                + " Password : " + map.get("password") + "</B></p>");

        msg.append("<P> As a security measure, we recommended that you change your password after you first log in.</p>");
        msg.append("<p>For any assistance, please feel free to call us at "+helplineNo+" helpline numbers.</p>");
        msg.append("<p>You may also write to us at "+email+".</p>");
        msg.append("<p>We assure you the best service at all times and look forward to a warm and long-standing association with you.</p>");
        msg.append("<P><a href='"+webUrl+"' >-"+company+"</a></P>");
        msg.append("</BODY></HTML>");

        return msg.toString();
    }

	/**
	 * Returns Email message of Forget Password
	 *
	 * @param map : params
	 * @return String
	 */

    public static String getForgetPasswordMessage(HashMap<String, String> map) {
        StringBuilder msg = new StringBuilder();
        String orsUrl = PropertyReader.getValue("ors.url");
      
        msg.append("<HTML><BODY>");
        msg.append("<div>");
        msg.append("<H1>Dear " + map.get("firstName")
                + " " + map.get("lastName") + ",</H1>");
        msg.append("<P>We have received a request to recover your password.</P>");
        msg.append("<P>Here are your login credentials:</P>");
        msg.append("<P>Please login into ORS  <a href='"+orsUrl+"'>"+orsUrl+"</a></P>");
        msg.append("<P><B>Please login into ORS and change your password. </B></p>");
       msg.append("<P><B>To access account user Login Id : "
                + map.get("login") + "<BR>" + " Password : "
                + map.get("password") + "</B></p>");
        msg.append("<div>");
        

        msg.append("<P><B>To access account user Login Id : "
                + map.get("login") + "<BR>" + " Password : "
                + map.get("password") + "</B></p>");
        msg.append("</BODY></HTML>");

        return msg.toString();
    }

	/**
	 * Returns Email message of Change Password
	 *
	 * @param map : params
	 * @return String
	 */
    public static String getChangePasswordMessage(HashMap<String, String> map) {
        StringBuilder msg = new StringBuilder();
      
        msg.append("<HTML><BODY>");
        msg.append("<H1>Your Password has been changed Successfully !! "
                + map.get("firstName") + " " + map.get("lastName") + "</H1>");
        msg.append("<P><B>To access account user Login Id : "
                + map.get("login") + "<BR>" + " Password : "
                + map.get("password") + "</B></p>");
        msg.append("</BODY></HTML>");

        return msg.toString();
    }

}