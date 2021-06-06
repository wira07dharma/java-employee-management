<%-- 
    Document   : generate_thr
    Created on : 16-Jan-2018, 14:36:47
    Author     : Gunadi
--%>

<%@page import="javax.swing.text.Style"%>
<%@ page language="java" %>

<%@ page import = "java.util.*" %>
<!-- package wihita -->
<%@ page import = "com.dimata.util.*" %>
<!-- package qdep -->
<%@ page import = "com.dimata.gui.jsp.*" %>
<%@ page import = "com.dimata.qdep.form.*" %>
<!--package harisma -->
<%@ page import = "com.dimata.harisma.entity.payroll.*" %>
<%@ page import = "com.dimata.harisma.form.payroll.*" %>
<%@ page import = "com.dimata.harisma.entity.admin.*" %>

<%@ include file = "../../main/javainit.jsp" %>
<% int appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_PAYROLL, AppObjInfo.G2_PAYROLL_SETUP, AppObjInfo.OBJ_PAYROLL_SETUP_COMPONENT);%>
<%@ include file = "../../main/checkuser.jsp" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%!
    public String drawList(Vector objectClass, long oidBenefitPeriod) {

        ControlList ctrlist = new ControlList();

        ctrlist.setAreaWidth("100%");
        ctrlist.setListStyle("tblStyle");
        ctrlist.setTitleStyle("title_tbl");
        ctrlist.setCellStyle("listgensell");
        ctrlist.setHeaderStyle("title_tbl");
        ctrlist.addHeader("No", "");
        ctrlist.addHeader("Cut Off Date", "");
        ctrlist.addHeader("Config Name", "");
        ctrlist.addHeader("Periode","");
        ctrlist.addHeader("Delete", "");

        ctrlist.setLinkRow(1); // untuk menge-sett link di kolom pertama atau dikolom yg lain

        Vector lstData = ctrlist.getData();
        Vector lstLinkData = ctrlist.getLinkData();
        ctrlist.setLinkPrefix("javascript:cmdEdit('");
        ctrlist.setLinkSufix("')");

        ctrlist.reset(); //berfungsi untuk menginisialisasi list menjadi kosong

        int no = 0;
        for (int i = 0; i < objectClass.size(); i++) {     
            PayThr payThr = (PayThr) objectClass.get(i);
            // rowx will be created secara berkesinambungan base on i
            Vector rowx = new Vector();
            PayPeriod periodData = new PayPeriod();
            ThrCalculationMain thrMain = new ThrCalculationMain();
            try {
                periodData = PstPayPeriod.fetchExc(payThr.getPeriodId());
                thrMain = PstThrCalculationMain.fetchExc(payThr.getCalculationMainId());
            } catch (Exception e){
                System.out.println(e);
            }
            
            no = no + 1;
            rowx.add("" + no);
            rowx.add(""+payThr.getCutOffDate());
            rowx.add(""+thrMain.getCalculationMainTitle());
            rowx.add(periodData.getPeriod());
            rowx.add("<a href=\"javascript:cmdAsk('"+payThr.getOID()+"')\">&times;&nbsp;Delete</a>");
            lstData.add(rowx);
            lstLinkData.add(String.valueOf(payThr.getOID()));

        }

        return ctrlist.draw(); // mengembalikan data-data control list

    }

    public String drawListEmp(Vector listField, Vector listRecord) {
        /* Some of array variable */
        String[][] arrField = new String[listRecord.size()+1][listField.size()]; /* for dinamic fields */
        
        /* update by Hendra Putu | 2015-11-05 */
        double countValueComp = 0;
        boolean[] showRecord = new boolean[listRecord.size()];
        
        /* 
        ===============================================================================
        * listRecord berisi baris data hasil query dan akan ditampung ke variabel array 
        ===============================================================================
        */
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
            ThrRptSetup fields = (ThrRptSetup)listField.get(i);    
            if(!fields.getRptSetupFieldName().equals("PAY_SLIP_ID")){
                if (!fields.getRptSetupFieldName().equals("STATUS_DATA")){
                    table +="<td class='title_tbl'>"+fields.getRptSetupFieldHeader()+"</td>";
                }
            }
        }
        table +="</tr>";
        
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
                        ThrRptSetup rpt = (ThrRptSetup)listField.get(i);
                        /* jika field name != PAY_SLIP_ID */
                        if(!rpt.getRptSetupFieldName().equals("PAY_SLIP_ID")){
                            if (!rpt.getRptSetupFieldName().equals("STATUS_DATA")){
                                arrField[y][i] = dyc.getField(rpt.getRptSetupFieldName());
                            } else {
                                isStatusData = true;
                            }
                        }  
                    }
                }
                
