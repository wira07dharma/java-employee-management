<%-- 
    Document   : working_hour_summary
    Created on : Dec 13, 2018, 1:55:23 PM
    Author     : IanRizky
--%>

<%@page import="com.dimata.gui.jsp.ControlCombo"%>


<%@page import="com.dimata.gui.jsp.ControlCombo"%>
<%@page import="com.dimata.harisma.entity.payroll.PstPayPeriod"%>
<%@page import="com.dimata.harisma.entity.payroll.PayPeriod"%>
<%@page import="com.dimata.harisma.entity.payroll.SalaryChangeEntity"%>
<%@page import="com.dimata.harisma.entity.payroll.SalaryChangeModel"%>
<%@ include file = "../../main/javainit.jsp" %>
<% int appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_REPORTS, AppObjInfo.G2_PRESENCE_REPORT, AppObjInfo.OBJ_PRESENCE_REPORT);
    int appObjCodePresenceEdit = AppObjInfo.composeObjCode(AppObjInfo.G1_EMPLOYEE, AppObjInfo.G2_ATTENDANCE, AppObjInfo.OBJ_PRESENCE);
    boolean privUpdatePresence = userSession.checkPrivilege(AppObjInfo.composeCode(appObjCodePresenceEdit, AppObjInfo.COMMAND_UPDATE));
%>
<%@ include file = "../../main/checkuser.jsp" %>
<%
    long hrdDepOid = Long.parseLong(String.valueOf(PstSystemProperty.getPropertyLongbyName(OID_HRD_DEPARTMENT)));
    boolean isHRDLogin = hrdDepOid == departmentOid ? true : false;
    long edpSectionOid = Long.parseLong(String.valueOf(PstSystemProperty.getPropertyLongbyName(OID_EDP_SECTION)));
    boolean isEdpLogin = edpSectionOid == sectionOfLoginUser.getOID() ? true : false;
    boolean isGeneralManager = positionType == PstPosition.LEVEL_GENERAL_MANAGER ? true : false;
%>
<%!
    public String getSectionLink(String sectionId){
        String str = "";
        try{
            Section section = PstSection.fetchExc(Long.valueOf(sectionId));
            str = section.getSection();
            return str;
        } catch(Exception e){
            System.out.println(e);
        }
        return str;
    }
%>
<%
    int iCommand = FRMQueryString.requestCommand(request);
    long companyId = FRMQueryString.requestLong(request, "company_id");
    long divisionId = FRMQueryString.requestLong(request, "division_id");
    long departmentId = FRMQueryString.requestLong(request, "department_id");
    long sectionId = FRMQueryString.requestLong(request, "inp_section_id");
    long payrollGroupId = FRMQueryString.requestLong(request, "payrollGroupId");
	long periodId = FRMQueryString.requestLong(request, "inp_period_id");
   
