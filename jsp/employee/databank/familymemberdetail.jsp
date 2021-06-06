<%@page import="java.text.DateFormat"%>
<%@page import="java.text.ParseException"%>
<%@page import="java.text.SimpleDateFormat"%>
<%
    /*
     * Page Name  		:  familymemberdetail.jsp
     * Created on 		:  [09-03-2019] [8.30] AM
     *
     * @author  		: Yokego
     * @version  		: 01
     */

    /**
     * *****************************************************************
     * Page Description : this page represent family member of employee Imput
     * Parameters : [input parameter ...] Output : [output ...]
     * *****************************************************************
     */
%>

<%@ page language = "java" %>
<!-- package java -->
<%@ page import = "java.util.*" %>

<!-- package dimata -->
<%@ page import = "com.dimata.util.*" %>

<!-- package qdep -->
<%@ page import = "com.dimata.gui.jsp.*" %>
<%@ page import = "com.dimata.qdep.form.*" %>

<!--package harisma -->
<%@ page import = "com.dimata.harisma.entity.masterdata.*" %>
<%@ page import = "com.dimata.harisma.entity.employee.*" %>
<%@ page import = "com.dimata.harisma.form.employee.*" %>
<%@ page import = "com.dimata.harisma.entity.admin.*" %>
<%@ include file = "../../main/javainit.jsp" %>

<% int appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_EMPLOYEE, AppObjInfo.G2_DATABANK, AppObjInfo.OBJ_DATABANK);%>
<%@ include file = "../../main/checkuser.jsp" %>
<%
    /* Check privilege except VIEW, view is already checked on checkuser.jsp as basic access*/
//boolean privAdd=userSession.checkPrivilege(AppObjInfo.composeCode(appObjCode, AppObjInfo.COMMAND_ADD));
//boolean privUpdate=userSession.checkPrivilege(AppObjInfo.composeCode(appObjCode, AppObjInfo.COMMAND_UPDATE));
//boolean privDelete=userSession.checkPrivilege(AppObjInfo.composeCode(appObjCode, AppObjInfo.COMMAND_DELETE));
%>

<!-- Jsp Block -->

