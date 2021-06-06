/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.harisma.entity.payroll;

import com.dimata.qdep.entity.Entity;
import java.util.Date;

/**
 *
 * @author Gunadi
 */
public class ThrCalculationMain extends Entity {

    private String calculationMainTitle = "";
    private String calculationMainDesc = "";
    private Date calculationMainDateCreate = null;

    public String getCalculationMainTitle() {
        return calculationMainTitle;
    }

    public void setCalculationMainTitle(String calculationMainTitle) {
        this.calculationMainTitle = calculationMainTitle;
    }

    public String getCalculationMainDesc() {
        return calculationMainDesc;
    }

    public void setCalculationMainDesc(String calculationMainDesc) {
        this.calculationMainDesc = calculationMainDesc;
    }

    public Date getCalculationMainDateCreate() {
        return calculationMainDateCreate;
    }

    public void setCalculationMainDateCreate(Date calculationMainDateCreate) {
        this.calculationMainDateCreate = calculationMainDateCreate;
    }
}