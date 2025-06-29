package org.hbdev.services;

import java.util.List;

import org.hbdev.dao.PatientDAO;
import org.hbdev.dao.PatientDaoImpl;
import org.hbdev.models.Patient;

public class PatientServiceImpl implements PatientService {

    private PatientDAO patientDAO;

    public PatientServiceImpl() {
        this.patientDAO = new PatientDaoImpl();
    }

    @Override
    public void printInfo(Patient object) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'printInfo'");
    }

    @Override
    public boolean exist(Patient object) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'exist'");
    }

    @Override
    public int count() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'count'");
    }

    @Override
    public List<Patient> getAll() {
        try {
            return patientDAO.findAll();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Patient getById(String id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getById'");
    }

    @Override
    public Patient update(String id, Patient object) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public boolean delete(Patient object) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

    @Override
    public Patient create(Patient patient) {
        return null;
    }

    @Override
    public String generateId(Patient p) {
        return p.getGender().name()+p.getBirthDate().getMonthValue()+"";
    }



}
