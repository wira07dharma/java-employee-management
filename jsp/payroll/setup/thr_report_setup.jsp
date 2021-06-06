
<%-- 
    Document   : thr_report_setup
    Created on : 25-Aug-2017, 08:57:29
    Author     : Gunadi
--%>

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


<%@ include file = "../../main/javainit.jsp" %>
<% int appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_PAYROLL, AppObjInfo.G2_PAYROLL_SETUP, AppObjInfo.OBJ_PAYROLL_SETUP_COMPONENT);%>
<%@ include file = "../../main/checkuser.jsp" %>
<%!
    public String drawList(Vector objectClass, long oidBenefitConfig) {

        ControlList ctrlist = new ControlList();

        ctrlist.setAreaWidth("100%");
        ctrlist.setListStyle("tblStyle");
        ctrlist.setTitleStyle("title_tbl");
        ctrlist.setCellStyle("listgensell");
        ctrlist.setHeaderStyle("title_tbl");
        ctrlist.setCellSpacing("0");
        ctrlist.addHeader("No", "");
        ctrlist.addHeader("Index", "");
        ctrlist.addHeader("Header Name", "");
        ctrlist.addHeader("Action", "");

        ctrlist.setLinkRow(1); // untuk menge-sett link di kolom pertama atau dikolom yg lain

        Vector lstData = ctrlist.getData();
        Vector lstLinkData = ctrlist.getLinkData();
        
        ctrlist.reset(); //berfungsi untuk menginisialisasi list menjadi kosong

        int no = 0;
        for (int i = 0; i < objectClass.size(); i++) {     
            ThrRptSetup thrRptSetup = (ThrRptSetup) objectClass.get(i);
            // rowx will be created secara berkesinambungan base on i
            Vector rowx = new Vector();

            no = no + 1;
            rowx.add("" + no);
            rowx.add(""+thrRptSetup.getRptSetupShowIdx());
            rowx.add(""+thrRptSetup.getRptSetupFieldHeader());
            rowx.add("<a href=\"javascript:cmdAsk('"+thrRptSetup.getOID()+"')\">&times;&nbsp;Delete</a>");
            lstData.add(rowx);
            lstLinkData.add(String.valueOf(thrRptSetup.getOID()));

        }

        return ctrlist.draw(); // mengembalikan data-data control list

    }
