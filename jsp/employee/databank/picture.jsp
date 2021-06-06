<% 
/* 
 * Page Name  		:  picture.jsp
 * Created on 		:  [date] [time] AM/PM 
 * 
 * @author  		: yunny 
 * @version  		: 01 
 */

/*******************************************************************
 * Page Description 	: [project description ... ] 
 * Imput Parameters 	: [input parameter ...] 
 * Output 			: [output ...] 
 *******************************************************************/
%>

<%@ page language = "java" %>
<!-- package java -->
<%@ page import = "java.util.*" %>

<!-- package dimata -->
<%@ page import = "com.dimata.util.*" %>
<%@ page import = "com.dimata.util.blob.*" %>

<!-- package qdep -->
<%@ page import = "com.dimata.gui.jsp.*" %>
<%@ page import = "com.dimata.qdep.form.*" %>

<!--package harisma -->

<%@ page import = "com.dimata.harisma.entity.masterdata.*" %>
<%@ page import = "com.dimata.harisma.entity.employee.*" %>
<%@ page import = "com.dimata.harisma.form.employee.*" %>
<%@ page import = "com.dimata.harisma.entity.admin.*" %>
<%@ page import = "com.dimata.harisma.session.employee.*" %>


<%@ include file = "../../main/javainit.jsp" %>

<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_EMPLOYEE, AppObjInfo.G2_DATABANK, AppObjInfo.OBJ_DATABANK); %>
<%@ include file = "../../main/checkuser.jsp" %>
<%
/* Check privilege except VIEW, view is already checked on checkuser.jsp as basic access*/
//boolean privAdd=userSession.checkPrivilege(AppObjInfo.composeCode(appObjCode, AppObjInfo.COMMAND_ADD));
//boolean privUpdate=userSession.checkPrivilege(AppObjInfo.composeCode(appObjCode, AppObjInfo.COMMAND_UPDATE));
//boolean privDelete=userSession.checkPrivilege(AppObjInfo.composeCode(appObjCode, AppObjInfo.COMMAND_DELETE));
%>

<!-- Jsp Block -->


<%
int iCommand = FRMQueryString.requestCommand(request);
int start = FRMQueryString.requestInt(request, "start");
int prevCommand = FRMQueryString.requestInt(request, "prev_command");
long oidEmpPicture = FRMQueryString.requestLong(request, "emp_picture_oid");
long oidEmployee = FRMQueryString.requestLong(request, "employee_oid");
String pictName =  FRMQueryString.requestString(request, "pict");



/*variable declaration*/
int recordToGet = 10;
String msgString = "";
int iErrCode = FRMMessage.NONE;
String whereClause = PstEmpPicture.fieldNames[PstEmpPicture.FLD_EMPLOYEE_ID]+ " = "+oidEmployee;

CtrlEmpPicture ctrlEmpPicture = new CtrlEmpPicture(request);
ControlLine ctrLine = new ControlLine();
Vector listEmpPicture = new Vector(1,1);

SessEmployeePicture sessEmployeePicture = new SessEmployeePicture();
String pictPath = "";
try{
	pictPath = sessEmployeePicture.fetchImageEmployee(oidEmployee);// fetchImagePeserta(oidEmployee);

}catch(Exception e){
	System.out.println("err."+e.toString());
}
	System.out.println("pictPath sebelum..............."+pictPath);

/*switch statement */
iErrCode = ctrlEmpPicture.action(iCommand , oidEmpPicture, oidEmployee);
/* end switch*/
FrmEmpPicture frmEmpPicture = ctrlEmpPicture.getForm();

EmpPicture empPicture = ctrlEmpPicture.getEmpPicture();
msgString =  ctrlEmpPicture.getMessage();


/*count list All CareerPath*/
int vectSize = PstEmpPicture.getCount(whereClause);

if((iCommand == Command.FIRST || iCommand == Command.PREV )||
  (iCommand == Command.NEXT || iCommand == Command.LAST)){
		start = ctrlEmpPicture.actionList(iCommand, start, vectSize, recordToGet);
 } 
/* end switch list*/
/* get record to display */
listEmpPicture = PstEmpPicture.list(start,recordToGet, whereClause , "");


long oidDepartment = 0;
if(oidEmployee != 0){
	Employee employee = new Employee();
	try{
		 employee = PstEmployee.fetchExc(oidEmployee);
		 oidDepartment = employee.getDepartmentId();
	}catch(Exception exc){
		 employee = new Employee();
	}
}
//listSection = PstSection.list(0,500,PstSection.fieldNames[PstSection.FLD_DEPARTMENT_ID]+ " = "+oidDepartment,"SECTION");
Vector listSection = PstSection.list(0,0,"","SECTION");
/*handle condition if size of record to display = 0 and start > 0 	after delete*/


