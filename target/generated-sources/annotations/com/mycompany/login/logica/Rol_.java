package com.mycompany.login.logica;

import com.mycompany.login.logica.Usuario;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2024-02-24T02:58:58")
@StaticMetamodel(Rol.class)
public class Rol_ { 

    public static volatile SingularAttribute<Rol, String> descripcion;
    public static volatile SingularAttribute<Rol, String> nombreRol;
    public static volatile ListAttribute<Rol, Usuario> listadoUsuarios;
    public static volatile SingularAttribute<Rol, Integer> id;

}