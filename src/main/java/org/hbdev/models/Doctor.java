package org.hbdev.models;

import org.hbdev.enums.Gender;

public class Doctor extends Person{


    @Override
    public String getInfo() {
        return String.format("""
            🩺 Doctor Badge 📁
            ─────────────────────────────
            👤 Full Name    : %s %s
            🆔 Patient ID   : %d
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
