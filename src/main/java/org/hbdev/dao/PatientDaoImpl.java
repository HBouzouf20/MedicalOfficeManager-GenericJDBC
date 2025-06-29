package org.hbdev.dao;

import org.hbdev.models.Patient;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PatientDaoImpl extends AbstractCrudDao<Patient, Long> implements PatientDAO{

    @Override
    protected void setInsertStatement(PreparedStatement stmt, Patient entity) throws SQLException {

    }

    @Override
    protected void setUpdateStatement(PreparedStatement stmt, Patient entity) throws SQLException {

    }

    @Override
    protected Long getEntityId(Patient entity) {
        return 0L;
    }

    @Override
    protected String getInsertQuery() {
        return "";
    }

    @Override
    protected String getUpdateQuery() {
        return "";
    }

    @Override
    public void prepareMedicalRecord(Patient patient) {

    }
}