Vector vectPict = new Vector(1,1);
 vectPict.add(""+oidEmployee);

session.putValue("SELECTED_PHOTO_SESSION", vectPict);

%>
<html><!-- #BeginTemplate "/Templates/maintab.dwt" -->
<head>
<!-- #BeginEditable "doctitle" --> 
<title>Picture </title>
<script language="JavaScript">
function cmdAdd(){
	document.frmcareerpath.emp_picture_oid.value="0";
	document.frmcareerpath.command.value="<%=Command.ADD%>";
	document.frmcareerpath.prev_command.value="<%=prevCommand%>";
	document.frmcareerpath.action="picture.jsp";
	document.frmcareerpath.submit();
}

function cmdAsk(oidEmpPicture){
	document.frmcareerpath.emp_picture_oid.value=oidEmpPicture;
	document.frmcareerpath.command.value="<%=Command.ASK%>";
	document.frmcareerpath.prev_command.value="<%=prevCommand%>";
	document.frmcareerpath.action="picture.jsp";
	document.frmcareerpath.submit();
}

function cmdConfirmDelete(oidEmpPicture){
	document.frmcareerpath.emp_picture_oid.value=oidEmpPicture;
	document.frmcareerpath.command.value="<%=Command.DELETE%>";
	document.frmcareerpath.prev_command.value="<%=prevCommand%>";
	document.frmcareerpath.action="picture.jsp";
	document.frmcareerpath.submit();
}
function cmdSave(){
	document.frmcareerpath.command.value="<%=Command.SAVE%>";
	document.frmcareerpath.prev_command.value="<%=prevCommand%>";
	document.frmcareerpath.action="upload_pict_process.jsp";
	document.frmcareerpath.submit();
	}

function cmdEdit(oidEmpPicture){
	document.frmcareerpath.emp_picture_oid.value=oidEmpPicture;
	document.frmcareerpath.command.value="<%=Command.EDIT%>";
	document.frmcareerpath.prev_command.value="<%=prevCommand%>";
	document.frmcareerpath.action="picture.jsp";
	document.frmcareerpath.submit();
	}

function cmdCancel(oidEmpPicture){
	document.frmcareerpath.emp_picture_oid.value=oidEmpPicture;
	document.frmcareerpath.command.value="<%=Command.EDIT%>";
	document.frmcareerpath.prev_command.value="<%=prevCommand%>";
	document.frmcareerpath.action="picture.jsp";
	document.frmcareerpath.submit();
}

function cmdBack(){
	document.frmcareerpath.command.value="<%=Command.BACK%>";
	document.frmcareerpath.action="picture.jsp";
	document.frmcareerpath.submit();
}

function cmdListSection(){	
	document.frmcareerpath.action="picture.jsp";
	document.frmcareerpath.submit();
}

function cmdBackEmp(empOID,com){
	document.frmcareerpath.employee_oid.value=empOID;
	document.frmcareerpath.prev_command.value=com;
	document.frmcareerpath.command.value="<%=Command.EDIT%>";
	document.frmcareerpath.action="employee_edit.jsp?employee_oid="+empOID;
	document.frmcareerpath.submit();
	}


function cmdListFirst(){
	document.frmcareerpath.command.value="<%=Command.FIRST%>";
	document.frmcareerpath.prev_command.value="<%=Command.FIRST%>";
	document.frmcareerpath.action="picture.jsp";
	document.frmcareerpath.submit();
}

function cmdListPrev(){
	document.frmcareerpath.command.value="<%=Command.PREV%>";
	document.frmcareerpath.prev_command.value="<%=Command.PREV%>";
	document.frmcareerpath.action="picture.jsp";
	document.frmcareerpath.submit();
	}

function cmdListNext(){
	document.frmcareerpath.command.value="<%=Command.NEXT%>";
	document.frmcareerpath.prev_command.value="<%=Command.NEXT%>";
	document.frmcareerpath.action="picture.jsp";
	document.frmcareerpath.submit();
}

function cmdListLast(){
	document.frmcareerpath.command.value="<%=Command.LAST%>";
	document.frmcareerpath.prev_command.value="<%=Command.LAST%>";
	document.frmcareerpath.action="picture.jsp";
	document.frmcareerpath.submit();
}


