package com.schmidmt.demo.models.dao;


import com.schmidmt.demo.models.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface UserRepository extends CrudRepository<User, Long> {

    public User findByUsername(String username);

}
