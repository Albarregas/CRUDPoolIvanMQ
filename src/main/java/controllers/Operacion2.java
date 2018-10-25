/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import beans.Ave;
import connection.Conexion;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
@WebServlet(name = "Operacion", urlPatterns = {"/Operacion"})
public class Operacion2 extends HttpServlet {

    public Conexion conex = new Conexion();

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
        if (request.getParameter("nombre").equals("Insertar")) {
            Ave ave = new Ave(
                    request.getParameter("Anilla"),
                    request.getParameter("Especie"),
                    request.getParameter("Lugar"),
                    request.getParameter("Fecha")
            );
            if (request.getParameter("boton").equals("Cancelar")) {
                request.getRequestDispatcher("/index.html").forward(request, response);
            } else if (request.getParameter("boton").equals("Aceptar")) {
                if (ave.getAnilla() != null
                        && ave.getEspecie() != null
                        && ave.getLugar() != null
                        && ave.getFecha() != null) {
                    insertarDB(ave);
                    request.setAttribute("Ave",ave);
                    request.getRequestDispatcher("JSP/insertar/finInsertar.jsp").forward(request, response);
                } else {
                    request.getRequestDispatcher("JSP/insertar/inicioInsertar.jsp").forward(request, response);
                }
            }
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

    private void insertarDB(Ave ave) {
        if(comprobar(ave.getAnilla())){
          try {
            Connection con = conex.iniciarConexion();
            String sql= "INSERT INTO `aves` VALUES ('"
                    +ave.getAnilla()+"','"
                    +ave.getEspecie()+"','"
                    +ave.getLugar()+"','"
                    +ave.getFecha()+"')";
            Statement sentencia = con.createStatement();
            sentencia.executeQuery(sql);
            conex.cerrarConexion();
        } catch (SQLException ex) {
            System.out.println("Error SQL en el metodo visualizar");
            Logger.getLogger(Realizar1.class.getName()).log(Level.SEVERE, null, ex);
        }  
        }
    }

    private boolean comprobar(String anilla) { 
        try {
            Connection con = conex.iniciarConexion();
            String sql = "select * from aves where anillas="+anilla;
            Statement sentencia = con.createStatement();
            ResultSet resultado = sentencia.executeQuery(sql);
            if(resultado.next()){
                conex.cerrarConexion();
                return true;
            }
            
        } catch (SQLException ex) {
            System.out.println("Error SQL en el metodo visualizar");
            Logger.getLogger(Realizar1.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

}
