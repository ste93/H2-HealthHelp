package core;

/**Includes
 *
 * @author Giulia Lucchi
 */
public enum SensorType {
    TEMPERATURE("temperature"),
    PRESSURE("pressure"),
    GLYCEMIA("glycemia"),
    HEART_RATE("heart_rate");

    private String type;

    SensorType(final String type){
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
