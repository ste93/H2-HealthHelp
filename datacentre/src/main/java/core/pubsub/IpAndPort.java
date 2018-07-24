package core.pubsub;

public enum IpAndPort {
    HOST_IP_AND_PORT("2.34.233.198", "8088");

    private String ip;
    private String port;

    IpAndPort(final String ip, final String port){
        this.ip = ip;
        this.port = port;
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return Integer.parseInt(port);
    }

}
