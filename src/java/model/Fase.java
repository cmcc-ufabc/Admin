package model;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;

/**
 *
 * @author erick
 */
@Entity
public class Fase implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue//(strategy = GenerationType.AUTO)
    private Long id;

    private boolean fase1;

    private boolean afinidades;

    private boolean fase2_quad1;

    private boolean fase2_quad2;

    private boolean fase2_quad3;

    private Date dataAtual;

    public Fase() {
    }

    public Fase(boolean afinidades, boolean fase2_quad1, boolean fase2_quad2, boolean fase2_quad3, boolean fase1, Date dataAtual) {
        this.afinidades = afinidades;
        this.fase1 = fase1;
        this.fase2_quad1 = fase2_quad1;
        this.fase2_quad2 = fase2_quad2;
        this.fase2_quad3 = fase2_quad3;
        this.dataAtual = dataAtual;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDataAtual() {
        return dataAtual;
    }

    public void setDataAtual(Date dataAtual) {
        this.dataAtual = dataAtual;
    }
    
    public boolean isAfinidades() {
        return afinidades;
    }

    public void setAfinidades(boolean afinidades) {
        this.afinidades = afinidades;
    }

    public boolean isFase2_quad1() {
        return fase2_quad1;
    }

    public void setFase2_quad1(boolean fase2_quad1) {
        this.fase2_quad1 = fase2_quad1;
    }

    public boolean isFase2_quad2() {
        return fase2_quad2;
    }

    public void setFase2_quad2(boolean fase2_quad2) {
        this.fase2_quad2 = fase2_quad2;
    }

    public boolean isFase2_quad3() {
        return fase2_quad3;
    }

    public void setFase2_quad3(boolean fase2_quad3) {
        this.fase2_quad3 = fase2_quad3;
    }

    public boolean isFase1() {
        return fase1;
    }

    public void setFase1(boolean fase1) {
        this.fase1 = fase1;
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
        if (!(object instanceof Fase)) {
            return false;
        }
        Fase other = (Fase) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Fase[ id=" + id + " ]";
    }
}
