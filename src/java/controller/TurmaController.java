package controller;

import facade.DisciplinaFacade;
import facade.TurmaDocenteFacade;
import facade.TurmaFacade;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import model.Afinidade;
import model.Disciplina;
import model.Docente;
import model.Horario;
import model.Pessoa;
import model.Turma;
import model.TurmaDocente;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleModel;
import util.TurmaDataModel;
import util.TurmaLazyModel;

@Named(value = "turmaController")
@SessionScoped
public class TurmaController implements Serializable {

    public TurmaController() {
        docente = (Pessoa) LoginBean.getUsuario(); //Pega o usuário logado
    }

    @EJB
    private DisciplinaFacade disciplinaFacade;

    @EJB
    private TurmaFacade turmaFacade;

    //Cadastro-------------------------------------------------------------------------------------------
    public void cadastrarTurmas() {

        String[] palavras;

        try {

            try (BufferedReader lerArq = new BufferedReader(new InputStreamReader(new FileInputStream("/home/charles/alocacao/Arquivos Alocação/Arquivos CSV/turmas.csv"), "UTF-8"))) {
                String linha = lerArq.readLine();

                while (linha != null) {

                    linha = linha.replaceAll("\"", "");

                    palavras = linha.split(";", -1);

                    Turma t = new Turma();

                    String codigo = palavras[2];
                    String nome = palavras[4];

                    Disciplina d = disciplinaFacade.findByCodOrName(codigo, nome);

                    t.setDisciplina(d);

                    palavras[18] = palavras[18].replaceAll(" ,", ",");
                    palavras[18] = palavras[18].replaceAll("\"", "");
                    String[] horariosCompletos = palavras[18]
                            .trim().split("(?<=semanal,)|(?<=quinzenal I)|(?<=quinzenal II)");

                    List<Horario> horarios = new ArrayList<Horario>();

                    for (String horarioCompleto : horariosCompletos) {

                        horarioCompleto = horarioCompleto.trim();
                        String[] partes = horarioCompleto.split("das|,");
                        Horario h = new Horario();
                        h.setDia(partes[0]);
                        h.setHora(partes[1]);
                        h.setSala(partes[2]);
                        h.setPeriodicidade(partes[3]);

                        horarios.add(h);

                    }

                    t.setHorarios(horarios);
                    turmaFacade.save(t);

                    linha = lerArq.readLine();

//                linha = linha.replaceAll("\"", "");
                }
            } //cabeçalho

        } catch (IOException e) {
            System.err.printf("Erro na abertura do arquivo: %s.\n", e.getMessage());
        }

    }

