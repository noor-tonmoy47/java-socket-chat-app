package Client;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable{
    private Socket socket;
    private static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;

    private String clientUserName;
    public ClientHandler(Socket socket){
        this.socket = socket;
        try {
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.clientUserName = bufferedReader.readLine();
            clientHandlers.add(this);

            broadCastMessage("Server: " + clientUserName + " has entered the chat!");
        } catch (IOException e) {
            closeEverything(this.socket, this.bufferedReader, this.bufferedWriter);
        }
    }
    @Override
    public void run() {
        String messageFromClient;

        while(socket.isConnected()){
            try{
                messageFromClient = bufferedReader.readLine();
                broadCastMessage(messageFromClient);
            } catch(IOException e){
                closeEverything(this.socket, this.bufferedReader, this.bufferedWriter);
                break;
            }
        }
    }

    public void broadCastMessage(String message){
        for (ClientHandler clientHandler: clientHandlers){
            try {
                if (!clientHandler.clientUserName.equals(this.clientUserName)){
                    clientHandler.bufferedWriter.write(message);
                    clientHandler.bufferedWriter.newLine();
                    clientHandler.bufferedWriter.flush();
                }
            } catch (IOException e){
                closeEverything(this.socket, this.bufferedReader, this.bufferedWriter);
            }


        }
    }

    public void removeClientHandler(){
        clientHandlers.remove(this);
        broadCastMessage("SERVER: " + clientUserName + " has left the chat!");
    }

    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter){
        removeClientHandler();
        try{
            if(bufferedReader != null){
                bufferedReader.close();
            }
            if(bufferedWriter != null){
                bufferedWriter.close();
            }
            if(socket != null){
                socket.close();
            }

        } catch(IOException e){
            e.printStackTrace();
        }
    }
}

