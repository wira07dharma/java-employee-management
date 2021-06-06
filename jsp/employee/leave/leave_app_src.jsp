
<%@page import="com.dimata.harisma.entity.masterdata.leaveconfiguration.PstLeaveConfigurationMainRequestOnly"%>
<%@page import="com.dimata.harisma.entity.payroll.PstPayGeneral"%>
<%@page import="com.dimata.harisma.entity.masterdata.leaveconfiguration.PstLeaveConfigurationMain"%>
<%-- 
    Document   : Leave_App
    Created on : Dec 26, 2009, 9:42:49 AM
    Author     : Tu Roy
--%>

<%@ page language = "java" %>
<!-- package java -->
<%@ page import = "java.util.*" %>
<!-- package wihita -->
<%@ page import = "com.dimata.util.*" %>
<!-- package qdep -->
<%@ page import = "com.dimata.gui.jsp.*" %>
<%@ page import = "com.dimata.qdep.form.*" %>
<!--package harisma -->
<%@ page import = "com.dimata.harisma.entity.masterdata.*" %>
<%@ page import = "com.dimata.harisma.entity.leave.*" %>
<%@ page import = "com.dimata.harisma.entity.admin.*" %>
<%@ page import = "com.dimata.harisma.entity.search.*" %>
<%@ page import = "com.dimata.harisma.form.search.*" %>
<%@ page import = "com.dimata.harisma.session.leave.*" %>

<%@ include file = "../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_EMPLOYEE, AppObjInfo.G2_LEAVE_MANAGEMENT, AppObjInfo.OBJ_LEAVE_APP_SRC); %>
<%@ include file = "../../main/checkuser.jsp" %>

<%
long hrdDepOid = Long.parseLong(String.valueOf(PstSystemProperty.getPropertyLongbyName(OID_HRD_DEPARTMENT)));
boolean isHRDLogin = hrdDepOid == departmentOid ? true : false;
long edpSectionOid = Long.parseLong(String.valueOf(PstSystemProperty.getPropertyLongbyName(OID_EDP_SECTION)));
boolean isEdpLogin = edpSectionOid == sectionOfLoginUser.getOID() ? true : false;
boolean isGeneralManager = positionType == PstPosition.LEVEL_GENERAL_MANAGER ? true : false;

int iCommand = FRMQueryString.requestCommand(request);
SrcLeaveApp objSrcLeaveApp = new SrcLeaveApp();
FrmSrcLeaveApp objFrmSrcLeaveApp = new FrmSrcLeaveApp();

//SessLeaveClosing.getPeriode(new Date());
//update by satrya 2013-09-19
if(iCommand==Command.GOTO){
objFrmSrcLeaveApp = new FrmSrcLeaveApp(request, objSrcLeaveApp);
objFrmSrcLeaveApp.requestEntityObject(objSrcLeaveApp); 
}
if(iCommand==Command.BACK)
{        
	objFrmSrcLeaveApp= new FrmSrcLeaveApp(request, objSrcLeaveApp);
	try
	{				
		objSrcLeaveApp = (SrcLeaveApp) session.getValue(SessLeaveApplication.SESS_SRC_LEAVE_APPLICATION);			
		if(objSrcLeaveApp == null)
		{
			objSrcLeaveApp = new SrcLeaveApp();
		}		
	}
	catch (Exception e)
	{
		objSrcLeaveApp = new SrcLeaveApp(); 
	}
}

// pengecekan status user yang login utk menentukan status approval mana yang mestinya default buatnya\
boolean isManager = false;
boolean isHrManager = false;
if(positionType == PstPosition.LEVEL_MANAGER)
{
    isManager = true;
	
    long hrdDepartmentOid = Long.parseLong(String.valueOf(PstSystemProperty.getPropertyLongbyName(OID_HRD_DEPARTMENT)));
	if(departmentOid == hrdDepartmentOid)
	{	
		isHrManager = true;
	}
}
 //update by satrya 2012-11-1
                /**
                    untuk pengaturan apakah departement itu dpt exsekusi leave
                **/
                        int isAdminExecuteLeave = 0;//hanya cuti full day jika fullDayLeave = 0
                        try{
                            isAdminExecuteLeave = Integer.parseInt(PstSystemProperty.getValueByName("LEAVE_EXCECUTE_BY_ADMIN"));
                        }catch(Exception ex){
                           System.out.println("Execption LEAVE_EXCECUTE_BY_ADMIN " + ex);
                            isAdminExecuteLeave =0;
                            
                        }
%>
<html><!-- #BeginTemplate "/Templates/main.dwt" -->
<head>
<!-- #BeginEditable "doctitle" --> 
<title>HARISMA - Leave Application</title>
<script language="JavaScript">
<!--
function cmdAdd(){
	document.frmsrcleaveapp.command.value="<%=String.valueOf(Command.ADD)%>";
	document.frmsrcleaveapp.action="leave_app_edit.jsp";
	document.frmsrcleaveapp.submit();
}

function cmdUpdateDiv(){
    document.frmsrcleaveapp.command.value="<%=String.valueOf(Command.GOTO)%>";
    document.frmsrcleaveapp.action="leave_app_src.jsp";
    document.frmsrcleaveapp.submit();
}
function cmdUpdateDep(){
    document.frmsrcleaveapp.command.value="<%=String.valueOf(Command.GOTO)%>";
    document.frmsrcleaveapp.action="leave_app_src.jsp";
    document.frmsrcleaveapp.submit();
}
function cmdUpdatePos(){
    document.frmsrcleaveapp.command.value="<%=String.valueOf(Command.GOTO)%>";
    document.frmsrcleaveapp.action="leave_app_src.jsp";
    document.frmsrcleaveapp.submit();
}



