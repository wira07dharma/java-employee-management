<%-- 
    Document   : thr_mapping
    Created on : 29-Aug-2017, 09:55:25
    Author     : Gunadi
--%>
<%@page import="com.dimata.harisma.form.payroll.CtrlThrCalculationMapping"%>
<%@page import="com.dimata.harisma.entity.payroll.PstThrCalculationMapping"%>
<%@page import="com.dimata.gui.jsp.*"%>
<%@page import="com.dimata.util.*"%>
<%@page import="com.dimata.harisma.entity.payroll.ThrCalculationMapping"%>
<%@page import="com.dimata.harisma.form.payroll.FrmThrCalculationMapping"%>
<%@ include file = "../../main/javainit.jsp" %>
<% 
int appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_REPORTS, AppObjInfo.G2_PAYROLL_REPORT, AppObjInfo.OBJ_CUSTOM_RPT_MAIN);
%>
<%@ include file = "../../main/checkuser.jsp" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%!
	public String drawList(int iCommand,FrmThrCalculationMapping frmObject, ThrCalculationMapping objEntity, Vector objectClass,  long mappingId, long oidMain, int status)

	{
		ControlList ctrlist = new ControlList();
		ctrlist.setAreaWidth("100%");
		ctrlist.setListStyle("tblStyle");
                ctrlist.setTitleStyle("title_tbl");
                ctrlist.setCellStyle("listgensell");
                ctrlist.setHeaderStyle("title_tbl");
                ctrlist.setCellSpacing("0");
                ctrlist.addHeader("No", "2%");
                //ctrlist.addHeader("Comp_Code", "7%");
                ctrlist.addHeader("Company", "8%");
                ctrlist.addHeader("Division", "8%");
                ctrlist.addHeader("Department", "8%");
                ctrlist.addHeader("Section", "8%");
                ctrlist.addHeader("Level", "8%");
                ctrlist.addHeader("Marital", "8%"); 
                ctrlist.addHeader("Tax Marital", "8%"); 
                ctrlist.addHeader("Employee Category", "8%"); 
                ctrlist.addHeader("Position", "8%");  
                ctrlist.addHeader("Grade", "8%"); 
                ctrlist.addHeader("Payroll Num", "8%");
                ctrlist.addHeader("Payroll Group", "8%");
                ctrlist.addHeader("LOS From", "8%");
                ctrlist.addHeader("LOS To", "8%");
                ctrlist.addHeader("LOS Current", "8%");
                ctrlist.setLinkRow(0);
		ctrlist.setLinkSufix("");
		Vector lstData = ctrlist.getData();
		Vector lstLinkData = ctrlist.getLinkData();
		Vector rowx = new Vector(1,1);
		ctrlist.reset();
		int index = -1;
                         
            
       
                    //level
                    Vector level_value = new Vector(1, 1);
                    Vector level_key = new Vector(1, 1);
                    level_key.add("- select -");
                        level_value.add("0");
                    Vector listLev = PstLevel.list(0, 0, null, null);
                    for (int s = 0; s< listLev.size(); s++) {
                        Level lev = (Level) listLev.get(s);
                        level_key.add(lev.getLevel());
                        level_value.add(String.valueOf(lev.getOID()));
                    }
                    
                    //day
                    Vector day_keyVal = new Vector(1, 1);
                    for (int s = 0; s < 32; s++) {
                        day_keyVal.add(""+s);
                    }
                    
                    //month
                    Vector month_keyVal = new Vector(1, 1);
                    for (int s = 0; s < 13; s++) {
                        month_keyVal.add(""+s);
                    }
                    
                    //year
                    Vector year_keyVal = new Vector(1, 1);
                    for (int s = 0; s < 13; s++) {
                        year_keyVal.add(""+s);
                    }
                    
                    
                    //current
                    Vector currentDate_value = new Vector(1, 1);
                    Vector currentDate_key = new Vector(1, 1);
                    currentDate_key.add("No");
                    currentDate_value.add("0");    
                    currentDate_key.add("Yes");
                    currentDate_value.add("1");
                    
                    //maarital
                    Vector marital_value = new Vector(1, 1);
                    Vector marital_key = new Vector(1, 1);
                    marital_key.add("- select -");
                    marital_value.add("0");
                    Vector listMarital = PstMarital.list(0, 0, null, null);
                    for (int s = 0; s< listMarital.size(); s++) {
                        Marital marital = (Marital) listMarital.get(s);
                        marital_key.add(marital.getMaritalCode());
                        marital_value.add(String.valueOf(marital.getOID()));
                    }
                    
                    
                     //emp_category
                    Vector empcat_value = new Vector(1, 1);
                    Vector empcat_key = new Vector(1, 1);
                    empcat_key.add("- select -");
                    empcat_value.add("0");
                    Vector listEmpcat = PstEmpCategory.list(0, 0, null, null);
                    for (int s = 0; s< listEmpcat.size(); s++) {
                        EmpCategory empcat = (EmpCategory) listEmpcat.get(s);
                        empcat_key.add(empcat.getEmpCategory());
                        empcat_value.add(String.valueOf(empcat.getOID()));
                    }
                    
                     //position
                    Vector position_value = new Vector(1, 1);
                    Vector position_key = new Vector(1, 1);
                    position_key.add("- select -");
                    position_value.add("0");
                    Vector listposition = PstPosition.list(0, 0, null, null);
                    for (int s = 0; s< listposition.size(); s++) {
                        Position position = (Position) listposition.get(s);
                        position_key.add(position.getPosition());
                        position_value.add(String.valueOf(position.getOID()));
                    }

                     //gradeLevel
                    Vector grade_value = new Vector(1, 1);
                    Vector grade_key = new Vector(1, 1);
                    grade_key.add("- select -");
                    grade_value.add("0");
                    Vector listGradeLevel = PstGradeLevel.list(0, 0, null, null);
                    for (int s = 0; s< listGradeLevel.size(); s++) {
                        GradeLevel gradeLevel = (GradeLevel) listGradeLevel.get(s);
                        grade_key.add(gradeLevel.getCodeLevel());
                        grade_value.add(String.valueOf(gradeLevel.getOID()));
                    }
                    
                    //payrollGroup
                    Vector payGroup_value = new Vector(1,1);
                    Vector payGroup_key = new Vector(1,1);
                    payGroup_key.add("- select -");
                    payGroup_value.add("0");
                    Vector listPayGroup = PstPayrollGroup.list(0, 0, null, null);
                    for (int s = 0; s< listPayGroup.size(); s++) {
                        PayrollGroup payGroup = (PayrollGroup) listPayGroup.get(s);
                        payGroup_key.add(payGroup.getPayrollGroupName());
                        payGroup_value.add(String.valueOf(payGroup.getOID()));
                    }
                                                 
		for (int i = 0; i < objectClass.size(); i++) {
			 ThrCalculationMapping thrMapping = (ThrCalculationMapping)objectClass.get(i);
			 rowx = new Vector();
			 if(mappingId == thrMapping.getOID())
				 index = i; 
     
			 if(index == i && (iCommand == Command.EDIT || iCommand == Command.ASK)){
                             
                                 //company
                    Vector comp_value = new Vector(1, 1);
                    Vector comp_key = new Vector(1, 1);
                    Vector listComp = PstCompany.list(0, 0, "", " COMPANY ");
                    comp_value.add(""+0);
                    comp_key.add("select");
                    for (int c = 0; c < listComp.size(); c++) {
                        Company div = (Company) listComp.get(c);
                        comp_key.add(div.getCompany());
                        comp_value.add(String.valueOf(div.getOID()));
                    }
                
                  //division
                    Vector div_value = new Vector(1, 1);
                    Vector div_key = new Vector(1, 1);
                    //update priska 2015-01-29
                       div_key.add("-select-");
                        div_value.add("0");
                        String strWhere="";
                        if(objEntity.getOID()>0){
                        strWhere = PstDivision.fieldNames[PstDivision.FLD_COMPANY_ID] + "=" +  "" + objEntity.getCompanyId() ;//oidCompany; 
                        } else {
                        strWhere = PstDivision.fieldNames[PstDivision.FLD_COMPANY_ID] + "=" +  "" + thrMapping.getCompanyId() ;//oidCompany; 
                        }	
                        Vector listDiv = PstDivision.list(0, 0, strWhere, " DIVISION ");
                    
                        for (int c = 0; c < listDiv.size(); c++) {
                        Division div = (Division) listDiv.get(c);
                        div_key.add(div.getDivision());
                        div_value.add(String.valueOf(div.getOID()));
                        
                    }  
                
                  
                    Vector dept_value = new Vector(1, 1);
                    Vector dept_key = new Vector(1, 1);
                    dept_key.add("-select-");
                    dept_value.add("0");
                    String strWhereDept="";
                    if(objEntity.getOID()>0){	
                    strWhereDept = PstDepartment.TBL_HR_DEPARTMENT+"."+PstDepartment.fieldNames[PstDepartment.FLD_DIVISION_ID] + "=" + "" + objEntity.getDivisionId(); //oidDivision;
                    } else {
                    strWhereDept = PstDepartment.TBL_HR_DEPARTMENT+"."+PstDepartment.fieldNames[PstDepartment.FLD_DIVISION_ID] + "=" + "" + thrMapping.getDivisionId(); //oidDivision;
                    }
                    Vector listDept = PstDepartment.list(0, 0, strWhereDept, " DEPARTMENT ");
                    boolean adaDept=false;
                    for (int d = 0; d < listDept.size(); d++) {
                    Department dept = (Department) listDept.get(d);
                    dept_key.add(dept.getDepartment());
                    dept_value.add(String.valueOf(dept.getOID()));
                    } 
                  
                         //section
                    Vector sec_value = new Vector(1, 1);
                    Vector sec_key = new Vector(1, 1);
                    sec_key.add("- select -");
                    sec_value.add("0");
                    String strWhereSec ="";
                    if(objEntity.getOID()>0){	
                    strWhereSec = PstSection.TBL_HR_SECTION+"."+PstSection.fieldNames[PstSection.FLD_DEPARTMENT_ID] + "=" + "" + objEntity.getDepartmentId(); 
                    } else {
                    strWhereSec = PstSection.TBL_HR_SECTION+"."+PstSection.fieldNames[PstSection.FLD_DEPARTMENT_ID] + "=" + "" + thrMapping.getDepartmentId(); 
                    }
                    
                    Vector listSec = PstSection.list(0, 0, strWhereSec, "SECTION");
                    for (int s = 0; s< listSec.size(); s++) {
                        Section sec = (Section) listSec.get(s);
                        sec_key.add(sec.getSection());
                        sec_value.add(String.valueOf(sec.getOID()));
                    }         
                                       
                if(objEntity.getOID()>0){			

                rowx.add("<input type=\"hidden\" name=\"" + frmObject.fieldNames[FrmThrCalculationMapping.FRM_FIELD_MAPPING_TYPE] + "\" value=\"" + PstThrCalculationMapping.TYPE_EXCLUSION+ "\" size=\"40\" class=\"elemenForm\"> "
                        + "<input type=\"hidden\" name=\"" + frmObject.fieldNames[FrmThrCalculationMapping.FRM_FIELD_CALCULATION_MAIN_ID] + "\" value=\"" + oidMain+ "\" size=\"40\" class=\"elemenForm\">") ;  
                rowx.add(ControlCombo.draw(frmObject.fieldNames[FrmThrCalculationMapping.FRM_FIELD_COMPANY_ID], "formElemen", null, String.valueOf(objEntity.getCompanyId()), comp_value, comp_key, "onChange=\"javascript:cmdUpdateCDDS(1)\""));
                rowx.add(ControlCombo.draw(frmObject.fieldNames[FrmThrCalculationMapping.FRM_FIELD_DIVISION_ID], "formElemen", null, String.valueOf(objEntity.getDivisionId()), div_value, div_key, "onChange=\"javascript:cmdUpdateCDDS(1)\""));
                rowx.add(ControlCombo.draw(frmObject.fieldNames[FrmThrCalculationMapping.FRM_FIELD_DEPARTMENT_ID], "formElemen", null, String.valueOf(objEntity.getDepartmentId()), dept_value, dept_key, "onChange=\"javascript:cmdUpdateCDDS(1)\""));
                rowx.add(ControlCombo.draw(frmObject.fieldNames[FrmThrCalculationMapping.FRM_FIELD_SECTION_ID], "formElemen", null, String.valueOf(objEntity.getSectionId()), sec_value, sec_key, ""));
                 
                rowx.add(ControlCombo.draw(frmObject.fieldNames[FrmThrCalculationMapping.FRM_FIELD_LEVEL_ID], "formElemen", null, String.valueOf(objEntity.getLevelId()), level_value, level_key, ""));
                rowx.add(ControlCombo.draw(frmObject.fieldNames[FrmThrCalculationMapping.FRM_FIELD_MARITAL_ID], "formElemen", null, String.valueOf(objEntity.getMaritalId()), marital_value, marital_key, ""));
                rowx.add(ControlCombo.draw(frmObject.fieldNames[FrmThrCalculationMapping.FRM_FIELD_TAX_MARITAL_ID], "formElemen", null, String.valueOf(objEntity.getTaxMaritalId()), marital_value, marital_key, ""));
                rowx.add(ControlCombo.draw(frmObject.fieldNames[FrmThrCalculationMapping.FRM_FIELD_EMPLOYEE_CATEGORY], "formElemen", null, String.valueOf(objEntity.getEmployeeCategory()), empcat_value, empcat_key, ""));
                rowx.add(ControlCombo.draw(frmObject.fieldNames[FrmThrCalculationMapping.FRM_FIELD_POSITION_ID], "formElemen", null, String.valueOf(objEntity.getPositionId()), position_value, position_key, ""));
                rowx.add(ControlCombo.draw(frmObject.fieldNames[FrmThrCalculationMapping.FRM_FIELD_GRADE], "formElemen", null, String.valueOf(objEntity.getGrade()), grade_value, grade_key, ""));
                rowx.add("<input type=\"text\" name=\"EMP_FULLNAME\"  class=\"elemenForm\"> <input type=\"hidden\" name=\""+FrmThrCalculationMapping.fieldNames[FrmThrCalculationMapping.FRM_FIELD_EMPLOYEE_ID]+"\" value=\"\" class=\"formElemen\" >  <a href=\"javascript:cmdSearchEmp()\">add employee</a>");
                rowx.add(ControlCombo.draw(frmObject.fieldNames[FrmThrCalculationMapping.FRM_FIELD_PAYROLL_GROUP_ID], "formElemen", null, String.valueOf(objEntity.getPayrollGroupId()), payGroup_value, payGroup_key, ""));
                
                rowx.add(""+ControlCombo.draw(frmObject.fieldNames[FrmThrCalculationMapping.FRM_FIELD_LOS_FROM_IN_DAY], "formElemen", null, String.valueOf(objEntity.getLosFromInDay()), day_keyVal, day_keyVal, "")+" <br> "+ ControlCombo.draw(frmObject.fieldNames[FrmThrCalculationMapping.FRM_FIELD_LOS_FROM_IN_MONTH], "formElemen", null, String.valueOf(objEntity.getLosFromInMonth()), month_keyVal, month_keyVal, "")+" <br> "+ ControlCombo.draw(frmObject.fieldNames[FrmThrCalculationMapping.FRM_FIELD_LOS_FROM_IN_YEAR], "formElemen", null, String.valueOf(objEntity.getLosFromInYear()), year_keyVal, year_keyVal, ""));
                rowx.add(""+ControlCombo.draw(frmObject.fieldNames[FrmThrCalculationMapping.FRM_FIELD_LOS_TO_IN_DAY], "formElemen", null, String.valueOf(objEntity.getLosToInDay()), day_keyVal, day_keyVal, "")+" <br> "+ ControlCombo.draw(frmObject.fieldNames[FrmThrCalculationMapping.FRM_FIELD_LOS_TO_IN_MONTH], "formElemen", null, String.valueOf(objEntity.getLosToInMonth()), month_keyVal, month_keyVal, "")+" <br> "+ ControlCombo.draw(frmObject.fieldNames[FrmThrCalculationMapping.FRM_FIELD_LOS_TO_IN_YEAR], "formElemen", null, String.valueOf(objEntity.getLosToInYear()), year_keyVal, year_keyVal, ""));
                rowx.add(""+ControlCombo.draw(frmObject.fieldNames[FrmThrCalculationMapping.FRM_FIELD_LOS_CURRENT_DATE], "formElemen", null, String.valueOf(objEntity.getLosCurrentDate()), currentDate_value, currentDate_key, "")+" <br> "+"/ "+ControlDate.drawDateWithStyle(frmObject.fieldNames[FrmThrCalculationMapping.FRM_FIELD_LOS_PER_CURRENT_DATE], objEntity.getLosPerCurrentDate(), 0, -40, "formElemen"));
                } else{
                rowx.add("<input type=\"hidden\" name=\"" + frmObject.fieldNames[FrmThrCalculationMapping.FRM_FIELD_MAPPING_TYPE] + "\" value=\"" + PstThrCalculationMapping.TYPE_EXCLUSION+ "\" size=\"40\" class=\"elemenForm\"> "
                        + "<input type=\"hidden\" name=\"" + frmObject.fieldNames[FrmThrCalculationMapping.FRM_FIELD_CALCULATION_MAIN_ID] + "\" value=\"" + oidMain+ "\" size=\"40\" class=\"elemenForm\">") ;  
                rowx.add(ControlCombo.draw(frmObject.fieldNames[FrmThrCalculationMapping.FRM_FIELD_COMPANY_ID], "formElemen", null, String.valueOf(thrMapping.getCompanyId()), comp_value, comp_key, "onChange=\"javascript:cmdUpdateCDDS(1)\""));
                rowx.add(ControlCombo.draw(frmObject.fieldNames[FrmThrCalculationMapping.FRM_FIELD_DIVISION_ID], "formElemen", null, String.valueOf(thrMapping.getDivisionId()), div_value, div_key, "onChange=\"javascript:cmdUpdateCDDS(1)\""));
                rowx.add(ControlCombo.draw(frmObject.fieldNames[FrmThrCalculationMapping.FRM_FIELD_DEPARTMENT_ID], "formElemen", null, String.valueOf(thrMapping.getDepartmentId()), dept_value, dept_key, "onChange=\"javascript:cmdUpdateCDDS(1)\""));
                rowx.add(ControlCombo.draw(frmObject.fieldNames[FrmThrCalculationMapping.FRM_FIELD_SECTION_ID], "formElemen", null, String.valueOf(thrMapping.getSectionId()), sec_value, sec_key, ""));
                 
                rowx.add(ControlCombo.draw(frmObject.fieldNames[FrmThrCalculationMapping.FRM_FIELD_LEVEL_ID], "formElemen", null, String.valueOf(thrMapping.getLevelId()), level_value, level_key, ""));
                rowx.add(ControlCombo.draw(frmObject.fieldNames[FrmThrCalculationMapping.FRM_FIELD_MARITAL_ID], "formElemen", null, String.valueOf(thrMapping.getMaritalId()), marital_value, marital_key, ""));
                rowx.add(ControlCombo.draw(frmObject.fieldNames[FrmThrCalculationMapping.FRM_FIELD_TAX_MARITAL_ID], "formElemen", null, String.valueOf(thrMapping.getTaxMaritalId()), marital_value, marital_key, ""));
                rowx.add(ControlCombo.draw(frmObject.fieldNames[FrmThrCalculationMapping.FRM_FIELD_EMPLOYEE_CATEGORY], "formElemen", null, String.valueOf(thrMapping.getEmployeeCategory()), empcat_value, empcat_key, ""));
                rowx.add(ControlCombo.draw(frmObject.fieldNames[FrmThrCalculationMapping.FRM_FIELD_POSITION_ID], "formElemen", null, String.valueOf(thrMapping.getPositionId()), position_value, position_key, ""));
                rowx.add(ControlCombo.draw(frmObject.fieldNames[FrmThrCalculationMapping.FRM_FIELD_GRADE], "formElemen", null, String.valueOf(objEntity.getGrade()), grade_value, grade_key, ""));
                rowx.add("<input type=\"text\" name=\"EMP_FULLNAME\"  class=\"elemenForm\"> <input type=\"hidden\" name=\""+FrmThrCalculationMapping.fieldNames[FrmThrCalculationMapping.FRM_FIELD_EMPLOYEE_ID]+"\" value=\"\" class=\"formElemen\" >  <a href=\"javascript:cmdSearchEmp()\">add employee</a>");
                rowx.add(ControlCombo.draw(frmObject.fieldNames[FrmThrCalculationMapping.FRM_FIELD_PAYROLL_GROUP_ID], "formElemen", null, String.valueOf(thrMapping.getPayrollGroupId()), payGroup_value, payGroup_key, ""));
                
                rowx.add(""+ControlCombo.draw(frmObject.fieldNames[FrmThrCalculationMapping.FRM_FIELD_LOS_FROM_IN_DAY], "formElemen", null, String.valueOf(thrMapping.getLosFromInDay()), day_keyVal, day_keyVal, "")+" <br> "+ ControlCombo.draw(frmObject.fieldNames[FrmThrCalculationMapping.FRM_FIELD_LOS_FROM_IN_MONTH], "formElemen", null, String.valueOf(thrMapping.getLosFromInMonth()), month_keyVal, month_keyVal, "")+" <br> "+ ControlCombo.draw(frmObject.fieldNames[FrmThrCalculationMapping.FRM_FIELD_LOS_FROM_IN_YEAR], "formElemen", null, String.valueOf(thrMapping.getLosFromInYear()), year_keyVal, year_keyVal, ""));
                rowx.add(""+ControlCombo.draw(frmObject.fieldNames[FrmThrCalculationMapping.FRM_FIELD_LOS_TO_IN_DAY], "formElemen", null, String.valueOf(thrMapping.getLosToInDay()), day_keyVal, day_keyVal, "")+" <br> "+ ControlCombo.draw(frmObject.fieldNames[FrmThrCalculationMapping.FRM_FIELD_LOS_TO_IN_MONTH], "formElemen", null, String.valueOf(thrMapping.getLosToInMonth()), month_keyVal, month_keyVal, "")+" <br> "+ ControlCombo.draw(frmObject.fieldNames[FrmThrCalculationMapping.FRM_FIELD_LOS_TO_IN_YEAR], "formElemen", null, String.valueOf(thrMapping.getLosToInYear()), year_keyVal, year_keyVal, ""));
                rowx.add(""+ControlCombo.draw(frmObject.fieldNames[FrmThrCalculationMapping.FRM_FIELD_LOS_CURRENT_DATE], "formElemen", null, String.valueOf(thrMapping.getLosCurrentDate()), currentDate_value, currentDate_key, "")+" <br> "+"/ "+ControlDate.drawDateWithStyle(frmObject.fieldNames[FrmThrCalculationMapping.FRM_FIELD_LOS_PER_CURRENT_DATE], objEntity.getLosPerCurrentDate(), 0, -40, "formElemen"));
                
                            }
			}else{
                    //list company        
                    Vector listComp = PstCompany.list(0, 0, "", " COMPANY ");
                    Hashtable<String, String> compNames = new Hashtable();
                    compNames.put("0", "-");
                    for (int c = 0; c < listComp.size(); c++) {
                        Company comp = (Company) listComp.get(c);
                        compNames.put(""+comp.getOID(), comp.getCompany());                       
                    }
                     //division
                    Vector listDiv = PstDivision.list(0, 0, "", "");
                    Hashtable<String, String> divNames = new Hashtable();
                    divNames.put("0", "-");
                    for (int c = 0; c < listDiv.size(); c++) {
                        Division division = (Division) listDiv.get(c);
                        divNames.put(""+division.getOID(), division.getDivision());                       
                    }
                    //department
                    Vector listDept = PstDepartment.list(0, 0, "", "");
                    Hashtable<String, String> deptNames = new Hashtable();
                    deptNames.put("0", "-");
                    for (int c = 0; c < listDept.size(); c++) {
                        Department department = (Department) listDept.get(c);
                        deptNames.put(""+department.getOID(), department.getDepartment());                       
                    }
                   //section
                    Vector listSect = PstSection.list(0, 0, "", "");
                    Hashtable<String, String> sectNames = new Hashtable();
                    sectNames.put("0", "-");
                    for (int c = 0; c < listSect.size(); c++) {
                        Section section = (Section) listSect.get(c);
                        sectNames.put(""+section.getOID(), section.getSection());                       
                    }
                    
                    //level
                    Vector listLevel = PstLevel.list(0, 0, "", "");
                    Hashtable<String, String> levelNames = new Hashtable();
                    levelNames.put("0", "-");
                    for (int c = 0; c < listLev.size(); c++) {
                        Level level = (Level) listLev.get(c);
                        levelNames.put(""+level.getOID(), level.getLevel());                       
                    }
                    //marital
                    Vector listMaritalstatus = PstMarital.list(0, 0, "", "");
                    Hashtable<String, String> MaritalstatusNames = new Hashtable();
                    MaritalstatusNames.put("0", "-");
                    for (int c = 0; c < listMaritalstatus.size(); c++) {
                        Marital marital = (Marital) listMarital.get(c);
                        MaritalstatusNames.put(""+marital.getOID(), marital.getMaritalStatus());                       
                    }
                    //emp category
                    Vector listEmpCat = PstEmpCategory.list(0, 0, "", "");
                    Hashtable<String, String> EmpCatNames = new Hashtable();
                    EmpCatNames.put("0", "-");
                    for (int c = 0; c < listEmpCat.size(); c++) {
                        EmpCategory empCategory = (EmpCategory) listEmpCat.get(c);
                        EmpCatNames.put(""+empCategory.getOID(), empCategory.getEmpCategory());                       
                    }
                    //position
                    Vector listPosition = PstPosition.list(0, 0, "", "");
                    Hashtable<String, String> PositionNames = new Hashtable();
                    PositionNames.put("0", "-");
                    for (int c = 0; c < listposition.size(); c++) {
                        Position position = (Position) listPosition.get(c);
                        PositionNames.put(""+position.getOID(), position.getPosition());                       
                    }
                    
                    //position
                    Vector listGrade = PstGradeLevel.list(0, 0, "", "");
                    Hashtable<String, String> GradeNames = new Hashtable();
                    GradeNames.put("0", "-");
                    for (int c = 0; c < listGrade.size(); c++) {
                        GradeLevel gradeLevel = (GradeLevel) listGrade.get(c);
                        GradeNames.put(""+gradeLevel.getOID(), gradeLevel.getCodeLevel());                       
                    }
                    
                    Vector listEmp = PstEmployee.list(0, 0, "", "");
                   
                    Hashtable<String, String> empNames = new Hashtable();
                    empNames.put("0", "-");
                    for (int c = 0; c < listEmp.size(); c++) {
                        Employee emp = (Employee) listEmp.get(c);
                        empNames.put(""+emp.getOID(), emp.getFullName());                       
                    }
                    
                    //payrollGroup
                    Vector listsPayGroup = PstPayrollGroup.list(0, 0, "", "");
                    Hashtable<String, String> PayrollGroupNames = new Hashtable();
                    PayrollGroupNames.put("0", "-");
                    for (int c = 0; c < listsPayGroup.size(); c++) {
                        PayrollGroup payGroup = (PayrollGroup) listsPayGroup.get(c);
                        PayrollGroupNames.put(""+payGroup.getOID(), payGroup.getPayrollGroupName());                       
                    }
                    
                rowx.add("<a href=\"javascript:cmdEdit('" + String.valueOf(thrMapping.getOID()) + "')\"> " + (i+1) + "</a>");
                rowx.add(""+compNames.get(""+thrMapping.getCompanyId()));
                rowx.add(""+divNames.get(""+thrMapping.getDivisionId()));
                rowx.add(""+deptNames.get(""+thrMapping.getDepartmentId()));
                rowx.add(""+sectNames.get(""+thrMapping.getSectionId()));
                rowx.add(""+levelNames.get(""+thrMapping.getLevelId()));
                rowx.add(""+MaritalstatusNames.get(""+thrMapping.getMaritalId()));
                rowx.add(""+MaritalstatusNames.get(""+thrMapping.getTaxMaritalId()));
                rowx.add(""+EmpCatNames.get(""+thrMapping.getEmployeeCategory()));
                rowx.add(""+PositionNames.get(""+thrMapping.getPositionId()));
                rowx.add(""+GradeNames.get(""+thrMapping.getGrade()));
                rowx.add(""+empNames.get(""+thrMapping.getEmployeeId()));
                rowx.add(""+PayrollGroupNames.get(""+thrMapping.getPayrollGroupId()));
                rowx.add(""+thrMapping.getLosFromInDay()+"-Day/ "+thrMapping.getLosFromInMonth()+"-Month/ "+thrMapping.getLosFromInYear()+"-Year");
                rowx.add(""+thrMapping.getLosToInDay()+"-Day/ "+thrMapping.getLosToInMonth()+"-Month/ "+thrMapping.getLosToInYear()+"-Year");
                if (thrMapping.getLosCurrentDate()>0){
                    rowx.add("Current Date");
                } else {
                    rowx.add(""+(thrMapping.getLosPerCurrentDate() != null ? thrMapping.getLosPerCurrentDate() : "-"));
                }
                	} 
			lstData.add(rowx);
		}

		 rowx = new Vector();
 
                 
                  //company
                    Vector comp_value1 = new Vector(1, 1);
                    Vector comp_key1 = new Vector(1, 1);
                    Vector listComp1 = PstCompany.list(0, 0, "", " COMPANY ");
                    comp_value1.add(""+0);
                    comp_key1.add("select");
                    for (int c = 0; c < listComp1.size(); c++) {
                        Company comp1 = (Company) listComp1.get(c);
                        comp_key1.add(comp1.getCompany());
                        comp_value1.add(String.valueOf(comp1.getOID()));
                    }
                  //division
                    Vector div_value1 = new Vector(1, 1);
                    Vector div_key1 = new Vector(1, 1);
                    //update priska 2015-01-29
                       div_key1.add("-select-");
                        div_value1.add("0");
                        String strWhere1 = PstDivision.fieldNames[PstDivision.FLD_COMPANY_ID] + "=" +  "" + (objEntity.getCompanyId()) ;//oidCompany; 
                        Vector listDiv1 = PstDivision.list(0, 0, strWhere1, " DIVISION ");
                    
                        for (int c = 0; c < listDiv1.size(); c++) {
                        Division div1 = (Division) listDiv1.get(c);
                        div_key1.add(div1.getDivision());
                        div_value1.add(String.valueOf(div1.getOID()));
                        
                    }  
                
                  
                    Vector dept_value1 = new Vector(1, 1);
                    Vector dept_key1 = new Vector(1, 1);
                    dept_key1.add("-select-");
                    dept_value1.add("0");
                    String strWhereDept1 = PstDepartment.TBL_HR_DEPARTMENT+"."+PstDepartment.fieldNames[PstDepartment.FLD_DIVISION_ID] + "=" + "" + objEntity.getDivisionId(); //oidDivision;
                    Vector listDept1 = PstDepartment.list(0, 0, strWhereDept1, " DEPARTMENT ");
                    for (int d = 0; d < listDept1.size(); d++) {
                    Department dept1 = (Department) listDept1.get(d);
                    dept_key1.add(dept1.getDepartment());
                    dept_value1.add(String.valueOf(dept1.getOID()));
                    } 
                    
                    //section
                    Vector sec_value = new Vector(1, 1);
                    Vector sec_key = new Vector(1, 1);
                    sec_key.add("- select -");
                    sec_value.add("0");
                    String strWhereSec ="";
                    strWhereSec = PstSection.TBL_HR_SECTION+"."+PstSection.fieldNames[PstSection.FLD_DEPARTMENT_ID] + "=" + "" + objEntity.getDepartmentId(); 
                      Vector listSec = PstSection.list(0, 0, strWhereSec, "SECTION");
                    for (int s = 0; s< listSec.size(); s++) {
                        Section sec = (Section) listSec.get(s);
                        sec_key.add(sec.getSection());
                        sec_value.add(String.valueOf(sec.getOID()));
                    }
                  if (status == 2 || status == 0) {    
                    if(iCommand == Command.ADD || (iCommand == Command.SAVE && frmObject.errorSize() > 0)){   
                    rowx.add("<input type=\"hidden\" name=\"" + frmObject.fieldNames[FrmThrCalculationMapping.FRM_FIELD_MAPPING_TYPE] + "\" value=\"" + PstThrCalculationMapping.TYPE_EXCLUSION+ "\" size=\"40\" class=\"elemenForm\"> "
                        + "<input type=\"hidden\" name=\"" + frmObject.fieldNames[FrmThrCalculationMapping.FRM_FIELD_CALCULATION_MAIN_ID] + "\" value=\"" + oidMain+ "\" size=\"40\" class=\"elemenForm\">") ;  
                    rowx.add(ControlCombo.draw(frmObject.fieldNames[FrmThrCalculationMapping.FRM_FIELD_COMPANY_ID], "formElemen", null, String.valueOf(objEntity.getCompanyId()), comp_value1, comp_key1, "onChange=\"javascript:cmdUpdateCDDS()\""));
                    rowx.add(ControlCombo.draw(frmObject.fieldNames[FrmThrCalculationMapping.FRM_FIELD_DIVISION_ID], "formElemen", null, String.valueOf(objEntity.getDivisionId()), div_value1, div_key1, "onChange=\"javascript:cmdUpdateCDDS()\""));
                    rowx.add(ControlCombo.draw(frmObject.fieldNames[FrmThrCalculationMapping.FRM_FIELD_DEPARTMENT_ID], "formElemen", null, String.valueOf(objEntity.getDepartmentId()), dept_value1, dept_key1, "onChange=\"javascript:cmdUpdateCDDS()\""));
                    rowx.add(ControlCombo.draw(frmObject.fieldNames[FrmThrCalculationMapping.FRM_FIELD_SECTION_ID], "formElemen", null, String.valueOf(objEntity.getSectionId()), sec_value, sec_key, ""));

                    rowx.add(ControlCombo.draw(frmObject.fieldNames[FrmThrCalculationMapping.FRM_FIELD_LEVEL_ID], "formElemen", null, String.valueOf(objEntity.getLevelId()), level_value, level_key, ""));
                    rowx.add(ControlCombo.draw(frmObject.fieldNames[FrmThrCalculationMapping.FRM_FIELD_MARITAL_ID], "formElemen", null, String.valueOf(objEntity.getMaritalId()), marital_value, marital_key, ""));
                    rowx.add(ControlCombo.draw(frmObject.fieldNames[FrmThrCalculationMapping.FRM_FIELD_TAX_MARITAL_ID], "formElemen", null, String.valueOf(objEntity.getTaxMaritalId()), marital_value, marital_key, ""));
                    rowx.add(ControlCombo.draw(frmObject.fieldNames[FrmThrCalculationMapping.FRM_FIELD_EMPLOYEE_CATEGORY], "formElemen", null, String.valueOf(objEntity.getEmployeeCategory()), empcat_value, empcat_key, ""));
                    rowx.add(ControlCombo.draw(frmObject.fieldNames[FrmThrCalculationMapping.FRM_FIELD_POSITION_ID], "formElemen", null, String.valueOf(objEntity.getPositionId()), position_value, position_key, ""));
                    rowx.add(ControlCombo.draw(frmObject.fieldNames[FrmThrCalculationMapping.FRM_FIELD_GRADE], "formElemen", null, String.valueOf(objEntity.getGrade()), grade_value, grade_key, ""));
                    rowx.add("<input type=\"text\" name=\"EMP_FULLNAME\"  class=\"elemenForm\"> <input type=\"hidden\" name=\""+FrmThrCalculationMapping.fieldNames[FrmThrCalculationMapping.FRM_FIELD_EMPLOYEE_ID]+"\" value=\"\" class=\"formElemen\" > <a href=\"javascript:cmdSearchEmp()\">add employee</a>");
                    rowx.add(ControlCombo.draw(frmObject.fieldNames[FrmThrCalculationMapping.FRM_FIELD_PAYROLL_GROUP_ID], "formElemen", null, String.valueOf(objEntity.getPayrollGroupId()), payGroup_value, payGroup_key, ""));
                    
                    rowx.add(""+ControlCombo.draw(frmObject.fieldNames[FrmThrCalculationMapping.FRM_FIELD_LOS_FROM_IN_DAY], "formElemen", null, String.valueOf(objEntity.getLosFromInDay()), day_keyVal, day_keyVal, "")+" <br> "+ ControlCombo.draw(frmObject.fieldNames[FrmThrCalculationMapping.FRM_FIELD_LOS_FROM_IN_MONTH], "formElemen", null, String.valueOf(objEntity.getLosFromInMonth()), month_keyVal, month_keyVal, "")+" <br> "+ ControlCombo.draw(frmObject.fieldNames[FrmThrCalculationMapping.FRM_FIELD_LOS_FROM_IN_YEAR], "formElemen", null, String.valueOf(objEntity.getLosFromInYear()), year_keyVal, year_keyVal, ""));
                    rowx.add(""+ControlCombo.draw(frmObject.fieldNames[FrmThrCalculationMapping.FRM_FIELD_LOS_TO_IN_DAY], "formElemen", null, String.valueOf(objEntity.getLosToInDay()), day_keyVal, day_keyVal, "")+" <br> "+ ControlCombo.draw(frmObject.fieldNames[FrmThrCalculationMapping.FRM_FIELD_LOS_TO_IN_MONTH], "formElemen", null, String.valueOf(objEntity.getLosToInMonth()), month_keyVal, month_keyVal, "")+" <br> "+ ControlCombo.draw(frmObject.fieldNames[FrmThrCalculationMapping.FRM_FIELD_LOS_TO_IN_YEAR], "formElemen", null, String.valueOf(objEntity.getLosToInYear()), year_keyVal, year_keyVal, ""));
                    rowx.add(""+ControlCombo.draw(frmObject.fieldNames[FrmThrCalculationMapping.FRM_FIELD_LOS_CURRENT_DATE], "formElemen", null, String.valueOf(objEntity.getLosCurrentDate()), currentDate_value, currentDate_key, "")+" <br> "+"/ "+ControlDate.drawDateWithStyle(frmObject.fieldNames[FrmThrCalculationMapping.FRM_FIELD_LOS_PER_CURRENT_DATE], objEntity.getLosPerCurrentDate(), 0, -40, "formElemen"));


                    }
                }

		lstData.add(rowx);

		return ctrlist.draw();
	}
        
        public String drawListMapping(int iCommand,FrmThrCalculationMapping frmObject, ThrCalculationMapping objEntity, Vector objectClass,  long mappingId,  long oidMain, int status)

	{
		ControlList ctrlist = new ControlList();
		ctrlist.setAreaWidth("100%");
		ctrlist.setListStyle("tblStyle");
                ctrlist.setTitleStyle("title_tbl");
                ctrlist.setCellStyle("listgensell");
                ctrlist.setHeaderStyle("title_tbl");
                ctrlist.setCellSpacing("0");
                ctrlist.addHeader("No", "2%");
                //ctrlist.addHeader("Comp_Code", "7%");
                ctrlist.addHeader("Company", "8%");
                ctrlist.addHeader("Division", "8%");
                ctrlist.addHeader("Department", "8%");
                ctrlist.addHeader("Section", "8%");
                ctrlist.addHeader("Level", "8%");
                ctrlist.addHeader("Marital", "8%"); 
                ctrlist.addHeader("Tax Marital", "8%"); 
                ctrlist.addHeader("Employee Category", "8%"); 
                ctrlist.addHeader("Position", "8%");  
                ctrlist.addHeader("Grade", "8%"); 
                ctrlist.addHeader("Payroll Num", "8%");
                ctrlist.addHeader("Payroll Group", "8%");
                ctrlist.addHeader("LOS From", "8%");
                ctrlist.addHeader("LOS To", "8%");
                ctrlist.addHeader("LOS Current", "8%");
                ctrlist.addHeader("Value", "8%");
                ctrlist.addHeader("Propotional", "8%");
                ctrlist.setLinkRow(0);
		ctrlist.setLinkSufix("");
		Vector lstData = ctrlist.getData();
		Vector lstLinkData = ctrlist.getLinkData();
		Vector rowx = new Vector(1,1);
		ctrlist.reset();
		int index = -1;
                         
            
       
                    //level
                    Vector level_value = new Vector(1, 1);
                    Vector level_key = new Vector(1, 1);
                    level_key.add("- select -");
                        level_value.add("0");
                    Vector listLev = PstLevel.list(0, 0, null, null);
                    for (int s = 0; s< listLev.size(); s++) {
                        Level lev = (Level) listLev.get(s);
                        level_key.add(lev.getLevel());
                        level_value.add(String.valueOf(lev.getOID()));
                    }
                    
                    //day
                    Vector day_keyVal = new Vector(1, 1);
                    for (int s = 0; s < 32; s++) {
                        day_keyVal.add(""+s);
                    }
                    
                    //month
                    Vector month_keyVal = new Vector(1, 1);
                    for (int s = 0; s < 13; s++) {
                        month_keyVal.add(""+s);
                    }
                    
                    //year
                    Vector year_keyVal = new Vector(1, 1);
                    for (int s = 0; s < 13; s++) {
                        year_keyVal.add(""+s);
                    }
                    
                    
                    //current
                    Vector currentDate_value = new Vector(1, 1);
                    Vector currentDate_key = new Vector(1, 1);
                    currentDate_key.add("No");
                    currentDate_value.add("0");    
                    currentDate_key.add("Yes");
                    currentDate_value.add("1");
                    
                    //maarital
                    Vector marital_value = new Vector(1, 1);
                    Vector marital_key = new Vector(1, 1);
                    marital_key.add("- select -");
                    marital_value.add("0");
                    Vector listMarital = PstMarital.list(0, 0, null, null);
                    for (int s = 0; s< listMarital.size(); s++) {
                        Marital marital = (Marital) listMarital.get(s);
                        marital_key.add(marital.getMaritalCode());
                        marital_value.add(String.valueOf(marital.getOID()));
                    }
                    
                    
                     //emp_category
                    Vector empcat_value = new Vector(1, 1);
                    Vector empcat_key = new Vector(1, 1);
                    empcat_key.add("- select -");
                    empcat_value.add("0");
                    Vector listEmpcat = PstEmpCategory.list(0, 0, null, null);
                    for (int s = 0; s< listEmpcat.size(); s++) {
                        EmpCategory empcat = (EmpCategory) listEmpcat.get(s);
                        empcat_key.add(empcat.getEmpCategory());
                        empcat_value.add(String.valueOf(empcat.getOID()));
                    }
                    
                     //position
                    Vector position_value = new Vector(1, 1);
                    Vector position_key = new Vector(1, 1);
                    position_key.add("- select -");
                    position_value.add("0");
                    Vector listposition = PstPosition.list(0, 0, null, null);
                    for (int s = 0; s< listposition.size(); s++) {
                        Position position = (Position) listposition.get(s);
                        position_key.add(position.getPosition());
                        position_value.add(String.valueOf(position.getOID()));
                    }

                     //gradeLevel
                    Vector grade_value = new Vector(1, 1);
                    Vector grade_key = new Vector(1, 1);
                    grade_key.add("- select -");
                    grade_value.add("0");
                    Vector listGradeLevel = PstGradeLevel.list(0, 0, null, null);
                    for (int s = 0; s< listGradeLevel.size(); s++) {
                        GradeLevel gradeLevel = (GradeLevel) listGradeLevel.get(s);
                        grade_key.add(gradeLevel.getCodeLevel());
                        grade_value.add(String.valueOf(gradeLevel.getOID()));
                    }
                    
                    //payrollGroup
                    Vector payGroup_value = new Vector(1,1);
                    Vector payGroup_key = new Vector(1,1);
                    payGroup_key.add("- select -");
                    payGroup_value.add("0");
                    Vector listPayGroup = PstPayrollGroup.list(0, 0, null, null);
                    for (int s = 0; s< listPayGroup.size(); s++) {
                        PayrollGroup payGroup = (PayrollGroup) listPayGroup.get(s);
                        payGroup_key.add(payGroup.getPayrollGroupName());
                        payGroup_value.add(String.valueOf(payGroup.getOID()));
                    }
                    
                                                 
		for (int i = 0; i < objectClass.size(); i++) {
			 ThrCalculationMapping thrMapping = (ThrCalculationMapping)objectClass.get(i);
			 rowx = new Vector();
			 if(mappingId == thrMapping.getOID())
				 index = i; 
     
			 if(index == i && (iCommand == Command.EDIT || iCommand == Command.ASK)){
                             
                                 //company
                    Vector comp_value = new Vector(1, 1);
                    Vector comp_key = new Vector(1, 1);
                    Vector listComp = PstCompany.list(0, 0, "", " COMPANY ");
                    comp_value.add(""+0);
                    comp_key.add("select");
                    for (int c = 0; c < listComp.size(); c++) {
                        Company div = (Company) listComp.get(c);
                        comp_key.add(div.getCompany());
                        comp_value.add(String.valueOf(div.getOID()));
                    }
                
                  //division
                    Vector div_value = new Vector(1, 1);
                    Vector div_key = new Vector(1, 1);
                    //update priska 2015-01-29
                       div_key.add("-select-");
                        div_value.add("0");
                        String strWhere="";
                        if(objEntity.getOID()>0){
                        strWhere = PstDivision.fieldNames[PstDivision.FLD_COMPANY_ID] + "=" +  "" + objEntity.getCompanyId() ;//oidCompany; 
                        } else {
                        strWhere = PstDivision.fieldNames[PstDivision.FLD_COMPANY_ID] + "=" +  "" + thrMapping.getCompanyId() ;//oidCompany; 
                        }	
                        Vector listDiv = PstDivision.list(0, 0, strWhere, " DIVISION ");
                    
                        for (int c = 0; c < listDiv.size(); c++) {
                        Division div = (Division) listDiv.get(c);
                        div_key.add(div.getDivision());
                        div_value.add(String.valueOf(div.getOID()));
                        
                    }  
                
                  
                    Vector dept_value = new Vector(1, 1);
                    Vector dept_key = new Vector(1, 1);
                    dept_key.add("-select-");
                    dept_value.add("0");
                    String strWhereDept="";
                    if(objEntity.getOID()>0){	
                    strWhereDept = PstDepartment.TBL_HR_DEPARTMENT+"."+PstDepartment.fieldNames[PstDepartment.FLD_DIVISION_ID] + "=" + "" + objEntity.getDivisionId(); //oidDivision;
                    } else {
                    strWhereDept = PstDepartment.TBL_HR_DEPARTMENT+"."+PstDepartment.fieldNames[PstDepartment.FLD_DIVISION_ID] + "=" + "" + thrMapping.getDivisionId(); //oidDivision;
                    }
                    Vector listDept = PstDepartment.list(0, 0, strWhereDept, " DEPARTMENT ");
                    boolean adaDept=false;
                    for (int d = 0; d < listDept.size(); d++) {
                    Department dept = (Department) listDept.get(d);
                    dept_key.add(dept.getDepartment());
                    dept_value.add(String.valueOf(dept.getOID()));
                    } 
                  
                         //section
                    Vector sec_value = new Vector(1, 1);
                    Vector sec_key = new Vector(1, 1);
                    sec_key.add("- select -");
                    sec_value.add("0");
                    String strWhereSec ="";
                    if(objEntity.getOID()>0){	
                    strWhereSec = PstSection.TBL_HR_SECTION+"."+PstSection.fieldNames[PstSection.FLD_DEPARTMENT_ID] + "=" + "" + objEntity.getDepartmentId(); 
                    } else {
                    strWhereSec = PstSection.TBL_HR_SECTION+"."+PstSection.fieldNames[PstSection.FLD_DEPARTMENT_ID] + "=" + "" + thrMapping.getDepartmentId(); 
                    }
                    
                    Vector listSec = PstSection.list(0, 0, strWhereSec, "SECTION");
                    for (int s = 0; s< listSec.size(); s++) {
                        Section sec = (Section) listSec.get(s);
                        sec_key.add(sec.getSection());
                        sec_value.add(String.valueOf(sec.getOID()));
                    }         
                                       
                if(objEntity.getOID()>0){			

                rowx.add("<input type=\"hidden\" name=\"" + frmObject.fieldNames[FrmThrCalculationMapping.FRM_FIELD_MAPPING_TYPE] + "\" value=\"" + PstThrCalculationMapping.TYPE_MAPPING+ "\" size=\"40\" class=\"elemenForm\"> "
                        + "<input type=\"hidden\" name=\"" + frmObject.fieldNames[FrmThrCalculationMapping.FRM_FIELD_CALCULATION_MAIN_ID] + "\" value=\"" + oidMain+ "\" size=\"40\" class=\"elemenForm\">") ;  
                rowx.add(ControlCombo.draw(frmObject.fieldNames[FrmThrCalculationMapping.FRM_FIELD_COMPANY_ID], "formElemen", null, String.valueOf(objEntity.getCompanyId()), comp_value, comp_key, "onChange=\"javascript:cmdUpdateCDDS(1)\""));
                rowx.add(ControlCombo.draw(frmObject.fieldNames[FrmThrCalculationMapping.FRM_FIELD_DIVISION_ID], "formElemen", null, String.valueOf(objEntity.getDivisionId()), div_value, div_key, "onChange=\"javascript:cmdUpdateCDDS(1)\""));
                rowx.add(ControlCombo.draw(frmObject.fieldNames[FrmThrCalculationMapping.FRM_FIELD_DEPARTMENT_ID], "formElemen", null, String.valueOf(objEntity.getDepartmentId()), dept_value, dept_key, "onChange=\"javascript:cmdUpdateCDDS(1)\""));
                rowx.add(ControlCombo.draw(frmObject.fieldNames[FrmThrCalculationMapping.FRM_FIELD_SECTION_ID], "formElemen", null, String.valueOf(objEntity.getSectionId()), sec_value, sec_key, ""));
                 
                rowx.add(ControlCombo.draw(frmObject.fieldNames[FrmThrCalculationMapping.FRM_FIELD_LEVEL_ID], "formElemen", null, String.valueOf(objEntity.getLevelId()), level_value, level_key, ""));
                rowx.add(ControlCombo.draw(frmObject.fieldNames[FrmThrCalculationMapping.FRM_FIELD_MARITAL_ID], "formElemen", null, String.valueOf(objEntity.getMaritalId()), marital_value, marital_key, ""));
                rowx.add(ControlCombo.draw(frmObject.fieldNames[FrmThrCalculationMapping.FRM_FIELD_TAX_MARITAL_ID], "formElemen", null, String.valueOf(objEntity.getTaxMaritalId()), marital_value, marital_key, ""));
                rowx.add(ControlCombo.draw(frmObject.fieldNames[FrmThrCalculationMapping.FRM_FIELD_EMPLOYEE_CATEGORY], "formElemen", null, String.valueOf(objEntity.getEmployeeCategory()), empcat_value, empcat_key, ""));
                rowx.add(ControlCombo.draw(frmObject.fieldNames[FrmThrCalculationMapping.FRM_FIELD_POSITION_ID], "formElemen", null, String.valueOf(objEntity.getPositionId()), position_value, position_key, ""));
                rowx.add(ControlCombo.draw(frmObject.fieldNames[FrmThrCalculationMapping.FRM_FIELD_GRADE], "formElemen", null, String.valueOf(objEntity.getGrade()), grade_value, grade_key, ""));
                rowx.add("<input type=\"text\" name=\"EMP_FULLNAME\"  class=\"elemenForm\"> <input type=\"hidden\" name=\""+FrmThrCalculationMapping.fieldNames[FrmThrCalculationMapping.FRM_FIELD_EMPLOYEE_ID]+"\" value=\"\" class=\"formElemen\" >  <a href=\"javascript:cmdSearchEmp()\">add employee</a>");
                rowx.add(ControlCombo.draw(frmObject.fieldNames[FrmThrCalculationMapping.FRM_FIELD_PAYROLL_GROUP_ID], "formElemen", null, String.valueOf(objEntity.getPayrollGroupId()), payGroup_value, payGroup_key, ""));
                
                rowx.add(""+ControlCombo.draw(frmObject.fieldNames[FrmThrCalculationMapping.FRM_FIELD_LOS_FROM_IN_DAY], "formElemen", null, String.valueOf(objEntity.getLosFromInDay()), day_keyVal, day_keyVal, "")+" <br> "+ ControlCombo.draw(frmObject.fieldNames[FrmThrCalculationMapping.FRM_FIELD_LOS_FROM_IN_MONTH], "formElemen", null, String.valueOf(objEntity.getLosFromInMonth()), month_keyVal, month_keyVal, "")+" <br> "+ ControlCombo.draw(frmObject.fieldNames[FrmThrCalculationMapping.FRM_FIELD_LOS_FROM_IN_YEAR], "formElemen", null, String.valueOf(objEntity.getLosFromInYear()), year_keyVal, year_keyVal, ""));
                rowx.add(""+ControlCombo.draw(frmObject.fieldNames[FrmThrCalculationMapping.FRM_FIELD_LOS_TO_IN_DAY], "formElemen", null, String.valueOf(objEntity.getLosToInDay()), day_keyVal, day_keyVal, "")+" <br> "+ ControlCombo.draw(frmObject.fieldNames[FrmThrCalculationMapping.FRM_FIELD_LOS_TO_IN_MONTH], "formElemen", null, String.valueOf(objEntity.getLosToInMonth()), month_keyVal, month_keyVal, "")+" <br> "+ ControlCombo.draw(frmObject.fieldNames[FrmThrCalculationMapping.FRM_FIELD_LOS_TO_IN_YEAR], "formElemen", null, String.valueOf(objEntity.getLosToInYear()), year_keyVal, year_keyVal, ""));
                rowx.add(""+ControlCombo.draw(frmObject.fieldNames[FrmThrCalculationMapping.FRM_FIELD_LOS_CURRENT_DATE], "formElemen", null, String.valueOf(objEntity.getLosCurrentDate()), currentDate_value, currentDate_key, "")+" <br> "+"/ "+ControlDate.drawDateWithStyle(frmObject.fieldNames[FrmThrCalculationMapping.FRM_FIELD_LOS_PER_CURRENT_DATE], objEntity.getLosPerCurrentDate(), 0, -40, "formElemen"));
                rowx.add("<input type=\"text\" name=\"" + frmObject.fieldNames[FrmThrCalculationMapping.FRM_FIELD_VALUE] + "\" value=\"" + objEntity.getValue() + "\" size=\"40\" class=\"elemenForm\">");
                rowx.add("<input type=\"checkbox\" id=\"propotional\" name=\"" + frmObject.fieldNames[FrmThrCalculationMapping.FRM_FIELD_PROPOTIONAL] + "\" value=\"" + objEntity.getPropotional() + "\" "+(objEntity.getPropotional() != 0 ? "checked" : "")+">");
                } else{
                rowx.add("<input type=\"hidden\" name=\"" + frmObject.fieldNames[FrmThrCalculationMapping.FRM_FIELD_MAPPING_TYPE] + "\" value=\"" + PstThrCalculationMapping.TYPE_MAPPING+ "\" size=\"40\" class=\"elemenForm\"> "
                        + "<input type=\"hidden\" name=\"" + frmObject.fieldNames[FrmThrCalculationMapping.FRM_FIELD_CALCULATION_MAIN_ID] + "\" value=\"" + oidMain+ "\" size=\"40\" class=\"elemenForm\">") ;  
                rowx.add(ControlCombo.draw(frmObject.fieldNames[FrmThrCalculationMapping.FRM_FIELD_COMPANY_ID], "formElemen", null, String.valueOf(thrMapping.getCompanyId()), comp_value, comp_key, "onChange=\"javascript:cmdUpdateCDDS(1)\""));
                rowx.add(ControlCombo.draw(frmObject.fieldNames[FrmThrCalculationMapping.FRM_FIELD_DIVISION_ID], "formElemen", null, String.valueOf(thrMapping.getDivisionId()), div_value, div_key, "onChange=\"javascript:cmdUpdateCDDS(1)\""));
                rowx.add(ControlCombo.draw(frmObject.fieldNames[FrmThrCalculationMapping.FRM_FIELD_DEPARTMENT_ID], "formElemen", null, String.valueOf(thrMapping.getDepartmentId()), dept_value, dept_key, "onChange=\"javascript:cmdUpdateCDDS(1)\""));
                rowx.add(ControlCombo.draw(frmObject.fieldNames[FrmThrCalculationMapping.FRM_FIELD_SECTION_ID], "formElemen", null, String.valueOf(thrMapping.getSectionId()), sec_value, sec_key, ""));
                 
                rowx.add(ControlCombo.draw(frmObject.fieldNames[FrmThrCalculationMapping.FRM_FIELD_LEVEL_ID], "formElemen", null, String.valueOf(thrMapping.getLevelId()), level_value, level_key, ""));
                rowx.add(ControlCombo.draw(frmObject.fieldNames[FrmThrCalculationMapping.FRM_FIELD_MARITAL_ID], "formElemen", null, String.valueOf(thrMapping.getMaritalId()), marital_value, marital_key, ""));
                rowx.add(ControlCombo.draw(frmObject.fieldNames[FrmThrCalculationMapping.FRM_FIELD_TAX_MARITAL_ID], "formElemen", null, String.valueOf(thrMapping.getTaxMaritalId()), marital_value, marital_key, ""));
                rowx.add(ControlCombo.draw(frmObject.fieldNames[FrmThrCalculationMapping.FRM_FIELD_EMPLOYEE_CATEGORY], "formElemen", null, String.valueOf(thrMapping.getEmployeeCategory()), empcat_value, empcat_key, ""));
                rowx.add(ControlCombo.draw(frmObject.fieldNames[FrmThrCalculationMapping.FRM_FIELD_POSITION_ID], "formElemen", null, String.valueOf(thrMapping.getPositionId()), position_value, position_key, ""));
                rowx.add(ControlCombo.draw(frmObject.fieldNames[FrmThrCalculationMapping.FRM_FIELD_GRADE], "formElemen", null, String.valueOf(objEntity.getGrade()), grade_value, grade_key, ""));
                rowx.add("<input type=\"text\" name=\"EMP_FULLNAME\"  class=\"elemenForm\"> <input type=\"hidden\" name=\""+FrmThrCalculationMapping.fieldNames[FrmThrCalculationMapping.FRM_FIELD_EMPLOYEE_ID]+"\" value=\"\" class=\"formElemen\" >  <a href=\"javascript:cmdSearchEmp()\">add employee</a>");
                rowx.add(ControlCombo.draw(frmObject.fieldNames[FrmThrCalculationMapping.FRM_FIELD_PAYROLL_GROUP_ID], "formElemen", null, String.valueOf(thrMapping.getPayrollGroupId()), payGroup_value, payGroup_key, ""));
                
                rowx.add(""+ControlCombo.draw(frmObject.fieldNames[FrmThrCalculationMapping.FRM_FIELD_LOS_FROM_IN_DAY], "formElemen", null, String.valueOf(thrMapping.getLosFromInDay()), day_keyVal, day_keyVal, "")+" <br> "+ ControlCombo.draw(frmObject.fieldNames[FrmThrCalculationMapping.FRM_FIELD_LOS_FROM_IN_MONTH], "formElemen", null, String.valueOf(thrMapping.getLosFromInMonth()), month_keyVal, month_keyVal, "")+" <br> "+ ControlCombo.draw(frmObject.fieldNames[FrmThrCalculationMapping.FRM_FIELD_LOS_FROM_IN_YEAR], "formElemen", null, String.valueOf(thrMapping.getLosFromInYear()), year_keyVal, year_keyVal, ""));
                rowx.add(""+ControlCombo.draw(frmObject.fieldNames[FrmThrCalculationMapping.FRM_FIELD_LOS_TO_IN_DAY], "formElemen", null, String.valueOf(thrMapping.getLosToInDay()), day_keyVal, day_keyVal, "")+" <br> "+ ControlCombo.draw(frmObject.fieldNames[FrmThrCalculationMapping.FRM_FIELD_LOS_TO_IN_MONTH], "formElemen", null, String.valueOf(thrMapping.getLosToInMonth()), month_keyVal, month_keyVal, "")+" <br> "+ ControlCombo.draw(frmObject.fieldNames[FrmThrCalculationMapping.FRM_FIELD_LOS_TO_IN_YEAR], "formElemen", null, String.valueOf(thrMapping.getLosToInYear()), year_keyVal, year_keyVal, ""));
                rowx.add(""+ControlCombo.draw(frmObject.fieldNames[FrmThrCalculationMapping.FRM_FIELD_LOS_CURRENT_DATE], "formElemen", null, String.valueOf(thrMapping.getLosCurrentDate()), currentDate_value, currentDate_key, "")+" <br> "+"/ "+ControlDate.drawDateWithStyle(frmObject.fieldNames[FrmThrCalculationMapping.FRM_FIELD_LOS_PER_CURRENT_DATE], objEntity.getLosPerCurrentDate(), 0, -40, "formElemen"));
                rowx.add("<input type=\"text\" name=\"" + frmObject.fieldNames[FrmThrCalculationMapping.FRM_FIELD_VALUE] + "\" value=\"" + thrMapping.getValue() + "\" size=\"40\" class=\"elemenForm\">");
                rowx.add("<input type=\"checkbox\" id=\"propotional\" name=\"" + frmObject.fieldNames[FrmThrCalculationMapping.FRM_FIELD_PROPOTIONAL] + "\" value=\"" + objEntity.getPropotional() + "\" "+(objEntity.getPropotional() != 0 ? "checked" : "")+">");
                            }
			}else{
                    //list company        
                    Vector listComp = PstCompany.list(0, 0, "", " COMPANY ");
                    Hashtable<String, String> compNames = new Hashtable();
                    compNames.put("0", "-");
                    for (int c = 0; c < listComp.size(); c++) {
                        Company comp = (Company) listComp.get(c);
                        compNames.put(""+comp.getOID(), comp.getCompany());                       
                    }
                     //division
                    Vector listDiv = PstDivision.list(0, 0, "", "");
                    Hashtable<String, String> divNames = new Hashtable();
                    divNames.put("0", "-");
                    for (int c = 0; c < listDiv.size(); c++) {
                        Division division = (Division) listDiv.get(c);
                        divNames.put(""+division.getOID(), division.getDivision());                       
                    }
                    //department
                    Vector listDept = PstDepartment.list(0, 0, "", "");
                    Hashtable<String, String> deptNames = new Hashtable();
                    deptNames.put("0", "-");
                    for (int c = 0; c < listDept.size(); c++) {
                        Department department = (Department) listDept.get(c);
                        deptNames.put(""+department.getOID(), department.getDepartment());                       
                    }
                   //section
                    Vector listSect = PstSection.list(0, 0, "", "");
                    Hashtable<String, String> sectNames = new Hashtable();
                    sectNames.put("0", "-");
                    for (int c = 0; c < listSect.size(); c++) {
                        Section section = (Section) listSect.get(c);
                        sectNames.put(""+section.getOID(), section.getSection());                       
                    }
                    
                    //level
                    Vector listLevel = PstLevel.list(0, 0, "", "");
                    Hashtable<String, String> levelNames = new Hashtable();
                    levelNames.put("0", "-");
                    for (int c = 0; c < listLev.size(); c++) {
                        Level level = (Level) listLev.get(c);
                        levelNames.put(""+level.getOID(), level.getLevel());                       
                    }
                    //marital
                    Vector listMaritalstatus = PstMarital.list(0, 0, "", "");
                    Hashtable<String, String> MaritalstatusNames = new Hashtable();
                    MaritalstatusNames.put("0", "-");
                    for (int c = 0; c < listMaritalstatus.size(); c++) {
                        Marital marital = (Marital) listMarital.get(c);
                        MaritalstatusNames.put(""+marital.getOID(), marital.getMaritalStatus());                       
                    }
                    //emp category
                    Vector listEmpCat = PstEmpCategory.list(0, 0, "", "");
                    Hashtable<String, String> EmpCatNames = new Hashtable();
                    EmpCatNames.put("0", "-");
                    for (int c = 0; c < listEmpCat.size(); c++) {
                        EmpCategory empCategory = (EmpCategory) listEmpCat.get(c);
                        EmpCatNames.put(""+empCategory.getOID(), empCategory.getEmpCategory());                       
                    }
                    //position
                    Vector listPosition = PstPosition.list(0, 0, "", "");
                    Hashtable<String, String> PositionNames = new Hashtable();
                    PositionNames.put("0", "-");
                    for (int c = 0; c < listposition.size(); c++) {
                        Position position = (Position) listPosition.get(c);
                        PositionNames.put(""+position.getOID(), position.getPosition());                       
                    }
                    
                    //position
                    Vector listGrade = PstGradeLevel.list(0, 0, "", "");
                    Hashtable<String, String> GradeNames = new Hashtable();
                    GradeNames.put("0", "-");
                    for (int c = 0; c < listGrade.size(); c++) {
                        GradeLevel gradeLevel = (GradeLevel) listGrade.get(c);
                        GradeNames.put(""+gradeLevel.getOID(), gradeLevel.getCodeLevel());                       
                    }
                    
                    Vector listEmp = PstEmployee.list(0, 0, "", "");
                   
                    Hashtable<String, String> empNames = new Hashtable();
                    empNames.put("0", "-");
                    for (int c = 0; c < listEmp.size(); c++) {
                        Employee emp = (Employee) listEmp.get(c);
                        empNames.put(""+emp.getOID(), emp.getFullName());                       
                    }
                    
                    //payrollGroup
                    Vector listsPayGroup = PstPayrollGroup.list(0, 0, "", "");
                    Hashtable<String, String> PayrollGroupNames = new Hashtable();
                    PayrollGroupNames.put("0", "-");
                    for (int c = 0; c < listsPayGroup.size(); c++) {
                        PayrollGroup payGroup = (PayrollGroup) listsPayGroup.get(c);
                        PayrollGroupNames.put(""+payGroup.getOID(), payGroup.getPayrollGroupName());                       
                    }
                    
                rowx.add("<a href=\"javascript:cmdEdit('" + String.valueOf(thrMapping.getOID()) + "')\"> " + (i+1) + "</a>");
                rowx.add(""+compNames.get(""+thrMapping.getCompanyId()));
                rowx.add(""+divNames.get(""+thrMapping.getDivisionId()));
                rowx.add(""+deptNames.get(""+thrMapping.getDepartmentId()));
                rowx.add(""+sectNames.get(""+thrMapping.getSectionId()));
                rowx.add(""+levelNames.get(""+thrMapping.getLevelId()));
                rowx.add(""+MaritalstatusNames.get(""+thrMapping.getMaritalId()));
                rowx.add(""+MaritalstatusNames.get(""+thrMapping.getTaxMaritalId()));
                rowx.add(""+EmpCatNames.get(""+thrMapping.getEmployeeCategory()));
                rowx.add(""+PositionNames.get(""+thrMapping.getPositionId()));
                rowx.add(""+GradeNames.get(""+thrMapping.getGrade()));
                rowx.add(""+empNames.get(""+thrMapping.getEmployeeId()));
                rowx.add(""+PayrollGroupNames.get(""+thrMapping.getPayrollGroupId()));
                rowx.add(""+thrMapping.getLosFromInDay()+"-Day/ "+thrMapping.getLosFromInMonth()+"-Month/ "+thrMapping.getLosFromInYear()+"-Year");
                rowx.add(""+thrMapping.getLosToInDay()+"-Day/ "+thrMapping.getLosToInMonth()+"-Month/ "+thrMapping.getLosToInYear()+"-Year");
                if (thrMapping.getLosCurrentDate()>0){
                    rowx.add("Current Date");
                } else {
                    rowx.add(""+(thrMapping.getLosPerCurrentDate() != null ? thrMapping.getLosPerCurrentDate() : "-"));
                }
                rowx.add(""+thrMapping.getValue());
                rowx.add(""+(thrMapping.getPropotional() != 0 ? "Yes" : "No"));
                	} 
			lstData.add(rowx);
		}

		 rowx = new Vector();
 
                 
                  //company
                    Vector comp_value1 = new Vector(1, 1);
                    Vector comp_key1 = new Vector(1, 1);
                    Vector listComp1 = PstCompany.list(0, 0, "", " COMPANY ");
                    comp_value1.add(""+0);
                    comp_key1.add("select");
                    for (int c = 0; c < listComp1.size(); c++) {
                        Company comp1 = (Company) listComp1.get(c);
                        comp_key1.add(comp1.getCompany());
                        comp_value1.add(String.valueOf(comp1.getOID()));
                    }
                  //division
                    Vector div_value1 = new Vector(1, 1);
                    Vector div_key1 = new Vector(1, 1);
                    //update priska 2015-01-29
                       div_key1.add("-select-");
                        div_value1.add("0");
                        String strWhere1 = PstDivision.fieldNames[PstDivision.FLD_COMPANY_ID] + "=" +  "" + (objEntity.getCompanyId()) ;//oidCompany; 
                        Vector listDiv1 = PstDivision.list(0, 0, strWhere1, " DIVISION ");
                    
                        for (int c = 0; c < listDiv1.size(); c++) {
                        Division div1 = (Division) listDiv1.get(c);
                        div_key1.add(div1.getDivision());
                        div_value1.add(String.valueOf(div1.getOID()));
                        
                    }  
                
                  
                    Vector dept_value1 = new Vector(1, 1);
                    Vector dept_key1 = new Vector(1, 1);
                    dept_key1.add("-select-");
                    dept_value1.add("0");
                    String strWhereDept1 = PstDepartment.TBL_HR_DEPARTMENT+"."+PstDepartment.fieldNames[PstDepartment.FLD_DIVISION_ID] + "=" + "" + objEntity.getDivisionId(); //oidDivision;
                    Vector listDept1 = PstDepartment.list(0, 0, strWhereDept1, " DEPARTMENT ");
                    for (int d = 0; d < listDept1.size(); d++) {
                    Department dept1 = (Department) listDept1.get(d);
                    dept_key1.add(dept1.getDepartment());
                    dept_value1.add(String.valueOf(dept1.getOID()));
                    } 
                    
                    //section
                    Vector sec_value = new Vector(1, 1);
                    Vector sec_key = new Vector(1, 1);
                    sec_key.add("- select -");
                    sec_value.add("0");
                    String strWhereSec ="";
                    strWhereSec = PstSection.TBL_HR_SECTION+"."+PstSection.fieldNames[PstSection.FLD_DEPARTMENT_ID] + "=" + "" + objEntity.getDepartmentId(); 
                      Vector listSec = PstSection.list(0, 0, strWhereSec, "SECTION");
                    for (int s = 0; s< listSec.size(); s++) {
                        Section sec = (Section) listSec.get(s);
                        sec_key.add(sec.getSection());
                        sec_value.add(String.valueOf(sec.getOID()));
                    }
                      
                  if (status == 1 || status == 0) {    
                    if(iCommand == Command.ADD || (iCommand == Command.SAVE && frmObject.errorSize() > 0)){   
                    rowx.add("<input type=\"hidden\" name=\"" + frmObject.fieldNames[FrmThrCalculationMapping.FRM_FIELD_MAPPING_TYPE] + "\" value=\"" + PstThrCalculationMapping.TYPE_MAPPING+ "\" size=\"40\" class=\"elemenForm\"> "
                        + "<input type=\"hidden\" name=\"" + frmObject.fieldNames[FrmThrCalculationMapping.FRM_FIELD_CALCULATION_MAIN_ID] + "\" value=\"" + oidMain+ "\" size=\"40\" class=\"elemenForm\">") ;  
                    rowx.add(ControlCombo.draw(frmObject.fieldNames[FrmThrCalculationMapping.FRM_FIELD_COMPANY_ID], "formElemen", null, String.valueOf(objEntity.getCompanyId()), comp_value1, comp_key1, "onChange=\"javascript:cmdUpdateCDDS()\""));
                    rowx.add(ControlCombo.draw(frmObject.fieldNames[FrmThrCalculationMapping.FRM_FIELD_DIVISION_ID], "formElemen", null, String.valueOf(objEntity.getDivisionId()), div_value1, div_key1, "onChange=\"javascript:cmdUpdateCDDS()\""));
                    rowx.add(ControlCombo.draw(frmObject.fieldNames[FrmThrCalculationMapping.FRM_FIELD_DEPARTMENT_ID], "formElemen", null, String.valueOf(objEntity.getDepartmentId()), dept_value1, dept_key1, "onChange=\"javascript:cmdUpdateCDDS()\""));
                    rowx.add(ControlCombo.draw(frmObject.fieldNames[FrmThrCalculationMapping.FRM_FIELD_SECTION_ID], "formElemen", null, String.valueOf(objEntity.getSectionId()), sec_value, sec_key, ""));

                    rowx.add(ControlCombo.draw(frmObject.fieldNames[FrmThrCalculationMapping.FRM_FIELD_LEVEL_ID], "formElemen", null, String.valueOf(objEntity.getLevelId()), level_value, level_key, ""));
                    rowx.add(ControlCombo.draw(frmObject.fieldNames[FrmThrCalculationMapping.FRM_FIELD_MARITAL_ID], "formElemen", null, String.valueOf(objEntity.getMaritalId()), marital_value, marital_key, ""));
                    rowx.add(ControlCombo.draw(frmObject.fieldNames[FrmThrCalculationMapping.FRM_FIELD_TAX_MARITAL_ID], "formElemen", null, String.valueOf(objEntity.getTaxMaritalId()), marital_value, marital_key, ""));
                    rowx.add(ControlCombo.draw(frmObject.fieldNames[FrmThrCalculationMapping.FRM_FIELD_EMPLOYEE_CATEGORY], "formElemen", null, String.valueOf(objEntity.getEmployeeCategory()), empcat_value, empcat_key, ""));
                    rowx.add(ControlCombo.draw(frmObject.fieldNames[FrmThrCalculationMapping.FRM_FIELD_POSITION_ID], "formElemen", null, String.valueOf(objEntity.getPositionId()), position_value, position_key, ""));
                    rowx.add(ControlCombo.draw(frmObject.fieldNames[FrmThrCalculationMapping.FRM_FIELD_GRADE], "formElemen", null, String.valueOf(objEntity.getGrade()), grade_value, grade_key, ""));
                    rowx.add("<input type=\"text\" name=\"EMP_FULLNAME\"  class=\"elemenForm\"> <input type=\"hidden\" name=\""+FrmThrCalculationMapping.fieldNames[FrmThrCalculationMapping.FRM_FIELD_EMPLOYEE_ID]+"\" value=\"\" class=\"formElemen\" > <a href=\"javascript:cmdSearchEmp()\">add employee</a>");
                    rowx.add(ControlCombo.draw(frmObject.fieldNames[FrmThrCalculationMapping.FRM_FIELD_PAYROLL_GROUP_ID], "formElemen", null, String.valueOf(objEntity.getPayrollGroupId()), payGroup_value, payGroup_key, ""));
                    
                    rowx.add(""+ControlCombo.draw(frmObject.fieldNames[FrmThrCalculationMapping.FRM_FIELD_LOS_FROM_IN_DAY], "formElemen", null, String.valueOf(objEntity.getLosFromInDay()), day_keyVal, day_keyVal, "")+" <br> "+ ControlCombo.draw(frmObject.fieldNames[FrmThrCalculationMapping.FRM_FIELD_LOS_FROM_IN_MONTH], "formElemen", null, String.valueOf(objEntity.getLosFromInMonth()), month_keyVal, month_keyVal, "")+" <br> "+ ControlCombo.draw(frmObject.fieldNames[FrmThrCalculationMapping.FRM_FIELD_LOS_FROM_IN_YEAR], "formElemen", null, String.valueOf(objEntity.getLosFromInYear()), year_keyVal, year_keyVal, ""));
                    rowx.add(""+ControlCombo.draw(frmObject.fieldNames[FrmThrCalculationMapping.FRM_FIELD_LOS_TO_IN_DAY], "formElemen", null, String.valueOf(objEntity.getLosToInDay()), day_keyVal, day_keyVal, "")+" <br> "+ ControlCombo.draw(frmObject.fieldNames[FrmThrCalculationMapping.FRM_FIELD_LOS_TO_IN_MONTH], "formElemen", null, String.valueOf(objEntity.getLosToInMonth()), month_keyVal, month_keyVal, "")+" <br> "+ ControlCombo.draw(frmObject.fieldNames[FrmThrCalculationMapping.FRM_FIELD_LOS_TO_IN_YEAR], "formElemen", null, String.valueOf(objEntity.getLosToInYear()), year_keyVal, year_keyVal, ""));
                    rowx.add(""+ControlCombo.draw(frmObject.fieldNames[FrmThrCalculationMapping.FRM_FIELD_LOS_CURRENT_DATE], "formElemen", null, String.valueOf(objEntity.getLosCurrentDate()), currentDate_value, currentDate_key, "")+" <br> "+"/ "+ControlDate.drawDateWithStyle(frmObject.fieldNames[FrmThrCalculationMapping.FRM_FIELD_LOS_PER_CURRENT_DATE], objEntity.getLosPerCurrentDate(), 0, -40, "formElemen"));
                    rowx.add("<input type=\"text\" name=\"" + frmObject.fieldNames[FrmThrCalculationMapping.FRM_FIELD_VALUE] + "\" value=\"" + objEntity.getValue() + "\" size=\"40\" class=\"elemenForm\">");
                    rowx.add("<input type=\"checkbox\" id=\"propotional\" name=\"" + frmObject.fieldNames[FrmThrCalculationMapping.FRM_FIELD_PROPOTIONAL] + "\" value=\"" + objEntity.getPropotional() + "\" "+(objEntity.getPropotional() != 0 ? "checked" : "")+">");
                    }
                }

		lstData.add(rowx);

		return ctrlist.draw();
	}

