package com.rental.controller;

import com.rental.dao.UserDAO;
import com.rental.model.User;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet(name = "LoginServlet", urlPatterns = {"/LoginServlet"})
public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Ambil input EMAIL dan PASSWORD
        String email = request.getParameter("email");
        String pass = request.getParameter("pass"); 
        
        UserDAO dao = new UserDAO();
        User user = dao.login(email, pass); // Login pakai email
        
        if(user != null) {
            HttpSession session = request.getSession();
            session.setAttribute("currentUser", user);
            
            // Redirect sesuai Role dengan parameter msg=success
            if(user.getRole().equalsIgnoreCase("ADMIN")) {
                response.sendRedirect("admin.jsp?msg=login_success");
            } else {
                response.sendRedirect("home.jsp?msg=login_success");
            }
        } else {
            // Gagal login, kirim parameter error
            response.sendRedirect("login.jsp?error=invalid");
        }
    }
}