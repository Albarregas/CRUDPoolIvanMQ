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
        ArrayList<Ave> lista = (ArrayList<Ave>) request.getAttribute("listadoBorrado");
        if (request.getParameter("boton").equals("Confirmar")) {
            request.setAttribute("listaBorrado", lista);
            borrarBD(lista);
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

    private void borrarBD(ArrayList<Ave> lista) {
        Connection con=conex.iniciarConexion();
        String sql="DELETE FROM Tabla WHERE anilla=IN(";
        for (int i = 0; i < lista.size(); i++) {
            sql=sql+lista.get(i).getAnilla()+",";
        }
        //sql.replace(sql.length(), )
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            int x = stmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(Concluir.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}
