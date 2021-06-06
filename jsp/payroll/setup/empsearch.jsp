<%@page contentType="text/html"%>
<!-- package java -->
<%@ page import = "java.util.*" %>
<!-- package dimata -->
<%@ page import = "com.dimata.util.*" %>
<!-- package qdep -->
<%@ page import = "com.dimata.gui.jsp.*" %>
<%@ page import = "com.dimata.qdep.form.*" %>
<!--package harisma -->
<%@ page import = "com.dimata.harisma.entity.employee.*" %>
<%@ page import = "com.dimata.harisma.form.employee.*" %>
<%@ page import = "com.dimata.harisma.form.attendance.*" %>
<%@ page import = "com.dimata.harisma.entity.masterdata.*" %>
<%@ page import = "com.dimata.harisma.entity.attendance.*" %>

<%@ include file = "../../main/javainit.jsp" %>
<% int appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_EMPLOYEE, AppObjInfo.G2_ATTENDANCE, AppObjInfo.OBJ_WORKING_SCHEDULE);

    boolean isSecretaryLogin = (positionType >= PstPosition.LEVEL_SECRETARY) ? true : false;
    long hrdDepartmentOid = Long.parseLong(String.valueOf(PstSystemProperty.getPropertyLongbyName(OID_HRD_DEPARTMENT)));
    boolean isHRDLogin = hrdDepartmentOid == departmentOid ? true : false;
    long edpSectionOid = Long.parseLong(String.valueOf(PstSystemProperty.getPropertyLongbyName(OID_EDP_SECTION)));
    boolean isEdpLogin = edpSectionOid == sectionOfLoginUser.getOID() ? true : false;
    boolean isGeneralManager = positionType == PstPosition.LEVEL_GENERAL_MANAGER ? true : false;

%>

<%!    public String drawList(Vector objectClass, String emp_period) {
        ControlList ctrlist = new ControlList();
        ctrlist.setAreaWidth("100%");
        ctrlist.setListStyle("listgen");
        ctrlist.setTitleStyle("listgentitle");
        ctrlist.setCellStyle("listgensell");
        ctrlist.setHeaderStyle("listgentitle");
        ctrlist.addHeader("Employee Number", "8%");
        ctrlist.addHeader("Full Name", "15%");
        ctrlist.addHeader("Department", "10%");
        ctrlist.addHeader("Section", "10%");

        ctrlist.setLinkRow(0);
        ctrlist.setLinkSufix("");
        Vector lstData = ctrlist.getData();
        Vector lstLinkData = ctrlist.getLinkData();
        ctrlist.setLinkPrefix("javascript:cmdEdit('");
        ctrlist.reset();

        Hashtable hDept = new Hashtable();
        Vector listDept = PstDepartment.listAll();
        hDept.put("0", "-");
        for (int ls = 0; ls < listDept.size(); ls++) {
            Department dept = (Department) listDept.get(ls);
            hDept.put(String.valueOf(dept.getOID()), dept.getDepartment());
        }

        // hastable untuk section
        //updated by Yunny
        Hashtable hSec = new Hashtable();
        Vector listSec = PstSection.listAll();
        hSec.put("0", "-");
        for (int secIdx = 0; secIdx < listSec.size(); secIdx++) {
            Section sec = (Section) listSec.get(secIdx);
            hSec.put(String.valueOf(sec.getOID()), sec.getSection());
        }

        String whereClauseEmpSchedule = "";
        Hashtable hEmpSchedule = new Hashtable();
        if (emp_period.compareToIgnoreCase("") != 0) {
            whereClauseEmpSchedule = PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_PERIOD_ID] + " = " + emp_period + " ";
            Vector listEmpSchedule = PstEmpSchedule.list(0, 0, whereClauseEmpSchedule, "");
            if (listEmpSchedule.size() > 0) {
                for (int ls = 0; ls < listEmpSchedule.size(); ls++) {
                    EmpSchedule es = (EmpSchedule) listEmpSchedule.get(ls);
                    hEmpSchedule.put(String.valueOf(es.getEmployeeId()), String.valueOf(es.getPeriodId()));
                }
            }
        }

        for (int i = 0; i < objectClass.size(); i++) {
            /*
             Employee employee = (Employee) objectClass.get(i);
             Vector rowx = new Vector();
             if (hEmpSchedule.get(String.valueOf(employee.getOID())) == null) {
             rowx.add(employee.getEmployeeNum());
             }
             else {
             rowx.add(employee.getEmployeeNum() + " *");
             }
             rowx.add(employee.getFullName());
             rowx.add(hDept.get(String.valueOf(employee.getDepartmentId())));
             lstData.add(rowx);
             if (hEmpSchedule.get(String.valueOf(employee.getOID())) == null) {
             lstLinkData.add(String.valueOf(employee.getOID()) + "','" + employee.getEmployeeNum() + "','" + employee.getFullName() + "','" + employee.getDepartmentId());
             }
             else {
             lstLinkData.add("");
             }
             */
            Employee employee = (Employee) objectClass.get(i);
            Vector rowx = new Vector();
            if (hEmpSchedule.get(String.valueOf(employee.getOID())) == null) {
                rowx.add(employee.getEmployeeNum());
                rowx.add(employee.getFullName());
                rowx.add(hDept.get(String.valueOf(employee.getDepartmentId())));
                rowx.add(hSec.get(String.valueOf(employee.getSectionId())));
                lstData.add(rowx);
                lstLinkData.add(String.valueOf(employee.getOID()) + "','" + employee.getEmployeeNum() + "','" + employee.getFullName() + "','" + employee.getDepartmentId());
            }
        }

        ctrlist.setLinkSufix("')");
        return ctrlist.draw();
    }