%>
<%  
    int iCommand = FRMQueryString.requestCommand(request);
    int start = FRMQueryString.requestInt(request, "start");
    int prevCommand = FRMQueryString.requestInt(request, "prev_command");
    long oidRptSetup = FRMQueryString.requestLong(request, "rpt_setup_id");
    int fieldIdx = FRMQueryString.requestInt(request, "field_name");
    int tblIdx = FRMQueryString.requestInt(request, "idx");
    int dataType = FRMQueryString.requestInt(request, "dataType");
    long oidComp = FRMQueryString.requestLong(request, "field_component");
    String fieldLos = FRMQueryString.requestString(request, "field_los");
    long oidField = FRMQueryString.requestLong(request, "oid_field");
    String inpFieldHeader = FRMQueryString.requestString(request, "inp_field_header");
    int inpShowIdx = FRMQueryString.requestInt(request, "inp_show_idx");
    

   
    /*variable declaration*/
    int recordToGet = 10;
    String msgString = "";
    int iErrCode = FRMMessage.NONE;
    String whereClause = "";
    String orderClause = "";//


    CtrlThrRptSetup ctrlThrRptSetup = new CtrlThrRptSetup(request);
    ControlLine ctrLine = new ControlLine();
    Vector listThrRptSetup = new Vector(1, 1);
    FrmThrRptSetup frmThrRptSetup = new FrmThrRptSetup();
    frmThrRptSetup = ctrlThrRptSetup.getForm();
    ThrRptSetup thrRptSetup = new ThrRptSetup();
    
    if (iCommand == Command.SAVE){
        int idx = PstThrRptSetup.getIdx();
        if (dataType == 0){
            thrRptSetup.setRptSetupShowIdx(idx);
            thrRptSetup.setRptSetupTableGroup(PstCustomRptConfig.realTableList[tblIdx]);
            thrRptSetup.setRptSetupTableName(PstCustomRptConfig.tableSystem[tblIdx][fieldIdx]);
            thrRptSetup.setRptSetupFieldName(PstCustomRptConfig.showFieldSystem[tblIdx][fieldIdx]);
            thrRptSetup.setRptSetupFieldHeader(PstCustomRptConfig.showFieldList[tblIdx][fieldIdx]);
            thrRptSetup.setRptSetupFieldType(PstCustomRptConfig.fieldTypeSystem[tblIdx][fieldIdx]);
            thrRptSetup.setRptSetupDataType(dataType);
            thrRptSetup.setRptSetupDataGroup(0);
            PstThrRptSetup.insertExc(thrRptSetup);
        }
        if (dataType == 2){
            PayComponent dataComp1 = PstPayComponent.fetchExc(oidComp);
            thrRptSetup.setRptSetupShowIdx(idx);
            thrRptSetup.setRptSetupTableGroup(PstPaySlip.TBL_PAY_SLIP);
            thrRptSetup.setRptSetupTableName(PstPaySlipComp.TBL_PAY_SLIP_COMP);
            thrRptSetup.setRptSetupFieldName(dataComp1.getCompCode());
            thrRptSetup.setRptSetupFieldHeader(dataComp1.getCompName());
            thrRptSetup.setRptSetupFieldType(1);
            thrRptSetup.setRptSetupDataType(dataType);
            thrRptSetup.setRptSetupDataGroup(0);
            PstThrRptSetup.insertExc(thrRptSetup);
        }
        if (dataType == 3){
            thrRptSetup.setRptSetupShowIdx(idx);
            thrRptSetup.setRptSetupTableGroup(PstEmployee.TBL_HR_EMPLOYEE);
            thrRptSetup.setRptSetupTableName(PstEmployee.TBL_HR_EMPLOYEE);
            thrRptSetup.setRptSetupFieldName("COMMENCING_DATE");
            thrRptSetup.setRptSetupFieldHeader(fieldLos);
            thrRptSetup.setRptSetupFieldType(1);
            thrRptSetup.setRptSetupDataType(dataType);
            thrRptSetup.setRptSetupDataGroup(0);
            PstThrRptSetup.insertExc(thrRptSetup);
        }
    }
    
    if (iCommand == Command.UPDATE){
            ThrRptSetup dataUp = PstThrRptSetup.fetchExc(oidField);
            thrRptSetup.setOID(oidField);
            thrRptSetup.setRptSetupTableGroup(dataUp.getRptSetupTableGroup());
            thrRptSetup.setRptSetupDataGroup(dataUp.getRptSetupDataGroup());
            thrRptSetup.setRptSetupDataType(dataUp.getRptSetupDataType());
            thrRptSetup.setRptSetupFieldName(dataUp.getRptSetupFieldName());
            thrRptSetup.setRptSetupTableName(dataUp.getRptSetupTableName());
            thrRptSetup.setRptSetupFieldType(dataUp.getRptSetupFieldType());
            thrRptSetup.setRptSetupFieldHeader(inpFieldHeader);
            thrRptSetup.setRptSetupShowIdx(inpShowIdx);
            PstThrRptSetup.updateExc(thrRptSetup);
            inpFieldHeader = "";
            inpShowIdx = 0;
    }

