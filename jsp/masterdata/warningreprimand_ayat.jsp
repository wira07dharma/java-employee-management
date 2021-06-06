<%-- 
    Document   : warning reprimand_ayat
    Created on : Nov 6, 2014, 4:23:25 PM
    Author     : Hendra McHen
--%>

<%@page import="com.dimata.gui.jsp.ControlCombo"%>
<%@page import="com.dimata.harisma.form.masterdata.FrmWarningReprimandAyat"%>
<%@page import="com.dimata.gui.jsp.ControlLine"%>
<%@page import="com.dimata.harisma.form.masterdata.CtrlWarningReprimandAyat"%>
<%@page import="com.dimata.gui.jsp.ControlList"%>
<%@page import="com.dimata.harisma.entity.admin.AppObjInfo"%>
<%@ include file = "../main/javainit.jsp" %>
<% int appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_MASTERDATA, AppObjInfo.G2_MD_COMPANY, AppObjInfo.OBJ_DIVISION);%>
<%@ include file = "../main/checkuser.jsp" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%!    // drawList(Vector, long); untuk menggambar table
    long pasal_id = 0;
    public String drawList(Vector objectClass, long oidAyat) {

        ControlList ctrlist = new ControlList(); //membuat new class ControlList
        // membuat tampilan dengan controllist
        ctrlist.setAreaWidth("100%");
        ctrlist.setListStyle("listgen");
        ctrlist.setTitleStyle("listgentitle");
        ctrlist.setCellStyle("listgensell");
        ctrlist.setHeaderStyle("listgentitle");
        ctrlist.setAreaStyle("customTable");
        ctrlist.addHeader("No", "20%");
        ctrlist.addHeader("Ayat Title", "50%");
        ctrlist.addHeader("Ayat Description", "90%");
        ctrlist.addHeader("Pasal Title", "30%");
        ctrlist.addHeader("Ayat Page","30%");

        /////////
        ctrlist.setLinkRow(1); // untuk menge-sett link di kolom pertama atau dikolom yg lain

        Vector lstData = ctrlist.getData();
        Vector lstLinkData = ctrlist.getLinkData();
        ctrlist.setLinkPrefix("javascript:cmdEdit('");
        ctrlist.setLinkSufix("')");
        ////////


        ctrlist.reset(); //berfungsi untuk menginisialisasi list menjadi kosong

        int no = 0;

        // objectClass mempunyai tipe data Vector
        // objectClass.size(); mendapatkan banyak record
        for (int i = 0; i < objectClass.size(); i++) {
            // membuat object WarningReprimandAyat berdasarkan objectClass ke-i
            
            WarningReprimandAyat ayat = (WarningReprimandAyat) objectClass.get(i);
            // rowx will be created secara berkesinambungan base on i
            Vector rowx = new Vector();

            no = no + 1;
            rowx.add("" + no);
            rowx.add(ayat.getAyatTitle());
            rowx.add(ayat.getAyatDescription());
            
            
            WarningReprimandPasal pasalTitle = new WarningReprimandPasal();
            try {
                pasalTitle = PstWarningReprimandPasal.fetchExc(ayat.getPasalId());
            } catch (Exception e) {
            }

            if (oidAyat == ayat.getOID()){
                pasal_id = ayat.getPasalId();
            }
            
            rowx.add(pasalTitle.getPasalTitle());
            rowx.add(String.valueOf(ayat.getAyatPage()));
            lstData.add(rowx);
            // menambah ID ke list LinkData
            lstLinkData.add(String.valueOf(ayat.getOID()));

        }

        return ctrlist.draw(); // mengembalikan data-data control list

    }

