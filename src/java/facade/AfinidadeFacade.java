package facade;

import controller.HibernateUtil;
import javax.ejb.Stateless;
import model.Afinidade;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

@Stateless
public class AfinidadeFacade extends AbstractFacade<Afinidade>{
    
    public AfinidadeFacade() {
        super(Afinidade.class);
    }

    @Override
    protected SessionFactory getSessionFactory() {

        return HibernateUtil.getSessionFactory();
    }
   
    //Remove afinidade
    @Override
    public void remove(Afinidade a) {
        Session session = getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        session.delete(a);
        try {
            transaction.commit();
        } finally {
            session.close();
        }
    } 
}


