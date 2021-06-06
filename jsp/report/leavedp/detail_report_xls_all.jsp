<%-- 
    Document   : detail_report_xls_all
    Created on : 09-Sep-2017, 09:32:52
    Author     : Gunadi
--%>
<%@page import="com.dimata.harisma.entity.attendance.DpStockTaken"%>
<%@page import="com.dimata.harisma.entity.attendance.PstDpStockTaken"%>
<%@page import="com.dimata.harisma.entity.attendance.PstDpStockManagement"%>
<%@page import="com.dimata.harisma.entity.attendance.LlStockTaken"%>
<%@page import="com.dimata.harisma.entity.attendance.PstLlStockTaken"%>
<%@page import="com.dimata.harisma.entity.attendance.PstLLStockManagement"%>
<%@page import="com.dimata.harisma.entity.attendance.LLStockManagement"%>
<%@page import="com.dimata.harisma.entity.attendance.AlStockTaken"%>
<%@page import="com.dimata.harisma.entity.attendance.PstAlStockTaken"%>
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

<%@ include file = "../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_REPORTS, AppObjInfo.G2_LEAVE_REPORT, AppObjInfo.OBJ_LEAVE_DP_DETAIL); %>
<%@ include file = "../../main/checkuser.jsp" %>
<%
    int command = FRMQueryString.requestInt(request, "command");
    
    Vector  allOidAl = new Vector();
    try{
        allOidAl = (Vector) session.getValue("allOidAl");
    } catch(Exception e){}
      
    
    long type = FRMQueryString.requestLong(request, "type");
    Date dateFrom = null;
    Date dateTo = null;
    
    if (command == Command.LIST){
     dateFrom = FRMQueryString.requestDateVer3(request, "dateFrom");
     dateTo = FRMQueryString.requestDateVer3(request, "dateTo");
    }
    
    %>
<%@page contentType="application/x-msexcel" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <link rel="stylesheet" href="../../styles/main.css" type="text/css">
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
        <script type="text/javascript">
            function cmdAsk(){
                document.getElementById("confirm").style.visibility="visible";
            }
            
            function cmdSearch(){
		document.frm.command.value="<%=String.valueOf(Command.LIST)%>";
		document.frm.action="detail_report.jsp";
		document.frm.submit();
            }
        </script>
    </head>
    <body>
        <div class="content-main">
            <form name="frm" method ="post" action="">
                
                <input type="hidden" name="command" value="<%=command%>" />
                <input type="hidden" name="type" value="<%=type%>" />
            <% 
            for (int x=0; x< allOidAl.size(); x++ ){
              
            long oid = (Long) allOidAl.get(x);
            AlStockManagement alStockManagement = new AlStockManagement();
            LLStockManagement llStockManagement = new LLStockManagement();
            DpStockManagement dpStockManagement = new DpStockManagement();

            Employee employee = new Employee();

            try{
                if (type == 1){
                   alStockManagement = PstAlStockManagement.fetchExc(oid);
                   dateFrom = alStockManagement.getEntitleDate();
                   dateTo   = alStockManagement.getExpiredDate();

                   try{
                     employee = PstEmployee.fetchExc(alStockManagement.getEmployeeId()) ;
                   }catch(Exception e){}

                } else if (type == 2) {
                   llStockManagement = PstLLStockManagement.fetchExc(oid);
                   dateFrom = llStockManagement.getEntitledDate();
                   dateTo   = llStockManagement.getExpiredDate2();

                   try{
                     employee = PstEmployee.fetchExc(llStockManagement.getEmployeeId()) ;
                   }catch(Exception e){}
                } else if (type == 0) {
                   dpStockManagement = PstDpStockManagement.fetchExc(oid);
                   //dateFrom = dpStockManagement.getDtStartDate();
                   //dateTo   = dpStockManagement.getDtExpiredDate();

                   try{
                     employee = PstEmployee.fetchExc(dpStockManagement.getEmployeeId()) ;
                   }catch(Exception e){}
                }

            }catch(Exception e){
            }                 


            Vector stockV = new Vector();
            if (type == 1){
             stockV = PstAlStockTaken.listForDetail(alStockManagement.getOID(), dateFrom, dateTo);
            } else if (type == 2){
             stockV = PstLlStockTaken.listForDetail(llStockManagement.getOID(), dateFrom, dateTo);
            } else if (type == 0){
             stockV = PstDpStockTaken.listForDetail(oid, dateFrom, dateTo);
            }
            %>
            <div>&nbsp;</div>
            <table class="tblStyle">
                <tr>
                    <td colspan="5"><%=employee.getFullName() %></td>
                </tr>
                    <tr>
                        <td class="title_tbl">No</td>
                        <td class="title_tbl">Date From</td>
                        <td class="title_tbl">Date To</td>
                        <td class="title_tbl">Tp</td>
                        <td class="title_tbl">Qty</td>
                    </tr>
                    <% for (int i=0; i<stockV.size(); i++){
                        Date from = new Date();
                        Date to = new Date();
                        double qty = 0;
                         String tp = "";
                        if (type == 1){
                            AlStockTaken alStockTaken  = (AlStockTaken) stockV.get(i);
                            from = alStockTaken.getTakenDate();
                            to = alStockTaken.getTakenFinnishDate();
                            qty = alStockTaken.getTakenQty();
                            tp  = "AL";
                        } else if (type == 2){
                            LlStockTaken llStockTaken  = (LlStockTaken) stockV.get(i);
                            from = llStockTaken.getTakenDate();
                            to = llStockTaken.getTakenFinnishDate();
                            qty = llStockTaken.getTakenQty();
                            tp  = "LL";
                        } else if (type == 0){
                            DpStockTaken dpStockTaken  = (DpStockTaken) stockV.get(i);
                            from = dpStockTaken.getTakenDate();
                            to = dpStockTaken.getTakenFinnishDate();
                            qty = dpStockTaken.getTakenQty();
                            tp  = "DP";
                        }
                    
                        
                         
                    %>
                    <tr>
                        <td><%=(i+1)%></td>
                        <td><%=Formater.formatDate(from, "yyyy-MM-dd")%> </td>
                        <td><%=Formater.formatDate(to, "yyyy-MM-dd")%></td>
                        <td><%=tp%></td>
                        <td><%=qty%></td>
                    </tr>
                    <% } %>
                    
                    <div>&nbsp;</div>
            <% } %>
                    
                </table>
            <div>&nbsp;</div>
            </form>
        </div>
    </body>
</html>