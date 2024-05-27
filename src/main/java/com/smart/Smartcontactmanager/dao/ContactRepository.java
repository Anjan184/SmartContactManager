package com.smart.Smartcontactmanager.dao;

import com.smart.Smartcontactmanager.entities.Contact;
import com.smart.Smartcontactmanager.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ContactRepository extends JpaRepository<Contact,Integer> {

    @Query("from Contact as c where c.user.id=:userId ")
    //current page
    //contact per page
    public Page<Contact> findContactsByUser(@Param("userId") int userId, Pageable pageable);

    //search
    public List<Contact> findByNameContainingAndUser(String name, User user);
}
