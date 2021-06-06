<% 
/* 
 * Page Name  		:  careerpath.jsp
 * Created on 		:  [date] [time] AM/PM 
 * 
 * @author  		: lkarunia 
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

<!-- package qdep -->
<%@ page import = "com.dimata.gui.jsp.*" %>
<%@ page import = "com.dimata.qdep.form.*" %>

<!--package harisma -->
<%@ page import = "com.dimata.harisma.entity.masterdata.*" %>
<%@ page import = "com.dimata.harisma.entity.employee.*" %>
<%@ page import = "com.dimata.harisma.form.employee.*" %>
<%@ page import = "com.dimata.harisma.entity.admin.*" %>
<%@ include file = "../../main/javainit.jsp" %>

<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_EMPLOYEE, AppObjInfo.G2_DATABANK, AppObjInfo.OBJ_DATABANK); %>
<%@ include file = "../../main/checkuser.jsp" %>
<%
/* Check privilege except VIEW, view is already checked on checkuser.jsp as basic access*/
boolean privAdd=userSession.checkPrivilege(AppObjInfo.composeCode(appObjCode, AppObjInfo.COMMAND_ADD));
boolean privUpdate=userSession.checkPrivilege(AppObjInfo.composeCode(appObjCode, AppObjInfo.COMMAND_UPDATE));
boolean privDelete=userSession.checkPrivilege(AppObjInfo.composeCode(appObjCode, AppObjInfo.COMMAND_DELETE));
%>

<!-- Jsp Block -->

<%!

	public String drawList(Vector objectClass ,  long workHistoryNowId)

	{
		ControlList ctrlist = new ControlList();
		ctrlist.setAreaWidth("90%");
		ctrlist.setListStyle("listgen");
		ctrlist.setTitleStyle("listgentitle");
		ctrlist.setCellStyle("listgensell");
		ctrlist.setHeaderStyle("listgentitle");
		ctrlist.addHeader("Department","20%");
		ctrlist.addHeader("Section","15%");
		ctrlist.addHeader("Position","15%");
		ctrlist.addHeader("Work From","15%");
		ctrlist.addHeader("Work To","15%");
		ctrlist.addHeader("Description","20%");

		ctrlist.setLinkRow(0);
		ctrlist.setLinkSufix("");
		Vector lstData = ctrlist.getData();
		Vector lstLinkData = ctrlist.getLinkData();
		ctrlist.setLinkPrefix("javascript:cmdEdit('");
		ctrlist.setLinkSufix("')");
		ctrlist.reset();
		int index = -1;

		for (int i = 0; i < objectClass.size(); i++) {
			CareerPath careerPath = (CareerPath)objectClass.get(i);
			 Vector rowx = new Vector();
			 if(workHistoryNowId == careerPath.getOID())
				 index = i;
						
			rowx.add(careerPath.getDepartment());
			rowx.add(careerPath.getSection());
			rowx.add(careerPath.getPosition());

			String str_dt_WorkFrom = ""; 
			try{
				Date dt_WorkFrom = careerPath.getWorkFrom();
				if(dt_WorkFrom==null){
					dt_WorkFrom = new Date();
				}

				str_dt_WorkFrom = Formater.formatDate(dt_WorkFrom, "dd MMMM yyyy");
			}catch(Exception e){ str_dt_WorkFrom = ""; }

			rowx.add(str_dt_WorkFrom);

			String str_dt_WorkTo = ""; 
			try{
				Date dt_WorkTo = careerPath.getWorkTo();
				if(dt_WorkTo==null){
					dt_WorkTo = new Date();
				}

				str_dt_WorkTo = Formater.formatTimeLocale(dt_WorkTo, "dd MMMM yyyy");
			}catch(Exception e){ str_dt_WorkTo = ""; }

			rowx.add(str_dt_WorkTo);

			rowx.add(careerPath.getDescription());

			lstData.add(rowx);
			lstLinkData.add(String.valueOf(careerPath.getOID()));
		}

		return ctrlist.draw(index);

		//return ctrlist.draw();
	}