%>
<%


                int losFromInDay   = 0;
                int losFromInMonth = 0;
                int losFromInYear  = 1;
                int losToInDay     = 0;
                int losToInMonth   = 0;
                int losToInYear    = 8;
                int losCurrentDate = 1;
                Date losPerCurrentDate = new Date();

                Employee employeex = new Employee();
                try {
                    employeex = PstEmployee.fetchExc(5044045l);
                } catch (Exception e) {}

                Date fromDate = new Date(employeex.getCommencingDate().getYear()+losFromInYear, employeex.getCommencingDate().getMonth()+losFromInMonth, employeex.getCommencingDate().getDate()+losFromInDay);
                Date toDate = new Date(employeex.getCommencingDate().getYear()+losToInYear, employeex.getCommencingDate().getMonth()+losToInMonth, employeex.getCommencingDate().getDate()+losToInDay);
                
                Date newDateDiff = new Date();
                
                boolean nilaitf = true;
                if (losCurrentDate == 1) {
                    newDateDiff = new Date();
                } else {
                    newDateDiff = losPerCurrentDate;
                }
                
                if (newDateDiff.before(fromDate)){
                    nilaitf = false;
                }
                if (newDateDiff.after(toDate)){
                    nilaitf = false;
                }

        
            CtrlThrCalculationMapping ctrlMapping = new CtrlThrCalculationMapping(request);
            long oidMapping = FRMQueryString.requestLong(request, "mapping_oid");
            long oidMain = FRMQueryString.requestLong(request, "oid_custom");
            int prevCommand = FRMQueryString.requestInt(request, "prev_command");
            int iCommand = FRMQueryString.requestCommand(request);
            int startMapping = FRMQueryString.requestInt(request, "startMapping");
            int startExclusion = FRMQueryString.requestInt(request, "startExclusion");
            // untuk status benefit=1 dan deduction = 2
            int status = FRMQueryString.requestInt(request, "status");

            int iErrCode = FRMMessage.ERR_NONE;
            String msgString = "";
            ControlLine ctrLine = new ControlLine();
            System.out.println("iCommand = " + iCommand);
            iErrCode = ctrlMapping.action(iCommand, oidMapping);
            msgString = ctrlMapping.getMessage();
            FrmThrCalculationMapping frmMapping = ctrlMapping.getForm();
            ThrCalculationMapping mapping = ctrlMapping.getThrCalculationMapping();
            oidMapping = mapping.getOID();

            /*variable declaration*/
            int recordToGet = 10;
            String whereClauseMapping = PstThrCalculationMapping.fieldNames[PstThrCalculationMapping.FLD_MAPPING_TYPE] + "=" + PstThrCalculationMapping.TYPE_MAPPING
                                        + " AND "+PstThrCalculationMapping.fieldNames[PstThrCalculationMapping.FLD_CALCULATION_MAIN_ID] + "=" + oidMain;
            String whereClauseExclusion = PstThrCalculationMapping.fieldNames[PstThrCalculationMapping.FLD_MAPPING_TYPE] + "=" + PstThrCalculationMapping.TYPE_EXCLUSION
                                        + " AND "+PstThrCalculationMapping.fieldNames[PstThrCalculationMapping.FLD_CALCULATION_MAIN_ID] + "=" + oidMain;
            
            Vector vListMapping = new Vector(1, 1);
            Vector vListExclusion = new Vector(1, 1);

            /*switch statement */
            iErrCode = ctrlMapping.action(iCommand, oidMapping);
            /* end switch*/

            /*count list All Language*/
            int vSizeMapping = PstThrCalculationMapping.getCount(whereClauseMapping);
            int vSizeExclusion = PstThrCalculationMapping.getCount(whereClauseExclusion);

            /*switch list Language*/
            if ((iCommand == Command.FIRST || iCommand == Command.PREV) ||
                    (iCommand == Command.NEXT || iCommand == Command.LAST)) {
                if (startMapping < 0) {
                    startMapping = 0;
                }
                if (startExclusion < 0) {
                    startExclusion = 0;
                }

                startMapping = ctrlMapping.actionList(iCommand, startMapping, vSizeMapping, recordToGet);
                startExclusion = ctrlMapping.actionList(iCommand, startExclusion, vSizeExclusion, recordToGet);
            }
            /* end switch list*/

            //PayExecutive payExecutive = ctrLanguage.getLanguage();
            msgString = ctrlMapping.getMessage();

            /* get record to display */
            if (startMapping < 0) {
                startMapping = 0;
            }
            vListMapping = PstThrCalculationMapping.list(startMapping, recordToGet, whereClauseMapping, "");

            if (startExclusion < 0) {
                startExclusion = 0;
            }
            vListExclusion = PstThrCalculationMapping.list(startExclusion, recordToGet, whereClauseExclusion, "");

            /*handle condition if size of record to display = 0 and start > 0 	after delete*/
            if (vListMapping.size() < 1 && startMapping > 0) {
                if (vSizeMapping - recordToGet > recordToGet) {
                    startMapping = startMapping - recordToGet;
                } //go to Command.PREV
                else {
                    startMapping = 0;
                    iCommand = Command.FIRST;
                    prevCommand = Command.FIRST; //go to Command.FIRST
                }
                if (startMapping < 0) {
                    startMapping = 0;
                }
                vListMapping = PstThrCalculationMapping.list(startMapping, recordToGet, whereClauseMapping, "");
            //listDeduction = PstPayComponent.list(startBenefit,recordToGet, whereClauseDeduction , orderClause);
            }

            if (vListExclusion.size() < 1 && startExclusion > 0) {
                if (vSizeExclusion - recordToGet > recordToGet) {
                    startExclusion = startExclusion - recordToGet;
                } //go to Command.PREV
                else {
                    startExclusion = 0;
                    iCommand = Command.FIRST;
                    prevCommand = Command.FIRST; //go to Command.FIRST
                }
                if (startExclusion < 0) {
                    startExclusion = 0;
                }

                vListExclusion = PstThrCalculationMapping.list(startExclusion, recordToGet, whereClauseExclusion, "");
            //listDeduction = PstPayComponent.list(startBenefit,recordToGet, whereClauseDeduction , orderClause);
            }

    
