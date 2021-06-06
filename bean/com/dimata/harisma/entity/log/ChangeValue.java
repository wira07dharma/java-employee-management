/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.harisma.entity.log;

import com.dimata.common.entity.contact.ContactList;
import com.dimata.common.entity.contact.PstContactList;
import com.dimata.harisma.entity.employee.Employee;
import com.dimata.harisma.entity.employee.PstCareerPath;
import com.dimata.harisma.entity.employee.PstEmployee;
import com.dimata.harisma.entity.locker.Locker;
import com.dimata.harisma.entity.locker.PstLocker;
import com.dimata.harisma.entity.masterdata.Company;
import com.dimata.harisma.entity.masterdata.Competency;
import com.dimata.harisma.entity.masterdata.Department;
import com.dimata.harisma.entity.masterdata.Division;
import com.dimata.harisma.entity.masterdata.Education;
import com.dimata.harisma.entity.masterdata.EmpCategory;
import com.dimata.harisma.entity.masterdata.EmployeeCompetency;
import com.dimata.harisma.entity.masterdata.GradeLevel;
import com.dimata.harisma.entity.masterdata.Level;
import com.dimata.harisma.entity.masterdata.Marital;
import com.dimata.harisma.entity.masterdata.PayrollGroup;
import com.dimata.harisma.entity.masterdata.Position;
import com.dimata.harisma.entity.masterdata.PstCompany;
import com.dimata.harisma.entity.masterdata.PstCompetency;
import com.dimata.harisma.entity.masterdata.PstDepartment;
import com.dimata.harisma.entity.masterdata.PstDivision;
import com.dimata.harisma.entity.masterdata.PstEducation;
import com.dimata.harisma.entity.masterdata.PstEmpCategory;
import com.dimata.harisma.entity.masterdata.PstEmployeeCompetency;
import com.dimata.harisma.entity.masterdata.PstGradeLevel;
import com.dimata.harisma.entity.masterdata.PstLevel;
import com.dimata.harisma.entity.masterdata.PstMarital;
import com.dimata.harisma.entity.masterdata.PstPayrollGroup;
import com.dimata.harisma.entity.masterdata.PstPosition;
import com.dimata.harisma.entity.masterdata.PstRace;
import com.dimata.harisma.entity.masterdata.PstReligion;
import com.dimata.harisma.entity.masterdata.PstResignedReason;
import com.dimata.harisma.entity.masterdata.PstSection;
import com.dimata.harisma.entity.masterdata.Race;
import com.dimata.harisma.entity.masterdata.Religion;
import com.dimata.harisma.entity.masterdata.ResignedReason;
import com.dimata.harisma.entity.masterdata.Section;
import com.dimata.harisma.entity.masterdata.location.Location;
import com.dimata.harisma.entity.masterdata.location.PstLocation;

/**
 *
 * @author Dimata 007
 */
public class ChangeValue {
    
