package core.dbmanager;

import static org.junit.Assert.*;

import core.dbmanager.associations.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.*;
import java.lang.Exception;

public class AssociationAndUserManagerTest {

    private AssociationsManager associationsManager = new AssociationsManagerImpl();
    private PatientManager patientManager = new PatientManagerImpl();
    private DoctorManager doctorManager = new DoctorManagerImpl();

    @Test
    public void test() throws Exception {
        //-------------------------------------------------------CREATE NEW PATIENTS-------------------------------------------------------

        //try adding a new patient, we want it to success
        assert(patientManager.createNewUser("marghe.pecos", "Margherita", "Pecorelli", "jskdlkdfjdjfi"));

        //try adding the same patient, we want it to fail
        assertFalse(patientManager.createNewUser("marghe.pecos", "Margherita", "Pecorelli", "jskdlkdfjdjfi"));

        //try adding a different patient but with the same ID, we want it to fail
        assertFalse(patientManager.createNewUser("marghe.pecos", "Marghe", "Peco", "alsllsalallal"));

        //try adding a new patient but with missing credential, we want it to fail
        assertFalse(patientManager.createNewUser("sara.rossi", "Sara", "Rossi", ""));
        assertFalse(patientManager.createNewUser("sara.rossi", "", "Rossi", "alslallksf"));
        assertFalse(patientManager.createNewUser("", "Sara", "Rossi", "alslallksf"));



        //-------------------------------------------------------CREATE NEW DOCOTRS-------------------------------------------------------
        //same of the patient

        //try adding a new doctor, we want it to success
        assert(doctorManager.createNewUser("mario.bianchi", "Mario", "Bianchi", "ksjdsofòjdfk"));

        //try adding the same doctor, we want it to fail
        assertFalse(doctorManager.createNewUser("mario.bianchi", "Mario", "Bianchi", "ksjdsofòjdfk"));

        //try adding a different doctor but with the same ID, we want it to fail
        assertFalse(doctorManager.createNewUser("mario.bianchi", "Ma", "Bianchinini", "laldaslaskdjask"));

        //try adding a new doctor but with missing credential, we want it to fail
        assertFalse(doctorManager.createNewUser("matteo.rosa", "Matteo", "Rosa", ""));
        assertFalse(doctorManager.createNewUser("matteo.rosa", "", "Rosa", "qpqpwoeqowoepw"));
        assertFalse(doctorManager.createNewUser("", "Matteo", "Rosa", "qpqpwoeqowoepw"));



        //-------------------------------------------------------GET PATIENTS' DATA-------------------------------------------------------
        try {
            //getting patient's data
            JSONObject json = patientManager.getUserData("marghe.pecos");

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
            JSONObject fail = patientManager.getUserData("fail.fail");
            Assert.fail();
        } catch (Exception e) {
            assertEquals(e.getMessage(), "404");
        }



        //-------------------------------------------------------GET DOCTORS' DATA-------------------------------------------------------
        //same of the patient

        try {
            //getting doctor's data
            JSONObject json = doctorManager.getUserData("mario.bianchi");

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
            JSONObject fail = doctorManager.getUserData("fail.fail");
            Assert.fail();
        } catch (Exception e) {
            assertEquals(e.getMessage(), "404");
        }



        //-------------------------------------------------------DELETE PATIENTS-------------------------------------------------------
        //deleting the patient created before
        assert(patientManager.deleteUser("marghe.pecos"));

        //try deleting a patient the doesn't exist, we want it to fail
        assertFalse(patientManager.deleteUser("doesnt.exist"));



        //-------------------------------------------------------DELETE DOCTORS-------------------------------------------------------
        //same of the patient

        // deleting the doctor created before
        assert(doctorManager.deleteUser("mario.bianchi"));

        //try deleting a doctor the doesn't exist, we want it to fail
        assertFalse(doctorManager.deleteUser("doesnt.exist"));



        //-------------------------------------------------------CREATE NEW ASSOCIATIONS-------------------------------------------------------
        //creating new patients and doctors
        patientManager.createNewUser("prova.pat", "Prova", "Pat", "ProvaPatCF");
        patientManager.createNewUser("prova.pat2", "Prova", "Pat2", "ProvaPat2CF");
        doctorManager.createNewUser("prova.doc", "Prova", "Doc", "ProvaDocCF");
        doctorManager.createNewUser("prova.doc2", "Prova", "Doc2", "ProvaDoc2CF");

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

        //try getting associations (created before) between a specific patient and a specific doctor
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
        JSONArray patientAssociations1 = null;
        JSONArray patientAssociations2 = null;

        //try getting all doctors associated to the specific patient
        try {
            patientAssociations1 = patientManager.getPatientAssociations("prova.pat");
            patientAssociations2 = patientManager.getPatientAssociations("prova.pat2");
        } catch (Exception e) {
            Assert.fail();
        }

        //try getting all doctors associated to the patient that doesn't exist, we want it to fail
        try {
            patientAssociations1 = patientManager.getPatientAssociations("doesnt.exist");
            Assert.fail();
        } catch (Exception e) {
            assertEquals(e.getMessage(), "404");
        }

        //try getting all doctors associated to a new patient that has no associations, we want it to return an empty JSONArray
        patientManager.createNewUser("prova.pat3", "Prova", "Pat3", "ProvaCF");
        try {
            patientAssociations1 = patientManager.getPatientAssociations("prova.pat3");
            assertEquals(patientAssociations1.length(), 0);
        } catch (Exception e) {
            Assert.fail();
        }



        //-------------------------------------------------------GET DOCTORS' ASSOCIATIONS-------------------------------------------------------
        //same of the patient
        JSONArray doctorAssociations1 = null;
        JSONArray doctorAssociations2 = null;

        //try getting all patients associated to the specific doctor
        try {
            doctorAssociations1 = doctorManager.getDoctorAssociations("prova.doc");
            doctorAssociations2 = doctorManager.getDoctorAssociations("prova.doc2");
        } catch (Exception e) {
            Assert.fail();
        }

        //try getting all patients associated to the doctor that doesn't exist, we want it to fail
        try {
            doctorAssociations1 = doctorManager.getDoctorAssociations("doesnt.exist");
            Assert.fail();
        } catch (Exception e) {
            assertEquals(e.getMessage(), "404");
        }

        //try getting all patients associated to a new doctor that has no associations, we want it to return an empty JSONArray
        doctorManager.createNewUser("prova.doc3", "Prova", "Doc3", "ProvaCF");
        try {
            doctorAssociations1 = doctorManager.getDoctorAssociations("prova.doc3");
            assertEquals(doctorAssociations1.length(), 0);
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
        patientManager.deleteUser("prova.pat");
        patientManager.deleteUser("prova.pat2");
        patientManager.deleteUser("prova.pat3");
        doctorManager.deleteUser("prova.doc");
        doctorManager.deleteUser("prova.doc2");
        doctorManager.deleteUser("prova.doc3");
    }

}
