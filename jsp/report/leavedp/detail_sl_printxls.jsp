<%-- 
    Document   : detail_sl_printxls
    Created on : 19-Sep-2017, 15:01:52
    Author     : Gunadi
--%>
<%@page import="com.dimata.harisma.entity.masterdata.PstScheduleCategory"%>
<%@page import="com.dimata.harisma.entity.masterdata.PstScheduleSymbol"%>
<%@page import="com.dimata.harisma.entity.masterdata.ScheduleSymbol"%>
<%@page import="com.dimata.system.entity.PstSystemProperty"%>
<%@page import="com.dimata.harisma.entity.leave.PstLeaveApplication"%>
<%@page import="com.dimata.harisma.entity.leave.PstSpecialUnpaidLeaveTaken"%>
<%@page import="com.dimata.harisma.session.employee.SessTmpSpecialEmployee"%>
<%@page import="com.dimata.harisma.session.employee.SessSpecialEmployee"%>
<%@page import="com.dimata.harisma.form.search.FrmSrcSpecialEmployeeQuery"%>
<%@page import="com.dimata.harisma.session.employee.SearchSpecialQuery"%>
<%@ page language="java" %>
<!-- package java -->
<%@ page import ="java.util.*,
                  com.dimata.harisma.entity.attendance.PstAlStockManagement,
                  com.dimata.harisma.entity.attendance.AlStockManagement,
                  com.dimata.harisma.form.attendance.FrmAlStockManagement,
                  com.dimata.harisma.form.masterdata.CtrlPosition,
                  com.dimata.harisma.form.masterdata.FrmPosition,
                  com.dimata.harisma.form.attendance.CtrlAlStockManagement,
                  com.dimata.gui.jsp.ControlList,
                  com.dimata.harisma.entity.employee.Employee,
                  com.dimata.harisma.entity.masterdata.LeavePeriod,
                  com.dimata.util.Command,
                  com.dimata.gui.jsp.ControlDate,
                  com.dimata.gui.jsp.ControlCombo,
                  com.dimata.util.Formater,
                  com.dimata.qdep.form.FRMQueryString,
                  com.dimata.harisma.entity.masterdata.PstLeavePeriod,
		  com.dimata.harisma.entity.attendance.ALStockReporting,
                  com.dimata.harisma.entity.attendance.DpStockManagement,
                  com.dimata.harisma.entity.masterdata.PstDepartment,
                  com.dimata.harisma.entity.masterdata.Department,
                  com.dimata.harisma.entity.employee.PstEmployee,
                  com.dimata.harisma.entity.search.SrcLeaveManagement,
                  com.dimata.harisma.form.search.FrmSrcLeaveManagement,				  
                  com.dimata.harisma.session.attendance.AnnualLeaveMontly,
                  com.dimata.harisma.session.leave.*,
                  com.dimata.harisma.session.attendance.*,                  
                  com.dimata.harisma.session.leave.SessLeaveApp,com.dimata.harisma.session.leave.RepItemLeaveAndDp"%>
<!-- package qdep -->

