<%--
    Document   : srcovertime
    Created on : Nov 12, 2011, 5:56:23 PM
    Author     : Wiweka
--%>

<%@ page language = "java" %>
<!-- package java -->
<%@ page import = "java.util.*" %>

<!-- package wihita -->
<%@ page import = "com.dimata.util.*" %>

<!-- package qdep -->
<%@ page import = "com.dimata.gui.jsp.*" %>
<%@ page import = "com.dimata.util.*" %>
<%@ page import = "com.dimata.qdep.form.*" %>
<%@ page import = "com.dimata.qdep.entity.*" %>

<!--package harisma -->
<%@ page import = "com.dimata.harisma.entity.search.*" %>
<%@ page import = "com.dimata.harisma.form.search.*" %>
<%@ page import = "com.dimata.harisma.entity.employee.*" %>
<%@ page import = "com.dimata.harisma.entity.masterdata.*" %>
<%@ page import = "com.dimata.harisma.entity.admin.*" %>
<%@ page import = "com.dimata.harisma.entity.payroll.*" %>
<%@ page import = "com.dimata.harisma.entity.overtime.*" %>
<%@ page import = "com.dimata.harisma.session.payroll.*" %>

<%@ include file = "../../main/javainit.jsp" %>
<% int appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_PAYROLL, AppObjInfo.G2_PAYROLL_OVERTIME, AppObjInfo.OBJ_PAYROLL_OVERTIME_FORM);%>
<%@ include file = "../../main/checkuser.jsp" %>

<% 
// Check privilege except VIEW, view is already checked on checkuser.jsp as basic access
//boolean privAdd = userSession.checkPrivilege(AppObjInfo.composeCode(appObjCode, AppObjInfo.COMMAND_ADD));
%>

<%
            long oidDivision = FRMQueryString.requestLong(request, FrmSrcOvertime.fieldNames[FrmSrcOvertime.FRM_FIELD_DIVISION_ID]);
            long oidDepartment = FRMQueryString.requestLong(request, FrmSrcOvertime.fieldNames[FrmSrcOvertime.FRM_FIELD_DEPARTMENT_ID]);
            //UPDATE BY SATRYA 2013-08-13
            //long oidDivision = FRMQueryString.requestLong(request, FrmSrcOvertime.fieldNames[FrmSrcOvertime.FRM_FIELD_DIVISION_ID]);
            int iCommand = FRMQueryString.requestCommand(request);
            SrcOvertime srcOvertime = new SrcOvertime();
            FrmSrcOvertime frmSrcOvertime = new FrmSrcOvertime();

            if (iCommand == Command.GOTO) {
                frmSrcOvertime = new FrmSrcOvertime(request, srcOvertime);
                frmSrcOvertime.requestEntityObject(srcOvertime);
            }

            if (iCommand == Command.BACK) {
                frmSrcOvertime = new FrmSrcOvertime(request, srcOvertime);
                try {
                    srcOvertime = (SrcOvertime) session.getValue(SessOvertime.SESS_SRC_OVERTIME);
                    if (srcOvertime == null) {
                        srcOvertime = new SrcOvertime();
                    }
                    System.out.println("ecccccc " + srcOvertime.getOrderBy());
                } catch (Exception e) {
                    srcOvertime = new SrcOvertime();
                }
            }

            boolean isSecretaryLogin = (positionType >= PstPosition.LEVEL_SECRETARY) ? true : false;
            long hrdDepartmentOid = Long.parseLong(String.valueOf(PstSystemProperty.getPropertyLongbyName(OID_HRD_DEPARTMENT)));
            boolean isHRDLogin = hrdDepartmentOid == departmentOid ? true : false;
            long edpSectionOid = Long.parseLong(String.valueOf(PstSystemProperty.getPropertyLongbyName(OID_EDP_SECTION)));
            boolean isEdpLogin = edpSectionOid == sectionOfLoginUser.getOID() ? true : false;
            boolean isGeneralManager = positionType == PstPosition.LEVEL_GENERAL_MANAGER ? true : false;
                                   
