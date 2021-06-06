<%-- 
    Document   : summary_hours_monthly
    Created on : 05-Dec-2017, 19:09:14
    Author     : Gunadi
--%>
<%@page import="com.dimata.harisma.session.attendance.SessPresence"%>
<%@page import="com.dimata.util.LogicParser"%>
<%@page import="com.dimata.harisma.entity.overtime.PstOvertimeDetail"%>
<%@page import="com.dimata.util.DateCalc"%>
<%@page import="com.dimata.harisma.entity.attendance.Presence"%>
<%@page import="com.dimata.harisma.entity.attendance.PstPresence"%>
<%@page import="com.dimata.harisma.session.attendance.PresenceReportDaily"%>
<%@page import="com.dimata.harisma.session.attendance.SummaryEmployeeAttendance"%>
<%@page import="com.dimata.gui.jsp.ControlList"%>
<%@page import="com.dimata.gui.jsp.ControlDate"%>
<%@page import="com.dimata.gui.jsp.ControlCombo"%>
<%@page import="com.dimata.harisma.session.attendance.SessEmpSchedule"%>
<%@ include file = "../../main/javainit.jsp" %>
<% int appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_REPORTS, AppObjInfo.G2_PRESENCE_REPORT, AppObjInfo.OBJ_PRESENCE_REPORT);
    int appObjCodePresenceEdit = AppObjInfo.composeObjCode(AppObjInfo.G1_EMPLOYEE, AppObjInfo.G2_ATTENDANCE, AppObjInfo.OBJ_PRESENCE);
    boolean privUpdatePresence = userSession.checkPrivilege(AppObjInfo.composeCode(appObjCodePresenceEdit, AppObjInfo.COMMAND_UPDATE));
%>
<%@ include file = "../../main/checkuser.jsp" %>
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
<%
    /* Check privilege except VIEW, view is already checked on checkuser.jsp as basic access*/
    //boolean privPrint = userSession.checkPrivilege(AppObjInfo.composeCode(appObjCode, AppObjInfo.COMMAND_PRINT));

    long hrdDepOid = Long.parseLong(String.valueOf(PstSystemProperty.getPropertyLongbyName(OID_HRD_DEPARTMENT)));
    boolean isHRDLogin = hrdDepOid == departmentOid ? true : false;
    long edpSectionOid = Long.parseLong(String.valueOf(PstSystemProperty.getPropertyLongbyName(OID_EDP_SECTION)));
    boolean isEdpLogin = edpSectionOid == sectionOfLoginUser.getOID() ? true : false;
    boolean isGeneralManager = positionType == PstPosition.LEVEL_GENERAL_MANAGER ? true : false;

//cek tipe browser, browser detection
    //String userAgent = request.getHeader("User-Agent");
    //boolean isMSIE = (userAgent!=null && userAgent.indexOf("MSIE") !=-1); 

%>
<%!

    public long getWorkingDuration(Date dtActualIn, Date dtActualOut, long breakLong) {
    
        long duration = 0;
        Date x = new Date();
        Date dt = new Date(breakLong);
        x = dt;
        if (dtActualIn != null && dtActualOut != null) { 
            long iDurTimeIn = dtActualIn.getTime() / 1000; 
            //update by devin 2014-02-26
            // long iDurTimeOut = (dtActualOut.getTime()- breakLong) / 1000; karena bisa saja nilai breaknya mines
            long iDurTimeOut = (dtActualOut.getTime()- Math.abs(breakLong)) / 1000;
            if (iDurTimeIn != iDurTimeOut) {
                duration = (iDurTimeIn == 0 || iDurTimeOut == 0) ? 0 : iDurTimeOut - iDurTimeIn;
            }
        }
        
        return duration;
        
    }

