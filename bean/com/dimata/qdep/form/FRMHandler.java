/*
 * FRMHandler.java
 *
 * Created on March 13, 2002, 10:31 AM
 */
package com.dimata.qdep.form;

/**
 *
 * @author gmudiasa
 * @version
 *
 * @update Kartika ; 2015-09-08 : * fitur type paramaters vector of Integer,
 * Long, String
 */
import java.util.*;

import javax.servlet.*;
import java.text.*;
import javax.servlet.http.*;

public class FRMHandler implements I_FRMType {

    public static final String YR = "_yr";
    public static final String MN = "_mn";
    public static final String DY = "_dy";
    public static final String MM = "_mm";
    public static final String HH = "_hh";
    /**
     * NIA
     */
    public static final String HR = "_hr";
    public static final String MI = "_mi";
    public static final String SEC = "_sec";
    public static String USER_DECIMAL_SYMBOL = ".";
    public static String USER_DIGIT_GROUP = ",";
    public static final String SYSTEM_DECIMAL_SYMBOL = ".";
    public static final String SYSTEM_DIGIT_GROUP = ",";
    private String formName;
    private String[] fieldNames;
    private int[] fieldTypes;
    private HttpServletRequest req;
    private Vector paramValues;
    private Hashtable hsErrors;
    private String msgStyle = "errfont";
    //added by eka,
    //secure from any injection
    private String[] specialChar = {"+or+", "+and+", "<", ">", "'", "/*", "\"", "--", ";"};
    private String[] specialCharReplace = {" or ", " and ", "&lt;", "&gt;", "", "", "", "", ""};

    public FRMHandler() {
    }

    /**
     * Creates new FRMHandler
     */
    public FRMHandler(I_FRMInterface ifrm, HttpServletRequest request) {
        formName = ifrm.getFormName();
        fieldNames = ifrm.getFieldNames();
        fieldTypes = ifrm.getFieldTypes();

        paramValues = new Vector(1, 1);
        hsErrors = new Hashtable();
        req = request;
    }

    // ----------------------------------------------- Request parameter
    public void requestParam() {
        String value = "";
        paramValues.clear();
        String[] values = null;

        for (int i = 0; i < fieldNames.length; i++) {

            switch (fieldTypes[i] & FILTER_TYPE) {
                case TYPE_DATE:
                    value = getParamDate(fieldNames[i]);
                    break;
                case TYPE_SPECIALSTRING:
                    value = req.getParameter(fieldNames[i]);
                    break;
                    
                case TYPE_VECTOR_FLOAT:
                case TYPE_VECTOR_INT:
                case TYPE_VECTOR_LONG:
                case TYPE_VECTOR_STRING:
                    values = req.getParameterValues(fieldNames[i]);
                    break;

                default:
                    value = req.getParameter(fieldNames[i]);

                    //System.out.println("--Val before cek "+fieldNames[i]+" : " + value);
                    value = checkValueForInjection(value);
            }

            switch (fieldTypes[i] & FILTER_TYPE) {
                case TYPE_VECTOR_FLOAT:
                case TYPE_VECTOR_INT:
                case TYPE_VECTOR_LONG:
                case TYPE_VECTOR_STRING:
                    paramValues.add(values);
                    /**/
                    try {

                        checkParamEntryVector(i);
                        //checkParamFormat(i);
                        checkParamTypeVector(i);

                    } catch (Exception e) {
                    }
                    /**/
                    break;
                    
                default: // for non vector
                    if (value == null) {
                        value = "";
                    } else {
                        value = value.trim();
                    }

                    paramValues.add(value);
                    /**/
                    try {

                        checkParamEntry(i);
                        checkParamFormat(i);
                        checkParamType(i);

                    } catch (Exception e) {
                    }
                /**/
            }
        }
    }

