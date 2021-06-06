
<%@page import="com.dimata.harisma.form.masterdata.FrmScheduleSymbol"%>
<%@ page language="java" %>
<!-- package java -->
<%@ page import ="java.util.*,                  
         com.dimata.util.Command,
         com.dimata.util.Formater,				  
         com.dimata.gui.jsp.*,
         com.dimata.gui.jsp.ControlCombo,
         com.dimata.qdep.form.FRMQueryString,
         com.dimata.gui.jsp.ControlList,				  
         com.dimata.gui.jsp.ControlLine ,
         com.dimata.harisma.entity.employee.Employee,
         com.dimata.harisma.entity.masterdata.LeavePeriod,				  
         com.dimata.harisma.entity.masterdata.PstLeavePeriod,
         com.dimata.harisma.entity.masterdata.PstPeriod,
         com.dimata.harisma.entity.masterdata.PstDepartment,
         com.dimata.harisma.entity.masterdata.Department,
         com.dimata.harisma.entity.employee.PstEmployee,
         com.dimata.harisma.entity.attendance.SpecialStockId,
         com.dimata.harisma.entity.attendance.PstSpecialStockId,				  
         com.dimata.harisma.form.attendance.FrmSpecialStockId,
         com.dimata.harisma.form.attendance.CtrlSpecialStockId,
         com.dimata.harisma.entity.search.SrcLeaveManagement, 
         com.dimata.harisma.form.search.FrmSrcLeaveManagement,
         com.dimata.harisma.entity.leave.I_Leave,
         com.dimata.harisma.session.attendance.SessLeaveManagement"%>    
<!-- package qdep -->
<%@ include file = "../../main/javainit.jsp" %>    
<!-- JSP Block -->
<% int appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_EMPLOYEE, AppObjInfo.G2_LEAVE_MANAGEMENT, AppObjInfo.OBJ_LEAVE_DP_MANAGEMENT);%>
<%@ include file = "../../main/checkuser.jsp" %>

<%
    /* Check privilege except VIEW, view is already checked on checkuser.jsp as basic access*/
//boolean privDelete = userSession.checkPrivilege(AppObjInfo.composeCode(appObjCode, AppObjInfo.COMMAND_DELETE));
//boolean privAdd = userSession.checkPrivilege(AppObjInfo.composeCode(appObjCode, AppObjInfo.COMMAND_ADD));
//boolean privUpdate = userSession.checkPrivilege(AppObjInfo.composeCode(appObjCode, AppObjInfo.COMMAND_UPDATE));
//boolean privPrint = userSession.checkPrivilege(AppObjInfo.composeCode(appObjCode, AppObjInfo.COMMAND_PRINT));
%>

<%    int iCommand = FRMQueryString.requestCommand(request);
    int start = FRMQueryString.requestInt(request, "start");
    long oidDp = FRMQueryString.requestLong(request, "special_leave_management_id");

// get and set data from / to session
    try {
        if (session.getValue(SessLeaveManagement.SESS_MANAGEMENT_LEAVE_AL) != null) {
            SrcLeaveManagement srcLeaveManagement = (SrcLeaveManagement) session.getValue(SessLeaveManagement.SESS_MANAGEMENT_LEAVE_AL);
            session.putValue(SessLeaveManagement.SESS_MANAGEMENT_LEAVE_AL, srcLeaveManagement);
        }
    } catch (Exception e) {
        System.out.println("Exc when get/set data from/to session");
    }

// konstanta untuk navigasi ke database  
    String msgString = "";
    int iErrCode = 0;

