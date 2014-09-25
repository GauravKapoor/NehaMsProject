package ec;


import java.io.PrintWriter;
import javax.servlet.http.HttpServletResponse;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author prathap
 */
public class common {
    
  public static PrintWriter clres;
  public static common cm;
 public static common getinstance(){
     if(cm==null){
         cm=new common();
     }
     return cm;
 }
    
  public void setresponse(PrintWriter rs){
      clres = rs;
  }
  
  public PrintWriter getresponse(){
      return clres;
  }
}
