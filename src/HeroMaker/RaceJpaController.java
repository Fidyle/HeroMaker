/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package HeroMaker;

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
public class RaceJpaController implements Serializable {

    public RaceJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Race race) throws PreexistingEntityException, Exception {
        if (race.getHeroCollection() == null) {
            race.setHeroCollection(new ArrayList<Hero>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Hero> attachedHeroCollection = new ArrayList<Hero>();
            for (Hero heroCollectionHeroToAttach : race.getHeroCollection()) {
                heroCollectionHeroToAttach = em.getReference(heroCollectionHeroToAttach.getClass(), heroCollectionHeroToAttach.getId());
                attachedHeroCollection.add(heroCollectionHeroToAttach);
            }
            race.setHeroCollection(attachedHeroCollection);
            em.persist(race);
            for (Hero heroCollectionHero : race.getHeroCollection()) {
                Race oldIdRaceOfHeroCollectionHero = heroCollectionHero.getIdRace();
                heroCollectionHero.setIdRace(race);
                heroCollectionHero = em.merge(heroCollectionHero);
                if (oldIdRaceOfHeroCollectionHero != null) {
                    oldIdRaceOfHeroCollectionHero.getHeroCollection().remove(heroCollectionHero);
                    oldIdRaceOfHeroCollectionHero = em.merge(oldIdRaceOfHeroCollectionHero);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findRace(race.getId()) != null) {
                throw new PreexistingEntityException("Race " + race + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Race race) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Race persistentRace = em.find(Race.class, race.getId());
            Collection<Hero> heroCollectionOld = persistentRace.getHeroCollection();
            Collection<Hero> heroCollectionNew = race.getHeroCollection();
            Collection<Hero> attachedHeroCollectionNew = new ArrayList<Hero>();
            for (Hero heroCollectionNewHeroToAttach : heroCollectionNew) {
                heroCollectionNewHeroToAttach = em.getReference(heroCollectionNewHeroToAttach.getClass(), heroCollectionNewHeroToAttach.getId());
                attachedHeroCollectionNew.add(heroCollectionNewHeroToAttach);
            }
            heroCollectionNew = attachedHeroCollectionNew;
            race.setHeroCollection(heroCollectionNew);
            race = em.merge(race);
            for (Hero heroCollectionOldHero : heroCollectionOld) {
                if (!heroCollectionNew.contains(heroCollectionOldHero)) {
                    heroCollectionOldHero.setIdRace(null);
                    heroCollectionOldHero = em.merge(heroCollectionOldHero);
                }
            }
            for (Hero heroCollectionNewHero : heroCollectionNew) {
                if (!heroCollectionOld.contains(heroCollectionNewHero)) {
                    Race oldIdRaceOfHeroCollectionNewHero = heroCollectionNewHero.getIdRace();
                    heroCollectionNewHero.setIdRace(race);
                    heroCollectionNewHero = em.merge(heroCollectionNewHero);
                    if (oldIdRaceOfHeroCollectionNewHero != null && !oldIdRaceOfHeroCollectionNewHero.equals(race)) {
                        oldIdRaceOfHeroCollectionNewHero.getHeroCollection().remove(heroCollectionNewHero);
                        oldIdRaceOfHeroCollectionNewHero = em.merge(oldIdRaceOfHeroCollectionNewHero);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = race.getId();
                if (findRace(id) == null) {
                    throw new NonexistentEntityException("The race with id " + id + " no longer exists.");
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
            Race race;
            try {
                race = em.getReference(Race.class, id);
                race.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The race with id " + id + " no longer exists.", enfe);
            }
            Collection<Hero> heroCollection = race.getHeroCollection();
            for (Hero heroCollectionHero : heroCollection) {
                heroCollectionHero.setIdRace(null);
                heroCollectionHero = em.merge(heroCollectionHero);
            }
            em.remove(race);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Race> findRaceEntities() {
        return findRaceEntities(true, -1, -1);
    }

    public List<Race> findRaceEntities(int maxResults, int firstResult) {
        return findRaceEntities(false, maxResults, firstResult);
    }

    private List<Race> findRaceEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Race.class));
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

    public Race findRace(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Race.class, id);
        } finally {
            em.close();
        }
    }

    public int getRaceCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Race> rt = cq.from(Race.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
