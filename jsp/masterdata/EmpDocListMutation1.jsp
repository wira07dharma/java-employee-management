<%-- 
    Document   : EmpDocListMutation
    Created on : 03-Dec-2015, 23:44:29
    Author     : GUSWIK/PRISKA
--%>


<%@page import="com.dimata.harisma.form.masterdata.FrmEmpDocListMutation"%>
<%@page import="com.dimata.harisma.form.masterdata.CtrlEmpDocListMutation"%>
<%@page language = "java" %>
<!-- package java -->
<%@ page import = "java.util.*" %>
<!-- package wihita -->
<%@ page import = "com.dimata.util.*" %>
<!-- package qdep -->
<%@ page import = "com.dimata.gui.jsp.*" %>
<%@ page import = "com.dimata.qdep.form.*" %>
<%@ include file = "../main/javainit.jsp" %>
<% int  appObjCode =  AppObjInfo.composeObjCode(AppObjInfo.G1_EMPLOYEE, AppObjInfo.G2_DATABANK, AppObjInfo.OBJ_DATABANK); %>
<%//@include file = "../main/checkuser.jsp" %>
<%
/* Check privilege except VIEW, view is already checked on checkuser.jsp as basic access*/
boolean privStart=true;//userSession.checkPrivilege(AppObjInfo.composeCode(appObjCode, AppObjInfo.COMMAND_START));
%>


<%
int iCommand = FRMQueryString.requestCommand(request);

long empDocId = FRMQueryString.requestLong(request, "empDocId");
long employeeId = FRMQueryString.requestLong(request, "employeeId");

long companyId = FRMQueryString.requestLong(request, "companyId");
long divisionId = FRMQueryString.requestLong(request, "divisionId");
long departmentId = FRMQueryString.requestLong(request, "departmentId");


    long oidEmpDocField = FRMQueryString.requestLong(request,"oidEmpDocField");
    String objectName = FRMQueryString.requestString(request,"ObjectName");
    String ObjectType = FRMQueryString.requestString(request,"ObjectType");
    String ObjectClass = FRMQueryString.requestString(request,"ObjectClass");
    String ObjectStatusfield = FRMQueryString.requestString(request,"ObjectStatusfield");


int start = FRMQueryString.requestInt(request, "start");
int prevCommand = FRMQueryString.requestInt(request, "prev_command");
long oidEmpDocListMutation = FRMQueryString.requestLong(request, "hidden_empDocListMutation_id");

/*variable declaration*/
int recordToGet = 10;
String msgString = "";
int iErrCode = FRMMessage.NONE;
String whereClause = "";
String orderClause = "";

CtrlEmpDocListMutation ctrlEmpDocListMutation = new CtrlEmpDocListMutation(request);
ControlLine ctrLine = new ControlLine();
Vector listEmpDocListMutation = new Vector(1,1);

/*switch statement */
iErrCode = ctrlEmpDocListMutation.action(iCommand , oidEmpDocListMutation);
/* end switch*/
FrmEmpDocListMutation frmEmpDocListMutation = ctrlEmpDocListMutation.getForm();

/*count list All Position*/
int vectSize = PstEmpDocListMutation.getCount(whereClause);

EmpDocListMutation empDocListMutation = ctrlEmpDocListMutation.getEmpDocListMutation();
msgString =  ctrlEmpDocListMutation.getMessage();
 
/*switch list Division*/
if((iCommand == Command.SAVE) && (iErrCode == FRMMessage.NONE)){
	//start = PstDivision.findLimitStart(division.getOID(),recordToGet, whereClause);
	oidEmpDocListMutation = empDocListMutation.getOID();
}


/* get record to display */
listEmpDocListMutation = PstEmpDocListMutation.list(start,recordToGet, whereClause , orderClause);

%>

<script language=JavaScript src="<%=approot%>/main/calendar.js"></script>
<html><!-- #BeginTemplate "/Templates/main.dwt" -->
<head>
<!-- #BeginEditable "doctitle" --> 
<title>HARISMA - Search Employee</title>
<script language="JavaScript">
    
function cmdUpdateDep(){
	document.empDocListMutation.command.value="<%=String.valueOf(Command.ADD)%>";
	document.empDocListMutation.action="EmpDocListMutation.jsp"; 
	document.empDocListMutation.submit();
}

function deptChange() {
    document.empDocListMutation.command.value = "<%=String.valueOf(Command.GOTO)%>";
    document.empDocListMutation.hidden_goto_dept.value = document.empDocListMutation.<%=FrmEmpDocListMutation.fieldNames[FrmEmpDocListMutation.FRM_FIELD_DEPARTMENT_ID]%>.value;
    document.empDocListMutation.action = "EmpDocListMutation.jsp";
    document.empDocListMutation.submit();
}