%>
<!DOCTYPE html>
<html>
    <!-- #BeginTemplate "/Templates/main.dwt" --> 
    <head>
        <!-- #BeginEditable "doctitle" --> 
        <title>HARISMA - Setup - Mapping Configuration</title>
        <!-- #EndEditable --> 
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
        <!-- #BeginEditable "styles" --> 
        <link rel="stylesheet" href="../../styles/main.css" type="text/css">
        <!-- #EndEditable --> <!-- #BeginEditable "stylestab" --> 
        <link rel="stylesheet" href="../../styles/tab.css" type="text/css">
        <!-- #EndEditable --> <!-- #BeginEditable "headerscript" --> 
<script language="JavaScript">
function cmdUpdateCDDS(n){
                document.frmthr.command.value="<%=String.valueOf(Command.GOTO)%>";
                document.frmthr.action="thr_mapping.jsp?n="+n+"";
                document.frmthr.submit();
            }

function cmdAdd(status){
	document.frmthr.status.value=status;
	document.frmthr.command.value="<%=Command.ADD%>";
        document.frmthr.prev_command.value="<%=prevCommand%>";
        document.frmthr.action="thr_mapping.jsp";
        document.frmthr.submit();
}
function cmdSearchEmp(){
        //emp_number = document.frmDocMasterFlow.EMP_NUMBER.value;
	window.open("<%=approot%>/employee/search/SearchMasterFlow.jsp?formName=frmthr&empPathId=<%=frmMapping.fieldNames[frmMapping.FRM_FIELD_EMPLOYEE_ID]%>", null, "height=550,width=600, status=yes,toolbar=no,menubar=no,location=no, scrollbars=yes");       
}
function cmdAsk(oidValue_Mapping){
	document.frmthr.Value_Mapping_oid.value=oidValue_Mapping;
	document.frmthr.command.value="<%=Command.ASK%>";
	document.frmthr.prev_command.value="<%=prevCommand%>";
	document.frmthr.action="value_mapping.jsp";
	document.frmthr.submit();
}

