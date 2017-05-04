package model;

import java.io.Serializable;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

//Vai ter as informações gerais das turmas planejadas para aquele ano
//Corresponde à primeira etapa da alocação
@Entity
public class OfertaDisciplina implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long ID;

    private String curso;
  
    @ManyToOne
    private Disciplina disciplina;

    //Horas de teoria
    private int t;

    //Horas pratica
    private int p;

    //Turno: D/N
    private String turno;

    //Campus: SA/SB
    private String campus;

    private int numTurmas;

    //Semanal ou quinzenal
    private String periodicidade;

    private int quadrimestre;
    
    @Transient //armazena temporariamente se o docente vai dar teoria e/ou pratica
    private String funcao;

    @OneToMany(mappedBy = "ofertaDisciplina", cascade = CascadeType.ALL)
    private Set<Disponibilidade> disponibilidades;

    public Set<Disponibilidade> getDisponibilidades() {
        return disponibilidades;
    }

    public void setDisponibilidades(Set<Disponibilidade> disponibilidades) {
        this.disponibilidades = disponibilidades;
    }
    
    @OneToMany(mappedBy = "ofertaDisciplina", cascade = CascadeType.ALL)
    private Set<Disp> dispo;

    public Set<Disp> getDispo() {
        return dispo;
    }

    public void setDispo(Set<Disp> dispo) {
        this.dispo = dispo;
    }
    
    public String getCurso() {
        return curso;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }

    public Disciplina getDisciplina() {
        return disciplina;
    }

    public void setDisciplina(Disciplina disciplina) {
        this.disciplina = disciplina;
    }

    public int getT() {
        return t;
    }

    public void setT(int t) {
        this.t = t;
    }

    public int getP() {
        return p;
    }

    public void setP(int p) {
        this.p = p;
    }

    public String getTurno() {
        return turno;
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }

    public String getCampus() {
        return campus;
    }

    public void setCampus(String campus) {
        this.campus = campus;
    }

    public int getNumTurmas() {
        return numTurmas;
    }

    public void setNumTurmas(int numTurmas) {
        this.numTurmas = numTurmas;
    }

    public String getPeriodicidade() {
        return periodicidade;
    }

    public void setPeriodicidade(String periodicidade) {
        this.periodicidade = periodicidade;
    }

    public int getQuadrimestre() {
        return quadrimestre;
    }

    public void setQuadrimestre(int quadrimestre) {
        this.quadrimestre = quadrimestre;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public String getFuncao() {
        
        return funcao;
    }

    public void setFuncao(String funcao) {
        this.funcao = funcao;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ID != null ? ID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {

        if (!(object instanceof OfertaDisciplina)) {
            return false;
        }
        OfertaDisciplina other = (OfertaDisciplina) object;
        if ((this.ID == null && other.ID != null) || (this.ID != null && !(this.ID.equals(other.ID)))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.disciplina.getNome() + " " + this.turno + " " + this.campus + " " + this.curso;
    }
}
