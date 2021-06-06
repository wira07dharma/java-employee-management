/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.harisma.entity.masterdata;

/**
 *
 * @author Dimata 007
 */
import com.dimata.qdep.entity.Entity;

public class CustomFieldMaster extends Entity {

    private String fieldName = "";
    private int fieldType = 0;
    private int required = 0;
    private String dataList = "";
    private int inputType = 0;
    private String showField = "";
    private String note = "";

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public int getFieldType() {
        return fieldType;
    }

    public void setFieldType(int fieldType) {
        this.fieldType = fieldType;
    }

    public int getRequired() {
        return required;
    }

    public void setRequired(int required) {
        this.required = required;
    }

    public String getDataList() {
        return dataList;
    }

    public void setDataList(String dataList) {
        this.dataList = dataList;
    }

    public int getInputType() {
        return inputType;
    }

    public void setInputType(int inputType) {
        this.inputType = inputType;
    }

    public String getShowField() {
        return showField;
    }

    public void setShowField(String showField) {
        this.showField = showField;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

}