//                if (showValue == 0){
//                   showRecord[y] = true;
//                } else {
//                    if (countValueComp == 0){
//                        showRecord[y] = false;
//                    } else {
//                        showRecord[y] = true;
//                    }
//                }
                countValueComp = 0;
            }
        }
        /* END of List Record */
        
        
        /* ============== record result ==============*/
        if (listRecord != null && listRecord.size()>0){
            for(int y=0; y<listRecord.size(); y++){
                if (showRecord[y] == false){
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
                    }

                    table +="</tr>";
                    
                    table +="</tr>";
                    yStart = yEnd;
                }
            }
        }
        table +="</table>";
        
        return table;

    }
%>
<%
    int iCommand = FRMQueryString.requestCommand(request);
    int start = FRMQueryString.requestInt(request, "start");
    int prevCommand = FRMQueryString.requestInt(request, "prev_command");
    long oidPayThr = FRMQueryString.requestLong(request, "pay_thr_oid");
    long calculationMainId = FRMQueryString.requestLong(request, FrmPayThr.fieldNames[FrmPayThr.FRM_FIELD_CALCULATION_MAIN_ID]);
    long periodId = FRMQueryString.requestLong(request, FrmPayThr.fieldNames[FrmPayThr.FRM_FIELD_PERIOD_ID]);
    
    Date cutOffDate = new Date();
    long dataCalculationMainId = 0;
    long dataPeriodId = 0;
    
    int recordToGet = 5;
    String msgString = "";
    int iErrCode = FRMMessage.NONE;
    String whereClause = "";
    String orderClause = "";//
    
    CtrlPayThr ctrlPayThr = new CtrlPayThr(request);
    ControlLine ctrLine = new ControlLine();
    Vector vListPayThr = new Vector(1, 1);
    FrmPayThr frmPayThr = new FrmPayThr();
    frmPayThr = ctrlPayThr.getForm();
    PayThr payThr = new PayThr();
  
    iErrCode = ctrlPayThr.action(iCommand, oidPayThr);

    Vector listEmployee = new Vector();
    

    /*count list All Position*/
    int vectSize = PstPayThr.getCount(whereClause); //PstWarningReprimandAyat.getCount(whereClause);
    payThr = ctrlPayThr.getPayThr();
    msgString = ctrlPayThr.getMessage();
    
    if ((iCommand == Command.SAVE) && (iErrCode == FRMMessage.NONE)) {
        start = PstPayThr.findLimitStart(payThr.getOID(), recordToGet, whereClause, orderClause);
        oidPayThr = payThr.getOID();
        
        
        String where = PstPayThr.getExclusionEmployee(calculationMainId, cutOffDate,periodId);
        String sqlData = PstPayThr.sqlData(where, periodId,1);
        listEmployee = PstPayThr.listEmployee(sqlData);
        
        PayThrEmployee payThrEmployee = new PayThrEmployee();
        
        if (listEmployee.size() > 0){
            for (int i=0; i < listEmployee.size(); i++){
                Long emp = (Long) listEmployee.get(i);
                payThrEmployee.setPayThrId(oidPayThr);
                payThrEmployee.setEmployeeId(emp);
                payThrEmployee.setValue(0);
                try {
                    long oid = PstPayThrEmployee.insertExc(payThrEmployee);
                } catch (Exception exc){
                    
                }
            }
            iCommand = Command.EDIT;
        }
    }
    
    if ((iCommand == Command.FIRST || iCommand == Command.PREV)
            || (iCommand == Command.NEXT || iCommand == Command.LAST)) {
        start = ctrlPayThr.actionList(iCommand, start, vectSize, recordToGet);
    }
    /* end switch list*/

    /* get record to display */
    vListPayThr = PstPayThr.list(start, recordToGet, whereClause, orderClause);
    cutOffDate = payThr.getCutOffDate();
    /*handle condition if size of record to display = 0 and start > 0 	after delete*/
    if (vListPayThr.size() < 1 && start > 0) {
        if (vectSize - recordToGet > recordToGet) {
            start = start - recordToGet;   //go to Command.PREV
        } else {
            start = 0;
            iCommand = Command.FIRST;
            prevCommand = Command.FIRST; //go to Command.FIRST
        }
        vListPayThr = PstPayThr.list(start, recordToGet, whereClause, orderClause);
    }
    
    String whereData = " RPT_SETUP_DATA_TYPE = 0 AND RPT_SETUP_DATA_GROUP = 0";
    Vector listData = PstThrRptSetup.list(0, 0, whereData, "");
    
   
    if (iCommand == Command.ASSIGN){
        cutOffDate = FRMQueryString.requestDate(request, FrmPayThr.fieldNames[FrmPayThr.FRM_FIELD_CUT_OFF_DATE]);
        String where = PstPayThr.getExclusionEmployee(calculationMainId, cutOffDate,periodId);
        String sqlData = PstPayThr.sqlData(where, periodId,0);
        listEmployee = PstPayThr.listData(sqlData, listData);
    }
    
   
    
