package com.schmidmt.demo.models.dao;

import com.schmidmt.demo.models.MyGrantAuthority;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface MyGrantAuthorityRepository extends CrudRepository<MyGrantAuthority, Integer> {
    public MyGrantAuthority getByRole(String role);
}