    /**
     * create by satrya ramayu 2014-07-12 mencari request form yg multiple
     * select
     */
    public void requestParamMultipleSelected(Vector list, String selected, String FormName) {
        String value = "";
        paramValues.clear();
        boolean sudahSesuaiData = false;
        if (list != null && list.size() > 0) {
            for (int ls = 0; ls < list.size(); ls++) {
                String selecteds = req.getParameter(FormName + ls);
                if (selecteds != null && selected != null && selecteds.equalsIgnoreCase(selected)) {
                    sudahSesuaiData = true;
                    for (int i = 0; i < fieldNames.length; i++) {
                        switch (fieldTypes[i] & FILTER_TYPE) {
                            case TYPE_DATE:
                                value = getParamDate(fieldNames[i] + ls);
                                break;
                            default:
                                value = req.getParameter(fieldNames[i] + ls);
                                value = checkValueForInjection(value);
                        }
                        if (value == null) {
                            value = "";
                        } else {
                            value = value.trim();
                        }
                        paramValues.add(value);
                        /**/
                        try {
                            checkParamEntry(i);
                            checkParamFormat(i);
                            checkParamType(i);
                        } catch (Exception e) {
                        }
                        /**/
                    }
                }
            }
        }//end cek vector
        if (sudahSesuaiData == false) {
            int tot = list != null ? list.size() + 1 : 1;
            for (int i = 0; i < fieldNames.length; i++) {
                switch (fieldTypes[i] & FILTER_TYPE) {
                    case TYPE_DATE:
                        value = getParamDate(fieldNames[i] + tot);
                        break;
                    default:
                        value = req.getParameter(fieldNames[i] + tot);
                        value = checkValueForInjection(value);
                }
                if (value == null) {
                    value = "";
                } else {
                    value = value.trim();
                }
                paramValues.add(value);
                /**/
                try {
                    checkParamEntry(i);
                    checkParamFormat(i);
                    checkParamType(i);
                } catch (Exception e) {
                }
                /**/
            }
        }

    }

    public String checkValueForInjection(String value) {
        if (value != null && value.length() > 0) {
            String specChar = "";
            int index = -1;
            boolean stop = false;
            for (int i = 0; i < specialChar.length; i++) {
                specChar = specialChar[i];
                stop = false;

//                System.out.println("specChar : "+specChar);
                while (!stop) {
                    index = value.indexOf(specChar);
//                    System.out.println("index : "+index);
                    //if exist
                    if (index > -1) {
                        value = replaceString(index, value, specChar, specialCharReplace[i]);
                    } else {
                        stop = true;
                    }
                }
            }
        }

        return value;
    }

    public String[] checkValueForInjection(String[] values) {
        if (values != null && values.length > 0) {
            String specChar = "";
            String value = "";
            int index = -1;
            boolean stop = false;
            for (int c = 0; c < values.length; c++) {
                value = values[c];
                for (int i = 0; i < specialChar.length; i++) {
                    specChar = specialChar[i];
                    stop = false;

//                System.out.println("specChar : "+specChar);
                    while (!stop) {
                        index = value.indexOf(specChar);
//                    System.out.println("index : "+index);
                        //if exist
                        if (index > -1) {
                            value = replaceString(index, value, specChar, specialCharReplace[i]);
                        } else {
                            stop = true;
                        }
                    }
                }
                values[c] = value;
            }
        }

        return values;
    }

    public String replaceString(int index, String value, String specChar, String charReplace) {

//        System.out.println("--index : "+index);
//        System.out.println("--value : "+value);
//        System.out.println("--specChar : "+specChar);
//        System.out.println("--charReplace : "+charReplace);
        if (index == 0) {
            value = charReplace + value.substring(index + specChar.length(), value.length());
        } else {
            String st1 = "";
            String st2 = "";
            st1 = value.substring(0, index);
            st2 = value.substring(index + specChar.length(), value.length());
            value = st1 + charReplace + st2;
//            System.out.println("--st1 : "+st1);
//	        System.out.println("--st2 : "+st2);
        }

        return value;
    }

    private void checkParamType(int idx) {
        String value = "";
        try {
            value = (String) paramValues.get(idx);
        } catch (Exception e) {
            return;
        }

        switch (fieldTypes[idx] & FILTER_TYPE) {
            case TYPE_INT:
                if (value.length() < 1) {
                    if ((fieldTypes[idx] & FILTER_ENTRY) == ENTRY_REQUIRED) {
                        this.addError(idx, FRMMessage.getErr(FRMMessage.ERR_REQUIRED));
                    }
                } else {
                    try {
                        value = deFormatStringInteger(value);
                        int val = Integer.parseInt(value);
                    } catch (Exception e) {
                        this.addError(idx, FRMMessage.getErr(FRMMessage.ERR_TYPE));
                    }
                }
                break;

            case TYPE_STRING:
                break;

            case TYPE_FLOAT:
                if (value.length() < 1) {
                    if ((fieldTypes[idx] & FILTER_ENTRY) == ENTRY_REQUIRED) {
                        //System.out.println("--------------->in check param type error");
                        //System.out.println("--------------->value = "+value);
                        //System.out.println("--------------->length = "+value.length());
                        this.addError(idx, FRMMessage.getErr(FRMMessage.ERR_REQUIRED));
                    }
                } else {
                    try {
                        value = deFormatStringDecimal(value);
                        float val = Float.parseFloat(value);
                    } catch (Exception e) {
                        this.addError(idx, FRMMessage.getErr(FRMMessage.ERR_TYPE));
                    }
                }
                break;

            case TYPE_BOOL:
                break;

            case TYPE_DATE:
                break;

            case TYPE_BLOB:
                break;

            case TYPE_LONG:
                if (value.length() < 1) {
                    if ((fieldTypes[idx] & FILTER_ENTRY) == ENTRY_REQUIRED) {
                        this.addError(idx, FRMMessage.getErr(FRMMessage.ERR_REQUIRED));
                    }
                } else {
                    try {
                        value = deFormatStringInteger(value);
                        long val = Long.parseLong(value);
                    } catch (Exception e) {
                        this.addError(idx, FRMMessage.getErr(FRMMessage.ERR_TYPE));
                    }
                }
                break;

            case TYPE_NUMERIC:
                break;

            case TYPE_COLLECTION:
                break;

            default:

        }// end switch
    }

