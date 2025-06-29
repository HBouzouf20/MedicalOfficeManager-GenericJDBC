package org.hbdev.models;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hbdev.annotations.Column;
import org.hbdev.annotations.Id;
import org.hbdev.enums.Gender;

@AllArgsConstructor
@NoArgsConstructor
@Data
public abstract class Person {
    @Id
    @Column(name = "person_id")
    private Long id; //id
    @Column(name = "first_name")
    private String firstname;
    @Column(name = "last_name")
    private String lastname;
    @Column(name = "birth_date")
    private LocalDate birthDate;
    @Column(name = "cin")
    private String cin;
    @Column(name = "gender")
    private Gender gender;

    public abstract String getInfo();
}
