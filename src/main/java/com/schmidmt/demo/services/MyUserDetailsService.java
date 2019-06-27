package com.schmidmt.demo.services;

import com.schmidmt.demo.config.SetupAdmin;
import com.schmidmt.demo.models.MyUserPrincipal;
import com.schmidmt.demo.models.User;
import com.schmidmt.demo.models.dao.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service("userDetailsService")
public class MyUserDetailsService implements UserDetailsService {

    private static Logger LOG = LoggerFactory
            .getLogger(SetupAdmin.class);

    public MyUserDetailsService() {}

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        return new MyUserPrincipal(user);
    }
}