%>
<%
int iCommand = FRMQueryString.requestCommand(request);
int start = FRMQueryString.requestInt(request, "start");
int prevCommand = FRMQueryString.requestInt(request, "prev_command");
long oidCareerPath = FRMQueryString.requestLong(request, "career_path_oid");
long oidEmployee = FRMQueryString.requestLong(request, "employee_oid");

/*variable declaration*/
int recordToGet = 10;
String msgString = "";
int iErrCode = FRMMessage.NONE;
String whereClause = PstCareerPath.fieldNames[PstCareerPath.FLD_EMPLOYEE_ID]+ " = "+oidEmployee;
String orderClause = PstCareerPath.fieldNames[PstCareerPath.FLD_WORK_FROM];

CtrlCareerPath ctrlCareerPath = new CtrlCareerPath(request);
ControlLine ctrLine = new ControlLine();
Vector listCareerPath = new Vector(1,1);
Vector listDepartment = PstDepartment.list(0, 0, "", "DEPARTMENT");
Vector listSection = new Vector(1,1);

/*switch statement */
iErrCode = ctrlCareerPath.action(iCommand , oidCareerPath, oidEmployee,request);
/* end switch*/
FrmCareerPath frmCareerPath = ctrlCareerPath.getForm();

CareerPath careerPath = ctrlCareerPath.getCareerPath();
msgString =  ctrlCareerPath.getMessage();

/*switch list CareerPath*/
if((iCommand == Command.SAVE) && (iErrCode == FRMMessage.NONE) && (oidCareerPath == 0))
	start = PstCareerPath.findLimitStart(careerPath.getOID(),recordToGet, whereClause, orderClause);

/*count list All CareerPath*/
int vectSize = PstCareerPath.getCount(whereClause);

if((iCommand == Command.FIRST || iCommand == Command.PREV )||
  (iCommand == Command.NEXT || iCommand == Command.LAST)){
		start = ctrlCareerPath.actionList(iCommand, start, vectSize, recordToGet);
 } 
/* end switch list*/

/* get record to display */
listCareerPath = PstCareerPath.list(start,recordToGet, whereClause , orderClause);

/*handle condition if size of record to display = 0 and start > 0 	after delete*/
if (listCareerPath.size() < 1 && start > 0)
{
	 if (vectSize - recordToGet > recordToGet)
			start = start - recordToGet;   //go to Command.PREV
	 else{
		 start = 0 ;
		 iCommand = Command.FIRST;
		 prevCommand = Command.FIRST; //go to Command.FIRST
	 }
	 listCareerPath = PstCareerPath.list(start,recordToGet, whereClause , orderClause);
}

long oidDepartment = 0;
if(oidEmployee != 0){
	Employee employee = new Employee();
	try{
		 employee = PstEmployee.fetchExc(oidEmployee);
		 oidDepartment = employee.getDepartmentId();
	}catch(Exception exc){
		 employee = new 	Employee();
	}
}
//listSection = PstSection.list(0,500,PstSection.fieldNames[PstSection.FLD_DEPARTMENT_ID]+ " = "+oidDepartment,"SECTION");
listSection = PstSection.list(0,0,"","SECTION");

%>
<html><!-- #BeginTemplate "/Templates/maintab.dwt" -->
<head>
<!-- #BeginEditable "doctitle" --> 
<title><%=dictionaryD.getWord(I_Dictionary.CAREER_PATH) %></title>
<script language="JavaScript">
function cmdAdd(){
	document.frmcareerpath.career_path_oid.value="0";
	document.frmcareerpath.command.value="<%=Command.ADD%>";
	document.frmcareerpath.prev_command.value="<%=prevCommand%>";
	document.frmcareerpath.action="careerpath.jsp";
	document.frmcareerpath.submit();
}

function cmdAsk(oidCareerPath){
	document.frmcareerpath.career_path_oid.value=oidCareerPath;
	document.frmcareerpath.command.value="<%=Command.ASK%>";
	document.frmcareerpath.prev_command.value="<%=prevCommand%>";
	document.frmcareerpath.action="careerpath.jsp";
	document.frmcareerpath.submit();
}

