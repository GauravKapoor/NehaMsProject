<%-- 
    Document   : handlesms
    Created on : Nov 9, 2012, 6:16:52 PM
    Author     : prathap
--%>

<%@page import="java.io.PrintWriter"%>
<%@page import="java.util.Random"%>
<%@page import="ec.TripleDESTest"%>
<%@page import="ec.Database"%>
<%@page import="ec.*"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <%
            String Sender = request.getParameter("sender");

            String Text = request.getParameter("text");
            
            PrintWriter out1=response.getWriter();
            
            common cm= common.getinstance();
            
            cm.setresponse(out1);

            PrintWriter pw= cm.getresponse();
            System.out.println("Sender" + Sender + "   Text=" + Text);

            String temp = Sender + Text;

            Database db = new Database();

            //String query = " insert into register values('" + Text + "','" + Sender + "')";

            //db.executeUpdate(query);

            byte[] encrypted = TripleDESTest.encrypt(temp);
            
            System.out.println("Encrypted data=  " + encrypted);
            
            Random rd = new Random();
            
            int random = rd.nextInt(100000);
            
            String str = TripleDESTest.bytetostr(encrypted);
            
            String data=Integer.toString(random);
            
             String query = " insert into register values('" + Text + "','" + Sender + "','"+data+"','"+str+"')";

            db.executeUpdate(query);
            
            pw.print(data);
                        
            
        
        %>
    </body>
</html>