%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <!-- #BeginTemplate "/Templates/main.dwt" --> 
    <head>
        <!-- #BeginEditable "doctitle" --> 
        <title>HARISMA - Benefit Config</title>
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
    
            #tbl0 {padding: 7px;}
            
            #input {padding: 3px; border: 1px solid #CCC; margin: 0px;}
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
            a {text-decoration: none; font-weight: bold; color: #0066CC;}
            a:hover {color: red;}
            
            #titleTd {background-color: #a0d5fb; color: #FFF; padding: 3px 5px; border-left: 1px solid #0066CC;}
            #listPos {background-color: #FFF; border: 1px solid #CCC; padding: 3px;cursor: pointer;margin: 3px 0px;} 
            #position_input { margin: 3px; padding: 5px 7px; color: #FF6600;}
            #payroll_input { margin: 3px; padding: 5px 7px; color: #FF6600;}
            #division_input { margin: 3px; padding: 5px 7px; color: #FF6600;}
            #confirm {background-color: #fad9d9;border: 1px solid #da8383; color: #bf3c3c; padding: 14px 21px;border-radius: 5px;}
            #caption {
                font-size: 12px;
                color: #474747;
                font-weight: bold;
                padding: 2px 0px 3px 0px;
            }
            #divinput {
                margin-bottom: 5px;
                padding-bottom: 5px;
            }
            
        </style>
        <script type="text/javascript">
            function cmdAddPersonal(){
                document.frmpersonal.command.value="<%=Command.SAVE%>";
                document.frmpersonal.action="thr_report_setup.jsp";
                document.frmpersonal.submit();
            }
            
            function cmdEditPersonal(oid){
                document.frmtblpersonal.command.value="<%=Command.EDIT%>";
                document.frmtblpersonal.oid_field.value=oid;
                document.frmtblpersonal.action="thr_report_setup.jsp";
                document.frmtblpersonal.submit();
            }
            
            function cmdUpdatePersonal(oid){
                document.frmtblpersonal.command.value="<%=Command.UPDATE%>";
                document.frmtblpersonal.oid_field.value=oid;
                document.frmtblpersonal.action="thr_report_setup.jsp";
                document.frmtblpersonal.submit();
            }
            
            function cmdAddPayment(){
                document.frmpayment.command.value="<%=Command.SAVE%>";
                document.frmpayment.action="thr_report_setup.jsp";
                document.frmpayment.submit();
            }
            
            function cmdEditPayment(oid){
                document.frmtblpayment.command.value="<%=Command.EDIT%>";
                document.frmtblpayment.oid_field.value=oid;
                document.frmtblpayment.action="thr_report_setup.jsp";
                document.frmtblpayment.submit();
            }
            
            function cmdUpdatePayment(oid){
                document.frmtblpayment.command.value="<%=Command.UPDATE%>";
                document.frmtblpayment.oid_field.value=oid;
                document.frmtblpayment.action="thr_report_setup.jsp";
                document.frmtblpayment.submit();
            }
            
            function cmdAddPaymentType(){
                document.frmpaymentype.command.value="<%=Command.SAVE%>";
                document.frmpaymentype.action="thr_report_setup.jsp";
                document.frmpaymentype.submit();
            }
            
            function cmdEditPaymentType(oid){
                document.frmtblpaymenttype.command.value="<%=Command.EDIT%>";
                document.frmtblpaymenttype.oid_field.value=oid;
                document.frmtblpaymenttype.action="thr_report_setup.jsp";
                document.frmtblpaymenttype.submit();
            }
            
            function cmdUpdatePaymentType(oid){
                document.frmtblpaymenttype.command.value="<%=Command.UPDATE%>";
                document.frmtblpaymenttype.oid_field.value=oid;
                document.frmtblpaymenttype.action="thr_report_setup.jsp";
                document.frmtblpaymenttype.submit();
            }
            
            function cmdAddComponent(){
                document.frmcomponent.command.value="<%=Command.SAVE%>";
                document.frmcomponent.action="thr_report_setup.jsp";
                document.frmcomponent.submit();
            }
            
            function cmdEditComponent(oid){
                document.frmtblcomp.command.value="<%=Command.EDIT%>";
                document.frmtblcomp.oid_field.value=oid;
                document.frmtblcomp.action="thr_report_setup.jsp";
                document.frmtblcomp.submit();
            }
            
            function cmdUpdateComponent(oid){
                document.frmtblcomp.command.value="<%=Command.UPDATE%>";
                document.frmtblcomp.oid_field.value=oid;
                document.frmtblcomp.action="thr_report_setup.jsp";
                document.frmtblcomp.submit();
            }
            
            function cmdAddLos(){
                document.frmlos.command.value="<%=Command.SAVE%>";
                document.frmlos.action="thr_report_setup.jsp";
                document.frmlos.submit();
            }

            function cmdEditLOS(oid){
                document.frmtbllos.command.value="<%=Command.EDIT%>";
                document.frmtbllos.oid_field.value=oid;
                document.frmtbllos.action="thr_report_setup.jsp";
                document.frmtbllos.submit();
            }
            
            function cmdUpdateLOS(oid){
                document.frmtbllos.command.value="<%=Command.UPDATE%>";
                document.frmtbllos.oid_field.value=oid;
                document.frmtbllos.action="thr_report_setup.jsp";
                document.frmtbllos.submit();
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
                                        <td height="20"> <div id="menu_utama"> <!-- #BeginEditable "contenttitle" -->Benefit Configuration<!-- #EndEditable --> </div> </td>
                                    </tr>
                                    <tr>
                                        <td>&nbsp;</td>
                                    </tr>
                                    <tr>
                                        <td style="background-color:#EEE;">
                                        
                                            <table style="padding:9px; border:1px solid #00CCFF;" <%=garisContent%> width="100%" border="0" cellspacing="1" cellpadding="1" class="tablecolor">
                                                <tr>
                                                    <td>
                                                        <div id="mn_utama"> List Report Configuration </div>
                                                        
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td>
                                                        <form name="frmpersonal" method="POST" action="">
                                                            <input type="hidden" name="command">
                                                            <input type="hidden" name="idx" value="0">
                                                            <input type="hidden" name="dataType" value="0">
                                                            <div id="caption">Personal Data</div>
                                                            <div id="divinput">
                                                                <select id="personalData" name="field_name">
                                                                    <%
                                                                        for (int i=0; i < PstThrRptSetup.showFieldList[0].length; i++){
                                                                    %>
                                                                        <option value="<%=i%>"><%=PstCustomRptConfig.showFieldList[0][i]%></option>
                                                                    <%
                                                                        }
                                                                    %>
                                                                </select>
                                                                <button style="margin: 5px 0px" id="btn" onclick="cmdAddPersonal()">Add</button>
                                                            </div>
                                                        </form>
                                                    </td>
                                                    <td>
                                                        <form name="frmpayment" method="POST" action="">
                                                            <input type="hidden" name="command">
                                                            <input type="hidden" name="idx" value="1">
                                                            <input type="hidden" name="dataType" value="0">
                                                            <div id="caption">Payment</div>
                                                            <div id="divinput">
                                                                <select id="payment" name="field_name">
                                                                    <%
                                                                        for (int i=0; i < PstThrRptSetup.showFieldList[1].length; i++){
                                                                    %>
                                                                        <option value="<%=i%>"><%=PstCustomRptConfig.showFieldList[1][i]%></option>
                                                                    <%
                                                                        }
                                                                    %>
                                                                </select>
                                                                <button style="margin: 5px 0px" id="btn" onclick="cmdAddPayment()">Add</button>
                                                            </div>
                                                        </form>
                                                    </td>
                                                    <td>
                                                        <form name="frmpaymentype" method="POST" action="">
                                                            <input type="hidden" name="command">
                                                            <input type="hidden" name="idx" value="2">
                                                            <input type="hidden" name="dataType" value="0">
                                                            <div id="caption">Payment Type</div>
                                                            <div id="divinput">
                                                                <select id="paymentType" name="field_name">
                                                                    <%
                                                                        for (int i=0; i < PstThrRptSetup.showFieldList[2].length; i++){
                                                                    %>
                                                                        <option value="<%=i%>"><%=PstCustomRptConfig.showFieldList[2][i]%></option>
                                                                    <%
                                                                        }
                                                                    %>
                                                                </select>
                                                                <button style="margin: 5px 0px" id="btn" onclick="cmdAddPaymentType()">Add</button>
                                                            </div>
                                                        </form>
                                                    </td>
                                                    <td>
                                                        <form name="frmcomponent" method="POST" action="">
                                                            <input type="hidden" name="command">
                                                            <input type="hidden" name="idx" value="2">
                                                            <input type="hidden" name="dataType" value="2">
                                                            <div id="caption">Component</div>
                                                            <div id="divinput">
                                                                <select id="comp" name="field_component">
                                                                    <%
                                                                            Vector listComponent = PstPayComponent.list(0, 0, "", "");
                                                                            if (listComponent != null && listComponent.size()>0){
                                                                                for(int i=0; i<listComponent.size(); i++){
                                                                                    PayComponent payComp = (PayComponent)listComponent.get(i);

                                                                                    %>
                                                                                    <option value="<%=payComp.getOID()%>"><%=payComp.getCompName()%></option>
                                                                                    <%
                                                                                }
                                                                            }
                                                                            %>
                                                                </select>
                                                                <button style="margin: 5px 0px" id="btn" onclick="cmdAddComponent()">Add</button>
                                                            </div>
                                                        </form>
                                                    </td>
                                                    <td>
                                                        <form name="frmlos" method="POST" action="">
                                                            <input type="hidden" name="command">
                                                            <input type="hidden" name="idx" value="2">
                                                            <input type="hidden" name="dataType" value="3">
                                                            <div id="caption">Length of Service</div>
                                                            <div id="divinput">
                                                                <select id="los" name="field_los">
                                                                    <option value="Day">Day</option>
                                                                    <option value="Month">Month</option>
                                                                    <option value="Year">Year</option>
                                                                </select>
                                                                <button style="margin: 5px 0px" id="btn" onclick="cmdAddLos()">Add</button>
                                                            </div>
                                                        </form>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td valign="top">
                                                        <form name="frmtblpersonal" method="POST" action=""> 
                                                            <input type="hidden" name="oid_field">
                                                            <input type="hidden" name="command">
                                                            <table border ="1" cellspacing="1" cellpadding="1">

                                                                <%
                                                                String whereData = " RPT_SETUP_DATA_TYPE = 0 AND RPT_SETUP_DATA_GROUP = 0 AND RPT_SETUP_TABLE_GROUP = '"+PstThrRptSetup.realTableList[0]+"'";
                                                                String tdWarna = "";
                                                                Vector listField = PstThrRptSetup.list(0, 0, whereData, "");
                                                                if (listField !=null && listField.size()>0){
                                                                    for(int j=0; j<listField.size(); j++){
                                                                        ThrRptSetup dataField = (ThrRptSetup)listField.get(j);
                                                                        if (iCommand == Command.EDIT && oidField == dataField.getOID()){
                                                                            %>
                                                                            <tr>
                                                                                <td id="td1"><button id="btn4" onclick="javascript:cmdUpdatePersonal('<%=dataField.getOID()%>')">s</button></td>
                                                                                <td id="td1"><input type="text" name="inp_show_idx" value="<%=dataField.getRptSetupShowIdx()%>" size="1" /></td>
                                                                                <td id="td1" style="background-color: <%=tdWarna%>;"><input type="text" name="inp_field_header" value="<%=dataField.getRptSetupFieldHeader()%>" /></td>
                                                                                <td id="td1"><button id="btn3" onclick="javascript:cmdAskPersonal('<%=dataField.getOID()%>')">&times;</button></td>
                                                                            </tr>
                                                                            <%
                                                                        } else {
                                                                        %>
                                                                        <tr>
                                                                            <td id="td1"><button id="btn4" onclick="javascript:cmdEditPersonal('<%=dataField.getOID()%>')">e</button></td>
                                                                            <td id="td1"><%=dataField.getRptSetupShowIdx()%></td>
                                                                            <td id="td1" style="background-color: <%=tdWarna%>;"><%=dataField.getRptSetupFieldHeader()%></td>
                                                                            <td id="td1"><button id="btn3" onclick="javascript:cmdAskPersonal('<%=dataField.getOID()%>')">&times;</button></td>
                                                                        </tr>
                                                                        <%
                                                                        }
                                                                        tdWarna = "";
                                                                    }
                                                                }
                                                                %>
                                                            </table>
                                                        </form>
                                                    </td>
                                                    <td valign="top">
                                                        <form name="frmtblpayment" method="POST" action=""> 
                                                            <input type="hidden" name="oid_field">
                                                            <input type="hidden" name="command">
                                                            <table border ="1" cellspacing="1" cellpadding="1">
                                                                <%
                                                                String wherePayment = " RPT_SETUP_DATA_TYPE = 0 AND RPT_SETUP_DATA_GROUP = 0 AND RPT_SETUP_TABLE_GROUP = '"+PstThrRptSetup.realTableList[1]+"'";
                                                                String tdWarnaPayment = "";
                                                                Vector listFieldPayment = PstThrRptSetup.list(0, 0, wherePayment, "");
                                                                if (listFieldPayment !=null && listFieldPayment.size()>0){
                                                                    for(int j=0; j<listFieldPayment.size(); j++){
                                                                        ThrRptSetup dataField = (ThrRptSetup)listFieldPayment.get(j);
                                                                        if (iCommand == Command.EDIT && oidField == dataField.getOID()){
                                                                            %>
                                                                            <tr>
                                                                                <td id="td1"><button id="btn4" onclick="javascript:cmdUpdatePayment('<%=dataField.getOID()%>')">s</button></td>
                                                                                <td id="td1"><input type="text" name="inp_show_idx" value="<%=dataField.getRptSetupShowIdx()%>" size="1" /></td>
                                                                                <td id="td1" style="background-color: <%=tdWarnaPayment%>;"><input type="text" name="inp_field_header" value="<%=dataField.getRptSetupFieldHeader()%>" /></td>
                                                                                <td id="td1"><button id="btn3" onclick="javascript:cmdAskPayment('<%=dataField.getOID()%>')">&times;</button></td>
                                                                            </tr>
                                                                            <%
                                                                        } else {
                                                                        %>
                                                                        <tr>
                                                                            <td id="td1"><button id="btn4" onclick="javascript:cmdEditPayment('<%=dataField.getOID()%>')">e</button></td>
                                                                            <td id="td1"><%=dataField.getRptSetupShowIdx()%></td>
                                                                            <td id="td1" style="background-color: <%=tdWarnaPayment%>;"><%=dataField.getRptSetupFieldHeader()%></td>
                                                                            <td id="td1"><button id="btn3" onclick="javascript:cmdAskPayment('<%=dataField.getOID()%>')">&times;</button></td>
                                                                        </tr>
                                                                        <%
                                                                        }
                                                                        tdWarnaPayment = "";
                                                                    }
                                                                }
                                                                %>
                                                            </table>
                                                        </form>
                                                    </td>
                                                    <td valign="top">
                                                        <form name="frmtblpaymenttype" method="POST" action=""> 
                                                            <input type="hidden" name="oid_field">
                                                            <input type="hidden" name="command">
                                                            <table border ="1" cellspacing="1" cellpadding="1">
                                                                <%
                                                                String wherePaymentType = " RPT_SETUP_DATA_TYPE = 0 AND RPT_SETUP_DATA_GROUP = 0 AND RPT_SETUP_TABLE_GROUP = '"+PstThrRptSetup.realTableList[2]+"'";
                                                                String tdWarnaPaymentType = "";
                                                                Vector listFieldPaymentType = PstThrRptSetup.list(0, 0, wherePaymentType, "");
                                                                if (listFieldPaymentType !=null && listFieldPaymentType.size()>0){
                                                                    for(int j=0; j<listFieldPaymentType.size(); j++){
                                                                        ThrRptSetup dataField = (ThrRptSetup)listFieldPaymentType.get(j);
                                                                        if (iCommand == Command.EDIT && oidField == dataField.getOID()){
                                                                            %>
                                                                            <tr>
                                                                                <td id="td1"><button id="btn4" onclick="javascript:cmdUpdatePaymentType('<%=dataField.getOID()%>')">s</button></td>
                                                                                <td id="td1"><input type="text" name="inp_show_idx" value="<%=dataField.getRptSetupShowIdx()%>" size="1" /></td>
                                                                                <td id="td1" style="background-color: <%=tdWarnaPaymentType%>;"><input type="text" name="inp_field_header" value="<%=dataField.getRptSetupFieldHeader()%>" /></td>
                                                                                <td id="td1"><button id="btn3" onclick="javascript:cmdAskPaymentType('<%=dataField.getOID()%>')">&times;</button></td>
                                                                            </tr>
                                                                            <%
                                                                        } else {
                                                                        %>
                                                                        <tr>
                                                                            <td id="td1"><button id="btn4" onclick="javascript:cmdEditPaymentType('<%=dataField.getOID()%>')">e</button></td>
                                                                            <td id="td1"><%=dataField.getRptSetupShowIdx()%></td>
                                                                            <td id="td1" style="background-color: <%=tdWarnaPaymentType%>;"><%=dataField.getRptSetupFieldHeader()%></td>
                                                                            <td id="td1"><button id="btn3" onclick="javascript:cmdAskPaymentType('<%=dataField.getOID()%>')">&times;</button></td>
                                                                        </tr>
                                                                        <%
                                                                        }
                                                                        tdWarnaPaymentType = "";
                                                                    }
                                                                }
                                                                %>
                                                            </table>
                                                        </form>
                                                    </td>
                                                    <td valign="top">
                                                        <form name="frmtblcomp" method="POST" action=""> 
                                                            <input type="hidden" name="oid_field">
                                                            <input type="hidden" name="command">
                                                            <table border ="1" cellspacing="1" cellpadding="1">
                                                            <%
                                                                String whereComp = " RPT_SETUP_DATA_TYPE = 2 AND RPT_SETUP_DATA_GROUP = 0";
                                                                String tdColor = "";              
                                                                Vector listComp = PstThrRptSetup.list(0, 0, whereComp, "");
                                                                if (listComp !=null && listComp.size()>0){
                                                                    for(int j=0; j<listComp.size(); j++){
                                                                        ThrRptSetup dataField = (ThrRptSetup)listComp.get(j);

                                                                        if (iCommand == Command.EDIT && oidField == dataField.getOID()){
                                                                            %>
                                                                            <tr>
                                                                                <td id="td1"><button id="btn4" onclick="javascript:cmdUpdateComponent('<%=dataField.getOID()%>')">s</button></td>
                                                                                <td id="td1"><input type="text" name="inp_show_idx" value="<%=dataField.getRptSetupShowIdx()%>" size="1" /></td>
                                                                                <td id="td1" style="background-color: <%=tdColor%>;"><input type="text" name="inp_field_header" value="<%=dataField.getRptSetupFieldHeader()%>" /></td>
                                                                                <td id="td1"><button id="btn3" onclick="javascript:cmdAskComponent('<%=dataField.getOID()%>')">&times;</button></td>
                                                                            </tr>
                                                                            <%
                                                                        } else {
                                                                        %>
                                                                        <tr>
                                                                            <td id="td1"><button id="btn4" onclick="javascript:cmdEditComponent('<%=dataField.getOID()%>')">e</button></td>
                                                                            <td id="td1"><%=dataField.getRptSetupShowIdx()%></td>
                                                                            <td id="td1" style="background-color: <%=tdColor%>;"><%=dataField.getRptSetupFieldHeader()%></td>
                                                                            <td id="td1"><button id="btn3" onclick="javascript:cmdAskComponent('<%=dataField.getOID()%>')">&times;</button></td>
                                                                        </tr>
                                                                        <%
                                                                        }
                                                                        tdColor = "";
                                                                    }
                                                                }
                                                                %>
                                                            </table>
                                                        </form>
                                                    </td>
                                                    <td valign="top">
                                                        <form name="frmtbllos" method="POST" action=""> 
                                                            <input type="hidden" name="oid_field">
                                                            <input type="hidden" name="command">
                                                            <table border ="1" cellspacing="1" cellpadding="1">
                                                                <%
                                                                String whereView = " RPT_SETUP_DATA_TYPE = 3 AND RPT_SETUP_DATA_GROUP = 0 ";
                                                                String tdWarna1 = "";
                                                                Vector listField1 = PstThrRptSetup.list(0, 0, whereView, "");
                                                                if (listField1 !=null && listField1.size()>0){
                                                                    for(int j=0; j<listField1.size(); j++){
                                                                        ThrRptSetup dataField = (ThrRptSetup)listField1.get(j);
                                                                        if (iCommand == Command.EDIT && oidField == dataField.getOID()){
                                                                            %>
                                                                            <tr>
                                                                                <td id="td1"><button id="btn4" onclick="javascript:cmdUpdateLOS('<%=dataField.getOID()%>')">s</button></td>
                                                                                <td id="td1"><input type="text" name="inp_show_idx" value="<%=dataField.getRptSetupShowIdx()%>" size="1" /></td>
                                                                                <td id="td1" style="background-color: <%=tdWarna1%>;"><input type="text" name="inp_field_header" value="<%=dataField.getRptSetupFieldHeader()%>" /></td>
                                                                                <td id="td1"><button id="btn3" onclick="javascript:cmdAskLOS('<%=dataField.getOID()%>')">&times;</button></td>
                                                                            </tr>
                                                                            <%
                                                                        } else {
                                                                        %>
                                                                        <tr>
                                                                            <td id="td1"><button id="btn4" onclick="javascript:cmdEditLOS('<%=dataField.getOID()%>')">e</button></td>
                                                                            <td id="td1"><%=dataField.getRptSetupShowIdx()%></td>
                                                                            <td id="td1" style="background-color: <%=tdWarna1%>;"><%=dataField.getRptSetupFieldHeader()%></td>
                                                                            <td id="td1"><button id="btn3" onclick="javascript:cmdAskLOS('<%=dataField.getOID()%>')">&times;</button></td>
                                                                        </tr>
                                                                        <%
                                                                        }
                                                                        tdWarna1 = "";
                                                                    }
                                                                }
                                                                %>
                                                            </table>
                                                        </form>
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
                                                            <button id="btn1" onclick="javascript:cmdDelete('<%=oidRptSetup%>')">Yes</button>
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
    <!-- #BeginEditable "script" --> <script language="JavaScript">
                var oBody = document.body;
                var oSuccess = oBody.attachEvent('onkeydown',fnTrapKD);
    </script>
                
    <!-- #EndEditable --> <!-- #EndTemplate -->
</html>