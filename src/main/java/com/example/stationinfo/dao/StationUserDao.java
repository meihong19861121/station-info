package com.example.stationinfo.dao;

import com.example.stationinfo.entity.StationUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StationUserDao extends JpaRepository<StationUser,String> {
    StationUser findByUsernameAndPassword(String username, String password);
}
