/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.harisma.entity.payroll;

/**
 *
 * @author Gunadi
 */
import com.dimata.qdep.entity.Entity;

public class ThrRptSetup extends Entity {

    private int rptSetupShowIdx = 0;
    private String rptSetupTableGroup = "";
    private String rptSetupTableName = "";
    private String rptSetupFieldName = "";
    private int rptSetupFieldType = 0;
    private String rptSetupFieldHeader = "";
    private int rptSetupDataType = 0;
    private int rptSetupDataGroup = 0;

    public int getRptSetupShowIdx() {
        return rptSetupShowIdx;
    }

    public void setRptSetupShowIdx(int rptSetupShowIdx) {
        this.rptSetupShowIdx = rptSetupShowIdx;
    }

    public String getRptSetupTableGroup() {
        return rptSetupTableGroup;
    }

    public void setRptSetupTableGroup(String rptSetupTableGroup) {
        this.rptSetupTableGroup = rptSetupTableGroup;
    }

    public String getRptSetupTableName() {
        return rptSetupTableName;
    }

    public void setRptSetupTableName(String rptSetupTableName) {
        this.rptSetupTableName = rptSetupTableName;
    }

    public String getRptSetupFieldName() {
        return rptSetupFieldName;
    }

    public void setRptSetupFieldName(String rptSetupFieldName) {
        this.rptSetupFieldName = rptSetupFieldName;
    }

    public int getRptSetupFieldType() {
        return rptSetupFieldType;
    }

    public void setRptSetupFieldType(int rptSetupFieldType) {
        this.rptSetupFieldType = rptSetupFieldType;
    }

    public String getRptSetupFieldHeader() {
        return rptSetupFieldHeader;
    }

    public void setRptSetupFieldHeader(String rptSetupFieldHeader) {
        this.rptSetupFieldHeader = rptSetupFieldHeader;
    }

    /**
     * @return the rptSetupDataType
     */
    public int getRptSetupDataType() {
        return rptSetupDataType;
    }

    /**
     * @param rptSetupDataType the rptSetupDataType to set
     */
    public void setRptSetupDataType(int rptSetupDataType) {
        this.rptSetupDataType = rptSetupDataType;
    }

    /**
     * @return the rptSetupDataGroup
     */
    public int getRptSetupDataGroup() {
        return rptSetupDataGroup;
    }

    /**
     * @param rptSetupDataGroup the rptSetupDataGroup to set
     */
    public void setRptSetupDataGroup(int rptSetupDataGroup) {
        this.rptSetupDataGroup = rptSetupDataGroup;
    }
}