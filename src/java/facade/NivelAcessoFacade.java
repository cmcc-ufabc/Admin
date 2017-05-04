package facade;

import controller.HibernateUtil;
import java.util.List;
import javax.ejb.Stateless;
import model.NivelAcesso;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author erick
 */
@Stateless
public class NivelAcessoFacade extends AbstractFacade<NivelAcesso>{
    public NivelAcessoFacade(){
        super(NivelAcesso.class);
    }
    
    @Override
    protected SessionFactory getSessionFactory() {
        return HibernateUtil.getSessionFactory();
    }
    
    //Verifica se o docente é adm
    public NivelAcesso achaAdm(Long idDocente){
        Session session = getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(NivelAcesso.class);
        criteria.add(Restrictions.eq("pessoa_id", idDocente));

        List<NivelAcesso> result = criteria.list();
        session.close();

        return result.get(0);
    }
}
