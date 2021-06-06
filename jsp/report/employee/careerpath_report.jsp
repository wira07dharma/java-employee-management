<%-- 
    Document   : careerpath_report
    Created on : Jul 16, 2018, 1:05:50 PM
    Author     : dimata005
--%>

<%@page import="com.dimata.gui.jsp.ControlCombo"%>
<%@ include file = "../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_MASTERDATA, AppObjInfo.G2_MD_COMPANY, AppObjInfo.OBJ_PUBLIC_HOLIDAY); %>
<%@ include file = "../../main/checkuser.jsp" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    int iCommand = FRMQueryString.requestCommand(request);
    int start = FRMQueryString.requestInt(request, "start");
    int prevCommand = FRMQueryString.requestInt(request, "prev_command");
    
    long oidCompany= FRMQueryString.requestLong(request, "oidCompany"); 
    long oidDivision = FRMQueryString.requestLong(request, "division");
    long oidDepartment = FRMQueryString.requestLong(request, "department");
    long oidSection = FRMQueryString.requestLong(request, "section");
%>
<!DOCTYPE html>
<html>
    <head>
        <script language="JavaScript">
            function cmdSearch(){
                document.frmlstcareer.command.value="<%=String.valueOf(Command.LIST)%>";
                document.frmlstcareer.action="export_excel/careerpath_report_xls.jsp";
                document.frmlstcareer.submit();
                
            }
            function cmdUpdateDiv(){
                    document.frmlstcareer.target="";
                    document.frmlstcareer.command.value="<%=String.valueOf(Command.GOTO)%>";
                    document.frmlstcareer.action="careerpath_report.jsp";        
                    document.frmlstcareer.submit();
            }

            function cmdUpdateDept(){
                     document.frmlstcareer.target="";
                    document.frmlstcareer.command.value="<%=String.valueOf(Command.GOTO)%>";
                    document.frmlstcareer.action="careerpath_report.jsp";        
                    document.frmlstcareer.submit();
            }

            function cmdUpdateSec(){
                     document.frmlstcareer.target="";
                    document.frmlstcareer.command.value="<%=String.valueOf(Command.GOTO)%>";
                    document.frmlstcareer.action="careerpath_report.jsp";        
                    document.frmlstcareer.submit();
            }
        </script>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>List Employee Mutation</title>
        <!-- #EndEditable -->
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
        <!-- #BeginEditable "styles" -->
        <link rel="stylesheet" href="../../styles/main.css" type="text/css">
        <!-- #EndEditable --> <!-- #BeginEditable "stylestab" -->
        <link rel="stylesheet" href="../../styles/tab.css" type="text/css">
    </head>
