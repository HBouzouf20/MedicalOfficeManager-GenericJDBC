package org.hbdev.dao;

import org.hbdev.models.Patient;

public interface PatientDAO extends CrudDao<Patient, Long>{
    void prepareMedicalRecord(Patient patient);

}
