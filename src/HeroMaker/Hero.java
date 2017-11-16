/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package HeroMaker;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Fidyle
 */
@Entity
@Table(name = "HERO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Hero.findAll", query = "SELECT h FROM Hero h")
    , @NamedQuery(name = "Hero.findById", query = "SELECT h FROM Hero h WHERE h.id = :id")
    , @NamedQuery(name = "Hero.findByCreator", query = "SELECT h FROM Hero h WHERE h.creator = :creator")
    , @NamedQuery(name = "Hero.findByName", query = "SELECT h FROM Hero h WHERE h.name = :name")})
public class Hero implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "CREATOR")
    private String creator;
    @Column(name = "NAME")
    private String name;
    @JoinColumns({
        @JoinColumn(name = "ID_CLASS", referencedColumnName = "ID_CLASS")
        , @JoinColumn(name = "ID_LEVEL", referencedColumnName = "ID")})
    @ManyToOne
    private Level level;
    @JoinColumn(name = "ID_RACE", referencedColumnName = "ID")
    @ManyToOne
    private Race idRace;

    public Hero() {
    }

    public Hero(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public Race getIdRace() {
        return idRace;
    }

    public void setIdRace(Race idRace) {
        this.idRace = idRace;
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
        if (!(object instanceof Hero)) {
            return false;
        }
        Hero other = (Hero) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "HeroMaker.Hero[ id=" + id + " ]";
    }
    
}
