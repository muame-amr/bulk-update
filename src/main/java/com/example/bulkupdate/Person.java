package com.example.bulkupdate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "PERSON", indexes = {
        @Index(name = "idx_person_id_first_name", columnList = "id, first_name, last_name, email, gender")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Person {

    @Id
    @SequenceGenerator(name = "person_sequence", sequenceName = "person_sequence", allocationSize = 1)
    @GeneratedValue(generator = "person_sequence", strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;
    private String email;
    private String gender;

//    @OneToOne(mappedBy = "person")
//    private Address address;
}
