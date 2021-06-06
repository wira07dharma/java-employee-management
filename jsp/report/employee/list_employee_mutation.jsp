<%-- 
    Document   : list_employee_mutation
    Created on : 15-Dec-2017, 09:29:52
    Author     : Gunadi
--%>

<%@page import="com.dimata.gui.jsp.ControlList"%>
<%@page import="com.dimata.gui.jsp.ControlCombo"%>
<%@page import="com.dimata.harisma.entity.payroll.PayPeriod"%>
<%@page import="com.dimata.harisma.entity.payroll.PstPayPeriod"%>
<%@ include file = "../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_MASTERDATA, AppObjInfo.G2_MD_COMPANY, AppObjInfo.OBJ_PUBLIC_HOLIDAY); %>
<%@ include file = "../../main/checkuser.jsp" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%!    public String drawList(JspWriter outJsp, Vector objectClass, int showPayslip, Date dateFrom, Date dateTo) {
        String result = "";
        
        ControlList ctrlist = new ControlList();
        ctrlist.setAreaWidth("100%");
        ctrlist.setListStyle("listgen");
        ctrlist.setTitleStyle("listgentitle");
        ctrlist.setCellStyle("listgensell");
        ctrlist.setHeaderStyle("listgentitle");
            
        ctrlist.addHeader("No", "5%","2", "0");
        ctrlist.addHeader("Payroll Number", "5%","2", "0");
        ctrlist.addHeader("Full Name", "5%","2", "0");
        if (showPayslip == 1){
            ctrlist.addHeader("Previous", "5%","0", "4");
            ctrlist.addHeader("Current", "5%","0", "4");
        } else {
            ctrlist.addHeader("Previous", "5%","1", "3");
            ctrlist.addHeader("Current", "5%","1", "3");
        }
        
        ctrlist.addHeader("Department", "5%","0", "0");
        ctrlist.addHeader("Section", "5%","0", "0");
        ctrlist.addHeader("Position", "5%","0", "0");
        if (showPayslip == 1){
            ctrlist.addHeader("Salary", "5%","0", "0");
        }
        ctrlist.addHeader("Department", "5%","0", "0");
        ctrlist.addHeader("Section", "5%","0", "0");
        ctrlist.addHeader("Position", "5%","0", "0");
        if (showPayslip == 1){
            ctrlist.addHeader("Salary", "5%","0", "0");
        }
        ctrlist.setLinkRow(0);
        ctrlist.setLinkSufix("");

        Vector lstData = ctrlist.getData();

        ctrlist.reset();
        
        int index = -1;
        if (objectClass.size() > 0 && objectClass != null){
            for (int i = 0; i < objectClass.size(); i++) {
                CareerPath careerPrev = (CareerPath) objectClass.get(i);
                CareerPath careerNow = new CareerPath();

                String whereClause = "DATE_ADD("+Formater.formatDate(careerPrev.getWorkTo(), "yyyy-MM-dd")+ ", INTERVAL 1 DAY) <= "
                                + PstCareerPath.fieldNames[PstCareerPath.FLD_WORK_FROM]+" AND "
                                + PstCareerPath.fieldNames[PstCareerPath.FLD_EMPLOYEE_ID]+ " = " + careerPrev.getEmployeeId();
                                
                String order = PstCareerPath.fieldNames[PstCareerPath.FLD_WORK_FROM];
                Vector listCareer = PstCareerPath.list(0, 0, whereClause, order);
                if (listCareer != null && listCareer.size()>0){
                    careerNow = (CareerPath) listCareer.get(0);
                }
                Vector rowx = new Vector();
                rowx.add(""+(i+1));
                rowx.add(PstEmployee.getEmployeeNumber(careerPrev.getEmployeeId()));
                rowx.add(PstEmployee.getEmployeeName(careerPrev.getEmployeeId()));
                rowx.add(careerPrev.getDepartment());
                rowx.add(careerPrev.getSection());
                rowx.add(careerPrev.getPosition());
                if(careerNow != null && listCareer.size() > 0){
                    rowx.add(PstEmployee.getDepartmentName(careerNow.getDepartmentId()));
                    rowx.add(PstEmployee.getSectionName(careerNow.getSectionId()));
                    rowx.add(PstEmployee.getPositionName(careerNow.getPositionId()));
                } else {
                    Employee emp = new Employee();
                    try {
                        emp = PstEmployee.fetchExc(careerPrev.getEmployeeId());
                    } catch (Exception exc){}
                    rowx.add(PstEmployee.getDepartmentName(emp.getDepartmentId()));
                    rowx.add(PstEmployee.getSectionName(emp.getSectionId()));
                    rowx.add(PstEmployee.getPositionName(emp.getPositionId()));
                }
                lstData.add(rowx);
            }
        }
        return ctrlist.drawList();
    }

