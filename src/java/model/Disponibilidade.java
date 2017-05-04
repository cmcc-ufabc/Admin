package model;

import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Disponibilidade implements Serializable {

    @Embeddable
    public static class Id implements Serializable {

        private Long pessoaId;

        private Long turmaId;

        public Id() {
        }

        public Id(Long pessoaId, Long turmaId) {
            this.pessoaId = pessoaId;
            this.turmaId = turmaId;
        }

        @Override
        public boolean equals(Object o) {
            if (o != null && o instanceof Id) {
                Id that = (Id) o;
                return this.pessoaId.equals(that.pessoaId) && this.turmaId.equals(that.turmaId);
            } else {
                return false;
            }
        }

        @Override
        public int hashCode() {
            return pessoaId.hashCode() + turmaId.hashCode();
        }
    }

    @EmbeddedId
    private Id id = new Id();
    
    /*@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long Id;*/

    private String ordemPreferencia;
  
    //Se o docente quer dar teoria, pratica ou ambos
    private String tp;

    @ManyToOne
    private Pessoa pessoa;

    @ManyToOne(cascade = CascadeType.ALL)
    private OfertaDisciplina ofertaDisciplina;

    public Disponibilidade() {
    }

    public Disponibilidade(String ordem, Pessoa p, OfertaDisciplina oD) {

        this.pessoa = p;

        this.ofertaDisciplina = oD;

        this.ordemPreferencia = ordem;

        this.id.pessoaId = p.getID();
        this.id.turmaId = oD.getID();

        //Integridade Referencial
        pessoa.getDisponibilidades().add(Disponibilidade.this);
        ofertaDisciplina.getDisponibilidades().add(Disponibilidade.this);
    }
    
    public Disponibilidade(String ordem, String tp, Pessoa p, OfertaDisciplina oD) {

        this.pessoa = p;

        this.ofertaDisciplina = oD;
        
        this.tp = tp;

        this.ordemPreferencia = ordem;

        this.id.pessoaId = p.getID();
        this.id.turmaId = oD.getID();

        //Integridade Referencial
        pessoa.getDisponibilidades().add(Disponibilidade.this);
        ofertaDisciplina.getDisponibilidades().add(Disponibilidade.this);
    }

    public Id getId() {
        return id;
    }

    public void setId(Id id) {
        this.id = id;
    }
    
    /*public Long getId(){
        return Id;
    }
    
    public void setId(Long Id){
        this.Id = Id;
    }*/

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
    
    /*@Override
    public int hashCode() {
        int hash = 0;
        hash += (Id != null ? Id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Disponibilidade)) {
            return false;
        }
        Disponibilidade other = (Disponibilidade) object;
        if ((this.Id == null && other.Id != null) || (this.Id != null && !this.Id.equals(other.Id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Disponibilidade[ Id=" + Id + " ]";
    }*/
}
