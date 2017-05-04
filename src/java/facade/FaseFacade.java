package facade;

import controller.HibernateUtil;
import java.util.List;
import javax.ejb.Stateless;
import model.Fase;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author erick
 */
@Stateless
public class FaseFacade extends AbstractFacade<Fase> {

    public FaseFacade() {
        super(Fase.class);
    }

    @Override
    protected SessionFactory getSessionFactory() {
        return HibernateUtil.getSessionFactory();
    }

    //Retorna o último registro da fase no banco de dados
    public Fase achaMax() {
        try {
            Session session = getSessionFactory().openSession();
            Fase result = (Fase) session.createCriteria(Fase.class)
                    .addOrder(Order.desc("id")).setMaxResults(1).uniqueResult();
            /*Session session = getSessionFactory().openSession();
             Query query = session.createQuery("Select * from Fase where id = ("
             + "select max(id) from Fase)");
             List<Fase> resultado = query.list();*/
            return result;
        } catch (HibernateException e) {
            return null;
        }
    }
}
