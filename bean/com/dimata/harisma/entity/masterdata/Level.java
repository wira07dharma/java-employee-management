
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

public class Level extends Entity { 

    private long groupRankId ;
	private String level = "";
	private String description = "";
        // added for HR
        private long employeeMedicalId = 0;
        private long familyMedicalId = 0;
        private long gradeLevelId=0;
        private int levelPoint = 0; // added by Hendra McHen 2015-01-08
        private String code = "";
        /* update by Hendra McHen 2015-09-09 */
        private int levelRank = 0;
        
        /**
        * @Author Gunadi Wirawan
        * @Desc field untuk menentukan entitled DP/EO untuk report EO/PH
        * @Request by Borobudur Jakarta
        */
        private int entitleDP = 0;
        private int entitledDPQty = 0;
        private int entitlePH = 0;
        private int entitledPHQty = 0;

    public long getGroupRankId(){
		return groupRankId;
	} 

	public void setGroupRankId(long groupRankId){
    	this.groupRankId = groupRankId;
    }

	public String getLevel(){ 
		return level; 
	} 

	public void setLevel(String level){ 
		if ( level == null ) {
			level = ""; 
		} 
		this.level = level; 
	} 

	public String getDescription(){ 
		return description; 
	} 

	public void setDescription(String description){ 
		if ( description == null ) {
			description = ""; 
		} 
		this.description = description; 
	}

        // added by bayu
        
        public long getEmployeeMedicalId() {
            return employeeMedicalId;
        }

        public void setEmployeeMedicalId(long employeeMedicalId) {
            this.employeeMedicalId = employeeMedicalId;
        }

        public long getFamilyMedicalId() {
            return familyMedicalId;
        }

        public void setFamilyMedicalId(long familyMedicalId) {
            this.familyMedicalId = familyMedicalId;
        }

    /**
     * @return the gradeLevelId
     */
    public long getGradeLevelId() {
        return gradeLevelId;
    }

    /**
     * @param gradeLevelId the gradeLevelId to set
     */
    public void setGradeLevelId(long gradeLevelId) {
        this.gradeLevelId = gradeLevelId;
    }

    /**
     * @return the levelPoint
     */
    public int getLevelPoint() {
        return levelPoint;
    }

    /**
     * @param levelPoint the levelPoint to set
     */
    public void setLevelPoint(int levelPoint) {
        this.levelPoint = levelPoint;
    }

    /**
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * @return the levelRank
     */
    public int getLevelRank() {
        return levelRank;
    }

    /**
     * @param levelRank the levelRank to set
     */
    public void setLevelRank(int levelRank) {
        this.levelRank = levelRank;
    }

    /**
     * @return the entitleDP
     */
    public int getEntitleDP() {
        return entitleDP;
    }

    /**
     * @param entitleDP the entitleDP to set
     */
    public void setEntitleDP(int entitleDP) {
        this.entitleDP = entitleDP;
    }

    /**
     * @return the entitledDPQty
     */
    public int getEntitledDPQty() {
        return entitledDPQty;
    }

    /**
     * @param entitledDPQty the entitledDPQty to set
     */
    public void setEntitledDPQty(int entitledDPQty) {
        this.entitledDPQty = entitledDPQty;
    }

    /**
     * @return the entitlePH
     */
    public int getEntitlePH() {
        return entitlePH;
    }

    /**
     * @param entitlePH the entitlePH to set
     */
    public void setEntitlePH(int entitlePH) {
        this.entitlePH = entitlePH;
    }

    /**
     * @return the entitledPHQty
     */
    public int getEntitledPHQty() {
        return entitledPHQty;
    }

    /**
     * @param entitledPHQty the entitledPHQty to set
     */
    public void setEntitledPHQty(int entitledPHQty) {
        this.entitledPHQty = entitledPHQty;
    }
        
        
}