<%//
    int iCommand = FRMQueryString.requestCommand(request);
    int start = FRMQueryString.requestInt(request, "start");
    int prevCommand = FRMQueryString.requestInt(request, "prev_command");
    long oidEmployee = FRMQueryString.requestLong(request, "employee_oid");
    long oidFamilyMember = FRMQueryString.requestLong(request, "oid");

    String result = "";
    
    FamilyMember familyMember = new FamilyMember();
    if (iCommand == Command.EDIT) {
        try {
            familyMember = PstFamilyMember.fetchExc(oidFamilyMember);
        } catch (Exception e) {
            result = e.getMessage();
        }
    }
    
    Vector vctMemberDetail = new Vector();
    if (iCommand == Command.SAVE) {
        result = "OK";
        try {
            String famName = FRMQueryString.requestString(request, FrmFamilyMember.fieldNames[FrmFamilyMember.FRM_FIELD_FULL_NAME]);
            String famRelation = FRMQueryString.requestString(request, FrmFamilyMember.fieldNames[FrmFamilyMember.FRM_FIELD_RELATIONSHIP]);
            String famRelationIndex = FRMQueryString.requestString(request, FrmFamilyMember.fieldNames[FrmFamilyMember.FRM_FIELD_RELATIONSHIP_INDEX]);
            String famRelationTitle = FRMQueryString.requestString(request, FrmFamilyMember.fieldNames[FrmFamilyMember.FRM_FIELD_RELATION_TITLE]);
            int sex = FRMQueryString.requestInt(request, FrmFamilyMember.fieldNames[FrmFamilyMember.FRM_FIELD_SEX]);
            Date famBirth = FRMQueryString.requestDate(request, FrmFamilyMember.fieldNames[FrmFamilyMember.FRM_FIELD_BIRTH_DATE]);
            boolean famIgnore = FRMQueryString.requestBoolean(request, FrmFamilyMember.fieldNames[FrmFamilyMember.FRM_FIELD_IGNORE_BIRTH]);
            long educationId = FRMQueryString.requestLong(request, FrmFamilyMember.fieldNames[FrmFamilyMember.FRM_FIELD_EDUCATION_ID]);
            String famEducationDegree = FRMQueryString.requestString(request, FrmFamilyMember.fieldNames[FrmFamilyMember.FRM_FIELD_EDUCATION_DEGREE]);
            long relegionId = FRMQueryString.requestLong(request, FrmFamilyMember.fieldNames[FrmFamilyMember.FRM_FIELD_RELIGION_ID]);
            String bloodType = FRMQueryString.requestString(request, FrmFamilyMember.fieldNames[FrmFamilyMember.FRM_FIELD_BLOOD_TYPE]);
            int status = FRMQueryString.requestInt(request, FrmFamilyMember.fieldNames[FrmFamilyMember.FRM_FIELD_STATUS]);
            String phoneNumber = FRMQueryString.requestString(request, FrmFamilyMember.fieldNames[FrmFamilyMember.FRM_FIELD_PHONE_NUMBER]);
            
            String famAddress = FRMQueryString.requestString(request, FrmFamilyMember.fieldNames[FrmFamilyMember.FRM_FIELD_ADDRESS]);
            
            String famJob = FRMQueryString.requestString(request, FrmFamilyMember.fieldNames[FrmFamilyMember.FRM_FIELD_JOB]);
            boolean famGuaranted = FRMQueryString.requestBoolean(request, FrmFamilyMember.fieldNames[FrmFamilyMember.FRM_FIELD_GUARANTEED]);
            String companyName = FRMQueryString.requestString(request, FrmFamilyMember.fieldNames[FrmFamilyMember.FRM_FIELD_COMPANY_NAME]);
            String companyPhoneNumber = FRMQueryString.requestString(request, FrmFamilyMember.fieldNames[FrmFamilyMember.FRM_FIELD_COMPANY_PHONE_NUMBER]);
            String companyAddress = FRMQueryString.requestString(request, FrmFamilyMember.fieldNames[FrmFamilyMember.FRM_FIELD_COMPANY_ADDRESS]);
            String companyPostalCode = FRMQueryString.requestString(request, FrmFamilyMember.fieldNames[FrmFamilyMember.FRM_FIELD_COMPANY_POSTAL_CODE]);
            
            //PRESENT
            String presentAddress = FRMQueryString.requestString(request, FrmFamilyMember.fieldNames[FrmFamilyMember.FRM_FIELD_PRESENT_ADDRESS]);
            String subvillagePresentAddress = FRMQueryString.requestString(request, FrmFamilyMember.fieldNames[FrmFamilyMember.FRM_FIELD_SUBVILLAGE_PRESENT_ADDRESS]);
            String villagePresentAddress = FRMQueryString.requestString(request, FrmFamilyMember.fieldNames[FrmFamilyMember.FRM_FIELD_VILLAGE_PRESENT_ADDRESS]);
            String subregencyPresentAddress = FRMQueryString.requestString(request, FrmFamilyMember.fieldNames[FrmFamilyMember.FRM_FIELD_SUBDISTRIC_PRESENT_ADDRESS]);
            String regencyPresentAddress = FRMQueryString.requestString(request, FrmFamilyMember.fieldNames[FrmFamilyMember.FRM_FIELD_DISTRIC_PRESENT_ADDRESS]);
            String provincePresentAddress = FRMQueryString.requestString(request, FrmFamilyMember.fieldNames[FrmFamilyMember.FRM_FIELD_REGENCY_PRESENT_ADDRESS]);
            String phoneNumberPresentAddress = FRMQueryString.requestString(request, FrmFamilyMember.fieldNames[FrmFamilyMember.FRM_FIELD_PHONE_NUMBER_PRESENT_ADDRESS]);
            String postalCodePresentAddress = FRMQueryString.requestString(request, FrmFamilyMember.fieldNames[FrmFamilyMember.FRM_FIELD_POSTAL_CODE_PRESENT_ADDRESS]);
            
            //ID
            String idCardAddress = FRMQueryString.requestString(request, FrmFamilyMember.fieldNames[FrmFamilyMember.FRM_FIELD_ID_CARD_ADDRESS]);
            String subvillageIdCardAddress = FRMQueryString.requestString(request, FrmFamilyMember.fieldNames[FrmFamilyMember.FRM_FIELD_SUBVILLAGE_ID_CARD_ADDRESS]);
            String villageIdCardAddress = FRMQueryString.requestString(request, FrmFamilyMember.fieldNames[FrmFamilyMember.FRM_FIELD_VILLAGE_ID_CARD_ADDRESS]);
            String subregencyIdCardAddress = FRMQueryString.requestString(request, FrmFamilyMember.fieldNames[FrmFamilyMember.FRM_FIELD_SUBDISTRIC_ID_CARD_ADDRESS]);
            String regencyIdCardAddress = FRMQueryString.requestString(request, FrmFamilyMember.fieldNames[FrmFamilyMember.FRM_FIELD_DISTRIC_ID_CARD_ADDRESS]);
            String provinceIdCardAddress = FRMQueryString.requestString(request, FrmFamilyMember.fieldNames[FrmFamilyMember.FRM_FIELD_REGENCY_ID_CARD_ADDRESS]);
            String phoneNumberIdCardAddress = FRMQueryString.requestString(request, FrmFamilyMember.fieldNames[FrmFamilyMember.FRM_FIELD_PHONE_NUMBER_ID_CARD_ADDRESS]);
            String postalCodeIdCardAddress = FRMQueryString.requestString(request, FrmFamilyMember.fieldNames[FrmFamilyMember.FRM_FIELD_POSTAL_CODE_ID_CARD_ADDRESS]);
            
            //SINCE
            String sinceAddress = FRMQueryString.requestString(request, FrmFamilyMember.fieldNames[FrmFamilyMember.FRM_FIELD_SINCE_ADDRESS]);
            String subvillageSinceAddress = FRMQueryString.requestString(request, FrmFamilyMember.fieldNames[FrmFamilyMember.FRM_FIELD_SUBVILLAGE_SINCE_ADDRESS]);
            String villageSinceAddress = FRMQueryString.requestString(request, FrmFamilyMember.fieldNames[FrmFamilyMember.FRM_FIELD_VILLAGE_SINCE_ADDRESS]);
            String subregencySinceAddress = FRMQueryString.requestString(request, FrmFamilyMember.fieldNames[FrmFamilyMember.FRM_FIELD_SUBDISTRIC_SINCE_ADDRESS]);
            String regencySinceAddress = FRMQueryString.requestString(request, FrmFamilyMember.fieldNames[FrmFamilyMember.FRM_FIELD_DISTRIC_SINCE_ADDRESS]);
            String provinceSinceAddress = FRMQueryString.requestString(request, FrmFamilyMember.fieldNames[FrmFamilyMember.FRM_FIELD_REGENCY_SINCE_ADDRESS]);
            String phoneNumberSinceAddress = FRMQueryString.requestString(request, FrmFamilyMember.fieldNames[FrmFamilyMember.FRM_FIELD_PHONE_NUMBER_SINCE_ADDRESS]);
            String postalCodeSinceAddress = FRMQueryString.requestString(request, FrmFamilyMember.fieldNames[FrmFamilyMember.FRM_FIELD_POSTAL_CODE_SINCE_ADDRESS]);
            
            if (famIgnore == true) {
                String startDateString = "15/31/0000";
                DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
                try {
                    famBirth = df.parse(startDateString);
                } catch (ParseException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    //handle exception
                }
            }

            familyMember.setOID(oidFamilyMember);
            familyMember.setEmployeeId(oidEmployee);
            familyMember.setFullName(famName);
            familyMember.setRelationship(famRelation);
            familyMember.setRelationshipIndex(famRelationIndex);
            familyMember.setRelationTitle(famRelationTitle);
            familyMember.setSex(sex);
            familyMember.setBirthDate(famBirth);
            familyMember.setIgnoreBirth(famIgnore);
            familyMember.setEducationId(educationId);
            familyMember.setEducationDegree(famEducationDegree);
            familyMember.setReligionId(relegionId);
            familyMember.setBloodType(bloodType);
            familyMember.setStatus(status);
            familyMember.setPhoneNumber(phoneNumber);
            
            familyMember.setAddress(famAddress);
            
            //PRESENT
            familyMember.setJob(famJob);
            familyMember.setGuaranteed(famGuaranted);
            familyMember.setCompanyName(companyName);
            familyMember.setCompanyPhone(companyPhoneNumber);
            familyMember.setCompanyAddress(companyAddress);
            familyMember.setCompanyPos(companyPostalCode);
            
            //ID
            familyMember.setPresentAddress(presentAddress);
            familyMember.setSubvillagePresentAddress(subvillagePresentAddress);
            familyMember.setVillagePresentAddress(villagePresentAddress);
            familyMember.setSubregencyPresentAddress(subregencyPresentAddress);
            familyMember.setRegencyPresentAddress(regencyPresentAddress);
            familyMember.setProvincePresentAddress(provincePresentAddress);
            familyMember.setPhonePresentAddress(phoneNumberPresentAddress);
            familyMember.setPostalcodePresentAddress(postalCodePresentAddress);
            
            //SINCE
            familyMember.setIdCardAddress(idCardAddress);
            familyMember.setSubvillageIdCardAddress(subvillageIdCardAddress);
            familyMember.setVillageIdCardAddress(villageIdCardAddress);
            familyMember.setSubregencyIdCardAddress(subregencyIdCardAddress);
            familyMember.setRegencyIdCardAddress(regencyIdCardAddress);
            familyMember.setProvinceIdCardAddress(provinceIdCardAddress);
            familyMember.setPhoneIdCardAddress(phoneNumberIdCardAddress);
            familyMember.setPostalcodeIdCardAddress(postalCodeIdCardAddress);
            
            familyMember.setSinceAddress(sinceAddress);
            familyMember.setSubvillageSinceAddress(subvillageSinceAddress);
            familyMember.setVillageSinceAddress(villageSinceAddress);
            familyMember.setSubregencySinceAddress(subregencySinceAddress);
            familyMember.setRegencySinceAddress(regencySinceAddress);
            familyMember.setProvinceSinceAddress(provinceSinceAddress);
            familyMember.setPhoneSinceAddress(phoneNumberSinceAddress);
            familyMember.setPostalcodeSinceAddress(postalCodeSinceAddress);
            
            if (oidFamilyMember == 0) {
                PstFamilyMember.insertExc(familyMember);
            } else {
                PstFamilyMember.updateExc(familyMember);
            }
            //vctMemberDetail.add(familyMember);
        } catch (Exception exc) {
            result = exc.toString();
            System.out.println(exc);
        }
    }

    CtrlFamilyMember ctrlFamilyMember = new CtrlFamilyMember(request);
    int iErrCode = FRMMessage.NONE;
    iErrCode = 0;//ctrlFamilyMember.action(iCommand, oidFamilyMember, oidEmployee, vctMemberDetail, request, emplx.getFullName(), appUserIdSess);
    //familyMember = ctrlFamilyMember.getFamilyMember();
    FrmFamilyMember frmFamilyMember = ctrlFamilyMember.getForm();
    String msgString = ctrlFamilyMember.getMessage();
    
    ControlLine ctrLine = new ControlLine();

    session.putValue("familymemberdetail", vctMemberDetail);
    session.putValue("oidFamilyMember", oidFamilyMember);