    private void checkParamTypeVector(int idx) {
        String[] values = null;
        try {
            values = (String[]) paramValues.get(idx);
        } catch (Exception e) {
            return;
        }

        if (values.length > 0) {
            String value = "";
            for (int vi = 0; vi < values.length; vi++) {
                value = values[vi];
                switch (fieldTypes[idx] & FILTER_TYPE) {
                    case TYPE_VECTOR_INT:
                        if (value.length() < 1) {
                            if ((fieldTypes[idx] & FILTER_ENTRY) == ENTRY_REQUIRED) {
                                this.addError(idx, FRMMessage.getErr(FRMMessage.ERR_REQUIRED));
                                return;
                            }
                        } else {
                            try {
                                value = deFormatStringInteger(value);
                                int val = Integer.parseInt(value);
                            } catch (Exception e) {
                                this.addError(idx, FRMMessage.getErr(FRMMessage.ERR_TYPE));
                                return;
                            }
                        }
                        break;

                    case TYPE_VECTOR_STRING:
                        return;
                        

                    case TYPE_VECTOR_FLOAT:
                        if (value.length() < 1) {
                            if ((fieldTypes[idx] & FILTER_ENTRY) == ENTRY_REQUIRED) {
                                this.addError(idx, FRMMessage.getErr(FRMMessage.ERR_REQUIRED));
                                return;
                            }
                        } else {
                            try {
                                value = deFormatStringDecimal(value);
                                float val = Float.parseFloat(value);
                            } catch (Exception e) {
                                this.addError(idx, FRMMessage.getErr(FRMMessage.ERR_TYPE));
                                return;
                            }
                        }
                        break;

                    case TYPE_VECTOR_LONG:
                        if (value.length() < 1) {
                            if ((fieldTypes[idx] & FILTER_ENTRY) == ENTRY_REQUIRED) {
                                this.addError(idx, FRMMessage.getErr(FRMMessage.ERR_REQUIRED));
                                return;
                            }
                        } else {
                            try {
                                value = deFormatStringInteger(value);
                                long val = Long.parseLong(value);
                            } catch (Exception e) {
                                this.addError(idx, FRMMessage.getErr(FRMMessage.ERR_TYPE));
                                return;
                            }
                        }
                        break;
                    default:
                        return;
                }// end switch
            }
        }
    }

    private void checkParamFormat(int idx) {
        String value = "";
        try {
            value = (String) paramValues.get(idx);
        } catch (Exception e) {
            return;
        }

        switch (fieldTypes[idx] & FILTER_FORMAT) {
            case FORMAT_TEXT:
                break;

            case FORMAT_EMAIL:
                break;

            case FORMAT_CURENCY:
                break;

            default:
        }
    }

    private void checkParamEntry(int idx) {
        String value = "";
        try {
            value = (String) paramValues.get(idx);
        } catch (Exception e) {
            return;
        }

        switch (fieldTypes[idx] & FILTER_ENTRY) {
            case ENTRY_OPTIONAL:
                break;

            case ENTRY_REQUIRED:
                if (((fieldTypes[idx] & FILTER_TYPE) == TYPE_STRING) || ((fieldTypes[idx] & FILTER_TYPE) == TYPE_DATE)) {
                    if (value.length() < 1) {
                        this.addError(idx, FRMMessage.getErr(FRMMessage.ERR_REQUIRED));
                    }
                }

                if ((fieldTypes[idx] & FILTER_TYPE) == TYPE_FLOAT) {
                    try {
                        value = deFormatStringDecimal(value);
                        float val = Float.parseFloat(value);
                        if (val < (0.00000001)) {
                            //System.out.println("--------------->in check param entry error");
                            //System.out.println("--------------->value = "+value);
                            //System.out.println("--------------->length = "+value.length());
                            this.addError(idx, FRMMessage.getErr(FRMMessage.ERR_REQUIRED));
                        }
                    } catch (Exception e) {
                        //this.addError(idx, FRMMessage.getErr(FRMMessage.ERR_TYPE));
                    }
                }
                if ((fieldTypes[idx] & FILTER_TYPE) == TYPE_LONG) {
                    try {
                        value = deFormatStringInteger(value);
                        long val = Long.parseLong(value);
                        if (val < 1) {
                            this.addError(idx, FRMMessage.getErr(FRMMessage.ERR_REQUIRED));
                        }
                    } catch (Exception e) {
                        // this.addError(idx, FRMMessage.getErr(FRMMessage.ERR_TYPE));
                    }
                }
                if ((fieldTypes[idx] & FILTER_TYPE) == TYPE_INT) {
                    try {
                        value = deFormatStringInteger(value);
                        int val = Integer.parseInt(value);
                        if (val < 1) {
                            this.addError(idx, FRMMessage.getErr(FRMMessage.ERR_REQUIRED));
                        }
                    } catch (Exception e) {
                        //this.addError(idx, FRMMessage.getErr(FRMMessage.ERR_TYPE));
                    }
                }

                break;

            case ENTRY_CONDITIONAL:
                break;

            default:

        }
    }

