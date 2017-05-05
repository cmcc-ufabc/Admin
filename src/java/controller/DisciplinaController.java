package controller;

import facade.AfinidadeFacade;
import facade.DisciplinaFacade;
import facade.PessoaFacade;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Named;
import model.Afinidade;
import model.Disciplina;
import model.Pessoa;
import util.AfinidadesLazyModel;
import util.DisciplinaDataModel;
import util.DisciplinaLazyModel;

@Named(value = "disciplinaController")
@SessionScoped
public class DisciplinaController extends Filtros implements Serializable {

    public DisciplinaController() {

    }

    //Guarda a disciplina atual
    private Disciplina disciplina;

    public Disciplina getDisciplina() {
        
        if (disciplina == null) {
            disciplina = new Disciplina();
        }
        return disciplina;
    }

    private Disciplina getDisciplina(Long key) {
        return this.buscar(key);
    }

    public void setDisciplina(Disciplina disciplina) {
        this.disciplina = disciplina;
    }

    @EJB
    private DisciplinaFacade disciplinaFacade;

    @EJB
    private AfinidadeFacade afinidadesFacade;

    @EJB
    private PessoaFacade pessoaFacade;

//--------------------------------------------Cadastro de Disciplina--------------------------------------   
    
    private Disciplina disciplinaSalvar;

    private DisciplinaLazyModel disciplinaLazyModel;

    //Inicia o lazymodel de disciplinas
    @PostConstruct
    public void init() {
        disciplinaLazyModel = new DisciplinaLazyModel(this.listarTodas());
    }

    //Busca os eixos pela sigla
    private String converteEixo(String sigla) {

        char letra = sigla.charAt(2);

        switch (letra) {

            case 'J':
                return "Energia";
            case 'K':
                return "Estrutura da Matéria";
            case 'L':
                return "Processos de Transformação";
            case 'M':
                return "Comunicação e Informação";
            case 'N':
                return "Representação e Simulação";
            case 'O':
                return "Estado, Sociedade e Mercado";
            case 'P':
                return "Pensamento, Expressão e Significado";
            case 'Q':
                return "Espaço, Cultura e Temporalidade";
            case 'R':
                return "Ciência, Tecnologia e Inovação";
            default:
                return "Mais de um eixo";
        }
    }

    //Busca o curso pela sigla
    private String converteCurso(String sigla) {

        switch (sigla.substring(0, 2)) {

            //Cursos do CMCC
            case "MC":

                switch (sigla.charAt(3)) {

                    case 'A':
                        return "Bacharelado em Ciência da Computação";
                    case 'B':
                        return "Bacharelado em Matemática";
                    case 'C':
                        return "Bacharelado em Neurociência";
                    case 'D':
                        return "Licenciatura em Matemática";
                    case 'X':
                        return "Disciplina não ofertada";
                    //Mudar
                    default:
                        return "";
                }
            //Cursos do CECS
            case "ES":

                switch (sigla.charAt(3)) {

                    case 'P':
                        return "Bacharelado em Políticas Públicas";
                    case 'S':
                        return "Engenharia Aeroespacial";
                    case 'U':
                        return "Engenharia Ambiental e Urbana";
                    case 'A':
                        return "Engenharia de Automação e Robótica";
                    case 'B':
                        return "Engenharia Biomédica";
                    case 'E':
                        return "Engenharia de Energia";
                    case 'G':
                        return "Engenharia de Gestão";
                    case 'I':
                        return "Engenharia de Informação";
                    case 'M':
                        return "Engenharia de Materiais";
                    case 'C':
                        return "Bacharelado em Economia";
                    case 'R':
                        return "Bacharelado em Relações Internacionais";
                    case 'T':
                        return "Bacharelado em Planejamento Territorial";
                    case 'O':
                        return "Engenharias";
                    case 'X':
                        return "Disciplina não ofertada";
                    //Mudar
                    default:
                        return "";
                }
            //Cursos do CCNH
            case "NH":

                switch (sigla.charAt(3)) {

                    case '1':
                        return "Ciências Biológicas";
                    case '2':
                        return "Filosofia";
                    case '3':
                        return "Física";
                    case '4':
                        return "Química";
                    case '5':
                        return "Licenciaturas";
                    //Mudar
                    default:
                        return "";
                }
        }
        return null;
    }
    
