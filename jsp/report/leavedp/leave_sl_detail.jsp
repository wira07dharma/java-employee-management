<%-- 
    Document   : leave_sl_detail
    Created on : 15-Sep-2017, 10:11:19
    Author     : Gunadi
--%>



<%@page import="com.dimata.harisma.entity.leave.PstLeaveApplication"%>
<%@page import="com.dimata.harisma.entity.leave.PstSpecialUnpaidLeaveTaken"%>
<%@page import="com.dimata.harisma.session.employee.SessTmpSpecialEmployee"%>
<%@page import="com.dimata.harisma.session.employee.SessSpecialEmployee"%>
<%@page import="com.dimata.harisma.form.search.FrmSrcSpecialEmployeeQuery"%>
<%@page import="com.dimata.harisma.session.employee.SearchSpecialQuery"%>
<%@ page language="java" %>
<!-- package java -->
<%@ page import ="java.util.*,
                  com.dimata.harisma.entity.attendance.PstAlStockManagement,
                  com.dimata.harisma.entity.attendance.AlStockManagement,
                  com.dimata.harisma.form.attendance.FrmAlStockManagement,
                  com.dimata.harisma.form.masterdata.CtrlPosition,
                  com.dimata.harisma.form.masterdata.FrmPosition,
                  com.dimata.harisma.form.attendance.CtrlAlStockManagement,
                  com.dimata.gui.jsp.ControlList,
                  com.dimata.harisma.entity.employee.Employee,
                  com.dimata.harisma.entity.masterdata.LeavePeriod,
                  com.dimata.util.Command,
                  com.dimata.gui.jsp.ControlDate,
                  com.dimata.gui.jsp.ControlCombo,
                  com.dimata.util.Formater,
                  com.dimata.qdep.form.FRMQueryString,
                  com.dimata.harisma.entity.masterdata.PstLeavePeriod,
		  com.dimata.harisma.entity.attendance.ALStockReporting,
                  com.dimata.harisma.entity.attendance.DpStockManagement,
                  com.dimata.harisma.entity.masterdata.PstDepartment,
                  com.dimata.harisma.entity.masterdata.Department,
                  com.dimata.harisma.entity.employee.PstEmployee,
                  com.dimata.harisma.entity.search.SrcLeaveManagement,
                  com.dimata.harisma.form.search.FrmSrcLeaveManagement,				  
                  com.dimata.harisma.session.attendance.AnnualLeaveMontly,
                  com.dimata.harisma.session.leave.*,
                  com.dimata.harisma.session.attendance.*,                  
                  com.dimata.harisma.session.leave.SessLeaveApp,com.dimata.harisma.session.leave.RepItemLeaveAndDp"%>
<!-- package qdep -->

<%@ include file = "../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_REPORTS, AppObjInfo.G2_LEAVE_REPORT, AppObjInfo.OBJ_LEAVE_DP_DETAIL); %>
<%@ include file = "../../main/checkuser.jsp" %>

<%
/* Check privilege except VIEW, view is already checked on checkuser.jsp as basic access*/    
//boolean privPrint = userSession.checkPrivilege(AppObjInfo.composeCode(appObjCode, AppObjInfo.COMMAND_PRINT));
%>

<%!
int DATA_NULL = 0;
int DATA_PRINT = 1;

/**
 * create list of report items
 */
