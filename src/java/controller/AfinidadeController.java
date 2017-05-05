package controller;

import facade.AfinidadeFacade;
import facade.DisciplinaFacade;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.SelectItem;
import javax.inject.Named;
import model.Afinidade;
import model.Disciplina;
import model.Pessoa;

@Named(value = "afinidadesController")
@SessionScoped
public class AfinidadeController extends Filtros implements Serializable{
    
    private static final long serialVersionUID = 1L;
    
    public AfinidadeController() {
        
    }
    
    @EJB
    private AfinidadeFacade afinidadeFacade;
    
    @EJB
    private DisciplinaFacade disciplinaFacade;

//-------------------------------------------Definir afinidade-------------------------------------------------------------------------
    //Guarda a afinidade atual
    private Afinidade afinidade;
    
    //Guarda o docente que esta definindo a afinidade
    private Pessoa docente;
  
    //Guarda a disciplina que o docente selecionou para adicionar como tendo afinidade
    private Disciplina paraAdicionar;
 
    //Guarda a disciplina que antes o docente possuía afinidade, mas não possui mais e vai remover
    private Disciplina paraRemover;
 
    //Todas afinidades do docentes, incluindo adicionadas e removidas
    private List<Afinidade> todasAfinidades;
    
    //Disciplinas disponiveis------------------------------------------------------
    private List<Disciplina> disponiveis;

    /**
     * Vê quais disciplinas o docente já escolheu,
     * para exibir só as que ele ainda não selecionou(não estão salvas na afinidade, 
     * ou estão salvas como "Removida", porque ele pode escolher de novo nesse caso)
     * @return 
     */
    public List<Disciplina> getDisponiveis() {

        if (disponiveis == null) {
            
            //Usuário que fez o logon
            docente = LoginBean.getUsuario();

            //Todas as afinidades do usuario
            todasAfinidades = new ArrayList(docente.getAfinidades());
            
            escolhidas = new ArrayList<>();
            
            //Adiciona ao array escolhidas as disciplinas que estao como "Adicionada" em afinidades
            for(Afinidade a: todasAfinidades){
                
                if(a.getEstado().equals("Adicionada")){
                    escolhidas.add(a.getDisciplina());
                }        
            }

            //Seleciona todas disciplinas do banco
            disponiveis = disciplinaFacade.findAll();

            //Remove de todas as disciplinas as que já foram escolhidas
            for (Disciplina e : escolhidas) {
                disponiveis.remove(e);
            }
        }
        return disponiveis;
    }
    
    //Disciplinas escolhidas---------------------------------------------------------
    private List<Disciplina> escolhidas;
 
    /**
     * Filtra as disciplinas da escolha de afinidades por eixos e/ou cursos
     * E atualiza as duas listas de disponíveis e escolhidas
     */
    public void filtrar() {
        
        disponiveis = disciplinaFacade.findByEixoCurso(super.getFiltrosSelecEixos(), super.getFiltrosSelecCursos());
        
        for (Disciplina t : escolhidas) {
            disponiveis.remove(t);
        }       
        super.setFiltrosSelecEixos(null);
        super.setFiltrosSelecCursos(null);
    }
      
    /**
     * Carrega todas as disciplinas disponiveis novamente
     */
    public void limparFiltro(){
        disponiveis = null;        
    }
    
    /**
     * Faz a relação entre a disciplina escolhida e o docente que a escolheu,
     * salvando a afinidade
     */
    public void salvarAfinidade() {
        
        //Data da inclusao/remocao
        Calendar cal = Calendar.getInstance();
        
        //Regarrega o objeto disciplina, inicializando a Colecao de Afinidade(Lazy)
        paraAdicionar = disciplinaFacade.inicializarColecaoAfinidades(paraAdicionar);
        
        afinidade = new Afinidade("Adicionada", cal.getTime(), docente, paraAdicionar);
        
        afinidadeFacade.merge(afinidade);
        
        disponiveis = null;
        paraAdicionar = null;
    }
    
    /**
     * "Remove" uma afinidade, marcando seu status como Removida
     */
    public void removerAfinidade(){
        
        //Percorre a lista de afinidades da docente e muda o status de Adicionada para removida
        for(Afinidade a: todasAfinidades){
            
            if(a.getDisciplina() == paraRemover){

                Calendar cal = Calendar.getInstance();
                a.setDataAcao(cal.getTime());
                a.setEstado("Removida");
                afinidadeFacade.edit(a);
            }
        }        
        disponiveis = null;
        paraRemover = null;
        paraAdicionar = null;           
    }

    //Getters e setters
    public Afinidade getAfinidade() {
        return afinidade;
    }

    public void setAfinidade(Afinidade afinidade) {
        this.afinidade = afinidade;
    }
    
    public Pessoa getPessoa() {
        return docente;
    }

    public void setPessoa(Pessoa pessoa) {
        this.docente = pessoa;
    }
    
    public Disciplina getParaAdicionar() {
        return paraAdicionar;
    }

    public void setParaAdicionar(Disciplina paraAdicionar) {
        this.paraAdicionar = paraAdicionar;
    }
    
    public Disciplina getParaRemover() {
        return paraRemover;
    }

    public void setParaRemover(Disciplina paraRemover) {
        this.paraRemover = paraRemover;
    }
    
    public void setDisponiveis(List<Disciplina> disponiveis) {
        this.disponiveis = disponiveis;
    }
    
    public List<Disciplina> getEscolhidas() {
        return escolhidas;
    }

    public void setEscolhidas(List<Disciplina> escolhidas) {
        this.escolhidas = escolhidas;
    }

//---------------------------------------------------CRUD-------------------------------------------------------
    private List<Afinidade> listarTodas() {
        return afinidadeFacade.findAll();
    }
    
    public Afinidade buscar(Long id) {
        return afinidadeFacade.find(id);
    }

    public SelectItem[] getItemsAvaiableSelectOne() {
        return JsfUtil.getSelectItems(listarTodas(), true);
    }

//----------------------------------------Páginas web------------------------------------------------------------
    public String prepareCreate(int i) {
        afinidade = new Afinidade();
        if (i == 1) {
            return "/view/afinidade/Create";
        } else {
            return "Create";
        }
    }
    
    public String prepareAfinidades(){
        disponiveis = null;
        escolhidas = null;
        return "/Afinidades/DefinirAfinidade";
    }
 
    //---------------------------------------------------------------------------------------------------
    
    @FacesConverter(forClass = Afinidade.class)
    public static class AfinidadesControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            AfinidadeController controller = (AfinidadeController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "afinidadeController");
            return controller.buscar(getKey(value));
        }

        java.lang.Long getKey(String value) {
            java.lang.Long key;
            key = Long.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Long value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Afinidade) {
                Afinidade d = (Afinidade) object;               
                return getStringKey(new BigDecimal(d.getId().toString()).setScale(0, BigDecimal.ROUND_HALF_UP).longValue());

            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Afinidade.class.getName());
            }
        }
    }
}

