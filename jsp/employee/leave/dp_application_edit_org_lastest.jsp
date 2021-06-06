
<% 
/* 
 * Page Name  		:  leave_edit.jsp
 * Created on 		:  [date] [time] AM/PM 
 * 
 * @author  		: karya 
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
<%@ page import = "com.dimata.harisma.entity.admin.*" %>
<%@ page import = "com.dimata.harisma.entity.masterdata.*" %>
<%@ page import = "com.dimata.harisma.entity.employee.*" %>
<%@ page import = "com.dimata.harisma.entity.attendance.*" %>
<%@ page import = "com.dimata.harisma.entity.leave.*" %>
<%@ page import = "com.dimata.harisma.form.attendance.*" %>
<%@ page import = "com.dimata.harisma.form.leave.*" %>
<%@ page import = "com.dimata.harisma.session.employee.*" %>
<%@ page import = "com.dimata.harisma.session.attendance.*" %>

<%@ include file = "../../main/javainit.jsp" %>
<%  int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_EMPLOYEE, AppObjInfo.G2_LEAVE_APPLICATION, AppObjInfo.OBJ_DP_APPLICATION); %>
<%@ include file = "../../main/checkuser.jsp" %>

<%
/* Check privilege except VIEW, view is already checked on checkuser.jsp as basic access*/

//boolean privStart=userSession.checkPrivilege(AppObjInfo.composeCode(appObjCode, AppObjInfo.COMMAND_VIEW));
//boolean privAdd=userSession.checkPrivilege(AppObjInfo.composeCode(appObjCode, AppObjInfo.COMMAND_ADD));
//boolean privUpdate=userSession.checkPrivilege(AppObjInfo.composeCode(appObjCode, AppObjInfo.COMMAND_UPDATE));
//boolean privDelete=userSession.checkPrivilege(AppObjInfo.composeCode(appObjCode, AppObjInfo.COMMAND_DELETE));
//boolean privPrint=userSession.checkPrivilege(AppObjInfo.composeCode(appObjCode, AppObjInfo.COMMAND_PRINT));
%>

<!-- Jsp Block -->
<%
int iCommand = FRMQueryString.requestCommand(request);
int start = FRMQueryString.requestInt(request,"start");
long oidDpApplication = FRMQueryString.requestLong(request, "hidden_dp_application_id");
boolean depHeadAuthorize = FRMQueryString.requestBoolean(request, "hidden_dep_head_authorize");
long approvalId = FRMQueryString.requestLong(request, "manager_id");

// ngambil dari form emp schedule
long oidEmployee = FRMQueryString.requestLong(request, FrmDpApplication.fieldNames[FrmDpApplication.FRM_FLD_EMPLOYEE_ID]); 
Date takenDate = FRMQueryString.requestDate(request, FrmDpApplication.fieldNames[FrmDpApplication.FRM_FLD_TAKEN_DATE]); 

int iErrCode = FRMMessage.ERR_NONE;
String errMsg = "";

ControlLine ctrLine = new ControlLine();
CtrlDpApplication ctrlDpApplication = new CtrlDpApplication(request);
iErrCode = ctrlDpApplication.action(iCommand, oidDpApplication);

errMsg = ctrlDpApplication.getMessage();
FrmDpApplication frmDpApplication = ctrlDpApplication.getForm();
DpApplication objDpApplication = ctrlDpApplication.getDpApplication();     
oidDpApplication = objDpApplication.getOID();

if(iCommand==Command.DELETE && frmDpApplication.errorSize()<1)
{
%>
	<jsp:forward page="dp_application_list.jsp">
	<jsp:param name="start" value="<%=start%>" />
	<jsp:param name="iCommand" value="<%=Command.LIST%>" />        
	</jsp:forward>
<%
}

