/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package HeroMaker;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Fidyle
 */
@Entity
@Table(name = "LEVEL")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Level.findAll", query = "SELECT l FROM Level l")
    , @NamedQuery(name = "Level.findByCharism", query = "SELECT l FROM Level l WHERE l.charism = :charism")
    , @NamedQuery(name = "Level.findByDexterity", query = "SELECT l FROM Level l WHERE l.dexterity = :dexterity")
    , @NamedQuery(name = "Level.findByStrength", query = "SELECT l FROM Level l WHERE l.strength = :strength")
    , @NamedQuery(name = "Level.findByWisdom", query = "SELECT l FROM Level l WHERE l.wisdom = :wisdom")
    , @NamedQuery(name = "Level.findByIdClass", query = "SELECT l FROM Level l WHERE l.levelPK.idClass = :idClass")
    , @NamedQuery(name = "Level.findById", query = "SELECT l FROM Level l WHERE l.levelPK.id = :id")})
public class Level implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected LevelPK levelPK;
    @Column(name = "CHARISM")
    private Integer charism;
    @Column(name = "DEXTERITY")
    private Integer dexterity;
    @Column(name = "STRENGTH")
    private Integer strength;
    @Column(name = "WISDOM")
    private Integer wisdom;
    @JoinTable(name = "ACQUIRE", joinColumns = {
        @JoinColumn(name = "ID_CLASS", referencedColumnName = "ID_CLASS")
        , @JoinColumn(name = "ID_LEVEL", referencedColumnName = "ID")}, inverseJoinColumns = {
        @JoinColumn(name = "ID_SPELL", referencedColumnName = "ID")})
    @ManyToMany
    private Collection<Spell> spellCollection;
    @OneToMany(mappedBy = "level")
    private Collection<Hero> heroCollection;
    @JoinColumn(name = "ID_CLASS", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Class class1;

    public Level() {
    }

    public Level(LevelPK levelPK) {
        this.levelPK = levelPK;
    }

    public Level(int idClass, int id) {
        this.levelPK = new LevelPK(idClass, id);
    }

    public LevelPK getLevelPK() {
        return levelPK;
    }

    public void setLevelPK(LevelPK levelPK) {
        this.levelPK = levelPK;
    }

    public Integer getCharism() {
        return charism;
    }

    public void setCharism(Integer charism) {
        this.charism = charism;
    }

    public Integer getDexterity() {
        return dexterity;
    }

    public void setDexterity(Integer dexterity) {
        this.dexterity = dexterity;
    }

    public Integer getStrength() {
        return strength;
    }

    public void setStrength(Integer strength) {
        this.strength = strength;
    }

    public Integer getWisdom() {
        return wisdom;
    }

    public void setWisdom(Integer wisdom) {
        this.wisdom = wisdom;
    }

    @XmlTransient
    public Collection<Spell> getSpellCollection() {
        return spellCollection;
    }

    public void setSpellCollection(Collection<Spell> spellCollection) {
        this.spellCollection = spellCollection;
    }

    @XmlTransient
    public Collection<Hero> getHeroCollection() {
        return heroCollection;
    }

    public void setHeroCollection(Collection<Hero> heroCollection) {
        this.heroCollection = heroCollection;
    }

    public Class getClass1() {
        return class1;
    }

    public void setClass1(Class class1) {
        this.class1 = class1;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (levelPK != null ? levelPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Level)) {
            return false;
        }
        Level other = (Level) object;
        if ((this.levelPK == null && other.levelPK != null) || (this.levelPK != null && !this.levelPK.equals(other.levelPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "HeroMaker.Level[ levelPK=" + levelPK + " ]";
    }
    
}