<body <%=noBack%> bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
    <table width="100%" border="0" cellspacing="0" cellpadding="0" height="100%" bgcolor="#F9FCFF" >
        <%if (headerStyle && !verTemplate.equalsIgnoreCase("0")) {%> 
        <%@include file="../../styletemplate/template_header.jsp" %>
        <%} else {%>
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
          <td width="88%" valign="top" align="left">
             <table width="100%" border="0" cellspacing="3" cellpadding="2">
                <tr>
                   <td width="100%">
                      <table width="100%" border="0" cellspacing="0" cellpadding="0">
                         <tr>
                            <td height="20"> <font color="#FF6600" face="Arial"><strong> <!-- #BeginEditable "contenttitle" -->
                                   List Employee Career Path <!-- #EndEditable -->
                                </strong></font> </td>
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
                                                      <form name="frmlstcareer" method ="post" action="">
                                                        <input type="hidden" name="command" value="<%=iCommand%>">
                                                        <input type="hidden" name="start" value="<%=start%>">
                                                        <input type="hidden" name="prev_command" value="<%=prevCommand%>">
                                                           <table width="100%" border="0" cellspacing="0" cellpadding="0">
                                                              <tr align="left" valign="top">
                                                                <td height="8"  colspan="3">
                                                                    <table width="100%" border="0" cellspacing="2" cellpadding="2">
                                                                        <tr> 
                                                                            <td width="1%" align="right" nowrap> <div align=left>Company</div>  </td>
                                                                            <td width="30%" nowrap="nowrap">:
                                                                                <%
                                                                                    Vector comp_value = new Vector(1,1);
                                                                                    Vector comp_key = new Vector(1,1);
                                                                                    comp_value.add("0");
                                                                                    comp_key.add("select ...");
                                                                                    Vector listComp = PstCompany.list(0, 0, "", " COMPANY ");
                                                                                    for (int i = 0; i < listComp.size(); i++) {
                                                                                            Company comp = (Company) listComp.get(i);
                                                                                            comp_key.add(comp.getCompany());
                                                                                            comp_value.add(String.valueOf(comp.getOID()));
                                                                                    }
                                                                                %> 
                                                                                <%= ControlCombo.draw("oidCompany","formElemen",null, ""+oidCompany, comp_value, comp_key, "onChange=\"javascript:cmdUpdateDiv()\"") %>
                                                                            </td>
                                                                        </tr>
                                                                        <tr> 
                                                                            <td width="1%" align="right" nowrap> <div align=left>Division</div></td>
                                                                            <td width="30%" nowrap="nowrap">:
                                                                                <%
                                                                                    String whereDiv ="";
                                                                                    if(oidCompany !=0){
                                                                                        whereDiv = PstDivision.fieldNames[PstDivision.FLD_COMPANY_ID]+" = "+oidCompany;
                                                                                    }
                                                                                    Vector listDivision = PstDivision.list(0, 0, whereDiv, "DIVISION");
                                                                                    Vector divValue = new Vector(1, 1);
                                                                                    Vector divKey = new Vector(1, 1);
                                                                                    divValue.add("0");
                                                                                    divKey.add("select ...");
                                                                                    for (int d = 0; d < listDivision.size(); d++) {
                                                                                        Division division = (Division) listDivision.get(d);
                                                                                        divValue.add("" + division.getOID());
                                                                                        divKey.add(division.getDivision());
                                                                                    }
                                                                                    out.println(ControlCombo.draw("division", null, "" + oidDivision, divValue, divKey, "onChange=\"javascript:cmdUpdateDept()\""));
                                                                                %>
                                                                            </td>
                                                                        </tr>
                                                                        <tr>
                                                                            <td width="6%" align="right" nowrap><div align="left">Department </div></td>
                                                                            <td width="30%" nowrap="nowrap">:
                                                                                <%
                                                                                    Vector dept_value = new Vector(1, 1);
                                                                                    Vector dept_key = new Vector(1, 1);
                                                                                    dept_value.add("0");
                                                                                    dept_key.add("select ...");
                                                                                    String whereDept= "" + (oidDivision != 0? PstDepartment.TBL_HR_DEPARTMENT +"." +PstDepartment.fieldNames[PstDepartment.FLD_DIVISION_ID]+"="+oidDivision : "");
                                                                                    Vector listDept = PstDepartment.list(0, 0, whereDept, " DEPARTMENT ");
                                                                                    for (int i = 0; i < listDept.size(); i++) {
                                                                                        Department dept = (Department) listDept.get(i);
                                                                                        dept_key.add(dept.getDepartment());
                                                                                        dept_value.add(String.valueOf(dept.getOID()));
                                                                                    }
                                                                                    out.println(ControlCombo.draw("department", null, "" + oidDepartment, dept_value, dept_key, "onChange=\"javascript:cmdUpdateSec()\""));
                                                                                %>
                                                                            </td>
                                                                        </tr>
                                                                        <tr>
                                                                            <td width="6%" align="right" nowrap><div align="left">Section </div></td>
                                                                            <td width="30%" nowrap="nowrap">:
                                                                                <%
                                                                                    Vector sec_value = new Vector(1, 1);
                                                                                    Vector sec_key = new Vector(1, 1);
                                                                                    sec_value.add("0");
                                                                                    sec_key.add("select ...");
                                                                                    String whereSec = "" + (oidDepartment==0? "" : PstSection.TBL_HR_SECTION+"."+PstSection.fieldNames[PstSection.FLD_DEPARTMENT_ID]+"="+oidDepartment);
                                                                                    Vector listSec = PstSection.list(0, 0, whereSec, " SECTION ");
                                                                                    for (int i = 0; i < listSec.size(); i++) {
                                                                                        Section sec = (Section) listSec.get(i);
                                                                                        sec_key.add(sec.getSection());
                                                                                        sec_value.add(String.valueOf(sec.getOID()));
                                                                                    }
                                                                                    out.println(ControlCombo.draw("section", null, "" + oidSection, sec_value, sec_key));
                                                                                %>
                                                                            </td>
                                                                        </tr>
                                                                        <tr>
                                                                            <td width="6%" align="right" nowrap><div align="left">Emp Payroll </div></td>
                                                                            <td width="30%" nowrap="nowrap">:
                                                                                <input type="text" name="searchNr" size="12">
                                                                            </td>
                                                                        </tr>
                                                                        <tr>
                                                                            <td width="6%" align="right" nowrap><div align="left">Emp Name </div></td>
                                                                            <td width="30%" nowrap="nowrap">:
                                                                                <input type="text" name="searchName" size="12">
                                                                            </td>
                                                                        </tr>
                                                                        <tr> 
                                                                            <td height="13" width="0%">&nbsp;</td>
                                                                            <td width="39%"><a href="javascript:cmdSearch()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image10','','<%=approot%>/images/BtnSearchOn.jpg',1)" id="aSearch"><img name="Image10" border="0" src="<%=approot%>/images/BtnSearch.jpg" width="24" height="24" alt="Search Employee"></a>
                                                                           <img src="<%=approot%>/images/spacer.gif" width="6" height="1">
                                                                          <a href="javascript:cmdSearch()">Search  for Employee</a></td>  
                                                                        </tr>
                                                                    </table>
                                                                </td>
                                                              </tr>
                                                           </table>
                                                      </form>
                                                    </td>
                                                  </tr>
                                                </table>
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
        <%if (headerStyle && !verTemplate.equalsIgnoreCase("0")) {%>
        <tr>
            <td valign="bottom">
                <!-- untuk footer -->
                <%@include file="../../footer.jsp" %>
            </td>

        </tr>
        <%} else {%>
        <tr> 
            <td colspan="2" height="20" > <!-- #BeginEditable "footer" --> 
                <%@ include file = "../../main/footer.jsp" %>
                <!-- #EndEditable --> </td>
        </tr>
        <%}%>
    </table>
</body>
</html>
