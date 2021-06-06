
/* Created on 	:  [date] [time] AM/PM 
 * 
 * @author  	:  [authorName] 
 * @version  	:  [version] 
 */
/*******************************************************************
 * Class Description 	: [project description ... ] 
 * Imput Parameters 	: [input parameter ...] 
 * Output 		: [output ...] 
 *******************************************************************/
package com.dimata.harisma.entity.masterdata;

/* package java */
import java.util.Date;

/* package qdep */
import com.dimata.qdep.entity.*;

public class Position extends Entity {

    private String position = "";
    private String description = "";
    private int positionLevel = 0;
    private int disabledAppUnderSupervisor = 0;
    private int disabledAppDeptScope = 0;
    private int disabedAppDivisionScope = 0;
    private int AllDepartment = 0;
    private int deadlineScheduleBefore = 360;
    private int deadlineScheduleAfter = 360;
    private int deadlineScheduleLeaveBefore = 360;
    private int deadlineScheduleLeaveAfter = 360;
    private int headTitle=0;
    //update by satrya 2012-10-19
    private int positionLevelPayrol=-1;
    //update by satrya 2014-03-06
    private String kodePosition;
    //update by satrya 2014-04-18
    private int flagShowPayInput;
    /* Update by Hendra Putu | 2015-09-09 */
    private int validStatus = 0;
    private Date validStart = null;
    private Date validEnd = null;
    private long levelId = 0;
    private long positionGroupId = 0;
    private int tenagaKerja = 0;
    private int jenisJabatan = 0;

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        if (position == null) {
            position = "";
        }
        this.position = position;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        if (description == null) {
            description = "";
        }
        this.description = description;
    }

    /** Getter for property positionLevel.
     * @return Value of property positionLevel.
     *
     */
    public int getPositionLevel() {
        return this.positionLevel;
    }

    /** Setter for property positionLevel.
     * @param positionLevel New value of property positionLevel.
     *
     */
    public void setPositionLevel(int positionLevel) {
        this.positionLevel = positionLevel;
    }

    public int getDisabledAppUnderSupervisor() {
        return disabledAppUnderSupervisor;
    }

    public void setDisabledAppUnderSupervisor(int disabledAppUnderSupervisor) {
        this.disabledAppUnderSupervisor = disabledAppUnderSupervisor;
    }

    public int getDisabledAppDeptScope() {
        return disabledAppDeptScope;
    }

    public void setDisabledAppDeptScope(int disabledAppDeptScope) {
        this.disabledAppDeptScope = disabledAppDeptScope;
    }

    public int getDisabedAppDivisionScope() {
        return disabedAppDivisionScope;
    }

    public void setDisabedAppDivisionScope(int disabedAppDivisionScope) {
        this.disabedAppDivisionScope = disabedAppDivisionScope;
    }

    public int getAllDepartment() {
        return AllDepartment;
    }

    public void setAllDepartment(int AllDepartment) {
        this.AllDepartment = AllDepartment;
    }

    public int getDeadlineScheduleBefore() {
        return deadlineScheduleBefore;
    }

    public void setDeadlineScheduleBefore(int deadlineScheduleBefore) {
        this.deadlineScheduleBefore = deadlineScheduleBefore;
    }

    public int getDeadlineScheduleAfter() {
        return deadlineScheduleAfter;
    }

    public void setDeadlineScheduleAfter(int deadlineScheduleAfter) {
        this.deadlineScheduleAfter = deadlineScheduleAfter;
    }

    public int getDeadlineScheduleLeaveBefore() {
        return deadlineScheduleLeaveBefore;
    }

    public void setDeadlineScheduleLeaveBefore(int deadlineScheduleLeaveBefore) {
        this.deadlineScheduleLeaveBefore = deadlineScheduleLeaveBefore;
    }

    public int getDeadlineScheduleLeaveAfter() {
        return deadlineScheduleLeaveAfter;
    }

    public void setDeadlineScheduleLeaveAfter(int deadlineScheduleLeaveAfter) {
        this.deadlineScheduleLeaveAfter = deadlineScheduleLeaveAfter;
    }

    /**
     * @return the headTitle
     */
    public int getHeadTitle() {
        return headTitle;
    }

    /**
     * @param headTitle the headTitle to set
     */
    public void setHeadTitle(int headTitle) {
        this.headTitle = headTitle;
    }

    /**
     * @return the positionLevelPayrol
     */
    public int getPositionLevelPayrol() {
        return positionLevelPayrol;
    }

    /**
     * @param positionLevelPayrol the positionLevelPayrol to set
     */
    public void setPositionLevelPayrol(int positionLevelPayrol) {
        this.positionLevelPayrol = positionLevelPayrol;
    }

    /**
     * @return the kodePosition
     */
    public String getKodePosition() {
        if(kodePosition==null){
            return "";
        }
        return kodePosition;
    }

    /**
     * @param kodePosition the kodePosition to set
     */
    public void setKodePosition(String kodePosition) {
        this.kodePosition = kodePosition;
    }

    /**
     * @return the flagShowPayInput
     */
    public int getFlagShowPayInput() {
        return flagShowPayInput;
    }

    /**
     * @param flagShowPayInput the flagShowPayInput to set
     */
    public void setFlagShowPayInput(int flagShowPayInput) {
        this.flagShowPayInput = flagShowPayInput;
    }

    /**
     * @return the validStatus
     */
    public int getValidStatus() {
        return validStatus;
    }

    /**
     * @param validStatus the validStatus to set
     */
    public void setValidStatus(int validStatus) {
        this.validStatus = validStatus;
    }

    /**
     * @return the validStart
     */
    public Date getValidStart() {
        return validStart;
    }

    /**
     * @param validStart the validStart to set
     */
    public void setValidStart(Date validStart) {
        this.validStart = validStart;
    }

    /**
     * @return the validEnd
     */
    public Date getValidEnd() {
        return validEnd;
    }

    /**
     * @param validEnd the validEnd to set
     */
    public void setValidEnd(Date validEnd) {
        this.validEnd = validEnd;
    }

    /**
     * @return the levelId
     */
    public long getLevelId() {
        return levelId;
    }

    /**
     * @param levelId the levelId to set
     */
    public void setLevelId(long levelId) {
        this.levelId = levelId;
    }

    /**
     * @return the positionGroupId
     */
    public long getPositionGroupId() {
        return positionGroupId;
    }

    /**
     * @param positionGroupId the positionGroupId to set
     */
    public void setPositionGroupId(long positionGroupId) {
        this.positionGroupId = positionGroupId;
    }

    /**
     * @return the tenagaKerja
     */
    public int getTenagaKerja() {
        return tenagaKerja;
}

    /**
     * @param tenagaKerja the tenagaKerja to set
     */
    public void setTenagaKerja(int tenagaKerja) {
        this.tenagaKerja = tenagaKerja;
    }

    /**
     * @return the jenisJabatan
     */
    public int getJenisJabatan() {
        return jenisJabatan;
    }

    /**
     * @param jenisJabatan the jenisJabatan to set
     */
    public void setJenisJabatan(int jenisJabatan) {
        this.jenisJabatan = jenisJabatan;
    }
}
