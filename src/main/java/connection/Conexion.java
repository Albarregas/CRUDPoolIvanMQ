/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connection;


import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 *
 * @author Iván
 */
public class Conexion {
    DataSource datasource=null;
    Connection con = null;

   //Este metodo inicia un pool de conexiones y se usa en el metodo init de cada controlador para no crear mas que uno.
   public void iniciarPool(){
        try {
            Context contextoInicial = new InitialContext();
            datasource = (DataSource) contextoInicial.lookup("java:comp/env/jdbc/Pool");
        } catch (NamingException ex) {
            System.out.println("Problemas en el acceso a la BD");
            ex.printStackTrace();
        }
    }
   //Este metodo inicia una conexion y la retorna para usarla.
    public Connection iniciarConexion(){   
        try {
            con = datasource.getConnection();
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
        return con;
    }
    //este metodo cierra la conexion abierta.
    public void cerrarConexion(){
        try {
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