%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Salary Change</title>
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
        <link rel="stylesheet" href="../../styles/main.css" type="text/css">
        <link rel="stylesheet" href="../../styles/tab.css" type="text/css">
        <link href="<%=approot%>/stylesheets/superTables.css" rel="Stylesheet" type="text/css" /> 
        <style type="text/css">
            #menu_utama {color: #0066CC; padding: 9px 14px; border-left: 1px solid #0066CC; font-size: 14px; background-color: #F3F3F3;}
            #mn_utama {color: #FF6600; padding: 5px 14px; border-left: 1px solid #999; font-size: 12px; background-color: #F5F5F5;}
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
            #tdForm {
                padding: 5px;
            }
            .tblStyle {border-collapse: collapse;font-size: 11px;}
            .tblStyle td {padding: 3px 5px; border: 1px solid #CCC;}
            .title_tbl {font-weight: bold;background-color: #DDD; color: #575757;}
        </style>
        <script type="text/javascript">
			function compChange(val) 
            {
                document.frm.command.value = "<%=Command.GOTO%>";
                document.frm.company_id.value = val;
                document.frm.division_id.value = "0";
                document.frm.department_id.value = "0";
                document.frm.action = "working_hour_summary.jsp";
                document.frm.submit();
            }
            function divisiChange(val) 
            {
                document.frm.command.value = "<%=Command.GOTO%>";
                document.frm.division_id.value = val;
                document.frm.action = "working_hour_summary.jsp";
                document.frm.submit();
            }
            function deptChange(val) 
            {
                document.frm.command.value = "<%=Command.GOTO%>";	
                document.frm.department_id.value = val;
                document.frm.action = "working_hour_summary.jsp";
                document.frm.submit();
            }
            function cmdSearch(){ 
                document.frm.command.value="<%=Command.LIST%>";
                document.frm.action="working_hour_summary.jsp";
                document.frm.submit();
            }
            function cmdExportExcel(){	 
                document.frm.action="export_excel/export_excel_working_hour_summary.jsp"; 
                document.frm.target = "ReportExcel";
                document.frm.submit();
            }
        </script>
    </head>
    <body <%=noBack%> bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
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
                                        <td height="20"> <div id="menu_utama">Salary Change Report</div> </td>
                                    </tr>
                                    <tr>
                                        <td>&nbsp;</td>
                                    </tr>
                                    <tr>
                                        <td style="background-color:#EEE;" valign="top">
                                        
                                            <table style="padding:9px; border:1px solid #00CCFF;" <%=garisContent%> width="100%" border="0" cellspacing="1" cellpadding="1" class="tablecolor">
                                                <tr>
                                                    <td valign="top">
                                                        <form name="frm" method="post" action="">
                                                            <input type="hidden" name="command" value="<%=iCommand%>" />
                                                            <input type="hidden" name="" value="" />
                                                            <div id="mn_utama">Search Salary Change</div>
                                                            <table>
																<tr>
                                                                    <td valign="top" id="tdForm">Period</td>
                                                                    <td valign="top" id="tdForm">
                                                                        <select name="inp_period_id">
                                                                            <option value="0">-select-</option>
                                                                        <%
                                                                        String selectedPeriod = "";
                                                                        Vector listPeriod = PstPeriod.list(0, 0, "", "START_DATE DESC"); 
                                                                        if (listPeriod != null && listPeriod.size() > 0){
                                                                            for(int i=0; i<listPeriod.size(); i++){
                                                                                Period periods = (Period)listPeriod.get(i);
                                                                                
                                                                                if (periodId == periods.getOID()){
                                                                                    selectedPeriod = " selected=\"selected\"";
                                                                                } else {
                                                                                    selectedPeriod = " ";
                                                                                }
                                                                                %>
                                                                                <option value="<%=periods.getOID()%>" <%=selectedPeriod%>><%=periods.getPeriod()%></option>
                                                                                <%
                                                                            }
                                                                        }
                                                                        %>
                                                                        </select>
                                                                    </td>
                                                                </tr>
                                                                <%
                                                                    /*
                                                                     * Description : get value Company, Division, Department, and Section
                                                                     * Date : 2015-02-17
                                                                     * Author : Hendra Putu
                                                                     */
                                                                    // List Company
                                                                    Vector comp_value = new Vector(1, 1);
                                                                    Vector comp_key = new Vector(1, 1);
                                                                    comp_value.add("0");
                                                                    comp_key.add("-select-");
                                                                    String comp_selected = "";
                                                                    // List Division
                                                                    Vector div_value = new Vector(1, 1);
                                                                    Vector div_key = new Vector(1, 1);
                                                                    String whereDivision = "COMPANY_ID = " + companyId;
                                                                    div_value.add("0");
                                                                    div_key.add("-select-");
                                                                    String div_selected = "";
                                                                    // List Department
                                                                    Vector depart_value = new Vector(1, 1);
                                                                    Vector depart_key = new Vector(1, 1);
                                                                    String whereComp = "" + companyId;
                                                                    String whereDiv = "" + divisionId;
                                                                    depart_value.add("0");
                                                                    depart_key.add("-select-");
                                                                    String depart_selected = "";
                                                                    // List Section
                                                                    Vector section_value = new Vector(1, 1);
                                                                    Vector section_key = new Vector(1, 1);
                                                                    Vector section_v = new Vector();
                                                                    Vector section_k = new Vector();
                                                                    String whereSection = "DEPARTMENT_ID = " + departmentId;
                                                                    section_value.add("0");
                                                                    section_key.add("-select-");
                                                                    section_v.add("0");
                                                                    section_k.add("-select-");
                                                                    /* List variabel if not isHRDLogin || isEdpLogin || isGeneralManager */
                                                                        
                                                                    String strComp = "";
                                                                    String strCompId = "0";
                                                                    String strDivisi = "";
                                                                    String strDivisiId = "0";
                                                                    String strDepart = "";
                                                                    String strDepartId = "0";
                                                                    String strSection = "";
                                                                    String strSectionId = "0";
                                                                    if (isHRDLogin || isEdpLogin || isGeneralManager) {
                                                                        
                                                                        Vector listComp = PstCompany.list(0, 0, "", " COMPANY ");
                                                                        for (int i = 0; i < listComp.size(); i++) {
                                                                            Company comp = (Company) listComp.get(i);
                                                                            if (comp.getOID() == companyId) {
                                                                                comp_selected = String.valueOf(companyId);
                                                                            }
                                                                            comp_key.add(comp.getCompany());
                                                                            comp_value.add(String.valueOf(comp.getOID()));
                                                                        }
                                                                            
                                                                        Vector listDiv = PstDivision.list(0, 0, whereDivision, " DIVISION ");
                                                                        if (listDiv != null && listDiv.size() > 0) {
                                                                            for (int i = 0; i < listDiv.size(); i++) {
                                                                                Division division = (Division) listDiv.get(i);
                                                                                if (division.getOID() == divisionId) {
                                                                                    div_selected = String.valueOf(divisionId);
                                                                                }
                                                                                div_key.add(division.getDivision());
                                                                                div_value.add(String.valueOf(division.getOID()));
                                                                            }
                                                                        }
                                                                            
                                                                        Vector listDepart = PstDepartment.listDepartmentVer1(0, 0, whereComp, whereDiv);
                                                                        if (listDepart != null && listDepart.size() > 0) {
                                                                            for (int i = 0; i < listDepart.size(); i++) {
                                                                                Department depart = (Department) listDepart.get(i);
                                                                                if (depart.getOID() == departmentId) {
                                                                                    depart_selected = String.valueOf(departmentId);
                                                                                }
                                                                                depart_key.add(depart.getDepartment());
                                                                                depart_value.add(String.valueOf(depart.getOID()));
                                                                            }
                                                                        }
                                                                            
                                                                        Vector listSection = PstSection.list(0, 0, whereSection, "");
                                                                        if (listSection != null && listSection.size() > 0) {
                                                                            for (int i = 0; i < listSection.size(); i++) {
                                                                                Section section = (Section) listSection.get(i);
                                                                                section_key.add(section.getSection());
                                                                                section_value.add(String.valueOf(section.getOID()));
                                                                                String sectionLink = section.getSectionLinkTo();
                                                                                if ((sectionLink != null) && sectionLink.length()>0) {
                                                                                    
                                                                                    for (String retval : sectionLink.split(",")) {
                                                                                        section_value.add(retval);
                                                                                        section_key.add(getSectionLink(retval));
                                                                                    }
                                                                                }
                                                                            }
                                                                        }
                                                                            
                                                                    } else {
                                                                        // for Company and Division
                                                                        if (emplx.getDivisionId() > 0) {
                                                                            Division empDiv = PstDivision.fetchExc(emplx.getDivisionId());
                                                                            Company empComp = PstCompany.fetchExc(empDiv.getCompanyId());
                                                                            strComp = empComp.getCompany();
                                                                            strCompId = String.valueOf(empComp.getOID());
                                                                            strDivisi = empDiv.getDivision();
                                                                            strDivisiId = String.valueOf(empDiv.getOID());
                                                                        }
                                                                        // for Department
                                                                        if (emplx.getDepartmentId() > 0) {
                                                                            Department empDepart = PstDepartment.fetchExc(emplx.getDepartmentId());
                                                                            strDepart = empDepart.getDepartment();
                                                                            strDepartId = String.valueOf(empDepart.getOID());
                                                                        }
                                                                        // for Section
                                                                        if (emplx.getSectionId() > 0) {
                                                                            Section empSection = PstSection.fetchExc(emplx.getSectionId());
                                                                            strSection = empSection.getSection();
                                                                            strSectionId = String.valueOf(empSection.getOID());
                                                                                
                                                                            section_v.add(String.valueOf(empSection.getOID()));
                                                                            section_k.add(empSection.getSection());
                                                                            String sectionLink = empSection.getSectionLinkTo();
                                                                            if ((sectionLink != null) && sectionLink.length()>0) {
                                                                                
                                                                                for (String retval : sectionLink.split(",")) {
                                                                                    section_v.add(retval);
                                                                                    section_k.add(getSectionLink(retval));
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                %>
                                                                <tr>
                                                                    <td valign="top" id="tdForm">Company</td>
                                                                    <td valign="top" id="tdForm">
                                                                        <%
                                                                            if (isHRDLogin || isEdpLogin || isGeneralManager) {
                                                                        %>
                                                                        <input type="hidden" name="company_id" value="<%=companyId%>" />
                                                                        <%= ControlCombo.draw("inp_company_id", "formElemen", null, comp_selected, comp_value, comp_key, " onChange=\"javascript:compChange(this.value)\"")%>
                                                                        <%
                                                                        } else {
                                                                        %>
                                                                        <input type="hidden" name="inp_company_id" value="<%=strCompId%>" />
                                                                        <input type="text" name="company_nm" disabled="disabled" value="<%=strComp%>" />
                                                                        <%
                                                                            }
                                                                        %>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td valign="top" id="tdForm"><%=dictionaryD.getWord(I_Dictionary.DIVISION) %></td>
                                                                    <td valign="top" id="tdForm">
                                                                        <%
                                                                            if (isHRDLogin || isEdpLogin || isGeneralManager) {
                                                                        %>
                                                                        <input type="hidden" name="division_id" value="<%=divisionId%>" />
                                                                        <%= ControlCombo.draw("inp_division_id", "formElemen", null, div_selected, div_value, div_key, " onChange=\"javascript:divisiChange(this.value)\"")%>
                                                                        <%
                                                                        } else {
                                                                        %>
                                                                        <input type="hidden" name="inp_division_id" value="<%=strDivisiId%>" />
                                                                        <input type="text" name="division_nm" disabled="disabled" value="<%=strDivisi%>" />
                                                                        <%
                                                                            }
                                                                        %>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td valign="top" id="tdForm"><%=dictionaryD.getWord(I_Dictionary.DEPARTMENT) %></td>
                                                                    <td valign="top" id="tdForm">
                                                                        <%
                                                                            if (isHRDLogin || isEdpLogin || isGeneralManager) {
                                                                        %>
                                                                        <input type="hidden" name="department_id" value="<%=departmentId%>" />	
                                                                        <%= ControlCombo.draw("inp_department_id", "formElemen", null, depart_selected, depart_value, depart_key, " onChange=\"javascript:deptChange(this.value)\"")%>
                                                                        <%
                                                                        } else {
                                                                        %>
                                                                        <input type="hidden" name="inp_department_id" value="<%=strDepartId%>" />
                                                                        <input type="text" name="department_nm" disabled="disabled" value="<%=strDepart%>" />
                                                                        <%
                                                                            }
                                                                        %>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td valign="top" id="tdForm"><%=dictionaryD.getWord(I_Dictionary.SECTION) %></td>
                                                                    <td valign="top" id="tdForm">
                                                                        <%
                                                                            if (isHRDLogin || isEdpLogin || isGeneralManager) {
                                                                        %> 
                                                                        <%= ControlCombo.draw("inp_section_id", "formElemen", null, "", section_value, section_key, "")%>  
                                                                        <%
                                                                        } else {
                                                                        %>
                                                                        <%= ControlCombo.draw("inp_section_id", "formElemen", null, "0", section_v, section_k, "")%> 
                                                                        <%
                                                                            }
                                                                        %>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td valign="top" id="tdForm">Payroll Group</td>
                                                                    <td valign="top" id="tdForm">
                                                                        <%   //priska 20150730
                                                                            Vector payrollGroup_value = new Vector(1, 1);
                                                                            Vector payrollGroup_key = new Vector(1, 1);
                                                                            Vector listPayrollGroup = PstPayrollGroup.list(0, 0, "", "PAYROLL_GROUP_NAME");
                                                                            payrollGroup_value.add(""+0);
                                                                            payrollGroup_key.add("select");
                                                                            for (int i = 0; i < listPayrollGroup.size(); i++) {
                                                                                PayrollGroup payrollGroup = (PayrollGroup) listPayrollGroup.get(i);
                                                                                payrollGroup_key.add(payrollGroup.getPayrollGroupName());
                                                                                payrollGroup_value.add(String.valueOf(payrollGroup.getOID()));
                                                                            }

                                                                            %>

                                                                             <%=ControlCombo.draw("payrollGroupId", null, "" + payrollGroupId, payrollGroup_value, payrollGroup_key, "")%>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td>&nbsp;</td>
                                                                    <td><button id="btn" onclick="javascript:cmdExportExcel()">Export To Excel</button></td>
                                                                </tr>
                                                                <tr>
                                                                    <td colspan="2">&nbsp;</td>
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
    </body>

                
    <!-- #EndEditable --> <!-- #EndTemplate -->
</html>