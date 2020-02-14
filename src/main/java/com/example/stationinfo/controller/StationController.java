package com.example.stationinfo.controller;

import com.example.stationinfo.dao.StationsDao;
import com.example.stationinfo.dto.Node;
import com.example.stationinfo.entity.StationUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;

@RestController
public class StationController {

    @Autowired
    private StationsDao stationsDao;

    @RequestMapping("/getNodes")
    public Object getNodes(String id, HttpSession session) {
        List<Node> rootNode = null;
        StationUser user = (StationUser) session.getAttribute("user");
        if (id == null) {
            try {
                Integer integer = Integer.valueOf(user.getStation());
                rootNode = stationsDao.getRootNode2(user.getStation());
            } catch (Exception e) {
                rootNode = stationsDao.getRootNode(user.getStation());
                rootNode.iterator().forEachRemaining(n -> {
                    n.setIsParent(true);
                });
            }
        }else{
            rootNode = stationsDao.queryByShangjidanwei(user.getStation());
        }
        return rootNode;
    }
}
