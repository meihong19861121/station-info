package com.example.stationinfo.dao;

import com.example.stationinfo.entity.StationInformation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StationInformationDao extends JpaRepository<StationInformation, String> {
    @Query(value = "SELECT * FROM station_information_collection WHERE stationid in (?1)",
            countQuery = "SELECT count(*) FROM station_information_collection WHERE stationid in (?1)",
            nativeQuery = true)
    Page<StationInformation> findByLastname(List<String> stationid, Pageable pageable);

    @Query(value = "SELECT * FROM station_information_collection WHERE stationid in (?1) order by reporttime desc",
            nativeQuery = true)
    List<StationInformation> findByExLastname(List<String> stationid);


    @Query(value = "SELECT * FROM station_information_collection WHERE stationid in (:stationid) and reporttime>=:startDate and reporttime<=:endDate",
            countQuery = "SELECT count(*) FROM station_information_collection WHERE stationid in (:stationid) and reporttime>=:startDate and reporttime<=:endDate",
            nativeQuery = true)
    Page<StationInformation> findByDate(@Param("stationid") List<String> stationid, @Param("startDate") String startDate,@Param("endDate") String endDate, Pageable pageable);

    @Query(value = "SELECT * FROM station_information_collection WHERE stationid in (?1) and reporttime>=?2 and reporttime<=?3 order by reporttime desc",
            nativeQuery = true)
    List<StationInformation> findExByDate(List<String> stationid, String startDate, String endDate);
}
