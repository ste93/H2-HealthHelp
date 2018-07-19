package controlUnitSimulator.simulator.core;

import java.util.ArrayList;
import java.util.List;

/**
 * Represent a patient with his doctor's association.
 * @author ManuBottax
 */
public class Patient {
    private String patientID;
    private List<String> associatedDoctor;

    /**
     * Create a new patient.
     * @param patientID - the ID of the specific patient. ( e.g name.surname.123 )
     */
    public Patient (String patientID) {
        this.patientID = patientID;
        this.associatedDoctor = new ArrayList<>();
    }

    /**
     * @return the ID of the patient.
     */
    public String getPatientID() {
        return patientID;
    }

    public void setPatientID(String patientID) {
        this.patientID = patientID;
    }

    /**
     * @return the list of the ID of the doctors associated with this patient.
     */
    public List<String> getAssociatedDoctor() {
        return associatedDoctor;
    }

    /**
     * Add a doctorID to the list of the doctors associated with this patient.
     * @param doctorID - the ID of the doctor. ( e.g name.surname.123 )
     */
    public void addDoctor( String doctorID ) {
        this.associatedDoctor.add(doctorID);
    }
}

