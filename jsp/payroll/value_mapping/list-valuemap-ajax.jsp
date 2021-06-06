<%-- 
    Document   : list-valuemap-ajax
    Created on : Mar 11, 2016, 8:58:56 AM
    Author     : Dimata 007
--%>
<%@page import="com.dimata.harisma.entity.payroll.PstPaySlip"%>
<%@page import="com.dimata.util.Formater"%>
<%@page import="com.dimata.harisma.session.employee.SessEmployeeView"%>
<%@page import="com.dimata.util.Command"%>
<%@page import="com.dimata.harisma.entity.payroll.PstValue_Mapping"%>
<%@page import="com.dimata.harisma.form.payroll.CtrlValue_Mapping"%>
<%@page import="com.dimata.harisma.entity.payroll.Value_Mapping"%>
<%@ include file = "../../main/javainit.jsp" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    String gradeCode= FRMQueryString.requestString(request, "grade_code");
    String compCode = FRMQueryString.requestString(request, "comp_code");
    int iCommand = FRMQueryString.requestInt(request, "command");
    int start = FRMQueryString.requestInt(request, "start");
    
    
    long s_companyId = FRMQueryString.requestLong(request, "s_company_id");
    long s_divisionId = FRMQueryString.requestLong(request, "s_division_id");
    long s_departmentId = FRMQueryString.requestLong(request, "s_department_id");
    long s_sectionId = FRMQueryString.requestLong(request, "s_section_id");
    long s_levelId = FRMQueryString.requestLong(request, "s_level_id");
    long s_empcatId = FRMQueryString.requestLong(request, "s_empcat_id");
    long s_positionId = FRMQueryString.requestLong(request, "s_position_id");
    long s_gradeId = FRMQueryString.requestLong(request, "s_grade_id");
    long s_maritalId = FRMQueryString.requestLong(request, "s_marital_id");
    long s_religionId = FRMQueryString.requestLong(request, "s_religion_id");
    long s_empId = FRMQueryString.requestLong(request, "s_emp_id");
    double s_value = FRMQueryString.requestDouble(request, "s_value");
    int s_type = FRMQueryString.requestInt(request, "s_type");
    long s_period = FRMQueryString.requestLong(request, "s_period");
    
    String whereClause = " 1=1 ";
    String order = PstValue_Mapping.fieldNames[PstValue_Mapping.FLD_START_DATE]+" DESC";
    Vector listValueMap = new Vector();
    CtrlValue_Mapping ctrlMapping = new CtrlValue_Mapping(request);
    SessEmployeeView sessEmpView = new SessEmployeeView();
    int recordToGet = 10;
    int vectSize = 0;
    vectSize = PstValue_Mapping.getCount("");
    if ((iCommand == Command.FIRST) || (iCommand == Command.NEXT) || (iCommand == Command.PREV)
            || (iCommand == Command.LAST) || (iCommand == Command.LIST)) {
        start = ctrlMapping.actionList(iCommand, start, vectSize, recordToGet);
    }

    
    String tb = " ";
    if (s_type == 1){
        tb = " he.";
    } else if (s_type == 2){
        tb = " pay.";
    }
    
    if (s_companyId != 0){
        whereClause += " AND " +tb+ PstValue_Mapping.fieldNames[PstValue_Mapping.FLD_COMPANY_ID]+" = "+s_companyId+" ";
    }
    if (s_divisionId != 0){
        whereClause += " AND " +tb+ PstValue_Mapping.fieldNames[PstValue_Mapping.FLD_DIVISION_ID]+" = "+s_divisionId+" ";
    }
    if (s_departmentId != 0){
        whereClause += " AND " +tb+ PstValue_Mapping.fieldNames[PstValue_Mapping.FLD_DEPARTMENT_ID]+" = "+s_departmentId+" ";
    }
    if (s_sectionId != 0){
        whereClause += " AND " +tb+ PstValue_Mapping.fieldNames[PstValue_Mapping.FLD_SECTION_ID]+" = "+s_sectionId+" ";
    }
    if (s_levelId != 0){
        whereClause += " AND " +tb+ PstValue_Mapping.fieldNames[PstValue_Mapping.FLD_LEVEL_ID]+" = "+s_levelId+" ";
    }
    if (s_empcatId != 0){
        whereClause += " AND " +tb+ PstValue_Mapping.fieldNames[PstValue_Mapping.FLD_EMPLOYEE_CATEGORY]+" = "+s_empcatId+" ";
    }
    if (s_positionId != 0){
        whereClause += " AND " +tb+ PstValue_Mapping.fieldNames[PstValue_Mapping.FLD_POSITION_ID]+" = "+s_positionId+" ";
    }
    if (s_gradeId != 0){
        whereClause += " AND " +tb+ PstValue_Mapping.fieldNames[PstValue_Mapping.FLD_GRADE]+" = "+s_gradeId+" ";
    }
    if (s_empId != 0){
        whereClause += " AND vm." + PstValue_Mapping.fieldNames[PstValue_Mapping.FLD_EMPLOYEE_ID]+" = "+s_empId+" ";
    }
    if (s_value != 0){
        whereClause += " AND vm." + PstValue_Mapping.fieldNames[PstValue_Mapping.FLD_VALUE]+" = "+s_value+" ";
    }
    if (s_period != 0 && s_type == 2){
        whereClause += " AND pay." + PstPaySlip.fieldNames[PstPaySlip.FLD_PERIOD_ID]+" = "+s_period+" ";
    }
    
    if (s_maritalId != 0){
        whereClause += " AND vm." + PstValue_Mapping.fieldNames[PstValue_Mapping.FLD_MARITAL_ID]+" = "+s_maritalId+" ";
    }
    if (s_religionId != 0){
        whereClause += " AND vm." + PstValue_Mapping.fieldNames[PstValue_Mapping.FLD_RELIGION_ID]+" = "+s_religionId+" ";
    }
    
    
    
    if (!(gradeCode.equals("0"))){
        whereClause += " AND vm." + PstValue_Mapping.fieldNames[PstValue_Mapping.FLD_GRADE]+" LIKE '%"+gradeCode+"%'";
        whereClause += " AND vm." + PstValue_Mapping.fieldNames[PstValue_Mapping.FLD_COMP_CODE]+"='"+compCode+"'";
        //vectSize = PstValue_Mapping.getCount(whereClause);
        Vector listValueMapCoun = PstValue_Mapping.listInnerJoin(0, 0, whereClause, order,s_empId, s_type);     
        vectSize =  listValueMapCoun.size();   
        if (iCommand == Command.LAST){
            start = vectSize - 10;
        }              
        listValueMap = PstValue_Mapping.listInnerJoin(start, 0, whereClause, order,s_empId,s_type);    
    } else {
        whereClause += " AND vm." + PstValue_Mapping.fieldNames[PstValue_Mapping.FLD_COMP_CODE]+"='"+compCode+"'";
        //vectSize = PstValue_Mapping.getCount(whereClause);
        Vector listValueMapCount = PstValue_Mapping.listInnerJoin(0, 0, whereClause, order, s_empId,s_type);
        vectSize =  listValueMapCount.size();
        if (iCommand == Command.LAST){
            start = vectSize - 10;
        }
        listValueMap = PstValue_Mapping.listInnerJoin(start, recordToGet, whereClause, order, s_empId, s_type);
        
        
    }

    if (listValueMap != null && listValueMap.size()>0){
        %>
        <table class="tblStyle" style="background-color: #F5F5F5">
            <tr>
                <td class="title_tbl">No</td>
                <td class="title_tbl">Start Date</td>
                <td class="title_tbl">End Date</td>
                <td class="title_tbl">Company</td>
                <td class="title_tbl">Division</td>
                <td class="title_tbl">Department</td>
                <td class="title_tbl">Section</td>
                <td class="title_tbl">Level</td>
                <td class="title_tbl">Marital</td>
                <td class="title_tbl">Length Of Service</td>
                <td class="title_tbl">Employee Category</td>
                <td class="title_tbl">Position</td>
                <td class="title_tbl">Grade</td>
                <td class="title_tbl">Religion</td>
                <td class="title_tbl">Payroll Num</td>
                <td class="title_tbl">GEO</td>
                <td class="title_tbl">Sex</td>
                <td class="title_tbl">Resign status</td>
                <td class="title_tbl">Value</td>
				<td class="title_tbl">Remark</td>
                <td class="title_tbl">Action</td>
            </tr>
        <%
        for(int i=0; i<listValueMap.size(); i++){
            Value_Mapping valueMapping = (Value_Mapping)listValueMap.get(i);
            String geo = PstValue_Mapping.GetGeoAddress(valueMapping); 
            String sex = "-";
            if (valueMapping.getSex() > -1){
                if (valueMapping.getSex()== 0){
                    sex = "Pria";
                } else {
                    sex = "Wanita";
                }
            }
            String rs = "-";
            if (valueMapping.getResignStatus() > -1){
                if (valueMapping.getResignStatus()== 0){
                    sex = "No";
                } else {
                    sex = "Yes";
                }
            }
            %>
            <tr>
                <td><%=(i+1)%></td>
                <td><%=""+valueMapping.getStartdate()%></td>
                <td><%=""+valueMapping.getEnddate()%></td>
                <td><%=""+sessEmpView.getCompanyName(valueMapping.getCompany_id())%></td>
                <td><%=""+sessEmpView.getDivisionName(valueMapping.getDivision_id())%></td>
                <td><%=""+sessEmpView.getDepartmentName(valueMapping.getDepartment_id())%></td>
                <td><%=""+sessEmpView.getSectionName(valueMapping.getSection_id())%></td>
                <td><%=""+sessEmpView.getLevelName(valueMapping.getLevel_id())%></td>
                <td><%=""+sessEmpView.getMaritalName(valueMapping.getMarital_id()) %></td>
                <td><%=""+valueMapping.getLength_of_service() %></td>
                <td><%=""+sessEmpView.getEmpCategory(valueMapping.getEmployee_category())%></td>
                <td><%=""+sessEmpView.getPositionName(valueMapping.getPosition_id())%></td>
                <td><%=""+sessEmpView.getGradeLevel(valueMapping.getGrade())%></td>
                <td><%=""+sessEmpView.getReligion(valueMapping.getReligion())%></td>
                <td><%=""+sessEmpView.getFullNameAndPayroll(valueMapping.getEmployee_id()) %></td>
                <td><%= geo %></td>
                <td><%= sex %></td>
                <td><%= rs %></td>
                <td><%=Formater.formatNumber(valueMapping.getValue(), "")%></td>
				<td><%=valueMapping.getRemark()%></td>
                <td>
                    <a class="btn-small-e" style="color:#FFF" href="javascript:cmdEdit('<%=valueMapping.getOID()%>','<%=valueMapping.getCompCode()%>')">e</a>
                    <a class="btn-small-x" style="color:#FFF" href="javascript:cmdAsk('<%=valueMapping.getOID()%>')">&times;</a>
                </td>
            </tr>
            <%
        }
        %>
        </table>
        <div>&nbsp;</div>
        <div id="record_count">
            <%
            if (vectSize >= recordToGet){
                %>
                List : <%=start%> &HorizontalLine; <%= (start+recordToGet) %> | 
                <%
            }
            %>
            Total : <%= vectSize %>
        </div>
        <div class="pagging">
            <a style="color:#F5F5F5" href="javascript:cmdListFirst('<%=start%>','<%=compCode%>','<%=s_companyId%>','<%=s_divisionId%>','<%=s_departmentId%>','<%=s_sectionId%>','<%=s_levelId%>','<%=s_empcatId%>','<%=s_positionId%>','<%=s_gradeId %>','<%=s_empId%>','<%=s_maritalId%>','<%=s_religionId%>','<%=s_value%>','<%=s_type%>','<%=s_period%>')" class="btn-small">First</a>
            <a style="color:#F5F5F5" href="javascript:cmdListPrev('<%=start%>','<%=compCode%>','<%=s_companyId%>','<%=s_divisionId%>','<%=s_departmentId%>','<%=s_sectionId%>','<%=s_levelId%>','<%=s_empcatId%>','<%=s_positionId%>','<%=s_gradeId %>','<%=s_empId%>','<%=s_maritalId%>','<%=s_religionId%>','<%=s_value%>','<%=s_type%>','<%=s_period%>')" class="btn-small">Previous</a>
            <a style="color:#F5F5F5" href="javascript:cmdListNext('<%=start%>','<%=compCode%>','<%=s_companyId%>','<%=s_divisionId%>','<%=s_departmentId%>','<%=s_sectionId%>','<%=s_levelId%>','<%=s_empcatId%>','<%=s_positionId%>','<%=s_gradeId %>','<%=s_empId%>','<%=s_maritalId%>','<%=s_religionId%>','<%=s_value%>','<%=s_type%>','<%=s_period%>')" class="btn-small">Next</a>
            <a style="color:#F5F5F5" href="javascript:cmdListLast('<%=start%>','<%=compCode%>','<%=s_companyId%>','<%=s_divisionId%>','<%=s_departmentId%>','<%=s_sectionId%>','<%=s_levelId%>','<%=s_empcatId%>','<%=s_positionId%>','<%=s_gradeId %>','<%=s_empId%>','<%=s_maritalId%>','<%=s_religionId%>','<%=s_value%>','<%=s_type%>','<%=s_period%>')" class="btn-small">Last</a>
        </div>
        <%
    } else {
        %>
        <p id="menu_utama">No Data</p>
        <%
        
    }
%>
