<%-- 
    Document   : src_bpjs
    Created on : 26-Feb-2016, 04:00:08
    Author     : GUSWIK
--%>
<%@page import="com.dimata.harisma.entity.arap.PstArApMain"%>
<%@page import="com.dimata.harisma.entity.arap.ArApMain"%>
<%@page import="com.dimata.harisma.entity.arap.ArApItem"%>
<%@page import="com.dimata.harisma.entity.arap.PstArApItem"%>
<%@page import="org.apache.poi.hssf.usermodel.HSSFCell"%>
<%@page import="org.apache.poi.hssf.usermodel.HSSFRow"%>
<%@page import="org.apache.poi.hssf.usermodel.HSSFSheet"%>
<%@page import="org.apache.poi.hssf.usermodel.HSSFWorkbook"%>
<%@page import="org.apache.poi.poifs.filesystem.POIFSFileSystem"%>
<%@page import="java.io.ByteArrayInputStream"%>
<%@page import="java.io.FileOutputStream"%>
<%@page import="com.dimata.util.blob.TextLoader"%>
<%@page import="com.dimata.qdep.form.FRMQueryString"%>
<%@ page language="java" %>
<%@ page import = "java.util.*" %>
<%@ page import = "java.text.*" %>
<!-- package wihita -->
<%@ page import = "com.dimata.util.*" %>
<!-- package qdep -->
<%@ page import = "com.dimata.gui.jsp.*" %>
<%@ page import = "com.dimata.qdep.form.*" %>

<%@ page import = "java.util.Date" %>

<!--package harisma -->
<%@ page import = "com.dimata.harisma.entity.search.*" %>
<%@ page import = "com.dimata.harisma.form.search.*" %>
<%@ page import = "com.dimata.harisma.entity.admin.*" %>
<%@ page import = "com.dimata.harisma.entity.payroll.*" %>
<%@ page import = "com.dimata.harisma.entity.employee.*" %>
<%@ page import = "com.dimata.harisma.form.payroll.*" %>
<%@ page import = "com.dimata.harisma.entity.admin.*" %>
<%@ page import = "com.dimata.harisma.session.employee.*" %>
<%@ page import = "com.dimata.printman.*" %>
<%@ page import = "com.dimata.harisma.printout.*" %>
<%@ page import = "com.dimata.harisma.printout.PayPrintText" %>

<%@ include file = "../../main/javainit.jsp" %>
<% int appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_PAYROLL, AppObjInfo.G2_PAYROLL_PROCESS, AppObjInfo.OBJ_PAYROLL_PROCESS_PRINT);%>
<%@ include file = "../../main/checkuser.jsp" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%
    CtrlPaySlipComp ctrlPaySlipComp = new CtrlPaySlipComp(request);
    int prevCommand = FRMQueryString.requestInt(request, "prev_command");
    int iCommand = FRMQueryString.requestCommand(request);
    int start = FRMQueryString.requestInt(request, "start");

    long oidCompany = FRMQueryString.requestLong(request, "oidCompany");
    long oidDivision = FRMQueryString.requestLong(request, "division");
    long oidDepartment = FRMQueryString.requestLong(request, "department");
    long oidSection = FRMQueryString.requestLong(request, "section");
    long periodeId = FRMQueryString.requestLong(request, "periodId");

