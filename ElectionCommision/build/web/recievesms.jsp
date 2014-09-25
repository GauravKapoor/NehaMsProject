<%-- 
    Document   : recievesms
    Created on : Feb 13, 2013, 11:47:57 AM
    Author     : prathap
--%>

<%@page import="java.util.Calendar"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.io.InputStream"%>
<%@page import="java.net.URLConnection"%>
<%@page import="java.net.URLEncoder"%>
<%@page import="java.net.URL"%>
<%@page import="java.net.HttpURLConnection"%>
<%@page import="ec.common"%>
<%@page import="java.io.PrintWriter"%>
<%@page import="ec.Database"%>
<%@page import="java.util.Random"%>
<%@page import="ec.TripleDESTest"%>
<%
    try {
        PrintWriter out1 = response.getWriter();

        common cm = common.getinstance();

        cm.setresponse(out1);

        PrintWriter pw = cm.getresponse();

        String Device = request.getParameter("device");

        String phoneno = request.getParameter("phone");

        String SMScenter = request.getParameter("smscenter");

        String Text = request.getParameter("text");


        System.out.println("Device=" + Device + " Phone=" + phoneno + " SMScenter=" + SMScenter + " Text=" + Text);

        Database dab = new Database();

        String query1 = " Select * from register where mobileno='" + phoneno + "'";

        ResultSet rest = dab.executeQuery(query1);
        String originalmsg = "";
        if (rest.next()) {
            String secretkey = rest.getString("encryptid");
            System.out.println("Secretkey:"+secretkey.trim()+" phone no:"+ phoneno);
            byte[] msgcontent = TripleDESTest.strtobyte(Text);
            originalmsg = TripleDESTest.redecrypt(msgcontent, "5869");
            System.out.println("Re originalmsg:"+originalmsg);

        } else {
            byte[] msgcontent = TripleDESTest.strtobyte(Text);
            originalmsg = TripleDESTest.decrypt(msgcontent);
             System.out.println("originalmsg:"+originalmsg);
        }


        if (originalmsg.startsWith("REGISTER")) {
            int idx = originalmsg.indexOf("#");
            String origcontent = originalmsg.substring(idx + 1);
            String voterdet[] = origcontent.split("#");

            int i = 0;
            while (i < voterdet.length) {

                String vdet[] = voterdet[i].split(":");
                System.out.println("Parameter-" + vdet[0]);
                System.out.println("Value - " + vdet[1]);
                i++;
            }



            String temp = phoneno + originalmsg;

            Database db = new Database();

            //String query = " insert into register values('" + Text + "','" + Sender + "')";

            //db.executeUpdate(query);

            byte[] encrypted = TripleDESTest.encrypt(temp);

            System.out.println("Encrypted data=" + encrypted);

            Random rd = new Random();

            //int random = rd.nextInt(100000);
            int random = 5869;
            String str = TripleDESTest.bytetostr(encrypted);

            String data = Integer.toString(random);

            String query = " insert into register values('" + origcontent + "','" + phoneno + "','" + data + "','" + str + "')";

            db.executeUpdate(query);
            String textforency = "secretkey#" + data;
            byte[] encrypt = TripleDESTest.encrypt(textforency);
            String enmsg = TripleDESTest.bytetostr(encrypt);
            String url = "http://192.168.1.9:9090/sendsms";
            try{
                
                String charset = "UTF-8";
                String param1 = phoneno;
                String param2 = enmsg;
                String param3 = "";
// ...
                String querys = String.format("phone=%s&text=%s&password=%s",
                URLEncoder.encode(param1, charset),
                URLEncoder.encode(param2, charset),
                URLEncoder.encode(param3, charset));
                URLConnection connection = new URL(url + "?" + querys).openConnection();
                connection.setRequestProperty("Accept-Charset", charset);
                InputStream resp = connection.getInputStream();
            }
            catch (Exception e){
                System.out.println("error while connecting to http URL. URL was - " + url);
                        
                e.printStackTrace();
            }
        } else if (originalmsg.startsWith("VERIFICATION")) {
            System.out.println("originalmsg:"+originalmsg);
            int idx = originalmsg.indexOf("#");
            String origcontent = originalmsg.substring(idx + 1);
            String voterdet[] = origcontent.split("#");

            int i = 0;
            while (i < voterdet.length) {

                String vdet[] = voterdet[i].split(":");
                System.out.println("Parameter-" + vdet[0]);
                System.out.println("Value - " + vdet[1]);
                i++;
            }

            String uid[] = voterdet[0].split(":");
            String votp[] = voterdet[3].split(":");

            String myquery = " Select * from otptab where uid='1234' and otp='123'";

            ResultSet myrst = dab.executeQuery(myquery);

            if (myrst.next()) {
                Random rnd = new Random();

                int prnkey = rnd.nextInt(1000000);

                Calendar cal = Calendar.getInstance();

                long st = cal.getTimeInMillis();

                String startt = Long.toString(st);

                String prnkeyst = Integer.toString(prnkey);

                myquery = "insert into prnkeytab values ('" + prnkeyst + "','" + startt + "','0')";

                dab.executeUpdate(myquery);

                myquery = " Select * from contender";

                ResultSet rs = dab.executeQuery(myquery);
                String leaderlist = "";
                while (rs.next()) {
                    leaderlist += rs.getString("cname") + ",";
                    leaderlist += rs.getString("pname") + ",";
                    leaderlist += rs.getString("psymbol") + "#";

                }
                if (leaderlist.trim().length() > 1) {

                    myquery = " Select * from register where mobileno ='" + phoneno + "'";
                    rs = dab.executeQuery(myquery);
                    if (rs.next()) {
                        String sskey = rs.getString("encryptid");
                        String textforency = "contenders#" + leaderlist.trim() + "@" + prnkeyst;
                        byte[] encrypt = TripleDESTest.reencrypt(textforency, sskey);
                        String enmsg = TripleDESTest.bytetostr(encrypt);
                        String url = "http://192.168.1.9:9090/sendsms";
                        String charset = "UTF-8";
                        String param1 = phoneno;
                        String param2 = enmsg;
                        String param3 = "";
// ...
                        String querys = String.format("phone=%s&text=%s&password=%s",
                                URLEncoder.encode(param1, charset),
                                URLEncoder.encode(param2, charset),
                                URLEncoder.encode(param3, charset));

                        URLConnection connection = new URL(url + "?" + querys).openConnection();
                        connection.setRequestProperty("Accept-Charset", charset);
                        InputStream resp = connection.getInputStream();
                    }
                }

            }
        } else if (originalmsg.startsWith("VOTE")) {

            int idx = originalmsg.indexOf("#");
            String origcontent = originalmsg.substring(idx + 1);
            String voterdet[] = origcontent.split("#");
//str = "VOTE#" + symbol[2]+"#"+sec_key;
            int i = 0;
            
            String myquery = " Select * from prnkeytab where prnkey='" + voterdet[1] + "'";

            ResultSet myrst = dab.executeQuery(myquery);
            String message = " Your vote recieved successfully";
            System.out.println(message);
            if (myrst.next()) {

                String startt = myrst.getString("starttime");

                long st = Long.parseLong(startt);
                long et = Calendar.getInstance().getTimeInMillis(); 
                long tot_time = et - st;
                System.out.println("tot_time:"+tot_time);
                if (tot_time > 999999990) {
                    message = " Your session has expired ";
                } else {

                    String symbol = voterdet[0];

                    Database db = new Database();
                    
                    System.out.println("Voter sysmbol:"+symbol);
                    String cquerymain = "Select * from contender where psymbol='"+symbol+"'";
                    ResultSet cset = db.executeQuery(cquerymain);
                    if (cset.next()){
                        int voteCount = Integer.parseInt(cset.getString("votecount")) + 1;
                            String query = "update contender set votecount='" + voteCount + "' where psymbol='" + symbol + "'";
                            db.executeUpdate(query);
                    }
//                    if (symbol.equals("HAND")) {
//
//                        String cquery = "Select * from contender where psymbol='HAND'";
//                        ResultSet cset = db.executeQuery(cquery);
//                        if (cset.next()) {
//                            int hand = Integer.parseInt(cset.getString("votecount")) + 1;
//                            String query = "update contender set votecount='" + hand + "' where psymbol='" + symbol + "'";
//                            db.executeUpdate(query);
//                        }
//                    } else if (symbol.equals("LOTUS")) {
//                        String cquery = "Select * from contender where psymbol='LOTUS'";
//                        ResultSet cset = db.executeQuery(cquery);
//                        if (cset.next()) {
//                            int lotus = Integer.parseInt(cset.getString("votecount")) + 1;
//                            String query = "update contender set votecount='" + lotus + "' where psymbol='" + symbol + "'";
//                            db.executeUpdate(query);
//                        }
//
//                    } else if (symbol.equals("ELEPHANT")) {
//                        String cquery = "Select * from contender where psymbol='ELEPHANT'";
//                        ResultSet cset = db.executeQuery(cquery);
//                        if (cset.next()) {
//                            int elephant = Integer.parseInt(cset.getString("votecount")) + 1;
//
//                            String query = "update contender set votecount='" + elephant + "' where psymbol='" + symbol + "'";
//                            db.executeUpdate(query);
//                        }
//                    } else if (symbol.equals("AEROPLANE")) {
//                        String cquery = "Select * from contender where psymbol='AEROPLANE'";
//                        ResultSet cset = db.executeQuery(cquery);
//                        if (cset.next()) {
//                            int aeroplane = Integer.parseInt(cset.getString("votecount")) + 1;
//                            String query = "update contender set votecount='" + aeroplane + "' where psymbol='" + symbol + "'";
//                            db.executeUpdate(query);
//                        }
//                    }

                    myquery = " Select * from register where mobileno ='" + phoneno + "'";
                    ResultSet rstt = dab.executeQuery(myquery);
                    if (rstt.next()) {
                        String sskey = rstt.getString("encryptid");
                        String textforency = message;
                        byte[] encrypt = TripleDESTest.reencrypt(textforency, sskey);
                        String enmsg = TripleDESTest.bytetostr(encrypt);
                        String url = "http://192.168.1.9:9090/sendsms";
                        String charset = "UTF-8";
                        String param1 = phoneno;
                        String param2 = enmsg;
                        String param3 = "";
// ...
                        String querys = String.format("phone=%s&text=%s&password=%s",
                                URLEncoder.encode(param1, charset),
                                URLEncoder.encode(param2, charset),
                                URLEncoder.encode(param3, charset));

                        URLConnection connection = new URL(url + "?" + querys).openConnection();
                        connection.setRequestProperty("Accept-Charset", charset);
                        InputStream resp = connection.getInputStream();
                    }
                }


            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
%> 
