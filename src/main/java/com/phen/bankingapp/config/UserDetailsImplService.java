package com.phen.bankingapp.config;

import com.phen.bankingapp.service.BankAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;



@Service
public class UserDetailsImplService implements UserDetailsService {


    @Autowired
    private BankAccountService bankAccountService;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {

        return UserDetailsImpl.buildUserDetail(bankAccountService.findUserByName(s));
    }

}
