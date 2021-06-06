<%-- 
    Document   : flyout
    Created on : Sep 18, 2013, 5:13:29 PM
    Author     : user
--%>

<%@page import="com.dimata.harisma.entity.admin.AppUser"%>
<%@page import="com.dimata.harisma.session.admin.SessUserSession"%>
<%@page import="com.dimata.util.lang.I_Dictionary"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="com.dimata.harisma.entity.admin.AppObjInfo"%>
<%@page import="com.dimata.harisma.entity.template.Flyout"%>


<%if(true){%>
<!-- isMSIE -->
<link rel="stylesheet" href="../stylesheets/flayout_no_ie.css" type="text/css">
<%}else{%>
<style type="text/css">
    .content{position:relative; width:100%; 
/*background-color:white;
background-repeat: repeat-x;
padding-top:61px;*/
min-height: 400px;
height: 400px !important;
background-attachment: local;
 background-image:url(../stylesheets/images/background.png);
 background-repeat: no-repeat;
background-position: right bottom;
 /*background:url(../menuaplikasi/imgHeader/elemet%20grafis.png) no-repeat right ;*/
}
</style>
<link rel="stylesheet" href=""+approot+"/stylesheets/flayout_with_ie.css" type="text/css">
<%}%>
<%
/*
 * Description : mengambil user name pada AppUser
 * Date : 2015-01-20
 * Author : Hendra Putu
*/
SessUserSession userSessionn = (SessUserSession)session.getValue(SessUserSession.HTTP_SESSION_NAME);
AppUser appUserSess1 = userSessionn.getAppUser();
String namaUser1 = appUserSess1.getFullName();
Flyout flyout = new Flyout();

 I_Dictionary dictionaryD = userSession.getUserDictionary();
 dictionaryD.loadWord();

 String strLangFlyOut = "";
 int appLanguageFlyOut = 0;
           if(session.getValue("APPLICATION_LANGUAGE")!=null){
                    strLangFlyOut = String.valueOf(session.getValue("APPLICATION_LANGUAGE"));
            }
 appLanguageFlyOut = (strLangFlyOut!=null && strLangFlyOut.length()>0) ? Integer.parseInt(strLangFlyOut) : 0;
 
 
flyout.setUrlKlik(url);
if(namaUser1.equals("Employee")){
    flyout.addMenuFlyOut("employee.jsp", "Employee", dictionaryD.getWord("DATA_BANK"), false, null, "data_bank", ""+approot+"/employee/databank/employee_view.jsp", "", userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_EMPLOYEE, AppObjInfo.G2_MENU_DATABANK, AppObjInfo.OBJ_MENU_DATABANK), 
             AppObjInfo.COMMAND_VIEW)));
} else {
    flyout.addMenuFlyOut("employee.jsp", "Employee", dictionaryD.getWord("DATA_BANK"), false, null, "data_bank", ""+approot+"/employee/databank/srcemployee.jsp", "", userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_EMPLOYEE, AppObjInfo.G2_MENU_DATABANK, AppObjInfo.OBJ_MENU_DATABANK), 
             AppObjInfo.COMMAND_VIEW)));
}

flyout.addMenuFlyOut("employee.jsp", "Employee", dictionaryD.getWord("COMPANY_STRUCTURE"), false, null, "company_structure", ""+approot+"/employee/databank/srcEmployee_structure.jsp", "", userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_EMPLOYEE, AppObjInfo.G2_MENU_COMPANY_STRUCTURE, AppObjInfo.OBJ_MENU_COMPANY_STRUCTURE), 
             AppObjInfo.COMMAND_VIEW)));
flyout.addMenuFlyOut("employee.jsp", "Employee", "Structure Template", false, null, "structure_template", ""+approot+"/masterdata/structure_template.jsp", "", userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_EMPLOYEE, AppObjInfo.G2_MENU_COMPANY_STRUCTURE, AppObjInfo.OBJ_MENU_COMPANY_STRUCTURE), 
             AppObjInfo.COMMAND_VIEW)));
flyout.addMenuFlyOut("employee.jsp", "Employee", dictionaryD.getWord("NEW_EMPLOYEE"), false, null, "new_employee", ""+approot+"/employee/databank/new_employee_list.jsp", "",userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_EMPLOYEE, AppObjInfo.G2_MENU_NEW_EMPLOYEE, AppObjInfo.OBJ_MENU_NEW_EMPLOYEE), 
             AppObjInfo.COMMAND_VIEW)));
flyout.addMenuFlyOut("employee.jsp", "Employee", dictionaryD.getWord("ABSANCE_MANAGEMENT"), false, null, "absence_management", ""+approot+"/employee/absence/srcabsence.jsp", "",userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_EMPLOYEE, AppObjInfo.G2_MENU_ABSENCE_MANAGEMENT, AppObjInfo.OBJ_MENU_ABSENCE_MANAGEMENT), 
             AppObjInfo.COMMAND_VIEW)));			 
			 
flyout.addMenuFlyOut("employee.jsp", "Employee", dictionaryD.getWord("RECOGNITIONS"), true, "Entry per Departement", "recognitions", "#", ""+approot+"/employee/recognition/recognitiondep.jsp",userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_EMPLOYEE, AppObjInfo.G2_MENU_RECOGNITION, AppObjInfo.OBJ_MENU_ENTRY_PER_DEPT), 
             AppObjInfo.COMMAND_VIEW))); 

flyout.addMenuFlyOut("employee.jsp", "Employee", dictionaryD.getWord("RECOGNITIONS"), true, dictionaryD.getWord("UPDATE"), "recognitions", "#", ""+approot+"/employee/recognition/srcrecognition.jsp",userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_EMPLOYEE, AppObjInfo.G2_MENU_RECOGNITION, AppObjInfo.OBJ_MENU_UPDATE_PER_EMPLOYEE), 
             AppObjInfo.COMMAND_VIEW)));
			 
flyout.addMenuFlyOut("employee.jsp", "Employee", dictionaryD.getWord("RECRUITMENT"), true, dictionaryD.getWord("STAFF_REQUISITION"), "recruitment", "#", ""+approot+"/employee/recruitment/srcstaffrequisition.jsp", userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_EMPLOYEE, AppObjInfo.G2_MENU_RECRUITMENT, AppObjInfo.OBJ_MENU_STAFF_REQUISITION), 
             AppObjInfo.COMMAND_VIEW))); 

flyout.addMenuFlyOut("employee.jsp", "Employee", dictionaryD.getWord("RECRUITMENT"), true, dictionaryD.getWord("EMPLOYEMENT_APPLICATION"), "recruitment", "#", ""+approot+"/employee/recruitment/srcrecrapplication.jsp", userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_EMPLOYEE, AppObjInfo.G2_MENU_RECRUITMENT, AppObjInfo.OBJ_MENU_EMPLOYEE_APPLICATION), 
             AppObjInfo.COMMAND_VIEW)));
			 
flyout.addMenuFlyOut("employee.jsp", "Employee", dictionaryD.getWord("RECRUITMENT"), true, dictionaryD.getWord("ORIENTATION_CHECKLIST"), "recruitment", "#", ""+approot+"/employee/recruitment/srcorichecklist.jsp", userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_EMPLOYEE, AppObjInfo.G2_MENU_RECRUITMENT, AppObjInfo.OBJ_MENU_ORIENTATION_CHECK_LIST), 
             AppObjInfo.COMMAND_VIEW)));

flyout.addMenuFlyOut("employee.jsp", "Employee", dictionaryD.getWord("RECRUITMENT"), true, dictionaryD.getWord("REMINDER"), "recruitment", "#", ""+approot+"/employee/recruitment/reminder.jsp", userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_EMPLOYEE, AppObjInfo.G2_MENU_RECRUITMENT, AppObjInfo.OBJ_MENU_REMINDER), 
             AppObjInfo.COMMAND_VIEW)));
			 
flyout.addMenuFlyOut("employee.jsp", "Employee", dictionaryD.getWord("WARNING_AND_REPRIMAND"), true, dictionaryD.getWord("WARNING"), "warning", "#", ""+approot+"/employee/warning/src_warning.jsp", userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_EMPLOYEE, AppObjInfo.G2_MENU_WARNING_AND_REPRIMAND, AppObjInfo.OBJ_MENU_WARNING), 
             AppObjInfo.COMMAND_VIEW)));

flyout.addMenuFlyOut("employee.jsp", "Employee", dictionaryD.getWord("WARNING_AND_REPRIMAND") , true, dictionaryD.getWord("REPRIMAND"), "warning", "#", ""+approot+"/employee/warning/src_reprimand.jsp", userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_EMPLOYEE, AppObjInfo.G2_MENU_WARNING_AND_REPRIMAND, AppObjInfo.OBJ_MENU_REPRIMAND), 
             AppObjInfo.COMMAND_VIEW)));

flyout.addMenuFlyOut("employee.jsp", "Employee", dictionaryD.getWord("EXCUSE_APPLICATION"), true, dictionaryD.getWord("EXCUSE")+" "+dictionaryD.getWord("FORM"), "excuse_application", "#", ""+approot+"/employee/leave/excuse_app_src.jsp", userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_EMPLOYEE, AppObjInfo.G2_MENU_EXCUSE_APPLICATION, AppObjInfo.OBJ_MENU_EXCUSE_FORM), 
             AppObjInfo.COMMAND_VIEW)));
			 
flyout.addMenuFlyOut("employee.jsp", "Employee", dictionaryD.getWord("ATTENDANCE"), true, dictionaryD.getWord("WORKING_SCHEDULE"), "attendance", "#", ""+approot+"/employee/attendance/srcempschedule.jsp", userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_EMPLOYEE, AppObjInfo.G2_MENU_ATTENDANCE, AppObjInfo.OBJ_MENU_WORKING_SCHEDULE), 
             AppObjInfo.COMMAND_VIEW)));
			 
flyout.addMenuFlyOut("employee.jsp", "Employee", dictionaryD.getWord("ATTENDANCE"), true, dictionaryD.getWord(I_Dictionary.MANUAL_REGISTRATION), "attendance", "#", ""+approot+"/employee/presence/srcpresence.jsp", userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_EMPLOYEE, AppObjInfo.G2_MENU_ATTENDANCE, AppObjInfo.OBJ_MENU_MANUAL_REGISTRATION), 
             AppObjInfo.COMMAND_VIEW)));
			 
flyout.addMenuFlyOut("employee.jsp", "Employee", dictionaryD.getWord("ATTENDANCE"), true, "Re-Generate Sch Holidays", "attendance", "#", ""+approot+"/report/presence/Update_schedule_If_holidays.jsp", userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_EMPLOYEE, AppObjInfo.G2_MENU_ATTENDANCE, AppObjInfo.OBJ_MENU_REGENERATE_SCHEDULE_HOLIDAYS), 
             AppObjInfo.COMMAND_VIEW)));

flyout.addMenuFlyOut("employee.jsp", "Employee", dictionaryD.getWord("ATTENDANCE"), true, "Multiple Set Attendance Status and Reason", "attendance", "#", ""+approot+"/employee/presence/MultipleSetAttendanceStatusandReason.jsp", userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_EMPLOYEE, AppObjInfo.G2_MENU_ATTENDANCE, AppObjInfo.OBJ_MENU_MANUAL_REGISTRATION), 
             AppObjInfo.COMMAND_VIEW)));		 
			 
flyout.addMenuFlyOut("employee.jsp", "Employee", dictionaryD.getWord("EMPLOYEE_OUTLET"), false, null, "employee_outlet", ""+approot+"/employee/outlet/outlet.jsp", "", userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_EMPLOYEE, AppObjInfo.G2_OUTLET, AppObjInfo.OBJ_EMPLOYEE_OUTLET), 
             AppObjInfo.COMMAND_VIEW)));	
if(namaUser1.equals("Employee")){
    flyout.addMenuFlyOut("employee.jsp", "Employee", dictionaryD.getWord("LEAVE_APPLICATION"), true, dictionaryD.getWord("LEAVE")+" "+dictionaryD.getWord("FORM"), "leave_application", "#", ""+approot+"/employee/leave/leave_app_edit_emp.jsp", userSession.checkPrivilege(AppObjInfo.composeCode(
                 AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_EMPLOYEE, AppObjInfo.G2_MENU_LEAVE_APPLICATION, AppObjInfo.OBJ_MENU_LEAVE_FORM), 
                 AppObjInfo.COMMAND_VIEW)));
} else {	 
    flyout.addMenuFlyOut("employee.jsp", "Employee", dictionaryD.getWord("LEAVE_APPLICATION"), true, dictionaryD.getWord("LEAVE")+" "+dictionaryD.getWord("FORM"), "leave_application", "#", ""+approot+"/employee/leave/leave_app_src.jsp", userSession.checkPrivilege(AppObjInfo.composeCode(
                 AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_EMPLOYEE, AppObjInfo.G2_MENU_LEAVE_APPLICATION, AppObjInfo.OBJ_MENU_LEAVE_FORM), 
                 AppObjInfo.COMMAND_VIEW)));

    flyout.addMenuFlyOut("employee.jsp", "Employee", dictionaryD.getWord("LEAVE_APPLICATION"), true, dictionaryD.getWord("LEAVE")+" AL "+dictionaryD.getWord("CLOSING"), "leave_application", "#", ""+approot+"/employee/leave/leave_al_closing.jsp", userSession.checkPrivilege(AppObjInfo.composeCode(
                 AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_EMPLOYEE, AppObjInfo.G2_MENU_LEAVE_APPLICATION, AppObjInfo.OBJ_MENU_LEAVE_AL_CLOSING), 
                 AppObjInfo.COMMAND_VIEW)));

    flyout.addMenuFlyOut("employee.jsp", "Employee", dictionaryD.getWord("LEAVE_APPLICATION"), true, dictionaryD.getWord("LEAVE")+" LI "+dictionaryD.getWord("CLOSING"), "leave_application", "#", ""+approot+"/employee/leave/leave_ll_closing.jsp", userSession.checkPrivilege(AppObjInfo.composeCode(
                 AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_EMPLOYEE, AppObjInfo.G2_MENU_LEAVE_APPLICATION, AppObjInfo.OBJ_MENU_LEAVE_LI_CLOSING), 
                 AppObjInfo.COMMAND_VIEW)));

    flyout.addMenuFlyOut("employee.jsp", "Employee", dictionaryD.getWord("LEAVE_APPLICATION"), true, "DP "+dictionaryD.getWord("MANAGEMENT"), "leave_application", "#", ""+approot+"/employee/attendance/dp.jsp", userSession.checkPrivilege(AppObjInfo.composeCode(
                 AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_EMPLOYEE, AppObjInfo.G2_MENU_LEAVE_APPLICATION, AppObjInfo.OBJ_MENU_DP_MANAGEMENT), 
                 AppObjInfo.COMMAND_VIEW)));

    flyout.addMenuFlyOut("employee.jsp", "Employee", dictionaryD.getWord("LEAVE_APPLICATION"), true, "DP Re-Calculate", "leave_application", "#", ""+approot+"/employee/leave/if_dp_not_balance.jsp", userSession.checkPrivilege(AppObjInfo.composeCode(
                 AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_EMPLOYEE, AppObjInfo.G2_MENU_LEAVE_APPLICATION, AppObjInfo.OBJ_MENU_DP_RECALCULATE), 
                 AppObjInfo.COMMAND_VIEW)));

    flyout.addMenuFlyOut("employee.jsp", "Employee", dictionaryD.getWord("LEAVE_APPLICATION"), true, "Special Stock Management", "leave_application", "#", ""+approot+"/employee/attendance/special_leave_management.jsp", userSession.checkPrivilege(AppObjInfo.composeCode(
                 AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_EMPLOYEE, AppObjInfo.G2_MENU_LEAVE_APPLICATION, AppObjInfo.OBJ_MENU_DP_RECALCULATE), 
                 AppObjInfo.COMMAND_VIEW)));
}
			 
flyout.addMenuFlyOut("employee.jsp", "Employee", dictionaryD.getWord("LEAVE_BALANCING"), true, "Annual Leave", "leave_balancing", "#", ""+approot+"/system/leave/AL_Balancing.jsp", userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_EMPLOYEE, AppObjInfo.G2_MENU_LEAVE_BALANCING, AppObjInfo.OBJ_MENU_ANNUAL_LEAVE), 
             AppObjInfo.COMMAND_VIEW)));
			 
flyout.addMenuFlyOut("employee.jsp", "Employee", dictionaryD.getWord("LEAVE_BALANCING"), true, "Long Leave", "leave_balancing", "#", ""+approot+"/system/leave/LL_Balancing.jsp", userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_EMPLOYEE, AppObjInfo.G2_MENU_LEAVE_BALANCING, AppObjInfo.OBJ_MENU_LONG_LEAVE), 
             AppObjInfo.COMMAND_VIEW)));

