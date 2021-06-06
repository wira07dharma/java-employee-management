<%-- 
    Document   : careerpath_report_xls
    Created on : Jul 16, 2018, 1:25:00 PM
    Author     : dimata005
--%>
<%@page import="com.dimata.util.Formater"%>
<%@page import="com.dimata.util.Command"%>
<%@ include file = "../../../main/javainit.jsp" %>
<%--<%@page contentType="application/x-msexcel" pageEncoding="UTF-8"%>--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    int iCommand = FRMQueryString.requestCommand(request);
    int start = FRMQueryString.requestInt(request, "start");
    int prevCommand = FRMQueryString.requestInt(request, "prev_command");
    
    long oidCompany= FRMQueryString.requestLong(request, "oidCompany"); 
    long oidDivision = FRMQueryString.requestLong(request, "division");
    long oidDepartment = FRMQueryString.requestLong(request, "department");
    long oidSection = FRMQueryString.requestLong(request, "section");
    String searchNr = FRMQueryString.requestString(request, "searchNr");
    String searchName = FRMQueryString.requestString(request, "searchName");
    
    Vector listData = new Vector();
    if (iCommand == Command.LIST){
        Vector<String> whereCollectEmp = new Vector<String>();
        String whereEmployee = "";
        
        if (oidCompany != 0){
            whereCollectEmp.add(PstEmployee.fieldNames[PstEmployee.FLD_COMPANY_ID]+" = "+oidCompany);
        }
        
        if (oidDivision != 0){
            whereCollectEmp.add(PstEmployee.fieldNames[PstEmployee.FLD_DIVISION_ID]+" = "+oidDivision);
        }
        
        if (oidDepartment != 0){
            whereCollectEmp.add(PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID]+" = "+oidDepartment);
        }
        
        if (oidSection != 0){
            whereCollectEmp.add(PstEmployee.fieldNames[PstEmployee.FLD_SECTION_ID]+" = "+oidSection);
        }
        
        if (!searchName.equals("")){
            whereCollectEmp.add(PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]+" LIKE '%"+searchName+"%'");
        }
        
        if (!searchNr.equals("")){
            whereCollectEmp.add(PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]+" LIKE '%"+searchNr+"%'");
        }
        
        if (whereCollectEmp != null && whereCollectEmp.size()>0){
            for (int i=0; i<whereCollectEmp.size(); i++){
                String where = (String)whereCollectEmp.get(i);
                whereEmployee += where;
                if (i < (whereCollectEmp.size()-1)){
                     whereEmployee += " AND ";
                }
            }
        }
        
        listData = PstEmployee.list(0, 0, whereEmployee, PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]);
    }    
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <table border="1" style="font-size: 14px">
            <tr>
                <td>No</td>
                <td>Payroll Number</td>
                <td>Full Name</td>
                <td>Company</td>
                <td>Division</td>
                <td>Department</td>
                <td>Section</td>
                <td>Position</td>
                <td>Level</td>
                <td>Category</td>
                <td>History From</td>
                <td>History To</td>
            </tr>
            <%
                int no = 0;
                if (listData.size() > 0){
                    for (int i=0; i < listData.size(); i++){
                        Employee emp = (Employee) listData.get(i);
                        
                        Vector listCareer = PstCareerPath.list(0, 0, PstCareerPath.fieldNames[PstCareerPath.FLD_EMPLOYEE_ID]+"="+emp.getOID(), PstCareerPath.fieldNames[PstCareerPath.FLD_WORK_FROM]);
                        if (!listCareer.isEmpty()){
                            no++;
                            if (listCareer.size() > 1){
                                CareerPath career = (CareerPath) listCareer.get(0);
                                %>
                                    <tr>
                                        <td rowspan="<%=listCareer.size()%>"><%=no%></td> 
                                        <td rowspan="<%=listCareer.size()%>"><%=emp.getEmployeeNum()%></td> 
                                        <td rowspan="<%=listCareer.size()%>"><%=emp.getFullName()%></td>
                                        <td><%=career.getCompany()%></td>
                                        <td><%=career.getDivision()%></td>
                                        <td><%=career.getDepartment()%></td>
                                        <td><%=career.getSection()%></td>
                                        <td><%=career.getPosition()%></td>
                                        <td><%=career.getLevel()%></td>
                                        <td><%=career.getEmpCategory()%></td>
                                        <td><%=Formater.formatTimeLocale(career.getWorkFrom(), "dd MMMM yyyy")%></td>
                                        <td><%=Formater.formatTimeLocale(career.getWorkTo(), "dd MMMM yyyy")%></td>
                                    </tr>
                                <%
                                    for (int x=1; x < listCareer.size();x++){
                                       career = (CareerPath) listCareer.get(x);
                                %>    
                                        <tr>
                                            <td><%=career.getCompany()%></td>
                                            <td><%=career.getDivision()%></td>
                                            <td><%=career.getDepartment()%></td>
                                            <td><%=career.getSection()%></td>
                                            <td><%=career.getPosition()%></td>
                                            <td><%=career.getLevel()%></td>
                                            <td><%=career.getEmpCategory()%></td>
                                            <td><%=Formater.formatTimeLocale(career.getWorkFrom(), "dd MMMM yyyy")%></td>
                                            <td><%=Formater.formatTimeLocale(career.getWorkTo(), "dd MMMM yyyy")%></td>
                                        </tr>
                                <%    
                                    }
                            } else {
                                CareerPath career = (CareerPath) listCareer.get(0);
                                %>
                                    <tr>
                                        <td rowspan="<%=listCareer.size()%>"><%=no%></td> 
                                        <td rowspan="<%=listCareer.size()%>"><%=emp.getEmployeeNum()%></td> 
                                        <td rowspan="<%=listCareer.size()%>"><%=emp.getFullName()%></td>
                                        <td><%=career.getCompany()%></td>
                                        <td><%=career.getDivision()%></td>
                                        <td><%=career.getDepartment()%></td>
                                        <td><%=career.getSection()%></td>
                                        <td><%=career.getPosition()%></td>
                                        <td><%=career.getLevel()%></td>
                                        <td><%=career.getEmpCategory()%></td>
                                        <td><%=Formater.formatTimeLocale(career.getWorkFrom(), "dd MMMM yyyy")%></td>
                                        <td><%=Formater.formatTimeLocale(career.getWorkTo(), "dd MMMM yyyy")%></td>
                                    </tr>
                                <%
                            }
                           
                        }
                    }
                }
            %>
        </table>
    </body>
</html>