public String drawList(JspWriter outObj, Vector listEmployee, String dtStart, String dtEnd) 
{ 
    String result = "";
    Date currDate = new Date();
    String formatFloat ="###.###";
    Vector listSymbolId = PstScheduleSymbol.getScheduleId(PstScheduleCategory.CATEGORY_SPECIAL_LEAVE);

    if(listEmployee!=null && listEmployee.size()>0 && listSymbolId!=null && listSymbolId.size()>0)
    {
            ControlList ctrlist = new ControlList();
            ctrlist.setAreaWidth("100%");
            ctrlist.setListStyle("listgen");
            ctrlist.setTitleStyle("listgentitle");
            ctrlist.setCellStyle("listgensell");
            ctrlist.setHeaderStyle("listgentitle");

            ctrlist.addHeader("NO","2%", "2", "0");
            ctrlist.addHeader("PayRoll","5%", "2", "0");
            ctrlist.addHeader("Employee","20%", "2", "0");
            
            
            for (int x=0;x<listSymbolId.size();x++){
                long oidScheduleSL = 0;
                ScheduleSymbol schSymbol = new ScheduleSymbol();
                if(listSymbolId.get(x)!=null){
                    oidScheduleSL = ((Long)listSymbolId.get(x)).longValue();
                    try {
                        schSymbol = PstScheduleSymbol.fetchExc(oidScheduleSL);
                    } catch (Exception exc){
                        System.out.println(exc.toString());
                    }
                    ctrlist.addHeader(""+schSymbol.getSchedule(),"20%","0", "3");
                }
            }
            
            int countCol = 0;
            for (int x=0;x<listSymbolId.size();x++){
                if(listSymbolId.get(x)!=null){
                   ctrlist.addHeader("Taken","4%", "0", "0");
                   ctrlist.addHeader("Will Be Taken","4%", "0", "0"); 
                   ctrlist.addHeader("Detail ","4%", "0", "0");
                   countCol = countCol + 3;
                }
            }
            
            ctrlist.setLinkRow(-1);
            ctrlist.setLinkSufix("");
            Vector lstData = ctrlist.getData();
            Vector lstLinkData = ctrlist.getLinkData();
            ctrlist.setLinkPrefix("javascript:cmdEdit('");
            ctrlist.setLinkSufix("')");
            ctrlist.reset();
                       
            
            //iterasi sebanyak data Special Leave yang ada
            int iterateNo = 0;
            //boolean resultAvailable = false;
            
            
            float[] totalSLTaken = new float[listSymbolId.size()];
            float[] totalSL2BTaken = new float[listSymbolId.size()];
            
            
            long oidDept=0;
            for (int i=0; i<listEmployee.size(); i++) { 
                Employee emp =  (Employee) listEmployee.get(i);
              try{
                
                
                iterateNo++;

                if(oidDept != emp.getDepartmentId()){
                    
                    oidDept=emp.getDepartmentId();
                    Department dep = new Department();
                    
                    try{
                        dep = PstDepartment.fetchExc(oidDept);                                            
                    } catch(Exception exc){      
                        System.out.println("EXCEPTION ::::"+exc.toString());
                    }
                    
                    
                Vector rowxDep = new Vector(1,1);
                
                //DP



                rowxDep.add("");
                rowxDep.add("<b>DEPT:</b>");
                rowxDep.add("<b>"+dep.getDepartment()+"</b>");
                
                for(int id=0;id<countCol;id++){                
                    rowxDep.add("");
                }                                
                lstData.add(rowxDep);
                lstLinkData.add("0");                    
                }
                
                Vector rowx = new Vector(1,1);
                
                
                rowx.add(""+iterateNo);
                rowx.add(""+emp.getEmployeeNum());
                rowx.add(""+emp.getFullName()); 
                
                for (int x=0;x<listSymbolId.size();x++){
                    long oidScheduleSL = 0;
                    ScheduleSymbol schSymbol = new ScheduleSymbol();
                    if(listSymbolId.get(x)!=null){
                        oidScheduleSL = ((Long)listSymbolId.get(x)).longValue();
                        String whereClause = "SP."+PstSpecialUnpaidLeaveTaken.fieldNames[PstSpecialUnpaidLeaveTaken.FLD_EMPLOYEE_ID]+" = "+emp.getOID()
                                            + " AND SP."+PstSpecialUnpaidLeaveTaken.fieldNames[PstSpecialUnpaidLeaveTaken.FLD_SCHEDULED_ID]+" = "+oidScheduleSL
                                            + " AND (SP."+PstSpecialUnpaidLeaveTaken.fieldNames[PstSpecialUnpaidLeaveTaken.FLD_TAKEN_DATE]+" BETWEEN "
                                            + "'" +dtStart+" 00:00:00' AND '"+dtEnd+" 23:59:00'" 
                                            + " OR SP."+PstSpecialUnpaidLeaveTaken.fieldNames[PstSpecialUnpaidLeaveTaken.FLD_TAKEN_FINNISH_DATE]+" BETWEEN "
                                            + "'" +dtStart+" 00:00:00' AND '"+dtEnd+" 23:59:00'"
                                            + " OR '" +dtStart+" 00:00:00' BETWEEN SP."+PstSpecialUnpaidLeaveTaken.fieldNames[PstSpecialUnpaidLeaveTaken.FLD_TAKEN_DATE]
                                            + " AND SP."+PstSpecialUnpaidLeaveTaken.fieldNames[PstSpecialUnpaidLeaveTaken.FLD_TAKEN_FINNISH_DATE]
                                            + " OR '"+dtEnd+" 23:59:00' BETWEEN SP."+PstSpecialUnpaidLeaveTaken.fieldNames[PstSpecialUnpaidLeaveTaken.FLD_TAKEN_DATE]
                                            + " AND SP."+PstSpecialUnpaidLeaveTaken.fieldNames[PstSpecialUnpaidLeaveTaken.FLD_TAKEN_FINNISH_DATE]
                                            + " )";
                        String whereTaken = whereClause + " AND LA."+PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DOC_STATUS]+" IN (2,3)";
                        String whereToBeTaken = whereClause + " AND LA."+PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DOC_STATUS]+" IN (0,1)";                        
                        rowx.add(""+PstSpecialUnpaidLeaveTaken.getSumQtySpcUnpLeaveTkn(whereTaken));
                        rowx.add(""+PstSpecialUnpaidLeaveTaken.getSumQtySpcUnpLeaveTkn(whereToBeTaken));
                        rowx.add("<a href=\"javascript:cmdPrintDetail('"+emp.getOID()+"','"+oidScheduleSL+"','"+dtStart+"','"+dtEnd+"')\">View Detail</a>");
                        
                        totalSLTaken[x] = totalSLTaken[x] + PstSpecialUnpaidLeaveTaken.getSumQtySpcUnpLeaveTkn(whereTaken);
                        totalSL2BTaken[x] = totalSL2BTaken[x] + PstSpecialUnpaidLeaveTaken.getSumQtySpcUnpLeaveTkn(whereToBeTaken);
                    }
                }
                               
                lstData.add(rowx);
                lstLinkData.add("0");
            	
                } catch(Exception exc){
                System.out.println(exc);
                }
            }
            
            Vector rowx = new Vector(1,1);  
                
            rowx.add("");
            rowx.add("<b>TOTAL</b>");
            rowx.add("<b>"+""+"</b>");
            
            for (int x=0;x<listSymbolId.size();x++){
                if(listSymbolId.get(x)!=null){
                   rowx.add("<b>"+totalSLTaken[x]+"</b>");
                   rowx.add("<b>"+totalSL2BTaken[x]+"</b>");
                   rowx.add("-");
                }
            }
            
            
            lstData.add(rowx);
            lstLinkData.add("0");

            result = ctrlist.drawList();                    
            /*try{
                       ctrlist.drawMe(outObj,0);
                   }catch(Exception e){
                       System.out.println("Exception "+e.toString());
    }
          return "";            */
     }
    else
    {					
            result += "<div class=\"msginfo\">&nbsp;&nbsp;Leave and Dp Stock Data Found found ...</div>";																
    }
    return result;	
}