flyout.addMenuFlyOut("employee.jsp", "Employee", dictionaryD.getWord("LEAVE_BALANCING"), true, "Day off Payment", "leave_balancing", "#", ""+approot+"/system/leave/DP_Balancing.jsp", userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_EMPLOYEE, AppObjInfo.G2_MENU_LEAVE_BALANCING, AppObjInfo.OBJ_MENU_DAY_OFF_PAYMENT), 
             AppObjInfo.COMMAND_VIEW)));
			 
flyout.addMenuFlyOut("employee.jsp", "Employee", dictionaryD.getWord("PERFORMANCE_MANAGEMENT"), true, dictionaryD.getWord("EXPLANATIONS")+" & "+dictionaryD.getWord("COVERAGE"), "assessment", "#", ""+approot+"/employee/appraisal/expcoverage.jsp", userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_EMPLOYEE, AppObjInfo.G2_MENU_ASSESSMENT, AppObjInfo.OBJ_MENU_EXPLANATION_COVERAGE), 
             AppObjInfo.COMMAND_VIEW)));
			 
flyout.addMenuFlyOut("employee.jsp", "Employee", dictionaryD.getWord("PERFORMANCE_MANAGEMENT"), true, dictionaryD.getWord(I_Dictionary.PERFORMANCE)+" & "+dictionaryD.getWord("ASSESSMENT"), "assessment", "#", ""+approot+"/employee/appraisalnew/srcappraisal.jsp", userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_EMPLOYEE, AppObjInfo.G2_MENU_ASSESSMENT, AppObjInfo.OBJ_MENU_PERFORMANCE_ASSESSMENT), 
             AppObjInfo.COMMAND_VIEW)));

flyout.addMenuFlyOut("employee.jsp", "Employee", dictionaryD.getWord("PERFORMANCE_MANAGEMENT"), true, dictionaryD.getWord(I_Dictionary.COMPANY)+" "+dictionaryD.getWord(I_Dictionary.PERFORMANCE)+" "+dictionaryD.getWord("REPORT"), "assessment", "#", ""+approot+"/employee/kpi/kpi_achievement.jsp", userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_EMPLOYEE, AppObjInfo.G2_MENU_COMPANY_STRUCTURE, AppObjInfo.OBJ_MENU_COMPANY_STRUCTURE), 
             AppObjInfo.COMMAND_VIEW)));

flyout.addMenuFlyOut("employee.jsp", "Employee", dictionaryD.getWord("REWARD_N_PUNISHMENT"), true, "Entri Opname Sales Reward n Punisment", "master_data", "#", ""+approot+"/configrewardnpunishment/search_entriopnamesales.jsp", userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_EMPLOYEE, AppObjInfo.G2_MD_EMPLOYEE, AppObjInfo.OBJ_EMPLOYEE_ENTRI_OPNAME_SALES), 
             AppObjInfo.COMMAND_VIEW)));

/*
 * Description : Menu Candidate
 * Date : 2015 Feb 11
 * Author : Hendra Putu
*/
flyout.addMenuFlyOut("employee.jsp", "Employee", dictionaryD.getWord("CANDIDATE"), true, (appLanguageFlyOut == 1 ? (dictionaryD.getWord("CANDIDATE")+" "+dictionaryD.getWord("SEARCH")): (dictionaryD.getWord("SEARCH")+" "+dictionaryD.getWord("CANDIDATE")) ), "master_data", "#", ""+approot+"/employee/candidate/candidate_search.jsp", userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MASTERDATA, AppObjInfo.G2_MD_EMPLOYEE, AppObjInfo.OBJ_EMPLOYEE_ENTRI_OPNAME_SALES), 
             AppObjInfo.COMMAND_VIEW)));
flyout.addMenuFlyOut("employee.jsp", "Employee", dictionaryD.getWord("CANDIDATE"), true, (appLanguageFlyOut == 1 ?(dictionaryD.getWord("CANDIDATE")+" "+dictionaryD.getWord("HISTORY")): (dictionaryD.getWord("HISTORY")+" "+dictionaryD.getWord("CANDIDATE"))), "master_data", "#", ""+approot+"/employee/candidate/expcoverage.jsp", userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MASTERDATA, AppObjInfo.G2_MD_EMPLOYEE, AppObjInfo.OBJ_EMPLOYEE_ENTRI_OPNAME_SALES), 
             AppObjInfo.COMMAND_VIEW)));

/*
 * Description : Menu Hutang kariawan
 * Date : 2015 April 17
 * Author : Agus Priska
*/
flyout.addMenuFlyOut("employee.jsp", "Employee", dictionaryD.getWord(I_Dictionary.EMPLOYEE_DEDUCTION), true, "Entry "+dictionaryD.getWord(I_Dictionary.EMPLOYEE_DEDUCTION), "master_data", "#", ""+approot+"/employee/arap/arap_main_list.jsp", userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MASTERDATA, AppObjInfo.G2_MD_EMPLOYEE, AppObjInfo.OBJ_EMPLOYEE_ENTRI_OPNAME_SALES), 
             AppObjInfo.COMMAND_VIEW)));
flyout.addMenuFlyOut("employee.jsp", "Employee", dictionaryD.getWord(I_Dictionary.EMPLOYEE_DEDUCTION), true, "List "+dictionaryD.getWord(I_Dictionary.EMPLOYEE_DEDUCTION), "master_data", "#", ""+approot+"/employee/arap/list_employee_deduction.jsp", userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MASTERDATA, AppObjInfo.G2_MD_EMPLOYEE, AppObjInfo.OBJ_EMPLOYEE_ENTRI_OPNAME_SALES), 
             AppObjInfo.COMMAND_VIEW)));
flyout.addMenuFlyOut("employee.jsp", "Employee", dictionaryD.getWord(I_Dictionary.EMPLOYEE_DEDUCTION), true, "Submit "+dictionaryD.getWord(I_Dictionary.ALL)+" to "+dictionaryD.getWord("PAYMENT")+" "+dictionaryD.getWord(I_Dictionary.EMPLOYEE_DEDUCTION), "master_data", "#", ""+approot+"/employee/arap/list_payment_deduction.jsp", userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MASTERDATA, AppObjInfo.G2_MD_EMPLOYEE, AppObjInfo.OBJ_EMPLOYEE_ENTRI_OPNAME_SALES), 
             AppObjInfo.COMMAND_VIEW)));
flyout.addMenuFlyOut("employee.jsp", "Employee", dictionaryD.getWord(I_Dictionary.EMPLOYEE_DEDUCTION), true, "List "+dictionaryD.getWord(I_Dictionary.EMPLOYEE_DEDUCTION), "master_data", "#", ""+approot+"/employee/arap/list_employee_deduction.jsp", userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MASTERDATA, AppObjInfo.G2_MD_EMPLOYEE, AppObjInfo.OBJ_EMPLOYEE_ENTRI_OPNAME_SALES), 
             AppObjInfo.COMMAND_VIEW)));
flyout.addMenuFlyOut("employee.jsp", "Employee", "Form Schedule", true, "Form Schedule Change", "master_data", "#", ""+approot+"/employee/formschedulechange/emp_schedule_change.jsp", userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MASTERDATA, AppObjInfo.G2_MD_EMPLOYEE, AppObjInfo.OBJ_EMPLOYEE_ENTRI_OPNAME_SALES), 
             AppObjInfo.COMMAND_VIEW)));
flyout.addMenuFlyOut("employee.jsp", "Employee", "Form Schedule", true, "Form Schedule EO", "master_data", "#", ""+approot+"/employee/formschedulechange/emp_schedule_EO.jsp", userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MASTERDATA, AppObjInfo.G2_MD_EMPLOYEE, AppObjInfo.OBJ_EMPLOYEE_ENTRI_OPNAME_SALES), 
             AppObjInfo.COMMAND_VIEW)));

flyout.addMenuFlyOut("employee.jsp", "Employee", "Form Schedule", true, "Form Schedule EH", "master_data", "#", ""+approot+"/employee/formschedulechange/emp_schedule_EH.jsp", userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MASTERDATA, AppObjInfo.G2_MD_EMPLOYEE, AppObjInfo.OBJ_EMPLOYEE_ENTRI_OPNAME_SALES), 
             AppObjInfo.COMMAND_VIEW)));


flyout.addMenuFlyOut("training.jsp", "Training", dictionaryD.getWord(I_Dictionary.TRAINING_TYPE), true, dictionaryD.getWord(I_Dictionary.TRAINING_TYPE), "training_type", "#", ""+approot+"/masterdata/list_train_type.jsp", userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_TRAINING, AppObjInfo.G2_TRAINING_TYPE, AppObjInfo.OBJ_MENU_TRAINING_TYPE), 
             AppObjInfo.COMMAND_VIEW)));
			 
flyout.addMenuFlyOut("training.jsp", "Training", dictionaryD.getWord(I_Dictionary.TRAINING_TYPE), true, dictionaryD.getWord(I_Dictionary.TRAINING_VENUE), "training_venue", "#", ""+approot+"/masterdata/list_train_venue.jsp", userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_TRAINING, AppObjInfo.G2_TRAINING_VANUE, AppObjInfo.OBJ_MENU_TRAINING_VANUE), 
             AppObjInfo.COMMAND_VIEW)));
			 			 
flyout.addMenuFlyOut("training.jsp", "Training", dictionaryD.getWord(I_Dictionary.TRAINING_TYPE), true, dictionaryD.getWord(I_Dictionary.TRAINING_TYPE), "training_program", "#", ""+approot+"/masterdata/srctraining.jsp", userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_TRAINING, AppObjInfo.G2_TRAINING_PROGRAM, AppObjInfo.OBJ_MENU_TRAINING_PROGRAM), 
             AppObjInfo.COMMAND_VIEW)));
			 	 
flyout.addMenuFlyOut("training.jsp", "Training", dictionaryD.getWord(I_Dictionary.TRAINING_TYPE), true, dictionaryD.getWord(I_Dictionary.TRAINING_PLAN), "training_plan", "#", ""+approot+"/employee/training/training_plan_list.jsp", userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_TRAINING, AppObjInfo.G2_TRAINING_PLAN, AppObjInfo.OBJ_MENU_TRAINING_PLAN), 
             AppObjInfo.COMMAND_VIEW)));
			 
flyout.addMenuFlyOut("training.jsp", "Training", dictionaryD.getWord(I_Dictionary.TRAINING_TYPE), true,  dictionaryD.getWord(I_Dictionary.TRAINING_ACTUAL), "training_actual", "#", ""+approot+"/employee/training/training_actual_list.jsp", userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_TRAINING, AppObjInfo.G2_TRAINING_ACTUAL, AppObjInfo.OBJ_MENU_TRAINING_ACTUAL), 
             AppObjInfo.COMMAND_VIEW)));
			 
flyout.addMenuFlyOut("training.jsp", "Training", dictionaryD.getWord(I_Dictionary.TRAINING_TYPE), true, dictionaryD.getWord(I_Dictionary.TRAINING_SEARCH), "training_search", "#", ""+approot+"/employee/training/search_training.jsp", userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_TRAINING, AppObjInfo.G2_TRAINING_TRAINING_SEARCH, AppObjInfo.OBJ_MENU_TRAINING_SEARCH), 
             AppObjInfo.COMMAND_VIEW)));
			 
flyout.addMenuFlyOut("training.jsp", "Training", dictionaryD.getWord(I_Dictionary.TRAINING_TYPE), true, dictionaryD.getWord(I_Dictionary.TRAINING_HISTORY), "training_history", "#", ""+approot+"/employee/training/src_training_hist_hr.jsp", userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_TRAINING, AppObjInfo.G2_TRAINING_TRAINING_HISTORY, AppObjInfo.OBJ_MENU_TRAINING_HISTORY), 
             AppObjInfo.COMMAND_VIEW)));

flyout.addMenuFlyOut("training.jsp", dictionaryD.getWord(I_Dictionary.TRAINING), dictionaryD.getWord(I_Dictionary.TRAINING_TYPE), true, "Training Organizer", "training_organizer", "#", ""+approot+"/masterdata/contact/srccontact_list.jsp", userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_TRAINING, AppObjInfo.G2_TRAINING_ORGANIZER , AppObjInfo.OBJ_TRAINING_ORGANIZER_CONTACT ), 
             AppObjInfo.COMMAND_VIEW)));
flyout.addMenuFlyOut("training.jsp", dictionaryD.getWord(I_Dictionary.TRAINING), dictionaryD.getWord(I_Dictionary.TRAINING_TYPE), true, "Organizer Type", "training_organizer", "#", ""+approot+"/masterdata/contact/contact_class.jsp", userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_TRAINING, AppObjInfo.G2_TRAINING_ORGANIZER , AppObjInfo.OBJ_TRAINING_ORGANIZER_CONTACT ), 
             AppObjInfo.COMMAND_VIEW)));
			 
flyout.addMenuFlyOut("reports.jsp", "Reports", dictionaryD.getWord(I_Dictionary.STAFF_CONTROL), true, "Attendance Record", "staff_control", "#", ""+approot+"/employee/presence/att_record_monthly.jsp", userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_REPORTS, AppObjInfo.G2_MENU_STAFF_CONTROL_REPORT, AppObjInfo.OBJ_MENU_ATTENDANCE_RECORD_REPORT), 
             AppObjInfo.COMMAND_VIEW)));
			 
flyout.addMenuFlyOut("reports.jsp", "Reports", dictionaryD.getWord(I_Dictionary.PRESENCE), true, "Daily Report", "Presence", "#", ""+approot+"/report/presence/presence_report_daily.jsp", userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_REPORTS, AppObjInfo.G2_MENU_PRESENCE_REPORT, AppObjInfo.OBJ_MENU_PRESENCE_DAILY_REPORT), 
             AppObjInfo.COMMAND_VIEW)));
			 
flyout.addMenuFlyOut("reports.jsp", "Reports", dictionaryD.getWord(I_Dictionary.PRESENCE), true, "Weekly Report", "Presence", "#", ""+approot+"/report/presence/weekly_presence.jsp", userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_REPORTS, AppObjInfo.G2_MENU_PRESENCE_REPORT, AppObjInfo.OBJ_MENU_PRESENCE_WEEKLY_REPORT), 
             AppObjInfo.COMMAND_VIEW)));
			 
flyout.addMenuFlyOut("reports.jsp", "Reports", dictionaryD.getWord(I_Dictionary.PRESENCE), true, "Monthly Report", "Presence", "#", ""+approot+"/report/presence/monthly_presence.jsp", userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_REPORTS, AppObjInfo.G2_MENU_PRESENCE_REPORT, AppObjInfo.OBJ_MENU_PRESENCE_MONTHLY_REPORT), 
             AppObjInfo.COMMAND_VIEW)));
			 
flyout.addMenuFlyOut("reports.jsp", "Reports", dictionaryD.getWord(I_Dictionary.PRESENCE), true, "Year Report", "Presence", "#", ""+approot+"/report/presence/year_report_presence.jsp", userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_REPORTS, AppObjInfo.G2_MENU_PRESENCE_REPORT, AppObjInfo.OBJ_MENU_YEAR_REPORT), 
             AppObjInfo.COMMAND_VIEW)));
			 
flyout.addMenuFlyOut("reports.jsp", "Reports", dictionaryD.getWord(I_Dictionary.PRESENCE), true, "Attendance Summary", "Presence", "#", ""+approot+"/report/presence/summary_attendance_sheet.jsp", userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_REPORTS, AppObjInfo.G2_MENU_PRESENCE_REPORT, AppObjInfo.OBJ_MENU_ATTENDANCE_SUMMARY), 
             AppObjInfo.COMMAND_VIEW)));

flyout.addMenuFlyOut("reports.jsp", "Reports", dictionaryD.getWord(I_Dictionary.PRESENCE), true, "Summary Hours Monthly", "Presence", "#", ""+approot+"/report/presence/summary_hours_monthly.jsp", userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_REPORTS, AppObjInfo.G2_MENU_PRESENCE_REPORT, AppObjInfo.OBJ_MENU_ATTENDANCE_SUMMARY), 
             AppObjInfo.COMMAND_VIEW)));

flyout.addMenuFlyOut("reports.jsp", "Reports", dictionaryD.getWord(I_Dictionary.PRESENCE), true, "EO/PH Report", "Presence", "#", ""+approot+"/report/presence/eo_ph_report.jsp", userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_REPORTS, AppObjInfo.G2_MENU_PRESENCE_REPORT, AppObjInfo.OBJ_MENU_ATTENDANCE_SUMMARY), 
             AppObjInfo.COMMAND_VIEW)));