// for manage data dp
    CtrlSpecialStockId ctrlSpecialStockId = new CtrlSpecialStockId(request);
    iErrCode = ctrlSpecialStockId.action(iCommand, oidDp);
    msgString = ctrlSpecialStockId.getMessage();
    SpecialStockId objSpecialStockId = ctrlSpecialStockId.getSpecialStockId();

    String messageSave = "";
    boolean savedOk = false;
    if (iCommand == Command.SAVE && iErrCode == 0) {
        msgString = "<div class=\"msginfo\">&nbsp;&nbsp;Data has been saved ...</div>";
        iCommand = Command.ADD;
        objSpecialStockId = new SpecialStockId();
        oidDp = 0;
        savedOk = true;
    }

    I_Leave leaveConfig = null;
    try {
        leaveConfig = (I_Leave) (Class.forName(PstSystemProperty.getValueByName("LEAVE_CONFIG")).newInstance());
    } catch (Exception except) {
        System.out.println("Exception" + except);
    }
    String readOnly = "readonly";
    String StyleBg = "background-color:#F5F5F5;text-align:right";
    boolean cekDpMinus = true;
    if (leaveConfig != null && leaveConfig.getConfigurationDpMinus() == false) {
        readOnly = "";
        StyleBg = "text-align:right";
        cekDpMinus = false;
    }