%>
<%!
    public String drawList(JspWriter outJsp,Date selectedDateFrom,Date selectedDateTo,Vector listEmployee,int statusResign, HttpSession session){
            String result = "";
            int max = (int)((selectedDateTo.getTime() - selectedDateFrom.getTime())/(1000*60*60*24));
            ControlList ctrlist = new ControlList();
            ctrlist.setAreaWidth("100%");
            ctrlist.setListStyle("listgen");
            ctrlist.setTitleStyle("listgentitle");
            ctrlist.setCellStyle("listgensell");
            //ctrlist.setCellStyles("listgensellstyles");
            ctrlist.setRowSelectedStyles("rowselectedstyles");
            ctrlist.setHeaderStyle("listheaderJs");
            //ctrlist.setStyleSelectableTableValue("id=\"selectable\""); 
            ctrlist.setMaxFreezingTable(3);
            //mengambil nama dari kode komponent
            ctrlist.addHeader("No", "5%","2", "0");
            ctrlist.addHeader("Payroll Number", "5%","2", "0");
            ctrlist.addHeader("Full Name", "5%","2", "0");
            //ctrlist.addHeader("Division", "5%","2", "0");
            ctrlist.addHeader("Departement", "5%","2", "0");
            
            ctrlist.addHeader("Duration (hour, minutes)", "75%", "1", "" + (max + 1) + "");
                
            Date tmpDate = new Date(selectedDateFrom.getTime());
            Date tmpEndDate = new Date(selectedDateTo.getTime());
            Date dtTemp = new Date(selectedDateFrom.getTime());
            while(dtTemp.before(tmpEndDate) || dtTemp.equals(tmpEndDate) ){
                int k=dtTemp.getDate();
                ctrlist.addHeader("" + k, "2%", "0", "0");
                dtTemp = new Date(dtTemp.getTime()+ 24*60*60*1000);
            }
            ctrlist.addHeader("Summary", "5%","2", "0");
            Vector rowXData = new Vector();
            int no = 0;
            try {
                if (listEmployee != null && listEmployee.size() > 0) {
                    ctrlist.drawListHeaderWithJsVer2(outJsp);
                    for (int idxRecord = 0; idxRecord < listEmployee.size(); idxRecord++) {
                        no++;
                        Vector rowx = new Vector();
                        SummaryEmployeeAttendance summaryEmployeeAttendance = (SummaryEmployeeAttendance) listEmployee.get(idxRecord);
                        rowx.add("" + no);
                        rowx.add("" + summaryEmployeeAttendance.getEmployeeNum());
                        rowx.add("" + summaryEmployeeAttendance.getFullName());
                        //rowx.add(""+summaryEmployeeAttendance.getDivision());
                        rowx.add("" + summaryEmployeeAttendance.getDepartment());
                        //rowx.add(summaryEmployeeAttendance.getSection() != null && summaryEmployeeAttendance.getSection().length() > 0 ? summaryEmployeeAttendance.getSection() : "-");
                            
                        Vector listEmpAttendance = SessEmpSchedule.listEmpPresenceDailyV2(0, selectedDateFrom, selectedDateTo,
                                0, summaryEmployeeAttendance.getEmployeeNum(), "", "", 0, 0, null, 0, "", statusResign, 0, 0);
                        Vector listPresencePersonalInOut = PstPresence.list(0, 0, "", 0, "", selectedDateFrom, selectedDateTo, 0, summaryEmployeeAttendance.getEmployeeNum(),
                                null, "", statusResign, 0, 0);//di tambahkan stsPresenceSel
                        //Vector listOvertimeDaily = PstOvertimeDetail.listOvertime(0, 0, 0L, "",selectedDateFrom, selectedDateTo, 0L, summaryEmployeeAttendance.getEmployeeNum(), "",null,0,0);  
          
                        //Vector vOvertimeDetail = PstOvertimeDetail.listOvertimeDetail(0, 0, 0L, "",selectedDateFrom, selectedDateTo, 0L,                                 summaryEmployeeAttendance.getEmployeeNum(), "",null,0,0);
                        Hashtable breakTimeDuration = PstScheduleSymbol.getBreakTimeDuration();
                        long totDuration = 0;

                        tmpDate = new Date(selectedDateFrom.getTime());
                        tmpEndDate = new Date(selectedDateTo.getTime());
                        dtTemp = new Date(selectedDateFrom.getTime());
                        while(dtTemp.before(tmpEndDate) || dtTemp.equals(tmpEndDate) ){
                            Vector dPresence = SessEmpSchedule.listEmpPresenceDaily(0, dtTemp, 0, summaryEmployeeAttendance.getEmployeeNum(), "", "", 0, 0, "", null, 0, "", statusResign, 0, 0);
                            if (dPresence != null && dPresence.size() > 0) {
                                PresenceReportDaily presenceReportDaily = (PresenceReportDaily) dPresence.get(0);
                                Date dtSchldIn1st = (Date) presenceReportDaily.getScheduleIn1st();
                                Date dtSchldOut1st = (Date) presenceReportDaily.getScheduleOut1st();
                                Date dtActualIn1st = (Date) presenceReportDaily.getActualIn1st();
                                Date dtActualOut1st = (Date) presenceReportDaily.getActualOut1st();
                                    
                                long preBreakOut = 0;
                                long preBreakIn = 0;
                                long breakDuration = 0L;
                                long breakOvertime = 0;
                                Presence presenceBreak = new Presence();
                                Hashtable hashCekTanggalSamaBreak = new Hashtable();
                                if (listPresencePersonalInOut != null && listPresencePersonalInOut.size() > 0) {
                                    Date dtSchDateTime = null;
                                    Date dtpresenceDateTime = null;
                                    Date dtSchEmpScheduleBIn = null;
                                    Date dtSchEmpScheduleBOut = null;
                                    long preBreakOutX = 0;
                                    long preBreakInX = 0;
                                    Date dtBreakOut = null;
                                    Date dtBreakIn = null;
                                    boolean ispreBreakOutsdhdiambil = false;
                                    for (int bIdx = 0; bIdx < listPresencePersonalInOut.size(); bIdx++) {
                                        presenceBreak = (Presence) listPresencePersonalInOut.get(bIdx);
                                        if (presenceBreak.getPresenceDatetime() != null /* update by satrya 2014-01-27 presenceBreak.getScheduleDatetime() !=null*/
                                                && presenceBreak.getEmployeeId() == presenceReportDaily.getEmpId()
                                                && (DateCalc.dayDifference(presenceBreak.getPresenceDatetime(), presenceReportDaily.getSelectedDate()) == 0)) {
                                                    
                                            if (presenceBreak.getStatus() == Presence.STATUS_OUT_PERSONAL) {
                                                preBreakOutX = dtpresenceDateTime == null ? 0 : dtpresenceDateTime.getTime();///yang di pakai mengurangi itu adalah presence PO  
                                                dtBreakOut = dtpresenceDateTime;
                                                if (dtSchEmpScheduleBIn != null && presenceBreak.getPresenceDatetime().getTime() > dtSchEmpScheduleBIn.getTime()) {
                                                    preBreakOut = presenceBreak.getPresenceDatetime().getTime();
                                                    listPresencePersonalInOut.remove(bIdx);
                                                    bIdx = bIdx - 1;
                                                } else if ((presenceBreak.getPresenceDatetime().getTime() < presenceBreak.getScheduleDatetime().getTime())) { ///jika karyawan mendahului istirahat
                                                    preBreakOut = presenceBreak.getPresenceDatetime().getTime();///yang di pakai mengurangi itu adalah presence PO 
                                                    listPresencePersonalInOut.remove(bIdx);
                                                    bIdx = bIdx - 1;
                                                        
                                                } else if (presenceBreak.getScheduleDatetime().getHours() == 0 && presenceBreak.getScheduleDatetime().getMinutes() == 0) {
                                                    preBreakOut = presenceBreak.getPresenceDatetime().getTime();//jika schedulenya 00:00
                                                    listPresencePersonalInOut.remove(bIdx);
                                                    bIdx = bIdx - 1;
                                                } else {
                                                    preBreakOut = presenceBreak.getScheduleDatetime().getTime(); //yang di pakai mengurangi adalah schedule PO
                                                    listPresencePersonalInOut.remove(bIdx);
                                                    bIdx = bIdx - 1;
                                                }
                                            } else if (presenceBreak.getStatus() == Presence.STATUS_IN_PERSONAL) {
                                                preBreakInX = presenceBreak.getPresenceDatetime() == null ? 0 : presenceBreak.getPresenceDatetime().getTime();///yang di pakai mengurangi itu adalah presence PI
                                                dtBreakIn = presenceBreak.getPresenceDatetime();
                                                if (preBreakOut != 0L) {
                                                    //update by satrya 2012-09-27
                                                    //if(presenceBreak.getScheduleDatetime()==null || presenceBreak.getPresenceDatetime().getTime() > presenceBreak.getScheduleDatetime().getTime()){
                                                    //update by satrya 2013-07-28\
                                                    //misal sch BO & BI = 13 s/d 14 dan ada presence BO 15.00 maka yg di set 15.00 untk penguranganya
                                                    if (dtSchEmpScheduleBIn != null && dtBreakOut != null && dtBreakIn != null
                                                            && dtBreakOut.getTime() > dtSchEmpScheduleBIn.getTime() && dtBreakIn.getTime() > dtSchEmpScheduleBIn.getTime()) {
                                                        //karena sudah pasti melewatijam istirahatnya
                                                        long tmpBreakDuration = ((Long) breakTimeDuration.get("" + presenceReportDaily.getScheduleId1())).longValue();
                                                        preBreakIn = presenceBreak.getPresenceDatetime().getTime() + tmpBreakDuration;
                                                        listPresencePersonalInOut.remove(bIdx);
                                                        bIdx = bIdx - 1;
                                                    } else if (presenceBreak.getPresenceDatetime().getTime() > presenceBreak.getScheduleDatetime().getTime()) { ///jika karyawan melewati jam istirahat
                                                        preBreakIn = presenceBreak.getPresenceDatetime().getTime();///yang di pakai mengurangi itu adalah presence PI
                                                        listPresencePersonalInOut.remove(bIdx);
                                                        bIdx = bIdx - 1;
                                                    } else if (presenceBreak.getScheduleDatetime().getHours() == 0 && presenceBreak.getScheduleDatetime().getMinutes() == 0) {
                                                        preBreakIn = presenceBreak.getPresenceDatetime().getTime(); //jika schedulenya 00:00 
                                                        listPresencePersonalInOut.remove(bIdx);
                                                        bIdx = bIdx - 1;
                                                    } else {
                                                        preBreakIn = presenceBreak.getScheduleDatetime().getTime(); //yang di pakai mengurangi adalah schedule PI
                                                        listPresencePersonalInOut.remove(bIdx);
                                                        bIdx = bIdx - 1;
                                                    }
                                                        
                                                    breakDuration = breakDuration + (preBreakIn - preBreakOut);
                                                        
                                                        
                                                    ispreBreakOutsdhdiambil = true;
                                                    preBreakOut = 0L;
                                                        
                                                    //breakDuration = breakDuration + presenceBreak.getPresenceDatetime().getTime()-  preBOut.getPresenceDatetime().getTime(); 
                                                    // preBOut=null;
                                                }
                                            }
                                                
                                        }
                                    }
                                }
                                long iDuration = getWorkingDuration(dtActualIn1st, dtActualOut1st, (breakDuration + breakOvertime));
                                long iDurationHour = (iDuration - (totDuration % 3600)) / 3600;
                                long iDurationMin = iDuration % 3600 / 60;
                                if (!(iDurationHour == 0 && iDurationMin == 0)) {
                                    String strDurationHour = (iDurationHour != 0) ? iDurationHour + "h, " : "";
                                    String strDurationMin = (iDurationMin != 0) ? iDurationMin + "m" : "";
                                    rowx.add(""+strDurationHour + strDurationMin);
                                } else {
                                    rowx.add("-");
                                }
                                totDuration = totDuration + iDuration;
                            } else {
                                    rowx.add("-");
                            }
                            dtTemp = new Date(dtTemp.getTime()+ 24*60*60*1000);
                        }
                        

                        long iDurationHour = (totDuration - (totDuration % 3600)) / 3600;
                        long iDurationMin = totDuration % 3600 / 60;
                        String duration = "";
                        if (!(iDurationHour == 0 && iDurationMin == 0)) {
                            String strDurationHour = (iDurationHour != 0) ? iDurationHour + "h, " : "";
                            String strDurationMin = (iDurationMin != 0) ? iDurationMin + "m" : "";
                            rowx.add("" +strDurationHour + strDurationMin );
                        } else {
                            rowx.add("-");
                        }      
                        
                        rowXData.add(rowx);
                        ctrlist.drawListRowJsVer2(outJsp, 0, rowx, idxRecord);
                            
                            
                    }result ="";
                    ctrlist.drawListEndTableJsVer2(outJsp);  
                    
                    session.putValue("SESS_HOURS_MONTHLY", ctrlist);
                    session.putValue("SESS_HOURS_DATA", rowXData);
                    
                }else{
                    result = "<i>Belum ada data dalam sistem ...</i>";
                }
            } catch (Exception exc) {
            }
            return result;
        }
%>
<%
    Date selectedDateFrom = FRMQueryString.requestDate(request, "check_date_start");
    Date selectedDateTo = FRMQueryString.requestDate(request, "check_date_finish");
    String empNum = FRMQueryString.requestString(request, "emp_number");
    String fullName = FRMQueryString.requestString(request, "full_name");
    String source = FRMQueryString.requestString(request, "source");
    String sStatusResign = FRMQueryString.requestString(request, "statusResign");
    //update by satrya 2012-09-28
    //String status1st = FRMQueryString.requestString(request, "status_schedule1st");
    //String status2nd = FRMQueryString.requestString(request, "status_schedule2nd");         
    SessEmpSchedule sessEmpSchedule = new SessEmpSchedule();
    //FrmPresence frpresence = new FrmPresence();
    int iCommand = FRMQueryString.requestCommand(request);
    int iErrCode = FRMMessage.NONE;
    //update by satrya 2012-07-25
    int prevCommand = FRMQueryString.requestInt(request, "prev_command");
    //int limitStart = FRMQueryString.requestInt(request, "start");
    int vectSize = 0;
    int start = FRMQueryString.requestInt(request, "start");
    //String  whereClause ="";
    //update by satrya 2013-08-14
    long oidCompany = FRMQueryString.requestLong(request, "company_id");
    long oidDivision = FRMQueryString.requestLong(request, "division_id");

    long oidDepartment = FRMQueryString.requestLong(request, "department");
    long oidSection = FRMQueryString.requestLong(request, "section");
    long payrollGroupId =  FRMQueryString.requestLong(request,"payrollGroupId");
    long empCategoryId = FRMQueryString.requestLong(request, "empCategoryId");
    
    String whereClause = " (1=1)";
        if (oidCompany != 0) {
            whereClause = whereClause + " AND HE." + PstEmployee.fieldNames[PstEmployee.FLD_COMPANY_ID] + " = " + oidCompany;
        }
        if (oidDivision != 0) {
            whereClause = whereClause + " AND HE." + PstEmployee.fieldNames[PstEmployee.FLD_DIVISION_ID] + " = " + oidDivision;
        }
        if (oidDepartment != 0) {
            whereClause = whereClause + " AND HE." + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID] + " = " + oidDepartment;
        }
        if (oidSection != 0) {
            whereClause = whereClause + " AND HE." + PstEmployee.fieldNames[PstEmployee.FLD_SECTION_ID] + " = " + oidSection;
        }
        if (payrollGroupId != 0){
            whereClause = whereClause + " AND HE." + PstEmployee.fieldNames[PstEmployee.FLD_PAYROLL_GROUP] + " = " + payrollGroupId;
        }
        if (empCategoryId != 0){
            whereClause = whereClause + " AND HE." + PstEmployee.fieldNames[PstEmployee.FLD_EMP_CATEGORY_ID] + " = " + empCategoryId;
        }