%>
<html><!-- #BeginTemplate "/Templates/main.dwt" -->
    <head>
        <!-- #BeginEditable "doctitle" -->
        <title>HARISMA - Search Employee</title>
        <script language="JavaScript">

            function cmdUpdateDiv(){
                document.frmsrcovertime.command.value="<%=String.valueOf(Command.GOTO)%>";
                document.frmsrcovertime.action="src_overtime.jsp";
                document.frmsrcovertime.submit();
            }
            function cmdUpdateDep(){
                document.frmsrcovertime.command.value="<%=String.valueOf(Command.GOTO)%>";
                document.frmsrcovertime.action="src_overtime.jsp";
                document.frmsrcovertime.submit();
            }
            function cmdUpdatePos(){
                document.frmsrcovertime.command.value="<%=String.valueOf(Command.GOTO)%>";
                document.frmsrcovertime.action="src_overtime.jsp";
                document.frmsrcovertime.submit();
            }


            function cmdAdd(){
                document.frmsrcovertime.command.value="<%=String.valueOf(Command.ADD)%>";
                document.frmsrcovertime.action="overtime.jsp";
                document.frmsrcovertime.submit();
            }

            function cmdSearch(){
                document.frmsrcovertime.command.value="<%=String.valueOf(Command.LIST)%>";
                document.frmsrcovertime.action="overtime_list.jsp";
                document.frmsrcovertime.submit();
            }

            function cmdHeadCount(){
                document.frmsrcovertime.command.value="<%=String.valueOf(Command.LIST)%>";
                document.frmsrcovertime.action="employee_headcount.jsp";
                document.frmsrcovertime.submit();
            }

            function cmdSpecialQuery(){
                document.frmsrcovertime.action="specialquery.jsp";
                document.frmsrcovertime.submit();
            }

            function fnTrapKD(){
                if (event.keyCode == 13) {
                    document.all.aSearch.focus();
                    cmdSearch();
                }
            }

            function MM_swapImgRestore() { //v3.0
                var i,x,a=document.MM_sr; for(i=0;a && i < a.length && (x=a[i]) && x.oSrc;i++) x.src=x.oSrc;
            }

            function MM_preloadImages() { //v3.0
                var d=document; if(d.images){ if(!d.MM_p) d.MM_p=new Array();
                    var i,j=d.MM_p.length,a=MM_preloadImages.arguments; for(i=0; i < a.length; i++)
                        if (a[i].indexOf("#")!=0){ d.MM_p[j]=new Image; d.MM_p[j++].src=a[i];}}
                }

                function MM_findObj(n, d) { //v4.0
                    var p,i,x;  if(!d) d=document; if((p=n.indexOf("?"))>0 && parent.frames.length) {
                        d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}
                    if(!(x=d[n]) && d.all) x=d.all[n]; for (i=0;!x && i < d.forms.length;i++) x=d.forms[i][n];
                    for(i=0;!x && d.layers && i < d.layers.length;i++) x=MM_findObj(n,d.layers[i].document);
                    if(!x && document.getElementById) x=document.getElementById(n); return x;
                }

                function MM_swapImage() { //v3.0
                    var i,j=0,x,a=MM_swapImage.arguments; document.MM_sr=new Array; for(i=0;i<(a.length-2);i+=3)
                        if ((x=MM_findObj(a[i]))!=null){document.MM_sr[j++]=x; if(!x.oSrc) x.oSrc=x.src; x.src=a[i+2];}
                }
        </script>
        <!-- #EndEditable -->
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
        <!-- #BeginEditable "styles" -->
        <link rel="stylesheet" href="../../styles/main.css" type="text/css">
        <!-- #EndEditable -->
        <!-- #BeginEditable "stylestab" -->
        <link rel="stylesheet" href="../../styles/tab.css" type="text/css">
        <!-- #EndEditable -->
        <!-- #BeginEditable "headerscript" -->
        <!-- #EndEditable -->
    </head>
  
    <body <%=noBack%> bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" onLoad="MM_preloadImages('<%=approot%>/images/BtnSearchOn.jpg','<%=approot%>/images/BtnNewOn.jpg')">
        <table width="100%" border="0" cellspacing="0" cellpadding="0" height="100%" bgcolor="#F9FCFF" >
             <%if(headerStyle && !verTemplate.equalsIgnoreCase("0")){%> 
           <%@include file="../../styletemplate/template_header.jsp" %>
            <%}else{%>
            <tr>
                <td ID="TOPTITLE" background="<%=approot%>/images/HRIS_HeaderBg3.jpg" width="100%" height="54">
                    <!-- #BeginEditable "header" -->
                    <%@ include file = "../../main/header.jsp" %>
                    <!-- #EndEditable -->
                </td>
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
                                        <td height="20">
                                            <font color="#FF6600" face="Arial"><strong>
                                                    <!-- #BeginEditable "contenttitle" -->
                                                    Overtime &gt; Overtime Search<!-- #EndEditable -->
                                                </strong></font>
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
                                                                            <td valign="top">
                                                                                <!-- #BeginEditable "content" -->
                                                                                <form name="frmsrcovertime" method="post" action="">
                                                                                    <input type="hidden" name="command" value="<%=iCommand%>">
                                                                                    <table width="100%" border="0" cellspacing="0" cellpadding="0">
                                                                                        <tr align="left" valign="top">
                                                                                            <td valign="middle" colspan="2">
                                                                                                <table width="100%" border="0" cellspacing="0" cellpadding="0" >
                                                                                                    <tr>
                                                                                                        <td width="3%">&nbsp;</td>
                                                                                                        <td width="97%">&nbsp;</td>
                                                                                                        <td width="0%">&nbsp;</td>
                                                                                                    </tr>
                                                                                                    <tr>
                                                                                                        <td width="3%">&nbsp;</td>
                                                                                                        <td width="97%">
<table border="0" cellspacing="2" cellpadding="2" width="72%">
	<tr align="left" valign="top">
		<td width="17%" nowrap>
			<div align="left">Overtime Date </div></td>
		<td width="83%"> from <%=ControlDate.drawDateWithStyle(frmSrcOvertime.fieldNames[FrmSrcOvertime.FRM_FIELD_REQ_DATE], srcOvertime.getRequestDate(), 2, -5, "formElemen", " onkeydown=\"javascript:fnTrapKD()\"")%> 
                    to <%=ControlDate.drawDateWithStyle(frmSrcOvertime.fieldNames[FrmSrcOvertime.FRM_FIELD_REQ_DATE_TO], srcOvertime.getRequestDateTo(), 2, -5, "formElemen", " onkeydown=\"javascript:fnTrapKD()\"")%>
                </td>
	</tr>
	<tr align="left" valign="top">
		<td width="17%" nowrap>
			<div align="left">No. Overtime</div></td>
		<td width="83%"><input type="text" name="<%=frmSrcOvertime.fieldNames[FrmSrcOvertime.FRM_FIELD_OV_NUMBER]%>"  value="<%= srcOvertime.getOvertimeNum()%>" class="elemenForm" size="40" onKeyDown="javascript:fnTrapKD()"></td>
	</tr>
	<tr align="left" valign="top">
		<td width="17%" nowrap>
			<div align="left">Company</div></td>
		<td width="83%"> 
<%
					Vector comp_value = new Vector(1, 1);
					Vector comp_key = new Vector(1, 1);  
String whereComp="";   
/*if(srcOvertime!=null && srcOvertime.getCompanyId()!=0){
    whereComp = PstCompany.fieldNames[PstCompany.FLD_COMPANY_ID] +"="+srcOvertime.getCompanyId();
}*/   
    Vector div_value = new Vector(1, 1);
    Vector div_key = new Vector(1, 1);      
    
    Vector dept_value = new Vector(1, 1);
    Vector dept_key = new Vector(1, 1);  

    Vector sec_value = new Vector(1,1);
    Vector sec_key = new Vector(1,1);
    
    sec_value.add("0");
    sec_key.add("select ...");
 if (processDependOnUserDept) {
        if (emplx.getOID() > 0) {
            if (isHRDLogin || isEdpLogin || isGeneralManager) {
                //keyList = PstDepartment.genDepIDandNameWithCompanyDiv(0, 1000, "", true);
                   comp_value.add("0");
                   comp_key.add("select ...");
                   
                    div_value.add("0");
                   div_key.add("select ...");
                   
                    dept_value.add("0");
                   dept_key.add("select ...");
            } else {
                Position position = null;
                try {
                    position = PstPosition.fetchExc(emplx.getPositionId());
                } catch (Exception exc) {
                }
                if (position != null & position.getDisabedAppDivisionScope() == 0 & position.getPositionLevel() >= PstPosition.LEVEL_MANAGER) {
                    String whereDiv = " d." + PstDepartment.fieldNames[PstDepartment.FLD_DIVISION_ID] + "=" + emplx.getDivisionId() + "";
                    //keyList = PstDepartment.genDepIDandNameWithCompanyDiv(0, 1000, whereDiv, true);
                    // comp_value.add("0");
                    // comp_key.add("select ...");
                     
                       //div_value.add("0");
                       //div_key.add("select ...");
                   
                       dept_value.add("0");
                       dept_key.add("select ...");
                    
                     whereComp = whereComp!=null && whereComp.length()>0 ? whereComp + " AND ("+  whereDiv +")": whereDiv;
                    
                } else {

                    String whereClsDep = "(d." + PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT_ID] + " = " + departmentOid
                            + ") OR (d." + PstDepartment.fieldNames[PstDepartment.FLD_JOIN_TO_DEPARTMENT_ID] + " = " + departmentOid + ") ";
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
                                if (comp.trim().compareToIgnoreCase("" + departmentOid) == 0) {
                                    grpIdx = countIdx;   // A ha .. found here 
                                }
                            }
                            countIdx++;
                        } while ((grpIdx < 0) && (countIdx < maxGrp) && (curr_loop < MAX_LOOP)); // if found then exit

                        // compose where clause
                        if (grpIdx >= 0) {
                            String[] grp = (String[]) depGroup.get(grpIdx);
                            for (int g = 0; g < grp.length; g++) {
                                String comp = grp[g];
                                whereClsDep = whereClsDep + " OR (d.DEPARTMENT_ID = " + comp + ")";
                            }
                        }
                    } catch (Exception exc) {
                            System.out.println(" Parsing Join Dept" + exc);

                    }
                    //keyList = PstDepartment.genDepIDandNameWithCompanyDiv(0, 1000, whereClsDep, false);
                    
                     whereComp = whereComp!=null && whereComp.length()>0 ? whereComp + " AND ("+ whereClsDep +")":whereClsDep;
                     
                }
            }
        }
 }else{
    comp_value.add("0");
    comp_key.add("select ...");
    
     div_value.add("0");
    div_key.add("select ...");

    dept_value.add("0");
    dept_key.add("select ...");
 }               
    Vector listCostDept = PstDepartment.listWithCompanyDivSection(0, 0, whereComp);
    String prevCompany="";
    String prevDivision="";
    