<%@page contentType="application/x-msexcel" pageEncoding="UTF-8"%>
<%
    response.setHeader("Content-Disposition","attachment; filename=sl_detail.xls ");
    int iCommand = FRMQueryString.requestCommand(request);

    String empName = FRMQueryString.requestString(request, "empname");
    String empNum = FRMQueryString.requestString(request, "empnum");
    long oidLevel = FRMQueryString.requestLong(request, "level");
    String[] oidDept = FRMQueryString.requestStringValues(request, "department");
    String[] oidPayGroup = FRMQueryString.requestStringValues(request, "payroll_group");
    String dateFrom = FRMQueryString.requestString(request, "date_from");
    String dateTo = FRMQueryString.requestString(request, "date_to");

    int year = Calendar.getInstance().get(Calendar.YEAR);
    if (dateFrom.equals("") && dateTo.equals("")){
        dateFrom = year+"-01-01";
        dateTo = year+"-12-31";
    }
    Vector<String> whereCollect = new Vector<String>();
    String whereClauseEmp = PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED]+" = 0";
    
    if (!empName.equals("")){
        whereClauseEmp = PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]+" LIKE '%"+empName+"%'";
        whereCollect.add(whereClauseEmp);
    }
    if (!empNum.equals("")){
        whereClauseEmp = PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]+" LIKE '%"+empNum+"%'";
        whereCollect.add(whereClauseEmp);
    }
    if (oidDept != null){
        String inDept = "";
        for (int i=0; i < oidDept.length; i++){
            if (Long.valueOf(oidDept[i])> 0){
                inDept = inDept + ","+ oidDept[i];
            }
        }
        if (!inDept.equals("")){
            inDept = inDept.substring(1);
            whereClauseEmp = PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID]+" IN ("+inDept+")";
            whereCollect.add(whereClauseEmp);
        }
    }
    if (oidLevel != 0){
        whereClauseEmp = PstEmployee.fieldNames[PstEmployee.FLD_LEVEL_ID]+" = "+oidLevel+"";
        whereCollect.add(whereClauseEmp);
    }
    if (oidPayGroup != null){
        String inPayGroup = "";
        for (int i=0; i < oidDept.length; i++){
            if (Long.valueOf(oidPayGroup[i])> 0){
                inPayGroup = inPayGroup + ","+ oidPayGroup[i];
            }
        }
        if (!inPayGroup.equals("")){
            inPayGroup = inPayGroup.substring(1);
            whereClauseEmp = PstEmployee.fieldNames[PstEmployee.FLD_PAYROLL_GROUP]+" IN ("+inPayGroup+")";
            whereCollect.add(whereClauseEmp);
        }
    }
