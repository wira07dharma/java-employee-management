<%-- 
    Document   : provinsi
    Created on : Nov 15, 2011, 3:02:34 PM
    Author     : Wiweka
--%>



<%@ page language = "java" %>
<!-- package java -->
<%@ page import = "java.util.*" %>
<!-- package dimata -->
<%@ page import = "com.dimata.util.*" %>
<!-- package qdep -->
<%@ page import = "com.dimata.gui.jsp.*" %>
<%@ page import = "com.dimata.qdep.form.*" %>
<!--package HRIS -->
<%@ page import = "com.dimata.harisma.entity.masterdata.*" %>
<%@ page import = "com.dimata.harisma.form.masterdata.*" %>
<%@ page import = "com.dimata.harisma.entity.admin.*" %>
<%@ include file = "../main/javainit.jsp" %>
<% int appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_MASTERDATA, AppObjInfo.G2_MD_COMPANY, AppObjInfo.OBJ_DEPARTMENT);%>
<%@ include file = "../main/checkuser.jsp" %>
<%
            /* Check privilege except VIEW, view is already checked on checkuser.jsp as basic access*/
//boolean privAdd=userSession.checkPrivilege(AppObjInfo.composeCode(appObjCode, AppObjInfo.COMMAND_ADD));
//boolean privUpdate=userSession.checkPrivilege(AppObjInfo.composeCode(appObjCode, AppObjInfo.COMMAND_UPDATE));
//boolean privDelete=userSession.checkPrivilege(AppObjInfo.composeCode(appObjCode, AppObjInfo.COMMAND_DELETE));
%>
<!-- Jsp Block -->
<%!    public String drawList(Vector objectClass, long idDistrict) {
        ControlList ctrlist = new ControlList();
        ctrlist.setAreaWidth("60%");
        ctrlist.setListStyle("listgen");
        ctrlist.setTitleStyle("listgentitle");
        ctrlist.setCellStyle("listgensell");
        ctrlist.setHeaderStyle("listgentitle");
        ctrlist.addHeader("Province", "25%");
        ctrlist.addHeader("Regency", "25%");
        ctrlist.addHeader("District", "25%");
        ctrlist.addHeader("Sub District", "25%");

        ctrlist.setLinkRow(0);
        ctrlist.setLinkSufix("");
        Vector lstData = ctrlist.getData();
        Vector lstLinkData = ctrlist.getLinkData();
        ctrlist.setLinkPrefix("javascript:cmdEdit('");
        ctrlist.setLinkSufix("')");
        ctrlist.reset();
        int index = -1;

        for (int i = 0; i < objectClass.size(); i++) {
            District district = (District) objectClass.get(i);
            Vector rowx = new Vector();
            if (idDistrict == district.getOID()) {
                index = i;
            }

            Provinsi provinsi = new Provinsi();
            try {
                provinsi = PstProvinsi.fetchExc(district.getIdPropinsi());
            } catch (Exception e) {
            }
            Kabupaten kabupaten = new Kabupaten();
            try {
                kabupaten = PstKabupaten.fetchExc(district.getIdKabupaten());
            } catch (Exception e) {
            }
            Kecamatan kecamatan = new Kecamatan();
            try {
                kecamatan = PstKecamatan.fetchExc(district.getIdKecamatan());
            } catch (Exception e) {
            }

            rowx.add(provinsi.getNmProvinsi());
            rowx.add(kabupaten.getNmKabupaten());
            rowx.add(kecamatan.getNmKecamatan());
            rowx.add(district.getNmDistrict());

            lstData.add(rowx);
            lstLinkData.add(String.valueOf(district.getOID()));
        }

        //return ctrlist.drawList(index);

        return ctrlist.draw(index);
    }

