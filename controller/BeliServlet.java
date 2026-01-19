package com.rental.controller;

import com.rental.dao.KendaraanDAO;
import com.rental.model.User;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet(name = "BeliServlet", urlPatterns = {"/BeliServlet"})
public class BeliServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Cek Login
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("currentUser");
        if(currentUser == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        
        // Ambil Data dari Form
        int idKendaraan = Integer.parseInt(request.getParameter("idKendaraan"));
        double hargaBeli = Double.parseDouble(request.getParameter("hargaBeli"));
        
        // Panggil DAO
        KendaraanDAO dao = new KendaraanDAO();
        // Disini idPelanggan diambil dari ID user yang login
        boolean sukses = dao.beliKendaraan(idKendaraan, currentUser.getId(), hargaBeli);
        
        if(sukses) {
            response.sendRedirect("home.jsp?msg=sold");
        } else {
            response.sendRedirect("home.jsp?msg=error");
        }
    }
}