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
public class LevelJpaController implements Serializable {

    public LevelJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Level level) throws PreexistingEntityException, Exception {
        if (level.getLevelPK() == null) {
            level.setLevelPK(new LevelPK());
        }
        if (level.getSpellCollection() == null) {
            level.setSpellCollection(new ArrayList<Spell>());
        }
        if (level.getHeroCollection() == null) {
            level.setHeroCollection(new ArrayList<Hero>());
        }
        level.getLevelPK().setIdClass(level.getClass1().getId());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Class class1 = level.getClass1();
            if (class1 != null) {
                class1 = em.getReference(class1.getClass(), class1.getId());
                level.setClass1(class1);
            }
            Collection<Spell> attachedSpellCollection = new ArrayList<Spell>();
            for (Spell spellCollectionSpellToAttach : level.getSpellCollection()) {
                spellCollectionSpellToAttach = em.getReference(spellCollectionSpellToAttach.getClass(), spellCollectionSpellToAttach.getId());
                attachedSpellCollection.add(spellCollectionSpellToAttach);
            }
            level.setSpellCollection(attachedSpellCollection);
            Collection<Hero> attachedHeroCollection = new ArrayList<Hero>();
            for (Hero heroCollectionHeroToAttach : level.getHeroCollection()) {
                heroCollectionHeroToAttach = em.getReference(heroCollectionHeroToAttach.getClass(), heroCollectionHeroToAttach.getId());
                attachedHeroCollection.add(heroCollectionHeroToAttach);
            }
            level.setHeroCollection(attachedHeroCollection);
            em.persist(level);
            if (class1 != null) {
                class1.getLevelCollection().add(level);
                class1 = em.merge(class1);
            }
            for (Spell spellCollectionSpell : level.getSpellCollection()) {
                spellCollectionSpell.getLevelCollection().add(level);
                spellCollectionSpell = em.merge(spellCollectionSpell);
            }
            for (Hero heroCollectionHero : level.getHeroCollection()) {
                Level oldLevelOfHeroCollectionHero = heroCollectionHero.getLevel();
                heroCollectionHero.setLevel(level);
                heroCollectionHero = em.merge(heroCollectionHero);
                if (oldLevelOfHeroCollectionHero != null) {
                    oldLevelOfHeroCollectionHero.getHeroCollection().remove(heroCollectionHero);
                    oldLevelOfHeroCollectionHero = em.merge(oldLevelOfHeroCollectionHero);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findLevel(level.getLevelPK()) != null) {
                throw new PreexistingEntityException("Level " + level + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Level level) throws NonexistentEntityException, Exception {
        level.getLevelPK().setIdClass(level.getClass1().getId());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Level persistentLevel = em.find(Level.class, level.getLevelPK());
            Class class1Old = persistentLevel.getClass1();
            Class class1New = level.getClass1();
            Collection<Spell> spellCollectionOld = persistentLevel.getSpellCollection();
            Collection<Spell> spellCollectionNew = level.getSpellCollection();
            Collection<Hero> heroCollectionOld = persistentLevel.getHeroCollection();
            Collection<Hero> heroCollectionNew = level.getHeroCollection();
            if (class1New != null) {
                class1New = em.getReference(class1New.getClass(), class1New.getId());
                level.setClass1(class1New);
            }
            Collection<Spell> attachedSpellCollectionNew = new ArrayList<Spell>();
            for (Spell spellCollectionNewSpellToAttach : spellCollectionNew) {
                spellCollectionNewSpellToAttach = em.getReference(spellCollectionNewSpellToAttach.getClass(), spellCollectionNewSpellToAttach.getId());
                attachedSpellCollectionNew.add(spellCollectionNewSpellToAttach);
            }
            spellCollectionNew = attachedSpellCollectionNew;
            level.setSpellCollection(spellCollectionNew);
            Collection<Hero> attachedHeroCollectionNew = new ArrayList<Hero>();
            for (Hero heroCollectionNewHeroToAttach : heroCollectionNew) {
                heroCollectionNewHeroToAttach = em.getReference(heroCollectionNewHeroToAttach.getClass(), heroCollectionNewHeroToAttach.getId());
                attachedHeroCollectionNew.add(heroCollectionNewHeroToAttach);
            }
            heroCollectionNew = attachedHeroCollectionNew;
            level.setHeroCollection(heroCollectionNew);
            level = em.merge(level);
            if (class1Old != null && !class1Old.equals(class1New)) {
                class1Old.getLevelCollection().remove(level);
                class1Old = em.merge(class1Old);
            }
            if (class1New != null && !class1New.equals(class1Old)) {
                class1New.getLevelCollection().add(level);
                class1New = em.merge(class1New);
            }
            for (Spell spellCollectionOldSpell : spellCollectionOld) {
                if (!spellCollectionNew.contains(spellCollectionOldSpell)) {
                    spellCollectionOldSpell.getLevelCollection().remove(level);
                    spellCollectionOldSpell = em.merge(spellCollectionOldSpell);
                }
            }
            for (Spell spellCollectionNewSpell : spellCollectionNew) {
                if (!spellCollectionOld.contains(spellCollectionNewSpell)) {
                    spellCollectionNewSpell.getLevelCollection().add(level);
                    spellCollectionNewSpell = em.merge(spellCollectionNewSpell);
                }
            }
            for (Hero heroCollectionOldHero : heroCollectionOld) {
                if (!heroCollectionNew.contains(heroCollectionOldHero)) {
                    heroCollectionOldHero.setLevel(null);
                    heroCollectionOldHero = em.merge(heroCollectionOldHero);
                }
            }
            for (Hero heroCollectionNewHero : heroCollectionNew) {
                if (!heroCollectionOld.contains(heroCollectionNewHero)) {
                    Level oldLevelOfHeroCollectionNewHero = heroCollectionNewHero.getLevel();
                    heroCollectionNewHero.setLevel(level);
                    heroCollectionNewHero = em.merge(heroCollectionNewHero);
                    if (oldLevelOfHeroCollectionNewHero != null && !oldLevelOfHeroCollectionNewHero.equals(level)) {
                        oldLevelOfHeroCollectionNewHero.getHeroCollection().remove(heroCollectionNewHero);
                        oldLevelOfHeroCollectionNewHero = em.merge(oldLevelOfHeroCollectionNewHero);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                LevelPK id = level.getLevelPK();
                if (findLevel(id) == null) {
                    throw new NonexistentEntityException("The level with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(LevelPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Level level;
            try {
                level = em.getReference(Level.class, id);
                level.getLevelPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The level with id " + id + " no longer exists.", enfe);
            }
            Class class1 = level.getClass1();
            if (class1 != null) {
                class1.getLevelCollection().remove(level);
                class1 = em.merge(class1);
            }
            Collection<Spell> spellCollection = level.getSpellCollection();
            for (Spell spellCollectionSpell : spellCollection) {
                spellCollectionSpell.getLevelCollection().remove(level);
                spellCollectionSpell = em.merge(spellCollectionSpell);
            }
            Collection<Hero> heroCollection = level.getHeroCollection();
            for (Hero heroCollectionHero : heroCollection) {
                heroCollectionHero.setLevel(null);
                heroCollectionHero = em.merge(heroCollectionHero);
            }
            em.remove(level);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Level> findLevelEntities() {
        return findLevelEntities(true, -1, -1);
    }

    public List<Level> findLevelEntities(int maxResults, int firstResult) {
        return findLevelEntities(false, maxResults, firstResult);
    }

    private List<Level> findLevelEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Level.class));
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

    public Level findLevel(LevelPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Level.class, id);
        } finally {
            em.close();
        }
    }

    public int getLevelCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Level> rt = cq.from(Level.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
