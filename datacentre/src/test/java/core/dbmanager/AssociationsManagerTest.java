package core.dbmanager;

import static org.junit.Assert.*;
import org.junit.Test;

public class AssociationsManagerTest {

    private AssociationsManager associationsManager = new AssociationsManagerImpl();

    @Test
    public void createNewPatient() {
        associationsManager.createNewPatient("marghe.pecos", "Margherita", "Pecorelli", "jskdlkdfjdjfi");

    }

    @Test
    public void getPatientData() {
        try {
            associationsManager.getPatientData("marghe.pecos");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
