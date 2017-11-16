/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package HeroMaker;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Fidyle
 */
@Entity
@Table(name = "SPELL")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Spell.findAll", query = "SELECT s FROM Spell s")
    , @NamedQuery(name = "Spell.findById", query = "SELECT s FROM Spell s WHERE s.id = :id")
    , @NamedQuery(name = "Spell.findByName", query = "SELECT s FROM Spell s WHERE s.name = :name")
    , @NamedQuery(name = "Spell.findByDescription", query = "SELECT s FROM Spell s WHERE s.description = :description")
    , @NamedQuery(name = "Spell.findByDamage", query = "SELECT s FROM Spell s WHERE s.damage = :damage")
    , @NamedQuery(name = "Spell.findByElement", query = "SELECT s FROM Spell s WHERE s.element = :element")})
public class Spell implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "NAME")
    private String name;
    @Basic(optional = false)
    @Column(name = "DESCRIPTION")
    private String description;
    @Basic(optional = false)
    @Column(name = "DAMAGE")
    private int damage;
    @Basic(optional = false)
    @Column(name = "ELEMENT")
    private String element;
    @ManyToMany(mappedBy = "spellCollection")
    private Collection<Level> levelCollection;

    public Spell() {
    }

    public Spell(Integer id) {
        this.id = id;
    }

    public Spell(Integer id, String name, String description, int damage, String element) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.damage = damage;
        this.element = element;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public String getElement() {
        return element;
    }

    public void setElement(String element) {
        this.element = element;
    }

    @XmlTransient
    public Collection<Level> getLevelCollection() {
        return levelCollection;
    }

    public void setLevelCollection(Collection<Level> levelCollection) {
        this.levelCollection = levelCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Spell)) {
            return false;
        }
        Spell other = (Spell) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "HeroMaker.Spell[ id=" + id + " ]";
    }
    
}
