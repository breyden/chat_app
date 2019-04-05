package thread;

import byte_array_size.ByteArraySize;
import overidden.ServerSocketM;

import java.io.*;
import java.net.Socket;

/**
 * Created by Arjun on 4/5/2017.
 */
public class MessageReaderForServer extends Thread {
    private int senderIndex;
    private ServerSocketM[] serverSockets;
    private Socket socket;
    private volatile boolean interrupted = false;
    private BufferedReader fromClient;
    private File file;

    public MessageReaderForServer(ServerSocketM[] serverSockets, int senderIndex){
        this.senderIndex = senderIndex;
        this.serverSockets = serverSockets;
        try{
            socket = serverSockets[senderIndex].accept(); // accepts the connection from a client(socket)
            System.out.println("Port " + socket.getLocalPort() + ": Joined the chat.");
            new MessageForwarder(serverSockets, socket, senderIndex, "From Server: " + socket.getLocalPort() + " has joined the chat.").start();
        } catch (Exception e){
            //e.printStackTrace();
            System.exit(11);
        }
    }

    public void run(){
        String message = "";
        while(!interrupted && !serverSockets[senderIndex].getAcceptedSocket().isClosed()){
            try{

                InputStream inputStream = socket.getInputStream();
                fromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                message = fromClient.readLine(); // read the message of the client

                if(message != null) {
                    if(message.toLowerCase().contains("@receivefilerequest:")){

                        System.out.println("Port " + socket.getLocalPort() + ": Made a request to send a file");
                        String path = System.getProperty("user.dir") + "/received_files/server/FileFrom" +  socket.getLocalPort() + ".png";

                        file = new File(path);
                        FileOutputStream fileOutputStream = new FileOutputStream(file);
                        byte[] bytes = new byte[ByteArraySize.ByteArraySize];
                        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);

                        System.out.println("(Receiving File)");

                        int read; // inputStream was previously BuferedInputStream

                        while ((read = inputStream.read(bytes)) > 0) {
                            fileOutputStream.write(bytes, 0, read);
                            // System.out.println("Read: " + read);
                            if(read<ByteArraySize.ByteArraySize){
                                break;
                            }
                        }

                        System.out.println("(File Received)");
                        for (int j = 0; j < serverSockets.length; j++) {
                            if(j != senderIndex && serverSockets[j].isAccepted() && serverSockets[j].canAcceptFile()){
                                System.out.println("(File from " + socket.getLocalPort() + " has been queued for " + serverSockets[j].getLocalPort() +" )");
                                serverSockets[j].queueFile(path);
                            }
                        }
                        new MessageForwarder(serverSockets, socket, senderIndex, ("Message From " + socket.getLocalPort()) + " : Please accept a file from me. Type '@AcceptFile' or '@RejectFile'").start();


                    } else if(message.trim().toLowerCase().contains("@private")){
                        try{
                            String receiverPort = message.substring(8, 12);
                            String messageToBeSent = message.substring(14, message.length());
                            for(int j = 0; j < serverSockets.length; j++){
                                if(serverSockets[j].isAccepted() && serverSockets[j].getAcceptedSocket().getLocalPort() == Integer.parseInt(receiverPort.trim())){
                                    Socket sck = serverSockets[j].getAcceptedSocket();
                                    System.out.println("(Sending private message to " + sck.getLocalPort()  + ")");
                                    PrintWriter pr = new PrintWriter(sck.getOutputStream());
                                    pr.write(messageToBeSent + "\n");
                                    pr.flush();
                                }
                            }
                        } catch(Exception x){
                            //x.printStackTrace();
                        }
                    } else if(message.trim().toLowerCase().contains("@filereceived")){

                    } else if (message.trim().toLowerCase().contains("@acceptfile")){
                        if(serverSockets[senderIndex].isFileWaitingToBeSent()) {
                            System.out.println("Port " + socket.getLocalPort() + " has accepted file " + serverSockets[senderIndex].getFilePath());
                            FileForwarder f = new FileForwarder(serverSockets[senderIndex]); // senderIndex = the one who accepted the file
                            f.start();
                            f.join();
                        }
                    } else if (message.trim().toLowerCase().contains("@rejectfile")){
                        if(serverSockets[senderIndex].isFileWaitingToBeSent()) {
                            System.out.println("Port " + socket.getLocalPort() + " has rejected file " + serverSockets[senderIndex].getFilePath());
                            serverSockets[senderIndex].fileSent();
                        } else {

                        }
                    } else {
                        new MessageForwarder(serverSockets, socket, senderIndex, ("Message From " + socket.getLocalPort() + " : " + message)).start();
                    }
                } else {
                    this.interrupt();
                    new MessageForwarder(serverSockets, socket, senderIndex, ("From Server: Port " + socket.getLocalPort() + " has left the chat.")).start();
                    serverSockets[senderIndex].goOffline();
                }


            } catch (Exception e){
                //e.printStackTrace();
                this.interrupt();
            }
        }
    }

    public void interrupt(){
        this.interrupted = true;
        try{
            fromClient.close();
            //socket.close();
            serverSockets[senderIndex].getAcceptedSocket().close();
            serverSockets[senderIndex].close();
        } catch (Exception e){
            //e.printStackTrace();
        }
    }
}