%>
<%
    int iCommand = FRMQueryString.requestCommand(request);
    int start = FRMQueryString.requestInt(request, "start");
    int prevCommand = FRMQueryString.requestInt(request, "prev_command");
    long payPeriodId = FRMQueryString.requestLong(request, "payPeriodId");
    int mutationType = FRMQueryString.requestInt(request, "mutationType");
    long payrollGroupId = FRMQueryString.requestLong(request, "payrollGroupId");
    
    PayPeriod payPeriod = new PayPeriod();
    
    Vector listEmployee = new Vector();
    if (iCommand == Command.LIST){
        String whereClause = "";
        
        if (payPeriodId != 0){
            try {
                payPeriod = PstPayPeriod.fetchExc(payPeriodId);
            } catch (Exception exc){}
            
            whereClause += PstCareerPath.fieldNames[PstCareerPath.FLD_WORK_TO] + " BETWEEN '"
                            + Formater.formatDate(payPeriod.getStartDate(), "yyyy-MM-dd")+"' AND '"
                            + Formater.formatDate(payPeriod.getEndDate(), "yyyy-MM-dd")+"'";
            
            if (mutationType > -1){
                whereClause += " AND " + PstCareerPath.fieldNames[PstCareerPath.FLD_MUTATION_TYPE] + " = " + mutationType;
            }
            
            if (payrollGroupId != 0){
                whereClause += " AND " + PstEmployee.fieldNames[PstEmployee.FLD_PAYROLL_GROUP] + " = " + payrollGroupId;
            }
            
            listEmployee = PstCareerPath.list(0, 0, whereClause, "");
        }
    }
    
    