//        if (selectedDateFrom != null && selectedDateTo != null) {
//            whereClause = whereClause + "  AND (( HE." + PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED]
//                    + " = " + PstEmployee.YES_RESIGN + " AND " + "HE. " + PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED_DATE]
//                    + " BETWEEN \"" + Formater.formatDate(selectedDateFrom, "yyyy-MM-dd  00:00:00") + "\"" + " AND " + "\"" + Formater.formatDate(selectedDateTo, "yyyy-MM-dd  23:59:59") + "\""
//                    + " ) OR (HE." + PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED]
//                    + " = " + PstEmployee.NO_RESIGN + "))";
//        }
        int statusResign=0;
        if(sStatusResign!=null && sStatusResign.length()>0){
            statusResign = Integer.parseInt(sStatusResign);
            if (statusResign == PstEmployee.YES_RESIGN) {
                whereClause = whereClause + " AND (HE." + PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED] + " = " + PstEmployee.YES_RESIGN
                        + " AND "+ PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED_DATE]+ " BETWEEN '" + Formater.formatDate(selectedDateFrom, "yyyy-MM-dd")+"'"
                        + " AND '" + Formater.formatDate(selectedDateTo, "yyyy-MM-dd")+"')";
            } else if (statusResign == PstEmployee.NO_RESIGN){
                whereClause = whereClause + " AND (HE." + PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED] + " = " + PstEmployee.NO_RESIGN
                        + " OR "+ PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED_DATE] +" >= '" + Formater.formatDate(selectedDateFrom, "yyyy-MM-dd")+"')";
            }
        }
        if (empNum != null && empNum.length() > 0) {
//            whereClauseEmpTime = whereClauseEmpTime + " AND "+ " emp."+PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]
//                                 + " LIKE '%"+empNum.trim()+"%'";
            Vector vectName = logicParser(empNum);
            whereClause = whereClause + " AND ";
            if (vectName != null && vectName.size() > 0) {
                //whereClause = whereClause + " AND (";

                whereClause = whereClause + " (";
                for (int i = 0; i < vectName.size(); i++) {
                    String str = (String) vectName.get(i);
                    if (!LogicParser.isInSign(str) && !LogicParser.isInLogEnglish(str)) {
                        whereClause = whereClause + " HE." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]
                                + " LIKE '%" + str.trim() + "%' ";
                    } else {
                        whereClause = whereClause + str.trim();
                    }
                }
                whereClause = whereClause + ")";
            }
        }
        if (fullName != null && fullName.length() > 0) {
//            whereClauseEmpTime = whereClauseEmpTime + " AND "+ " emp."+PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]
//                                 + " LIKE '%"+fullName.trim()+"%'";
            Vector vectName = logicParser(fullName);
            whereClause = whereClause + " AND ";
            if (vectName != null && vectName.size() > 0) {
                //whereClause = whereClause + " AND (";

                whereClause = whereClause + " (";
                for (int i = 0; i < vectName.size(); i++) {
                    String str = (String) vectName.get(i);
                    if (!LogicParser.isInSign(str) && !LogicParser.isInLogEnglish(str)) {
                        whereClause = whereClause + " HE." + PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]
                                + " LIKE '%" + str.trim() + "%' ";
                    } else {
                        whereClause = whereClause + str.trim();
                    }
                }
                whereClause = whereClause + ")";
            }
        }
        String getListEmployeeId = PstEmployee.getSEmployeeId(0, 0, whereClause, PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]);
        Vector listEmployee = SessPresence.employeeAttendanceWithResignStatus(oidCompany, oidDivision, oidDepartment, oidSection, empNum, fullName, selectedDateFrom, selectedDateTo, statusResign,payrollGroupId, empCategoryId);
    
