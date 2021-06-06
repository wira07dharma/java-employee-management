<%-- 
    Document    : custom_rpt_generate
    Created on  : Apr 3, 2015, 11:37:15 AM
    Author      : Hendra Putu
    Description : custom rpt generate berguna untuk menampilkan hasil report 
    yang telah disusun di manage data.

================================================================================
    Custom Report adalah suatu module atau fitur yang dapat memudahkan 
    pengguna membuat atau meng-customize report yang diinginkan.
    ada 3 tahapan dalam pembuatan custom report.
    1. Buat terlebih dahulu nama report dan juga pembuatnya.
    2. Manage data atau susun data-data yang ingin ditampilkan.
    3. Generate report yang telah disusun.
    Dari sisi pengguna ini akan terasa mudah, dan sangat menguntungkan, 
    sebab mereka tidak perlu report" lagi menentukan list request yang 
    akan diberikan ke developer app.
    Dari sisi developer, efisien dan efektif dalam resorce dan kecepatan.
================================================================================
--%>

<%@page import="org.json.JSONArray"%>
<%@page import="org.json.JSONObject"%>
<%@page import="com.dimata.harisma.entity.payroll.PayComponent"%>
<%@page import="com.dimata.harisma.entity.payroll.PstPayComponent"%>
<%@page import="com.dimata.harisma.entity.payroll.PstPayBanks"%>
<%@page import="com.dimata.harisma.entity.payroll.PayBanks"%>
<%@page import="java.math.RoundingMode"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="com.dimata.harisma.entity.payroll.PaySlipComp"%>
<%@page import="com.dimata.harisma.entity.payroll.PaySlip"%>
<%@page import="com.dimata.harisma.entity.payroll.CustomRptDynamic"%>
<%@page import="com.dimata.harisma.entity.payroll.CustomRptConfig"%>
<%@page import="com.dimata.harisma.entity.payroll.PstCustomRptConfig"%>
<%@page import="com.dimata.harisma.entity.payroll.PstCustomRptMain"%>
<%@page import="com.dimata.harisma.entity.payroll.CustomRptMain"%>
<%@page import="com.dimata.gui.jsp.ControlLine"%>
<%@page import="com.dimata.harisma.form.payroll.CtrlCustomRptMain"%>
<%@page import="com.dimata.harisma.form.payroll.FrmCustomRptMain"%>
<%@page import="com.dimata.gui.jsp.ControlList"%>
<%@ include file = "../../main/javainit.jsp" %>
<% 
int appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_REPORTS, AppObjInfo.G2_PAYROLL_REPORT, AppObjInfo.OBJ_CUSTOM_RPT_GENERATE);
%>
<%@ include file = "../../main/checkuser.jsp" %>
<%!
    /*///
    * method name : apostrophe(String value, String opr)
    * Description : memanipulasi value dengan menampahkan 'kutip satu
    */
    public String apostrophe(String value, String opr){
        String str = "";
        if (opr.equals("=")){
            str = "'"+value+"'";
        } else if (opr.equals("BETWEEN")){
            String[] data = new String[5];
            int inc = 0;
            for (String retval : value.split(" ")) {
                data[inc]= retval;
                inc++;
            }
            str = "'"+data[0]+"' AND '"+data[1]+"'";
        } else if (opr.equals("LIKE")){
            str = "'"+value+"'";
        } else if (opr.equals("IN")){
            String stIn = "";
            for (String retval : value.split(",")) {
                stIn += " '"+ retval +"', ";
            }
            stIn += "'0'";
            str += "("+stIn+")";
        } else if (opr.equals("!=")) {
            str = "'"+value+"'";
        } else {
            str = value;
        }
        return str;
    }
    /* Convert double */
    public double convertDouble(String val){
        BigDecimal bDecimal = new BigDecimal(Double.valueOf(val));
        bDecimal = bDecimal.setScale(0, RoundingMode.HALF_UP);
        return bDecimal.doubleValue();
    }
    /* Convert int */
    public int convertInteger(double val){
        BigDecimal bDecimal = new BigDecimal(val);
        bDecimal = bDecimal.setScale(0, RoundingMode.HALF_UP);
        return bDecimal.intValue();
    }
    /* drawList Table*/
    public String drawList(Vector listField, Vector listRecord, Vector listComp, Vector listComb, long oidCustom, int showValue) {
        /* === get list of SUBTOTAL === */
        String whereSubTotal = " RPT_CONFIG_DATA_TYPE = 3 AND RPT_CONFIG_DATA_GROUP = 0 AND RPT_MAIN_ID ="+oidCustom;
        Vector listSubTotal = PstCustomRptConfig.list(0, 0, whereSubTotal, "");
        String[] countName = new String[listField.size()];
        int[] countIdx = new int[listField.size()];
        /* get name of count by SECTION or DEPARTMENT or else */
        if (listSubTotal != null && listSubTotal.size()>0){
            for(int st=0; st<listSubTotal.size(); st++){
                CustomRptConfig sbt = (CustomRptConfig)listSubTotal.get(st);
                if ("COUNT".equals(sbt.getRptConfigTableGroup())){
                    countName[st] = sbt.getRptConfigTableName();
                }
            }
        }
        /* Some of array variable */
        String[][] arrField = new String[listRecord.size()+1][listField.size()]; /* for dinamic fields */
        double[][] totalComp = new double[listRecord.size()][listComp.size()]; /* for total salary component */
        double[] totalCombine = new double[listRecord.size()]; /* for total combine */
        /* update by Hendra Putu | 2015-11-05 */
        double countValueComp = 0;
        boolean[] showRecord = new boolean[listRecord.size()];
       
        /* mengambil field dinamis */
        /* Check count and get index */
        int idx = 0;
        for(int i=0;i<listField.size(); i++){
            CustomRptConfig fields = (CustomRptConfig)listField.get(i);              
            for(int cn=0; cn<countName.length; cn++){
                if (fields.getRptConfigFieldName().equals(countName[cn])){
                    countIdx[idx] = i;
                    idx++;
                }
            }
        }
        
        /* 
        ===============================================================================
        * listRecord berisi baris data hasil query dan akan ditampung ke variabel array 
        ===============================================================================
        */
        int grandCols = listField.size();
        boolean isStatusData = false;
        if (listRecord != null && listRecord.size()>0){
            for(int y=0; y<listRecord.size(); y++){
                CustomRptDynamic dyc = (CustomRptDynamic)listRecord.get(y);
                /* Get dinamis field */
                if (listField != null && listField.size() > 0){
                    for(int i=0;i<listField.size(); i++){
                        CustomRptConfig rpt = (CustomRptConfig)listField.get(i);
                        /* jika field name != PAY_SLIP_ID */
                        if(!rpt.getRptConfigFieldName().equals("PAY_SLIP_ID")){
                            if (!rpt.getRptConfigFieldName().equals("STATUS_DATA")){
                                arrField[y][i] = dyc.getField(rpt.getRptConfigFieldName());
                            } else {
                                isStatusData = true;
                            }
                        }  
                    }
                }
                /* get value field */
                for(int k=0; k<listComp.size(); k++){
                    CustomRptConfig rptComp = (CustomRptConfig)listComp.get(k);                    
                    totalComp[y][k] = convertDouble(dyc.getField(rptComp.getRptConfigFieldName()));
                    countValueComp += totalComp[y][k];
                }
                /* get value field combine */
                for(int l=0; l<listComb.size(); l++){
                    CustomRptConfig rptComb = (CustomRptConfig)listComb.get(l);
                    totalCombine[y] = convertDouble(dyc.getField(rptComb.getRptConfigFieldName()));
                }
                if (showValue == 0){
                   showRecord[y] = true;
                } else {
                    if (countValueComp == 0){
                        showRecord[y] = false;
                    } else {
                        showRecord[y] = true;
                    }
                }
                countValueComp = 0;
            }
        }
        /* END of List Record */
        int[] countTotal = new int[idx];
        boolean[] countFind = new boolean[idx];
        /* inisialisasi */
        for(int d=0; d<idx; d++){
            countTotal[d] = 1;
            countFind[d] = false;
        }
        int nomor = 0;
        int yStart = 0;
        int yEnd = 0;
        double total = 0;
        boolean ketemu = false;
        String table = "<table class='tblStyle'>";
        table +="<tr>";
        table +="<td class='title_tbl'>No</td>";
        /* mengambil field dinamis */
        for(int i=0;i<listField.size(); i++){
            CustomRptConfig fields = (CustomRptConfig)listField.get(i);    
            if(!fields.getRptConfigFieldName().equals("PAY_SLIP_ID")){
                if (!fields.getRptConfigFieldName().equals("STATUS_DATA")){
                    table +="<td class='title_tbl'>"+fields.getRptConfigFieldHeader()+"</td>";
                }
            }
        }
        /* mengambil field salary component */
        for(int j=0; j<listComp.size(); j++){
            CustomRptConfig comp = (CustomRptConfig)listComp.get(j);
            table +="<td class='title_tbl'>"+comp.getRptConfigFieldHeader()+"</td>";
        }
        /* mengambil field combine */
        for(int k=0; k<listComb.size(); k++){
            CustomRptConfig comb = (CustomRptConfig)listComb.get(k);
            table +="<td class='title_tbl'>"+comb.getRptConfigFieldHeader()+"</td>";
        }
        table +="</tr>";
        /* ============== record result ==============*/
        if (listRecord != null && listRecord.size()>0){
            for(int y=0; y<listRecord.size(); y++){
                if (showRecord[y] == true){
                    nomor++;
                    table +="<tr>";
                    table +="<td>"+nomor+"</td>";
                    /* Fields dinamic */
                    if (listField != null && listField.size() > 0){
                        for(int i=0;i<listField.size(); i++){
                            if (arrField[y][i] != null){
                                table +="<td>"+arrField[y][i]+"</td>";
                            }
                        }
                        /* count record by ... */
                        for(int sb=0; sb<idx; sb++){
                            if (arrField[y][countIdx[sb]].equals(arrField[y+1][countIdx[sb]])){
                                countTotal[sb] = countTotal[sb] + 1;
                            } else {
                                countFind[sb] = true;
                                ketemu = true;
                                yEnd = y+1;
                            }
                        }
                    }

                    for(int k=0; k<listComp.size(); k++){
                        table +="<td>"+convertInteger(totalComp[y][k])+"</td>";
                    }
                    for(int l=0; l<listComb.size(); l++){
                        table +="<td>"+convertInteger(totalCombine[y])+"</td>";
                    }
                    table +="</tr>";
                    table +="<tr>";

                    boolean addColoum = false;
                    if (ketemu == true){
                        for(int f=0; f<idx; f++){
                            if (countFind[f] == true){
                                if (addColoum == false){
                                    for(int a=0; a<countIdx[f]+1; a++){
                                        table +="<td id='tdTotal'>&nbsp;</td>";
                                    }
                                    addColoum = true;
                                }
                                table +="<td id='tdTotal'>"+countTotal[f]+"</td>";

                                countFind[f] = false;
                                countTotal[f] = 1;
                            }
                        }
                        addColoum = false;
                        for(int b=0; b<listField.size()-(countIdx[idx-1]+2); b++){
                            table +="<td id='tdTotal'>&nbsp;</td>";
                        }
                        for(int kolom=0; kolom<listComp.size(); kolom++){
                            for(int x=yStart; x<yEnd; x++){
                                total += totalComp[x][kolom];
                            }
                            table +="<td id='tdTotal'>"+convertInteger(total)+"</td>";
                            total = 0;
                        }
                        for(int x=yStart; x<yEnd; x++){
                            total += totalCombine[x];
                        }
                        table +="<td id='tdTotal'>"+convertInteger(total)+"</td>";
                        total = 0;
                        ketemu = false;
                    }
                    table +="</tr>";
                    yStart = yEnd;
                }
            }
            table += "<tr>";
            if (isStatusData == true){
                grandCols = grandCols - 1;
            }
            table += "<td class='td_grand_total' colspan=\""+grandCols+"\">Grand Total</td>";
            /* mengambil field salary component */
            double grandTotalComp = 0;
            for(int j=0; j<listComp.size(); j++){
                for(int y=0; y<listRecord.size(); y++){
                    grandTotalComp = grandTotalComp + totalComp[y][j];
                }
                table +="<td class='td_grand_total'>"+convertInteger(grandTotalComp)+"</td>";
                grandTotalComp = 0;
            }
            /* mengambil field combine */
            double grandTotalComb = 0;
            
            for(int y=0; y<listRecord.size(); y++){
                grandTotalComb = grandTotalComb +  totalCombine[y];
            }
            if (grandTotalComb != 0){
                table +="<td class='td_grand_total'>"+convertInteger(grandTotalComb)+"</td>";
            }
            
            table += "</tr>";
        }
        table +="</table>";
        
        return table;

    }
    
    public String drawListDetail(Vector listEmployeeField, Vector listRecord, Vector listSalaryComponentField, Vector listCombineField, long oidCustom, int showValue) {
        //SET COLUMN HEADER EMPLOYEE TO BE HIDDEN
        ArrayList<String> arrayHideColumnNameEmployee = new ArrayList<String>();
        arrayHideColumnNameEmployee.add("PAY_SLIP_ID");
        arrayHideColumnNameEmployee.add("STATUS_DATA");
        arrayHideColumnNameEmployee.add("COMPANY");
        arrayHideColumnNameEmployee.add("PAYROLL_GROUP_NAME");
        arrayHideColumnNameEmployee.add("PERIOD");
        
        ArrayList<Integer> arrayIndexHideColumnEmployee = new ArrayList<Integer>();
        
        
        // ============================== SETTING SUBTOTAL ==============================
        
        //GET LIST SUBTOTAL
        String whereSubTotal = " RPT_CONFIG_DATA_TYPE = 3 AND RPT_CONFIG_DATA_GROUP = 0 AND RPT_MAIN_ID = " + oidCustom;
        Vector listSubTotal = PstCustomRptConfig.list(0, 0, whereSubTotal, "");
        
        //SET VARIABLE SUBTOTAL
        JSONArray jArraySubTotal = new JSONArray();
        for (int st = 0; st < listSubTotal.size(); st++) {
            CustomRptConfig sbt = (CustomRptConfig) listSubTotal.get(st);
            if (arrayHideColumnNameEmployee.contains(sbt.getRptConfigFieldName())) {
                continue;
            }
            JSONObject jObjectSubTotal = new JSONObject();
            //GET SUBTOTAL NAME
            if ("COUNT".equals(sbt.getRptConfigTableGroup())) {
                jObjectSubTotal.put("SUBTOTAL_NAME", sbt.getRptConfigTableName());
                //GET SUBTOTAL INDEX
                for (int ef = 0; ef < listEmployeeField.size(); ef++) {
                    CustomRptConfig fields = (CustomRptConfig) listEmployeeField.get(ef);
                    if (fields.getRptConfigFieldName().equals(sbt.getRptConfigTableName())) {
                        jObjectSubTotal.put("SUBTOTAL_INDEX", ef);
                        jObjectSubTotal.put("SUBTOTAL_TITLE", fields.getRptConfigFieldHeader());
                        break;
                    }
                }
            }
            jObjectSubTotal.put("SUBTOTAL_DATA", "");
            jObjectSubTotal.put("SUBTOTAL_COUNT", 0);
            jObjectSubTotal.put("SUBTOTAL_SHOW", false);
            jArraySubTotal.put(jObjectSubTotal);
        }
        
        
        // ============================== DRAW TABLE DATA ==============================
        
        String table = "<table class='tblStyle'>";
        table += "<tr>";
        table +="<td class='title_tbl'>No</td>";
        
        // ========== SHOW COLUMN HEADER EMPLOYEE DATA ==========
        for (int ef = 0; ef < listEmployeeField.size(); ef++) {
            CustomRptConfig fields = (CustomRptConfig) listEmployeeField.get(ef);
            //GET INDEX COLUMN TO HIDE
            if (arrayHideColumnNameEmployee.contains(fields.getRptConfigFieldName())) {
                arrayIndexHideColumnEmployee.add(ef);
            } else {
                table += "<td class='title_tbl'>" + fields.getRptConfigFieldHeader() + "</td>";
            }
        }
        
        // ========== SHOW COLUMN HEADER SALARY COMPONENT ==========
        
        //GET SALARY COMPONENT DEDUCTION
        Vector<PayComponent> listCompDeduction = PstPayComponent.list(0, 0, "COMP_TYPE=2", "");
        ArrayList<Integer> arrayIndexComponentDeduction = new ArrayList<Integer>();
        
        for (int sf = 0; sf < listSalaryComponentField.size(); sf++) {
            CustomRptConfig comp = (CustomRptConfig) listSalaryComponentField.get(sf);
            table += "<td class='title_tbl'>" + comp.getRptConfigFieldHeader() + "</td>";
            
            //GET INDEX SALARY COMPONENT DEDUCTION
            for (PayComponent pc : listCompDeduction) {
                if (pc.getCompName().equals(comp.getRptConfigFieldHeader())) {
                    arrayIndexComponentDeduction.add(sf);
                    break;
                }
            }
        }
        
        // ========== SHOW COLUMN HEADER COMBINE ==========
        for (int cf = 0; cf < listCombineField.size(); cf++) {
            CustomRptConfig comb = (CustomRptConfig) listCombineField.get(cf);
            table += "<td class='title_tbl'>" + comb.getRptConfigFieldHeader() + "</td>";
        }
        table += "</tr>";
        
        
        // ========== SHOW COLUMN DATA ==========
        String lastData = "";
        String styleData = "background-color: #EEE;";
        int grandTotalEmployee = 0;
        double[] grandTotalSalary = new double[listSalaryComponentField.size()];
        double[] grandTotalCombine = new double[listCombineField.size()];
        
        for (int i = 0; i < listRecord.size(); i++) {
            CustomRptDynamic dyc = (CustomRptDynamic) listRecord.get(i);
            grandTotalEmployee++;
            int nomor = i + 1;
            table += "<tr>";
            table += "<td style='" + styleData + "'>" + nomor + "</td>";
            
            // ========== SHOW DATA EMPLOYEE ==========
            for (int ef = 0; ef < listEmployeeField.size(); ef++) {
                CustomRptConfig rpt = (CustomRptConfig) listEmployeeField.get(ef);
                if (arrayIndexHideColumnEmployee.contains(ef)) {
                    continue;
                }
                String currentData = dyc.getField(rpt.getRptConfigFieldName());
                table += "<td style='" + styleData + "'>" + currentData + "</td>";
                
                //CHECK IF SUBTOTAL SHOULD SHOW
                for (int ja = 0; ja < jArraySubTotal.length(); ja++) {
                    JSONObject jo = jArraySubTotal.getJSONObject(ja);
                    if (jo.get("SUBTOTAL_INDEX").equals(ef)) {
                        jo.put("SUBTOTAL_DATA", currentData);
                        
                        //COUNT DATA
                        int count = jo.getInt("SUBTOTAL_COUNT");
                        jo.put("SUBTOTAL_COUNT", count + 1);
                        
                        //GET CURRENT DATA
                        if (!lastData.equals(currentData)) {
                            lastData = currentData;
                        }
                        
                        //CHECK IF CURRENT DATA IS EQUAL TO THE NEXT DATA (IF NEXT DATA IS EXIST)
                        try {
                            CustomRptDynamic dycNext = (CustomRptDynamic) listRecord.get(i + 1);
                            String nextData = dycNext.getField(rpt.getRptConfigFieldName());
                            if (lastData.equals(nextData)) {
                                jo.put("SUBTOTAL_SHOW", false);
                            } else {
                                jo.put("SUBTOTAL_SHOW", true);
                            }
                        } catch (Exception e) {
                            jo.put("SUBTOTAL_SHOW", true);
                        }
                    }
                }
            }
            
            // ========== SHOW DATA SALARY COMPONENT ==========
            for (int sf = 0; sf < listSalaryComponentField.size(); sf++) {
                CustomRptConfig rptComp = (CustomRptConfig)listSalaryComponentField.get(sf);
                double salary = convertDouble(dyc.getField(rptComp.getRptConfigFieldName()));
                grandTotalSalary[sf] += salary;
                String fontRed = "";
                if (arrayIndexComponentDeduction.contains(sf)) {
                    fontRed = "color: red;";
                }
                table += "<td style='" + styleData + " text-align: right;" + fontRed + "'>" + String.format("%,.0f", salary) + "</td>";
                
                //COUNT SUBTOTAL SALARY
                for (int ja = 0; ja < jArraySubTotal.length(); ja++) {
                    JSONObject jo = jArraySubTotal.getJSONObject(ja);
                    if (jo.isNull("" + sf)) {
                        jo.put("" + sf, 0);
                    }
                    double storedSalary = jo.getDouble("" + sf);
                    jo.put("" + sf, storedSalary + salary);
                }
            }
            
            // ========== SHOW DATA COMBINE ==========
            for (int cf = 0; cf < listCombineField.size(); cf++) {
                CustomRptConfig rptComb = (CustomRptConfig)listCombineField.get(cf);
                double combine = convertDouble(dyc.getField(rptComb.getRptConfigFieldName()));
                grandTotalSalary[cf] += combine;
                table += "<td style='" + styleData + " text-align: right'>" + String.format("%,.0f", combine) + "</td>";
                
                //COUNT SUBTOTAL COMBINE
                for (int ja = 0; ja < jArraySubTotal.length(); ja++) {
                    JSONObject jo = jArraySubTotal.getJSONObject(ja);
                    if (jo.isNull("" + cf)) {
                        jo.put("" + cf, 0);
                    }
                    double storedCombine = jo.getDouble("" + cf);
                    jo.put("" + cf, storedCombine + combine);
                }
            }
            table += "</tr>";
            
            // ========== SHOW SUBTOTAL ==========
            int colspanSubTotal = listEmployeeField.size() - arrayIndexHideColumnEmployee.size() + 1;
            for (int ja = 0; ja < jArraySubTotal.length(); ja++) {
                JSONObject jo = jArraySubTotal.getJSONObject(ja);
                if (jo.get("SUBTOTAL_SHOW").equals(true) && i > 0) {
                    String addText = "";
                    if (jo.get("SUBTOTAL_NAME").equals("SECTION")) {
                        addText = "Sub ";
                    }
                    table += "<tr>";
                    table += "<td id='tdTotal' colspan='" + colspanSubTotal + "'>" + addText + "Total " + jo.get("SUBTOTAL_TITLE") + " " + jo.get("SUBTOTAL_DATA") + " : " + jo.get("SUBTOTAL_COUNT") + "</td>";
                    
                    //RESET DATA SUBTOTAL
                    jo.put("SUBTOTAL_COUNT", 0);
                    
                    for (int sf = 0; sf < listSalaryComponentField.size(); sf++) {
                        double subTotalSalary = jo.getDouble("" + sf);
                        String fontRed = "";
                        if (arrayIndexComponentDeduction.contains(sf)) {
                            fontRed = "color: red;";
                        }
                        table += "<td id='tdTotal' style='text-align: right;" + fontRed + "'>" + String.format("%,.0f", subTotalSalary) + "</td>";
                        //RESET DATA SUBTOTAL SALARY
                        jo.put("" + sf, 0);
                    }
                    
                    for (int cf = 0; cf < listCombineField.size(); cf++) {
                        double subTotalCombine = jo.getDouble("" + cf);
                        String fontRed = "";
                        if (arrayIndexComponentDeduction.contains(cf)) {
                            fontRed = "color: red;";
                        }
                        table += "<td id='tdTotal' style='text-align: right;" + fontRed + "'>" + String.format("%,.0f", subTotalCombine) + "</td>";
                        //RESET DATA SUBTOTAL COMBINE
                        jo.put("" + cf, 0);
                    }
                    table += "</tr>";
                }
            }
        }
        
        // ========== SHOW GRAND TOTAL ==========
        int colspanGrandTotal = listEmployeeField.size() - arrayIndexHideColumnEmployee.size() + 1;
        table += "<tr>";
        //table += "<td class='td_grand_total'></td>";
        table += "<td class='td_grand_total' colspan='" + colspanGrandTotal + "'>Grand Total : " + grandTotalEmployee + "</td>";
        for (int sf = 0; sf < listSalaryComponentField.size(); sf++) {
            table += "<td class='td_grand_total' style='text-align: right;'>" + String.format("%,.0f", grandTotalSalary[sf]) + "</td>";
        }
        for (int cf = 0; cf < listCombineField.size(); cf++) {
            table += "<td class='td_grand_total' style='text-align: right;'>" + String.format("%,.0f", grandTotalCombine[cf]) + "</td>";
        }
        table += "</tr>";
        
        table += "<table>";
        return table;
    }
    
    public String drawListSummary(Vector listEmployeeField, Vector listRecord, Vector listSalaryComponentField, Vector listCombineField, long oidCustom, int showValue) {
        //SET COLUMN HEADER EMPLOYEE TO BE HIDDEN
        ArrayList<String> arrayHideColumnNameEmployee = new ArrayList<String>();
        arrayHideColumnNameEmployee.add("PAY_SLIP_ID");
        arrayHideColumnNameEmployee.add("STATUS_DATA");
        arrayHideColumnNameEmployee.add("COMPANY");
        arrayHideColumnNameEmployee.add("PAYROLL_GROUP_NAME");
        arrayHideColumnNameEmployee.add("PERIOD");
        
        ArrayList<Integer> arrayIndexHideColumnEmployee = new ArrayList<Integer>();
        
        
        // ============================== SETTING SUBTOTAL ==============================
        
        //GET LIST SUBTOTAL
        String whereSubTotal = " RPT_CONFIG_DATA_TYPE = 3 AND RPT_CONFIG_DATA_GROUP = 0 AND RPT_MAIN_ID = " + oidCustom;
        Vector listSubTotal = PstCustomRptConfig.list(0, 1, whereSubTotal, "");
        
        //SET VARIABLE SUBTOTAL
        JSONArray jArraySubTotal = new JSONArray();
        for (int st = 0; st < listSubTotal.size(); st++) {
            CustomRptConfig sbt = (CustomRptConfig) listSubTotal.get(st);
            if (arrayHideColumnNameEmployee.contains(sbt.getRptConfigFieldName())) {
                continue;
            }
            JSONObject jObjectSubTotal = new JSONObject();
            //GET SUBTOTAL NAME
            if ("COUNT".equals(sbt.getRptConfigTableGroup())) {
                jObjectSubTotal.put("SUBTOTAL_NAME", sbt.getRptConfigTableName());
                //GET SUBTOTAL INDEX
                for (int ef = 0; ef < listEmployeeField.size(); ef++) {
                    CustomRptConfig fields = (CustomRptConfig) listEmployeeField.get(ef);
                    if (fields.getRptConfigFieldName().equals(sbt.getRptConfigTableName())) {
                        jObjectSubTotal.put("SUBTOTAL_INDEX", ef);
                        jObjectSubTotal.put("SUBTOTAL_TITLE", fields.getRptConfigFieldHeader());
                        break;
                    }
                }
            }
            jObjectSubTotal.put("SUBTOTAL_DATA", "");
            jObjectSubTotal.put("SUBTOTAL_COUNT", 0);
            jObjectSubTotal.put("SUBTOTAL_SHOW", false);
            jArraySubTotal.put(jObjectSubTotal);
        }
        
        
        // ============================== DRAW TABLE DATA ==============================
        
        String table = "<table class='tblStyle'>";
        table += "<tr>";
        table +="<td class='title_tbl'>NO</td>";
        for (int ja = 0; ja < jArraySubTotal.length(); ja++) {
            JSONObject jo = jArraySubTotal.getJSONObject(ja);
            table +="<td class='title_tbl'>" + jo.getString("SUBTOTAL_TITLE").toUpperCase() + "</td>";
        }
        table +="<td class='title_tbl'>JUMLAH</td>";
        
        // ========== SHOW COLUMN HEADER EMPLOYEE DATA ==========
        for (int ef = 0; ef < listEmployeeField.size(); ef++) {
            CustomRptConfig fields = (CustomRptConfig) listEmployeeField.get(ef);
            //GET INDEX COLUMN TO HIDE
            if (arrayHideColumnNameEmployee.contains(fields.getRptConfigFieldName())) {
                arrayIndexHideColumnEmployee.add(ef);
            } else {
                //table += "<td class='title_tbl'>" + fields.getRptConfigFieldHeader() + "</td>";
            }
        }
        
        // ========== SHOW COLUMN HEADER SALARY COMPONENT ==========
        
        //GET SALARY COMPONENT DEDUCTION
        Vector<PayComponent> listCompDeduction = PstPayComponent.list(0, 0, "COMP_TYPE=2", "");
        ArrayList<Integer> arrayIndexComponentDeduction = new ArrayList<Integer>();
        
        for (int sf = 0; sf < listSalaryComponentField.size(); sf++) {
            CustomRptConfig comp = (CustomRptConfig) listSalaryComponentField.get(sf);
            table += "<td class='title_tbl'>" + comp.getRptConfigFieldHeader() + "</td>";
            
            //GET INDEX SALARY COMPONENT DEDUCTION
            for (PayComponent pc : listCompDeduction) {
                if (pc.getCompName().equals(comp.getRptConfigFieldHeader())) {
                    arrayIndexComponentDeduction.add(sf);
                    break;
                }
            }
        }
        
        // ========== SHOW COLUMN HEADER COMBINE ==========
        for (int cf = 0; cf < listCombineField.size(); cf++) {
            CustomRptConfig comb = (CustomRptConfig) listCombineField.get(cf);
            table += "<td class='title_tbl'>" + comb.getRptConfigFieldHeader() + "</td>";
        }
        table += "</tr>";
        
        
        // ========== SHOW COLUMN DATA ==========
        String lastData = "";
        String styleData = "background-color: #EEE;";
        
        double[] grandTotalSalary = new double[listSalaryComponentField.size()];
        double[] grandTotalCombine = new double[listCombineField.size()];
        
        int nomor = 0;
        int grandTotalEmployee = 0;
        for (int i = 0; i < listRecord.size(); i++) {
            CustomRptDynamic dyc = (CustomRptDynamic) listRecord.get(i);
            
            // ========== SHOW DATA EMPLOYEE ==========
            for (int ef = 0; ef < listEmployeeField.size(); ef++) {
                CustomRptConfig rpt = (CustomRptConfig) listEmployeeField.get(ef);
                if (arrayIndexHideColumnEmployee.contains(ef)) {
                    continue;
                }
                String currentData = dyc.getField(rpt.getRptConfigFieldName());
                
                //CHECK IF SUBTOTAL SHOULD SHOW
                for (int ja = 0; ja < jArraySubTotal.length(); ja++) {
                    JSONObject jo = jArraySubTotal.getJSONObject(ja);
                    if (jo.get("SUBTOTAL_INDEX").equals(ef)) {
                        jo.put("SUBTOTAL_DATA", currentData);
                        
                        //COUNT DATA
                        int count = jo.getInt("SUBTOTAL_COUNT");
                        jo.put("SUBTOTAL_COUNT", count + 1);
                        
                        //GET CURRENT DATA
                        if (!lastData.equals(currentData)) {
                            lastData = currentData;
                        }
                        
                        //CHECK IF CURRENT DATA IS EQUAL TO THE NEXT DATA (IF NEXT DATA IS EXIST)
                        try {
                            CustomRptDynamic dycNext = (CustomRptDynamic) listRecord.get(i + 1);
                            String nextData = dycNext.getField(rpt.getRptConfigFieldName());
                            if (lastData.equals(nextData)) {
                                jo.put("SUBTOTAL_SHOW", false);
                            } else {
                                jo.put("SUBTOTAL_SHOW", true);
                            }
                        } catch (Exception e) {
                            jo.put("SUBTOTAL_SHOW", true);
                        }
                    }
                }
            }
            
            // ========== SHOW DATA SALARY COMPONENT ==========
            for (int sf = 0; sf < listSalaryComponentField.size(); sf++) {
                CustomRptConfig rptComp = (CustomRptConfig)listSalaryComponentField.get(sf);
                double salary = convertDouble(dyc.getField(rptComp.getRptConfigFieldName()));
                grandTotalSalary[sf] += salary;
                
                //COUNT SUBTOTAL SALARY
                for (int ja = 0; ja < jArraySubTotal.length(); ja++) {
                    JSONObject jo = jArraySubTotal.getJSONObject(ja);
                    if (jo.isNull("" + sf)) {
                        jo.put("" + sf, 0);
                    }
                    double storedSalary = jo.getDouble("" + sf);
                    jo.put("" + sf, storedSalary + salary);
                }
            }
            
            // ========== SHOW DATA COMBINE ==========
            for (int cf = 0; cf < listCombineField.size(); cf++) {
                CustomRptConfig rptComb = (CustomRptConfig)listCombineField.get(cf);
                double combine = convertDouble(dyc.getField(rptComb.getRptConfigFieldName()));
                grandTotalCombine[cf] += combine;
                        
                //COUNT SUBTOTAL COMBINE
                for (int ja = 0; ja < jArraySubTotal.length(); ja++) {
                    JSONObject jo = jArraySubTotal.getJSONObject(ja);
                    if (jo.isNull("" + cf)) {
                        jo.put("" + cf, 0);
                    }
                    double storedCombine = jo.getDouble("" + cf);
                    jo.put("" + cf, storedCombine + combine);
                }
            }
            
            // ========== SHOW SUBTOTAL ==========
            for (int ja = 0; ja < jArraySubTotal.length(); ja++) {
                JSONObject jo = jArraySubTotal.getJSONObject(ja);
                if (jo.get("SUBTOTAL_SHOW").equals(true) && i > 0) {
                    nomor++;
                    
                    table += "<tr>";
                    table += "<td style='" + styleData + "'>" + nomor + "</td>";
                    for (int ja2 = 0; ja2 < jArraySubTotal.length(); ja2++) {
                        JSONObject jo2 = jArraySubTotal.getJSONObject(ja2);
                        table +="<td style='" + styleData + "'>" + jo2.getString("SUBTOTAL_DATA") + "</td>";
                    }
                    table += "<td style='text-align: center;" + styleData + "'>" + jo.get("SUBTOTAL_COUNT") + "</td>";
                    grandTotalEmployee += jo.getDouble("SUBTOTAL_COUNT");
                    
                    //RESET DATA SUBTOTAL
                    jo.put("SUBTOTAL_COUNT", 0);
                    
                    for (int sf = 0; sf < listSalaryComponentField.size(); sf++) {
                        double subTotalSalary = jo.getDouble("" + sf);
                        String fontRed = "";
                        if (arrayIndexComponentDeduction.contains(sf)) {
                            fontRed = "color: red;";
                        }
                        table += "<td style='text-align: right;" + styleData + fontRed + "'>" + String.format("%,.0f", subTotalSalary) + "</td>";
                        //RESET DATA SUBTOTAL SALARY
                        jo.put("" + sf, 0);
                    }
                    
                    for (int cf = 0; cf < listCombineField.size(); cf++) {
                        double subTotalCombine = jo.getDouble("" + cf);
                        String fontRed = "";
                        if (arrayIndexComponentDeduction.contains(cf)) {
                            fontRed = "color: red;";
                        }
                        table += "<td style='text-align: right;" + styleData + fontRed + "'>" + String.format("%,.0f", subTotalCombine) + "</td>";
                        //RESET DATA SUBTOTAL COMBINE
                        jo.put("" + cf, 0);
                    }
                    table += "</tr>";
                    
                }
            }
        }
        
        // ========== SHOW GRAND TOTAL ==========
        int colspanGrandTotal = jArraySubTotal.length() + 1;
        table += "<tr>";
        //table += "<td class='td_grand_total'></td>";
        table += "<td class='td_grand_total' colspan='" + colspanGrandTotal + "'>Grand Total</td>";
        table += "<td class='td_grand_total' style='text-align: center;'>" + grandTotalEmployee + "</td>";
        
        for (int sf = 0; sf < listSalaryComponentField.size(); sf++) {
            table += "<td class='td_grand_total' style='text-align: right;'>" + String.format("%,.0f", grandTotalSalary[sf]) + "</td>";
        }
        
        for (int cf = 0; cf < listCombineField.size(); cf++) {
            table += "<td class='td_grand_total' style='text-align: right;'>" + String.format("%,.0f", grandTotalCombine[cf]) + "</td>";
        }
        table += "</tr>";
        
        table += "<table>";
        return table;
    }
    
    public String drawListWithoutPaySlip(Vector listField, Vector listRecord, long oidCustom){
        String table = "";
        String[][] arrField = new String[listRecord.size() + 1][listField.size()]; /* for dinamic fields */
            
        if (listRecord != null && listRecord.size() > 0) {
            for (int y = 0; y < listRecord.size(); y++) {
                CustomRptDynamic dyc = (CustomRptDynamic) listRecord.get(y);
                /* Get dinamis field */
                if (listField != null && listField.size() > 0) {
                    for (int i = 0; i < listField.size(); i++) {
                        CustomRptConfig rpt = (CustomRptConfig) listField.get(i);
                        arrField[y][i] = dyc.getField(rpt.getRptConfigFieldName());
                    }
                }
            }
        }
            
        int nomor = 0;
        int yStart = 0;
        int yEnd = 0;
        double total = 0;
        boolean ketemu = false;
        table = "<table class='tblStyle'>";
        table += "<tr>";
        table += "<td class='title_tbl'>No</td>";
        /* mengambil field dinamis */
        for (int i = 0; i < listField.size(); i++) {
            CustomRptConfig fields = (CustomRptConfig) listField.get(i);
            table += "<td class='title_tbl'>" + fields.getRptConfigFieldHeader() + "</td>";
        }
            
        table += "</tr>";
        /* ============== record result ==============*/
        if (listRecord != null && listRecord.size() > 0) {
            for (int y = 0; y < listRecord.size(); y++) {
                nomor++;
                table += "<tr>";
                table += "<td>" + nomor + "</td>";
                /* Fields dinamic */
                if (listField != null && listField.size() > 0) {
                    for (int i = 0; i < listField.size(); i++) {
                        if (arrField[y][i] != null) {
                            table += "<td>" + arrField[y][i] + "</td>";
                        }
                    }
                }
                table += "</tr>";
                yStart = yEnd;
            }
        }
        table += "</table>";
            
        return table;
    }
    
    /* Update 2016-04-15 */
    public String getBankName(String oid){
        String name = "";
        if (oid.equals("0")){
            name = "CASH";
        } else {
            try {
                PayBanks payBank = PstPayBanks.fetchExc(Long.valueOf(oid));
                name = payBank.getBankName();
            } catch(Exception e){
                System.out.println(e.toString());
            }
        }
        
        return name;
    }
