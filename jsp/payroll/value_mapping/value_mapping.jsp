
<%@page import="com.dimata.harisma.entity.payroll.value_mapping.Value_Mapping"%>
<%@page import="com.dimata.harisma.entity.payroll.value_mapping.PstValue_Mapping"%>
<%@page import="com.dimata.harisma.form.payroll.value_mapping.CtrlValue_Mapping"%>
<%@page import="com.dimata.harisma.form.payroll.value_mapping.FrmValue_Mapping"%>
<%@page import="java.util.Date"%>
<%@page import="com.dimata.gui.jsp.ControlLine"%>
<%@page import="com.dimata.qdep.form.FRMMessage"%>
<%@page import="com.dimata.qdep.form.FRMQueryString"%>
<%@page import="com.dimata.harisma.entity.masterdata.Position"%>
<%@page import="com.dimata.harisma.entity.masterdata.Section"%>
<%@page import="com.dimata.harisma.entity.masterdata.PstSection"%>
<%@page import="com.dimata.harisma.entity.masterdata.Level"%>
<%@page import="com.dimata.harisma.entity.masterdata.PstLevel"%>
<%@page import="com.dimata.harisma.entity.masterdata.Marital"%>
<%@page import="com.dimata.harisma.entity.masterdata.PstMarital"%>
<%@page import="com.dimata.harisma.entity.masterdata.PstEmpCategory"%>
<%@page import="com.dimata.harisma.entity.masterdata.EmpCategory"%>
<%@page import="com.dimata.harisma.entity.masterdata.PstPosition"%>
<%@page import="com.dimata.gui.jsp.ControlDate"%>
<%@page import="com.dimata.gui.jsp.ControlDate"%>
<%@page import="com.dimata.gui.jsp.ControlCombo"%>
<%@page import="com.dimata.harisma.entity.masterdata.Department"%>
<%@page import="com.dimata.harisma.entity.masterdata.PstDepartment"%>
<%@page import="com.dimata.harisma.entity.masterdata.Division"%>
<%@page import="com.dimata.harisma.entity.masterdata.PstDivision"%>
<%@page import="com.dimata.harisma.entity.masterdata.Company"%>
<%@page import="com.dimata.harisma.entity.masterdata.PstCompany"%>
<%@page import="com.dimata.util.Command"%>
<%@page import="java.util.Vector"%>
<%@page import="com.dimata.gui.jsp.ControlList"%>
<%@page import="com.dimata.harisma.entity.admin.AppObjInfo"%>
<%@page import="com.dimata.harisma.form.masterdata.FrmKecamatan"%>
<%@ page language="java" %>

<%@ page import = "java.util.*" %>
<!-- package wihita -->
<%@ page import = "com.dimata.util.*" %>
<!-- package qdep -->
<%@ page import = "com.dimata.gui.jsp.*" %>
<%@ page import = "com.dimata.qdep.form.*" %>

