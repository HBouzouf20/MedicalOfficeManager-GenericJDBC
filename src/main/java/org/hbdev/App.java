package org.hbdev;

import java.time.LocalDate;

import org.hbdev.enums.Gender;
import org.hbdev.models.Patient;
import org.hbdev.services.PatientService;
import org.hbdev.services.PatientServiceImpl;

public class App {
    public static void main(String[] args) throws Exception {
        PatientService service = new PatientServiceImpl();
        service.getAll().forEach(System.out::println);
    }
}
