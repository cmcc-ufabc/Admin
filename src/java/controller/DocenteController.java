package controller;

import facade.CreditoFacade;
import facade.DispFacade;
import facade.DocenteFacade;
import facade.FaseFacade;
import facade.OfertaDisciplinaFacade;
import facade.PessoaFacade;
import facade.TurmaDocenteFacade;
import facade.TurmaFacade;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import model.Afinidade;
import model.Credito;
import model.Disp;
import model.Disponibilidade;
import model.Docente;
import model.Fase;
import model.OfertaDisciplina;
import model.Pessoa;
import model.Turma;
import model.TurmaDocente;
import util.AfinidadeDataModel;
import util.DispDataModel;
import util.DisponibilidadeDataModel;
import util.DocenteDataModel;
import util.PessoaLazyModel;
import util.TurmaDataModel;

@Named(value = "docenteController")
@SessionScoped
public class DocenteController extends Filtros implements Serializable{
    
    public DocenteController(){
        quad = 1; //O primeiro quadrimestre é exibido por default
    }
    
    @EJB
    private DocenteFacade docenteFacade;
    
    @EJB
    private PessoaFacade pessoaFacade;
 
    @EJB
    private CreditoFacade creditoFacade;
    
    @EJB
    private TurmaDocenteFacade turmaFacade;
    
    @EJB
    private TurmaFacade tFacade;
    
    @EJB
    private DispFacade dispFacade;
    
    @EJB
    private OfertaDisciplinaFacade ofertaFacade;
    
    //Docente atual----------------------------------------------------
    private Docente docente;
    
    public Docente getDocente() {
        return docente;
    }

    public void setDocente(Docente docente) {
        this.docente = docente;
    }

//--------------------------------------------Cadastro dos docentes------------------------------------------------------
    
//Docente para salvar
    private Docente docenteSalvar;
    
    public Docente getDocenteSalvar() {
        
        if(docenteSalvar == null){
            docenteSalvar = new Docente();
        }
        return docenteSalvar;
    }

    public void setDocenteSalvar(Docente docenteSalvar) {
        this.docenteSalvar = docenteSalvar;
    } 
    
    private PessoaLazyModel docenteLazyModel;

    public PessoaLazyModel getDocenteLazyModel() {
        
        if(docenteLazyModel == null){
            docenteLazyModel = new PessoaLazyModel(pessoaFacade.listDocentes());
        }
        
        return docenteLazyModel;
    }
    
    //Inicia o lazymodel de docentes
    @PostConstruct
    public void init() {
        docenteLazyModel = new PessoaLazyModel(pessoaFacade.listDocentes());  
    }
    
    //Cadastra docentes
    public void cadastrarDocentes() {

        String[] palavras;

        try {
            try (BufferedReader lerArq = new BufferedReader(new InputStreamReader(new FileInputStream("/home/charles/NetBeansProjects/Arquivos CSV/docentes.csv"), "UTF-8"))) {

                String linha = lerArq.readLine(); //cabeçalho

                linha = lerArq.readLine();

                while (linha != null) {

                    linha = linha.replaceAll("\"", "");

                    palavras = linha.split(",");

                    List<Docente> docentes = docenteFacade.findByName(trataNome(palavras[1]));

                    if (docentes.isEmpty()) {

                        Docente d = new Docente();

                        d.setNome(trataNome(palavras[1]));
                        d.setSiape(palavras[2]);
                        d.setEmail(palavras[3]);
                        d.setCentro(palavras[4]);
                        d.setAdm(false);

                        docenteFacade.save(d);
                    }
                    linha = lerArq.readLine();
                }
            }
        } catch (IOException e) {
            System.err.printf("Erro na abertura do arquivo: %s.\n", e.getMessage());
        }
        docenteLazyModel = null;
        JsfUtil.addSuccessMessage("Cadastro de docentes realizado com sucesso", "");
    }