function cmdConfirmDelete(oidValue_Mapping){
	document.frmthr.Value_Mapping_oid.value=oidValue_Mapping;
	document.frmthr.command.value="<%=Command.DELETE%>";
	document.frmthr.prev_command.value="<%=prevCommand%>";
	document.frmthr.action="value_mapping.jsp";
	document.frmthr.submit();
}

function cmdSave(type){
    if (type==0){
        var c = document.getElementById('propotional')
        if (c.checked) document.frmthr.<%=FrmThrCalculationMapping.fieldNames[FrmThrCalculationMapping.FRM_FIELD_PROPOTIONAL]%>.value=1;
        else document.frmthr.<%=FrmThrCalculationMapping.fieldNames[FrmThrCalculationMapping.FRM_FIELD_PROPOTIONAL]%>.value=0;
    }
    document.frmthr.command.value="<%=Command.SAVE%>";
    document.frmthr.status.value="3";
    document.frmthr.prev_command.value="<%=prevCommand%>";
    document.frmthr.action="thr_mapping.jsp";
    document.frmthr.submit();
}

 function cmdEdit(oidMapping){
    document.frmthr.mapping_oid.value=oidMapping;
    document.frmthr.status.value="0";
    document.frmthr.command.value="<%=Command.EDIT%>";
    document.frmthr.prev_command.value="<%=prevCommand%>";
    document.frmthr.action="thr_mapping.jsp";
    document.frmthr.submit();
}

