
/* Created on 	:  [date] [time] AM/PM 
 * 
 * @author  	: karya
 * @version  	: 01
 */
/**
 * *****************************************************************
 * Class Description : [project description ... ] Imput Parameters : [input
 * parameter ...] Output : [output ...]
 * *****************************************************************
 */
package com.dimata.harisma.entity.employee;

/* package java */
import java.util.Date;

/* package qdep */
import com.dimata.qdep.entity.*;

public class Employee extends Entity {

    private long departmentId;
    private String departmentName;
    private long positionId;
    private String positionName;
    private long sectionId;
    private String sectionName = "";
    private String companyName = "";
    private long companyId;
    private String employeeNum = "";
    private long empCategoryId;
    private String empCategoryName;
    private long levelId;
    private String levelName;
    private String fullName = "";
    private String address = "";
    private long addrCountryId = 0;
    private long addrProvinceId = 0;
    private long addrRegencyId = 0;
    private long addrSubRegencyId = 0;
    private long addrDistrictId = 0;
    private String geoAddress = "";
    private String phone = "";
    private String handphone = "";
    private int postalCode = 0;
    private int sex;
    private String birthPlace = "";
    private Date birthDate;
    private long religionId;
    private String bloodType = "";
    private String astekNum = "";
    private Date astekDate;
    private long maritalId;
    private long lockerId;
    private Date commencingDate;
    private int resigned;
    private Date resignedDate;
    private String barcodeNumber = "";// = "";
    private long resignedReasonId;
    private String resignedDesc = "";
    private double basicSalary;
    private boolean isAssignToAccounting;
    private long divisionId;

    private String divisionName = "";
    //khusus untuk permintaan intimas
    private String empPin = "";
    //edited by yunny
    private String curier = "";
    // penambahan kolom sehubungan dengan payroll
    private String indentCardNr = "";
    private String indentCardIssuedBy = "";
    private Date indentCardValidTo;
    private Date indentCardBirthDate = new Date();

    private String taxRegNr = "";
    private String addressForTax = "";
    private int nationalityType;
    private String emailAddress = "";
    private Date categoryDate;
    private int leaveStatus = 0;
    private long race = 0;
    // Tambahan untuk hard rock artha
    private String addressPermanent = "";
    private long addrPmntCountryId = 0;
    private long addrPmntProvinceId = 0;
    private long addrPmntRegencyId = 0;
    private long addrPmntSubRegencyId = 0;
    private long addrPmntDistrictId = 0;
    private String geoAddressPmnt = "";
    private String phoneEmergency;
    private FamilyMemberList familyMemberList = null;//Gede_15Nov2011{
    private String father = "";
    private String mother = "";
    private String parentsAddress = "";
    private String nameEmg = "";
    private String phoneEmg = "";
    private String addressEmg = "";//}
    //Gede_27Nov2011{
    private long hodEmployeeId = 0;//}    
    private long taxMaritalId = 0; // Kartika; 2012-05-08
//update by satrya 2012-11-14
    private long educationId = 0;
    private long gradeLevelId = 0;

    private int countIdx = 0;
    private long locationId;
    private Date end_contract;
    private long workassigncompanyId;
    private long workassigndivisionId;
    private long workassigndepartmentId;
    private long workassignsectionId;
    private long workassignpositionId;
    private String idcardtype;
    private String npwp;
    private String bpjs_no;
    private Date bpjs_date;
    private String shio;
    private String elemen;
    private String iq;
    private String eq;

    private String noRekening = "";

    // Add property by Hendra McHen 2015-01-09
    private Date probationEndDate;
    private int statusPensiunProgram;
    private Date startDatePensiun;
    private int presenceCheckParameter;
    private String medicalInfo;
    /* Add Field by Hendra McHen | 2015-04-24 */
    private int danaPendidikan = 0;

    private long payrollGroup = 0;
    private long providerID = 0;
    private int memOfBpjsKesahatan = 0;
    private int memOfBpjsKetenagaKerjaan = 0;
    private String noKK = "";
    //added by dewok 20190610
    private String namaPemilikRekening = "";
    private String cabangBank = "";
    private String kodeBank = "";
    private Date tanggalPajakTerdaftar = null;
    private String namaBank = "";
    private String addressIdCard = "";