function cmdConfirmDelete(oidCareerPath){
	document.frmcareerpath.career_path_oid.value=oidCareerPath;
	document.frmcareerpath.command.value="<%=Command.DELETE%>";
	document.frmcareerpath.prev_command.value="<%=prevCommand%>";
	document.frmcareerpath.action="careerpath.jsp";
	document.frmcareerpath.submit();
}
function cmdSave(){
	document.frmcareerpath.command.value="<%=Command.SAVE%>";
	document.frmcareerpath.prev_command.value="<%=prevCommand%>";
	document.frmcareerpath.action="careerpath.jsp";
	document.frmcareerpath.submit();
	}

function cmdEdit(oidCareerPath){
	document.frmcareerpath.career_path_oid.value=oidCareerPath;
	document.frmcareerpath.command.value="<%=Command.EDIT%>";
	document.frmcareerpath.prev_command.value="<%=prevCommand%>";
	document.frmcareerpath.action="careerpath.jsp";
	document.frmcareerpath.submit();
	}

function cmdCancel(oidCareerPath){
	document.frmcareerpath.career_path_oid.value=oidCareerPath;
	document.frmcareerpath.command.value="<%=Command.EDIT%>";
	document.frmcareerpath.prev_command.value="<%=prevCommand%>";
	document.frmcareerpath.action="careerpath.jsp";
	document.frmcareerpath.submit();
}

function cmdBack(){
	document.frmcareerpath.command.value="<%=Command.BACK%>";
	document.frmcareerpath.action="careerpath.jsp";
	document.frmcareerpath.submit();
}

function cmdListSection(){	
	document.frmcareerpath.action="careerpath.jsp";
	document.frmcareerpath.submit();
}

function cmdBackEmp(empOID){
	document.frmcareerpath.employee_oid.value=empOID;
	document.frmcareerpath.command.value="<%=Command.EDIT%>";	
	document.frmcareerpath.action="employee_edit.jsp";
	document.frmcareerpath.submit();
	}


function cmdListFirst(){
	document.frmcareerpath.command.value="<%=Command.FIRST%>";
	document.frmcareerpath.prev_command.value="<%=Command.FIRST%>";
	document.frmcareerpath.action="careerpath.jsp";
	document.frmcareerpath.submit();
}

function cmdListPrev(){
	document.frmcareerpath.command.value="<%=Command.PREV%>";
	document.frmcareerpath.prev_command.value="<%=Command.PREV%>";
	document.frmcareerpath.action="careerpath.jsp";
	document.frmcareerpath.submit();
	}

function cmdListNext(){
	document.frmcareerpath.command.value="<%=Command.NEXT%>";
	document.frmcareerpath.prev_command.value="<%=Command.NEXT%>";
	document.frmcareerpath.action="careerpath.jsp";
	document.frmcareerpath.submit();
}