flyout.addMenuFlyOut("reports.jsp", "Reports", dictionaryD.getWord(I_Dictionary.PRESENCE), true, "Rekapitulasi Absensi", "Presence", "#", ""+approot+"/report/presence/rekapitulasi_absensi.jsp", userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_REPORTS, AppObjInfo.G2_MENU_PRESENCE_REPORT, AppObjInfo.OBJ_MENU_ATTENDANCE_SUMMARY), 
             AppObjInfo.COMMAND_VIEW)));

flyout.addMenuFlyOut("reports.jsp", "Reports", dictionaryD.getWord(I_Dictionary.PRESENCE), true, "Rekapitulasi SO", "Presence", "#", ""+approot+"/report/presence/RekapitulasiSo.jsp", userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_REPORTS, AppObjInfo.G2_MENU_PRESENCE_REPORT, AppObjInfo.OBJ_MENU_ATTENDANCE_SUMMARY), 
             AppObjInfo.COMMAND_VIEW)));

flyout.addMenuFlyOut("reports.jsp", "Reports", dictionaryD.getWord(I_Dictionary.PRESENCE), true, "Working Hour Summary", "Presence", "#", ""+approot+"/report/presence/working_hour_summary.jsp", userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_REPORTS, AppObjInfo.G2_MENU_PRESENCE_REPORT, AppObjInfo.OBJ_MENU_ATTENDANCE_SUMMARY), 
             AppObjInfo.COMMAND_VIEW)));
			 
flyout.addMenuFlyOut("reports.jsp", "Reports", dictionaryD.getWord(I_Dictionary.LATENESS), true, "Daily Report", "lateness", "#", ""+approot+"/report/lateness/lateness_report.jsp", userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_REPORTS, AppObjInfo.G2_MENU_LATENESS_REPORT, AppObjInfo.OBJ_MENU_LATENESS_DAILY_REPORT), 
             AppObjInfo.COMMAND_VIEW)));
			 
flyout.addMenuFlyOut("reports.jsp", "Reports", dictionaryD.getWord(I_Dictionary.LATENESS), true, "Weekly Report", "lateness", "#", ""+approot+"/report/lateness/lateness_weekly_report.jsp", userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_REPORTS, AppObjInfo.G2_MENU_LATENESS_REPORT, AppObjInfo.OBJ_MENU_LATENESS_WEEKLY_REPORT), 
             AppObjInfo.COMMAND_VIEW)));
			 
flyout.addMenuFlyOut("reports.jsp", "Reports", dictionaryD.getWord(I_Dictionary.LATENESS), true, "Monthly Report", "lateness", "#", ""+approot+"/report/lateness/lateness_monthly_report.jsp", userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_REPORTS, AppObjInfo.G2_MENU_LATENESS_REPORT, AppObjInfo.OBJ_MENU_LATENESS_MONTHLY_REPORT), 
             AppObjInfo.COMMAND_VIEW)));
			 
flyout.addMenuFlyOut("reports.jsp", "Reports", "Split Shift", true, "Daily Report", "split_shift", "#", ""+approot+"/report/splitshift/daily_split.jsp", userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_REPORTS, AppObjInfo.G2_MENU_SPLIT_SHIFT_REPORT, AppObjInfo.OBJ_MENU_SPLIT_SHIFT_DAILY_REPORT), 
             AppObjInfo.COMMAND_VIEW)));
			 
flyout.addMenuFlyOut("reports.jsp", "Reports", "Split Shift", true, "Weekly Report", "split_shift", "#", ""+approot+"/report/splitshift/weekly_split.jsp", userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_REPORTS, AppObjInfo.G2_MENU_SPLIT_SHIFT_REPORT, AppObjInfo.OBJ_MENU_SPLIT_SHIFT_WEEKLY_REPORT), 
             AppObjInfo.COMMAND_VIEW)));
			 
flyout.addMenuFlyOut("reports.jsp", "Reports", "Split Shift", true, "Monthly Report", "split_shift", "#", ""+approot+"/report/splitshift/monthly_split.jsp", userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_REPORTS, AppObjInfo.G2_MENU_SPLIT_SHIFT_REPORT, AppObjInfo.OBJ_MENU_SPLIT_SHIFT_MONTHLY_REPORT), 
             AppObjInfo.COMMAND_VIEW)));
			 
flyout.addMenuFlyOut("reports.jsp", "Reports", dictionaryD.getWord(I_Dictionary.NIGHT_SHIFT), true, "Daily Report", "night_shift", "#", ""+approot+"/report/nightshift/daily_night.jsp",userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_REPORTS, AppObjInfo.G2_MENU_NIGHT_SHIFT_REPORT, AppObjInfo.OBJ_MENU_NIGHT_SHIFT_DAILY_REPORT), 
             AppObjInfo.COMMAND_VIEW)));
			 
flyout.addMenuFlyOut("reports.jsp", "Reports", dictionaryD.getWord(I_Dictionary.NIGHT_SHIFT), true, "Weekly Report", "night_shift", "#", ""+approot+"/report/nightshift/weekly_night.jsp",	userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_REPORTS, AppObjInfo.G2_MENU_NIGHT_SHIFT_REPORT, AppObjInfo.OBJ_MENU_NIGHT_SHIFT_WEEKLY_REPORT), 
             AppObjInfo.COMMAND_VIEW)));
			 
flyout.addMenuFlyOut("reports.jsp", "Reports", dictionaryD.getWord(I_Dictionary.NIGHT_SHIFT), true, "Monthly Report", "night_shift", "#", ""+approot+"/report/nightshift/monthly_night.jsp",	userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_REPORTS, AppObjInfo.G2_MENU_NIGHT_SHIFT_REPORT, AppObjInfo.OBJ_MENU_NIGHT_SHIFT_MONTHLY_REPORT), 
             AppObjInfo.COMMAND_VIEW)));
			 
flyout.addMenuFlyOut("reports.jsp", "Reports", dictionaryD.getWord(I_Dictionary.ABSENTEEISM), true, "Daily Report", "absenteeism", "#", ""+approot+"/report/absenteeism/daily_absence.jsp",	userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_REPORTS, AppObjInfo.G2_MENU_ABSENTEEISM_REPORT, AppObjInfo.OBJ_MENU_ABSENTEEISM_DAILY_REPORT), 
             AppObjInfo.COMMAND_VIEW)));
			 
flyout.addMenuFlyOut("reports.jsp", "Reports", dictionaryD.getWord(I_Dictionary.ABSENTEEISM), true, "Weekly Report", "absenteeism", "#", ""+approot+"/report/absenteeism/weekly_absence.jsp",	userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_REPORTS, AppObjInfo.G2_MENU_ABSENTEEISM_REPORT, AppObjInfo.OBJ_MENU_ABSENTEEISM_WEEKLY_REPORT), 
             AppObjInfo.COMMAND_VIEW)));
			 
flyout.addMenuFlyOut("reports.jsp", "Reports", dictionaryD.getWord(I_Dictionary.ABSENTEEISM), true, "Monthly Report", "absenteeism", "#", ""+approot+"/report/absenteeism/monthly_absence.jsp",	userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_REPORTS, AppObjInfo.G2_MENU_ABSENTEEISM_REPORT, AppObjInfo.OBJ_MENU_ABSENTEEISM_MONTHLY_REPORT), 
             AppObjInfo.COMMAND_VIEW)));
			 
flyout.addMenuFlyOut("reports.jsp", "Reports", dictionaryD.getWord(I_Dictionary.SICKNESS), true, "Daily Report", "sickness", "#", ""+approot+"/report/sickness/daily_sickness.jsp",	userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_REPORTS, AppObjInfo.G2_MENU_SICKNESS_REPORT, AppObjInfo.OBJ_MENU_SICKNESS_DAILY_REPORT), 
             AppObjInfo.COMMAND_VIEW)));
			 
flyout.addMenuFlyOut("reports.jsp", "Reports", dictionaryD.getWord(I_Dictionary.SICKNESS), true, "Weekly Report", "sickness", "#", ""+approot+"/report/sickness/weekly_sickness.jsp",	userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_REPORTS, AppObjInfo.G2_MENU_SICKNESS_REPORT, AppObjInfo.OBJ_MENU_SICKNESS_WEEKLY_REPORT), 
             AppObjInfo.COMMAND_VIEW)));
			 
flyout.addMenuFlyOut("reports.jsp", "Reports", dictionaryD.getWord(I_Dictionary.SICKNESS), true, "Monthly Report", "sickness", "#", ""+approot+"/report/sickness/monthly_sickness.jsp",	userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_REPORTS, AppObjInfo.G2_MENU_SICKNESS_REPORT, AppObjInfo.OBJ_MENU_SICKNESS_MONTHLY_REPORT), 
             AppObjInfo.COMMAND_VIEW)));
			 
flyout.addMenuFlyOut("reports.jsp", "Reports", dictionaryD.getWord(I_Dictionary.SICKNESS), true, "Zero Sickness", "sickness", "#", ""+approot+"/report/sickness/zero_sickness.jsp",	userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_REPORTS, AppObjInfo.G2_MENU_SICKNESS_REPORT, AppObjInfo.OBJ_MENU_SICKNESS_ZERO_REPORT), 
             AppObjInfo.COMMAND_VIEW)));
			 
flyout.addMenuFlyOut("reports.jsp", "Reports", dictionaryD.getWord(I_Dictionary.SPECIAL_DISPENSATION), true, "Daily Report", "special_dispensation", "#", ""+approot+"/report/sickness/zero_sickness.jsp",	userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_REPORTS, AppObjInfo.G2_MENU_SPECIAL_DISPENSATION_REPORT, AppObjInfo.OBJ_MENU_SPECIAL_DISPENSATION_DAILY_REPORT), 
             AppObjInfo.COMMAND_VIEW)));
			 
flyout.addMenuFlyOut("reports.jsp", "Reports", dictionaryD.getWord(I_Dictionary.SPECIAL_DISPENSATION), true, "Weekly Report", "special_dispensation", "#", ""+approot+"/report/specialdisp/weekly_specialdisp.jsp",	userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_REPORTS, AppObjInfo.G2_MENU_SPECIAL_DISPENSATION_REPORT, AppObjInfo.OBJ_MENU_SPECIAL_DISPENSATION_WEEKLY_REPORT), 
             AppObjInfo.COMMAND_VIEW)));
			 
flyout.addMenuFlyOut("reports.jsp", "Reports", dictionaryD.getWord(I_Dictionary.SPECIAL_DISPENSATION), true, "Monthly Report", "special_dispensation", "#", ""+approot+"/report/specialdisp/monthly_specialdisp.jsp",	userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_REPORTS, AppObjInfo.G2_MENU_SPECIAL_DISPENSATION_REPORT, AppObjInfo.OBJ_MENU_SPECIAL_DISPENSATION_MONTHLY_REPORT), 
             AppObjInfo.COMMAND_VIEW)));
			 
flyout.addMenuFlyOut("reports.jsp", "Reports", dictionaryD.getWord(I_Dictionary.LEAVE) + " " +dictionaryD.getWord(I_Dictionary.REPORT), true, "Leave & DP Summary", "leave_report", "#", ""+approot+"/report/leavedp/leave_dp_sum.jsp",	userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_REPORTS, AppObjInfo.G2_MENU_LEAVE_REPORT, AppObjInfo.OBJ_MENU_LEAVE_DP_SUMMARY), 
             AppObjInfo.COMMAND_VIEW)));
			 
flyout.addMenuFlyOut("reports.jsp", "Reports", dictionaryD.getWord(I_Dictionary.LEAVE) + " " +dictionaryD.getWord(I_Dictionary.REPORT), true, "Leave & DP Detail ", "leave_report", "#", ""+approot+"/report/leavedp/leave_dp_detail.jsp",	userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_REPORTS, AppObjInfo.G2_MENU_LEAVE_REPORT, AppObjInfo.OBJ_MENU_LEAVE_DP_DETAIL), 
             AppObjInfo.COMMAND_VIEW)));

flyout.addMenuFlyOut("reports.jsp", "Reports", dictionaryD.getWord(I_Dictionary.LEAVE) + " " +dictionaryD.getWord(I_Dictionary.REPORT), true, "Special Leave Detail ", "leave_report", "#", ""+approot+"/report/leavedp/leave_sl_detail.jsp",	userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_REPORTS, AppObjInfo.G2_MENU_LEAVE_REPORT, AppObjInfo.OBJ_MENU_LEAVE_DP_DETAIL), 
             AppObjInfo.COMMAND_VIEW)));
			 
flyout.addMenuFlyOut("reports.jsp", "Reports", dictionaryD.getWord(I_Dictionary.LEAVE) + " " +dictionaryD.getWord(I_Dictionary.REPORT), true, "Leave & DP Sum.Period", "leave_report", "#", ""+approot+"/report/leavedp/leave_department_by_period.jsp",	userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_REPORTS, AppObjInfo.G2_MENU_LEAVE_REPORT, AppObjInfo.OBJ_MENU_LEAVE_DP_SUMMARY_PERIOD), 
             AppObjInfo.COMMAND_VIEW)));
			 
flyout.addMenuFlyOut("reports.jsp", "Reports", dictionaryD.getWord(I_Dictionary.LEAVE) + " " +dictionaryD.getWord(I_Dictionary.REPORT), true, "Leave & DP Detail Period", "leave_report", "#", ""+approot+"/report/leavedp/leave_dp_detail_period.jsp",	userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_REPORTS, AppObjInfo.G2_MENU_LEAVE_REPORT, AppObjInfo.OBJ_MENU_LEAVE_DP_DETAIL_PERIOD), 
             AppObjInfo.COMMAND_VIEW)));
			 
flyout.addMenuFlyOut("reports.jsp", "Reports", dictionaryD.getWord(I_Dictionary.LEAVE) + " " +dictionaryD.getWord(I_Dictionary.REPORT), true, "Special & Unpaid Period", "leave_report", "#", ""+approot+"/employee/leave/leave_sp_period.jsp",	userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_REPORTS, AppObjInfo.G2_MENU_LEAVE_REPORT, AppObjInfo.OBJ_MENU_SPECIAL_UNPAID_PERIOD), 
             AppObjInfo.COMMAND_VIEW)));
			 
flyout.addMenuFlyOut("reports.jsp", "Reports", dictionaryD.getWord(I_Dictionary.LEAVE) + " " +dictionaryD.getWord(I_Dictionary.REPORT), true, "DP Ekspired", "leave_report", "#", ""+approot+"/report/attendance/dpexp_report.jsp",	userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_REPORTS, AppObjInfo.G2_MENU_LEAVE_REPORT, AppObjInfo.OBJ_MENU_DP_EXPIRED), 
             AppObjInfo.COMMAND_VIEW)));
			 
flyout.addMenuFlyOut("reports.jsp", "Reports", dictionaryD.getWord(I_Dictionary.TRAINEE), true, "Monthly Report", "trainee", "#", ""+approot+"/report/training/monthly_tr_rpt.jsp",	userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_REPORTS, AppObjInfo.G2_MENU_TRAINEE_REPORT, AppObjInfo.OBJ_MENU_TRAINEE_MONTHLY_REPORT), 
             AppObjInfo.COMMAND_VIEW)));
			 
flyout.addMenuFlyOut("reports.jsp", "Reports", dictionaryD.getWord(I_Dictionary.TRAINEE), true, "End Period", "trainee", "#", ""+approot+"/report/training/end_tr_rpt.jsp",	userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_REPORTS, AppObjInfo.G2_MENU_TRAINEE_REPORT, AppObjInfo.OBJ_MENU_TRAINEE_END_PERIOD), 
             AppObjInfo.COMMAND_VIEW)));
			 
flyout.addMenuFlyOut("reports.jsp", "Reports", dictionaryD.getWord(I_Dictionary.EMPLOYEE), true, "List of Employee Category", "employee", "#", ""+approot+"/report/employee/list_employee_category.jsp",	userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_REPORTS, AppObjInfo.G2_MENU_EMPLOYEE_REPORT, AppObjInfo.OBJ_MENU_LIST_EMPLOYEE_CATEGORY_REPORT), 
             AppObjInfo.COMMAND_VIEW)));
			 
flyout.addMenuFlyOut("reports.jsp", "Reports", dictionaryD.getWord(I_Dictionary.EMPLOYEE), true, "List of Employee Resignation", "employee", "#", ""+approot+"/report/employee/list_employee_resignation1.jsp",	userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_REPORTS, AppObjInfo.G2_MENU_EMPLOYEE_REPORT, AppObjInfo.OBJ_MENU_LIST_EMPLOYEE_RESIGNATION_REPORT), 
             AppObjInfo.COMMAND_VIEW)));
			 
flyout.addMenuFlyOut("reports.jsp", "Reports", dictionaryD.getWord(I_Dictionary.EMPLOYEE), true, "List of Employee Education", "employee", "#", ""+approot+"/report/employee/list_employee_education.jsp",	userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_REPORTS, AppObjInfo.G2_MENU_EMPLOYEE_REPORT, AppObjInfo.OBJ_MENU_LIST_EMPLOYEE_EDUCATION_REPORT), 
             AppObjInfo.COMMAND_VIEW)));
			 
