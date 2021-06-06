/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.harisma.entity.masterdata;

import com.dimata.qdep.entity.Entity;

/**
 *
 * @author khirayinnura
 */
public class EmpRelevantDocGroup extends Entity {

    private String docGroup = "";
    private String docGroupDesc = "";

    public String getDocGroup() {
        return docGroup;
    }

    public void setDocGroup(String docGroup) {
        this.docGroup = docGroup;
    }

    public String getDocGroupDesc() {
        return docGroupDesc;
    }

    public void setDocGroupDesc(String docGroupDesc) {
        this.docGroupDesc = docGroupDesc;
    }
}