%>
<!-- End of JSP Block -->
<html>
    <!-- #BeginTemplate "/Templates/main.dwt" -->
    <head>
        <!-- #BeginEditable "doctitle" --> 
        <title>HARISMA - Stock Management</title>
        <!-- #EndEditable --> <!-- #BeginEditable "headerscript" --> <!-- #EndEditable --> 
        <!-- mySQL format yyyy-mm-dd 
        <script language="JavaScript" src="<%//=approot%>/main/calendar/calendar3.js"></script>
        -->
        <!-- Date only with year scrolling -->
        <script language="JavaScript">
        <!--
            function calqty()
            {
                var alqty = document.frdp.<%=FrmSpecialStockId.fieldNames[FrmSpecialStockId.FRM_FIELD_QTY]%>.value;
                var usedqty = document.frdp.<%=FrmSpecialStockId.fieldNames[FrmSpecialStockId.FRM_FIELD_QTY_USED]%>.value;
                if (isNaN(alqty) || alqty == "")
                {
                    alqty = 0;
                }

                if (isNaN(usedqty) || usedqty == "")
                {
                    usedqty = 0;
                }

                if (parseFloat(alqty) - parseFloat(usedqty) < 0 && <%=cekDpMinus%>)
                {
                    alert('Used quantity should be less or equal with Entitled quantity !!!');
            usedqty = 0        ;
                    document.frdp.<%=FrmSpecialStockId.fieldNames[FrmSpecialStockId.FRM_FIELD_QTY_USED]%>.value = 0;
                }

                var resqty = document.frdp.<%=FrmSpecialStockId.fieldNames[FrmSpecialStockId.FRM_FIELD_QTY_RESIDUE]%>.value = parseFloat(alqty) - parseFloat(usedqty);
            }


            function cmdSave()
    {        
                document.frdp.command.value = "<%=String.valueOf(Command.SAVE)%>";
                document.frdp.action = "dp_special_add.jsp";
                if (<%=leaveConfig != null && leaveConfig.getConfigurationDpMinus() == false%>) {
                    //alert(document.frdp.<//%=FrmSpecialStockId.fieldNames[FrmSpecialStockId.FRM_FIELD_QTY]%>);
                    //alert(document.frdp.<//%=FrmSpecialStockId.fieldNames[FrmSpecialStockId.FRM_FIELD_QTY]%>.value);
                    var alqty = document.frdp.<%=FrmSpecialStockId.fieldNames[FrmSpecialStockId.FRM_FIELD_QTY]%>.value;

                    if (parseFloat(alqty) == 0) {
                        var answer = confirm(" are you sure to input Stock Acquisition '0' value?")
                        if (answer) {
                            //alert ("Woo Hoo! So am I.")
                            document.frdp.submit();
                        }

                        // else 
                        //alert ("Darn. Well, keep trying then.")

                    } else {

                        document.frdp.submit();
                    }
                } else {
                    document.frdp.submit();

                }

            }

    function cmdBack() {        
                document.frdp.command.value = "<%=String.valueOf(Command.BACK)%>";
                document.frdp.action = "special_leave_management.jsp";
                document.frdp.submit();
            }

            function cmdSearchEmp() {
                emp_number = document.frdp.EMP_NUMBER.value;
                emp_fullname = document.frdp.EMP_FULLNAME.value;
                emp_department = document.frdp.EMP_DEPARTMENT.value;
                //window.open("empsearchdp.jsp?emp_number=" + emp_number + "&emp_fullname=" + emp_fullname + "&emp_department=" + emp_department);
                //window.open("empsearchdp.jsp?emp_number=" + emp_number + "&emp_fullname=" + emp_fullname + "&emp_department=" + emp_department, null, "height=460,width=600, status=yes,toolbar=no,menubar=no,location=no, scrollbars=yes");
                window.open("<%=approot%>/employee/search/search.jsp?formName=frdp&empPathId=<%=FrmSpecialStockId.fieldNames[FrmSpecialStockId.FRM_FIELD_EMPLOYEE_ID]%>", null, "height=550,width=600, status=yes,toolbar=no,menubar=no,location=no, scrollbars=yes");
                    }

                    function cmdClearSearchEmp() {
                        document.frdp.EMP_NUMBER.value = "";
                        document.frdp.EMP_FULLNAME.value = "";
                        document.frdp.EMP_DEPARTMENT.value = "";
                    }

        //--------------- Calendar  -------------------------------
                    function getThn() {
            <%=ControlDatePopup.writeDateCaller("frdp", FrmSpecialStockId.fieldNames[FrmSpecialStockId.FRM_FIELD_OWNING_DATE])%>
            <%=ControlDatePopup.writeDateCaller("frdp", FrmSpecialStockId.fieldNames[FrmSpecialStockId.FRM_FIELD_EXPIRED_DATE])%>
            <%=ControlDatePopup.writeDateCaller("frdp", FrmSpecialStockId.fieldNames[FrmSpecialStockId.FRM_FIELD_EXPIRED_DATE])%>

                    }


                    function hideObjectForDate(index) {
                        if (index == 1) {
            <%=ControlDatePopup.writeDateHideObj("frdp", FrmSpecialStockId.fieldNames[FrmSpecialStockId.FRM_FIELD_STATUS])%>
                        }
                    }

                    function showObjectForDate() {
            <%=ControlDatePopup.writeDateShowObj("frdp", FrmSpecialStockId.fieldNames[FrmSpecialStockId.FRM_FIELD_STATUS])%>
                    }



        //-------------- script control line -------------------

                    function MM_swapImgRestore() { //v3.0
                        var i, x, a = document.MM_sr;
                        for (i = 0; a && i < a.length && (x = a[i]) && x.oSrc; i++)
                            x.src = x.oSrc;
                    }

                    function MM_preloadImages() { //v3.0
                        var d = document;
                        if (d.images) {
                            if (!d.MM_p)
                                d.MM_p = new Array();
                            var i, j = d.MM_p.length, a = MM_preloadImages.arguments;
                            for (i = 0; i < a.length; i++)
                                if (a[i].indexOf("#") != 0) {
                                    d.MM_p[j] = new Image;
                                    d.MM_p[j++].src = a[i];
                                }
                        }
                    }

                    function MM_findObj(n, d) { //v4.0
                        var p, i, x;
                        if (!d)
                            d = document;
                        if ((p = n.indexOf("?")) > 0 && parent.frames.length) {
                            d = parent.frames[n.substring(p + 1)].document;
                            n = n.substring(0, p);
                        }
                        if (!(x = d[n]) && d.all)
                            x = d.all[n];
                        for (i = 0; !x && i < d.forms.length; i++)
                            x = d.forms[i][n];
                        for (i = 0; !x && d.layers && i < d.layers.length; i++)
                            x = MM_findObj(n, d.layers[i].document);
                        if (!x && document.getElementById)
                            x = document.getElementById(n);
                        return x;
                    }

                    function MM_swapImage() { //v3.0
                        var i, j = 0, x, a = MM_swapImage.arguments;
                        document.MM_sr = new Array;
                        for (i = 0; i < (a.length - 2); i += 3)
                            if ((x = MM_findObj(a[i])) != null) {
                                document.MM_sr[j++] = x;
                                if (!x.oSrc)
                                    x.oSrc = x.src;
                                x.src = a[i + 2];
                            }
                    }
        //-->
        </script>
        <!-- #EndEditable --> 
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
        <!-- #BeginEditable "styles" --> 
        <link rel="stylesheet" href="../../styles/main.css" type="text/css">
        <!-- #EndEditable --> <!-- #BeginEditable "stylestab" --> 
        <link rel="stylesheet" href="../../styles/tab.css" type="text/css">
        <link rel="stylesheet" href="<%=approot%>/styles/calendar.css" type="text/css">


    </head>
    <body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" onLoad="MM_preloadImages('/harisma_proj/images/BtnDelOn.jpg', '/harisma_proj/images/BtnNewOn.jpg', '<%=approot%>/images/BtnSearchOn.jpg', '/harisma_proj/images/BtnBackOn.jpg')">
        <!-- Untuk Calender-->
        <%=(ControlDatePopup.writeTable(approot))%>
        <!-- End Calender-->
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
                    <table width="100%" border="0" cellspacing="3" cellpadding="2">
                        <tr> 
                            <td width="100%"> 
                                <table width="100%" border="0" cellspacing="0" cellpadding="0">
                                    <tr> 
                                        <td height="20"> <font color="#FF6600" face="Arial"><strong> <!-- #BeginEditable "contenttitle" -->Attendance 
                                                &gt; Stock &gt; Add New Stock Per Employee<!-- #EndEditable --> 
                                            </strong></font> </td>
                                    </tr>
                                    <tr> 
                                        <td> 
                                            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                                                <tr> 
                                                    <td  style="background-color:<%=bgColorContent%>; "> 
                                                        <table width="100%" border="0" cellspacing="1" cellpadding="1" class="tablecolor">
                                                            <tr> 
                                                                <td valign="top"> 
                                                                    <table style="border:1px solid <%=garisContent%>"  width="100%" border="0" cellspacing="1" cellpadding="1" class="tabbg">
                                                                        <tr> 
                                                                            <td valign="top"> <!-- #BeginEditable "content" --> 
                                                                                <form name="frdp" method="post" action="">
                                                                                    <input type="hidden" name="command" value="<%=String.valueOf(iCommand)%>">
                                                                                    <input type="hidden" name="start" value="<%=String.valueOf(start)%>">
                                                                                    <input type="hidden" name="special_leave_management_id" value="<%=String.valueOf(oidDp)%>">

                                                                                    <table width="100%" border="0" cellspacing="2" cellpadding="2">
                                                                                        <tr> 
                                                                                            <td colspan="3"> 
                                                                                        <li>&nbsp;<b>Pick one employee using 
                                                                                                Search form</b> 
                                                                                            </td>
                                                                                            </tr>
                                                                                        <tr> 
                                                                                            <td colspan="3"> 
                                                                                                <table cellpadding="1" cellspacing="1" border="0" width="40%" bgcolor="#E0EDF0">
                                                                                                    <tr> 
                                                                                                        <td width="84"></td>
                                                                                                        <td width="11"></td>
                                                                                                        <td width="285"></td>
                                                                                                    </tr>
                                                                                                    <tr> 
                                                                                                        <td valign="top" width="84"> 
                                                                                                            <div align="left"><%=dictionaryD.getWord(I_Dictionary.PAYROLL)%></div>
                                                                                                        </td>
                                                                                                        <td width="11">:</td>
                                                                                                        <td width="285"> 
                                                                                                            <input type="text" name="EMP_NUMBER"  value="" class="elemenForm" size="10" readonly>
                                                                                                            <input type="hidden" name="<%=FrmSpecialStockId.fieldNames[FrmSpecialStockId.FRM_FIELD_EMPLOYEE_ID]%>" value="" class="formElemen" >
                                                                                                        </td>
                                                                                                    </tr>
                                                                                                    <tr> 
                                                                                                        <td valign="top" width="84"> 
                                                                                                            <div align="left">Name</div>
                                                                                                        </td>
                                                                                                        <td width="11">:</td>
                                                                                                        <td width="285"> 
                                                                                                            <input type="text" name="EMP_FULLNAME"  value="" class="elemenForm" size="40" readonly>
                                                                                                        </td>
                                                                                                    </tr>
                                                                                                    <tr> 
                                                                                                        <td valign="top" width="84"><%=dictionaryD.getWord(I_Dictionary.DEPARTMENT)%></td>
                                                                                                        <td width="11">:</td>
                                                                                                        <td width="285"> 
                                                                                                            <input type="text" name="EMP_DEPARTMENT"  value="" class="elemenForm" size="20" readonly>
                                                                                                        </td>
                                                                                                    </tr>
                                                                                                    <tr> 
                                                                                                        <td valign="top" width="84">&nbsp;</td>
                                                                                                        <td width="11">&nbsp;</td>
                                                                                                        <td width="285"> <a href="javascript:cmdSearchEmp()" class="command">Search 
                                                                                                                Employee</a> | <a href="javascript:cmdClearSearchEmp()" class="command">Clear 
                                                                                                                Search</a> 
                                                                                                        </td>
                                                                                                    </tr>
                                                                                                </table>
                                                                                            </td>
                                                                                        </tr>
                                                                                        <tr> 
                                                                                            <td colspan="3">&nbsp;</td>
                                                                                        </tr>
                                                                                        <tr> 
                                                                                            <td colspan="3"> 
                                                                                        <li>&nbsp;<b>Fill Day Off Payment Data </b> 
                                                                                            </td>
                                                                                            </tr>
                                                                                        <tr> 
                                                                                            <td colspan="3"> 
                                                                                                <table width="100%" border="0">
                                                                                                    <tr> 
                                                                                                        <td valign="top" width="50%"> 
                                                                                                            <table width="100%" border="0">
                                                                                                                <tr> 
                                                                                                                    <td width="24%">Owning
                                                                                                                        Date</td>
                                                                                                                    <td width="4%">:</td>
                                                                                                                    <td width="72%"> 
                                                                                                                        <%
                                                                                                                            Date selectedOwnDate = objSpecialStockId.getOwningDate() != null ? objSpecialStockId.getOwningDate() : new Date();
                                                                                                                        %>
                                                                                                                        <%=ControlDatePopup.writeDate(FrmSpecialStockId.fieldNames[FrmSpecialStockId.FRM_FIELD_OWNING_DATE], selectedOwnDate)%>
                                                                                                                    </td>

                                                                                                                    </td>
                                                                                                                </tr>
                                                                                                                <tr> 
                                                                                                                    <td width="24%">Expired Date</td>
                                                                                                                    <td width="4%">:</td>
                                                                                                                    <td width="72%"> 
                                                                                                                        <%
                                                                                                                            Date selectedExpDate = objSpecialStockId.getExpiredDate() != null ? objSpecialStockId.getExpiredDate() : new Date();
																															selectedExpDate.setMonth(11);
																															selectedExpDate.setDate(31);
                                                                                                                        %>
                                                                                                                        <%=ControlDatePopup.writeDate(FrmSpecialStockId.fieldNames[FrmSpecialStockId.FRM_FIELD_EXPIRED_DATE], selectedExpDate)%>
                                                                                                                    </td>
                                                                                                                </tr>
                                                                                                                <tr> 
                                                                                                                    <td width="24%">Schedule</td>
                                                                                                                    <td width="4%">:</td>
                                                                                                                    <td width="72%"> 
                                                                                                                        <select name="<%=FrmSpecialStockId.fieldNames[FrmSpecialStockId.FRM_FIELD_SCHEDULE_ID]%>">
                                                                                                                            <%
                                                                                                                                String scheduleCategoryId = "SELECT " + PstScheduleCategory.fieldNames[PstScheduleCategory.FLD_SCHEDULE_CATEGORY_ID];
                                                                                                                                scheduleCategoryId += " FROM " + PstScheduleCategory.TBL_HR_SCHEDULE_CATEGORY;
                                                                                                                                scheduleCategoryId += " WHERE " + PstScheduleCategory.fieldNames[PstScheduleCategory.FLD_CATEGORY_TYPE] + " = " + PstScheduleCategory.CATEGORY_STOCK_LEAVE;
                                                                                                                                String whereSchedule = PstScheduleSymbol.fieldNames[PstScheduleSymbol.FLD_SCHEDULE_CATEGORY_ID] + " IN (" + scheduleCategoryId + ")";
                                                                                                                                Vector vSchedule = PstScheduleSymbol.list(0, 0, whereSchedule, PstScheduleSymbol.fieldNames[PstScheduleSymbol.FLD_SCHEDULE]);
                                                                                                                                for (int i = 0; i < vSchedule.size(); i++) {
                                                                                                                                    ScheduleSymbol sce = (ScheduleSymbol) vSchedule.get(i);
                                                                                                                            %>
                                                                                                                            <option value="<%=sce.getOID()%>"><%=sce.getSchedule()%></option> <%
                                                                                                                                }
                                                                                                                            %>						
                                                                                                                        </select>
                                                                                                                    </td>
                                                                                                                </tr>
                                                                                                                <tr> 
                                                                                                                    <td width="24%">QTY</td>
                                                                                                                    <td width="4%">:</td>
                                                                                                                    <td width="72%"> 
                                                                                                                        <input type="text" bgcolor =\"000000\" name="<%=FrmSpecialStockId.fieldNames[FrmSpecialStockId.FRM_FIELD_QTY]%>" value="1" onKeyUp="javascript:calqty()" style="background-color:#F5F5F5;text-align:right" size="10" readonly>
                                                                                                                    </td>
                                                                                                                </tr>
                                                                                                            </table>
                                                                                                        </td>
                                                                                                        <td valign="top" width="50%"> 
                                                                                                            <table width="100%" border="0">
                                                                                                                <tr> 
                                                                                                                    <td width="24%">QTY Used</td>
                                                                                                                    <td width="4%">:</td>
                                                                                                                    <td width="72%"> 
                                                                                                                        <input type="text" bgcolor =\"000000\" name="<%=FrmSpecialStockId.fieldNames[FrmSpecialStockId.FRM_FIELD_QTY_USED]%>" value="<%=String.valueOf(objSpecialStockId.getQtyUsed())%>" onKeyUp="javascript:calqty()" style="background-color:#F5F5F5;text-align:right" size="10">
                                                                                                                    </td>
                                                                                                                </tr>
                                                                                                                <tr> 
                                                                                                                    <td width="24%">QTY Residu</td>
                                                                                                                    <td width="4%">:</td>
                                                                                                                    <td width="72%"> 
                                                                                                                        <input type="text" bgcolor =\"000000\" name="<%=FrmSpecialStockId.fieldNames[FrmSpecialStockId.FRM_FIELD_QTY_RESIDUE]%>" value="1" onKeyUp="javascript:calqty()" style="background-color:#F5F5F5;text-align:right" size="10" readonly>
                                                                                                                    </td>
                                                                                                                </tr>
                                                                                                                <%--
                                                                                                                <tr> 
                                                                                                                  <td width="26%">Status</td>
                                                                                                                  <td width="4%">:</td>
                                                                                                                  <td width="72%"> 
                                                                                                                    <input type="text" bgcolor =\"000000\" name="<%=FrmSpecialStockId.fieldNames[FrmSpecialStockId.FRM_FIELD_STATUS]%>" value="<%=String.valueOf(objSpecialStockId.getStatus())%>" onKeyUp="javascript:calqty()" style="background-color:#F5F5F5;text-align:right" size="10">
                                                                                                                  </td>
                                                                                                                </tr>
                                                                                                                --%>
                                                                                                                <tr> 
                                                                                                                    <td width="26%">Note</td>
                                                                                                                    <td width="4%">:</td>
                                                                                                                    <td width="70%"> 
                                                                                                                        <textarea type="text" name="<%=FrmSpecialStockId.fieldNames[FrmSpecialStockId.FRM_FIELD_NOTE]%>" size="40" value=""></textarea>
                                                                                                                    </td>
                                                                                                                </tr>
                                                                                                            </table>
                                                                                                        </td>
                                                                                                    </tr>
                                                                                                </table>
                                                                                            </td>
                                                                                        </tr>
                                                                                        <tr> 
                                                                                            <td colspan="3">
                                                                                                <%
                                                                                                    if (savedOk) {
                                                                                                        out.println(msgString);
                                                                                                    } else {
                                                                                                        out.println("&nbsp;");
                                                                                                    }
                                                                                                %>
                                                                                            </td>
                                                                                        </tr>
                                                                                        <tr> 
                                                                                            <td colspan="3"> 
                                                                                                <%
                                                                                                    ControlLine ctrLine = new ControlLine();
                                                                                                    ctrLine.setLocationImg(approot + "/images");
                                                                                                    ctrLine.initDefault();
                                                                                                    ctrLine.setTableWidth("60%");

                                                                                                    String scomDel = "javascript:cmdAsk('" + oidDp + "')";
                                                                                                    String sconDelCom = "javascript:cmdConfirmDelete('" + oidDp + "')";
                                                                                                    String scancel = "javascript:cmdEdit('" + oidDp + "')";
                                                                                                    ctrLine.setBackCaption("Back to List");
                                                                                                    ctrLine.setCommandStyle("buttonlink");
                                                                                                    ctrLine.setBackCaption("Back to List Stock");
                                                                                                    ctrLine.setSaveCaption("Save Stock");
                                                                                                    ctrLine.setConfirmDelCaption("Yes Delete Stock");
                                                                                                    ctrLine.setDeleteCaption("Delete Stock");
                                                                                                    ctrLine.setAddCaption("");

                                                                                                    if (privDelete) {
                                                                                                        ctrLine.setConfirmDelCommand(sconDelCom);
                                                                                                        ctrLine.setDeleteCommand(scomDel);
                                                                                                        ctrLine.setEditCommand(scancel);
                                                                                                    } else {
                                                                                                        ctrLine.setConfirmDelCaption("");
                                                                                                        ctrLine.setDeleteCaption("");
                                                                                                        ctrLine.setEditCaption("");
                                                                                                    }

                                                                                                    if (privAdd == false && privUpdate == false) {
                                                                                                        ctrLine.setSaveCaption("");
                                                                                                    }

                                                                                                    if (iCommand == Command.ASK) {
                                                                                                        ctrLine.setDeleteQuestion(msgString);
                                                                                                    }

                                                                                                    out.println(ctrLine.drawImage(iCommand, iErrCode, msgString));
                                                                                                %>										  
                                                                                            </td>
                                                                                        </tr>
                                                                                    </table>
                                                                                </form>
                                                                                <script language="javascript">
                                                                                    document.frdp.EMP_NUMBER.focus();
                                                                                </script>
                                                                            </td>
                                                                        </tr>
                                                                    </table>
                                                                </td>
                                                            </tr>
                                                        </table>
                                                    </td>
                                                </tr>
                                                <tr> 
                                                    <td>&nbsp; </td>
                                                </tr>
                                            </table>
                                        </td>
                                    </tr>
                                </table>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
            <%
                if(headerStyle && !verTemplate.equalsIgnoreCase("0")){
            %>
            <tr>
                <td valign="bottom">
                    <%@include file="../../footer.jsp" %>
                </td>
            </tr>
            <%
                } else{
            %>
            <tr> 
                <td colspan="2" height="20" >
                    <%@ include file = "../../main/footer.jsp" %>
                </td>
            </tr>
            <%}%>
        </table>
    </body>
</html>
