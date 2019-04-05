package overidden;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Arjun on 4/5/2017.
 */
public class ServerSocketM  extends ServerSocket {
    private Socket socket;
    private boolean accepted = false;
    private String lock = "";
    private String filePath;
    private boolean fileAccepted;
    private int fileCount;
    private boolean fileWaitingToBeSent = false;
    private boolean isConnected = false;

    public ServerSocketM(int portNumber) throws IOException {
        super(portNumber);
        this.filePath = null;
        this.fileAccepted = false;
    }

    @Override
    public Socket accept() throws IOException{
        socket = super.accept();
        isConnected = true;
        accepted = true;
        return socket;
    }

    public boolean isAccepted(){
        return accepted;
    }

    public Socket getAcceptedSocket(){
        return (accepted? socket:null);
    }

    public String getLock(){
        return lock;
    }

    public void queueFile(String path){
        if(!path.trim().equalsIgnoreCase("")){
            filePath = path;
            fileWaitingToBeSent = true;
        }
    }

    public boolean canAcceptFile(){
        return (filePath==null);
    }

    public boolean isFileWaitingToBeSent(){
        return fileWaitingToBeSent;
    }

    public String getFilePath(){
        return filePath;
    }

    public void fileSent(){
        // todo write code to send files in the string array
        filePath = null;
        fileWaitingToBeSent = false;
    }

    public void goOffline(){
        this.isConnected = false;
    }

    public boolean isConnected(){
        return this.isConnected;
    }

}

