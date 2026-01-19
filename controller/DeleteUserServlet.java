package com.rental.controller;

import com.rental.dao.UserDAO;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet(name = "DeleteUserServlet", urlPatterns = {"/DeleteUserServlet"})
public class DeleteUserServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String idStr = request.getParameter("id");
        if(idStr != null) {
            int id = Integer.parseInt(idStr);
            
            UserDAO dao = new UserDAO();
            dao.deleteUser(id); // Panggil method 'Cuci Piring' tadi
        }
        
        response.sendRedirect("admin.jsp?msg=user_deleted");
    }
}