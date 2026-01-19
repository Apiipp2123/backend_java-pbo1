package com.rental.controller;

import com.rental.dao.KendaraanDAO;
import com.rental.model.Kendaraan;
import com.rental.model.Mobil;
import com.rental.model.Motor;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet(name = "EditVehicleServlet", urlPatterns = {"/EditVehicleServlet"})
public class EditVehicleServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            String jenis = request.getParameter("jenis"); // "mobil" / "motor"
            String merk = request.getParameter("merk");
            String model = request.getParameter("model");
            int tahun = Integer.parseInt(request.getParameter("tahun"));
            String noPlat = request.getParameter("noPlat");
            double hargaSewa = Double.parseDouble(request.getParameter("hargaSewa"));
            double hargaBeli = Double.parseDouble(request.getParameter("hargaBeli"));
            
            // LOGIC OOP: Buat object sesuai jenis agar method getJenis() return value yang benar
            Kendaraan k = null;
            if(jenis.equalsIgnoreCase("mobil")) {
                k = new Mobil(id, merk, model, tahun, noPlat, "tersedia", hargaSewa, hargaBeli);
            } else {
                k = new Motor(id, merk, model, tahun, noPlat, "tersedia", hargaSewa, hargaBeli);
            }
            
            KendaraanDAO dao = new KendaraanDAO();
            boolean sukses = dao.updateKendaraan(k);
            
            if(sukses) {
                response.sendRedirect("admin.jsp?msg=edited");
            } else {
                response.sendRedirect("admin.jsp?msg=error");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("admin.jsp?msg=error");
        }
    }
}