    //Cadastra docentes do CMCC
    public void cadastrarDocentesCMCC() {

        String[] palavras;

        try {
            try (BufferedReader lerArq = new BufferedReader(new InputStreamReader(new FileInputStream("C:\\Users\\Juliana\\Documents\\NetBeansProjects\\alocacao\\Arquivos Alocação\\Arquivos CSV\\Docentes CMCC.csv"), "UTF-8"))) {

                String linha = lerArq.readLine(); //cabeçalho

                linha = lerArq.readLine();

                while (linha != null) {

                    linha = linha.replaceAll("\"", "");

                    palavras = linha.split("_");

                    List<Docente> docentes = docenteFacade.findByName(palavras[0]);

                    if (docentes.isEmpty()) {

                        Docente d = new Docente();

                        d.setNome(palavras[0]);
                        d.setSiape(palavras[1]);
                        d.setEmail(palavras[4]);
                        d.setCentro(palavras[2]);
                        d.setAreaAtuacao(palavras[3]);
                        d.setAdm(false);

                        docenteFacade.save(d);
                    }
                    linha = lerArq.readLine();
                }
            }
        } catch (IOException e) {
            System.err.printf("Erro na abertura do arquivo: %s.\n", e.getMessage());
        }
        docenteLazyModel = null;
        JsfUtil.addSuccessMessage("Cadastro de docentes realizado com sucesso", "");
    }

    //Faz o tratamento dos nomes
    private String trataNome(String nome) {

        String retorno = "";
        String[] palavras = nome.split(" ");

        for (String p : palavras) {

            if (p.equals("DAS") || p.equals("DOS") || p.length() <= 2) {
                p = p.toLowerCase();
                retorno += p + " ";
            } else {
                p = p.charAt(0) + p.substring(1, p.length()).toLowerCase();
                retorno += p + " ";
            }
        }
        return retorno;
    }
    
//-------------------------------------------Resumo Afinidades-------------------------------------------------------------------------------------------
    
    //Afinidades de acordo com o docente
    private AfinidadeDataModel afinidadesDoDocente;
  
    private boolean mostrarAdicionadas;
  
    /**
     * Método para a exibição da quantidade de creditos de cada docente na tabela
     * de resumos de afinidades
     * @param d
     * @return 
     */
    public int qtdAfinidades(Docente d){
        
        Set<Afinidade> afinidades = d.getAfinidades();
        int qtd = 0;
        
        for(Afinidade a: afinidades){
            if(a.getEstado().equals("Adicionada")){
                qtd++;
            }
        }
        return qtd;
    }
    
    /**
     * Preenche as afinidades do docente selecionado 
     * para a visualização dos detalhes no Resumo das Afinidades
     */
    public void preencherAfinidadesDoDocente(){

        mostrarAdicionadas = false;
        
        List<Afinidade> afinidades;
        if (docente != null) {
            //afinidades = new ArrayList<>(docente.getAfinidades());
            List<Afinidade> previa = new ArrayList<>(docente.getAfinidades());
            afinidades = ordenar(previa);
        } else {
            afinidades = new ArrayList<>();
        }
        afinidadesDoDocente = new AfinidadeDataModel(afinidades);
    }
    
    /**
     * Filtro caso o usuario administrador não queira ver as afinidades que foram removidas
     */
    public void verSoAdicionadas() {

        List<Afinidade> afinidades = new ArrayList<>(docente.getAfinidades());
        List<Afinidade> adicionadas = new ArrayList<>();

        if (mostrarAdicionadas) {
            for (Afinidade a : afinidades) {
                if (a.getEstado().equals("Adicionada")) {
                    adicionadas.add(a);
                }
            }
            afinidadesDoDocente = new AfinidadeDataModel(ordenar(adicionadas));
            //afinidadesDoDocente = new AfinidadeDataModel(adicionadas);
        } else {
            afinidadesDoDocente = new AfinidadeDataModel(ordenar(afinidades));
            //afinidadesDoDocente = new AfinidadeDataModel(afinidades);
        }
    }
    
    //Método para ordenar as afindades do docente
    public List<Afinidade> ordenar(List<Afinidade> docente){
        List<Afinidade> ordenada = new ArrayList<>();
        int i,j, tamanho = docente.size();
        Afinidade aux;
        Afinidade vetor[] = new Afinidade[tamanho];
        for(i=0;i<tamanho;i++){
            vetor[i] = docente.get(i);
        }
        for(i=0;i<docente.size();i++){
            for(j=i;j<tamanho;j++){
                if(vetor[i].getDisciplina().getNome().compareTo(vetor[j].getDisciplina().getNome()) > 0){
                    aux = vetor[i];
                    vetor[i] = vetor[j];
                    vetor[j] = aux;
                }
            }
        }
        for(i=0;i<tamanho;i++){
            ordenada.add(vetor[i]);
        }
        return ordenada;
    }
    
    public AfinidadeDataModel getAfinidadesDoDocente() {
        
        return afinidadesDoDocente;
    }

    public void setAfinidadesDoDocente(AfinidadeDataModel afinidadesDoDocente) {
        this.afinidadesDoDocente = afinidadesDoDocente;
    }
    