<%@ include file = "../../main/javainit.jsp" %>
<% int appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_PAYROLL, AppObjInfo.G2_PAYROLL_SETUP, AppObjInfo.OBJ_PAYROLL_SETUP_COMPONENT);%>
<%@ include file = "../../main/checkuser.jsp" %>
<%
/* Check privilege except VIEW, view is already checked on checkuser.jsp as basic access*/
//boolean privAdd=userSession.checkPrivilege(AppObjInfo.composeCode(appObjCode, AppObjInfo.COMMAND_ADD));
//boolean privUpdate=userSession.checkPrivilege(AppObjInfo.composeCode(appObjCode, AppObjInfo.COMMAND_UPDATE));
//boolean privDelete=userSession.checkPrivilege(AppObjInfo.composeCode(appObjCode, AppObjInfo.COMMAND_DELETE));
%>
<!-- Jsp Block -->
<%!
	public String drawList(int iCommand,FrmValue_Mapping frmObject, Value_Mapping objEntity, Vector objectClass,  long value_MappingId)

	{
		ControlList ctrlist = new ControlList();
		ctrlist.setAreaWidth("100%");
		ctrlist.setListStyle("listgen");
		ctrlist.setTitleStyle("listgentitle");
		ctrlist.setCellStyle("listgensell");
		ctrlist.setHeaderStyle("listgentitle");
                ctrlist.addHeader("No", "2%");
                //ctrlist.addHeader("Comp_Code", "7%");
                ctrlist.addHeader("Parameter", "8%");
                ctrlist.addHeader("Number Of Maps", "5%");
                ctrlist.addHeader("Start Date", "8%");
                ctrlist.addHeader("End Date", "8%");
                ctrlist.addHeader("Company", "8%");
                ctrlist.addHeader("Division", "8%");
                ctrlist.addHeader("Department", "8%");
                ctrlist.addHeader("Section", "8%");
                ctrlist.addHeader("Level", "8%");
                ctrlist.addHeader("Marital", "8%"); 
                ctrlist.addHeader("Tax Marital", "8%"); 
                ctrlist.addHeader("Length Of Service", "8%"); 
                ctrlist.addHeader("Employee Category", "8%"); 
                ctrlist.addHeader("Position", "8%");  
                ctrlist.addHeader("Grade", "8%"); 
                ctrlist.addHeader("Payroll Num", "8%");
                ctrlist.addHeader("GEO", "8%");
                ctrlist.addHeader("LOS from", "8%");
                ctrlist.addHeader("LOS to", "8%");
                ctrlist.addHeader("LOS Current", "8%");
                ctrlist.addHeader("Value", "8%");
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
                    
                                                 
		for (int i = 0; i < objectClass.size(); i++) {
			 Value_Mapping value_Mapping = (Value_Mapping)objectClass.get(i);
			 rowx = new Vector();
			 if(value_MappingId == value_Mapping.getOID())
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
                        if(objEntity.getValuemappingid()>0){
                        strWhere = PstDivision.fieldNames[PstDivision.FLD_COMPANY_ID] + "=" +  "" + objEntity.getCompany_id() ;//oidCompany; 
                        } else {
                        strWhere = PstDivision.fieldNames[PstDivision.FLD_COMPANY_ID] + "=" +  "" + value_Mapping.getCompany_id() ;//oidCompany; 
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
                    if(objEntity.getValuemappingid()>0){	
                    strWhereDept = PstDepartment.TBL_HR_DEPARTMENT+"."+PstDepartment.fieldNames[PstDepartment.FLD_DIVISION_ID] + "=" + "" + objEntity.getDivision_id(); //oidDivision;
                    } else {
                    strWhereDept = PstDepartment.TBL_HR_DEPARTMENT+"."+PstDepartment.fieldNames[PstDepartment.FLD_DIVISION_ID] + "=" + "" + value_Mapping.getDivision_id(); //oidDivision;
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
                    if(objEntity.getValuemappingid()>0){	
                    strWhereSec = PstSection.TBL_HR_SECTION+"."+PstSection.fieldNames[PstSection.FLD_DEPARTMENT_ID] + "=" + "" + objEntity.getDepartment_id(); 
                    } else {
                    strWhereSec = PstSection.TBL_HR_SECTION+"."+PstSection.fieldNames[PstSection.FLD_DEPARTMENT_ID] + "=" + "" + value_Mapping.getDepartment_id(); 
                    }
                    
                    Vector listSec = PstSection.list(0, 0, strWhereSec, "SECTION");
                    for (int s = 0; s< listSec.size(); s++) {
                        Section sec = (Section) listSec.get(s);
                        sec_key.add(sec.getSection());
                        sec_value.add(String.valueOf(sec.getOID()));
                    }         
                                       
                if(objEntity.getValuemappingid()>0){			

                rowx.add(" <input type=\"hidden\" name=\"" + frmObject.fieldNames[FrmValue_Mapping.FRM_FIELD_COMP_CODE] + "\" value=\"" + objEntity.getCompCode() + "\" size=\"12\" class=\"elemenForm\"> "+(i+1));             
                // rowx.add("<input type=\"text\" name=\"" + frmObject.fieldNames[FrmValue_Mapping.FRM_FIELD_COMP_CODE] + "\" value=\"" + objEntity.getCompCode() + "\" size=\"12\" class=\"elemenForm\">");
                rowx.add("<input type=\"text\" name=\"" + frmObject.fieldNames[FrmValue_Mapping.FRM_FIELD_PARAMETER] + "\" value=\"" + objEntity.getParameter() + "\" size=\"40\" class=\"elemenForm\">");
                rowx.add("<input type=\"text\" name=\"" + frmObject.fieldNames[FrmValue_Mapping.FRM_FIELD_NUMBER_OF_MAPS] + "\" value=\"" + objEntity.getNumber_of_map() + "\" size=\"40\" class=\"elemenForm\">");
                rowx.add(ControlDate.drawDateWithStyle(frmObject.fieldNames[FrmValue_Mapping.FRM_FIELD_START_DATE], objEntity.getStartdate(), 0, -40, "formElemen") +"*"+ frmObject.getErrorMsg(frmObject.FRM_FIELD_START_DATE));
                rowx.add(ControlDate.drawDateWithStyle(frmObject.fieldNames[FrmValue_Mapping.FRM_FIELD_END_DATE], objEntity.getEnddate(), 0, -40, "formElemen") +"*"+ frmObject.getErrorMsg(frmObject.FRM_FIELD_END_DATE));
                 
                rowx.add(ControlCombo.draw(frmObject.fieldNames[FrmValue_Mapping.FRM_FIELD_COMPANY_ID], "formElemen", null, String.valueOf(objEntity.getCompany_id()), comp_value, comp_key, "onChange=\"javascript:cmdUpdateCDDS(1)\""));
                rowx.add(ControlCombo.draw(frmObject.fieldNames[FrmValue_Mapping.FRM_FIELD_DIVISION_ID], "formElemen", null, String.valueOf(objEntity.getDivision_id()), div_value, div_key, "onChange=\"javascript:cmdUpdateCDDS(1)\""));
                rowx.add(ControlCombo.draw(frmObject.fieldNames[FrmValue_Mapping.FRM_FIELD_DEPARTMENT_ID], "formElemen", null, String.valueOf(objEntity.getDepartment_id()), dept_value, dept_key, "onChange=\"javascript:cmdUpdateCDDS(1)\""));
                rowx.add(ControlCombo.draw(frmObject.fieldNames[FrmValue_Mapping.FRM_FIELD_SECTION_ID], "formElemen", null, String.valueOf(objEntity.getSection_id()), sec_value, sec_key, ""));
                 
                rowx.add(ControlCombo.draw(frmObject.fieldNames[FrmValue_Mapping.FRM_FIELD_LEVEL_ID], "formElemen", null, String.valueOf(objEntity.getLevel_id()), level_value, level_key, ""));
                rowx.add(ControlCombo.draw(frmObject.fieldNames[FrmValue_Mapping.FRM_FIELD_MARITAL_ID], "formElemen", null, String.valueOf(objEntity.getMarital_id()), marital_value, marital_key, ""));
                rowx.add(ControlCombo.draw(frmObject.fieldNames[FrmValue_Mapping.FRM_FIELD_TAX_MARITAL_ID], "formElemen", null, String.valueOf(objEntity.getTaxMaritalId()), marital_value, marital_key, ""));
                rowx.add("<input type=\"text\" name=\"" + frmObject.fieldNames[FrmValue_Mapping.FRM_FIELD_LENGTH_OF_SERVICE] + "\" value=\"" + objEntity.getLength_of_service() + "\" size=\"40\" class=\"elemenForm\">");
                rowx.add(ControlCombo.draw(frmObject.fieldNames[FrmValue_Mapping.FRM_FIELD_EMPLOYEE_CATEGORY], "formElemen", null, String.valueOf(objEntity.getEmployee_category()), empcat_value, empcat_key, ""));
                rowx.add(ControlCombo.draw(frmObject.fieldNames[FrmValue_Mapping.FRM_FIELD_POSITION_ID], "formElemen", null, String.valueOf(objEntity.getPosition_id()), position_value, position_key, ""));
                rowx.add(ControlCombo.draw(frmObject.fieldNames[FrmValue_Mapping.FRM_FIELD_GRADE], "formElemen", null, String.valueOf(objEntity.getGrade()), grade_value, grade_key, ""));
                rowx.add("<input type=\"text\" name=\"EMP_FULLNAME\"  class=\"elemenForm\"> <input type=\"hidden\" name=\""+FrmValue_Mapping.fieldNames[FrmValue_Mapping.FRM_FIELD_EMPLOYEE_ID]+"\" value=\"\" class=\"formElemen\" >  <a href=\"javascript:cmdSearchEmp()\">add employee</a>");
                rowx.add("<input type=\"text\" name=\"geo_address_pmnt\" value=\"" + objEntity.getGeoAddressPmnt() + "\" size=\"40\" onClick=\"javascript:updateGeoAddressPmnt()\"><input type=\"hidden\" name=\"" + frmObject.fieldNames[FrmValue_Mapping.FRM_FIELD_ADDR_COUNTRY_ID] + "\" value=\"" + objEntity.getAddrCountryId() + "\"> <input type=\"hidden\" name=\"" + frmObject.fieldNames[FrmValue_Mapping.FRM_FIELD_ADDR_PROVINCE_ID] + "\" value=\"" + objEntity.getAddrProvinceId() + "\"> <input type=\"hidden\" name=\"" + frmObject.fieldNames[FrmValue_Mapping.FRM_FIELD_ADDR_REGENCY_ID] + "\" value=\"" + objEntity.getAddrRegencyId() + "\"> <input type=\"hidden\" name=\"" + frmObject.fieldNames[FrmValue_Mapping.FRM_FIELD_ADDR_SUBREGENCY_ID] + "\" value=\"" + objEntity.getAddrSubRegencyId() + "\"> ");
                
                rowx.add(""+ControlCombo.draw(frmObject.fieldNames[FrmValue_Mapping.FRM_FIELD_LOS_FROM_IN_DAY], "formElemen", null, String.valueOf(objEntity.getLosFromInDay()), day_keyVal, day_keyVal, "")+" <br> "+ ControlCombo.draw(frmObject.fieldNames[FrmValue_Mapping.FRM_FIELD_LOS_FROM_IN_MONTH], "formElemen", null, String.valueOf(objEntity.getLosFromInMonth()), month_keyVal, month_keyVal, "")+" <br> "+ ControlCombo.draw(frmObject.fieldNames[FrmValue_Mapping.FRM_FIELD_LOS_FROM_IN_YEAR], "formElemen", null, String.valueOf(objEntity.getLosFromInYear()), year_keyVal, year_keyVal, ""));
                rowx.add(""+ControlCombo.draw(frmObject.fieldNames[FrmValue_Mapping.FRM_FIELD_LOS_TO_IN_DAY], "formElemen", null, String.valueOf(objEntity.getLosToInDay()), day_keyVal, day_keyVal, "")+" <br> "+ ControlCombo.draw(frmObject.fieldNames[FrmValue_Mapping.FRM_FIELD_LOS_TO_IN_MONTH], "formElemen", null, String.valueOf(objEntity.getLosToInMonth()), month_keyVal, month_keyVal, "")+" <br> "+ ControlCombo.draw(frmObject.fieldNames[FrmValue_Mapping.FRM_FIELD_LOS_TO_IN_YEAR], "formElemen", null, String.valueOf(objEntity.getLosToInYear()), year_keyVal, year_keyVal, ""));
                rowx.add(""+ControlCombo.draw(frmObject.fieldNames[FrmValue_Mapping.FRM_FIELD_LOS_CURRENT_DATE], "formElemen", null, String.valueOf(objEntity.getLosCurrentDate()), currentDate_value, currentDate_key, "")+" <br> "+"/ "+ControlDate.drawDateWithStyle(frmObject.fieldNames[FrmValue_Mapping.FRM_FIELD_LOS_PER_CURRENT_DATE], objEntity.getLosPerCurrentDate(), 0, -40, "formElemen"));
                
                rowx.add("<input type=\"text\" name=\"" + frmObject.fieldNames[FrmValue_Mapping.FRM_FIELD_VALUE] + "\" value=\"" + objEntity.getValue() + "\" size=\"40\" class=\"elemenForm\">");
                
                } else{
                rowx.add(" <input type=\"hidden\" name=\"" + frmObject.fieldNames[FrmValue_Mapping.FRM_FIELD_COMP_CODE] + "\" value=\"" + objEntity.getCompCode() + "\" size=\"12\" class=\"elemenForm\"> "+(i+1));             
                // rowx.add("<input type=\"text\" disabled=\"disabled\" name=\"" + frmObject.fieldNames[FrmValue_Mapping.FRM_FIELD_COMP_CODE] + "\" value=\"" + value_Mapping.getCompCode() + "\" size=\"12\" class=\"elemenForm\">");
                rowx.add("<input type=\"text\" name=\"" + frmObject.fieldNames[FrmValue_Mapping.FRM_FIELD_PARAMETER] + "\" value=\"" + value_Mapping.getParameter() + "\" size=\"40\" class=\"elemenForm\">");
                rowx.add("<input type=\"text\" name=\"" + frmObject.fieldNames[FrmValue_Mapping.FRM_FIELD_NUMBER_OF_MAPS] + "\" value=\"" + value_Mapping.getNumber_of_map() + "\" size=\"40\" class=\"elemenForm\">");
                rowx.add(ControlDate.drawDateWithStyle(frmObject.fieldNames[FrmValue_Mapping.FRM_FIELD_START_DATE], value_Mapping.getStartdate(), 0, -40, "formElemen") +"*"+ frmObject.getErrorMsg(frmObject.FRM_FIELD_START_DATE));
                rowx.add(ControlDate.drawDateWithStyle(frmObject.fieldNames[FrmValue_Mapping.FRM_FIELD_END_DATE], value_Mapping.getEnddate(), 0, -40, "formElemen") +"*"+ frmObject.getErrorMsg(frmObject.FRM_FIELD_END_DATE));
                 
                rowx.add(ControlCombo.draw(frmObject.fieldNames[FrmValue_Mapping.FRM_FIELD_COMPANY_ID], "formElemen", null, String.valueOf(value_Mapping.getCompany_id()), comp_value, comp_key, "onChange=\"javascript:cmdUpdateCDDS(1)\""));
                rowx.add(ControlCombo.draw(frmObject.fieldNames[FrmValue_Mapping.FRM_FIELD_DIVISION_ID], "formElemen", null, String.valueOf(value_Mapping.getDivision_id()), div_value, div_key, "onChange=\"javascript:cmdUpdateCDDS(1)\""));
                rowx.add(ControlCombo.draw(frmObject.fieldNames[FrmValue_Mapping.FRM_FIELD_DEPARTMENT_ID], "formElemen", null, String.valueOf(value_Mapping.getDepartment_id()), dept_value, dept_key, "onChange=\"javascript:cmdUpdateCDDS(1)\""));
                rowx.add(ControlCombo.draw(frmObject.fieldNames[FrmValue_Mapping.FRM_FIELD_SECTION_ID], "formElemen", null, String.valueOf(value_Mapping.getSection_id()), sec_value, sec_key, ""));
                 
                rowx.add(ControlCombo.draw(frmObject.fieldNames[FrmValue_Mapping.FRM_FIELD_LEVEL_ID], "formElemen", null, String.valueOf(value_Mapping.getLevel_id()), level_value, level_key, ""));
                rowx.add(ControlCombo.draw(frmObject.fieldNames[FrmValue_Mapping.FRM_FIELD_MARITAL_ID], "formElemen", null, String.valueOf(value_Mapping.getMarital_id()), marital_value, marital_key, ""));
                rowx.add(ControlCombo.draw(frmObject.fieldNames[FrmValue_Mapping.FRM_FIELD_TAX_MARITAL_ID], "formElemen", null, String.valueOf(value_Mapping.getTaxMaritalId()), marital_value, marital_key, ""));
                rowx.add("<input type=\"text\" name=\"" + frmObject.fieldNames[FrmValue_Mapping.FRM_FIELD_LENGTH_OF_SERVICE] + "\" value=\"" + value_Mapping.getLength_of_service() + "\" size=\"40\" class=\"elemenForm\">");
                rowx.add(ControlCombo.draw(frmObject.fieldNames[FrmValue_Mapping.FRM_FIELD_EMPLOYEE_CATEGORY], "formElemen", null, String.valueOf(value_Mapping.getEmployee_category()), empcat_value, empcat_key, ""));
                rowx.add(ControlCombo.draw(frmObject.fieldNames[FrmValue_Mapping.FRM_FIELD_POSITION_ID], "formElemen", null, String.valueOf(value_Mapping.getPosition_id()), position_value, position_key, ""));
                rowx.add(ControlCombo.draw(frmObject.fieldNames[FrmValue_Mapping.FRM_FIELD_GRADE], "formElemen", null, String.valueOf(objEntity.getGrade()), grade_value, grade_key, ""));
                rowx.add("<input type=\"text\" name=\"EMP_FULLNAME\"  class=\"elemenForm\"> <input type=\"hidden\" name=\""+FrmValue_Mapping.fieldNames[FrmValue_Mapping.FRM_FIELD_EMPLOYEE_ID]+"\" value=\"\" class=\"formElemen\" >  <a href=\"javascript:cmdSearchEmp()\">add employee</a>");
                rowx.add("<input type=\"text\" name=\"geo_address_pmnt\" value=\"" + value_Mapping.getGeoAddressPmnt() + "\" size=\"40\" onClick=\"javascript:updateGeoAddressPmnt()\"><input type=\"hidden\" name=\"" + frmObject.fieldNames[FrmValue_Mapping.FRM_FIELD_ADDR_COUNTRY_ID] + "\" value=\"" + value_Mapping.getAddrCountryId() + "\"> <input type=\"hidden\" name=\"" + frmObject.fieldNames[FrmValue_Mapping.FRM_FIELD_ADDR_PROVINCE_ID] + "\" value=\"" + value_Mapping.getAddrProvinceId() + "\"> <input type=\"hidden\" name=\"" + frmObject.fieldNames[FrmValue_Mapping.FRM_FIELD_ADDR_REGENCY_ID] + "\" value=\"" + value_Mapping.getAddrRegencyId() + "\"> <input type=\"hidden\" name=\"" + frmObject.fieldNames[FrmValue_Mapping.FRM_FIELD_ADDR_SUBREGENCY_ID] + "\" value=\"" + value_Mapping.getAddrSubRegencyId() + "\"> ");
                
                rowx.add(""+ControlCombo.draw(frmObject.fieldNames[FrmValue_Mapping.FRM_FIELD_LOS_FROM_IN_DAY], "formElemen", null, String.valueOf(value_Mapping.getLosFromInDay()), day_keyVal, day_keyVal, "")+" <br> "+ ControlCombo.draw(frmObject.fieldNames[FrmValue_Mapping.FRM_FIELD_LOS_FROM_IN_MONTH], "formElemen", null, String.valueOf(value_Mapping.getLosFromInMonth()), month_keyVal, month_keyVal, "")+" <br> "+ ControlCombo.draw(frmObject.fieldNames[FrmValue_Mapping.FRM_FIELD_LOS_FROM_IN_YEAR], "formElemen", null, String.valueOf(value_Mapping.getLosFromInYear()), year_keyVal, year_keyVal, ""));
                rowx.add(""+ControlCombo.draw(frmObject.fieldNames[FrmValue_Mapping.FRM_FIELD_LOS_TO_IN_DAY], "formElemen", null, String.valueOf(value_Mapping.getLosToInDay()), day_keyVal, day_keyVal, "")+" <br> "+ ControlCombo.draw(frmObject.fieldNames[FrmValue_Mapping.FRM_FIELD_LOS_TO_IN_MONTH], "formElemen", null, String.valueOf(value_Mapping.getLosToInMonth()), month_keyVal, month_keyVal, "")+" <br> "+ ControlCombo.draw(frmObject.fieldNames[FrmValue_Mapping.FRM_FIELD_LOS_TO_IN_YEAR], "formElemen", null, String.valueOf(value_Mapping.getLosToInYear()), year_keyVal, year_keyVal, ""));
                rowx.add(""+ControlCombo.draw(frmObject.fieldNames[FrmValue_Mapping.FRM_FIELD_LOS_CURRENT_DATE], "formElemen", null, String.valueOf(value_Mapping.getLosCurrentDate()), currentDate_value, currentDate_key, "")+" <br> "+"/ "+ControlDate.drawDateWithStyle(frmObject.fieldNames[FrmValue_Mapping.FRM_FIELD_LOS_PER_CURRENT_DATE], objEntity.getLosPerCurrentDate(), 0, -40, "formElemen"));
                
                
                rowx.add("<input type=\"text\" name=\"" + frmObject.fieldNames[FrmValue_Mapping.FRM_FIELD_VALUE] + "\" value=\"" + value_Mapping.getValue() + "\" size=\"40\" class=\"elemenForm\">");
                
                            }
			}else{
                    //list company        
                    Vector listComp = PstCompany.list(0, 0, "", " COMPANY ");
                    Hashtable<String, String> compNames = new Hashtable();
                    for (int c = 0; c < listComp.size(); c++) {
                        Company comp = (Company) listComp.get(c);
                        compNames.put(""+comp.getOID(), comp.getCompany());                       
                    }
                     //division
                    Vector listDiv = PstDivision.list(0, 0, "", "");
                    Hashtable<String, String> divNames = new Hashtable();
                    for (int c = 0; c < listDiv.size(); c++) {
                        Division division = (Division) listDiv.get(c);
                        divNames.put(""+division.getOID(), division.getDivision());                       
                    }
                    //department
                    Vector listDept = PstDepartment.list(0, 0, "", "");
                    Hashtable<String, String> deptNames = new Hashtable();
                    for (int c = 0; c < listDept.size(); c++) {
                        Department department = (Department) listDept.get(c);
                        deptNames.put(""+department.getOID(), department.getDepartment());                       
                    }
                   //section
                    Vector listSect = PstSection.list(0, 0, "", "");
                    Hashtable<String, String> sectNames = new Hashtable();
                    for (int c = 0; c < listSect.size(); c++) {
                        Section section = (Section) listSect.get(c);
                        sectNames.put(""+section.getOID(), section.getSection());                       
                    }
                    
                    //level
                    Vector listLevel = PstLevel.list(0, 0, "", "");
                    Hashtable<String, String> levelNames = new Hashtable();
                    for (int c = 0; c < listLev.size(); c++) {
                        Level level = (Level) listLev.get(c);
                        levelNames.put(""+level.getOID(), level.getLevel());                       
                    }
                    //marital
                    Vector listMaritalstatus = PstMarital.list(0, 0, "", "");
                    Hashtable<String, String> MaritalstatusNames = new Hashtable();
                    for (int c = 0; c < listMaritalstatus.size(); c++) {
                        Marital marital = (Marital) listMarital.get(c);
                        MaritalstatusNames.put(""+marital.getOID(), marital.getMaritalStatus());                       
                    }
                    //emp category
                    Vector listEmpCat = PstEmpCategory.list(0, 0, "", "");
                    Hashtable<String, String> EmpCatNames = new Hashtable();
                    for (int c = 0; c < listEmpCat.size(); c++) {
                        EmpCategory empCategory = (EmpCategory) listEmpCat.get(c);
                        EmpCatNames.put(""+empCategory.getOID(), empCategory.getEmpCategory());                       
                    }
                    //position
                    Vector listPosition = PstPosition.list(0, 0, "", "");
                    Hashtable<String, String> PositionNames = new Hashtable();
                    for (int c = 0; c < listposition.size(); c++) {
                        Position position = (Position) listPosition.get(c);
                        PositionNames.put(""+position.getOID(), position.getPosition());                       
                    }
                    
                    //position
                    Vector listGrade = PstGradeLevel.list(0, 0, "", "");
                    Hashtable<String, String> GradeNames = new Hashtable();
                    for (int c = 0; c < listGrade.size(); c++) {
                        GradeLevel gradeLevel = (GradeLevel) listGrade.get(c);
                        GradeNames.put(""+gradeLevel.getOID(), gradeLevel.getCodeLevel());                       
                    }
                    
                    Vector listEmp = PstEmployee.list(0, 0, "", "");
                   
                    Hashtable<String, String> empNames = new Hashtable();
                    for (int c = 0; c < listEmp.size(); c++) {
                        Employee emp = (Employee) listEmp.get(c);
                        empNames.put(""+emp.getOID(), emp.getFullName());                       
                    }
                    
                String geo = PstValue_Mapping.GetGeoAddress(value_Mapping);           
                rowx.add("<a href=\"javascript:cmdEdit('" + String.valueOf(value_Mapping.getOID()) + "')\"> " + (i+1) + "</a>");
                rowx.add(""+value_Mapping.getParameter());
                rowx.add(""+value_Mapping.getNumber_of_map());
                rowx.add(""+value_Mapping.getStartdate());
                rowx.add(""+value_Mapping.getEnddate());
                rowx.add(""+compNames.get(""+value_Mapping.getCompany_id()));
                rowx.add(""+divNames.get(""+value_Mapping.getDivision_id()));
                rowx.add(""+deptNames.get(""+value_Mapping.getDepartment_id()));
                rowx.add(""+sectNames.get(""+value_Mapping.getSection_id()));
                rowx.add(""+levelNames.get(""+value_Mapping.getLevel_id()));
                rowx.add(""+MaritalstatusNames.get(""+value_Mapping.getMarital_id()));
                rowx.add(""+MaritalstatusNames.get(""+value_Mapping.getTaxMaritalId()));
                rowx.add(""+value_Mapping.getLength_of_service());
                rowx.add(""+EmpCatNames.get(""+value_Mapping.getEmployee_category()));
                rowx.add(""+PositionNames.get(""+value_Mapping.getPosition_id()));
                rowx.add(""+GradeNames.get(""+value_Mapping.getGrade()));
                rowx.add(""+empNames.get(""+value_Mapping.getEmployee_id()));
                rowx.add("" + geo );
                rowx.add(""+value_Mapping.getLosFromInDay()+"-Day/ "+value_Mapping.getLosFromInMonth()+"-Month/ "+value_Mapping.getLosFromInYear()+"-Year");
                rowx.add(""+value_Mapping.getLosToInDay()+"-Day/ "+value_Mapping.getLosToInMonth()+"-Month/ "+value_Mapping.getLosToInYear()+"-Year");
                if (value_Mapping.getLosCurrentDate()>0){
                    rowx.add("Current Date");
                } else {
                    rowx.add(""+value_Mapping.getLosPerCurrentDate());
                }
                
                rowx.add(""+value_Mapping.getValue());
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
                        String strWhere1 = PstDivision.fieldNames[PstDivision.FLD_COMPANY_ID] + "=" +  "" + (objEntity.getCompany_id()) ;//oidCompany; 
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
                    String strWhereDept1 = PstDepartment.TBL_HR_DEPARTMENT+"."+PstDepartment.fieldNames[PstDepartment.FLD_DIVISION_ID] + "=" + "" + objEntity.getDivision_id(); //oidDivision;
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
                    strWhereSec = PstSection.TBL_HR_SECTION+"."+PstSection.fieldNames[PstSection.FLD_DEPARTMENT_ID] + "=" + "" + objEntity.getDepartment_id(); 
                      Vector listSec = PstSection.list(0, 0, strWhereSec, "SECTION");
                    for (int s = 0; s< listSec.size(); s++) {
                        Section sec = (Section) listSec.get(s);
                        sec_key.add(sec.getSection());
                        sec_value.add(String.valueOf(sec.getOID()));
                    }
                      
		if(iCommand == Command.ADD || (iCommand == Command.SAVE && frmObject.errorSize() > 0)){                         
                rowx.add("<input type=\"hidden\"  name=\"" + frmObject.fieldNames[FrmValue_Mapping.FRM_FIELD_COMP_CODE] + "\" value=\"" + objEntity.getCompCode() + "\" size=\"12\" class=\"elemenForm\">") ;            
                //rowx.add("<input type=\"text\" disabled=\"disabled\" name=\"" + frmObject.fieldNames[FrmValue_Mapping.FRM_FIELD_COMP_CODE] + "\" value=\"" + objEntity.getCompCode() + "\" size=\"12\" class=\"elemenForm\">");
                rowx.add("<input type=\"text\" name=\"" + frmObject.fieldNames[FrmValue_Mapping.FRM_FIELD_PARAMETER] + "\" value=\"" + objEntity.getParameter() + "\" size=\"40\" class=\"elemenForm\">");
                rowx.add("<input type=\"text\" name=\"" + frmObject.fieldNames[FrmValue_Mapping.FRM_FIELD_NUMBER_OF_MAPS] + "\" value=\"" + objEntity.getNumber_of_map() + "\" size=\"40\" class=\"elemenForm\">");
                rowx.add(ControlDate.drawDateWithStyle(frmObject.fieldNames[FrmValue_Mapping.FRM_FIELD_START_DATE], objEntity.getStartdate(), 0, -40, "formElemen") +"*"+ frmObject.getErrorMsg(frmObject.FRM_FIELD_START_DATE));
                rowx.add(ControlDate.drawDateWithStyle(frmObject.fieldNames[FrmValue_Mapping.FRM_FIELD_END_DATE], objEntity.getEnddate(), 0, -40, "formElemen") +"*"+ frmObject.getErrorMsg(frmObject.FRM_FIELD_END_DATE));
                 
                rowx.add(ControlCombo.draw(frmObject.fieldNames[FrmValue_Mapping.FRM_FIELD_COMPANY_ID], "formElemen", null, String.valueOf(objEntity.getCompany_id()), comp_value1, comp_key1, "onChange=\"javascript:cmdUpdateCDDS()\""));
                rowx.add(ControlCombo.draw(frmObject.fieldNames[FrmValue_Mapping.FRM_FIELD_DIVISION_ID], "formElemen", null, String.valueOf(objEntity.getDivision_id()), div_value1, div_key1, "onChange=\"javascript:cmdUpdateCDDS()\""));
                rowx.add(ControlCombo.draw(frmObject.fieldNames[FrmValue_Mapping.FRM_FIELD_DEPARTMENT_ID], "formElemen", null, String.valueOf(objEntity.getDepartment_id()), dept_value1, dept_key1, "onChange=\"javascript:cmdUpdateCDDS()\""));
                rowx.add(ControlCombo.draw(frmObject.fieldNames[FrmValue_Mapping.FRM_FIELD_SECTION_ID], "formElemen", null, String.valueOf(objEntity.getSection_id()), sec_value, sec_key, ""));
                 
                rowx.add(ControlCombo.draw(frmObject.fieldNames[FrmValue_Mapping.FRM_FIELD_LEVEL_ID], "formElemen", null, String.valueOf(objEntity.getLevel_id()), level_value, level_key, ""));
                rowx.add(ControlCombo.draw(frmObject.fieldNames[FrmValue_Mapping.FRM_FIELD_MARITAL_ID], "formElemen", null, String.valueOf(objEntity.getMarital_id()), marital_value, marital_key, ""));
                rowx.add(ControlCombo.draw(frmObject.fieldNames[FrmValue_Mapping.FRM_FIELD_TAX_MARITAL_ID], "formElemen", null, String.valueOf(objEntity.getTaxMaritalId()), marital_value, marital_key, ""));
                rowx.add("<input type=\"text\" name=\"" + frmObject.fieldNames[FrmValue_Mapping.FRM_FIELD_LENGTH_OF_SERVICE] + "\" value=\"" + objEntity.getLength_of_service() + "\" size=\"40\" class=\"elemenForm\">");
                rowx.add(ControlCombo.draw(frmObject.fieldNames[FrmValue_Mapping.FRM_FIELD_EMPLOYEE_CATEGORY], "formElemen", null, String.valueOf(objEntity.getEmployee_category()), empcat_value, empcat_key, ""));
                rowx.add(ControlCombo.draw(frmObject.fieldNames[FrmValue_Mapping.FRM_FIELD_POSITION_ID], "formElemen", null, String.valueOf(objEntity.getPosition_id()), position_value, position_key, ""));
                rowx.add(ControlCombo.draw(frmObject.fieldNames[FrmValue_Mapping.FRM_FIELD_GRADE], "formElemen", null, String.valueOf(objEntity.getGrade()), grade_value, grade_key, ""));
                rowx.add("<input type=\"text\" name=\"EMP_FULLNAME\"  class=\"elemenForm\"> <input type=\"hidden\" name=\""+FrmValue_Mapping.fieldNames[FrmValue_Mapping.FRM_FIELD_EMPLOYEE_ID]+"\" value=\"\" class=\"formElemen\" > <a href=\"javascript:cmdSearchEmp()\">add employee</a>");
                rowx.add("<input type=\"text\" name=\"geo_address_pmnt\" value=\"" + objEntity.getGeoAddressPmnt() + "\" size=\"40\" onClick=\"javascript:updateGeoAddressPmnt()\"><input type=\"hidden\" name=\"" + frmObject.fieldNames[FrmValue_Mapping.FRM_FIELD_ADDR_COUNTRY_ID] + "\" value=\"" + objEntity.getAddrCountryId() + "\"> <input type=\"hidden\" name=\"" + frmObject.fieldNames[FrmValue_Mapping.FRM_FIELD_ADDR_PROVINCE_ID] + "\" value=\"" + objEntity.getAddrProvinceId() + "\"> <input type=\"hidden\" name=\"" + frmObject.fieldNames[FrmValue_Mapping.FRM_FIELD_ADDR_REGENCY_ID] + "\" value=\"" + objEntity.getAddrRegencyId() + "\"> <input type=\"hidden\" name=\"" + frmObject.fieldNames[FrmValue_Mapping.FRM_FIELD_ADDR_SUBREGENCY_ID] + "\" value=\"" + objEntity.getAddrSubRegencyId() + "\"> ");
                
                rowx.add(""+ControlCombo.draw(frmObject.fieldNames[FrmValue_Mapping.FRM_FIELD_LOS_FROM_IN_DAY], "formElemen", null, String.valueOf(objEntity.getLosFromInDay()), day_keyVal, day_keyVal, "")+" <br> "+ ControlCombo.draw(frmObject.fieldNames[FrmValue_Mapping.FRM_FIELD_LOS_FROM_IN_MONTH], "formElemen", null, String.valueOf(objEntity.getLosFromInMonth()), month_keyVal, month_keyVal, "")+" <br> "+ ControlCombo.draw(frmObject.fieldNames[FrmValue_Mapping.FRM_FIELD_LOS_FROM_IN_YEAR], "formElemen", null, String.valueOf(objEntity.getLosFromInYear()), year_keyVal, year_keyVal, ""));
                rowx.add(""+ControlCombo.draw(frmObject.fieldNames[FrmValue_Mapping.FRM_FIELD_LOS_TO_IN_DAY], "formElemen", null, String.valueOf(objEntity.getLosToInDay()), day_keyVal, day_keyVal, "")+" <br> "+ ControlCombo.draw(frmObject.fieldNames[FrmValue_Mapping.FRM_FIELD_LOS_TO_IN_MONTH], "formElemen", null, String.valueOf(objEntity.getLosToInMonth()), month_keyVal, month_keyVal, "")+" <br> "+ ControlCombo.draw(frmObject.fieldNames[FrmValue_Mapping.FRM_FIELD_LOS_TO_IN_YEAR], "formElemen", null, String.valueOf(objEntity.getLosToInYear()), year_keyVal, year_keyVal, ""));
                rowx.add(""+ControlCombo.draw(frmObject.fieldNames[FrmValue_Mapping.FRM_FIELD_LOS_CURRENT_DATE], "formElemen", null, String.valueOf(objEntity.getLosCurrentDate()), currentDate_value, currentDate_key, "")+" <br> "+"/ "+ControlDate.drawDateWithStyle(frmObject.fieldNames[FrmValue_Mapping.FRM_FIELD_LOS_PER_CURRENT_DATE], objEntity.getLosPerCurrentDate(), 0, -40, "formElemen"));
                
                
                rowx.add("<input type=\"text\" name=\"" + frmObject.fieldNames[FrmValue_Mapping.FRM_FIELD_VALUE] + "\" value=\"" + objEntity.getValue() + "\" size=\"40\" class=\"elemenForm\">");
                
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

        
int iCommand = FRMQueryString.requestCommand(request);
int start = FRMQueryString.requestInt(request, "start");
int prevCommand = FRMQueryString.requestInt(request, "prev_command");
long oidValue_Mapping = FRMQueryString.requestLong(request, "Value_Mapping_oid");
String oidcompCode = FRMQueryString.requestString(request, "compCode");

long oidEmployee = FRMQueryString.requestLong(request, "employee_oid");
if ((oidcompCode != null) && (oidcompCode!="") && (oidcompCode.length() > 0)  ) {
    session.putValue("SELECTED_COMPCODE_ID", oidcompCode);
} else {
     try{
      oidcompCode = ((String)session.getValue("SELECTED_COMPCODE_ID"));
     }catch(Exception e) {
     }
}

/*variable declaration*/
int recordToGet = 10;
String msgString = "";
int iErrCode = FRMMessage.NONE;
String scompCode = FRMQueryString.requestString(request, FrmValue_Mapping.fieldNames[FrmValue_Mapping.FRM_FIELD_COMP_CODE]);
String whereClause ="";
if (oidcompCode.length() > 0){
    whereClause = PstValue_Mapping.fieldNames[PstValue_Mapping.FLD_COMP_CODE] +" = \"" + oidcompCode + "\"";
} else {
    whereClause = PstValue_Mapping.fieldNames[PstValue_Mapping.FLD_COMP_CODE] +" = \"" + scompCode + "\"";
}

String orderClause = "";

CtrlValue_Mapping ctrlValue_Mapping = new CtrlValue_Mapping(request);
ControlLine ctrLine = new ControlLine();
Vector listValue_Mapping = new Vector(1,1);

iErrCode = ctrlValue_Mapping.action(iCommand , oidValue_Mapping);
/* end switch*/
FrmValue_Mapping frmValue_Mapping = ctrlValue_Mapping.getForm();

/*count list All GroupRank*/
int vectSize = PstValue_Mapping.getCount(whereClause);

/*switch list GroupRank*/
if((iCommand == Command.FIRST || iCommand == Command.PREV )||
  (iCommand == Command.NEXT || iCommand == Command.LAST)){
		start = ctrlValue_Mapping.actionList(iCommand, start, vectSize, recordToGet);
 } 
/* end switch list*/

Value_Mapping value_Mapping = ctrlValue_Mapping.getValue_Mapping();
msgString =  ctrlValue_Mapping.getMessage();

/* get record to display */

listValue_Mapping = PstValue_Mapping.list(start,recordToGet, whereClause , orderClause);

/*handle condition if size of record to display = 0 and start > 0 	after delete*/
if (listValue_Mapping.size() < 1 && start > 0)
{
	 if (vectSize - recordToGet > recordToGet)
			start = start - recordToGet;   //go to Command.PREV
	 else{
		 start = 0 ;
		 iCommand = Command.FIRST;
		 prevCommand = Command.FIRST; //go to Command.FIRST
	 }
	 listValue_Mapping = PstValue_Mapping.list(start,recordToGet, whereClause , orderClause);
}

if (listValue_Mapping  == null){
    iCommand = Command.ADD;
}

    String scompcode = FRMQueryString.requestString(request, FrmValue_Mapping.fieldNames[FrmValue_Mapping.FRM_FIELD_COMP_CODE]);
    String sparameter = FRMQueryString.requestString(request, FrmValue_Mapping.fieldNames[FrmValue_Mapping.FRM_FIELD_PARAMETER]);
    int snumber_of_map = FRMQueryString.requestInt(request, FrmValue_Mapping.fieldNames[FrmValue_Mapping.FRM_FIELD_NUMBER_OF_MAPS]);
    Date sstartdate = FRMQueryString.requestDate(request, FrmValue_Mapping.fieldNames[FrmValue_Mapping.FRM_FIELD_START_DATE]);
    Date senddate = FRMQueryString.requestDate(request, FrmValue_Mapping.fieldNames[FrmValue_Mapping.FRM_FIELD_END_DATE]); 
    long scompanyId = FRMQueryString.requestLong(request, FrmValue_Mapping.fieldNames[FrmValue_Mapping.FRM_FIELD_COMPANY_ID]);
    long sdivisionId = FRMQueryString.requestLong(request, FrmValue_Mapping.fieldNames[FrmValue_Mapping.FRM_FIELD_DIVISION_ID]);
    long sdepartementId = FRMQueryString.requestLong(request, FrmValue_Mapping.fieldNames[FrmValue_Mapping.FRM_FIELD_DEPARTMENT_ID]);
    long ssectionId = FRMQueryString.requestLong(request, FrmValue_Mapping.fieldNames[FrmValue_Mapping.FRM_FIELD_SECTION_ID]);
    long slevelId = FRMQueryString.requestLong(request, FrmValue_Mapping.fieldNames[FrmValue_Mapping.FRM_FIELD_LEVEL_ID]);
    long smaritalId = FRMQueryString.requestLong(request, FrmValue_Mapping.fieldNames[FrmValue_Mapping.FRM_FIELD_MARITAL_ID]);
    float slength_of_service = FRMQueryString.requestFloat(request, FrmValue_Mapping.fieldNames[FrmValue_Mapping.FRM_FIELD_LENGTH_OF_SERVICE]);
    long semployeecategory = FRMQueryString.requestLong(request, FrmValue_Mapping.fieldNames[FrmValue_Mapping.FRM_FIELD_EMPLOYEE_CATEGORY]);
    long spositionId = FRMQueryString.requestLong(request, FrmValue_Mapping.fieldNames[FrmValue_Mapping.FRM_FIELD_POSITION_ID]);
    long semployeeId = FRMQueryString.requestLong(request, FrmValue_Mapping.fieldNames[FrmValue_Mapping.FRM_FIELD_EMPLOYEE_ID]);
    long saddrcountryId = FRMQueryString.requestLong(request, FrmValue_Mapping.fieldNames[FrmValue_Mapping.FRM_FIELD_ADDR_COUNTRY_ID]);
    long saddrprovinceId = FRMQueryString.requestLong(request, FrmValue_Mapping.fieldNames[FrmValue_Mapping.FRM_FIELD_ADDR_PROVINCE_ID]);
    long saddrregencyId = FRMQueryString.requestLong(request, FrmValue_Mapping.fieldNames[FrmValue_Mapping.FRM_FIELD_ADDR_REGENCY_ID]);
    long saddrsubregencyId = FRMQueryString.requestLong(request, FrmValue_Mapping.fieldNames[FrmValue_Mapping.FRM_FIELD_ADDR_SUBREGENCY_ID]);
    double svalue = FRMQueryString.requestDouble(request, FrmValue_Mapping.fieldNames[FrmValue_Mapping.FRM_FIELD_VALUE]);
    
    
    
    if (oidcompCode.length() > 0){
                              value_Mapping.setCompCode(oidcompCode);
                         } else {
                             value_Mapping.setCompCode(scompcode);
                         }
 
    if(iCommand==Command.GOTO){
                        
                         value_Mapping.setValuemappingid(oidValue_Mapping);
                       
                         
                         value_Mapping.setParameter(sparameter);//"PARAMETER",
                         value_Mapping.setNumber_of_map(snumber_of_map);//"NUMBER_OF_MAPS",
                         value_Mapping.setStartdate(sstartdate);//"START_DATE",
                         value_Mapping.setEnddate(senddate);//"END_DATE",
                         value_Mapping.setCompany_id(scompanyId);//"COMPANY_ID",
                         value_Mapping.setDivision_id(sdivisionId);//"DIVISION_ID",
                         value_Mapping.setDepartment_id(sdepartementId); //"DEPARTMENT_ID",
                         value_Mapping.setSection_id(ssectionId);//"SECTION_ID",
                         value_Mapping.setLevel_id(slevelId);//"LEVEL_ID",
                         value_Mapping.setMarital_id(smaritalId);//"MARITAL_ID",
                         value_Mapping.setLength_of_service(slength_of_service);//"LENGTH_OF_SERVICE",
                         value_Mapping.setEmployee_category(semployeecategory);//"EMPLOYEE_CATEGORY",
                         value_Mapping.setPosition_id(spositionId);//"POSITION",
                         value_Mapping.setEmployee_id(semployeeId);//"EMPLOYEE_ID",
                         value_Mapping.setAddrCountryId(saddrcountryId);//"ADDR_COUNTRY_ID",
                         value_Mapping.setAddrProvinceId(saddrprovinceId);//"ADDR_PROVINCE_ID",
                         value_Mapping.setAddrRegencyId(saddrregencyId);//"ADDR_REGENCY_ID",
                         value_Mapping.setAddrSubRegencyId(saddrsubregencyId);////"ADDR_SUBREGENCY_ID",
                         value_Mapping.setValue(svalue);//"VALUE"
    
            long n = FRMQueryString.requestLong(request, "n");
            if (n==1){
                iCommand=Command.EDIT;
            } else {
                iCommand=Command.ADD;
            }
    }

%>
<html><!-- #BeginTemplate "/Templates/main.dwt" -->
<head>
<!-- #BeginEditable "doctitle" --> 
<title>Doc Type</title>
<script language="JavaScript">
function cmdUpdateCDDS(n){
                document.frmValue_Mapping.command.value="<%=String.valueOf(Command.GOTO)%>";
                document.frmValue_Mapping.action="value_mapping.jsp?n="+n+"";
                document.frmValue_Mapping.submit();
            }

function cmdAdd(compCode){
	document.frmValue_Mapping.Value_Mapping_oid.value="0";
	document.frmValue_Mapping.command.value="<%=Command.ADD%>";
	document.frmValue_Mapping.prev_command.value="<%=prevCommand%>";
	document.frmValue_Mapping.action="value_mapping.jsp?compCode="+compCode+"";
	document.frmValue_Mapping.submit();
}
function cmdSearchEmp(){
        //emp_number = document.frmDocMasterFlow.EMP_NUMBER.value;
	window.open("<%=approot%>/employee/search/SearchMasterFlow.jsp?formName=frmValue_Mapping&empPathId=<%=frmValue_Mapping.fieldNames[frmValue_Mapping.FRM_FIELD_EMPLOYEE_ID]%>", null, "height=550,width=600, status=yes,toolbar=no,menubar=no,location=no, scrollbars=yes");       
}
function cmdAsk(oidValue_Mapping){
	document.frmValue_Mapping.Value_Mapping_oid.value=oidValue_Mapping;
	document.frmValue_Mapping.command.value="<%=Command.ASK%>";
	document.frmValue_Mapping.prev_command.value="<%=prevCommand%>";
	document.frmValue_Mapping.action="value_mapping.jsp";
	document.frmValue_Mapping.submit();
}
function updateGeoAddressPmnt(){
                oidNegara    = document.frmValue_Mapping.<%=frmValue_Mapping.fieldNames[frmValue_Mapping.FRM_FIELD_ADDR_COUNTRY_ID]%>.value;
                oidProvinsi  = document.frmValue_Mapping.<%=frmValue_Mapping.fieldNames[frmValue_Mapping.FRM_FIELD_ADDR_PROVINCE_ID]%>.value ;
                oidKabupaten = document.frmValue_Mapping.<%=frmValue_Mapping.fieldNames[frmValue_Mapping.FRM_FIELD_ADDR_REGENCY_ID]%>.value ;
                oidKecamatan = document.frmValue_Mapping.<%=frmValue_Mapping.fieldNames[frmValue_Mapping.FRM_FIELD_ADDR_SUBREGENCY_ID]%>.value;                    
                window.open("<%=approot%>/payroll/value_mapping/geo_address.jsp?formName=frmValue_Mapping&employee_oid=<%=String.valueOf(oidEmployee)%>&addresstype=2&"+
                    "<%=FrmKecamatan.fieldNames[FrmKecamatan.FRM_FIELD_ID_NEGARA]%>="+oidNegara+"&"+
                    "<%=FrmKecamatan.fieldNames[FrmKecamatan.FRM_FIELD_ID_PROPINSI]%>="+oidProvinsi+"&"+
                    "<%=FrmKecamatan.fieldNames[FrmKecamatan.FRM_FIELD_ID_KABUPATEN]%>="+oidKabupaten+"&"+
                    "<%=FrmKecamatan.fieldNames[FrmKecamatan.FRM_FIELD_ID_KECAMATAN]%>="+oidKecamatan+"",                                                
                null, "height=550,width=600, status=yes,toolbar=no,menubar=no,location=no, scrollbars=yes");
            }
function cmdConfirmDelete(oidValue_Mapping){
	document.frmValue_Mapping.Value_Mapping_oid.value=oidValue_Mapping;
	document.frmValue_Mapping.command.value="<%=Command.DELETE%>";
	document.frmValue_Mapping.prev_command.value="<%=prevCommand%>";
	document.frmValue_Mapping.action="value_mapping.jsp";
	document.frmValue_Mapping.submit();
}

function cmdSave(){
	document.frmValue_Mapping.command.value="<%=Command.SAVE%>";
	document.frmValue_Mapping.prev_command.value="<%=prevCommand%>";
	document.frmValue_Mapping.action="value_mapping.jsp";
	document.frmValue_Mapping.submit();
}

function cmdEdit(oidValue_Mapping){
	document.frmValue_Mapping.Value_Mapping_oid.value=oidValue_Mapping;
	document.frmValue_Mapping.command.value="<%=Command.EDIT%>";
	document.frmValue_Mapping.prev_command.value="<%=prevCommand%>";
	document.frmValue_Mapping.action="value_mapping.jsp";
	document.frmValue_Mapping.submit();
}

function cmdCancel(oidValue_Mapping){
	document.frmValue_Mapping.Value_Mapping_oid.value=oidValue_Mapping;
	document.frmValue_Mapping.command.value="<%=Command.EDIT%>";
	document.frmValue_Mapping.prev_command.value="<%=prevCommand%>";
	document.frmValue_Mapping.action="value_mapping.jsp";
	document.frmValue_Mapping.submit();
}

function cmdBack(){
	document.frmValue_Mapping.command.value="<%=Command.BACK%>";
	document.frmValue_Mapping.action="value_mapping.jsp";
	document.frmValue_Mapping.submit();
}

function cmdListFirst(){
	document.frmValue_Mapping.command.value="<%=Command.FIRST%>";
	document.frmValue_Mapping.prev_command.value="<%=Command.FIRST%>";
	document.frmValue_Mapping.action="value_mapping.jsp";
	document.frmValue_Mapping.submit();
}

function cmdListPrev(){
	document.frmValue_Mapping.command.value="<%=Command.PREV%>";
	document.frmValue_Mapping.prev_command.value="<%=Command.PREV%>";
	document.frmValue_Mapping.action="value_mapping.jsp";
	document.frmValue_Mapping.submit();
}

function cmdListNext(){
	document.frmValue_Mapping.command.value="<%=Command.NEXT%>";
	document.frmValue_Mapping.prev_command.value="<%=Command.NEXT%>";
	document.frmValue_Mapping.action="value_mapping.jsp";
	document.frmValue_Mapping.submit();
}

function cmdListLast(){
	document.frmValue_Mapping.command.value="<%=Command.LAST%>";
	document.frmValue_Mapping.prev_command.value="<%=Command.LAST%>";
	document.frmValue_Mapping.action="value_mapping.jsp";
	document.frmValue_Mapping.submit();
}

function cmdListCateg(oidValue_Mapping){
	document.frmValue_Mapping.Value_Mapping_oid.value=oidValue_Mapping;
	document.frmValue_Mapping.command.value="<%=Command.LIST%>";
	document.frmValue_Mapping.action="value_mapping.jsp";
	document.frmValue_Mapping.submit();
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
<!-- #EndEditable --> 
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"> 
<!-- #BeginEditable "styles" --> 
<link rel="stylesheet" href="../../styles/main.css" type="text/css">
<!-- #EndEditable -->
<!-- #BeginEditable "stylestab" --> 
<link rel="stylesheet" href="../../styles/tab.css" type="text/css">
<!-- #EndEditable -->
<!-- #BeginEditable "headerscript" --> 
<SCRIPT language=JavaScript>
function hideObjectForEmployee(){    
} 
	 
function hideObjectForLockers(){ 
}
	
function hideObjectForCanteen(){
}
	
function hideObjectForClinic(){
}

function hideObjectForMasterdata(){
}

</SCRIPT>
<!-- #EndEditable -->
</head> 

<body <%=noBack%> bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" onLoad="MM_preloadImages('<%=approot%>/images/BtnNewOn.jpg')">
<table width="100%" border="0" cellspacing="0" cellpadding="0" height="100%" bgcolor="#F9FCFF" >
     <%if(headerStyle && !verTemplate.equalsIgnoreCase("0")){%> 
           <%@include file="../../styletemplate/template_header.jsp" %>
            <%}else{%>
  <tr> 
    <td ID="TOPTITLE" background="<%=approot%>/images/HRIS_HeaderBg3.jpg" width="100%" height="54"> 
      <!-- #BeginEditable "header" --> 
      <%@ include file = "../../main/header.jsp" %>
      <!-- #EndEditable --> 
    </td>
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
    <td width="88%" valign="top" align="left"> 
      <table width="100%" border="0" cellspacing="3" cellpadding="2"> 
        <tr> 
          <td width="100%">
      <table width="100%" border="0" cellspacing="0" cellpadding="0"> 
        <tr> 
          <td height="20">
		    <font color="#FF6600" face="Arial"><strong>
			  <!-- #BeginEditable "contenttitle" -->Masterdata 
                  &gt; Value_Mapping<!-- #EndEditable --> 
            </strong></font>
	      </td>
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
                            <td valign="top">
		    				  <!-- #BeginEditable "content" --> 
                                    <form name="frmValue_Mapping" method ="post" action="">
                                      <input type="hidden" name="command" value="<%=iCommand%>">
                                      <input type="hidden" name="vectSize" value="<%=vectSize%>">
                                      <input type="hidden" name="start" value="<%=start%>">
                                      <input type="hidden" name="prev_command" value="<%=prevCommand%>">
				      <input type="hidden" name="Value_Mapping_oid" value="<%=oidValue_Mapping%>">
                                      <table width="100%" border="0" cellspacing="0" cellpadding="0">
                                        <tr align="left" valign="top"> 
                                          <td height="8"  colspan="3"> 
                                            <table width="100%" border="0" cellspacing="0" cellpadding="0">                                              
                                              <tr align="left" valign="top"> 
                                                <td colspan="3" >
                                                  <table width="100%" border="0" cellspacing="0" cellpadding="0">
                                                    <tr>
                                                      <td class="listtitle" width="37%">&nbsp;</td>
                                                      <td width="63%" class="comment">&nbsp;</td>
                                                    </tr>
                                                    <tr> 
                                                      <td class="listtitle" width="37%"> List     <%=value_Mapping.getCompCode()%></td>
                                                      <td width="63%" class="comment">*)entry 
                                                        required </td>
                                                    </tr>
                                                  </table>
                                                </td>
                                              </tr>
                                              <%
												try{
												%>
                                              <tr align="left" valign="top"> 
                                                <td height="22" valign="middle" colspan="3"> 
                                                  <%= drawList(iCommand,frmValue_Mapping, value_Mapping, listValue_Mapping,oidValue_Mapping)%></td>
                                              </tr>
                                              <% 
											  }catch(Exception exc){ 
											  }%>
                                              <tr align="left" valign="top"> 
                                                <td height="8" align="left" colspan="3" class="command"> 
                                                  <span class="command"> 
                                                  <% 
								   int cmd = 0;
									   if ((iCommand == Command.FIRST || iCommand == Command.PREV )|| 
										(iCommand == Command.NEXT || iCommand == Command.LAST))
											cmd =iCommand; 
								   else{
									  if(iCommand == Command.NONE || prevCommand == Command.NONE)
										cmd = Command.FIRST;
									  else 
									  	cmd =prevCommand; 
								   } 
							    %>
                                                  <% ctrLine.setLocationImg(approot+"/images");
							   	ctrLine.initDefault();
								 %>
                                                  <%=ctrLine.drawImageListLimit(cmd,vectSize,start,recordToGet)%> 
                                                  </span> </td>
                                              </tr>
                                              <%if((iCommand != Command.ADD && iCommand != Command.ASK && iCommand != Command.EDIT)&& (frmValue_Mapping.errorSize()<1)){
											  	if(privAdd){%>
                                              <tr align="left" valign="top"> 
                                                <td height="22" valign="middle" colspan="3"> 
                                                  <table cellpadding="0" cellspacing="0" border="0">
                                                    <tr> 
                                                      <td>&nbsp;</td>
                                                    </tr>
                                                    <tr> 
                                                      <td width="4"><img src="<%=approot%>/images/spacer.gif" width="1" height="1"></td>
                                                      <td width="24"><a href="javascript:cmdAdd()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image261','','<%=approot%>/images/BtnNewOn.jpg',1)"><img name="Image261" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="Add new data"></a></td>
                                                      <td width="6"><img src="<%=approot%>/images/spacer.gif" width="1" height="1"></td>
                                                      <td height="22" valign="middle" colspan="3" width="951"><a href="javascript:cmdAdd('<%=oidcompCode%>')" class="command">Add New </a> </td>
                                                     
                                                    </tr>
                                                  </table>
                                                </td>
                                              </tr>
                                              <%}
											  }%>
                                            </table>
                                          </td>										  
                                        </tr>
                                        <tr align="left" valign="top"> 
                                          <td height="8" valign="middle" width="17%">&nbsp;</td>
                                          <td height="8" colspan="2" width="83%">&nbsp; 
                                          </td>
                                        </tr>
                                        <tr align="left" valign="top" > 
                                          <td colspan="3" class="command" height="26"> 
                                            <%
									ctrLine.setLocationImg(approot+"/images");
									ctrLine.initDefault();
									ctrLine.setTableWidth("80%");
									String scomDel = "javascript:cmdAsk('"+oidValue_Mapping+"')";
									String sconDelCom = "javascript:cmdConfirmDelete('"+oidValue_Mapping+"')";
									String scancel = "javascript:cmdEdit('"+oidValue_Mapping+"')";
									ctrLine.setBackCaption("Back to List Value Mapping");
									ctrLine.setCommandStyle("buttonlink");
									ctrLine.setAddCaption("Add Value Mapping");
									ctrLine.setSaveCaption("Save Value Mapping");
									ctrLine.setDeleteCaption("Delete Value Mapping");
									ctrLine.setConfirmDelCaption("Yes Delete Value Mapping");

									if ( privDelete ){
										ctrLine.setConfirmDelCommand(sconDelCom);
										ctrLine.setDeleteCommand(scomDel);
										ctrLine.setEditCommand(scancel);
									}else{ 
										ctrLine.setConfirmDelCaption("");
										ctrLine.setDeleteCaption("");
										ctrLine.setEditCaption("");
									}

									if(privAdd == false  && privUpdate == false){
										ctrLine.setSaveCaption("");
									}

									if (privAdd == false){
										ctrLine.setAddCaption("");
									}
									
									if((listValue_Mapping.size()<1)&&(iCommand == Command.NONE))
										 iCommand = Command.ADD;  
										 
									if(iCommand == Command.ASK)
										ctrLine.setDeleteQuestion(msgString);										 
									%>
                                            <%= ctrLine.drawImage(iCommand, iErrCode, msgString)%> 
                                          </td>
                                        </tr>
                                      </table>
                                    </form>
                                    <!-- #EndEditable -->
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
</body>
<!-- #BeginEditable "script" -->
<script language="JavaScript">
	//var oBody = document.body;
	//var oSuccess = oBody.attachEvent('onkeydown',fnTrapKD);
</script>
<!-- #EndEditable -->
<!-- #EndTemplate --></html>