flyout.addMenuFlyOut("reports.jsp", "Reports", dictionaryD.getWord(I_Dictionary.EMPLOYEE), true, "List of Emp Category by Name", "employee", "#", ""+approot+"/report/employee/list_employee_by_Name.jsp",	userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_REPORTS, AppObjInfo.G2_MENU_EMPLOYEE_REPORT, AppObjInfo.OBJ_MENU_LIST_EMPLOYEE_CATEGORY_BY_NAME_REPORT), 
             AppObjInfo.COMMAND_VIEW)));
			 
flyout.addMenuFlyOut("reports.jsp", "Reports", dictionaryD.getWord(I_Dictionary.EMPLOYEE), true, "List Number Of Absences", "employee", "#", ""+approot+"/report/employee/list_absence_reason.jsp",	userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_REPORTS, AppObjInfo.G2_MENU_EMPLOYEE_REPORT, AppObjInfo.OBJ_MENU_LIST_NUMBER_ABSENCES_REPORT), 
             AppObjInfo.COMMAND_VIEW)));
			
flyout.addMenuFlyOut("reports.jsp", "Reports", dictionaryD.getWord(I_Dictionary.EMPLOYEE), true, "List Of Employee Race", "employee", "#", ""+approot+"/report/employee/list_employee_race.jsp",	userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_REPORTS, AppObjInfo.G2_MENU_EMPLOYEE_REPORT, AppObjInfo.OBJ_MENU_LIST_EMPLOYEE_RACE_REPORT), 
             AppObjInfo.COMMAND_VIEW)));

flyout.addMenuFlyOut("reports.jsp", "Reports", dictionaryD.getWord(I_Dictionary.EMPLOYEE), true, "List of Employee Mutation", "employee", "#", ""+approot+"/report/employee/list_employee_mutation.jsp",	userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_REPORTS, AppObjInfo.G2_MENU_EMPLOYEE_REPORT, AppObjInfo.OBJ_MENU_LIST_EMPLOYEE_CATEGORY_REPORT), 
             AppObjInfo.COMMAND_VIEW)));
	  
flyout.addMenuFlyOut("reports.jsp", "Reports", dictionaryD.getWord(I_Dictionary.TRAINING) +" "+dictionaryD.getWord(I_Dictionary.REPORT), true, "Monthly Training", "training_report", "#", ""+approot+"/report/training/monthly_train.jsp",	userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_REPORTS, AppObjInfo.G2_MENU_TRAINING_REPORT, AppObjInfo.OBJ_MENU_MONTHLY_REPORT), 
             AppObjInfo.COMMAND_VIEW)));
			  
flyout.addMenuFlyOut("reports.jsp", "Reports", dictionaryD.getWord(I_Dictionary.TRAINING) +" "+dictionaryD.getWord(I_Dictionary.REPORT), true, "Training Profiles", "training_report", "#", ""+approot+"/report/training/src_training_profiles.jsp",	userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_REPORTS, AppObjInfo.G2_MENU_TRAINING_REPORT, AppObjInfo.OBJ_MENU_PROFILES_REPORT), 
             AppObjInfo.COMMAND_VIEW))); 
			  
flyout.addMenuFlyOut("reports.jsp", "Reports", dictionaryD.getWord(I_Dictionary.TRAINING) +" "+dictionaryD.getWord(I_Dictionary.REPORT), true, "Training Target", "training_report", "#", ""+approot+"/report/training/src_training_target.jsp",	userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_REPORTS, AppObjInfo.G2_MENU_TRAINING_REPORT, AppObjInfo.OBJ_MENU_TARGET_REPORT), 
             AppObjInfo.COMMAND_VIEW)));
			  
flyout.addMenuFlyOut("reports.jsp", "Reports", dictionaryD.getWord(I_Dictionary.TRAINING) +" "+dictionaryD.getWord(I_Dictionary.REPORT), true, "Report By Departement", "training_report", "#", ""+approot+"/report/training/src_report_dept.jsp",	userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_REPORTS, AppObjInfo.G2_MENU_TRAINING_REPORT, AppObjInfo.OBJ_MENU_REPORT_BY_DEPARTEMENT), 
             AppObjInfo.COMMAND_VIEW)));
			  
flyout.addMenuFlyOut("reports.jsp", "Reports", dictionaryD.getWord(I_Dictionary.TRAINING) +" "+dictionaryD.getWord(I_Dictionary.REPORT), true, "Report By Employee", "training_report", "#", ""+approot+"/report/training/src_report_emp.jsp",	userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_REPORTS, AppObjInfo.G2_MENU_TRAINING_REPORT, AppObjInfo.OBJ_MENU_REPORT_BY_EMPLOYEE), 
             AppObjInfo.COMMAND_VIEW)));
			 
flyout.addMenuFlyOut("reports.jsp", "Reports", dictionaryD.getWord(I_Dictionary.TRAINING) +" "+dictionaryD.getWord(I_Dictionary.REPORT), true, "Report By Trainer", "training_report", "#", ""+approot+"/report/training/src_report_trainer.jsp",	userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_REPORTS, AppObjInfo.G2_MENU_TRAINING_REPORT, AppObjInfo.OBJ_MENU_REPORT_BY_TRAINNER), 
             AppObjInfo.COMMAND_VIEW))); 
			 
flyout.addMenuFlyOut("reports.jsp", "Reports", dictionaryD.getWord(I_Dictionary.TRAINING) +" "+dictionaryD.getWord(I_Dictionary.REPORT), true, "Report By Course Detail", "training_report", "#", ""+approot+"/report/training/src_training_course.jsp",	userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_REPORTS, AppObjInfo.G2_MENU_TRAINING_REPORT, AppObjInfo.OBJ_MENU_REPORT_BY_COURSE_DETAIL), 
             AppObjInfo.COMMAND_VIEW))); 
			 
flyout.addMenuFlyOut("reports.jsp", "Reports",  dictionaryD.getWord(I_Dictionary.TRAINING) +" "+dictionaryD.getWord(I_Dictionary.REPORT), true, "Report By Course Date", "training_report", "#", ""+approot+"/report/training/src_training_coursedate.jsp",	userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_REPORTS, AppObjInfo.G2_MENU_TRAINING_REPORT, AppObjInfo.OBJ_MENU_REPORT_BY_COURSE_DATE), 
             AppObjInfo.COMMAND_VIEW))); 
			 
flyout.addMenuFlyOut("reports.jsp", "Reports", dictionaryD.getWord(I_Dictionary.PAYROLL), true, "List Of Salary Summary", "payroll", "#", ""+approot+"/report/payroll/gaji_transfer.jsp",	userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_REPORTS, AppObjInfo.G2_MENU_PAYROLL_REPORT, AppObjInfo.OBJ_MENU_LIST_SALARY_SUMMARY_REPORT), 
             AppObjInfo.COMMAND_VIEW)));

flyout.addMenuFlyOut("reports.jsp", "Reports",dictionaryD.getWord(I_Dictionary.PAYROLL), true, "Salary Summary", "payroll", "#", ""+approot+"/report/payroll/src_list_benefit_deduction.jsp",	userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_REPORTS, AppObjInfo.G2_MENU_PAYROLL_REPORT, AppObjInfo.OBJ_MENU_LIST_SALARY_SUMMARY_REPORT), 
             AppObjInfo.COMMAND_VIEW)));
			 
flyout.addMenuFlyOut("reports.jsp", "Reports", dictionaryD.getWord(I_Dictionary.PAYROLL), true, "Report Rekonsiliasi", "payroll", "#", ""+approot+"/report/payroll/src_list_benefit_deduction_department.jsp",	userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_REPORTS, AppObjInfo.G2_MENU_PAYROLL_REPORT, AppObjInfo.OBJ_MENU_LIST_SALARY_SUMMARY_REPORT), 
             AppObjInfo.COMMAND_VIEW)));

flyout.addMenuFlyOut("reports.jsp", "Reports", dictionaryD.getWord(I_Dictionary.PAYROLL), true, "Report ESPT Monthly", "payroll", "#", ""+approot+"/report/payroll/src_espt_month.jsp",	userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_REPORTS, AppObjInfo.G2_MENU_PAYROLL_REPORT, AppObjInfo.OBJ_MENU_LIST_SALARY_SUMMARY_REPORT), 
             AppObjInfo.COMMAND_VIEW)));

flyout.addMenuFlyOut("reports.jsp", "Reports", dictionaryD.getWord(I_Dictionary.PAYROLL), true, "Report for A1 Tax-Form ", "payroll", "#", ""+approot+"/report/payroll/src_espt_A1.jsp",	userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_REPORTS, AppObjInfo.G2_MENU_PAYROLL_REPORT, AppObjInfo.OBJ_MENU_LIST_SALARY_SUMMARY_REPORT), 
             AppObjInfo.COMMAND_VIEW)));


flyout.addMenuFlyOut("reports.jsp", "Reports", dictionaryD.getWord(I_Dictionary.PAYROLL), true, "Report for Annual Tax Different ", "payroll", "#", ""+approot+"/report/payroll/src_annual_tax_different.jsp",	userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_REPORTS, AppObjInfo.G2_MENU_PAYROLL_REPORT, AppObjInfo.OBJ_MENU_LIST_SALARY_SUMMARY_REPORT), 
             AppObjInfo.COMMAND_VIEW)));

flyout.addMenuFlyOut("reports.jsp", "Reports", dictionaryD.getWord(I_Dictionary.PAYROLL), true, "Custom Report", "payroll", "#", ""+approot+"/report/payroll/custom_rpt_main.jsp",	userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_REPORTS, AppObjInfo.G2_MENU_PAYROLL_REPORT, AppObjInfo.OBJ_MENU_LIST_SALARY_SUMMARY_REPORT), 
             AppObjInfo.COMMAND_VIEW)));

flyout.addMenuFlyOut("reports.jsp", "Reports", dictionaryD.getWord(I_Dictionary.PAYROLL), true, "JV Report", "payroll", "#", ""+approot+"/report/payroll/jv_report.jsp",	userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_REPORTS, AppObjInfo.G2_MENU_PAYROLL_REPORT, AppObjInfo.OBJ_MENU_LIST_SALARY_SUMMARY_REPORT), 
             AppObjInfo.COMMAND_VIEW)));

flyout.addMenuFlyOut("reports.jsp", "Reports", dictionaryD.getWord(I_Dictionary.PAYROLL), true, "Salary Movement Report", "payroll", "#", ""+approot+"/report/payroll/salary_movement_report.jsp",	userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_REPORTS, AppObjInfo.G2_MENU_PAYROLL_REPORT, AppObjInfo.OBJ_MENU_LIST_SALARY_SUMMARY_REPORT), 
             AppObjInfo.COMMAND_VIEW)));

flyout.addMenuFlyOut("reports.jsp", "Reports", dictionaryD.getWord(I_Dictionary.PAYROLL), true, "Salary Changes Report", "payroll", "#", ""+approot+"/report/payroll/salary_change_rpt.jsp",	userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_REPORTS, AppObjInfo.G2_MENU_PAYROLL_REPORT, AppObjInfo.OBJ_MENU_LIST_SALARY_SUMMARY_REPORT), 
             AppObjInfo.COMMAND_VIEW)));
			 
flyout.addMenuFlyOut("reports.jsp", "Reports", dictionaryD.getWord(I_Dictionary.PAYROLL), true, "Leave Entitle Report", "payroll", "#", ""+approot+"/report/payroll/leave_entitle_report.jsp",	userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_REPORTS, AppObjInfo.G2_MENU_PAYROLL_REPORT, AppObjInfo.OBJ_MENU_LIST_SALARY_SUMMARY_REPORT), 
             AppObjInfo.COMMAND_VIEW)));


flyout.addMenuFlyOut("reports.jsp", "Reports", "Payroll", true, "Taken Leave Report", "payroll", "#", ""+approot+"/report/payroll/taken_leave_report.jsp",	userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_REPORTS, AppObjInfo.G2_MENU_PAYROLL_REPORT, AppObjInfo.OBJ_MENU_LIST_SALARY_SUMMARY_REPORT), 
             AppObjInfo.COMMAND_VIEW)));

flyout.addMenuFlyOut("reports.jsp", "Reports", "Payroll", true, "Rekapitulasi Night Shift", "payroll", "#", ""+approot+"/report/presence/Rekapitulasi_night_shift.jsp",	userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_REPORTS, AppObjInfo.G2_MENU_PAYROLL_REPORT, AppObjInfo.OBJ_MENU_LIST_SALARY_SUMMARY_REPORT), 
             AppObjInfo.COMMAND_VIEW)));

flyout.addMenuFlyOut("canteen.jsp", "Canteen", "Data", true, "Manual Visitation", "excuse_application", "#", ""+approot+"/harisma-canteen/canteen/srccanteenvisitation.jsp",	userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_CANTEEN, AppObjInfo.G2_MENU_DATA_CANTEEN, AppObjInfo.OBJ_MENU_MANUAL_VISITIATION), 
             AppObjInfo.COMMAND_VIEW)));
			 
flyout.addMenuFlyOut("canteen.jsp", "Canteen", "Data", true, "Canteen Schedule", "excuse_application", "#", ""+approot+"/harisma-canteen/canteen/canteenschedule.jsp",	userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_CANTEEN, AppObjInfo.G2_MENU_DATA_CANTEEN, AppObjInfo.OBJ_MENU_DATA_CANTEEN_SCHEDULE), 
             AppObjInfo.COMMAND_VIEW)));
			 
flyout.addMenuFlyOut("canteen.jsp", "Canteen", "Data", true, "Checklist Group", "excuse_application", "#", ""+approot+"/harisma-canteen/canteen/checklistgroup.jsp",	userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_CANTEEN, AppObjInfo.G2_MENU_CANTEEN, AppObjInfo.OBJ_MENU_CANTEEN_CHECKLIST_GROUP), 
             AppObjInfo.COMMAND_VIEW)));
			 
flyout.addMenuFlyOut("canteen.jsp", "Canteen", "Data", true, "Checklist Item", "excuse_application", "#", ""+approot+"/harisma-canteen/canteen/checklistitem.jsp",	userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_CANTEEN, AppObjInfo.G2_MENU_CANTEEN, AppObjInfo.OBJ_MENU_CANTEEN_CHECKLIST_ITEM), 
             AppObjInfo.COMMAND_VIEW)));
			 
flyout.addMenuFlyOut("canteen.jsp", "Canteen", "Data", true, "Checklist Mark", "excuse_application", "#", ""+approot+"/harisma-canteen/canteen/checklistmark.jsp",	userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_CANTEEN, AppObjInfo.G2_MENU_CANTEEN, AppObjInfo.OBJ_MENU_CANTEEN_CHECKLIST_MARK), 
             AppObjInfo.COMMAND_VIEW)));
			 
flyout.addMenuFlyOut("canteen.jsp", "Canteen", "Data", true, "Menu Item", "excuse_application", "#", ""+approot+"/harisma-canteen/canteen/menuitem.jsp",	userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_CANTEEN, AppObjInfo.G2_MENU_CANTEEN, AppObjInfo.OBJ_MENU_CANTEEN_MENU_ITEM), 
             AppObjInfo.COMMAND_VIEW)));
			 
flyout.addMenuFlyOut("canteen.jsp", "Canteen", "Data", true, "Meal Time", "excuse_application", "#", ""+approot+"/harisma-canteen/canteen/mealtime.jsp",	userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_CANTEEN, AppObjInfo.G2_MENU_CANTEEN, AppObjInfo.OBJ_MENU_CANTEEN_MEAL_TIME), 
             AppObjInfo.COMMAND_VIEW)));
			 
flyout.addMenuFlyOut("canteen.jsp", "Canteen", "Data", true, "Comment Group", "excuse_application", "#", ""+approot+"/harisma-canteen/canteen/cardquestiongroup.jsp",	userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_CANTEEN, AppObjInfo.G2_MENU_CANTEEN, AppObjInfo.OBJ_MENU_CANTEEN_COMMENT_GROUP), 
             AppObjInfo.COMMAND_VIEW)));
			 
flyout.addMenuFlyOut("canteen.jsp", "Canteen", "Data", true, "Comment Question", "excuse_application", "#", ""+approot+"/harisma-canteen/canteen/cardquestion.jsp",	userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_CANTEEN, AppObjInfo.G2_MENU_CANTEEN, AppObjInfo.OBJ_MENU_CANTEEN_COMMENT_QUESTION), 
             AppObjInfo.COMMAND_VIEW)));
			 
