<%-- 
    Document   : thr_calculation_setup
    Created on : 28-Aug-2017, 14:34:09
    Author     : Gunadi
--%>
<%@page import="com.dimata.harisma.entity.payroll.PstThrCalculationMain"%>
<%@page import="com.dimata.harisma.entity.payroll.ThrCalculationMain"%>
<%@page import="com.dimata.gui.jsp.*"%>
<%@page import="com.dimata.harisma.form.payroll.*"%>
<%@ include file = "../../main/javainit.jsp" %>
<% 
int appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_REPORTS, AppObjInfo.G2_PAYROLL_REPORT, AppObjInfo.OBJ_CUSTOM_RPT_MAIN);
%>
<%@ include file = "../../main/checkuser.jsp" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%!
    public String drawList(Vector objectClass, long oidBenefitConfig) {

        ControlList ctrlist = new ControlList();

        ctrlist.setAreaWidth("100%");
        ctrlist.setListStyle("tblStyle");
        ctrlist.setTitleStyle("title_tbl");
        ctrlist.setCellStyle("listgensell");
        ctrlist.setHeaderStyle("title_tbl");
        ctrlist.setCellSpacing("0");
        ctrlist.addHeader("No", "");
        ctrlist.addHeader("Title", "");
        ctrlist.addHeader("Description", "");
        ctrlist.addHeader("Create Date", "");
        ctrlist.addHeader("Action", "");

        ctrlist.setLinkRow(1); // untuk menge-sett link di kolom pertama atau dikolom yg lain

        Vector lstData = ctrlist.getData();
        //Vector lstLinkData = ctrlist.getLinkData();
//        ctrlist.setLinkPrefix("javascript:cmdEdit('");
//        ctrlist.setLinkSufix("')");

        ctrlist.reset(); //berfungsi untuk menginisialisasi list menjadi kosong

        int no = 0;
        for (int i = 0; i < objectClass.size(); i++) {     
            ThrCalculationMain thrCalculationMain = (ThrCalculationMain) objectClass.get(i);
            // rowx will be created secara berkesinambungan base on i
            Vector rowx = new Vector();

            no = no + 1;
            rowx.add("" + no);
            rowx.add(""+thrCalculationMain.getCalculationMainTitle());
            rowx.add(""+thrCalculationMain.getCalculationMainDesc());
            rowx.add(Formater.formatDate(thrCalculationMain.getCalculationMainDateCreate(),"yyyy-MM-dd"));
            rowx.add("<a href=\"javascript:cmdEdit('"+thrCalculationMain.getOID()+"')\">Edit</a> | "
            + " <a href=\"javascript:cmdConfigure('"+thrCalculationMain.getOID()+"')\">Configure</a> | "
            + " <a href=\"javascript:cmdAsk('"+thrCalculationMain.getOID()+"')\">Delete</a>");
            lstData.add(rowx);
            //lstLinkData.add(String.valueOf(thrCalculationMain.getOID()));

        }

        return ctrlist.draw(); // mengembalikan data-data control list

    }

