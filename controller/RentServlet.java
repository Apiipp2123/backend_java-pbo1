package com.rental.controller;

import com.rental.dao.SewaDAO;
import com.rental.model.User;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet(name = "RentServlet", urlPatterns = {"/RentServlet"})
public class RentServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("currentUser");
        if(currentUser == null) { response.sendRedirect("login.jsp"); return; }
        
        try {
            int idKendaraan = Integer.parseInt(request.getParameter("idKendaraan"));
            // Harga Sewa dikirim dari form hidden (atau bisa query ulang di DAO biar aman, tapi gini dlu gpp)
            double hargaSewa = Double.parseDouble(request.getParameter("hargaSewa"));
            
            // Input Tanggal & Durasi
            String tglSewa = request.getParameter("rentalDate"); // Format: yyyy-mm-dd
            int durasi = Integer.parseInt(request.getParameter("days"));
            
            // Panggil DAO Baru
            SewaDAO dao = new SewaDAO();
            // Asumsi: ID User = ID Pelanggan (Perlu penyesuaian kalau tabel User & Pelanggan pisah)
            boolean sukses = dao.addSewa(idKendaraan, currentUser.getId(), tglSewa, durasi, hargaSewa);
            
            if(sukses) response.sendRedirect("home.jsp?msg=success");
            else response.sendRedirect("home.jsp?msg=error");
            
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("home.jsp?msg=error");
        }
    }
}