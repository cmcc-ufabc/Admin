package model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;

@Entity
public class Afinidade implements Serializable{
    
    private static final long serialVersionUID = 1L;
    
    @Embeddable
    public static class Id implements Serializable{
        
        private static final long serialVersionUID = 1L;
        
        private Long pessoaId;
        
        private Long disciplinaId;
        
        public Id() {}
        
        public Id(Long pessoaId, Long disciplinaId){
            this.pessoaId = pessoaId;
            this.disciplinaId = disciplinaId;
        }
        
        @Override
        public boolean equals(Object o){
            if(o != null && o instanceof Id){
                Id that = (Id)o;
                return this.pessoaId.equals(that.pessoaId) && this.disciplinaId.equals(that.disciplinaId);
            }
            else{
                return false;
            }
        }
        
        @Override
        public int hashCode(){
            return pessoaId.hashCode() + disciplinaId.hashCode();
        }
    }
    
    @EmbeddedId
    private Id id = new Id();
 
    private String estado;
    
    //Quando o docente adicionou ou removeu uma afinidade
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dataAcao;
    
    @ManyToOne
    private Pessoa pessoa;
    
    @ManyToOne  
    private Disciplina disciplina;
    
    public Afinidade() {}
    
    public Afinidade(String estado, Date data, Pessoa p, Disciplina d){
        
        this.estado = estado;
        this.dataAcao = data;
        
        this.pessoa = p;
        this.disciplina = d;
        
        this.id.pessoaId = p.getID();
        this.id.disciplinaId = d.getID();
        
        //Integridade Referencial
        pessoa.getAfinidades().add(this);
        disciplina.getAfinidades().add(this);
    }

    public Id getId() {
        return id;
    }

    public void setId(Id id) {
        this.id = id;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Date getDataAcao() {
        return dataAcao;
    }

    public void setDataAcao(Date dataAcao) {
        this.dataAcao = dataAcao;
    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public Disciplina getDisciplina() {
        return disciplina;
    }

    public void setDisciplina(Disciplina disciplina) {
        this.disciplina = disciplina;
    }
}