<%-- 
    Document   : benefit_input_headcount_detail
    Created on : 21-Sep-2017, 10:36:06
    Author     : Gunadi
--%>
<%@page import="com.dimata.harisma.entity.payroll.PstBenefitPeriod"%>
<%@ include file = "../../main/javainit.jsp" %>
<%@page contentType="application/vnd.ms-excel" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%
    response.setHeader("Content-Disposition","attachment; filename=headcount_detail.xls ");
    long dataBenefitConfigId = FRMQueryString.requestLong(request, "config_id");
    long dataPeriodId = FRMQueryString.requestLong(request, "period_id");
    
    Vector listEmployee = PstBenefitPeriod.getEmployeeList(dataBenefitConfigId, dataPeriodId);
    Vector listPrevEmployee = PstBenefitPeriod.getPreviousEmployeeList(dataBenefitConfigId, dataPeriodId);
    Vector listNewEmployee = PstBenefitPeriod.getNewEmployeeList(dataBenefitConfigId, dataPeriodId);
    Vector listResignedEmployee = PstBenefitPeriod.getResignedEmployeeList(dataBenefitConfigId, dataPeriodId);
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>HARISMA - Headcount Detail</title>
    </head>
    <table border="0" cellspacing="0" cellpadding="0">
        <tr>
            <td valign="top">
                <div id="caption">Last Month Head Count</div>
                <table border ="1" cellspacing="1" cellpadding="1">
                    <tr>
                        <td>No</td>
                        <td>Payroll Number</td>
                    </tr>
                <%
                    if (listPrevEmployee.size() > 0 && listPrevEmployee != null){
                        for (int i = 0; i < listPrevEmployee.size(); i++){
                            Employee employee = (Employee) listPrevEmployee.get(i);
                %>
                            <tr>
                                <td><%=(i+1)%></td>
                                <td><%=employee.getEmployeeNum()%></td>
                            </tr>
                <%
                        }
                    }
                %>
                </table>
            </td>
            <td></td>
            <td valign="top">
                <div id="caption">This Month Head Count</div>
                <table border ="1" cellspacing="1" cellpadding="1">
                    <tr>
                        <td>No</td>
                        <td>Payroll Number</td>
                    </tr>
                <%
                    if (listEmployee.size() > 0 && listEmployee != null){
                        for (int i = 0; i < listEmployee.size(); i++){
                            Employee employee = (Employee) listEmployee.get(i);
                %>
                            <tr>
                                <td><%=(i+1)%></td>
                                <td><%=employee.getEmployeeNum()%></td>
                            </tr>
                <%
                        }
                    }
                %>
                </table>
            </td>
            <td></td>
            <td valign="top">
                <div id="caption">New This Month</div>
                <table border ="1" cellspacing="1" cellpadding="1">
                    <tr>
                        <td>No</td>
                        <td>Payroll Number</td>
                    </tr>
                <%
                    if (listNewEmployee.size() > 0 && listNewEmployee != null){
                        for (int i = 0; i < listNewEmployee.size(); i++){
                            Employee employee = (Employee) listNewEmployee.get(i);
                %>
                            <tr>
                                <td><%=(i+1)%></td>
                                <td><%=employee.getEmployeeNum()%></td>
                            </tr>
                <%
                        }
                    }
                %>
                </table>
            </td>
            <td></td>
            <td valign="top">
                <div id="caption">Resigned From Last Month</div>
                <table border ="1" cellspacing="1" cellpadding="1">
                    <tr>
                        <td>No</td>
                        <td>Payroll Number</td>
                    </tr>
                <%
                    if (listResignedEmployee.size() > 0 && listResignedEmployee != null){
                        for (int i = 0; i < listResignedEmployee.size(); i++){
                            Employee employee = (Employee) listResignedEmployee.get(i);
                %>
                            <tr>
                                <td><%=(i+1)%></td>
                                <td><%=employee.getEmployeeNum()%></td>
                            </tr>
                <%
                        }
                    }
                %>
                </table>
            </td>

        </tr>
    </table>
    </body>
</html>
