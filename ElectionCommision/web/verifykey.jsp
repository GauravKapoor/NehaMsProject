<%-- 
    Document   : verifykey
    Created on : Nov 12, 2012, 12:48:00 PM
    Author     : prathap
--%>

<%@page import="java.sql.ResultSet"%>
<%@page import="ec.Database"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%

String sskey = request.getParameter("key");

Database db = new Database();

String query = "Select * from register where encryptid ='"+sskey+"'";

ResultSet rs = db.executeQuery(query);

if(rs.next()){
    String phoneno =rs.getString("mobileno");
    pageContext.forward("register.jsp?mobno="+phoneno+"");
}else{
    pageContext.forward("index.html");
}


%>
