/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package HeroMaker;

import HeroMaker.exceptions.NonexistentEntityException;
import HeroMaker.exceptions.PreexistingEntityException;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author Fidyle
 */
public class HeroJpaController implements Serializable {

    public HeroJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Hero hero) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Level level = hero.getLevel();
            if (level != null) {
                level = em.getReference(level.getClass(), level.getLevelPK());
                hero.setLevel(level);
            }
            Race idRace = hero.getIdRace();
            if (idRace != null) {
                idRace = em.getReference(idRace.getClass(), idRace.getId());
                hero.setIdRace(idRace);
            }
            em.persist(hero);
            if (level != null) {
                level.getHeroCollection().add(hero);
                level = em.merge(level);
            }
            if (idRace != null) {
                idRace.getHeroCollection().add(hero);
                idRace = em.merge(idRace);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findHero(hero.getId()) != null) {
                throw new PreexistingEntityException("Hero " + hero + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Hero hero) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Hero persistentHero = em.find(Hero.class, hero.getId());
            Level levelOld = persistentHero.getLevel();
            Level levelNew = hero.getLevel();
            Race idRaceOld = persistentHero.getIdRace();
            Race idRaceNew = hero.getIdRace();
            if (levelNew != null) {
                levelNew = em.getReference(levelNew.getClass(), levelNew.getLevelPK());
                hero.setLevel(levelNew);
            }
            if (idRaceNew != null) {
                idRaceNew = em.getReference(idRaceNew.getClass(), idRaceNew.getId());
                hero.setIdRace(idRaceNew);
            }
            hero = em.merge(hero);
            if (levelOld != null && !levelOld.equals(levelNew)) {
                levelOld.getHeroCollection().remove(hero);
                levelOld = em.merge(levelOld);
            }
            if (levelNew != null && !levelNew.equals(levelOld)) {
                levelNew.getHeroCollection().add(hero);
                levelNew = em.merge(levelNew);
            }
            if (idRaceOld != null && !idRaceOld.equals(idRaceNew)) {
                idRaceOld.getHeroCollection().remove(hero);
                idRaceOld = em.merge(idRaceOld);
            }
            if (idRaceNew != null && !idRaceNew.equals(idRaceOld)) {
                idRaceNew.getHeroCollection().add(hero);
                idRaceNew = em.merge(idRaceNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = hero.getId();
                if (findHero(id) == null) {
                    throw new NonexistentEntityException("The hero with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Hero hero;
            try {
                hero = em.getReference(Hero.class, id);
                hero.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The hero with id " + id + " no longer exists.", enfe);
            }
            Level level = hero.getLevel();
            if (level != null) {
                level.getHeroCollection().remove(hero);
                level = em.merge(level);
            }
            Race idRace = hero.getIdRace();
            if (idRace != null) {
                idRace.getHeroCollection().remove(hero);
                idRace = em.merge(idRace);
            }
            em.remove(hero);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Hero> findHeroEntities() {
        return findHeroEntities(true, -1, -1);
    }

    public List<Hero> findHeroEntities(int maxResults, int firstResult) {
        return findHeroEntities(false, maxResults, firstResult);
    }

    private List<Hero> findHeroEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Hero.class));
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

    public Hero findHero(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Hero.class, id);
        } finally {
            em.close();
        }
    }

    public int getHeroCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Hero> rt = cq.from(Hero.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