    //Método de cadastro com novo arquivo (A ser difinido como olficial)
    //obs: ao apagar as turmas acontecem problemas para carregar a fase 2 devido à consulta por idTurma
    public void cadastrarTurmas2() {
        String[] palavras;
        try {
            try (BufferedReader lerArq = new BufferedReader(new InputStreamReader(new FileInputStream("/home/charles/Documentos/Erick/Teste.csv"), "UTF-8"))) {
                String linha = lerArq.readLine();
                while (linha != null) {
                    linha = linha.replaceAll("\"", "");
                    palavras = linha.split(";", -1);

                    Turma t = new Turma();

                    String nome = palavras[0];
                    String codigo = palavras[1];

                    Disciplina d = disciplinaFacade.findByCodOrName(codigo, nome);

                    t.setDisciplina(d);

                    palavras[3] = palavras[3].replaceAll(" ,", ",");
                    palavras[3] = palavras[3].replaceAll("\"", "");
                    palavras[4] = palavras[4].replaceAll(" ,", ",");
                    palavras[4] = palavras[4].replaceAll("\"", "");

                    String teoria = palavras[3];
                    String pratica = palavras[4];

                    if (teoria.contains("quinzenal II")) {
                        teoria = teoria.replace("quinzenal II", "quinzenal 2");
                    }
                    if (teoria.contains("quinzenal I")) {
                        teoria = teoria.replace("quinzenal I", "quinzenal 1");
                    }
                    if (pratica.contains("quinzenal II")) {
                        pratica = pratica.replace("quinzenal II", "quinzenal 2");
                    }
                    if (pratica.contains("quinzenal I")) {
                        pratica = pratica.replace("quinzenal I", "quinzenal 1");
                    }
                    if (teoria.contains("quinzenal 2,")) {
                        teoria = teoria.replace("quinzenal 2,", "quinzenal 2 ");
                    }
                    if (teoria.contains("quinzenal 1,")) {
                        teoria = teoria.replace("quinzenal 1,", "quinzenal 1 ");
                    }
                    if (teoria.contains("semanal,")) {
                        teoria = teoria.replace("semanal,", "semanal");
                    }
                    if (pratica.contains("quinzenal 2,")) {
                        pratica = pratica.replace("quinzenal 2,", "quinzenal 2 ");
                    }
                    if (pratica.contains("quinzenal 1,")) {
                        pratica = pratica.replace("quinzenal 1,", "quinzenal 1 ");
                    }
                    if (pratica.contains("semanal,")) {
                        pratica = pratica.replace("semanal,", "semanal");
                    }
                    String tp;
                    if (teoria.isEmpty() || pratica.isEmpty()) {
                        tp = teoria + pratica;
                    } else {
                        tp = teoria + " " + pratica;
                    }

                    String[] horariosCompletos = tp.trim().split("(?<=semanal)|(?<=quinzenal 1)|(?<=quinzenal 2)");

                    List<Horario> horarios = new ArrayList<Horario>();

                    for (String horarioCompleto : horariosCompletos) {

                        horarioCompleto = horarioCompleto.trim();
                        String[] partes = horarioCompleto.split("das|,");
                        Horario h = new Horario();
                        //String dia = partes[0];
                        //String hora = partes[1];
                        //String sala = partes[2];
                        //String periodo = partes[3];
                        h.setDia(partes[0]);
                        h.setHora(partes[1]);
                        h.setSala(partes[2]);
                        h.setPeriodicidade(partes[3]);

                        horarios.add(h);
                    }

                    t.setHorarios(horarios);

                    String campus = palavras[5];
                    String codturma = palavras[2];
                    String turno = palavras[6];
                    String curso = palavras[12];

                    if (campus.equals("Santo André")) {
                        campus = "SA";
                    }
                    if (campus.equals("São Bernardo do Campo")) {
                        campus = "SBC";
                    }
                    if (turno.equals("diurno")) {
                        turno = "D";
                    }
                    if (turno.equals("noturno")) {
                        turno = "N";
                    }

                    t.setCampus(campus);
                    t.setCodturma(palavras[2]);
                    t.setTurno(turno);
                    t.setCurso(palavras[12]);
                    turmaFacade.save(t);

                    linha = lerArq.readLine();
                }
            }
        } catch (IOException e) {
            System.err.printf("Erro na abertura do arquivo: %s.\n", e.getMessage());
        }

    }

    //Apaga todas as turmas cadastradas
    public void deleteTurmas() {
        List<Turma> turmasOfertadas = turmaFacade.findAll();
        //For para apagar cada turma do banco.
        for (Turma t : turmasOfertadas) {
            //t = turmaFacade.find(t.getID());
            turmaFacade.remove(t);
        }
        turmalazymodel = null;
    }

    //DataModel e LazyModel ---------------------------------------------------------------------------------
    private TurmaDataModel turmaDataModel; // Não está mais sendo usado para a lista de turmas na Fase 2

    public TurmaDataModel getTurmaDataModel() {

        if (turmaDataModel == null) {

            List<Turma> turmas = turmaFacade.findAll();
            turmaDataModel = new TurmaDataModel(turmas);

        }

        return turmaDataModel;
    }

    public void setTurmaDataModel(TurmaDataModel turmaDataModel) {
        this.turmaDataModel = turmaDataModel;
    }

    private TurmaLazyModel turmalazymodel; //LazyModel para página de Cadastro e Turmas da Fase 2

    public TurmaLazyModel getTurmalazymodel() {
        if (turmalazymodel == null) {

            List<Turma> turmas = turmaFacade.findAll();
            turmalazymodel = new TurmaLazyModel(turmas);
        }
        return turmalazymodel;
    }

    private TurmaLazyModel listaRequisicoes; //DataModel para o docente administrar suas turmas na Fase 2