%>

<%
    long hrdDepartmentOid = Long.parseLong(String.valueOf(PstSystemProperty.getPropertyLongbyName(OID_HRD_DEPARTMENT)));
    boolean isHRDLogin = hrdDepartmentOid == departmentOid ? true : false;
    long edpSectionOid = Long.parseLong(String.valueOf(PstSystemProperty.getPropertyLongbyName(OID_EDP_SECTION)));
    boolean isEdpLogin = edpSectionOid == sectionOfLoginUser.getOID() ? true : false;
    boolean isGeneralManager = positionType == PstPosition.LEVEL_GENERAL_MANAGER ? true : false;

    int iCommand = FRMQueryString.requestCommand(request);

    String empName = FRMQueryString.requestString(request, "empname");
    String empNum = FRMQueryString.requestString(request, "empnum");
    long oidLevel = FRMQueryString.requestLong(request, "level");
    String[] oidDept = FRMQueryString.requestStringValues(request, "department");
    String[] oidPayGroup = FRMQueryString.requestStringValues(request, "payroll_group");
    String dateFrom = FRMQueryString.requestString(request, "date_from");
    String dateTo = FRMQueryString.requestString(request, "date_to");

    int year = Calendar.getInstance().get(Calendar.YEAR);
    if (dateFrom.equals("") && dateTo.equals("")){
        dateFrom = year+"-01-01";
        dateTo = year+"-12-31";
    }
    Vector<String> whereCollect = new Vector<String>();
    String whereClauseEmp = PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED]+" = 0";
    
    if (!empName.equals("")){
        whereClauseEmp = PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]+" LIKE '%"+empName+"%'";
        whereCollect.add(whereClauseEmp);
    }
    if (!empNum.equals("")){
        whereClauseEmp = PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]+" LIKE '%"+empNum+"%'";
        whereCollect.add(whereClauseEmp);
    }
    if (oidDept != null){
        String inDept = "";
        for (int i=0; i < oidDept.length; i++){
            if (Long.valueOf(oidDept[i])> 0){
                inDept = inDept + ","+ oidDept[i];
            }
        }
        if (!inDept.equals("")){
            inDept = inDept.substring(1);
            whereClauseEmp = PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID]+" IN ("+inDept+")";
            whereCollect.add(whereClauseEmp);
        }
    }
    if (oidLevel != 0){
        whereClauseEmp = PstEmployee.fieldNames[PstEmployee.FLD_LEVEL_ID]+" = "+oidLevel+"";
        whereCollect.add(whereClauseEmp);
    }
    if (oidPayGroup != null){
        String inPayGroup = "";
        for (int i=0; i < oidDept.length; i++){
            if (Long.valueOf(oidPayGroup[i])> 0){
                inPayGroup = inPayGroup + ","+ oidPayGroup[i];
            }
        }
        if (!inPayGroup.equals("")){
            inPayGroup = inPayGroup.substring(1);
            whereClauseEmp = PstEmployee.fieldNames[PstEmployee.FLD_PAYROLL_GROUP]+" IN ("+inPayGroup+")";
            whereCollect.add(whereClauseEmp);
        }
    }
