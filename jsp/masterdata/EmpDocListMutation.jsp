<%-- 
    Document   : company
    Created on : Sep 30, 2011, 3:56:51 PM
    Author     : Wiweka
--%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.dimata.harisma.entity.attendance.I_Atendance"%>
<%
            /*
             * Page Name  		:  company.jsp
             * Created on 		:  [date] [time] AM/PM
             *
             * @author  		: Ari_20110930
             * @version  		: -
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
<%@ page import = "com.dimata.harisma.form.masterdata.*" %>
<%@ page import = "com.dimata.harisma.entity.admin.*" %>

<%@ include file = "../main/javainit.jsp" %>
<% int appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_MASTERDATA, AppObjInfo.G2_MD_COMPANY, AppObjInfo.OBJ_DIVISION);%>
<%@ include file = "../main/checkuser.jsp" %>

<!-- Jsp Block -->

<%
int iCommand = FRMQueryString.requestCommand(request);

long empDocListMutationId = FRMQueryString.requestLong(request, "empDocListMutationId");
long empDocId = FRMQueryString.requestLong(request, "empDocId");
long employeeId = FRMQueryString.requestLong(request, "employeeId");

long companyId = FRMQueryString.requestLong(request, FrmEmpDocListMutation.fieldNames[FrmEmpDocListMutation.FRM_FIELD_COMPANY_ID]);
long divisionId = FRMQueryString.requestLong(request, FrmEmpDocListMutation.fieldNames[FrmEmpDocListMutation.FRM_FIELD_DIVISION_ID]);
long departmentId = FRMQueryString.requestLong(request, FrmEmpDocListMutation.fieldNames[FrmEmpDocListMutation.FRM_FIELD_DEPARTMENT_ID]);
long sectionId = FRMQueryString.requestLong(request, FrmEmpDocListMutation.fieldNames[FrmEmpDocListMutation.FRM_FIELD_SECTION_ID]);

long empCatId = FRMQueryString.requestLong(request, FrmEmpDocListMutation.fieldNames[FrmEmpDocListMutation.FRM_FIELD_EMP_CAT_ID]);
long positionId = FRMQueryString.requestLong(request, FrmEmpDocListMutation.fieldNames[FrmEmpDocListMutation.FRM_FIELD_POSITION_ID]);
long levelId = FRMQueryString.requestLong(request, FrmEmpDocListMutation.fieldNames[FrmEmpDocListMutation.FRM_FIELD_LEVEL_ID]);

Date workTo = FRMQueryString.requestDate(request, FrmEmpDocListMutation.fieldNames[FrmEmpDocListMutation.FRM_FIELD_WORK_FROM]);


long oidEmpDocField = FRMQueryString.requestLong(request,"oidEmpDocField");
String ObjectName = FRMQueryString.requestString(request,"ObjectName");
String ObjectType = FRMQueryString.requestString(request,"ObjectType");
String ObjectClass = FRMQueryString.requestString(request,"ObjectClass");
String ObjectStatusfield = FRMQueryString.requestString(request,"ObjectStatusfield");


int start = FRMQueryString.requestInt(request, "start");
int prevCommand = FRMQueryString.requestInt(request, "prev_command");


/*variable declaration*/
int recordToGet = 10;
String msgString = "";
int iErrCode = FRMMessage.NONE;
String whereClause = ""+PstEmpDocListMutation.fieldNames[PstEmpDocListMutation.FLD_EMP_DOC_ID]+"="+empDocId+" AND "+PstEmpDocListMutation.fieldNames[PstEmpDocListMutation.FLD_EMPLOYEE_ID]+"="+employeeId+" AND "+PstEmpDocListMutation.fieldNames[PstEmpDocListMutation.FLD_OBJECT_NAME]+"=\""+ObjectName+"\"";
String orderClause = "";

Employee employee = new Employee();
try{employee = PstEmployee.fetchExc(employeeId);}catch (Exception e){}
        
CtrlEmpDocListMutation ctrlEmpDocListMutation = new CtrlEmpDocListMutation(request);
ControlLine ctrLine = new ControlLine();
Vector listEmpDocListMutation = new Vector(1,1);

/*switch statement */
iErrCode = ctrlEmpDocListMutation.action(iCommand , empDocListMutationId);
/* end switch*/
FrmEmpDocListMutation frmEmpDocListMutation = ctrlEmpDocListMutation.getForm();

/*count list All Position*/
int vectSize = PstEmpDocListMutation.getCount(whereClause);