function cmdCancel(oidValue_Mapping){
	document.frmthr.Value_Mapping_oid.value=oidValue_Mapping;
	document.frmthr.command.value="<%=Command.EDIT%>";
	document.frmthr.prev_command.value="<%=prevCommand%>";
	document.frmthr.action="value_mapping.jsp";
	document.frmthr.submit();
}

function cmdBack(){
	document.frmthr.command.value="<%=Command.BACK%>";
	document.frmthr.action="thr_mapping.jsp";
	document.frmthr.submit();
}

function cmdListFirst(){
	document.frmthr.command.value="<%=Command.FIRST%>";
	document.frmthr.prev_command.value="<%=Command.FIRST%>";
	document.frmthr.action="value_mapping.jsp";
	document.frmthr.submit();
}

function cmdListPrev(){
	document.frmthr.command.value="<%=Command.PREV%>";
	document.frmthr.prev_command.value="<%=Command.PREV%>";
	document.frmthr.action="value_mapping.jsp";
	document.frmthr.submit();
}

function cmdListNext(){
	document.frmthr.command.value="<%=Command.NEXT%>";
	document.frmthr.prev_command.value="<%=Command.NEXT%>";
	document.frmthr.action="value_mapping.jsp";
	document.frmthr.submit();
}

function cmdListLast(){
	document.frmthr.command.value="<%=Command.LAST%>";
	document.frmthr.prev_command.value="<%=Command.LAST%>";
	document.frmthr.action="value_mapping.jsp";
	document.frmthr.submit();
}

