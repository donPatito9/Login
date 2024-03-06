/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.login.logica;

import com.mycompany.login.persistencia.ControladorPersistencia;
import java.util.List;

/**
 *
 * @author UserDevs
 */
public class ControladorLogica {
    ControladorPersistencia controlPersist;
    
    
    public ControladorLogica(){
        controlPersist = new ControladorPersistencia();
    }
    public Usuario validarUsuario(String usuario, String contrasenia){
        
        //String mensaje = "";
        
        //boolean ok = false;
        Usuario usr = null;
        List <Usuario> listadoUsuarios = controlPersist.traerUsuarios();
        
        for (Usuario usu : listadoUsuarios) {
            //System.out.println("Usuario: " +usu.getNombreUsuario());
            if (usu.getNombreUsuario().equals(usuario)){
                
                if (usu.getContrasenia().equals(contrasenia)) {
                   // mensaje = "Usuario y contraseña Correcta. Bienvenido/a!";
                   usr = usu;
                   return usr;
                }
                else {
                   // mensaje = "Contraseña incorrecta";
                   usr = null;
                    return usr;
                }
                }
                else {
                    //mensaje = "Usuario no encontrado";
                    usr = null;
                   // return user;// error de usuario
                }
            }
        return usr;
    }

    public List<Usuario> traerUsuarios() {
       return controlPersist.traerUsuarios();
    }

    public List<Rol> traerRoles() {
        
       return controlPersist.traerRoles();
    }

    public void crearUsuario(String usuario, String contrasenia, String rolRecibido) {
        Usuario usu = new Usuario();
        usu.setNombreUsuario(usuario);
        usu.setContrasenia(contrasenia);
        
        Rol rolEncontrado = new Rol();
        
        rolEncontrado = this.traerRol(rolRecibido);
        if (rolEncontrado != null) {
            usu.setUnRol(rolEncontrado);
        }
        int id = this.buscarUltimaIdUsuarios();
        usu.setId(id +1);
        
        controlPersist.crearUsuario(usu);
    }

    private Rol traerRol(String rolRecibido) {
        List<Rol> listadoRoles = controlPersist.traerRoles();
        
        for (Rol rol : listadoRoles) {
        
            if(rol.getNombreRol().equals(rolRecibido)) {
                return rol;
            }
        }
    return null;
    }

    private int buscarUltimaIdUsuarios() {
        List <Usuario> listadoUsuarios = this.traerUsuarios();
        
        Usuario usu = listadoUsuarios.get(listadoUsuarios.size()-1);
        return usu.getId();
    }

    public void borrarUsuario(int id_usuario) {
        //llamar al metodo borrarUsuario desde controladorPersistencia
       controlPersist.borrarUsuario(id_usuario);
    }

    public Usuario traerUsuario(int id_usuario) {
      
        return controlPersist.traerUsuario(id_usuario);
    }

    public void editarUsuario(Usuario usu, String usuario, String contrasenia, String rolRecibido) {
   
        usu.setNombreUsuario(usuario);
        usu.setContrasenia(contrasenia);
        
        Rol rolEncontrado = new Rol();
        
        rolEncontrado = this.traerRol(rolRecibido);
        if (rolEncontrado != null) {
            usu.setUnRol(rolEncontrado);
        }
        
        controlPersist.editarUsuario(usu);
        
    }    
}
