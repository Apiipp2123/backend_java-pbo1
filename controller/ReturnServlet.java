package com.rental.controller;

import com.rental.dao.SewaDAO;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet(name = "ReturnServlet", urlPatterns = {"/ReturnServlet"})
public class ReturnServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            int idSewa = Integer.parseInt(request.getParameter("idSewa"));
            String returnDate = request.getParameter("returnDate"); // Tanggal Simulasi Admin
            
            SewaDAO dao = new SewaDAO();
            double denda = dao.processReturn(idSewa, returnDate);
            
            if(denda > 0) {
                response.sendRedirect("admin.jsp?msg=late&amount=" + String.format("%.0f", denda));
            } else {
                response.sendRedirect("admin.jsp?msg=returned");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("admin.jsp?msg=error");
        }
    }
}