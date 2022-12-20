package com.example.demo.Service;

import com.example.demo.Dto.AM_MainDto;
import com.example.demo.Repository.AM_MainRepository;
import com.example.demo.entity.AM_Main;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class AM_MainServiceImpl implements AM_MainService{

    private final AM_MainRepository amMainRepository;

    @Autowired
    public AM_MainServiceImpl(AM_MainRepository amMainRepository) {
        this.amMainRepository = amMainRepository;
    }

    @Override
    public void data_save(AM_MainDto am_mainDto) {
        AM_Main am_main = new AM_Main(null, am_mainDto.getSector(), am_mainDto.getProvider(),am_mainDto.getInstallDate(),
                am_mainDto.getServiceNum(), am_mainDto.getCustomer(), am_mainDto.getNotes(), am_mainDto.getIp(),
                am_mainDto.getModelType(), am_mainDto.getSerialNum(), am_mainDto.getVersion(), am_mainDto.getAccount(),
                am_mainDto.getDdns(), am_mainDto.getAddress(), am_mainDto.getContractType(), am_mainDto.getLicense(),
                am_mainDto.getMarker(), am_mainDto.getRespon(),am_mainDto.getRecent(), am_mainDto.getError(), am_mainDto.getEtc());
        amMainRepository.save(am_main);
    }

    @Override
    public void data_delete() {

    }

    @Override
    public void data_change() {

    }

    @Override
    public List<AM_Main> data_findAll() {
        List<AM_Main> allAm = amMainRepository.findAll();
        return allAm;
    }

    @Override
    public ArrayList<ArrayList<String>> getExcelFile(MultipartFile excel) {

        ArrayList<ArrayList<String>> excelDatas = new ArrayList<>();

        try {
            HSSFWorkbook workbook = new HSSFWorkbook(excel.getInputStream());
            HSSFSheet sheet = workbook.getSheetAt(0);

            int rows = sheet.getPhysicalNumberOfRows();
            int cells = 21;

            ArrayList<String> excelData = new ArrayList<>();
            for(int i=0; i < rows; i++) {
                HSSFRow row = sheet.getRow(i);
                for(int j=0; j < cells; j++) {
                    if (row.getCell(j) == null || row.getCell(j).toString().isEmpty() || row.getCell(j).toString().length() == 0){
                        excelData.add(null);
                    }
                    else {
                        excelData.add(row.getCell(j).toString());
                    }
                }
                excelDatas.add(excelData);
                excelData = new ArrayList<>();
            }

            workbook.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return excelDatas;

    }

    @Override
    public void excelData_save(ArrayList<ArrayList<String>> excelDatas) {

        amMainRepository.deleteAll();
        amMainRepository.restartSeq();


        for (ArrayList<String> excelData : excelDatas){

            AM_Main am_mainDto = new AM_Main(null,excelData.get(1),excelData.get(2),excelData.get(3),excelData.get(4),
                    excelData.get(5), excelData.get(6), excelData.get(7),excelData.get(8), excelData.get(9), excelData.get(10),
                    excelData.get(11), excelData.get(12), excelData.get(13), excelData.get(14), excelData.get(15),
                    excelData.get(16), excelData.get(17),excelData.get(18), excelData.get(19), excelData.get(20));
            amMainRepository.save(am_mainDto);
        }

    }

    @Override
    public List<AM_Main> searched_findAll(String provider, String customer) {

        List<AM_Main> byNameContains;

        if (provider.equals("all") && customer.equals("all")){
            byNameContains = amMainRepository.findAll();
        }
        else if (provider.equals("all") && !customer.equals("all")){
            byNameContains = amMainRepository.findByCustomerContains(customer);
        }
        else if (customer.equals("all") && !provider.equals("all")){
            byNameContains = amMainRepository.findByProviderContains(provider);
        }
        else {
            byNameContains = amMainRepository.findByProviderAndCustomerContains(provider,customer);
        }

        return byNameContains;
    }

    @Override
    public Optional<AM_Main> findById(Long id) {

        Optional<AM_Main> byId = amMainRepository.findById(id);
        if (byId.isPresent()){
            return byId;
        }

        return Optional.empty();
    }

    @Override
    public void saveById(Long id, AM_MainDto am_mainDto) {
        AM_Main am_main = new AM_Main(id, am_mainDto.getSector(), am_mainDto.getProvider(),am_mainDto.getInstallDate(),
                am_mainDto.getServiceNum(), am_mainDto.getCustomer(), am_mainDto.getNotes(), am_mainDto.getIp(),
                am_mainDto.getModelType(), am_mainDto.getSerialNum(), am_mainDto.getVersion(), am_mainDto.getAccount(),
                am_mainDto.getDdns(), am_mainDto.getAddress(), am_mainDto.getContractType(), am_mainDto.getLicense(),
                am_mainDto.getMarker(), am_mainDto.getRespon(),am_mainDto.getRecent(), am_mainDto.getError(), am_mainDto.getEtc());

        amMainRepository.save(am_main);
    }

    @Override
    public void excelData_put(HttpServletResponse response) throws IOException {

        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("Student");
        List<AM_Main> all = amMainRepository.findAll();
        writePutData(sheet, all);
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();

    }

    public void writePutData(HSSFSheet sheet, List<AM_Main> all) {
        Cell cell;
        Row row;
        int i = 0;

        for (AM_Main data : all){
            row = sheet.createRow(i);
            cell = row.createCell(0);
            cell.setCellValue(data.getId());
            cell = row.createCell(1);
            cell.setCellValue(data.getSector());
            cell = row.createCell(2);
            cell.setCellValue(data.getProvider());
            cell = row.createCell(3);
            cell.setCellValue(data.getInstallDate());
            cell = row.createCell(4);
            cell.setCellValue(data.getServiceNum());
            cell = row.createCell(5);
            cell.setCellValue(data.getCustomer());
            cell = row.createCell(6);
            cell.setCellValue(data.getNotes());
            cell = row.createCell(7);
            cell.setCellValue(data.getIp());
            cell = row.createCell(8);
            cell.setCellValue(data.getModelType());
            cell = row.createCell(9);
            cell.setCellValue(data.getSerialNum());
            cell = row.createCell(10);
            cell.setCellValue(data.getVersion());
            cell = row.createCell(11);
            cell.setCellValue(data.getAccount());
            cell = row.createCell(12);
            cell.setCellValue(data.getDdns());
            cell = row.createCell(13);
            cell.setCellValue(data.getAddress());
            cell = row.createCell(14);
            cell.setCellValue(data.getContractType());
            cell = row.createCell(15);
            cell.setCellValue(data.getLicense());
            cell = row.createCell(16);
            cell.setCellValue(data.getMarker());
            cell = row.createCell(17);
            cell.setCellValue(data.getRespon());
            cell = row.createCell(18);
            cell.setCellValue(data.getRecent());
            cell = row.createCell(19);
            cell.setCellValue(data.getError());
            cell = row.createCell(20);
            cell.setCellValue(data.getEtc());
            i++;
        }

        for (int j = 0; j < 21; j++){
            sheet.autoSizeColumn(j);
        }

    }


}