    public TurmaLazyModel getListaRequisicoes() {
        List<Turma> requisicoes = new ArrayList<>();
        Long id = docente.getID();
        List<TurmaDocente> salvas = new ArrayList<>();
        //Método para buscar turmas do docente em TurmaDocenteFacade
        if (listaRequisicoes == null) {
            salvas = turmasEscolhidasFacade.listTurmas(id);
            for (TurmaDocente d : salvas) {
                Turma t = turmaFacade.listByID(d.getIdTurma());
                String turma = t.getCodturma();
                String turno = t.getTurno();
                String campus = t.getCampus();
                requisicoes.add(t);
            }
            listaRequisicoes = new TurmaLazyModel(requisicoes);
        }
        return listaRequisicoes;
    }

    private int total;

    //Método para buscar o número de solicitações por turma
    public int solicitacoes(Long id) {
        total = turmasEscolhidasFacade.totalSolicitacoes(id);

        return total;
    }

    //Método para visualizar os horários das turmas
    public String horariosTurma(Long id) {
        String horario = "";
        Turma t = turmaFacade.find(id);
        List<Horario> h = t.getHorarios();
        for (int i = 0; i < h.size(); i++) {
            if (i < h.size() - 1) {
                horario = horario + h.get(i).getDia() + " " + h.get(i).getHora() + " " + h.get(i).getPeriodicidade() + ", ";
            } else {
                horario = horario + h.get(i).getDia() + " " + h.get(i).getHora() + " " + h.get(i).getPeriodicidade();
            }
        }
        return horario;
    }

    //Filtros para a Fase II ---------------------------------------------------------------------------------
    private String campus;

    private String turno;

    private boolean filtrarAfinidades;

    private Set<Afinidade> afinidades;

    private List<Disciplina> AfinidadesDocente;

    public String getCampus() {
        return campus;
    }

    public void setCampus(String campus) {
        this.campus = campus;
    }

    public String getTurno() {
        return turno;
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }

    public boolean isFiltrarAfinidades() {
        return filtrarAfinidades;
    }

    public void setFiltrarAfinidades(boolean filtrarAfinidades) {
        this.filtrarAfinidades = filtrarAfinidades;
    }

    //Método para filtrar as turmas da Fase 2
    public void filtrar() {
        turmalazymodel = null;
        AfinidadesDocente = new ArrayList<>();

        if (filtrarAfinidades) {
            afinidades = docente.getAfinidades();
            for (Afinidade a : afinidades) {
                if (a.getEstado().equals("Adicionada")) {
                    AfinidadesDocente.add(a.getDisciplina());
                }
            }
        }

        turmalazymodel = new TurmaLazyModel(turmaFacade.filtrarTurmas(AfinidadesDocente, campus, turno));

        filtrarAfinidades = false;
        campus = "";
        turno = "";
    }

    public void limparFiltros() {
        turmalazymodel = null;
        List<Turma> turmas = turmaFacade.findAll();
        turmalazymodel = new TurmaLazyModel(turmas);
    }

    //Fase II Disponibilidade -------------------------------------------------------------------------------
    //Turma que sera selecionada para visualizacao
    private Turma selectedTurma;

    public Turma getSelectedTurma() {
        return selectedTurma;
    }

    public void setSelectedTurma(Turma selectedTurma) {
        this.selectedTurma = selectedTurma;
    }

    public void onRowSelect(SelectEvent event) {
        aux = event; //Atribui a aux a turma selecionada para ser usada por outros metodos
        turmasSchedule.clear();
        //docenteSchedule.clear();
        Turma t = (Turma) event.getObject();
        preencherTurma(t);
        //preecherSchedule();
    }

    public void onRowUnselect(UnselectEvent event) {

    }

    //Mostrara as turmas da disciplina selecionada
    private ScheduleModel turmasSchedule;

    //Mostrara as turmas já selecionadas pelo docente
    private ScheduleModel docenteSchedule;

    public ScheduleModel getTurmasSchedule() {
        return turmasSchedule;
    }

    public void setTurmasSchedule(ScheduleModel turmasSchedule) {
        this.turmasSchedule = turmasSchedule;
    }

    public ScheduleModel getDocenteSchedule() {
        return docenteSchedule;
    }

