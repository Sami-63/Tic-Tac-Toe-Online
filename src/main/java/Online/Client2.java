/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Online;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

/**
 *
 * @author As-Sami
 */
public class Client2 {

    public static void main(String[] args) throws IOException {

        Socket socket = new Socket("192.168.1.12", 12345);
        NetworkConnection nc = new NetworkConnection(socket);
        System.out.println("Network connected");

        System.out.print("Enter username : ");
        Scanner sc = new Scanner(System.in);
        String username = sc.next();
        nc.write(username);

        System.out.println("\nSearching for players...");

        Data data = (Data) nc.read();
        String msg = data.msg;
        System.out.println(msg);

        char game[] = new char[9];
        for (int i = 0; i < 9; i++) {
            game[i] = '.';
        }printGame(game);

        int turn = -1;
        if(msg.equals("1")){
            System.out.println("You got the first move");
            turn = 0;
        }else{
            System.out.println("Opponent got the first move");
            turn = 1;
        }
        System.out.println("turn = " + turn);

        for(int i=0 ; i<9 ; i++){
            if(i%2==turn){
                String s;
                int n;
                do{
                    s = sc.next();
                    n = Integer.parseInt(s);
                }while( game[n]!='.' );

                game[n] = 'X';
                printGame(game);
                Data d1 = new Data();
                d1.msg = s;
                nc.write(d1);

                int res = check(game);
                if(res==1){
                    System.out.println("You have won....");
                    break;
                }else if(res==0){
                    System.out.println("You lose....");
                    break;
                }

            }else{
                Data d1 = (Data) nc.read();
                String s = d1.msg;
                int n = Integer.parseInt(s);

                game[n] = 'O';
                printGame(game);
                
                int res = check(game);
                if(res==1){
                    System.out.println("You have won....");
                    break;
                }else if(res==0){
                    System.out.println("You lose....");
                    break;
                }
            }
        }
        socket.close();
    }

    static void printGame(char game[]){
        for (int i = 0; i < 9; i++) {
            System.out.print(game[i]);
            if ((i + 1) % 3 == 0) {
                System.out.print("\n");
            }
        }
    }

    static int check(char game[]) {

        if ((game[0] == 'X' && game[1] == 'X' && game[2] == 'X')
                || (game[3] == 'X' && game[4] == 'X' && game[5] == 'X')
                || (game[6] == 'X' && game[7] == 'X' && game[8] == 'X')
                || (game[0] == 'X' && game[3] == 'X' && game[6] == 'X')
                || (game[1] == 'X' && game[4] == 'X' && game[7] == 'X')
                || (game[2] == 'X' && game[5] == 'X' && game[8] == 'X')
                || (game[0] == 'X' && game[4] == 'X' && game[8] == 'X')
                || (game[6] == 'X' && game[4] == 'X' && game[2] == 'X')
                || (game[0] == 'X' && game[2] == 'X' && game[4] == 'X' && game[6] == 'X' && game[8] == 'X')) {
            return 1;
        }

        if ((game[0] == 'O' && game[1] == 'O' && game[2] == 'O')
                || (game[3] == 'O' && game[4] == 'O' && game[5] == 'O')
                || (game[6] == 'O' && game[7] == 'O' && game[8] == 'O')
                || (game[0] == 'O' && game[3] == 'O' && game[6] == 'O')
                || (game[1] == 'O' && game[4] == 'O' && game[7] == 'O')
                || (game[2] == 'O' && game[5] == 'O' && game[8] == 'O')
                || (game[0] == 'O' && game[4] == 'O' && game[8] == 'O')
                || (game[6] == 'O' && game[4] == 'O' && game[2] == 'O')
                || (game[0] == 'O' && game[2] == 'O' && game[4] == 'O' && game[6] == 'O' && game[8] == 'O')) {
            return 0;
        }

        return -1;
    }
}
