
package com.gitata.parkingally.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EmelParkingLotResponse {

    @SerializedName("id_parque")
    @Expose
    private String parkId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("active")
    @Expose
    private Integer active;
    @SerializedName("id_entidade")
    @Expose
    private Integer entityId;
    @SerializedName("capacidade_max")
    @Expose
    private Integer maxCapacity;
    @SerializedName("occupation")
    @Expose
    private Integer occupation;
    @SerializedName("data_ocupacao")
    @Expose
    private String occupationDate;
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("longitude")
    @Expose
    private String longitude;
    @SerializedName("type")
    @Expose
    private String type;

    /**
     * No args constructor for use in serialization
     * 
     */
    public EmelParkingLotResponse() {
    }

    /**
     * 
     * @param parkId
     * @param type
     * @param maxCapacity
     * @param entityId
     * @param occupation
     * @param latitude
     * @param name
     * @param active
     * @param occupationDate
     * @param longitude
     */
    public EmelParkingLotResponse(String parkId, String name, Integer active, Integer entityId, Integer maxCapacity, Integer occupation, String occupationDate, String latitude, String longitude, String type) {
        super();
        this.parkId = parkId;
        this.name = name;
        this.active = active;
        this.entityId = entityId;
        this.maxCapacity = maxCapacity;
        this.occupation = occupation;
        this.occupationDate = occupationDate;
        this.latitude = latitude;
        this.longitude = longitude;
        this.type = type;
    }

    public String getParkId() {
        return parkId;
    }

    public void setParkId(String parkId) {
        this.parkId = parkId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getActive() {
        return active;
    }

    public void setActive(Integer active) {
        this.active = active;
    }

    public Integer getEntityId() {
        return entityId;
    }

    public void setEntityId(Integer entityId) {
        this.entityId = entityId;
    }

    public Integer getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(Integer maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public Integer getOccupation() {
        return occupation;
    }

    public void setOccupation(Integer occupation) {
        this.occupation = occupation;
    }

    public String getOccupationDate() {
        return occupationDate;
    }

    public void setOccupationDate(String occupationDate) {
        this.occupationDate = occupationDate;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