function cmdSearch() {
    document.empDocListMutation.command.value = "<%=String.valueOf(Command.LIST)%>";									
    document.empDocListMutation.action = "EmpDocListMutation.jsp";
    document.empDocListMutation.submit();
}


        function MM_swapImgRestore() 
        { //v3.0
                var i,x,a=document.MM_sr; for(i=0;a&&i<a.length&&(x=a[i])&&x.oSrc;i++) x.src=x.oSrc;
        }

        function MM_preloadImages() 
        { //v3.0
                var d=document; if(d.images){ if(!d.MM_p) d.MM_p=new Array();
                var i,j=d.MM_p.length,a=MM_preloadImages.arguments; for(i=0; i<a.length; i++)
                if (a[i].indexOf("#")!=0){ d.MM_p[j]=new Image; d.MM_p[j++].src=a[i];}}
        }

        function MM_findObj(n, d) 
        { //v4.0
                var p,i,x;  if(!d) d=document; if((p=n.indexOf("?"))>0&&parent.frames.length) {
                d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}
                if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];
                for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=MM_findObj(n,d.layers[i].document);
                if(!x && document.getElementById) x=document.getElementById(n); return x;
        }

        function MM_swapImage() 
        { //v3.0
                var i,j=0,x,a=MM_swapImage.arguments; document.MM_sr=new Array; for(i=0;i<(a.length-2);i+=3)
                if ((x=MM_findObj(a[i]))!=null){document.MM_sr[j++]=x; if(!x.oSrc) x.oSrc=x.src; x.src=a[i+2];}
        }
</script>


<!-- #EndEditable --> 
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"> 
<!-- #BeginEditable "styles" --> 
<link rel="stylesheet" href="<%=approot%>/styles/main.css" type="text/css">
<!-- #EndEditable -->
<!-- #BeginEditable "stylestab" --> 
<link rel="stylesheet" href="<%=approot%>/styles/tab.css" type="text/css">
<!-- #EndEditable -->
<link rel="stylesheet" href="<%=approot%>/styles/calendar.css" type="text/css">
    
</head> 

<body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">

<!-- Untuk Calender-->
<table class="ds_box" cellpadding="0" cellspacing="0" id="ds_conclass" style="display: none;">
    <tr><td id="ds_calclass">
    </td></tr>
</table> 
<script language=JavaScript src="<%=approot%>/main/calendar.js"></script>
<!-- End Calender-->