function cmdSearch(){
	document.frmsrcleaveapp.command.value="<%=String.valueOf(Command.LIST)%>";
	document.frmsrcleaveapp.action="leave_app_list.jsp";
	document.frmsrcleaveapp.submit();
}
function cmdSearchExecute(){
	document.frmsrcleaveapp.command.value="<%=String.valueOf(Command.LIST)%>";
	document.frmsrcleaveapp.action="leave_app_execute.jsp";
	document.frmsrcleaveapp.submit();
}
function cmdSearchStatusToBeApp(){
	document.frmsrcleaveapp.command.value="<%=String.valueOf(Command.LIST)%>";
	document.frmsrcleaveapp.action="leave_app_to_be_app.jsp";
	document.frmsrcleaveapp.submit();
}
function cmdSearchStatusApprove(){
	document.frmsrcleaveapp.command.value="<%=String.valueOf(Command.LIST)%>";
	document.frmsrcleaveapp.action="leave_application_approve.jsp";
	document.frmsrcleaveapp.submit();
}

//-------------------------- for Calendar -------------------------
function getThn(){
<%
     out.println(ControlDatePopup.writeDateCaller("frmsrcleaveapp",FrmSrcLeaveApp.fieldNames[FrmSrcLeaveApp.FRM_FIELD_SUBMISSION_DATE]));
%>
}


function hideObjectForDate(){
        <%
        /*
            out.println(ControlDatePopup.writeDateHideObj("frmsrcleaveapp", FrmSrcLeaveApp.fieldNames[FrmSrcLeaveApp.FRM_FIELD_APPROVAL_STATUS]));
            out.println(ControlDatePopup.writeDateHideObj("frmsrcleaveapp", FrmSrcLeaveApp.fieldNames[FrmSrcLeaveApp.FRM_FIELD_DOC_STATUS]));
            out.println(ControlDatePopup.writeDateHideObj("frmsrcleaveapp", FrmSrcLeaveApp.fieldNames[FrmSrcLeaveApp.FRM_FIELD_APPROVAL_HR_MAN]));
 *      */
        %>
}

function showObjectForDate(){
        <%
        /*
            out.println(ControlDatePopup.writeDateShowObj("frmsrcleaveapp", FrmSrcLeaveApp.fieldNames[FrmSrcLeaveApp.FRM_FIELD_APPROVAL_STATUS]));
            out.println(ControlDatePopup.writeDateShowObj("frmsrcleaveapp", FrmSrcLeaveApp.fieldNames[FrmSrcLeaveApp.FRM_FIELD_DOC_STATUS]));
            out.println(ControlDatePopup.writeDateHideObj("frmsrcleaveapp", FrmSrcLeaveApp.fieldNames[FrmSrcLeaveApp.FRM_FIELD_APPROVAL_HR_MAN]));
 *      */
        %>
} 


//-------------------------------------------

function fnTrapKD(){
   if (event.keyCode == 13) {
	document.all.aSearch.focus();
	cmdSearch();
   }
}

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
<!-- #EndEditable --> 
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"> 
<!-- #BeginEditable "styles" --> 
<link rel="stylesheet" href="../../styles/main.css" type="text/css">
<!-- #EndEditable -->
<!-- #BeginEditable "stylestab" --> 
<link rel="stylesheet" href="../../styles/tab.css" type="text/css">
<!-- #EndEditable -->
<!-- #EndEditable -->
<link rel="stylesheet" href="<%=approot%>/styles/calendar.css" type="text/css">
<!-- #BeginEditable "stylestab" -->  
</head> 

<body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" onLoad="MM_preloadImages('<%=approot%>/images/BtnSearchOn.jpg','<%=approot%>/images/BtnNewOn.jpg')">

<!-- Untuk Calendar-->
<%=ControlDatePopup.writeTable(approot)%>
<!-- End Calendar-->


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
                <td height="20"> <font color="#FF6600" face="Arial"><strong> <!-- #BeginEditable "contenttitle" --> 
                  Employee &gt; Leave Application &gt; Leave Form<!-- #EndEditable --> 
                  </strong></font> </td>
        </tr>
        <tr> 
          <td>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
              <tr> 
                <td  style="background-color:<%=bgColorContent%>; "> 
                  <table width="100%" border="0" cellspacing="1" cellpadding="1" >
                    <tr> 
                      <td valign="top">                          
                        <table style="border:1px solid <%=garisContent%>" width="100%" border="0" cellspacing="1" cellpadding="1" class="tablecolor">
                          <tr> 
                            <td valign="top">
		    				  <!-- #BeginEditable "content" --> 
                                    <form name="frmsrcleaveapp" method="post" action="">
                                      <input type="hidden" name="command" value="<%=String.valueOf(iCommand)%>">
                                      <!--update by satrya 2013-11-15-->
                                      <input type="hidden" name="<%=objFrmSrcLeaveApp.fieldNames[objFrmSrcLeaveApp.FRM_FIELD_TYPE_FORM]%>" value="<%=PstLeaveApplication.LEAVE_APPLICATION%>">  
                                      <table border="0" cellspacing="2" cellpadding="2" width="100%" >
                                        <tr> 
                                          <td width="13%"> 
                                            <div align="left"><%=dictionaryD.getWord(I_Dictionary.PAYROLL) %></div>
                                          </td>
                                          <td width="2%">:</td>
                                          <td width="85%"> 
                                            <input type="text" name="<%=objFrmSrcLeaveApp.fieldNames[objFrmSrcLeaveApp.FRM_FIELD_EMP_NUMBER] %>"  value="<%= objSrcLeaveApp.getEmpNum() %>" class="elemenForm"  onkeydown="javascript:fnTrapKD()">
                                          </td>
                                        </tr>
                                        <tr> 
                                          <td width="13%"> 
                                            <div align="left"><%=dictionaryD.getWord(I_Dictionary.FULL_NAME) %></div>
                                          </td>
                                          <td width="2%">:</td>
                                          <td width="85%"> 
                                            <input type="text" name="<%=objFrmSrcLeaveApp.fieldNames[objFrmSrcLeaveApp.FRM_FIELD_FULLNAME] %>"  value="<%= objSrcLeaveApp.getFullName() %>" class="elemenForm" onKeyDown="javascript:fnTrapKD()" size="40">
                                          </td>
                                        </tr>
