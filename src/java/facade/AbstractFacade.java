package facade;

import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Projections;

public abstract class AbstractFacade<T> {

    private final Class<T> entityClass;

    public AbstractFacade(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    protected abstract SessionFactory getSessionFactory();

    /**
     * Salva uma entidade no banco de dados
     *
     * @param entity Entidade a ser salva
     */
    public void save(T entity) {
        Session session = getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        session.save(entity);
        try {
            transaction.commit();
        } finally {
            session.close();
        }
    }

    /**
     * Atualiza uma entidade no banco de dados.
     *
     * @param entity Entidade a ser atualizada
     */
    public void edit(T entity) {
        Session session = getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        session.update(entity);
        try {
            transaction.commit();
        } finally {
            session.close();
        }
    }

    /**
     * Combina os dados de uma entidade no banco de dados e retorna esta
     * entidade atualizada. Caso não exista esta entidade, uma nova é criada.
     *
     * @param entity Entidade a ser combinada
     * @return Entidade atualizada/criada
     */
    public T merge(T entity) {
        Session session = getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        T entidade = (T) session.merge(entity);
        transaction.commit();
        session.close();
        return entidade;
    }

    /**
     * Remove uma entidade do banco de dados.
     *
     * @param entity Entidade do banco de dados a ser removida
     */
    public void remove(T entity) {
        Session session = getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        session.delete(entity);
        try {
            transaction.commit();
        } finally {
            session.close();
        }
    }

    /**
     * Busca uma entidade no banco de dados de acordo com o id.
     *
     * @param id ID da entidade a ser busca
     * @return
     */
    public T find(Long id) {
        Session session = getSessionFactory().openSession();
        T entity = (T) session.get(entityClass, id);
        session.close();
        return entity;
    }

    /**
     * Busca todas as entidades do banco de dados.
     *
     * @return Lista com todas as entidades do banco de dados
     */
    public List<T> findAll() {
        Session session = getSessionFactory().openSession();
        Criteria crit = session.createCriteria(entityClass);
        crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);//faz um select distinct

        //crit.setMaxResults(50);
        List results = crit.list();
        
        session.close();
        return results;
    }
    
    /**
     * Busca todas as entidades do banco de dados dentro de um range específico.
     *
     * @param range Vetor representando o range de dados.
     * @return Lista com todas as entidades do banco de dados
     */
    public List<T> findRange(int[] range) {
        Session session = getSessionFactory().openSession();
        Criteria crit = session.createCriteria(entityClass);
        crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);//faz um select distinct
        crit.setMaxResults(range[1] - range[0]);
        crit.setFirstResult(range[0]);
        List results = crit.list();
        session.close();
        return results;
    }

    /**
     * Conta quanto itens dessa entidade possui o banco de dados.
     *
     * @return A quantidade de itens que possui o banco
     */
    public int count() {
        Session session = getSessionFactory().openSession();
        Criteria crit = session.createCriteria(entityClass);
        int count = ((Number) crit.setProjection(Projections.rowCount()).uniqueResult()).intValue();
        session.close();
        return count;
    }
}
