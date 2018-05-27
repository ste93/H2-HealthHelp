package core.dbmanager;

/**
 * Created by lucch on 27/05/2018.
 */
public class ManagerMain {


        public static void main(String[] args) throws Exception {
            H2dbManager dbManager = new H2dbManagerImpl();
           System.out.println(dbManager.getSensorsType("giulia.lucchi"));

    }

}
