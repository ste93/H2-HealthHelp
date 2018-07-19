package core.pubsub;

public enum IpAndPort {
    HOST_IP_AND_PORT("192.168.43.120", "5672");

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