    public boolean isMostrarAdicionadas() {
        return mostrarAdicionadas;
    }

    public void setMostrarAdicionadas(boolean mostrarAdicionadas) {
        this.mostrarAdicionadas = mostrarAdicionadas;
    }
 
//------------------------------------Fase I da alocação didática-----------------------------------------
   
    //Créditos por quadrimestre para o planejamento anual
    private double creditos;
    
    public double getCreditos() {
        return creditos;
    }

    public void setCreditos(double creditos) {
        this.creditos = creditos;
    }

    /**
     * Associa a quantidade de créditos ao quadrimestre atual e ao docente que
     * está fazendo o planejamento
     * @param quad Long
     */
    public void salvarCreditos(Long quad) {

        docente = (Docente) LoginBean.getUsuario();
        Integer quadrimestre = (int) (long) quad;
        boolean salvar = true;

        //Verifica se já existe um planejamento de credito para aquele quadrimestre
        List<Credito> listCreditos = docente.getCreditos();
        if (listCreditos.size() > 0) {
            for (Credito c : listCreditos) {
                if (c.getQuadrimestre() == quadrimestre) { //substitui o planejamento anterior
                    c.setQuantidade(creditos);
                    try {
                        listCreditos.add(c);
                        docente.setCreditos(listCreditos);
                        creditoFacade.edit(c);
                        salvar = false;
                        JsfUtil.addSuccessMessage("Créditos editados com sucesso!");
                    } catch (Exception e) {
                        JsfUtil.addErrorMessage(e, "Ocorreu um erro de persistência, não foi possível editar os créditos " + e.getMessage());
                    }
                }
            }
        }

        if (salvar) {
            Credito credito = new Credito();
            credito.setQuadrimestre(quadrimestre);
            credito.setQuantidade(creditos);
            credito.setDocente(docente);
            listCreditos.add(credito);
            docente.setCreditos(listCreditos);

            try {
                creditoFacade.save(credito);

                JsfUtil.addSuccessMessage("Créditos salvos com sucesso!");
            } catch (Exception e) {
                JsfUtil.addErrorMessage(e, "Ocorreu um erro de persistência, não foi possível salvar os créditos " + e.getMessage());
            }
        }
        creditos = 0.0;
        LoginBean.setUsuario(docente);
        //salvar = true;
    }
    
//    public double creditosQuad(Long quad){
//        
//        docente = (Docente) LoginBean.getUsuario();
//        Integer quadrimestre = (int) (long) quad;
//        double credito = 0;
//        List<Credito> all = docente.getCreditos();
//        for(Credito c : all){
//            if(c.getQuadrimestre() == quadrimestre){
//                credito = c.getQuantidade();
//            }
//        }
//        
//        return credito;
//        
//    }
    
//-----------------------------------------Resumo Fase I-------------------------------------------------------------------------------------------
    
    //private DisponibilidadeDataModel disponibilidadesDocente;
    private DispDataModel disponibilidadesDocente;
    
    //Quadrimestre para visualização dos docentes no resumo
    private int quad;
   
    //Quantidade de disponibilidades do docente
    public int qtdDisponibilidades(Docente d){
        
        //Set<Disponibilidade> all = d.getDisponibilidades();
        //List<Disponibilidade> byQuad = new ArrayList<>();
        Set<Disp> all = d.getDispo();
        List<Disp> byQuad = new ArrayList<>();
        
        /*for(Disponibilidade disp : all){
            if(disp.getOfertaDisciplina().getQuadrimestre() == quad){
                byQuad.add(disp);
            }
        }*/
        for(Disp disp : all){
            if(disp.getOfertaDisciplina().getQuadrimestre() == quad){
                byQuad.add(disp);
            }
        }
        return byQuad.size();
    }
    
    //Quantidade de créditos no quadrimestre
    public double creditosQuad(Docente d){
        double creditosQuad = 0.0;
        List<Credito> listCreditos = d.getCreditos();
        if(listCreditos.size() > 0){
            
            for(Credito c: listCreditos){
                if(c.getQuadrimestre() == quad){
                    creditosQuad = c.getQuantidade();
                }
            } 
        }     
        return creditosQuad;
    }
    
