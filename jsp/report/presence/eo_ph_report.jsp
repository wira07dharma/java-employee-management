<%-- 
    Document   : eo_ph_report
    Created on : 12-Dec-2017, 15:48:58
    Author     : Gunadi
--%>

<%@page import="com.dimata.harisma.session.attendance.PresenceReportDaily"%>
<%@page import="com.dimata.harisma.session.attendance.SessEmpSchedule"%>
<%@page import="com.dimata.harisma.entity.attendance.PstEmpSchedule"%>
<%@page import="com.dimata.gui.jsp.ControlList"%>
<%@page import="com.dimata.gui.jsp.ControlDate"%>
<%@page import="com.dimata.gui.jsp.ControlCombo"%>
<%@page import="com.dimata.qdep.form.FRMQueryString"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.util.Vector"%>
<%@ include file = "../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_MASTERDATA, AppObjInfo.G2_MD_COMPANY, AppObjInfo.OBJ_PUBLIC_HOLIDAY); %>
<%@ include file = "../../main/checkuser.jsp" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%
    int iCommand = FRMQueryString.requestCommand(request);
    int start = FRMQueryString.requestInt(request, "start");
    int prevCommand = FRMQueryString.requestInt(request, "prev_command");
    int iLevelDw = FRMQueryString.requestInt(request, "levelDW");
    Date selectedDateFrom = FRMQueryString.requestDate(request, "check_date_start");
    Date selectedDateTo = FRMQueryString.requestDate(request, "check_date_finish");
    
    Vector listPH = new Vector();
    Vector listLevel = new Vector();
            
    if(iCommand == Command.LIST){
        String wherePH = PstPublicHolidays.fieldNames[PstPublicHolidays.FLD_HOLIDAY_DATE]
            + " BETWEEN '" + Formater.formatDate(selectedDateFrom, "yyyy-MM-dd")+"' AND '"
            + Formater.formatDate(selectedDateTo, "yyyy-MM-dd")+"'";
        
        String order = PstPublicHolidays.fieldNames[PstPublicHolidays.FLD_HOLIDAY_DATE];
        
        listPH = PstPublicHolidays.listPublicHolidayUnion(selectedDateFrom,selectedDateTo);
        
        String whereLevel = PstLevel.fieldNames[PstLevel.FLD_ENTITLE_PH]+" = 1 ";
        listLevel = PstLevel.list(0, 0, whereLevel, "");
    }
    