%>
<%
    int iCommand = FRMQueryString.requestCommand(request);
    long oidCustom = FRMQueryString.requestLong(request, "oid_custom");
    int start = FRMQueryString.requestInt(request, "start");
    int prevCommand = FRMQueryString.requestInt(request, "prev_command");
    
    /*variable declaration*/
    int recordToGet = 10;
    String msgString = "";
    int iErrCode = FRMMessage.NONE;
    String whereClause = "";
    String orderClause = " CALCULATION_MAIN_TITLE ASC";//
    
    CtrlThrCalculationMain ctrThrCalculationMain = new CtrlThrCalculationMain(request);
    ControlLine ctrLine = new ControlLine();
    Vector listThrCalculationMain = new Vector(1, 1);
    FrmThrCalculationMain frmThrCalculationMain = new FrmThrCalculationMain();
    frmThrCalculationMain = ctrThrCalculationMain.getForm();
    ThrCalculationMain thrCalculationMain = new ThrCalculationMain();
    
    /* code process save */
    iErrCode = ctrThrCalculationMain.action(iCommand, oidCustom);
    /* end switch*/
    
    /*count list All Position*/
    int vectSize = PstThrCalculationMain.getCount(whereClause); //PstWarningReprimandAyat.getCount(whereClause);
    thrCalculationMain = ctrThrCalculationMain.getThrCalculationMain();
    msgString = ctrThrCalculationMain.getMessage();
    
    if ((iCommand == Command.SAVE) && (iErrCode == FRMMessage.NONE)) {
        start = PstThrCalculationMain.findLimitStart(thrCalculationMain.getOID(), recordToGet, whereClause, orderClause);
        oidCustom = thrCalculationMain.getOID();
    }

    if ((iCommand == Command.FIRST || iCommand == Command.PREV)
            || (iCommand == Command.NEXT || iCommand == Command.LAST)) {
        start = ctrThrCalculationMain.actionList(iCommand, start, vectSize, recordToGet);
    }
   
    
    /* get record to display */
    listThrCalculationMain = PstThrCalculationMain.list(start, recordToGet, whereClause, orderClause);
    
    /*handle condition if size of record to display = 0 and start > 0 	after delete*/
    if (listThrCalculationMain.size() < 1 && start > 0) {
        if (vectSize - recordToGet > recordToGet) {
            start = start - recordToGet;   //go to Command.PREV
        } else {
            start = 0;
            iCommand = Command.FIRST;
            prevCommand = Command.FIRST; //go to Command.FIRST
        }
        listThrCalculationMain = PstThrCalculationMain.list(start, recordToGet, whereClause, orderClause);
    }