//    if (!dateFrom.equals("") && !dateTo.equals("")){
//        whereClauseEmp = " odt."+PstSpecialUnpaidLeaveTaken.fieldNames[PstSpecialUnpaidLeaveTaken.FLD_TAKEN_DATE]+" BETWEEN "
//        + "'" +dateFrom+" 00:00:00' AND '"+dateTo+" 23:59:00'" ;
//        whereCollect.add(whereClauseEmp);
//    }
    
    if (whereCollect != null && whereCollect.size()>0){
        whereClauseEmp = "";
        for (int i=0; i<whereCollect.size(); i++){
            String where = (String)whereCollect.get(i);
            whereClauseEmp += where;
            if (i < (whereCollect.size()-1)){
                 whereClauseEmp += " AND ";
            }
        }
    }
    
    Vector listEmployee = new Vector();
    if(iCommand != Command.NONE) {
        listEmployee = PstEmployee.list(0,0,whereClauseEmp, "DEPARTMENT_ID");
    }
%>
<!-- End of JSP Block -->
<html>
<!-- #BeginTemplate "/Templates/main.dwt" -->
<head>
<!-- #BeginEditable "doctitle" -->
         <%@ include file = "../../main/konfigurasi_jquery.jsp" %>    
        <script src="../../javascripts/jquery.min-1.6.2.js" type="text/javascript"></script>
        <script src="../../javascripts/chosen.jquery.js" type="text/javascript"></script>
<title>HARISMA - Special Leave Report</title>
<script language="JavaScript">

function cmdPrintXls()
{ 
        document.frpresence.action="detail_sl_printxls.jsp";
	document.frpresence.submit();
}

function cmdView()
{
	document.frpresence.command.value="<%=Command.LIST%>";
	document.frpresence.action="leave_sl_detail.jsp";
	document.frpresence.submit();
}

function cmdPrintDetail(employeeId,scheduleId,dtFrom,dtTo){
        window.open("<%=approot%>/report/leavedp/detail_sl.jsp?employeeId="+employeeId+"&schId="+scheduleId+"&dateFrom="+dtFrom+"&dateTo="+dtTo, null, "height=400,width=700, status=yes,toolbar=no,menubar=no,location=no, scrollbars=yes");       
}

function cmdPrintDetailAll(type){
        window.open("<%=approot%>/report/leavedp/detail_report_all.jsp?type="+type+"", null, "height=400,width=700, status=yes,toolbar=no,menubar=no,location=no, scrollbars=yes");       
}

function cmdPrint()
{
	var linkPage = "<%=printroot%>.report.attendance.AnnualLeaveMonthlyPdf";
	window.open(linkPage);
}