%>
<html>
    <head>
        <script language="JavaScript">
            function cmdSearch(){
                document.frmeoph.command.value="<%=String.valueOf(Command.LIST)%>";
                document.frmeoph.action="eo_ph_report.jsp";
                document.frmeoph.submit();
                
            }
            
            function cmdExport(){ 
                //var linkPage = "<//%=printroot%>.report.attendance.AttendanceSummaryXls";
                document.frmeoph.action="export_excel/export_excel_eo_ph_report.jsp"; 
                document.frmeoph.submit();
                
            }
        </script>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Report EO/PH</title>
        <!-- #EndEditable -->
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
        <!-- #BeginEditable "styles" -->
        <link rel="stylesheet" href="../../styles/main.css" type="text/css">
        <!-- #EndEditable --> <!-- #BeginEditable "stylestab" -->
        <link rel="stylesheet" href="../../styles/tab.css" type="text/css">
        <!-- #EndEditable --> <!-- #BeginEditable "headerscript" -->
        <!-- #EndEditable -->
        <style type="text/css">
            #menu_utama {color: #0066CC; font-weight: bold; padding: 5px 14px; border-left: 1px solid #0066CC; font-size: 14px; background-color: #F7F7F7;}
            #mn_utama {color: #FF6600; padding: 5px 14px; border-left: 1px solid #999; font-size: 14px; background-color: #F5F5F5;}
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
            #tdForm {
                padding: 5px;
            }
            .tableScroll{}
            #tbl {border-collapse: collapse;font-size: 11px;display: inline-block; height: 40%; width: 40%;overflow: auto;}
            .tblStyle td {padding: 3px 5px; border: 1px solid #CCC;}
            .title_tbl {font-weight: bold;background-color: #ffffff; color: #575757; width: 100%; height: 100%}
        </style>
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
    <td width="88%" valign="top" align="left">
      <table width="100%" border="0" cellspacing="3" cellpadding="2">
        <tr>
          <td width="100%">
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
              <tr>
                <td height="20"> <font color="#FF6600" face="Arial"><strong> <!-- #BeginEditable "contenttitle" -->
                  Report EO/PH <!-- #EndEditable -->
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
                                    <form name="frmeoph" method ="post" action="">
                                      <input type="hidden" name="command" value="<%=iCommand%>">
                                      <input type="hidden" name="start" value="<%=start%>">
                                      <input type="hidden" name="prev_command" value="<%=prevCommand%>">
                                      <table width="100%" border="0" cellspacing="0" cellpadding="0">
                                        <tr align="left" valign="top">
                                          <td height="8"  colspan="3">
                                            <table width="100%" border="0" cellspacing="2" cellpadding="2">
                                              <tr> 
                                                <td width="1%" align="right" nowrap> <div align=left>Date</div>  </td>
                                                <td width="30%" nowrap="nowrap">:
                                                  <%=ControlDate.drawDateWithStyle("check_date_start", selectedDateFrom != null ? selectedDateFrom : new Date(), 2, -5, "formElemen", " onkeydown=\"javascript:fnTrapKD()\"")%> 
                                                to <%=ControlDate.drawDateWithStyle("check_date_finish", selectedDateTo != null ? selectedDateTo : new Date(), 2, -5, "formElemen", " onkeydown=\"javascript:fnTrapKD()\"")%>
                                              </tr>
                                              <tr> 
                                                <td height="13" width="0%">&nbsp;</td>
                                                <td width="39%"><a href="javascript:cmdSearch()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image10','','<%=approot%>/images/BtnSearchOn.jpg',1)" id="aSearch"><img name="Image10" border="0" src="<%=approot%>/images/BtnSearch.jpg" width="24" height="24" alt="Search Employee"></a>
                                                   <img src="<%=approot%>/images/spacer.gif" width="6" height="1">
                                                  <a href="javascript:cmdSearch()">Search</a></td>  
                                              </tr>
                                            </table>
                                          </td>
                                        </tr>
                                        <%if(iCommand==Command.LIST){%> 
                                        <tr>
                                            <td colspan="7">
                                                <%
                                                    if(listPH != null && listPH.size() > 0){
                                                %>
                                                <table class="listgen" width="50%" cellspacing="1">
                                                    <tr>
                                                        <td class="listgentitle" width="5%" style="text-align: center">No</td>
                                                        <td class="listgentitle" width="15%" style="text-align: center">PH Date</td>
                                                        <td class="listgentitle" width="15%" style="text-align: center">PH Name</td>
                                                        <td class="listgentitle" width="10%" style="text-align: center">Total Staff Working</td>
                                                        <td class="listgentitle" width="10%" style="text-align: center">Staff Level</td>
                                                        <td class="listgentitle" width="10%" style="text-align: center">Staff</td>
                                                        <td class="listgentitle" width="10%" style="text-align: center">Entitle Days</td>
                                                        <td class="listgentitle" width="10%" style="text-align: center">Total Entitle</td>
                                                    </tr>
                                                    
                                                <%
                                                    for(int i=0; i<listPH.size();i++){
                                                        PublicHolidays publicHolidays = (PublicHolidays)listPH.get(i);
                                                        int level = listLevel.size();
                                                        int totalStaff= 0;
                                                        int totalEnt = 0;
                                                        
                                                        HashMap<Long,Integer> mapStaff = new HashMap<Long, Integer>();
                                                        HashMap<Long,Integer> mapEntitle = new HashMap<Long, Integer>();
                                                        for(int x=0; x < listLevel.size();x++){
                                                            int jmlStaff = 0;
                                                            int jmlEnt = 0;
                                                            Level lvl = (Level) listLevel.get(x);
                                                            String whereClause = PstEmployee.fieldNames[PstEmployee.FLD_LEVEL_ID]+"="+lvl.getOID()+" AND ("
                                                                                + PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED]+" = 0 OR "+
                                                                                PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED_DATE]+" > '"+publicHolidays.getDtHolidayDate()+"')";
                                                            Vector listEmployee = PstEmployee.list(0, 0, whereClause, "");
                                                            if (listEmployee != null && listEmployee.size() > 0){
                                                                for (int xx=0; xx < listEmployee.size(); xx++){
                                                                    Employee emp = (Employee) listEmployee.get(xx);
                                                                    Vector dPresence = SessEmpSchedule.listEmpPresenceDaily(0, publicHolidays.getDtHolidayDate(), 0, emp.getEmployeeNum(), emp.getFullName(), "", 0, 0, "", null, 0, "", 2, 0, 0);
//                                                                    boolean isPresence = SessEmpSchedule.getCheckAdaDataPresence(0, publicHolidays.getDtHolidayDate(), publicHolidays.getDtHolidayDate(), 0, emp.getEmployeeNum(), emp.getFullName(), "", 0, 0, null, 0, "", 0, 0, 0);
//                                                                    if (isPresence){
//                                                                        jmlStaff = jmlStaff + 1;
//                                                                        totalStaff = totalStaff + 1;
//                                                                    }
                                                                    if (dPresence != null && dPresence.size()>0){
                                                                        PresenceReportDaily presenceReportDaily = (PresenceReportDaily) dPresence.get(0);
                                                                        Date dtActualIn1st = (Date) presenceReportDaily.getActualIn1st();
                                                                        Date dtActualOut1st = (Date) presenceReportDaily.getActualOut1st();
                                                                        if (dtActualIn1st != null || dtActualOut1st != null){
                                                                            jmlStaff = jmlStaff + 1;
                                                                            totalStaff = totalStaff + 1;
                                                                            jmlEnt = jmlEnt + lvl.getEntitledPHQty();
                                                                        }
                                                                    }
                                                                }
                                                                totalEnt = totalEnt + jmlEnt;
                                                                mapStaff.put(lvl.getOID(),jmlStaff);
                                                                mapEntitle.put(lvl.getOID(),jmlEnt);
                                                            }
                                                            
                                                        }
                                                        
                                                %>
                                                    <tr>
                                                        <td rowspan="<%=level%>" class="listgensell"><%=(i+1)%></td>
                                                        <td rowspan="<%=level%>" class="listgensell"><%=publicHolidays.getDtHolidayDate()%></td>
                                                        <td rowspan="<%=level%>" class="listgensell"><%=publicHolidays.getStDesc()%></td>
                                                        <% Level lvl1 = new Level();
                                                            try { 
                                                                lvl1 = (Level) listLevel.get(0);
                                                            } catch (Exception exc){} 
                                                            
                                                        %>
                                                        <td rowspan="<%=level%>" class="listgensell"><%=totalStaff%></td>
                                                        <td class="listgensell"><%=lvl1.getLevel()%></td>
                                                        <td class="listgensell"><%=mapStaff.get(lvl1.getOID())%></td>
                                                        <td class="listgensell"><%=mapEntitle.get(lvl1.getOID())%></td>
                                                        <td rowspan="<%=level%>" class="listgensell"><%=totalEnt%></td>
                                                    </tr>
                                                    <% for(int x=1; x < listLevel.size();x++){ 
                                                        Level lvl2 = (Level) listLevel.get(x);
    
                                                    %>
                                                    <tr>
                                                        <td class="listgensell"><%=lvl2.getLevel()%></td>
                                                        <td class="listgensell"><%=mapStaff.get(lvl2.getOID())%></td>
                                                        <td class="listgensell"><%=mapEntitle.get(lvl2.getOID())%></td>
                                                    </tr>
                                                    <% } %>
                                                <%
                                                    
                                                    }
                                                %>
                                                </table>
                                                <tr> 
                                                                                            
                                                        <td width="30%"> 
                                                            <table border="0" cellspacing="0" cellpadding="0" width="137">
                                                                <tr> 
                                                                    <td width="16"><a href="javascript:cmdExport()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image10','','<%=approot%>/images/excel.png',1)" id="aSearch"><img name="Image10" border="0" src="<%=approot%>/images/excel.png" width="24" height="24" alt="Export Excel"></a></td>
                                                                    <td width="2"><img src="<%=approot%>/images/spacer.gif" width="4" height="1"></td>
                                                                    <td width="94" class="command" nowrap><a href="javascript:cmdExport()">Export Excel</a></td>
                                                                </tr>
                                                            </table></td>
                                                    </tr>
                                                
                                                <%
                                                    }
                                                %>
                                            </td>  
                                        </tr>
                                        <% } %>
                                        <tr> 
                                            <td width="1%">&nbsp;</td>
                                            <td width="7%">&nbsp;</td>
                                            <td width="92%">&nbsp;</td>
                                        </tr>
                                        <tr align="left" valign="top" > 
                                          <td colspan="4"> 
                                            <div align="left"></div>
                                          </td>
                                        </tr>
                                            </table>
                                              </form>
                                          </td>
                                        </tr>
                                      </table>
                                    
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
</html>
