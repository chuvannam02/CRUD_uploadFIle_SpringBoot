package com.springboot.tutorial.models;

import jakarta.persistence.*;

@Entity
@Table(name = "tblEmployee")
public class Employee {
    @Id
//    The @Idannotation is inherited from javax.persistence.Id,
//    indicating the member field below is the primary key of current entity.
//    Hence your Hibernate and spring framework as well as you can do some reflect works based on this annotation
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    The @GeneratedValue annotation is to configure the way of increment of the specified column(field).
//    For example, when using Mysql, you may specify auto_increment in the definition of table to make it self-incremental,
//    and then use
    private long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    // Getter and setter
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


}
