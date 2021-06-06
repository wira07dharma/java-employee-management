<%-- 
    Document   : config_potongan_excel
    Created on : 17-May-2018, 09:57:41
    Author     : Gunadi
--%>

<%@page import="com.dimata.util.Formater"%>
<%@page import="com.dimata.util.Command"%>
<%@page import="com.dimata.harisma.entity.employee.PstEmployee"%>
<%@page import="com.dimata.harisma.entity.masterdata.PstDivision"%>
<%@page import="com.dimata.system.entity.PstSystemProperty"%>
<%@page import="com.dimata.harisma.entity.employee.Employee"%>
<%@page import="java.util.Vector"%>
<%@page import="com.dimata.qdep.form.FRMQueryString"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="com.dimata.qdep.db.DBHandler"%>
<%@page import="com.dimata.qdep.db.DBResultSet"%>
<%@page import="com.dimata.harisma.entity.payroll.PstPayComponent"%>
<%@page import="com.dimata.harisma.entity.payroll.PayComponent"%>
<%@page import="com.dimata.harisma.entity.log.ChangeValue"%>
<%@page import="com.dimata.harisma.entity.payroll.PayConfigPotongan"%>
<%@page import="com.dimata.harisma.entity.payroll.PstPayConfigPotongan"%>
<%@ include file = "../../../main/javainit.jsp" %>
<%@page contentType="application/x-msexcel" pageEncoding="UTF-8"%>
<%!
    String[] statusValue = {"Non-Active", "Active"};
    
    public String getComponentName(long componentId){
        String str = "";
        try {
            PayComponent payComp = PstPayComponent.fetchExc(componentId);
            str = payComp.getCompName();
        } catch(Exception e){
            System.out.println(e.toString());
        }
        return str;
    }