    public void setDocenteSchedule(ScheduleModel docenteSchedule) {
        this.docenteSchedule = docenteSchedule;
    }

    //Método para salvar turmas selecionadas
    public void incluirTurma() {
        try {
            listaRequisicoes = null;
            FacesContext context = FacesContext.getCurrentInstance();
            Turma t = (Turma) aux.getObject();

            int testeaaa = 0;
            Long id = t.getID();
            Long docenteid = docente.getID();
            boolean conflito = verificar(t);

            if (conflito) {
                context.addMessage(null, new FacesMessage("Error", "Turma Requisitada não pode ser Salva devido à conflitos de horário!"));
            } else {
                TurmaDocente turmasEscolhidas = new TurmaDocente();
                turmasEscolhidas.setIdTurma(id);
                turmasEscolhidas.setIdDocente(docenteid);
                turmasEscolhidasFacade.save(turmasEscolhidas);
                preecherSchedule();
                context.addMessage(null, new FacesMessage("Successful", "Turma Requisitada Salva!"));
            }
        } catch (Exception e) {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage("Error", "Turma Requisitada não pode ser Salva!"));
        }
    }

    public boolean verificar(Turma escolhida) {
        List<Horario> selecionado = escolhida.getHorarios();
        boolean conflito = false;

        List<TurmaDocente> salvas = new ArrayList<>();
        salvas = turmasEscolhidasFacade.listTurmas(docente.getID());
        for (TurmaDocente td : salvas) {
            Turma t = turmaFacade.find(td.getIdTurma());
            String nome = t.getDisciplina().getNome();
            List<Horario> horaAula = t.getHorarios();
            for (Horario h : horaAula) {
                String hora1 = h.getDia() + h.getHora();
                for (Horario h2 : selecionado) {
                    String hora2 = h2.getDia() + h2.getHora();
                    if (h2.getDia().equals(h.getDia()) && h2.getHora().equals(h.getHora())) {
                        conflito = true;
                    }
                }
            }
        }
        return conflito;
    }

    //Turma que sera selecionada para exclusao
    private Turma selectedTurma2;

    public Turma getSelectedTurma2() {
        return selectedTurma;
    }

    public void setSelectedTurma2(Turma selectedTurma) {
        this.selectedTurma = selectedTurma;
    }

    public void onRowSelect2(SelectEvent event) {
        aux2 = event; //Atribui a aux a turma selecionada para ser usada por outros metodos
        turmasSchedule.clear();
        Turma t = (Turma) event.getObject();
        preencherTurma(t);
    }

    public void onRowUnselect2(UnselectEvent event) {

    }

    //Método para deletar turma selecionada
    public void excluirTurma() {
        FacesContext context = FacesContext.getCurrentInstance();
        Turma t = (Turma) aux2.getObject();
        Long idTurma = t.getID();
        Long idDocente = docente.getID();
        TurmaDocente td = turmasEscolhidasFacade.TurmaSelecionada(idTurma, idDocente);
        turmasEscolhidasFacade.remove(td);
        listaRequisicoes = null;
        preecherSchedule();
        context.addMessage(null, new FacesMessage("Successful", "Turma Deletada"));
    }

    //Método para listar e preencher as turmas do Docente no Schedule
    public void preecherSchedule() {
        docenteSchedule.clear();
        List<TurmaDocente> requisicoes = new ArrayList<TurmaDocente>();
        List<Turma> escolhidas = new ArrayList<Turma>();
        Turma t;
        Long id = docente.getID();
        //Cria a lista de todas as requisições de turma
        //if (requisicoes == null) {
        requisicoes = turmasEscolhidasFacade.listTurmas(id);
        //}
        //Cria a lista das solicitações do docente atual
        for (TurmaDocente atual : requisicoes) {
            //if (atual.getIdDocente() == docente.getID()) {
            t = turmaFacade.find(atual.getIdTurma());
            escolhidas.add(t);
            //}
        }
        requisicoes = null;
        //Método para preencher o Schedule do Docente
        preencherRequisicoes(escolhidas);
    }

    private static SelectEvent aux;

    private static SelectEvent aux2;

    private Pessoa docente;

    //private TurmaDocente turmasEscolhidas;
    @EJB
    private TurmaDocenteFacade turmasEscolhidasFacade;

    public Pessoa getDocente() {
        return docente;
    }

    public void setDocente(Pessoa docente) {
        this.docente = docente;
    }

    /*public TurmaDocente getTurmasEscolhidas(){
     return turmasEscolhidas;
     }
    
     public void setTurmasEscolhidas(TurmaDocente turmasEscolhidas){
     this.turmasEscolhidas = turmasEscolhidas;
     }*/
    @PostConstruct
    public void init() {
        turmasSchedule = new DefaultScheduleModel();
        docenteSchedule = new DefaultScheduleModel();
        //turmalazymodel = new TurmaLazyModel(TurmaFacade.listTurma());

//        Calendar i = Calendar.getInstance();
//        i.set(Calendar.AM_PM, Calendar.PM);
//        i.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
//        i.set(Calendar.HOUR, 5);
//        i.set(Calendar.MINUTE, 0);
//        
//        Calendar e = Calendar.getInstance();
//        i.set(Calendar.AM_PM, Calendar.PM);
//        i.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
//        i.set(Calendar.HOUR, 7);
//        i.set(Calendar.MINUTE, 0);
//        turmasSchedule.addEvent(new DefaultScheduleEvent("Turma A", i.getTime(), e.getTime()));
    }

    public void preencherTurma(Turma turma) {

        List<Horario> horarios = turma.getHorarios();

        for (Horario h : horarios) {

            int dia = conversorDia(h.getDia());

            String hora = h.getHora().trim();
            int horaInicio = Integer.parseInt(hora.substring(0, 2));
            int minutoInicio = Integer.parseInt(hora.substring(3, 5));

            int horaFim = Integer.parseInt(hora.substring(9, 11));
            int minutoFim = Integer.parseInt(hora.substring(12, 14));

            //Inicio do horario
            Calendar inicio = Calendar.getInstance();
            inicio.set(Calendar.DAY_OF_WEEK, dia);
            if (horaInicio > 12) {
                inicio.set(Calendar.AM_PM, Calendar.PM);
                inicio.set(Calendar.HOUR, horaInicio - 12);

            } else {
                inicio.set(Calendar.AM_PM, Calendar.AM);
                inicio.set(Calendar.HOUR, horaInicio);
            }
            inicio.set(Calendar.MINUTE, minutoInicio);

            //Fim do horario
            Calendar fim = Calendar.getInstance();
            fim.set(Calendar.DAY_OF_WEEK, dia);
            if (horaFim > 12) {
                fim.set(Calendar.AM_PM, Calendar.PM);
                fim.set(Calendar.HOUR, horaFim - 12);

            } else {
                fim.set(Calendar.AM_PM, Calendar.AM);
                fim.set(Calendar.HOUR, horaFim);
            }
            fim.set(Calendar.MINUTE, minutoFim);

            String sigla = converterSigla(turma);

            //turmasSchedule.addEvent(new DefaultScheduleEvent(sigla, inicio.getTime(), fim.getTime()));
            DefaultScheduleEvent t = new DefaultScheduleEvent(sigla, inicio.getTime(), fim.getTime(), "blue-event");
            turmasSchedule.addEvent(t);
            //docenteSchedule.addEvent(t);

        }

    }

    //Salva solicitacoes no banco
    public void preencherRequisicoes(List<Turma> turma) {
        //List<Horario> horarios = new ArrayList<Horario>();
        for (Turma t : turma) {
            List<Horario> horarios = t.getHorarios();
            for (Horario h : horarios) {
                int dia = conversorDia(h.getDia());

                String hora = h.getHora().trim();
                int horaInicio = Integer.parseInt(hora.substring(0, 2));
                int minutoInicio = Integer.parseInt(hora.substring(3, 5));

                int horaFim = Integer.parseInt(hora.substring(9, 11));
                int minutoFim = Integer.parseInt(hora.substring(12, 14));

                //Inicio do horario
                Calendar inicio = Calendar.getInstance();
                inicio.set(Calendar.DAY_OF_WEEK, dia);
                if (horaInicio > 12) {
                    inicio.set(Calendar.AM_PM, Calendar.PM);
                    inicio.set(Calendar.HOUR, horaInicio - 12);

                } else {
                    inicio.set(Calendar.AM_PM, Calendar.AM);
                    inicio.set(Calendar.HOUR, horaInicio);
                }
                inicio.set(Calendar.MINUTE, minutoInicio);

                //Fim do horario
                Calendar fim = Calendar.getInstance();
                fim.set(Calendar.DAY_OF_WEEK, dia);
                if (horaFim > 12) {
                    fim.set(Calendar.AM_PM, Calendar.PM);
                    fim.set(Calendar.HOUR, horaFim - 12);

                } else {
                    fim.set(Calendar.AM_PM, Calendar.AM);
                    fim.set(Calendar.HOUR, horaFim);
                }
                fim.set(Calendar.MINUTE, minutoFim);

                String sigla = converterSigla(t);
                //Preenche o Schdule do Docente
                //docenteSchedule.addEvent(new DefaultScheduleEvent(sigla, inicio.getTime(), fim.getTime()));
                DefaultScheduleEvent schedule = new DefaultScheduleEvent(sigla, inicio.getTime(), fim.getTime(), "darkgreen-event");
                docenteSchedule.addEvent(schedule);
            }
        }
        //eventoTurma();
        //docenteSchedule.addEvent(novo);
    }
    
    /*public void eventoTurma(){
        Turma turma = (Turma) aux.getObject();
        List<Horario> horarios = turma.getHorarios();
        DefaultScheduleEvent schedule = null;
        for (Horario h : horarios) {

            int dia = conversorDia(h.getDia());

            String hora = h.getHora().trim();
            int horaInicio = Integer.parseInt(hora.substring(0, 2));
            int minutoInicio = Integer.parseInt(hora.substring(3, 5));

            int horaFim = Integer.parseInt(hora.substring(9, 11));
            int minutoFim = Integer.parseInt(hora.substring(12, 14));

            //Inicio do horario
            Calendar inicio = Calendar.getInstance();
            inicio.set(Calendar.DAY_OF_WEEK, dia);
            if (horaInicio > 12) {
                inicio.set(Calendar.AM_PM, Calendar.PM);
                inicio.set(Calendar.HOUR, horaInicio - 12);

            } else {
                inicio.set(Calendar.AM_PM, Calendar.AM);
                inicio.set(Calendar.HOUR, horaInicio);
            }
            inicio.set(Calendar.MINUTE, minutoInicio);

            //Fim do horario
            Calendar fim = Calendar.getInstance();
            fim.set(Calendar.DAY_OF_WEEK, dia);
            if (horaFim > 12) {
                fim.set(Calendar.AM_PM, Calendar.PM);
                fim.set(Calendar.HOUR, horaFim - 12);

            } else {
                fim.set(Calendar.AM_PM, Calendar.AM);
                fim.set(Calendar.HOUR, horaFim);
            }
            fim.set(Calendar.MINUTE, minutoFim);

            String sigla = converterSigla(turma);
            schedule = new DefaultScheduleEvent(sigla, inicio.getTime(), fim.getTime(), "blue-event");
            docenteSchedule.addEvent(schedule);
        }
    }*/

    //Método para converter o dia para Calendar
    private int conversorDia(String dia) {

        dia = dia.trim();

        switch (dia) {
            case "segunda":
                return Calendar.MONDAY;
            case "terca":
                return Calendar.TUESDAY;
            case "quarta":
                return Calendar.WEDNESDAY;
            case "quinta":
                return Calendar.THURSDAY;
            case "sexta":
                return Calendar.FRIDAY;
            case "sabado":
                return Calendar.SATURDAY;
        }

        return 0;
    }

    //Método para transformar o nome da disciplina em sigla
    private String converterSigla(Turma t) {
        String nome = t.getDisciplina().getNome();
        String cod = t.getCodturma();
        String sigla = "";
        int n = nome.length();
        for (int i = 0; i < n; i++) {
            if (nome.charAt(i) >= 'A' && nome.charAt(i) <= 'Z') {
                sigla = sigla + nome.charAt(i);
            }
        }
        sigla = sigla + "-" + cod;
        return sigla;
    }

}
