package model;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author erick
 */

//Carregará as turmas escolhidas pelo docente
@Entity
//@Table(name = "turmadocente") //Acrescentado
public class TurmaDocente implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue//(strategy = GenerationType.AUTO)
    //@Column(name="Solicitacao_id")
    private Long id;
    
    //private String turma;
    private Long idTurma;
    
    private Long idDocente;
    /*Mudança de Many to one para One to many e uso do SET LIST*/
    //@OneToMany(mappedBy = "requisicao_turma", cascade = CascadeType.ALL)
    //private Set<Docente> docente;
    
    //@ElementCollection (fetch = FetchType.EAGER)
    //private List<Horario> horarios;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getIdTurma(){
        return idTurma;
    }
    
    public void setIdTurma(Long idTurma){
        this.idTurma = idTurma;
    }
    
    /*public String getTurma(){
        return turma;
    }
    
    public void setTurma(String turma){
        this.turma = turma;
    }*/
    
    public Long getIdDocente(){
        return idDocente;
    }
    
    public void setIdDocente(Long idDocente){
        this.idDocente = idDocente;
    }
    
    /*public List<Horario> getHorarios(){
        return horarios;
    }
    
    public void setHorarios(List<Horario> horarios){
        this.horarios = horarios;
    }*/

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TurmaDocente)) {
            return false;
        }
        TurmaDocente other = (TurmaDocente) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.TurmaDocente[ id=" + id + " ]";
    }
}