    public String getStringValue(String field, String value){
        String str = value;
        /* Employee */
        if (field.equals("EMPLOYEE_ID")){
            str = getEmployeeName(Long.valueOf(value));
        }
        /* Education */
        if (field.equals("EDUCATION_ID")){
            str = getEducation(Long.valueOf(value));
        }
        /* ContactList */
        if (field.equals("INSTITUTION_ID")){
            str = getContactList(Long.valueOf(value));
        }
        /* Competency  */
        if (field.equals("COMPETENCY_ID")){
            str = getCompetency(Long.valueOf(value));
        }
        /* Company */
        if (field.equals("COMPANY_ID")){
            str = getCompanyName(Long.valueOf(value));
        }
        /* Division */
        if (field.equals("DIVISION_ID")){
            str = getDivisionName(Long.valueOf(value));
        }
        /* Department */
        if (field.equals("DEPARTMENT_ID")){
            str = getDepartmentName(Long.valueOf(value));
        }
        /* Section */
        if (field.equals("SECTION_ID")){
            str = getSectionName(Long.valueOf(value));
        }
        /* Level */
        if (field.equals("LEVEL_ID")){
            str = getLevelName(Long.valueOf(value));
        }
        /* Emp category */
        if (field.equals("EMP_CATEGORY_ID")){
            str = getEmpCategory(Long.valueOf(value));
        }
        /* Position */
        if (field.equals("POSITION_ID")){
            str = getPositionName(Long.valueOf(value));
        }
        /* Gender */
        if (field.equals("SEX")){
            str = PstEmployee.sexKey[Integer.parseInt(value)];
        }
        /* Religion */
        if (field.equals("RELIGION_ID")){
            str = getReligionName(Long.valueOf(value));
        }
        /* Marital */
        if (field.equals("MARITAL_ID")){
            str = getMaritalName(Long.valueOf(value));
        }
        /* Marital Tax*/
        if (field.equals("TAX_MARITAL_ID")){
            str = getMaritalName(Long.valueOf(value));
        }
        /* Locker */
        if (field.equals("LOCKER_ID")){
            str = getLockerName(Long.valueOf(value));
        }
        /* Resigned */
        if (field.equals("RESIGNED")){
            str = PstEmployee.resignKey[Integer.parseInt(value)];
        }
        /* Resigned Reason */
        if(field.equals("RESIGNED_REASON_ID")){
            str = getResignedReasonName(Long.valueOf(value));
        }
        /* Race */
        if(field.equals("RACE")){
            str = getRaceName(Long.valueOf(value));
        }
        /* Grade Lvl*/
        if(field.equals("GRADE_LEVEL_ID")){
            str = getGradeLevelName(Long.valueOf(value));
        }
        /* Location */
        if(field.equals("LOCATION_ID")){
            str = getLocationName(Long.valueOf(value));
        }
        /* Work Assign Company */
        if(field.equals("WORK_ASSIGN_COMPANY_ID")){
            str = getCompanyName(Long.valueOf(value));
        }
        /* Work Assign Division */
        if(field.equals("WORK_ASSIGN_DIVISION_ID")){
            str = getDivisionName(Long.valueOf(value));
        }
        /* Work Assign Department */
        if(field.equals("WORK_ASSIGN_DEPARTMENT_ID")){
            str = getDepartmentName(Long.valueOf(value));
        }
        /* Work Assign Section */
        if(field.equals("WORK_ASSIGN_SECTION_ID")){
            str = getSectionName(Long.valueOf(value));
        }
        /* Work Assign Position */
        if(field.equals("WORK_ASSIGN_POSITION_ID")){
            str = getPositionName(Long.valueOf(value));
        }
        /* Status Pensiun Program*/
        if (field.equals("STATUS_PENSIUN_PROGRAM")){
            str = PstEmployee.statusPensiunProgramKey[Integer.parseInt(value)];
        }
        /* Presence Check Parameter */
        if (field.equals("PRESENCE_CHECK_PARAMETER")){
            str = PstEmployee.presenceCheckKey[Integer.parseInt(value)];
        }
        /* Dana Pendidikan */
        if (field.equals("DANA_PENDIDIKAN")){
            str = PstEmployee.statusDanaPendidikanKey[Integer.parseInt(value)];
        }
        /* Payroll Group */
        if(field.equals("PAYROLL_GROUP")){
            str = getPayrollGroupName(Long.valueOf(value));
        }
        /* Provider */
        if(field.equals("PROVIDER_ID")){
            str = getContactList(Long.valueOf(value));
        }
        /* Member of Kesehatan */
        if (field.equals("MEMBER_OF_KESEHATAN")){
            str = PstEmployee.memberOfBPJSKesehatanKey[Integer.parseInt(value)];
        }
        /* Member of Ketenagakerjaan */
        if (field.equals("MEMBER_OF_KETENAGAKERJAAN")){
            str = PstEmployee.memberOfBPJSKetenagaKerjaanKey[Integer.parseInt(value)];
        }
        /* history group */
        if (field.equals("HISTORY_GROUP")){
            str = PstCareerPath.historyGroup[Integer.parseInt(value)];
        }
        /* History Type */
        if (field.equals("HISTORY_TYPE")){
            str = PstCareerPath.historyType[Integer.parseInt(value)];
        }
        /* Warning */
        /* Reprimand */
        /* Award */
        /* Relevand Doc */
        /* Experience*/
        return str;
    }
    
    public String getEmployeeName(long oid) {
        String str = "-";
        try {
            Employee emp = PstEmployee.fetchExc(oid);
            str = emp.getFullName();
        } catch (Exception ex) {
            System.out.println("Employee ==> " + ex.toString());
        }
        return str;
    }
    
    public String getEducation(long oid) {
        String str = "-";
        try {
            Education education = PstEducation.fetchExc(oid);
            str = education.getEducation();
        } catch (Exception ex) {
            System.out.println("Education ==> " + ex.toString());
        }
        return str;
    }
    
    public String getContactList(long oid){
        String str = "-";
        try {
            ContactList contactList = PstContactList.fetchExc(oid);
            str = contactList.getCompName();
        } catch (Exception ex) {
            System.out.println("Contact List ==> " + ex.toString());
        }
        return str;
    }
    
    public String getCompetency(long oid){
        String str = "-";
        try {
            Competency competency = PstCompetency.fetchExc(oid);
            str = competency.getCompetencyName();
        } catch (Exception ex) {
            System.out.println("Competency Name ==> " + ex.toString());
        }
        return str;
    }
    
