package core.pubsub;

import org.json.JSONObject;

/**
 * Created by lucch on 04/06/2018.
 */
public class provamerda {
    public static void main(String[] args) throws Exception {
        String message = "datacentre.receive.sensor" + "{\"patientId\": \"marghe.lucchi\",\n" +
                "\"dataType\": \"temperature\",\n" +
                "   \"value\": 123,\n" +
                "   \"unit\": \"dk\",\n" +
                "   \"timestamp\": \"25-12-1334 11:34\",\n" +
                "   \"output\": {\n" +
                "       \"level\": 0,\n" +
                "       \"description\": \"fghjkl\"\n" +
                "   }\n" +
                "}";

        //  System.out.println(message.split("datacentre.receive.sensor")[1]);
        // System.out.println(message.contains("datacentre.receive.sensor"));
        JSONObject chepalle = new JSONObject()
                .put("idPatient", "giulia.lucchi")
                .put("value", 12)
                .put("unit", "gradi")
                .put("timestamp", "457")
                .put("output", new JSONObject()
                                    .put("level", 0)
                                    .put("description", "sdfnm"));
        JSONObject json = new JSONObject().put("type", "temperature").put("message", chepalle);
        System.out.println((String) json.get("type"));
        JSONObject value = (JSONObject) json.get("message");
        System.out.println(value);
        String jsonToString = value.toString();
        String ok = jsonToString.substring(1, jsonToString.length() - 1).replace("}", "").replace("{", "");

        // System.out.println("patientId: "+ (String)value.get("patientId"));


    }
}
