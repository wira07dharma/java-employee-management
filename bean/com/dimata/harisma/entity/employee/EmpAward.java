package com.dimata.harisma.entity.employee;

// import java
import java.util.Date;

// import qdep
import com.dimata.qdep.entity.*;

/**
 *
 * @author bayu
 */
public class EmpAward extends Entity {

    private long employeeId = 0;
    private long deptId = 0;
    private long sectId = 0;
    private Date awardDate = new Date();
    private long awardType = 0;
    private String awardDesc = "";
    private long providerId = 0;
    private String title = "";
    private String awardFrom = "";

    public EmpAward() {
    }

    public long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(long employeeId) {
        this.employeeId = employeeId;
    }

    public long getDepartmentId() {
        return deptId;
    }

    public void setDepartmentId(long deptId) {
        this.deptId = deptId;
    }

    public long getSectionId() {
        return sectId;
    }

    public void setSectionId(long sectId) {
        this.sectId = sectId;
    }

    public Date getAwardDate() {
        return awardDate;
    }

    public void setAwardDate(Date awardDate) {
        this.awardDate = awardDate;
    }

    public long getAwardType() {
        return awardType;
    }

    public void setAwardType(long awardType) {
        this.awardType = awardType;
    }

    public String getAwardDescription() {
        return awardDesc;
    }

    public void setAwardDescription(String awardDesc) {
        this.awardDesc = awardDesc;
    }

    public long getProviderId() {
        return providerId;
    }

    public void setProviderId(long providerId) {
        this.providerId = providerId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAwardFrom() {
        return awardFrom;
    }

    public void setAwardFrom(String awardFrom) {
        this.awardFrom = awardFrom;
    }

}
