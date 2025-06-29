package org.hbdev.models;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hbdev.annotations.Column;
import org.hbdev.annotations.Table;
import org.hbdev.enums.Gender;
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "patients")
public class Patient extends Person {
    @Column(name = "assurance")
    private String assurance;
    @Column(name = "status")
    private String status;

    @Override
    public String getInfo() {
        return String.format("""
            🩺 Medical Record 📁
            ─────────────────────────────
            👤 Full Name    : %s %s
            🆔 Patient ID   : %s
            📅 Birth Date   : %s
            🪪 CIN          : %s
            🚻 Gender       : %s
            ─────────────────────────────
            """,
            getFirstname(),
            getLastname(),
            getId(),
            getBirthDate() != null ? getBirthDate().toString() : "N/A",
            getCin() != null ? getCin() : "N/A",
            getGender()
    );
    }


}
