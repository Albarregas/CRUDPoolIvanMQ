/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import beans.Ave;
import connection.Conexion;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Iván
 */
@WebServlet(name = "Realizar", urlPatterns = {"/Realizar"})
public class Realizar extends HttpServlet {

    public Conexion conex = new Conexion();

    @Override
    public void init() throws ServletException {
        super.init(); //To change body of generated methods, choose Tools | Templates.
        conex.iniciarPool();
    }

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (request.getParameter("Aceptar").equals("Crear")) {
            request.getRequestDispatcher("JSP/crear/inicioInsertar.jsp").forward(request, response);
        }
        if (request.getParameter("Aceptar").equals("Visualizar")) {
            request.setAttribute("lista", visualizarContenido());
            request.getRequestDispatcher("JSP/visualizar/leer.jsp").forward(request, response);
        }
        if (request.getParameter("Aceptar").equals("Modificar")) {
            request.setAttribute("lista", visualizarContenido());
            request.getRequestDispatcher("JSP/actualizar/inicioActualizar.jsp").forward(request, response);
        }
        if (request.getParameter("Aceptar").equals("Borrar")) {
            request.setAttribute("lista", visualizarContenido());
            request.getRequestDispatcher("JSP/borrar/leerEliminar.jsp").forward(request, response);
        }
        processRequest(request, response);
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

    private ArrayList<Ave> visualizarContenido() {
        ArrayList<Ave> listado = new ArrayList<Ave>();
        try {

            Connection con = conex.iniciarConexion();
            String sql = "select * from aves";
            Statement sentencia = con.createStatement();
            ResultSet resultado = sentencia.executeQuery(sql);
            Ave ave = null;
            while (resultado.next()) {
                ave = new Ave();
                ave.setAnilla(resultado.getString("anilla"));
                ave.setEspecie(resultado.getString("especie"));
                ave.setLugar(resultado.getString("lugar"));
                ave.setFecha(resultado.getDate("fecha"));
                listado.add(ave);
            }
            conex.cerrarConexion();
        } catch (SQLException ex) {
            System.out.println("Error SQL en el metodo visualizar");
            Logger.getLogger(Realizar.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listado;
    }

}