%>
<%
    response.setHeader("Content-Disposition","attachment; filename=Employee_deduction.xls ");

    int iCommand = FRMQueryString.requestCommand(request);
    String srcNrk = FRMQueryString.requestString(request, "src_nrk");
    String srcName = FRMQueryString.requestString(request, "src_name");
    long srcDivision = FRMQueryString.requestLong(request, "src_division");
    long srcComponent = FRMQueryString.requestLong(request, "src_component");
    long srcCompany = FRMQueryString.requestLong(request, "src_company");
    long srcDepartment = FRMQueryString.requestLong(request, "src_department");
    long srcSection = FRMQueryString.requestLong(request, "src_section");
    long srcPayGroup = FRMQueryString.requestLong(request, "src_paygroup");
    int statusActive = FRMQueryString.requestInt(request, "src_status");
    long oid = FRMQueryString.requestLong(request, "oid");
    /* get data form */
    long empId = FRMQueryString.requestLong(request, "emp_id");
    long compId = FRMQueryString.requestLong(request, "comp_id");
    String startDate = FRMQueryString.requestString(request, "start_date");
    String endDate = FRMQueryString.requestString(request, "end_date");
    double angsuran = FRMQueryString.requestDouble(request, "angsuran");
    String rekening = FRMQueryString.requestString(request, "rekening");
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    
    /* get data user login*/
    long employeeId = emplx.getOID();
    long divisionId = 0;
    long sdmDivisionOid = Long.parseLong(String.valueOf(PstSystemProperty.getPropertyLongbyName(OID_SDM_DIVISION)));
    String clientName = PstSystemProperty.getValueByName("CLIENT_NAME");
    if (emplx.getDivisionId() == sdmDivisionOid){
        divisionId = 0;
    } else {
        divisionId = emplx.getDivisionId();
    }
    
    String whereClause = PstDivision.fieldNames[PstDivision.FLD_VALID_STATUS]+"="+PstDivision.VALID_ACTIVE
            + " AND "+PstDivision.fieldNames[PstDivision.FLD_COMPANY_ID]+"="+srcCompany;
    if (divisionId != 0){
        whereClause += " AND "+PstDivision.fieldNames[PstDivision.FLD_DIVISION_ID]+"="+divisionId;
    }
    
    Vector divisionList = PstDivision.list(0, 0, whereClause, "");
    ChangeValue changeValue = new ChangeValue();
    whereClause = "";
    /* pencarian where default utk potongan */
    if (divisionId != 0){
        whereClause = PstEmployee.fieldNames[PstEmployee.FLD_DIVISION_ID]+"="+divisionId;
        Vector empList = PstEmployee.list(0, 0, whereClause, "");
        String employeeIds = "";
        if (empList != null && empList.size()>0){
            if (empList.size() > 1){
                for (int e=0; e<empList.size(); e++){
                    Employee emp = (Employee)empList.get(e);
                    employeeIds += emp.getOID()+",";
                }
                employeeIds = " IN (" + employeeIds.substring(0, employeeIds.length()-1)+")";
            } else {
                Employee emp = (Employee)empList.get(0);
                employeeIds = "="+ emp.getOID();
            }
        }
        whereClause = PstPayConfigPotongan.fieldNames[PstPayConfigPotongan.FLD_EMPLOYEE_ID]+" "+employeeIds;
    }
    
    if (iCommand == Command.LIST){
        if (srcNrk != null && srcNrk.length()>0){
            whereClause = PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]+"='"+srcNrk+"'";
        }
        if (srcName != null && srcName.length()>0){
            whereClause = PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]+" LIKE '%"+srcName+"%'";
        }
        
        if (srcCompany != 0){
            whereClause = PstEmployee.fieldNames[PstEmployee.FLD_COMPANY_ID]+"="+srcCompany;
        }
        
        if (srcDivision != 0){
            whereClause = PstEmployee.fieldNames[PstEmployee.FLD_DIVISION_ID]+"="+srcDivision;
        }

        if (srcDepartment != 0){
            whereClause = PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID]+"="+srcDepartment;
        }
        
        if (srcSection != 0){
            whereClause = PstEmployee.fieldNames[PstEmployee.FLD_SECTION_ID]+"="+srcSection;
        }
        
        if(srcPayGroup != 0){
            if (whereClause.equals("")){
                whereClause += PstEmployee.fieldNames[PstEmployee.FLD_PAYROLL_GROUP]+"="+srcPayGroup;                
            } else {
                whereClause += " AND " +PstEmployee.fieldNames[PstEmployee.FLD_PAYROLL_GROUP]+"="+srcPayGroup;                
            }
        }
                      
        Vector empList = PstEmployee.list(0, 0, whereClause, "");
        String employeeIds = "";
        if (empList != null && empList.size()>0){
            if (empList.size() > 1){
                for (int e=0; e<empList.size(); e++){
                    Employee emp = (Employee)empList.get(e);
                    employeeIds += emp.getOID()+",";
                }
                employeeIds = " IN (" + employeeIds.substring(0, employeeIds.length()-1)+")";
            } else {
                Employee emp = (Employee)empList.get(0);
                employeeIds = "="+ emp.getOID();
            }
        }
        
        whereClause = PstPayConfigPotongan.fieldNames[PstPayConfigPotongan.FLD_EMPLOYEE_ID]+" "+employeeIds;
        
        if (srcComponent != 0){
            whereClause = whereClause + " AND " + PstPayConfigPotongan.fieldNames[PstPayConfigPotongan.FLD_COMPONENT_ID]+ " = "+srcComponent;
        }
        
        whereClause += " AND " + PstPayConfigPotongan.fieldNames[PstPayConfigPotongan.FLD_VALID_STATUS]+"='"+statusActive+"'";
        
    }

    Vector payConfigList = PstPayConfigPotongan.list(0, 0, whereClause, "");
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <style type="text/css">
            .tblStyle {border-collapse: collapse; font-size: 12px; }
            .tblStyle td {padding: 5px 7px; border: 1px solid #CCC; font-size: 12px; }
            .title_tbl {font-weight: bold;background-color: #DDD; color: #575757;}
            .title_page {color:#0db2e1; font-weight: bold; font-size: 14px; background-color: #EEE; border-left: 1px solid #0099FF; padding: 12px 18px;}
            
            .tblForm td{
                padding: 5px 7px; 
                font-size: 12px;
                font-weight: bold;
            }
        </style>
    </head>
    <body>
        <%
            if ((iCommand == Command.LIST || iCommand == Command.EDIT || iCommand == Command.CANCEL || iCommand == Command.SAVE) && payConfigList.size()>0 ){
        %>
            <table class="tblStyle">
                    <tr>
                        <td class="title_tbl">No</td>
                        <td class="title_tbl"><%=(clientName.equals("BPD") ? "NRK" : "Payroll")%></td>
                        <td class="title_tbl">Nama</td>
                        <td class="title_tbl">Satuan Kerja</td>
                        <td class="title_tbl">Tanggal Berlaku</td>
                        <td class="title_tbl">Tanggal Berhenti</td>
                        <td class="title_tbl">Komponen Potongan</td>
                        <td class="title_tbl">Angsuran Perbulan</td>
                        <td class="title_tbl">No. Rekening</td>
                        <td class="title_tbl">Status</td>
                    </tr>
                    <%
                        if (payConfigList != null && payConfigList.size()>0){
                            double totalAngsuran = 0.0;
                            for(int i=0; i<payConfigList.size(); i++){
                                PayConfigPotongan payConfig = (PayConfigPotongan)payConfigList.get(i);
                                String nrk = "";
                                String nama = "";
                                String divisi = "";
                                try {
                                    Employee emp = PstEmployee.fetchExc(payConfig.getEmployeeId());
                                    nrk = emp.getEmployeeNum();
                                    nama = emp.getFullName();
                                    divisi = changeValue.getDivisionName(emp.getDivisionId());
                                } catch(Exception e){
                                    System.out.println(e.toString());
                                }
                                totalAngsuran = totalAngsuran + payConfig.getAngsuranPerbulan();
                    %>
                    <tr>
                        <td><%= (i+1) %></td>
                        <td><%= nrk %></td>
                        <td><%= nama %></td>
                        <td><%= divisi %></td>
                        <td><%= payConfig.getStartDate() %></td>
                        <td><%= payConfig.getEndDate() %></td>
                        <td><%= getComponentName(payConfig.getComponentId()) %></td>
                        <td><%= Formater.formatNumberMataUang(payConfig.getAngsuranPerbulan(), "Rp")  %></td>
                        <td><%= payConfig.getNoRekening() %></td>
                        <td><%= (payConfig.getValidStatus() == 1 ? "Active" : "Non-Active") %>
                        </td>
                    </tr>
                    <%
                            }
                    %>
                    <tr>
                        <td colspan="7" rowspan="1" style="text-align: right;"><strong>Total</strong></td>
                        <td><%= Formater.formatNumberMataUang(totalAngsuran, "Rp")  %></td>
                        <td></td>
                        <td></td>
                    </tr>
                    
                    <%
                            
                        }
                    %>
                </table>
        <%
            }
        %>
    </body>
</html>
