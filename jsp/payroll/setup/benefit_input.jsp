<%-- 
    Document   : benefit_input
    Created on : Feb 22, 2015, 12:01:27 AM
    Author     : Dimata 007
--%>
<%@page import="com.dimata.harisma.session.payroll.SessBenefitLevel"%>
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
        ctrlist.addHeader("Period From", "");
        ctrlist.addHeader("Period To", "");
        ctrlist.addHeader("Payroll Period","");
        ctrlist.addHeader("Benefit Config Code", "");
        ctrlist.addHeader("Delete", "");

        ctrlist.setLinkRow(1); // untuk menge-sett link di kolom pertama atau dikolom yg lain

        Vector lstData = ctrlist.getData();
        Vector lstLinkData = ctrlist.getLinkData();
        ctrlist.setLinkPrefix("javascript:cmdEdit('");
        ctrlist.setLinkSufix("')");

        ctrlist.reset(); //berfungsi untuk menginisialisasi list menjadi kosong

        int no = 0;
        for (int i = 0; i < objectClass.size(); i++) {     
            BenefitPeriod benefitPeriod = (BenefitPeriod) objectClass.get(i);
            // rowx will be created secara berkesinambungan base on i
            Vector rowx = new Vector();
            PayPeriod periodData = new PayPeriod();
            BenefitConfig codeConfig = new BenefitConfig();
            try {
                periodData = PstPayPeriod.fetchExc(benefitPeriod.getPeriodId());
                codeConfig = PstBenefitConfig.fetchExc(benefitPeriod.getBenefitConfigId());
            } catch (Exception e){
                System.out.println(e);
            }
            
            no = no + 1;
            rowx.add("" + no);
            rowx.add(""+benefitPeriod.getPeriodFrom());
            rowx.add(""+benefitPeriod.getPeriodTo());
            rowx.add(periodData.getPeriod());
            rowx.add(codeConfig.getCode());
            rowx.add("<a href=\"javascript:cmdAsk('"+benefitPeriod.getOID()+"')\">&times;&nbsp;Delete</a>");
            lstData.add(rowx);
            lstLinkData.add(String.valueOf(benefitPeriod.getOID()));

        }

        return ctrlist.draw(); // mengembalikan data-data control list

    }

    public String getNameEmployee(long oid){
        String fullName = "Not yet approved";
        if (oid != 0){
            try {
                Employee emp = PstEmployee.fetchExc(oid);
                fullName = emp.getFullName();
                return fullName;
            } catch (Exception e){
                System.out.println(e.toString());
            }
        }
        return fullName;
    }