%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>HARISMA - Working Hour</title>
        <%@ include file = "../../main/konfigurasi_jquery.jsp" %>    
        <link rel="stylesheet" href="../../styles/main.css" type="text/css">
        <link rel="stylesheet" href="../../styles/tab.css" type="text/css">
        <script type="text/javascript" src="../../javascripts/jquery.min.js"></script>
        <script type="text/javascript" src="../../javascripts/jquery-ui.min.js"></script>
        <script type="text/javascript" src="../../javascripts/gridviewScroll.min.js"></script>
        <link href="../../stylesheets/GridviewScroll.css" rel="stylesheet" />
        <script language="JavaScript">

            function fnTrapKD(){ 
                if (event.keyCode == 13) {
                    //document.all.aSearch.focus();
                    cmdView();
                }
            }

            function cmdUpdateDiv(){
                document.frpresence.command.value="<%=String.valueOf(Command.GOTO)%>";
                document.frpresence.action="summary_hours_monthly.jsp";
                document.frpresence.submit();
            }
            function cmdUpdateDep(){
                document.frpresence.command.value="<%=String.valueOf(Command.GOTO)%>";
                document.frpresence.action="summary_hours_monthly.jsp";
                document.frpresence.submit();
            }
            function cmdUpdatePos(){
                document.frpresence.command.value="<%=String.valueOf(Command.GOTO)%>";
                document.frpresence.action="summary_hours_monthly.jsp";
                document.frpresence.submit();
            }
            function cmdExport(){ 
                //var linkPage = "<//%=printroot%>.report.attendance.AttendanceSummaryXls";
                document.frpresence.action="export_excel/export_excel_summary_hours_monthly.jsp"; 
                document.frpresence.submit();
                
            }
            function cmdSearch(){
                document.frpresence.command.value="<%=String.valueOf(Command.LIST)%>";
                document.frpresence.action="summary_hours_monthly.jsp";
                document.frpresence.submit();
                
            }
            </script>
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
        <link rel="stylesheet" href="../../styles/main.css" type="text/css">
       <link rel="stylesheet" href="../../styles/tab.css" type="text/css">
        <link href="<%=approot%>/stylesheets/superTables.css" rel="Stylesheet" type="text/css" /> 
    </head>
    <body <%=noBack%> bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" onLoad="MM_preloadImages('<%=approot%>/images/BtnSearchOn.jpg')">
        <%//if(isMSIE){%>
        <table width="100%" border="0" cellspacing="0" cellpadding="0" height="100%" bgcolor="#F9FCFF" >
            <%if (headerStyle && !verTemplate.equalsIgnoreCase("0")) {%> 
            <%@include file="../../styletemplate/template_header.jsp" %>
            <%} else {%>
          
            <%//}else{%>
           
            <%//}%>
            <tr> 
                <td ID="TOPTITLE" background="<%=approot%>/images/HRIS_HeaderBg3.jpg" width="100%" height="54">    
                    <%@ include file = "../../main/header.jsp" %>
                </td>
            </tr>
            <tr> 
                <td  bgcolor="#9BC1FF" height="15" ID="MAINMENU" valign="middle"> <!-- #BeginEditable "menumain" --> 
                    <%@ include file = "../../main/mnmain.jsp" %>
                </td>
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
                                        <td height="20"> <font color="#FF6600" face="Arial"><strong>  Report &gt; Summary 
                                                &gt; Hours Monthly  </strong></font> </td>
                                    </tr>
                                    <tr> 
                                        <td> 
                                            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                                                <tr> 
                                                    <td  style="background-color:<%=bgColorContent%>; "> 
                                                        <table width="100%" border="0"  >
                                                            <tr> 
                                                                <td valign="top"> 
                                                                    <table style="border:1px solid <%=garisContent%>" width="100%" border="0" cellspacing="1" cellpadding="1" class="tablecolor">
                                                                        <tr> 
                                                                            <td valign="top"> 
                                                                                <form name="frpresence" method="post" action="">
                                                                                    <input type="hidden" name="hidden_empschedule_id" value="<%=iCommand%>">
                                                                                   
                                                                                    <input type="hidden" name="command" value="">
                                                                                    <input type="hidden" name="source" value="">
                                                                                    <input type="hidden" name="start" value="<%=String.valueOf(start)%>">
                                                                                    <table width="100%" border="0" cellspacing="2" cellpadding="2">
                                                                                       
                                                                                        <tr>
                                                                                            <td width="6%" nowrap="nowrap"> <div align="left">Payrol Num </div></td>
                                                                                            <td width="30%" nowrap="nowrap">:
                                                                                                <input type="text" size="40" name="emp_number"  value="<%= sessEmpSchedule != null && sessEmpSchedule.getEmpNum() != null ? sessEmpSchedule.getEmpNum() : empNum%>" title="You can Input Payroll Number more than one, ex-sample : 1111,2222" class="elemenForm" onKeyDown="javascript:fnTrapKD()"> </td>

                                                                                            <td width="5%" nowrap="nowrap"> Full Name </td>
                                                                                            <td width="59%" nowrap="nowrap">:
                                                                                                <input type="text" size="50" name="full_name"  value="<%= sessEmpSchedule != null && sessEmpSchedule.getEmpFullName() != null ? sessEmpSchedule.getEmpFullName() : fullName%>" title="You can Input Full Name more than one, ex-sample : saya,kamu" class="elemenForm" onKeyDown="javascript:fnTrapKD()"> </td>
                                                                                        </tr>
                                                                                        <tr>
                                                                                            <td width="6%" nowrap="nowrap"> <div align="left">Company </div></td>
                                                                                            <td width="30%" nowrap="nowrap">:

                                                                                                <% /* update by satrya 2014-01-20 Vector comp_value = new Vector(1, 1);
                                                                                                     Vector comp_key = new Vector(1, 1);
                                                                                                     String whereCompany="";
                                                                                                     if (!(isHRDLogin || isEdpLogin || isGeneralManager || isDirector)){
                                                                                                     whereCompany = PstCompany.fieldNames[PstCompany.FLD_COMPANY_ID]+"='"+ emplx.getCompanyId()+"'";
                                                                                                     } else{
                                                                                                     comp_value.add("0");
                                                                                                     comp_key.add("select ...");                                            
                                                                                                     }
                                                                                                     Vector listComp = PstCompany.list(0, 0, whereCompany, " COMPANY ");
                                                                                                     for (int i = 0; i < listComp.size(); i++) {
                                                                                                     Company comp = (Company) listComp.get(i);
                                                                                                     comp_key.add(comp.getCompany());
                                                                                                     comp_value.add(String.valueOf(comp.getOID()));
                                                                                                     }
                                                                                                     //update by satrya 2013-08-13
                                                                                                     //jika user memilih select kembali
                                                                                                     if(oidCompany==0){
                                                                                                     oidDivision =0;oidDepartment=0;oidSection=0; 
                                                                                                     }*/
                                                                                                %> <%/*= update by satrya 2014-01-20 ControlCombo.draw("company_id", "formElemen", null, "" + oidCompany, comp_value, comp_key, "onChange=\"javascript:cmdUpdateDiv()\"")*/%>  
                                                                                                <%
                                                                                                    Vector comp_value = new Vector(1, 1);
                                                                                                    Vector comp_key = new Vector(1, 1);
                                                                                                    String whereComp = "";
                                                                                                    /*if(srcOvertime!=null && srcOvertime.getCompanyId()!=0){
                                                                                                     whereComp = PstCompany.fieldNames[PstCompany.FLD_COMPANY_ID] +"="+srcOvertime.getCompanyId();
                                                                                                     }*/
                                                                                                    Vector div_value = new Vector(1, 1);
                                                                                                    Vector div_key = new Vector(1, 1);

                                                                                                    Vector dept_value = new Vector(1, 1);
                                                                                                    Vector dept_key = new Vector(1, 1);
                                                                                                    if (processDependOnUserDept) {
                                                                                                        if (emplx.getOID() > 0) {
                                                                                                            if (isHRDLogin || isEdpLogin || isGeneralManager || isDirector) {
                                                                                                                //keyList = PstDepartment.genDepIDandNameWithCompanyDiv(0, 1000, "", true);
                                                                                                                comp_value.add("0");
                                                                                                                comp_key.add("select ...");

                                                                                                                div_value.add("0");
                                                                                                                div_key.add("select ...");

                                                                                                                dept_value.add("0");
                                                                                                                dept_key.add("select ...");
                                                                                                            } else {
                                                                                                                Position position = null;
                                                                                                                try {
                                                                                                                    position = PstPosition.fetchExc(emplx.getPositionId());
                                                                                                                } catch (Exception exc) {
                                                                                                                }
                                                                                                                if (position != null & position.getDisabedAppDivisionScope() == 0 & position.getPositionLevel() >= PstPosition.LEVEL_MANAGER) {
                                                                                                                    String whereDiv = " d." + PstDepartment.fieldNames[PstDepartment.FLD_DIVISION_ID] + "=" + emplx.getDivisionId() + "";
                                                                                                                    //keyList = PstDepartment.genDepIDandNameWithCompanyDiv(0, 1000, whereDiv, true);
                                                                                                                    // comp_value.add("0");
                                                                                                                    // comp_key.add("select ...");

                                                                                                                    //div_value.add("0");
                                                                                                                    //div_key.add("select ...");

                                                                                                                    dept_value.add("0");
                                                                                                                    dept_key.add("select ...");

                                                                                                                    whereComp = whereComp != null && whereComp.length() > 0 ? whereComp + " AND (" + whereDiv + ")" : whereDiv;

                                                                                                                } else {

                                                                                                                    String whereClsDep = "(" + PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT_ID] + " = " + departmentOid
                                                                                                                            + ") OR (" + PstDepartment.fieldNames[PstDepartment.FLD_JOIN_TO_DEPARTMENT_ID] + " = " + departmentOid + ") ";
                                                                                                                    try {
                                                                                                                        String joinDept = PstSystemProperty.getValueByName("JOIN_DEPARMENT");
                                                                                                                        Vector depGroup = com.dimata.util.StringParser.parseGroup(joinDept);

                                                                                                                        int grpIdx = -1;
                                                                                                                        int maxGrp = depGroup == null ? 0 : depGroup.size();
                                                                                                                        int countIdx = 0;
                                                                                                                        int MAX_LOOP = 10;
                                                                                                                        int curr_loop = 0;
                                                                                                                        do { // find group department belonging to curretn user base in departmentOid
                                                                                                                            curr_loop++;
                                                                                                                            String[] grp = (String[]) depGroup.get(countIdx);
                                                                                                                            for (int g = 0; g < grp.length; g++) {
                                                                                                                                String comp = grp[g];
                                                                                                                                if (comp.trim().compareToIgnoreCase("" + departmentOid) == 0) {
                                                                                                                                    grpIdx = countIdx;   // A ha .. found here 
                                                                                                                                }
                                                                                                                            }
                                                                                                                            countIdx++;
                                                                                                                        } while ((grpIdx < 0) && (countIdx < maxGrp) && (curr_loop < MAX_LOOP)); // if found then exit

                                                                                                                        // compose where clause
                                                                                                                        if (grpIdx >= 0) {
                                                                                                                            String[] grp = (String[]) depGroup.get(grpIdx);
                                                                                                                            for (int g = 0; g < grp.length; g++) {
                                                                                                                                String comp = grp[g];
                                                                                                                                whereClsDep = whereClsDep + " OR (DEPARTMENT_ID = " + comp + ")";
                                                                                                                            }
                                                                                                                        }
                                                                                                                    } catch (Exception exc) {
                                                                                                                        System.out.println(" Parsing Join Dept" + exc);

                                                                                                                    }
                                                                                                                    //keyList = PstDepartment.genDepIDandNameWithCompanyDiv(0, 1000, whereClsDep, false);

                                                                                                                    whereComp = whereComp != null && whereComp.length() > 0 ? whereComp + " AND (" + whereClsDep + ")" : whereClsDep;

                                                                                                                }
                                                                                                            }
                                                                                                        }
                                                                                                    } else {
                                                                                                        comp_value.add("0");
                                                                                                        comp_key.add("select ...");

                                                                                                        div_value.add("0");
                                                                                                        div_key.add("select ...");

                                                                                                        dept_value.add("0");
                                                                                                        dept_key.add("select ...");
                                                                                                    }
                                                                                                    Vector listCostDept = PstDepartment.listWithCompanyDiv(0, 0, whereComp);
                                                                                                    String prevCompany = "";
                                                                                                    String prevDivision = "";



                                                                                                    long prevCompanyTmp = 0;
                                                                                                    for (int i = 0; i < listCostDept.size(); i++) {
                                                                                                        Department dept = (Department) listCostDept.get(i);
                                                                                                        if (prevCompany.equals(dept.getCompany())) {
                                                                                                            if (prevDivision.equals(dept.getDivision())) {
                                                                                                                //if(srcOvertime!=null && srcOvertime.getCompanyId()!=0){
                                                                                                                dept_key.add(dept.getDepartment());
                                                                                                                dept_value.add(String.valueOf(dept.getOID()));
                                                                                                                //}
                                                                                                            } else {
                                                                                                                div_key.add(dept.getDivision());
                                                                                                                div_value.add("" + dept.getDivisionId());
                                                                                                                if (dept_key != null && dept_key.size() == 0) {
                                                                                                                    dept_key.add(dept.getDepartment());
                                                                                                                    dept_value.add(String.valueOf(dept.getOID()));
                                                                                                                }
                                                                                                                prevDivision = dept.getDivision();
                                                                                                            }
                                                                                                        } else {
                                                                                                            String chkAdaDiv = "";
                                                                                                            if (div_key != null && div_key.size() > 0) {
                                                                                                                chkAdaDiv = (String) div_key.get(0);
                                                                                                            }
                                                                                                            if ((div_key != null && div_key.size() == 0) || (chkAdaDiv.equalsIgnoreCase("select ..."))) {
                                                                                                                if (prevCompanyTmp != dept.getCompanyId()) {
                                                                                                                    comp_key.add(dept.getCompany());
                                                                                                                    comp_value.add("" + dept.getCompanyId());
                                                                                                                    prevCompanyTmp = dept.getCompanyId();
                                                                                                                }
                                                                                                                //untuk karyawan admin yg hanya bisa akses departement tertentu (ketika di awal)
                                                                                                                ////update
                                                                                                                if (processDependOnUserDept) {
                                                                                                                    if (emplx.getOID() > 0) {
                                                                                                                        if (isHRDLogin || isEdpLogin || isGeneralManager || isDirector) {
                                                                                                                            if (oidCompany != 0) {
                                                                                                                                div_key.add(dept.getDivision());
                                                                                                                                div_value.add("" + dept.getDivisionId());

                                                                                                                                dept_key.add(dept.getDepartment());
                                                                                                                                dept_value.add(String.valueOf(dept.getOID()));
                                                                                                                                prevCompany = dept.getCompany();
                                                                                                                                prevDivision = dept.getDivision();
                                                                                                                            }
                                                                                                                        } else {
                                                                                                                            Position position = null;
                                                                                                                            try {
                                                                                                                                position = PstPosition.fetchExc(emplx.getPositionId());
                                                                                                                            } catch (Exception exc) {
                                                                                                                            }
                                                                                                                            if (position != null & position.getDisabedAppDivisionScope() == 0 & position.getPositionLevel() >= PstPosition.LEVEL_MANAGER) {
                                                                                                                                if (oidCompany != 0) {
                                                                                                                                    div_key.add(dept.getDivision());
                                                                                                                                    div_value.add("" + dept.getDivisionId());

                                                                                                                                    dept_key.add(dept.getDepartment());
                                                                                                                                    dept_value.add(String.valueOf(dept.getOID()));
                                                                                                                                    prevCompany = dept.getCompany();
                                                                                                                                    prevDivision = dept.getDivision();
                                                                                                                                } //update by satrya 2013-09-19
                                                                                                                                else if ((div_key != null && div_key.size() == 0) || (chkAdaDiv.equalsIgnoreCase("select ..."))) {
                                                                                                                                    div_key.add(dept.getDivision());
                                                                                                                                    div_value.add("" + dept.getDivisionId());

                                                                                                                                    //update by satrya 2013-09-19
                                                                                                                                    dept_key.add(dept.getDepartment());
                                                                                                                                    dept_value.add(String.valueOf(dept.getOID()));

                                                                                                                                    prevCompany = dept.getCompany();
                                                                                                                                    prevDivision = dept.getDivision();
                                                                                                                                }

                                                                                                                            } else {

                                                                                                                                div_key.add(dept.getDivision());
                                                                                                                                div_value.add("" + dept.getDivisionId());

                                                                                                                                dept_key.add(dept.getDepartment());
                                                                                                                                dept_value.add(String.valueOf(dept.getOID()));
                                                                                                                                prevCompany = dept.getCompany();
                                                                                                                                prevDivision = dept.getDivision();
                                                                                                                            }
                                                                                                                        }
                                                                                                                    }
                                                                                                                } else {
                                                                                                                    if (oidCompany != 0) {
                                                                                                                        div_key.add(dept.getDivision());
                                                                                                                        div_value.add("" + dept.getDivisionId());

                                                                                                                        dept_key.add(dept.getDepartment());
                                                                                                                        dept_value.add(String.valueOf(dept.getOID()));
                                                                                                                        prevCompany = dept.getCompany();
                                                                                                                        prevDivision = dept.getDivision();
                                                                                                                    }
                                                                                                                }

                                                                                                            } else {
                                                                                                                if (prevCompanyTmp != dept.getCompanyId()) {
                                                                                                                    comp_key.add(dept.getCompany());
                                                                                                                    comp_value.add("" + dept.getCompanyId());
                                                                                                                    prevCompanyTmp = dept.getCompanyId();
                                                                                                                }

                                                                                                            }

                                                                                                        }
                                                                                                    }
                                                                                                %>
                                                                                                <%= ControlCombo.draw("company_id", "formElemen", null, "" + oidCompany, comp_value, comp_key, "onChange=\"javascript:cmdUpdateDiv()\"")%>
                                                                                            </td>

                                                                                            <td width="5%" nowrap="nowrap"><%=dictionaryD.getWord(I_Dictionary.DIVISION) %></td>
                                                                                            <td width="59%" nowrap="nowrap">:
                                                                                                <%/*  // update by satrya 2014-01-20 
                                                                                                     Vector div_value = new Vector(1, 1);
                                                                                                     Vector div_key = new Vector(1, 1);
                                                                                                     String whereDivision ="";
                                                                                                     if (!(isHRDLogin || isEdpLogin || isGeneralManager || isDirector)){
                                                                                                     whereDivision = PstDivision.fieldNames[PstDivision.FLD_DIVISION_ID]+"='"+ emplx.getDivisionId()+"'";
                                                                                                     oidDivision = emplx.getDivisionId();
                                                                                                     } else{
                                                                                                     div_value.add("0");
                                                                                                     div_key.add("select ...");                                            
                                                                                                     }
                                                                                                     Vector listDiv = PstDivision.list(0, 0, whereDivision, " DIVISION ");
                                                                                                     for (int i = 0; i < listDiv.size(); i++) {
                                                                                                     Division div = (Division) listDiv.get(i);
                                                                                                     div_key.add(div.getDivision());
                                                                                                     div_value.add(String.valueOf(div.getOID()));
                                                                                                     }
                                                                                                     //update by satrya 2013-08-13
                                                                                                     //jika user memilih select kembali
                                                                                                     if(oidDivision==0){
                                                                                                     oidDepartment=0;
                                                                                                     }*/
                                                                                                %> <%/*= update by satrya 2014-01-20 ControlCombo.draw("division_id", "formElemen", null, "" + oidDivision, div_value, div_key, "onChange=\"javascript:cmdUpdateDep()\"")*/%>
                                                                                                <%

                                                                                                    //update by satrya 2013-08-13
                                                                                                    //jika user memilih select kembali
                                                                                                    if (oidCompany == 0) {
                                                                                                        oidDivision = 0;
                                                                                                    }

                                                                                                    if (oidCompany != 0) {
                                                                                                        whereComp = "(" + (whereComp != null && whereComp.length() == 0 ? "1=1" : whereComp) + ") AND " + PstCompany.fieldNames[PstCompany.FLD_COMPANY_ID] + "=" + oidCompany;
                                                                                                        listCostDept = PstDepartment.listWithCompanyDiv(0, 0, whereComp);
                                                                                                        prevCompany = "";
                                                                                                        prevDivision = "";

                                                                                                        div_value = new Vector(1, 1);
                                                                                                        div_key = new Vector(1, 1);

                                                                                                        dept_value = new Vector(1, 1);
                                                                                                        dept_key = new Vector(1, 1);

                                                                                                        prevCompanyTmp = 0;
                                                                                                        long tmpFirstDiv = 0;

                                                                                                        if (processDependOnUserDept) {
                                                                                                            if (emplx.getOID() > 0) {
                                                                                                                if (isHRDLogin || isEdpLogin || isGeneralManager || isDirector) {
                                                                                                                    //keyList = PstDepartment.genDepIDandNameWithCompanyDiv(0, 1000, "", true);
                                                                                                                    comp_value.add("0");
                                                                                                                    comp_key.add("select ...");

                                                                                                                    div_value.add("0");
                                                                                                                    div_key.add("select ...");

                                                                                                                    dept_value.add("0");
                                                                                                                    dept_key.add("select ...");
                                                                                                                } else {
                                                                                                                    Position position = null;
                                                                                                                    try {
                                                                                                                        position = PstPosition.fetchExc(emplx.getPositionId());
                                                                                                                    } catch (Exception exc) {
                                                                                                                    }
                                                                                                                    if (position != null & position.getDisabedAppDivisionScope() == 0 & position.getPositionLevel() >= PstPosition.LEVEL_MANAGER) {
                                                                                                                        //div_value.add("0");
                                                                                                                        //div_key.add("select ...");

                                                                                                                        dept_value.add("0");
                                                                                                                        dept_key.add("select ...");

                                                                                                                    }
                                                                                                                }
                                                                                                            }
                                                                                                        } else {
                                                                                                            comp_value.add("0");
                                                                                                            comp_key.add("select ...");

                                                                                                            div_value.add("0");
                                                                                                            div_key.add("select ...");

                                                                                                            dept_value.add("0");
                                                                                                            dept_key.add("select ...");
                                                                                                        }
                                                                                                        long prevDivTmp = 0;
                                                                                                        for (int i = 0; i < listCostDept.size(); i++) {
                                                                                                            Department dept = (Department) listCostDept.get(i);
                                                                                                            if (prevCompany.equals(dept.getCompany())) {
                                                                                                                if (prevDivision.equals(dept.getDivision())) {
                                                                                                                    //update
                                                                                                                    if (oidDivision != 0) {
                                                                                                                        dept_key.add(dept.getDepartment());
                                                                                                                        dept_value.add(String.valueOf(dept.getOID()));
                                                                                                                    }
                                                                                                                    //lama
                        /*
                                                                                                                     dept_key.add(dept.getDepartment());
                                                                                                                     dept_value.add(String.valueOf(dept.getOID()));
                                                                                                                     */

                                                                                                                } else {
                                                                                                                    div_key.add(dept.getDivision());
                                                                                                                    div_value.add("" + dept.getDivisionId());
                                                                                                                    if (dept_key != null && dept_key.size() == 0) {
                                                                                                                        dept_key.add(dept.getDepartment());
                                                                                                                        dept_value.add(String.valueOf(dept.getOID()));
                                                                                                                    }
                                                                                                                    prevDivision = dept.getDivision();
                                                                                                                }
                                                                                                            } else {
                                                                                                                String chkAdaDiv = "";
                                                                                                                if (div_key != null && div_key.size() > 0) {
                                                                                                                    chkAdaDiv = (String) div_key.get(0);
                                                                                                                }
                                                                                                                if ((div_key != null && div_key.size() == 0) || (chkAdaDiv.equalsIgnoreCase("select ..."))) {
                                                                                                                    //comp_key.add(dept.getCompany());
                                                                                                                    //comp_value.add(""+dept.getCompanyId());



                                                                                                                    if (prevDivTmp != dept.getDivisionId()) {
                                                                                                                        div_key.add(dept.getDivision());
                                                                                                                        div_value.add("" + dept.getDivisionId());
                                                                                                                        prevDivTmp = dept.getDivisionId();
                                                                                                                    }

                                                                                                                    tmpFirstDiv = dept.getDivisionId();

                                                                                                                    // dept_key.add(dept.getDepartment());
                                                                                                                    //   dept_value.add(String.valueOf(dept.getOID()));           

                                                                                                                    prevCompany = dept.getCompany();
                                                                                                                    prevDivision = dept.getDivision();
                                                                                                                }
                                                                                                                /*else{
                                                                                                                 if(prevCompanyTmp!=dept.getCompanyId()){
                                                                                                                 comp_key.add(dept.getCompany());
                                                                                                                 comp_value.add(""+dept.getCompanyId());
                                                                                                                 prevCompanyTmp=dept.getCompanyId();
                                                                                                                 }
              
                                                                                                                 }*/
                                                                                                                String chkAdaDpt = "";
                                                                                                                if (whereComp != null && whereComp.length() > 0) {
                                                                                                                    chkAdaDpt = "(" + (whereComp != null && whereComp.length() == 0 ? "1=1" : whereComp) + ") AND d." + PstDivision.fieldNames[PstDivision.FLD_DIVISION_ID] + "=" + oidDivision;
                                                                                                                }
                                                                                                                Vector listCheckAdaDept = PstDepartment.listWithCompanyDiv(0, 0, chkAdaDpt);
                                                                                                                if ((listCheckAdaDept == null || listCheckAdaDept.size() == 0)) {

                                                                                                                    if (processDependOnUserDept) {
                                                                                                                        if (emplx.getOID() > 0) {
                                                                                                                            if (isHRDLogin || isEdpLogin || isGeneralManager || isDirector) {
                                                                                                                            } else {
                                                                                                                                Position position = null;
                                                                                                                                try {
                                                                                                                                    position = PstPosition.fetchExc(emplx.getPositionId());
                                                                                                                                } catch (Exception exc) {
                                                                                                                                }

                                                                                                                                oidDivision = tmpFirstDiv;

                                                                                                                            }
                                                                                                                        }
                                                                                                                    } else {
                                                                                                                        oidDivision = tmpFirstDiv;

                                                                                                                    }

                                                                                                                }
                                                                                                            }
                                                                                                        }
                                                                                                    }
                                                                                                %>
                                                                                                <%= ControlCombo.draw("division_id", "formElemen", null, "" + oidDivision, div_value, div_key, "onChange=\"javascript:cmdUpdateDep()\"")%> 
                                                                                            </td>
                                                                                        </tr>
                                                                                        <tr> 
                                                                                            <td width="6%" align="right" nowrap> 
                                                                                                <div align="left"><%=dictionaryD.getWord(I_Dictionary.DEPARTMENT) %></div>                                                                                            </td>
                                                                                            <td width="30%" nowrap="nowrap"> : 
                                                                                                <%

                                                                                                    /*  Vector dept_value = new Vector(1, 1);
                                                                                                     Vector dept_key = new Vector(1, 1);
                                                                                                     //Vector listDept = new Vector(1, 1);
                                                                                                     DepartmentIDnNameList keyList = new DepartmentIDnNameList();

                                                                                                     if (processDependOnUserDept) {
                                                                                                     if (emplx.getOID() > 0) {
                                                                                                     if (isHRDLogin || isEdpLogin || isGeneralManager) {
                                                                                                     keyList = PstDepartment.genDepIDandNameWithCompanyDiv(0, 1000, "", true);
                                                                                                     //listDept = PstDepartment.list(0, 0, "", "DEPARTMENT");
                                                                                                     } else {
                                                                                                     Position position = null;
                                                                                                     try {
                                                                                                     position = PstPosition.fetchExc(emplx.getPositionId());
                                                                                                     } catch (Exception exc) {
                                                                                                     }
                                                                                                     if (position != null & position.getDisabedAppDivisionScope() == 0 & position.getPositionLevel() >= PstPosition.LEVEL_MANAGER) {
                                                                                                     String whereDiv = " d." + PstDepartment.fieldNames[PstDepartment.FLD_DIVISION_ID] + "=" + emplx.getDivisionId() + "";
                                                                                                     keyList = PstDepartment.genDepIDandNameWithCompanyDiv(0, 1000, whereDiv, true);
                                                                                                     } else {

                                                                                                     String whereClsDep = "(" + PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT_ID] + " = " + departmentOid
                                                                                                     + ") OR (" + PstDepartment.fieldNames[PstDepartment.FLD_JOIN_TO_DEPARTMENT_ID] + " = " + departmentOid + ") ";
                                                                                                     try {
                                                                                                     String joinDept = PstSystemProperty.getValueByName("JOIN_DEPARMENT");
                                                                                                     Vector depGroup = com.dimata.util.StringParser.parseGroup(joinDept);

                                                                                                     int grpIdx = -1;
                                                                                                     int maxGrp = depGroup == null ? 0 : depGroup.size();
                                                                                                     int countIdx = 0;
                                                                                                     int MAX_LOOP = 10;
                                                                                                     int curr_loop = 0;
                                                                                                     do { // find group department belonging to curretn user base in departmentOid
                                                                                                     curr_loop++;
                                                                                                     String[] grp = (String[]) depGroup.get(countIdx);
                                                                                                     for (int g = 0; g < grp.length; g++) {
                                                                                                     String comp = grp[g];
                                                                                                     if (comp.trim().compareToIgnoreCase("" + departmentOid) == 0) {
                                                                                                     grpIdx = countIdx;   // A ha .. found here 
                                                                                                     }
                                                                                                     }
                                                                                                     countIdx++;
                                                                                                     } while ((grpIdx < 0) && (countIdx < maxGrp) && (curr_loop < MAX_LOOP)); // if found then exit

                                                                                                     // compose where clause
                                                                                                     if (grpIdx >= 0) {
                                                                                                     String[] grp = (String[]) depGroup.get(grpIdx);
                                                                                                     for (int g = 0; g < grp.length; g++) {
                                                                                                     String comp = grp[g];
                                                                                                     whereClsDep = whereClsDep + " OR (DEPARTMENT_ID = " + comp + ")";
                                                                                                     }
                                                                                                     }
                                                                                                     } catch (Exception exc) {
                                                                                                     System.out.println(" Parsing Join Dept" + exc);
                                                                                                                         
                                                                                                     }
                                                                                                     keyList = PstDepartment.genDepIDandNameWithCompanyDiv(0, 1000, whereClsDep, false);
                                                                                                     //listDept = PstDepartment.list(0, 0,whereClsDep, "");
                                                                                                     }
                                                                                                     }
                                                                                                     } else {
                                                                                                     //dept_value.add("0");
                                                                                                     //dept_key.add("select ...");
                                                                                                     keyList = PstDepartment.genDepIDandNameWithCompanyDiv(0, 1000, "", true);
                                                                                                     //listDept = PstDepartment.list(0, 0, "", "DEPARTMENT");
                                                                                                     }
                                                                                                     } else {
                                                                                                     //dept_value.add("0");
                                                                                                     //dept_key.add("select ...");
                                                                                                     keyList = PstDepartment.genDepIDandNameWithCompanyDiv(0, 1000, "", true);
                                                                                                     //listDept = PstDepartment.list(0, 0, "", "DEPARTMENT");
                                                                                                     }
                                                                                                     dept_value = keyList.getDepIDs();
                                                                                                     dept_key = keyList.getDepNames();

                                                                                                     /*for (int i = 0; i < listDept.size(); i++) {
                                                                                                     Department dept = (Department) listDept.get(i);
                                                                                                     dept_key.add(dept.getDepartment());
                                                                                                     dept_value.add(String.valueOf(dept.getOID()));
                                                                                                     } */


                                                                                                    /*   String selectValueDepartment = "" + oidDepartment;//+objSrcLeaveApp.getDepartmentId();

                                                                                                     */%>
                                                                                                <%//=ControlCombo.draw("department", "elementForm", null, selectValueDepartment, dept_value, dept_key, " onkeydown=\"javascript:fnTrapKD()\"")%>

                                                                                                <%/*=ControlCombo.draw("department", "elementForm", null, selectValueDepartment, dept_value, dept_key, " onChange=\"javascript:cmdUpdateDep()\"")*/%>
                                                                                                <%
                                                                                                    dept_value = new Vector(1, 1);
                                                                                                    dept_key = new Vector(1, 1);
                                                                                                    //dept_value.add("0");
                                                                                                    //dept_key.add("select ...");
                                                                                                    //String strWhere = PstDepartment.fieldNames[PstDepartment.FLD_DIVISION_ID] + "=" + oidDivision;
                                                                                                    Vector listDept = new Vector(); //PstDepartment.list(0, 0, strWhere, PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT]);
                                            /*for (int i = 0; i < listDept.size(); i++) {
                                                                                                     Department dept = (Department) listDept.get(i);
                                                                                                     dept_key.add(dept.getDepartment());
                                                                                                     dept_value.add(String.valueOf(dept.getOID()));
                                                                                                     }*/

                                                                                                    if (processDependOnUserDept) {
                                                                                                        if (emplx.getOID() > 0) {

                                                                                                            if (isHRDLogin || isEdpLogin || isGeneralManager || isDirector) {
                                                                                                                String strWhere = PstDepartment.TBL_HR_DEPARTMENT + "." + PstDepartment.fieldNames[PstDepartment.FLD_DIVISION_ID] + "=" + oidDivision;
                                                                                                                dept_value.add("0");
                                                                                                                dept_key.add("select ...");
                                                                                                                listDept = PstDepartment.list(0, 0, strWhere, "DEPARTMENT");

                                                                                                            } else {
                                                                                                                Position position = new Position();
                                                                                                                try {
                                                                                                                    position = PstPosition.fetchExc(emplx.getPositionId());
                                                                                                                } catch (Exception exc) {
                                                                                                                }

                                                                                                                String whereClsDep = "(((hr_department.DEPARTMENT_ID = " + departmentOid + ") "
                                                                                                                        + "AND hr_department." + PstDepartment.fieldNames[PstDepartment.FLD_DIVISION_ID] + "=" + oidDivision + ") OR "
                                                                                                                        + "(hr_department." + PstDepartment.fieldNames[PstDepartment.FLD_JOIN_TO_DEPARTMENT_ID] + "=" + departmentOid + "))";

                                                                                                                if (position.getOID() != 0 && position.getDisabedAppDivisionScope() == 0) {
                                                                                                                    whereClsDep = " ( hr_department." + PstDepartment.fieldNames[PstDepartment.FLD_DIVISION_ID] + "=" + oidDivision + ") ";
                                                                                                                }

                                                                                                                Vector SectionList = new Vector();
                                                                                                                try {
                                                                                                                    String joinDept = PstSystemProperty.getValueByName("JOIN_DEPARMENT");
                                                                                                                    Vector depGroup = com.dimata.util.StringParser.parseGroup(joinDept);

                                                                                                                    String joinDeptSection = PstSystemProperty.getValueByName("JOIN_DEPARTMENT_SECTION");
                                                                                                                    Vector depSecGroup = com.dimata.util.StringParser.parseGroup(joinDeptSection);

                                                                                                                    int grpIdx = -1;
                                                                                                                    int maxGrp = depGroup == null ? 0 : depGroup.size();

                                                                                                                    int grpSecIdx = -1;
                                                                                                                    int maxGrpSec = depSecGroup == null ? 0 : depSecGroup.size();

                                                                                                                    int countIdx = 0;
                                                                                                                    int MAX_LOOP = 10;
                                                                                                                    int curr_loop = 0;

                                                                                                                    int countIdxSec = 0;
                                                                                                                    int MAX_LOOPSec = 10;
                                                                                                                    int curr_loopSec = 0;

                                                                                                                    do { // find group department belonging to curretn user base in departmentOid
                                                                                                                        curr_loop++;
                                                                                                                        String[] grp = (String[]) depGroup.get(countIdx);
                                                                                                                        for (int g = 0; g < grp.length; g++) {
                                                                                                                            String comp = grp[g];
                                                                                                                            if (comp.trim().compareToIgnoreCase("" + departmentOid) == 0) {
                                                                                                                                grpIdx = countIdx;   // A ha .. found here                                       
                                                                                                                            }
                                                                                                                        }
                                                                                                                        countIdx++;
                                                                                                                    } while ((grpIdx < 0) && (countIdx < maxGrp) && (curr_loop < MAX_LOOP)); // if found then exit                            

                                                                                                                    Vector idxSecGroup = new Vector();

                                                                                                                    for (int x = 0; x < maxGrpSec; x++) {

                                                                                                                        String[] grp = (String[]) depSecGroup.get(x);
                                                                                                                        for (int j = 0; j < 1; j++) {

                                                                                                                            String comp = grp[j];
                                                                                                                            if (comp.trim().compareToIgnoreCase("" + departmentOid) == 0) {
                                                                                                                                Counter counter = new Counter();
                                                                                                                                counter.setCounter(x);
                                                                                                                                idxSecGroup.add(counter);
                                                                                                                            }
                                                                                                                        }
                                                                                                                    }

                                                                                                                    for (int s = 0; s < idxSecGroup.size(); s++) {

                                                                                                                        Counter counter = (Counter) idxSecGroup.get(s);

                                                                                                                        String[] grp = (String[]) depSecGroup.get(counter.getCounter());

                                                                                                                        Section sec = new Section();
                                                                                                                        sec.setDepartmentId(Long.parseLong(grp[0]));
                                                                                                                        sec.setOID(Long.parseLong(grp[2]));
                                                                                                                        SectionList.add(sec);

                                                                                                                    }

                                                                                                                    // compose where clause
                                                                                                                    if (grpIdx >= 0) {
                                                                                                                        String[] grp = (String[]) depGroup.get(grpIdx);
                                                                                                                        for (int g = 0; g < grp.length; g++) {
                                                                                                                            String comp = grp[g];
                                                                                                                            whereClsDep = whereClsDep + " OR (j.DEPARTMENT_ID = " + comp + ")";
                                                                                                                        }
                                                                                                                    }
                                                                                                                    whereClsDep = " (" + whereClsDep + ") AND hr_department." + PstDepartment.fieldNames[PstDepartment.FLD_DIVISION_ID] + "=" + oidDivision;
                                                                                                                } catch (Exception exc) {
                                                                                                                    System.out.println(" Parsing Join Dept" + exc);
                                                                                                                }

                                                                                                                //dept_value.add("0");
                                                                                                                //dept_key.add("select ...");
                                                                                                                listDept = PstDepartment.list(0, 0, whereClsDep, "");

                                                                                                                for (int idx = 0; idx < SectionList.size(); idx++) {

                                                                                                                    Section sect = (Section) SectionList.get(idx);

                                                                                                                    long sectionOid = 0;

                                                                                                                    for (int z = 0; z < listDept.size(); z++) {

                                                                                                                        Department dep = new Department();

                                                                                                                        dep = (Department) listDept.get(z);

                                                                                                                        if (sect.getDepartmentId() == dep.getOID()) {

                                                                                                                            sectionOid = sect.getOID();

                                                                                                                        }
                                                                                                                    }

                                                                                                                    if (sectionOid != 0) {

                                                                                                                        Section lstSection = new Section();
                                                                                                                        Department lstDepartment = new Department();

                                                                                                                        try {
                                                                                                                            lstSection = PstSection.fetchExc(sectionOid);
                                                                                                                        } catch (Exception e) {
                                                                                                                            System.out.println("Exception " + e.toString());
                                                                                                                        }

                                                                                                                        try {
                                                                                                                            lstDepartment = PstDepartment.fetchExc(lstSection.getDepartmentId());
                                                                                                                        } catch (Exception e) {
                                                                                                                            System.out.println("Exception " + e.toString());
                                                                                                                        }

                                                                                                                        listDept.add(lstDepartment);

                                                                                                                    }
                                                                                                                }
                                                                                                            }
                                                                                                        } else {
                                                                                                            dept_value.add("0");
                                                                                                            dept_key.add("select ...");
                                                                                                            listDept = PstDepartment.list(0, 0, (PstDepartment.fieldNames[PstDepartment.FLD_DIVISION_ID] + "=" + oidDivision), "DEPARTMENT");
                                                                                                        }
                                                                                                    } else {
                                                                                                        dept_value.add("0");
                                                                                                        dept_key.add("select ...");
                                                                                                        listDept = PstDepartment.list(0, 0, (PstDepartment.fieldNames[PstDepartment.FLD_DIVISION_ID] + "=" + oidDivision), "DEPARTMENT");
                                                                                                    }

                                                                                                    for (int i = 0; i < listDept.size(); i++) {
                                                                                                        Department dept = (Department) listDept.get(i);
                                                                                                        dept_key.add(dept.getDepartment());
                                                                                                        dept_value.add(String.valueOf(dept.getOID()));
                                                                                                    }


                                                                                                    //update by satrya 2013-08-13
                                                                                                    //jika user memilih select kembali
                                                                                                    if (oidDepartment == 0) {
                                                                                                        oidSection = 0;
                                                                                                    }
                                                                                                %> <%= ControlCombo.draw("department", "formElemen", null, "" + oidDepartment, dept_value, dept_key, "onChange=\"javascript:cmdUpdatePos()\"")%>
                                                                                            </td>
                                                                                            <td width="5%" align="left" nowrap valign="top"><%=dictionaryD.getWord(I_Dictionary.SECTION) %></td>
                                                                                            <td width="59%" nowrap="nowrap">:
                                                                                                <%
                                                                                                    /*
                                                                                                     Vector sec_value = new Vector(1, 1);
                                                                                                     Vector sec_key = new Vector(1, 1);
                                                                                                     sec_value.add("0");
                                                                                                     sec_key.add("select ...");

                                                                                                     //String sWhereClause = PstSection.fieldNames[PstSection.FLD_DEPARTMENT_ID] + " = " + sSelectedDepartment;                                                       
                                                                                                     //Vector listSec = PstSection.list(0, 0, sWhereClause, " SECTION ");
                                                                                                     String secWhere = PstSection.fieldNames[PstSection.FLD_DEPARTMENT_ID] + "=" + oidDepartment;
                                                                                                     Vector listSec = PstSection.list(0, 0, secWhere, " SECTION ");
                                                                                                     for (int i = 0; i < listSec.size(); i++) {
                                                                                                     Section sec = (Section) listSec.get(i);
                                                                                                     sec_key.add(sec.getSection());
                                                                                                     sec_value.add(String.valueOf(sec.getOID()));
                                                                                                     }*/
                                                                                                %>
                                                                                                <%/*=ControlCombo.draw("section", null, "" + oidSection, sec_value, sec_key)*/%>     
                                                                                                <%

                                                                                                    Vector sec_value = new Vector(1, 1);
                                                                                                    Vector sec_key = new Vector(1, 1);
                                                                                                    sec_value.add("0");
                                                                                                    sec_key.add("select ...");

                                                                                                    //String sWhereClause = PstSection.fieldNames[PstSection.FLD_DEPARTMENT_ID] + " = " + sSelectedDepartment;                                                       
                                                                                                    //Vector listSec = PstSection.list(0, 0, sWhereClause, " SECTION ");
                                                                                                    String secWhere = PstSection.fieldNames[PstSection.FLD_DEPARTMENT_ID] + "=" + oidDepartment;
                                                                                                    Vector listSec = PstSection.list(0, 0, secWhere, " SECTION ");
                                                                                                    for (int i = 0; i < listSec.size(); i++) {
                                                                                                        Section sec = (Section) listSec.get(i);
                                                                                                        sec_key.add(sec.getSection());
                                                                                                        sec_value.add(String.valueOf(sec.getOID()));
                                                                                                    }
                                                                                                %>
                                                                                                <%=ControlCombo.draw("section", null, "" + oidSection, sec_value, sec_key)%>
                                                                                            </td>

                                                                                        </tr>
                                                                                        <tr>
                                                                                            <td width="6%" align="right" nowrap><div align="left">Payroll Group </div></td>
                                                                                            <td width="30%" nowrap="nowrap">:
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
                                                                                            <td width="5%" align="right" nowrap><div align="left">Employee Category </div></td>
                                                                                            <td width="30%" nowrap="nowrap">:
                                                                                                <%   //priska 20150730
                                                                                                    Vector empCat_value = new Vector(1, 1);
                                                                                                    Vector empCat_key = new Vector(1, 1);
                                                                                                    Vector listEmpCategory = PstEmpCategory.list(0, 0, "", "");
                                                                                                    empCat_value.add(""+0);
                                                                                                    empCat_key.add("select");
                                                                                                    for (int i = 0; i < listEmpCategory.size(); i++) {
                                                                                                        EmpCategory empCategory = (EmpCategory) listEmpCategory.get(i);
                                                                                                        empCat_key.add(empCategory.getEmpCategory());
                                                                                                        empCat_value.add(String.valueOf(empCategory.getOID()));
                                                                                                    }

                                                                                                    %>

                                                                                                     <%=ControlCombo.draw("empCategoryId", null, "" + empCategoryId, empCat_value, empCat_key, "")%>
                                                                                            </td>
                                                                                        </tr>
                                                                                        <tr> 
                                                                                            <td width="6%" align="right" nowrap> <div align=left>Date</div>                                                                                             </td>
                                                                                            <td width="30%" nowrap="nowrap">:
                                                                                                
                                                                                                <%
                                                                                                    //String selectDateStart = "" + selectedDateFrom; 
                                                                                                    //String selectDateFinish 