flyout.addMenuFlyOut("canteen.jsp", "Canteen", "Data", true, "Daily Report", "excuse_application", "#", ""+approot+"/harisma-canteen/report/canteen/detail_daily_report.jsp",	userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_CANTEEN, AppObjInfo.G2_MENU_CANTEEN_REPORT_DETAIL, AppObjInfo.OBJ_MENU_CANTEEN_DAILY_REPORT), 
             AppObjInfo.COMMAND_VIEW)));
			 
flyout.addMenuFlyOut("canteen.jsp", "Canteen", "Data", true, "Weekly Report", "excuse_application", "#", ""+approot+"/harisma-canteen/report/canteen/detail_weekly_report.jsp",	userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_CANTEEN, AppObjInfo.G2_MENU_CANTEEN_REPORT_DETAIL, AppObjInfo.OBJ_MENU_CANTEEN_WEEKLY_REPORT), 
             AppObjInfo.COMMAND_VIEW)));
			 
flyout.addMenuFlyOut("canteen.jsp", "Canteen", "Data", true, "Monthly Report", "excuse_application", "#", ""+approot+"/harisma-canteen/report/canteen/detail_monthly_report.jsp",	userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_CANTEEN, AppObjInfo.G2_MENU_CANTEEN_REPORT_DETAIL, AppObjInfo.OBJ_MENU_CANTEEN_MONTHLY_REPORT), 
             AppObjInfo.COMMAND_VIEW)));
			 
flyout.addMenuFlyOut("canteen.jsp", "Canteen", "Data", true, "Daily Meal Record", "excuse_application", "#", ""+approot+"/harisma-canteen/report/canteen/summary_daily_report.jsp",	userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_CANTEEN, AppObjInfo.G2_MENU_CANTEEN_REPORT_SUMMARY, AppObjInfo.OBJ_MENU_DAILY_MEAL_RECORD_REPORT), 
             AppObjInfo.COMMAND_VIEW)));
			 
flyout.addMenuFlyOut("canteen.jsp", "Canteen", "Data", true, "Periodic Meal Record", "excuse_application", "#", ""+approot+"/harisma-canteen/report/canteen/summary_periodic_report.jsp",	userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_CANTEEN, AppObjInfo.G2_MENU_CANTEEN_REPORT_SUMMARY, AppObjInfo.OBJ_MENU_PERIODIC_MEAL_RECORD_REPORT), 
             AppObjInfo.COMMAND_VIEW)));
			 
flyout.addMenuFlyOut("canteen.jsp", "Canteen", "Data", true, "Meal Report", "excuse_application", "#", ""+approot+"/harisma-canteen/report/canteen/periodic_meal_report.jsp",	userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_CANTEEN, AppObjInfo.G2_MENU_CANTEEN_REPORT_SUMMARY, AppObjInfo.OBJ_MENU_MEAL_REPORT), 
             AppObjInfo.COMMAND_VIEW)));
			 
flyout.addMenuFlyOut("canteen.jsp", "Canteen", "Data", true, "Meal Report Departement", "excuse_application", "#", ""+approot+"/harisma-canteen/report/canteen/summary_periodic_department.jsp",	userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_CANTEEN, AppObjInfo.G2_MENU_CANTEEN_REPORT_SUMMARY, AppObjInfo.OBJ_MENU_MEAL_REPORT_DEPARTEMENT), 
             AppObjInfo.COMMAND_VIEW)));
			 
flyout.addMenuFlyOut("canteen.jsp", "Canteen", "Data", true, "Monthly Canteen Report", "excuse_application", "#", ""+approot+"/harisma-canteen/report/canteen/monthly_canteen_report.jsp",	userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_CANTEEN, AppObjInfo.G2_MENU_CANTEEN_REPORT_SUMMARY, AppObjInfo.OBJ_MENU_MONTHLY_CANTEEN_REPORT), 
             AppObjInfo.COMMAND_VIEW)));
			 
flyout.addMenuFlyOut("clinic.jsp", "Clinic", "Data", true, "Employee & Family", "employee_family", "#", ""+approot+"/clinic/srcemployeefam.jsp",	userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_CLINIC, AppObjInfo.G2_MENU_EMPLOYEE_AND_FAMILY, AppObjInfo.OBJ_MENU_EMPLOYEE_AND_FAMILY), 
             AppObjInfo.COMMAND_VIEW)));
			 
flyout.addMenuFlyOut("clinic.jsp", "Clinic", "Data", true, "Medical Record", "medical_record", "#", ""+approot+"/clinic/disease/scrmedicalrecord.jsp",	userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_CLINIC, AppObjInfo.G2_MENU_CLINIC_MEDICAL_RECORD, AppObjInfo.OBJ_MENU_CLINIC_MEDICAL_RECORD), 
             AppObjInfo.COMMAND_VIEW)));
			 
flyout.addMenuFlyOut("clinic.jsp", "Clinic", "Data", true, "Employee Visit", "employee_visit", "#", ""+approot+"/clinic/empvisit/srcemployeevisit.jsp",	userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_CLINIC, AppObjInfo.G2_MENU_EMPLOYEE_VISIT, AppObjInfo.OBJ_MENU_EMPLOYEE_VISIT), 
             AppObjInfo.COMMAND_VIEW)));
			 
flyout.addMenuFlyOut("clinic.jsp", "Clinic", "Data", true, "Guest Handling", "guest_handling", "#", ""+approot+"/clinic/guest/srcguesthandling.jsp",	userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_CLINIC, AppObjInfo.G2_MENU_GUEST_HANDLING, AppObjInfo.OBJ_MENU_GUEST_HANDLING), 
             AppObjInfo.COMMAND_VIEW)));
			 
flyout.addMenuFlyOut("clinic.jsp", "Clinic", dictionaryD.getWord(I_Dictionary.MEDICINE), true, "List Of Medicine", "medicine", "#", ""+approot+"/clinic/stock/medicine.jsp",	userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_CLINIC, AppObjInfo.G2_MENU_MEDICINE, AppObjInfo.OBJ_MENU_LIST_OF_MEDICINE), 
             AppObjInfo.COMMAND_VIEW)));
			 
flyout.addMenuFlyOut("clinic.jsp", "Clinic",  dictionaryD.getWord(I_Dictionary.MEDICINE), true, "Medicine Consumption", "medicine", "#", ""+approot+"/clinic/stock/medconsump.jsp",	userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_CLINIC, AppObjInfo.G2_MENU_MEDICINE, AppObjInfo.OBJ_MENU_MEDICINE_CONSUMPTION), 
             AppObjInfo.COMMAND_VIEW)));
			 
flyout.addMenuFlyOut("clinic.jsp", "Clinic", dictionaryD.getWord(I_Dictionary.MEDICINE), true, "Type", "medicine", "#", ""+approot+"/clinic/disease/diseasetype.jsp",	userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_CLINIC, AppObjInfo.G2_MENU_MEDICINE, AppObjInfo.OBJ_MENU_MEDICINE_CONSUMPTION), 
             AppObjInfo.COMMAND_VIEW)));
			 
flyout.addMenuFlyOut("clinic.jsp", "Clinic", dictionaryD.getWord(I_Dictionary.MEDICAL), true, "Medical Level", "medical", "#", ""+approot+"/clinic/medexpense/med_level.jsp",	userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_CLINIC, AppObjInfo.G2_MENU_MEDICAL, AppObjInfo.OBJ_MENU_MEDICAL_LEVEL), 
             AppObjInfo.COMMAND_VIEW)));
			 
flyout.addMenuFlyOut("clinic.jsp", "Clinic",dictionaryD.getWord(I_Dictionary.MEDICAL), true, "Medical Case", "medical", "#", ""+approot+"/clinic/medexpense/med_case.jsp",	userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_CLINIC, AppObjInfo.G2_MENU_MEDICAL, AppObjInfo.OBJ_MENU_MEDICAL_CASE), 
             AppObjInfo.COMMAND_VIEW)));
			 
flyout.addMenuFlyOut("clinic.jsp", "Clinic", dictionaryD.getWord(I_Dictionary.MEDICAL), true, "Medical Budget", "medical", "#", ""+approot+"/clinic/medexpense/med_budget.jsp",	userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_CLINIC, AppObjInfo.G2_MENU_MEDICAL, AppObjInfo.OBJ_MENU_MEDICAL_BUDGET), 
             AppObjInfo.COMMAND_VIEW)));
			 
flyout.addMenuFlyOut("clinic.jsp", "Clinic", dictionaryD.getWord(I_Dictionary.MEDICAL), true, "Group", "medical", "#", ""+approot+"/clinic/medexpense/med_group.jsp",	userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_CLINIC, AppObjInfo.G2_MENU_MEDICAL, AppObjInfo.OBJ_MENU_MEDICAL_GROUP), 
             AppObjInfo.COMMAND_VIEW)));
			 
flyout.addMenuFlyOut("clinic.jsp", "Clinic", dictionaryD.getWord(I_Dictionary.MEDICAL), true, "Type", "medical", "#", ""+approot+"/clinic/medexpense/med_type.jsp",	userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_CLINIC, AppObjInfo.G2_MENU_MEDICAL, AppObjInfo.OBJ_MENU_MEDICAL_TYPE), 
             AppObjInfo.COMMAND_VIEW)));
			 
flyout.addMenuFlyOut("locker.jsp", "Locker", dictionaryD.getWord(I_Dictionary.LOCKER), true, "Locker", "locker", "#", ""+approot+"/locker/srclocker.jsp",	userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_LOCKER, AppObjInfo.G2_MENU_LOCKER, AppObjInfo.OBJ_MENU_LOCKER), 
             AppObjInfo.COMMAND_VIEW)));
			 
flyout.addMenuFlyOut("locker.jsp", "Locker", dictionaryD.getWord(I_Dictionary.LOCKER), true, "Locker Treatment", "locker", "#", ""+approot+"/locker/srclockertreatment.jsp",	userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_LOCKER, AppObjInfo.G2_MENU_LOCKER_TREATMENT, AppObjInfo.OBJ_MENU_LOCKER_TREATMENT), 
             AppObjInfo.COMMAND_VIEW)));
			 
flyout.addMenuFlyOut("master_data.jsp", "Master Data", dictionaryD.getWord(I_Dictionary.COMPANY), true, dictionaryD.getWord(I_Dictionary.COMPANY), "company", "#", ""+approot+"/masterdata/company.jsp",	userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_MASTERDATA, AppObjInfo.G2_MENU_MD_COMPANY, AppObjInfo.OBJ_MENU_COMMPANY), 
             AppObjInfo.COMMAND_VIEW)));
			 
flyout.addMenuFlyOut("master_data.jsp", "Master Data", dictionaryD.getWord(I_Dictionary.COMPANY), true, dictionaryD.getWord(I_Dictionary.DIVISION), "company", "#", ""+approot+"/masterdata/division.jsp",	userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_MASTERDATA, AppObjInfo.G2_MENU_MD_COMPANY, AppObjInfo.OBJ_MENU_DIVISION), 
             AppObjInfo.COMMAND_VIEW)));
			 
flyout.addMenuFlyOut("master_data.jsp", "Master Data", dictionaryD.getWord(I_Dictionary.COMPANY), true, dictionaryD.getWord(I_Dictionary.DEPARTMENT), "company", "#", ""+approot+"/masterdata/department.jsp",	userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_MASTERDATA, AppObjInfo.G2_MENU_MD_COMPANY, AppObjInfo.OBJ_MENU_DEPARTMENT), 
             AppObjInfo.COMMAND_VIEW)));
			 
flyout.addMenuFlyOut("master_data.jsp", "Master Data", dictionaryD.getWord(I_Dictionary.COMPANY), true, dictionaryD.getWord(I_Dictionary.POSITION), "company", "#", ""+approot+"/masterdata/srcposition.jsp",userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_MASTERDATA, AppObjInfo.G2_MENU_MD_COMPANY, AppObjInfo.OBJ_MENU_POSITION), 
             AppObjInfo.COMMAND_VIEW)));
			 
flyout.addMenuFlyOut("master_data.jsp", "Master Data", dictionaryD.getWord(I_Dictionary.COMPANY), true, dictionaryD.getWord("SECTION"), "company", "#", ""+approot+"/masterdata/srcsection.jsp",	userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_MASTERDATA, AppObjInfo.G2_MENU_MD_COMPANY, AppObjInfo.OBJ_MENU_SECTION), 
             AppObjInfo.COMMAND_VIEW)));
			 
flyout.addMenuFlyOut("master_data.jsp", "Master Data", dictionaryD.getWord(I_Dictionary.COMPANY), true, dictionaryD.getWord("PUBLIC_HOLIDAY"), "company", "#", ""+approot+"/masterdata/publicHoliday.jsp",	userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_MASTERDATA, AppObjInfo.G2_MENU_MD_COMPANY, AppObjInfo.OBJ_MENU_PUBLIC_HOLIDAY), 
             AppObjInfo.COMMAND_VIEW)));
			 
flyout.addMenuFlyOut("master_data.jsp", "Master Data", dictionaryD.getWord(I_Dictionary.COMPANY), true, dictionaryD.getWord("LEAVE_TARGET"), "company", "#", ""+approot+"/masterdata/leaveTarget.jsp",	userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_MASTERDATA, AppObjInfo.G2_MENU_MD_COMPANY, AppObjInfo.OBJ_MENU_LEAVE_TARGET), 
             AppObjInfo.COMMAND_VIEW)));
			 
flyout.addMenuFlyOut("master_data.jsp", "Master Data", dictionaryD.getWord(I_Dictionary.EMPLOYEE), true,  dictionaryD.getWord("EDUCATION"), "employee", "#", ""+approot+"/masterdata/education.jsp",	userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_MASTERDATA, AppObjInfo.G2_MENU_MD_EMPLOYEE, AppObjInfo.OBJ_MENU_EMPLOYEE_EDUCATION), 
             AppObjInfo.COMMAND_VIEW)));
			 
flyout.addMenuFlyOut("master_data.jsp", "Master Data", dictionaryD.getWord(I_Dictionary.EMPLOYEE), true, dictionaryD.getWord("FAMILY")+" "+dictionaryD.getWord("RELATIONSHIP"), "employee", "#", ""+approot+"/masterdata/famRelation.jsp",	userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_MASTERDATA, AppObjInfo.G2_MENU_MD_EMPLOYEE, AppObjInfo.OBJ_MENU_EMPLOYEE_FAMILY_RELATION), 
             AppObjInfo.COMMAND_VIEW)));
			 
flyout.addMenuFlyOut("master_data.jsp", "Master Data", dictionaryD.getWord(I_Dictionary.EMPLOYEE), true, dictionaryD.getWord("WARNING"), "employee", "#", ""+approot+"/masterdata/empwarning.jsp",	userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_MASTERDATA, AppObjInfo.G2_MENU_MD_EMPLOYEE, AppObjInfo.OBJ_MENU_EMPLOYEE_WARNING), 
             AppObjInfo.COMMAND_VIEW)));
			 
flyout.addMenuFlyOut("master_data.jsp", "Master Data", dictionaryD.getWord(I_Dictionary.EMPLOYEE), true, dictionaryD.getWord("REPRIMAND"), "employee", "#", ""+approot+"/masterdata/empreprimand.jsp",	userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_MASTERDATA, AppObjInfo.G2_MENU_MD_EMPLOYEE, AppObjInfo.OBJ_MENU_EMPLOYEE_REPRIMAND), 
             AppObjInfo.COMMAND_VIEW)));
			 
flyout.addMenuFlyOut("master_data.jsp", "Master Data", dictionaryD.getWord(I_Dictionary.EMPLOYEE), true, dictionaryD.getWord("LEVEL"), "employee", "#", ""+approot+"/masterdata/level.jsp",	userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_MASTERDATA, AppObjInfo.G2_MENU_MD_EMPLOYEE, AppObjInfo.OBJ_MENU_EMPLOYEE_LEVEL), 
             AppObjInfo.COMMAND_VIEW)));
			 
flyout.addMenuFlyOut("master_data.jsp", "Master Data", dictionaryD.getWord(I_Dictionary.EMPLOYEE), true, dictionaryD.getWord("CATEGORY"), "employee", "#", ""+approot+"/masterdata/empcategory.jsp",	userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_MASTERDATA, AppObjInfo.G2_MENU_MD_EMPLOYEE, AppObjInfo.OBJ_MENU_EMPLOYEE_CATEGORY), 
             AppObjInfo.COMMAND_VIEW)));
			 
flyout.addMenuFlyOut("master_data.jsp", "Master Data", dictionaryD.getWord(I_Dictionary.EMPLOYEE), true, dictionaryD.getWord(I_Dictionary.RELIGION), "employee", "#", ""+approot+"/masterdata/religion.jsp",	userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_MASTERDATA, AppObjInfo.G2_MENU_MD_EMPLOYEE, AppObjInfo.OBJ_MENU_EMPLOYEE_RELIGION), 
             AppObjInfo.COMMAND_VIEW)));
			 
