package com.rays.pro4.Util;

import java.util.HashMap;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;


import com.rays.pro4.Bean.DropdownListBean;
import com.rays.pro4.Model.BaseModel;

/**
 *  HTML Utility class to produce HTML contents like Dropdown List.
 *  
 * @author Lokesh SOlanki
 *
 */
public class HTMLUtility {\
    /**
     * Creates and returns HTML SELECT list from HashMap
     *
     * @param name        : Select HTML element name
     * @param selectedVal : Default selected value
     * @param map         : Value of Map
     * @return String : HTML SELECT list
     */
    public static String getList(String name, String selectedVal, HashMap<String, String> map) {
        StringBuffer sb = new StringBuffer("<select style='width: 203px;  height: 23px;' class='form-control' name='" + name + "'>");
        Set<String> keys = map.keySet();
        String val = null;
        boolean select = true;

        if (select) {
            sb.append("<option style='width: 203px;  height: 30px;' selected value=''>--------------Select---------------------`</option>");
        }

        for (String key : keys) {
            val = map.get(key);
            if (key.trim().equals(selectedVal)) {
                sb.append("<option selected value='" + key + "'>" + val + "</option>");
            } else {
                sb.append("<option value='" + key + "'>" + val + "</option>");
            }
        }
        sb.append("</select>");
        return sb.toString();
        }

    /**
     * Creates and returns HTML SELECT list from List
     *
     * @param name        : Select HTML element name
     * @param selectedVal : Default selected value
     * @param list        : List of DropdownListBean
     * @return String : HTML SELECT list
     */
    public static String getList(String name, String selectedVal, List list) {

        Collections.sort(list);
        StringBuffer sb = new StringBuffer("<select style='width: 203px;  height: 23px;' class='form-control' name='" + name + "'>");
        boolean select = true;
        if (select) {
            sb.append("<option style='width: 203px;  height: 30px;' selected value=''>--------------Select-----------------`</option>");
        }

        List<DropdownListBean> dd = (List<DropdownListBean>) list;
        String key = null;
        String val = null;

       for (DropdownListBean obj : dd) {
            key = String.valueOf(obj.getKey());
            val = obj.getValue();

            if (key.trim().equals(selectedVal)) {
                sb.append("<option selected value='" + key + "'>" + val + "</option>");
            } else {
                sb.append("<option value='" + key.trim() + "'>" + val + "</option>");
            }
        }
        sb.append("</select>");
        return sb.toString();
        }

    /**
     * Returns Error message with HTML tag and CSS
     *
     * @param request : request
     * @return String : Error message
     */
    public static String getErrorMessage(HttpServletRequest request) {
        String msg = ServletUtility.getErrorMessage(request);
        if (!DataValidator.isNull(msg)) {
            msg = "<p class='st-error-header'>" + msg + "</p>";
        }
        return msg;
        }

    /**
     * Returns Error message with HTML tag and CSS
     *
     * @param request
     * @return
     */
    public static String getErrorMessage(HttpServletRequest request) {
        String msg = ServletUtility.getErrorMessage(request);
        if (!DataValidator.isNull(msg)) {
            msg = "<p class='st-error-header'>" + msg + "</p>";
        }
       return msg;
        }
    /**
     * Returns Success message with HTML tag and CSS
     * @param request : request
     * @return
     */

    public static String getSuccessMessage(HttpServletRequest request) {
        String msg = ServletUtility.getSuccessMessage(request);
        if (!DataValidator.isNull(msg)) {
            msg = "<p class='st-success-header'>" + msg + "</p>";
        }
        return msg;
        }

    /**
     * Creates submit button if user has access permission
     *
     * @param label   : Submit button label
     * @param access  : User has access or not
     * @return
     */
    public static String getSubmitButton(String label, boolean access,
            HttpServletRequest request) {

        String button = "";

        if (access)
        {
            button = "<input type='submit' name='operation'    value='" + label + "' >";
        }
        return button;
        }

    /**
     * Returns hidden fields in HTML form for CreatedBy, ModifiedBy, CreatedDatetime, ModifiedDatetime
     * @param request : request
     * @return : HTML
     */
    public static String getCommonFields(HttpServletRequest request) {

        BaseModel model = ServletUtility.getModel(request);
        StringBuffer sb = new StringBuffer();

        sb.append("<input type='hidden' name='id' value=" + model.getId() + ">");
        return sb.toString();
        }

}
