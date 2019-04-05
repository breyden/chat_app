# Makefile for Data Structures Binary Search Tree Assignments

RECEIVED = received_files
SRCDIR = src
BINDIR = bin

JAVAC = javac
JFLAGS =-g -d $(BINDIR) -cp $(BINDIR)


vpath %.java $(SRCDIR):$(SRCDIR)/thread:$(SRCDIR)/overidden:$(SRCDIR)/byte_array_size:$(SRCDIR)/map:$(SRCDIR)/driver
vpath %.class $(BINDIR)

#/byte_array_size:$(BINDIR)/thread:$(BINDIR)/overidden:$(BINDIR)/map:$(BINDIR)/driver

# define general build rule for java sources
.SUFFIXES:  .java  .class

.java.class:
	$(JAVAC)  $(JFLAGS)  $<

#default rule - will be invoked by make


all: ByteArraySize.class ServerSocketM.class\
	MessageSenderToServerForClient.class MessageReaderFromServerForClient.class Client.class\
	FileForwarder.class MessageForwarder.class MessageReaderForServer.class Server.class


# The following two targets deal with the mutual dependency between Position and BinaryTree
# Position.class: BinaryTree.class

# BinaryTree.class:
#	rm -rf $(BINDIR)/binarytree/Position.class $(BINDIR)/binarytree/BinaryTree.class
#	javac $(JFLAGS) $(SRCDIR)/binarytree/Position.java $(SRCDIR)/binarytree/BinaryTree.java



clean:
	@rm -f  $(BINDIR)/*.class
	@rm -f $(BINDIR)/*/*.class
	@rm -f $(RECEIVED)/*/*
