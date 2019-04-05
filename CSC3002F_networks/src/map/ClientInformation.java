package map;

/**
 * Created by cppkus001 on 2017/04/06.
 */
public class ClientInformation {
    private String name;
    private String portNumber;

    public ClientInformation(String portNumber, String name){
        this.portNumber = portNumber;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPortNumber() {
        return portNumber;
    }

    public void setPortNumber(String portNumber) {
        this.portNumber = portNumber;
    }
}