%>

<%
    //String emp_number = request.getParameter("emp_number");
    //String emp_fullname = request.getParameter("emp_fullname");
    String emp_department = request.getParameter("emp_department");
    int iCommand = FRMQueryString.requestCommand(request);


    String name = FRMQueryString.requestString(request, "name");
    String empnum = FRMQueryString.requestString(request, "empnum");
    long department = FRMQueryString.requestLong(request, "department");
    long section = FRMQueryString.requestLong(request, "section");
    long empCategory = FRMQueryString.requestLong(request, "empCategory");
    long position = FRMQueryString.requestLong(request, "position");



    //String emp_section = request.getParameter("emp_section");
    String emp_period = "0";

    //out.println(emp_number);
    //out.println(emp_fullname);
    //out.println(emp_department);

    String whereClause = "";
    String whereClause1 = "";
    String whereClause2 = "";
    String whereClause3 = "";
    String whereClause4 = "";

    /* if (emp_number.compareToIgnoreCase("") != 0) {
     whereClause1 = PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM] + " LIKE '%" + emp_number + "%' ";
     }*/
    /*if (emp_fullname.compareToIgnoreCase("") != 0) {
     whereClause2 = PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME] + " LIKE '%" + emp_fullname + "%' ";
     }*/
    if (emp_department.compareToIgnoreCase("0") != 0) {
        whereClause3 = PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID] + " = " + emp_department + " ";
    }
    // untuk permintaan Intimas, nambah parameter section
    // updated by yunny
        /*if (emp_section.compareToIgnoreCase("0") != 0) {
     whereClause4 = PstEmployee.fieldNames[PstEmployee.FLD_SECTION_ID] + " = " + emp_section + " ";
     }*/


    if (whereClause3.length() > 0) {
        if (whereClause.length() > 0) {
            whereClause += " AND " + whereClause3;
        } else {
            whereClause = whereClause3 + " AND ";
        }
    }


    //out.println(whereClause);

    whereClause += PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED] + " = 0 ";

    if (empnum.length() > 0) {
        whereClause += " AND " + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM] + " LIKE  '%" + empnum + "%'";
    }
    if (name.length() > 0) {
        whereClause += " AND " + PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME] + " LIKE  '%" + name + "%'";
    }
    if (department > 0) {
        whereClause += " AND " + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID] + " = " + department + " ";
    }
    if (section > 0) {
        whereClause += " AND " + PstEmployee.fieldNames[PstEmployee.FLD_SECTION_ID] + " = " + section + " ";
    }

    if (empCategory > 0) {
        whereClause += " AND " + PstEmployee.fieldNames[PstEmployee.FLD_EMP_CATEGORY_ID] + " = " + empCategory + " ";
    }
    int start = 0;
    int recordToGet = 0;
    String orderClause = "";
    Vector listEmp = new Vector(1, 1);
    if (iCommand == Command.LIST){
        
    listEmp = PstEmployee.list(start, recordToGet, whereClause, orderClause);
    }

