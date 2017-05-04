package model;

import java.io.Serializable;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Disciplina implements Serializable {

    private static final long serialVersionUID = 1L;

    public Disciplina() {

    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="disciplina_id")
    private Long ID;
    
    private String nome;
    
    private String eixo;
    
    private String codigo;
    
    private String curso;
    
    @OneToMany(mappedBy = "disciplina", cascade = CascadeType.ALL)
    private Set<Afinidade> afinidades;
    
    @OneToMany(mappedBy = "disciplina", cascade = CascadeType.ALL)
    private Set<OfertaDisciplina> ofertasDisciplinas;

    public Set<OfertaDisciplina> getOfertasDisciplinas() {
        return ofertasDisciplinas;
    }

    public void setOfertasDisciplinas(Set<OfertaDisciplina> ofertasDisciplinas) {
        this.ofertasDisciplinas = ofertasDisciplinas;
    }
    
    Disciplina(Long disciplinaId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Set<Afinidade> getAfinidades() {
        return afinidades;
    }

    public void setAfinidades(Set<Afinidade> afinidades) {
        this.afinidades = afinidades;
    }
 
    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
    
    public String getCodigo(){
        return codigo;
    }
    
    public void setCodigo(String codigo){
        this.codigo = codigo;
    }
    
    public String getEixo() {
        return eixo;
    }

    public void setEixo(String eixo) {
        
        if(eixo.equals("")){
            this.eixo = null;
        }
        else{
            this.eixo = eixo;
        }
    }

    public String getCurso() {
        return curso;
    }

    public void setCurso(String curso) {
        
        if(curso.equals("")){
            this.curso = null;
        }
        else{
            this.curso = curso;
        }  
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ID != null ? ID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {

        if (!(object instanceof Disciplina)) {
            return false;
        }
        Disciplina other = (Disciplina) object;
        if ((this.ID == null && other.ID != null) || (this.ID != null && !(this.ID.equals(other.ID)))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.nome;
    }
}

