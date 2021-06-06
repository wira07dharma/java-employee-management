<%-- 
    Document   : export_excel_working_hour_summary
    Created on : Dec 13, 2018, 3:58:50 PM
    Author     : IanRizky
--%>
<%@page import="com.dimata.harisma.entity.masterdata.ScheduleSymbol"%>
<%@page import="com.dimata.harisma.entity.masterdata.PstScheduleCategory"%>
<%@page import="com.dimata.harisma.entity.masterdata.PstScheduleSymbol"%>
<%@page import="com.dimata.harisma.entity.leave.LeaveOidSym"%>
<%@page import="com.dimata.harisma.entity.leave.PstLeaveApplication"%>
<%@page import="com.dimata.system.entity.PstSystemProperty"%>
<%@page import="com.dimata.harisma.session.attendance.PresenceReportDaily"%>
<%@page import="com.dimata.harisma.session.attendance.SessEmpSchedule"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.Calendar"%>
<%@page import="com.dimata.harisma.entity.masterdata.PstPeriod"%>
<%@page import="com.dimata.harisma.entity.masterdata.Period"%>
<%@page import="com.dimata.harisma.entity.attendance.PstEmpSchedule"%>
<%@page import="com.dimata.util.Formater"%>
<%@page import="com.dimata.harisma.entity.employee.Employee"%>
<%@page import="com.dimata.harisma.entity.employee.PstEmployee"%>
<%@page import="java.util.Vector"%>
<%@page import="com.dimata.qdep.form.FRMQueryString"%>
<%!
	public long getWorkingDuration(Date dtActualIn, Date dtActualOut, long breakLong) {
        long iDurationHour = 0;
        Date x = new Date();
        Date dt = new Date(breakLong);
        x = dt;
        if (dtActualIn != null && dtActualOut != null) { 
            long iDurTimeIn = dtActualIn.getTime() / 1000; 
            //update by devin 2014-02-26
            // long iDurTimeOut = (dtActualOut.getTime()- breakLong) / 1000; karena bisa saja nilai breaknya mines
            long iDurTimeOut = (dtActualOut.getTime()- Math.abs(breakLong)) / 1000;
            long iDuration = 0;
            if (iDurTimeIn != iDurTimeOut) {
                iDuration = (iDurTimeIn == 0 || iDurTimeOut == 0) ? 0 : iDurTimeOut - iDurTimeIn;
            }
            iDurationHour = (iDuration - (iDuration % 3600)) / 3600;
        }
        return iDurationHour;
    }
%>
<%
	response.setHeader("Content-Disposition","attachment; filename=Working_Hour_Summary.xls ");
	
	int iCommand = FRMQueryString.requestCommand(request);
    long companyId = FRMQueryString.requestLong(request, "company_id");
    long divisionId = FRMQueryString.requestLong(request, "division_id");
    long departmentId = FRMQueryString.requestLong(request, "department_id");
    long sectionId = FRMQueryString.requestLong(request, "inp_section_id");
    long payrollGroupId = FRMQueryString.requestLong(request, "payrollGroupId");
	long periodId = FRMQueryString.requestLong(request, "inp_period_id");
	long employeeId = FRMQueryString.requestLong(request, "emp_id");
	
	String where = " RESIGNED = 0 ";
	if (companyId > 0){
		where += " AND COMPANY_ID = "+companyId;
	}
	if (divisionId > 0){
		where += " AND DIVISION_ID = "+divisionId;
	}
	if (departmentId > 0){
		where += " AND DEPARTMENT_ID = "+departmentId;
	}
	if (sectionId > 0){
		where += " AND SECTION_ID = "+sectionId;
	}
	if (payrollGroupId > 0){
		where += " AND PAYROLL_GROUP = "+payrollGroupId;
	}
	if (employeeId > 0){
		where += " AND EMPLOYEE_ID = "+employeeId;
	}
	
	
	Vector listEmployee = PstEmployee.list(0, 0, where, "");
	
	long oidAl = 0;
	long oidDP = 0;
	long oidLL = 0;
	long oidSL = 0;
	long oidDC = 0;
	long oidEO = 0;
	long oidDO = 0;
	try {
		oidAl = Long.parseLong(PstSystemProperty.getValueByName("OID_AL"));
		oidDP = Long.parseLong(PstSystemProperty.getValueByName("OID_DP"));
		oidLL = Long.parseLong(PstSystemProperty.getValueByName("OID_LL"));
		oidSL = Long.parseLong(PstSystemProperty.getValueByName("OID_SPECIAL_LEAVE"));
		oidDC = Long.parseLong(PstSystemProperty.getValueByName("OID_SICK_LEAVE"));
		oidDO = Long.parseLong(PstSystemProperty.getValueByName("OID_DAY_OFF"));
		oidEO = Long.parseLong(PstSystemProperty.getValueByName("OID_EO"));
	} catch (Exception exc){}
	
	Vector vctSchIDOff = PstScheduleSymbol.getScheduleId(PstScheduleCategory.CATEGORY_OFF);