    public String getCompanyName(long companyId) {
        String str = "-";
        try {
            Company company = PstCompany.fetchExc(companyId);
            str = company.getCompany();
        } catch(Exception e){
            str = "-";
            System.out.println("getCompanyName()=>"+e.toString());
        }
        return str;
    }
    
    public String getDivisionName(long divisionId) {
        String str = "-";
        try {
            Division division = PstDivision.fetchExc(divisionId);
            str = division.getDivision();
        } catch(Exception e){
            str = "-";
            System.out.println("getDivisionName()=>"+e.toString());
        }
        return str;
    }
    
    public String getDepartmentName(long departmentId) {
        String str = "-";
        try {
            Department department = PstDepartment.fetchExc(departmentId);
            str = department.getDepartment();
        } catch(Exception e){
            str = "-";
            System.out.println("getDepartment()=>"+e.toString());
        }
        return str;
    }
    
    public String getSectionName(long sectionId) {
        String str = "-";
        try {
            Section section = PstSection.fetchExc(sectionId);
            str = section.getSection();
        } catch(Exception e){
            str = "-";
            System.out.println("getSection()=>"+e.toString());
        }
        return str;
    }
    
    public String getLevelName(long oid) {
        String str = "-";
        try {
            Level level = PstLevel.fetchExc(oid);
            str = level.getLevel();
        } catch(Exception e){
            str = "-";
            System.out.println("getLevel()=>"+e.toString());
        }
        return str;
    }
    
    public String getPositionName(long posId) {
        String position = "-";
        Position pos = new Position();
        try {
            pos = PstPosition.fetchExc(posId);
            position = pos.getPosition();
        } catch (Exception ex) {
            System.out.println("getPositionName ==> " + ex.toString());
        }
        return position;
    }
    
    public String getEmpCategory(long oid) {
        String str = "-";
        try {
            EmpCategory empCat = PstEmpCategory.fetchExc(oid);
            str = empCat.getEmpCategory();
        } catch (Exception ex) {
            System.out.println("getEmpCategory ==> " + ex.toString());
        }
        return str;
    }
    
    public String getReligionName(long oid) {
        String str = "-";
        try {
            Religion religion = PstReligion.fetchExc(oid);
            str = religion.getReligion();
        } catch (Exception ex) {
            System.out.println("getReligionName ==> " + ex.toString());
        }
        return str;
    }
    public String getMaritalName(long oid) {
        String str = "-";
        try {
            Marital marital = PstMarital.fetchExc(oid);
            str = marital.getMaritalStatus() + " - " + marital.getNumOfChildren();
        } catch (Exception ex) {
            System.out.println("getMaritalName ==> " + ex.toString());
        }
        return str;
    }
    public String getLockerName(long oid) {
        String str = "-";
        try {
            Locker locker = PstLocker.fetchExc(oid);
            str = locker.getLockerNumber();
        } catch (Exception ex) {
            System.out.println("getLockerName ==> " + ex.toString());
        }
        return str;
    }
    public String getResignedReasonName(long oid) {
        String str = "-";
        try {
            ResignedReason resignedReason = PstResignedReason.fetchExc(oid);
            str = resignedReason.getResignedReason();
        } catch (Exception ex) {
            System.out.println("getResignedReasonName ==> " + ex.toString());
        }
        return str;
    }
    public String getRaceName(long oid) {
        String str = "-";
        try {
            Race race = PstRace.fetchExc(oid);
            str = race.getRaceName();
        } catch (Exception ex) {
            System.out.println("getRaceName ==> " + ex.toString());
        }
        return str;
    }
    public String getGradeLevelName(long oid) {
        String str = "-";
        try {
            GradeLevel gradeLevel = PstGradeLevel.fetchExc(oid);
            str = gradeLevel.getCodeLevel();
        } catch (Exception ex) {
            System.out.println("getGradeLevelName ==> " + ex.toString());
        }
        return str;
    }
    public String getLocationName(long oid) {
        String str = "-";
        try {
            Location location = PstLocation.fetchExc(oid);
            str = location.getName();
        } catch (Exception ex) {
            System.out.println("getLocationName ==> " + ex.toString());
        }
        return str;
    }
    public String getPayrollGroupName(long oid) {
        String str = "-";
        try {
            PayrollGroup payrollGroup = PstPayrollGroup.fetchExc(oid);
            str = payrollGroup.getPayrollGroupName();
        } catch (Exception ex) {
            System.out.println("getPayrollGroupName ==> " + ex.toString());
        }
        return str;
    }
    
}