function cmdListCateg(oidValue_Mapping){
	document.frmthr.Value_Mapping_oid.value=oidValue_Mapping;
	document.frmthr.command.value="<%=Command.LIST%>";
	document.frmthr.action="value_mapping.jsp";
	document.frmthr.submit();
}

function cmdBackMapping(){
        document.frmthr.command.value="<%=Command.BACK%>";
	document.frmthr.action="thr_calculation_setup.jsp";
	document.frmthr.submit();
}

function fnTrapKD(){
	//alert(event.keyCode);
	switch(event.keyCode) {
		case <%=LIST_PREV%>:
			cmdListPrev();
			break;
		case <%=LIST_NEXT%>:
			cmdListNext();
			break;
		case <%=LIST_FIRST%>:
			cmdListFirst();
			break;
		case <%=LIST_LAST%>:
			cmdListLast();
			break;
		default:
			break;
	}
}

//-------------- script control line -------------------
	function MM_swapImgRestore() { //v3.0
		var i,x,a=document.MM_sr; for(i=0;a&&i<a.length&&(x=a[i])&&x.oSrc;i++) x.src=x.oSrc;
	}

function MM_preloadImages() { //v3.0
		var d=document; if(d.images){ if(!d.MM_p) d.MM_p=new Array();
		var i,j=d.MM_p.length,a=MM_preloadImages.arguments; for(i=0; i<a.length; i++)
		if (a[i].indexOf("#")!=0){ d.MM_p[j]=new Image; d.MM_p[j++].src=a[i];}}
	}