//    if (!dateFrom.equals("") && !dateTo.equals("")){
//        whereClauseEmp = " odt."+PstSpecialUnpaidLeaveTaken.fieldNames[PstSpecialUnpaidLeaveTaken.FLD_TAKEN_DATE]+" BETWEEN "
//        + "'" +dateFrom+" 00:00:00' AND '"+dateTo+" 23:59:00'" ;
//        whereCollect.add(whereClauseEmp);
//    }
    
    if (whereCollect != null && whereCollect.size()>0){
        whereClauseEmp = "";
        for (int i=0; i<whereCollect.size(); i++){
            String where = (String)whereCollect.get(i);
            whereClauseEmp += where;
            if (i < (whereCollect.size()-1)){
                 whereClauseEmp += " AND ";
            }
        }
    }
    
    Vector listEmployee = new Vector();
    if(iCommand != Command.NONE) {
        listEmployee = PstEmployee.list(0,0,whereClauseEmp, "DEPARTMENT_ID");
    }
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <style type="text/css">
            .tblStyle {border-collapse: collapse;}
            .tblStyle td {padding: 5px 7px; border: 1px solid #CCC; font-size: 12px;}
            .title_tbl {font-weight: bold;background-color: #DDD; color: #575757;}
            .title_page {color:#0db2e1; font-weight: bold; font-size: 14px; background-color: #EEE; border-left: 1px solid #0099FF; padding: 12px 18px;}

            body {color:#373737;}
            #menu_utama {padding: 9px 14px; font-weight: bold; border-left: 1px solid #0099FF; font-size: 14px; background-color: #F3F3F3;}
            #menu_title {color:#0099FF; font-size: 14px;}
            #menu_teks {color:#CCC;}
            
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
            
            .title_part {color:#FF7E00; background-color: #F7F7F7; border-left: 1px solid #0099FF; padding: 9px 11px;}
            
            body {background-color: #EEE;}
            .header {
                
            }
            .content-main {
                padding: 5px 25px 25px 25px;
                margin: 0px 23px 59px 23px;
            }
            .content-info {
                padding: 21px;
                border-bottom: 1px solid #E5E5E5;
            }
            .content-title {
                padding: 21px;
                border-bottom: 1px solid #E5E5E5;
                margin-bottom: 5px;
            }
            #title-large {
                color: #575757;
                font-size: 16px;
                font-weight: bold;
            }
            #title-small {
                color:#797979;
                font-size: 11px;
            }
            .content {
                padding: 21px;
            }
            .box {
                margin: 17px 7px;
                background-color: #FFF;
                color:#575757;
            }
            #box_title {
                padding:21px; 
                font-size: 14px; 
                color: #007fba;
                border-top: 1px solid #28A7D1;
                border-bottom: 1px solid #EEE;
            }
            #box_content {
                padding:21px; 
                font-size: 12px;
                color: #575757;
            }
            .box-info {
                padding:21px 43px; 
                background-color: #F7F7F7;
                border-bottom: 1px solid #CCC;
                -webkit-box-shadow: 0 1px 4px rgba(0, 0, 0, 0.065);
                 -moz-box-shadow: 0 1px 4px rgba(0, 0, 0, 0.065);
                      box-shadow: 0 1px 4px rgba(0, 0, 0, 0.065);
            }
            #title-info-name {
                padding: 11px 15px;
                font-size: 35px;
                color: #535353;
            }
            #title-info-desc {
                padding: 7px 15px;
                font-size: 21px;
                color: #676767;
            }
            
            #photo {
                padding: 7px; 
                background-color: #FFF; 
                border: 1px solid #DDD;
            }
            .btn {
                background-color: #00a1ec;
                border-radius: 3px;
                font-family: Arial;
                color: #EEE;
                font-size: 12px;
                padding: 7px 15px 7px 15px;
                text-decoration: none;
            }

            .btn:hover {
                color: #FFF;
                background-color: #007fba;
                text-decoration: none;
            }
            
            .btn-small {
                font-family: sans-serif;
                text-decoration: none;
                padding: 5px 7px; 
                background-color: #676767; color: #F5F5F5; 
                font-size: 11px; cursor: pointer;
                border-radius: 3px;
            }
            .btn-small:hover { background-color: #474747; color: #FFF;}
            
            .btn-small-e {
                font-family: sans-serif;
                text-decoration: none;
                padding: 3px 5px; 
                background-color: #92C8E8; color: #F5F5F5; 
                font-size: 11px; cursor: pointer;
                border-radius: 3px;
            }
            .btn-small-e:hover { background-color: #659FC2; color: #FFF;}
            
            .btn-small-x {
                font-family: sans-serif;
                text-decoration: none;
                padding: 3px 5px; 
                background-color: #EB9898; color: #F5F5F5; 
                font-size: 11px; cursor: pointer;
                border-radius: 3px;
            }
            .btn-small-x:hover { background-color: #D14D4D; color: #FFF;}
            
            .tbl-main {border-collapse: collapse; font-size: 11px; background-color: #FFF; margin: 0px;}
            .tbl-main td {padding: 4px 7px; border: 1px solid #DDD; }
            #tbl-title {font-weight: bold; background-color: #F5F5F5; color: #575757;}
            
            .tbl-small {border-collapse: collapse; font-size: 11px; background-color: #FFF;}
            .tbl-small td {padding: 2px 3px; border: 1px solid #DDD; }
            
            #caption {
                font-size: 12px;
                color: #474747;
                font-weight: bold;
                padding: 2px 0px 3px 0px;
            }
            #divinput {
                margin-bottom: 5px;
                padding-bottom: 5px;
            }
            
            #div_item_sch {
                background-color: #EEE;
                color: #575757;
                padding: 5px 7px;
            }
            
            .form-style {
                font-size: 12px;
                color: #575757;
                border: 1px solid #DDD;
                border-radius: 5px;
            }
            .form-title {
                padding: 11px 21px;
                margin-bottom: 2px;
                border-bottom: 1px solid #DDD;
                background-color: #EEE;
                border-top-left-radius: 5px;
                border-top-right-radius: 5px;
                font-weight: bold;
            }
            .form-content {
                padding: 21px;
            }
            .form-footer {
                border-top: 1px solid #DDD;
                padding: 11px 21px;
                margin-top: 2px;
                background-color: #EEE;
                border-bottom-left-radius: 5px;
                border-bottom-right-radius: 5px;
            }
            #confirm {
                font-size: 12px;
                padding: 7px 0px 8px 15px;
                background-color: #FF6666;
                color: #FFF;
                visibility: hidden;
            }
            #btn-confirm-y {
                padding: 7px 15px 8px 15px;
                background-color: #F25757; color: #FFF; 
                font-size: 12px; cursor: pointer;
            }
            #btn-confirm-n {
                padding: 7px 15px 8px 15px;
                background-color: #E34949; color: #FFF; 
                font-size: 12px; cursor: pointer;
            }
            .footer-page {
                font-size: 12px;
            }
            #record_count{
                font-size: 12px;
                font-weight: bold;
                padding-bottom: 9px;
            }
            .caption {font-weight: bold; padding-bottom: 3px;}
            .divinput {margin-bottom: 7px;}
            #payroll_num {
                background-color: #DEDEDE;
                border-radius: 3px;
                font-family: Arial;
                font-weight: bold;
                color: #474747;
                font-size: 12px;
                padding: 5px 11px 5px 11px;
                cursor: pointer;
            }
        </style>
    </head>
    <body>
        <%
            String department = "";
            if (oidDept != null){
                for (int x=0; x<oidDept.length;x++){
                    Department dept = new Department();
                    try {
                        dept = PstDepartment.fetchExc(Long.valueOf(oidDept[x]));
                    } catch (Exception exc){
                        System.out.println(exc.toString());
                    }
                    if (dept != null){
                        if (department.equals("")){
                            department = dept.getDepartment();
                        } else {
                            department = department + " , " + dept.getDepartment();
                        }
                    }
                }
            }
            
        String companyName="";
        try{
            companyName = String.valueOf(PstSystemProperty.getValueByName("PRINT_HEADER"));
        }catch(Exception ex){
            companyName=""; 
        }    
        
            if (listEmployee.size()>0 && listEmployee != null){
                Vector listSymbolId = PstScheduleSymbol.getScheduleId(PstScheduleCategory.CATEGORY_SPECIAL_LEAVE);
        %>
        <table border="0">
            <tr>
                <td colspan="4"><strong><%=companyName%></strong></td>
            </tr>
            <tr>
                <td colspan="4"><strong>Special Leave Report</strong></td>
            </tr>
            <tr>
                <td colspan="4"><strong>Period <%=dateFrom%> - <%=dateTo%></strong></td>
            </tr>
        </table>
            <table class="tblStyle" border="1" id="table">
                <tr>
                    <td class="title_tbl" style="text-align:center; vertical-align:middle" rowspan="2">No</td>
                    <td class="title_tbl" style="text-align:center; vertical-align:middle" rowspan="2">PAYROLL</td>
                    <td class="title_tbl" style="text-align:center; vertical-align:middle" rowspan="2">EMPLOYEE</td>
                    <% 
                        for (int x=0;x<listSymbolId.size();x++){
                            long oidScheduleSL = 0;
                            ScheduleSymbol schSymbol = new ScheduleSymbol();
                            if(listSymbolId.get(x)!=null){
                                oidScheduleSL = ((Long)listSymbolId.get(x)).longValue();
                                try {
                                    schSymbol = PstScheduleSymbol.fetchExc(oidScheduleSL);
                                } catch (Exception exc){
                                    System.out.println(exc.toString());
                                }
                    %>
                                <td class="title_tbl" style="text-align:center; vertical-align:middle" colspan="2"><%=schSymbol.getSchedule()%></td>
                    <%
                            }
                        }
                    %>
                </tr>
                <tr>
                    <% 
                        for (int x=0;x<listSymbolId.size();x++){
                            if(listSymbolId.get(x)!=null){
                    %>
                                <td class="title_tbl" style="text-align:center; vertical-align:middle">TAKEN</td>
                                <td class="title_tbl" style="text-align:center; vertical-align:middle">WILL BE TAKEN</td>
                    <%
                            }
                        }
                    %>
                </tr>
            <%
                float[] totalSLTaken = new float[listSymbolId.size()];
                float[] totalSL2BTaken = new float[listSymbolId.size()];
                for (int i=0; i<listEmployee.size(); i++) { 
                    Employee emp =  (Employee) listEmployee.get(i);
            %>
                    <tr>
                        <td><%=(i+1)%></td>
                        <td><%=emp.getEmployeeNum()%></td>
                        <td><%=emp.getFullName()%></td>
                        <%
                            for (int x=0;x<listSymbolId.size();x++){
                                long oidScheduleSL = 0;
                                ScheduleSymbol schSymbol = new ScheduleSymbol();
                                if(listSymbolId.get(x)!=null){
                                    oidScheduleSL = ((Long)listSymbolId.get(x)).longValue();
                                    String whereClause = "SP."+PstSpecialUnpaidLeaveTaken.fieldNames[PstSpecialUnpaidLeaveTaken.FLD_EMPLOYEE_ID]+" = "+emp.getOID()
                                                        + " AND SP."+PstSpecialUnpaidLeaveTaken.fieldNames[PstSpecialUnpaidLeaveTaken.FLD_SCHEDULED_ID]+" = "+oidScheduleSL
                                                        + " AND (SP."+PstSpecialUnpaidLeaveTaken.fieldNames[PstSpecialUnpaidLeaveTaken.FLD_TAKEN_DATE]+" BETWEEN "
                                                        + "'" +dateFrom+" 00:00:00' AND '"+dateTo+" 23:59:00'" 
                                                        + " OR SP."+PstSpecialUnpaidLeaveTaken.fieldNames[PstSpecialUnpaidLeaveTaken.FLD_TAKEN_FINNISH_DATE]+" BETWEEN "
                                                        + "'" +dateFrom+" 00:00:00' AND '"+dateTo+" 23:59:00'"
                                                        + " OR '" +dateFrom+" 00:00:00' BETWEEN SP."+PstSpecialUnpaidLeaveTaken.fieldNames[PstSpecialUnpaidLeaveTaken.FLD_TAKEN_DATE]
                                                        + " AND SP."+PstSpecialUnpaidLeaveTaken.fieldNames[PstSpecialUnpaidLeaveTaken.FLD_TAKEN_FINNISH_DATE]
                                                        + " OR '"+dateTo+" 23:59:00' BETWEEN SP."+PstSpecialUnpaidLeaveTaken.fieldNames[PstSpecialUnpaidLeaveTaken.FLD_TAKEN_DATE]
                                                        + " AND SP."+PstSpecialUnpaidLeaveTaken.fieldNames[PstSpecialUnpaidLeaveTaken.FLD_TAKEN_FINNISH_DATE]
                                                        + " )";
                                    String whereTaken = whereClause + " AND LA."+PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DOC_STATUS]+" IN (2,3)";
                                    String whereToBeTaken = whereClause + " AND LA."+PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DOC_STATUS]+" IN (0,1)";                        
                                    %>
                                    <td><%=PstSpecialUnpaidLeaveTaken.getSumQtySpcUnpLeaveTkn(whereTaken)%></td>
                                    <td><%=PstSpecialUnpaidLeaveTaken.getSumQtySpcUnpLeaveTkn(whereToBeTaken)%></td>
                                    
                                    <%
                                    totalSLTaken[x] = totalSLTaken[x] + PstSpecialUnpaidLeaveTaken.getSumQtySpcUnpLeaveTkn(whereTaken);
                                    totalSL2BTaken[x] = totalSL2BTaken[x] + PstSpecialUnpaidLeaveTaken.getSumQtySpcUnpLeaveTkn(whereToBeTaken);
                                }
                            }
                        %>
                    </tr>
            <%
                }
            %>  
            <tr>
                <td></td>
                <td>Total</td>
                <td></td>
                <%
                for (int x=0;x<listSymbolId.size();x++){
                    if(listSymbolId.get(x)!=null){
                        %>
                            <td><%=totalSLTaken[x]%></td>
                            <td><%=totalSL2BTaken[x]%></td>
                        <%
                    }
                }
                %>
            </tr>
            </table>
        <%
            }
        %>
    </body>
</html>
