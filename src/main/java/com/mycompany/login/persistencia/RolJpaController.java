/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.login.persistencia;

import com.mycompany.login.logica.Rol;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.mycompany.login.logica.Usuario;
import com.mycompany.login.persistencia.exceptions.NonexistentEntityException;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author UserDevs
 */
public class RolJpaController implements Serializable {

    public RolJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
      public RolJpaController(){
        emf = Persistence.createEntityManagerFactory("loginPU");
    }


    public void create(Rol rol) {
        if (rol.getListadoUsuarios() == null) {
            rol.setListadoUsuarios(new ArrayList<Usuario>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Usuario> attachedListadoUsuarios = new ArrayList<Usuario>();
            for (Usuario listadoUsuariosUsuarioToAttach : rol.getListadoUsuarios()) {
                listadoUsuariosUsuarioToAttach = em.getReference(listadoUsuariosUsuarioToAttach.getClass(), listadoUsuariosUsuarioToAttach.getId());
                attachedListadoUsuarios.add(listadoUsuariosUsuarioToAttach);
            }
            rol.setListadoUsuarios(attachedListadoUsuarios);
            em.persist(rol);
            for (Usuario listadoUsuariosUsuario : rol.getListadoUsuarios()) {
                Rol oldUnRolOfListadoUsuariosUsuario = listadoUsuariosUsuario.getUnRol();
                listadoUsuariosUsuario.setUnRol(rol);
                listadoUsuariosUsuario = em.merge(listadoUsuariosUsuario);
                if (oldUnRolOfListadoUsuariosUsuario != null) {
                    oldUnRolOfListadoUsuariosUsuario.getListadoUsuarios().remove(listadoUsuariosUsuario);
                    oldUnRolOfListadoUsuariosUsuario = em.merge(oldUnRolOfListadoUsuariosUsuario);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Rol rol) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Rol persistentRol = em.find(Rol.class, rol.getId());
            List<Usuario> listadoUsuariosOld = persistentRol.getListadoUsuarios();
            List<Usuario> listadoUsuariosNew = rol.getListadoUsuarios();
            List<Usuario> attachedListadoUsuariosNew = new ArrayList<Usuario>();
            for (Usuario listadoUsuariosNewUsuarioToAttach : listadoUsuariosNew) {
                listadoUsuariosNewUsuarioToAttach = em.getReference(listadoUsuariosNewUsuarioToAttach.getClass(), listadoUsuariosNewUsuarioToAttach.getId());
                attachedListadoUsuariosNew.add(listadoUsuariosNewUsuarioToAttach);
            }
            listadoUsuariosNew = attachedListadoUsuariosNew;
            rol.setListadoUsuarios(listadoUsuariosNew);
            rol = em.merge(rol);
            for (Usuario listadoUsuariosOldUsuario : listadoUsuariosOld) {
                if (!listadoUsuariosNew.contains(listadoUsuariosOldUsuario)) {
                    listadoUsuariosOldUsuario.setUnRol(null);
                    listadoUsuariosOldUsuario = em.merge(listadoUsuariosOldUsuario);
                }
            }
            for (Usuario listadoUsuariosNewUsuario : listadoUsuariosNew) {
                if (!listadoUsuariosOld.contains(listadoUsuariosNewUsuario)) {
                    Rol oldUnRolOfListadoUsuariosNewUsuario = listadoUsuariosNewUsuario.getUnRol();
                    listadoUsuariosNewUsuario.setUnRol(rol);
                    listadoUsuariosNewUsuario = em.merge(listadoUsuariosNewUsuario);
                    if (oldUnRolOfListadoUsuariosNewUsuario != null && !oldUnRolOfListadoUsuariosNewUsuario.equals(rol)) {
                        oldUnRolOfListadoUsuariosNewUsuario.getListadoUsuarios().remove(listadoUsuariosNewUsuario);
                        oldUnRolOfListadoUsuariosNewUsuario = em.merge(oldUnRolOfListadoUsuariosNewUsuario);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                int id = rol.getId();
                if (findRol(id) == null) {
                    throw new NonexistentEntityException("The rol with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(int id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Rol rol;
            try {
                rol = em.getReference(Rol.class, id);
                rol.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The rol with id " + id + " no longer exists.", enfe);
            }
            List<Usuario> listadoUsuarios = rol.getListadoUsuarios();
            for (Usuario listadoUsuariosUsuario : listadoUsuarios) {
                listadoUsuariosUsuario.setUnRol(null);
                listadoUsuariosUsuario = em.merge(listadoUsuariosUsuario);
            }
            em.remove(rol);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Rol> findRolEntities() {
        return findRolEntities(true, -1, -1);
    }

    public List<Rol> findRolEntities(int maxResults, int firstResult) {
        return findRolEntities(false, maxResults, firstResult);
    }

    private List<Rol> findRolEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Rol.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Rol findRol(int id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Rol.class, id);
        } finally {
            em.close();
        }
    }

    public int getRolCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Rol> rt = cq.from(Rol.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