function MM_findObj(n, d) { //v4.0
		var p,i,x;  if(!d) d=document; if((p=n.indexOf("?"))>0&&parent.frames.length) {
		d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}
		if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];
		for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=MM_findObj(n,d.layers[i].document);
		if(!x && document.getElementById) x=document.getElementById(n); return x;
	}

function MM_swapImage() { //v3.0
		var i,j=0,x,a=MM_swapImage.arguments; document.MM_sr=new Array; for(i=0;i<(a.length-2);i+=3)
		if ((x=MM_findObj(a[i]))!=null){document.MM_sr[j++]=x; if(!x.oSrc) x.oSrc=x.src; x.src=a[i+2];}
	}

</script>
        <style type="text/css">
            .tblStyle {border-collapse: collapse;font-size: 11px;}
            .tblStyle td {padding: 3px 5px; border: 1px solid #CCC;}
            .title_tbl {font-weight: bold;background-color: #DDD; color: #575757;}
            .title_page {color:#0db2e1; font-weight: bold; font-size: 14px; background-color: #EEE; border-left: 1px solid #0099FF; padding: 12px 18px;}
            .title_part {color:#FF7E00; background-color: #F7F7F7; border-left: 1px solid #0099FF; padding: 9px 11px;}
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
        </style>
        <!-- #EndEditable --> 
    </head>
    <body <%=noBack%> bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
        <table width="100%" border="0" cellspacing="0" cellpadding="0" height="100%" bgcolor="#F9FCFF" >
             <%if(headerStyle && !verTemplate.equalsIgnoreCase("0")){%> 
           <%@include file="../../styletemplate/template_header.jsp" %>
            <%}else{%>
            <tr> 
                <td ID="TOPTITLE" background="<%=approot%>/images/HRIS_HeaderBg3.jpg" width="100%" height="54"> 
                    <!-- #BeginEditable "header" --> 
                    <%@ include file = "../../main/header.jsp" %>
                <!-- #EndEditable --> </td>
            </tr>
            <tr> 
                <td  bgcolor="#9BC1FF" height="15" ID="MAINMENU" valign="middle"> <!-- #BeginEditable "menumain" --> 
                <%@ include file = "../../main/mnmain.jsp" %>
                <!-- #EndEditable --> </td>
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
                <td width="100%" valign="top" align="left"> 
                    <table width="100%" border="0" cellspacing="3" cellpadding="2">
                        <tr> 
                            <td width="100%"> 
                                <table width="100%" border="0" cellspacing="0" cellpadding="0">
                                    <tr> 
                                        <td height="20"><div class="title_page"><button id="btn" onclick="cmdBackMapping()">Back</button> Mapping Configuration</div></td>
                                    </tr>
                                    <tr> 
                                        <td> 
                                            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                                                <tr> 
                                                    <td  style="background-color:<%=bgColorContent%>; "> 
                                                        <table width="100%" border="0" cellspacing="1" cellpadding="1" class="tablecolor">
                                                            <tr> 
                                                                <td valign="top"> 
                                                                    <table style="border:1px solid <%=garisContent%>" width="100%" border="0" cellspacing="1" cellpadding="1" class="tabbg">
                                                                        <tr> 
                                                                            <td valign="top"> <!-- #BeginEditable "content" --> 
                                                                                <form name="frmthr" method="post" action="">
                                                                                    <input type="hidden" name="command" value="">
                                                                                    <input type="hidden" name="startMapping" value="<%=startMapping%>">
                                                                                    <input type="hidden" name="startExclusion" value="<%=startExclusion%>">
                                                                                    <input type="hidden" name="oid_custom" value="<%=oidMain%>">
                                                                                    <input type="hidden" name="mapping_oid" value="<%=oidMapping%>">
                                                                                    <input type="hidden" name="prev_command" value="<%=prevCommand%>">
                                                                                    <input type="hidden" name="status" value="<%=status%>">
                                                                                    <table width="100%" border="0" cellspacing="0" cellpadding="0">
                                                                                    <tr> <td>
                                                                                            <table width="100%" border="0" cellspacing="0" cellpadding="0">   
                                                                                                <tr align="left" valign="top"> 
                                                                                                    <td  align="left" height="14" valign="middle" colspan="2" class="listtitle"><div class="title_part">Layering List</div> </td>
                                                                                                </tr> 
                                                                                            </table>
                                                                                    </td></tr>
                                                                                    <%
            try {
                // out.println("listBenefit"+listBenefit.size());
                if ((vListMapping == null || vListMapping.size() < 1) && (iCommand == Command.NONE)) {
                    iCommand = Command.ADD;
                }

                                                                                    %>
                                                                                    <tr> 
                                                                                    <td width="24%" colspan="2">
                                                                                    <%= drawListMapping(iCommand, frmMapping, mapping, vListMapping, oidMapping,oidMain,status)%>
                                                                                    <table cellpadding="0" cellspacing="0" border="0" width="100%">
                                                                                        <%
            } catch (Exception exc) {
            }
                                                                                        %>
                                                                                        
                                                                                        <tr align="left" valign="top"> 
                                                                                            <td width="519">
                                                                                                
                                                                                                <table width="100%" height="68" border="0">
                                                                                                    
                                                                                                    <tr>
                                                                                                    
                                                                                                    <%
            int cmd = 0;
            if ((iCommand == Command.FIRST || iCommand == Command.PREV) ||
                    (iCommand == Command.NEXT || iCommand == Command.LAST)) {
                cmd = iCommand;
            } else {
                if (iCommand == Command.NONE || prevCommand == Command.NONE) {
                    cmd = Command.FIRST;
                } else {
                    cmd = prevCommand;
                }
            }
                                                                                                    %>
                                                                                                    <% ctrLine.setLocationImg(approot + "/images");
            ctrLine.initDefault();
                                                                                                    %>
                                                                                                    <%=ctrLine.drawImageListLimit(cmd, vSizeMapping, startMapping, recordToGet)%> 
                                                                                                    
                                                                                                    <%
            //	out.println("masukq"+Command.ADD) ;

            if ((iCommand != Command.ADD && iCommand != Command.ASK && iCommand != Command.EDIT) && (frmMapping.errorSize() < 1)) {
            %>    
                                                                                                    <tr> 
                                                                                                        <td width="150"><a href="javascript:cmdAdd(1)" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image261','','<%=approot%>/images/BtnNewOn.jpg',1)"><img name="Image261" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="Add new data"></a>
                                                                                                            <a href="javascript:cmdAdd(1)" class="command">Add 
                                                                                                        New Layering</a> </td>
                                                                                                    </tr>
                                                                                                    <%
            }%>
                                                                                                    <% if (status == 1 || status == 0) {
                if ((iCommand == Command.ADD) || (iCommand == Command.SAVE) && (frmMapping.errorSize() > 0) || (iCommand == Command.EDIT) || (iCommand == Command.ASK)) {%>
                                                                                                    <tr align="left" valign="top"> 
                                                                                                        <td >
                                                                                                            <a href="javascript:cmdSave(0)" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image261','','<%=approot%>/images/BtnSave.jpg',1)"><img name="Image261" border="0" src="<%=approot%>/images/BtnSave.jpg" width="24" height="24" alt="Save Data"></a>
                                                                                                            <a href="javascript:cmdSave(0)" class="command">Save Layering</a> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                                                                                            <img name="Image261" border="0" src="<%=approot%>/images/BtnDel.jpg" width="24" height="24" alt="Delete">
                                                                                                            <a href="javascript:cmdConfirmDelete()" class="command">Delete Layering</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                                                                                            <a href="javascript:cmdBack()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image261','','<%=approot%>/images/BtnBack.jpg',1)"><img name="Image261" border="0" src="<%=approot%>/images/BtnBack.jpg" width="24" height="24" alt="Back to List"></a>
                                                                                                        <a href="javascript:cmdBack()" class="command">Back to List</a> </td>
                                                                                                    </tr>
                                                                                                    <%}
            }%>
                                                                                                </table>
                                                                                            </td>
                                                                                        </tr>
                                                                                        <tr> 
                                                                                            <td  align="left" height="14" valign="middle" colspan="2" class="listtitle"><div class="title_part">Exclusion List</div> </td>
                                                                                        </tr>
                                                                                        <%
            try {
                // out.println("listBenefit"+listBenefit.size());
                if ((vListExclusion == null || vListExclusion.size() < 1) && (iCommand == Command.NONE)) {
                    iCommand = Command.ADD;
                }

                                                                                        %>
                                                                                        <tr> 
                                                                                            <td> 
                                                                                                <%= drawList(iCommand, frmMapping, mapping, vListExclusion, oidMapping,oidMain,status)%>
                                                                                                
                                                                                            </td>
                                                                                            <%
            } catch (Exception exc) {
            }
                                                                                            %>
                                                                                        </tr>
                                                                                    </table>
                                                                                    <table width="100%" border="0">
                                                                                        <tr>
                                                                                        <%
            cmd = 0;
            if ((iCommand == Command.FIRST || iCommand == Command.PREV) ||
                    (iCommand == Command.NEXT || iCommand == Command.LAST)) {
                cmd = iCommand;
            } else {
                if (iCommand == Command.NONE || prevCommand == Command.NONE) {
                    cmd = Command.FIRST;
                } else {
                    cmd = prevCommand;
                }
            }
                                                                                        %>
                                                                                        <% ctrLine.setLocationImg(approot + "/images");
            ctrLine.initDefault();
                                                                                        %>
                                                                                        <%=ctrLine.drawImageListLimit(cmd, vSizeExclusion, startExclusion, recordToGet)%> 
                                                                                        
                                                                                        <%
            //	out.println("masukq"+Command.ADD) ;
            if ((iCommand != Command.ADD && iCommand != Command.ASK && iCommand != Command.EDIT) && (frmMapping.errorSize() < 1)) {
            %>    
                                                                                        <tr> 
                                                                                            <td width="150" colspan="3"><a href="javascript:cmdAdd(2)" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image261','','<%=approot%>/images/BtnNewOn.jpg',1)"><img name="Image261" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="Add new data"></a>
                                                                                                <a href="javascript:cmdAdd(2)" class="command">Add 
                                                                                            New Exclusion</a> </td>
                                                                                        </tr>
                                                                                        <%
            }%>
                                                                                        <%if (status == 2 || status == 0) {
                if ((iCommand == Command.ADD) || (iCommand == Command.SAVE) && (frmMapping.errorSize() > 0) || (iCommand == Command.EDIT) || (iCommand == Command.ASK)) {%>
                                                                                        <tr align="left" valign="top"> 
                                                                                            <td>
                                                                                                <a href="javascript:cmdSave(1)" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image261','','<%=approot%>/images/BtnSave.jpg',1)"><img name="Image261" border="0" src="<%=approot%>/images/BtnSave.jpg" width="24" height="24" alt="Save Data"></a>
                                                                                                <a href="javascript:cmdSave(1)" class="command">Save Exclusion</a> &nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;
                                                                                                <img name="Image261" border="0" src="<%=approot%>/images/BtnDel.jpg" width="24" height="24" alt="Delete">
                                                                                                <a href="javascript:cmdConfirmDelete()" class="command">Delete Exclusion</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;
                                                                                                <a href="javascript:cmdBack()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image261','','<%=approot%>/images/BtnBack.jpg',1)"><img name="Image261" border="0" src="<%=approot%>/images/BtnBack.jpg" width="24" height="24" alt="Back to List"></a>
                                                                                            <a href="javascript:cmdBack()" class="command">Back to List</a> </td>
                                                                                        </tr>
                                                                                    </table>
                                                                                    <%}
            }%>
                                                                                </form>
                                                                            <!-- #EndEditable --> </td>
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
            <tr>
                <td>&nbsp;</td>
            </tr>
             <%if(headerStyle && !verTemplate.equalsIgnoreCase("0")){%>
            <tr>
                            <td valign="bottom">
                               <!-- untuk footer -->
                                <%@include file="../../footer.jsp" %>
                            </td>
                            
            </tr>
            <%}else{%>
            <tr> 
                <td colspan="2" height="20" > <!-- #BeginEditable "footer" --> 
      <%@ include file = "../../main/footer.jsp" %>
                <!-- #EndEditable --> </td>
            </tr>
            <%}%>
        </table>
                </td>
            </tr>
        </table>
    </body>
    <!-- #BeginEditable "script" --> <script language="JavaScript">
        var oBody = document.body;
        var oSuccess = oBody.attachEvent('onkeydown',fnTrapKD);
    </script>
    
    <!-- #EndEditable --> <!-- #EndTemplate -->
</html>