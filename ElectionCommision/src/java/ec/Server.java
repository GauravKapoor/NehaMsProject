package ec;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.HashMap;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author bala
 */
public class Server extends Thread {

    public HashMap<String, String> mob_reg;
    public static DatagramSocket serverSocket;
    int listenport;
    String IDS = "webser123";
    String rand = "78945";
    String sphone;
    String loginid;
    String Rloginid;
    String serverip = "192.168.1.1"; //opass webserver ip 
    int serverport = 8500;//opass webserver port
    public static Server serv = null;
    public boolean flag = false;
    public boolean threadflag = false;
    public long regstarttime = 0;
    public long regendtime = 0;
    public long smstotaltime = 0;
    public int totaluser = 0;
    public long loginstarttime = 0;
    public long loginendtime = 0;
    public long logintotaltime = 0;
    public int totalloginuser = 0;

    public static Server getinstance() {
        if (serv == null) {
            serv = new Server();

        }
        return serv;
    }

    Server() {

        mob_reg = new HashMap<String, String>();


    }

    @Override
    public void run() {

        try {
            serverSocket = new DatagramSocket(serverport);
            //guiinst.writetolog("Web Server started...");
            String mob_idu = "";
            String mob_ip = "";
            String mob_cred = "";
            String mob_port = "";
            int mob_i_count = 0;
            String mob_dns = "";
            threadflag = true;
            while (true) {
                byte[] content = new byte[168];

                DatagramPacket recievepacket = new DatagramPacket(content, content.length);

                serverSocket.receive(recievepacket);

                InetAddress ipaddress = recievepacket.getAddress();

                int port = recievepacket.getPort();

                // data[]=recievepacket.getData().toString().split("#");


                byte b[] = recievepacket.getData();

                System.out.println(b + "   " + b.length);

                String ack = TripleDESTest.decrypt(b);

                System.out.println(ack);
                // guiinst.writetolog(ack);

                if (ack.length() > 0) {

                    String temp[] = ack.split("#");

                    if (temp[0].equals("REGISTER")) { //recieved packet from TSP
                        System.out.println("Registration in webserver" + ack);

                        String mno = temp[1];

                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();

        }


    }

    public String padding(String result) {
        String password = result + "#";

        String pad = "";
        System.out.println("password length=" + password.length());

        if (password.length() != 160) {

            System.out.println(password.length());
            for (int i = password.length(); i < 160; i++) {
                pad = pad + "X";
            }
        }
        password = password + pad;

        System.out.println("password length=" + password.length());

        return password.trim();
    }
}
