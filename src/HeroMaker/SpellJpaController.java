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
public class SpellJpaController implements Serializable {

    public SpellJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Spell spell) throws PreexistingEntityException, Exception {
        if (spell.getLevelCollection() == null) {
            spell.setLevelCollection(new ArrayList<Level>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Level> attachedLevelCollection = new ArrayList<Level>();
            for (Level levelCollectionLevelToAttach : spell.getLevelCollection()) {
                levelCollectionLevelToAttach = em.getReference(levelCollectionLevelToAttach.getClass(), levelCollectionLevelToAttach.getLevelPK());
                attachedLevelCollection.add(levelCollectionLevelToAttach);
            }
            spell.setLevelCollection(attachedLevelCollection);
            em.persist(spell);
            for (Level levelCollectionLevel : spell.getLevelCollection()) {
                levelCollectionLevel.getSpellCollection().add(spell);
                levelCollectionLevel = em.merge(levelCollectionLevel);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findSpell(spell.getId()) != null) {
                throw new PreexistingEntityException("Spell " + spell + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Spell spell) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Spell persistentSpell = em.find(Spell.class, spell.getId());
            Collection<Level> levelCollectionOld = persistentSpell.getLevelCollection();
            Collection<Level> levelCollectionNew = spell.getLevelCollection();
            Collection<Level> attachedLevelCollectionNew = new ArrayList<Level>();
            for (Level levelCollectionNewLevelToAttach : levelCollectionNew) {
                levelCollectionNewLevelToAttach = em.getReference(levelCollectionNewLevelToAttach.getClass(), levelCollectionNewLevelToAttach.getLevelPK());
                attachedLevelCollectionNew.add(levelCollectionNewLevelToAttach);
            }
            levelCollectionNew = attachedLevelCollectionNew;
            spell.setLevelCollection(levelCollectionNew);
            spell = em.merge(spell);
            for (Level levelCollectionOldLevel : levelCollectionOld) {
                if (!levelCollectionNew.contains(levelCollectionOldLevel)) {
                    levelCollectionOldLevel.getSpellCollection().remove(spell);
                    levelCollectionOldLevel = em.merge(levelCollectionOldLevel);
                }
            }
            for (Level levelCollectionNewLevel : levelCollectionNew) {
                if (!levelCollectionOld.contains(levelCollectionNewLevel)) {
                    levelCollectionNewLevel.getSpellCollection().add(spell);
                    levelCollectionNewLevel = em.merge(levelCollectionNewLevel);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = spell.getId();
                if (findSpell(id) == null) {
                    throw new NonexistentEntityException("The spell with id " + id + " no longer exists.");
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
            Spell spell;
            try {
                spell = em.getReference(Spell.class, id);
                spell.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The spell with id " + id + " no longer exists.", enfe);
            }
            Collection<Level> levelCollection = spell.getLevelCollection();
            for (Level levelCollectionLevel : levelCollection) {
                levelCollectionLevel.getSpellCollection().remove(spell);
                levelCollectionLevel = em.merge(levelCollectionLevel);
            }
            em.remove(spell);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Spell> findSpellEntities() {
        return findSpellEntities(true, -1, -1);
    }

    public List<Spell> findSpellEntities(int maxResults, int firstResult) {
        return findSpellEntities(false, maxResults, firstResult);
    }

    private List<Spell> findSpellEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Spell.class));
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

    public Spell findSpell(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Spell.class, id);
        } finally {
            em.close();
        }
    }

    public int getSpellCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Spell> rt = cq.from(Spell.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
