package core;

import core.dbmanager.AssociationsManager;
import core.dbmanager.AssociationsManagerImpl;
import org.json.JSONException;

public class Main {

    public static void main(String[] args) throws Exception {
        AssociationsManager associationsManager = new AssociationsManagerImpl();
        //associationsManager.getPatientData("kksksksk.lucchi");
        associationsManager.getDoctorData("giulia.lucchi");
        //System.out.println(associationsManager.createNewDoctor("kdkdkdk.pecos", "marghe", "pecos", "kjdjdjd"));
    }

}
