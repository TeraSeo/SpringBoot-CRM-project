package com.example.demo.Service;

import com.example.demo.Dto.AM_SignUpDto;
import com.example.demo.Repository.AMRepository;
import com.example.demo.entity.AM_SignUp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AMServiceImpl implements AM_SignUpInService {

    private boolean logined = false;

    private final AMRepository amRepository;

    @Autowired
    public AMServiceImpl(AMRepository amRepository) {
        this.amRepository = amRepository;
    }

    @Override
    public String get_Id(Long id) {
        Optional<AM_SignUp> AM = amRepository.findById(id);
        String userId = AM.get().getUserId();
        return userId;
    }

    @Override
    public boolean login(String loginId, String loginPw) {   // login이 되면 logined를 true로 만듦
        List<AM_SignUp> AM = amRepository.findAll();
        String password = "";
        for (AM_SignUp login : AM){
            if (login.getUserId().equals(loginId)) {
                password = login.getPassword();
                if (password.equals(loginPw)){
                    if (login.getAllowance()){
                        logined = true;
                        return logined;
                    }
                }
            }

        }
        return logined;
    }

    @Override
    public String get_Name(Long id) {
        Optional<AM_SignUp> AM = amRepository.findById(id);
        String name = AM.get().getName();
        return name;

    }

    @Override
    public AM_SignUp signUp(AM_SignUpDto am_signUpDto) {
        AM_SignUp am_signUp = new AM_SignUp();
        am_signUp.setUserId(am_signUpDto.getUserId());
        am_signUp.setPassword(am_signUpDto.getUserPassword());
        am_signUp.setName(am_signUpDto.getUserName());
        am_signUp.setEmail(am_signUpDto.getUserEmail());
        amRepository.save(am_signUp);
        return am_signUp;
    }

    @Override
    public boolean getLogined() {
        return logined;
    }

    @Override
    public void setLogined(boolean logined) {
        this.logined = logined;
    }

    @Override
    public List<AM_SignUp> findAll() {

        List<AM_SignUp> all = amRepository.findAll();

        return all;
    }

    @Override
    public void setAllowance(Long id, boolean allowance) {

        Optional<AM_SignUp> byId = amRepository.findById(id);
        AM_SignUp am_signUp = byId.get();
        am_signUp.setAllowance(allowance);
        amRepository.save(am_signUp);

    }
}