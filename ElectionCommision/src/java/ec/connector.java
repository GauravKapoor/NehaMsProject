/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ec;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author prathap
 */
public class connector extends Thread {

    public static connector cnt;
    DatagramSocket serversocket;
    private int hand =0;
     private int lotus =0;
      private int aeroplane =0;
       private int elephant =0;

    public static connector getinstance() {

        if (cnt == null) {

            cnt = new connector();
        }
        return cnt;
    }

    connector() {
        try {
            serversocket = new DatagramSocket(6000);


            start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void run() {

        try {
            System.out.println("Server Thread started");
            while (true) {
                byte[] recievedata = new byte[208];

                DatagramPacket dp = new DatagramPacket(recievedata, recievedata.length);

                serversocket.receive(dp);

                System.out.println("Recieved packet from client");

                byte b[]=dp.getData();
                
                String udpdata = TripleDESTest.decrypt(b);
                
                //String udpdata = new String(dp.getData());
                System.out.println("clent data" + udpdata);
                String tempdata[] = udpdata.trim().split("#");

                if (tempdata[0].equals("START")) {  //START#12345

                    Database db = new Database();

                    String query = " select * from register where encryptid='" + tempdata[1] + "'";

                    ResultSet rs = db.executeQuery(query);
                    System.out.println(tempdata[0]);
                    if (rs.next()) {
                        System.out.println("inside ");
                        int portno = dp.getPort();

                        InetAddress ip = dp.getAddress();

                        String ipaddr = ip.getHostAddress();

                        query = "insert into userkey values ('" + tempdata[1] + "','" + ipaddr + "','" + portno + "')";

                        db.executeUpdate(query);

                        System.out.println("Updated to database");
                    } else {

                        String reply = "INVALID#Secret key Invalid/Please Register to Election Commision";
                        
                        byte[] data = reply.getBytes();

                        int portno = dp.getPort();

                        InetAddress ip = dp.getAddress();

                        String ipaddress = ip.getHostAddress();
                        InetAddress ipaddr = InetAddress.getByName(ipaddress);

                        DatagramPacket serverpacket = new DatagramPacket(data, data.length, ipaddr, portno);
                        serversocket.send(serverpacket);
                        System.out.println(" Invalid message sent");
                    }


                }else if(tempdata[0].equals("VOTE")) { 
                    String symbol= tempdata[1];
                    
                    Database db= new Database();
                    
                    
                    
                    if(symbol.equals("HAND")){
                        hand++;
                        
                        String query = "update contender set votecount='"+hand+"' where psymbol='"+symbol+"'";
                        db.executeUpdate(query);
                       
                    }else if(symbol.equals("LOTUS")){
                        lotus++;
                        String query = "update contender set votecount='"+lotus+"' where psymbol='"+symbol+"'";
                        db.executeUpdate(query);
                        
                    }else if(symbol.equals("ELEPHANT")){
                        elephant++;
                        
                        String query = "update contender set votecount='"+elephant+"' where psymbol='"+symbol+"'";
                        db.executeUpdate(query);
                        
                    }else if(symbol.equals("AEROPLANE")){
                        aeroplane++;
                           String query = "update contender set votecount='"+aeroplane+"' where psymbol='"+symbol+"'";
                        db.executeUpdate(query);
                    }
                    
                    
                }


            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void sendmessage(String message) {

        try {
            
            Database db = new Database();

            String query = "select * from userkey";

            ResultSet rs = db.executeQuery(query);

            while (rs.next()) {

                String ipadr = rs.getString("ipaddr");
                int portno = Integer.parseInt(rs.getString("port"));
                
                String secretkeyid=rs.getString("secretkey");
                query = "select * from register where encryptid='"+secretkeyid+"'";

                rs = db.executeQuery(query);
                String secretkey ="";
                if(rs.next()){
                    secretkey=rs.getString("encryptdata");
                }
                
                message+="@"+secretkey.trim();
                
                System.out.println("Message = "+message);
                
                String pad_res= TripleDESTest.padding(message);
                
                byte[] data = TripleDESTest.encrypt(pad_res);//message.getBytes();

                InetAddress ipaddr = InetAddress.getByName(ipadr);

                DatagramPacket pkt = new DatagramPacket(data, data.length, ipaddr, portno);
                serversocket.send(pkt);
                System.out.println(" message sent");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