%>
<!DOCTYPE html>
<html>
    <head>
        <script language="JavaScript">
            function cmdSearch(){
                document.frmlistmutation.command.value="<%=String.valueOf(Command.LIST)%>";
                document.frmlistmutation.action="list_employee_mutation.jsp";
                document.frmlistmutation.submit();
                
            }
        </script>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>List Employee Mutation</title>
        <!-- #EndEditable -->
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
        <!-- #BeginEditable "styles" -->
        <link rel="stylesheet" href="../../styles/main.css" type="text/css">
        <!-- #EndEditable --> <!-- #BeginEditable "stylestab" -->
        <link rel="stylesheet" href="../../styles/tab.css" type="text/css">
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
             <table width="100%" border="0" cellspacing="3" cellpadding="2">
                <tr>
                   <td width="100%">
                      <table width="100%" border="0" cellspacing="0" cellpadding="0">
                         <tr>
                            <td height="20"> <font color="#FF6600" face="Arial"><strong> <!-- #BeginEditable "contenttitle" -->
                                   List Employee Mutation <!-- #EndEditable -->
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
                                                <table style="border:1px solid <%=garisContent%>" width="100%" border="0" cellspacing="1" cellpadding="1" class="tabbg">
                                                  <tr>
                                                    <td valign="top"> <!-- #BeginEditable "content" -->
                                                      <form name="frmlistmutation" method ="post" action="">
                                                        <input type="hidden" name="command" value="<%=iCommand%>">
                                                        <input type="hidden" name="start" value="<%=start%>">
                                                        <input type="hidden" name="prev_command" value="<%=prevCommand%>">
                                                           <table width="100%" border="0" cellspacing="0" cellpadding="0">
                                                              <tr align="left" valign="top">
                                                                <td height="8"  colspan="3">
                                                                    <table width="100%" border="0" cellspacing="2" cellpadding="2">
                                                                        <tr> 
                                                                            <td width="1%" align="right" nowrap> <div align=left>Periode</div>  </td>
                                                                            <td width="30%" nowrap="nowrap">:
                                                                                <%
                                                                                    Vector period_val = new Vector();
                                                                                    Vector period_key = new Vector();
                                                                                    String order = PstPayPeriod.fieldNames[PstPayPeriod.FLD_START_DATE] + " DESC";
                                                                                    Vector listPeriod = PstPayPeriod.list(0,0,"",order);
                                                                                    period_val.add(""+0);
                                                                                    period_key.add("select");
                                                                                    if (listPeriod != null && listPeriod.size() >0){
                                                                                        for (int i=0; i < listPeriod.size(); i++){
                                                                                            PayPeriod period = (PayPeriod) listPeriod.get(i);
                                                                                            period_key.add(period.getPeriod());
                                                                                            period_val.add(String.valueOf(period.getOID()));
                                                                                        }
                                                                                    }
                                                                                %>
                                                                                <%=ControlCombo.draw("payPeriodId", null, "", period_val, period_key, "")%>
                                                                            </td>
                                                                        </tr>
                                                                        <tr> 
                                                                            <td width="1%" align="right" nowrap> <div align=left>Mutation Type</div></td>
                                                                            <td width="30%" nowrap="nowrap">:
                                                                                <%
                                                                                    Vector mutation_val = new Vector();
                                                                                    Vector mutation_key = new Vector();
                                                                                    
                                                                                    mutation_val.add(""+0);
                                                                                    mutation_key.add("select");
                                                                                    
                                                                                    for (int i=0; i < PstCareerPath.mutationType.length; i++){
                                                                                        mutation_val.add(""+i);
                                                                                        mutation_key.add(PstCareerPath.mutationType[i]);
                                                                                    }
                                                                                %>
                                                                                <%=ControlCombo.draw("mutationType", null, "", mutation_val, mutation_key, "")%>
                                                                            </td>
                                                                        </tr>
                                                                        <tr>
                                                                            <td width="6%" align="right" nowrap><div align="left">Payroll Group </div></td>
                                                                            <td width="30%" nowrap="nowrap">:
                                                                                <%  
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

                                                                                     <%=ControlCombo.draw("payrollGroupId", null, "", payrollGroup_value, payrollGroup_key, "")%>
                                                                            </td>
                                                                        </tr>
                                                                        <tr>
                                                                            <%
                                                                                String chekr = "";
                                                                                String chekr1= "";
                                                                            %>
                                                                            <td width="5%" align="right" nowrap><div align="left">With Payroll </div></td>
                                                                            <td>:
                                                                                <input type="radio" name="statusResign" value="0" <%=chekr%>>
                                                                                No
                                                                                <input type="radio" name="statusResign" value="1" <%=chekr1%>>
                                                                                Yes
                                                                        </tr>
                                                                        <tr> 
                                                                            <td height="13" width="0%">&nbsp;</td>
                                                                            <td width="39%"><a href="javascript:cmdSearch()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image10','','<%=approot%>/images/BtnSearchOn.jpg',1)" id="aSearch"><img name="Image10" border="0" src="<%=approot%>/images/BtnSearch.jpg" width="24" height="24" alt="Search Employee"></a>
                                                                           <img src="<%=approot%>/images/spacer.gif" width="6" height="1">
                                                                          <a href="javascript:cmdSearch()">Search  for Employee</a></td>  
                                                                        </tr>
                                                                    </table>
                                                                </td>
                                                              </tr>
                                                              <%if(iCommand==Command.LIST){%> 
                                                              <tr>
                                                                <td colspan="7">
                                                                    <%=drawList(out,listEmployee,0,payPeriod.getStartDate(),payPeriod.getEndDate())%>
                                                                </td>
                                                              </tr>
                                                              <% } %>
                                                           </table>
                                                      </form>
                                                    </td>
                                                  </tr>
                                                </table>
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
</html>
