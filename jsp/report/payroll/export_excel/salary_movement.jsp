<%-- 
    Document   : salary_movement
    Created on : Dec 13, 2018, 3:54:32 PM
    Author     : IanRizky
--%>
<%@page import="com.dimata.harisma.entity.payroll.PayPeriod"%>
<%@page import="com.dimata.harisma.entity.payroll.PstPayPeriod"%>
<%@page import="com.dimata.harisma.entity.payroll.Value_Mapping"%>
<%@page import="com.dimata.harisma.entity.payroll.PstValue_Mapping"%>
<%@page import="com.dimata.util.Formater"%>
<%@page import="com.dimata.harisma.entity.payroll.PstPayGeneral"%>
<%@page import="com.dimata.harisma.entity.payroll.PayGeneral"%>
<%@page import="org.apache.commons.codec.binary.Base64"%>
<%@page import="org.apache.commons.io.IOUtils"%>
<%@page import="java.io.ByteArrayOutputStream"%>
<%@page import="java.io.InputStream"%>
<%@page import="java.net.URLConnection"%>
<%@page import="java.net.URL"%>
<%@page import="com.dimata.qdep.form.FRMQueryString"%>
<%@ include file = "../../../main/javainit.jsp" %>
<%!

	public static String getCompany(long oidCompany){
		String returnStr="-";
		if(oidCompany!=0){
			PayGeneral payGeneral =new PayGeneral();
			try{
				payGeneral =PstPayGeneral.fetchExc(oidCompany);
			}catch(Exception exc){

			}
			returnStr = payGeneral.getCompanyName();
		}
		return returnStr;
	}

	public static String getDivision(long oidDivision){
		String returnStr="-";
		if(oidDivision!=0){
			Division division =new Division();
			try{
				division =PstDivision.fetchExc(oidDivision);
			}catch(Exception exc){

			}
			returnStr = division.getDivision();
		}
		return returnStr;
	}

	public static String getDepartment(long oidDepartment){
		String returnStr="-";
		if(oidDepartment!=0){
			Department department =new Department();
			try{
				department =PstDepartment.fetchExc(oidDepartment);
			}catch(Exception exc){

			}
			returnStr = department.getDepartment();
		}
		return returnStr;
	}

	public static String getSection(long oidSection){
		String returnStr="-";
		if(oidSection!=0){
			Section section =new Section();
			try{
				section =PstSection.fetchExc(oidSection);
			}catch(Exception exc){

			}
			returnStr = section.getSection();
		}
		return returnStr;
	}

	public static String getPosition(long oidPosition){
		String returnStr="-";
		if(oidPosition!=0){
			Position position =new Position();
			try{
				position =PstPosition.fetchExc(oidPosition);
			}catch(Exception exc){

			}
			returnStr = position.getPosition();
		}
		return returnStr;
	}

	public static String getLevel(long oidLevel){
		String returnStr="-";
		if(oidLevel!=0){
			Level level =new Level();
			try{
				level =PstLevel.fetchExc(oidLevel);
			}catch(Exception exc){

			}
			returnStr = level.getLevel();
		}
		return returnStr;
	}

	public static String getCategory(long oidCategory){
		String returnStr="-";
		if(oidCategory!=0){
			EmpCategory cat =new EmpCategory();
			try{
				cat =PstEmpCategory.fetchExc(oidCategory);
			}catch(Exception exc){

			}
			returnStr = cat.getEmpCategory();
		}
		return returnStr;
	}

%>
<%
	response.setHeader("Content-Disposition","attachment; filename=Salary_Movement.xls ");
	
	int iCommand = FRMQueryString.requestCommand(request);
    long companyId = FRMQueryString.requestLong(request, "company_id");
    long divisionId = FRMQueryString.requestLong(request, "division_id");
    long departmentId = FRMQueryString.requestLong(request, "department_id");
    long sectionId = FRMQueryString.requestLong(request, "inp_section_id");
    long payrollGroupId = FRMQueryString.requestLong(request, "payrollGroupId");
	String empNum = FRMQueryString.requestString(request, "emp_num");

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
	if (!empNum.equals("")){
		where += " AND EMPLOYEE_NUM IN ("+empNum+")";
	}
	
	
	Vector listEmployee = PstEmployee.list(0, 0, where, "");
	
	String basicCode = "";
	String meritCode = "";
	String gradeAlCode = "";
	try {
		basicCode = PstSystemProperty.getValueByName("BASIC_SALARY_CODE");
		meritCode = PstSystemProperty.getValueByName("MERIT_CODE");
		gradeAlCode = PstSystemProperty.getValueByName("GRADE_ALLOWANCE_CODE");
	} catch (Exception exc){}
	
	String frmCurrency = "#,###";