    public String getAddressPermanent() {
        return addressPermanent;
    }

    public void setAddressPermanent(String addressPermanent) {
        this.addressPermanent = addressPermanent;
    }

    public String getPhoneEmergency() {
        if (phoneEmergency == null) {
            phoneEmergency = "";
        }
        return phoneEmergency;
    }

    public void setPhoneEmergency(String phoneEmergency) {
        this.phoneEmergency = phoneEmergency;
    }

    public long getDivisionId() {
        return divisionId;
    }

    public void setDivisionId(long divisionId) {
        this.divisionId = divisionId;
    }

    public long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(long departmentId) {
        this.departmentId = departmentId;
    }

    public long getPositionId() {
        return positionId;
    }

    public void setPositionId(long positionId) {
        this.positionId = positionId;
    }

    public long getSectionId() {
        return sectionId;
    }

    public void setSectionId(long sectionId) {
        this.sectionId = sectionId;
    }

    public String getEmployeeNum() {
        return employeeNum;
    }

    public void setEmployeeNum(String employeeNum) {
        if (employeeNum == null) {
            employeeNum = "";
        }
        this.employeeNum = employeeNum;
    }

    public long getEmpCategoryId() {
        return empCategoryId;
    }

    public void setEmpCategoryId(long empCategoryId) {
        this.empCategoryId = empCategoryId;
    }

    public long getLevelId() {
        return levelId;
    }

    public void setLevelId(long levelId) {
        this.levelId = levelId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        if (fullName == null) {
            fullName = "";
        }
        this.fullName = fullName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        if (address == null) {
            address = "";
        }
        this.address = address;
    }

    public String getPhone() {
        if (phone == null) {
            phone = "";
        }
        return phone;
    }

    public void setPhone(String phone) {
        if (phone == null) {
            phone = "";
        }
        this.phone = phone;
    }

    public String getHandphone() {
        if (handphone == null) {
            handphone = "";
        }
        return handphone;
    }

    public void setHandphone(String handphone) {
        if (handphone == null) {
            handphone = "";
        }
        this.handphone = handphone;
    }

