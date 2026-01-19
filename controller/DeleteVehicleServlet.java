package com.rental.controller;

import com.rental.dao.KendaraanDAO;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet(name = "DeleteVehicleServlet", urlPatterns = {"/DeleteVehicleServlet"})
public class DeleteVehicleServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            // Ambil ID dari URL (?id=123)
            String idStr = request.getParameter("id");
            if(idStr != null) {
                int id = Integer.parseInt(idStr);
                
                KendaraanDAO dao = new KendaraanDAO();
                // Panggil method delete di DAO
                boolean sukses = dao.deleteKendaraan(id);
                
                if(sukses) {
                    response.sendRedirect("admin.jsp?msg=deleted");
                } else {
                    response.sendRedirect("admin.jsp?msg=error");
                }
            } else {
                response.sendRedirect("admin.jsp");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("admin.jsp?msg=error");
        }
    }
}