%>
<%@page contentType="application/vnd.ms-excel" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <table border="1" style="border-collapse: collapse">
			<tr>
				<th rowspan="2">No</th>
				<th rowspan="2">Payroll</th>
				<th rowspan="2">Name</th>
				<th colspan="9">Career Path</th>
				<th colspan="4">Salary</th>
			</tr>
			<tr>
				<th>Company</th>
				<th>Division</th>
				<th>Department</th>
				<th>Section</th>
				<th>Position</th>
				<th>Level</th>
				<th>Category</th>
				<th>History From</th>
				<th>History To</th>
				<th>Payroll Period</th>
				<th>Start Date</th>
				<th>End Date</th>
				<th>Basic Salary</th>
			</tr>
			<%
				if (listEmployee.size()>0){
					for (int i=0; i < listEmployee.size();i++){
						Employee emp = (Employee) listEmployee.get(i);
						
						String whereCareer = PstCareerPath.fieldNames[PstCareerPath.FLD_EMPLOYEE_ID]+"="+emp.getOID();
						String order = PstCareerPath.fieldNames[PstCareerPath.FLD_WORK_FROM];
						Vector listCareer = PstCareerPath.list(0, 0, whereCareer, order);
						
						%>
							<%
								Date lastWorkDate = new Date();
								if (listCareer.size()>0){
									for (int x = 0; x < listCareer.size(); x++){
										CareerPath careerPath = (CareerPath) listCareer.get(x);
										lastWorkDate = careerPath.getWorkTo();
										String whereMap = PstValue_Mapping.fieldNames[PstValue_Mapping.FLD_START_DATE]+" BETWEEN '"
													+ Formater.formatDate(careerPath.getWorkFrom(),"yyyy-MM-dd") + "' AND '"
													+ Formater.formatDate(careerPath.getWorkTo(),"yyyy-MM-dd") +"' AND "
													+ PstValue_Mapping.fieldNames[PstValue_Mapping.FLD_EMPLOYEE_ID]+"="+emp.getOID()
													+ " AND "+PstValue_Mapping.fieldNames[PstValue_Mapping.FLD_COMP_CODE]+"='"+basicCode+"'";
										String orderMap = PstValue_Mapping.fieldNames[PstValue_Mapping.FLD_START_DATE];
										Vector listValBasic = PstValue_Mapping.list(0, 0, whereMap, orderMap);
										%>
										<tr>
											<% if (x==0) {%>
												<td><%=(i+1)%></td>
												<td><%=emp.getEmployeeNum()%></td>
												<td><%=emp.getFullName()%></td>
											<% } else { %>
												<td></td>
												<td></td>
												<td></td>
											<% } %>
											
											<td><%=getCompany(careerPath.getCompanyId())%></td>
											<td><%=getDivision(careerPath.getDivisionId())%></td>
											<td><%=getDepartment(careerPath.getDepartmentId())%></td>
											<td><%=getSection(careerPath.getSectionId())%></td>
											<td><%=getPosition(careerPath.getPositionId())%></td>
											<td><%=getLevel(careerPath.getLevelId())%></td>
											<td><%=getCategory(careerPath.getEmpCategoryId())%></td>
											<td><%=Formater.formatDate(careerPath.getWorkFrom(), "dd-MMM-yyyy")%></td>
											<td><%=Formater.formatDate(careerPath.getWorkTo(), "dd-MMM-yyyy")%></td>
											<%
												if (listValBasic.size()>0){
													Value_Mapping valMap = (Value_Mapping) listValBasic.get(0);
													PayPeriod payPeriod = new PayPeriod();
													try {
														payPeriod = PstPayPeriod.getPayPeriodBySelectedDate(valMap.getStartdate());
													} catch (Exception exc){}
													
													%> 
														<td><%=payPeriod.getPeriod()%></td>
														<td><%=Formater.formatDate(payPeriod.getStartDate(), "dd-MMM-yyyy")%></td>
														<td><%=Formater.formatDate(payPeriod.getEndDate(), "dd-MMM-yyyy")%></td>
														<td style="text-align: center"><%=Formater.formatNumberVer1(valMap.getValue(), frmCurrency)%></td>
													<%
												} else {
													%><td colspan="4" style="text-align: center">No Data Available</td><%
												}
											%>
										</tr>
										<%
											if (listValBasic.size() > 1){
												for (int xx=1; xx < listValBasic.size(); xx++){
													%>
														<td></td>
														<td></td>
														<td></td>
														<td></td>
														<td></td>
														<td></td>
														<td></td>
														<td></td>
														<td></td>
														<td></td>
														<td></td>
														<td></td>
													<%
														Value_Mapping valMap = (Value_Mapping) listValBasic.get(xx);
														PayPeriod payPeriod = new PayPeriod();
														try {
															payPeriod = PstPayPeriod.getPayPeriodBySelectedDate(valMap.getStartdate());
														} catch (Exception exc){}

														%> 
															<td><%=payPeriod.getPeriod()%></td>
															<td><%=Formater.formatDate(payPeriod.getStartDate(), "dd-MMM-yyyy")%></td>
															<td><%=Formater.formatDate(payPeriod.getEndDate(), "dd-MMM-yyyy")%></td>
															<td style="text-align: center"><%=Formater.formatNumberVer1(valMap.getValue(), frmCurrency)%></td>
														<%
												}
											}
									}
									lastWorkDate.setDate(lastWorkDate.getDate() + 1);
									String whereMap = PstValue_Mapping.fieldNames[PstValue_Mapping.FLD_START_DATE]+" BETWEEN '"
												+ Formater.formatDate(lastWorkDate,"yyyy-MM-dd") + "' AND '"
												+ Formater.formatDate(new Date(),"yyyy-MM-dd") +"' AND "
												+ PstValue_Mapping.fieldNames[PstValue_Mapping.FLD_EMPLOYEE_ID]+"="+emp.getOID()
												+ " AND "+PstValue_Mapping.fieldNames[PstValue_Mapping.FLD_COMP_CODE]+"='"+basicCode+"'";
									String orderMap = PstValue_Mapping.fieldNames[PstValue_Mapping.FLD_START_DATE];
									Vector listValBasic = PstValue_Mapping.list(0, 0, whereMap, orderMap);
								%>
								<tr>
									<td></td>
									<td></td>
									<td></td>
									<td><%=getCompany(emp.getCompanyId())%></td>
									<td><%=getDivision(emp.getDivisionId())%></td>
									<td><%=getDepartment(emp.getDepartmentId())%></td>
									<td><%=getSection(emp.getSectionId())%></td>
									<td><%=getPosition(emp.getPositionId())%></td>
									<td><%=getLevel(emp.getLevelId())%></td>
									<td><%=getCategory(emp.getEmpCategoryId())%></td>
									<td><%=Formater.formatDate(lastWorkDate, "dd-MMM-yyyy")%></td>
									<td><%=Formater.formatDate(new Date(), "dd-MMM-yyyy")%></td>
									<%
										if (listValBasic.size()>0){
											Value_Mapping valMap = (Value_Mapping) listValBasic.get(0);
											PayPeriod payPeriod = new PayPeriod();
											try {
												payPeriod = PstPayPeriod.getPayPeriodBySelectedDate(valMap.getStartdate());
											} catch (Exception exc){}

											%> 
												<td><%=payPeriod.getPeriod()%></td>
												<td><%=Formater.formatDate(payPeriod.getStartDate(), "dd-MMM-yyyy")%></td>
												<td><%=Formater.formatDate(payPeriod.getEndDate(), "dd-MMM-yyyy")%></td>
												<td style="text-align: center"><%=Formater.formatNumberVer1(valMap.getValue(), frmCurrency)%></td>
											<%
										} else {
											%><td colspan="4" style="text-align: center">No Data Available</td><%
										}
									%>
								</tr>
								<%
								if (listValBasic.size() > 1){
									for (int xx=1; xx < listValBasic.size(); xx++){
										%>
											<td></td>
											<td></td>
											<td></td>
											<td></td>
											<td></td>
											<td></td>
											<td></td>
											<td></td>
											<td></td>
											<td></td>
											<td></td>
											<td></td>
										<%
											Value_Mapping valMap = (Value_Mapping) listValBasic.get(xx);
											PayPeriod payPeriod = new PayPeriod();
											try {
												payPeriod = PstPayPeriod.getPayPeriodBySelectedDate(valMap.getStartdate());
											} catch (Exception exc){}

											%> 
												<td><%=payPeriod.getPeriod()%></td>
												<td><%=Formater.formatDate(payPeriod.getStartDate(), "dd-MMM-yyyy")%></td>
												<td><%=Formater.formatDate(payPeriod.getEndDate(), "dd-MMM-yyyy")%></td>
												<td style="text-align: center"><%=Formater.formatNumberVer1(valMap.getValue(), frmCurrency)%></td>
											<%
									}
								}
									
										
								} else {
									String whereMap = PstValue_Mapping.fieldNames[PstValue_Mapping.FLD_START_DATE]+" BETWEEN '"
												+ Formater.formatDate(emp.getCommencingDate(),"yyyy-MM-dd") + "' AND '"
												+ Formater.formatDate(new Date(),"yyyy-MM-dd") +"' AND "
												+ PstValue_Mapping.fieldNames[PstValue_Mapping.FLD_EMPLOYEE_ID]+"="+emp.getOID()
												+ " AND "+PstValue_Mapping.fieldNames[PstValue_Mapping.FLD_COMP_CODE]+"='"+basicCode+"'";
									String orderMap = PstValue_Mapping.fieldNames[PstValue_Mapping.FLD_START_DATE];
									Vector listValBasic = PstValue_Mapping.list(0, 0, whereMap, orderMap);
									%>
									<tr>
										<td><%=(i+1)%></td>
										<td><%=emp.getEmployeeNum()%></td>
										<td><%=emp.getFullName()%></td>
										<td><%=getCompany(emp.getCompanyId())%></td>
										<td><%=getDivision(emp.getDivisionId())%></td>
										<td><%=getDepartment(emp.getDepartmentId())%></td>
										<td><%=getSection(emp.getSectionId())%></td>
										<td><%=getPosition(emp.getPositionId())%></td>
										<td><%=getLevel(emp.getLevelId())%></td>
										<td><%=getCategory(emp.getEmpCategoryId())%></td>
										<td><%=Formater.formatDate(emp.getCommencingDate(), "dd-MMM-yyyy")%></td>
										<td><%=Formater.formatDate(new Date(), "dd-MMM-yyyy")%></td>
										<%
											if (listValBasic.size()>0){
												Value_Mapping valMap = (Value_Mapping) listValBasic.get(0);
												PayPeriod payPeriod = new PayPeriod();
												try {
													payPeriod = PstPayPeriod.getPayPeriodBySelectedDate(valMap.getStartdate());
												} catch (Exception exc){}

												%> 
													<td><%=payPeriod.getPeriod()%></td>
													<td><%=Formater.formatDate(payPeriod.getStartDate(), "dd-MMM-yyyy")%></td>
													<td><%=Formater.formatDate(payPeriod.getEndDate(), "dd-MMM-yyyy")%></td>
													<td style="text-align: center"><%=Formater.formatNumberVer1(valMap.getValue(), frmCurrency)%></td>
												<%

												
											} else {
												%><td colspan="4" style="text-align: center">No Data Available</td><%
											}
										%>
									</tr>
									<%
										if (listValBasic.size() > 1){
											for (int xx=1; xx < listValBasic.size(); xx++){
												%>
													<td></td>
													<td></td>
													<td></td>
													<td></td>
													<td></td>
													<td></td>
													<td></td>
													<td></td>
													<td></td>
													<td></td>
													<td></td>
													<td></td>
												<%
													Value_Mapping valMap = (Value_Mapping) listValBasic.get(xx);
													PayPeriod payPeriod = new PayPeriod();
													try {
														payPeriod = PstPayPeriod.getPayPeriodBySelectedDate(valMap.getStartdate());
													} catch (Exception exc){}
													
													%> 
														<td><%=payPeriod.getPeriod()%></td>
														<td><%=Formater.formatDate(payPeriod.getStartDate(), "dd-MMM-yyyy")%></td>
														<td><%=Formater.formatDate(payPeriod.getEndDate(), "dd-MMM-yyyy")%></td>
														<td style="text-align: center"><%=Formater.formatNumberVer1(valMap.getValue(), frmCurrency)%></td>
													<%
											}
										}
								}
					}
				}
			%>
		</table>
    </body>
</html>