%>
<%  
    int iCommand = FRMQueryString.requestCommand(request);
    int start = FRMQueryString.requestInt(request, "start");
    int prevCommand = FRMQueryString.requestInt(request, "prev_command");
    long oidAyat = FRMQueryString.requestLong(request, "hidden_ayat_id");
    /*variable declaration*/
    int recordToGet = 10;
    String msgString = "";
    int iErrCode = FRMMessage.NONE;
    String whereClause = "";
    String orderClause = "";

    
    CtrlWarningReprimandAyat ctrlAyat = new CtrlWarningReprimandAyat(request);
    ControlLine ctrLine = new ControlLine();
    Vector listAyat = new Vector(1, 1);

    /*switch statement */
    iErrCode = ctrlAyat.action(iCommand, oidAyat);
    /* end switch*/
    FrmWarningReprimandAyat frmAyat = ctrlAyat.getForm();
    /*count list All Position*/
    int vectSize = PstWarningReprimandAyat.getCount(whereClause);
    WarningReprimandAyat ayat = ctrlAyat.getWarningReprimandAyat();
    msgString = ctrlAyat.getMessage();
    
    if ((iCommand == Command.SAVE) && (iErrCode == FRMMessage.NONE)) {
        start = PstWarningReprimandAyat.findLimitStart(ayat.getOID(), recordToGet, whereClause, orderClause);
        oidAyat = ayat.getOID();
    }

    if ((iCommand == Command.FIRST || iCommand == Command.PREV)
            || (iCommand == Command.NEXT || iCommand == Command.LAST)) {
        start = ctrlAyat.actionList(iCommand, start, vectSize, recordToGet);
    }
    /* end switch list*/

    /* get record to display */
    listAyat = PstWarningReprimandAyat.list(start, recordToGet, whereClause, orderClause);

    /*handle condition if size of record to display = 0 and start > 0 	after delete*/
    if (listAyat.size() < 1 && start > 0) {
        if (vectSize - recordToGet > recordToGet) {
            start = start - recordToGet;   //go to Command.PREV
        } else {
            start = 0;
            iCommand = Command.FIRST;
            prevCommand = Command.FIRST; //go to Command.FIRST
        }
        listAyat = PstWarningReprimandAyat.list(start, recordToGet, whereClause, orderClause);
    }


%>
<%

    /*
     * Some code of ComboBox
     */

    String CtrOrderClause = PstWarningReprimandPasal.fieldNames[PstWarningReprimandPasal.FLD_PASAL_ID];
    Vector vectMasterClass = PstWarningReprimandPasal.list(0, 10, "", CtrOrderClause);

    Vector val_MasterClass = new Vector(1, 1); //hidden values that will be deliver on request (oids) 

    Vector key_MasterClass = new Vector(1, 1); //texts that displayed on combo box

    val_MasterClass.add("0");

    key_MasterClass.add("All Pasal");

    for (int c = 0; c < vectMasterClass.size(); c++) {

        WarningReprimandPasal cbPasal = (WarningReprimandPasal) vectMasterClass.get(c);

        val_MasterClass.add("" + cbPasal.getOID());

        key_MasterClass.add(cbPasal.getPasalTitle());

    }