// get employee and deparment data
String strEmpFullName = "";
String strEmpDepartment = "";
String strEmpPosition = "";
Employee objEmployee = new Employee();
try
{
	if( oidEmployee == 0 )
	{
		oidEmployee =  objDpApplication.getEmployeeId();
	}
	
	objEmployee = PstEmployee.fetchExc(oidEmployee);

	// get fullname
	strEmpFullName = objEmployee.getFullName();
	
	// get deparment
	Department objDepartment = new Department();
	objDepartment = PstDepartment.fetchExc(objEmployee.getDepartmentId());
	strEmpDepartment = objDepartment.getDepartment();
                
        //get position
        Position objPosition = new Position();
        objPosition = PstPosition.fetchExc(objEmployee.getPositionId());
        strEmpPosition = objPosition.getPosition();
}
catch(Exception e)
{
	System.out.println("Exc when fetch employee and deparment data : " + e.toString());
}

String strDpDate = "";
String strDpNotes = "";
if(iCommand != Command.ADD)
{	 
	// get day off payment data
	DpStockManagement objDpStockManagement = new DpStockManagement();
	try
	{
		objDpStockManagement = PstDpStockManagement.fetchExc(objDpApplication.getDpId());
		strDpDate = Formater.formatDate(objDpStockManagement.getDtOwningDate(),"MMM dd, yyyy");
		strDpNotes = objDpStockManagement.getStNote();
	}
	catch(Exception e)
	{
		System.out.println("Exc when fetch Dp data : " + e.toString());
	}
}
%>
<!-- End of Jsp Block -->
<html><!-- #BeginTemplate "/Templates/main.dwt" -->
<head>
<!-- #BeginEditable "doctitle" --> 
<title>HARISMA - DP Application</title>
<script language="JavaScript">
<!--
function cmdPrint()
{ 
        document.frm_dp_application.command.value="<%=String.valueOf(Command.PRINT)%>";
        document.frm_dp_application.action="<%=approot%>/servlet/com.dimata.harisma.report.leave.DpApplicationHrPdf?oidDpApplication=<%=String.valueOf(oidDpApplication)%>";
        document.frm_dp_application.submit();					
}

function cmdCancel()
{
	document.frm_dp_application.command.value="<%=String.valueOf(Command.CANCEL)%>";
	document.frm_dp_application.action="dp_application_edit.jsp";
	document.frm_dp_application.submit();
} 

function cmdEdit(oid)
{ 
	document.frm_dp_application.command.value="<%=String.valueOf(Command.EDIT)%>";
	document.frm_dp_application.action="dp_application_edit.jsp";
	document.frm_dp_application.submit(); 
} 

function cmdSave()
{
	document.frm_dp_application.command.value="<%=String.valueOf(Command.SAVE)%>"; 
	document.frm_dp_application.action="dp_application_edit.jsp";
	document.frm_dp_application.submit();
}

function cmdAsk(oid)
{
	document.frm_dp_application.command.value="<%=String.valueOf(Command.ASK)%>"; 
	document.frm_dp_application.action="dp_application_edit.jsp";
	document.frm_dp_application.submit();
} 

function cmdConfirmDelete(oid)
{
	document.frm_dp_application.command.value="<%=String.valueOf(Command.DELETE)%>";
	document.frm_dp_application.action="dp_application_edit.jsp"; 
	document.frm_dp_application.submit();
}  

function cmdBack()
{
	document.frm_dp_application.command.value="<%=String.valueOf(Command.FIRST)%>"; 
	document.frm_dp_application.action="dp_application_list.jsp";
	document.frm_dp_application.submit();
}

function searchEmployee()
{
	window.open("dp_application_empsearch.jsp?emp_fullname=" + document.frm_dp_application.EMP_NAME.value, null, "height=600,width=800,status=yes,toolbar=no,menubar=no,location=no,scrollbars=yes");
}