    public int getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(int postalCode) {
        this.postalCode = postalCode;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getBirthPlace() {
        if (birthPlace == null) {
            birthPlace = "";
        }
        return birthPlace;
    }

    public void setBirthPlace(String birthPlace) {
        if (birthPlace == null) {
            birthPlace = "";
        }
        this.birthPlace = birthPlace;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public long getReligionId() {
        return religionId;
    }

    public void setReligionId(long religionId) {
        this.religionId = religionId;
    }

    public String getBloodType() {
        return bloodType;
    }

    public void setBloodType(String bloodType) {
        if (bloodType == null) {
            bloodType = "";
        }
        this.bloodType = bloodType;
    }

    public String getAstekNum() {
        return astekNum;
    }

    public void setAstekNum(String astekNum) {
        if (astekNum == null) {
            astekNum = "";
        }
        this.astekNum = astekNum;
    }

    public Date getAstekDate() {
        return astekDate;
    }

    public void setAstekDate(Date astekDate) {
        this.astekDate = astekDate;
    }

    public long getMaritalId() {
        return maritalId;
    }

    public void setMaritalId(long maritalId) {
        this.maritalId = maritalId;
    }

    public long getLockerId() {
        return lockerId;
    }

    public void setLockerId(long lockerId) {
        this.lockerId = lockerId;
    }

    public Date getCommencingDate() {
        return commencingDate;
    }

    public void setCommencingDate(Date commencingDate) {
        this.commencingDate = commencingDate;
    }

    public int getResigned() {
        return resigned;
    }

    public void setResigned(int resigned) {
        this.resigned = resigned;
    }

    public Date getResignedDate() {
        return resignedDate;
    }

    public void setResignedDate(Date resignedDate) {
        this.resignedDate = resignedDate;
    }

    public String getBarcodeNumber() {
        return barcodeNumber;
    }

    public void setBarcodeNumber(String barcodeNumber) {
        //if ( barcodeNumber == null ) {
        //	barcodeNumber = ""; 
        //} 
        this.barcodeNumber = barcodeNumber;
    }

    public long getResignedReasonId() {
        return resignedReasonId;
    }

    public void setResignedReasonId(long resignedReasonId) {
        this.resignedReasonId = resignedReasonId;
    }

    public String getResignedDesc() {
        return resignedDesc;
    }

    public void setResignedDesc(String resignedDesc) {
        if (resignedDesc == null) {
            resignedDesc = "";
        }
        this.resignedDesc = resignedDesc;
    }

    public double getBasicSalary() {
        return basicSalary;
    }

    public void setBasicSalary(double basicSalary) {
        this.basicSalary = basicSalary;
    }

    public boolean getIsAssignToAccounting() {
        return isIsAssignToAccounting();
    }

    public void setIsAssignToAccounting(boolean isAssignToAccounting) {
        this.isAssignToAccounting = isAssignToAccounting;
    }

    /**
     * Getter for property empPin.
     *
     * @return Value of property empPin.
     */
    public String getEmpPin() {
        return empPin;
    }

    /**
     * Setter for property empPin.
     *
     * @param empPin New value of property empPin.
     */
    public void setEmpPin(String empPin) {
        this.empPin = empPin;
    }

    public String getCurier() {
        return curier;
    }

    public void setCurier(String curier) {
        if (curier == null) {
            curier = "";
        }
        this.curier = curier;
    }

    /**
     * Getter for property indentCardNr.
     *
     * @return Value of property indentCardNr.
     */
    public String getIndentCardNr() {
        return indentCardNr;
    }

    /**
     * Setter for property indentCardNr.
     *
     * @param indentCardNr New value of property indentCardNr.
     */
    public void setIndentCardNr(String indentCardNr) {
        if (indentCardNr == null) {
            indentCardNr = "";
        }
        this.indentCardNr = indentCardNr;
    }

    /**
     * Getter for property indentCardValidTo.
     *
     * @return Value of property indentCardValidTo.
     */
    public Date getIndentCardValidTo() {
        return indentCardValidTo;
    }

    /**
     * Setter for property indentCardValidTo.
     *
     * @param indentCardValidTo New value of property indentCardValidTo.
     */
    public void setIndentCardValidTo(Date indentCardValidTo) {
        this.indentCardValidTo = indentCardValidTo;
    }

    /**
     * Getter for property taxRegNr.
     *
     * @return Value of property taxRegNr.
     */
    public String getTaxRegNr() {
        return taxRegNr;
    }

    /**
     * Setter for property taxRegNr.
     *
     * @param taxRegNr New value of property taxRegNr.
     */
    public void setTaxRegNr(String taxRegNr) {
        if (taxRegNr == null) {
            taxRegNr = "";
        }
        this.taxRegNr = taxRegNr;
    }

    /**
     * Getter for property addressForTax.
     *
     * @return Value of property addressForTax.
     */
    public String getAddressForTax() {
        return addressForTax;
    }

    /**
     * Setter for property addressForTax.
     *
     * @param addressForTax New value of property addressForTax.
     */
    public void setAddressForTax(String addressForTax) {
        if (addressForTax == null) {
            addressForTax = "";
        }
        this.addressForTax = addressForTax;
    }

    /**
     * Getter for property nationalityType.
     *
     * @return Value of property nationalityType.
     */
    public int getNationalityType() {
        return nationalityType;
    }

    /**
     * Setter for property nationalityType.
     *
     * @param nationalityType New value of property nationalityType.
     */
    public void setNationalityType(int nationalityType) {
        this.nationalityType = nationalityType;
    }

    /**
     * Getter for property emailAddress.
     *
     * @return Value of property emailAddress.
     */
    public String getEmailAddress() {
        return emailAddress;
    }

    /**
     * Setter for property emailAddress.
     *
     * @param emailAddress New value of property emailAddress.
     */
    public void setEmailAddress(String emailAddress) {
        if (emailAddress == null) {
            emailAddress = "";
        }
        this.emailAddress = emailAddress;
    }

    /**
     * Getter for property categoryDate.
     *
     * @return Value of property categoryDate.
     */
    public Date getCategoryDate() {
        return categoryDate;
    }

    /**
     * Setter for property categoryDate.
     *
     * @param categoryDate New value of property categoryDate.
     */
    public void setCategoryDate(Date categoryDate) {
        this.categoryDate = categoryDate;
    }

    /**
     * Getter for property leaveStatus.
     *
     * @return Value of property leaveStatus.
     */
    public int getLeaveStatus() {
        return leaveStatus;
    }

    /**
     * Setter for property leaveStatus.
     *
     * @param leaveStatus New value of property leaveStatus.
     */
    public void setLeaveStatus(int leaveStatus) {
        this.leaveStatus = leaveStatus;
    }

    public long getRace() {
        return race;
    }

    public void setRace(long race) {
        this.race = race;
    }

    public FamilyMemberList getFamilyMemberList() {
        return familyMemberList;
    }

    public void setFamilyMemberList(FamilyMemberList familyMemberList) {
        this.familyMemberList = familyMemberList;
    }

    /**
     * @desc mengecek apakah employee sedang ulang tahun pada tgl tertentu
     * @param employee Employee
     * @param date Date
     * @return boolean
     */
    public boolean isBirthDay(Date date) {
        boolean status = false;

        if (this.getBirthDate() != null) {
            if (this.getBirthDate().getDate() == date.getDate()
                    && this.getBirthDate().getMonth() == date.getMonth()) {
                return true;
            }
        }
        return status;
    }

    /**
     * @return the companyId
     */
    public long getCompanyId() {
        return companyId;
    }

    /**
     * @param companyId the companyId to set
     */
    public void setCompanyId(long companyId) {
        this.companyId = companyId;
    }
//Gede_15Nov2011{

    /**
     * @return the father
     */
    public String getFather() {
        if (father == null) {
            father = "";
        }
        return father;
    }

    /**
     * @param father the father to set
     */
    public void setFather(String father) {
        this.father = father;
    }

    /**
     * @return the mother
     */
    public String getMother() {
        if (mother == null) {
            mother = "";
        }
        return mother;
    }

    /**
     * @param mother the mother to set
     */
    public void setMother(String mother) {
        this.mother = mother;
    }

    /**
     * @return the parentsAddress
     */
    public String getParentsAddress() {
        if (parentsAddress == null) {
            parentsAddress = "";
        }
        return parentsAddress;
    }

    /**
     * @param parentsAddress the parentsAddress to set
     */
    public void setParentsAddress(String parentsAddress) {
        this.parentsAddress = parentsAddress;
    }

    /**
     * @return the nameEmg
     */
    public String getNameEmg() {
        if (nameEmg == null) {
            nameEmg = "";
        }
        return nameEmg;
    }

    /**
     * @param nameEmg the nameEmg to set
     */
    public void setNameEmg(String nameEmg) {
        this.nameEmg = nameEmg;
    }

    /**
     * @return the phoneEmg
     */
    public String getPhoneEmg() {
        if (phoneEmg == null) {
            phoneEmg = "";
        }
        return phoneEmg;
    }

    /**
     * @param phoneEmg the phoneEmg to set
     */
    public void setPhoneEmg(String phoneEmg) {
        this.phoneEmg = phoneEmg;
    }

    /**
     * @return the addressEmg
     */
    public String getAddressEmg() {
        if (addressEmg == null) {
            addressEmg = "";
        }
        return addressEmg;
    }

    /**
     * @param addressEmg the addressEmg to set
     */
    public void setAddressEmg(String addressEmg) {
        this.addressEmg = addressEmg;
    }                //}
    //Gede_27Nov2011{

    /**
     * @return the hodEmployeeId
     */
    public long getHodEmployeeId() {
        return hodEmployeeId;
    }

    /**
     * @param hodEmployeeId the hodEmployeeId to set
     */
    public void setHodEmployeeId(long hodEmployeeId) {
        this.hodEmployeeId = hodEmployeeId;
    }//}

    /**
     * @return the addrCountryId
     */
    public long getAddrCountryId() {
        return addrCountryId;
    }

    /**
     * @param addrCountryId the addrCountryId to set
     */
    public void setAddrCountryId(long addrCountryId) {
        this.addrCountryId = addrCountryId;
    }

    /**
     * @return the addrProvinceId
     */
    public long getAddrProvinceId() {
        return addrProvinceId;
    }

    /**
     * @param addrProvinceId the addrProvinceId to set
     */
    public void setAddrProvinceId(long addrProvinceId) {
        this.addrProvinceId = addrProvinceId;
    }

    /**
     * @return the addrRegencyId
     */
    public long getAddrRegencyId() {
        return addrRegencyId;
    }

    /**
     * @param addrRegencyId the addrRegencyId to set
     */
    public void setAddrRegencyId(long addrRegencyId) {
        this.addrRegencyId = addrRegencyId;
    }

    /**
     * @return the addrSubRegencyId
     */
    public long getAddrSubRegencyId() {
        return addrSubRegencyId;
    }

    /**
     * @param addrSubRegencyId the addrSubRegencyId to set
     */
    public void setAddrSubRegencyId(long addrSubRegencyId) {
        this.addrSubRegencyId = addrSubRegencyId;
    }

    /**
     * @return the isAssignToAccounting
     */
    public boolean isIsAssignToAccounting() {
        return isAssignToAccounting;
    }

    /**
     * @return the addrPmntCountryId
     */
    public long getAddrPmntCountryId() {
        return addrPmntCountryId;
    }

    /**
     * @param addrPmntCountryId the addrPmntCountryId to set
     */
    public void setAddrPmntCountryId(long addrPmntCountryId) {
        this.addrPmntCountryId = addrPmntCountryId;
    }

    /**
     * @return the addrPmntProvinceId
     */
    public long getAddrPmntProvinceId() {
        return addrPmntProvinceId;
    }

    /**
     * @param addrPmntProvinceId the addrPmntProvinceId to set
     */
    public void setAddrPmntProvinceId(long addrPmntProvinceId) {
        this.addrPmntProvinceId = addrPmntProvinceId;
    }

    /**
     * @return the addrPmntRegencyId
     */
    public long getAddrPmntRegencyId() {
        return addrPmntRegencyId;
    }

    /**
     * @param addrPmntRegencyId the addrPmntRegencyId to set
     */
    public void setAddrPmntRegencyId(long addrPmntRegencyId) {
        this.addrPmntRegencyId = addrPmntRegencyId;
    }

    /**
     * @return the addrPmntSubRegencyId
     */
    public long getAddrPmntSubRegencyId() {
        return addrPmntSubRegencyId;
    }

    /**
     * @param addrPmntSubRegencyId the addrPmntSubRegencyId to set
     */
    public void setAddrPmntSubRegencyId(long addrPmntSubRegencyId) {
        this.addrPmntSubRegencyId = addrPmntSubRegencyId;
    }

    /**
     * @return the indentCardIssuedBy
     */
    public String getIndentCardIssuedBy() {
        if (indentCardIssuedBy == null) {
            indentCardIssuedBy = "";
        }
        return indentCardIssuedBy;
    }

    /**
     * @param indentCardIssuedBy the indentCardIssuedBy to set
     */
    public void setIndentCardIssuedBy(String indentCardIssuedBy) {
        this.indentCardIssuedBy = indentCardIssuedBy;
    }

    /**
     * @return the indentCardBirthday
     */
    public Date getIndentCardBirthDate() {
        return indentCardBirthDate;
    }

    /**
     * @param indentCardBirthday the indentCardBirthday to set
     */
    public void setIndentCardBirthDate(Date indentCardBirthDate) {
        this.indentCardBirthDate = indentCardBirthDate;
    }

    /**
     * @return the geoAddress
     */
    public String getGeoAddress() {
        if (geoAddress == null) {
            geoAddress = "";
        }
        return geoAddress;
    }

    /**
     * @param geoAddress the geoAddress to set
     */
    public void setGeoAddress(String geoAddress) {
        this.geoAddress = geoAddress;
    }

    /**
     * @return the geoAddressPmnt
     */
    public String getGeoAddressPmnt() {
        if (geoAddressPmnt == null) {
            geoAddressPmnt = "";
        }
        return geoAddressPmnt;
    }

    /**
     * @param geoAddressPmnt the geoAddressPmnt to set
     */
    public void setGeoAddressPmnt(String geoAddressPmnt) {
        this.geoAddressPmnt = geoAddressPmnt;
    }

    /**
     * @return the taxMaritalId
     */
    public long getTaxMaritalId() {
        return taxMaritalId;
    }

    /**
     * @param taxMaritalId the taxMaritalId to set
     */
    public void setTaxMaritalId(long taxMaritalId) {
        this.taxMaritalId = taxMaritalId;
    }

    /**
     * @return the educationId
     */
    public long getEducationId() {
        return educationId;
    }

    /**
     * @param educationId the educationId to set
     */
    public void setEducationId(long educationId) {
        this.educationId = educationId;
    }

    /**
     * @return the noRekening
     */
    public String getNoRekening() {
        if (noRekening == null) {
            return "";
        }
        return noRekening;
    }

    /**
     * @param noRekening the noRekening to set
     */
    public void setNoRekening(String noRekening) {
        this.noRekening = noRekening;
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
     * @return the countIdx
     */
    public int getCountIdx() {
        return countIdx;
    }

    /**
     * @param countIdx the countIdx to set
     */
    public void setCountIdx(int countIdx) {
        this.countIdx = countIdx;
    }

    /**
     * @return the locationId
     */
    public long getLocationId() {
        return locationId;
    }

    /**
     * @param locationId the locationId to set
     */
    public void setLocationId(long locationId) {
        this.locationId = locationId;
    }

    /**
     * @return the end_contract
     */
    public Date getEnd_contract() {
        return end_contract;
    }

    /**
     * @param end_contract the end_contract to set
     */
    public void setEnd_contract(Date end_contract) {
        this.end_contract = end_contract;
    }

    /**
     * @return the workassigncompanyId
     */
    public long getWorkassigncompanyId() {
        return workassigncompanyId;
    }

    /**
     * @param workassigncompanyId the workassigncompanyId to set
     */
    public void setWorkassigncompanyId(long workassigncompanyId) {
        this.workassigncompanyId = workassigncompanyId;
    }

    /**
     * @return the workassigndivisionId
     */
    public long getWorkassigndivisionId() {
        return workassigndivisionId;
    }

    /**
     * @param workassigndivisionId the workassigndivisionId to set
     */
    public void setWorkassigndivisionId(long workassigndivisionId) {
        this.workassigndivisionId = workassigndivisionId;
    }

    /**
     * @return the workassigndepartmentId
     */
    public long getWorkassigndepartmentId() {
        return workassigndepartmentId;
    }

    /**
     * @param workassigndepartmentId the workassigndepartmentId to set
     */
    public void setWorkassigndepartmentId(long workassigndepartmentId) {
        this.workassigndepartmentId = workassigndepartmentId;
    }

    /**
     * @return the workassignsectionId
     */
    public long getWorkassignsectionId() {
        return workassignsectionId;
    }

    /**
     * @param workassignsectionId the workassignsectionId to set
     */
    public void setWorkassignsectionId(long workassignsectionId) {
        this.workassignsectionId = workassignsectionId;
    }

    /**
     * @return the workassignpositionId
     */
    public long getWorkassignpositionId() {
        return workassignpositionId;
    }

    /**
     * @param workassignpositionId the workassignpositionId to set
     */
    public void setWorkassignpositionId(long workassignpositionId) {
        this.workassignpositionId = workassignpositionId;
    }

    /**
     * @return the idcardtype
     */
    public String getIdcardtype() {
        if (idcardtype == null) {
            idcardtype = "";
        }
        return idcardtype;
    }

    /**
     * @param idcardtype the idcardtype to set
     */
    public void setIdcardtype(String idcardtype) {
        this.idcardtype = idcardtype;
    }

    /**
     * @return the npwp
     */
    public String getNpwp() {
        if (npwp == null) {
            npwp = "";
        }
        return npwp;
    }

    /**
     * @param npwp the npwp to set
     */
    public void setNpwp(String npwp) {
        this.npwp = npwp;
    }

    /**
     * @return the bpjs_no
     */
    public String getBpjs_no() {
        if (bpjs_no == null) {
            bpjs_no = "";
        }
        return bpjs_no;
    }

    /**
     * @param bpjs_no the bpjs_no to set
     */
    public void setBpjs_no(String bpjs_no) {
        this.bpjs_no = bpjs_no;
    }

    /**
     * @return the bpjs_date
     */
    public Date getBpjs_date() {
        return bpjs_date;
    }

    /**
     * @param bpjs_date the bpjs_date to set
     */
    public void setBpjs_date(Date bpjs_date) {
        this.bpjs_date = bpjs_date;
    }

    /**
     * @return the shio
     */
    public String getShio() {
        if (shio == null) {
            return "";
        }
        return shio;
    }

    /**
     * @param shio the shio to set
     */
    public void setShio(String shio) {
        this.shio = shio;
    }

    /**
     * @return the elemen
     */
    public String getElemen() {
        if (elemen == null) {
            return "";
        }
        return elemen;
    }

    /**
     * @param elemen the elemen to set
     */
    public void setElemen(String elemen) {
        this.elemen = elemen;
    }

    /**
     * @return the iq
     */
    public String getIq() {
        return iq;
    }

    /**
     * @param iq the iq to set
     */
    public void setIq(String iq) {
        this.iq = iq;
    }

    /**
     * @return the eq
     */
    public String getEq() {
        return eq;
    }

    /**
     * @param eq the eq to set
     */
    public void setEq(String eq) {
        this.eq = eq;
    }

    /**
     * @return the probationEndDate
     */
    public Date getProbationEndDate() {
        return probationEndDate;
    }

    /**
     * @param probationEndDate the probationEndDate to set
     */
    public void setProbationEndDate(Date probationEndDate) {
        this.probationEndDate = probationEndDate;
    }

    /**
     * @return the statusPensiunProgram
     */
    public int getStatusPensiunProgram() {
        return statusPensiunProgram;
    }

    /**
     * @param statusPensiunProgram the statusPensiunProgram to set
     */
    public void setStatusPensiunProgram(int statusPensiunProgram) {
        this.statusPensiunProgram = statusPensiunProgram;
    }

    /**
     * @return the startDatePensiun
     */
    public Date getStartDatePensiun() {
        return startDatePensiun;
    }

    /**
     * @param startDatePensiun the startDatePensiun to set
     */
    public void setStartDatePensiun(Date startDatePensiun) {
        this.startDatePensiun = startDatePensiun;
    }

    /**
     * @return the presenceCheckParameter
     */
    public int getPresenceCheckParameter() {
        return presenceCheckParameter;
    }

    /**
     * @param presenceCheckParameter the presenceCheckParameter to set
     */
    public void setPresenceCheckParameter(int presenceCheckParameter) {
        this.presenceCheckParameter = presenceCheckParameter;
    }

    /**
     * @return the medicalInfo
     */
    public String getMedicalInfo() {
        return medicalInfo;
    }

    /**
     * @param medicalInfo the medicalInfo to set
     */
    public void setMedicalInfo(String medicalInfo) {
        this.medicalInfo = medicalInfo;
    }

    /**
     * @return the danaPendidikan
     */
    public int getDanaPendidikan() {
        return danaPendidikan;
    }

    /**
     * @param danaPendidikan the danaPendidikan to set
     */
    public void setDanaPendidikan(int danaPendidikan) {
        this.danaPendidikan = danaPendidikan;
    }

    /**
     * @return the payrollGroup
     */
    public long getPayrollGroup() {
        return payrollGroup;
    }

    /**
     * @param payrollGroup the payrollGroup to set
     */
    public void setPayrollGroup(long payrollGroup) {
        this.payrollGroup = payrollGroup;
    }

    /**
     * @return the departmentName
     */
    public String getDepartmentName() {
        if (departmentName == null) {
            departmentName = "";
        }
        return departmentName;
    }

    /**
     * @param departmentName the departmentName to set
     */
    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    /**
     * @return the positionName
     */
    public String getPositionName() {
        if (positionName == null) {
            positionName = "";
        }
        return positionName;
    }

    /**
     * @param positionName the positionName to set
     */
    public void setPositionName(String positionName) {

        this.positionName = positionName;
    }

    /**
     * @return the companyName
     */
    public String getCompanyName() {
        if (companyName == null) {
            companyName = "";
        }
        return companyName;
    }

    /**
     * @param companyName the companyName to set
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    /**
     * @return the levelName
     */
    public String getLevelName() {
        if (levelName == null) {
            levelName = "";
        }
        return levelName;
    }

    /**
     * @param levelName the levelName to set
     */
    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    /**
     * @return the empCategoryName
     */
    public String getEmpCategoryName() {
        if (empCategoryName == null) {
            empCategoryName = "";
        }
        return empCategoryName;
    }

    /**
     * @param empCategoryName the empCategoryName to set
     */
    public void setEmpCategoryName(String empCategoryName) {
        this.empCategoryName = empCategoryName;
    }

    /**
     * @return the providerID
     */
    public long getProviderID() {
        return providerID;
    }

    /**
     * @param providerID the providerID to set
     */
    public void setProviderID(long providerID) {
        this.providerID = providerID;
    }

    /**
     * @return the memOfBpjsKesahatan
     */
    public int getMemOfBpjsKesahatan() {
        return memOfBpjsKesahatan;
    }

    /**
     * @param memOfBpjsKesahatan the memOfBpjsKesahatan to set
     */
    public void setMemOfBpjsKesahatan(int memOfBpjsKesahatan) {
        this.memOfBpjsKesahatan = memOfBpjsKesahatan;
    }

    /**
     * @return the memOfBpjsKetenagaKerjaan
     */
    public int getMemOfBpjsKetenagaKerjaan() {
        return memOfBpjsKetenagaKerjaan;
    }

    /**
     * @param memOfBpjsKetenagaKerjaan the memOfBpjsKetenagaKerjaan to set
     */
    public void setMemOfBpjsKetenagaKerjaan(int memOfBpjsKetenagaKerjaan) {
        this.memOfBpjsKetenagaKerjaan = memOfBpjsKetenagaKerjaan;
    }

    /**
     * @return the divisionName
     */
    public String getDivisionName() {
        return divisionName;
    }

    /**
     * @param divisionName the divisionName to set
     */
    public void setDivisionName(String divisionName) {
        this.divisionName = divisionName;
    }

    /**
     * @return the sectionName
     */
    public String getSectionName() {
        return sectionName;
    }

    /**
     * @param sectionName the sectionName to set
     */
    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    /**
     * @return the addrDistrictId
     */
    public long getAddrDistrictId() {
        return addrDistrictId;
    }

    /**
     * @param addrDistrictId the addrDistrictId to set
     */
    public void setAddrDistrictId(long addrDistrictId) {
        this.addrDistrictId = addrDistrictId;
    }

    /**
     * @return the addrPmntDistrictId
     */
    public long getAddrPmntDistrictId() {
        return addrPmntDistrictId;
    }

    /**
     * @param addrPmntDistrictId the addrPmntDistrictId to set
     */
    public void setAddrPmntDistrictId(long addrPmntDistrictId) {
        this.addrPmntDistrictId = addrPmntDistrictId;
    }

    /**
     * @return the noKK
     */
    public String getNoKK() {
        return noKK;
    }

    /**
     * @param noKK the noKK to set
     */
    public void setNoKK(String noKK) {
        this.noKK = noKK;
    }

    public String getNamaPemilikRekening() {
        return namaPemilikRekening;
    }

    public void setNamaPemilikRekening(String namaPemilikRekening) {
        this.namaPemilikRekening = namaPemilikRekening;
    }

    public String getCabangBank() {
        return cabangBank;
    }

    public void setCabangBank(String cabangBank) {
        this.cabangBank = cabangBank;
    }

    public String getKodeBank() {
        return kodeBank;
    }

    public void setKodeBank(String kodeBank) {
        this.kodeBank = kodeBank;
    }

    public Date getTanggalPajakTerdaftar() {
        return tanggalPajakTerdaftar;
    }

    public void setTanggalPajakTerdaftar(Date tanggalPajakTerdaftar) {
        this.tanggalPajakTerdaftar = tanggalPajakTerdaftar;
    }

    public String getNamaBank() {
        return namaBank;
    }

    public void setNamaBank(String namaBank) {
        this.namaBank = namaBank;
    }

    public String getAddressIdCard() {
        return addressIdCard;
    }

    public void setAddressIdCard(String addressIdCard) {
        this.addressIdCard = addressIdCard;
    }

}