%>
<!DOCTYPE html>
<html>
        <head>
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
        <!-- #BeginEditable "styles" -->
        <link rel="stylesheet" href="../styles/main.css" type="text/css">
        <!-- #EndEditable --><!-- #BeginEditable "stylestab" -->
        <link rel="stylesheet" href="../styles/tab.css" type="text/css">
        <!-- #EndEditable --><!-- #BeginEditable "headerscript" -->
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
        <script language="JavaScript">
            function getCmd(){
                document.<%=FrmWarningReprimandAyat.FRM_NAME_AYAT%>.action = "warningreprimand_ayat.jsp";
                document.<%=FrmWarningReprimandAyat.FRM_NAME_AYAT%>.submit();
            }
            function cmdAdd() {              
                document.<%=FrmWarningReprimandAyat.FRM_NAME_AYAT%>.command.value="<%=Command.ADD%>";               
                document.<%=FrmWarningReprimandAyat.FRM_NAME_AYAT%>.hidden_ayat_id.value = "0";
                getCmd();
            }
            function cmdBack() {
                document.<%=FrmWarningReprimandAyat.FRM_NAME_AYAT%>.command.value="<%=Command.BACK%>";               
                getCmd();
            }

            function cmdEdit(oid) {
                document.<%=FrmWarningReprimandAyat.FRM_NAME_AYAT%>.command.value = "<%=Command.EDIT%>";
                document.<%=FrmWarningReprimandAyat.FRM_NAME_AYAT%>.hidden_ayat_id.value = oid;
                getCmd();
            }

            function cmdSave() {
                document.<%=FrmWarningReprimandAyat.FRM_NAME_AYAT%>.command.value = "<%=Command.SAVE%>";
                getCmd();
            }
            
            function cmdListFirst(){
                    document.<%=FrmWarningReprimandAyat.FRM_NAME_AYAT%>.command.value="<%=Command.FIRST%>";
                    document.<%=FrmWarningReprimandAyat.FRM_NAME_AYAT%>.prev_command.value="<%=Command.FIRST%>";
                    getCmd();
            }

            function cmdListPrev(){
                    document.<%=FrmWarningReprimandAyat.FRM_NAME_AYAT%>.command.value="<%=Command.PREV%>";
                    document.<%=FrmWarningReprimandAyat.FRM_NAME_AYAT%>.prev_command.value="<%=Command.PREV%>";
                    getCmd();
                    }

            function cmdListNext(){
                    document.<%=FrmWarningReprimandAyat.FRM_NAME_AYAT%>.command.value="<%=Command.NEXT%>";
                    document.<%=FrmWarningReprimandAyat.FRM_NAME_AYAT%>.prev_command.value="<%=Command.NEXT%>";
                    getCmd();
            }

            function cmdListLast(){
                    document.<%=FrmWarningReprimandAyat.FRM_NAME_AYAT%>.command.value="<%=Command.LAST%>";
                    document.<%=FrmWarningReprimandAyat.FRM_NAME_AYAT%>.prev_command.value="<%=Command.LAST%>";
                    getCmd();
            }

        </script>
        <style type="text/css">   
            #title_master_data {background-color: #FFF; font-size: 12px; padding: 3px; color: #FF0000; border-left: 2px solid #333; padding-left: 5px;}
            #table_area {background-color: #EEEEEE;border:1px solid #CCCCCC;padding: 5px;}
            #btn {padding: 7px;color:#FFFFFF;background-color: #4474c3;border: 1px solid #2c508d;cursor: pointer; border-radius: 3px;}
            #bth:hover {background-color: #333333;}
            #inputTx {padding:7px; border: 1px solid #ccc;font-size: 14px;}
            .customTable td {padding:3px;}
            .cbPasal {padding: 7px;border: 1px solid #ccc;font-size: 14px;}
        </style>
        <title>Hairisma Master Data Ayat</title>
        </head>
<body <%=noBack%> bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" onLoad="MM_preloadImages('<%=approot%>/images/BtnNewOn.jpg')">
<table width="100%" border="0" cellspacing="0" cellpadding="0" height="100%" bgcolor="#F9FCFF" >
<%if(headerStyle && !verTemplate.equalsIgnoreCase("0")){%>
<%@include file="../styletemplate/template_header.jsp" %>
<%}else{%>
<tr>
    <td ID="TOPTITLE" background="<%=approot%>/images/HRIS_HeaderBg3.jpg" width="100%" height="54"><!-- #BeginEditable "header" -->
      <%@ include file = "../main/header.jsp" %>
      <!-- #EndEditable --></td>
  </tr>
  <tr>
    <td  bgcolor="#9BC1FF" height="15" ID="MAINMENU" valign="middle"><!-- #BeginEditable "menumain" -->
      <%@ include file = "../main/mnmain.jsp" %>
    </td>
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
                <td valign="top" align="left">
                    <table width="100%" border="0" cellspacing="3" cellpadding="2">
                        <tr>
                            <td width="100%">
                                <table width="100%" border="0" cellspacing="0" cellpadding="0">
                                    <tr>
                                        <td height="20">
                                            <font color="#FF6600" face="Arial"><strong>
                                                <!-- #BeginEditable "contenttitle" -->
                                                Master Data &gt; Ayat<!-- #EndEditable -->
                                            </strong>
                                            </font>
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
                                                                                <form name="<%=FrmWarningReprimandAyat.FRM_NAME_AYAT%>" method="POST" action="">

                                                                                    <input type="hidden" name="start" value="<%=start%>">
                                                                                    <input type="hidden" name="command" value="<%=iCommand%>">
                                                                                    <input type="hidden" name="hidden_ayat_id" value="<%=oidAyat%>">
                                                                                    <input type="hidden" name="vectSize" value="<%=vectSize%>">
                                                                                    <input type="hidden" name="start" value="<%=start%>">
                                                                                    <input type="hidden" name="prev_command" value="<%=prevCommand%>">
                                                                                    <table>
                                                                                        <tr><td><div id="title_master_data">Verse (Ayat) List</div></td></tr>
                                                                                        <tr>
                                                                                            <%if (listAyat != null && listAyat.size() > 0) {%>
                                                                                            <td>
                                                                                                <!-- untuk drawlist -->
                                                                                                <!-- membuat tabel yang ditulis dari method drowList(); -->
                                                                                                <%=drawList(listAyat, oidAyat)%>
                                                                                            </td>
                                                                                            
                                                                                            <%} else {%>
                                                                                            <td>
                                                                                                record not found
                                                                                            </td>
                                                                                            <%}%>
                                                                                        </tr>
                                                                                        <tr>
                                                                                            <td>
                                                                                                <span class="command"> 
                                                                                                    <%
                                                                                                        int cmd = 0;
                                                                                                        if ((iCommand == Command.FIRST || iCommand == Command.PREV)
                                                                                                                || (iCommand == Command.NEXT || iCommand == Command.LAST)) {
                                                                                                            cmd = iCommand;
                                                                                                        } else {
                                                                                                            if (iCommand == Command.NONE || prevCommand == Command.NONE) {
                                                                                                                cmd = Command.FIRST;
                                                                                                            } else {
                                                                                                                if ((iCommand == Command.SAVE) && (iErrCode == FRMMessage.NONE) && (oidAyat == 0)) {
                                                                                                                    cmd = PstWarningReprimandAyat.findLimitCommand(start, recordToGet, vectSize);
                                                                                                                } else {
                                                                                                                    cmd = prevCommand;
                                                                                                                }
                                                                                                            }
                                                                                                        }
                                                                                                    %>
                                                                                                    <% 
                                                                                                        ctrLine.setLocationImg(approot + "/images");
                                                                                                        ctrLine.initDefault();
                                                                                                    %>
                                                                                                    <%=ctrLine.drawImageListLimit(cmd, vectSize, start, recordToGet)%> 
                                                                                                </span>
                                                                                            </td>
                                                                                        </tr>
                                                                                        <tr>
                                                                                            <td>
                                                                                                <!-- untuk list record -->
                                                                                            </td>
                                                                                        </tr>
                                                                                        <%if (!(iCommand == Command.ADD || iCommand == Command.EDIT)) {%>
                                                                                        <tr>

                                                                                            <td>

                                                                                                <button id="btn" onclick="cmdAdd()">Add New Pasal</button>

                                                                                            </td>

                                                                                        </tr>
                                                                                        <%}%>
                                                                                        <!-- inputan user -->
                                                                                        <%if (iCommand == Command.ADD || iCommand == Command.EDIT) {%>
                                                                                        <% if (iCommand == Command.ADD){%>
                                                                                        <tr><td><div id="title_master_data">Add Verse (Ayat)</div></td></tr>
                                                                                        <% } else { %>
                                                                                        <tr><td><div id="title_master_data">Edit Verse (Ayat)</div></td></tr>
                                                                                        <% } %>
                                                                                        <tr>
                                                                                            <td>
                                                                                                <input placeholder="type ayat title..." id="inputTx" type="text" name="<%=FrmWarningReprimandAyat.fieldNames[FrmWarningReprimandAyat.FRM_FIELD_AYAT_TITLE]%>" value="<%=ayat.getAyatTitle()%>" size="50">
                                                                                            </td>
                                                                                        </tr>
                                                                                        <tr>
                                                                                            <td>
                                                                                                <textarea name="<%=FrmWarningReprimandAyat.fieldNames[FrmWarningReprimandAyat.FRM_FIELD_AYAT_DESCRIPTION]%>" size="50"><%=ayat.getAyatDescription()%></textarea>
                                                                                            </td>
                                                                                        </tr>
                                                                                        <tr>
                                                                                            <td>
                                                                                                <%=ControlCombo.draw(FrmWarningReprimandAyat.fieldNames[FrmWarningReprimandAyat.FRM_FIELD_PASAL_ID], null, String.valueOf(pasal_id), val_MasterClass, key_MasterClass, "", "cbPasal")%>                                    
                                                                                            </td>
                                                                                        </tr>
                                                                                        <tr>
                                                                                            <td>
                                                                                                <input placeholder="type ayat page (only number)..." id="inputTx" type="text" name="<%=FrmWarningReprimandAyat.fieldNames[FrmWarningReprimandAyat.FRM_FIELD_AYAT_PAGE]%>" value="<%=ayat.getAyatPage()%>" size="50">
                                                                                            </td>
                                                                                        </tr>
                                                                                        <tr>
                                                                                            <td>
                                                                                                <button id="btn" onclick="cmdSave()">Save Ayat</button>
                                                                                                <button id="btn" onclick="cmdBack()">Back</button>
                                                                                            </td>
                                                                                        </tr>
                                                                                        <%}%>   
                                                                                    </table>
                                                                                </form>
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
<%if (headerStyle && !verTemplate.equalsIgnoreCase("0")) {%>
<tr>
    <td valign="bottom">
        <!-- untuk footer -->
        <%@include file="../footer.jsp" %>
    </td>

</tr>
<%} else {%>
<tr> 
    <td colspan="2" height="20" > <!-- #BeginEditable "footer" --> 
        <%@ include file = "../main/footer.jsp" %>
        <!-- #EndEditable --> </td>
</tr>
<%}%>
</table>
</body>
</html>