flyout.addMenuFlyOut("master_data.jsp", "Master Data", dictionaryD.getWord(I_Dictionary.EMPLOYEE), true, dictionaryD.getWord("MARITAL"), "employee", "#", ""+approot+"/masterdata/marital.jsp",	userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_MASTERDATA, AppObjInfo.G2_MENU_MD_EMPLOYEE, AppObjInfo.OBJ_MENU_EMPLOYEE_MARITAL), 
             AppObjInfo.COMMAND_VIEW)));
			 
flyout.addMenuFlyOut("master_data.jsp", "Master Data", dictionaryD.getWord(I_Dictionary.EMPLOYEE), true, dictionaryD.getWord("RACE"), "employee", "#", ""+approot+"/masterdata/race.jsp",	userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_MASTERDATA, AppObjInfo.G2_MENU_MD_EMPLOYEE, AppObjInfo.OBJ_MENU_EMPLOYEE_RACE), 
             AppObjInfo.COMMAND_VIEW)));
			 
flyout.addMenuFlyOut("master_data.jsp", "Master Data", dictionaryD.getWord(I_Dictionary.EMPLOYEE), true, dictionaryD.getWord("LANGUANGE"), "employee", "#", ""+approot+"/masterdata/masterlanguage.jsp",	userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_MASTERDATA, AppObjInfo.G2_MENU_MD_EMPLOYEE, AppObjInfo.OBJ_MENU_EMPLOYEE_LANGUAGE), 
             AppObjInfo.COMMAND_VIEW)));
			 
flyout.addMenuFlyOut("master_data.jsp", "Master Data", dictionaryD.getWord(I_Dictionary.EMPLOYEE), true, "Image Assign", "employee", "#", ""+approot+"/masterdata/image_assign.jsp",	userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_MASTERDATA, AppObjInfo.G2_MENU_MD_EMPLOYEE, AppObjInfo.OBJ_MENU_EMPLOYEE_IMAGE_ASSIGN), 
             AppObjInfo.COMMAND_VIEW)));
			 
flyout.addMenuFlyOut("master_data.jsp", "Master Data", dictionaryD.getWord(I_Dictionary.EMPLOYEE), true, "Resigned Reason", "employee", "#", ""+approot+"/masterdata/resignedreason.jsp",	userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_MASTERDATA, AppObjInfo.G2_MENU_MD_EMPLOYEE, AppObjInfo.OBJ_MENU_EMPLOYEE_RESIGNED_REASON), 
             AppObjInfo.COMMAND_VIEW)));
			 
flyout.addMenuFlyOut("master_data.jsp", "Master Data", dictionaryD.getWord(I_Dictionary.EMPLOYEE), true, dictionaryD.getWord("AWARD")+" "+dictionaryD.getWord(I_Dictionary.TYPE) , "employee", "#", ""+approot+"/masterdata/awardtype.jsp",	userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_MASTERDATA, AppObjInfo.G2_MENU_MD_EMPLOYEE, AppObjInfo.OBJ_MENU_EMPLOYEE_AWARD_TYPE), 
             AppObjInfo.COMMAND_VIEW)));
			 
flyout.addMenuFlyOut("master_data.jsp", "Master Data", dictionaryD.getWord(I_Dictionary.EMPLOYEE), true, "Abseance Reason", "employee", "#", ""+approot+"/masterdata/reason.jsp",	userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_MASTERDATA, AppObjInfo.G2_MENU_MD_EMPLOYEE, AppObjInfo.OBJ_MENU_EMPLOYEE_ABSENCE_REASON), 
             AppObjInfo.COMMAND_VIEW)));
			 
flyout.addMenuFlyOut("master_data.jsp", "Master Data", dictionaryD.getWord(I_Dictionary.EMPLOYEE), true,dictionaryD.getWord("CUSTOM_FIELD")+" Master", "employee", "#", ""+approot+"/masterdata/custom_field_master.jsp",	userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_MASTERDATA, AppObjInfo.G2_MENU_MD_EMPLOYEE, AppObjInfo.OBJ_MENU_EMPLOYEE_ABSENCE_REASON), 
             AppObjInfo.COMMAND_VIEW)));

flyout.addMenuFlyOut("master_data.jsp", "Master Data", dictionaryD.getWord(I_Dictionary.EMPLOYEE), true, "Payroll Group", "employee", "#", ""+approot+"/masterdata/PayrollGroup.jsp",	userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_MASTERDATA, AppObjInfo.G2_MENU_MD_EMPLOYEE, AppObjInfo.OBJ_MENU_EMPLOYEE_ABSENCE_REASON), 
             AppObjInfo.COMMAND_VIEW)));
	
flyout.addMenuFlyOut("master_data.jsp", "Master Data", dictionaryD.getWord("SCHEDULE"), true, dictionaryD.getWord("PERIODE"), "Schedule", "#", ""+approot+"/masterdata/period.jsp",	userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_MASTERDATA, AppObjInfo.G2_MENU_MD_SCHEDULE, AppObjInfo.OBJ_MENU_SCHEDULE_PERIOD), 
             AppObjInfo.COMMAND_VIEW)));
			 
flyout.addMenuFlyOut("master_data.jsp", "Master Data", dictionaryD.getWord("SCHEDULE"), true, dictionaryD.getWord("CATEGORY"), "Schedule", "#", ""+approot+"/masterdata/schedulecategory.jsp",	userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_MASTERDATA, AppObjInfo.G2_MENU_MD_SCHEDULE, AppObjInfo.OBJ_MENU_SCHEDULE_CATEGORY), 
             AppObjInfo.COMMAND_VIEW)));
			 
flyout.addMenuFlyOut("master_data.jsp", "Master Data", dictionaryD.getWord("SCHEDULE"), true, dictionaryD.getWord("SYMBOL"), "Schedule", "#", ""+approot+"/masterdata/srcschedulesymbol.jsp",	userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_MASTERDATA, AppObjInfo.G2_MENU_MD_SCHEDULE, AppObjInfo.OBJ_MENU_SCHEDULE_SYMBOL), 
             AppObjInfo.COMMAND_VIEW)));
			 
flyout.addMenuFlyOut("master_data.jsp", "Master Data", dictionaryD.getWord("LOCKER_")+" data", true, dictionaryD.getWord("LOCKER") +" "+dictionaryD.getWord("LOCATION"), "locker_data", "#", ""+approot+"/masterdata/lockerlocation.jsp",	userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_MASTERDATA, AppObjInfo.G2_MENU_MD_LOCKER_DATA, AppObjInfo.OBJ_MENU_LOCKER_DATA_LOCATION), 
             AppObjInfo.COMMAND_VIEW)));
flyout.addMenuFlyOut("master_data.jsp", "Master Data", dictionaryD.getWord("LOCKER_")+" data", true, dictionaryD.getWord("LOCKER") +" "+dictionaryD.getWord("CONDITION"), "locker_data", "#", ""+approot+"/masterdata/lockercondition.jsp",userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_MASTERDATA, AppObjInfo.G2_MENU_MD_LOCKER_DATA, AppObjInfo.OBJ_MENU_LOCKER_DATA_CONDITION), 
             AppObjInfo.COMMAND_VIEW)));
			 
flyout.addMenuFlyOut("master_data.jsp", "Master Data", dictionaryD.getWord("ASSESSMENT"), true, dictionaryD.getWord(I_Dictionary.GROUP) +" "+dictionaryD.getWord("RANK"), "assessment", "#", ""+approot+"/masterdata/grouprankHR.jsp",	userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_MASTERDATA, AppObjInfo.G2_MENU_MD_ASSESSMENT, AppObjInfo.OBJ_MENU_MD_ASSESSMENT_GROUP_RANK), 
             AppObjInfo.COMMAND_VIEW)));
			 
flyout.addMenuFlyOut("master_data.jsp", "Master Data", dictionaryD.getWord("ASSESSMENT"), true, "Evaluation Criteria", "assessment", "#", ""+approot+"/masterdata/evaluation.jsp",	userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_MASTERDATA, AppObjInfo.G2_MENU_MD_ASSESSMENT, AppObjInfo.OBJ_MENU_MD_ASSESSMENT_EVALUATION_CRITERIA), 
             AppObjInfo.COMMAND_VIEW)));
			 
flyout.addMenuFlyOut("master_data.jsp", "Master Data", dictionaryD.getWord("ASSESSMENT"), true, "Form Creator", "assessment", "#", ""+approot+"/masterdata/assessment/assessmentFormMain.jsp",	userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_MASTERDATA, AppObjInfo.G2_MENU_MD_ASSESSMENT, AppObjInfo.OBJ_MENU_MD_ASSESSMENT_FORM_CREATOR), 
             AppObjInfo.COMMAND_VIEW)));
			 
flyout.addMenuFlyOut("master_data.jsp", "Master Data", dictionaryD.getWord("RECRUITMENT"), true, dictionaryD.getWord("GENERAL") +" "+dictionaryD.getWord("QUESTION"), "recruitment", "#", ""+approot+"/employee/recruitment/recrgeneral.jsp",	userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_MASTERDATA, AppObjInfo.G2_MENU_MD_RECRUITMENT, AppObjInfo.OBJ_MENU_GENERAL_QUESTION), 
             AppObjInfo.COMMAND_VIEW)));
			 
flyout.addMenuFlyOut("master_data.jsp", "Master Data", dictionaryD.getWord("RECRUITMENT"), true, "ILNESS "+dictionaryD.getWord(I_Dictionary.TYPE), "recruitment", "#", ""+approot+"/employee/recruitment/recrillness.jsp",	userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_MASTERDATA, AppObjInfo.G2_MENU_MD_RECRUITMENT, AppObjInfo.OBJ_MENU_ILLNESS_TYPE), 
             AppObjInfo.COMMAND_VIEW)));
			 
flyout.addMenuFlyOut("master_data.jsp", "Master Data", dictionaryD.getWord("RECRUITMENT"), true, "Interview Point", "recruitment", "#", ""+approot+"/employee/recruitment/recrinterviewpoint.jsp",	userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_MASTERDATA, AppObjInfo.G2_MENU_MD_RECRUITMENT, AppObjInfo.OBJ_MENU_INTERVIEW_POINTS), 
             AppObjInfo.COMMAND_VIEW)));
			 
flyout.addMenuFlyOut("master_data.jsp", "Master Data", dictionaryD.getWord("RECRUITMENT"), true, "Interviewer", "recruitment", "#", ""+approot+"/employee/recruitment/recrinterviewer.jsp",userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_MASTERDATA, AppObjInfo.G2_MENU_MD_RECRUITMENT, AppObjInfo.OBJ_MENU_INTERVIEWER), 
             AppObjInfo.COMMAND_VIEW)));
			 
flyout.addMenuFlyOut("master_data.jsp", "Master Data", dictionaryD.getWord("RECRUITMENT"), true, "Interview Factor", "recruitment", "#", ""+approot+"/employee/recruitment/recrinterviewer.jsp",userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_MASTERDATA, AppObjInfo.G2_MENU_MD_RECRUITMENT, AppObjInfo.OBJ_MENU_INTERVIEWER), 
             AppObjInfo.COMMAND_VIEW)));
			 
flyout.addMenuFlyOut("master_data.jsp", "Master Data", dictionaryD.getWord("RECRUITMENT"), true, "Orientation Group", "recruitment", "#", ""+approot+"/employee/recruitment/origroup.jsp", userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_MASTERDATA, AppObjInfo.G2_MENU_MD_RECRUITMENT, AppObjInfo.OBJ_MENU_ORIENTATION_GROUP), 
             AppObjInfo.COMMAND_VIEW)));
			 
flyout.addMenuFlyOut("master_data.jsp", "Master Data", dictionaryD.getWord("RECRUITMENT"), true, "Orientation Activity", "recruitment", "#", ""+approot+"/employee/recruitment/oriactivity.jsp", userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_MASTERDATA, AppObjInfo.G2_MENU_MD_RECRUITMENT, AppObjInfo.OBJ_MENU_ORIENTATION_ACTIVITY), 
             AppObjInfo.COMMAND_VIEW)));
			 
flyout.addMenuFlyOut("master_data.jsp", "Master Data", "Geo Area", true, dictionaryD.getWord("COUNTRY"), "geo_area", "#", ""+approot+"/masterdata/negara.jsp", userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_MASTERDATA, AppObjInfo.G2_MENU_MD_GEO_AREA, AppObjInfo.OBJ_MENU_EMP_COUNTRY), 
             AppObjInfo.COMMAND_VIEW)));
			 
flyout.addMenuFlyOut("master_data.jsp", "Master Data", "Geo Area", true, dictionaryD.getWord("PROVINCE"), "geo_area", "#", ""+approot+"/masterdata/provinsi.jsp", userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_MASTERDATA, AppObjInfo.G2_MENU_MD_GEO_AREA, AppObjInfo.OBJ_MENU_EMP_PROVINCE), 
             AppObjInfo.COMMAND_VIEW)));
			 
flyout.addMenuFlyOut("master_data.jsp", "Master Data", "Geo Area", true, dictionaryD.getWord("REGENCY"), "geo_area", "#", ""+approot+"/masterdata/kabupaten.jsp", userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_MASTERDATA, AppObjInfo.G2_MENU_MD_GEO_AREA, AppObjInfo.OBJ_MENU_EMP_REGENCY), 
             AppObjInfo.COMMAND_VIEW)));
			 
flyout.addMenuFlyOut("master_data.jsp", "Master Data", "Geo Area", true, "District", "geo_area", "#", ""+approot+"/masterdata/kecamatan.jsp", userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_MASTERDATA, AppObjInfo.G2_MENU_MD_GEO_AREA, AppObjInfo.OBJ_MENU_EMP_REGENCY), 
             AppObjInfo.COMMAND_VIEW)));
			 
flyout.addMenuFlyOut("master_data.jsp", "Master Data", "Geo Area", true, "Sub District", "geo_area", "#", ""+approot+"/masterdata/district.jsp", userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_MASTERDATA, AppObjInfo.G2_MENU_MD_GEO_AREA, AppObjInfo.OBJ_MENU_EMP_REGENCY), 
             AppObjInfo.COMMAND_VIEW)));

flyout.addMenuFlyOut("master_data.jsp", "Master Data", dictionaryD.getWord("LOCATION")+" Outlet", false, null, "geo_area", ""+approot+"/masterdata/location.jsp", "",userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MASTERDATA, AppObjInfo.G2_MD_LOCATION_OUTLET, AppObjInfo.OBJ_LOCATION_OUTLET), 
             AppObjInfo.COMMAND_VIEW)));


flyout.addMenuFlyOut("master_data.jsp", "Master Data",  dictionaryD.getWord("LEAVE") +" "+ dictionaryD.getWord("CONFIGURATION"), false, null, "master_data", ""+approot+"/masterdata/leave_setting.jsp", "",userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MASTERDATA, AppObjInfo.G2_MD_COMPANY, AppObjInfo.OBJ_LEAVE_CONFIGURATION), 
             AppObjInfo.COMMAND_VIEW)));

flyout.addMenuFlyOut("master_data.jsp", "Master Data", dictionaryD.getWord("GRADE")+" Level", false, null, "master_data", ""+approot+"/masterdata/grade_level.jsp", "",userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MASTERDATA, AppObjInfo.G2_MD_EMPLOYEE, AppObjInfo.OBJ_EMPLOYEE_GRADE_LEVEL), 
             AppObjInfo.COMMAND_VIEW)));
flyout.addMenuFlyOut("master_data.jsp", "Master Data", dictionaryD.getWord("INFORMATION_HRD"), false, null, "master_data", ""+approot+"/masterdata/information_hrd.jsp", "",userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MASTERDATA, AppObjInfo.G2_MD_EMPLOYEE, AppObjInfo.OBJ_EMPLOYEE_INFORMATION_HRD), 
             AppObjInfo.COMMAND_VIEW)));

flyout.addMenuFlyOut("master_data.jsp", "Master Data", "Jenis Stock Opname", false, null, "master_data", ""+approot+"/masterdata/jenisso.jsp", "",userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MASTERDATA, AppObjInfo.G2_MD_LOCATION_OUTLET, AppObjInfo.OBJ_JENIS_SO), 
             AppObjInfo.COMMAND_VIEW)));
flyout.addMenuFlyOut("master_data.jsp", "Master Data", "Periode Stock Opname", false, null, "master_data", ""+approot+"/masterdata/periodstockopname.jsp", "",userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MASTERDATA, AppObjInfo.G2_MD_LOCATION_OUTLET, AppObjInfo.OBJ_PERIODE_SO), 
             AppObjInfo.COMMAND_VIEW)));