%>
<%
            int iCommand = FRMQueryString.requestCommand(request);
            int start = FRMQueryString.requestInt(request, "start");
            int prevCommand = FRMQueryString.requestInt(request, "prev_command");
            long oidDistrict = FRMQueryString.requestLong(request, "hidden_district_id");
            long oidKecamatan = FRMQueryString.requestLong(request, FrmDistrict.fieldNames[FrmDistrict.FRM_FIELD_ID_KECAMATAN]);
            long oidKabupaten = FRMQueryString.requestLong(request, FrmDistrict.fieldNames[FrmDistrict.FRM_FIELD_ID_KABUPATEN]);
            long oidProvinsi = FRMQueryString.requestLong(request, FrmDistrict.fieldNames[FrmDistrict.FRM_FIELD_ID_PROPINSI]);
            long oidNegara = FRMQueryString.requestLong(request, FrmDistrict.fieldNames[FrmDistrict.FRM_FIELD_ID_NEGARA]);


            /*variable declaration*/
            int recordToGet = 10;
            String msgString = "";
            int iErrCode = FRMMessage.NONE;
            String whereClause = "";
            //String orderClause ="";
            String orderClause = "p."+PstProvinsi.fieldNames[PstProvinsi.FLD_NM_PROVINSI]+", k." +PstKabupaten.fieldNames[PstKabupaten.FLD_NM_KABUPATEN]+", kec." +PstKecamatan.fieldNames[PstKecamatan.FLD_NM_KECAMATAN]+", d." +PstDistrict.fieldNames[PstDistrict.FLD_NAMA_DISTRICT];

            CtrlDistrict ctrlDistrict = new CtrlDistrict(request);
            ControlLine ctrLine = new ControlLine();
            Vector listDistrict = new Vector(1, 1);

            /*switch statement */
            iErrCode = ctrlDistrict.action(iCommand, oidDistrict);
            /* end switch*/
            FrmDistrict frmDistrict = ctrlDistrict.getForm();

            /*count list All District*/
            int vectSize = PstDistrict.getCount(whereClause);

            District district = ctrlDistrict.getDistrict();
            msgString = ctrlDistrict.getMessage();
            int commandRefresh = FRMQueryString.requestInt(request, "commandRefresh");
            if(commandRefresh == 1){
            district.setDistrictId(oidDistrict);
            district.setIdKecamatan(oidKecamatan);
            district.setIdKabupaten(oidKabupaten);
            district.setIdPropinsi(oidProvinsi);
            district.setIdNegara(oidNegara);
            }


            
               // frmDistrict = new FrmDistrict(request, district);
                //frmDistrict.requestEntityObject(district);
                //iCommand = FRMQueryString.requestCommand(request);
            
            /*switch list District*/
            if ((iCommand == Command.SAVE) && (iErrCode == FRMMessage.NONE)) {
                start = PstDistrict.findLimitStart(district.getOID(), recordToGet, whereClause, orderClause);
                oidDistrict = district.getOID();
            }

            if ((iCommand == Command.FIRST || iCommand == Command.PREV)
                    || (iCommand == Command.NEXT || iCommand == Command.LAST)) {
                start = ctrlDistrict.actionList(iCommand, start, vectSize, recordToGet);
            }
            /* end switch list*/

            /* get record to display */
            listDistrict = PstDistrict.listJoinDist(start, recordToGet, whereClause, orderClause);

            /*handle condition if size of record to display = 0 and start > 0 	after delete*/
            if (listDistrict.size() < 1 && start > 0) {
                if (vectSize - recordToGet > recordToGet) {
                    start = start - recordToGet;   //go to Command.PREV
                } else {
                    start = 0;
                    iCommand = Command.FIRST;
                    prevCommand = Command.FIRST; //go to Command.FIRST
                }
                listDistrict = PstDistrict.listJoinDist(start, recordToGet, whereClause, orderClause);
            }




