/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package HeroMaker;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author Fidyle
 */
@Embeddable
public class LevelPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "ID_CLASS")
    private int idClass;
    @Basic(optional = false)
    @Column(name = "ID")
    private int id;

    public LevelPK() {
    }

    public LevelPK(int idClass, int id) {
        this.idClass = idClass;
        this.id = id;
    }

    public int getIdClass() {
        return idClass;
    }

    public void setIdClass(int idClass) {
        this.idClass = idClass;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) idClass;
        hash += (int) id;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof LevelPK)) {
            return false;
        }
        LevelPK other = (LevelPK) object;
        if (this.idClass != other.idClass) {
            return false;
        }
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "HeroMaker.LevelPK[ idClass=" + idClass + ", id=" + id + " ]";
    }
    
}