%>

<html><!-- #BeginTemplate "/Templates/maintab.dwt" -->
    <head>
        <!-- #BeginEditable "doctitle" -->
        <title>HARISMA - Family Member</title>
        <script language="JavaScript">
<!--
            function cmdAdd() {
                document.FrmFamilyMember.family_member_oid.value = "0";
                document.FrmFamilyMember.command.value = "<%=Command.ADD%>";
                document.FrmFamilyMember.prev_command.value = "<%=prevCommand%>";
                document.FrmFamilyMember.action = "familymember.jsp";
                document.FrmFamilyMember.submit();
            }

            function cmdAsk(oidFamilyMember) {
                document.FrmFamilyMember.family_member_oid.value = oidFamilyMember;
                document.FrmFamilyMember.command.value = "<%=Command.ASK%>";
                document.FrmFamilyMember.prev_command.value = "<%=prevCommand%>";
                document.FrmFamilyMember.action = "familymember.jsp";
                document.FrmFamilyMember.submit();
            }

            function cmdConfirmDelete(oidFamilyMember) {
                document.FrmFamilyMember.family_member_oid.value = oidFamilyMember;
                document.FrmFamilyMember.command.value = "<%=Command.DELETE%>";
                document.FrmFamilyMember.prev_command.value = "<%=prevCommand%>";
                document.FrmFamilyMember.action = "familymember.jsp";
                document.FrmFamilyMember.submit();
            }

            function cmdSave() {
                document.FrmFamilyMember.command.value = "<%=Command.SAVE%>";
                document.FrmFamilyMember.prev_command.value = "<%=prevCommand%>";
                document.FrmFamilyMember.action = "familymemberdetail.jsp";
                document.FrmFamilyMember.submit();
            }

            function cmdBack(oidFamilyMember) {
                document.FrmFamilyMember.oid.value = oidFamilyMember;
                document.FrmFamilyMember.command.value = "<%=Command.BACK%>";
                document.FrmFamilyMember.prev_command.value = "<%=prevCommand%>";
                document.FrmFamilyMember.action = "familymemberdetail.jsp";
                document.FrmFamilyMember.submit();
            }

            function cmdListFirst() {
                document.FrmFamilyMember.command.value = "<%=Command.FIRST%>";
                document.FrmFamilyMember.prev_command.value = "<%=Command.FIRST%>";
                document.FrmFamilyMember.action = "familymemberdetail.jsp";
                document.FrmFamilyMember.submit();
            }

            function cmdListPrev() {
                document.FrmFamilyMember.command.value = "<%=Command.PREV%>";
                document.FrmFamilyMember.prev_command.value = "<%=Command.PREV%>";
                document.FrmFamilyMember.action = "familymemberdetail.jsp";
                document.FrmFamilyMember.submit();
            }

            function cmdListNext() {
                document.FrmFamilyMember.command.value = "<%=Command.NEXT%>";
                document.FrmFamilyMember.prev_command.value = "<%=Command.NEXT%>";
                document.FrmFamilyMember.action = "familymemberdetail.jsp";
                document.FrmFamilyMember.submit();
            }

            function cmdListLast() {
                document.FrmFamilyMember.command.value = "<%=Command.LAST%>";
                document.FrmFamilyMember.prev_command.value = "<%=Command.LAST%>";
                document.FrmFamilyMember.action = "familymemberdetail.jsp";
                document.FrmFamilyMember.submit();
            }
            function fnTrapKD() {
                //alert(event.keyCode);
                switch (event.keyCode) {
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
            //-->
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
            <!--
             function hideObjectForEmployee() {
            }

            function hideObjectForLockers() {
            }

            function hideObjectForCanteen() {
            }

            function hideObjectForClinic() {
            }

            function hideObjectForMasterdata() {
            }

            function MM_swapImgRestore() { //v3.0
                var i, x, a = document.MM_sr;
                for (i = 0; a && i < a.length && (x = a[i]) && x.oSrc; i++)
                    x.src = x.oSrc;
            }

            function MM_preloadImages() { //v3.0
                var d = document;
                if (d.images) {
                    if (!d.MM_p)
                        d.MM_p = new Array();
                    var i, j = d.MM_p.length, a = MM_preloadImages.arguments;
                    for (i = 0; i < a.length; i++)
                        if (a[i].indexOf("#") != 0) {
                            d.MM_p[j] = new Image;
                            d.MM_p[j++].src = a[i];
                        }
                }
            }

            function MM_findObj(n, d) { //v4.0
                var p, i, x;
                if (!d)
                    d = document;
                if ((p = n.indexOf("?")) > 0 && parent.frames.length) {
                    d = parent.frames[n.substring(p + 1)].document;
                    n = n.substring(0, p);
                }
                if (!(x = d[n]) && d.all)
                    x = d.all[n];
                for (i = 0; !x && i < d.forms.length; i++)
                    x = d.forms[i][n];
                for (i = 0; !x && d.layers && i < d.layers.length; i++)
                    x = MM_findObj(n, d.layers[i].document);
                if (!x && document.getElementById)
                    x = document.getElementById(n);
                return x;
            }

            function MM_swapImage() { //v3.0
                var i, j = 0, x, a = MM_swapImage.arguments;
                document.MM_sr = new Array;
                for (i = 0; i < (a.length - 2); i += 3)
                    if ((x = MM_findObj(a[i])) != null) {
                        document.MM_sr[j++] = x;
                        if (!x.oSrc)
                            x.oSrc = x.src;
                        x.src = a[i + 2];
                    }
            }
            //-->
        </SCRIPT>
        <style type="text/css">
            .tblStyle {border-collapse: collapse;font-size: 11px;}
            .tblStyle td {padding: 3px 5px; border: 1px solid #CCC; background-color: #F7F7F7;}
            .title_tbl {font-weight: bold;background-color: #DDD; color: #575757; }
            body {color:#373737;}
            #menu_utama {padding: 9px 14px; border-left: 1px solid #0099FF; font-size: 14px; background-color: #F3F3F3;}
            #menu_title {color:#0099FF; font-size: 14px; font-weight: bold;}
            #menu_teks {color:#CCC;}
            #box_title {padding:9px; background-color: #D5D5D5; font-weight: bold; color:#575757; margin-bottom: 7px; }
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

            .breadcrumb {
                background-color: #EEE;
                color:#0099FF;
                padding: 7px 9px;
            }
            .content-box {
                background-color: #EEE;
                padding: 15px;
                margin: 15px 9px;
                border-radius: 3px;
            }
            .navbar {
                font-family: sans-serif;
                font-size: 12px;
                background-color: #0084ff;
                padding: 7px 9px;
                color : #FFF;
                border-top:1px solid #0084ff;
                border-bottom: 1px solid #0084ff;
            }
            .navbar ul {
                list-style-type: none;
                margin: 0;
                padding: 0;
            }

            .navbar li {
                padding: 7px 9px;
                display: inline;
                cursor: pointer;
            }

            .navbar li:hover {
                background-color: #0b71d0;
                border-bottom: 1px solid #033a6d;
            }

            .active {
                background-color: #0b71d0;
                border-bottom: 1px solid #033a6d;
            }
            .title_part {color:#FF7E00; background-color: #F7F7F7; border-left: 1px solid #0099FF; padding: 9px 11px;}
            #confirm {
                padding: 18px 21px;
                background-color: #FF6666;
                color: #FFF;
                border: 1px solid #CF5353;
            }
            #btn-confirm {
                padding: 3px; border: 1px solid #CF5353; 
                background-color: #CF5353; color: #FFF; 
                font-size: 11px; cursor: pointer;
            }
        </style>
        <script type="text/javascript">
            function cmdAsking(oid) {
                document.FrmFamilyMember.oid.value = oid;
                document.FrmFamilyMember.command.value = "<%=Command.ASK%>";
                document.FrmFamilyMember.action = "familymemberdetail.jsp";
                document.FrmFamilyMember.submit();
            }

            function cmdHapus(oid) {
                document.FrmFamilyMember.oid.value = oid;
                document.FrmFamilyMember.command.value = "<%=Command.DELETE%>";
                document.FrmFamilyMember.action = "familymemberdetail.jsp";
                document.FrmFamilyMember.submit();
            }

            function cmdReload() {
                window.location.reload(true);
            }
        </script>
        <style type="text/css">
            body {background-color: #EEE;}
            .header {

            }
            .content-main {
                background-color: #FFF;
                margin: 25px 23px 59px 23px;
                border: 1px solid #DDD;
                border-radius: 5px;
            }
            .content-info {
                padding: 21px;
                border-bottom: 1px solid #E5E5E5;
            }
            .content-title {
                padding: 21px;
                border-bottom: 1px solid #E5E5E5;
                margin-bottom: 5px;
                background-color: #EEE;
            }
            #title-large {
                color: #575757;
                font-size: 16px;
                font-weight: bold;
            }
            #title-small {
                color:#797979;
                font-size: 11px;
            }
            .content {
                padding: 21px;
            }
            .btn {
                background: #ebebeb;
                border-radius: 3px;
                font-family: Arial;
                color: #7a7a7a;
                font-size: 12px;
                padding: 2px 3px ;
                border: solid #d9d9d9 1px;
                text-decoration: none;
            }

            .btn:hover {
                color: #FFF;
                background: #FF6666;
                text-decoration: none;
                border: 1px solid #d74e4e;
            }

            .btn-small {
                padding: 3px; border: 1px solid #CCC; 
                background-color: #EEE; color: #777777; 
                font-size: 11px; cursor: pointer;
            }
            .btn-small:hover {border: 1px solid #999; background-color: #CCC; color: #FFF;}

            .tbl-main {border-collapse: collapse; font-size: 11px; background-color: #FFF; margin: 0px;}
            .tbl-main td {padding: 4px 7px; border: 1px solid #DDD; }
            #tbl-title {font-weight: bold; background-color: #F5F5F5; color: #575757;}

            .tbl-small {border-collapse: collapse; font-size: 11px; background-color: #FFF;}
            .tbl-small td {padding: 2px 3px; border: 1px solid #DDD; }

            #caption {padding: 7px 0px 2px 0px; font-size: 12px; font-weight: bold; color: #575757;}
            #div_input {}

            .form-style {
                font-size: 12px;
                color: #575757;
                border: 1px solid #DDD;
                border-radius: 5px;
            }
            .form-title {
                padding: 11px 21px;
                margin-bottom: 2px;
                border-bottom: 1px solid #DDD;
                background-color: #EEE;
                border-top-left-radius: 5px;
                border-top-right-radius: 5px;
                font-weight: bold;
            }
            .form-content {
                padding: 21px;
            }
            .form-footer {
                border-top: 1px solid #DDD;
                padding: 11px 21px;
                margin-top: 2px;
                background-color: #EEE;
                border-bottom-left-radius: 5px;
                border-bottom-right-radius: 5px;
            }
            #confirm {
                padding: 18px 21px;
                background-color: #FF6666;
                color: #FFF;
                border: 1px solid #CF5353;
            }
            #btn-confirm {
                padding: 3px; border: 1px solid #CF5353; 
                background-color: #CF5353; color: #FFF; 
                font-size: 11px; cursor: pointer;
            }
            .footer-page {

            }

        </style>
        <!-- #EndEditable -->
        <script language="JavaScript">
            function getFamship(select) {
                var text = select.options[select.selectedIndex].text;
                var valueIndex = "";
                if (text === "Anak Pertama") {
                    valueIndex = "1";
                } else if (text === "Anak Kedua") {
                    valueIndex = "2";
                } else {
                    valueIndex = "0";
                }
                document.getElementById("index_famship").value = "ke-" + valueIndex;
            }
        </script>
    </head>

    <body>
        <div class="content-main">
            <form name="FrmFamilyMember" method="post" action="">
                <input type="hidden" name="command" value="<%=iCommand%>">
                <input type="hidden" name="start" value="<%=start%>">
                <input type="hidden" name="prev_command" value="<%=prevCommand%>">
                <input type="hidden" name="employee_oid" value="<%=oidEmployee%>">
                <input type="hidden" name="oid" value="<%=oidFamilyMember%>">
                <div class="content-title">
                    <center>
                        <div id="title-large">Data Keluarga</div>
                    </center>
                </div>
                <div class="content-box">
                    <table>
                        <tr>
                            <td width="6%" nowrap="nowrap"> <div align="left"><strong><%=dictionaryD.getWord(I_Dictionary.FULL_NAME)%></strong></div></td>
                            <td width="30%" nowrap="nowrap">:
                                <input type="text" size="50" name="<%=FrmFamilyMember.fieldNames[FrmFamilyMember.FRM_FIELD_FULL_NAME]%>"  value="<%=familyMember.getFullName()%>"  class="elemenForm">
                            </td>
                        </tr>
                        <tr>
                            <td width="6%" nowrap="nowrap"> <div align="left"><strong><%=dictionaryD.getWord(I_Dictionary.RELATIONSHIP)%></strong></div></td>
                            <td width="30%" nowrap="nowrap">:
                                <%  Vector relationX_value = new Vector(1, 1);
                                    Vector relationX_key = new Vector(1, 1);
                                    Vector listRelationX = PstFamRelation.listAll();
                                    for (int i = 0; i < listRelationX.size(); i++) {
                                        FamRelation famRelation = (FamRelation) listRelationX.get(i);
                                        //relation4_value.add(""+famRelation.getFamRelationType());
                                        relationX_value.add("" + famRelation.getOID());
                                        relationX_key.add(famRelation.getFamRelation());
                                    }
                                %>                                                                                                                                               
                                <%= ControlCombo.draw(FrmFamilyMember.fieldNames[FrmFamilyMember.FRM_FIELD_RELATIONSHIP], "elemenForm", null, "" + familyMember.getRelationship(), relationX_value, relationX_key, "onChange='getFamship(this)'")%>
                                #                               
                                <input type="text" size="4" id="index_famship" name="<%=FrmFamilyMember.fieldNames[FrmFamilyMember.FRM_FIELD_RELATIONSHIP_INDEX]%>"  value="<%=familyMember.getRelationshipIndex()%>"  class="elemenForm" readonly="readonly" /> *Auto 
                            </td>
                            <td width="5%" nowrap="nowrap"><strong><%=dictionaryD.getWord(I_Dictionary.RELATION_TITLE)%></strong> </td>
                            <td width="59%" nowrap="nowrap">:
                                <input type="text" size="30" name="<%=FrmFamilyMember.fieldNames[FrmFamilyMember.FRM_FIELD_RELATION_TITLE]%>"  value="<%=familyMember.getRelationTitle()%>"  class="elemenForm">
                            </td>
                        </tr>
                        <tr>
                            <td width="6%" nowrap="nowrap"> <div align="left"><strong><%=dictionaryD.getWord(I_Dictionary.GENDER)%></strong> </div></td>
                            <td width="30%" nowrap="nowrap">:
                                <% for (int i = 0; i < PstEmployee.sexValue.length; i++) {
                                        String str = "";
                                        if (familyMember.getSex() == PstEmployee.sexValue[i]) {
                                            str = "checked";
                                        }
                                %> <input type="radio" name="<%=(FrmFamilyMember.fieldNames[FrmFamilyMember.FRM_FIELD_SEX])%>" value="<%="" + PstEmployee.sexValue[i]%>" <%=str%> style="border:none">
                                <%=PstEmployee.sexKey[i]%> 
                                <% }%>


                            <td width="5%" nowrap="nowrap"><strong><%=dictionaryD.getWord(I_Dictionary.DATE_OF_BIRTH)%></strong></td>
                            <td width="59%" nowrap="nowrap">:
                                <!--<input type="text" size="6" name="full_name"  value=""  class="elemenForm">-->
                                <%=ControlDate.drawDateEmpty(FrmFamilyMember.fieldNames[FrmFamilyMember.FRM_FIELD_BIRTH_DATE], familyMember.getBirthDate(), "formElemen", 0, -115)%>
                                <input type="checkbox" name="<%=FrmFamilyMember.fieldNames[FrmFamilyMember.FRM_FIELD_IGNORE_BIRTH]%>" value="1"<%if (familyMember.getIgnoreBirth()) {%>checked<%}%>>
                                Ignore 
                            </td>
                        </tr>
                        <tr>
                            <td width="6%" nowrap="nowrap"> <div align="left"><strong><%=dictionaryD.getWord(I_Dictionary.EDUCATION)%></strong></div></td>
                            <td width="30%" nowrap="nowrap">:
                                <%    Vector education_value = new Vector(1, 1);
                                    Vector education_key = new Vector(1, 1);
                                    Vector listEducation = PstEducation.listAll();
                                    for (int i = 0; i < listEducation.size(); i++) {
                                        Education education = (Education) listEducation.get(i);
                                        education_value.add("" + education.getOID());
                                        education_key.add("" + education.getEducation());
                                    }
                                %>
                                <% if ((listEducation != null) && (listEducation.size() > 0)) {%>
                                <%= ControlCombo.draw(FrmFamilyMember.fieldNames[FrmFamilyMember.FRM_FIELD_EDUCATION_ID], "formElemen", null, "" + familyMember.getEducationId(), education_value, education_key)%> 
                                <% } else {%>
                                <font class="comment">No 
                                Education available</font> 
                                <% }%>
                                * <%= frmFamilyMember.getErrorMsg(FrmEmpEducation.FRM_FIELD_EDUCATION_ID)%> 

                            <td width="5%" nowrap="nowrap"><strong><%=dictionaryD.getWord(I_Dictionary.DEGREE)%>/<%=dictionaryD.getWord(I_Dictionary.TITLE)%></strong></td>
                            <td width="59%" nowrap="nowrap">:
                                <input type="text" size="30" name="<%=FrmFamilyMember.fieldNames[FrmFamilyMember.FRM_FIELD_EDUCATION_DEGREE]%>"  value="<%=familyMember.getEducationDegree()%>"  class="elemenForm"> </td>
                        </tr>
                        <tr>
                            <td width="6%" nowrap="nowrap"> <div align="left"><strong><%=dictionaryD.getWord(I_Dictionary.RELIGION)%></strong></div></td>
                            <td width="30%" nowrap="nowrap">:
                                <% Vector relKey = new Vector(1, 1);
                                    Vector relValue = new Vector(1, 1);
                                    Vector listReligion = PstReligion.listAll();
                                    for (int i = 0; i < listReligion.size(); i++) {
                                        Religion religion = (Religion) listReligion.get(i);
                                        relKey.add(religion.getReligion());
                                        relValue.add("" + religion.getOID());
                                    }
                                    out.println(ControlCombo.draw(FrmFamilyMember.fieldNames[FrmFamilyMember.FRM_FIELD_RELIGION_ID], "formElemen", null, "" + familyMember.getReligionId(), relValue, relKey));
                                %> * <%=frmFamilyMember.getErrorMsg(FrmFamilyMember.FRM_FIELD_RELIGION_ID)%> 
                            </td>

                            <td width="5%" nowrap="nowrap"><strong><%=dictionaryD.getWord(I_Dictionary.BLOOD_TYPE)%></strong></td>
                            <td width="59%" nowrap="nowrap">:
                                <%
                                    out.println(ControlCombo.draw(FrmFamilyMember.fieldNames[FrmFamilyMember.FRM_FIELD_BLOOD_TYPE], "formElemen", null, familyMember.getBloodType(), PstFamilyMember.getBlood(), PstFamilyMember.getBlood()));
                                %>
                        </tr>
                        <tr>
                            <td width="6%" nowrap="nowrap"> <div align="left"><strong><%=dictionaryD.getWord(I_Dictionary.STATUS)%></strong></div></td>
                            <td width="30%" nowrap="nowrap">:
                                <% for (int i = 0; i < PstFamilyMember.statusValue.length; i++) {
                                        String str = "";
                                        if (familyMember.getStatus()== PstFamilyMember.statusValue[i]) {
                                            str = "checked";
                                        }
                                %> <input type="radio" name="<%=(FrmFamilyMember.fieldNames[FrmFamilyMember.FRM_FIELD_STATUS])%>" value="<%="" + PstFamilyMember.statusValue[i]%>" <%=str%> style="border:none">
                                <%=PstFamilyMember.statusKey[i]%> 
                                <% }%>
                            </td>

                            <td width="5%" nowrap="nowrap"><strong>Telp./HP</strong></td>
                            <td width="59%" nowrap="nowrap">:
                                <input type="text" size="15" name="<%=(FrmFamilyMember.fieldNames[FrmFamilyMember.FRM_FIELD_PHONE_NUMBER])%>"  value="<%=familyMember.getPhoneNumber()%>"  class="elemenForm"> </td>
                        </tr>
                    </table>
                </div>
                <div class="content-box">
                    <table>
                        <tr>
                            <td width="6%" nowrap="nowrap"> <div align="left"><strong><%=dictionaryD.getWord(I_Dictionary.JOB)%></strong></div></td>
                            <td width="30%" nowrap="nowrap">:
                                <input type="text" size="50" name="<%=FrmFamilyMember.fieldNames[FrmFamilyMember.FRM_FIELD_JOB]%>"  value="<%= familyMember.getJob()%>"  class="elemenForm" > </td>
                            <td width="5%" nowrap="nowrap"><strong><%=dictionaryD.getWord(I_Dictionary.GUARANTED)%></strong> </td>
                            <td width="59%" nowrap="nowrap">:
                                <input type="checkbox" size="30" name="<%=FrmFamilyMember.fieldNames[FrmFamilyMember.FRM_FIELD_GUARANTEED]%>"  value="1"  class="elemenForm"<%if (familyMember.getGuaranteed()) {%>checked<%}%>>
                                Yes </td>
                        </tr>
                        <tr>
                            <td width="6%" nowrap="nowrap"> <div align="left"><strong><%=dictionaryD.getWord(I_Dictionary.COMPANY_NAME)%></strong></div></td>
                            <td width="30%" nowrap="nowrap">:
                                <input type="text" size="50" name="<%=FrmFamilyMember.fieldNames[FrmFamilyMember.FRM_FIELD_COMPANY_NAME]%>"  value="<%=familyMember.getCompanyName()%>"  class="elemenForm" > </td>

                            <td width="5%" nowrap="nowrap"><strong>Telp.</strong></td>
                            <td width="59%" nowrap="nowrap">:
                                <input type="text" size="10" name="<%=FrmFamilyMember.fieldNames[FrmFamilyMember.FRM_FIELD_COMPANY_PHONE_NUMBER]%>"  value="<%=familyMember.getCompanyPhone()%>"  class="elemenForm"> </td>
                        </tr>
                        <tr>
                            <td width="6%" nowrap="nowrap"> <div align="left"><strong><%=dictionaryD.getWord(I_Dictionary.COMPANY_ADDRESS)%></strong></div></td>
                            <td width="30%" nowrap="nowrap">:
                                <input type="text" size="50" name="<%=FrmFamilyMember.fieldNames[FrmFamilyMember.FRM_FIELD_COMPANY_ADDRESS]%>"  value="<%=familyMember.getCompanyAddress()%>"  class="elemenForm"> </td>
                            <td width="5%" nowrap="nowrap"><strong><%=dictionaryD.getWord(I_Dictionary.ZIP_CODE)%></strong></td>
                            <td width="59%" nowrap="nowrap">:
                                <input type="text" size="5" name="<%=FrmFamilyMember.fieldNames[FrmFamilyMember.FRM_FIELD_COMPANY_POSTAL_CODE]%>"  value="<%=familyMember.getCompanyPos()%>"  class="elemenForm"> </td>
                        </tr>
                    </table>
                </div>
                <div class="content-box">
                    <table>
                        <tr>
                            <td width="6%" nowrap="nowrap"> <div align="left"><strong><%=dictionaryD.getWord(I_Dictionary.ADDRESS_PRESENT)%></strong></div></td>
                            <td width="30%" nowrap="nowrap">: <input type="text" size="43" name="<%=FrmFamilyMember.fieldNames[FrmFamilyMember.FRM_FIELD_PRESENT_ADDRESS]%>"  value="<%=familyMember.getPresentAddress()%>"  class="elemenForm"> </td>
                        </tr>
                        <tr>
                            <td width="6%" nowrap="nowrap"> <div align="left"><strong><%=dictionaryD.getWord(I_Dictionary.SUB_VILLAGE)%></strong></div></td>
                            <td width="30%" nowrap="nowrap">: <input type="text" size="25" name="<%=FrmFamilyMember.fieldNames[FrmFamilyMember.FRM_FIELD_SUBVILLAGE_PRESENT_ADDRESS]%>"  value="<%=familyMember.getSubvillagePresentAddress()%>"  class="elemenForm"> </td>
                            <td width="5%" nowrap="nowrap"><strong><%=dictionaryD.getWord(I_Dictionary.VILLAGE)%></strong></td>
                            <td width="59%" nowrap="nowrap">: <input type="text" size="20" name="<%=FrmFamilyMember.fieldNames[FrmFamilyMember.FRM_FIELD_VILLAGE_PRESENT_ADDRESS]%>"  value="<%=familyMember.getVillagePresentAddress()%>"  class="elemenForm"> </td>
                        </tr>
                        <tr>
                            <td width="6%" nowrap="nowrap"> <div align="left"><strong><%=dictionaryD.getWord(I_Dictionary.SUB_REGENCY)%></strong></div></td>
                            <td width="30%" nowrap="nowrap">: <input type="text" size="25" name="<%=FrmFamilyMember.fieldNames[FrmFamilyMember.FRM_FIELD_SUBDISTRIC_PRESENT_ADDRESS]%>"  value="<%=familyMember.getSubregencyPresentAddress()%>"  class="elemenForm" > </td>
                            <td width="5%" nowrap="nowrap"><strong><%=dictionaryD.getWord(I_Dictionary.REGENCY)%></strong></td>
                            <td width="59%" nowrap="nowrap">: <input type="text" size="20" name="<%=FrmFamilyMember.fieldNames[FrmFamilyMember.FRM_FIELD_DISTRIC_PRESENT_ADDRESS]%>"  value="<%=familyMember.getRegencyPresentAddress()%>"  class="elemenForm" > </td>
                        </tr>
                        <tr>
                            <td width="6%" nowrap="nowrap"> <div align="left"><strong><%=dictionaryD.getWord(I_Dictionary.PROVINCE)%></strong></div></td>
                            <td width="30%" nowrap="nowrap">: <input type="text" size="25" name="<%=FrmFamilyMember.fieldNames[FrmFamilyMember.FRM_FIELD_REGENCY_PRESENT_ADDRESS]%>"  value="<%=familyMember.getProvincePresentAddress()%>"  class="elemenForm"> </td>
                            <td width="5%" nowrap="nowrap"><strong>Telp.</strong></td>
                            <td width="59%" nowrap="nowrap">
                                : <input type="text" size="10" name="<%=FrmFamilyMember.fieldNames[FrmFamilyMember.FRM_FIELD_PHONE_NUMBER_PRESENT_ADDRESS]%>"  value="<%=familyMember.getPhonePresentAddress()%>"  class="elemenForm" >
                                <strong><%=dictionaryD.getWord(I_Dictionary.ZIP_CODE)%></strong>
                                : <input type="text" size="5" name="<%=FrmFamilyMember.fieldNames[FrmFamilyMember.FRM_FIELD_POSTAL_CODE_PRESENT_ADDRESS]%>"  value="<%=familyMember.getPostalcodePresentAddress()%>"  class="elemenForm" > 
                            </td>
                        </tr>
                        <tr><td></td></tr>
                        <tr><td></td></tr>
                        <tr><td></td></tr>
                        <tr>
                            <td width="6%" nowrap="nowrap"> <div align="left"><strong><%=dictionaryD.getWord(I_Dictionary.ADDRESS_ID_CARD)%></strong></div></td>
                            <td width="30%" nowrap="nowrap">: <input type="text" size="43" name="<%=FrmFamilyMember.fieldNames[FrmFamilyMember.FRM_FIELD_ID_CARD_ADDRESS]%>"  value="<%=familyMember.getIdCardAddress()%>"  class="elemenForm" > </td>
                        </tr>
                        <tr>
                            <td width="6%" nowrap="nowrap"> <div align="left"><strong><%=dictionaryD.getWord(I_Dictionary.SUB_VILLAGE)%></strong></div></td>
                            <td width="30%" nowrap="nowrap">: <input type="text" size="25" name="<%=FrmFamilyMember.fieldNames[FrmFamilyMember.FRM_FIELD_SUBVILLAGE_ID_CARD_ADDRESS]%>"  value="<%=familyMember.getSubvillageIdCardAddress()%>"  class="elemenForm"> </td>
                            <td width="5%" nowrap="nowrap"><strong><%=dictionaryD.getWord(I_Dictionary.VILLAGE)%></strong></td>
                            <td width="59%" nowrap="nowrap">: <input type="text" size="20" name="<%=FrmFamilyMember.fieldNames[FrmFamilyMember.FRM_FIELD_VILLAGE_ID_CARD_ADDRESS]%>"  value="<%=familyMember.getVillageIdCardAddress()%>"  class="elemenForm" > </td>
                        </tr>
                        <tr>
                            <td width="6%" nowrap="nowrap"> <div align="left"><strong><%=dictionaryD.getWord(I_Dictionary.SUB_REGENCY)%></strong></div></td>
                            <td width="30%" nowrap="nowrap">: <input type="text" size="25" name="<%=FrmFamilyMember.fieldNames[FrmFamilyMember.FRM_FIELD_SUBDISTRIC_ID_CARD_ADDRESS]%>"  value="<%=familyMember.getSubregencyIdCardAddress()%>"  class="elemenForm"> </td>
                            <td width="5%" nowrap="nowrap"><strong><%=dictionaryD.getWord(I_Dictionary.REGENCY)%></strong></td>
                            <td width="59%" nowrap="nowrap">: <input type="text" size="20" name="<%=FrmFamilyMember.fieldNames[FrmFamilyMember.FRM_FIELD_DISTRIC_ID_CARD_ADDRESS]%>"  value="<%=familyMember.getRegencyIdCardAddress()%>"  class="elemenForm"> </td>
                        </tr>
                        <tr>
                            <td width="6%" nowrap="nowrap"> <div align="left"><strong><%=dictionaryD.getWord(I_Dictionary.PROVINCE)%></strong></div></td>
                            <td width="30%" nowrap="nowrap">: <input type="text" size="25" name="<%=FrmFamilyMember.fieldNames[FrmFamilyMember.FRM_FIELD_REGENCY_ID_CARD_ADDRESS]%>"  value="<%=familyMember.getProvinceIdCardAddress()%>"  class="elemenForm"> </td>
                            <td width="5%" nowrap="nowrap"><strong>Telp.</strong></td>
                            <td width="28%" nowrap="nowrap">
                                : <input type="text" size="10" name="<%=FrmFamilyMember.fieldNames[FrmFamilyMember.FRM_FIELD_PHONE_NUMBER_ID_CARD_ADDRESS]%>"  value="<%=familyMember.getPhoneIdCardAddress()%>"  class="elemenForm">
                                <strong><%=dictionaryD.getWord(I_Dictionary.ZIP_CODE)%></strong>
                                : <input type="text" size="5" name="<%=FrmFamilyMember.fieldNames[FrmFamilyMember.FRM_FIELD_POSTAL_CODE_ID_CARD_ADDRESS]%>"  value="<%=familyMember.getPostalcodeIdCardAddress()%>"  class="elemenForm" > 
                            </td>
                        </tr>
                        <tr><td></td></tr>
                        <tr><td></td></tr>
                        <tr><td></td></tr>
                        <tr>
                            <td width="6%" nowrap="nowrap"> <div align="left"><strong><%=dictionaryD.getWord(I_Dictionary.ADDRESS_SINCE)%></strong></div></td>
                            <td width="30%" nowrap="nowrap">: <input type="text" size="43" name="<%=FrmFamilyMember.fieldNames[FrmFamilyMember.FRM_FIELD_SINCE_ADDRESS]%>"  value="<%=familyMember.getSinceAddress()%>"  class="elemenForm" > </td>
                        </tr>
                        <tr>
                            <td width="6%" nowrap="nowrap"> <div align="left"><strong><%=dictionaryD.getWord(I_Dictionary.SUB_VILLAGE)%></strong></div></td>
                            <td width="30%" nowrap="nowrap">: <input type="text" size="25" name="<%=FrmFamilyMember.fieldNames[FrmFamilyMember.FRM_FIELD_SUBVILLAGE_SINCE_ADDRESS]%>"  value="<%=familyMember.getSubvillageSinceAddress()%>"  class="elemenForm"> </td>
                            <td width="5%" nowrap="nowrap"><strong><%=dictionaryD.getWord(I_Dictionary.VILLAGE)%></strong></td>
                            <td width="59%" nowrap="nowrap">: <input type="text" size="20" name="<%=FrmFamilyMember.fieldNames[FrmFamilyMember.FRM_FIELD_VILLAGE_SINCE_ADDRESS]%>"  value="<%=familyMember.getVillageSinceAddress()%>"  class="elemenForm" > </td>
                        </tr>
                        <tr>
                            <td width="6%" nowrap="nowrap"> <div align="left"><strong><%=dictionaryD.getWord(I_Dictionary.SUB_REGENCY)%></strong></div></td>
                            <td width="30%" nowrap="nowrap">: <input type="text" size="25" name="<%=FrmFamilyMember.fieldNames[FrmFamilyMember.FRM_FIELD_SUBDISTRIC_SINCE_ADDRESS]%>"  value="<%=familyMember.getSubregencySinceAddress()%>"  class="elemenForm" > </td>
                            <td width="5%" nowrap="nowrap"><strong><%=dictionaryD.getWord(I_Dictionary.REGENCY)%></strong></td>
                            <td width="59%" nowrap="nowrap">: <input type="text" size="20" name="<%=FrmFamilyMember.fieldNames[FrmFamilyMember.FRM_FIELD_DISTRIC_SINCE_ADDRESS]%>"  value="<%=familyMember.getRegencySinceAddress()%>"  class="elemenForm" > </td>
                        </tr>
                        <tr>
                            <td width="6%" nowrap="nowrap"> <div align="left"><strong><%=dictionaryD.getWord(I_Dictionary.PROVINCE)%></strong></div></td>
                            <td width="30%" nowrap="nowrap">: <input type="text" size="25" name="<%=FrmFamilyMember.fieldNames[FrmFamilyMember.FRM_FIELD_REGENCY_SINCE_ADDRESS]%>"  value="<%=familyMember.getProvinceSinceAddress()%>"  class="elemenForm"> </td>
                            <td width="5%" nowrap="nowrap"><strong>Telp.</strong></td>
                            <td width="59%" nowrap="nowrap">
                                : <input type="text" size="10" name="<%=FrmFamilyMember.fieldNames[FrmFamilyMember.FRM_FIELD_PHONE_NUMBER_SINCE_ADDRESS]%>"  value="<%=familyMember.getPhoneSinceAddress()%>"  class="elemenForm" >
                                <strong><%=dictionaryD.getWord(I_Dictionary.ZIP_CODE)%></strong>
                                : <input type="text" size="5" name="<%=FrmFamilyMember.fieldNames[FrmFamilyMember.FRM_FIELD_POSTAL_CODE_SINCE_ADDRESS]%>"  value="<%=familyMember.getPostalcodeSinceAddress()%>"  class="elemenForm" >
                            </td>
                        </tr>
                        <tr>
                            <td>
                                &nbsp;<%=result%> 
                                <% if (result.equals("OK")) {%>
                                <script>
                                    window.opener.location.reload();
                                    self.close();
                                    alert("<%=dictionaryD.getWord(I_Dictionary.FAMILY_MEMBER)%> [<%=familyMember.getFullName()%>] , <%=FRMMessage.getMessage(1002)%>");
                                </script>
                                <%} else {

                                    }
                                %>
                            </td>
                        </tr>
                    </table>
                </div>
                <div class="command">
                    <%
                        ctrLine.setLocationImg(approot + "/images");

                        ctrLine.initDefault();
                        ctrLine.setTableWidth("80%");
                        String scomDel = "javascript:cmdAsk('" + oidFamilyMember + "')";
                        String sconDelCom = "javascript:cmdConfirmDelete('" + oidFamilyMember + "')";
                        String scancel = "javascript:cmdEdit('" + oidFamilyMember + "')";
                        ctrLine.setBackCaption("");
                        ctrLine.setCommandStyle("buttonlink");
                        ctrLine.setAddCaption("");
                        ctrLine.setSaveCaption("Save Family Member");
                        ctrLine.setDeleteCaption("");
                        ctrLine.setConfirmDelCaption("");

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

                        iCommand = Command.EDIT;
                    %>
                    <%= ctrLine.drawImage(iCommand, iErrCode, msgString)%> 
                </div>
            </form>
        </div>
    </body>
</html>
