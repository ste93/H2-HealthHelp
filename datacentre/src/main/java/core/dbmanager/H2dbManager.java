package core.dbmanager;

import core.SensorType;
import core.UserRole;

import javax.json.Json;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

/**
 * Created by lucch on 26/05/2018.
 */
public interface H2dbManager {

    boolean registration(final String idCode, final String password, final String name, final String surname, final String cf, final ArrayList<String> phones, final String mail, final UserRole role);//builder

    boolean login(final String idCode, final String password, final UserRole role);


    ArrayList<String> getSensorsType(final String idPatient);

    boolean addNewSensorType(final String idPatient, final SensorType sensorType);

    boolean addSensorValue(final String idPatient, final SensorType sensorType, final Json message);

    boolean deleteValues(final String idPatient, final SensorType sensorType, final Optional<Date> start, final Optional<Date> end);

    boolean getValues(final String idPatient, final SensorType sensorType, final Optional<Date> start, final Optional<Date> end);


    ArrayList<Json> getAdvices(final String idPatient); // oggetto con classe advice da creare
    boolean addAdvice(final Json message);


    ArrayList<Json> getDrugs(final String idPatient); //oggetto con classe drug da creare
    boolean addDrug(final String idPatient, final Json message);


}
