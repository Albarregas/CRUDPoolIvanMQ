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
import java.sql.PreparedStatement;
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
@WebServlet(name = "Concluir", urlPatterns = {"/Concluir"})
public class Concluir extends HttpServlet {

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

        if (request.getParameter("boton").equals("Confirmar")) {
            String[] valores = request.getParameterValues("valores");
            ArrayList<Ave> listadoBorrar = null;
            String prefix = "", con_comas = "";
            for (int i = 0; i < valores.length; i++) {
                con_comas += prefix + "'" + valores[i] + "'";
                prefix = ",";
            }
            listadoBorrar = obtenerDatos(con_comas);
            request.setAttribute("listadoBorrado", listadoBorrar);
            borrarBD(con_comas);
            request.getRequestDispatcher("JSP/borrar/finEliminar.jsp").forward(request, response);

        } else if (request.getParameter("boton").equals("Cancelar")) {
            request.setAttribute("lista", visualizarContenido());
            request.getRequestDispatcher("JSP/borrar/leerEliminar.jsp").forward(request, response);
        }
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
        Ave ave = new Ave(
                    request.getParameter("Anilla"),
                    request.getParameter("Especie"),
                    request.getParameter("Lugar"),
                    request.getParameter("Fecha")
            );
        request.setAttribute("Ave", ave);
        if (request.getParameter("boton").equals("Confirmar")) {
            
            if (!ave.getAnilla().equals("")
                    && !ave.getEspecie().equals("")
                    && !ave.getLugar().equals("")
                    && !ave.getFecha().equals("")) {
                actualizarDB(ave);
                request.getRequestDispatcher("JSP/actualizar/finActualizar.jsp").forward(request, response);

            }else{
                request.setAttribute("error", "Por favor, no dejes campos en blanco.");
                request.getRequestDispatcher("JSP/actualizar/Actualizar.jsp").forward(request, response);
            }
        } else {
            request.setAttribute("lista", visualizarContenido());
            request.getRequestDispatcher("JSP/actualizar/inicioActualizar.jsp").forward(request, response);
        }
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
                ave.setFecha(resultado.getString("fecha"));
                listado.add(ave);
            }
            conex.cerrarConexion();
        } catch (SQLException ex) {
            System.out.println("Error SQL en el metodo visualizar");
            Logger.getLogger(Realizar1.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listado;
    }

    private void borrarBD(String valor) {
        Connection con = conex.iniciarConexion();
        String sql = "DELETE FROM aves WHERE anilla IN(";
        sql = sql + valor + ")";
        try {
            Statement stmt = con.createStatement();
            stmt.executeUpdate(sql);
        } catch (SQLException ex) {
            Logger.getLogger(Concluir.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private ArrayList<Ave> obtenerDatos(String valor) {
        ArrayList<Ave> listado = new ArrayList<Ave>();
        try {

            Connection con = conex.iniciarConexion();
            String sql = "SELECT * FROM aves where anilla IN(";
            sql = sql + valor + ")";
            Statement sentencia = con.createStatement();
            ResultSet resultado = sentencia.executeQuery(sql);
            Ave ave = null;
            while (resultado.next()) {
                ave = new Ave();
                ave.setAnilla(resultado.getString("anilla"));
                ave.setEspecie(resultado.getString("especie"));
                ave.setLugar(resultado.getString("lugar"));
                ave.setFecha(resultado.getString("fecha"));
                listado.add(ave);
            }
            conex.cerrarConexion();
        } catch (SQLException ex) {
            System.out.println("Error SQL en el metodo visualizar");
            Logger.getLogger(Realizar1.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listado;
    }

    private void actualizarDB(Ave ave) {
        try {
            String sql = "UPDATE aves SET "
                    + "especie = ?"
                    + ",lugar = ?"
                    + ",fecha = ?"
                    + "WHERE anilla=?";

            Connection con = conex.iniciarConexion();
            PreparedStatement pstm = con.prepareStatement(sql);
            pstm.setString(1, ave.getEspecie());
            pstm.setString(2, ave.getLugar());
            pstm.setString(3, ave.getFecha());
            pstm.setString(4, ave.getAnilla());

            pstm.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(Concluir.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
