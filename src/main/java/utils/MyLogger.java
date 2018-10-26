/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

/**
 *
 * @author Iván
 */
public class MyLogger {
    public static void doLog(Exception excepcion,Class clase,String nivel){
        System.setProperty("logPath", clase.getSimpleName());
        System.setProperty("projectName", "CRUDPoolIvanMQ");
        
    }
}
