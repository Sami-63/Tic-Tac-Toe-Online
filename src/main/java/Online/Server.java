/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Online;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

/**
 *
 * @author As-Sami
 */
public class Server {

    static HashMap<String, User> userList = new HashMap<>(); // list of all players
    static Queue<String> q = new LinkedList<>(); // waiting players

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(12345);
        System.out.println("Server started...");

        while (true) {
            Socket socket = serverSocket.accept();
            NetworkConnection nc = new NetworkConnection(socket);

            System.out.println("Found a user... ");
            new Thread(new GetUsername(nc)).start();
        }

    }

    public static class GetUsername implements Runnable {

        NetworkConnection nc;
        String name;

        public GetUsername(NetworkConnection nc) {
            this.nc = nc;
        }

        @Override
        public void run() {
            Object nameObj = nc.read();
            name = (String) nameObj;

            User user = new User(nc, name);
            userList.put(name, user);

            System.out.println("Got user #" + name);
            
            if (q.isEmpty()) {
                q.add(name);
            } else if (q.size() == 1) {
                String opponent = q.remove();
                user.setOpponent(opponent);
                userList.get(opponent).setOpponent(name);

                new Thread(new CreateConnection(name, opponent)).start();
            }
        }
    }

    public static class CreateConnection implements Runnable {

        User player1, player2;
        Random rand = new Random();
        
        public CreateConnection(String p1, String p2) {
            this.player1 = userList.get(p1);
            this.player2 = userList.get(p2);
        }

        @Override
        public void run() {
            boolean turn = rand.nextBoolean();
            
            if (turn) {

                Data data = new Data();
                data.msg = "1";
                player1.nc.write(data);

                data.msg = "0";
                player2.nc.write(data);
                                
                System.out.println("if block");
                
                for(int i=0 ; i<9 ; i++){
                    if(i%2==0){
                        System.out.println("Receiving data...." + player1.username);
                        data = (Data) player1.nc.read();
                        System.out.println("received data\n");
                        
                        System.out.println("Writing data to " + player2.username);
                        player2.nc.write(data);
                        System.out.println("Data sent.....");
                        
                    }else{
                        System.out.println("Receiving data from : " + player2.username );
                        data = (Data) player2.nc.read();
                        System.out.println("received data\n");
                        
                        System.out.println("Writing data to " + player1.username);
                        player1.nc.write(data);
                        System.out.println("Data sent.....");
                    }
                }

            } else {

                Data data = new Data();
                data.msg = "1";
                player2.nc.write(data);

                data.msg = "0";
                player1.nc.write(data);
                
                System.out.println("else block");
                for(int i=0 ; i<9 ; i++){
                    if(i%2==1){
                        System.out.println("Receiving data...." + player1.username);
                        data = (Data) player1.nc.read();
                        System.out.println("received data\n");
                        
                        System.out.println("Writing data to " + player2.username);
                        player2.nc.write(data);
                        System.out.println("Data sent.....");
                    }else{
                        System.out.println("Receiving data from : " + player2.username );
                        data = (Data) player2.nc.read();
                        System.out.println("received data\n");
                        
                        System.out.println("Writing data to " + player1.username);
                        player1.nc.write(data);
                        System.out.println("Data sent.....");
                    }
                }
            }
        }
    }
}