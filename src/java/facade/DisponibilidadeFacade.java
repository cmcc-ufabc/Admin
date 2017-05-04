package facade;

import controller.HibernateUtil;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import model.Disciplina;
import model.Disponibilidade;
import model.Pessoa;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

@Stateless
public class DisponibilidadeFacade extends AbstractFacade<Disponibilidade> {

    public DisponibilidadeFacade() {
        super(Disponibilidade.class);
    }

    @Override
    protected SessionFactory getSessionFactory() {
        return HibernateUtil.getSessionFactory();
    }

    /**
     * Retorna uma lista de disponibilidades de acordo com o docente pesquisado
     * @param docente objeto da classe pessoa
     * @return Lista com as disponibilidades pesquisadas de acordo com o
     * parametro informado
     */
    public List<Disponibilidade> findByDocente(Pessoa docente) {
        try {
            Session session = getSessionFactory().openSession();
            Criteria criteria = session.createCriteria(Disponibilidade.class);
            criteria.add(Restrictions.eq("pessoa", docente));
            criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
            List resultado = criteria.list();

            session.close();
            return resultado;
        } catch (HibernateException e) {
            return null;
        }
    }

    /**
     * Retorna uma lista de disponibilidade de acordo com o docente e o
     * quadrimestre buscados
     * @param docente objeto da classe Pessoa
     * @param quad inteiro representando o quadrimestre
     * @return
     */
    public List<Disponibilidade> findByDocenteQuad(Pessoa docente, int quad) {
        try {
            Session session = getSessionFactory().openSession();
            Criteria criteria = session.createCriteria(Disponibilidade.class);
            criteria.add(Restrictions.eq("pessoa", docente));
            if (quad != 0) {
                criteria.createAlias("ofertaDisciplina", "t").add(Restrictions.eq("t.quadrimestre", quad));
            }
            criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
            List resultado = criteria.list();

            session.close();
            return resultado;
        } catch (HibernateException e) {
            return null;
        }
    }

    /**
     * Retorna uma lista de disponibilidade de acordo com a area de atuação do
     * docente e o quadrimestre buscados
     * @param areasAtuacao lista de String com todos filtros escolhidos
     * @param quad inteiro representando o quadrimestre
     * @return
     */
    public List<Disponibilidade> findByAreaQuad(List<String> areasAtuacao, int quad) {
        try {
            Session session = getSessionFactory().openSession();
            Criteria criteria = session.createCriteria(Disponibilidade.class);
            List<Disponibilidade> disponibilidades = new ArrayList<>();

            if (quad != 0) {
                if (areasAtuacao != null) {
                    for (String a : areasAtuacao) {
                        criteria.createAlias("ofertaDisciplina", "o").add(Restrictions.eq("o.quadrimestre", quad));
                        criteria.createAlias("pessoa", "p").add(Restrictions.eq("p.areaAtuacao", a));
                        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
                        List resultado = criteria.list();
                        disponibilidades.addAll(resultado);
                        criteria = session.createCriteria(Disponibilidade.class);
                    }
                } else {
                    criteria.createAlias("ofertaDisciplina", "o").add(Restrictions.eq("o.quadrimestre", quad));
                    List resultado = criteria.list();
                    disponibilidades.addAll(resultado);
//                   criteria = session.createCriteria(Disponibilidade.class);
                }
            }
            session.close();
            return disponibilidades;
        } catch (HibernateException e) {
            return null;
        }
    }

    /**
     * Retorna a lista de disponibilidade de acordo com a disciplina pesquisada
     * @param disciplina
     * @param campus string opcional
     * @param turno string opcional
     * @param quadrimestre int opcional
     * @return
     */
    public List<Disponibilidade> findByDiscTurCamQuad(Disciplina disciplina,
            String campus, String turno, int quadrimestre) {
        try {
            Session session = getSessionFactory().openSession();
            Criteria crit = session.createCriteria(Disponibilidade.class);
            if (disciplina != null) {
                if (quadrimestre != 0) {
                    crit.add(Restrictions.eq("quadrimestre", quadrimestre));
                }
                crit.createAlias("turma", "t").add(Restrictions.eq("t.disciplina", disciplina));
                if (!campus.equals("")) {
                    crit.add(Restrictions.eq("t.campus", campus));
                }
                if (!turno.equals("")) {
                    crit.add(Restrictions.eq("t.turno", turno));
                }
            } else {
                if (quadrimestre != 0) {
                    crit.add(Restrictions.eq("quadrimestre", quadrimestre));
                }
                crit.createAlias("turma", "t");
                if (!campus.equals("")) {
                    crit.add(Restrictions.eq("t.campus", campus));
                }
                if (!turno.equals("")) {
                    crit.add(Restrictions.eq("t.turno", turno));
                }
            }
            crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
            List result = crit.list();
            return result;
        } catch (HibernateException e) {
            return null;
        }
    }

    //Busca a disponibildiade pela pessoa e oferta
    public Disponibilidade FindDisponibilidade(Long oferta, Pessoa pessoa) {
        
        Session session = getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(Disponibilidade.class);
        criteria.createAlias("ofertaDisciplina", "o").add(Restrictions.eq("o.ID", oferta));
        criteria.add(Restrictions.eq("pessoa", pessoa));
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        
        List<Disponibilidade> result = criteria.list();
        
        session.close();
        
        return result.get(0);

        /*Session session = getSessionFactory().openSession();
         Criteria criteria = session.createCriteria(Disponibilidade.class);
         String ordem = "2";
         criteria.add(Restrictions.eq("ordemPreferencia", ordem));
         List<Disponibilidade> result = criteria.list();
         Disponibilidade d = (Disponibilidade) result.get(0);
         session.close();*/
        //criteria.add(Restrictions.eq("pessoaId", pessoa));
        //criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        //Disponibilidade d = result.get(0);
        /*try {
            session = getSessionFactory().openSession();
            Query query = session.createQuery("delete from Disponibilidade where ofertaDisciplina_ID = :oferta and pessoa_pessoa_id = :pessoa");
            query.setParameter("ofertaDisciplina_ID", oferta);
            query.setParameter("pessoa_pessoa_id", pessoa);
            //List<Disponibilidade> resultado = query.list();
            return d;
        } catch (HibernateException e) {
            return null;
        }*/
    }

    //    /**
//     * Retorna a lista de disponibilidade de acordo com a disciplina pesquisada
//     * @param disciplina
//     * @param campus string opcional
//     * @param turno string opcional
//     * @return lista de disponibilidades de acordo com os filtros
//     */
//    public List<Disponibilidade> findByDiscTurCam(Disciplina disciplina, String campus, String turno){
//        
//        try{
//            
//            Session session = getSessionFactory().openSession();
//            Criteria crit = session.createCriteria(Disponibilidade.class);
//            
//            if(disciplina != null){
//                
//                crit.createAlias("turma", "t").add(Restrictions.eq("t.disciplina", disciplina));
//                
//                if(!campus.equals("")){
//                    crit.add(Restrictions.eq("t.campus", campus));
//                }
//                
//                if(!turno.equals("")){
//                    crit.add(Restrictions.eq("t.turno", turno));
//                }
//                
//                
//            }
//            else{
//                
//                crit.createAlias("turma", "t");
//                
//                if(!campus.equals("")){
//                    crit.add(Restrictions.eq("t.campus", campus));
//                }
//                
//                if(!turno.equals("")){
//                    crit.add(Restrictions.eq("t.turno", turno));
//                }
//            }
//            
//            crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
//            List result = crit.list();
//            return result;
//            
//        }
//        catch(HibernateException e){
//            return null;
//        }
//
//        
//    }
}