long prevCompanyTmp=0;   
//boolean deptMatchWithDpt=false;    
Hashtable prevDeptSame= new Hashtable();
Hashtable sampeDivWithDept = new Hashtable();
    for (int i = 0; i < listCostDept.size(); i++) {
        Department dept = (Department) listCostDept.get(i);
       // deptMatchWithDpt=false;
        if (prevCompany.equals(dept.getCompany())) {
            if (prevDivision.equals(dept.getDivision()) && (sampeDivWithDept.containsKey(""+dept.getDivisionId()))) {
                //if(srcOvertime!=null && srcOvertime.getCompanyId()!=0){
                //update by satrya 2013-09-20
                if((prevDeptSame.get(""+dept.getOID())==null) || (prevDeptSame.containsKey(""+dept.getOID())==false) ){
                    dept_key.add(dept.getDepartment());
                    dept_value.add(String.valueOf(dept.getOID()));
                    prevDeptSame.put(""+dept.getOID(),dept.getOID()); 
                    sampeDivWithDept.put(""+dept.getDivisionId(), dept.getOID());
                }
                //}
                    
                       //update by satrya 2013-09-20
                     if(dept.getSection()!=null){
                       sec_key.add(dept.getSection());
                       sec_value.add(""+dept.getSectionId());
                     }
                //update by satrya 2013-09-20
                prevDivision = dept.getDivision();
            } 
            else {
              if(!prevDivision.equalsIgnoreCase(dept.getDivision())){
                div_key.add(dept.getDivision());
                div_value.add(""+dept.getDivisionId());
              }
                if(dept_key!=null && dept_key.size()==0){
                        //update by satrya 2013-09-20
                   if((prevDeptSame.get(""+dept.getOID())==null) || (prevDeptSame.containsKey(""+dept.getOID())==false) ){
                       dept_key.add(dept.getDepartment());
                       dept_value.add(String.valueOf(dept.getOID()));
                       prevDeptSame.put(""+dept.getOID(),dept.getOID()); 
                       
                       sampeDivWithDept.put(""+dept.getDivisionId(), dept.getOID());
                   }
                }
                prevDivision = dept.getDivision();
            }
        } else {
            String chkAdaDiv="";
            if(div_key!=null && div_key.size()>0){
                chkAdaDiv = (String)div_key.get(0);
            }
            if((div_key!=null && div_key.size()==0 ) || ( chkAdaDiv.equalsIgnoreCase("select ..."))){ 
             if(prevCompanyTmp!=dept.getCompanyId()){
                    comp_key.add(dept.getCompany());
                    comp_value.add(""+dept.getCompanyId());
                    prevCompanyTmp=dept.getCompanyId();
              }
             //untuk karyawan admin yg hanya bisa akses departement tertentu (ketika di awal)
             ////update
if (processDependOnUserDept) {
        if (emplx.getOID() > 0) {
            if (isHRDLogin || isEdpLogin || isGeneralManager) {
                if(srcOvertime!=null && srcOvertime.getCompanyId()!=0){  
                    div_key.add(dept.getDivision());
                    div_value.add(""+dept.getDivisionId()); 

                     //update by satrya 2013-09-20
                if((prevDeptSame.get(""+dept.getOID())==null) || (prevDeptSame.containsKey(""+dept.getOID())==false) ){
                    dept_key.add(dept.getDepartment());
                    dept_value.add(String.valueOf(dept.getOID()));
                    prevDeptSame.put(""+dept.getOID(), dept.getOID()); 
                    sampeDivWithDept.put(""+dept.getDivisionId(), dept.getOID());
                }
                    
                    
                       //update by satrya 2013-09-20
                     if(dept.getSection()!=null){
                       sec_key.add(dept.getSection());
                       sec_value.add(""+dept.getSectionId());
                     }
                    prevCompany = dept.getCompany();
                    prevDivision = dept.getDivision();             
                }
            } else {
                Position position = null;
                try {
                    position = PstPosition.fetchExc(emplx.getPositionId());
                } catch (Exception exc) {
                }
                if (position != null & position.getDisabedAppDivisionScope() == 0 & position.getPositionLevel() >= PstPosition.LEVEL_MANAGER) { 
                    if(srcOvertime!=null && srcOvertime.getCompanyId()!=0){
                       div_key.add(dept.getDivision());
                       div_value.add(""+dept.getDivisionId()); 

                     //update by satrya 2013-09-20
                if((prevDeptSame.get(""+dept.getOID())==null) || (prevDeptSame.containsKey(""+dept.getOID())==false) ){
                    dept_key.add(dept.getDepartment());
                    dept_value.add(String.valueOf(dept.getOID()));
                    prevDeptSame.put(""+dept.getOID(), dept.getOID()); 
                   sampeDivWithDept.put(""+dept.getDivisionId(), dept.getOID());
                }
                      
                       //update by satrya 2013-09-20
                     if(dept.getSection()!=null){
                       sec_key.add(dept.getSection());
                       sec_value.add(""+dept.getSectionId());
                     }
                       prevCompany = dept.getCompany();
                       prevDivision = dept.getDivision();             
                   }
                    //update by satrya 2013-09-19
                    else if((div_key!=null && div_key.size()==0 ) || ( chkAdaDiv.equalsIgnoreCase("select ..."))){
                        div_key.add(dept.getDivision());
                        div_value.add(""+dept.getDivisionId()); 
                        
                  //update by satrya 2013-09-20
                if((prevDeptSame.get(""+dept.getOID())==null) || (prevDeptSame.containsKey(""+dept.getOID())==false) ){ 
                    dept_key.add(dept.getDepartment());
                    dept_value.add(String.valueOf(dept.getOID()));
                    prevDeptSame.put(""+dept.getOID(), dept.getOID()); 
                   sampeDivWithDept.put(""+dept.getDivisionId(), dept.getOID());
                }
                       
                       
                       //update by satrya 2013-09-20
                     if(dept.getSection()!=null){
                       sec_key.add(dept.getSection());
                       sec_value.add(""+dept.getSectionId());
                     }
                       
                       prevCompany = dept.getCompany();
                       prevDivision = dept.getDivision(); 
                   }
                   
                }else{
                    
                     div_key.add(dept.getDivision());
                       div_value.add(""+dept.getDivisionId()); 

                        //update by satrya 2013-09-20
                if((prevDeptSame.get(""+dept.getOID())==null) || (prevDeptSame.containsKey(""+dept.getOID())==false) ){
                    dept_key.add(dept.getDepartment());
                    dept_value.add(String.valueOf(dept.getOID()));
                    prevDeptSame.put(""+dept.getOID(), dept.getOID()); 
                    sampeDivWithDept.put(""+dept.getDivisionId(), dept.getOID());
                }
                       
                       
                       //update by satrya 2013-09-20
                     if(dept.getSection()!=null){
                       sec_key.add(dept.getSection());
                       sec_value.add(""+dept.getSectionId());
                     }
                       prevCompany = dept.getCompany();
                       prevDivision = dept.getDivision(); 
                }
            }
        }
 }else{
      if(srcOvertime!=null && srcOvertime.getCompanyId()!=0){
                    div_key.add(dept.getDivision());
                    div_value.add(""+dept.getDivisionId()); 

                  //update by satrya 2013-09-20
                if((prevDeptSame.get(""+dept.getOID())==null) || (prevDeptSame.containsKey(""+dept.getOID())==false) ){
                    dept_key.add(dept.getDepartment());
                    dept_value.add(String.valueOf(dept.getOID()));
                    prevDeptSame.put(""+dept.getOID(), dept.getOID()); 
                    sampeDivWithDept.put(""+dept.getDivisionId(), dept.getOID());
                }
                    
                       //update by satrya 2013-09-20
                     if(dept.getSection()!=null){
                       sec_key.add(dept.getSection());
                       sec_value.add(""+dept.getSectionId());
                     }
                    prevCompany = dept.getCompany();
                    prevDivision = dept.getDivision();             
       }
 }
             ///batas lama
                /*if(srcOvertime!=null && srcOvertime.getCompanyId()!=0){
                    div_key.add(dept.getDivision());
                    div_value.add(""+dept.getDivisionId()); 

                    dept_key.add(dept.getDepartment());
                    dept_value.add(String.valueOf(dept.getOID()));
                    prevCompany = dept.getCompany();
                    prevDivision = dept.getDivision();             
                }*/
             //batas lama
             
            }
           else{
              if(prevCompanyTmp!=dept.getCompanyId()){
                    comp_key.add(dept.getCompany());
                    comp_value.add(""+dept.getCompanyId());
                    prevCompanyTmp=dept.getCompanyId();
              }
              
            }
           
        }
    }  
			%> <%= ControlCombo.draw(frmSrcOvertime.fieldNames[FrmSrcOvertime.FRM_FIELD_COMPANY_ID], "formElemen", null, "" + srcOvertime.getCompanyId(), comp_value, comp_key, "onChange=\"javascript:cmdUpdateDiv()\"")%> </td>
	</tr>
	<tr align="left" valign="top">
		<td width="17%" nowrap>
			<div align="left"><%=dictionaryD.getWord(I_Dictionary.DIVISION) %></div></td>
		<td width="83%"> 