<table width="100%" border="0" cellspacing="0" cellpadding="0" height="100%" bgcolor="#F9FCFF" >

  
  <tr> 
    <td width="88%" valign="top" align="left"> 
      <table width="100%" border="0" cellspacing="3" cellpadding="2"> 
        <tr> 
          <td width="100%">
      <table width="100%" border="0" cellspacing="0" cellpadding="0"> 
        <tr> 
                <td height="20"> <font color="#FF6600" face="Arial"><strong> <!-- #BeginEditable "contenttitle" -->
                Search Employee
                <!-- #EndEditable --> 
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
                            <td valign="top">
		    				  <!-- #BeginEditable "content" -->
                                <% if (privStart) { %>
                                    <form name="empDocListMutation" method="post" action="">
                                    <input type="hidden" name="command" value="<%=String.valueOf(iCommand)%>">
                                    <input type="hidden" name="empDocId" value="<%=String.valueOf(empDocId)%>">
                                    <input type="hidden" name="employeeId" value="<%=String.valueOf(employeeId)%>">
                                    <input type="hidden" name="objectName" value="<%=String.valueOf(objectName)%>">
                                    <table width="100%" border="0" cellspacing="0" cellpadding="0">
                                      <tr>
                                            <td>
                                            <table width="100%" border="0" cellspacing="2" cellpadding="2">
                                              <tr> 
                                                <td width="19%">Name</td>
                                                <td width="1%">:</td>
                                                <td width="80%"> 
                                                  <%=String.valueOf(empDocListMutation.getEmployeeId())%>
                                                </td>
                                              </tr>
                                           
                                             
                                              
                                              <tr> 
                                                <td width="19%"><%=dictionaryD.getWord(I_Dictionary.COMPANY)%></td>
                                                <td width="1%">:</td>
                                                <td width="80%"><% 
							Vector com_value = new Vector(1,1);
							Vector com_key = new Vector(1,1); 
							com_value.add("0");
							com_key.add("-");
                                                        String strWhere = "";
							Vector listCom = PstCompany.list(0, 0, strWhere, "COMPANY");
							for (int i = 0; i < listCom.size(); i++) {
								Company com = (Company) listCom.get(i);
								com_key.add(com.getCompany());
								com_value.add(String.valueOf(com.getOID()));
							}
						%>
                                                <%= ControlCombo.draw(FrmEmpDocListMutation.fieldNames[FrmEmpDocListMutation.FRM_FIELD_COMPANY_ID],"formElemen",null,String.valueOf(empDocListMutation.getCompanyId()), com_value, com_key, "") %> 
                                                </td>
                                              </tr>
                                              
                                              <tr> 
                                                <td width="19%"><%=dictionaryD.getWord(I_Dictionary.DIVISION) %></td>
                                                <td width="1%">:</td>
                                                <td width="80%"><% 
							Vector div_value = new Vector(1,1);
							Vector div_key = new Vector(1,1); 
							div_value.add("0");
							div_key.add("-");
                                                        String strWhereDiv = PstDivision.fieldNames[PstDivision.FLD_COMPANY_ID]+"="+companyId;
							Vector listDiv = PstDivision.list(0, 0, strWhereDiv, "DIVISION");
							for (int i = 0; i < listDiv.size(); i++) {
								Division div = (Division) listDiv.get(i);
								div_key.add(div.getDivision());
								div_value.add(String.valueOf(div.getOID()));
							}
						%>
                                                <%= ControlCombo.draw(FrmEmpDocListMutation.fieldNames[FrmEmpDocListMutation.FRM_FIELD_DIVISION_ID],"formElemen",null,String.valueOf(empDocListMutation.getDivisionId()), div_value, div_key, "") %> 
                                                </td>
                                              </tr>
                                              
                                              <tr> 
                                                <td width="19%"><%=dictionaryD.getWord(I_Dictionary.DEPARTMENT) %></td>
                                                <td width="1%">:</td>
                                                <td width="80%"><% 
							Vector dep_value = new Vector(1,1);
							Vector dep_key = new Vector(1,1); 
							dep_value.add("0");
							dep_key.add("-");
                                                        String strWhereDep = PstDepartment.fieldNames[PstDepartment.FLD_DIVISION_ID]+"="+divisionId;
							Vector listDep = PstDepartment.list(0, 0, strWhereDep, "DEPARTMENT");
							for (int i = 0; i < listDep.size(); i++) {
								Department dep = (Department) listDep.get(i);
								dep_key.add(dep.getDepartment());
								dep_value.add(String.valueOf(dep.getOID()));
							}
						%>
                                                <%= ControlCombo.draw(FrmEmpDocListMutation.fieldNames[FrmEmpDocListMutation.FRM_FIELD_DEPARTMENT_ID],"formElemen",null,String.valueOf(empDocListMutation.getDepartmentId()), dep_value, dep_key, "") %> 
                                                </td>
                                              </tr>
                                              <tr> 
                                                <td width="19%"><%=dictionaryD.getWord(I_Dictionary.SECTION) %></td>
                                                <td width="1%">:</td>
                                                <td width="80%"> 
                                                <% 
							Vector sec_value = new Vector(1,1);
							Vector sec_key = new Vector(1,1); 
							sec_value.add("0");
							sec_key.add("all section ...");
                                                        String strWhereSec = PstSection.fieldNames[PstSection.FLD_DEPARTMENT_ID]+"="+departmentId;
							Vector listSec = PstSection.list(0, 0, strWhereSec, "SECTION");
							for (int i = 0; i < listSec.size(); i++) {
								Section sec = (Section) listSec.get(i);
								sec_key.add(sec.getSection());
								sec_value.add(String.valueOf(sec.getOID()));
							}
						%>
                                                <%= ControlCombo.draw(FrmEmpDocListMutation.fieldNames[FrmEmpDocListMutation.FRM_FIELD_SECTION_ID],"formElemen",null,String.valueOf(empDocListMutation.getSectionId()), sec_value, sec_key, "") %> 
                                                </td>
                                              </tr>
                                              <tr> 
                                                <td width="19%">Position</td>
                                                <td width="1%">:</td>
                                                <td width="80%"> 
                                              <% 
							Vector pos_value = new Vector(1,1);
							Vector pos_key = new Vector(1,1); 
							pos_value.add("0");
							pos_key.add("all position ...");                                                       
							Vector listPos = PstPosition.list(0, 0, "", " POSITION ");
                                                        for (int i = 0; i < listPos.size(); i++) {
								Position pos = (Position) listPos.get(i);
								pos_key.add(pos.getPosition());
								pos_value.add(String.valueOf(pos.getOID()));
							}
						%>
                                                <%= ControlCombo.draw(FrmEmpDocListMutation.fieldNames[FrmEmpDocListMutation.FRM_FIELD_POSITION_ID],"formElemen",null,String.valueOf(empDocListMutation.getPositionId()), pos_value, pos_key, "") %> </td>
                                              </tr>
					      
                                              
                                              <tr> 
                                                <td width="19%">&nbsp;</td>
                                                <td width="1%">&nbsp;</td>
                                                <td width="80%"> 
                                                  <input type="submit" name="Submit" value="Search Employee" onClick="javascript:cmdSearch()">
                                                <!--  <input type="submit" name="Submit" value="Add Employee" onClick="javascript:cmdAdd()">
                                                -->
                                                </td>
                                              </tr>
                                              
                                            </table>
                                        </td>
                                      </tr>
                                      
                                        </table>
                                    </form>
                                <% } 
                                   else
                                   {
                                %>
                                <div align="center">You do not have sufficient privilege to access this page.</div>
                                <% } %>
                                    <!-- #EndEditable -->
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
      </table>
    </td> 
  </tr>
</table>
</body>
<!-- #BeginEditable "script" -->
<!-- #EndEditable -->
<!-- #EndTemplate --></html>


