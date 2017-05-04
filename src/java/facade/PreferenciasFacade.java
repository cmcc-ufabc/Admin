
package facade;

import controller.HibernateUtil;
import java.util.List;
import javax.ejb.Stateless;
import model.Preferencias;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author erick
 */

@Stateless
public class PreferenciasFacade extends AbstractFacade<Preferencias> {

    public PreferenciasFacade() {
        super(Preferencias.class);
    }
    
    public int retornaPreferencia(Long docente_id, int quadrimestre){
        int preferenciaHorarios = 0;
        Session session = getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(Preferencias.class);
        criteria.add(Restrictions.eq("quadrimestre", quadrimestre));
        criteria.add(Restrictions.eq("pessoa_id", docente_id));
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        
        List<Preferencias> results = criteria.list();
        session.close();
        if(results.size()>0){
            preferenciaHorarios = results.get(0).getPreferenciaHorarios();
        }
        return preferenciaHorarios;
    }
    
    public String docenteObservacoes(Long docente_id, int quadrimestre){
        String observacoes = "";
        Session session = getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(Preferencias.class);
        criteria.add(Restrictions.eq("pessoa_id", docente_id));
        criteria.add(Restrictions.eq("quadrimestre", quadrimestre));
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        
        List<Preferencias> results = criteria.list();
        session.close();
        if(results.size()>0){
            observacoes = results.get(0).getObservacoes();
        }       
        return observacoes;
    }

    @Override
    protected SessionFactory getSessionFactory() {
        return HibernateUtil.getSessionFactory();
    }
}