//-------------- script control line -------------------
function MM_swapImgRestore() { //v3.0
  var i,x,a=document.MM_sr; for(i=0;a&&i<a.length&&(x=a[i])&&x.oSrc;i++) x.src=x.oSrc;
}

function MM_preloadImages() { //v3.0
  var d=document; if(d.images){ if(!d.MM_p) d.MM_p=new Array();
    var i,j=d.MM_p.length,a=MM_preloadImages.arguments; for(i=0; i<a.length; i++)
    if (a[i].indexOf("#")!=0){ d.MM_p[j]=new Image; d.MM_p[j++].src=a[i];}}
}

function MM_findObj(n, d) { //v4.0
  var p,i,x;  if(!d) d=document; if((p=n.indexOf("?"))>0&&parent.frames.length) {
    d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}
  if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];
  for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=MM_findObj(n,d.layers[i].document);
  if(!x && document.getElementById) x=document.getElementById(n); return x;
}

function MM_swapImage() { //v3.0
  var i,j=0,x,a=MM_swapImage.arguments; document.MM_sr=new Array; for(i=0;i<(a.length-2);i+=3)
   if ((x=MM_findObj(a[i]))!=null){document.MM_sr[j++]=x; if(!x.oSrc) x.oSrc=x.src; x.src=a[i+2];}
}
//-->
</script>
<link rel="stylesheet" href="<%=approot%>/javascripts/datepicker/themes/jquery.ui.all.css"/>

<script src="<%=approot%>/javascripts/datepicker/jquery.ui.core.js"></script>
<script src="<%=approot%>/javascripts/datepicker/jquery.ui.widget.js"></script>
<script src="<%=approot%>/javascripts/datepicker/jquery.ui.datepicker.js"></script>