%>
<%
    int iCommand = FRMQueryString.requestCommand(request);
    int start = FRMQueryString.requestInt(request, "start");
    int prevCommand = FRMQueryString.requestInt(request, "prev_command");
    long oidBenefitPeriod = FRMQueryString.requestLong(request, "benefit_period_id");
    long benefitConfigId = FRMQueryString.requestLong(request, FrmBenefitPeriod.fieldNames[FrmBenefitPeriod.FRM_FIELD_BENEFIT_CONFIG_ID]);
    double totalRevenue = FRMQueryString.requestDouble(request, FrmBenefitPeriod.fieldNames[FrmBenefitPeriod.FRM_FIELD_TOTAL_REVENUE]);
    long periodId = FRMQueryString.requestLong(request, FrmBenefitPeriod.fieldNames[FrmBenefitPeriod.FRM_FIELD_PERIOD_ID]);
    
    Date periodFrom = null;
    Date periodTo = null;
    Date approveDate1 = null;
    Date approveDate2 = null;
    Date createDate = null;
    
    long dataBenefitConfigId = 0;
    long dataPeriodId = 0;
    double dataTotalRevenue = 0;
    /*variable declaration*/
    int recordToGet = 5;
    String msgString = "";
    int iErrCode = FRMMessage.NONE;
    String whereClause = "";
    String orderClause = "";//

    
    CtrlBenefitPeriod ctrBenefitPeriod = new CtrlBenefitPeriod(request);
    ControlLine ctrLine = new ControlLine();
    Vector listBenefitPeriod = new Vector(1, 1);
    FrmBenefitPeriod frmBenefitPeriod = new FrmBenefitPeriod();
    frmBenefitPeriod = ctrBenefitPeriod.getForm();
    BenefitPeriod benefitPeriod = new BenefitPeriod();
  
    iErrCode = ctrBenefitPeriod.action(iCommand, oidBenefitPeriod);

    

    /*count list All Position*/
    int vectSize = PstBenefitPeriod.getCount(whereClause); //PstWarningReprimandAyat.getCount(whereClause);
    benefitPeriod = ctrBenefitPeriod.getBenefitPeriod();
    msgString = ctrBenefitPeriod.getMessage();
    
    if ((iCommand == Command.SAVE) && (iErrCode == FRMMessage.NONE)) {
        start = PstBenefitPeriod.findLimitStart(benefitPeriod.getOID(), recordToGet, whereClause, orderClause);
        oidBenefitPeriod = benefitPeriod.getOID();
    }

    if ((iCommand == Command.FIRST || iCommand == Command.PREV)
            || (iCommand == Command.NEXT || iCommand == Command.LAST)) {
        start = ctrBenefitPeriod.actionList(iCommand, start, vectSize, recordToGet);
    }
    /* end switch list*/

    /* get record to display */
    listBenefitPeriod = PstBenefitPeriod.list(start, recordToGet, whereClause, orderClause);
    periodFrom = benefitPeriod.getPeriodFrom();
    periodTo = benefitPeriod.getPeriodTo();
    approveDate1 = benefitPeriod.getApprove1Date();
    approveDate2 = benefitPeriod.getApprove2Date();
    createDate = benefitPeriod.getCreateEmpDate();
    /*handle condition if size of record to display = 0 and start > 0 	after delete*/
    if (listBenefitPeriod.size() < 1 && start > 0) {
        if (vectSize - recordToGet > recordToGet) {
            start = start - recordToGet;   //go to Command.PREV
        } else {
            start = 0;
            iCommand = Command.FIRST;
            prevCommand = Command.FIRST; //go to Command.FIRST
        }
        listBenefitPeriod = PstBenefitPeriod.list(start, recordToGet, whereClause, orderClause);
    }
