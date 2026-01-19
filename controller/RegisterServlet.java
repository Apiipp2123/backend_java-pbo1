package com.rental.controller;

import com.rental.dao.UserDAO;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet(name = "RegisterServlet", urlPatterns = {"/RegisterServlet"})
public class RegisterServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String u = request.getParameter("user");
        String e = request.getParameter("email");
        String p = request.getParameter("pass");
        
        // Data Profil Default (Bisa dikembangkan nanti di form register)
        String nama = request.getParameter("nama") != null ? request.getParameter("nama") : u;
        String alamat = "-"; 
        String telp = "-";
        
        UserDAO dao = new UserDAO();
        boolean sukses = dao.registerUser(u, e, p, nama, alamat, telp);
        
        if(sukses) {
            // Berhasil daftar, lempar ke login dengan pesan sukses
            response.sendRedirect("login.jsp?msg=registered");
        } else {
            // Gagal, lempar balik ke register dengan pesan error
            response.sendRedirect("register.jsp?error=exists");
        }
    }
}