flyout.addMenuFlyOut("master_data.jsp", "Master Data", dictionaryD.getWord("REWARD_N_PUNISHMENT"), true, dictionaryD.getWord("CONFIGURATION")+" "+dictionaryD.getWord("REWARD_N_PUNISHMENT"), "master_data", "#", ""+approot+"/configrewardnpunishment/configrewardnpunishment.jsp", userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MASTERDATA, AppObjInfo.G2_MD_EMPLOYEE, AppObjInfo.OBJ_EMPLOYEE_REWARD_PUNISMENT), 
             AppObjInfo.COMMAND_VIEW)));
flyout.addMenuFlyOut("master_data.jsp", "Master Data", dictionaryD.getWord("REWARD_N_PUNISHMENT"), true, "Koefisien "+ dictionaryD.getWord(I_Dictionary.POSITION), "master_data", "#", ""+approot+"/koefisionposition/koefisionposition.jsp", userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MASTERDATA, AppObjInfo.G2_MD_EMPLOYEE, AppObjInfo.OBJ_EMPLOYEE_KOEFISIEN_POSITION), 
             AppObjInfo.COMMAND_VIEW)));

flyout.addMenuFlyOut("master_data.jsp", "Master Data", "Pay Day", false, null, "geo_area", ""+approot+"/masterdata/pay_day.jsp", "",userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MASTERDATA, AppObjInfo.G2_MD_LOCATION_OUTLET, AppObjInfo.OBJ_LOCATION_OUTLET), 
             AppObjInfo.COMMAND_VIEW)));


/*This menu is created by McHen*/
flyout.addMenuFlyOut("master_data.jsp", "Master Data", dictionaryD.getWord("WARNING_AND_REPRIMAND"), true, dictionaryD.getWord("CHAPTER"), "master_data", "#", ""+approot+"/masterdata/warningreprimand_bab.jsp",	userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_MASTERDATA, AppObjInfo.G2_MENU_MD_COMPANY, AppObjInfo.OBJ_MENU_COMMPANY), 
             AppObjInfo.COMMAND_VIEW)));
flyout.addMenuFlyOut("master_data.jsp", "Master Data", dictionaryD.getWord("WARNING_AND_REPRIMAND"), true, dictionaryD.getWord("ARTICLE"), "master_data", "#", ""+approot+"/masterdata/warningreprimand_pasal.jsp",	userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_MASTERDATA, AppObjInfo.G2_MENU_MD_COMPANY, AppObjInfo.OBJ_MENU_COMMPANY), 
             AppObjInfo.COMMAND_VIEW)));
flyout.addMenuFlyOut("master_data.jsp", "Master Data", dictionaryD.getWord("WARNING_AND_REPRIMAND"), true, dictionaryD.getWord("VERSE"), "master_data", "#", ""+approot+"/masterdata/warningreprimand_ayat.jsp",	userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_MASTERDATA, AppObjInfo.G2_MENU_MD_COMPANY, AppObjInfo.OBJ_MENU_COMMPANY), 
             AppObjInfo.COMMAND_VIEW)));
//priska 2015-02-10
flyout.addMenuFlyOut("master_data.jsp", "Master Data", dictionaryD.getWord("COMPANY_DOCUMENT"), true, dictionaryD.getWord("DOCUMENT") +" "+ dictionaryD.getWord(I_Dictionary.TYPE), "company", "#", ""+approot+"/masterdata/doc_type.jsp",	userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_MASTERDATA, AppObjInfo.G2_MENU_MD_COMPANY, AppObjInfo.OBJ_MENU_COMMPANY), 
             AppObjInfo.COMMAND_VIEW)));
		 
flyout.addMenuFlyOut("master_data.jsp", "Master Data", dictionaryD.getWord("COMPANY_DOCUMENT"), true, dictionaryD.getWord("DOCUMENT") +" " + dictionaryD.getWord("EXPENSES"), "company", "#", ""+approot+"/masterdata/doc_expenses.jsp",	userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_MASTERDATA, AppObjInfo.G2_MENU_MD_COMPANY, AppObjInfo.OBJ_MENU_DIVISION), 
             AppObjInfo.COMMAND_VIEW)));
			 
flyout.addMenuFlyOut("master_data.jsp", "Master Data", dictionaryD.getWord("COMPANY_DOCUMENT"), true, dictionaryD.getWord("DOCUMENT") +" MASTER ", "company", "#", ""+approot+"/masterdata/doc_master.jsp",	userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_MASTERDATA, AppObjInfo.G2_MENU_MD_COMPANY, AppObjInfo.OBJ_MENU_DIVISION), 
             AppObjInfo.COMMAND_VIEW)));

flyout.addMenuFlyOut("master_data.jsp", "Master Data", dictionaryD.getWord("COMPANY_DOCUMENT"), true, dictionaryD.getWord(I_Dictionary.EMPLOYEE) +" " + dictionaryD.getWord("DOCUMENT"), "company", "#", ""+approot+"/masterdata/EmpDocument.jsp",	userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_MASTERDATA, AppObjInfo.G2_MENU_MD_COMPANY, AppObjInfo.OBJ_MENU_DIVISION), 
             AppObjInfo.COMMAND_VIEW)));


		 
flyout.addMenuFlyOut("master_data.jsp", "Master Data", dictionaryD.getWord(I_Dictionary.COMPETENCY), true, dictionaryD.getWord(I_Dictionary.COMPETENCY) +" " + dictionaryD.getWord(I_Dictionary.TYPE), "competency_type", "#", ""+approot+"/masterdata/competency_type.jsp",	userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_MASTERDATA, AppObjInfo.G2_MENU_MD_COMPANY, AppObjInfo.OBJ_MENU_DIVISION), 
             AppObjInfo.COMMAND_VIEW)));
			 
flyout.addMenuFlyOut("master_data.jsp", "Master Data", dictionaryD.getWord(I_Dictionary.COMPETENCY), true, dictionaryD.getWord(I_Dictionary.COMPETENCY) +" " + dictionaryD.getWord(I_Dictionary.GROUP), "competency_group", "#", ""+approot+"/masterdata/competency_group.jsp",	userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_MASTERDATA, AppObjInfo.G2_MENU_MD_COMPANY, AppObjInfo.OBJ_MENU_DEPARTMENT), 
             AppObjInfo.COMMAND_VIEW)));

flyout.addMenuFlyOut("master_data.jsp", "Master Data", dictionaryD.getWord(I_Dictionary.COMPETENCY), true, dictionaryD.getWord(I_Dictionary.COMPETENCY) , "competency", "#", ""+approot+"/masterdata/competency.jsp",	userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_MASTERDATA, AppObjInfo.G2_MENU_MD_COMPANY, AppObjInfo.OBJ_MENU_COMMPANY), 
             AppObjInfo.COMMAND_VIEW)));

flyout.addMenuFlyOut("master_data.jsp", "Master Data", dictionaryD.getWord(I_Dictionary.COMPETENCY), true, dictionaryD.getWord(I_Dictionary.COMPETENCY) +" "+dictionaryD.getWord("DETAIL"), "competency_detail", "#", ""+approot+"/masterdata/competency_detail.jsp",	userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_MASTERDATA, AppObjInfo.G2_MENU_MD_COMPANY, AppObjInfo.OBJ_MENU_DEPARTMENT), 
             AppObjInfo.COMMAND_VIEW)));

flyout.addMenuFlyOut("master_data.jsp", "Master Data", dictionaryD.getWord(I_Dictionary.COMPETENCY), true, dictionaryD.getWord(I_Dictionary.COMPETENCY) +" "+dictionaryD.getWord("LEVEL"), "competency_level", "#", ""+approot+"/masterdata/competency_level.jsp",	userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_MASTERDATA, AppObjInfo.G2_MENU_MD_COMPANY, AppObjInfo.OBJ_MENU_DEPARTMENT), 
             AppObjInfo.COMMAND_VIEW)));

//add by priska 2015-02-10
flyout.addMenuFlyOut("master_data.jsp", "Master Data", dictionaryD.getWord(I_Dictionary.KEY)+" "+dictionaryD.getWord(I_Dictionary.PERFORMANCE)+" Indicator ", true, "KPI "+ dictionaryD.getWord(I_Dictionary.TYPE), "Key_Performance_Indicator", "#", ""+approot+"/masterdata/kpi_type.jsp",	userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_MASTERDATA, AppObjInfo.G2_MENU_MD_COMPANY, AppObjInfo.OBJ_MENU_DEPARTMENT), 
             AppObjInfo.COMMAND_VIEW)));
		 
flyout.addMenuFlyOut("master_data.jsp", "Master Data", dictionaryD.getWord(I_Dictionary.KEY)+" "+dictionaryD.getWord(I_Dictionary.PERFORMANCE)+" Indicator ", true, "KPI "+dictionaryD.getWord(I_Dictionary.GROUP), "Key_Performance_Indicator", "#", ""+approot+"/masterdata/kpi_group.jsp",	userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_MASTERDATA, AppObjInfo.G2_MENU_MD_COMPANY, AppObjInfo.OBJ_MENU_DEPARTMENT), 
             AppObjInfo.COMMAND_VIEW)));

flyout.addMenuFlyOut("master_data.jsp", "Master Data", dictionaryD.getWord(I_Dictionary.KEY)+" "+dictionaryD.getWord(I_Dictionary.PERFORMANCE)+" Indicator ", true, dictionaryD.getWord(I_Dictionary.COMPETITOR), "Key_Performance_Indicator", "#", ""+approot+"/masterdata/competitor.jsp",	userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_MASTERDATA, AppObjInfo.G2_MENU_MD_COMPANY, AppObjInfo.OBJ_MENU_DEPARTMENT), 
             AppObjInfo.COMMAND_VIEW)));

flyout.addMenuFlyOut("master_data.jsp", "Master Data", dictionaryD.getWord(I_Dictionary.KEY)+" "+dictionaryD.getWord(I_Dictionary.PERFORMANCE)+" Indicator ", true, "KPI List", "Key_Performance_Indicator", "#", ""+approot+"/masterdata/kpi_list.jsp",	userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_MASTERDATA, AppObjInfo.G2_MENU_MD_COMPANY, AppObjInfo.OBJ_MENU_DEPARTMENT), 
             AppObjInfo.COMMAND_VIEW)));

flyout.addMenuFlyOut("master_data.jsp", "Master Data", dictionaryD.getWord(I_Dictionary.KEY)+" "+dictionaryD.getWord(I_Dictionary.PERFORMANCE)+" Indicator ", true, "KPI Company Target", "Key_Performance_Indicator", "#", ""+approot+"/masterdata/kpi_company_target.jsp",	userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_MASTERDATA, AppObjInfo.G2_MENU_MD_COMPANY, AppObjInfo.OBJ_MENU_DEPARTMENT), 
             AppObjInfo.COMMAND_VIEW)));

flyout.addMenuFlyOut("master_data.jsp", "Master Data", dictionaryD.getWord(I_Dictionary.KEY)+" "+dictionaryD.getWord(I_Dictionary.PERFORMANCE)+" Indicator ", true, "KPI Achievment Target", "Key_Performance_Indicator", "#", ""+approot+"/masterdata/kpi_achievment.jsp",	userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_MASTERDATA, AppObjInfo.G2_MENU_MD_COMPANY, AppObjInfo.OBJ_MENU_DEPARTMENT), 
             AppObjInfo.COMMAND_VIEW)));

//add by priska 20150420
flyout.addMenuFlyOut("master_data.jsp", "Master Data", dictionaryD.getWord(I_Dictionary.EMPLOYEE_DEDUCTION), true, dictionaryD.getWord(I_Dictionary.EMPLOYEE) +" "+dictionaryD.getWord("DEDUCTION"), "Employee Deduction", "#", ""+approot+"/masterdata/arap_creditor.jsp",	userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_MASTERDATA, AppObjInfo.G2_MENU_MD_COMPANY, AppObjInfo.OBJ_MENU_DEPARTMENT), 
             AppObjInfo.COMMAND_VIEW)));
		 
flyout.addMenuFlyOut("master_data.jsp", "Master Data", "Relevant Doc", true,"Relevant Doc", "Emp relevant Doc", "#", ""+approot+"/masterdata/relevant_doc_group.jsp",	userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_MASTERDATA, AppObjInfo.G2_MENU_MD_COMPANY, AppObjInfo.OBJ_MENU_DEPARTMENT), 
             AppObjInfo.COMMAND_VIEW)));


flyout.addMenuFlyOut("system.jsp", "System", "Service", true, "Service Center", "service_center", "#", ""+approot+"/service/service_center.jsp", userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_SYSTEM, AppObjInfo.G2_MENU_SERVICE_CENTER, AppObjInfo.OBJ_MENU_SERVICE_CENTER), 
             AppObjInfo.COMMAND_VIEW))); 
			 
flyout.addMenuFlyOut("system.jsp", "System", "Service", true, "Manual Service", "manual_prosess", "#", ""+approot+"/service/attendance_manual_calculation.jsp", userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_SYSTEM, AppObjInfo.G2_MENU_MANUAL_PROCESS, AppObjInfo.OBJ_MENU_MANUAL_PROCESS), 
             AppObjInfo.COMMAND_VIEW)));
			 
flyout.addMenuFlyOut("system.jsp", "System", "Service", true, "Admin Test Mesin", "service_center", "#", ""+approot+"/system/test_mesin_odbc.jsp", userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_SYSTEM, AppObjInfo.G2_MENU_ADMIN_TEST_MESIN, AppObjInfo.OBJ_MENU_ADMIN_TEST_MESIN), 
             AppObjInfo.COMMAND_VIEW)));
			 
flyout.addMenuFlyOut("system.jsp", "System", "Service", true, "Admin Query Setup", "admin_query_setup", "#", ""+approot+"/system/query_setup.jsp", userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_SYSTEM, AppObjInfo.G2_MENU_ADMIN_QUERY_SETUP, AppObjInfo.OBJ_MENU_ADMIN_QUERY_SETUP), 
             AppObjInfo.COMMAND_VIEW)));


flyout.addMenuFlyOut("system.jsp", "System", "User Management", true, "User List", "user_management", "#", ""+approot+"/admin/userlist.jsp", userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_SYSTEM, AppObjInfo.G2_MENU_USER_MANAGEMENT, AppObjInfo.OBJ_MENU_USER_LIST), 
             AppObjInfo.COMMAND_VIEW)));

flyout.addMenuFlyOut("system.jsp", "System", "User Management", true, "Group Priviledge", "user_management", "#", ""+approot+"/admin/grouplist.jsp", userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_SYSTEM, AppObjInfo.G2_MENU_USER_MANAGEMENT, AppObjInfo.OBJ_MENU_USER_GROUP), 
             AppObjInfo.COMMAND_VIEW)));

flyout.addMenuFlyOut("system.jsp", "System", "User Management", true, "Priviledge", "user_management", "#", ""+approot+"/admin/privilegelist.jsp", userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_SYSTEM, AppObjInfo.G2_MENU_USER_MANAGEMENT, AppObjInfo.OBJ_MENU_USER_PRIVILEGE), 
             AppObjInfo.COMMAND_VIEW)));
flyout.addMenuFlyOut("system.jsp", "System", "User Management", true, "Update Password", "user_management", "#", ""+approot+"/admin/userupdatepasswd.jsp", userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_SYSTEM, AppObjInfo.G2_MENU_USER_MANAGEMENT, AppObjInfo.OBJ_MENU_USER_UPDATE_PASSWORD), 
             AppObjInfo.COMMAND_VIEW)));
			 
flyout.addMenuFlyOut("system.jsp", "System", "User Management", true, "User Compare", "user_management", "#", ""+approot+"/employee/databank/harisma_vs_machine.jsp", userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_SYSTEM, AppObjInfo.G2_MENU_USER_MANAGEMENT, AppObjInfo.OBJ_MENU_USER_COMPARE), 
             AppObjInfo.COMMAND_VIEW)));

		 
flyout.addMenuFlyOut("system.jsp", "System", "System Management", true, "System Properties", "system_management", "#", ""+approot+"/system/sysprop.jsp", userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_SYSTEM, AppObjInfo.G2_MENU_SYSTEM_MANAGEMENT, AppObjInfo.OBJ_MENU_SYSTEM_PROPERTIES), 
             AppObjInfo.COMMAND_VIEW))); 
			 
flyout.addMenuFlyOut("system.jsp", "System", "System Management", true, "Login History", "system_management", "#", ""+approot+"/common/logger/history.jsp", userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_SYSTEM, AppObjInfo.G2_MENU_SYSTEM_MANAGEMENT, AppObjInfo.OBJ_MENU_LOGIN_HISTORY), 
             AppObjInfo.COMMAND_VIEW))); 
			 
flyout.addMenuFlyOut("system.jsp", "System", "System Management", true, "System Log", "system_management", "#", ""+approot+"/system/user_activity_log/user_activity_log.jsp", userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_SYSTEM, AppObjInfo.G2_MENU_SYSTEM_MANAGEMENT, AppObjInfo.OBJ_MENU_SYSTEM_LOG), 
             AppObjInfo.COMMAND_VIEW)));
			 
	