function searchDp()  
{
	var emp_oid = document.frm_dp_application.<%=FrmDpApplication.fieldNames[FrmDpApplication.FRM_FLD_EMPLOYEE_ID]%>.value;
	var taken_yr = document.frm_dp_application.<%=FrmDpApplication.fieldNames[FrmDpApplication.FRM_FLD_TAKEN_DATE]%>_yr.value;
	var taken_mn = document.frm_dp_application.<%=FrmDpApplication.fieldNames[FrmDpApplication.FRM_FLD_TAKEN_DATE]%>_mn.value;
	var taken_dy = document.frm_dp_application.<%=FrmDpApplication.fieldNames[FrmDpApplication.FRM_FLD_TAKEN_DATE]%>_dy.value;			
	if(emp_oid != 0)
	{	
		window.open("dp_application_dpstock.jsp?emp_oid="+emp_oid+"&taken_yr="+taken_yr+"&taken_mnr="+taken_mn+"&taken_dy="+taken_dy, null, "height=600,width=800,status=yes,toolbar=no,menubar=no,location=no,scrollbars=yes");
	}
	else
	{
		alert('Please select an employee first ...');
	}
}

function checkApproval()
{
	var empLoggedIn = "<%=String.valueOf(emplx.getOID())%>";
	var empApprovalSelected = document.frm_dp_application.<%=FrmDpApplication.fieldNames[FrmDpApplication.FRM_FLD_APPROVAL_ID]%>.value;
	if(empLoggedIn != 0)
	{
		if(empApprovalSelected != 0)
		{
			if(empLoggedIn != empApprovalSelected)
			{
				document.frm_dp_application.command.value="<%=String.valueOf(Command.LIST)%>"; 
				document.frm_dp_application.action="dp_application_app_login.jsp";
				document.frm_dp_application.submit();  		
			}
		}
		else
		{
			alert('Please choose an authorized manager to approve this DP Application ...');    					
		}
	}
	else
	{
		alert('You should login into Harisma as an authorized user ...'); 
		document.frm_dp_application.<%=FrmDpApplication.fieldNames[FrmDpApplication.FRM_FLD_APPROVAL_ID]%>.value = "0";   		
	}
}

