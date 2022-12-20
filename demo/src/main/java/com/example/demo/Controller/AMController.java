package com.example.demo.Controller;

import com.example.demo.Dto.AM_LoginDto;
import com.example.demo.Dto.AM_MainDto;
import com.example.demo.Dto.AM_SignUpDto;
import com.example.demo.Service.AM_AdminService;
import com.example.demo.Service.AM_MainService;
import com.example.demo.Service.AM_SignUpInService;
import com.example.demo.entity.AM_Main;
import com.example.demo.entity.AM_SignUp;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller   // @RestController 는 RestApi개발을 위해 사용한다.
@RequestMapping("AM")
public class AMController {

    boolean adminLogin = false;
    boolean login = false; // 로그인이 잘 되었는가 아닌가
    boolean searched = false; // 검색된 결과 인가 아닌가
    String loginCheck = "";
    private final AM_SignUpInService am_signUpInService;
    private final AM_MainService am_mainService;
    private final AM_AdminService am_adminService;

    @Autowired
    public AMController(AM_SignUpInService am_signUpService, AM_MainService am_mainService, AM_AdminService am_adminService) {
        this.am_signUpInService = am_signUpService;
        this.am_mainService = am_mainService;
        this.am_adminService = am_adminService;
    }

    @GetMapping()
    public String login_page(Model model){
        model.addAttribute("loginCheck",loginCheck);
        loginCheck = "";
        searched = false;
        return "login";
    }

    @GetMapping("signUp")
    public String signUp_page(){
        searched = false;
        return "signUp";
    }

    @GetMapping("adminLogin")
    public String adminLogin_page(){
        searched = false;
        return "admin";
    }

    @GetMapping("home")
    public String home_page(Model model){
        searched = false;
        if (login || adminLogin){
            if (login){
                model.addAttribute("authority","user");
            }
            else {
                model.addAttribute("authority","admin");
            }

            return "home";

        }
        else {
            return "redirect:/AM";
        }

    }

    @GetMapping("adminPage")
    public String adminPage(Model model){
        searched = false;
        if (adminLogin){
            List<AM_SignUp> all = am_signUpInService.findAll();
            model.addAttribute("all", all);

            return "adminPage";
        }
        else{
            return "redirect:adminLogin";
        }

    }

    @GetMapping("{id}")
    public String main_searched(@PathVariable("id") String id, Model model){
        searched = false;
        if (login || adminLogin){

            if (login){
                model.addAttribute("authority","user");
            }
            else {
                model.addAttribute("authority","admin");
            }

            Optional<AM_Main> byId = am_mainService.findById(Long.parseLong(id));
            model.addAttribute("id",id);
            model.addAttribute("byId", byId.get());

            return "modify";
        }
        else {
            return "redirect:/AM";
        }

    }

    @GetMapping("main")
    public String main_page(Model model, RedirectAttributes redirectAttributes){
        if (login || adminLogin){

            if (login){
                model.addAttribute("authority","user");
            }
            else {
                model.addAttribute("authority","admin");
            }

            if (!searched){
                List<AM_Main> datas = am_mainService.data_findAll();
                model.addAttribute("datas",datas);
                model.addAttribute("counts",datas.size());
            }

            return "main";
        }
        else {
            return "redirect:/AM";
        }

    }

    @PostMapping("signUpAction")
    public String signUpAction(AM_SignUpDto am_signUpDto, Model model){

        model.addAttribute("id", am_signUpDto.getUserId());
        model.addAttribute("pw", am_signUpDto.getUserPassword());
        model.addAttribute("name", am_signUpDto.getUserName());
        model.addAttribute("email", am_signUpDto.getUserEmail());

        am_signUpInService.signUp(am_signUpDto);

        return "redirect:/AM";
    }

    @PostMapping("adminLoginAction")
    public String adminLoginAction(@RequestParam String userId, @RequestParam String userPassword){

        adminLogin = am_adminService.loginCheck(userId, userPassword);

        if (adminLogin){
            return "redirect:adminPage";
        }
        else{
            return "redirect:adminLogin";
        }

    }

    @PostMapping("loginAction")
    public String loginAction(AM_LoginDto am_loginDto){
        String loginId = am_loginDto.getUserId();
        String loginPw = am_loginDto.getUserPassword();

        login = am_signUpInService.login(loginId, loginPw);  // 아이디 비번 일치하면 login을 true로 한다.
        if (login) {
            loginCheck = "";
            return "redirect:home";
        }
        else {
            loginCheck = "아이디 혹은 비밀번호가 일치하지 않습니다.";
            return "redirect:/AM";
        }
    }

    @GetMapping("logoutAction")
    public String logoutAction(){
        login = false;
        am_signUpInService.setLogined(false);
        return "redirect:/AM";
    }

    @GetMapping("adminLogoutAction")
    public String adminLogoutAction(){
        adminLogin = false;
        return "redirect:/AM";
    }

    @PostMapping("homeAction")
    public String homeAction(AM_MainDto am_mainDto){
        System.out.println(am_mainDto.getAccount());
        am_mainService.data_save(am_mainDto);
        return "redirect:main";
    }

    @PostMapping("searchAction")
    public String searchAction(@RequestParam("provider") String provider, @RequestParam("search") String customer,
                               RedirectAttributes redirectAttributes, Model model){

        searched = true;

        if (provider.isEmpty() || provider == null){
            provider = "all";
        }
        if (customer.isEmpty() ||customer == null){
            customer = "all";
        }

        System.out.println(provider + ", " + customer);

        List<AM_Main> am_mains = am_mainService.searched_findAll(provider, customer);

        redirectAttributes.addFlashAttribute("datas", am_mains);
        redirectAttributes.addFlashAttribute("counts",am_mains.size());

        return "redirect:main" ;
    }

    @PostMapping("excelAction")
    public String excelAction(@RequestParam("file") MultipartFile excel) throws IOException {
        ArrayList<ArrayList<String>> excelDatas = am_mainService.getExcelFile(excel);
        am_mainService.excelData_save(excelDatas);

        return "redirect:main";
    }

    @PostMapping("modifyAction")
    public String modifyAction(AM_MainDto am_mainDto, @RequestParam() String id){

        am_mainService.saveById(Long.parseLong(id), am_mainDto);

        return "redirect:main";
    }

    @PostMapping("adminpageAction")
    public String adminpageAction(@RequestParam() String id, @RequestParam() String allowance){
        boolean check = false;

        if (allowance.equals("true")){
            check = true;
        }

        am_signUpInService.setAllowance(Long.parseLong(id), check);

        return "redirect:adminPage";
    }

    @PostMapping("excelDownload")
    public String excelDownload(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;filename=backup.xls");
        am_mainService.excelData_put(response);

        return "redirect:main";
    }

}
