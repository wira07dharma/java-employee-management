<%-- 
    Document   : export_excel_eo_ph_report
    Created on : 13-Dec-2017, 16:13:54
    Author     : Gunadi
--%>

<%@page import="java.util.HashMap"%>
<%@page import="com.dimata.harisma.entity.masterdata.PublicHolidays"%>
<%@page import="com.dimata.harisma.entity.employee.PstEmployee"%>
<%@page import="com.dimata.harisma.session.attendance.SessEmpSchedule"%>
<%@page import="com.dimata.harisma.entity.employee.Employee"%>
<%@page import="com.dimata.harisma.session.attendance.PresenceReportDaily"%>
<%@page import="com.dimata.harisma.entity.masterdata.Level"%>
<%@page import="com.dimata.harisma.entity.masterdata.PstLevel"%>
<%@page import="com.dimata.system.entity.PstSystemProperty"%>
<%@page import="com.dimata.util.Formater"%>
<%@page import="com.dimata.harisma.entity.masterdata.PstPublicHolidays"%>
<%@page import="java.util.Vector"%>
<%@page import="java.util.Date"%>
<%@page import="com.dimata.qdep.form.FRMQueryString"%>
<%@page contentType="application/x-msexcel" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%
    response.setHeader("Content-Disposition","attachment; filename=eo_ph_report.xls ");
    int iLevelDw = FRMQueryString.requestInt(request, "levelDW");
    Date selectedDateFrom = FRMQueryString.requestDate(request, "check_date_start");
    Date selectedDateTo = FRMQueryString.requestDate(request, "check_date_finish");
    
    Vector listPH = new Vector();
    Vector listLevel = new Vector();
            
    
    String wherePH = PstPublicHolidays.fieldNames[PstPublicHolidays.FLD_HOLIDAY_DATE]
        + " BETWEEN '" + Formater.formatDate(selectedDateFrom, "yyyy-MM-dd")+"' AND '"
        + Formater.formatDate(selectedDateTo, "yyyy-MM-dd")+"'";

    String order = PstPublicHolidays.fieldNames[PstPublicHolidays.FLD_HOLIDAY_DATE];

    listPH = PstPublicHolidays.listPublicHolidayUnion(selectedDateFrom,selectedDateTo);

    listPH = PstPublicHolidays.listPublicHolidayUnion(selectedDateFrom,selectedDateTo);
        
    String whereLevel = PstLevel.fieldNames[PstLevel.FLD_ENTITLE_PH]+" = 1 ";
    listLevel = PstLevel.list(0, 0, whereLevel, "");
    
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h3>EO/PH Report <%=Formater.formatDate(selectedDateFrom, "yyyy-MM-dd")%> to <%=Formater.formatDate(selectedDateTo, "yyyy-MM-dd")%></h3>
        <%
            if(listPH != null && listPH.size() > 0){
        %>
        <table width="50%" cellspacing="1" border="1">
            <tr>
                <td width="5%" style="text-align: center">No</td>
                <td width="15%" style="text-align: center">PH Date</td>
                <td width="15%" style="text-align: center">PH Name</td>
                <td width="10%" style="text-align: center">Total Staff Working</td>
                <td width="10%" style="text-align: center">Staff Level</td>
                <td width="10%" style="text-align: center">Staff</td>
                <td width="10%" style="text-align: center">Entitle Days</td>
                <td width="10%" style="text-align: center">Total Entitle</td>
            </tr>

        <%
            for(int i=0; i<listPH.size();i++){
                PublicHolidays publicHolidays = (PublicHolidays)listPH.get(i);
                int level = listLevel.size();
                int totalStaff= 0;
                int totalEnt = 0;

                HashMap<Long,Integer> mapEntitle = new HashMap<Long, Integer>();
                HashMap<Long,Integer> mapStaff = new HashMap<Long, Integer>();
                for(int x=0; x < listLevel.size();x++){
                    int jmlStaff = 0;
                    int jmlEnt = 0;
                    Level lvl = (Level) listLevel.get(x);
                    String whereClause = PstEmployee.fieldNames[PstEmployee.FLD_LEVEL_ID]+"="+lvl.getOID()+" AND ("
                                        + PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED]+" = 0 OR "+
                                        PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED_DATE]+" > '"+publicHolidays.getDtHolidayDate()+"')";
                    Vector listEmployee = PstEmployee.list(0, 0, whereClause, "");
                    if (listEmployee != null && listEmployee.size() > 0){
                        for (int xx=0; xx < listEmployee.size(); xx++){
                            Employee emp = (Employee) listEmployee.get(xx);
                            Vector dPresence = SessEmpSchedule.listEmpPresenceDaily(0, publicHolidays.getDtHolidayDate(), 0, emp.getEmployeeNum(), emp.getFullName(), "", 0, 0, "", null, 0, "", 2, 0, 0);
//                                                                    boolean isPresence = SessEmpSchedule.getCheckAdaDataPresence(0, publicHolidays.getDtHolidayDate(), publicHolidays.getDtHolidayDate(), 0, emp.getEmployeeNum(), emp.getFullName(), "", 0, 0, null, 0, "", 0, 0, 0);
//                                                                    if (isPresence){
//                                                                        jmlStaff = jmlStaff + 1;
//                                                                        totalStaff = totalStaff + 1;
//                                                                    }
                            if (dPresence != null && dPresence.size()>0){
                                PresenceReportDaily presenceReportDaily = (PresenceReportDaily) dPresence.get(0);
                                Date dtActualIn1st = (Date) presenceReportDaily.getActualIn1st();
                                Date dtActualOut1st = (Date) presenceReportDaily.getActualOut1st();
                                if (dtActualIn1st != null || dtActualOut1st != null){
                                    jmlStaff = jmlStaff + 1;
                                    totalStaff = totalStaff + 1;
                                    jmlEnt = jmlEnt + lvl.getEntitledPHQty();
                                }
                            }
                        }
                        totalEnt = totalEnt + jmlEnt;
                        mapStaff.put(lvl.getOID(),jmlStaff);
                        mapEntitle.put(lvl.getOID(),jmlEnt);
                    }

                }

        %>
            <tr>
                <td rowspan="<%=level%>" style="vertical-align: middle"><%=(i+1)%></td>
                <td rowspan="<%=level%>" style="vertical-align: middle"><%=publicHolidays.getDtHolidayDate()%></td>
                <td rowspan="<%=level%>" style="vertical-align: middle"><%=publicHolidays.getStDesc()%></td>
                <% Level lvl1 = new Level();
                    try { 
                        lvl1 = (Level) listLevel.get(0);
                    } catch (Exception exc){} 

                %>
                <td rowspan="<%=level%>" style="vertical-align: middle"><%=totalStaff%></td>
                <td ><%=lvl1.getLevel()%></td>
                <td ><%=mapStaff.get(lvl1.getOID())%></td>
                <td ><%=mapEntitle.get(lvl1.getOID())%></td>
                <td rowspan="<%=level%>" style="vertical-align: middle"><%=totalEnt%></td>
            </tr>
            <% for(int x=1; x < listLevel.size();x++){ 
                Level lvl2 = (Level) listLevel.get(x);

            %>
            <tr>
                <td ><%=lvl2.getLevel()%></td>
                <td ><%=mapStaff.get(lvl2.getOID())%></td>
                <td ><%=mapEntitle.get(lvl2.getOID())%></td>
            </tr>
            <% } %>
        <%

            }
        %>
        </table>
        <%
            }
        %>
    </body>
</html>
