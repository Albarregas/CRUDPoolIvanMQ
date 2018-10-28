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
import java.sql.Date;
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
        //Esta cadena nos servira para redireccionar el flujo.
        String url = null;
        //Si pulsamos el boton Borrar entrara.
        if (request.getParameter("boton").equals("Borrar")) {
            //Recogemos todos los valores que hayamos marcado
            String[] valores = request.getParameterValues("valores");
            ArrayList<Ave> listadoBorrar = null;
            //Utilizamos este metodo para meter todos los valores en una cadena separados por comas.
            String prefix = "", con_comas = "";
            for (int i = 0; i < valores.length; i++) {
                con_comas += prefix + "'" + valores[i] + "'";
                prefix = ",";
            }
            //Guardamos los datos que quiere borrar el usuario y los dejamos en sesion para despues mostrarlos
            listadoBorrar = obtenerDatos(con_comas);
            request.setAttribute("listadoBorrado", listadoBorrar);
            //LLamamos al metodo y lo borramos de la base de datos.
            borrarBD(con_comas);
            url = "JSP/borrar/finEliminar.jsp";

        } else if (request.getParameter("boton").equals("Cancelar")) {
            request.setAttribute("lista", visualizarContenido());
            url = "JSP/borrar/leerEliminar.jsp";
        }
        request.getRequestDispatcher(url).forward(request, response);
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
    //Utilizo el metodo doPost para recoger el formulario actualizar
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //Redireccionara el flujo
        String url = null;
        //Recogemos el ave del formulario
        Ave ave = new Ave();
        try {
            ave.setAnilla(request.getParameter("Anilla"));
            ave.setEspecie(request.getParameter("Especie"));
            ave.setLugar(request.getParameter("Lugar"));
            ave.setFecha(Date.valueOf(request.getParameter("Fecha")));

            //Lo pasamos por sesion para que puedan leerlo en la pantalla final
            request.setAttribute("Ave", ave);
            //Si has pulsado el boton Actualizar entrara
            if (request.getParameter("boton").equals("Actualizar")) {
                //Comprobamos que los datos no esten vacios.
                if (!ave.getAnilla().equals("")
                        && !ave.getEspecie().equals("")
                        && !ave.getLugar().equals("")
                        && !ave.getFecha().equals("")) {
                    //LLamamos al metodo para actualizar el ave.
                    actualizarDB(ave);
                    url = "JSP/actualizar/finActualizar.jsp";
                } else {
                    //Si estan vacios..
                    request.setAttribute("error", "Por favor, no dejes campos en blanco.");
                    url = "JSP/actualizar/Actualizar.jsp";
                }
            } else {
                //Si pulsa el boton cancelar...
                request.setAttribute("lista", visualizarContenido());
                url = "JSP/actualizar/inicioActualizar.jsp";
            }
        } catch (IllegalArgumentException e) {
            //Todo esto lo hago para controlar si borra una fecha el usuario y devolverle todos los datos que ya tenia.
            request.setAttribute("error", "Por favor, no dejes la fecha en blanco.");
            request.setAttribute("Ave", ave);
            url = "JSP/actualizar/Actualizar.jsp";
        }
        request.getRequestDispatcher(url).forward(request, response);
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
    //Este metodo sirve para sacar todo el contenido de la base de datos.

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

    //Borra todos los datos de las claves que introduzcas por parametros
    //para insertar varios datos en la string tienen que estar separados por comas ","
    private void borrarBD(String valor) {

        String sql = "DELETE FROM aves WHERE anilla IN(";
        sql += valor + ")";
        try {
            Connection con = conex.iniciarConexion();
            Statement stmt = con.createStatement();
            stmt.executeUpdate(sql);
            conex.cerrarConexion();
        } catch (SQLException ex) {
            Logger.getLogger(Concluir.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //Obtiene todos los datos de los que introduzcas por parametros en una string
    //para insertar varios datos en la string tienen que estar separados por comas ","
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

    //Este metodo actualiza el ave que se le pasa por parametro.
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
            pstm.setDate(3, ave.getFecha());
            pstm.setString(4, ave.getAnilla());

            pstm.executeUpdate();
            conex.cerrarConexion();
        } catch (SQLException ex) {
            Logger.getLogger(Concluir.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
