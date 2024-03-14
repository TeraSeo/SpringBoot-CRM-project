package com.example.demo.Service;

import com.example.demo.Dto.AM_MainDto;
import com.example.demo.entity.AM_Main;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface AM_MainService {

    void data_save(AM_MainDto am_mainDto);

    void data_delete();

    void data_change();

    List<AM_Main> data_findAll();

    ArrayList<ArrayList<String>> getExcelFile(MultipartFile excel);

    void excelData_save(ArrayList<ArrayList<String>> excelDatas);

    List<AM_Main> searched_findAll(String company, String search);

    Optional<AM_Main> findById(Long id);

    void saveById(Long id, AM_MainDto am_mainDto);

    void excelData_put (HttpServletResponse response) throws IOException;

    void writePutData(HSSFSheet sheet, List<AM_Main> all);

}