%>
<html><!-- #BeginTemplate "/Templates/main.dwt" -->
    <head>
        <!-- #BeginEditable "doctitle" -->
        <title>HARISMA - Master Data > District</title>
        <script language="JavaScript">
            function cmdUpdateForm(){
                document.frmdistrict.command.value="<%=iCommand%>";
                document.frmdistrict.commandRefresh.value= "1";
                document.frmdistrict.action="district.jsp";
                document.frmdistrict.submit();
            }

            function cmdAdd(){
                document.frmdistrict.hidden_district_id.value="0";
                document.frmdistrict.command.value="<%=Command.ADD%>";
                document.frmdistrict.prev_command.value="<%=prevCommand%>";
                document.frmdistrict.action="district.jsp";
                document.frmdistrict.submit();
            }

            function cmdAsk(oidDistrict){
                document.frmdistrict.hidden_district_id.value=oidDistrict;
                document.frmdistrict.command.value="<%=Command.ASK%>";
                document.frmdistrict.prev_command.value="<%=prevCommand%>";
                document.frmdistrict.action="district.jsp";
                document.frmdistrict.submit();
            }

            function cmdConfirmDelete(oidDistrict){
                document.frmdistrict.hidden_district_id.value=oidDistrict;
                document.frmdistrict.command.value="<%=Command.DELETE%>";
                document.frmdistrict.prev_command.value="<%=prevCommand%>";
                document.frmdistrict.action="district.jsp";
                document.frmdistrict.submit();
            }
            function cmdSave(){
                document.frmdistrict.command.value="<%=Command.SAVE%>";
                document.frmdistrict.prev_command.value="<%=prevCommand%>";
                document.frmdistrict.action="district.jsp";
                document.frmdistrict.submit();
            }

            function cmdEdit(oidDistrict){
                document.frmdistrict.hidden_district_id.value=oidDistrict;
                document.frmdistrict.command.value="<%=Command.EDIT%>";
                document.frmdistrict.prev_command.value="<%=prevCommand%>";
                document.frmdistrict.action="district.jsp";
                document.frmdistrict.submit();
            }

            function cmdCancel(oidDistrict){
                document.frmdistrict.hidden_district_id.value=oidDistrict;
                document.frmdistrict.command.value="<%=Command.EDIT%>";
                document.frmdistrict.prev_command.value="<%=prevCommand%>";
                document.frmdistrict.action="district.jsp";
                document.frmdistrict.submit();
            }

            function cmdBack(){
                document.frmdistrict.command.value="<%=Command.BACK%>";
                document.frmdistrict.action="district.jsp";
                document.frmdistrict.submit();
            }

            function cmdListFirst(){
                document.frmdistrict.command.value="<%=Command.FIRST%>";
                document.frmdistrict.prev_command.value="<%=Command.FIRST%>";
                document.frmdistrict.action="district.jsp";
                document.frmdistrict.submit();
            }

            function cmdListPrev(){
                document.frmdistrict.command.value="<%=Command.PREV%>";
                document.frmdistrict.prev_command.value="<%=Command.PREV%>";
                document.frmdistrict.action="district.jsp";
                document.frmdistrict.submit();
            }

            function cmdListNext(){
                document.frmdistrict.command.value="<%=Command.NEXT%>";
                document.frmdistrict.prev_command.value="<%=Command.NEXT%>";
                document.frmdistrict.action="district.jsp";
                document.frmdistrict.submit();
            }

            function cmdListLast(){
                document.frmdistrict.command.value="<%=Command.LAST%>";
                document.frmdistrict.prev_command.value="<%=Command.LAST%>";
                document.frmdistrict.action="district.jsp";
                document.frmdistrict.submit();
            }

            

            function fnTrapKD(){
                //alert(event.keyCode);
                switch(event.keyCode)         {
                    case <%=LIST_PREV%>:
                            cmdListPrev();
                        break        ;
                    case <%=LIST_NEXT%>:
                            cmdListNext();
                        break        ;
                    case <%=LIST_FIRST%>:
                            cmdListFirst();
                        break        ;
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
        <link rel="stylesheet" href="../styles/main.css" type="text/css">
        <!-- #EndEditable -->
        <!-- #BeginEditable "stylestab" -->
        <link rel="stylesheet" href="../styles/tab.css" type="text/css">
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
           <%@include file="../styletemplate/template_header.jsp" %>
            <%}else{%>
            <tr>
                <td ID="TOPTITLE" background="<%=approot%>/images/HRIS_HeaderBg3.jpg" width="100%" height="54">
                    <!-- #BeginEditable "header" -->
                    <%@ include file = "../main/header.jsp" %>
                    <!-- #EndEditable -->
                </td>
            </tr>
            <tr>
                <td  bgcolor="#9BC1FF" height="15" ID="MAINMENU" valign="middle"> <!-- #BeginEditable "menumain" -->
                    <%@ include file = "../main/mnmain.jsp" %>
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
                                                    <!-- #BeginEditable "contenttitle" -->
                                                    Master Data &gt; Regency<!-- #EndEditable -->
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
                                                                                <form name="frmdistrict" method ="post" action="">
                                                                                    <input type="hidden" name="command" value="<%=iCommand%>">
                                                                                    <input type="hidden" name="vectSize" value="<%=vectSize%>">
                                                                                    <input type="hidden" name="start" value="<%=start%>">
                                                                                    <input type="hidden" name="prev_command" value="<%=prevCommand%>">
                                                                                    <input type="hidden" name="commandRefresh" value= "0">
                                                                                    <input type="hidden" name="hidden_district_id" value="<%=oidDistrict%>">
                                                                                    <table width="100%" border="0" cellspacing="0" cellpadding="0">
                                                                                        <tr align="left" valign="top">
                                                                                            <td height="8"  colspan="3">
                                                                                                <table width="100%" border="0" cellspacing="0" cellpadding="0">
                                                                                                    <tr align="left" valign="top">
                                                                                                        <td height="14" valign="middle" colspan="3">&nbsp;
                                                                                                            <span class="listtitle">Sub District List </span></td>
                                                                                                    </tr>
                                                                                                    <%
                                                                                                                try {
                                                                                                                    if (listDistrict.size() > 0) {
                                                                                                    %>
                                                                                                    <tr align="left" valign="top">
                                                                                                        <td height="22" valign="middle" colspan="3">
                                                                                                            <%= drawList(listDistrict, oidDistrict)%></td>
                                                                                                    </tr>
                                                                                                    <%  }
                                                                                                                } catch (Exception exc) {
                                                                                                                }%>
                                                                                                    <tr align="left" valign="top">
                                                                                                        <td height="8" align="left" colspan="3" class="command">
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
                                                                                                                                    if ((iCommand == Command.SAVE) && (iErrCode == FRMMessage.NONE)) {
                                                                                                                                        cmd = PstDistrict.findLimitCommand(start, recordToGet, vectSize);
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
                                                                                                            </span> </td>
                                                                                                    </tr>
                                                                                                    <%//if((iCommand == Command.NONE || iCommand == Command.DELETE || iCommand ==Command.BACK || iCommand ==Command.SAVE)&& (frmDistrict.errorSize()<1)){
                                                                                                                if ((iCommand != Command.ADD && iCommand != Command.ASK && iCommand != Command.EDIT) && (frmDistrict.errorSize() < 1)) {
                                                                                                                    if (privAdd) {%>
                                                                                                    <tr align="left" valign="top">
                                                                                                        <td>
                                                                                                            <table cellpadding="0" cellspacing="0" border="0">
                                                                                                                <tr>
                                                                                                                    <td>&nbsp;</td>
                                                                                                                </tr>
                                                                                                                <tr>
                                                                                                                    <td width="4"><img src="<%=approot%>/images/spacer.gif" width="1" height="1"></td>
                                                                                                                    <td width="24"><a href="javascript:cmdAdd()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image261','','<%=approot%>/images/BtnNewOn.jpg',1)"><img name="Image261" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="Add new data"></a></td>
                                                                                                                    <td width="6"><img src="<%=approot%>/images/spacer.gif" width="1" height="1"></td>
                                                                                                                    <td height="22" valign="middle" colspan="3" width="951">
                                                                                                                        <a href="javascript:cmdAdd()" class="command">Add New District</a> </td>
                                                                                                                </tr>
                                                                                                            </table>
                                                                                                        </td>
                                                                                                    </tr>
                                                                                                    <%}
                                                                                                                }%>
                                                                                                </table>
                                                                                            </td>
                                                                                        </tr>
                                                                                        <tr>
                                                                                            <td>&nbsp;
                                                                                            </td>
                                                                                        </tr>
                                                                                        <tr align="left" valign="top">
                                                                                            <td height="8" valign="middle" colspan="3">
                                                                                                <%if ((iCommand == Command.ADD) || (iCommand == Command.SAVE) && (frmDistrict.errorSize() > 0) || (iCommand == Command.EDIT) || (iCommand == Command.ASK)) {%>
                                                                                                <table width="100%" border="0" cellspacing="2" cellpadding="2">
                                                                                                    <tr>
                                                                                                        <td><b class="listtitle"><%=oidDistrict == 0 ? "Add" : "Edit"%> District</b></td>
                                                                                                    </tr>
                                                                                                    <tr>
                                                                                                        <td height="100%">
                                                                                                            <table border="0" cellspacing="2" cellpadding="2" width="50%">
                                                                                                                <tr align="left" valign="top">
                                                                                                                    <td valign="top" width="18%">&nbsp;</td>
                                                                                                                    <td width="82%" class="comment">*)entry required </td>
                                                                                                                </tr>
                                                                                                                <tr align="left" valign="top">
                                                                                                                    <td valign="top" width="18%">Country</td>
                                                                                                                    <td width="82%"><%
                                                                                                                        Vector neg_value = new Vector(1, 1);
                                                                                                                        Vector neg_key = new Vector(1, 1);
                                                                                                                        neg_value.add("0");
                                                                                                                        neg_key.add("select ...");
                                                                                                                        Vector listNeg = PstNegara.list(0, 0, "", " NAMA_NGR ");
                                                                                                                        for (int i = 0; i < listNeg.size(); i++) {
                                                                                                                            Negara neg = (Negara) listNeg.get(i);
                                                                                                                            neg_key.add(neg.getNmNegara());
                                                                                                                            neg_value.add(String.valueOf(neg.getOID()));
                                                                                                                        }
                                                                                                                        %>
                                                                                                                        <%= ControlCombo.draw(frmDistrict.fieldNames[FrmDistrict.FRM_FIELD_ID_NEGARA], "formElemen", null, "" + district.getIdNegara(), neg_value, neg_key, "onChange=\"javascript:cmdUpdateForm()\"")%>
                                                                                                                    </td>
                                                                                                                </tr>
                                                                                                                <tr align="left" valign="top">
                                                                                                                    <td valign="top" width="18%">Province</td>
                                                                                                                    <td width="82%"><%
                                                                                                                        Vector pro_value = new Vector(1, 1);
                                                                                                                        Vector pro_key = new Vector(1, 1);
                                                                                                                        pro_value.add("0");
                                                                                                                        pro_key.add("select ...");
                                                                                                                        String strWhere = PstProvinsi.fieldNames[PstProvinsi.FLD_ID_NEGARA] + "=" + district.getIdNegara();
                                                                                                                        Vector listPro = PstProvinsi.list(0, 0, strWhere, PstProvinsi.fieldNames[PstProvinsi.FLD_NM_PROVINSI]);
                                                                                                                        for (int i = 0; i < listPro.size(); i++) {
                                                                                                                            Provinsi pro = (Provinsi) listPro.get(i);
                                                                                                                            pro_key.add(pro.getNmProvinsi());
                                                                                                                            pro_value.add(String.valueOf(pro.getOID()));
                                                                                                                        }
                                                                                                                        %>
                                                                                                                        <%= ControlCombo.draw(frmDistrict.fieldNames[FrmDistrict.FRM_FIELD_ID_PROPINSI], "formElemen", null, "" + district.getIdPropinsi(), pro_value, pro_key, "onChange=\"javascript:cmdUpdateForm()\"")%>
                                                                                                                    </td>
                                                                                                                </tr>
                                                                                                                <tr align="left" valign="top">
                                                                                                                    <td valign="top" width="18%">Regency</td>
                                                                                                                    <td width="82%"><%
                                                                                                                        Vector kab_value = new Vector(1, 1);
                                                                                                                        Vector kab_key = new Vector(1, 1);
                                                                                                                        kab_value.add("0");
                                                                                                                        kab_key.add("select ...");
                                                                                                                        String strWherePro = PstKabupaten.fieldNames[PstKabupaten.FLD_ID_PROPINSI] + "=" + district.getIdPropinsi();
                                                                                                                        Vector listKab = PstKabupaten.list(0, 0, strWherePro, " NAMA_KAB ");
                                                                                                                        for (int i = 0; i < listKab.size(); i++) {
                                                                                                                            Kabupaten kab = (Kabupaten) listKab.get(i);
                                                                                                                            kab_key.add(kab.getNmKabupaten());
                                                                                                                            kab_value.add(String.valueOf(kab.getOID()));
                                                                                                                        }
                                                                                                                        %>
                                                                                                                        <%= ControlCombo.draw(frmDistrict.fieldNames[FrmDistrict.FRM_FIELD_ID_KABUPATEN], "formElemen", null, "" + district.getIdKabupaten(), kab_value, kab_key, "onChange=\"javascript:cmdUpdateForm()\"")%>
                                                                                                                    </td>
                                                                                                                </tr>
                                                                                                                <tr align="left" valign="top">
                                                                                                                    <td valign="top" width="18%">District</td>
                                                                                                                    <td width="82%"><%
                                                                                                                        Vector kec_value = new Vector(1, 1);
                                                                                                                        Vector kec_key = new Vector(1, 1);
                                                                                                                        kec_value.add("0");
                                                                                                                        kec_key.add("select ...");
                                                                                                                        String strWhereKab = PstKecamatan.fieldNames[PstKecamatan.FLD_ID_KABUPATEN] + "=" + district.getIdKabupaten();
                                                                                                                        Vector listKec = PstKecamatan.list(0, 0,strWhereKab, " NAMA_KECAM ");
                                                                                                                        for (int i = 0; i < listKec.size(); i++) {
                                                                                                                            Kecamatan kecamatan = (Kecamatan) listKec.get(i);
                                                                                                                            kec_key.add(kecamatan.getNmKecamatan());
                                                                                                                            kec_value.add(String.valueOf(kecamatan.getOID()));
                                                                                                                        }
                                                                                                                        %>
                                                                                                                        <%= ControlCombo.draw(frmDistrict.fieldNames[FrmDistrict.FRM_FIELD_ID_KECAMATAN], "formElemen", null, "" + district.getIdKecamatan(), kec_value, kec_key, "onChange=\"javascript:cmdUpdateForm()\"")%>
                                                                                                                    </td>
                                                                                                                </tr>
                                                                                                                <tr align="left" valign="top">
                                                                                                                    <td valign="top" width="18%">Sub District</td>
                                                                                                                    <td width="82%">
                                                                                                                        <input type="text" name="<%=frmDistrict.fieldNames[FrmDistrict.FRM_FIELD_NAMA_DISTRICT]%>"  value="<%= district.getNmDistrict()%>" class="elemenForm" size="35">
                                                                                                                        * <%=frmDistrict.getErrorMsg(FrmDistrict.FRM_FIELD_NAMA_DISTRICT)%></td>
                                                                                                                </tr>
                                                                                                            </table>
                                                                                                        </td>
                                                                                                    </tr>
                                                                                                    <tr align="left" valign="top" >
                                                                                                        <td class="command">
                                                                                                            <%
                                                                                                                ctrLine.setLocationImg(approot + "/images");
                                                                                                                ctrLine.initDefault();
                                                                                                                ctrLine.setTableWidth("80%");
                                                                                                                String scomDel = "javascript:cmdAsk('" + oidDistrict + "')";
                                                                                                                String sconDelCom = "javascript:cmdConfirmDelete('" + oidDistrict + "')";
                                                                                                                String scancel = "javascript:cmdEdit('" + oidDistrict + "')";
                                                                                                               
                                                                                                                ctrLine.setBackCaption("Back to List");
                                                                                                                ctrLine.setCommandStyle("buttonlink");
                                                                                                                ctrLine.setBackCaption("Back to Regency List ");
                                                                                                                ctrLine.setSaveCaption("Save Regency");
                                                                                                                ctrLine.setConfirmDelCaption("Yes Delete Regency");
                                                                                                                ctrLine.setDeleteCaption("Delete Regency");

                                                                                                                if (privDelete) {
                                                                                                                    ctrLine.setConfirmDelCommand(sconDelCom);
                                                                                                                    ctrLine.setDeleteCommand(scomDel);
                                                                                                                    ctrLine.setEditCommand(scancel);
                                                                                                                } else {
                                                                                                                    ctrLine.setConfirmDelCaption("");
                                                                                                                    ctrLine.setDeleteCaption("");
                                                                                                                    ctrLine.setEditCaption("");
                                                                                                                }

                                                                                                                if (privAdd == false && privUpdate == false) {
                                                                                                                    ctrLine.setSaveCaption("");
                                                                                                                }

                                                                                                                if (privAdd == false) {
                                                                                                                    ctrLine.setAddCaption("");
                                                                                                                }

                                                                                                                if (iCommand == Command.ASK) {
                                                                                                                    ctrLine.setDeleteQuestion(msgString);
                                                                                                                }
                                                                                                            %>
                                                                                                            <%= ctrLine.drawImage(iCommand, iErrCode, msgString)%>
                                                                                                        </td>
                                                                                                    </tr>
                                                                                                </table>
                                                                                                <%}%>
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
                                <%@include file="../footer.jsp" %>
                            </td>
                            
            </tr>
            <%}else{%>
            <tr> 
                <td colspan="2" height="20" > <!-- #BeginEditable "footer" --> 
      <%@ include file = "../main/footer.jsp" %>
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
