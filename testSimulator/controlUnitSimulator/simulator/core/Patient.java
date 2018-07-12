package controlUnitSimulator.simulator.core;

import java.util.ArrayList;
import java.util.List;

public class Patient {
    private String patientID;
    private List<String> associatedDoctor;

    public Patient (String patientID) {
        this.patientID = patientID;
        this.associatedDoctor = new ArrayList<>();
    }

    public String getPatientID() {
        return patientID;
    }

    public void setPatientID(String patientID) {
        this.patientID = patientID;
    }

    public List<String> getAssociatedDoctor() {
        return associatedDoctor;
    }

    public void addDoctor( String doctor ) {
        this.associatedDoctor.add(doctor);
    }
}

