<%-- 
    Document   : export_excel_public_holiday
    Created on : 12-Dec-2017, 14:10:08
    Author     : Gunadi
--%>
<%@page import="com.dimata.harisma.entity.masterdata.PstReligion"%>
<%@page import="com.dimata.harisma.entity.masterdata.Religion"%>
<%@page import="com.dimata.util.Formater"%>
<%@page import="com.dimata.harisma.entity.masterdata.PublicHolidays"%>
<%@page import="java.util.Vector"%>
<%@page import="com.dimata.harisma.entity.masterdata.PstPublicHolidays"%>
<%@page import="com.dimata.qdep.form.FRMQueryString"%>
<%
    response.setHeader("Content-Disposition","attachment; filename=public_holiday.xls ");
    
    String stCurrYear = FRMQueryString.requestString(request, "curr_year");
    
    String whereClause = "Year("+PstPublicHolidays.fieldNames[PstPublicHolidays.FLD_HOLIDAY_DATE]+") = '"+ stCurrYear +"'";
    String orderClause = PstPublicHolidays.fieldNames[PstPublicHolidays.FLD_HOLIDAY_DATE];
    
    Vector listHoliday = PstPublicHolidays.list(0,0, whereClause , orderClause);
%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h3>Public Holiday <%=stCurrYear%></h3>
        <%
            if (listHoliday != null && listHoliday.size()>0){
        %>
            <table border="1">
                <tr>
                    <td style="text-align: center"><strong>No</strong></td>
                    <td style="text-align: center"><strong>Date From</strong></td>
                    <td style="text-align: center"><strong>Date To</strong></td>
                    <td style="text-align: center"><strong>Public Holidays</strong></td>
                    <td style="text-align: center"><strong>Entitlement</strong></td>
                    <td style="text-align: center"><strong>Total Days</strong></td>
                </tr>
        <%
                for (int i=0; i < listHoliday.size();i++){
                    PublicHolidays objPublicHolidays = (PublicHolidays)listHoliday.get(i);
                    String ent = "";
                    if (objPublicHolidays.getiHolidaySts() == PstPublicHolidays.STS_NATIONAL){
                        ent = PstPublicHolidays.stHolidaySts[PstPublicHolidays.STS_NATIONAL];
                    } else if (objPublicHolidays.getiHolidaySts() == PstPublicHolidays.STS_BLACK_DAY){
                        ent = PstPublicHolidays.stHolidaySts[PstPublicHolidays.STS_NATIONAL];
                    } else if (objPublicHolidays.getiHolidaySts() == PstPublicHolidays.STS_YELLOW_DAY){
                        ent = PstPublicHolidays.stHolidaySts[PstPublicHolidays.STS_NATIONAL];
                    } else {
                        Religion religion = new Religion();
                        try {
                            religion = PstReligion.fetchExc(objPublicHolidays.getiHolidaySts());
                            ent = religion.getReligion();
                        } catch (Exception exc){
                            ent = "-";
                        }
                    }
        %>
                <tr>
                    <td><%=(i+1)%></td>
                    <td><%=Formater.formatDate(objPublicHolidays.getDtHolidayDate(), "dd-MM-yyyy")%></td>
                    <td><%=Formater.formatDate(objPublicHolidays.getDtHolidayDateTo(), "dd-MM-yyyy")%></td>
                    <td><%=objPublicHolidays.getStDesc()%></td>
                    <td><%=ent%></td>
                    <td><%=objPublicHolidays.getDays()%></td>
                </tr>
        <%
                }
        %>
        </table>
        <%
            }
        %>
        
            
        
    </body>
</html>
