package com.example.stationinfo.controller;

import com.example.stationinfo.dao.StationInformationDao;
import com.example.stationinfo.dao.StationUserDao;
import com.example.stationinfo.dao.StationsDao;
import com.example.stationinfo.entity.StationInformation;
import com.example.stationinfo.entity.StationUser;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.apache.poi.ss.usermodel.BorderStyle.THIN;
import static org.apache.poi.ss.usermodel.HorizontalAlignment.CENTER;

@Controller
public class UserController {

    @Autowired
    StationUserDao stationUserDao;

    @Autowired
    private StationInformationDao stationInformationDao;

    @Autowired
    StationsDao stationsDao;


    @RequestMapping("/login")
    public String login(HttpSession session, String username, String password) {
        StationUser user = stationUserDao.findByUsernameAndPassword(username, password);
        if (user != null) {
            session.setAttribute("user", user);
            return "index";
        } else {
            if (session.getAttribute("user") != null) {
                return "index";
            }
            return "login";
        }

    }

    @RequestMapping("/quit")
    public String quit(HttpSession session) {
        session.invalidate();
        return "login";


    }


    @RequestMapping("/getInfo")
    @ResponseBody
    public Object getInfo(HttpSession session, Integer page, Integer limit, String stationId, String startDate, String endDate) {

        StationUser user = (StationUser) session.getAttribute("user");
        List<String> stations = new ArrayList<>();
        if (stationId == null || stationId.equals("")) {
            try {
                Integer.valueOf(user.getStation());
                stations.add(user.getStation());
            } catch (Exception e) {
                stations = stationsDao.queryByStationId(user.getStation());
            }
        } else {
            try {
                Integer.valueOf(stationId);
                stations.add(stationId);
            } catch (Exception e) {
                stations = stationsDao.queryByStationId(user.getStation());
            }
        }
        Page<StationInformation> byLastname = null;
        if (startDate != null && !startDate.equals("") && endDate != null && !endDate.equals("")) {
            byLastname = stationInformationDao.findByDate(stations, startDate, endDate, PageRequest.of(page - 1, limit, Sort.Direction.DESC, "reporttime"));
        } else {
            byLastname = stationInformationDao.findByLastname(stations, PageRequest.of(page - 1, limit, Sort.Direction.DESC, "reporttime"));
        }
        Map map = new HashMap();
        map.put("code", 0);
        map.put("count", byLastname.getTotalElements());
        map.put("data", byLastname.getContent());
        return map;
    }

//    @RequestMapping("/export")
//    public ResponseEntity<byte[]> export(HttpSession session, String stationId, String startDate, String endDate) {
//        StationUser user = (StationUser) session.getAttribute("user");
//        List<String> stations = new ArrayList<>();
//        if (stationId == null || stationId.equals("")) {
//            try {
//                Integer.valueOf(user.getStation());
//                stations.add(user.getStation());
//            } catch (Exception e) {
//                stations = stationsDao.queryByStationId(user.getStation());
//            }
//        } else {
//            try {
//                Integer.valueOf(stationId);
//                stations.add(stationId);
//            } catch (Exception e) {
//                stations = stationsDao.queryByStationId(user.getStation());
//            }
//        }
//        //设置excel文件名
//        String fileName = "信息报表";
//        //设置HttpHeaders，设置fileName编码，排除导出文档名称乱码问题
//        HttpHeaders headers = CsvUtil.setCsvHeader(fileName);
//        byte[] value = null;
//        try {
//            //获取要导出的数据
//            String[] sTitles = new String[]{"序号", "进站时间", "姓名", "性别", "年龄", "体温", "身份证号", "电话号码", "备注"};
//            String[] mapKeys = new String[]{"xuhao", "shijian", "username", "sex", "age", "tiwen", "idCarNum", "mobile", "beizhu"};
//            List<Map> dataList = new ArrayList<>();
//            List<StationInformation> list = new ArrayList<>();
//            if (startDate != null && !startDate.equals("") && endDate != null && !endDate.equals("")) {
//                list = this.stationInformationDao.findExByDate(stations, startDate, endDate);
//            } else {
//                list = stationInformationDao.findByExLastname(stations);
//            }
//            int count = 1;
//            for (StationInformation s : list) {
//                Map map = new HashMap();
//                map.put("xuhao", count++);
//                map.put("username", s.getName());
//                map.put("sex", s.getSex());
//                map.put("age", s.getAge());
//                map.put("idCarNum", "=\"" + s.getIdCardNo() + "\"");
//                map.put("mobile", "=\"" + s.getPhone() + "\"");
//                map.put("tiwen", s.getTemperature());
//                map.put("beizhu", s.getComment());
//                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//                String dateString = formatter.format(s.getReporttime());
//                map.put("shijian", dateString);
//                dataList.add(map);
//            }
//            ByteArrayOutputStream os = CsvUtil.doExport(sTitles, mapKeys, dataList);
//            value = os.toByteArray();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return new ResponseEntity<byte[]>(value, headers, HttpStatus.OK);
//
//    }


