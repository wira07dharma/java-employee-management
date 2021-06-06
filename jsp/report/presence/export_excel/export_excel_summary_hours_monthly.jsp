<%-- 
    Document   : export_excel_summary_hoours_monthly
    Created on : 11-Dec-2017, 15:37:25
    Author     : Gunadi
--%>
<%@page import="com.dimata.harisma.session.attendance.SessPresence"%>
<%@page import="com.dimata.harisma.entity.employee.PstEmployee"%>
<%@page import="com.dimata.harisma.session.attendance.SessEmpSchedule"%>
<%@ page import ="java.util.*"%>
<%@ page import = "java.util.Date" %>
<%@ page import = "java.text.*" %>

<%@ page import ="com.dimata.gui.jsp.*"%>
<%@ page import ="com.dimata.util.*"%>
<%@ page import ="com.dimata.qdep.form.*"%>
<%@page contentType="application/x-msexcel" pageEncoding="UTF-8"%>
<%!    private static Vector logicParser(String text) {
        Vector vector = LogicParser.textSentence(text);
        for (int i = 0; i < vector.size(); i++) {
            String code = (String) vector.get(i);
            if (((vector.get(vector.size() - 1)).equals(LogicParser.SIGN))
                    && ((vector.get(vector.size() - 1)).equals(LogicParser.ENGLISH))) {
                vector.remove(vector.size() - 1);
            }
        }
        return vector;
    }
%>
<%!

    public String drawList(HttpSession session){
        
        ControlList ctrlList = new ControlList();
        if(session.getValue("SESS_HOURS_MONTHLY")!=null){
            ctrlList = (ControlList)session.getValue("SESS_HOURS_MONTHLY");
            Vector lstData = ctrlList.getData();
            if(session.getValue("SESS_HOURS_DATA")!=null){
                Vector rowX = (Vector)session.getValue("SESS_HOURS_DATA");
                if (rowX != null && rowX.size()>0){
                    for (int i = 0 ; i < rowX.size();i++){
                        Vector rowData = (Vector)rowX.get(i);
                        lstData.add(rowData);
                    }
                }
            }
        }
        
        return ctrlList.drawList();
    }
        
        
    
%>
<%
    response.setHeader("Content-Disposition","attachment; filename=summary_hours_monthly.xls ");
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <style>
            table, th, td{
                border: 1px solid black;
            }
            .listheaderJs{
                vertical-align: middle;
                text-align: center;
            }
        </style>
    </head>
    <body>
        <h3>Summary Hours Monthly</h3>
        <%= drawList(session) %>
    </body>
</html>
