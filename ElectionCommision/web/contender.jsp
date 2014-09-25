<%-- 
    Document   : contender
    Created on : Nov 21, 2012, 5:34:27 PM
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
  
  String cn1=request.getParameter("Cname1");
  String pn1=request.getParameter("Pname1");
  String ps1=request.getParameter("Psymbol1");
  
  String cn2=request.getParameter("Cname2");
  String pn2=request.getParameter("Pname2");
  String ps2=request.getParameter("Psymbol2");
  
  String cn3=request.getParameter("Cname3");
  String pn3=request.getParameter("Pname3");
  String ps3=request.getParameter("Psymbol3");
  
  String cn4=request.getParameter("Cname4");
  String pn4=request.getParameter("Pname4");
  String ps4=request.getParameter("Psymbol4");
  
  
  Database db =new Database();
  
 
  
  String query="Delete from contender";
  
   db.executeUpdate(query);
  
  query ="INSERT INTO contender VALUES('"+cn1+"','"+pn1+"','"+ps1+"','0')";
  
  
  
  db.executeUpdate(query);
  
  query =" INSERT INTO contender VALUES('"+cn2+"','"+pn2+"','"+ps2+"','0')";
  db.executeUpdate(query);
  
  query =" INSERT INTO contender VALUES('"+cn3+"','"+pn3+"','"+ps3+"','0')";
  db.executeUpdate(query);
  
  query =" INSERT INTO contender VALUES('"+cn4+"','"+pn4+"','"+ps4+"','0')";
  db.executeUpdate(query);

  pageContext.forward("index.html");
%>