package com.example.stationinfo.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Stations {
    @Id
    private String stationid;
    private String stationname;
    private String shangjidanwei;

    public String getStationid() {
        return stationid;
    }

    public void setStationid(String stationid) {
        this.stationid = stationid;
    }

    public String getStationname() {
        return stationname;
    }

    public void setStationname(String stationname) {
        this.stationname = stationname;
    }

    public String getShangjidanwei() {
        return shangjidanwei;
    }

    public void setShangjidanwei(String shangjidanwei) {
        this.shangjidanwei = shangjidanwei;
    }
}