function getThn(){
            <%=ControlDatePopup.writeDateCaller("frm_dp_application",FrmDpApplication.fieldNames[FrmDpApplication.FRM_FLD_SUBMISSION_DATE])%>
 
    }


    function hideObjectForDate(index){
    }

    function showObjectForDate(){
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
<!-- #EndEditable --> <!-- #BeginEditable "stylestab" --> 
<link rel="stylesheet" href="../../styles/tab.css" type="text/css">
<link rel="stylesheet" href="<%=approot%>/styles/calendar.css" type="text/css">
<!-- #EndEditable --> <!-- #BeginEditable "headerscript" --> 
<!-- #EndEditable --> 
</head>
<body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
<%=(ControlDatePopup.writeTable(approot))%>
<table width="100%" border="0" cellspacing="0" cellpadding="0" height="100%" bgcolor="#F9FCFF" >
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
  <tr> 
    <td width="88%" valign="top" align="left"> 
      <table width="100%" border="0" cellspacing="3" cellpadding="2">
        <tr> 
          <td width="100%"> 
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
              <tr> 
                <td height="20"> <font color="#FF6600" face="Arial"><strong> <!-- #BeginEditable "contenttitle" --> 
                  Employee &gt; Leave Management &gt; DP Application<!-- #EndEditable --> 
                  </strong></font> </td>
              </tr>
              <tr> 
                <td> 
                  <table width="100%" border="0" cellspacing="0" cellpadding="0">
                    <tr> 
                      <td class="tablecolor"> 
                        <table width="100%" border="0" cellspacing="1" cellpadding="1" class="tablecolor">
                          <tr> 
                            <td valign="top"> 
                              <table width="100%" border="0" cellspacing="1" cellpadding="1" class="tabbg">
                                <tr> 
                                  <td valign="top"> <!-- #BeginEditable "content" --> 
                                    <form name="frm_dp_application" method="post">
                                      <input type="hidden" name="command" value="<%=String.valueOf(iCommand)%>">  
                                      <input type="hidden" name="start" value="<%=String.valueOf(start)%>">
                                      <input type="hidden" name="hidden_dp_application_id" value="<%=String.valueOf(oidDpApplication)%>">
                                      <input type="hidden" name="hidden_dep_head_authorize" value="<%=String.valueOf(depHeadAuthorize)%>">									  									  
                                      <input type="hidden" name="manager_id" value="<%=String.valueOf(approvalId)%>">									  									  									  
                                      <table width="100%" border="0" cellspacing="0" cellpadding="0">
                                        <tr> 
                                          <td width="2%">&nbsp;</td>
                                          <td width="94%"> 
                                            <table width="100%" border="0" cellspacing="1" cellpadding="1" >
                                              <tr> 
                                                <td align="center"><b><font size="3">
                                                    Extra Off Day Reaquest
                                                  </font></b></td>
                                              </tr>
                                              <tr> 
                                                <td align="center"> 
                                                  <div align="center"><b><font size="3">
                                                      (Level C and Above)
                                                  </font></b></div>
                                                </td>
                                              </tr>
                                              <tr> 
                                                <td> 
                                                  <table width="100%" border="0">
                                                    <tr align="right"> 
                                                      <td colspan="6">&nbsp; </td>
                                                    </tr>
                                                    <tr> 
                                                      <td width="10%">Date Of Submission</td>
                                                      <td width="2%">:</td>
                                                      <td>
                                                      <%=ControlDatePopup.writeDate(FrmDpApplication.fieldNames[FrmDpApplication.FRM_FLD_SUBMISSION_DATE],(objDpApplication.getSubmissionDate()==null ? new Date() : objDpApplication.getSubmissionDate()))%>
                                                     <!-- <td width="26%">< %=ControlDate.drawDate(FrmDpApplication.fieldNames[FrmDpApplication.FRM_FLD_SUBMISSION_DATE], (objDpApplication.getSubmissionDate()==null ? new Date() : objDpApplication.getSubmissionDate()), "formElemen", 1, -5)%>
                                                     -->
                                                        </td>
                                                      <td colspan="2%"> 
                                                      </td>
                                                      <td width="2%"> 
                                                      </td>
                                                    </tr>
                                                    <tr> 
                                                      <td width="4%">To</td>
                                                      <td width="2%">:</td>
                                                      <td> 
                                                        <input type="text" name="TO_POSITION" size="40" value="HR MANAGER">
                                                      </td>
                                                      <td width="9%"> 
                                                        <div align="right"><%=dictionaryD.getWord(I_Dictionary.DEPARTMENT) %></div>
                                                      </td>
                                                      <td width="2%"> 
                                                        <div align="center">:</div>
                                                      </td>
                                                      <td width="26%"> 
                                                        <input type="text" name="TO_DEPARTMENT" size="40" value="HRD">
                                                      </td>
                                                    </tr>
                                                    </table>
                                                </td>
                                            </tr>
                                            
                                                    <tr><td><hr></td></tr>
                                            <tr> 
                                                <td> 
                                                  <table width="100%" border="0">
                                                    <tr> 
                                                      <td width="10%">Name</td>
                                                      <td width="2%">:</td>
                                                      <td> 
                                                        <input type="hidden" name="<%=FrmDpApplication.fieldNames[FrmDpApplication.FRM_FLD_EMPLOYEE_ID]%>" value="<%=String.valueOf(oidEmployee)%>">
                                                        <!--input type="text" name="EMP_NAME" style="background-color:#F5F5F5" readonly size="40" value="< %=strEmpFullName%>"-->
                                                        <b><%=strEmpFullName%></b>
                                                        <!--<input type="button" name="btnSchEmp" value="Search Employee" onClick="javascript:searchEmployee()">-->
                                                      </td>
                                                    </tr>
                                                    <tr> 
                                                      <td width="10%">Position</td>
                                                      <td width="2%">:</td>
                                                      <td> <b>
                                                        <%=strEmpPosition%>
                                                        <!--<input type="button" name="btnSchEmp" value="Search Employee" onClick="javascript:searchEmployee()">-->
                                                      </b></td>
                                                    </tr>
                                                    <tr> 
                                                      <td width="10%"><%=dictionaryD.getWord(I_Dictionary.DEPARTMENT) %></td>
                                                      <td width="2%">:</td>
                                                      <td><b><%=strEmpDepartment%></b>
                                                        <!--<input type="button" name="btnSchEmp" value="Search Employee" onClick="javascript:searchEmployee()">-->
                                                      </td>
                                                    </tr>
                                                  </table>
                                                </td>
                                              </tr>
                                              <tr> 
                                                <td>&nbsp;</td>
                                              </tr>
                                              <tr> 
                                                <td> 
                                                  <table width="100%" border="0">
                                                    <tr> 
                                                      <td colspan="3">
                                                          <%
                                                                Date dtTakenDate =  objDpApplication.getTakenDate()!=null ? objDpApplication.getTakenDate() : (takenDate!=null ? takenDate : new Date());
                                                            %>
                                                          <input type="hidden" name="<%=FrmDpApplication.fieldNames[FrmDpApplication.FRM_FLD_TAKEN_DATE]%>_yr" size="40" value="<%=String.valueOf(dtTakenDate.getYear()+1900)%>">
                                                          <input type="hidden" name="<%=FrmDpApplication.fieldNames[FrmDpApplication.FRM_FLD_TAKEN_DATE]%>_mn" size="40" value="<%=String.valueOf(dtTakenDate.getMonth()+1)%>">
                                                          <input type="hidden" name="<%=FrmDpApplication.fieldNames[FrmDpApplication.FRM_FLD_TAKEN_DATE]%>_dy" size="40" value="<%=String.valueOf(dtTakenDate.getDate())%>">
                                                          <table><tr><td>
                                                          I would like to take my Day Off on <u><b><%=Formater.formatDate(dtTakenDate,"MMMM dd, yyyy")%></b></u> 
                                                          in replace of 
                                                            </td><td>
                                                            <input type="hidden" name="<%=FrmDpApplication.fieldNames[FrmDpApplication.FRM_FLD_DP_ID]%>" value="<%=String.valueOf(objDpApplication.getDpId())%>">
                                                            <input type="text" name="DP_OWNING_DATE" style="background-color:#F5F5F5" readonly size="20" value="<%=strDpDate%>">
                                                            <%if( (objDpApplication.getApprovalId()==0) && (objDpApplication.getDpId()==0) ){%>
                                                                <a href="javascript:searchDp()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image10x','','<%=approot%>/images/icon/folderopen.gif',1)"><img name="Image10x" border="0" src="<%=approot%>/images/icon/folder.gif" width="24" height="24" alt="Search Trainer"></a>
                                                            <%}%>
                                                            </td></tr>
                                                            <tr><td>
                                                                as per approved extra workday request form attached.
                                                            </td></tr>
                                                            </table>
                                                         </td>
                                                    </tr>
                                                    <tr> 
                                                      <td colspan="3">
                                                        <input type="hidden" name="DP_NOTES" style="background-color:#F5F5F5" readonly size="82" value="<%=strDpNotes%>">
                                                      </td>
                                                    </tr>
                                                    <tr> 
                                                      <td colspan="3">Thank you.</td>
                                                    </tr>
                                                    <tr> 
                                                      <td colspan="3">&nbsp;</td>
                                                    </tr>
                                                    <tr> 
                                                      <td width="40%">Requested by,</td>
                                                      <td width="30%">&nbsp;</td>
                                                      <td width="40%"> 
                                                        <%if(objDpApplication.getApprovalId()!=0){%>
                                                        Approved By, 
                                                        <%}else{%>
                                                        &nbsp; 
                                                        <%}%>
                                                      </td>
                                                    </tr>
                                                    <tr> 
                                                      <td width="40%"><b> 
                                                        <!--input type="text" name="EMP_SIGN_NAME" style="background-color:#F5F5F5" readonly size="40" value="< %=strEmpFullName%>"-->
                                                        <u><%=strEmpFullName%></u>
                                                        </b></td>
                                                      <td width="30%">&nbsp;</td>
                                                      <td width="40%"> 
                                                      <%												  
                                                              if(oidDpApplication!=0)
                                                              {
                                                                      Vector divHeadKey = new Vector(1,1);
                                                                      Vector divHeadValue = new Vector(1,1);

                                                                      divHeadKey.add("Select Department Head");
                                                                      divHeadValue.add("0");

                                                                      String selectedApproval = ""+objDpApplication.getApprovalId();
                                                                      if(depHeadAuthorize)
                                                                      {
                                                                              selectedApproval = ""+approvalId;
                                                                      }

                                                                      Vector vectPositionLvl1 = new Vector(1,1);
                                                                      vectPositionLvl1.add(""+PstPosition.LEVEL_SECRETARY);
                                                                      vectPositionLvl1.add(""+PstPosition.LEVEL_SUPERVISOR);
                                                                      vectPositionLvl1.add(""+PstPosition.LEVEL_MANAGER);   
                                                                      vectPositionLvl1.add(""+PstPosition.LEVEL_GENERAL_MANAGER);														           

                                                                      Vector listDivHead = SessEmployee.listEmployeeByPositionLevel(objEmployee, vectPositionLvl1);
                                                                      for(int i=0; i<listDivHead.size(); i++)
                                                                      {
                                                                              Employee objEmp = (Employee)listDivHead.get(i);

                                                                              if(objEmployee.getOID() != objEmp.getOID())
                                                                              {
                                                                                      divHeadKey.add(objEmp.getFullName());
                                                                                      divHeadValue.add(""+objEmp.getOID());
                                                                              }
                                                                      }
                                                                      String strAttribute = "class=\"formElemen\" onChange=\"javascript:checkApproval()\"";
                                                                      out.println(ControlCombo.draw(FrmDpApplication.fieldNames[FrmDpApplication.FRM_FLD_APPROVAL_ID], null, selectedApproval, divHeadValue, divHeadKey, strAttribute));														  
                                                              }
                                                              else
                                                              {
                                                                    out.println("&nbsp;");
                                                              }													  		  
                                                      %>
                                                      </td>
                                                    </tr>
                                                  </table>
                                                </td>
                                              </tr>
                                              <tr> 
                                                <td>&nbsp; </td>
                                              </tr>
                                              <tr> 
                                                <td valign="top"> 
                                                  <table width="100%" border="0" class="tablecolor" cellpadding="1" cellspacing="1">
                                                    <tr> 
                                                      <td valign="top"> 
                                                        <table width="100%" border="0" bgcolor="#F9FCFF">
                                                          <tr><td></td></tr>
                                                          <tr> 
                                                          <%
                                                         // int balance = SessDayOfPayment.getDpBalance(objEmployee.getOID());
                                                          %>
                                                            <td width="5"></td>
                                                            <input type="hidden" name="<%=FrmDpApplication.fieldNames[FrmDpApplication.FRM_FLD_BALANCE]%>" value="<%=String.valueOf(objDpApplication.getBalance())%>">
                                                            <input type="hidden" name="<%=FrmDpApplication.fieldNames[FrmDpApplication.FRM_FLD_NEW_BALANCE]%>" value="<%=String.valueOf(objDpApplication.getNewBalance())%>">
                                                            <td><b>Previous Balance : <u><%=String.valueOf(objDpApplication.getBalance())%></u></b> </td>
                                                            <td><b>New Balance : <u> <%=String.valueOf(objDpApplication.getNewBalance())%> </u></b> </td>
                                                            <td width="5"></td>
                                                          </tr>
                                                          <tr><td></td></tr>
                                                        </table>
                                                      </td>
                                                    </tr>
                                                  </table>
                                                </td>
                                              </tr>
                                              <tr><td>
                                                  <%
                                                  Vector vListNotApproval = SessDayOfPayment.listAplicationNotApproved(objEmployee.getOID());
                                                  out.println("<b>Dp Application not yet approve :</b>");
                                                  for(int i=0;i<vListNotApproval.size();i++){
                                                    DpApplication dpApp = new DpApplication();
                                                    dpApp = (DpApplication)vListNotApproval.get(i);
                                                    out.println("<UL>");
                                                    out.println("<LI><font color='RED'>"+Formater.formatDate(dpApp.getTakenDate(), "dd-MMMM-yyyy")+"</font></LI>");
                                                    out.println("</UL>");
                                                  }
                                                  %>
                                                  
                                              </td></tr>
                                              <tr> 
                                                <td>
                                                    <b>Note :</b><br>
                                                    Level C  B approval will be given by Dept. Head or Division Head condern<br>
                                                    Level A & Executive Committee Members will be approved by General Manager
                                                </td>
                                              </tr>
                                              <tr> 
                                                <td>&nbsp;</td>
                                              </tr>
                                              <tr> 
                                                <td> 
                                                  <%
                                                    ctrLine.setLocationImg(approot+"/images");
                                                    ctrLine.initDefault();
                                                    ctrLine.setTableWidth("80");
                                                    ctrLine.setCommandStyle("buttonlink");												

                                                    String scomDel = "javascript:cmdAsk('"+oidDpApplication+"')";
                                                    String sconDelCom = "javascript:cmdConfirmDelete('"+oidDpApplication+"')";
                                                    String scancel = "javascript:cmdEdit('"+oidDpApplication+"')";

                                                    ctrLine.setAddCaption("");												
                                                    ctrLine.setBackCaption("Back to List Dp Application");
                                                    ctrLine.setConfirmDelCaption("Yes Delete Dp Application");
                                                    ctrLine.setDeleteCaption("Delete Dp Application");
                                                    ctrLine.setSaveCaption("Save Dp Application");

                                                    if ( (privDelete) && (objDpApplication.getApprovalId()==0) )
                                                    {
                                                            ctrLine.setConfirmDelCommand(sconDelCom);
                                                            ctrLine.setDeleteCommand(scomDel);
                                                            ctrLine.setEditCommand(scancel);
                                                    }
                                                    else
                                                    {												 
                                                            ctrLine.setConfirmDelCaption("");
                                                            ctrLine.setDeleteCaption("");
                                                            ctrLine.setEditCaption("");
                                                    }

                                                    if((!privAdd) && (!privUpdate))
                                                    {
                                                            ctrLine.setSaveCaption("");
                                                    }

                                                    if (!privAdd)
                                                    {
                                                            ctrLine.setAddCaption("");
                                                    }

                                                    out.println(ctrLine.drawImage(iCommand, iErrCode, errMsg));
                                                    %>	
                                                </td>
                                              </tr>
                                              </tr>
                                              <%
                                              if( (privPrint) && (oidDpApplication > 0) && (objDpApplication.getApprovalId()!=0) )
                                              {  
                                              %>
                                              <tr> 
                                                <td> 
                                                  <table width="22%" border="0" cellpadding="0" cellspacing="0">
                                                    <tr> 
                                                      <td width="12%" valign="top"><a href="javascript:cmdPrint()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image321','','<%=approot%>/images/BtnNewOn.jpg',1)"><img name="Image321" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24"></a></td>
                                                      <td width="88%" valign="top" nowrap class="button">&nbsp; 
                                                        <a href="javascript:cmdPrint()" class="buttonlink">Print Dp Application</a></td>
                                                    </tr>
                                                  </table> 																								
                                                </td>
                                              </tr>											  
                                              <%
                                              }
                                              %>
                                            </table>
                                          </td>
                                          <td width="2%">&nbsp;</td>
                                        </tr>
                                      </table>
                                    </form>
									<%
									if( (objDpApplication.getApprovalId()==0) && (objDpApplication.getDpId()==0) )
									{
									%>									
                                    <script language="javascript">
										document.frm_dp_application.btnSrcDp.focus();
									</script>	
									<%
									}
									%>
                                    <!-- #EndEditable --> </td>
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
<!-- #BeginEditable "script" --> <!-- #EndEditable --> 
<!-- #EndTemplate --></html>
