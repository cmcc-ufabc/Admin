package model;

import java.io.Serializable;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "PESSOA_DETAILS_TYPE",discriminatorType = DiscriminatorType.STRING)
public class Pessoa implements Serializable, Comparable<Pessoa> {

    private static final long serialVersionUID = 1L;

    public Pessoa() {

    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "pessoa_id")
    private Long ID;

    private String nome;

    private String siape;

    private String email;

    private String centro;

    private boolean adm;
    
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "pessoa", cascade = CascadeType.ALL)
    private Set<Afinidade> afinidades;
    
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "pessoa", cascade = CascadeType.ALL)
    private Set<Disponibilidade> disponibilidades;  
    
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "pessoa", cascade = CascadeType.ALL)
    private Set<Disp> dispo;  

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
    
    public String getSiape() {
        return siape;
    }

    public void setSiape(String siape) {
        this.siape = siape;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCentro() {
        return centro;
    }

    public void setCentro(String centro) {
        this.centro = centro;
    }

    public boolean isAdm() {
        return adm;
    }

    public void setAdm(boolean adm) {
        this.adm = adm;
    }

    public Set<Afinidade> getAfinidades() {
        return afinidades;
    }

    public void setAfinidades(Set<Afinidade> afinidades) {
        this.afinidades = afinidades;
    }

    public Set<Disponibilidade> getDisponibilidades() {
        return disponibilidades;
    }

    public void setDisponibilidades(Set<Disponibilidade> disponibilidades) {
        this.disponibilidades = disponibilidades;
    }
    
    public Set<Disp> getDispo() {
        return dispo;
    }

    public void setDisp(Set<Disp> dispo) {
        this.dispo = dispo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ID != null ? ID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Pessoa)) {
            return false;
        }
        Pessoa other = (Pessoa) object;
        if ((this.ID == null && other.ID != null) || (this.ID != null && !(this.ID.equals(other.ID)))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.nome;
    }
    
    @Override
    public int compareTo(Pessoa o) {
        return this.getNome().compareTo(o.getNome());
    }
}