%>
<html>
    <head><title>Search Employee</title>
        <script language="javascript">
            function cmdEdit(oid, number, fullname, department) {
                //alert(oid + " " + number + " " + fullname);
                self.opener.document.frm_pay_emp_level.department.value = department;
                self.opener.document.frm_pay_emp_level.payrollNum.value = number;
                self.opener.document.frm_pay_emp_level.command.value = "<%=Command.ADD%>";                 
                //self.close();
                self.opener.document.frm_pay_emp_level.submit();
            }
            
            function cmdSearch() {
                document.searchEmp.command.value = "<%=String.valueOf(Command.LIST)%>";									
                document.searchEmp.action = "empsearch.jsp";
                document.searchEmp.submit();
            }
        </script>
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
        <link rel="stylesheet" href="../../styles/main.css" type="text/css">
        <link rel="stylesheet" href="../../styles/tab.css" type="text/css">
    </head>
    <body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
        <table width="100%" border="0" cellspacing="0" cellpadding="0" height="100%" bgcolor="#FCFDEC" >
            <%-- <tr> 
              <td ID="TOPTITLE" background="<%=approot%>/images/HRIS_HeaderBg3.jpg" width="100%" height="54"> 
                <%@ include file = "../../main/header.jsp" %>
            </tr> --%>
            <tr> 
                <td width="88%" valign="top" align="left"> 
                    <table width="100%" border="0" cellspacing="1" cellpadding="1">
                        <tr> 
                            <td width="100%"> 
                                <table width="100%" border="0" cellspacing="0" cellpadding="0">
                                    <tr> 
                                        <td height="20"> <font color="#FF6600" face="Arial"><strong>Employee</strong></font></td>
                                    </tr>
                                    <tr> 
                                        <td> 
                                            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                                                <tr> 
                                                    <td class="tablecolor"> 
                                                        <table width="100%" border="0" cellspacing="1" cellpadding="1" class="tablecolor">
                                                            <tr> 
                                                                <td valign="top"> 
                                                                    <table width="100%" border="0" cellspacing="1" cellpadding="1" class="tabbg">
                                                                        <tr> 
                                                                            <td valign="top">
                                                                                <table width="100%" border="0" cellspacing="0" cellpadding="0">
                                                                                    <tr align="left" valign="top"> 
                                                                                        <td height="8"  colspan="3"> 
                                                                                            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                                                                                                <tr align="left" valign="top"> 
                                                                                                    <td height="22" valign="middle"> 
                                                                                                        <form name="searchEmp" method="post" action="">

                                                                                                            <input type="hidden" name="command" value="<%=String.valueOf(iCommand)%>">
                                                                                                            <input type="hidden" name="start" value="<%=String.valueOf(start)%>">
                                                                                                            <input type="hidden" name="emp_department" value="<%=String.valueOf(emp_department)%>">
                                                                                                            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                                                                                                                <tr>
                                                                                                                    <td>
                                                                                                                        <table width="100%" border="0" cellspacing="2" cellpadding="2">
                                                                                                                            <tr> 
                                                                                                                                <td width="19%">Name</td>
                                                                                                                                <td width="1%">:</td>
                                                                                                                                <td width="80%"> 
                                                                                                                                    <input type="text" name="name"  value="<%=name%>" class="elemenForm" size="40">
                                                                                                                                </td>
                                                                                                                            </tr>
                                                                                                                            <tr> 
                                                                                                                                <td width="19%">Payroll Number</td>
                                                                                                                                <td width="1%">:</td>
                                                                                                                                <td width="80%"> 
                                                                                                                                    <input type="text" name="empnum"  value="<%=String.valueOf(empnum)%>" class="elemenForm">
                                                                                                                                </td>
                                                                                                                            </tr>

                                                                                                                            <tr> 
                                                                                                                                <td width="19%">Category</td>
                                                                                                                                <td width="1%">:</td>
                                                                                                                                <td width="80%"> 
                                                                                                                                    <%
                                                                                                                                        Vector cat_value = new Vector(1, 1);
                                                                                                                                        Vector cat_key = new Vector(1, 1);
                                                                                                                                        cat_value.add("0");
                                                                                                                                        cat_key.add("all category ...");
                                                                                                                                        Vector listCat = PstEmpCategory.list(0, 0, "", " EMP_CATEGORY ");
                                                                                                                                        for (int i = 0; i < listCat.size(); i++) {
                                                                                                                                            EmpCategory cat = (EmpCategory) listCat.get(i);
                                                                                                                                            cat_key.add(cat.getEmpCategory());
                                                                                                                                            cat_value.add(String.valueOf(cat.getOID()));
                                                                                                                                        }
                                                                                                                                    %>
                                                                                                                                    <%= ControlCombo.draw("empCategory", "formElemen", null, String.valueOf(empCategory), cat_value, cat_key, "")%> </td>
                                                                                                                            </tr>
                                                                                                                            <tr> 
                                                                                                                                <td width="19%"><%=dictionaryD.getWord(I_Dictionary.DEPARTMENT)%></td>
                                                                                                                                <td width="1%">:</td>
                                                                                                                                <td width="80%"> 
                                                                                                                                    <%
                                                                                                                                        Vector dept_value = new Vector(1, 1);
                                                                                                                                        Vector dept_key = new Vector(1, 1);
                                                                                                                                        Vector listDept = new Vector(1, 1);
                                                                                                                                        if (processDependOnUserDept) {
                                                                                                                                            if (emplx.getOID() > 0) {
                                                                                                                                                if (isHRDLogin || isEdpLogin || isGeneralManager) {
                                                                                                                                                    dept_value.add("0");
                                                                                                                                                    dept_key.add("select ...");
                                                                                                                                                    listDept = PstDepartment.list(0, 0, "", "DEPARTMENT");
                                                                                                                                                } else {
                                                                                                                                                    String whereClsDep = "(DEPARTMENT_ID = " + departmentOid + ")";
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

                                                                                                                                                    listDept = PstDepartment.list(0, 0, whereClsDep, "");
                                                                                                                                                }
                                                                                                                                            } else {
                                                                                                                                                dept_value.add("0");
                                                                                                                                                dept_key.add("select ...");
                                                                                                                                                listDept = PstDepartment.list(0, 0, "", "DEPARTMENT");
                                                                                                                                            }
                                                                                                                                        } else {
                                                                                                                                            dept_value.add("0");
                                                                                                                                            dept_key.add("select ...");
                                                                                                                                            listDept = PstDepartment.list(0, 0, "", "DEPARTMENT");
                                                                                                                                        }

                                                                                                                                        for (int i = 0; i < listDept.size(); i++) {
                                                                                                                                            Department dept = (Department) listDept.get(i);
                                                                                                                                            dept_key.add(dept.getDepartment());
                                                                                                                                            dept_value.add(String.valueOf(dept.getOID()));
                                                                                                                                        }

                                                                                                                                        //String sSelectedDepartment = iCommand==Command.GOTO ? ""+lDepartmentOid : srcEmpSchedule.getDepartment();
                                                                                                                                    %>
                                                                                                                                    <%//= ControlCombo.draw("DEPARTMENT_ID","formElemen",null, selectDept, dept_value, dept_key, "onchange=\"javascript:deptChange();\"") %>
                                                                                                                                    <%= ControlCombo.draw("department", "formElemen", null, String.valueOf(department), dept_value, dept_key, "onChange=\"javascript:cmdUpdateDep()\"")%> </td>
                                                                                                                            </tr>
                                                                                                                            <tr> 
                                                                                                                                <td width="19%"><%=dictionaryD.getWord(I_Dictionary.SECTION)%></td>
                                                                                                                                <td width="1%">:</td>
                                                                                                                                <td width="80%"> 
                                                                                                                                    <%
                                                                                                                                        Vector sec_value = new Vector(1, 1);
                                                                                                                                        Vector sec_key = new Vector(1, 1);
                                                                                                                                        sec_value.add("0");
                                                                                                                                        sec_key.add("all section ...");
                                                                                                                                        String strWhere = PstSection.fieldNames[PstSection.FLD_DEPARTMENT_ID] + "=" + department;
                                                                                                                                        Vector listSec = PstSection.list(0, 0, strWhere, " DEPARTMENT_ID, SECTION ");
                                                                                                                                        for (int i = 0; i < listSec.size(); i++) {
                                                                                                                                            Section sec = (Section) listSec.get(i);
                                                                                                                                            sec_key.add(sec.getSection());
                                                                                                                                            sec_value.add(String.valueOf(sec.getOID()));
                                                                                                                                        }
                                                                                                                                    %>
                                                                                                                                    <%= ControlCombo.draw("section", "formElemen", null, String.valueOf(section), sec_value, sec_key, "")%> </td>
                                                                                                                            </tr>
                                                                                                                            <tr> 
                                                                                                                                <td width="19%">Position</td>
                                                                                                                                <td width="1%">:</td>
                                                                                                                                <td width="80%"> 
                                                                                                                                    <%
                                                                                                                                        Vector pos_value = new Vector(1, 1);
                                                                                                                                        Vector pos_key = new Vector(1, 1);
                                                                                                                                        pos_value.add("0");
                                                                                                                                        pos_key.add("all position ...");
                                                                                                                                        Vector listPos = PstPosition.list(0, 0, "", " POSITION ");
                                                                                                                                        for (int i = 0; i < listPos.size(); i++) {
                                                                                                                                            Position pos = (Position) listPos.get(i);
                                                                                                                                            pos_key.add(pos.getPosition());
                                                                                                                                            pos_value.add(String.valueOf(pos.getOID()));
                                                                                                                                        }
                                                                                                                                    %>
                                                                                                                                    <%= ControlCombo.draw("position", "formElemen", null, String.valueOf(position), pos_value, pos_key, "")%> </td>
                                                                                                                            </tr>

                                                                                                                            <input type="hidden" name="resign" value="0">
                                                                                                                            <tr> 
                                                                                                                                <td width="19%">&nbsp;</td>
                                                                                                                                <td width="1%">&nbsp;</td>
                                                                                                                                <td width="80%"> 
                                                                                                                                    <input type="submit" name="Submit" value="Search Employee" onClick="javascript:cmdSearch()">
                                                                                                                                    <!--  <input type="submit" name="Submit" value="Add Employee" onClick="javascript:cmdAdd()">
                                                                                                                                    -->
                                                                                                                                </td>
                                                                                                                            </tr>

                                                                                                                        </table>
                                                                                                                    </td>
                                                                                                                </tr>
                                                                                                                <tr>
                                                                                                                    <td>

                                                                                                                        <table width="100%" border="0" cellspacing="0" cellpadding="0">
                                                                                                                            <%if ((listEmp != null) && (listEmp.size() > 0)) {%>
                                                                                                                            <tr> 
                                                                                                                                <td height="8" width="100%"><%=drawList(listEmp, emp_period)%></td>
                                                                                                                            </tr>
                                                                                                                            <%} else {%>
                                                                                                                            <tr> 
                                                                                                                                <td height="8" width="100%" class="comment"><span class="comment"><br>
                                                                                                                                        &nbsp;No Employee available</span> 
                                                                                                                                </td>
                                                                                                                            </tr>
                                                                                                                            <%}%>
                                                                                                                        </table>
                                                                                                                    </td>
                                                                                                                </tr>
                                                                                                            </table>
                                                                                                        </form>    

                                                                                                    </td>
                                                                                                </tr>
                                                                                                <%--
                                                                                                <tr align="left" valign="top"> 
                                                                                                  <td height="22" valign="middle"> 
                                                                                                  &nbsp;&nbsp;&nbsp;<b>Note</b> : <br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;* - this employee already had a schedule
                                                                                                  </td>
                                                                                                </tr>
                                                                                                --%>
                                                                                            </table>
                                                                                        </td>
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
                <td colspan="2" height="20" bgcolor="#15A9F5">
                    <%@ include file = "../../main/footer.jsp" %>
                </td>
            </tr>
        </table>

    </body>
</html>