<tr> 
    <td width="13%"> 
      <div align="left">Company</div>
    </td>
    <td width="2%">:</td>
    <td width="85%"> 
     <%
					Vector comp_value = new Vector(1, 1);
					Vector comp_key = new Vector(1, 1);  
String whereComp="";
 String whereClauseLeaveDinamis="";  
/*if(srcOvertime!=null && srcOvertime.getCompanyId()!=0){
    whereComp = PstCompany.fieldNames[PstCompany.FLD_COMPANY_ID] +"="+srcOvertime.getCompanyId();
}*/   
    Vector div_value = new Vector(1, 1);
    Vector div_key = new Vector(1, 1);      
    
    Vector dept_value = new Vector(1, 1);
    Vector dept_key = new Vector(1, 1);                                      
 if (processDependOnUserDept) {
        if (emplx.getOID() > 0) {
            if (isHRDLogin || isEdpLogin || isGeneralManager || isDirector) {
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

                    String whereClsDep = "(" + PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT_ID] + " = " + departmentOid
                            + ") OR (" + PstDepartment.fieldNames[PstDepartment.FLD_JOIN_TO_DEPARTMENT_ID] + " = " + departmentOid + ") ";
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
                                whereClsDep = whereClsDep + " OR (DEPARTMENT_ID = " + comp + ")";
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
    Vector listCostDept = new Vector();//PstDepartment.listWithCompanyDiv(0, 0, whereComp);
    whereClauseLeaveDinamis = "cm."+PstLeaveConfigurationMainRequestOnly.fieldNames[PstLeaveConfigurationMainRequestOnly.FLD_EMPLOYEE_ID]+"="+emplx.getOID();
    listCostDept = PstLeaveConfigurationMainRequestOnly.listCompLeaveConfig(whereClauseLeaveDinamis, "");  
    //update by satrya 2014-07-02
    if(listCostDept!=null && listCostDept.size()>0){
        /// maka tidak melakukan apapa
    }else{
        whereClauseLeaveDinamis = "cm."+PstLeaveConfigurationMain.fieldNames[PstLeaveConfigurationMain.FLD_EMPLOYEE_ID]+"="+emplx.getOID();
        listCostDept = PstLeaveConfigurationMain.listCompLeaveConfig(whereClauseLeaveDinamis, "");  
    }
    //old 2014-06-xx
    //whereClauseLeaveDinamis = "cm."+PstLeaveConfigurationMain.fieldNames[PstLeaveConfigurationMain.FLD_EMPLOYEE_ID]+"="+emplx.getOID();
    //listCostDept = PstLeaveConfigurationMain.listCompLeaveConfig(whereClauseLeaveDinamis, "");  
    if(listCostDept!=null && listCostDept.size()>0){ 
        /// maka yg dipakai adalah list config dinamis
    }else{
        listCostDept = PstDepartment.listWithCompanyDiv(0, 0, whereComp);
    }
    String prevCompany="";
    String prevDivision="";
    


long prevCompanyTmp=0;        
    for (int i = 0; i < listCostDept.size(); i++) {
        Department dept = (Department) listCostDept.get(i);
        if (prevCompany.equals(dept.getCompany())) {
            if (prevDivision.equals(dept.getDivision())) {
                //if(srcOvertime!=null && srcOvertime.getCompanyId()!=0){
                    dept_key.add(dept.getDepartment());
                    dept_value.add(String.valueOf(dept.getOID()));
                //}
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
             if(prevCompanyTmp!=dept.getCompanyId()){
                    comp_key.add(dept.getCompany());
                    comp_value.add(""+dept.getCompanyId());
                    prevCompanyTmp=dept.getCompanyId();
              }
             //untuk karyawan admin yg hanya bisa akses departement tertentu (ketika di awal)
             ////update
if (processDependOnUserDept) {
        if (emplx.getOID() > 0) {
            if (isHRDLogin || isEdpLogin || isGeneralManager || isDirector) {
                if(objSrcLeaveApp!=null && objSrcLeaveApp.getCompanyId()!=0){
                    div_key.add(dept.getDivision());
                    div_value.add(""+dept.getDivisionId()); 

                    dept_key.add(dept.getDepartment());
                    dept_value.add(String.valueOf(dept.getOID()));
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
                    if(objSrcLeaveApp!=null && objSrcLeaveApp.getCompanyId()!=0){
                       div_key.add(dept.getDivision());
                       div_value.add(""+dept.getDivisionId()); 

                       dept_key.add(dept.getDepartment());
                       dept_value.add(String.valueOf(dept.getOID()));
                       prevCompany = dept.getCompany();
                       prevDivision = dept.getDivision();             
                   }
                    //update by satrya 2013-09-19
                    else if((div_key!=null && div_key.size()==0 ) || ( chkAdaDiv.equalsIgnoreCase("select ..."))){
                        div_key.add(dept.getDivision());
                        div_value.add(""+dept.getDivisionId()); 
                        
                        //update by satrya 2013-09-19
                        dept_key.add(dept.getDepartment());
                       dept_value.add(String.valueOf(dept.getOID()));
                       
                       prevCompany = dept.getCompany();
                       prevDivision = dept.getDivision(); 
                   }
                   
                }else{
                    
                     div_key.add(dept.getDivision());
                       div_value.add(""+dept.getDivisionId()); 

                       dept_key.add(dept.getDepartment());
                       dept_value.add(String.valueOf(dept.getOID()));
                       prevCompany = dept.getCompany();
                       prevDivision = dept.getDivision(); 
                }
            }
        }
 }else{
      if(objSrcLeaveApp!=null && objSrcLeaveApp.getCompanyId()!=0){
                    div_key.add(dept.getDivision());
                    div_value.add(""+dept.getDivisionId()); 

                    dept_key.add(dept.getDepartment());
                    dept_value.add(String.valueOf(dept.getOID()));
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
			%>
                        <%= ControlCombo.draw(objFrmSrcLeaveApp.fieldNames[objFrmSrcLeaveApp.FRM_FIELD_COMPANY_ID], "formElemen", null, "" + objSrcLeaveApp.getCompanyId(), comp_value, comp_key, "onChange=\"javascript:cmdUpdateDiv()\"")%> </td>
</tr>
<tr> 
    <td width="13%"> 
      <div align="left"><%=dictionaryD.getWord(I_Dictionary.DIVISION) %></div>
    </td>
    <td width="2%">:</td>
    <td width="85%"> 
<%
					
                                   //update by satrya 2013-08-13
                                   //jika user memilih select kembali
                                   if(objSrcLeaveApp!=null && objSrcLeaveApp.getCompanyId()==0){ 
                                       objSrcLeaveApp.setDivisionId(0); 
                                   }

if(objSrcLeaveApp!=null && objSrcLeaveApp.getCompanyId()!=0){
    whereComp = "("+(whereComp!=null && whereComp.length()==0?"1=1":whereComp) + ") AND " + PstCompany.fieldNames[PstCompany.FLD_COMPANY_ID] +"="+objSrcLeaveApp.getCompanyId();
    //listCostDept = PstDepartment.listWithCompanyDiv(0, 0, whereComp);
    whereClauseLeaveDinamis = "("+(whereClauseLeaveDinamis!=null && whereClauseLeaveDinamis.length()==0?"1=1":whereClauseLeaveDinamis) + ") AND comp." + PstPayGeneral.fieldNames[PstPayGeneral.FLD_GEN_ID] +"="+objSrcLeaveApp.getCompanyId(); 
    //update by satrya 2014-07-02
    listCostDept = PstLeaveConfigurationMainRequestOnly.listCompLeaveConfig(whereClauseLeaveDinamis, ""); 
    if(listCostDept!=null && listCostDept.size()>0){ 
        //tidak melakukan apapa
    }else{
        listCostDept = PstLeaveConfigurationMain.listCompLeaveConfig(whereClauseLeaveDinamis, ""); 
    }
    //listCostDept = PstLeaveConfigurationMain.listCompLeaveConfig(whereClauseLeaveDinamis, ""); 
    if(listCostDept!=null && listCostDept.size()>0){ 
        /// maka yg dipakai adalah list config dinamis
    }else{
        listCostDept = PstDepartment.listWithCompanyDiv(0, 0, whereComp);
    }
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
            if (isHRDLogin || isEdpLogin || isGeneralManager || isDirector) {
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
                if(objSrcLeaveApp!=null && objSrcLeaveApp.getDivisionId()!=0){
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
          String chkAdaDptDinamis="";
          /* update by satrya 2014-06-18if(whereComp!=null && whereComp.length()>0){
                chkAdaDpt = "("+(whereComp!=null && whereComp.length()==0?"1=1":whereComp)+ ") AND d." + PstDivision.fieldNames[PstDivision.FLD_DIVISION_ID] +"="+objSrcLeaveApp.getDivisionId();
          }
            Vector listCheckAdaDept = PstDepartment.listWithCompanyDiv(0, 0, chkAdaDpt);*/
          if(whereComp!=null && whereComp.length()>0){
                chkAdaDpt = "("+(whereComp!=null && whereComp.length()==0?"1=1":whereComp)+ ") AND d." + PstDivision.fieldNames[PstDivision.FLD_DIVISION_ID] +"="+objSrcLeaveApp.getDivisionId();
          }
          if(whereClauseLeaveDinamis!=null && whereClauseLeaveDinamis.length()>0){
                chkAdaDptDinamis = "("+(whereClauseLeaveDinamis!=null && whereClauseLeaveDinamis.length()==0?"1=1":whereClauseLeaveDinamis)+ ") AND divs." + PstDivision.fieldNames[PstDivision.FLD_DIVISION_ID] +"="+objSrcLeaveApp.getDivisionId();
          }
            Vector listCheckAdaDept = new Vector();//PstDepartment.listWithCompanyDiv(0, 0, chkAdaDpt); 
            //update by satrya 2014-07-02
            listCheckAdaDept = PstLeaveConfigurationMainRequestOnly.listCompLeaveConfig(chkAdaDptDinamis, "");  
            if(listCheckAdaDept!=null && listCheckAdaDept.size()>0){
                //maka tidak melakukan apapa
            }else{
                listCheckAdaDept = PstLeaveConfigurationMain.listCompLeaveConfig(chkAdaDptDinamis, ""); 
            }
            //listCheckAdaDept = PstLeaveConfigurationMain.listCompLeaveConfig(chkAdaDptDinamis, ""); 
            if(listCheckAdaDept!=null && listCheckAdaDept.size()>0){ 
                /// maka yg dipakai adalah list config dinamis
            }else{
                listCheckAdaDept = PstDepartment.listWithCompanyDiv(0, 0, chkAdaDpt);
            }
            if((listCheckAdaDept==null || listCheckAdaDept.size()==0)){
                
if (processDependOnUserDept) {
        if (emplx.getOID() > 0) {
            if (isHRDLogin || isEdpLogin || isGeneralManager || isDirector) {
                
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
                objSrcLeaveApp.setDivisionId(tmpFirstDiv);
            }
        }
 }else{
     objSrcLeaveApp.setDivisionId(tmpFirstDiv);
 }
               
            }
        }
    }
}
			%> <%= ControlCombo.draw(objFrmSrcLeaveApp.fieldNames[objFrmSrcLeaveApp.FRM_FIELD_DIVISION_ID], "formElemen", null,""+objSrcLeaveApp.getDivisionId() , div_value, div_key, "onChange=\"javascript:cmdUpdateDep()\"")%> </td>
</tr>
                                        <tr> 
                                          <td width="13%"> 
                                            <div align="left"><%=dictionaryD.getWord(I_Dictionary.DEPARTMENT) %></div>
                                          </td>
                                          <td width="2%">:</td>
                                          <td width="85%"> 
                                            <%

            //update by satrya 2013-08-13
            //jika user memilih select kembali
            if(objSrcLeaveApp!=null && objSrcLeaveApp.getDepartmentId()==0){  
                objSrcLeaveApp.setSectionId(0); 
            }
if(objSrcLeaveApp!=null && objSrcLeaveApp.getDivisionId()!=0){
    if(whereComp!=null && whereComp.length()>0){
        whereComp = "("+whereComp + ") AND d." + PstDivision.fieldNames[PstDivision.FLD_DIVISION_ID] +"="+objSrcLeaveApp.getDivisionId();
    }
    
    //listCostDept = PstDepartment.listWithCompanyDiv(0, 0, whereComp);
    whereClauseLeaveDinamis = "("+whereClauseLeaveDinamis + ") AND dpt." + PstDivision.fieldNames[PstDivision.FLD_DIVISION_ID] +"="+objSrcLeaveApp.getDivisionId();
    //update by satrya 2014-07-02
    listCostDept = PstLeaveConfigurationMainRequestOnly.listCompLeaveConfig(whereClauseLeaveDinamis, "");  
    if(listCostDept!=null && listCostDept.size()>0){
        //tidak melakukan apapa
    }else{
        listCostDept = PstLeaveConfigurationMain.listCompLeaveConfig(whereClauseLeaveDinamis, ""); 
    }
    //listCostDept = PstLeaveConfigurationMain.listCompLeaveConfig(whereClauseLeaveDinamis, ""); 
    if(listCostDept!=null && listCostDept.size()>0){ 
        /// maka yg dipakai adalah list config dinamis
    }else{
        listCostDept = PstDepartment.listWithCompanyDiv(0, 0, whereComp);
    }
    prevCompany="";
    prevDivision="";
    
    div_value = new Vector(1, 1);
    div_key = new Vector(1, 1);      
    
    dept_value = new Vector(1, 1);
    dept_key = new Vector(1, 1); 

    prevCompanyTmp=0; 

if (processDependOnUserDept) {
        if (emplx.getOID() > 0) {
            if (isHRDLogin || isEdpLogin || isGeneralManager || isDirector) {
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
                    prevCompanyTmp=dept.getCompanyId();
              }
              
            }
           
        }
    }
}
			%> <%= ControlCombo.draw(objFrmSrcLeaveApp.fieldNames[objFrmSrcLeaveApp.FRM_FIELD_DEPARTMENT], "formElemen", null, "" + objSrcLeaveApp.getDepartmentId(), dept_value, dept_key, "onChange=\"javascript:cmdUpdatePos()\"")%> </td>
                                        </tr>
                                        <tr> 
                                          <td width="13%"> 
                                            <div align="left"><%=dictionaryD.getWord(I_Dictionary.SECTION) %></div>
                                          </td>
                                          <td width="2%">:</td>
                                          <td width="85%"> 
                                            <% 
            Vector sec_value = new Vector(1, 1);
            Vector sec_key = new Vector(1, 1);
 
            sec_value.add("0");
            sec_key.add("select ...");
            
            String whereSection = "";
            Vector listSec = new Vector();
                                                   
            if(isHRDLogin || isEdpLogin || isGeneralManager || isDirector){

                if(objSrcLeaveApp.getDepartmentId() != 0){
                    whereSection = PstSection.fieldNames[PstSection.FLD_DEPARTMENT_ID]+" = "+objSrcLeaveApp.getDepartmentId();
                }
                
                listSec = PstSection.list(0, 0, whereSection, " SECTION ");
                
            }else{
                
                String joinDeptSection = PstSystemProperty.getValueByName("JOIN_DEPARTMENT_SECTION");
                Vector depSecGroup = com.dimata.util.StringParser.parseGroup(joinDeptSection);
                int maxGrpSec = depSecGroup == null ? 0 : depSecGroup.size();                                
                
                Vector idxSecGroup = new Vector();
                
                for(int j = 0 ; j < maxGrpSec ; j++){
                    
                    String[] grp = (String[]) depSecGroup.get(j);
                     
                    if(grp[0].trim().compareToIgnoreCase(""+departmentOid)==0){
                        
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
                        
                        if(section.getDepartmentId() == objSrcLeaveApp.getDepartmentId()){
                            
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
                        whereSection = PstSection.fieldNames[PstSection.FLD_DEPARTMENT_ID]+" = "+objSrcLeaveApp.getDepartmentId();
                    //}
                
                    listSec = PstSection.list(0, 0, whereSection, " SECTION ");                    
                    
                }
                
            }
							
							/*sec_value.add("0");
							sec_key.add("all section ...");
                                                        String strWhere = PstSection.fieldNames[PstSection.FLD_DEPARTMENT_ID]+"="+oidDepartment;
							Vector listSec = PstSection.list(0, 0, strWhere, " DEPARTMENT_ID, SECTION ");*/
							for (int i = 0; i < listSec.size(); i++) {
								Section sec = (Section) listSec.get(i);
								sec_key.add(sec.getSection());
								sec_value.add(String.valueOf(sec.getOID()));
							}
						%>
                                                <%= ControlCombo.draw(objFrmSrcLeaveApp.fieldNames[objFrmSrcLeaveApp.FRM_FIELD_SECTION],"formElemen",null,String.valueOf(objSrcLeaveApp.getSectionId()), sec_value, sec_key, "") %>
                                          </td>
                                        </tr>
                                        <tr> 
                                          <td width="13%"> 
                                            <div align="left"><%=dictionaryD.getWord(I_Dictionary.POSITION) %></div>
                                          </td>
                                          <td width="2%">:</td>
                                          <td width="85%"> 
                                            <%														
											Vector position_value = new Vector(1,1);
											Vector position_key = new Vector(1,1);
											position_value.add("0");
											position_key.add("select ...");                                                       
											Vector listPos = PstPosition.list(0, 0, "", " POSITION ");                                                            
											String selectValuePosition = ""+objSrcLeaveApp.getPositionId();
											for (int i = 0; i < listPos.size(); i++) 
											{
												Position pos = (Position) listPos.get(i);
												position_key.add(pos.getPosition());
												position_value.add(String.valueOf(pos.getOID()));
											}														
											%>
                                            <%=ControlCombo.draw(objFrmSrcLeaveApp.fieldNames[objFrmSrcLeaveApp.FRM_FIELD_POSITION],"elementForm", null, selectValuePosition, position_value, position_key," onkeydown=\"javascript:fnTrapKD()\"") %> </td>
                                        </tr>
										<tr>
											<td width="13%">Type Leave</td>
											<td width="2%">:</td>
											<td>
												<%														
											Vector vTypeLeave_value = new Vector(1,1);
											Vector vTypeLeave_key = new Vector(1,1);
											vTypeLeave_value.add("0");
											vTypeLeave_key.add("Regular"); 
                                                                                        vTypeLeave_value.add(""+PstPublicLeaveDetail.TYPE_LEAVE);
											vTypeLeave_key.add("Public Leave");
                                                                                        String selectTypeValue = ""+objSrcLeaveApp.getPositionId();								
                                                                                        
											%>
                                            <%=ControlCombo.draw(objFrmSrcLeaveApp.fieldNames[objFrmSrcLeaveApp.FRM_FIELD_PUBLIC_LEAVE_TYPE],"elementForm", null, selectTypeValue, vTypeLeave_value, vTypeLeave_key," onkeydown=\"javascript:fnTrapKD()\"") %> 
											</td>
										</tr>
                                        <tr>
                                          <td width="13%">Document Status</td>
                                          <td width="2%">:</td>
                                          <td>
                                              <table border="0" cellspacing="0" cellpadding="0" width="60%" >
                                                  <tr>
                                                      <td width="5%">
                                                        <input type="checkbox" name="<%=objFrmSrcLeaveApp.fieldNames[objFrmSrcLeaveApp.FRM_FIELD_DOC_STATUS_DRAFT]%>" <%=(objSrcLeaveApp.isDraft() ? "checked" : "")%> value="1">
                                                        <i><font color="#000000">Draft</font></i>
                                                      </td>
                                                      <td width="5%">
                                                        <input type="checkbox" name="<%=objFrmSrcLeaveApp.fieldNames[objFrmSrcLeaveApp.FRM_FIELD_DOC_STATUS_TO_BE_APPROVE]%>" <%=(objSrcLeaveApp.isToBeApprove() ? "checked" : "")%> value="1">
                                                        <i><font color="#000000">To Be Approve</font></i>
                                                      </td>
                                                      <td width="5%">
                                                        <input type="checkbox" name="<%=objFrmSrcLeaveApp.fieldNames[objFrmSrcLeaveApp.FRM_FIELD_DOC_STATUS_APPROVED]%>" <%=(objSrcLeaveApp.isApproved() ? "checked" : "")%> value="1">
                                                        <i><font color="#000000">Approved</font></i>
                                                      </td>
                                                      <td width="5%">
                                                        <input type="checkbox" name="<%=objFrmSrcLeaveApp.fieldNames[objFrmSrcLeaveApp.FRM_FIELD_DOC_STATUS_EXECUTED]%>" <%=(objSrcLeaveApp.isExecuted() ? "checked" : "")%> value="1">
                                                        <i><font color="#000000">Executed</font></i>
                                                      </td>
                                                  </tr>
                                              </table>
                                          </td>
                                        </tr>
                                        <tr> 
                                          <td width="13%">Date Of Application</td>
                                          <td width="2%">:</td>
                                          <td width="85%"> 
                                            <input type="checkbox" name="<%=objFrmSrcLeaveApp.fieldNames[objFrmSrcLeaveApp.FRM_FIELD_SUBMISSION]%>" <%=(objSrcLeaveApp.isSubmission() ? "checked" : "")%> value="1">
                                            <i><font color="#FF0000">Select all date</font></i></td>
                                        </tr>
                                        <tr> 
                                          <td width="13%">&nbsp;</td>
                                          <td width="2%">&nbsp;</td>
                                          <td width="85%">&nbsp;                                          
                                          <%=ControlDatePopup.writeDate(FrmSrcLeaveApp.fieldNames[FrmSrcLeaveApp.FRM_FIELD_SUBMISSION_DATE],objSrcLeaveApp.getSubmissionDate())%>
                                        </td>                                        
                                        </tr>
                                        
                                        <tr> 
                                          <td width="13%">Date Leave</td>
                                          <td width="2%">: &nbsp;</td>
                                          <td width="85%">&nbsp;                                          
                                          <% 
                                              
                                                Date st = new Date();
                                                st.setHours(0);
                                                st.setMinutes(0);
                                                st.setSeconds(0);
                                                Date end = new Date();
                                                end.setHours(23);
                                                end.setMinutes(59);
                                                end.setSeconds(59);
                                                 String ctrTimeStart = ControlDate.drawTime(FrmSrcLeaveApp.fieldNames[FrmSrcLeaveApp.FRM_FIELD_SELECTED_DATE_FROM_LEAVE],  objSrcLeaveApp.getSelectedFrom() != null ? objSrcLeaveApp.getSelectedFrom() : st, "elemenForm", 24,0, 0); 
                                                 String ctrTimeEnd = ControlDate.drawTime(FrmSrcLeaveApp.fieldNames[FrmSrcLeaveApp.FRM_FIELD_SELECTED_DATE_TO_LEAVE],  objSrcLeaveApp.getSelectedTo() != null ?  iCommand==Command.NONE ? end : objSrcLeaveApp.getSelectedTo() : end, "elemenForm", 24,0, 0); 
                                                 %>
                                                <%=ControlDate.drawDateWithStyle(FrmSrcLeaveApp.fieldNames[FrmSrcLeaveApp.FRM_FIELD_SELECTED_DATE_FROM_LEAVE], objSrcLeaveApp.getSelectedFrom() != null ? objSrcLeaveApp.getSelectedFrom() : null, 2, -5, "formElemen", " onkeydown=\"javascript:fnTrapKD()\"") + ctrTimeStart%>  
                                                <%//=ControlDate.drawDateWithStyle("check_date_start", selectedDateFrom != null ? selectedDateFrom : new Date(), 2, -5, "formElemen", " onkeydown=\"javascript:fnTrapKD()\"")%> 
                                          to  <%=ControlDate.drawDateWithStyle(FrmSrcLeaveApp.fieldNames[FrmSrcLeaveApp.FRM_FIELD_SELECTED_DATE_TO_LEAVE], objSrcLeaveApp.getSelectedTo() != null ? objSrcLeaveApp.getSelectedTo() : null, 2, -5, "formElemen", " onkeydown=\"javascript:fnTrapKD()\"") + ctrTimeEnd%>                                                                                            </td>
                                            <% //out.println("date "+date);
                                                //long periodId = PstPeriod.getPeriodIdBySelectedDate(date); 

                                            %>
                                        </td>                                        
                                        </tr>
                                        <%--
                                        <tr>
                                          <td width="11%">Dep Head Approval</td>
                                          <td nowrap width="2%">:</td>
                                          <td nowrap width="87%"> 
                                            <%															
											Vector approval_value = new Vector(1,1);
											Vector approval_key = new Vector(1,1);

                                                                                        approval_value.add("-1");
											approval_key.add("All Status");
                                                                                        
											approval_value.add("0");
											approval_key.add("Not Approved");  
                                                                                        
                                                                                        approval_value.add("1");
											approval_key.add("Approved");
											
											String selectValueApprovalStatus = ""+objSrcLeaveApp.getApprovalStatus();                                                          
											
											%>
                                            <%=ControlCombo.draw(objFrmSrcLeaveApp.fieldNames[objFrmSrcLeaveApp.FRM_FIELD_APPROVAL_STATUS],"elementForm", null, selectValueApprovalStatus, approval_value, approval_key," onkeydown=\"javascript:fnTrapKD()\"") %></td>
                                        </tr>                                        
                                        <tr>
                                          <td width="11%">HR Manager Approval</td>
                                          <td nowrap width="2%">:</td>
                                          <td nowrap width="87%"> 
                                            <%															
											Vector approval_HR_value = new Vector(1,1);
											Vector approval_HR_key = new Vector(1,1);

                                                                                        approval_HR_value.add("-1");
											approval_HR_key.add("All Status");
                                                                                        
											approval_HR_value.add("0");
											approval_HR_key.add("Not Approved");  
                                                                                        
                                                                                        approval_HR_value.add("1");
											approval_HR_key.add("Approved");
											
											String selectValueApprovalHRStatus = ""+objSrcLeaveApp.getApprovalHRMan();                                                          
											
											%>
                                            <%=ControlCombo.draw(objFrmSrcLeaveApp.fieldNames[objFrmSrcLeaveApp.FRM_FIELD_APPROVAL_HR_MAN],"elementForm", null, selectValueApprovalHRStatus, approval_HR_value, approval_HR_key," onkeydown=\"javascript:fnTrapKD()\"") %></td>
                                        </tr>  
                                        <tr>
                                          <td width="11%">GM</td>
                                          <td nowrap width="2%">:</td>
                                          <td nowrap width="87%"> 
                                            <%															
											Vector approval_GM_value = new Vector(1,1);
											Vector approval_GM_key = new Vector(1,1);

                                                                                        approval_GM_value.add("-1");
											approval_GM_key.add("All Status");
                                                                                        
											approval_GM_value.add("0");
											approval_GM_key.add("Not Approved");  
                                                                                        
                                                                                        approval_GM_value.add("1");
											approval_GM_key.add("Approved");
											
											String selectValueApprovalGMStatus = ""+objSrcLeaveApp.getApprovalGM();                                                          
											
											%>
                                            <%=ControlCombo.draw(objFrmSrcLeaveApp.fieldNames[objFrmSrcLeaveApp.FRM_FIELD_APPROVAL_GM],"elementForm", null, selectValueApprovalGMStatus, approval_GM_value, approval_GM_key,"onkeydown=\"javascript:fnTrapKD()\"") %></td>
                                        </tr> 
                                        --%>
                                        <tr> 
                                          <td width="13%">&nbsp;</td>
                                          <td nowrap width="2%">&nbsp;</td>
                                          <td nowrap width="85%">&nbsp;</td>
                                        </tr>
                                        <tr> 
                                          <td width="13%">&nbsp;</td>
                                          <td nowrap width="2%">&nbsp;</td>
                                          <td nowrap width="85%"> 
                                            <table border="0" cellpadding="0" cellspacing="0" width="500">
                                              <tr> 
                                                <%if(privView){ %>
                                                <td width="24px" ><a href="javascript:cmdSearch()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image10','','<%=approot%>/images/BtnSearchOn.jpg',1)" id="aSearch"><img name="Image10" border="0" src="<%=approot%>/images/BtnSearch.jpg" width="24" height="24" alt="Search for Leave Application" ></a></td>
                                                <td width="5px"></td>
                                                <td width="30px" class="command" nowrap><a href="javascript:cmdSearch()">Search</a></td>  
                                                <td width="30px"><img src="<%=approot%>/images/spacer.gif" width="8" height="8"></td>
                                                <%}if(privAdd){ %>
                                                <td width="30px" ><a href="javascript:cmdAdd()" onMouseOut="MM_swapImgRestore()" onMouseOut="MM_swapImgRestore()" ><img name="Image300" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="Add Leave Application" ></a></td>
                                                <td width="5px"></td>
                                                <td class="command" width="180px"><a href="javascript:cmdAdd()">Add Leave Application</a></td>      
                                                <%}if(isHRDLogin || isAdminExecuteLeave !=0){%>
                                                       <td width="30px" ><a href="javascript:cmdSearchStatusToBeApp()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image10','','<%=approot%>/images/BtnSearchOn.jpg',1)" id="aSearch"><img name="Image10" border="0" src="<%=approot%>/images/BtnSearch.jpg" width="24" height="24" alt="Search Execution Appliaction"></a></td>
                                                <td width="5px"></td>
                                                <td class="command" width="240px"><a href="javascript:cmdSearchStatusToBeApp()">Search To Be App</a></td>      
                                                 <%if(isHRDLogin){%>
                                                <td width="30px" ><a href="javascript:cmdSearchStatusApprove()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image10','','<%=approot%>/images/BtnSearchOn.jpg',1)" id="aSearch"><img name="Image10" border="0" src="<%=approot%>/images/BtnSearch.jpg" width="24" height="24" alt="Search Execution Appliaction"></a></td>
                                                <td width="5px"></td>
                                                <td class="command" width="240px"><a href="javascript:cmdSearchStatusApprove()">Search All Approve HRD </a></td>
                                               
                                                <td width="30px" ><a href="javascript:cmdSearchExecute()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image10','','<%=approot%>/images/BtnSearchOn.jpg',1)" id="aSearch"><img name="Image10" border="0" src="<%=approot%>/images/BtnSearch.jpg" width="24" height="24" alt="Search Execution Appliaction"></a></td>
                                                <td width="5px"></td>
                                                <td class="command" width="240px"><a href="javascript:cmdSearchExecute()">Search Execution</a></td> 
                                                <%}else{%>
                                                <td width="30px" ></td>
                                                <td width="5px"></td>
                                                <td class="command" width="240px"></td> 
                                                <%}%>
                                                <%}%>
                                               
                                               
                                               
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
</html>