    @RequestMapping(value = "/export")
    public ResponseEntity<Resource> excel2007Export(HttpSession session,HttpServletResponse response, String stationId, String startDate, String endDate) {
        StationUser user = (StationUser) session.getAttribute("user");
        String stationName="";
        List<String> stations = new ArrayList<>();
        if (stationId == null || stationId.equals("")) {
            try {
                Integer.valueOf(user.getStation());
                stations.add(user.getStation());
                stationName= stationsDao.getOne(user.getStation()).getShangjidanwei();
            } catch (Exception e) {
                stationName=user.getStation();
                stations = stationsDao.queryByStationId(user.getStation());
            }
        } else {
            try {
                Integer.valueOf(stationId);
                stations.add(stationId);
                stationName= stationsDao.getOne(user.getStation()).getShangjidanwei();
            } catch (Exception e) {
                stationName=user.getStation();
                stations = stationsDao.queryByStationId(user.getStation());
            }
        }
        List<StationInformation> list = new ArrayList<>();
        if (startDate != null && !startDate.equals("") && endDate != null && !endDate.equals("")) {
            list = this.stationInformationDao.findExByDate(stations, startDate, endDate);
        } else {
            list = stationInformationDao.findByExLastname(stations);
        }
        try {
            ClassPathResource cpr = new ClassPathResource("/template.xlsx");
            InputStream is = cpr.getInputStream();
            Workbook workbook = new XSSFWorkbook(is);

            org.apache.poi.ss.usermodel.Sheet sheet0 = workbook.getSheetAt(0);
            // 这里作为演示，造几个演示数据，模拟数据库里查数据
            CellStyle cellStyle = workbook.createCellStyle();
            cellStyle.setBorderBottom(THIN);
            cellStyle.setBorderLeft(THIN);
            cellStyle.setBorderTop(THIN);
            cellStyle.setBorderRight(THIN);
            cellStyle.setAlignment(CENTER);
            Row r = sheet0.getRow(0);
            r.getCell(0).setCellValue("中国石油甘肃"+stationName+"销售公司疫情登记表");
            for (int i = 0; i < list.size(); i++) {
                Row row = sheet0.createRow(i + 2);
                //序号
                Cell cell_0 = row.createCell(0);
                cell_0.setCellValue(i + 1);
                cell_0.setCellStyle(cellStyle);
                //加油站名称
                Cell cell_1 = row.createCell(1);
                cell_1.setCellValue(list.get(i).getStationname());
                cell_1.setCellStyle(cellStyle);
//                进站时间
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                String dateString = formatter.format(list.get(i).getReporttime());
                Cell cell_2 = row.createCell(2);
                cell_2.setCellValue(dateString);
                cell_2.setCellStyle(cellStyle);

//                姓名
                Cell cell_3 = row.createCell(3);
                cell_3.setCellValue(list.get(i).getName());
                cell_3.setCellStyle(cellStyle);

//                性别
                Cell cell_4 = row.createCell(4);
                cell_4.setCellValue(list.get(i).getSex());
                cell_4.setCellStyle(cellStyle);

//                年龄
                Cell cell_5 = row.createCell(5);
                cell_5.setCellValue(list.get(i).getAge());
                cell_5.setCellStyle(cellStyle);

//                体温

                Cell cell_6 = row.createCell(6);
                cell_6.setCellValue(list.get(i).getTemperature());
                cell_6.setCellStyle(cellStyle);

//                身份证号

                Cell cell_7 = row.createCell(7);
                cell_7.setCellValue(list.get(i).getIdCardNo());
                cell_7.setCellStyle(cellStyle);

//                电话号码
                Cell cell_8 = row.createCell(8);
                cell_8.setCellValue(list.get(i).getPhone());
                cell_8.setCellStyle(cellStyle);

//                备注
                Cell cell_9 = row.createCell(9);
                cell_9.setCellValue(list.get(i).getComment());
                cell_9.setCellStyle(cellStyle);

//                row.createCell(0).setCellValue(i);
//                row.createCell(1).setCellValue(list.get(i));
//                Cell cell = row.createCell(2);
//                CellStyle cellStyle = workbook.createCellStyle();
//                cellStyle.setBorderBottom(THIN);
//                cell.setCellStyle(cellStyle);


            }
            String fileName = "data.xlsx";
            downLoadExcel(fileName, response, workbook);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<Resource>(HttpStatus.OK);
    }

    public static void downLoadExcel(String fileName, HttpServletResponse response, Workbook workbook) {
        try {
            response.setCharacterEncoding("UTF-8");
            response.setHeader("content-Type", "application/vnd.ms-excel");
            response.setHeader("Content-Disposition",
                    "attachment;filename=\"" + URLEncoder.encode(fileName, "UTF-8") + "\"");
            workbook.write(response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
