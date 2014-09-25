<%-- 
    Document   : voter_reg
    Created on : Nov 9, 2012, 5:04:21 PM
    Author     : prathap
--%>

<%@page import="java.util.Random"%>
<%@page import="java.io.File"%>
<%@page import="java.io.FileOutputStream"%>
<%@page import="java.math.BigInteger"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="ec.*" %> 

<!DOCTYPE html>
<%
  
  String vuid=request.getParameter("uid");
  String vvid=request.getParameter("vid");
  String vname=request.getParameter("vname");
  String vfname=request.getParameter("fname");
  String vaddress=request.getParameter("address");
  String vmno=request.getParameter("mno");
  String varea=request.getParameter("area");
  String vgender=request.getParameter("gender");
  String vdob=request.getParameter("dob");
  Random rd  = new Random();
  int rand = rd.nextInt(60000);
  String sskey =Integer.toString(rand);
  
  
  
  String query =" INSERT INTO voter VALUES('"+vuid+"','"+vvid+"','"+vname+"','"+vfname+"','"+vaddress+"','"+vmno+"','"+varea+"','"+vgender+"','"+vdob+"','"+false+"')";
  
  Database db =new Database();
  
  db.executeUpdate(query);
  
  pageContext.forward("index.jsp");
%>