    private void checkParamEntryVector(int idx) {
        String[] values = null;
        try {
            values = (String[]) paramValues.get(idx);
        } catch (Exception e) {
            return;
        }

        switch (fieldTypes[idx] & FILTER_ENTRY) {
            case ENTRY_OPTIONAL:
                break;

            case ENTRY_REQUIRED:
                if (((fieldTypes[idx] & FILTER_TYPE) == TYPE_STRING) || ((fieldTypes[idx] & FILTER_TYPE) == TYPE_DATE)) {
                    if (values.length < 1) {
                        this.addError(idx, FRMMessage.getErr(FRMMessage.ERR_REQUIRED));
                    }
                }

                if (values.length > 0) {
                    String value = "";
                    for (int vi = 0; vi < values.length; vi++) {
                        value = values[vi];
                        if ((fieldTypes[idx] & FILTER_TYPE) == TYPE_VECTOR_FLOAT) {
                            try {
                                value = deFormatStringDecimal(value);
                                float val = Float.parseFloat(value);
                                if (val < (0.00000001)) {
                                    this.addError(idx, FRMMessage.getErr(FRMMessage.ERR_REQUIRED));
                                    break;
                                }
                            } catch (Exception e) {
                                //this.addError(idx, FRMMessage.getErr(FRMMessage.ERR_TYPE));
                            }
                        }
                        if ((fieldTypes[idx] & FILTER_TYPE) == TYPE_VECTOR_LONG) {
                            try {
                                value = deFormatStringInteger(value);
                                long val = Long.parseLong(value);
                                if (val < 1) {
                                    this.addError(idx, FRMMessage.getErr(FRMMessage.ERR_REQUIRED));
                                    break;
                                }
                            } catch (Exception e) {
                                // this.addError(idx, FRMMessage.getErr(FRMMessage.ERR_TYPE));
                            }
                        }
                        if ((fieldTypes[idx] & FILTER_TYPE) == TYPE_VECTOR_INT) {
                            try {
                                value = deFormatStringInteger(value);
                                int val = Integer.parseInt(value);
                                if (val < 1) {
                                    this.addError(idx, FRMMessage.getErr(FRMMessage.ERR_REQUIRED));
                                    break;
                                }
                            } catch (Exception e) {
                                //this.addError(idx, FRMMessage.getErr(FRMMessage.ERR_TYPE));
                            }
                        }
                    }
                }

                break;

            case ENTRY_CONDITIONAL:
                break;

            default:

        }
    }

    // ----------------------------------------------- Get Parameter Value
    public Vector getParamValues() {
        return paramValues;
    }

    public String getParamString(String parName) {
        String sVal = req.getParameter(parName);
        if (sVal == null) {
            return "";
        }
        return sVal;
    }
    //update by satrya 2012-11-27

    /**
     *
     * @param parName
     * @return
     * @throws parName == null exception
     */
    public String[] getParamsStringValues(String parName) {
        String sVal[] = req.getParameterValues(parName);
        try {
            if (sVal == null) {
                return null;
            }

        } catch (Exception ex) {
            System.out.println("Exception" + ex);
        }
        return sVal;
    }