EmpDocListMutation empDocListMutation = ctrlEmpDocListMutation.getEmpDocListMutation();
msgString =  ctrlEmpDocListMutation.getMessage();
 


/*switch list Division*/
if((iCommand == Command.SAVE) && (iErrCode == FRMMessage.NONE)){
	//start = PstDivision.findLimitStart(division.getOID(),recordToGet, whereClause);
	empDocListMutationId = empDocListMutation.getOID();
}


/* get record to display */
listEmpDocListMutation = PstEmpDocListMutation.list(start,recordToGet, whereClause , orderClause);
if (listEmpDocListMutation.size() > 0){
empDocListMutation = (EmpDocListMutation)listEmpDocListMutation.get(0);
empDocListMutationId=empDocListMutation.getOID();
                                               
                                             
}
Date dateF = new Date(); 
SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    try{
        if (empDocListMutation.getWorkFrom() !=null && empDocListMutation.getOID() != 0){
         dateF = empDocListMutation.getWorkFrom();
        }
    } catch (Exception e){

    } 
%>
<html><!-- #BeginTemplate "/Templates/main.dwt" -->
    <head>
        <!-- #BeginEditable "doctitle" -->
        <title>HARISMA - Master Data </title>
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

function cmdSave(){
                document.empDocListMutation.command.value="<%=Command.SAVE%>";
                document.empDocListMutation.prev_command.value="<%=prevCommand%>";
                document.empDocListMutation.action="EmpDocListMutation.jsp";
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
        <link rel="stylesheet" href="../styles/main.css" type="text/css">
        <!-- #EndEditable -->
        <!-- #BeginEditable "stylestab" -->
        <link rel="stylesheet" href="../styles/tab.css" type="text/css">
        <!-- #EndEditable -->
        <!-- #BeginEditable "headerscript" -->
        <SCRIPT language=JavaScript>
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

        </SCRIPT>
        <!-- #EndEditable -->
    </head>

    <body <%=noBack%> bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" onLoad="MM_preloadImages('<%=approot%>/images/BtnNewOn.jpg')">
        <table width="100%" border="0" cellspacing="0" cellpadding="0" height="100%" bgcolor="#F9FCFF" >
             <%if(headerStyle && !verTemplate.equalsIgnoreCase("0")){%> 
           <%//@include file="../styletemplate/template_header.jsp" %>
            <%}else{%>
            <tr>
                <td ID="TOPTITLE" background="<%=approot%>/images/HRIS_HeaderBg3.jpg" width="100%" height="54">
                    <!-- #BeginEditable "header" -->
                    <%@ include file = "../main/header.jsp" %>
                    <!-- #EndEditable -->
                </td>
            </tr>
            <tr>
                <td  bgcolor="#9BC1FF" height="15" ID="MAINMENU" valign="middle"> <!-- #BeginEditable "menumain" -->
                    <%@ include file = "../main/mnmain.jsp" %>
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
                                                    Master Data &gt; <%=dictionaryD.getWord(I_Dictionary.COMPANY)%><!-- #EndEditable -->
                                                </strong></font>
                                        </td>
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
                                                                                <form name="empDocListMutation" method ="post" action="">
                                                                                        <input type="hidden" name="command" value="<%=String.valueOf(iCommand)%>">
                                                                                        
                                                                                        <input type="hidden" name="empDocListMutationId" value="<%=String.valueOf(empDocListMutationId)%>">
                                                                                        <input type="hidden" name="<%=FrmEmpDocListMutation.fieldNames[FrmEmpDocListMutation.FRM_FIELD_EMP_DOC_LIST_MUTATION_ID]%>" value="<%=String.valueOf(empDocListMutationId)%>">
                                                                                        
                                                                                        <input type="hidden" name="empDocId" value="<%=String.valueOf(empDocId)%>">
                                                                                        <input type="hidden" name="employeeId" value="<%=String.valueOf(employeeId)%>">
                                                                                        <input type="hidden" name="ObjectName" value="<%=String.valueOf(ObjectName)%>">
                                                                                        
                                                                                        <input type="hidden" name="<%=FrmEmpDocListMutation.fieldNames[FrmEmpDocListMutation.FRM_FIELD_EMP_DOC_ID]%>" value="<%=String.valueOf(empDocId)%>">
                                                                                        <input type="hidden" name="<%=FrmEmpDocListMutation.fieldNames[FrmEmpDocListMutation.FRM_FIELD_EMPLOYEE_ID]%>" value="<%=String.valueOf(employeeId)%>">
                                                                                        <input type="hidden" name="<%=FrmEmpDocListMutation.fieldNames[FrmEmpDocListMutation.FRM_FIELD_OBJECT_NAME]%>" value="<%=String.valueOf(ObjectName)%>">
                                                                                        
                                                                                      <table width="100%" border="0" cellspacing="0" cellpadding="0">
                                                                                          <tr>
                                                                                                <td>
                                                                                                <table width="100%" border="0" cellspacing="2" cellpadding="2">
                                                                                                  <tr> 
                                                                                                    <td width="19%">Name</td>
                                                                                                    <td width="1%">:</td>
                                                                                                    <td width="80%"> 
                                                                                                      <%=String.valueOf(employee.getFullName())%>
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
                                                                                                    <%= ControlCombo.draw(FrmEmpDocListMutation.fieldNames[FrmEmpDocListMutation.FRM_FIELD_COMPANY_ID],"formElemen",null,String.valueOf(companyId==0?empDocListMutation.getCompanyId():companyId), com_value, com_key, "onChange=\"javascript:cmdUpdateDep()\"") %> 
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
                                                                                                            String strWhereDiv = PstDivision.fieldNames[PstDivision.FLD_COMPANY_ID]+"="+(companyId==0?empDocListMutation.getCompanyId():companyId);
                                                                                                            Vector listDiv = PstDivision.list(0, 0, strWhereDiv, "DIVISION");
                                                                                                            for (int i = 0; i < listDiv.size(); i++) {
                                                                                                                    Division div = (Division) listDiv.get(i);
                                                                                                                    div_key.add(div.getDivision());
                                                                                                                    div_value.add(String.valueOf(div.getOID()));
                                                                                                            }
                                                                                                    %>
                                                                                                    <%= ControlCombo.draw(FrmEmpDocListMutation.fieldNames[FrmEmpDocListMutation.FRM_FIELD_DIVISION_ID],"formElemen",null,String.valueOf(divisionId==0?empDocListMutation.getDivisionId():divisionId), div_value, div_key, "onChange=\"javascript:cmdUpdateDep()\"") %> 
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
                                                                                                            String strWhereDep = " hr_department."+PstDepartment.fieldNames[PstDepartment.FLD_DIVISION_ID]+"="+(divisionId==0?empDocListMutation.getDivisionId():divisionId);
                                                                                                            Vector listDep = PstDepartment.list(0, 0, strWhereDep, "DEPARTMENT");
                                                                                                            for (int i = 0; i < listDep.size(); i++) {
                                                                                                                    Department dep = (Department) listDep.get(i);
                                                                                                                    dep_key.add(dep.getDepartment());
                                                                                                                    dep_value.add(String.valueOf(dep.getOID()));
                                                                                                            }
                                                                                                    %>
                                                                                                    <%= ControlCombo.draw(FrmEmpDocListMutation.fieldNames[FrmEmpDocListMutation.FRM_FIELD_DEPARTMENT_ID],"formElemen",null,String.valueOf(departmentId==0?empDocListMutation.getDepartmentId():departmentId), dep_value, dep_key, "onChange=\"javascript:cmdUpdateDep()\"") %> 
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
                                                                                                            String strWhereSec = PstSection.fieldNames[PstSection.FLD_DEPARTMENT_ID]+"="+(departmentId==0?empDocListMutation.getDepartmentId():departmentId);
                                                                                                            Vector listSec = PstSection.list(0, 0, strWhereSec, "SECTION");
                                                                                                            for (int i = 0; i < listSec.size(); i++) {
                                                                                                                    Section sec = (Section) listSec.get(i);
                                                                                                                    sec_key.add(sec.getSection());
                                                                                                                    sec_value.add(String.valueOf(sec.getOID()));
                                                                                                            }
                                                                                                    %>
                                                                                                    <%= ControlCombo.draw(FrmEmpDocListMutation.fieldNames[FrmEmpDocListMutation.FRM_FIELD_SECTION_ID],"formElemen",null,String.valueOf(sectionId==0?empDocListMutation.getSectionId():sectionId), sec_value, sec_key, "") %> 
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
                                                                                                    <%= ControlCombo.draw(FrmEmpDocListMutation.fieldNames[FrmEmpDocListMutation.FRM_FIELD_POSITION_ID],"formElemen",null,String.valueOf(positionId==0?empDocListMutation.getPositionId():positionId), pos_value, pos_key, "") %> </td>
                                                                                                  </tr>
                                                                                                  <tr> 
                                                                                                    <td width="19%">Emp Cat</td>
                                                                                                    <td width="1%">:</td>
                                                                                                    <td width="80%"> 
                                                                                                  <% 
                                                                                                            Vector cat_value = new Vector(1,1);
                                                                                                            Vector cat_key = new Vector(1,1); 
                                                                                                            cat_value.add("0");
                                                                                                            cat_key.add("all emp cat ...");                                                       
                                                                                                            Vector listCat = PstEmpCategory.list(0, 0, "", "");
                                                                                                            for (int i = 0; i < listCat.size(); i++) {
                                                                                                                    EmpCategory empCategory = (EmpCategory) listCat.get(i);
                                                                                                                    cat_key.add(empCategory.getEmpCategory());
                                                                                                                    cat_value.add(String.valueOf(empCategory.getOID()));
                                                                                                            }
                                                                                                    %>
                                                                                                    <%= ControlCombo.draw(FrmEmpDocListMutation.fieldNames[FrmEmpDocListMutation.FRM_FIELD_EMP_CAT_ID],"formElemen",null,String.valueOf(empCatId==0?empDocListMutation.getEmpCatId():empCatId), cat_value, cat_key, "") %> </td>
                                                                                                  </tr>
                                                                                                  
                                                                                                  <tr> 
                                                                                                    <td width="19%">Level</td>
                                                                                                    <td width="1%">:</td>
                                                                                                    <td width="80%"> 
                                                                                                  <% 
                                                                                                            Vector Level_value = new Vector(1,1);
                                                                                                            Vector Level_key = new Vector(1,1); 
                                                                                                            Level_value.add("0");
                                                                                                            Level_key.add("all  ...");                                                       
                                                                                                            Vector listLevel = PstLevel.list(0, 0, "", "");
                                                                                                            for (int i = 0; i < listLevel.size(); i++) {
                                                                                                                    Level Level = (Level) listLevel.get(i);
                                                                                                                    Level_key.add(Level.getLevel());
                                                                                                                    Level_value.add(String.valueOf(Level.getOID()));
                                                                                                            }
                                                                                                    %>
                                                                                                    <%= ControlCombo.draw(FrmEmpDocListMutation.fieldNames[FrmEmpDocListMutation.FRM_FIELD_LEVEL_ID],"formElemen",null,String.valueOf(levelId==0?empDocListMutation.getLevelId():levelId), Level_value, Level_key, "") %> </td>
                                                                                                  </tr>
                                                                                                   <tr> 
                                                                                                    <td width="19%">Work From</td>
                                                                                                    <td width="1%">:</td>
                                                                                                    <td width="80%"> 
                                                                                                  <input type="hidden" name="<%=FrmEmpDocListMutation.fieldNames[FrmEmpDocListMutation.FRM_FIELD_WORK_FROM]%>" value="1" class="formElemen" >
                                                                                                <%=ControlDate.drawDateWithStyle(FrmEmpDocListMutation.fieldNames[FrmEmpDocListMutation.FRM_FIELD_WORK_FROM], dateF == null ? empDocListMutation.getWorkFrom() : dateF, 20, -150, "formElemen")%> 
                                                                                                    </td>
                                                                                                  </tr>


                                                                                                  <tr> 
                                                                                                    <td width="19%">&nbsp;</td>
                                                                                                    <td width="1%">&nbsp;</td>
                                                                                                    <td width="80%"> 
                                                                                                      <input type="submit" name="Submit" value="Save" onClick="javascript:cmdSave()">
                                                                                                    <!--  <input type="submit" name="Submit" value="Add Employee" onClick="javascript:cmdAdd()">
                                                                                                    -->
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
                                <%@include file="../footer.jsp" %>
                            </td>
                            
            </tr>
            <%}else{%>
            <tr> 
                <td colspan="2" height="20" > <!-- #BeginEditable "footer" --> 
      <%@ include file = "../main/footer.jsp" %>
                <!-- #EndEditable --> </td>
            </tr>
            <%}%>
        </table>
    </body>
    <!-- #BeginEditable "script" -->
    <script language="JavaScript">
                //var oBody = document.body;
                //var oSuccess = oBody.attachEvent('onkeydown',fnTrapKD);
    </script>
    <!-- #EndEditable -->
    <!-- #EndTemplate --></html>

