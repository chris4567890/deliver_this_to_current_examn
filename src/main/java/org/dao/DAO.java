package org.dao;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import org.config.HibernateConfig;
import org.jetbrains.annotations.NotNull;
import org.model.Car;

import java.util.List;
import java.util.function.Consumer;
//source for this code is one of my group projects.
public abstract class DAO<T,K> implements IDAO<T, K> {

    Class<T> entityClass;
    public static EntityManagerFactory emf;

    public DAO(Class<T> tClass, boolean isTesting){
        if(isTesting){
            emf = HibernateConfig.getEntityManagerFactoryConfig(isTesting);
        }else{
            emf = HibernateConfig.getEntityManagerFactoryConfig(isTesting);
        }
        entityClass = tClass;
    }
    public List<T> getAll(){
        try(var em = emf.createEntityManager()){
            TypedQuery<T> query = em.createQuery("select h from "+entityClass.getSimpleName()+" h", entityClass);
            return query.getResultList();
        }
    }




    public T getById(K in, @NotNull Consumer<T> initializer){
        try(var em = emf.createEntityManager()){
            T result = em.find(entityClass, in);
            initializer.accept(result);
            return result;
        }
    }

    public void create(T in){
        try(var em = emf.createEntityManager()){
            em.getTransaction().begin();
            em.persist(in);
            em.getTransaction().commit();
        }
    }

    public T update(T in, K id){
        try(var em = emf.createEntityManager()){
            em.getTransaction().begin();
            T found = em.find(entityClass, id);
            if(found != null){
                T merged = em.merge(in);
                em.getTransaction().commit();
                return merged;
            }
        }
        return null;
    }

    public T delete(K id){
        try(var em = emf.createEntityManager()){
            em.getTransaction().begin();
            T found = em.find(entityClass, id);
            if(found != null){
                em.remove(found);
                em.getTransaction().commit();
                return found;
            }
        }
        return null;
    }


    



}