<%
					
                                   //update by satrya 2013-08-13
                                   //jika user memilih select kembali
                                   if(srcOvertime!=null && srcOvertime.getCompanyId()==0){  
                                       srcOvertime.setDivisionId(0); 
                                   }

if(srcOvertime!=null && srcOvertime.getCompanyId()!=0){
    whereComp = "("+(whereComp!=null && whereComp.length()==0?"1=1":whereComp) + ") AND " + PstCompany.fieldNames[PstCompany.FLD_COMPANY_ID] +"="+srcOvertime.getCompanyId();
 listCostDept = PstDepartment.listWithCompanyDiv(0, 0, whereComp);
    prevCompany="";
    prevDivision="";
    
    div_value = new Vector(1, 1);
    div_key = new Vector(1, 1);      
    
    dept_value = new Vector(1, 1);
    dept_key = new Vector(1, 1); 

    prevCompanyTmp=0;  
long tmpFirstDiv=0;   

if (processDependOnUserDept) {
        if (emplx.getOID() > 0) {
            if (isHRDLogin || isEdpLogin || isGeneralManager) {
                //keyList = PstDepartment.genDepIDandNameWithCompanyDiv(0, 1000, "", true);
                   comp_value.add("0");
                   comp_key.add("select ...");
                   
                   div_value.add("0");
                   div_key.add("select ...");
                   
                   dept_value.add("0");
                   dept_key.add("select ...");
            } else {
                Position position = null;
                try {
                    position = PstPosition.fetchExc(emplx.getPositionId());
                } catch (Exception exc) {
                }
                if (position != null & position.getDisabedAppDivisionScope() == 0 & position.getPositionLevel() >= PstPosition.LEVEL_MANAGER) { 
                       //div_value.add("0");
                       //div_key.add("select ...");
                   
                       dept_value.add("0");
                       dept_key.add("select ...");
                    
                } 
            }
        }
 }else{
    comp_value.add("0");
    comp_key.add("select ...");
    
    div_value.add("0");
    div_key.add("select ...");

    dept_value.add("0");
    dept_key.add("select ...");
 }
   long prevDivTmp=0;
    for (int i = 0; i < listCostDept.size(); i++) {
        Department dept = (Department) listCostDept.get(i);
        if (prevCompany.equals(dept.getCompany())) {
            if (prevDivision.equals(dept.getDivision())) {
                //update
                if(srcOvertime!=null && srcOvertime.getDivisionId()!=0){
                    dept_key.add(dept.getDepartment());
                    dept_value.add(String.valueOf(dept.getOID()));
                }
                //lama
                /*
                dept_key.add(dept.getDepartment());
                dept_value.add(String.valueOf(dept.getOID()));
                */
                
            } 
            else {
                div_key.add(dept.getDivision());
                div_value.add(""+dept.getDivisionId());
                if(dept_key!=null && dept_key.size()==0){
                    dept_key.add(dept.getDepartment());
                    dept_value.add(String.valueOf(dept.getOID())); 
                }
                prevDivision = dept.getDivision();
            }
        } else {
           String chkAdaDiv="";
            if(div_key!=null && div_key.size()>0){
                chkAdaDiv = (String)div_key.get(0);
            }
            if((div_key!=null && div_key.size()==0 ) || ( chkAdaDiv.equalsIgnoreCase("select ..."))){ 
             //comp_key.add(dept.getCompany());
             //comp_value.add(""+dept.getCompanyId());
             
            
             
             if(prevDivTmp!=dept.getDivisionId()){
                    div_key.add(dept.getDivision());
                    div_value.add(""+dept.getDivisionId()); 
                    prevDivTmp=dept.getDivisionId();
              }
             
                    tmpFirstDiv=dept.getDivisionId(); 

                   // dept_key.add(dept.getDepartment());
                 //   dept_value.add(String.valueOf(dept.getOID()));           
               
            prevCompany = dept.getCompany();
            prevDivision = dept.getDivision();             
            }
           /*else{
              if(prevCompanyTmp!=dept.getCompanyId()){
                    comp_key.add(dept.getCompany());
                    comp_value.add(""+dept.getCompanyId());
                    prevCompanyTmp=dept.getCompanyId();
              }
              
            }*/
          String chkAdaDpt="";
          if(whereComp!=null && whereComp.length()>0){
                chkAdaDpt = "("+(whereComp!=null && whereComp.length()==0?"1=1":whereComp)+ ") AND d." + PstDivision.fieldNames[PstDivision.FLD_DIVISION_ID] +"="+srcOvertime.getDivisionId();
          }
            Vector listCheckAdaDept = PstDepartment.listWithCompanyDiv(0, 0, chkAdaDpt);
            if((listCheckAdaDept==null || listCheckAdaDept.size()==0)){
                
if (processDependOnUserDept) {
        if (emplx.getOID() > 0) {
            if (isHRDLogin || isEdpLogin || isGeneralManager) {
                
            } else {
                Position position = null;
                try {
                    position = PstPosition.fetchExc(emplx.getPositionId());
                } catch (Exception exc) {
                }
               /* if (position != null & position.getDisabedAppDivisionScope() == 0 & position.getPositionLevel() >= PstPosition.LEVEL_MANAGER) { 
                      
                }else{
                     srcOvertime.setDivisionId(tmpFirstDiv);
                }*/ 
                srcOvertime.setDivisionId(tmpFirstDiv);
            }
        }
 }else{
     srcOvertime.setDivisionId(tmpFirstDiv);
 }
               
            }
        }
    }
}
			%> <%= ControlCombo.draw(frmSrcOvertime.fieldNames[FrmSrcOvertime.FRM_FIELD_DIVISION_ID], "formElemen", null,""+srcOvertime.getDivisionId() , div_value, div_key, "onChange=\"javascript:cmdUpdateDep()\"")%> </td>
	</tr>
	<tr align="left" valign="top">
		<td width="17%" nowrap>
			<div align="left"><%=dictionaryD.getWord(I_Dictionary.DEPARTMENT) %></div></td>
		<td width="83%"> 