%>
<html>
    <head>
        <!-- #BeginEditable "doctitle" --> 
        <title>HARISMA - THR</title>
        <!-- #EndEditable --> 
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
        <!-- #BeginEditable "styles" --> 
        <link rel="stylesheet" href="../../styles/main.css" type="text/css">
        <!-- #EndEditable --> <!-- #BeginEditable "stylestab" --> 
        <link rel="stylesheet" href="../../styles/tab.css" type="text/css">
        <!-- #EndEditable --> <!-- #BeginEditable "headerscript" --> 
        
        <style type="text/css">
            #menu_utama {color: #0066CC; font-weight: bold; padding: 5px 14px; border-left: 1px solid #0066CC; font-size: 14px; background-color: #F7F7F7;}
            #mn_utama {color: #FF6600; padding: 5px 14px; border-left: 1px solid #999; font-size: 14px; background-color: #F5F5F5;}            
            #btn {
              background: #3498db;
              border: 1px solid #0066CC;
              border-radius: 3px;
              font-family: Arial;
              color: #ffffff;
              font-size: 12px;
              padding: 3px 9px 3px 9px;
            }

            #btn:hover {
              background: #3cb0fd;
              border: 1px solid #3498db;
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
            
            .tblStyle {border-collapse: collapse;font-size: 11px;}
            .tblStyle td {padding: 3px 5px; border: 1px solid #CCC;}
            .title_tbl {font-weight: bold;background-color: #DDD; color: #575757;}
            a {text-decoration: none; font-weight: bold; color: #3498db;}
            a:hover {color: red;}
            
            #titleTd {background-color: #3cb0fd; color: #FFF; padding: 3px 5px; border-left: 1px solid #0066CC;}
            #subtitle {padding: 2px 7px; font-weight: bold; background-color: #FFF; border-left: 1px solid #3498db;}
            #td1{ padding: 3px;}
            #td2{ padding: 3px 7px 3px 5px;}
            #tdrs {padding: 3px; border-top: 1px solid #333;text-align: right;}
            #tdrs1 { padding: 3px; border-top: 1px solid #373737;text-align: right;background-color: #ebffd2; color: #3d6a02; font-weight: bold;}
            #tdrs2 { padding: 3px; text-align: right;background-color: #dff6ff; color: #197a9e; font-weight: bold;}
            #tbl {border-collapse: collapse;}
            #td3 {padding: 3px; border: 1px solid #CCC;}
            #td3Header{padding: 3px; border: 1px solid #CCC; background-color: #DDD; color:#333; font-weight: bold;}
            #td4 {padding:3px 5px 3px 7px;background-color:#F5F5F5;}
            #tdsave {padding: 3px;}
            #confirm {background-color: #fad9d9;border: 1px solid #da8383; color: #bf3c3c; padding: 14px 21px;border-radius: 5px;}
        </style>
        <script type="text/javascript">
            
            function getCmd(){
                document.<%=FrmPayThr.FRM_NAME_PAY_THR%>.action = "generate_thr.jsp";
                document.<%=FrmPayThr.FRM_NAME_PAY_THR%>.submit();
            }
            
            function cmdAdd() {              
                document.<%=FrmPayThr.FRM_NAME_PAY_THR%>.command.value="<%=Command.ADD%>";               
                document.<%=FrmPayThr.FRM_NAME_PAY_THR%>.pay_thr_oid.value = "0";
                getCmd();
            }
            
            function cmdGenerate(){
                document.<%=FrmPayThr.FRM_NAME_PAY_THR%>.command.value = "<%=Command.ASSIGN%>";
                getCmd();
            }
            
            function cmdSave(){
                document.<%=FrmPayThr.FRM_NAME_PAY_THR%>.command.value = "<%=Command.SAVE%>";
                getCmd();
            }
        </script>
        <!-- #EndEditable --> 
    </head>
    <body <%=noBack%> bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
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
                            <td width="100%" colspan="3" valign="top"> 
                                <table width="100%" border="0" cellspacing="0" cellpadding="0">
                                    <tr> 
                                        <td height="20"> <div id="menu_utama"> <!-- #BeginEditable "contenttitle" -->Generate THR<!-- #EndEditable --> </div> </td>
                                    </tr>
                                    <tr>
                                        <td>&nbsp;</td>
                                    </tr>
                                    <tr>
                                        <td style="background-color:#EEE;">
                                        
                                            <table style="padding:9px; border:1px solid #00CCFF;" <%=garisContent%> width="100%" border="0" cellspacing="1" cellpadding="1" class="tablecolor">
                                                <tr>
                                                    <td>

                                                        <form name="<%=FrmPayThr.FRM_NAME_PAY_THR%>" method="POST" action="">
                                                        <input type="hidden" name="start" value="<%=start%>">
                                                        <input type="hidden" name="command" value="<%=iCommand%>">
                                                        <input type="hidden" name="pay_thr_oid" value="<%=oidPayThr%>">
                                                        <input type="hidden" name="prev_command" value="<%=prevCommand%>">
                                                        <% 
                                                        if ((iCommand == Command.ADD)||(iCommand == Command.EDIT)||(iCommand == Command.ASSIGN)){
                                                            if(iCommand == Command.ADD){
                                                                %>
                                                                <div id="mn_utama">Form Add THR Report </div>
                                                                <%
                                                            } 
                                                            if(iCommand == Command.ASSIGN){
                                                                %>
                                                                <div id="mn_utama">Form Add THR Report </div>
                                                                <%
                                                            }
                                                            if(iCommand == Command.EDIT){
                                                                %>
                                                                <div id="mn_utama">Form Edit THR Report </div>
                                                                <%
                                                            }
                                                        %>
                                                        
                                                        <table style="color:#373737;" border="0" cellspacing="0" cellpadding="0">
                                                            <tr>
                                                                <td>
                                                                    Cut Off Date
                                                                </td>
                                                                <td>&nbsp</td>
                                                                <td>
                                                                    <%=ControlDate.drawDateWithStyle(FrmPayThr.fieldNames[FrmPayThr.FRM_FIELD_CUT_OFF_DATE], cutOffDate != null ? cutOffDate : new Date(), 2, -5, "formElemen", " onkeydown=\"javascript:fnTrapKD()\"")%>
                                                                </td>
                                                            </tr>
                                                            <tr>
                                                                <td>
                                                                    Configuration
                                                                </td>
                                                                <td>&nbsp</td>
                                                                <%
                                                                
                                                                if (iCommand == Command.EDIT){
                                                                    dataCalculationMainId = payThr.getCalculationMainId();
                                                                } else {
                                                                    dataCalculationMainId = calculationMainId;
                                                                }
                                                                %>    
                                                                <td colspan="3">
                                                                    <select name="<%=FrmPayThr.fieldNames[FrmPayThr.FRM_FIELD_CALCULATION_MAIN_ID]%>">
                                                                        <option value="0">-select-</option>
                                                                    <%
                                                                    String selectedMain = "";
                                                                    Vector vListMain = PstThrCalculationMain.list(0, 0, "", ""); 
                                                                    if (vListMain != null && vListMain.size() > 0){
                                                                        for(int i=0; i<vListMain.size(); i++){
                                                                            ThrCalculationMain main = (ThrCalculationMain)vListMain.get(i);
                                                                            if (dataCalculationMainId == main.getOID()){
                                                                                selectedMain = " selected=\"selected\"";
                                                                            } else {
                                                                                selectedMain = " ";
                                                                            }
                                                                            %>
                                                                            <option value="<%=main.getOID()%>" <%=selectedMain%>><%=main.getCalculationMainTitle()%></option>
                                                                            <%
                                                                        }
                                                                    }
                                                                    %>
                                                                    </select>
                                                                </td>
                                                            </tr>
                                                            <tr>
                                                                <td>
                                                                    Payroll Period
                                                                </td>
                                                                <td>&nbsp</td>
                                                                <%
                                                                
                                                                if (iCommand == Command.EDIT){
                                                                    dataPeriodId = payThr.getPeriodId();
                                                                } else {
                                                                    dataPeriodId = periodId;
                                                                }
                                                                %>
                                                                <td colspan="3">
                                                                    <select name="<%=FrmPayThr.fieldNames[FrmPayThr.FRM_FIELD_PERIOD_ID]%>">
                                                                        <option value="0">-select-</option>
                                                                    <%
                                                                    String selectedPeriod = "";
                                                                    Vector listPeriod = PstPayPeriod.list(0, 0, "", "START_DATE DESC"); 
                                                                    if (listPeriod != null && listPeriod.size() > 0){
                                                                        for(int i=0; i<listPeriod.size(); i++){
                                                                            PayPeriod periods = (PayPeriod)listPeriod.get(i);
                                                                            if (dataPeriodId == periods.getOID()){
                                                                                selectedPeriod = " selected=\"selected\"";
                                                                            } else {
                                                                                selectedPeriod = " ";
                                                                            }
                                                                            %>
                                                                            <option value="<%=periods.getOID()%>" <%=selectedPeriod%>><%=periods.getPeriod()%></option>
                                                                            <%
                                                                        }
                                                                    }
                                                                    %>
                                                                    </select>
                                                                    <button id="btn" onclick="cmdGenerate()">Generate</button>
                                                                </td>
                                                            </tr>
                                                            <%
                                                            if (iCommand == Command.ASSIGN){
                                                                double forDistribute = 0;
                                                            %>
                                                            <tr>
                                                                <td colspan="4"><div id="subtitle">List Employee</div></td>
                                                            </tr>
                                                            <tr>
                                                                <td colspan="4">
                                                                    <table id="tbl">
                                                                        <tr>
                                                                            <%=drawListEmp(listData, listEmployee)%>
                                                                        </tr> 
                                                                    </table>
                                                                </td>
                                                            </tr>
                                                            <tr>
                                                                <td id="tdsave" colspan="4">
                                                                    <button id="btn" name="save" onclick="javascript:cmdSave()">Save Result</button>&nbsp;
                                                                </td>
                                                            </tr>
                                                            <% } %>
                                                        </table>
                                                        <%}%><!-- end if add or edit form -->
                                                        </form>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td>
                                                        <div id="mn_utama"> List Report THR </div>
                                                        <button style="margin: 5px 0px" id="btn" onclick="cmdAdd()">Add New</button>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td>&nbsp;</td>
                                                </tr>
                                                <%
                                                if (iCommand == Command.ASK){
                                                %>
                                                <tr>
                                                    <td>
                                                        <span id="confirm">
                                                            <strong>Are you sure to delete item ?</strong> &nbsp;
                                                            <button id="btn1" onclick="javascript:cmdDelete('<%=oidPayThr%>')">Yes</button>
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
                                                    <%if (vListPayThr != null && vListPayThr.size() > 0) {%>
                                                    <td>
                                                        <%=drawList(vListPayThr, oidPayThr)%>
                                                    </td>

                                                    <%} else {%>
                                                    <td>
                                                        record not found
                                                    </td>
                                                    <%}%>
                                                </tr>
                                                <tr align="left" valign="top">
                                                            <td height="8" align="left" colspan="3" class="command">
                                                                <span class="command">
                                                                    <%
                                                                                int cmd = 0;
                                                                                if ((iCommand == Command.FIRST || iCommand == Command.PREV)
                                                                                        || (iCommand == Command.NEXT || iCommand == Command.LAST)) {
                                                                                    cmd = iCommand;
                                                                                } else {
                                                                                    if (iCommand == Command.NONE || prevCommand == Command.NONE) {
                                                                                        cmd = Command.FIRST;
                                                                                    } else {
                                                                                        cmd = prevCommand;
                                                                                    }
                                                                                }
                                                                    %>
                                                                    <% ctrLine.setLocationImg(approot + "/images");
                                                                                ctrLine.initDefault();
                                                                    %>
                                                                    <%=ctrLine.drawImageListLimit(cmd, vectSize, start, recordToGet)%>
                                                                </span> </td>
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
    </body>
    <!-- #BeginEditable "script" --> 
    <script language="JavaScript">
                var oBody = document.body;
                var oSuccess = oBody.attachEvent('onkeydown',fnTrapKD);
    </script>
                
    <!-- #EndEditable --> <!-- #EndTemplate -->
</html>

