package com.courier.backend.dto;

import com.courier.backend.beans.Cities;
import java.util.List;

public class StationDto {

    private Integer id;
    private String zone;
    private String subtitle;
    private List<Cities> cityList;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public List<Cities> getCityList() {
        return cityList;
    }

    public void setCityList(List<Cities> cityList) {
        this.cityList = cityList;
    }
}
