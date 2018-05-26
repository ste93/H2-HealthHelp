package core;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

/**
 * Created by lucch on 23/05/2018.
 */
public class provamain {
    public static void main(String[] args){

        //create a istance of client
        Client client = ClientBuilder.newClient();
        //Using WebTarget, we can define a path to a specific resource
        String relationship = client.target("http://localhost:3000/database/association/relationship")
                                            .queryParam("idPatient", "giulia.lucchi")
                .request()
                .get(String.class);


        System.out.println(relationship);



    }
}
