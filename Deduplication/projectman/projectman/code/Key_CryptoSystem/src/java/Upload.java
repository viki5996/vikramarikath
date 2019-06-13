/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import Connection.DB;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.InetAddress;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 *
 * @author selvarani
 */
@WebServlet(urlPatterns = {"/Upload"})
public class Upload extends HttpServlet {

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, FileUploadException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        HttpSession session=request.getSession(true);
       try {
           StringBuffer sb1 = new StringBuffer();
           
           

            String saveFile="",sn="",un="";
            int fileidnum=0,downloadcount=0,vc=0;
            sn=(String)session.getAttribute("Sn");
            un=(String)session.getAttribute("un");
            String contentType = request.getContentType();
            // Create a factory for disk-based file items
            DiskFileItemFactory factory = new DiskFileItemFactory();

// Set factory constraints
            factory.setSizeThreshold(4012);
//factory.setRepository("c:");

// Create a new file upload handler
            ServletFileUpload upload = new ServletFileUpload(factory);

// Set overall request size constraint
            //upload.setSizeMax(10024);

// Parse the request
            List items = null;
            try {
                items = upload.parseRequest(request);
            } catch (FileUploadException e) {
                e.printStackTrace();
            }
            byte[] data = null;
            String fileName = null;
// Process the uploaded items
            Iterator iter = items.iterator();
            while (iter.hasNext()) {
                FileItem item = (FileItem) iter.next();

                if (item.isFormField()) {
                    //processFormField(item);

                    String name = item.getFieldName();
                    String value = item.getString();

                } else {
                    data = item.get();
                    fileName = item.getName();
                     System.out.println("fn:" + fileName);
                }
            }
            String extension = "";

int i = fileName.lastIndexOf('.');
if (i > 0) {
    extension = fileName.substring(i+1);
}
System.out.println("--"+extension);
            saveFile = fileName;
             String path = request.getSession().getServletContext().getRealPath("/");
               System.out.println(path);
              
               String strPath = path+"\\"+saveFile;
    System.out.println(strPath);
            File ff = new File(strPath);
            FileOutputStream fileOut = new FileOutputStream(ff);
     
            fileOut.write(data, 0, data.length);
            fileOut.flush();
            fileOut.close();
            System.out.println(strPath);
            System.out.println("Thrid");
            Connection con = null;
            PreparedStatement psmnt = null;
            FileInputStream fis;
            InputStream sImage;
           FileInputStream inFile;
FileOutputStream outFile;

//inFile = new FileInputStream(patt+saveFile);
//outFile = new FileOutputStream(strPath);
BufferedReader br = new BufferedReader(new FileReader(strPath));

 try {
       
        String line = br.readLine();

        while (line != null) {
            sb1.append(line);
            sb1.append("\n");
            line = br.readLine();
           
      // out.println(sb1);
        }}catch(Exception e){
        
        }
                java.util.Date now = new java.util.Date();
                String DATE_FORMAT = "yyyy-MM-dd hh:mm:ss";
                SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
                String strDateNew = sdf.format(now);
                System.out.println(strDateNew);
                String ip1=InetAddress.getLocalHost().getHostAddress();
                try {
               
                    DB Db=new DB();
                   File f = new File(strPath);
               double cost=0.0;
                    long length = f.length();
                   
                          cost=(length*(0.02/100)); 
                 System.out.println("length " + length);
                      String ip=request.getRemoteAddr();int id=0;
                      ResultSet rs=Db.Select("select max(Fid) from upload");
                      if(rs.next())
                      {
                          id=rs.getInt(1);
                      }
                      id=id+1;
                      con=Db.con; 
                int id1=id;
                      String dd="select * from upload where Filename='"+saveFile+"'";
                      Statement st=con.createStatement();
                      ResultSet rs2=st.executeQuery(dd);
                         String dd1="select * from upload where enc='"+hash(sb1)+"'";
                      Statement st1=con.createStatement();
                      ResultSet rs3=st1.executeQuery(dd1);
                      if(rs2.next())
                      {
                        session.setAttribute("msg", "File Name Already Exists!");
              session.setAttribute("color", "red"); 
                         response.sendRedirect("Upload.jsp#!/page_SERVICES");    
                      }
                      else if(rs3.next())
                      {
                          session.setAttribute("msg", "File Content Already Exists!");
              session.setAttribute("color", "red"); 
                response.sendRedirect("Upload.jsp#!/page_SERVICES");  
                      }
                          
                      else
                      {
                    String queryString = "insert into upload(Un,S_Name, Fid, Filename, Extension,Size,Content,Cost,Date,Keyvalue,req,enc) values (?,?,?,?,?,?,?,?,?,?,?,?)";
                                                                System.out.println("four" + un);
                                                                psmnt = con.prepareStatement(queryString);
                                                               fis = new FileInputStream(f);
                                                                psmnt.setString(1, un);
                                                                psmnt.setString(2, sn);
                                                                psmnt.setInt(3, id1);
                                                                psmnt.setString(4, saveFile);
                                                                psmnt.setString(5, extension);
                                                                  psmnt.setLong(6,length);
                                                                   psmnt.setBinaryStream(7, (InputStream) fis, (int) (f.length()));
                                                                   psmnt.setDouble(8,cost);
                                                                   psmnt.setString(9, strDateNew);
                                                                    psmnt.setString(10, "No");
                                                                        psmnt.setString(11, "");
                                                                        psmnt.setString(12,hash(sb1));
                                                                int s = psmnt.executeUpdate();
                                                                int Flength=0,Dcount=0;
                                FileWriter fw=new FileWriter(new File("C:\\XML/"+saveFile+""));
            fw.write(extension);
            fw.close();

       
                       
           boolean status=new Ftpcon().upload(new File("C:\\XML/"+saveFile+""));
                                                             
                                                               if(s>0)
                                                               {
                                                                  if(String.valueOf(session.getAttribute("Sn")).compareToIgnoreCase("Not")!=0)
                                                                   session.setAttribute("msg", "'"+saveFile+"' has been stored.");
                                                                   else
                                                                      session.setAttribute("msg", "'"+saveFile+"' has been stored.");  
                                                                   session.setAttribute("color", "white"); 
                                                                   response.sendRedirect("mail?fid="+id+"&&fn="+saveFile+"");
                            
                                                               }
                                                               else
                                                               {
                                                                  session.setAttribute("msg", "Failed !");
              session.setAttribute("color", "red"); 
                                                                   response.sendRedirect("Upload.jsp#!/page_SERVICES"); 
                                                               }
                      }
               
                                   
                                                            } catch (Exception ex) {
                    out.println(ex);
                }
            } catch (Exception e) {
                out.println(e);
            }

    }
    

private static String hash(StringBuffer sb) {
        // Implement hash (MD5)
        
        String md5 = null;
         
        if(null == sb) return null;
         
        try {
             
        //Create MessageDigest object for MD5
        MessageDigest digest = MessageDigest.getInstance("MD5");
         
        //Update input string in message digest
        digest.update(sb.toString().getBytes(), 0, sb.length());
 
        //Converts message digest value in base 16 (hex)
        md5 = new BigInteger(1, digest.digest()).toString(16);
 
        } catch (Exception e) {
 
            e.printStackTrace();
        }
       
        
        
        return md5.toString();
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (FileUploadException ex) {
            Logger.getLogger(Upload.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (FileUploadException ex) {
            Logger.getLogger(Upload.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    private char[] String(double cost) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