    //Retorna as disponibilidades do docente
    public void preencherDisponibilidadesDoDocente() {

        //List<Disponibilidade> all;
        List<Disp> all;

        /*if (docente != null) {
            all = new ArrayList<>(docente.getDisponibilidades());
            List<Disponibilidade> byQuad = new ArrayList<>();
            for (Disponibilidade d : all) {
                if (d.getOfertaDisciplina().getQuadrimestre() == quad) {
                    byQuad.add(d);
                }
            }
            disponibilidadesDocente = new DisponibilidadeDataModel(byQuad);
        } */
        if (docente != null) {
            all = new ArrayList<>(docente.getDispo());
            List<Disp> byQuad = new ArrayList<>();
            for (Disp d : all) {
                if (d.getOfertaDisciplina().getQuadrimestre() == quad) {
                    byQuad.add(d);
                }
            }
            //disponibilidadesDocente = new DispDataModel(byQuad);
            disponibilidadesDocente = new DispDataModel(ordenarDisp(byQuad));
        } else { //caso o usuario não tenha clicado em nada, para não dar nullpointer
            all = new ArrayList<>();
            //disponibilidadesDocente = new DisponibilidadeDataModel(all);
            //disponibilidadesDocente = new DispDataModel(all);
            disponibilidadesDocente = new DispDataModel(ordenarDisp(all));
        }
    }

    public List<Disp> ordenarDisp(List<Disp> docente){
        List<Disp> ordenada = new ArrayList<>();
        int i,j,primeira,segunda,tamanho = docente.size();
        Disp aux;
        Disp vetor[] = new Disp[tamanho];
        for(i=0;i<tamanho;i++){
            vetor[i] = docente.get(i);
        }
        for(i=0;i<tamanho;i++){
            for(j=i;j<tamanho;j++){
                primeira = Integer.parseInt(vetor[i].getOrdemPreferencia());
                segunda = Integer.parseInt(vetor[j].getOrdemPreferencia());
                if(primeira > segunda){
                    aux = vetor[i];
                    vetor[i] = vetor[j];
                    vetor[j] = aux;
                }
            }
        }
        for(i=0;i<tamanho;i++){
            ordenada.add(vetor[i]);
        }
        return ordenada;
    }
    
    /*public DisponibilidadeDataModel getDisponibilidadesDocente() {
        return disponibilidadesDocente;
    }

    public void setDisponibilidadesDocente(DisponibilidadeDataModel disponibilidadesDocente) {
        this.disponibilidadesDocente = disponibilidadesDocente;
    }*/
    
    public DispDataModel getDisponibilidadesDocente() {
        return disponibilidadesDocente;
    }

    public void setDisponibilidadesDocente(DispDataModel disponibilidadesDocente) {
        this.disponibilidadesDocente = disponibilidadesDocente;
    }
    
    public int getQuad() {
        return quad;
    }

    public void setQuad(int quad) {
        this.quad = quad;
    }
    
//-----------------------------------------Resumo Fase II---------------------------------------------
    private TurmaDataModel turmaModel;
    
    private DispDataModel dispModel;
    
    public TurmaDataModel getTurmaModel(){
        return turmaModel;
    }
    
    public void setTurmaModel(TurmaDataModel turmaModel){
        this.turmaModel = turmaModel;
    }
    
    public DispDataModel getDispModel(){
        return dispModel;
    }
    
    public void setDispModel(DispDataModel dispModel){
        this.dispModel = dispModel;
    }
    
    public int totalTurmas(Docente d){
        int total = 0;
        total = turmaFacade.escolhasDocente(d.getID());
        return total;
    }
    
    //Busca as turmas selecionadas
    public void buscarSelecionadas(){
        List<TurmaDocente> selecionadas = new ArrayList<TurmaDocente>();
        List<Turma> turmasDocente = new ArrayList<Turma>();
        selecionadas = turmaFacade.listTurmas(docente.getID());
        for(TurmaDocente selecionada: selecionadas){
            Turma t = tFacade.achaTurma(selecionada.getIdTurma());
            turmasDocente.add(t);
        }
        turmaModel = new TurmaDataModel(turmasDocente);
    }
    
    @EJB 
    private FaseFacade verificaFase;
    
