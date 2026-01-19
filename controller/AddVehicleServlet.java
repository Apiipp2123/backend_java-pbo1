package com.rental.controller;

import com.rental.dao.KendaraanDAO;
import com.rental.model.Kendaraan;
import com.rental.model.Mobil;
import com.rental.model.Motor;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet(name = "AddVehicleServlet", urlPatterns = {"/AddVehicleServlet"})
public class AddVehicleServlet extends HttpServlet {
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            // 1. Ambil Data dari Form
            String type = request.getParameter("type");   // "mobil" atau "motor"
            String brand = request.getParameter("brand");
            String model = request.getParameter("model");
            double price = Double.parseDouble(request.getParameter("price"));
            double hargaBeli = Double.parseDouble(request.getParameter("hargaBeli"));
            
            // Default value (karena form admin simpel)
            int tahun = 2023; 
            String noPlat = "B " + (int)(Math.random()*9000+1000) + " ZZ"; // Generate plat acak
            
            // 2. LOGIC OOP: Tentukan Instance Child Class
            Kendaraan k = null;
            
            if(type.equalsIgnoreCase("mobil")) {
                // Constructor: id (0 utk baru), merk, model, tahun, plat, status, hargaSewa, hargaBeli
                k = new Mobil(0, brand, model, tahun, noPlat, "tersedia", price, hargaBeli);
            } else {
                k = new Motor(0, brand, model, tahun, noPlat, "tersedia", price, hargaBeli);
            }
            
            // 3. Panggil DAO
            KendaraanDAO dao = new KendaraanDAO();
            boolean sukses = dao.addKendaraan(k); // method addKendaraan menerima param 'Kendaraan' (Induk)
            
            if(sukses) {
                response.sendRedirect("admin.jsp?msg=added");
            } else {
                response.sendRedirect("admin.jsp?msg=error");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("admin.jsp?msg=error");
        }
    }
}