%>
<html>
    <head>
        <!-- #BeginEditable "doctitle" --> 
        <title>HARISMA - Benefit Input</title>
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
                document.<%=FrmBenefitPeriod.FRM_NAME_BENEFIT_PERIOD%>.action = "benefit_input.jsp";
                document.<%=FrmBenefitPeriod.FRM_NAME_BENEFIT_PERIOD%>.submit();
            }
            function cmdAdd() {              
                document.<%=FrmBenefitPeriod.FRM_NAME_BENEFIT_PERIOD%>.command.value="<%=Command.ADD%>";               
                document.<%=FrmBenefitPeriod.FRM_NAME_BENEFIT_PERIOD%>.benefit_period_id.value = "0";
                getCmd();
            }
            function cmdBack() {
                document.<%=FrmBenefitPeriod.FRM_NAME_BENEFIT_PERIOD%>.command.value="<%=Command.BACK%>";               
                getCmd();
            }

            function cmdEdit(oid) {
                document.<%=FrmBenefitPeriod.FRM_NAME_BENEFIT_PERIOD%>.command.value = "<%=Command.EDIT%>";
                document.<%=FrmBenefitPeriod.FRM_NAME_BENEFIT_PERIOD%>.benefit_period_id.value = oid;
                getCmd();
            }

            function cmdSave() {
                document.<%=FrmBenefitPeriod.FRM_NAME_BENEFIT_PERIOD%>.command.value = "<%=Command.SAVE%>";
                getCmd();
            }
            function cmdAsk(oid){
                document.<%=FrmBenefitPeriod.FRM_NAME_BENEFIT_PERIOD%>.command.value="<%=Command.ASK%>";
                document.<%=FrmBenefitPeriod.FRM_NAME_BENEFIT_PERIOD%>.benefit_period_id.value = oid;
                getCmd();
            }
            function cmdDelete(oid){
                document.<%=FrmBenefitPeriod.FRM_NAME_BENEFIT_PERIOD%>.command.value = "<%=Command.DELETE%>";
                document.<%=FrmBenefitPeriod.FRM_NAME_BENEFIT_PERIOD%>.benefit_period_id.value = oid;
                getCmd();
            }
            function cmdListFirst(){
                document.<%=FrmBenefitPeriod.FRM_NAME_BENEFIT_PERIOD%>.command.value="<%=Command.FIRST%>";
                document.<%=FrmBenefitPeriod.FRM_NAME_BENEFIT_PERIOD%>.prev_command.value="<%=Command.FIRST%>";
                getCmd();
            }

            function cmdListPrev(){
                document.<%=FrmBenefitPeriod.FRM_NAME_BENEFIT_PERIOD%>.command.value="<%=Command.PREV%>";
                document.<%=FrmBenefitPeriod.FRM_NAME_BENEFIT_PERIOD%>.prev_command.value="<%=Command.PREV%>";
                getCmd();
            }

            function cmdListNext(){
                document.<%=FrmBenefitPeriod.FRM_NAME_BENEFIT_PERIOD%>.command.value="<%=Command.NEXT%>";
                document.<%=FrmBenefitPeriod.FRM_NAME_BENEFIT_PERIOD%>.prev_command.value="<%=Command.NEXT%>";
                getCmd();
            }

            function cmdListLast(){
                document.<%=FrmBenefitPeriod.FRM_NAME_BENEFIT_PERIOD%>.command.value="<%=Command.LAST%>";
                document.<%=FrmBenefitPeriod.FRM_NAME_BENEFIT_PERIOD%>.prev_command.value="<%=Command.LAST%>";
                getCmd();
            } 
            function cmdCalculate(){
                document.<%=FrmBenefitPeriod.FRM_NAME_BENEFIT_PERIOD%>.command.value = "<%=Command.ASSIGN%>";
                getCmd();
            }
            function cmdAskApprove(oid){
                
            }
            function cmdPrint(oid){
                window.open("<%=approot%>/servlet/com.dimata.harisma.report.payroll.BenefitEmployeePdf?oid="+oid+"");
            }
            function cmdDetail(configId, periodId){
                window.open("benefit_input_headcount_detail.jsp?config_id="+configId+"&period_id="+periodId);
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
                                        <td height="20"> <div id="menu_utama"> <!-- #BeginEditable "contenttitle" -->Benefit Input<!-- #EndEditable --> </div> </td>
                                    </tr>
                                    <tr>
                                        <td>&nbsp;</td>
                                    </tr>
                                    <tr>
                                        <td style="background-color:#EEE;">
                                        
                                            <table style="padding:9px; border:1px solid #00CCFF;" <%=garisContent%> width="100%" border="0" cellspacing="1" cellpadding="1" class="tablecolor">
                                                <tr>
                                                    <td>

                                                        <form name="<%=FrmBenefitPeriod.FRM_NAME_BENEFIT_PERIOD%>" method="POST" action="">
                                                        <input type="hidden" name="start" value="<%=start%>">
                                                        <input type="hidden" name="command" value="<%=iCommand%>">
                                                        <input type="hidden" name="benefit_period_id" value="<%=oidBenefitPeriod%>">
                                                        <input type="hidden" name="vectSize" value="<%=vectSize%>">
                                                        <input type="hidden" name="prev_command" value="<%=prevCommand%>">
                                                        <% 
                                                        if ((iCommand == Command.ADD)||(iCommand == Command.EDIT)||(iCommand == Command.ASSIGN)){
                                                            if(iCommand == Command.ADD){
                                                                %>
                                                                <div id="mn_utama">Form Add Benefit Period </div>
                                                                <%
                                                            } 
                                                            if(iCommand == Command.ASSIGN){
                                                                %>
                                                                <div id="mn_utama">Form Add Benefit Period </div>
                                                                <%
                                                            }
                                                            if(iCommand == Command.EDIT){
                                                                %>
                                                                <div id="mn_utama">Form Edit Benefit Period </div>
                                                                <%
                                                            }
                                                        %>
                                                        
                                                        <table style="color:#373737;" border="0" cellspacing="0" cellpadding="0">
                                                            <tr>
                                                                <td valign="top">
                                                                    Benefit Configuration
                                                                </td>
                                                                <td>
                                                                    <select name="<%=FrmBenefitPeriod.fieldNames[FrmBenefitPeriod.FRM_FIELD_BENEFIT_CONFIG_ID]%>">
                                                                        <option value="0">-select-</option>
                                                                    <%
                                                                    String selectedBConfig = "";
                                                                    
                                                                    if (iCommand == Command.EDIT){
                                                                        dataBenefitConfigId = benefitPeriod.getBenefitConfigId();
                                                                    } else {
                                                                        dataBenefitConfigId = benefitConfigId;
                                                                    }
                                                                    Vector listBenefitConfig = PstBenefitConfig.list(0, 0, "", "");
                                                                    if (listBenefitConfig != null && listBenefitConfig.size() > 0){
                                                                        for(int i=0; i<listBenefitConfig.size(); i++){
                                                                            BenefitConfig bConfig = (BenefitConfig)listBenefitConfig.get(i);
                                                                            
                                                                            if (dataBenefitConfigId == bConfig.getOID()){

                                                                                selectedBConfig = " selected=\"selected\"";
                                                                            } else {
                                                                                selectedBConfig = " ";
                                                                            }
                                                                            %>
                                                                            <option value="<%=bConfig.getOID()%>" <%=selectedBConfig%>><%=bConfig.getTitle()%></option>
                                                                            <%
                                                                        }
                                                                    }
                                                                    %>
                                                                    </select>
                                                                </td>
                                                                <td>
                                                                    Period From
                                                                </td>
                                                                <td>
                                                                    <%=ControlDate.drawDateWithStyle(FrmBenefitPeriod.fieldNames[FrmBenefitPeriod.FRM_FIELD_PERIOD_FROM], periodFrom != null ? periodFrom : new Date(), 2, -5, "formElemen", " onkeydown=\"javascript:fnTrapKD()\"")%>
                                                                    &nbsp;To&nbsp;
                                                                    <%=ControlDate.drawDateWithStyle(FrmBenefitPeriod.fieldNames[FrmBenefitPeriod.FRM_FIELD_PERIOD_TO], periodTo != null ? periodTo : new Date(), 2, -5, "formElemen", " onkeydown=\"javascript:fnTrapKD()\"")%>
                                                                </td>
                                                            </tr>
                                                            <tr>
                                                                <td>
                                                                    Payroll Period
                                                                </td>
                                                                <%
                                                                
                                                                if (iCommand == Command.EDIT){
                                                                    dataPeriodId = benefitPeriod.getPeriodId();
                                                                } else {
                                                                    dataPeriodId = periodId;
                                                                }
                                                                %>
                                                                <td colspan="3">
                                                                    <select name="<%=FrmBenefitPeriod.fieldNames[FrmBenefitPeriod.FRM_FIELD_PERIOD_ID]%>">
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
                                                                </td>
                                                            </tr>
                                                            <tr>
                                                                <td>
                                                                    Total Revenue
                                                                </td>
                                                                <td colspan="3">
                                                                    <%
                                                                    
                                                                    if(iCommand == Command.EDIT){
                                                                        dataTotalRevenue = benefitPeriod.getTotalRevenue();
                                                                    } else {
                                                                        dataTotalRevenue = totalRevenue;
                                                                    }
                                                                    String totalRevenueFormat = Formater.formatNumberMataUang(dataTotalRevenue, "Rp");
                                                                    %>
                                                                    
                                                                    <input type="text" title="<%=totalRevenueFormat%>" name="<%=FrmBenefitPeriod.fieldNames[FrmBenefitPeriod.FRM_FIELD_TOTAL_REVENUE]%>" value="<%=dataTotalRevenue%>" />
                                                                    <button id="btn" onclick="cmdCalculate()">Calculate</button>
                                                                </td>
                                                            </tr>
                                                            <%
                                                            if (iCommand == Command.ASSIGN || iCommand == Command.EDIT){
                                                                double forDistribute = 0;
                                                            %>
                                                            <tr>
                                                                <td colspan="4"><div id="subtitle">Deduction</div></td>
                                                            </tr>
                                                            <tr>
                                                                <td colspan="4">
                                                                    <table>
                                                                        <%
                                                                        String whereDeduction = "BENEFIT_CONFIG_ID = " + dataBenefitConfigId;
                                                                        String orderDeduction = "DEDUCTION_INDEX ASC";
                                                                        String cssRs = "tdrs";
                                                                        int nomor = 1;
                                                                        double deductionResult = 0;
                                                                        Vector listDeduction = PstBenefitConfigDeduction.list(0, 0, whereDeduction, orderDeduction);
                                                                        double[] arrDeduction = new double[listDeduction.size()+1];
                                                                        arrDeduction[0] = dataTotalRevenue;
                                                                        for(int i=0; i<listDeduction.size();i++){
                                                                            BenefitConfigDeduction deduction = (BenefitConfigDeduction)listDeduction.get(i);
                                                                            if (deduction.getDeductionReference() == 0){
                                                                                arrDeduction[deduction.getDeductionIndex()] = dataTotalRevenue - ((deduction.getDeductionPercent() * dataTotalRevenue)/100);
                                                                                deductionResult = (deduction.getDeductionPercent() * dataTotalRevenue)/100;
                                                                            } else {
                                                                                BenefitConfigDeduction ded = PstBenefitConfigDeduction.fetchExc(deduction.getDeductionReference());
                                                                                arrDeduction[deduction.getDeductionIndex()] = arrDeduction[deduction.getDeductionIndex()-1]-((deduction.getDeductionPercent() * arrDeduction[ded.getDeductionIndex()])/100);
                                                                                deductionResult = (deduction.getDeductionPercent() * arrDeduction[ded.getDeductionIndex()])/100;
                                                                            }
                                                                            forDistribute = arrDeduction[deduction.getDeductionIndex()];
                                                                            %>
                                                                            <tr>
                                                                                <td id="td1"><%=nomor%>)</td>
                                                                                <td id="td1"><%=deduction.getDeductionPercent()%>% &times; <%=deduction.getDeductionDescription()%>&nbsp;</td>
                                                                                <td id="td1"><%=Formater.formatNumberMataUang(deductionResult, "Rp")%></td>
                                                                            </tr>
                                                                            <%
                                                                            if(i == listDeduction.size()-1){
                                                                                cssRs = "tdrs1";
                                                                            }
                                                                            %>
                                                                            <tr>
                                                                                <td id="<%=cssRs%>" colspan="3"><%=Formater.formatNumberMataUang(arrDeduction[deduction.getDeductionIndex()],"Rp")%></td>
                                                                            </tr>
                                                                            <%
                                                                            nomor++;
                                                                        }
                                                                        %>
                                                                        
                                                                    </table>
                                                                </td>
                                                            </tr>
                                                            
                                                            <tr>
                                                                <td colspan="4"><div id="subtitle">Service Charge Distribution</div></td>
                                                            </tr>
                                                            <tr>
                                                                <td colspan="4">
                                                                    <table>
                                                                        <%
                                                                        BenefitConfig distribution = PstBenefitConfig.fetchExc(dataBenefitConfigId);
                                                                        int totalEmployee = PstBenefitPeriod.getTotalEmployee(dataBenefitConfigId, dataPeriodId);
                                                                        /* data approve */
                                                                        long bcApproveId1 = distribution.getApprove1EmpId();
                                                                        long bcApproveId2 = distribution.getApprove2EmpId();
                                                                        int totalPoint = 0;
                                                                        Vector listPoint = PstBenefitPeriod.getTotalPoint(dataBenefitConfigId, dataPeriodId);
                                                                        if(listPoint != null && listPoint.size()>0){
                                                                            for(int i=0; i<listPoint.size(); i++){
                                                                                SessBenefitLevel pLevel = (SessBenefitLevel)listPoint.get(i);
                                                                                totalPoint = totalPoint + pLevel.getSumPoint();
                                                                            }
                                                                        }
                                                                        double distributionResult1 = ((distribution.getDistributionPercent1() * forDistribute)/100)/totalEmployee;
                                                                        double distributionResult2 = ((distribution.getDistributionPercent2() * forDistribute)/100)/totalPoint;
                                                                        %>
                                                                  
                                                                        <tr>
                                                                            <td id="td2">1)</td>
                                                                            <td colspan="3" id="td2"><%=distribution.getDistributionPart1()%></td>
                                                                        </tr>
                                                                        <tr>
                                                                            <td id="td2">&nbsp;</td>
                                                                            <td id="td2"><%=distribution.getDistributionPercent1()%>% &times; <%=Formater.formatNumberMataUang(forDistribute,"Rp")%></td>
                                                                            <td id="td2">
                                                                                <%=totalEmployee%>
                                                                                <input type="hidden" name="<%=FrmBenefitPeriod.fieldNames[FrmBenefitPeriod.FRM_FIELD_PART_1_TOTAL_DIVIDER]%>" value="<%=totalEmployee%>" />
                                                                            </td>
                                                                            <td id="tdrs2">
                                                                                <%=Formater.formatNumberMataUang(distributionResult1,"Rp")%>
                                                                                <input type="hidden" name="<%=FrmBenefitPeriod.fieldNames[FrmBenefitPeriod.FRM_FIELD_PART_1_VALUE]%>" value="<%=distributionResult1%>" />
                                                                            </td>
                                                                            <td id ="td2">
                                                                                <button id="btn" onclick="cmdDetail('<%=dataBenefitConfigId%>','<%=dataPeriodId%>')">View Detail</button>
                                                                            </td>
                                                                        </tr>
                                                                        <tr>
                                                                            <td id="td2">2)</td>
                                                                            <td colspan="3" id="td2"><%=distribution.getDistributionPart2()%></td>
                                                                        </tr>
                                                                        <tr>
                                                                            <td>&nbsp;</td>
                                                                            <td id="td2"><%=distribution.getDistributionPercent2()%>% &times; <%=Formater.formatNumberMataUang(forDistribute,"Rp")%></td>
                                                                            <td id="td2">
                                                                                <%=totalPoint%>
                                                                                <input type="hidden" name="<%=FrmBenefitPeriod.fieldNames[FrmBenefitPeriod.FRM_FIELD_PART_2_TOTAL_DIVIDER]%>" value="<%=totalPoint%>" />
                                                                            </td>
                                                                            <td id="tdrs2">
                                                                                <%=Formater.formatNumberMataUang(distributionResult2,"Rp")%>
                                                                                <input type="hidden" name="<%=FrmBenefitPeriod.fieldNames[FrmBenefitPeriod.FRM_FIELD_PART_2_VALUE]%>" value="<%=distributionResult2%>" />
                                                                            </td>
                                                                        </tr>
                                                                    </table>
                                                                </td>
                                                            </tr>
                                                            <tr>
                                                                <td colspan="4">
                                                                    <table id="tbl">
                                                                        <tr>
                                                                            <td id="td3Header">Level</td>
                                                                            <td id="td3Header">Level Point</td>
                                                                            <td id="td3Header">Flat Service Charge</td>
                                                                            <td id="td3Header">Point</td>
                                                                            <td id="td3Header">Service By Point</td>
                                                                            <td id="td3Header">Total Service Charge</td>
                                                                            <td id="td3Header">Head Count</td>
                                                                        </tr>
                                                                        <%
                                                                            double serviceByPoint = 0;
                                                                            double totalServiceCharge = 0;
                                                                            for(int i=0; i<listPoint.size();i++){
                                                                                SessBenefitLevel pointLevel = (SessBenefitLevel)listPoint.get(i);
                                                                                serviceByPoint = pointLevel.getLevelPoint() * distributionResult2;
                                                                                totalServiceCharge = distributionResult1 + serviceByPoint;
                                                                        %>
                                                                        <tr>
                                                                            <td id="td3"><%=pointLevel.getLevel()%></td>
                                                                            <td id="td3"><%=pointLevel.getLevelPoint()%></td>
                                                                            <td id="td3"><%=Formater.formatNumberMataUang(distributionResult1,"Rp")%></td>
                                                                            <td id="td3"><%=Formater.formatNumberMataUang(distributionResult2,"Rp")%></td>
                                                                            <td id="td3"><%=Formater.formatNumberMataUang(serviceByPoint,"Rp")%></td>
                                                                            <td id="td3"><%=Formater.formatNumberMataUang(totalServiceCharge,"Rp")%></td>
                                                                            <td id="td3"><%=pointLevel.getCountPoint()%></td>
                                                                        </tr>
                                                                        <%
                                                                            }
                                                                        %>
                                                                    </table>
                                                                </td>
                                                            </tr>
                                                            <tr>
                                                                <td valign="top" colspan="4" style="padding:9px 0px;">
                                                                    <table style="border-collapse: collapse;border: 1px solid #DDD;">
                                                                        <tr>
                                                                            <td id="td4">Status Document</td>
                                                                            <td id="td4">
                                                                                <select name="<%=FrmBenefitPeriod.fieldNames[FrmBenefitPeriod.FRM_FIELD_DOC_STATUS]%>">
                                                                                    <% for(int i=0; i<PstBenefitPeriod.docStatusVal.length; i++){%>
                                                                                    <option value="<%=PstBenefitPeriod.docStatusVal[i]%>"><%=PstBenefitPeriod.docStatusKey[i]%></option>
                                                                                    <%}%>
                                                                                </select>
                                                                            </td>
                                                                            <td id="td4">Created by</td>
                                                                            <td id="td4">
                                                                                <select name="<%=FrmBenefitPeriod.fieldNames[FrmBenefitPeriod.FRM_FIELD_CREATE_EMP_ID]%>">
                                                                                    <option>-select-</option>
                                                                                    <%
                                                                                    String selectedCreatedEmp = "";
                                                                                    Vector listEmpApp1 = PstEmployee.list(0, 0, "", "FULL_NAME"); 
                                                                                    if (listEmpApp1 != null && listEmpApp1.size() > 0){
                                                                                        for(int i=0; i<listEmpApp1.size(); i++){
                                                                                            Employee employies = (Employee)listEmpApp1.get(i);
                                                                                            if (benefitPeriod.getCreateEmpId() == employies.getOID()){
                                                                                                selectedCreatedEmp = " selected=\"selected\"";
                                                                                            } else {
                                                                                                selectedCreatedEmp = " ";
                                                                                            }
                                                                                            %>
                                                                                            <option value="<%=employies.getOID()%>" <%=selectedCreatedEmp%>><%=employies.getFullName()%></option>
                                                                                            <%
                                                                                        }
                                                                                    }
                                                                                    %>
                                                                                </select>
                                                                            </td>
                                                                        </tr>
                                                                        <tr>
                                                                            <td id="td4">Approve 1</td>
                                                                            <td id="td4">
                                                                                
                                                                                <select name="<%=FrmBenefitPeriod.fieldNames[FrmBenefitPeriod.FRM_FIELD_APPROVE_1_EMP_ID]%>">
                                                                                    <option>-select-</option>
                                                                                    <%
                                                                                    String selectedApp1Emp = "";
                                                                                    if (listEmpApp1 != null && listEmpApp1.size() > 0){
                                                                                        for(int i=0; i<listEmpApp1.size(); i++){
                                                                                            Employee employies = (Employee)listEmpApp1.get(i);
                                                                                            if (benefitPeriod.getApprove1EmpId() == employies.getOID()){
                                                                                                selectedApp1Emp = " selected=\"selected\"";
                                                                                            } else {
                                                                                                selectedApp1Emp = " ";
                                                                                            }
                                                                                            %>
                                                                                            <option value="<%=employies.getOID()%>" <%=selectedApp1Emp%>><%=employies.getFullName()%></option>
                                                                                            <%
                                                                                        }
                                                                                    }
                                                                                    %>
                                                                                </select>
                                                                            </td>
                                                                            <td id="td4">Approve 2</td>
                                                                            <td id="td4">
                                                                                <select name="<%=FrmBenefitPeriod.fieldNames[FrmBenefitPeriod.FRM_FIELD_APPROVE_2_EMP_ID]%>">
                                                                                    <option>-select-</option>
                                                                                    <%
                                                                                    String selectedApp2Emp = "";
                                                                                    if (listEmpApp1 != null && listEmpApp1.size() > 0){
                                                                                        for(int i=0; i<listEmpApp1.size(); i++){
                                                                                            Employee employies = (Employee)listEmpApp1.get(i);
                                                                                            if (benefitPeriod.getApprove2EmpId() == employies.getOID()){
                                                                                                selectedApp2Emp = " selected=\"selected\"";
                                                                                            } else {
                                                                                                selectedApp2Emp = " ";
                                                                                            }
                                                                                            %>
                                                                                            <option value="<%=employies.getOID()%>" <%=selectedApp2Emp%>><%=employies.getFullName()%></option>
                                                                                            <%
                                                                                        }
                                                                                    }
                                                                                    %>
                                                                                </select>
                                                                            </td>
                                                                        </tr>
                                                                    </table>
                                                                </td>
                                                            </tr>
                                                            <tr>
                                                                <td id="tdsave" colspan="4">
                                                                    <button id="btn" name="save" onclick="javascript:cmdSave()">Save Result</button>&nbsp;
                                                                    <button id="btn" name="print" onclick="javascript:cmdPrint('<%=benefitPeriod.getOID()%>')">Print PDF</button>
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
                                                        <div id="mn_utama"> List Benefit Period </div>
                                                        <button style="margin: 5px 0px" id="btn" onclick="cmdAdd()">Add Benefit Period</button>
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
                                                            <button id="btn1" onclick="javascript:cmdDelete('<%=oidBenefitPeriod%>')">Yes</button>
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
                                                    <%if (listBenefitPeriod != null && listBenefitPeriod.size() > 0) {%>
                                                    <td>
                                                        <%=drawList(listBenefitPeriod, oidBenefitPeriod)%>
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
