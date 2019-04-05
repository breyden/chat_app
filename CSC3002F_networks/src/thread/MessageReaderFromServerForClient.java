package thread;

import byte_array_size.ByteArraySize;

import java.io.*;
import java.net.Socket;

/**
 * Created by Arjun on 4/5/2017.
 */
public class MessageReaderFromServerForClient extends Thread {
    private BufferedReader bufferedReader;
    private Socket socket;
    private volatile boolean interrupted = false;

    public MessageReaderFromServerForClient(Socket socket){
        this.socket = socket; // client socket
    }

    public void run(){
        while(!interrupted && !socket.isClosed()) {

            try {
                System.out.println("(Waiting for messages or Type in a message to send)");
                InputStream inputStream = socket.getInputStream();
                this.bufferedReader = new BufferedReader(new InputStreamReader(inputStream)); // gets messages from server

                String message = bufferedReader.readLine(); // reads the messages

                if (message == null){
                    System.out.println("Server Went Offline, Please Connect Later!");
                    this.interrupt();
                    System.exit(0);

                } else {

                    if (message.equalsIgnoreCase("@receivefilerequest:")) {
                        // read file from output stream and save
                        File file = new File(System.getProperty("user.dir") + "/received_files/client/receiver_is_" + socket.getPort() + ".png");
                        FileOutputStream fileOutputStream = new FileOutputStream(file);
                        byte[] bytes = new byte[ByteArraySize.ByteArraySize];
                        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);

                        System.out.println("(Receiving File)");

                        int read; // inputStream was previously BuferedInputStream

                        while ((read = inputStream.read(bytes)) > 0) {
                            fileOutputStream.write(bytes, 0, read);
                            //System.out.println("Read: " + read);
                            if(read<ByteArraySize.ByteArraySize){
                                break;
                            }
                        }


                        System.out.println("(File Received)");
                    } else {
                        System.out.println(message); // print messages from other clients
                    }
                }
            } catch (java.net.SocketException e1) {
                this.interrupt(); // socket must have been closed and hence an interruption is needed
                //e1.printStackTrace();
            } catch (Exception e2) {
                //e2.printStackTrace();
            }
        }
        //System.exit(0);
    }

    public void interrupt(){
        this.interrupted = true;
        try {
            bufferedReader.close();
        } catch (Exception e){

        }
    }
}