<script>
$(function() {
    $( ".datepicker" ).datepicker({ dateFormat: "yy-mm-dd" });
});
</script>
<!-- #EndEditable -->
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<!-- #BeginEditable "styles" -->
<link rel="stylesheet" href="../../styles/main.css" type="text/css">
<!-- #EndEditable --> <!-- #BeginEditable "stylestab" -->
<link rel="stylesheet" href="../../styles/tab.css" type="text/css">
<!-- #EndEditable --> <!-- #BeginEditable "headerscript" -->
<!-- #EndEditable -->
</head>
<body <%=noBack%> bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" onLoad="MM_preloadImages('<%=approot%>/images/BtnSearchOn.jpg')">
<table width="100%" border="0" cellspacing="0" cellpadding="0" height="100%" bgcolor="#F9FCFF" >
     <%if(headerStyle && !verTemplate.equalsIgnoreCase("0")){%> 
           <%@include file="../../styletemplate/template_header.jsp" %>
            <%}else{%>
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
                <td height="20"> <font color="#FF6600" face="Arial"><strong> <!-- #BeginEditable "contenttitle" -->Report
                  &gt; Leave &gt; Detail Leave Report<!-- #EndEditable --> </strong></font>
                </td>
              </tr>
              <tr> 
                <td> 
                  <table width="100%" border="0" cellspacing="0" cellpadding="0">
                    <tr> 
                      <td  style="background-color:<%=bgColorContent%>; "> 
                        <table width="100%" border="0" cellspacing="1" cellpadding="1" class="tablecolor">
                          <tr> 
                            <td valign="top"> 
                              <table style="border:1px solid <%=garisContent%>" width="100%" border="0" cellspacing="1" cellpadding="1" class="tabbg">
                                <tr> 
                                  <td valign="top"> <!-- #BeginEditable "content" -->
                                    <form name="frpresence" method="post" action="">
				      <input type="hidden" name="command" value="<%=iCommand%>">
                                      <table width="60%" border="0" cellspacing="2" cellpadding="2">
                                        <tr> 
                                          <td width="1%">&nbsp;</td>
                                          <td nowrap width="18%">Name</td>
                                          <td width="3%">:</td>
                                          <td width="78%"> 
                                            <input type="text" name="empname"  value="<%=empName%>" class="elemenForm" size="40">
                                          </td>
                                        </tr>
                                        <tr> 
                                          <td width="1%">&nbsp;</td>
                                          <td nowrap width="18%">Payroll Number</td>
                                          <td width="3%">:</td>
                                          <td width="78%"> 
                                            <input type="text" name="empnum"  value="<%=empNum%>" class="elemenForm">
                                          </td>
                                        </tr>
                                        <!--
                                        <tr> 
                                          <td width="1%">&nbsp;</td>
                                          <td nowrap width="18%">Category</td>
                                          <td width="3%">:</td>
                                          <td width="78%"> 
                                            <% 
											/*
												Vector cat_value = new Vector(1,1);
												Vector cat_key = new Vector(1,1);        
												cat_value.add("0");
												cat_key.add("select ...");                                                          
												Vector listCat = PstEmpCategory.list(0, 0, "", " EMP_CATEGORY ");                                                        
												for (int i = 0; i < listCat.size(); i++) 
												{
													EmpCategory cat = (EmpCategory) listCat.get(i);
													cat_key.add(cat.getEmpCategory());
													cat_value.add(String.valueOf(cat.getOID()));
												}
												out.println(ControlCombo.draw(FrmSrcLeaveManagement.fieldNames[FrmSrcLeaveManagement.FRM_FIELD_CATEGORY],"formElemen",null, ""+srcLeaveManagement.getEmpCatId(), cat_value, cat_key, ""));
											*/	
											%>
                                          </td>
                                        </tr>
										-->
                                        <tr> 
                                          <td width="1%">&nbsp;</td>
                                          <td nowrap width="18%"><%=dictionaryD.getWord(I_Dictionary.DEPARTMENT) %></td>
                                          <td width="3%">:</td>
                                          <td width="78%"> 
                                            <% 
            Vector dept_value = new Vector(1, 1);
            Vector dept_key = new Vector(1, 1);
            //Vector listDept = new Vector(1, 1);
            DepartmentIDnNameList keyList= new DepartmentIDnNameList ();            
            if (processDependOnUserDept) {
                if (emplx.getOID() > 0) {
                    if (isHRDLogin || isEdpLogin || isGeneralManager) {
                        //dept_value.add("0");
                        //dept_key.add("select ...");
                        //listDept = PstDepartment.list(0, 0, "", "DEPARTMENT");
                        keyList= PstDepartment.genDepIDandNameWithCompanyDiv(0, 1000, "", true);
                    } else {
                        String whereClsDep="(DEPARTMENT_ID = " + departmentOid+" OR JOIN_TO_DEPARTMENT_ID='"+departmentOid+"')";
                        try {
                            String joinDept = PstSystemProperty.getValueByName("JOIN_DEPARMENT");
                            Vector depGroup = com.dimata.util.StringParser.parseGroup(joinDept);

                            int grpIdx = -1;
                            int maxGrp = depGroup == null ? 0 : depGroup.size();
                            int countIdx = 0;
                            int MAX_LOOP = 10;
                            int curr_loop = 0;
                            do { // find group department belonging to curretn user base in departmentOid
                                curr_loop++;
                                String[] grp = (String[]) depGroup.get(countIdx);
                                for (int g = 0; g < grp.length; g++) {
                                    String comp = grp[g];
                                    if(comp.trim().compareToIgnoreCase(""+departmentOid)==0){
                                      grpIdx = countIdx;   // A ha .. found here 
                                    }
                                }
                                countIdx++;
                            } while ((grpIdx < 0) && (countIdx < maxGrp) && (curr_loop<MAX_LOOP)); // if found then exit
                            
                            // compose where clause
                            if(grpIdx>=0){
                                String[] grp = (String[]) depGroup.get(grpIdx);
                                for (int g = 0; g < grp.length; g++) {
                                    String comp = grp[g];
                                    whereClsDep=whereClsDep+ " OR (DEPARTMENT_ID = " + comp+")"; 
                                }         
                               }                                                  
                        } catch (Exception exc) {
                            System.out.println(" Parsing Join Dept" + exc);
                        }

                        //listDept = PstDepartment.list(0, 0,whereClsDep, "");
                        keyList= PstDepartment.genDepIDandNameWithCompanyDiv(0, 1000, whereClsDep, true);
                    }
                } else {
                    //dept_value.add("0");
                    //dept_key.add("select ...");
                    //listDept = PstDepartment.list(0, 0, "", "DEPARTMENT");
                    keyList= PstDepartment.genDepIDandNameWithCompanyDiv(0, 1000, "", true);
                }
            } else {
                //dept_value.add("0");
                //dept_key.add("select ...");
                //listDept = PstDepartment.list(0, 0, "", "DEPARTMENT");
                keyList= PstDepartment.genDepIDandNameWithCompanyDiv(0, 1000, "", true);
            }
                                            
                                            

            //Vector dept_value = new Vector(1,1);
            //Vector dept_key = new Vector(1,1);

            //dept_key.add("ALL DEPARTMENT");
            //dept_value.add("0");

            //Vector listDept = PstDepartment.list(0, 0, "", "DEPARTMENT");
            //String selectDept = String.valueOf(searchSpecialQuery.get);
            /*for (int i = 0; i < listDept.size(); i++) 
            {
                    Department dept = (Department) listDept.get(i);
                    dept_key.add(dept.getDepartment());
                    dept_value.add(String.valueOf(dept.getOID()));
            }*/
            dept_value = keyList.getDepIDs();
            dept_key = keyList.getDepNames();                                      
            out.println(ControlCombo.drawStringArraySelected("department","formElemen",null, oidDept, dept_key, dept_value, null, "multiple"));
											%>
                                          </td>
                                        </tr>
                                        <!--
                                        <tr> 
                                          <td width="1%">&nbsp;</td>
                                          <td nowrap width="18%"><%=dictionaryD.getWord(I_Dictionary.SECTION) %></td>
                                          <td width="3%">:</td>
                                          <td width="78%"> 
                                            <% 
											/*
												Vector sec_value = new Vector(1,1);
												Vector sec_key = new Vector(1,1); 
												sec_value.add("0");
												sec_key.add("select ...");
												Vector listSec = PstSection.list(0, 0, "", " DEPARTMENT_ID, SECTION ");
												for (int i = 0; i < listSec.size(); i++) 
												{
													Section sec = (Section) listSec.get(i);
													sec_key.add(sec.getSection());
													sec_value.add(String.valueOf(sec.getOID()));
												}
												out.println(ControlCombo.draw(FrmSrcLeaveManagement.fieldNames[FrmSrcLeaveManagement.FRM_FIELD_SECTION],"formElemen",null, ""+srcLeaveManagement.getEmpSectionId(), sec_value, sec_key, ""));
											*/	
											%>
                                          </td>
                                        </tr>
										-->
                                        <!--
                                        <tr> 
                                          <td width="1%">&nbsp;</td>
                                          <td nowrap width="18%">Position</td>
                                          <td width="3%">:</td>
                                          <td width="78%"> 
                                            <% 
											/*
												Vector pos_value = new Vector(1,1);
												Vector pos_key = new Vector(1,1); 
												pos_value.add("0");
												pos_key.add("select ...");                                                       
												Vector listPos = PstPosition.list(0, 0, "", " POSITION ");                                                            
												for (int i = 0; i < listPos.size(); i++) 
												{
													Position pos = (Position) listPos.get(i);
													pos_key.add(pos.getPosition());
													pos_value.add(String.valueOf(pos.getOID()));
												}
												out.println(ControlCombo.draw(FrmSrcLeaveManagement.fieldNames[FrmSrcLeaveManagement.FRM_FIELD_POSITION],"formElemen",null, ""+srcLeaveManagement.getEmpPosId(), pos_value, pos_key, ""));
											*/	
											%>
                                          </td>
                                        </tr>
										-->
										<tr> 
                                          <td width="1%">&nbsp;</td>
                                          <td nowrap width="18%">Level</td>
                                          <td width="3%">:</td>
                                          <td width="78%"> 
                                            <% 
											
												Vector lev_value = new Vector(1,1);
												Vector lev_key = new Vector(1,1); 
												lev_value.add("0");
												lev_key.add("select ...");
												Vector listLev = PstLevel.list(0, 0, "", " LEVEL_ID, LEVEL ");
												for (int i = 0; i < listLev.size(); i++) 
												{
													Level lev = (Level) listLev.get(i);
													lev_key.add(lev.getLevel());
													lev_value.add(String.valueOf(lev.getOID()));
												}
												out.println(ControlCombo.draw("level","formElemen",null, ""+oidLevel, lev_value, lev_key, ""));
												
											%>
                                          </td>
                                        </tr>
                                        <tr> 
                                          <td width="1%">&nbsp;</td>
                                          <td nowrap width="18%">Payroll Group</td>
                                          <td width="3%">:</td>
                                          <td width="78%"> 
                                            <% 
											
												Vector pg_value = new Vector(1,1);
												Vector pg_key = new Vector(1,1); 
												pg_value.add("0");
												pg_key.add("select ...");
												Vector listPayGroup = PstPayrollGroup.list(0, 0, "", " PAYROLL_GROUP_NAME ");
												for (int i = 0; i < listPayGroup.size(); i++) 
												{
													PayrollGroup payrollGroup = (PayrollGroup) listPayGroup.get(i);
													pg_key.add(payrollGroup.getPayrollGroupName());
													pg_value.add(String.valueOf(payrollGroup.getOID()));
												}
												out.println(ControlCombo.drawStringArraySelected("payroll_group","formElemen",null, oidPayGroup, pg_key,pg_value,  null, "multiple"));
												
											%>
                                          </td>
                                        </tr>
                                        <tr> 
                                          <td width="1%">&nbsp;</td>
                                          <td nowrap width="18%">Payroll Group</td>
                                          <td width="3%">:</td>
                                          <td width="78%"> 
                                              <input type="text" name="date_from" id="date_from" value="<%=dateFrom%>" class="datepicker" /> <strong>To</strong> <input type="text" name="date_to" id="date_to" value="<%=dateTo%>" class="datepicker" />
                                          </td>
                                        </tr>
                                        <tr> 
                                          <td width="1%">&nbsp;</td>
                                          <td width="18%" nowrap> 
                                            <div align="left"></div>
                                          </td>
                                          <td width="3%">&nbsp;</td>
                                          <td width="78%"> 
                                            <table border="0" cellspacing="0" cellpadding="0" width="197">
                                              <tr> 
                                                <td width="24"><a href="javascript:cmdView()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image10','','<%=approot%>/images/BtnSearchOn.jpg',1)" id="aSearch"><img name="Image10" border="0" src="<%=approot%>/images/BtnSearch.jpg" width="24" height="24" alt="View Report"></a></td>
                                                <td width="10"><img src="<%=approot%>/images/spacer.gif" width="4" height="1"></td>
                                                <td width="163" class="command" nowrap><a href="javascript:cmdView()">View Report</a></td>
                                              </tr>
                                            </table>
                                          </td>
                                        </tr>
                                      </table>									  
									  
									  <% 
									  if(iCommand == Command.LIST)
									  {
									  %>
									  <table width="100%" border="0" cellspacing="2" cellpadding="2">
                                        <tr>
                                          <td><hr></td>
                                        </tr>
                                        <tr>
                                          <td><%=drawList(out, listEmployee,dateFrom, dateTo)%></td>
                                        </tr>
                                        <tr>
                                            
                                        </tr>
                                        <%
										if(listEmployee != null && listEmployee.size() > 0 && privPrint)
										{
										%>
                                        <tr>
                                          <td class="command">
                                            <table border="0" cellspacing="0" cellpadding="0" width="197">
                                              <tr>
                                                <td width="24"><a href="javascript:cmdPrintXls()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image110','','<%=approot%>/images/BtnNewOn.jpg',1)" id="aSearch"><img name="Image110" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="Print Report"></a></td>
                                                <td width="10"><img src="<%=approot%>/images/spacer.gif" width="4" height="1"></td>
                                                <td width="163" class="command" nowrap><a href="javascript:cmdPrintXls()">Print Report Xls</a></td>
                                              </tr>
                                            </table></td>
                                        </tr>
                                        <%
										}
										%>
                                      </table>
									  <%
									  }
									  %>									  
                                    </form>
                                    <!-- #EndEditable --> </td>
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
  <%if(headerStyle && !verTemplate.equalsIgnoreCase("0")){%>
            <tr>
                            <td valign="bottom">
                               <!-- untuk footer -->
                                <%@include file="../../footer.jsp" %>
                            </td>
                            
            </tr>
            <%}else{%>
            <tr> 
                <td colspan="2" height="20" <%=bgFooterLama%>> <!-- #BeginEditable "footer" --> 
      <%@ include file = "../../main/footer.jsp" %>
                <!-- #EndEditable --> </td>
            </tr>
            <%}%>
</table>
</body>
<!-- #BeginEditable "script" --> 
<!-- #EndEditable --> <!-- #EndTemplate -->
</html>