    //Além dos eixos foi colocado o curso referente dos BI's para buscar por cursos nos filtros
    private String converteBI(String sigla){
        switch (sigla.substring(0, 2)) {
            case "BI":
                return "Comum aos BI's";
            case "BC":
                return "Bacharelado em Ciência e Tecnologia";
            case "BH":
                return "Bacharelado em Ciência e Humanidades";
            default:
                return "";
        }
    }

    //Método de cadastro de disciplinas
    public void cadastrarDisciplinas() {

        String[] palavras;
        String eixo;
        String curso;

        try {
            try (BufferedReader lerArq = new BufferedReader(new InputStreamReader(new FileInputStream("/home/charles/alocacao/Arquivos Alocação/Arquivos CSV/CatalogoDisciplinas_2016.csv"), "UTF-8"))) {
                String linha = lerArq.readLine(); //cabeçalho
                
                linha = lerArq.readLine();
                
                while (linha != null) {
//                linha = linha.replace("\"", "");
                    
                    palavras = linha.split(";");
                    //String nome = palavras[1];
                    
                    List<Disciplina> disciplinaExist = disciplinaFacade.findByName(palavras[1]);//2
                    
                    if (disciplinaExist.isEmpty()) {
                        Disciplina d = new Disciplina();
                        d.setNome(palavras[1]);//2
                        d.setCodigo(palavras[0]);//1
                        
                        if (palavras[0].substring(0, 2).equals("BC") || palavras[0].substring(0, 2).equals("BH")
                                || palavras[0].substring(0, 2).equals("BI")) {
                            
                            eixo = converteEixo(palavras[0]);
                            d.setEixo(eixo);
                            //Acréscimo do curso nos BI's
                            curso = converteBI(palavras[0]);
                            d.setCurso(curso);
                        } else {
                            curso = converteCurso(palavras[0]);
                            d.setCurso(curso); 
                        }
                        disciplinaFacade.save(d);
                    }
                    linha = lerArq.readLine();
                }
            } //cabeçalho
        } catch (IOException e) {
            System.err.printf("Erro na abertura do arquivo: %s.\n", e.getMessage());
        }
        Disciplina d = new Disciplina();
        recriarModelo();

        JsfUtil.addSuccessMessage("Cadastro de disciplinas realizado com sucesso");
    }
    
    //Deleta todas as disciplinas
    public void deleteAll() {

        try {
            //Todas as disciplinas existentes
            List<Disciplina> disciplinas = disciplinaFacade.findAll();

            //Itera sobre cada disciplina para apagar as afinidades, caso existam
            for (Disciplina d : disciplinas) {
                d = disciplinaFacade.inicializarColecaoAfinidades(d);
                Set<Afinidade> afinidades = d.getAfinidades();
                Pessoa docente;

                for (Afinidade a : afinidades) {
                    d.getAfinidades().remove(a);
                    docente = a.getPessoa();
                    docente.getAfinidades().remove(a);
                    pessoaFacade.edit(docente);

                    //Atualizar o usuário atual caso a disciplina apagada tivesse
                    //alguma relação com ele
                    //Mudar depois
                    if (docente.getNome().equals(LoginBean.getUsuario().getNome())) {
                        LoginBean.setUsuario(docente);
                    }
                    afinidadesFacade.remove(a);
                }
                disciplinaFacade.remove(d); 
            }
            JsfUtil.addSuccessMessage("Disciplinas Deletadas");
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, "Ocorreu um erro de persistência");
        }
        recriarModelo();
    }

    //Preenche o lazymodel
    public DisciplinaLazyModel getDisciplinaLazyModel() {

        if (disciplinaLazyModel == null) {
            disciplinaLazyModel = new DisciplinaLazyModel(this.listarTodas());
        }

        return this.disciplinaLazyModel;
    }

    public void recriarModelo() {

        disciplinaLazyModel = null;
    }

    //Disciplina a ser salva
    public Disciplina getDisciplinaSalvar() {

        if (disciplinaSalvar == null) {
            disciplinaSalvar = new Disciplina();
        }

        return disciplinaSalvar;
    }

    public void setDisciplinaSalvar(Disciplina disciplinaSalvar) {
        this.disciplinaSalvar = disciplinaSalvar;
    }
    
