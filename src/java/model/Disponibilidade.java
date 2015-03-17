/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;


@Entity
public class Disponibilidade {
   
    @Embeddable
    public static class Id implements Serializable{
        
        private Long pessoaId;
        
        private Long turmaId;
        
        public Id() {}
        
        public Id(Long pessoaId, Long turmaId){
            this.pessoaId = pessoaId;
            this.turmaId = turmaId;
        }
        
        @Override
        public boolean equals(Object o){
            if(o != null && o instanceof Id){
                Id that = (Id)o;
                return this.pessoaId.equals(that.pessoaId) && this.turmaId.equals(that.turmaId);
            }
            
            else{
                return false;
            }
        }
        
        @Override
        public int hashCode(){
            return pessoaId.hashCode() + turmaId.hashCode();
        }    
        
    }
    
    @EmbeddedId
    private Id id = new Id();
    
    private String horario;
    
//    @Temporal(javax.persistence.TemporalType.DATE)
//    private Date dataAcao;
    
    @ManyToOne 
//    @JoinColumn(name = "pessoaId", referencedColumnName = "pessoa", insertable = false, updatable = false)
    private Pessoa pessoa;
    
    @ManyToOne
//  @JoinColumn(insertable = false, updatable = false)    
    private TurmasPlanejamento turmaPlanejamento;
    
    public Disponibilidade() {}
    
    public Disponibilidade(String horario, Pessoa p, TurmasPlanejamento tP){
        
        this.horario = horario;
        
        this.pessoa = p;
        
        this.turmaPlanejamento = tP;
        
        this.id.pessoaId = p.getID();
        this.id.turmaId = tP.getID();
        
        //Integridade Referencial
        pessoa.getDisponibilidades().add(this);
        turmaPlanejamento.getDisponibilidades().add(this);
    }

    public Id getId() {
        return id;
    }

    public void setId(Id id) {
        this.id = id;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }



    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public TurmasPlanejamento getTurmaPlanejamento() {
        return turmaPlanejamento;
    }

    public void setTurmaPlanejamento(TurmasPlanejamento turmaPlanejamento) {
        this.turmaPlanejamento = turmaPlanejamento;
    }

    
    
    
}