%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>THR Configuration Main</title>
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
        <link rel="stylesheet" href="../../styles/main.css" type="text/css">
        <link rel="stylesheet" href="../../styles/tab.css" type="text/css">
        <link href="<%=approot%>/stylesheets/superTables.css" rel="Stylesheet" type="text/css" /> 
        <style type="text/css">
            
            #mn_utama {
                color: #474747; 
                font-weight: bold;
                padding: 7px 11px; 
                border-left: 2px solid #DE072B; 
                font-size: 12px; 
                background-color: #FFF;
            }
            
            #menu_utama {padding: 9px 14px; border-left: 1px solid #0099FF; font-size: 14px; background-color: #F3F3F3; color:#0099FF; font-size: 14px; font-weight: bold;}
            
            #btn {
              background-color: #0796DE;
              font-weight: bold;
              border-radius: 3px;
              font-family: Arial;
              color: #F7F7F7;
              font-size: 12px;
              padding: 7px 11px;
            }

            #btn:hover {
                color: #FFF;
                background-color: #0B82BD;
            }
            #btn1 {
              background: #f27979;
              border: 1px solid #d74e4e;
              border-radius: 3px;
              font-family: Arial;
              color: #ffffff;
              font-size: 12px;
              padding: 3px 9px 3px 9px;
            }

            #btn1:hover {
              background: #d22a2a;
              border: 1px solid #c31b1b;
            }
            #tdForm {
                padding: 5px;
            }
            .tblStyle {border-collapse: collapse;font-size: 11px;}
            .tblStyle td {padding: 3px 5px; border: 1px solid #CCC; vertical-align: middle;}
            .tblStyle tr:hover td {
                color : #474747;
                background-color: #EEE;
                cursor: pointer;
              }
            .title_tbl {
                font-weight: bold;
                background-color: #DDD; 
                color: #575757; 
                padding-top: 9px; 
                padding-bottom: 9px;
            }
            #confirm {background-color: #fad9d9;border: 1px solid #da8383; color: #bf3c3c; padding: 14px 21px;border-radius: 5px;}
            
.dropdown {
    color:#575757;
    background-color: #EEE;
    padding: 5px 7px;
    border-radius: 3px;
    border:1px solid #CCC;
    position: relative;
    display: inline-block;
}

.dropdown-content {
    display: none;
    position: absolute;
    background-color: #FFF;
    min-width: 160px;
    box-shadow: 0px 8px 16px 0px rgba(0,0,0,0.2);
    padding: 5px 0px;
    z-index: 1;
    border-radius: 3px;
    border:1px solid #999;
}

.dropdown:hover .dropdown-content {
    display: block;
}
#div_drop {
    padding: 5px 9px;
}
#div_drop:hover {
    color: #237C99;
    background-color: #E5E5E5;
}
#div_drop a{
    color: #2A87B5;
}
        </style>
        <script type="text/javascript">
            function getCmd(){
                document.<%=FrmThrCalculationMain.FRM_NAME_THR_CALCULATION_MAIN%>.action = "thr_calculation_setup.jsp";
                document.<%=FrmThrCalculationMain.FRM_NAME_THR_CALCULATION_MAIN%>.submit();
            }
            function cmdAdd() {              
                document.<%=FrmThrCalculationMain.FRM_NAME_THR_CALCULATION_MAIN%>.command.value="<%=Command.ADD%>";               
                document.<%=FrmThrCalculationMain.FRM_NAME_THR_CALCULATION_MAIN%>.oid_custom.value = "0";
                getCmd();
            }
            function cmdBack() {
                document.<%=FrmThrCalculationMain.FRM_NAME_THR_CALCULATION_MAIN%>.command.value="<%=Command.BACK%>";               
                getCmd();
            }

            function cmdEdit(oid) {
                document.<%=FrmThrCalculationMain.FRM_NAME_THR_CALCULATION_MAIN%>.command.value = "<%=Command.EDIT%>";
                document.<%=FrmThrCalculationMain.FRM_NAME_THR_CALCULATION_MAIN%>.oid_custom.value = oid;
                getCmd();
            }

            function cmdSave() {
                document.<%=FrmThrCalculationMain.FRM_NAME_THR_CALCULATION_MAIN%>.command.value = "<%=Command.SAVE%>";
                getCmd();
            }
            function cmdAsk(oid){
                document.<%=FrmThrCalculationMain.FRM_NAME_THR_CALCULATION_MAIN%>.command.value="<%=Command.ASK%>";
                document.<%=FrmThrCalculationMain.FRM_NAME_THR_CALCULATION_MAIN%>.oid_custom.value = oid;
                getCmd();
            }
            function cmdDelete(oid){
                document.<%=FrmThrCalculationMain.FRM_NAME_THR_CALCULATION_MAIN%>.command.value = "<%=Command.DELETE%>";
                document.<%=FrmThrCalculationMain.FRM_NAME_THR_CALCULATION_MAIN%>.oid_custom.value = oid;
                getCmd();
            }
            function cmdConfigure(oid){
                document.<%=FrmThrCalculationMain.FRM_NAME_THR_CALCULATION_MAIN%>.command.value = <%=Command.LIST%>;
                document.<%=FrmThrCalculationMain.FRM_NAME_THR_CALCULATION_MAIN%>.oid_custom.value = oid;
                document.<%=FrmThrCalculationMain.FRM_NAME_THR_CALCULATION_MAIN%>.action = "thr_mapping.jsp";
                document.<%=FrmThrCalculationMain.FRM_NAME_THR_CALCULATION_MAIN%>.submit();
            }
            function cmdGenerate(oid){
                document.<%=FrmThrCalculationMain.FRM_NAME_THR_CALCULATION_MAIN%>.oid_custom.value = oid;
                document.<%=FrmThrCalculationMain.FRM_NAME_THR_CALCULATION_MAIN%>.action = "custom_rpt_generate.jsp";
                document.<%=FrmThrCalculationMain.FRM_NAME_THR_CALCULATION_MAIN%>.submit();
            }
            function cmdListFirst(){
                document.<%=FrmThrCalculationMain.FRM_NAME_THR_CALCULATION_MAIN%>.command.value="<%=Command.FIRST%>";
                document.<%=FrmThrCalculationMain.FRM_NAME_THR_CALCULATION_MAIN%>.prev_command.value="<%=Command.FIRST%>";
                getCmd();
            }

            function cmdListPrev(){
                document.<%=FrmThrCalculationMain.FRM_NAME_THR_CALCULATION_MAIN%>.command.value="<%=Command.PREV%>";
                document.<%=FrmThrCalculationMain.FRM_NAME_THR_CALCULATION_MAIN%>.prev_command.value="<%=Command.PREV%>";
                getCmd();
            }

            function cmdListNext(){
                document.<%=FrmThrCalculationMain.FRM_NAME_THR_CALCULATION_MAIN%>.command.value="<%=Command.NEXT%>";
                document.<%=FrmThrCalculationMain.FRM_NAME_THR_CALCULATION_MAIN%>.prev_command.value="<%=Command.NEXT%>";
                getCmd();
            }

            function cmdListLast(){
                document.<%=FrmThrCalculationMain.FRM_NAME_THR_CALCULATION_MAIN%>.command.value="<%=Command.LAST%>";
                document.<%=FrmThrCalculationMain.FRM_NAME_THR_CALCULATION_MAIN%>.prev_command.value="<%=Command.LAST%>";
                getCmd();
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
        </script>
        <link rel="stylesheet" href="<%=approot%>/javascripts/datepicker/themes/jquery.ui.all.css">
        <script src="<%=approot%>/javascripts/jquery.js"></script>
        <script src="<%=approot%>/javascripts/datepicker/jquery.ui.core.js"></script>
	<script src="<%=approot%>/javascripts/datepicker/jquery.ui.widget.js"></script>
	<script src="<%=approot%>/javascripts/datepicker/jquery.ui.datepicker.js"></script>
        <script>
	$(function() {
            $( "#datepicker" ).datepicker({ dateFormat: "yy-mm-dd" });
	});
	</script>
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
                    <table width="100%" border="0" cellspacing="3" cellpadding="2" id="tbl0">
                        <tr> 
                            <td width="100%" colspan="3" valign="top" style="padding: 12px"> 
                                <table width="100%" border="0" cellspacing="0" cellpadding="0">
                                    <tr> 
                                        <td height="20"> <div id="menu_utama"> <!-- #BeginEditable "contenttitle" -->Custom Report<!-- #EndEditable --> </div> </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            &nbsp;
                                        </td>
                                    </tr>
                                    <tr>
                                        <td style="background-color:#EEE;" valign="top">
                                        
                                            <table style="padding:21px; border:1px solid #00CCFF;" <%=garisContent%> width="100%" border="0" cellspacing="1" cellpadding="1" class="tablecolor">
                                                <tr>
                                                    <td valign="top">
                                                        
                                                        
                                                        <form name="<%=FrmThrCalculationMain.FRM_NAME_THR_CALCULATION_MAIN%>" method="POST" action="">
                                                            <input type="hidden" name="command" value="<%=iCommand%>" />
                                                            <input type="hidden" name="oid_custom" value="<%=oidCustom%>" />
                                                            <input type="hidden" name="start" value="<%=start%>" />
                                                            <input type="hidden" name="prev_command" value="<%=prevCommand%>" />
                                                            <%
                                                            if(iCommand == Command.EDIT || iCommand == Command.ADD){
                                                            %>
                                                            <div id="mn_utama">Form Calculation Main</div>
                                                            <table>
                                                                <tr>
                                                                    <td valign="top" id="tdForm"><strong>Title</strong></td>
                                                                    <td valign="top" id="tdForm">
                                                                        <input type="text" name="<%=FrmThrCalculationMain.fieldNames[FrmThrCalculationMain.FRM_FIELD_CALCULATION_MAIN_TITLE]%>" size="50" value="<%=thrCalculationMain.getCalculationMainTitle()%>" />
                                                                    </td>
                                                                </tr>
                                                                
                                                                <tr>
                                                                    <td valign="top" id="tdForm"><strong>Description</strong></td>
                                                                    <td valign="top" id="tdForm">
                                                                        <input type="text" name="<%=FrmThrCalculationMain.fieldNames[FrmThrCalculationMain.FRM_FIELD_CALCULATION_MAIN_DESC]%>" size="70" value="<%=thrCalculationMain.getCalculationMainDesc()%>" />
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td valign="top" id="tdForm"><strong>Date Create</strong></td>
                                                                    <td valign="top" id="tdForm">
                                                                        <input type="text" name="<%=FrmThrCalculationMain.fieldNames[FrmThrCalculationMain.FRM_FIELD_CALCULATION_MAIN_DATE_CREATE]%>" id="datepicker" value="<%=thrCalculationMain.getCalculationMainDateCreate() != null ? thrCalculationMain.getCalculationMainDateCreate() : Formater.formatDate(new Date(), "yyyy-MM-dd")%>" />
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td valign="top" id="tdForm" colspan="2">
                                                                        <a id="btn" href="javascript:cmdSave()">Save</a>&nbsp;
                                                                        <a id="btn" href="javascript:cmdBack()">Back</a>
                                                                    </td>
                                                                </tr>
                                                            </table>
                                                            <% } %>
                                                        </form>
                                                        
                                                    </td>
                                                </tr>
                                                <%
                                                if (iCommand == Command.ASK){
                                                %>
                                                <tr>
                                                    <td>
                                                        <span id="confirm">
                                                            <strong>Are you sure to delete item ?</strong> &nbsp;
                                                            <button id="btn1" onclick="javascript:cmdDelete('<%=oidCustom%>')">Yes</button>
                                                            &nbsp;<button id="btn1" onclick="javascript:cmdBack()">No</button>
                                                        </span>
                                                    </td>
                                                </tr>
                                                <%
                                                }if (privAdd == true){
                                                %>
                                                <tr>
                                                    <td>
                                                        <a id="btn" href="javascript:cmdAdd()">Add New</a>
                                                    </td>
                                                </tr>
                                                  <%
                                                    }
                                                    %>
                                                
                                                <tr>
                                                    <%
                                                    if (listThrCalculationMain != null && listThrCalculationMain.size()>0){
                                                    %>
                                                    <td>
                                                        <div id="mn_utama">List Config</div>
                                                        <div>&nbsp;</div>
                                                        <%=drawList(listThrCalculationMain, oidCustom)%>
                                                    </td>
                                                    <div> 
                                                    <%
                                                        int cmd = 0;
                                                        if ((iCommand == Command.FIRST || iCommand == Command.PREV)
                                                                || (iCommand == Command.NEXT || iCommand == Command.LAST)) {
                                                            cmd = iCommand;
                                                        } else {
                                                            if (iCommand == Command.NONE || prevCommand == Command.NONE) {
                                                                cmd = Command.FIRST;
                                                            } else {
                                                                if ((iCommand == Command.SAVE) && (iErrCode == FRMMessage.NONE)) {
                                                                    cmd = PstThrCalculationMain.findLimitCommand(start, recordToGet, vectSize);
                                                                } else {
                                                                    cmd = prevCommand;
                                                                }
                                                            }
                                                        }

                                                    %>
                                                    <% ctrLine.setLocationImg(approot + "/images");
                                                        ctrLine.initDefault();
                                                    %>
                                                    <%=ctrLine.drawImageListLimit(cmd, vectSize, start, recordToGet)%> 
                                                </div>
                                                    <%
                                                    } else {
                                                    %>
                                                    <td>
                                                        <div id="mn_utama">List Config</div>
                                                        record not found
                                                    </td>
                                                    <%    
                                                    }
                                                    %>
                                                </tr>
                                                <tr>
                                                    <td>&nbsp;</td>
                                                </tr>
                                            </table>
                                        
                                        </td>
                                    </tr>
                                </table><!---End Tble--->
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
        </table>
        <table>
            <tr>
                <td>&nbsp;</td>
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
    <!-- #BeginEditable "script" --> <script language="JavaScript">
                var oBody = document.body;
                var oSuccess = oBody.attachEvent('onkeydown',fnTrapKD);
    </script>
                
    <!-- #EndEditable --> <!-- #EndTemplate -->
</html>