%>
<!DOCTYPE html>
<%  

    /* Update by Hendra Putu | 20150403 */
    int iCommand = FRMQueryString.requestCommand(request);
    long oidCustom = FRMQueryString.requestLong(request, "oid_custom");
    int start = FRMQueryString.requestInt(request, "start");
    int prevCommand = FRMQueryString.requestInt(request, "prev_command");
    int generate = FRMQueryString.requestInt(request, "generate");
    int selectionChoose = FRMQueryString.requestInt(request, "selection_choose");
    String[] whereField = FRMQueryString.requestStringValues(request, "where_field");
    String[] whereValue = FRMQueryString.requestStringValues(request, "where_value");
    String[] whereType = FRMQueryString.requestStringValues(request, "where_type");
    String[] operator = FRMQueryString.requestStringValues(request, "operator");
    String whereCustom = FRMQueryString.requestString(request, "where_custom");
    /* Update by Hendra Putu | 2015-11-05 */
    int showValue = FRMQueryString.requestInt(request, "show_value");
    int viewType = FRMQueryString.requestInt(request, "view_type");
    /*
    * showValue berfungsi jika total gross = 0 | Total Gross = Componen + Componen + ... 
    */
    /* get Custom Report Title */
    CustomRptMain customRptMain = new CustomRptMain();
    if (oidCustom > 0){
        customRptMain = PstCustomRptMain.fetchExc(oidCustom);
    }
    /* menentukan nilai custom report to Export XLS */
    String dataExportExcel = "";
    boolean ketemuData = false;
    Vector listCustToXLS = PstCustomRptConfig.list(0, 0, PstCustomRptConfig.fieldNames[PstCustomRptConfig.FLD_RPT_MAIN_ID]+"="+oidCustom, "");
    if (listCustToXLS != null && listCustToXLS.size()>0){
        for(int x=0; x<listCustToXLS.size(); x++){
            CustomRptConfig cRc = (CustomRptConfig)listCustToXLS.get(x);
            if (cRc.getRptConfigTableGroup().equals("pay_slip")){
                ketemuData = true;
            }
        }
    }
    if (ketemuData == true){
        dataExportExcel = "CustomRptXLSDetail";//"CustomRptXLS";
    } else {
        dataExportExcel = "CustomRptVersi2XLS";
    }
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Custom Report Generate</title>
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
        <link rel="stylesheet" href="../../styles/main.css" type="text/css">
        <link rel="stylesheet" href="../../styles/tab.css" type="text/css">
        <link href="<%=approot%>/stylesheets/superTables.css" rel="Stylesheet" type="text/css" /> 
        <style type="text/css">
            #menu_utama {padding: 9px 14px; border-left: 1px solid #0099FF; font-size: 14px; background-color: #F3F3F3;}
            #menu_title {color:#0099FF; font-size: 14px; font-weight: bold;}
            #menu_teks {color:#CCC;}
            #mn_utama {color: #FF6600; padding: 5px 14px; border-left: 1px solid #999; font-size: 14px; background-color: #F5F5F5;}
            #btn {
              background: #C7C7C7;
              border: 1px solid #BBBBBB;
              border-radius: 3px;
              font-family: Arial;
              color: #474747;
              font-size: 11px;
              padding: 3px 7px;
              cursor: pointer;
            }

            #btn:hover {
              color: #FFF;
              background: #B3B3B3;
              border: 1px solid #979797;
            }
            #btn1 {
              background: #f27979;
              border: 1px solid #d74e4e;
              border-radius: 3px;
              font-family: Arial;
              color: #ffffff;
              font-size: 12px;
              padding: 3px 9px 3px 9px;
            }

            #btn1:hover {
              background: #d22a2a;
              border: 1px solid #c31b1b;
            }
            #tdForm {
                padding: 5px;
            }
            .tblStyle {border-collapse: collapse;font-size: 11px;}
            .tblStyle td {padding: 3px 5px; border: 1px solid #CCC;}
            .title_tbl {font-weight: bold;background-color: #DDD; color: #575757;}
            .td_grand_total {font-weight: bold; background-color: #0096bb; color: #F5F5F5;}
            #confirm {background-color: #fad9d9;border: 1px solid #da8383; color: #bf3c3c; padding: 14px 21px;border-radius: 5px;}
            #td1 {border: 1px solid #CCC; background-color: #DDD;}
            #td2 {border: 1px solid #CCC;}
            #tdTotal {background-color: khaki/*#fad9d9*/; font-weight: bold}
            #tdTotalSummary {background-color: #ffffff;}
            #query {
                padding: 7px 9px; color: #f7f7f7; background-color: #797979; 
                border:1px solid #575757; border-radius: 5px; 
                margin-bottom: 5px; font-size: 12px;
                font-family: Courier New,Courier,Lucida Sans Typewriter,Lucida Typewriter,monospace;
            }
            #info {
                width: 500px;
                padding: 21px; color: #797979; background-color: #F7F7F7;
                border:1px solid #DDD;
                font-size: 12px;
            }
            
            .LockOff {
                display: none;
                visibility: hidden;
            }

            .LockOn {
                display: block;
                visibility: visible;
                position: absolute;
                z-index: 999;
                top: 0px;
                left: 0px;
                width: 105%;
                height: 105%;
                background-color: #ccc;
                text-align: center;
                padding-top: 20%;
                filter: alpha(opacity=75);
                opacity: 0.75;
                font-size: 250%;
            }
        </style>
        <script type="text/javascript">
            function getCmd(){
                document.form_generate.action = "custom_rpt_generate.jsp";
                document.form_generate.submit();
            }
            function cmdGenerate(val){             
                document.form_generate.command.value="<%=Command.ADD%>"; 
                document.form_generate.selection_choose.value=val;
                document.form_generate.generate.value="1";
                getCmd();
                lockScreen('We are processing generate report, please wait...');
            }
            function cmdBack() {
                document.form_generate.command.value="<%=Command.BACK%>";               
                document.form_generate.action="custom_rpt_main.jsp";
                document.form_generate.submit();
            }
            function cmdClear() {
                document.form_generate.command.value="<%=Command.CANCEL%>";
                document.form_generate.generate.value="0";
                document.form_generate.action="custom_rpt_generate.jsp";
                document.form_generate.submit();
            }
            function cmdShowQuery(data){
                document.form_generate.query_result.value=data;

                document.form_generate.action = "custom_rpt_query.jsp","ShowQuery", "height=457,width=457,status=yes,toolbar=no,menubar=no,location=center,scrollbars=yes";
                document.form_generate.submit();
            }
            function cmdExportToExcel(data){
                //added by dewok 2019-06-03 for check option view detail or summary
                var viewType = document.getElementsByName('view_type');
                for (var i = 0; i < viewType.length; i++) {
                    if (viewType[i].checked) {
                        if (viewType[i].value === "1") {
                            data = "CustomRptXLSSummary";
                        }
                        break;
                    }
                }
                document.form_generate.action="<%=printroot%>.report.payroll."+data; 
                document.form_generate.target = "ReportExcel";
                document.form_generate.submit();
            }
            function cmdSendExcel(){
                document.form_generate.action="<%=printroot%>.report.payroll.CustomRptXLSEmail"; 
                document.form_generate.target = "ReportExcel";
                document.form_generate.submit();
            }
            function cmdChooseSelection(val){
                document.form_generate.generate.value="0";
                document.form_generate.selection_choose.value=val;
                document.form_generate.action="custom_rpt_generate.jsp";
                document.form_generate.submit();
            }
            function cmdPosted()
            {
              document.form_generate.command.value="<%=Command.SAVE%>";
              document.form_generate.action="custom_rpt_generate.jsp";
              document.form_generate.submit();
              lockScreen('We are posting the journals, please wait...');
            }

            function lockScreen(str)
            {
               var lock = document.getElementById('theLockPane');
               if (lock)
                  lock.className = 'LockOn';

               lock.innerHTML = str;
            }
        </script>
<script src="../../javascripts/jquery.min-1.6.2.js" type="text/javascript"></script>
<script src="../../javascripts/chosen.jquery.js" type="text/javascript"></script>
<link rel="stylesheet" href="../../stylesheets/chosen.css" >
    </head>
    <body <%=noBack%> bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
        <div id="theLockPane" class="LockOff"></div> 
        <table width="100%" border="0" cellspacing="0" cellpadding="0" height="100%" bgcolor="#F9FCFF" >
            <%if (headerStyle && !verTemplate.equalsIgnoreCase("0")) {%> 
            <%@include file="../../styletemplate/template_header.jsp" %>
            <%} else {%>
            <tr> 
                <td ID="TOPTITLE" background="<%=approot%>/images/HRIS_HeaderBg3.jpg" width="100%" height="54"> 
                    <!-- #BeginEditable "header" --> 
                    <%@ include file = "../../main/header.jsp" %>
                    <!-- #EndEditable --> </td>
            </tr>
            <tr> 
                <td  bgcolor="#9BC1FF" height="15" ID="MAINMENU" valign="middle"> <!-- #BeginEditable "menumain" --> 
                    <%@ include file = "../../main/mnmain.jsp" %>
                    <!-- #EndEditable --> </td>
            </tr>
            <tr> 
                <td  bgcolor="#9BC1FF" height="10" valign="middle"> 
                    <table width="100%" border="0" cellspacing="0" cellpadding="0">
                        <tr> 
                            <td align="left"><img src="<%=approot%>/images/harismaMenuLeft1.jpg" width="8" height="8"></td>
                            <td align="center" background="<%=approot%>/images/harismaMenuLine1.jpg" width="100%"><img src="<%=approot%>/images/harismaMenuLine1.jpg" width="8" height="8"></td>
                            <td align="right"><img src="<%=approot%>/images/harismaMenuRight1.jpg" width="8" height="8"></td>
                        </tr>
                    </table>
                </td>
            </tr>
            <%}%>
            <tr> 
                <td width="88%" valign="top" align="left"> 
                    <table width="100%" border="0" cellspacing="3" cellpadding="2" id="tbl0">
                        <tr> 
                            <td width="100%" colspan="3" valign="top" style="padding: 12px"> 
                                <table width="100%" border="0" cellspacing="0" cellpadding="0">
                                    <tr>
                                        <td height="20"> <div id="menu_utama"><button id="btn" onclick="cmdBack()">Back</button>&nbsp; <span id="menu_title"><%=customRptMain.getRptMainTitle()%></span> - <span id="menu_teks">Custom Report Generate</span> <!-- #EndEditable --> </div> </td>
                                    </tr>
                                    <tr>
                                        <td>&nbsp;</td>
                                    </tr>
                                    <tr>
                                        <td style="background-color:#EEE;" valign="top">
                                        
                                            <table style="padding:9px; border:1px solid #00CCFF;" <%=garisContent%> width="100%" border="0" cellspacing="1" cellpadding="1" class="tablecolor">
                                                
                                                <tr>
                                                    <td valign="top">
                                                        <form name="form_generate" method="POST" action="">
                                                            <input type="hidden" name="command" value="<%=iCommand%>" />
                                                            <input type="hidden" name="oid_custom" value="<%=oidCustom%>" />
                                                            <input type="hidden" name="start" value="<%=start%>" />
                                                            <input type="hidden" name="prev_command" value="<%=prevCommand%>" />
                                                            <input type="hidden" name="generate" value="<%=generate%>" />
                                                            <input type="hidden" name="query_result" value="" />
                                                            <input type="hidden" name="selection_choose" value="" />
                                                            
                                                           
                                                            <table>
                                                                
                                                                <tr>
                                                                    <td colspan="2">
                                                                        <div id="info">
                                                                            <h2>Operator Description</h2>
                                                                            <p><strong>=, !=, >, <, >=, <=</strong><br />
                                                                                This operator is used for only one of data, either number or letters. 
                                                                                Example : name = 'John' or Salary = 1000. But in this field, just type value of data.
                                                                                Example : John or 219.
                                                                            </p>
                                                                            <p><strong>IN</strong><br />
                                                                                It is used for some of data which it have same type. Example: IN(123, 456, 78) or IN('abc', 'def'). Just type test, text, etc.
                                                                            </p>
                                                                            <p><strong>BETWEEN</strong><br />
                                                                                The BETWEEN operator is used when you want to get data by range. E.g: 2014-12-01 To 2015-01-01.
                                                                                So you just type 2014-12-01 2015-01-01. ([date_from][space][date_to]) Note: the between operator is used if the value is date.
                                                                            </p>
                                                                            <p><strong>LIKE</strong><br />
                                                                                You can type not complete value. E.g: You want to find some of data that has letter 'a', so you must type: %a%</p>                                                                         
                                                                        </div>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td>
                                                                        <%
                                                                        if (selectionChoose == 0){
                                                                            %>
                                                                            <input type="radio" name="choose" value="0" onclick="cmdChooseSelection(this.value)" checked="checked" />Default
                                                                            &nbsp;
                                                                            <input type="radio" name="choose" value="1" onclick="cmdChooseSelection(this.value)" />Custom
                                                                            <%
                                                                        } else {
                                                                            %>
                                                                            <input type="radio" name="choose" value="0" onclick="cmdChooseSelection(this.value)" />Default
                                                                            &nbsp;
                                                                            <input type="radio" name="choose" value="1" onclick="cmdChooseSelection(this.value)" checked="checked" />Custom
                                                                            <%
                                                                        }
                                                                        %>
                                                                        
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td>
                                                                        Show zero (0) value :
                                                                        <input type="radio" name="show_value" <%= (showValue == 0 ? "checked":"") %> value="0" />Yes &nbsp;| 
                                                                        <input type="radio" name="show_value" <%= (showValue == 1 ? "checked":"") %> value="1" />No
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td>Report View Type :
                                                                        <input type="radio" name="view_type" <%= (viewType == 0 ? "checked":"") %> value="0" />Detail &nbsp;| 
                                                                        <input type="radio" name="view_type" <%= (viewType == 1 ? "checked":"") %> value="1" />Summary
                                                                        <%--
                                                                        - Per :
                                                                        <select name="summary_group">
                                                                            <%//
                                                                                String whereSubTotal = " RPT_CONFIG_DATA_TYPE = 3 AND RPT_CONFIG_DATA_GROUP = 0 AND RPT_MAIN_ID = " + oidCustom;
                                                                                Vector<CustomRptConfig> listSubTotal = PstCustomRptConfig.list(0, 0, whereSubTotal, "");
                                                                                for (CustomRptConfig sub : listSubTotal) {
                                                                            %>
                                                                            <option value="<%=sub.getRptConfigTableName() %>"><%=sub.getRptConfigTableName()%></option>
                                                                            <%
                                                                                }
                                                                            %>
                                                                        </select>
                                                                        --%>
                                                                    </td>
                                                                </tr>
                                                                <%
                                                                if (selectionChoose == 0){
                                                                %>
                                                                <tr>
                                                                    <td colspan="2">
                                                                        <h2>Selection default</h2>
                                                                        <table>
                                                                        <%
                                                                        if (oidCustom != 0){
                                                                            /* mengambil where yang ada di config report */
                                                                            Vector listWhere = PstCustomRptConfig.listWhere(oidCustom);
                                                                            if (listWhere != null && listWhere.size()>0){
                                                                                for(int i=0; i<listWhere.size(); i++){
                                                                                    CustomRptConfig selection = (CustomRptConfig)listWhere.get(i);
                                                                                    %>
                                                                                    <tr>
                                                                                        <td>
                                                                                            <%=selection.getRptConfigFieldHeader()%>
                                                                                        </td>
                                                                                        <td>
                                                                                            <input type="hidden" name="where_field" value="<%=selection.getRptConfigTableName()+"."+selection.getRptConfigFieldName()%>" />
                                                                                            <select name="operator">
                                                                                                <%
                                                                                                    String operatorSelected = "";
                                                                                                    if (operator != null) {
                                                                                                        operatorSelected = operator[i];
                                                                                                    }
                                                                                                %>
                                                                                                <option <%= (operatorSelected.equals("=") ? "selected":"") %> value="=">=</option>
                                                                                                <option <%= (operatorSelected.equals("!=") ? "selected":"") %> value="!=">!=</option>
                                                                                                <option <%= (operatorSelected.equals(">") ? "selected":"") %> value=">">></option>
                                                                                                <option <%= (operatorSelected.equals("<") ? "selected":"") %> value="<"><</option>
                                                                                                <option <%= (operatorSelected.equals(">=") ? "selected":"") %> value=">=">>=</option>
                                                                                                <option <%= (operatorSelected.equals("<=") ? "selected":"") %> value="<="><=</option>
                                                                                                <option <%= (operatorSelected.equals("BETWEEN") ? "selected":"") %> value="BETWEEN">BETWEEN</option>
                                                                                                <option <%= (operatorSelected.equals("LIKE") ? "selected":"") %> value="LIKE">LIKE</option>
                                                                                                <option <%= (operatorSelected.equals("IN") ? "selected":"") %> value="IN">IN</option>
                                                                                            </select>
                                                                                            <!--<input type="text" name="where_value" value="" size="70" />-->
                                                                                            <%
                                                                                            String fieldName = selection.getRptConfigFieldHeader();
                                                                                            String[] dataFields = fieldName.split(" ");
                                                                                            boolean isDate = false;
                                                                                            if (dataFields != null && dataFields.length>0){
                                                                                                for(int df=0; df<dataFields.length; df++){
                                                                                                    String hurufkecil = dataFields[df].toLowerCase();
                                                                                                    if (hurufkecil.equals("date")){
                                                                                                        isDate = true;
                                                                                                    }
                                                                                                }
                                                                                            }
                                                                                            if (isDate == true){
                                                                                                %>
                                                                                                <input type="text" name="where_value" value="" size="70" />
                                                                                                <%
                                                                                            } else {
                                                                                                %>
                                         
                                                                                                <select name="where_value" class="chosen-select">
                                                                                                    <option value="">-SELECT-</option>
                                                                                                    <%
                                                                                                    String sqlData = "SELECT DISTINCT "+selection.getRptConfigFieldName()+" FROM "+selection.getRptConfigTableName();
                                                                                                    Vector listSelectData = PstCustomRptConfig.listSelectData(sqlData, selection.getRptConfigFieldName());
                                                                                                    if (listSelectData != null && listSelectData.size()>0){
                                                                                                        if (selection.getRptConfigFieldName().equals("BANK_ID") || selection.getRptConfigFieldName().equals("STATUS_DATA")){
                                                                                                            if (selection.getRptConfigFieldName().equals("BANK_ID")){
                                                                                                                
                                                                                                                for(int sd=0; sd<listSelectData.size(); sd++){
                                                                                                                    CustomRptDynamic dycData = (CustomRptDynamic)listSelectData.get(sd);
                                                                                                                    %>
                                                                                                                    <option value="<%=dycData.getField(selection.getRptConfigFieldName())%>"><%= getBankName(dycData.getField(selection.getRptConfigFieldName())) %></option>
                                                                                                                    <%
                                                                                                                }        
                                                                                                            }
                                                                                                            if (selection.getRptConfigFieldName().equals("STATUS_DATA")){
                                                                                                                %>
                                                                                                                <option value="0">Active</option>
                                                                                                                <option value="1">History</option>
                                                                                                                <%
                                                                                                            }
                                                                                                        } else {
                                                                                                            String divisionSelected = "";
                                                                                                            if (selection.getRptConfigFieldName().equals("DIVISION")){
                                                                                                                /* Check Administrator */
                                                                                                                if (appUserSess.getAdminStatus()==0){
                                                                                                                    
                                                                                                                    try {
                                                                                                                        Division divisi = PstDivision.fetchExc(emplx.getDivisionId());
                                                                                                                        divisionSelected = divisi.getDivision();
                                                                                                                    } catch(Exception e){
                                                                                                                        System.out.println("divisionName=>"+e.toString());
                                                                                                                    }
                                                                                                                }
                                                                                                            }
                                                                                                            if (divisionSelected.length()>0){
                                                                                                                %>
                                                                                                                <option selected="selected" value="<%=divisionSelected%>"><%=divisionSelected%></option>
                                                                                                                <%
                                                                                                            } else {
                                                                                                                for(int sd=0; sd<listSelectData.size(); sd++){
                                                                                                                    CustomRptDynamic dycData = (CustomRptDynamic)listSelectData.get(sd);
                                                                                                                    String selected = "";
                                                                                                                    if (whereValue != null) {
                                                                                                                        if (dycData.getField(selection.getRptConfigFieldName()).equals(whereValue[i])) {
                                                                                                                            selected = "selected";
                                                                                                                        }
                                                                                                                    }
                                                                                                                    %>
                                                                                                                    <option <%= selected %> value="<%=dycData.getField(selection.getRptConfigFieldName())%>"><%= dycData.getField(selection.getRptConfigFieldName()) %></option>
                                                                                                                    <%
                                                                                                                }
                                                                                                            }
                                                                                                            
                                                                                                        }
                                                                                                    }
                                                                                                    %>

                                                                                                </select>
                                                                                                <%
                                                                                            }
                                                                                            %>
                                                                                            
                                                                                            <input type="hidden" name="where_type" value="<%=selection.getRptConfigFieldType()%>" />
                                                                                        </td>
                                                                                    </tr>
                                                                                    <%
                                                                                }
                                                                            }
                                                                        }
                                                                        %>
                                                                        </table>
                                                                    </td>
                                                                </tr>
                                                                <% } else { %>
                                                                <tr>
                                                                    <td colspan="2">
                                                                        <h2>Selection Custom</h2>
                                                                        <textarea name="where_custom" rows="5" cols="52">
                                                                            
                                                                        </textarea> 
                                                                    </td>
                                                                </tr>
                                                                <% } %>
                                                                
                                                                <tr>
                                                                    <td colspan="2">
                                                                        <button id="btn" onclick="cmdGenerate(<%=selectionChoose%>)">Generate</button>&nbsp;
                                                                        <button id="btn" onclick="cmdExportToExcel('<%=dataExportExcel%>')">Export to Excel</button>&nbsp;
                                                                        <button id="btn" onclick="cmdClear()">Clear</button>&nbsp;
                                                                        <button id="btn" onclick="cmdSendExcel()">Send Excel</button>&nbsp;  
                                                                        Email : <input type="text" name="emailAddress" placeholder="alamat email" value="" />
                                                                    </td>
                                                                </tr>
                                                                
                                                            </table>
                                                        </form>
                                                    </td>
                                                </tr>
                                                <%
                                                if (generate == 1){
                                                %>
                                                <tr>
                                                    <td>
                                                        <%
                                                        /*
                                                        * Description : Code to find WHERE DATA 
                                                        */
                                                        String whereClause = "";
                                                        whereClause = PstCustomRptConfig.findWhereData(selectionChoose, whereField, whereValue, whereType, operator, whereCustom);
                                                        /*
                                                        * Description : Get SELECT DATA and JOIN DATA
                                                        * Date : 2015-04-08 
                                                        */
                                                        String[] dataJoin = new String[7];
                                                        
                                                        String strSelect = "SELECT ";
                                                        String strJoin = "";
                                                        String strOrderBy = "";
                                                        int inc = 0;
                                                        /* WHERE CLAUSE WITH RPT_CONFIG_DATA_TYPE = 0 */
                                                        String where = " RPT_CONFIG_DATA_TYPE = 0 AND RPT_CONFIG_DATA_GROUP = 0 AND RPT_MAIN_ID ="+oidCustom;
                                                        Vector listData = PstCustomRptConfig.list(0, 0, where, "");
                                                        /* WHERE CLAUSE WITH RPT_CONFIG_DATA_TYPE = 2 */
                                                        String whereComp = " RPT_CONFIG_DATA_TYPE = 2 AND RPT_CONFIG_DATA_GROUP = 0 AND RPT_MAIN_ID ="+oidCustom;
                                                        Vector listSalaryComp = PstCustomRptConfig.list(0, 0, whereComp, "");
                                                        /* WHERE CLAUSE WITH RPT_CONFIG_DATA_TYPE = 1 */
                                                        String whereComb = " RPT_CONFIG_DATA_TYPE = 1 AND RPT_CONFIG_DATA_GROUP = 0 AND RPT_MAIN_ID ="+oidCustom;
                                                        Vector listComb = PstCustomRptConfig.list(0, 0, whereComb, "");
                                                        /* Array join */
                                                        int[] joinCollection = new int[PstCustomRptConfig.joinDataPriority.length];
                                                        /* inisialisasi joinCollection */
                                                        for(int d=0; d<PstCustomRptConfig.joinDataPriority.length; d++){
                                                            joinCollection[d] = -1;
                                                        }
                                                        boolean found = false;
                                                        if (listData != null && listData.size() > 0){
                                                            int incC = 0;
                                                            for(int i=0;i<listData.size(); i++){
                                                                CustomRptConfig rpt = (CustomRptConfig)listData.get(i);
                                                                /* mendapatkan join data */
                                                                for(int k=0; k<PstCustomRptConfig.joinData.length; k++){
                                                                    for (String retval : PstCustomRptConfig.joinData[k].split(" ")) {
                                                                        dataJoin[inc] = retval;
                                                                        inc++;
                                                                    }
                                                                    inc = 0;
                                                                    
                                                                    /* bandingkan nilai table dengan data join */
                                                                    
                                                                if(rpt.getRptConfigTableAsName() != null){
                                                                    if (rpt.getRptConfigTableAsName().equals(dataJoin[4])){
                                                                        /* cek data join pada array joinCollection */
                                                                        for(int c=0; c<joinCollection.length; c++){
                                                                            if (PstCustomRptConfig.joinDataPriority[k] == joinCollection[c]){
                                                                                found = true; /* jika found == true, maka data sudah ada di joinCollection */
                                                                            }
                                                                        }
                                                                        if (found == false){
                                                                            joinCollection[incC] = PstCustomRptConfig.joinDataPriority[k];
                                                                        }
                                                                        found = false;
                                                                    }
                                                                } else {
                                                                    if (rpt.getRptConfigTableName().equals(dataJoin[2])){
                                                                        /* cek data join pada array joinCollection */
                                                                        for(int c=0; c<joinCollection.length; c++){
                                                                            if (PstCustomRptConfig.joinDataPriority[k] == joinCollection[c]){
                                                                                found = true; /* jika found == true, maka data sudah ada di joinCollection */
                                                                            }
                                                                        }
                                                                        if (found == false){
                                                                            joinCollection[incC] = PstCustomRptConfig.joinDataPriority[k];
                                                                        }
                                                                        found = false;
                                                                    }    
                                                                }
                                                                    
                                                                }
                                                                incC++;
                                                                /* mendapatkan data select */
                                                                if(rpt.getRptConfigTableAsName() != null){
                                                                    strSelect += (rpt.getRptConfigTableAsName().equals("")?rpt.getRptConfigTableName():rpt.getRptConfigTableAsName())+"."+rpt.getRptConfigFieldName();
                                                                } else {
                                                                    strSelect += rpt.getRptConfigTableName()+"."+rpt.getRptConfigFieldName();
                                                                }
                                                                if (i == listData.size()-1){
                                                                    strSelect +=" ";
                                                                } else {
                                                                    strSelect += ", ";
                                                                }
                                                            }
                                                            /* join Collection */
                                                            Arrays.sort(joinCollection); /* sorting array */
                                                            for(int m=0; m<joinCollection.length; m++){
                                                                if (joinCollection[m] != -1){
                                                                    strJoin += PstCustomRptConfig.joinData[joinCollection[m]] + " ";
                                                                }
                                                            }
                                                        }
                                                        /* ORDER BY */
                                                        String whereOrder = " RPT_CONFIG_DATA_TYPE = 0 AND RPT_CONFIG_DATA_GROUP = 2 AND RPT_MAIN_ID ="+oidCustom;
                                                        Vector listOrder = PstCustomRptConfig.list(0, 0, whereOrder, "");
                                                        if (listOrder != null && listOrder.size() > 0){
                                                            strOrderBy = " ORDER BY ";
                                                            for(int ord=0;ord<listOrder.size(); ord++){
                                                                CustomRptConfig rptOrder = (CustomRptConfig)listOrder.get(ord);
                                                                strOrderBy += rptOrder.getRptConfigTableName() + "." + rptOrder.getRptConfigFieldName();
                                                                if (ord == listOrder.size()-1){
                                                                    strOrderBy +=" ";
                                                                } else {
                                                                    strOrderBy +=", ";
                                                                }
                                                                
                                                            }
                                                        }
                                                        %>
                                                        <div id="query"><%=strSelect+" FROM hr_employee "+strJoin+" "+whereClause+ strOrderBy%></div>
                                                        <%
                                                        Vector listResult = new Vector();
                                                        if (listSalaryComp != null && listSalaryComp.size()>0){
                                                            listResult = PstCustomRptConfig.listData(strSelect+" FROM hr_employee "+strJoin+" "+whereClause + strOrderBy, listData);
                                                            %>
                                                            <% if (viewType == 0) { %>
                                                            <div><%=drawListDetail(listData, listResult, listSalaryComp, listComb, oidCustom, showValue)%></div>
                                                            <% } else { %>
                                                            <div><%=drawListSummary(listData, listResult, listSalaryComp, listComb, oidCustom, showValue)%></div>
                                                            <% } %>
                                                            <%
                                                        } else {
                                                            listResult = PstCustomRptConfig.listDataWithoutPaySlip(strSelect+" FROM hr_employee "+strJoin+" "+whereClause + strOrderBy, listData);
                                                            %>
                                                            <div><%= drawListWithoutPaySlip(listData, listResult, oidCustom) %></div>
                                                            <%
                                                        }
                                                        %>
                                                        
                                                    </td>
                                                </tr>
                                                <% } %>
                                                <%
                                                if (iCommand == Command.ASK){
                                                %>
                                                <tr>
                                                    <td>
                                                        <span id="confirm">
                                                            <strong>Are you sure to delete item ?</strong> &nbsp;
                                                            <button id="btn1" onclick="javascript:cmdDelete('<%=oidCustom%>')">Yes</button>
                                                            &nbsp;<button id="btn1" onclick="javascript:cmdBack()">No</button>
                                                        </span>
                                                    </td>
                                                </tr>
                                                <%
                                                }
                                                %>
                                                <tr>
                                                    <td>&nbsp;</td>
                                                </tr>
                                                <tr>
                                                    <td>&nbsp;</td>
                                                </tr>
                                            </table>
                                        
                                        </td>
                                    </tr>
                                </table><!---End Tble--->
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
        </table>
        <table>
            <tr>
                <td>&nbsp;</td>
            </tr>
            <%if (headerStyle && !verTemplate.equalsIgnoreCase("0")) {%>
            <tr>
                <td valign="bottom">
                    <!-- untuk footer -->
                    <%@include file="../../footer.jsp" %>
                </td>

            </tr>
            <%} else {%>
            <tr> 
                <td colspan="2" height="20" > <!-- #BeginEditable "footer" --> 
                    <%@ include file = "../../main/footer.jsp" %>
                    <!-- #EndEditable --> </td>
            </tr>
            <%}%>
        </table>
        <div id="theLockPane" class="LockOff"></div> 
    <script type="text/javascript">
            var config = {
                '.chosen-select'           : {},
                '.chosen-select-deselect'  : {allow_single_deselect:true},
                '.chosen-select-no-single' : {disable_search_threshold:10},
                '.chosen-select-no-results': {no_results_text:'Oops, nothing found!'},
                '.chosen-select-width'     : {width:"100%"}
            }
            for (var selector in config) {
                $(selector).chosen(config[selector]);
            }
    </script>
    </body>
    <!-- #BeginEditable "script" --> <script language="JavaScript">
                var oBody = document.body;
                var oSuccess = oBody.attachEvent('onkeydown',fnTrapKD);
    </script>
                
    <!-- #EndEditable --> <!-- #EndTemplate -->
</html>
<!--


-->