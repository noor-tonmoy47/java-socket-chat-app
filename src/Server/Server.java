package Server;

import Client.ClientHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public ServerSocket serverSocket;
    public Socket socket;
    ClientHandler clientHandler;
    Server(ServerSocket serverSocket){
        this.serverSocket =  serverSocket;
    }
    private void clientConnection(){
        try{
            while (!serverSocket.isClosed()){
                this.socket = serverSocket.accept();
                System.out.println("A new Client has connected");

                clientHandler = new ClientHandler(this.socket);

                Thread serverThread = new Thread(clientHandler);
                serverThread.start();
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    private void closeServerSocket(){
        if (serverSocket != null) {
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(5001);
            Server server = new Server(serverSocket);
            server.clientConnection();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