flyout.addMenuFlyOut("system.jsp", "System", dictionaryD.getWord(I_Dictionary.TIME) +" Keeping", true, "Service Manager", "tiime_keeping", "#", ""+approot+"/system/timekeepingpro/svcmgr.jsp", userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_SYSTEM, AppObjInfo.G2_MENU_TIMEKEEPING, AppObjInfo.OBJ_MENU_SERVICE_MANAGER), 
             AppObjInfo.COMMAND_VIEW)));

flyout.addMenuFlyOut("system.jsp", "System", "Service", true, "Service Service Bakup", "Service Service Bakup", "#", ""+approot+"/service/service_bakup_emp_outlet.jsp", userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_SYSTEM, AppObjInfo.G2_TIMEKEEPING, AppObjInfo.OBJ_SERVICE_MANAGER), 
             AppObjInfo.COMMAND_VIEW))); 
		

flyout.addMenuFlyOut("system.jsp", "System", "Service", true, "Service Email Logs", "Service Email Logs", "#", ""+approot+"/service/email_logs.jsp", userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_SYSTEM, AppObjInfo.G2_TIMEKEEPING, AppObjInfo.OBJ_SERVICE_MANAGER), 
             AppObjInfo.COMMAND_VIEW))); 

		
flyout.addMenuFlyOut("payroll_setup.jsp", "Payroll Setup", dictionaryD.getWord(I_Dictionary.PAYROLL_SETUP), false, null, "general", ""+approot+"/payroll/setup/general_list.jsp", "", userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_PAYROLL, AppObjInfo.G2_MENU_PAYROLL_SETUP, AppObjInfo.OBJ_MENU_GENERAL), 
             AppObjInfo.COMMAND_VIEW)));
			 
flyout.addMenuFlyOut("payroll_setup.jsp", "Payroll Setup", dictionaryD.getWord(I_Dictionary.PAYROLL) + " Periode" , false, null, "payroll_periode", ""+approot+"/payroll/setup/period.jsp", "", userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_PAYROLL, AppObjInfo.G2_MENU_PAYROLL_SETUP, AppObjInfo.OBJ_MENU_PAYROLL_PERIOD), 
             AppObjInfo.COMMAND_VIEW)));

flyout.addMenuFlyOut("payroll_setup.jsp", "Payroll Setup", "Bank List", false, null, "bank_list", ""+approot+"/payroll/setup/list-bank.jsp", "", userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_PAYROLL, AppObjInfo.G2_MENU_PAYROLL_SETUP, AppObjInfo.OBJ_MENU_BANK_LIST), 
             AppObjInfo.COMMAND_VIEW)));

flyout.addMenuFlyOut("payroll_setup.jsp", "Payroll Setup", dictionaryD.getWord(I_Dictionary.PAY_SLIP) +" "+ dictionaryD.getWord(I_Dictionary.GROUP), false, null, "pay_slip_group", ""+approot+"/payroll/process/pay_payslip_group.jsp", "", userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_PAYROLL, AppObjInfo.G2_MENU_PAYROLL_SETUP, AppObjInfo.OBJ_MENU_PAY_SLIP_GROUP), 
             AppObjInfo.COMMAND_VIEW)));
// update by Hendra McHen 2015-01-29
flyout.addMenuFlyOut("payroll_setup.jsp", "Payroll Setup", dictionaryD.getWord(I_Dictionary.BENEFIT) +" "+ dictionaryD.getWord(I_Dictionary.CONFIGURATION)  , true, "Benefit Configuration", "benefit_config", "#", ""+approot+"/payroll/setup/benefit_config.jsp", userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_PAYROLL, AppObjInfo.G2_MENU_PAYROLL_SETUP, AppObjInfo.OBJ_MENU_BANK_LIST), 
             AppObjInfo.COMMAND_VIEW)));
flyout.addMenuFlyOut("payroll_setup.jsp", "Payroll Setup", dictionaryD.getWord(I_Dictionary.BENEFIT) +" "+ dictionaryD.getWord(I_Dictionary.CONFIGURATION) , true, "Benefit Input", "benefit_input", "#", ""+approot+"/payroll/setup/benefit_input.jsp", userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_PAYROLL, AppObjInfo.G2_MENU_PAYROLL_SETUP, AppObjInfo.OBJ_MENU_BANK_LIST), 
             AppObjInfo.COMMAND_VIEW)));
flyout.addMenuFlyOut("payroll_setup.jsp", "Payroll Setup", dictionaryD.getWord(I_Dictionary.BENEFIT) +" "+ dictionaryD.getWord(I_Dictionary.CONFIGURATION) , true, "Benefit Employee", "benefit_employee", "#", ""+approot+"/payroll/setup/benefit_employee.jsp", userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_PAYROLL, AppObjInfo.G2_MENU_PAYROLL_SETUP, AppObjInfo.OBJ_MENU_BANK_LIST), 
             AppObjInfo.COMMAND_VIEW)));
flyout.addMenuFlyOut("payroll_setup.jsp", "Payroll Setup", dictionaryD.getWord(I_Dictionary.BENEFIT) +" "+ dictionaryD.getWord(I_Dictionary.CONFIGURATION) , true, "Benefit Input History", "benefit_input_history", "#", ""+approot+"/payroll/setup/benefit_input_history.jsp", userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_PAYROLL, AppObjInfo.G2_MENU_PAYROLL_SETUP, AppObjInfo.OBJ_MENU_BANK_LIST), 
             AppObjInfo.COMMAND_VIEW)));

flyout.addMenuFlyOut("payroll_setup.jsp", "Payroll Setup", dictionaryD.getWord(I_Dictionary.THR_CONFIGURATION), true, dictionaryD.getWord(I_Dictionary.REPORT_SETUP), "thr_report_setup", "#", ""+approot+"/payroll/setup/thr_report_setup.jsp", userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_PAYROLL, AppObjInfo.G2_MENU_PAYROLL_SETUP, AppObjInfo.OBJ_MENU_BANK_LIST), 
             AppObjInfo.COMMAND_VIEW)));
flyout.addMenuFlyOut("payroll_setup.jsp", "Payroll Setup", dictionaryD.getWord(I_Dictionary.THR_CONFIGURATION), true, dictionaryD.getWord(I_Dictionary.CALCULATION_SETUP), "thr_calculation_setup", "#", ""+approot+"/payroll/setup/thr_calculation_setup.jsp", userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_PAYROLL, AppObjInfo.G2_MENU_PAYROLL_SETUP, AppObjInfo.OBJ_MENU_BANK_LIST), 
             AppObjInfo.COMMAND_VIEW)));
flyout.addMenuFlyOut("payroll_setup.jsp", "Payroll Setup", dictionaryD.getWord(I_Dictionary.THR_CONFIGURATION), true, "Generate THR", "generate_thr", "#", ""+approot+"/payroll/setup/generate_thr.jsp", userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_PAYROLL, AppObjInfo.G2_MENU_PAYROLL_SETUP, AppObjInfo.OBJ_MENU_BANK_LIST), 
             AppObjInfo.COMMAND_VIEW)));


flyout.addMenuFlyOut("payroll_setup.jsp", "Payroll Setup", dictionaryD.getWord(I_Dictionary.SALARY) +" "+ dictionaryD.getWord(I_Dictionary.COMPONENT), false, null, "salary_component", ""+approot+"/payroll/setup/salary-comp.jsp", "", userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_PAYROLL, AppObjInfo.G2_MENU_PAYROLL_SETUP, AppObjInfo.OBJ_MENU_SALARY_COMPONENT), 
             AppObjInfo.COMMAND_VIEW)));

flyout.addMenuFlyOut("payroll_setup.jsp", "Payroll Setup", dictionaryD.getWord(I_Dictionary.SALARY) +" "+ dictionaryD.getWord(I_Dictionary.LEVEL), false, null, "salary_level", ""+approot+"/payroll/setup/salary-level.jsp", "", userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_PAYROLL, AppObjInfo.G2_MENU_PAYROLL_SETUP, AppObjInfo.OBJ_MENU_SALARY_LEVEL), 
             AppObjInfo.COMMAND_VIEW))); 

flyout.addMenuFlyOut("payroll_setup.jsp", "Payroll Setup", dictionaryD.getWord(I_Dictionary.EMPLOYEE) +" "+ dictionaryD.getWord(I_Dictionary.SETUP), false, null, "employee_setup", ""+approot+"/payroll/setup/employee-setup.jsp", "", userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_PAYROLL, AppObjInfo.G2_MENU_PAYROLL_SETUP, AppObjInfo.OBJ_MENU_EMPLOYEE_SETUP), 
             AppObjInfo.COMMAND_VIEW)));

flyout.addMenuFlyOut("payroll_setup.jsp", "Payroll Setup", dictionaryD.getWord(I_Dictionary.CURRENCY), false, null, "currency", ""+approot+"/payroll/setup/currency.jsp", "",userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_PAYROLL, AppObjInfo.G2_MENU_PAYROLL_SETUP, AppObjInfo.OBJ_MENU_CURRENCY), 
             AppObjInfo.COMMAND_VIEW)));
			 			 
flyout.addMenuFlyOut("payroll_setup.jsp", "Payroll Setup", dictionaryD.getWord(I_Dictionary.CURRENCY) +" "+ dictionaryD.getWord(I_Dictionary.RATE), false, null, "currency", ""+approot+"/payroll/setup/currency_rate.jsp", "",userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_PAYROLL, AppObjInfo.G2_MENU_PAYROLL_SETUP, AppObjInfo.OBJ_MENU_CURRENCY_RATE), 
             AppObjInfo.COMMAND_VIEW)));

flyout.addMenuFlyOut("payroll_setup.jsp", "Payroll Setup", "Chart of Account", false, null, "chartofaccount", ""+approot+"/masterdata/account_chart.jsp", "",userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_PAYROLL, AppObjInfo.G2_MENU_PAYROLL_SETUP, AppObjInfo.OBJ_MENU_CURRENCY_RATE), 
             AppObjInfo.COMMAND_VIEW)));
			 
flyout.addMenuFlyOut("payroll_setup.jsp", "Payroll Setup", dictionaryD.getWord(I_Dictionary.EMPLOYEE_DEDUCTION), false, null, "master_data", ""+approot+"/payroll/process/config_potongan.jsp", "", userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_PAYROLL, AppObjInfo.G2_MENU_PAYROLL_SETUP, AppObjInfo.OBJ_MENU_GENERAL), 
             AppObjInfo.COMMAND_VIEW)));
/*			 
flyout.addMenuFlyOut("payroll_setup.jsp", "Payroll Setup", dictionaryD.getWord(I_Dictionary.EMPLOYEE_DEDUCTION), true, "List "+dictionaryD.getWord(I_Dictionary.EMPLOYEE_DEDUCTION), "master_data", "#", ""+approot+"/employee/arap/list_employee_deduction.jsp", userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_PAYROLL, AppObjInfo.G2_MENU_PAYROLL_SETUP, AppObjInfo.OBJ_MENU_GENERAL), 
             AppObjInfo.COMMAND_VIEW)));
flyout.addMenuFlyOut("payroll_setup.jsp", "Payroll Setup", dictionaryD.getWord(I_Dictionary.EMPLOYEE_DEDUCTION), true, "Creditor List", "master_data", "#", ""+approot+"/masterdata/arap_creditor.jsp", userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_PAYROLL, AppObjInfo.G2_MENU_PAYROLL_SETUP, AppObjInfo.OBJ_MENU_GENERAL), 
             AppObjInfo.COMMAND_VIEW)));
*/

flyout.addMenuFlyOut("overtime.jsp", "Overtime", "Overtime Form", false, null, "overtime_form", ""+approot+"/payroll/overtimeform/src_overtime.jsp", "",userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_PAYROLL, AppObjInfo.G2_MENU_PAYROLL_OVERTIME, AppObjInfo.OBJ_MENU_PAYROLL_OVERTIME_FORM), 
             AppObjInfo.COMMAND_VIEW))); 
			 	 
flyout.addMenuFlyOut("overtime.jsp", "Overtime", "Overtime Report & Process", false, null, "overtime_report", ""+approot+"/payroll/overtimeform/src_overtime_report.jsp", "",userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_PAYROLL, AppObjInfo.G2_MENU_PAYROLL_OVERTIME, AppObjInfo.OBJ_MENU_PAYROLL_OVERTIME_REPORT_PROCESS), 
             AppObjInfo.COMMAND_VIEW))); 


flyout.addMenuFlyOut("overtime.jsp", "Overtime", "Overtime Index", false, null, "overtime_index", ""+approot+"/payroll/overtime/ov-index.jsp", "",userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_PAYROLL, AppObjInfo.G2_MENU_PAYROLL_OVERTIME, AppObjInfo.OBJ_MENU_PAYROLL_OVERTIME_INDEX), 
             AppObjInfo.COMMAND_VIEW))); 
			 
flyout.addMenuFlyOut("overtime.jsp", "Overtime", "Overtime Summary", false, null, "overtime_summary", ""+approot+"/payroll/overtimeform/src_overtime_summary.jsp", "",userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_PAYROLL, AppObjInfo.G2_MENU_PAYROLL_OVERTIME, AppObjInfo.OBJ_MENU_PAYROLL_OVERTIME_SUMMARY), 
             AppObjInfo.COMMAND_VIEW)));
		
flyout.addMenuFlyOut("payroll_process.jsp", "Payroll Process", dictionaryD.getWord(I_Dictionary.PREPARE) + " "+" Data", false, null, "prepare_data",""+approot+"/payroll/process/pay-pre-data.jsp", "",userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_PAYROLL, AppObjInfo.G2_MENU_PAYROLL_PROCESS, AppObjInfo.OBJ_MENU_PAYROLL_PROCESS_PREPARE_DATA), 
             AppObjInfo.COMMAND_VIEW)));

flyout.addMenuFlyOut("payroll_process.jsp", "Payroll Process", dictionaryD.getWord(I_Dictionary.PAYROLL)+" Input", false, null, "payroll_input","" + approot + "/payroll/process/pay-input.jsp", "",userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_PAYROLL, AppObjInfo.G2_MENU_PAYROLL_PROCESS, AppObjInfo.OBJ_MENU_PAYROLL_PROCESS_INPUT), 
             AppObjInfo.COMMAND_VIEW)));

/*Menu created by Kartika : 20150225 */
flyout.addMenuFlyOut("payroll_process.jsp", "Payroll Process", dictionaryD.getWord(I_Dictionary.EMPLOYEE)+" A/R", true, "AR Entry"          , "payroll_employee_ar","#", ""+approot + "/arap/arap_entry_edit.jsp?get_time_menu=1397466008793&arap_type=0&menu_type=1", userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_PAYROLL, AppObjInfo.G2_MENU_PAYROLL_PROCESS, AppObjInfo.OBJ_MENU_PAYROLL_PROCESS_INPUT), 
             AppObjInfo.COMMAND_VIEW)));
/*End Menu byh Kartika*/

flyout.addMenuFlyOut("payroll_process.jsp", "Payroll Process", dictionaryD.getWord(I_Dictionary.PAYROLL)+" Process", false, null, "payroll_process","" + approot + "/payroll/process/pay-process.jsp", "",userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_PAYROLL, AppObjInfo.G2_MENU_PAYROLL_PROCESS, AppObjInfo.OBJ_MENU_PAYROLL_PROCESS_PROCESS), 
             AppObjInfo.COMMAND_VIEW)));
 

flyout.addMenuFlyOut("payroll_process.jsp", "Payroll Process", dictionaryD.getWord(I_Dictionary.PAY_SLIP)+" Printing", false, null, "payroll_process", "" + approot + "/payroll/process/pay-printing.jsp", "",userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_PAYROLL, AppObjInfo.G2_MENU_PAYROLL_PROCESS, AppObjInfo.OBJ_MENU_PAYROLL_PROCESS_PRINTING), 
             AppObjInfo.COMMAND_VIEW)));

flyout.addMenuFlyOut("payroll.jsp", dictionaryD.getWord(I_Dictionary.PAYROLL), "Simulasi Perubahan Gaji", false, null, "payroll_process", "" + approot + "/payroll/simulation/srcpaysimulation.jsp", "",userSession.checkPrivilege(AppObjInfo.composeCode(
             AppObjInfo.composeObjCode(AppObjInfo.G1_MENU_PAYROLL, AppObjInfo.G2_MENU_PAYROLL_PROCESS, AppObjInfo.OBJ_MENU_PAYROLL_PROCESS_PROCESS), 
             AppObjInfo.COMMAND_VIEW)));

//flyout.MenuFlyout(out, url, isMSIE, approot);
flyout.drawMenuFlayOut(out, url, isMSIEE, approot);;

%>

                
               

