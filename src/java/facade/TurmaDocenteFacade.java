package facade;

import controller.HibernateUtil;
import java.util.List;
import javax.ejb.Stateless;
import model.TurmaDocente;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author erick
 */
@Stateless
public class TurmaDocenteFacade extends AbstractFacade<TurmaDocente>{
    
    public TurmaDocenteFacade(){
        super(TurmaDocente.class);
    }
    
    @Override
    protected SessionFactory getSessionFactory() {
        return HibernateUtil.getSessionFactory();
    }
    
    //Lista turmas do Docente
    public List<TurmaDocente> listTurmas(Long id){ 
        
        Session session = getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(TurmaDocente.class);
        criteria.add(Restrictions.eq("idDocente", id));
        //criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

        List results = criteria.list();
        session.close();

        return results;      
    }
    
    //Busca a turma marcada pelo docente
    public TurmaDocente TurmaSelecionada(Long turma, Long docente){
        Session session = getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(TurmaDocente.class);
        criteria.add(Restrictions.eq("idTurma", turma));
        criteria.add(Restrictions.eq("idDocente", docente));
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        
        List<TurmaDocente> results = criteria.list();
        session.close();

        return results.get(0);
    }
    
    //Verifica quantas solicitações a turma possui
    public int totalSolicitacoes(Long id){ //Contagem de Solicitações de uma Turma
        Session session = getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(TurmaDocente.class);
        criteria.add(Restrictions.eq("idTurma", id));

        List result = criteria.list();
        session.close();

        return result.size();
    }
    
    //Retorna quantos docentes marcaram a turma
    public List<TurmaDocente> docentesPorTurma(Long idTurma){
        Session session = getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(TurmaDocente.class);
        criteria.add(Restrictions.eq("idTurma", idTurma));
        //criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

        List results = criteria.list();
        session.close();

        return results; 
    }
    
    //Retorna todas as escolhas do docente
    public int escolhasDocente(Long idDocente){
        Session session = getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(TurmaDocente.class);
        criteria.add(Restrictions.eq("idDocente", idDocente));

        List result = criteria.list();
        session.close();

        return result.size();
    }
}