<%

            //update by satrya 2013-08-13
            //jika user memilih select kembali
            if(srcOvertime!=null && srcOvertime.getDepartmentId()==0){   
                srcOvertime.setSectionId(0);  
            }
if(srcOvertime!=null && srcOvertime.getDivisionId()!=0){
    if(whereComp!=null && whereComp.length()>0){
        whereComp = "("+whereComp + ") AND d." + PstDivision.fieldNames[PstDivision.FLD_DIVISION_ID] +"="+srcOvertime.getDivisionId();
    }
    
    listCostDept = PstDepartment.listWithCompanyDiv(0, 0, whereComp);
    //update by satrya 2013-09-20
    //prevCompany="";
    //prevDivision="";
    
    //div_value = new Vector(1, 1);
    //div_key = new Vector(1, 1);      
    
    dept_value = new Vector(1, 1);
    dept_key = new Vector(1, 1); 

    prevCompanyTmp=0; 

if (processDependOnUserDept) {
        if (emplx.getOID() > 0) {
            if (isHRDLogin || isEdpLogin || isGeneralManager) {
                //keyList = PstDepartment.genDepIDandNameWithCompanyDiv(0, 1000, "", true);
                   comp_value.add("0");
                   comp_key.add("select ...");
                   
                   div_value.add("0");
                   div_key.add("select ...");
                   
                   dept_value.add("0");
                   dept_key.add("select ...");
            } else {
                Position position = null;
                try {
                    position = PstPosition.fetchExc(emplx.getPositionId());
                } catch (Exception exc) {
                }
                if (position != null & position.getDisabedAppDivisionScope() == 0 & position.getPositionLevel() >= PstPosition.LEVEL_MANAGER) { 
                       //div_value.add("0");
                       //div_key.add("select ...");
                   
                       dept_value.add("0");
                       dept_key.add("select ...");
                    
                } 
            }
        }
 }else{
    comp_value.add("0");
    comp_key.add("select ...");
    
    div_value.add("0");
    div_key.add("select ...");

    dept_value.add("0");
    dept_key.add("select ...");
 }
                
    for (int i = 0; i < listCostDept.size(); i++) {
        Department dept = (Department) listCostDept.get(i);
        if (prevCompany.equals(dept.getCompany())) {
            if (prevDivision.equals(dept.getDivision())) {
                dept_key.add(dept.getDepartment());
                dept_value.add(String.valueOf(dept.getOID()));
            } 
            else {
                div_key.add(dept.getDivision());
                div_value.add(""+dept.getDivisionId());
                //update by satrya 2013-09-20
                if((dept_key!=null && dept_key.size()==0) ||(listCostDept.size()==1)){
                    dept_key.add(dept.getDepartment());
                    dept_value.add(String.valueOf(dept.getOID())); 
                }
                prevDivision = dept.getDivision();
            }
        } else {
            String chkAdaDiv="";
            if(div_key!=null && div_key.size()>0){
                chkAdaDiv = (String)div_key.get(0);
            }
            if((div_key!=null && div_key.size()==0 ) || ( chkAdaDiv.equalsIgnoreCase("select ..."))){ 
             comp_key.add(dept.getCompany());
             comp_value.add(""+dept.getCompanyId());
             
             
             div_key.add(dept.getDivision());
             div_value.add(""+dept.getDivisionId()); 
              
             dept_key.add(dept.getDepartment());
             dept_value.add(String.valueOf(dept.getOID()));
            prevCompany = dept.getCompany();
            prevDivision = dept.getDivision();             
            }else{
              if(prevCompanyTmp!=dept.getCompanyId()){
                    comp_key.add(dept.getCompany());
                    comp_value.add(""+dept.getCompanyId());
                    //update by satrya 2013-09-19
                    //jika dept masih kosong
                    //update by satrya 2013-09-19
                     String chkAdaDept="";
                    if(dept_key!=null && dept_key.size()>0){
                        chkAdaDept = (String)dept_key.get(0);
                    }
                    if((dept_key!=null && dept_key.size()==0 ) || (chkAdaDept.equalsIgnoreCase("select ..."))){  
                         dept_key.add(dept.getDepartment());
                         dept_value.add(String.valueOf(dept.getOID()));
                         prevCompany =dept.getCompany(); 
                    }
                    prevCompanyTmp=dept.getCompanyId();
              }
              
            }
           
        }
    }
}
			%> <%= ControlCombo.draw(frmSrcOvertime.fieldNames[FrmSrcOvertime.FRM_FIELD_DEPARTMENT_ID], "formElemen", null, "" + srcOvertime.getDepartmentId(), dept_value, dept_key, "onChange=\"javascript:cmdUpdatePos()\"")%> </td>
	</tr>
	<tr align="left" valign="top">
		<td width="17%" nowrap>
			<div align="left"><%=dictionaryD.getWord(I_Dictionary.SECTION) %></div></td>
		<td width="83%"> <%
                
