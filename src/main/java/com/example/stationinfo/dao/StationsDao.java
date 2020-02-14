package com.example.stationinfo.dao;

import com.example.stationinfo.dto.Node;
import com.example.stationinfo.entity.Stations;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StationsDao extends JpaRepository<Stations, String> {

    @Query("SELECT " +

            "    new com.example.stationinfo.dto.Node(v.shangjidanwei, v.shangjidanwei,false ) " +

            "FROM " +

            "    Stations v  where v.shangjidanwei=?1 or v.stationid=?1" +

            "GROUP BY " +

            "    v.shangjidanwei")
    List<Node> getRootNode(String shangjidanwei);

    @Query("SELECT " +

            "    new com.example.stationinfo.dto.Node(v.stationid, v.stationname,false ) " +

            "FROM " +

            "    Stations v  where v.shangjidanwei=?1 or v.stationid=?1")
    List<Node> getRootNode2(String shangjidanwei);

    @Query("SELECT " +

            "    new com.example.stationinfo.dto.Node(v.stationid, v.stationname,false ) " +

            "FROM " +

            "    Stations v  where v.shangjidanwei=?1")
    List<Node> queryByShangjidanwei(String shangjidanwei);

    @Query(value = "SELECT " +

            "    v.stationid " +

            "FROM " +

            "    stations v  where v.shangjidanwei=?1",nativeQuery = true)
    List<String> queryByStationId(String shangjidanwei);
}