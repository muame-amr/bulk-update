package com.example.bulkupdate;

import javax.persistence.*;

@Entity
@Table(name = "Address")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String state;
    private String city;
    private String street;
    private String zipCode;

//    @OneToOne
//    private Person person;
}