/*Vector sec_value = new Vector(1, 1);
Vector sec_key = new Vector(1, 1);
if (processDependOnUserDept) {
        if (emplx.getOID() > 0) {
            if (isHRDLogin || isEdpLogin || isGeneralManager) {
                //keyList = PstDepartment.genDepIDandNameWithCompanyDiv(0, 1000, "", true);
                   sec_value.add("0");
                   sec_key.add("select ...");
            } else {
                Position position = null;
                try {
                    position = PstPosition.fetchExc(emplx.getPositionId());
                } catch (Exception exc) {
                }
                //if (position != null & position.getDisabedAppDivisionScope() == 0 & position.getPositionLevel() >= PstPosition.LEVEL_MANAGER) {
                     sec_value.add("0");
                    sec_key.add("select ..."); 
               // }
            }
        }
 }else{
    comp_value.add("0");
    comp_key.add("select ...");
 }*/
//update by satrya 2013-09-17
String whereSection = "";
            Vector listSec = new Vector();
                                                   
            if(isHRDLogin || isEdpLogin || isGeneralManager){

                if(srcOvertime.getDepartmentId() != 0){ 
                    whereSection = PstSection.fieldNames[PstSection.FLD_DEPARTMENT_ID]+" = "+srcOvertime.getDepartmentId(); 
                    listSec = PstSection.list(0, 0, whereSection, " SECTION "); 
                }
                
                
                
            }else{
                
                String joinDeptSection = PstSystemProperty.getValueByName("JOIN_DEPARTMENT_SECTION");
                Vector depSecGroup = com.dimata.util.StringParser.parseGroup(joinDeptSection);
                int maxGrpSec = depSecGroup == null ? 0 : depSecGroup.size();                                
                
                Vector idxSecGroup = new Vector();
                
                for(int j = 0 ; j < maxGrpSec ; j++){
                    
                    String[] grp = (String[]) depSecGroup.get(j);
                     
                    if(grp[0].trim().compareToIgnoreCase(""+srcOvertime.getDepartmentId())==0){
                        
                        Counter counter = new Counter();
                        counter.setCounter(j);
                        idxSecGroup.add(counter);
                        
                    }                                    
                    
                }
                
                Vector SectionList = new Vector();
                
                if(idxSecGroup != null && idxSecGroup.size() > 0){
                    
                    for(int i = 0 ; i < idxSecGroup.size(); i++){
                        
                        Counter counter = (Counter)idxSecGroup.get(i);
                                
                        String[] grp = (String[]) depSecGroup.get(counter.getCounter());
                                
                        Section sec = new Section();
                        sec.setDepartmentId(Long.parseLong(grp[1]));
                        sec.setOID(Long.parseLong(grp[2]));
                        SectionList.add(sec);
                        
                    }
                    
                }
                
                if(SectionList != null && SectionList.size() > 0){
                    
                    for(int i = 0 ; i < SectionList.size() ; i++){
                        
                        Section section = new Section();
                        section = (Section)SectionList.get(i);
                        
                        if(section.getDepartmentId() == srcOvertime.getDepartmentId()){ 
                            
                            Section objSection = new Section();
                            
                            try{
                                objSection = PstSection.fetchExc(section.getOID());
                               
                                listSec.add(objSection);
                            }catch(Exception e){
                                System.out.println("Exception "+e.toString());
                            }
                        }
                        
                    }
                    
                }
                
                if(listSec == null || listSec.size() <= 0){
                    
                    //if(oidDepartment != 0){
                        whereSection = PstSection.fieldNames[PstSection.FLD_DEPARTMENT_ID]+" = "+srcOvertime.getDepartmentId();
                    //}
                
                    listSec = PstSection.list(0, 0, whereSection, " SECTION ");                    
                    
                }
                
            }
					//Vector listSec = PstSection.list(0, 0, "", " DEPARTMENT_ID, SECTION ");
					//String strWhereSec = PstSection.fieldNames[PstSection.FLD_DEPARTMENT_ID] + "=" + srcOvertime.getDepartmentId();
					//Vector listSec = PstSection.list(0, 0, strWhereSec, " SECTION ");
                                      if(listSec!=null && listSec.size()>0 || srcOvertime.getDepartmentId()!=0){
                                          sec_value = new Vector(1, 1);
                                          sec_key = new Vector(1, 1);
 
                                          sec_value.add("0");
                                          sec_key.add("select ...");
					for (int i = 0; i < listSec.size(); i++) {
						Section sec = (Section) listSec.get(i);
						sec_key.add(sec.getSection());
						sec_value.add(String.valueOf(sec.getOID()));
					}
                                      }
			%> <%= ControlCombo.draw(frmSrcOvertime.fieldNames[FrmSrcOvertime.FRM_FIELD_SECTION_ID], "formElemen", null, "" + srcOvertime.getSectionId(), sec_value, sec_key, " onkeydown=\"javascript:fnTrapKD()\"")%></td>

	</tr>
	<tr align="left" valign="top">
		<td width="17%" nowrap>
			<div align="left">Status Doc.</div></td>
		<td width="83%"> <%
					Vector obj_status = new Vector(1, 1);
					Vector val_status = new Vector(1, 1);
					Vector key_status = new Vector(1, 1);
					val_status.add("-1");
					key_status.add("- All -");

                                        val_status.add(String.valueOf(I_DocStatus.DOCUMENT_STATUS_DRAFT));
                                        key_status.add(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_DRAFT]);

                                        val_status.add(String.valueOf(I_DocStatus.DOCUMENT_STATUS_TO_BE_APPROVED));
                                        key_status.add(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_TO_BE_APPROVED]);

                                        val_status.add(String.valueOf(I_DocStatus.DOCUMENT_STATUS_FINAL));
                                        key_status.add(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_FINAL]);

                                        val_status.add(String.valueOf(I_DocStatus.DOCUMENT_STATUS_PROCEED));
                                        key_status.add(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_PROCEED]);                                                            

                                        val_status.add(String.valueOf(I_DocStatus.DOCUMENT_STATUS_CLOSED));
                                        key_status.add(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_CLOSED]);                                                            

                                        val_status.add(String.valueOf(I_DocStatus.DOCUMENT_STATUS_CANCELLED));
                                        key_status.add(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_CANCELLED]);
                                        
					out.println(ControlCombo.draw(FrmSrcOvertime.fieldNames[FrmSrcOvertime.FRM_FIELD_STATUS_DOC], null, "", val_status, key_status, "tabindex=\"4\"", "formElemen"));
			%></td>
	</tr>
	<tr align="left" valign="top">
		<td width="17%" nowrap>
			<div align="left">Request by</div></td>
		<td width="83%"><%
					Vector req_value = new Vector(1, 1);
					Vector req_key = new Vector(1, 1);
					req_value.add("0");
					req_key.add("select ...");
					//Vector listDiv = PstDivision.list(0, 0, "", " DIVISION ");
					String strWhereReq = PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID] + "=" + srcOvertime.getDepartmentId();
					Vector listReq = PstEmployee.list(0, 0, strWhereReq, PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]);
					for (int i = 0; i < listReq.size(); i++) {
						Employee req = (Employee) listReq.get(i);
						req_key.add(req.getFullName());
						req_value.add(String.valueOf(req.getOID()));
					}
			%> <%=ControlCombo.draw(frmSrcOvertime.fieldNames[FrmSrcOvertime.FRM_FIELD_REQ_ID], "formElemen", null, "" + srcOvertime.getRequestId(), req_value, req_key)%>
		</td>
	</tr-->
	<!--tr align="left" valign="top">
		<td width="17%" nowrap>
			<div align="left">Approval by</div></td>
		<td width="83%"><%
					/*Vector app_value = new Vector(1, 1);
					Vector app_key = new Vector(1, 1);
					app_value.add("0");
					app_key.add("select ...");
					//Vector listDiv = PstDivision.list(0, 0, "", " DIVISION ");
					String strWhereApp = PstEmployee.fieldNames[PstEmployee.FLD_DIVISION_ID] + "=" + oidDivision;
					Vector listApp = PstEmployee.list(0, 0, strWhereApp, PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]);
					for (int i = 0; i < listApp.size(); i++) {
						Employee app = (Employee) listApp.get(i);
						app_key.add(app.getFullName());
						app_value.add(String.valueOf(app.getOID()));
					} */
			%> <% //ControlCombo.draw(frmSrcOvertime.fieldNames[FrmSrcOvertime.FRM_FIELD_APPROVAL_ID], "formElemen", null, "" + srcOvertime.getApprovalId(), req_value, req_key)%>
		</td>
	</tr-->
	<!--tr align="left" valign="top">
		<td width="17%" nowrap>
			<div align="left">Acknowledge by</div></td>
		<td width="83%"><%
					/*Vector ack_value = new Vector(1, 1);
					Vector ack_key = new Vector(1, 1);
					ack_value.add("0");
					ack_key.add("select ...");
					//Vector listDiv = PstDivision.list(0, 0, "", " DIVISION ");
					String strWhereAck = PstEmployee.fieldNames[PstEmployee.FLD_DIVISION_ID] + "=" + oidDivision;
					Vector listAck = PstEmployee.list(0, 0, strWhereAck, PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]);
					for (int i = 0; i < listReq.size(); i++) {
						Employee ack = (Employee) listAck.get(i);
						ack_key.add(ack.getFullName());
						ack_value.add(String.valueOf(ack.getOID()));
					} */
			%> <% // ControlCombo.draw(frmSrcOvertime.fieldNames[FrmSrcOvertime.FRM_FIELD_ACK_ID], "formElemen", null, "" + srcOvertime.getAckId(), req_value, req_key)%>
		</td>
	</tr-->
	<tr align="left" valign="top">
		<td width="17%" height="21" nowrap>
			<div align="left">Sort By</div></td>
		<td width="83%"> <%= ControlCombo.draw(frmSrcOvertime.fieldNames[FrmSrcOvertime.FRM_FIELD_ORDER], "formElemen", null, "" + srcOvertime.getOrderBy(), FrmSrcOvertime.getOrderValue(), FrmSrcOvertime.getOrderKey(), " onkeydown=\"javascript:fnTrapKD()\"")%></td>
	</tr>
	<tr align="left" valign="top">
		<td width="17%"> <div align="left"></div></td>
		<td width="83%">
			<table width="50%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td ><a href="javascript:cmdSearch()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image10','','<%=approot%>/images/BtnSearchOn.jpg',1)" id="aSearch"><img name="Image10" border="0" src="<%=approot%>/images/BtnSearch.jpg" width="24" height="24" alt="Search Employee"></a></td>
					<td ><img src="<%=approot%>/images/spacer.gif" width="6" height="1"></td>
					<td  class="command" nowrap="true"><a href="javascript:cmdSearch()">Search for Overtime</a></td>

					<td ><img src="<%=approot%>/images/spacer.gif" width="10" height="8"></td>
					<td ><a href="javascript:cmdAdd()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image100','','<%=approot%>/images/BtnNewOn.jpg',1)"><img name="Image100" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="Add New Employee"></a></td>
					<td ><img src="<%=approot%>/images/spacer.gif" width="6" height="1"></td>
					<td  class="command" nowrap="true" ><a href="javascript:cmdAdd()">Add New Overtime</a></td>
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
                                                                                    </table>
                                                                                </form>
                                                                                <!-- #EndEditable -->
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
            <%if(headerStyle && !verTemplate.equalsIgnoreCase("0")){%>
            <tr>
                            <td valign="bottom">
                               <!-- untuk footer -->
                                <%@include file="../../footer.jsp" %>
                            </td>
                            
            </tr>
            <%}else{%>
            <tr> 
                <td colspan="2" height="20" > <!-- #BeginEditable "footer" --> 
      <%@ include file = "../../main/footer.jsp" %>
                <!-- #EndEditable --> </td>
            </tr>
            <%}%>
        </table>
    </body>
    <!-- #BeginEditable "script" -->
    <!-- #EndEditable -->
    <!-- #EndTemplate --></html>

