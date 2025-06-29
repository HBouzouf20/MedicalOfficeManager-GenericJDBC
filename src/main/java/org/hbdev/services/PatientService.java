package org.hbdev.services;

import org.hbdev.models.Patient;

public interface PatientService extends CrudService<Patient, String>, HelperService<Patient> {
    String generateId(Patient p);
}