function cmdListLast(){
	document.frmcareerpath.command.value="<%=Command.LAST%>";
	document.frmcareerpath.prev_command.value="<%=Command.LAST%>";
	document.frmcareerpath.action="careerpath.jsp";
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
<!-- #EndEditable -->
</head>  

<body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" onLoad="MM_preloadImages('<%=approot%>/images/BtnNewOn.jpg')">
<table width="100%" border="0" cellspacing="0" cellpadding="0" height="100%" bgcolor="#F9FCFF">
  <tr> 
    <td ID="TOPTITLE" background="<%=approot%>/images/HRIS_HeaderBg3.jpg" width="100%" height="54"> 
      <!-- #BeginEditable "header" --> 
      <%@ include file = "../../main/header.jsp" %>
      <!-- #EndEditable --> </td>
  </tr>
  <tr> 
    <td bgcolor="#9BC1FF"  ID="MAINMENU" valign="middle" height="15"> <!-- #BeginEditable "menumain" --> 
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
  <tr> 
    <td width="88%" valign="top" align="left"> 
      <table width="100%" border="0" cellspacing="3" cellpadding="2">
        <tr> 
          <td width="100%"> 
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
              <tr> 
                <td> <font color="#FF6600" face="Arial"><strong> <!-- #BeginEditable "contenttitle" -->Employee 
                  &gt; Career Path<!-- #EndEditable --> </strong></font> </td>
              </tr>
              <tr> 
                <td> 
                  <table width="100%" border="0" cellspacing="0" cellpadding="0">
                    <tr> 
                      <td valign="top"> 
                        <table width="100%" border="0" cellspacing="1" cellpadding="1">
                          <tr> 
                            <td valign="top"> <!-- #BeginEditable "content" --> 
                              <form name="frmcareerpath" method ="post" action="careerpath.jsp">
                                <input type="hidden" name="command" value="<%=iCommand%>">
                                <input type="hidden" name="vectSize" value="<%=vectSize%>">
                                <input type="hidden" name="start" value="<%=start%>">
                                <input type="hidden" name="prev_command" value="<%=prevCommand%>">
                                <input type="hidden" name="career_path_oid" value="<%=oidCareerPath%>">
                                <input type="hidden" name="department_oid" value="<%=oidDepartment%>">
                                <input type="hidden" name="employee_oid" value="<%=oidEmployee%>">
                                <input type="hidden" name="<%=FrmCareerPath.fieldNames[FrmCareerPath.FRM_FIELD_DEPARTMENT_ID]%>" value="<%=oidDepartment%>">
                                <table width="100%" border="0" cellspacing="0" cellpadding="0">
                                  <% if(oidEmployee != 0){%>
                                  <tr> 
                                    <td> 
									<br>
                                      <table width="100%" border="0" cellspacing="2" cellpadding="2" height="26">
                                        <tr> 
                                          <td width="2%" bgcolor="#FFFFFF">&nbsp;</td>
                                          <td width="11%" nowrap bgcolor="#0066CC"> 
                                            <div align="center" class="tablink"><a href="javascript:cmdBackEmp('<%=oidEmployee%>')" class="tablink">Personal 
                                              Data</a></div>
                                          </td>
                                          <td width="12%" nowrap bgcolor="#0066CC"> 
                                            <div align="center"  class="tablink"><a href="familymember.jsp?employee_oid=<%=oidEmployee%>" class="tablink">Family 
                                              Member</a></div>
                                          </td>
                                          <td width="9%" nowrap bgcolor="#0066CC"> 
                                            <div align="center"  class="tablink"><a href="emplanguage.jsp?employee_oid=<%=oidEmployee%>" class="tablink">Language</a></div>
                                          </td>
                                          <td width="10%" nowrap bgcolor="#0066CC"> 
                                            <div align="center"  class="tablink"><a href="empeducation.jsp?employee_oid=<%=oidEmployee%>" class="tablink"><%=dictionaryD.getWord(I_Dictionary.EDUCATION) %></a></div>
                                          </td>
                                          <td width="9%" nowrap bgcolor="#0066CC"> 
                                            <div align="center"  class="tablink"><a href="experience.jsp?employee_oid=<%=oidEmployee%>" class="tablink"><span class="tablink"><%=dictionaryD.getWord(I_Dictionary.EXPERIENCE) %></span></a></div>
                                          </td>
                                          <td width="10%" nowrap bgcolor="#66CCFF"> 
                                            <div align="center"><font class="tablink" ><span class="tablink">Career 
                                              Path</span></font></div>
                                          </td>
                                          <td width="37%">&nbsp;</td>
                                        </tr>
                                      </table>
                                    </td>
                                  </tr>
                                  <%}%>
                                  <tr> 
                                    <td class="tablecolor"> 
                                      <table width="100%" border="0" cellspacing="1" cellpadding="1" class="tablecolor">
                                        <tr> 
                                          <td valign="top"> 
                                            <table width="100%" border="0" cellspacing="0" cellpadding="0" class="tabbg">
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
                                                        <table width="100%" border="0" cellspacing="2" cellpadding="1">
                                                          <tr> 
                                                            <td width="17%">Employee 
                                                              Number </td>
                                                            <td width="2%">:</td>
                                                            <td width="81%"><%=employee.getEmployeeNum()%></td>
                                                          </tr>
                                                          <tr> 
                                                            <td width="17%">Name</td>
                                                            <td width="2%">:</td>
                                                            <td width="81%"><%=employee.getFullName()%></td>
                                                          </tr>
                                                          <% Department department = new Department();
                                                               try{
                                                                    department = PstDepartment.fetchExc(employee.getDepartmentId());
                                                               }catch(Exception exc){
                                                                    department = new Department();
                                                               }
                                                            %>
                                                          <tr> 
                                                            <td width="17%"><%=dictionaryD.getWord(I_Dictionary.DEPARTMENT) %></td>
                                                            <td width="2%">:</td>
                                                            <td width="81%"><%=department.getDepartment()%></td>
                                                          </tr>
                                                          <tr> 
                                                            <td width="17%">Address</td>
                                                            <td width="2%">:</td>
                                                            <td width="81%"><%=employee.getAddress()%></td>
                                                          </tr>
                                                        </table>
                                                      </td>
                                                    </tr>
                                                    <tr align="left" valign="top"> 
                                                      <td height="14" valign="middle" colspan="3" class="listedittitle">&nbsp;</td>
                                                    </tr>
                                                    <%}%>
                                                    <%
                                                    try{
                                                            if (listCareerPath.size()>0){
                                                    %>
                                                    <tr align="left" valign="top"> 
                                                      <td height="22" valign="middle" colspan="3" class="listtitle"> 
                                                        &nbsp;Career Path List 
                                                      </td>
                                                    </tr>
                                                    <tr align="left" valign="top"> 
                                                      <td height="22" valign="middle" colspan="3"> 
                                                        <%= drawList(listCareerPath,iCommand == Command.SAVE?careerPath.getOID():oidCareerPath)%> </td>
                                                    </tr>
                                                    <%  }else { %>
                                                    <tr align="left" valign="top"> 
                                                      <td height="22" valign="middle" colspan="3" class="comment"> 
                                                        No Career Path available 
                                                      </td>
                                                    </tr>
                                                    <% } 
                                                      }catch(Exception exc){ 
                                                      }%>
                                                    <tr align="left" valign="top"> 
                                                      <td height="8" align="left" colspan="3" class="listedittitle"> 
                                                        <span class="command"> 
                                                        <% 
                                                           int cmd = 0;
                                                                   if ((iCommand == Command.FIRST || iCommand == Command.PREV )|| 
                                                                        (iCommand == Command.NEXT || iCommand == Command.LAST))
                                                                                cmd =iCommand; 
                                                           else{
                                                                  if(iCommand == Command.NONE || prevCommand == Command.NONE)
                                                                        cmd = Command.FIRST;
                                                                  else{ 
                                                                        if((iCommand == Command.SAVE) && (iErrCode == FRMMessage.NONE) && (oidCareerPath == 0))
                                                                                cmd = PstCareerPath.findLimitCommand(start,recordToGet,vectSize);
                                                                        else
                                                                                cmd =prevCommand; 
                                                                  }
                                                           } 
                                                        %>
                                                        <% ctrLine.setLocationImg(approot+"/images");
                                                        ctrLine.initDefault();
                                                         %>
                                                        <%=ctrLine.drawImageListLimit(cmd,vectSize,start,recordToGet)%> </span> </td>
                                                    </tr>
                                                    <% if(iCommand == Command.NONE || (iCommand == Command.SAVE && frmCareerPath.errorSize()<1)|| iCommand == Command.DELETE || iCommand == Command.BACK ||
                                                      iCommand == Command.FIRST || iCommand == Command.PREV || iCommand == Command.NEXT || iCommand == Command.LAST ){%>
                                                    <% if(privAdd){%>
                                                    <tr align="left" valign="top"> 
                                                      <td> 
                                                        <table cellpadding="0" cellspacing="0" border="0">
                                                          <tr> 
                                                            <td>&nbsp;</td>
                                                          </tr>
                                                          <tr> 
                                                            <td width="4" height="25"><img src="<%=approot%>/images/spacer.gif" width="1" height="1"></td>
                                                            <td width="24" height="25"><a href="javascript:cmdAdd()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image261','','<%=approot%>/images/BtnNewOn.jpg',1)"><img name="Image261" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="Add new data"></a></td>
                                                            <td width="6" height="25"><img src="<%=approot%>/images/spacer.gif" width="1" height="1"></td>
                                                            <td height="25" valign="middle" colspan="3" width="951"><a href="javascript:cmdAdd()" class="command">Add 
                                                              New Career Path</a> 
                                                            </td>
                                                          </tr>
                                                        </table>
                                                      </td>
                                                    </tr>
                                                    <% } 
                                                    }%>
                                                  </table>
                                                </td>
                                              </tr>
                                              <tr> 
                                                <td>&nbsp; </td>
                                              </tr>
                                              <tr align="left" valign="top"> 
                                                <td height="8" valign="middle" colspan="3"> 
                                                  <%if((iCommand ==Command.ADD)||(iCommand==Command.SAVE)&&(frmCareerPath.errorSize()>0)||(iCommand==Command.EDIT)||(iCommand==Command.ASK)||(iCommand==Command.LIST)){%>
                                                  <table width="100%" border="0" cellspacing="2" cellpadding="2">
                                                    <tr> 
                                                      <td colspan="2"><b class="listtitle">Career 
                                                        Path Editor</b></td>
                                                    </tr>
                                                    <tr> 
                                                      <td width="100%" colspan="2"> 
                                                        <table border="0" cellspacing="2" cellpadding="2" width="100%">
                                                          <tr align="left" valign="top"> 
                                                            <td valign="top" width="17%">Department 
                                                            </td>
                                                            <td width="83%"> 
                                                              <%   Vector department_value = new Vector(1,1);
                                                                   Vector department_key = new Vector(1,1);
                                                                   for(int i=0;i<listDepartment.size();i++){
                                                                    Department department = (Department)listDepartment.get(i);
                                                                    department_value.add(""+department.getOID());
                                                                    department_key.add(department.getDepartment());
                                                                   }

                                                                   String selDept =""+careerPath.getDepartmentId();
                                                                   if(careerPath.getDepartmentId() == 0)
                                                                                selDept = ""+oidDepartment;
                                                               %>
                                                              <%= ControlCombo.draw("department","formElemen",null, selDept, department_value, department_key) %> * <%= frmCareerPath.getErrorMsg(FrmCareerPath.FRM_FIELD_DEPARTMENT_ID) %> </td>
                                                          </tr>
                                                          <tr align="left" valign="top"> 
                                                            <td valign="top" width="17%">Section 
                                                            </td>
                                                            <td width="83%"> 
                                                              <%  Vector section_value = new Vector(1,1);
                                                                      Vector section_key = new Vector(1,1);
                                                                      for(int i = 0;i< listSection.size();i++){
                                                                            Section section = (Section)listSection.get(i);
                                                                            section_key.add(section.getSection());
                                                                            section_value.add(""+section.getOID());
                                                                      }
                                                               %>
                                                              <% if(listSection != null && listSection.size()>0){%>
                                                              <%= ControlCombo.draw(frmCareerPath.fieldNames[FrmCareerPath.FRM_FIELD_SECTION_ID],"formElemen",null, ""+careerPath.getSectionId(),section_value, section_key) %> 
                                                              <% }else{%>
                                                              <font class="comment">No 
                                                              Section available</font> 
                                                              <%}%>
                                                              * <%= frmCareerPath.getErrorMsg(FrmCareerPath.FRM_FIELD_SECTION_ID) %> </td>
                                                          </tr>
                                                          <tr align="left" valign="top"> 
                                                            <td valign="top" width="17%">Position 
                                                            </td>
                                                            <td width="83%"> 
                                                              <% Vector position_value = new Vector(1,1);
												  	 			 Vector position_key = new Vector(1,1);
																 Vector listPosition = PstPosition.list(0, 0, "", "POSITION");  
																   for(int i=0;i <listPosition.size();i++){
																		Position position = (Position)listPosition.get(i);
																		position_value.add(""+position.getOID());
																		position_key.add(position.getPosition());
																   }
															   %>
                                                              <%= ControlCombo.draw(frmCareerPath.fieldNames[FrmCareerPath.FRM_FIELD_POSITION_ID],"formElemen",null, ""+careerPath.getPositionId(), position_value, position_key) %> * <%= frmCareerPath.getErrorMsg(FrmCareerPath.FRM_FIELD_POSITION_ID) %> </td>
                                                          </tr>
                                                          <tr align="left" valign="top"> 
                                                            <td valign="top" width="17%">Work 
                                                              From</td>
                                                            <td width="83%"> <%=	ControlDate.drawDateWithStyle(frmCareerPath.fieldNames[FrmCareerPath.FRM_FIELD_WORK_FROM], careerPath.getWorkFrom()==null?new Date():careerPath.getWorkFrom(), 5,-30,"formElemen") %> to 
                                                              <%=	ControlDate.drawDateWithStyle(frmCareerPath.fieldNames[FrmCareerPath.FRM_FIELD_WORK_TO], careerPath.getWorkTo()==null?new Date():careerPath.getWorkTo(), 5,-35,"formElemen") %> * 
                                                              <% String strStart = frmCareerPath.getErrorMsg(FrmCareerPath.FRM_FIELD_WORK_FROM);
															  	 String strEnd = frmCareerPath.getErrorMsg(FrmCareerPath.FRM_FIELD_WORK_TO);																
																 if((strStart.length()>0)&&(strEnd.length()>0)){
															  		%>
                                                              <%= strStart %> 
                                                              <%}else{
																		if((strStart.length()>0)||(strEnd.length()>0)){%>
                                                              <%= strStart.length()>0?strStart:strEnd %> 
                                                              <% }
																	}%>
                                                            </td>
                                                          </tr>
                                                          <tr align="left" valign="top"> 
                                                            <td valign="top" width="17%">Description</td>
                                                            <td width="83%"> 
                                                              <textarea name="<%=frmCareerPath.fieldNames[FrmCareerPath.FRM_FIELD_DESCRIPTION] %>" class="elemenForm" cols="30" rows="3"><%= careerPath.getDescription() %></textarea>
                                                            </td>
                                                          </tr>
                                                        </table>
                                                      </td>
                                                    </tr>
                                                    <tr align="left" valign="top" > 
                                                      <td colspan="2" class="command"> 
                                                        <%
									ctrLine.setLocationImg(approot+"/images");
									ctrLine.initDefault();
									ctrLine.setTableWidth("80%");
									String scomDel = "javascript:cmdAsk('"+oidCareerPath+"')";
									String sconDelCom = "javascript:cmdConfirmDelete('"+oidCareerPath+"')";
									String scancel = "javascript:cmdEdit('"+oidCareerPath+"')";
									ctrLine.setBackCaption("Back to List Career Path");
									ctrLine.setCommandStyle("buttonlink");
									ctrLine.setAddCaption("Add Career Path");
									ctrLine.setSaveCaption("Save Career Path");
									ctrLine.setDeleteCaption("Delete Career Path");
									ctrLine.setConfirmDelCaption("Delete Career Path");

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
									%>
                                                        <%= ctrLine.drawImage(iCommand, iErrCode, msgString)%> </td>
                                                    </tr>
                                                    <tr> 
                                                      <td width="13%">&nbsp;</td>
                                                      <td width="87%">&nbsp;</td>
                                                    </tr>
                                                    <tr align="left" valign="top" > 
                                                      <td colspan="3"> 
                                                        <div align="left"></div>
                                                      </td>
                                                    </tr>
                                                  </table>
                                                  <%}%>
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
                              </form>
                              <!-- #EndEditable --> </td>
                          </tr>
                        </table>
                      </td>
                    </tr>
                    <tr> 
                      <td >&nbsp;</td>
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
  <tr> 
    <td colspan="2" height="20" > <!-- #BeginEditable "footer" --> 
      <%@ include file = "../../main/footer.jsp" %>
      <!-- #EndEditable --> </td>
  </tr>
</table>
</body>
<!-- #BeginEditable "script" --> 
<script language="JavaScript">
	var oBody = document.body;
	var oSuccess = oBody.attachEvent('onkeydown',fnTrapKD);
</script>
<!-- #EndEditable --> 
<!-- #EndTemplate --></html>