    //Retorna a ordem de prioridade no planejamento do docente para a turma selecionada
    public int prioridadeTurma(Turma t){
        int prioridade = 0;
        Long idDisciplina = t.getDisciplina().getID();
        //List<OfertaDisciplina> ofertas = new ArrayList<OfertaDisciplina>();
        List<Disp> disponibilidades = new ArrayList<Disp>();
        
        Fase f = verificaFase.achaMax();
        int quad = 0;
        if(f.isFase2_quad1() == true){
            quad = 1;
        } else if(f.isFase2_quad2() == true){
            quad = 2;
        } else {
            quad = 3;
        }
        
        disponibilidades = dispFacade.findByDocenteQuad(docente, quad);//findByDocente(docente);
        for(Disp disponibilidade : disponibilidades){
            OfertaDisciplina oferta = ofertaFacade.ofertaPorId(disponibilidade.getOfertaDisciplina().getID());
            
            int idOferta = Integer.valueOf(oferta.getDisciplina().getID().toString());
            int id = Integer.valueOf(idDisciplina.toString());
            if(idOferta == id && t.getTurno().equals(disponibilidade.getOfertaDisciplina().getTurno())){
                prioridade = Integer.parseInt(disponibilidade.getOrdemPreferencia());
            }
            //ofertas.add(oferta);
        }
        /*for(OfertaDisciplina oferta : ofertas){
            int idOferta = Integer.valueOf(oferta.getDisciplina().getID().toString());
            int id = Integer.valueOf(idDisciplina.toString());
            if(idOferta == id){
                prioridade = 
            }
        }*/
        
        //ofertas = ofertaFacade.buscarDiscplina(idDisciplina);
        /*for(OfertaDisciplina oferta : ofertas){
            for(Disp disponibilidade : disponibilidades){
                int idOferta = Integer.valueOf(oferta.getDisciplina().getID().toString());
                int idDisp = Integer.valueOf(disponibilidade.getOfertaDisciplina().getDisciplina().getID().toString());
                if(idOferta == idDisp){
                    if(oferta.getTurno().equals(t.getTurno())){
                        prioridade = Integer.parseInt(disponibilidade.getOrdemPreferencia());
                    }
                }
            }
        }*/
        return prioridade;
    }
    
//-----------------------------------------DataModel--------------------------------------------------
//Utilizado para exibição nos Resumos de Afinidades e da Fase I da Alocação
    private DocenteDataModel docenteDataModel;

    public DocenteDataModel getDocenteDataModel() {
        
        if(docenteDataModel == null){
            docenteDataModel = new DocenteDataModel(this.listarTodas());
        }
        return docenteDataModel;
    }

    //------------------------------Filtros de Docente-------------------------------------------
    
    //Método para filtrar a busca
    public void filtrar() {
        
        if (!getFiltrosSelecAreaAtuacao().isEmpty()) {
            List<Docente> docentesFiltrados = docenteFacade.findByArea(getFiltrosSelecAreaAtuacao());

            setFiltrosSelecAreaAtuacao(null);
            docenteDataModel = new DocenteDataModel(docentesFiltrados); 
        }
        else{
            docenteDataModel = new DocenteDataModel(docenteFacade.findAll());
        }  
    }
    
    //Limpar filtro
    public void limparFiltro(){
     
        limparFiltroDocente();
        docenteDataModel = null;
        quad = 1;
    }

    //------------------------------------------CRUD---------------------------------------------------------------------------------------------
    
    //Buscar por id
    public Docente buscar(Long id) {
        return docenteFacade.find(id);
    }
    
    //Salvar docente
    public void salvar(){
        try {
            docenteFacade.save(docenteSalvar);
            JsfUtil.addSuccessMessage("Docente " + docenteSalvar.getNome() + " cadastrado com sucesso!");
            docenteSalvar = null;
            docenteLazyModel = null;
        } catch (Exception e) {
            JsfUtil.addErrorMessage("Não foi possível cadastrar o docente");
        }
    }

    //Editar docente
    public void editar() {
        try {
            docenteFacade.edit(docente);
            JsfUtil.addSuccessMessage("Docente editado com sucesso!");
            docente = null;
            docenteLazyModel = null;
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, "Ocorreu um erro de persistência, não foi possível editar o docente: " + e.getMessage());
        }
    }

    //Deletar docente
    public void delete() {
        docente = (Docente) docenteLazyModel.getRowData();
        try {
            docenteFacade.remove(docente);
            docente = null;
            JsfUtil.addSuccessMessage("Docente Deletado");
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, "Ocorreu um erro de persistência");
        }
        docenteLazyModel = null;
    }
    
    //Buscar todos os docentes
    public List<Docente> listarTodas(){
        return docenteFacade.findAll();
    }
    
    //----------------------------------------Páginas web------------------------------------------------------
    
    public String prepareEdit() {
        docente = (Docente) (Pessoa) docenteLazyModel.getRowData();
        return "/Cadastro/editDocente";
    }
}