function fnTrapKD(){
	//alert(event.keyCode);
	switch(event.keyCode) {
		case <%=LIST_PREV%>:
			cmdListPrev();
			break;
		case <%=LIST_NEXT%>:
			cmdListNext();
			break;
		case <%=LIST_FIRST%>:
			cmdListFirst();
			break;
		case <%=LIST_LAST%>:
			cmdListLast();
			break;
		default:
			break;
	}
}

function cmdBackEmployeeList() {
    document.frmcareerpath.action = "employee_list.jsp?command=<%=Command.FIRST%>";
    document.frmcareerpath.submit();
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
<SCRIPT language=JavaScript>
<!--
function hideObjectForEmployee(){    
} 
	 
function hideObjectForLockers(){ 
}
	
function hideObjectForCanteen(){
}
	
function hideObjectForClinic(){
}

function hideObjectForMasterdata(){
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
</SCRIPT>
<style type="text/css">
    .tblStyle {border-collapse: collapse;font-size: 11px;}
    .tblStyle td {padding: 3px 5px; border: 1px solid #CCC;}
    .title_tbl {font-weight: bold;background-color: #DDD; color: #575757;}
    .title_page {color:#0db2e1; font-weight: bold; font-size: 14px; background-color: #EEE; border-left: 1px solid #0099FF; padding: 12px 18px;}

    body {color:#373737;}
    #menu_utama {padding: 9px 14px; border-left: 1px solid #0099FF; font-size: 14px; background-color: #F3F3F3;}
    #menu_title {color:#0099FF; font-size: 14px; font-weight: bold;}
    #menu_teks {color:#CCC;}
    #box_title {padding:9px; background-color: #D5D5D5; font-weight: bold; color:#575757; margin-bottom: 7px; }
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
    .breadcrumb {
        background-color: #EEE;
        color:#0099FF;
        padding: 7px 9px;
    }
    .navbar {
        font-family: sans-serif;
        font-size: 12px;
        background-color: #0084ff;
        padding: 7px 9px;
        color : #FFF;
        border-top:1px solid #0084ff;
        border-bottom: 1px solid #0084ff;
    }
    .navbar ul {
        list-style-type: none;
        margin: 0;
        padding: 0;
    }

    .navbar li {
        padding: 7px 9px;
        display: inline;
        cursor: pointer;
    }

    .navbar li:hover {
        background-color: #0b71d0;
        border-bottom: 1px solid #033a6d;
    }

    .active {
        background-color: #0b71d0;
        border-bottom: 1px solid #033a6d;
    }
    .title_part {color:#FF7E00; background-color: #F7F7F7; border-left: 1px solid #0099FF; padding: 9px 11px;}
</style>
        <style type="text/css">
            body {background-color: #EEE;}
            .header {
                
            }
            .content-main {
                background-color: #FFF;
                margin: 25px 23px 59px 23px;
                border: 1px solid #DDD;
                border-radius: 5px;
            }
            .content-info {
                padding: 21px;
                border-bottom: 1px solid #E5E5E5;
            }
            .content-title {
                padding: 21px;
                border-bottom: 1px solid #E5E5E5;
                margin-bottom: 5px;
                background-color: #EEE;
            }
            #title-large {
                color: #575757;
                font-size: 16px;
                font-weight: bold;
            }
            #title-small {
                color:#797979;
                font-size: 11px;
            }
            .content {
                padding: 21px;
            }
            .btn {
                background: #ebebeb;
                border-radius: 3px;
                font-family: Arial;
                color: #7a7a7a;
                font-size: 12px;
                padding: 5px 11px 5px 11px;
                border: solid #d9d9d9 1px;
                text-decoration: none;
            }

            .btn:hover {
                color: #474747;
                background: #ddd;
                text-decoration: none;
                border: 1px solid #C5C5C5;
            }
            
            .btn-small {
                padding: 3px; border: 1px solid #CCC; 
                background-color: #EEE; color: #777777; 
                font-size: 11px; cursor: pointer;
            }
            .btn-small:hover {border: 1px solid #999; background-color: #CCC; color: #FFF;}
            
            .tbl-main {border-collapse: collapse; font-size: 11px; background-color: #FFF; margin: 0px;}
            .tbl-main td {padding: 4px 7px; border: 1px solid #DDD; }
            #tbl-title {font-weight: bold; background-color: #F5F5F5; color: #575757;}
            
            .tbl-small {border-collapse: collapse; font-size: 11px; background-color: #FFF;}
            .tbl-small td {padding: 2px 3px; border: 1px solid #DDD; }
            
            #caption {padding: 7px 0px 2px 0px; font-size: 12px; font-weight: bold; color: #575757;}
            #div_input {}
            
            .form-style {
                font-size: 12px;
                color: #575757;
                border: 1px solid #DDD;
                border-radius: 5px;
            }
            .form-title {
                padding: 11px 21px;
                margin-bottom: 2px;
                border-bottom: 1px solid #DDD;
                background-color: #EEE;
                border-top-left-radius: 5px;
                border-top-right-radius: 5px;
                font-weight: bold;
            }
            .form-content {
                padding: 21px;
            }
            .form-footer {
                border-top: 1px solid #DDD;
                padding: 11px 21px;
                margin-top: 2px;
                background-color: #EEE;
                border-bottom-left-radius: 5px;
                border-bottom-right-radius: 5px;
            }
            #confirm {
                padding: 18px 21px;
                background-color: #FF6666;
                color: #FFF;
                border: 1px solid #CF5353;
            }
            #btn-confirm {
                padding: 3px; border: 1px solid #CF5353; 
                background-color: #CF5353; color: #FFF; 
                font-size: 11px; cursor: pointer;
            }
            .footer-page {
                
            }
            
        </style>
<!-- #EndEditable -->
</head>  

<body>
        <div class="header">
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
            <%if (headerStyle && !verTemplate.equalsIgnoreCase("0")) {%> 
            <%@include file="../../styletemplate/template_header.jsp" %>
            <%} else {%>
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
            </table>
        </div>
        <div id="menu_utama">
            <span id="menu_title"><%=dictionaryD.getWord(I_Dictionary.EMPLOYEE)%> <strong style="color:#333;"> / </strong> <%=dictionaryD.getWord(I_Dictionary.PICTURE) %></span>
        </div>
        <% if (oidEmployee != 0) {%>
            <div class="navbar">

                  <ul style="margin-left: 97px">
                    <li class=""> <a href="employee_edit.jsp?employee_oid=<%=oidEmployee%>&prev_command=<%=Command.EDIT%>" class="tablink"><%=dictionaryD.getWord(I_Dictionary.PERSONAL_DATA)%></a> </li>
                    <li class=""> <a href="familymember.jsp?employee_oid=<%=oidEmployee%>" class="tablink"><%=dictionaryD.getWord(I_Dictionary.FAMILY_MEMBER) %></a> </li>
                    <li class=""> <a href="emplanguage.jsp?employee_oid=<%=oidEmployee%>" class="tablink"><%=dictionaryD.getWord(I_Dictionary.COMPETENCIES) %></a> </li>
                    <li class=""> <a href="empeducation.jsp?employee_oid=<%=oidEmployee%>" class="tablink"><%=dictionaryD.getWord(I_Dictionary.EDUCATION) %></a> </li>
                    <li class=""> <a href="experience.jsp?employee_oid=<%=oidEmployee%>" class="tablink"><%=dictionaryD.getWord(I_Dictionary.EXPERIENCE) %></a></li>
                    <li class=""> <a href="careerpath.jsp?employee_oid=<%=oidEmployee%>" class="tablink"><%=dictionaryD.getWord(I_Dictionary.CAREER_PATH) %></a> </li>
                    <li class=""> <a href="training.jsp?employee_oid=<%=oidEmployee%>" class="tablink"><%=dictionaryD.getWord(I_Dictionary.TRAINING_ON_DATABANK)%></a> </li>
                    <li class=""> <a href="warning.jsp?employee_oid=<%=oidEmployee%>" class="tablink"><%=dictionaryD.getWord(I_Dictionary.WARNING) %></a> </li>
                    <li class=""> <a href="reprimand.jsp?employee_oid=<%=oidEmployee%>" class="tablink"><%=dictionaryD.getWord(I_Dictionary.REPRIMAND) %></a> </li>
                    <li class=""> <a href="award.jsp?employee_oid=<%=oidEmployee%>" class="tablink"><%=dictionaryD.getWord(I_Dictionary.AWARD) %></a> </li>
                    <li class="active"> <a href="picture.jsp?employee_oid=<%=oidEmployee%>" class="tablink"><%=dictionaryD.getWord(I_Dictionary.PICTURE) %></a> </li>
                    <li class=""> <a href="doc_relevant.jsp?employee_oid=<%=oidEmployee%>" class="tablink"><%=dictionaryD.getWord(I_Dictionary.RELEVANT_DOCS) %></a> </li>
                  </ul>
            </div>
        <%}%>
        <div class="content-main">
            <form name="frmcareerpath" method ="post"  enctype="multipart/form-data" action="upload_pict_process.jsp">
                    <input type="hidden" name="command" value="<%=iCommand%>">
                    <input type="hidden" name="vectSize" value="<%=vectSize%>">
                    <input type="hidden" name="start" value="<%=start%>">
                    <input type="hidden" name="prev_command" value="<%=prevCommand%>">
                    <input type="hidden" name="emp_picture_oid" value="<%=oidEmpPicture%>">
                    <input type="hidden" name="department_oid" value="<%=oidDepartment%>">
                    <input type="hidden" name="employee_oid" value="<%=oidEmployee%>">
                    <input type="hidden" name="<%=FrmCareerPath.fieldNames[FrmCareerPath.FRM_FIELD_DEPARTMENT_ID]%>" value="<%=oidDepartment%>">
                    <div class="content">
                    <table width="100%" border="0" cellspacing="0" cellpadding="0">

                      <tr> 
                        <td  style="background-color:<%=bgColorContent%>;"> 
                          <table width="100%" border="0" cellspacing="1" cellpadding="1" class="tablecolor">
                            <tr> 
                              <td valign="top"> 
                                <table style="border:1px solid <%=garisContent%>"  width="100%" border="0" cellspacing="0" cellpadding="0" class="tabbg">
                                  <tr align="left" valign="top"> 
                                    <td height="8"  colspan="3"> 
                                      <table width="100%" border="0" cellspacing="0" cellpadding="0">
                                        <tr align="left" valign="top"> 
                                          <td height="14" valign="middle" colspan="3" class="listedittitle">&nbsp;</td>
                                        </tr>
                                        <% if(oidEmployee != 0){
                                                Employee employee = new Employee();
                                                try{
                                                     employee = PstEmployee.fetchExc(oidEmployee);
                                                }catch(Exception exc){
                                                     employee = new Employee();
                                                }
                                          %>
                                        <tr align="left" valign="top"> 
                                          <td height="14" valign="middle" colspan="3" class="listedittitle"> 
                                            <table width="100%" height="184" border="0" cellpadding="1" cellspacing="2">
                                              <tr> 
                                                <td width="18%" height="25">Employee 
                                                  Number </td>
                                                <td width="1%">:</td>
                                                <td width="41%"><%=employee.getEmployeeNum()%></td>
                                                <td width="40%" rowspan="6" valign="top"> 
                                                  <table width="48%" height="158" border="0">
                                                    <tr> 

                                                      <td height="154" valign="top">  <%
                                                          if(pictPath!=null && pictPath.length()>0)
                                                          {
                                                                out.println("<img width=\"250\"  src=\""+approot+"/"+pictPath+"\">");
                                                          }
                                                          else{
                                                                 %>
                                                                        <img width="250"  src="<%=approot%>/imgcache/no_photo.JPEG"></image>
                                                                 <%

                                                          }	%>  </td>
</tr>
                                                         </table></td>
                                              </tr>
                                              <tr> 
                                                <td width="18%" height="21">Name</td>
                                                <td width="1%">:</td>
                                                <td width="41%"><%=employee.getFullName()%></td>
                                              </tr>
                                              <% Department department = new Department();
                                                   try{
                                                        department = PstDepartment.fetchExc(employee.getDepartmentId());
                                                   }catch(Exception exc){
                                                        department = new Department();
                                                   }
                                                %>
                                              <tr> 
                                                <td width="18%" height="21">Department</td>
                                                <td width="1%">:</td>
                                                <td width="41%"><%=department.getDepartment()%></td>
                                              </tr>
                                              <tr> 
                                                <td width="18%" height="21">Address</td>
                                                <td width="1%">:</td>
                                                <td width="41%"><%=employee.getAddress()%></td>
                                              </tr>
                                              <%//if((iCommand ==Command.ADD)||(iCommand==Command.SAVE)&&(frmEmpPicture.errorSize()>0)||(iCommand==Command.EDIT)||(iCommand==Command.ASK)||(iCommand==Command.LIST)){ %>

                                              <%//} else{
                                                                                                      %>
                                                                                                      <tr> 
                                                <td colspan="3">&nbsp;</td>
                                              </tr>
                                                                                                       <tr> 
                                                <td colspan="3">&nbsp;</td>
                                              </tr>
                                                                                                      <%//}%>
                                            </table>
                                                                                                    <table width="100%" border="0">
                                              <tr> 
                                                <td height="21" colspan="3"><strong>Upload 
                                                  Picture</strong><i><font color="#CC0000"></font></i></td>
                                              </tr>
                                              <tr> 
                                                <td height="21" colspan="3"><i><font color="#CC0000"> 
                                                  ( Click browse... 
                                                  if you want to add/edit 
                                                  picture )</font></i></i></td>
                                              </tr>
                                              <tr> 
                                                <td height="26" colspan="3"><input type="file" name="pict" size="60" height="100"></td>
                                              </tr>
                                            </table>

                                          </td>
                                        </tr>

                                        <%} %>
                                                                                       <% if(iCommand==Command.NONE || iCommand==Command.SAVE) {%>
                                        <tr align="left" valign="top"> 
                                          <td> 
                                            <table cellpadding="0" cellspacing="0" border="0">
                                              <tr> 
                                                <td width="4" height="25"><img src="<%=approot%>/images/spacer.gif" width="1" height="1"></td>
                                                <td width="24" height="25"><a href="javascript:cmdSave()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image261','','<%=approot%>/images/BtnNewOn.jpg',1)"><img name="Image261" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="Add new data"></a></td>
                                                <td width="6" height="25"><img src="<%=approot%>/images/spacer.gif" width="1" height="1"></td>
                                                <td height="25" valign="middle" colspan="3" width="951"><a href="javascript:cmdSave()" class="command">Save Picture</a> 
                                                </td>
                                              </tr>
                                            </table>
                                          </td>
                                        </tr>
                                                                                             <% }%>
                                      </table>
                                    </td>
                                  </tr>
                                  <tr align="left" valign="top"> 
                                    <td height="8" valign="middle" colspan="3"> 
                                      <%//if((iCommand ==Command.ADD)||(iCommand==Command.SAVE) || (iCommand==Command.NONE)&&(frmEmpPicture.errorSize()>0)||(iCommand==Command.EDIT)||(iCommand==Command.ASK)||(iCommand==Command.LIST)){%>
                                      <table width="100%" border="0" cellspacing="2" cellpadding="2">
                                        <tr align="left" valign="top" > 
                                          <td height="22" class="command"> <%
                                                                                                    ctrLine.setLocationImg(approot+"/images");
                                                                                                    ctrLine.initDefault();
                                                                                                    ctrLine.setTableWidth("80%");
                                                                                                    String scomDel = "javascript:cmdAsk('"+oidEmpPicture+"')";
                                                                                                    String sconDelCom = "javascript:cmdConfirmDelete('"+oidEmpPicture+"')";
                                                                                                    String scancel = "javascript:cmdEdit('"+oidEmpPicture+"')";
                                                                                                    ctrLine.setBackCaption("");
                                                                                                    ctrLine.setCommandStyle("buttonlink");
                                                                                                    ctrLine.setAddCaption("");
                                                                                                    ctrLine.setSaveCaption("Save Picture");
                                                                                                    ctrLine.setDeleteCaption("");
                                                                                                    ctrLine.setConfirmDelCaption("");

                                                            if (privDelete){
                                                                    ctrLine.setConfirmDelCommand(sconDelCom);
                                                                    ctrLine.setDeleteCommand(scomDel);
                                                                    ctrLine.setEditCommand(scancel);
                                                            }else{ 
                                                                    ctrLine.setConfirmDelCaption("");
                                                                    ctrLine.setDeleteCaption("");
                                                                    ctrLine.setEditCaption("");
                                                            }

                                                            if(privAdd == false  && privUpdate == false){
                                                                    ctrLine.setSaveCaption("");
                                                            }

                                                            if (privAdd == false){
                                                                    ctrLine.setAddCaption("");
                                                            }
                                                            %> <%= ctrLine.drawImage(iCommand, iErrCode, msgString)%> </td>
                                        </tr>
                                      </table>
                                      <%//}%>
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
                    </div>
                                    <div class="content">
                    <a id="btn" href="javascript:cmdBackEmployeeList()">Back to Employee List</a>
                </div>
             </form>
        </div>
        <div class="footer-page">
            <table>
                <%
                if (headerStyle && !verTemplate.equalsIgnoreCase("0")) {%>
                <tr>
                    <td valign="bottom"><%@include file="../../footer.jsp" %></td>
                </tr>
                <%} else {%>
                <tr> 
                    <td colspan="2" height="20" ><%@ include file = "../../main/footer.jsp" %></td>
                </tr>
                <%}%>
            </table>
        </div>
                              
</body>
</html>