    public int getParamInt(String parName) {
        String sVal = req.getParameter(parName);
        try {
            return Integer.parseInt(sVal);
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * create by satrya 2014-03-19 berguna untuk multiple selected checbox
     *
     * @param parName
     * @return
     */
    public Date getParamDateVer2(String baseName) {
        try {
            int yr = getParamInt(baseName + YR);
            int mn = getParamInt(baseName + MN);
            int dy = getParamInt(baseName + DY);
            int hh = getParamInt(baseName + HH) != 0 ? getParamInt(baseName + HH) : getParamInt(baseName + HR);
            int mm = getParamInt(baseName + MM) != 0 ? getParamInt(baseName + MM) : getParamInt(baseName + MI);

            //if(yr<1 || mn<1 || dy<1){
            //update by satrya 2012-11-27
            if ((yr < 1 || mn < 1 || dy < 1) && hh < 1) {
                return null;
            } //update by satrya 2012-11-27
            else if ((yr < 1 || mn < 1 || dy < 1) && hh > 1) {
                Date dtX = new Date();
                yr = dtX.getYear();
                mn = dtX.getMonth();
                dy = dtX.getDate();

            }
            //System.out.println("-----------------------");
            //System.out.println("yr : "+yr);
            //System.out.println("mn : "+mn);
            //System.out.println("dy : "+dy);
            //System.out.println("-----------------------");

            Date dt = new Date(yr - 1900, mn - 1, dy, hh, mm);
            return dt;
        } catch (Exception e) {
            return null;
            //return String.valueOf(new Date().getTime());
        }
    }

    /**
     * create by satrya 2014-03-19 berguna untuk multiple selected checbox
     *
     * @param parName
     * @return
     */
    public long getParamLong(String parName) {
        String sVal = req.getParameter(parName);
        try {
            sVal=deFormatStringInteger(sVal);
            return Long.parseLong(sVal);
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * create by satrya 2014-03-19 berguna untuk multiple selected checbox
     *
     * @param parName
     * @return
     */
    public double getParamDouble(String parName) {
        String sVal = req.getParameter(parName);
        try {
            return Double.parseDouble(sVal);
        } catch (Exception e) {
            return 0;
        }
    }

    public float getParamFloat(String parName) {
        String sVal = req.getParameter(parName);
        try {
            sVal = deFormatStringDecimal(sVal);
            return Float.parseFloat(sVal);
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * create by satrya 2014-03-19 berguna untuk multiple selected checbox
     *
     * @param parName
     * @return
     */
    public boolean getParamBoolean(String parName) {
        String sVal = req.getParameter(parName);
        try {
            return Boolean.parseBoolean(sVal);
        } catch (Exception e) {
            return false;
        }
    }

    public String getParamValue(int idx) {
        if (idx >= paramValues.size()) {
            return "";
        }
        return (String) paramValues.get(idx);
    }

    protected String getString(int idx) {
        String sParam = (String) paramValues.get(idx);
        try {
            return sParam;
        } catch (Exception e) {
            return "";
        }
    }

    protected int getInt(int idx) {
        String sVal = (String) paramValues.get(idx);
        try {
            sVal = deFormatStringInteger(sVal);
            return Integer.parseInt(sVal);
        } catch (Exception e) {
            return 0;
        }

    }

    protected long getLong(int idx) {
        String sVal = (String) paramValues.get(idx);
        try {
            sVal = deFormatStringInteger(sVal);
            return Long.parseLong(sVal);
        } catch (Exception e) {
            return 0;
        }
    }

    protected float getFloat(int idx) {
        String sVal = (String) paramValues.get(idx);
        try {
            sVal = deFormatStringDecimal(sVal);
            return Float.parseFloat(sVal);
        } catch (Exception e) {
            return 0;
        }
    }

    protected double getDouble(int idx) {
        String sVal = (String) paramValues.get(idx);
        try {
            sVal = deFormatStringDecimal(sVal);
            return Double.parseDouble(sVal);
        } catch (Exception e) {
            return 0;
        }
    }

    protected boolean getBoolean(int idx) {
        String sVal = (String) paramValues.get(idx);
        try {
            if (sVal.equalsIgnoreCase("true") || sVal.equals("1")) {
                return true;
            }

            return false;
        } catch (Exception e) {
            return false;
        }
    }

    /* protected Date getDate(int idx) {
     String sVal = (String)paramValues.get(idx);
     try{
     Date dt = new Date();
     if((sVal != null)&&(sVal.length()>0))
     dt = new Date(Long.parseLong(sVal));
     return dt;
     }catch(Exception e){
     System.out.println("get Date "+e.toString());
     //return new Date();
     return null;
     }
     }*/
    protected Date getDate(int idx) {
        String sVal = (String) paramValues.get(idx);
        try {
            //System.out.println("*** " + new Date(Long.parseLong(sVal)));
            return new Date(Long.parseLong(sVal));
        } catch (Exception e) {
            //return new Date();
            return null;
        }
    }

    /**
     * KAR
     *
     */
    protected Vector<Integer> getVectorInt(String fieldName) {
        try {
            Vector ints = new Vector();
            String sVals[] = req.getParameterValues(fieldName);
            if ((sVals == null) || (sVals.length < 1)) {
                return ints;
            }

            for (int i = 0; i < sVals.length; i++) {
                try {
                    ints.add(new Integer(Integer.parseInt(sVals[i])));
                } catch (Exception exc) {
                    System.out.println("EXC : getVectorInt i=" + i + " - " + exc);
                }
            }

            return ints;

        } catch (Exception e) {
        }

        return new Vector(1, 1);

    }

    /**
     * KAR
     *
     */
    protected Vector<Long> getVectorLong(String fieldName) {
        try {
            Vector longs = new Vector();
            String sVals[] = req.getParameterValues(fieldName);
            if ((sVals == null) || (sVals.length < 1)) {
                return longs;
            }

            for (int i = 0; i < sVals.length; i++) {
                try {
                    longs.add(new Long(Long.parseLong(sVals[i])));
                } catch (Exception exc) {
                    System.out.println("EXC : getVectorLong i=" + i + " - " + exc);
                }
            }

            return longs;

        } catch (Exception e) {
        }

        return new Vector(1, 1);

    }

    /**
     * KARTIKA 2015-09-08
     *
     */
    protected Vector<String> getVectorString(String fieldName) {
        try {
            Vector<String> strings = new Vector();
            String sVals[] = req.getParameterValues(fieldName);
            if ((sVals == null) || (sVals.length < 1)) {
                return strings;
            }

            for (int i = 0; i < sVals.length; i++) {
                try {
                    strings.add("" + sVals[i]);
                } catch (Exception exc) {
                    System.out.println("EXC : getVectorString i=" + i + " - " + exc);
                }
            }

            return strings;

        } catch (Exception e) {
        }

        return new Vector(1, 1);

    }

    // ----------------------------------------------- Error form handler
    public int errorSize() {
        return hsErrors.size();
    }

    public void clearError() {
        this.hsErrors.clear();
    }

    public Hashtable getErrors() {
        return hsErrors;
    }

    public String getErrorMsg(int i) {
        String msg = "";
        String key = String.valueOf(i);
        try {
            msg = (String) hsErrors.get(key);
        } catch (Exception e) {
        }

        if (msg == null || msg.length() < 1) {
            return "";
        }
        return "<span class=\"" + msgStyle + "\">" + msg + "</span>";
    }

    //update by satrya 2012-11-10
    public String getErrorMsgModif(int i) {
        String msg = "";
        String key = String.valueOf(i);
        try {
            msg = (String) hsErrors.get(key);
        } catch (Exception e) {
        }

        if (msg == null || msg.length() < 1) {
            return "";
        }
        return "<span class=\"" + msgStyle + "\">" + msg + " ,If Employe is a Daily Worker (DL)  please replace 'DL-' with '12' ,for  example 'DL-333' become to '12333'.     If Employe's  Status  'Resigned'  please input the barcode number, barcode number is unique for example -R(BarcodeNumb/PinNumber)</span>";
    }

    public void addError(int fldIndex, String errString) {
        if (hsErrors.containsKey(String.valueOf(fldIndex))) {
            return;
        }
        hsErrors.put(String.valueOf(fldIndex), errString);
    }

    public void removeError(int fldIndex) {
        if (!hsErrors.containsKey(String.valueOf(fldIndex))) {
            return;
        }
        hsErrors.remove(String.valueOf(fldIndex));
    }

    // ----------------------------------------------- Composed form object handler
    private String getParamDate(String baseName) {
        try {
            int yr = getParamInt(baseName + YR);
            int mn = getParamInt(baseName + MN);
            int dy = getParamInt(baseName + DY);
            int hh = getParamInt(baseName + HH) != 0 ? getParamInt(baseName + HH) : getParamInt(baseName + HR);
            int mm = getParamInt(baseName + MM) != 0 ? getParamInt(baseName + MM) : getParamInt(baseName + MI);

            //if(yr<1 || mn<1 || dy<1){
            //update by satrya 2012-11-27
            if ((yr < 1 || mn < 1 || dy < 1) && hh < 1) {
                return "";
            } //update by satrya 2012-11-27
            else if ((yr < 1 || mn < 1 || dy < 1) && hh > 1) {
                Date dtX = new Date();
                yr = dtX.getYear();
                mn = dtX.getMonth();
                dy = dtX.getDate();

            }
            //System.out.println("-----------------------");
            //System.out.println("yr : "+yr);
            //System.out.println("mn : "+mn);
            //System.out.println("dy : "+dy);
            //System.out.println("-----------------------");

            Date dt = new Date(yr - 1900, mn - 1, dy, hh, mm);
            return String.valueOf(dt.getTime());
        } catch (Exception e) {
            return "";
            //return String.valueOf(new Date().getTime());
        }
    }

    /**
     * NIA
     *
     */
    private Date getTime(String baseName) {
        try {
            int hr = getParamInt(baseName + HR);
            int mi = getParamInt(baseName + MI);
            int sec = getParamInt(baseName + SEC);

            //System.out.println("-----------------------");
            //System.out.println("hr : "+hr);
            //System.out.println("mi : "+mi);
            //System.out.println("sec : "+sec);
            //System.out.println("-----------------------");
            Date dt = new Date();

            dt.setHours(hr);
            dt.setMinutes(mi);
            dt.setSeconds(sec);

            return dt;
        } catch (Exception e) {
            System.out.println("get Date " + e.toString());
            //return new Date();
            return null;
        }
    }

    /* NIA
     this function used to format String in USER FORMAT into Decimal.
     */
    public static String deFormatStringDecimal(String str) {
        try {
            StringTokenizer strToken = new StringTokenizer(str, USER_DIGIT_GROUP);
            String strValue = "";
            while (strToken.hasMoreTokens()) {
                strValue = strValue + strToken.nextToken();
            }
            strValue = strValue.replace(USER_DECIMAL_SYMBOL.charAt(0), SYSTEM_DECIMAL_SYMBOL.charAt(0));

            return strValue;

        } catch (Exception exc) {
            System.out.println("error deFormatStringDecimal" + exc);
            return "";
        }
    }

    public void requestParamMultipleSelected(Vector list, String FormName) {
        String value = "";
        paramValues.clear();
        boolean sudahSesuaiData = false;
        if (list != null && list.size() > 0) {
            for (int ls = 0; ls < list.size(); ls++) {
                String selecteds = req.getParameter(FormName + ls);
                if (selecteds != null) {
                    sudahSesuaiData = true;
                    for (int i = 0; i < fieldNames.length; i++) {
                        switch (fieldTypes[i] & FILTER_TYPE) {
                            case TYPE_DATE:
                                value = getParamDate(fieldNames[i] + ls);
                                break;
                            default:
                                value = req.getParameter(fieldNames[i] + ls);
                                value = checkValueForInjection(value);
                        }
                        if (value == null) {
                            value = "";
                        } else {
                            value = value.trim();
                        }
                        paramValues.add(value);
                        /**/
                        try {
                            checkParamEntry(i);
                            checkParamFormat(i);
                            checkParamType(i);
                        } catch (Exception e) {
                        }
                        /**/
                    }
                }
            }
        }//end cek vector
        if (sudahSesuaiData == false) {
            int tot = list != null ? list.size() + 1 : 1;
            for (int i = 0; i < fieldNames.length; i++) {
                switch (fieldTypes[i] & FILTER_TYPE) {
                    case TYPE_DATE:
                        value = getParamDate(fieldNames[i] + tot);
                        break;
                    default:
                        value = req.getParameter(fieldNames[i] + tot);
                        value = checkValueForInjection(value);
                }
                if (value == null) {
                    value = "";
                } else {
                    value = value.trim();
                }
                paramValues.add(value);
                /**/
                try {
                    checkParamEntry(i);
                    checkParamFormat(i);
                    checkParamType(i);
                } catch (Exception e) {
                }
                /**/
            }
        }

    }

    /* NIA
     this function used to format String in USER FORMAT into Integer.
     */
    private String deFormatStringInteger(String str) {
        try {
            StringTokenizer strToken = new StringTokenizer(str, USER_DIGIT_GROUP);
            String strValue = "";
            while (strToken.hasMoreTokens()) {
                strValue = strValue + strToken.nextToken();
            }
            return strValue;

        } catch (Exception exc) {
            System.out.println("error deFormatStringDecimal" + exc);
            return "";
        }
    }

    /**
     * this function used to format Double into String in SYSTEM FORMAT
     *
     * @param number
     * @return NIA
     * @update by Edhy on March 28, 2005
     */
    public static String userFormatStringDecimal(double number) {
        String format = "##" + SYSTEM_DIGIT_GROUP + "###" + SYSTEM_DECIMAL_SYMBOL + "00";
        DecimalFormat df = new DecimalFormat(format);

        // add by edhy
        boolean bBetweenZeroToOne = false;
        if ((0 < number) && (number < 1)) {
            bBetweenZeroToOne = true;
        }

        String add = "";
        try {
            StringTokenizer strToken = new StringTokenizer(format, SYSTEM_DIGIT_GROUP);
            String str = "";
            while (strToken.hasMoreTokens()) {
                str = strToken.nextToken();
            }

            if (str != null && str.length() > 0) {
                int lg = str.length();
                if (str.equals("#") || str.equals("##")) {
                    String num = String.valueOf(number);
                    StringTokenizer tok = new StringTokenizer(num, ".");
                    while (tok.hasMoreTokens()) {
                        str = tok.nextToken();
                    }

                    if (str != null && str.length() > 0) {
                        if (Integer.parseInt(str) == 0) {
                            add = ".0";
                        }
                    }
                }
            }

            String strValue = (df.format((double) number).toString()) + add;
            if (strValue != null && strValue.length() > 0) {
                add = strValue.substring(0, strValue.indexOf(SYSTEM_DECIMAL_SYMBOL));
                add = add.replace(SYSTEM_DIGIT_GROUP.charAt(0), USER_DIGIT_GROUP.charAt(0));
                add = add + USER_DECIMAL_SYMBOL + strValue.substring(strValue.indexOf(SYSTEM_DECIMAL_SYMBOL) + 1, strValue.length());
            }

            if (number == 0 || bBetweenZeroToOne) {
                add = "0" + add;
            }

        } catch (Exception e) {
            System.out.println("Exception on FrmHandler.userFormatStringDecimal : " + e.toString());
            add = "";
        }

        return add;
    }

    public static String userFormatStringLong(long number) {
        String format = "##" + SYSTEM_DIGIT_GROUP + "###";
        DecimalFormat df = new DecimalFormat(format);
        String strValue = "";
        try {
            strValue = df.format(number).toString();
            strValue = strValue.replace(SYSTEM_DIGIT_GROUP.charAt(0), USER_DIGIT_GROUP.charAt(0));
        } catch (Exception e) {
            System.out.println("Exception on FrmHandler.userFormatStringLong : " + e.toString());
            strValue = "";
        }

        return strValue;
    }

    public String getMsgStyle() {
        return msgStyle;
    }

    public void setMsgStyle(String msgStyle) {
        this.msgStyle = msgStyle;
    }

    /**
     * this function used to format Double into String in SYSTEM FORMAT without
     * point
     *
     * @param number
     * @return BB
     * @updated by Edhy on March 28, 2005
     */
    public static String userFormatStringDecimalWithoutPoint(double number) {
        String format = "##" + SYSTEM_DIGIT_GROUP + "###" + SYSTEM_DECIMAL_SYMBOL + "00";
        DecimalFormat df = new DecimalFormat(format);

        // add by edhy
        boolean bBetweenZeroToOne = false;
        if ((0 < number) && (number < 0)) {
            bBetweenZeroToOne = true;
        }

        String add = "";
        try {
            StringTokenizer strToken = new StringTokenizer(format, SYSTEM_DIGIT_GROUP);
            String str = "";
            while (strToken.hasMoreTokens()) {
                str = strToken.nextToken();
            }

            if (str != null && str.length() > 0) {
                int lg = str.length();
                if (str.equals("#") || str.equals("##")) {
                    String num = String.valueOf(number);
                    StringTokenizer tok = new StringTokenizer(num, ".");
                    while (tok.hasMoreTokens()) {
                        str = tok.nextToken();
                    }

                    if (str != null && str.length() > 0) {
                        if (Integer.parseInt(str) == 0) {
                            add = ".0";
                        }
                    }

                }
            }

            String strValue = (df.format((double) number).toString()) + add;
            if (strValue != null && strValue.length() > 0) {
                add = strValue.substring(0, strValue.indexOf(SYSTEM_DECIMAL_SYMBOL));
                add = add.replace(SYSTEM_DIGIT_GROUP.charAt(0), USER_DIGIT_GROUP.charAt(0));
                add = add + USER_DECIMAL_SYMBOL + strValue.substring(strValue.indexOf(SYSTEM_DECIMAL_SYMBOL) + 1, strValue.length());
            }

            if (number == 0 || bBetweenZeroToOne) {
                add = "0" + add;
            }

        } catch (Exception e) {
            System.out.println("Exception on FrmHandler.userFormatStringDecimalWithoutPoint : " + e.toString());
            add = "";
        }

        add = add.substring(0, add.indexOf(","));
        if (add.length() == 0) {
            add = "0";
        }
        return add;
    }

    // this method is setter method for variable "USER_DIGIT_GROUP"
    public void setDigitSeparator(String digitSeparator) {
        this.USER_DIGIT_GROUP = digitSeparator;
    }

    // this method is setter method for variable "USER_DECIMAL_SYMBOL" 
    public void setDecimalSeparator(String decimalSeparator) {
        this.USER_DECIMAL_SYMBOL = decimalSeparator;
    }

    public static void main(String args[]) {
        String result = userFormatStringDecimal(0.01);
        System.out.println(result);
    }
} // end of FRMHandler
