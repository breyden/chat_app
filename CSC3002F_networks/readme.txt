Information about directories:
    1) bin: stores all the compiled files for the .java files
    2) received_files: stores all the files that the server and the clients receive
    3) src: stores the source code, i.e, the .java files

How to run the app?
    1) navigate to the CSC3002F_networks directory
    2) open a terminal from there
    3) run the makefile to compile the classes
    4) navigate to the CSC3002F/bin directory
    5) type "java driver/Server" to run the server - the number of participants and their port numbers will be asked
          before the server actually starts
    6) type "java driver/Client" to run an instance of the client - the port number of the client will be asked before connecting to the server,
          therefore, the server must have started first for the client to be able to connect to the server
    7) the client can start typing messages or type '@help' to get help

How does a client send a file?
    1) run the app as listed above
    2) type '@sendfile: <full_path_to_the_file>' and press Enter

How to test the file sending functionality?
    1) run the app as listed above
    2) type '@sendfile:', the dog.png file in the src/files/ directory will be sent to the server
          the server will store the file in the directory received_files/server/
    3) the other clients (that is, all the clients except the one sending the file) will get a message requesting them to either
          accept or reject the file
    4) Only after a client has accepted the file, then he will receive it. The file will be stored in the directory received_ffiles/client/

Makefile
    The makefile has 2 targets: make all and make clean
    make clean: this will remove all the files received by the server and the clients
    make all: this will compile all source code and stores the compiled files in the bin directory
