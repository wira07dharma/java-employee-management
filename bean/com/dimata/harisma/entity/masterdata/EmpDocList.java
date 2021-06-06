/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.harisma.entity.masterdata;

import com.dimata.qdep.entity.Entity;

/**
 *
 * @author GUSWIK
 */
public class EmpDocList extends Entity{
    private long emp_doc_lis_id ; 
     private long emp_doc_id ; 
     private long employee_id ; 
     private String assign_as ;
     private String job_desc ;
     private String object_name ;

    /**
     * @return the emp_doc_lis_id
     */
    public long getEmp_doc_lis_id() {
        return emp_doc_lis_id;
    }

    /**
     * @param emp_doc_lis_id the emp_doc_lis_id to set
     */
    public void setEmp_doc_lis_id(long emp_doc_lis_id) {
        this.emp_doc_lis_id = emp_doc_lis_id;
    }

    /**
     * @return the emp_doc_id
     */
    public long getEmp_doc_id() {
        return emp_doc_id;
    }

    /**
     * @param emp_doc_id the emp_doc_id to set
     */
    public void setEmp_doc_id(long emp_doc_id) {
        this.emp_doc_id = emp_doc_id;
    }

    /**
     * @return the employee_id
     */
    public long getEmployee_id() {
        return employee_id;
    }

    /**
     * @param employee_id the employee_id to set
     */
    public void setEmployee_id(long employee_id) {
        this.employee_id = employee_id;
    }

    /**
     * @return the assign_as
     */
    public String getAssign_as() {
        if (assign_as == null){
            assign_as = "";
        }
        return assign_as;
    }

    /**
     * @param assign_as the assign_as to set
     */
    public void setAssign_as(String assign_as) {
        this.assign_as = assign_as;
    }

    /**
     * @return the job_desc
     */
    public String getJob_desc() {
        if (job_desc == null){
            job_desc="";
        }
        return job_desc;
    }

    /**
     * @param job_desc the job_desc to set
     */
    public void setJob_desc(String job_desc) {
        this.job_desc = job_desc;
    }

    /**
     * @return the object_name
     */
    public String getObject_name() {
        return object_name;
    }

    /**
     * @param object_name the object_name to set
     */
    public void setObject_name(String object_name) {
        this.object_name = object_name;
    }

}