//------------------------------------------Resumo de Afinidades----------------------------------------------------
    
    private DisciplinaDataModel disciplinaDataModel;

    /**
     * Quantos docentes escolheram aquela disciplina como tendo afinidade
     * Para mostrar na tabela de resumo
     * @param d 
     * @return  
     */
    public int qdtAfinidades(Disciplina d) {

        d = disciplinaFacade.inicializarColecaoAfinidades(d);

        Set<Afinidade> afinidades = d.getAfinidades();
        int qtd = 0;
        for (Afinidade a : afinidades) {
            if (a.getEstado().equals("Adicionada")) {
                qtd++;
            }
        }

        return qtd;
    }

    //Lista de afinidades dos docentes com aquela disciplina
    private AfinidadesLazyModel afinidadesDaDisciplina;
    
    //Caso na visualização dos detalhes das afinidades seja desejado ver apenas as diciplinas que 
    //foram adicionadas como afinidades
    private boolean mostrarAdicionadas;
    
    private Disciplina selecionada;

    //Preenche o data model com os docentes que escolheram aquela disciplina como afinidade
    //para visualização dos detalhes
    public void preencherAfinidadesDisciplina() {

        List<Afinidade> afinidades;
        if (selecionada != null) {
            afinidades = new ArrayList<>(selecionada.getAfinidades());

        } else {
            afinidades = new ArrayList<>();
        }

        mostrarAdicionadas = false;
        afinidadesDaDisciplina = new AfinidadesLazyModel(afinidades);
    }

    //Visualizar disciplinas adicionadas como afinidade
    public void verSoAdicionadas() {

        List<Afinidade> afinidades = new ArrayList<>(selecionada.getAfinidades());
        List<Afinidade> adicionadas = new ArrayList<>();

        if (mostrarAdicionadas) {
            for (Afinidade a : afinidades) {
                if (a.getEstado().equals("Adicionada")) {
                    adicionadas.add(a);
                }
            }

            afinidadesDaDisciplina = new AfinidadesLazyModel(adicionadas);
        } else {
            afinidadesDaDisciplina = new AfinidadesLazyModel(afinidades);
        }
    }
    
    public void limparSelecao() {
        selecionada = null;
        afinidadesDaDisciplina = null;
    }

    public DisciplinaDataModel getDisciplinaDataModel() {
        if (disciplinaDataModel == null) {
            disciplinaDataModel = new DisciplinaDataModel(this.listarTodas());
        }

        return disciplinaDataModel;
    }

    public void setDisciplinaDataModel(DisciplinaDataModel disciplinaDataModel) {
        this.disciplinaDataModel = disciplinaDataModel;
    }

    public AfinidadesLazyModel getAfinidadesDaDisciplina() {

        return afinidadesDaDisciplina;
    }

    public void setAfinidadesDaDisciplina(AfinidadesLazyModel afinidadesDaDisciplina) {
        this.afinidadesDaDisciplina = afinidadesDaDisciplina;
    }
    
    public boolean isMostrarAdicionadas() {
        return mostrarAdicionadas;
    }

    public void setMostrarAdicionadas(boolean mostrarAdicionadas) {
        this.mostrarAdicionadas = mostrarAdicionadas;
    }
    
    public Disciplina getSelecionada() {
        return selecionada;
    }

    public void setSelecionada(Disciplina selecionada) {
        this.selecionada = selecionada;
    }