%>
                                                                                                <%=ControlDate.drawDateWithStyle("check_date_start", selectedDateFrom != null ? selectedDateFrom : new Date(), 2, -5, "formElemen", " onkeydown=\"javascript:fnTrapKD()\"")%> 
                                                                                                to <%=ControlDate.drawDateWithStyle("check_date_finish", selectedDateTo != null ? selectedDateTo : new Date(), 2, -5, "formElemen", " onkeydown=\"javascript:fnTrapKD()\"")%>                                                                                            </td>
                                                                                                <% //out.println("date "+date);
                                                                                                    //long periodId = PstPeriod.getPeriodIdBySelectedDate(date); 

                                                                                                %>
                                                                                           
                                                                                            <%

                                                                                                /* Vector status_value = new Vector(1, 1);
                                                                                                 Vector status_key = new Vector(1, 1);
                                                                                                                                                                          
                                                                                                 for (int i = 0; i < PstEmpSchedule.strPresenceStatus.length ; i++) { 
                                                                                                 status_value.add(""+i);
                                                                                                 status_key.add(PstEmpSchedule.strPresenceStatus[i]);
                                                                                                 }*/
                                                                                            %>
                                                                                            <%//=ControlCombo.draw("status_schedule1st","-select-", status1st, status_value, status_key)%>                                                                                            </td>
                                                                                            <%
                                                                                                String chekr = "";
                                                                                                String chekr1 = "";
                                                                                                String chekr2 = "";
                                                                                                if (sStatusResign.equals("0") || sStatusResign.equals("")){
                                                                                                   chekr = " checked ";
                                                                                                } else {
                                                                                                   chekr = ""; 
                                                                                                }

                                                                                                 if (sStatusResign.equals("1")){
                                                                                                   chekr1 = " checked ";
                                                                                                } else {
                                                                                                   chekr1 = ""; 
                                                                                                }

                                                                                                if (sStatusResign.equals("2")){
                                                                                                   chekr2 = " checked ";
                                                                                                } else {
                                                                                                   chekr2 = ""; 
                                                                                                }
                                                                                            %>
                                                                                            <td width="5%" align="right" nowrap><div align="left">Resigned Status </div></td>
                                                                                            <td>:
                                                                                                <input type="radio" name="statusResign" value="0" <%=chekr%>>
                                                                                                No
                                                                                                <input type="radio" name="statusResign" value="1" <%=chekr1%>>
                                                                                                Yes
                                                                                                <input type="radio" name="statusResign" value="2" <%=chekr2%>>
                                                                                                All </td>
                                                                                        </tr>
                                                                                        <tr> 
                                                                                            <td height="13" width="0%">&nbsp;</td>
                                                                                            <td width="39%"><a href="javascript:cmdSearch()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image10','','<%=approot%>/images/BtnSearchOn.jpg',1)" id="aSearch"><img name="Image10" border="0" src="<%=approot%>/images/BtnSearch.jpg" width="24" height="24" alt="Search Employee"></a>
                                                                                           <img src="<%=approot%>/images/spacer.gif" width="6" height="1">
                                                                                          <a href="javascript:cmdSearch()">Search  for Employee</a></td>  
                                                                                        </tr>
                                                                                        <%if(iCommand==Command.LIST){%> 
                                                                                        <tr>
                                                                                            <td colspan="7"><%=drawList(out,selectedDateFrom,selectedDateTo,listEmployee,0,session)%></td>  
                                                                                        </tr>
                                                                                       
                                                                                        <tr> 
                                                                                            
                                                                                            <td width="30%"> 
                                                                                                <table border="0" cellspacing="0" cellpadding="0" width="137">
                                                                                                    <tr> 
                                                                                                        <td width="16"><a href="javascript:cmdExport)" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image10','','<%=approot%>/images/excel.png',1)" id="aSearch"><img name="Image10" border="0" src="<%=approot%>/images/excel.png" width="24" height="24" alt="Export Excel"></a></td>
                                                                                                        <td width="2"><img src="<%=approot%>/images/spacer.gif" width="4" height="1"></td>
                                                                                                        <td width="94" class="command" nowrap><a href="javascript:cmdExport()">Export Excel</a></td>
                                                                                                    </tr>
                                                                                                </table></td>
                                                                                        </tr>
                                                                                         <%}%>
                                                                                    </table>

                                                                                </form>
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
            <%if (headerStyle && !verTemplate.equalsIgnoreCase("0")) {%>
            <tr>
                <td valign="bottom">
                   
                    <%@include file="../../footer.jsp" %>
                </td>

            </tr>
            <%} else {%>
            <tr> 
                <td colspan="2" height="20" <%=bgFooterLama%>>
                    <%@ include file = "../../main/footer.jsp" %>
                   </td>
            </tr>
            <%}%>
        </table>
        <script type="text/javascript">
	    $(document).ready(function () {
	        gridviewScroll();
	    });
            <%
                int freesize=4;
                
            %>
	    function gridviewScroll() {
	        gridView1 = $('#GridView1').gridviewScroll({
                width: 1310,
                height: 500,
                railcolor: "##33AAFF",
                barcolor: "#CDCDCD",
                barhovercolor: "#606060",
                bgcolor: "##33AAFF",
                freezesize: <%=freesize%>,
                arrowsize: 30,
                varrowtopimg: "<%=approot%>/images/arrowvt.png",
                varrowbottomimg: "<%=approot%>/images/arrowvb.png",
                harrowleftimg: "<%=approot%>/images/arrowhl.png",
                harrowrightimg: "<%=approot%>/images/arrowhr.png",
                headerrowcount: 2,
                railsize: 16,
                barsize: 15
            });
	    }
	</script>

    </body>
   
</html>