%>
<%@page contentType="application/vnd.ms-excel" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <table border="1" style='border-collapse: collapse;'>
			<thead>
				<tr>
					<th>No</th>
					<th>Emp Number.</th>
					<th>Name</th>
					<th>Department</th>
					<th>Section</th>
					<th>Position</th>
					<th>Start Working</th>
					<th>Total Sickness</th>
					<th>Total AL</th>
					<th>Total LL</th>
					<th>Total SL</th>
					<th>Total EO</th>
					<th>Total DO</th>
					<th>Total Days</th>
					<th>Total Hours (Finger)</th>
					<th>Total Hours (Working Hours)</th>
					<th>Remarks</th>
				</tr>
			</thead>
			<tbody>
				<%
					if (listEmployee.size() > 0){
						for (int i=0; i < listEmployee.size();i++){
							Employee emp = (Employee) listEmployee.get(i);
							Formater.formatDate(emp.getCommencingDate(), "d-MMM-yyyy");
							
							Period period = new Period();
							try {
								period = PstPeriod.fetchExc(periodId);
							} catch (Exception exc){}
							
							double al = 0;
							double ll = 0;
							double sl = 0;
							double dp = 0;
							double eo = 0;
							double dc = 0;
							double sdo = 0;
							double days = 0;
							
							long fingerDur = 0;
							long realDur = 0;
							
							Vector listAttendance = SessEmpSchedule.listEmpPresenceDaily(0, period.getStartDate(), period.getEndDate(), 0, emp.getEmployeeNum(), "", "", 0, 31, null, 0, "", 0, 0, 0, "");
							if (listAttendance.size()>0){
								for (int x = 0; x < listAttendance.size(); x++) { 
									boolean isNotOff = true;
									PresenceReportDaily presenceReportDaily = (PresenceReportDaily) listAttendance.get(x);
									long lDatePresence = presenceReportDaily.getSelectedDate().getTime();
									long schSymbol = presenceReportDaily.getScheduleId1();
									long lEmpId = presenceReportDaily.getEmpId();
									Date dtSchldIn1st = (Date) presenceReportDaily.getScheduleIn1st();
									Date dtSchldOut1st = (Date) presenceReportDaily.getScheduleOut1st();
									Date dtActualIn1st = (Date) presenceReportDaily.getActualIn1st();
									Date dtActualOut1st = (Date) presenceReportDaily.getActualOut1st();
				
									Vector listLeaveAplication=new Vector();
									try{
											listLeaveAplication =  PstLeaveApplication.listOid(lEmpId, dtSchldIn1st,dtSchldOut1st);
									}catch(Exception exc){
										System.out.println("errorr"+exc);
									}
									
									ScheduleSymbol symbol = new ScheduleSymbol();
									try {
										symbol = PstScheduleSymbol.fetchExc(schSymbol);
									} catch (Exception exc){}
									
									long oidLeave  = 0;
									if(listLeaveAplication !=null && listLeaveAplication.size()>0){
										//LeaveOidSym obj = new LeaveOidSym();
										try{
										for (int j = 0; j < listLeaveAplication.size(); j++) {
											LeaveOidSym leaveOidSym = (LeaveOidSym) listLeaveAplication.get(j);
											oidLeave = leaveOidSym.getLeaveOid();
											//update by satrya 2013-12-12
											//mencari jika ada schedule off
											if(vctSchIDOff!=null && vctSchIDOff.size()>0){
												for(int xOff=0;xOff<vctSchIDOff.size();xOff++){
													long oidOff = (Long)vctSchIDOff.get(xOff);
													//jika schedulenya off maka dihilangkan symbol cutinya
													if((presenceReportDaily.getScheduleId1() == oidOff)){
														isNotOff = false;
														continue;
													}
												}
											}
											
											long symId = PstScheduleSymbol.getScheduleId(leaveOidSym.getLeaveSymbol());
											
											if (symId == oidAl && isNotOff){
												al = al + 1;
											}
											if (symId == oidLL && isNotOff){
												ll = ll + 1;
											}
											if (symId == oidSL && isNotOff){
												sl = sl + 1;
											}
											if (symId == oidDC && isNotOff){
												dc = dc + 1;
											}
										}

										}catch(Exception ex){System.out.println("Exception list Leave Application"+ex);}

									}
									
									if (schSymbol == oidDO){
										sdo = sdo + 1;
									}else if (schSymbol == oidEO){
										eo = eo + 1;
									}
									
									long finger = getWorkingDuration(dtActualIn1st, dtActualOut1st, 0);
									fingerDur = fingerDur + finger;
									/*if (finger > 0){*/
										Calendar c1 = Calendar.getInstance();
										Calendar c2 = Calendar.getInstance();
										c1.setTime(symbol.getTimeIn());
										c2.setTime(symbol.getTimeOut());
										if(c2.get(Calendar.HOUR_OF_DAY) < 12) {
											c2.set(Calendar.DAY_OF_YEAR, c2.get(Calendar.DAY_OF_YEAR) + 1);
										}
										long elapsed = c2.getTimeInMillis() - c1.getTimeInMillis();
										long hour = (elapsed / (1000*60*60)) % 24;
										if (hour > 0){
											days = days + 1;
										}
										realDur = realDur + hour;
									/*}*/
									
									
								}
							}
							%>
							<tr>
								<td><%=(i+1)%></td>
								<td><%=emp.getEmployeeNum()%></td>
								<td><%=emp.getFullName()%></td>
								<td><%=PstEmployee.getDepartmentName(emp.getDepartmentId())%></td>
								<td><%=PstEmployee.getSectionName(emp.getSectionId())%></td>
								<td><%=PstEmployee.getPositionName(emp.getPositionId())%></td>
								<td><%=Formater.formatDate(emp.getCommencingDate(), "d-MMM-yyyy")%></td>
								<td><%=dc%></td>
								<td><%=al%></td>
								<td><%=ll%></td>
								<td><%=sl%></td>
								<td><%=eo%></td>
								<td><%=sdo%></td>
								<td><%=days%></td>
								<td><%=fingerDur%></td>
								<td><%=realDur%></td>
								<td></td>
							</tr>
							<%
						}
					}
				%>
			</tbody>
		</table>
    </body>
</html>
