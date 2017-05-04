package model;

import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Disp implements Serializable {
    
    @Id
    @GeneratedValue//(strategy = GenerationType.AUTO)

    private Long id;

    private String ordemPreferencia;
  
    //Se o docente quer dar teoria, pratica ou ambos
    private String tp;

    @ManyToOne
    private Pessoa pessoa;

    @ManyToOne//(cascade = CascadeType.ALL)
    private OfertaDisciplina ofertaDisciplina;

    public Disp() {
    }

    public Disp(String ordem, Pessoa p, OfertaDisciplina oD) {

        this.pessoa = p;

        this.ofertaDisciplina = oD;

        this.ordemPreferencia = ordem;

        //Integridade Referencial
        //pessoa.getDisponibilidades().add(Disp.this);
        //ofertaDisciplina.getDisponibilidades().add(Disponibilidade.this);
    }
    
    public Disp(String ordem, String tp, Pessoa p, OfertaDisciplina oD) {

        this.pessoa = p;

        this.ofertaDisciplina = oD;
        
        this.tp = tp;

        this.ordemPreferencia = ordem;

        //Integridade Referencial
        //pessoa.getDisponibilidades().add(Disponibilidade.this);
        //ofertaDisciplina.getDisponibilidades().add(Disponibilidade.this);
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }
    
    public OfertaDisciplina getOfertaDisciplina() {
        return ofertaDisciplina;
    }

    public void setOfertaDisciplina(OfertaDisciplina ofertaDisciplina) {
        this.ofertaDisciplina = ofertaDisciplina;
    }  

    public String getOrdemPreferencia() {
        return ordemPreferencia;
    }

    public void setOrdemPreferencia(String ordemPreferencia) {
        this.ordemPreferencia = ordemPreferencia;
    }

    public String getTp() {
        return tp;
    }

    public void setTp(String tp) {
        this.tp = tp;
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
        if (!(object instanceof Disp)) {
            return false;
        }
        Disp other = (Disp) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Disp[ id=" + id + " ]";
    }
}
