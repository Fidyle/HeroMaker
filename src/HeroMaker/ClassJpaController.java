/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package HeroMaker;

import HeroMaker.exceptions.IllegalOrphanException;
import HeroMaker.exceptions.NonexistentEntityException;
import HeroMaker.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Fidyle
 */
public class ClassJpaController implements Serializable {

    public ClassJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Class class1) throws PreexistingEntityException, Exception {
        if (class1.getLevelCollection() == null) {
            class1.setLevelCollection(new ArrayList<Level>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Level> attachedLevelCollection = new ArrayList<Level>();
            for (Level levelCollectionLevelToAttach : class1.getLevelCollection()) {
                levelCollectionLevelToAttach = em.getReference(levelCollectionLevelToAttach.getClass(), levelCollectionLevelToAttach.getLevelPK());
                attachedLevelCollection.add(levelCollectionLevelToAttach);
            }
            class1.setLevelCollection(attachedLevelCollection);
            em.persist(class1);
            for (Level levelCollectionLevel : class1.getLevelCollection()) {
                Class oldClass1OfLevelCollectionLevel = levelCollectionLevel.getClass1();
                levelCollectionLevel.setClass1(class1);
                levelCollectionLevel = em.merge(levelCollectionLevel);
                if (oldClass1OfLevelCollectionLevel != null) {
                    oldClass1OfLevelCollectionLevel.getLevelCollection().remove(levelCollectionLevel);
                    oldClass1OfLevelCollectionLevel = em.merge(oldClass1OfLevelCollectionLevel);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findClass(class1.getId()) != null) {
                throw new PreexistingEntityException("Class " + class1 + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Class class1) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Class persistentClass = em.find(Class.class, class1.getId());
            Collection<Level> levelCollectionOld = persistentClass.getLevelCollection();
            Collection<Level> levelCollectionNew = class1.getLevelCollection();
            List<String> illegalOrphanMessages = null;
            for (Level levelCollectionOldLevel : levelCollectionOld) {
                if (!levelCollectionNew.contains(levelCollectionOldLevel)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Level " + levelCollectionOldLevel + " since its class1 field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Level> attachedLevelCollectionNew = new ArrayList<Level>();
            for (Level levelCollectionNewLevelToAttach : levelCollectionNew) {
                levelCollectionNewLevelToAttach = em.getReference(levelCollectionNewLevelToAttach.getClass(), levelCollectionNewLevelToAttach.getLevelPK());
                attachedLevelCollectionNew.add(levelCollectionNewLevelToAttach);
            }
            levelCollectionNew = attachedLevelCollectionNew;
            class1.setLevelCollection(levelCollectionNew);
            class1 = em.merge(class1);
            for (Level levelCollectionNewLevel : levelCollectionNew) {
                if (!levelCollectionOld.contains(levelCollectionNewLevel)) {
                    Class oldClass1OfLevelCollectionNewLevel = levelCollectionNewLevel.getClass1();
                    levelCollectionNewLevel.setClass1(class1);
                    levelCollectionNewLevel = em.merge(levelCollectionNewLevel);
                    if (oldClass1OfLevelCollectionNewLevel != null && !oldClass1OfLevelCollectionNewLevel.equals(class1)) {
                        oldClass1OfLevelCollectionNewLevel.getLevelCollection().remove(levelCollectionNewLevel);
                        oldClass1OfLevelCollectionNewLevel = em.merge(oldClass1OfLevelCollectionNewLevel);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = class1.getId();
                if (findClass(id) == null) {
                    throw new NonexistentEntityException("The class with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Class class1;
            try {
                class1 = em.getReference(Class.class, id);
                class1.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The class1 with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Level> levelCollectionOrphanCheck = class1.getLevelCollection();
            for (Level levelCollectionOrphanCheckLevel : levelCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Class (" + class1 + ") cannot be destroyed since the Level " + levelCollectionOrphanCheckLevel + " in its levelCollection field has a non-nullable class1 field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(class1);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Class> findClassEntities() {
        return findClassEntities(true, -1, -1);
    }

    public List<Class> findClassEntities(int maxResults, int firstResult) {
        return findClassEntities(false, maxResults, firstResult);
    }

    private List<Class> findClassEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Class.class));
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

    public Class findClass(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Class.class, id);
        } finally {
            em.close();
        }
    }

    public int getClassCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Class> rt = cq.from(Class.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
