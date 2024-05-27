package com.smart.Smartcontactmanager.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter

public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int cId;
    private String name;
    private String secondName;
    private String work;
    private String email;
    private String phone;
    @Column(length=5000)
    private String description;
    @ManyToOne
    @JsonIgnore
    private User user;


    public int getcId() {
        return cId;
    }

    public String getName() {
        return name;
    }

    public String getSecondName() {
        return secondName;
    }

    public String getWork() {
        return work;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getDescription() {
        return description;
    }

    public User getUser() {
        return user;
    }

    @Override
    public boolean equals(Object obj) {
        return this.cId==((Contact)obj).getcId();
    }
}