//------------------------------------------Filtros de Disciplina-------------------------------------------
    
    //Método para filtrar disciplinas
    public void filtrar() {

        List<Disciplina> disciplinasFiltradas = disciplinaFacade.findByEixoCurso(super.getFiltrosSelecEixos(), super.getFiltrosSelecCursos());

        disciplinaDataModel = new DisciplinaDataModel(disciplinasFiltradas);
        
        super.setFiltrosSelecEixos(null);
        super.setFiltrosSelecCursos(null);
    }

    //Limpar filtro
    public void limparFiltro() {
        disciplinaDataModel = null;
    }

//-------------------------------------------Metodos de CRUD--------------------------------------------------------------------------------
    
    //Salvar disciplina
    public void salvar() {
        try {
            disciplinaFacade.save(disciplinaSalvar);
            JsfUtil.addSuccessMessage("Disciplina " + disciplinaSalvar.getNome() + " cadastrada com sucesso!");
            disciplinaSalvar = null;
            disciplinaLazyModel = null;
        } catch (Exception e) {
            JsfUtil.addErrorMessage("Não foi possível cadastrar a disciplina");
        }
    }

    //Editar disciplina
    public void editar() {
        try {
            disciplinaFacade.merge(disciplina);
            JsfUtil.addSuccessMessage("Disciplina " + disciplina.getNome() + " editada com sucesso!");
            disciplina = new Disciplina();
            disciplinaLazyModel = null;
        } catch (Exception e) {
            JsfUtil.addErrorMessage("Não foi possível editar a disciplina");
        }
    }

    //Deletar disciplina
    public void delete() {
        disciplina = (Disciplina) disciplinaLazyModel.getRowData();

        try {
            disciplina = disciplinaFacade.inicializarColecaoAfinidades(disciplina);
            Set<Afinidade> afinidades = disciplina.getAfinidades();
            Pessoa atual;

            for (Afinidade a : afinidades) {
                disciplina.getAfinidades().remove(a);
                atual = a.getPessoa();
                atual.getAfinidades().remove(a);
//               disciplinaFacade.edit(disciplina);
                pessoaFacade.edit(atual);

                if (atual.getNome().equals(LoginBean.getUsuario().getNome())) {
                    LoginBean.setUsuario(atual);
                }
                afinidadesFacade.remove(a);
            }
//           OfertaController tpc = new OfertaController();
            disciplinaFacade.remove(disciplina);
            disciplina = null;
            JsfUtil.addSuccessMessage("Disciplina Deletada");
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, "Ocorreu um erro de persistência");
        }
        recriarModelo();
    }

    //Buscar por id
    public Disciplina buscar(Long id) {
        return disciplinaFacade.find(id);
    }

    //Buscar todas as disciplinas
    private List<Disciplina> listarTodas() {
        return disciplinaFacade.findAll();
    }

//---------------------------------------------Páginas web------------------------------------------------------------
    
    public String prepareCreate(int i) {
        disciplina = new Disciplina();
        if (i == 1) {
            return "/view/disciplina/Create";
        } else {
            return "Create";
        }
    }

    public String index() {
        disciplina = null;
        disciplinaLazyModel = null;
        return "/index";
    }

    public String prepareEdit() {
        disciplina = (Disciplina) disciplinaLazyModel.getRowData();
        return "/Cadastro/editDisciplina";
    }

    public String prepareView() {
        disciplina = (Disciplina) disciplinaLazyModel.getRowData();
        return "View";
    }

//-----------------------------------------------------------------------------------------------------------------------------
    
@FacesConverter(forClass = Disciplina.class)
    public static class DisciplinaControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            DisciplinaController controller = (DisciplinaController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "disciplinaController");
            return controller.getDisciplina(getKey(value));
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
            if (object instanceof Disciplina) {
                Disciplina d = (Disciplina) object;
                return getStringKey(new BigDecimal(d.getID().toString()).setScale(0, BigDecimal.ROUND_HALF_UP).longValue());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Disciplina.class.getName());
            }
        }
    }

}
