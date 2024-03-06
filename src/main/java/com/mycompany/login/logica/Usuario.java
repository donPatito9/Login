package com.mycompany.login.logica;

import java.io.Serializable;
import javax.persistence.Entity;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 *
 * @author RobinsonConcha
 */

@Entity
public class Usuario implements Serializable {
    
    @Id
    //@GeneratedValue(strategy=GenerationType.SEQUENCE)
    
    private int id;//existe id. No usar name=id en JoinColumn
    private String nombreUsuario;
    private String contrasenia;
    
    @ManyToOne
    @JoinColumn(name="fk_rol")//error comun al ejecutar al existir id sugerencia alter nombre id por fk_rol
    private Rol unRol;
    
    public Usuario() {
    }

    public Usuario(int id, String nombreUsuario, String contrasenia, Rol unRol) {
        this.id = id;
        this.nombreUsuario = nombreUsuario;
        this.contrasenia = contrasenia;
        this.unRol = unRol;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    public Rol getUnRol() {
        return unRol;
    }

    public void setUnRol(Rol unRol) {
        this.unRol = unRol;
    }

}