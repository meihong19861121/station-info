package com.example.stationinfo;

import com.example.stationinfo.StationInfoApplicationTests;
import com.example.stationinfo.dao.StationInformationDao;
import com.example.stationinfo.dao.StationsDao;
import com.example.stationinfo.dto.Node;
import com.example.stationinfo.entity.StationInformation;
import com.example.stationinfo.entity.Stations;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@Component
class StationsDaoTest extends StationInfoApplicationTests {

    @Autowired
    StationsDao stationsDao;

    @Autowired
    StationInformationDao stationInformationDao;

    @Test
    public void a(){
//        List<Node> rootNode = stationsDao.getRootNode();
//        System.out.println(rootNode);
//        Optional<Stations> byId = stationsDao.findById("101001");
//        System.out.println(byId);
        Page<StationInformation> byLastname = stationInformationDao.findByLastname(Arrays.asList("121003"),  PageRequest.of(1, 3, Sort.Direction.ASC,"id"));
        System.out.println(byLastname);

    }
}