%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="../../styles/main.css" type="text/css">
        <link rel="stylesheet" href="../../styles/tab.css" type="text/css">
        <link href="<%=approot%>/stylesheets/superTables.css" rel="Stylesheet" type="text/css" /> 
        <title>Report</title>
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
            #tdTotal {background-color: #fad9d9;}
            #query {
                padding: 7px 9px; color: #f7f7f7; background-color: #797979; 
                border:1px solid #575757; border-radius: 5px; 
                margin-bottom: 5px; font-size: 12px;
                font-family: Courier New,Courier,Lucida Sans Typewriter,Lucida Typewriter,monospace;
            }
            #searchForm {
                width: 100%;
                padding: 21px; color: #797979; background-color: #F7F7F7;
                border:1px solid #DDD;
                font-size: 12px;
            }

            #resultForm {
                width: 100%;
                padding: 21px; color: #474747; background-color: #ffffff;
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
        <SCRIPT language=JavaScript>        


            function cmdSubmit(){
                document.frm_printing.target="";    
                document.frm_printing.command.value="<%=String.valueOf(Command.POST)%>";
                document.frm_printing.action="uploadArapMain.jsp?command=<%=String.valueOf(Command.POST)%>";        
                document.frm_printing.submit();
            }

 
            
        
        </SCRIPT>
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
                                        <td height="20"> <div id="menu_utama"><span id="menu_teks">Report</span> <!-- #EndEditable --> </div> </td>
                                    </tr>
                                    <tr>
                                        <td>&nbsp;</td>
                                    </tr>
                                    <tr>
                                        <td style="background-color:#EEE;" valign="top">

                                            <table style="padding:9px; border:1px solid #00CCFF;" <%=garisContent%> width="100%" border="0" cellspacing="1" cellpadding="1" class="tablecolor">

                                                <tr>
                                                    <td valign="top">
                                                        <form name="frm_printing" method="post" action="" enctype="multipart/form-data">
                                                            <input type="hidden" name="command" value="<%=iCommand%>">
                                                            <input type="hidden" name="paySlipPeriod" value="1">

                                                            <table >

                                                                <tr>
                                                                    <td colspan="2">
                                                                        <div id="searchForm">
                                                                            <h2>Upload Form</h2>

                                                                            <table>
                                                                                <tr>
                                                                                    <td width="20%" style="background: #EEE">  <input type ="file" name ="file"></td>
                                                                                    <td width="3%">&nbsp;</td>
                                                                                    <td width="10%" style="background: #EEE"><button id="btn" onclick="cmdSubmit(0)">cmdSubmit</button></td>
                                                                                    <td width="3%">&nbsp;</td>
                                                                                </tr>
                                                                                <tr>
                                                                                    <td width="3%">&nbsp;</td>
                                                                                </tr> 
                                                                                <%

                                                                                    Vector vDataPerEmp = new Vector();


                                                                                    try {
                                                                                        if (iCommand == Command.POST) {

                                                                                            TextLoader uploader = new TextLoader();
                                                                                            FileOutputStream fOut = null;
                                                                                            ByteArrayInputStream inStream = null;
                                                                                            uploader.uploadText(config, request, response);
                                                                                            Object obj = uploader.getTextFile("file");
                                                                                            byte byteText[] = null;
                                                                                            byteText = (byte[]) obj;
                                                                                            inStream = new ByteArrayInputStream(byteText);
                                                                                            Excel tp = new Excel();

                                                                                            EmployeeUploadDeduction employeeUploadDeduction = new EmployeeUploadDeduction();

                                                                                            Vector headerX = new Vector();
                                                                                            boolean schDate = false;
                                                                                            int col_name = -1;
                                                                                            int row_name = -1;
                                                                                            POIFSFileSystem fs = new POIFSFileSystem(inStream);
                                                                                            HSSFWorkbook wb = new HSSFWorkbook(fs);
                                                                                            HSSFSheet sheet = (HSSFSheet) wb.getSheetAt(0);
                                                                                            int rows = sheet.getPhysicalNumberOfRows();
                                                                                            int nilTrueFalse = 1;
                                                                                            int NUM_CELL = 0;
                                                                                            int COL_EMPNUMB = 0;
                                                                                            int ROW_EMPLOYEE = 1;

                                                                                            for (int r = 0; r < rows; r++) {
                                                                                                Employee employee = null;
                                                                                                try {
                                                                                                    employeeUploadDeduction = new EmployeeUploadDeduction();
                                                                                                    HSSFRow row = sheet.getRow(r);
                                                                                                    int cells = 0;
                                                                                                    //if number of cell is static
                                                                                                    cells = row.getPhysicalNumberOfCells();
                                                                                                    // ambil jumlah kolom yang sebenarnya
                                                                                                    NUM_CELL = cells;

                                                                                                    String cellColor = "";
                                                                                                    String employeeNum = "";


                                                                                                    for (int c = 0; c <= cells; c++) {
                                                                                                        cellColor = "#CCCCCC";
                                                                                                        HSSFCell cell = row.getCell((short) c);
                                                                                                        String value = null;
                                                                                                        int dtInt = 0;
                                                                                                        if (cell != null) {
                                                                                                            switch (cell.getCellType()) {
                                                                                                                case HSSFCell.CELL_TYPE_FORMULA:
                                                                                                                    value = String.valueOf(cell.getCellFormula());
                                                                                                                    ;
                                                                                                                    break;
                                                                                                                case HSSFCell.CELL_TYPE_NUMERIC:
                                                                                                                    value = String.valueOf(cell.getNumericCellValue());
                                                                                                                    if (value.endsWith(".0")) {
                                                                                                                        value = value.substring(0, value.length() - 2);
                                                                                                                    }
                                                                                                                    ;
                                                                                                                    if (value != null && value.length() > 0) {
                                                                                                                        dtInt = Integer.parseInt(value);
                                                                                                                    }
                                                                                                                    break;
                                                                                                                case HSSFCell.CELL_TYPE_STRING:
                                                                                                                    value = String.valueOf(cell.getStringCellValue());

                                                                                                                    break;
                                                                                                                default:
                                                                                                                    value = String.valueOf(cell.getStringCellValue() != null ? cell.getStringCellValue() : "");
                                                                                                                    ;
                                                                                                            }
                                                                                                            try { // search for the row containts the first employee data
                                                                                                                String spString = ">>";


                                                                                                                if (value != null && r >= ROW_EMPLOYEE && c >= 0 && c <= 7 && value != null && (!value.toLowerCase().contains(spString.toLowerCase())) && (!value.equals(""))) {

                                                                                                                    if (c == 0) {
                                                                                                                        //mencari namanya
                                                                                                                        employeeNum = value;
                                                                                                                    }
                                                                                                                    //mencari employee berdasarkan employee_number
                                                                                                                    if (employee == null && c == COL_EMPNUMB) {
                                                                                                                        employee = PstEmployee.getEmployeeByNum(value);
                                                                                                                        if (employee != null) {
                                                                                                                            employeeUploadDeduction.setEmpId(employee.getOID());
                                                                                                                            employeeUploadDeduction.setEmpName(employee.getFullName());
                                                                                                                            employeeUploadDeduction.setEmpNumb(employee.getEmployeeNum());
                                                                                                                        } else {
                                                                                                                            employee = PstEmployee.getEmployeeByFullName(employee.getFullName());
                                                                                                                            if (employee != null) {
                                                                                                                                employeeUploadDeduction.setEmpId(employee.getOID());
                                                                                                                                employeeUploadDeduction.setEmpName(employee.getFullName());
                                                                                                                                employeeUploadDeduction.setEmpNumb(employee.getEmployeeNum());
                                                                                                                            } else {
                                                                                                                                employeeUploadDeduction.setEmpId(0);
                                                                                                                                employeeUploadDeduction.setEmpName(null);
                                                                                                                                employeeUploadDeduction.setEmpNumb(null);
                                                                                                                                employee = new Employee();
                                                                                                                            }
                                                                                                                        }
                                                                                                                    }
                                                                                                                    if (employee != null && c == 1) {
                                                                                                                        employeeUploadDeduction.setJumlahhutang(Double.parseDouble(value));
                                                                                                                    }
                                                                                                                    if (employee != null && c == 2) {
                                                                                                                        employeeUploadDeduction.setBanyakBayar(Double.parseDouble(value));
                                                                                                                    }
                                                                                                                    if (employee != null && c == 3) {
                                                                                                                        Period period = new Period();
                                                                                                                        //baru sampai sini
                                                                                                                        try {
                                                                                                                            if (value != null) {
                                                                                                                                period = PstPeriod.getPeriodByName(value);
                                                                                                                                employeeUploadDeduction.setPeriode(period.getOID());
                                                                                                                            }
                                                                                                                        } catch (Exception e) {
                                                                                                                        }

                                                                                                                    }
                                                                                                                    if (employee != null && c == 4) {
                                                                                                                        //baru sampai sini
                                                                                                                        try {
                                                                                                                            if (value != null) {
                                                                                                                                employeeUploadDeduction.setCompCode(value);
                                                                                                                            }
                                                                                                                        } catch (Exception e) {
                                                                                                                        }
                                                                                                                    }
                                                                                                                    if (employee != null && c == 5) {
                                                                                                                        //baru sampai sini
                                                                                                                        try {
                                                                                                                            if (value != null) {
                                                                                                                                employeeUploadDeduction.setDeskripsi(value);
                                                                                                                            }
                                                                                                                        } catch (Exception e) {
                                                                                                                        }
                                                                                                                    }
                                                                                                                }
                                                                                                            } catch (Exception exc) {
                                                                                                                System.out.println("r=" + r + " c=" + c + " " + exc);
                                                                                                            }
                                                                                                        }

                                                                                                    }
                                                                                                } catch (Exception exc) {
                                                                                                    System.out.println("Exception" + exc);
                                                                                                }
                                                                                                //prosess menampung schedule input karyawan
                                                                                                if (employeeUploadDeduction != null && employeeUploadDeduction.getEmpNumb() != null) {
                                                                                                    vDataPerEmp.add(employeeUploadDeduction);
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                        if (vDataPerEmp.size() > 0) {
                                                                                            for (int x = 0; x < vDataPerEmp.size(); x++) {
                                                                                                EmployeeUploadDeduction employeeUploadDeduction = (EmployeeUploadDeduction) vDataPerEmp.get(x);

                                                                                                ArApMain arap = new ArApMain();
                                                                                                long compId = 0;
                                                                                                try {
                                                                                                   compId = PstPayComponent.getIdName(employeeUploadDeduction.getCompCode());
                                                                                                }catch(Exception e){
                                                                                                }

                                                                                                
                                                                                                
                                                                                                //arap.setVoucherNo(this.getString(FRM_VOUCHER_NO));
                                                                                                arap.setVoucherDate(new Date());
                                                                                                //arap.setContactId(this.getLong(FRM_CONTACT_ID));
                                                                                                arap.setNumberOfPayment((int) employeeUploadDeduction.getBanyakBayar());
                                                                                                //arap.setIdPerkiraanLawan(this.getLong(FRM_ID_PERKIRAAN_LAWAN));
                                                                                                //arap.setIdPerkiraan(this.getLong(FRM_ID_PERKIRAAN));
                                                                                                //arap.setIdCurrency(this.getLong(FRM_ID_CURRENCY));
                                                                                                //arap.setRate(this.getDouble(FRM_RATE));
                                                                                                //arap.setAmount(this.getDouble(FRM_AMOUNT));
                                                                                                //arap.setNotaNo(this.getString(FRM_NOTA_NO));
                                                                                                //arap.setNotaDate(this.getDate(FRM_NOTA_DATE));
                                                                                                arap.setDescription(employeeUploadDeduction.getDeskripsi());
                                                                                                //arap.setArApMainStatus(this.getInt(FRM_ARAP_MAIN_STATUS));
                                                                                                //arap.setArApType(this.getInt(FRM_ARAP_TYPE));
                                                                                                //arap.setArApDocStatus(this.getInt(FRM_ARAP_DOC_STATUS));
                                                                                                //arap.setCounter(this.getInt(FRM_COUNTER));
                                                                                                arap.setComponentDeductionId(compId);
                                                                                                arap.setEmployeeId(employeeUploadDeduction.getEmpId());
                                                                                                arap.setEntryDate(new Date());
                                                                                                //arap.setPeriodeEvery(this.getInt(FRM_PERIOD_EVERY));
                                                                                                //arap.setPeriodeEveryDMY(this.getInt(FRM_PERIOD_EVERY_DMY));
                                                                                                //arap.setPayment_amount_plan(this.getDouble(FRM_PAYMENT_AMOUNT_PLAN));
                                                                                                arap.setPeriodType(1);
                                                                                                arap.setPeriodId(employeeUploadDeduction.getPeriode());
                                                                                                Period periodX = new Period();
                                                                                                try{
                                                                                                    periodX = PstPeriod.fetchExc(employeeUploadDeduction.getPeriode());
                                                                                                }catch(Exception e ){
                                                                                                    System.out.print(e);
                                                                                                }
                                                                                                
                                                                                                arap.setStartofpaymentdate(periodX.getStartDate());


                                                                                                if (arap.getOID() == 0) {
                                                                                                    synchronized (this) {
                                                                                                        try {
                                                                                                            arap = PstArApMain.createOrderNomor(arap);
                                                                                                            long oid = PstArApMain.insertExc(arap);

                                                                                                            //save item by priska 20150418


                                                                                                            double angsuran = 0;
                                                                                                            int jumlahitem = 0;
                                                                                                            if (arap.getNumberOfPayment() > 0) {
                                                                                                                angsuran = (arap.getAmount() / arap.getNumberOfPayment());
                                                                                                                jumlahitem = arap.getNumberOfPayment();
                                                                                                            } else {
                                                                                                                angsuran = (arap.getPayment_amount_plan());
                                                                                                                jumlahitem = (int) (arap.getAmount() / arap.getPayment_amount_plan());
                                                                                                            }

                                                                                                            double mod = arap.getAmount() % arap.getPayment_amount_plan(); //sisa hasil bagi
                                                                                                            double div = arap.getAmount() / arap.getPayment_amount_plan(); //pembagi

                                                                                                            Date startPaymentDate = null;
                                                                                                            Date endPaymentDate = null;

                                                                                                            if (arap.getPeriodType() == 1) {
                                                                                                                Period period = new Period();
                                                                                                                period = PstPeriod.fetchExc(arap.getPeriodId());

                                                                                                                Calendar calendar = Calendar.getInstance();
                                                                                                                calendar.setTime(period.getStartDate());
                                                                                                                startPaymentDate = calendar.getTime();

                                                                                                                calendar.setTime(period.getEndDate());
                                                                                                                endPaymentDate = calendar.getTime();
                                                                                                            } else {
                                                                                                                startPaymentDate = arap.getStartofpaymentdate();
                                                                                                            }

                                                                                                            for (int i = 0; i < jumlahitem; i++) {
                                                                                                                ArApItem arApItem = new ArApItem();
                                                                                                                arApItem.setArApMainId(oid);
                                                                                                                arApItem.setAngsuran(angsuran);

                                                                                                                arApItem.setLeftToPay(angsuran);
                                                                                                                PayComponent payComponent = new PayComponent();
                                                                                                                try {
                                                                                                                    payComponent = PstPayComponent.fetchExc(arap.getComponentDeductionId());
                                                                                                                } catch (Exception e) {
                                                                                                                    System.out.printf("paycomponent null");
                                                                                                                }

                                                                                                                Date cloneStartDate = null;
                                                                                                                if (arap.getPeriodType() == 1) {
                                                                                                                    arApItem.setDueDate(endPaymentDate);
                                                                                                                    arApItem.setDescription(payComponent.getCompName() + " " + String.valueOf(startPaymentDate).toString() + " - " + String.valueOf(endPaymentDate).toString());
                                                                                                                    arApItem.setArApItemStatus(0);
                                                                                                                    //arApItem.setDouble(FLD_LEFT_TO_PAY, aktiva.getLeftToPay());
                                                                                                                    arApItem.setCurrencyId(arap.getIdCurrency());
                                                                                                                    arApItem.setRate(arap.getRate());
                                                                                                                    //arApItem.setLong(FLD_SELLING_AKTIVA_ID, aktiva.getSellingAktivaId());
                                                                                                                    //arApItem.setLong(FLD_RECEIVE_AKTIVA_ID, aktiva.getReceiveAktivaId());

                                                                                                                    // start get next date
                                                                                                                    //SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                                                                                                                    Calendar calendar = Calendar.getInstance();
                                                                                                                    calendar.setTime(startPaymentDate);
                                                                                                                    calendar.add(Calendar.MONTH, 1);
                                                                                                                    Date nextStartDate = calendar.getTime();

                                                                                                                    startPaymentDate = nextStartDate;

                                                                                                                    calendar.setTime(startPaymentDate);
                                                                                                                    calendar.add(Calendar.MONTH, 1);
                                                                                                                    calendar.set(Calendar.DAY_OF_MONTH, 1);
                                                                                                                    calendar.add(Calendar.DATE, -1);
                                                                                                                    Date nextEndDate = calendar.getTime();

                                                                                                                    endPaymentDate = nextEndDate;
                                                                                                                    // end get next date

                                                                                                                    long saveItem = PstArApItem.insertExc(arApItem);
                                                                                                                } else {
                                                                                                                    cloneStartDate = (Date) startPaymentDate.clone();
                                                                                                                    int every = arap.getPeriodeEvery();
                                                                                                                    if (arap.getPeriodeEveryDMY() == 0) {
                                                                                                                        cloneStartDate.setDate(cloneStartDate.getDate() + every);
                                                                                                                    } else if (arap.getPeriodeEveryDMY() == 1) {
                                                            ;                                                            cloneStartDate.setDate(cloneStartDate.getDate() + (7 * every));
                                                                                                                    } else if (arap.getPeriodeEveryDMY() == 2) {
                                                                                                                        cloneStartDate.setDate(cloneStartDate.getDate());
                                                                                                                        cloneStartDate.setMonth(cloneStartDate.getMonth() + every);
                                                                                                                        cloneStartDate.setYear(cloneStartDate.getYear());
                                                                                                                    } else if (arap.getPeriodeEveryDMY() == 3) {
                                                                                                                        cloneStartDate.setYear(cloneStartDate.getYear() + every);
                                                                                                                    }
                                                                                                                    cloneStartDate.setDate(cloneStartDate.getDate() - 1);

                                                                                                                    arApItem.setDueDate(cloneStartDate);
                                                                                                                    arApItem.setDescription(payComponent.getCompName() + " " + String.valueOf(startPaymentDate).toString() + " - " + String.valueOf(cloneStartDate).toString());
                                                                                                                    arApItem.setArApItemStatus(0);
                                                                                                                    //arApItem.setDouble(FLD_LEFT_TO_PAY, aktiva.getLeftToPay());
                                                                                                                    arApItem.setCurrencyId(arap.getIdCurrency());
                                                                                                                    arApItem.setRate(arap.getRate());
                                                                                                                    //arApItem.setLong(FLD_SELLING_AKTIVA_ID, aktiva.getSellingAktivaId());
                                                                                                                    //arApItem.setLong(FLD_RECEIVE_AKTIVA_ID, aktiva.getReceiveAktivaId());


                                                                                                                    long saveItem = PstArApItem.insertExc(arApItem);
                                                                                                                    //int every = arap.getPeriodeEvery();
                                                                                                                    if (arap.getPeriodeEveryDMY() == 0) {
                                                                                                                        startPaymentDate.setDate(startPaymentDate.getDate() + every);
                                                                                                                    } else if (arap.getPeriodeEveryDMY() == 1) {
                                                                                                                        startPaymentDate.setDate(startPaymentDate.getDate() + (7 * every));
                                                                                                                    } else if (arap.getPeriodeEveryDMY() == 2) {
                                                                                                                        startPaymentDate.setDate(startPaymentDate.getDate());
                                                                                                                        startPaymentDate.setMonth(startPaymentDate.getMonth() + every);
                                                                                                                        startPaymentDate.setYear(startPaymentDate.getYear());
                                                                                                                    } else if (arap.getPeriodeEveryDMY() == 3) {
                                                                                                                        startPaymentDate.setYear(startPaymentDate.getYear() + every);
                                                                                                                    }
                                                                                                                }
                                                                                                            }

                                                                                                            if (mod > 0 && arap.getNumberOfPayment() < 1) {
                                                                                                                ArApItem arApItem = new ArApItem();
                                                                                                                arApItem.setArApMainId(oid);
                                                                                                                arApItem.setAngsuran(mod);

                                                                                                                arApItem.setLeftToPay(mod);
                                                                                                                PayComponent payComponent = new PayComponent();
                                                                                                                try {
                                                                                                                    payComponent = PstPayComponent.fetchExc(arap.getComponentDeductionId());
                                                                                                                } catch (Exception e) {
                                                                                                                    System.out.printf("paycomponent null");
                                                                                                                }

                                                                                                                Date cloneStartDate = (Date) startPaymentDate.clone();
                                                                                                                int every = arap.getPeriodeEvery();
                                                                                                                if (arap.getPeriodeEveryDMY() == 0) {
                                                                                                                    cloneStartDate.setDate(cloneStartDate.getDate() + every);
                                                                                                                } else if (arap.getPeriodeEveryDMY() == 1) {
                                                                                                                    cloneStartDate.setDate(cloneStartDate.getDate() + (7 * every));
                                                                                                                } else if (arap.getPeriodeEveryDMY() == 2) {
                                                                                                                    cloneStartDate.setDate(cloneStartDate.getDate());
                                                                                                                    cloneStartDate.setMonth(cloneStartDate.getMonth() + every);
                                                                                                                    cloneStartDate.setYear(cloneStartDate.getYear());
                                                                                                                } else if (arap.getPeriodeEveryDMY() == 3) {
                                                                                                                    cloneStartDate.setYear(cloneStartDate.getYear() + every);
                                                                                                                }
                                                                                                                cloneStartDate.setDate(cloneStartDate.getDate() - 1);
                                                                                                                arApItem.setDueDate(cloneStartDate);

                                                                                                                arApItem.setDescription(payComponent.getCompName() + " " + String.valueOf(startPaymentDate).toString() + " - " + String.valueOf(cloneStartDate).toString());
                                                                                                                arApItem.setArApItemStatus(0);
                                                                                                                //arApItem.setDouble(FLD_LEFT_TO_PAY, aktiva.getLeftToPay());
                                                                                                                arApItem.setCurrencyId(arap.getIdCurrency());
                                                                                                                arApItem.setRate(arap.getRate());
                                                                                                                //arApItem.setLong(FLD_SELLING_AKTIVA_ID, aktiva.getSellingAktivaId());
                                                                                                                //arApItem.setLong(FLD_RECEIVE_AKTIVA_ID, aktiva.getReceiveAktivaId());

                                                                                                                long saveItem = PstArApItem.insertExc(arApItem);
                                                                                                            }


                                                                                                        } catch (Exception exc) {
                                                                                                        }
                                                                                                    }
                                                                                                }
                                                                                            }


                                                                                        }

                                                                                    } catch (Exception e) {
                                                                                    }


                                                                                %>
                                                                                <%//=vDataPerEmp.size()%>

                                                                            </table>        
                                                                        </div>
                                                                    </td>
                                                                </tr>

                                                            </table>

                                                        </form>
                                                    </td>
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
    </body>
</html>
