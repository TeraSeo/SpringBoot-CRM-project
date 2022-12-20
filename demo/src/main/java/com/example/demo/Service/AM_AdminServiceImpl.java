package com.example.demo.Service;

import com.example.demo.Repository.AM_AdminRepository;
import com.example.demo.entity.AM_Admin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AM_AdminServiceImpl implements AM_AdminService{

    private final AM_AdminRepository am_adminRepository;

    @Autowired
    public AM_AdminServiceImpl(AM_AdminRepository am_adminRepository) {
        this.am_adminRepository = am_adminRepository;
    }

    @Override
    public boolean loginCheck(String userId, String userPassword) {

        List<AM_Admin> admins = am_adminRepository.findAll();
        for (AM_Admin admin : admins){
            if (admin.getUserId().equals(userId)){
                if (admin.getUserPassword().equals(userPassword)){
                    return true;
                }
            }
        }

        return false;
    }

}
