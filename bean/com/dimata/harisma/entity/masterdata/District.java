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
public class District extends Entity {

    private long districtId = 0;
    private long idNegara = 0;
    private long idPropinsi = 0;
    private long idKabupaten = 0;
    private long idKecamatan = 0;
    private String nmDistrict = "";

 
    /**
     * @return the idPropinsi
     */
    public long getIdPropinsi() {
        return idPropinsi;
    }

    /**
     * @param idPropinsi the idPropinsi to set
     */
    public void setIdPropinsi(long idPropinsi) {
        this.idPropinsi = idPropinsi;
    }

    /**
     * @return the idKabupaten
     */
    public long getIdKabupaten() {
        return idKabupaten;
    }

    /**
     * @param idKabupaten the idKabupaten to set
     */
    public void setIdKabupaten(long idKabupaten) {
        this.idKabupaten = idKabupaten;
    }

    /**
     * @return the nmDistrict
     */
    public String getNmDistrict() {
        return nmDistrict;
    }

    /**
     * @param nmDistrict the nmDistrict to set
     */
    public void setNmDistrict(String nmDistrict) {
        this.nmDistrict = nmDistrict;
    }

    /**
     * @return the districtId
     */
    public long getDistrictId() {
        return districtId;
    }

    /**
     * @param districtId the districtId to set
     */
    public void setDistrictId(long districtId) {
        this.districtId = districtId;
    }

    /**
     * @return the idNegara
     */
    public long getIdNegara() {
        return idNegara;
    }

    /**
     * @param idNegara the idNegara to set
     */
    public void setIdNegara(long idNegara) {
        this.idNegara = idNegara;
    }

    /**
     * @return the idKecamatan
     */
    public long getIdKecamatan() {
        return idKecamatan;
    }

    /**
     * @param idKecamatan the idKecamatan to set
     */
    public void setIdKecamatan(long idKecamatan) {
        this.idKecamatan = idKecamatan;
    }
}
