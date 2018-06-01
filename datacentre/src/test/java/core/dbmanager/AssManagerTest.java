package core.dbmanager;

import static org.junit.Assert.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.*;

import java.lang.Exception;

public class AssManagerTest {

    private AssManager associationsManager = new AssociationsManagerImpl();

    @Test
    public void test() throws JSONException {
        //-------------------------------------------------------CREATE NEW PATIENTS-------------------------------------------------------
        //try adding a new patient, we want it to success
        assert(associationsManager.createNewPatient("marghe.pecos", "Margherita", "Pecorelli", "jskdlkdfjdjfi"));

        //try adding the same patient, we want it to fail
        assertFalse(associationsManager.createNewPatient("marghe.pecos", "Margherita", "Pecorelli", "jskdlkdfjdjfi"));

        //try adding a different patient but with the same ID, we want it to fail
        assertFalse(associationsManager.createNewPatient("marghe.pecos", "Marghe", "Peco", "alsllsalallal"));

        //try adding a new patient but with missing credential, we want it to fail
        assertFalse(associationsManager.createNewPatient("sara.rossi", "Sara", "Rossi", ""));
        assertFalse(associationsManager.createNewPatient("sara.rossi", "", "Rossi", "alslallksf"));
        assertFalse(associationsManager.createNewPatient("", "Sara", "Rossi", "alslallksf"));



        //-------------------------------------------------------CREATE NEW DOCOTRS-------------------------------------------------------
        //same of the patient

        //try adding a new doctor, we want it to success
        assert(associationsManager.createNewDoctor("mario.bianchi", "Mario", "Bianchi", "ksjdsofòjdfk"));

        //try adding the same doctor, we want it to fail
        assertFalse(associationsManager.createNewDoctor("mario.bianchi", "Mario", "Bianchi", "ksjdsofòjdfk"));

        //try adding a different doctor but with the same ID, we want it to fail
        assertFalse(associationsManager.createNewDoctor("mario.bianchi", "Ma", "Bianchinini", "laldaslaskdjask"));

        //try adding a new doctor but with missing credential, we want it to fail
        assertFalse(associationsManager.createNewDoctor("matteo.rosa", "Matteo", "Rosa", ""));
        assertFalse(associationsManager.createNewDoctor("matteo.rosa", "", "Rosa", "qpqpwoeqowoepw"));
        assertFalse(associationsManager.createNewDoctor("", "Matteo", "Rosa", "qpqpwoeqowoepw"));



        //-------------------------------------------------------GET PATIENTS' DATA-------------------------------------------------------
        try {
            //getting patient's data
            JSONObject json = associationsManager.getPatientData("marghe.pecos");
            //checking if patient's data are those expected
            assertEquals(json.get("_id"), "marghe.pecos");
            assertEquals(json.get("name"), "Margherita");
            assertEquals(json.get("surname"), "Pecorelli");
            assertEquals(json.get("cf"), "jskdlkdfjdjfi");
        } catch (Exception e) {
            Assert.fail();
        }

        //try getting data of an non-existent patient, we want it to throw an exception: 404 (not found)
        try {
            JSONObject fail = associationsManager.getPatientData("fail.fail");
            Assert.fail();
        } catch (Exception e) {
            assertEquals(e.getMessage(), "404");
        }



        //-------------------------------------------------------GET DOCTORS' DATA-------------------------------------------------------
        //same of the patient

        try {
            //getting doctor's data
            JSONObject json = associationsManager.getDoctorData("mario.bianchi");
            //checking if patient's data are those expected
            assertEquals(json.get("_id"), "mario.bianchi");
            assertEquals(json.get("name"), "Mario");
            assertEquals(json.get("surname"), "Bianchi");
            assertEquals(json.get("cf"), "ksjdsofòjdfk");
        } catch (Exception e) {
            Assert.fail();
        }

        //try getting data of an non-existent doctor, we want it to throw an exception: 404 (not found)
        try {
            JSONObject fail = associationsManager.getDoctorData("fail.fail");
            Assert.fail();
        } catch (Exception e) {
            assertEquals(e.getMessage(), "404");
        }



        //-------------------------------------------------------DELETE PATIENTS-------------------------------------------------------
        //deleting the patient created before
        assert(associationsManager.deletePatient("marghe.pecos"));

        //try deleting a patient the doesn't exist, we want it to fail
        assertFalse(associationsManager.deletePatient("doesnt.exist"));



        //-------------------------------------------------------DELETE DOCTORS-------------------------------------------------------
        //same of the patient

        // deleting the doctor created before
        assert(associationsManager.deleteDoctor("mario.bianchi"));

        //try deleting a doctor the doesn't exist, we want it to fail
        assertFalse(associationsManager.deleteDoctor("doesnt.exist"));



        //-------------------------------------------------------CREATE NEW ASSOCIATIONS-------------------------------------------------------
        //creating new patients and doctors
        associationsManager.createNewPatient("prova.pat", "Prova", "Pat", "ProvaPatCF");
        associationsManager.createNewPatient("prova.pat2", "Prova", "Pat2", "ProvaPat2CF");
        associationsManager.createNewDoctor("prova.doc", "Prova", "Doc", "ProvaDocCF");
        associationsManager.createNewDoctor("prova.doc2", "Prova", "Doc2", "ProvaDoc2CF");

        //try adding a new association between the patient and the doctor created before
        assert(associationsManager.createNewAssociation("prova.pat", "prova.doc"));

        //try adding the same association, we want it to success (it already exist, so it'is ok)
        assert(associationsManager.createNewAssociation("prova.pat", "prova.doc"));

        //try adding a new association between a patient and a doctor that don't exist, we want it to fail
        assertFalse(associationsManager.createNewAssociation("doesnt.exist", "doesnt.exist"));

        //try adding a new association between two patients that both exist, we want it to fail
        assertFalse(associationsManager.createNewAssociation("prova.pat", "prova.pat2"));

        //try adding a new association between two doctors that both exist, we want it to fail
        assertFalse(associationsManager.createNewAssociation("prova.doc2", "prova.doc"));

        //try adding new associations between patients and doctors just created
        assert(associationsManager.createNewAssociation("prova.pat", "prova.doc2"));
        assert(associationsManager.createNewAssociation("prova.pat2", "prova.doc"));
        assert(associationsManager.createNewAssociation("prova.pat2", "prova.doc2"));



        //-------------------------------------------------------GET ASSOCIATIONS-------------------------------------------------------
        JSONObject json = new JSONObject();

        //try getting the association (created before) between a specific patient and a specific doctor
        try {
            json = associationsManager.getAssociation("prova.pat", "prova.doc");
        } catch (Exception e) {
            Assert.fail(); //it shouldn't get here
        }
        //checking if patient and doctor are those expected
        assertEquals(json.get("idPatient"), "prova.pat");
        assertEquals(json.get("idDoctor"), "prova.doc");

        //try getting the association (created before) between a patient and a doctor that don't exist, we want it to fail
        try {
            json = associationsManager.getAssociation("doesnt.exist", "doesnt.exist");
            Assert.fail(); //it shouldn't get here
        } catch (Exception e) {
            assertEquals(e.getMessage(), "404");
        }

        //try getting the association (created before) between two patients that both exist, we want it to fail
        try {
            json = associationsManager.getAssociation("prova.pat", "prova.pat2");
            Assert.fail(); //it shouldn't get here
        } catch (Exception e) {
            assertEquals(e.getMessage(), "404");
        }

        //try getting the association (created before) between two doctors that both exist, we want it to fail
        try {
            json = associationsManager.getAssociation("prova.doc2", "prova.doc");
            Assert.fail(); //it shouldn't get here
        } catch (Exception e) {
            assertEquals(e.getMessage(), "404");
        }



        //-------------------------------------------------------GET PATIENTS' ASSOCIATIONS-------------------------------------------------------
        JSONArray patientAssociations;

        //try getting all doctors associated to the specific patient
        try {
            patientAssociations = associationsManager.getPatientAssociations("prova.pat");
            patientAssociations = associationsManager.getPatientAssociations("prova.pat2");
        } catch (Exception e) {
            Assert.fail();
        }

        //try getting all doctors associated to the patient that doesn't exist, we want it to fail
        try {
            patientAssociations = associationsManager.getPatientAssociations("doesnt.exist");
            Assert.fail();
        } catch (Exception e) {
            assertEquals(e.getMessage(), "404");
        }

        //try getting all doctors associated to a new patient that has no associations, we want it to return an empty JSONArray
        associationsManager.createNewPatient("prova.pat3", "Prova", "Pat3", "ProvaCF");
        try {
            patientAssociations = associationsManager.getPatientAssociations("prova.pat3");
            assertEquals(patientAssociations.length(), 0);
        } catch (Exception e) {
            Assert.fail();
        }



        //-------------------------------------------------------GET DOCTORS' ASSOCIATIONS-------------------------------------------------------
        //same of the patient

        JSONArray doctorAssociations;

        //try getting all patients associated to the specific doctor
        try {
            doctorAssociations = associationsManager.getDoctorAssociations("prova.doc");
            doctorAssociations = associationsManager.getDoctorAssociations("prova.doc2");
        } catch (Exception e) {
            Assert.fail();
        }

        //try getting all patients associated to the doctor that doesn't exist, we want it to fail
        try {
            doctorAssociations = associationsManager.getDoctorAssociations("doesnt.exist");
            Assert.fail();
        } catch (Exception e) {
            assertEquals(e.getMessage(), "404");
        }

        //try getting all patients associated to a new doctor that has no associations, we want it to return an empty JSONArray
        associationsManager.createNewDoctor("prova.doc3", "Prova", "Doc3", "ProvaCF");
        try {
            doctorAssociations = associationsManager.getDoctorAssociations("prova.doc3");
            assertEquals(doctorAssociations.length(), 0);
        } catch (Exception e) {
            Assert.fail();
        }



        //-------------------------------------------------------DELETE ASSOCIATIONS-------------------------------------------------------
        //try deleting all existing associations between specific patient and doctor
        assert(associationsManager.deleteAssociation("prova.pat", "prova.doc"));
        assert(associationsManager.deleteAssociation("prova.pat", "prova.doc2"));
        assert(associationsManager.deleteAssociation("prova.pat2", "prova.doc"));
        assert(associationsManager.deleteAssociation("prova.pat2", "prova.doc2"));

        //try deleting an association tha doesn't exist, we want it to fail
        assertFalse(associationsManager.deleteAssociation("prova.pat", "prova.doc3"));

        //try deleting an association between a patient and a doctor that don't exist, we want it to fail
        assertFalse(associationsManager.deleteAssociation("doesnt.exist", "doesnt.exist"));

        //deleting all patients and doctors created for testing
        associationsManager.deletePatient("prova.pat");
        associationsManager.deletePatient("prova.pat2");
        associationsManager.deletePatient("prova.pat3");
        associationsManager.deleteDoctor("prova.doc");
        associationsManager.deleteDoctor("prova.doc2");
        associationsManager.deleteDoctor("prova.doc3");
    }

}
