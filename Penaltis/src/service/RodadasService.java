package service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;
import jogo.DisputaPenaltisUI;
import jogo.ResultadoPenaltis;
import jogo.TelaClassificacaoUI;

public class RodadasService {

    private static final String STATUS_PENDENTE = "PENDENTE";
    private static final String STATUS_FINALIZADO = "FINALIZADO";
    private static final int PONTOS_VITORIA = 3;
    private static final int PONTOS_EMPATE = 1;

    private final Random random;
    private final ArquivoJson arquivoRodadas;
    private final DadosService dadosService;
    private final DisputaPenaltisUI disputaPenaltisUI;

    public RodadasService() {
        this.random = new Random();
        this.arquivoRodadas = new ArquivoJson("src/service/rodadas.json");
        this.dadosService = new DadosService();
        this.disputaPenaltisUI = new DisputaPenaltisUI();
    }

    public void fazerRodada(Dados dados) {
        System.out.println("Realizando a rodada");
        fazerRodada(dados, null);
    }

    public void fazerRodada(Dados dados, String meuTime) {
        if (dados == null || dados.getTemporadas() == null || dados.getTemporadas().isEmpty()) {
            throw new IllegalArgumentException("Não há temporadas carregadas.");
        }

        Temporada temporadaAtiva = buscarTemporadaAtiva(dados.getTemporadas());
        if (temporadaAtiva == null) {
            throw new IllegalStateException("Nenhuma temporada ativa foi encontrada.");
        }

        RodadasDados rodadasDados = lerRodadas();
        TemporadaRodadas temporadaRodadas = obterOuCriarTemporadaRodadas(rodadasDados, temporadaAtiva);
        gerarRodadasSeNecessario(temporadaAtiva, temporadaRodadas);

        if (temporadaEncerrada(temporadaRodadas)) {
            abrirNovaTemporada(dados, temporadaAtiva);
            salvarRodadas(rodadasDados);
            dadosService.salvarDados(dados);
            return;
        }

        int proximaRodada = temporadaAtiva.getRodada() + 1;
        List<PartidaRodada> partidasDaRodada = temporadaRodadas.getPartidas()
                .stream()
                .filter(p -> p.getRodada() == proximaRodada)
                .collect(Collectors.toList());

        if (partidasDaRodada.isEmpty()) {
            if (temporadaEncerrada(temporadaRodadas)) {
                abrirNovaTemporada(dados, temporadaAtiva);
                dadosService.salvarDados(dados);
            }
            salvarRodadas(rodadasDados);
            return;
        }

        Map<Integer, Time> timesPorId = indexarTimesPorId(temporadaAtiva);

        for (PartidaRodada partida : partidasDaRodada) {
            if (STATUS_FINALIZADO.equals(partida.getStatus())) {
                continue;
            }

            Time timeA = timesPorId.get(partida.getTimeAId());
            Time timeB = timesPorId.get(partida.getTimeBId());
            if (timeA == null || timeB == null) {
                throw new IllegalStateException("Time não encontrado para a partida gerada.");
            }

            if (envolveMeuTime(timeA, timeB, meuTime)) {
                jogarPartidaMeuTime(timeA, timeB, partida);
                continue;
            }

            simularConfronto(timeA, timeB, partida);
        }

        temporadaAtiva.setRodada(proximaRodada);
        if (temporadaEncerrada(temporadaRodadas)) {
            abrirNovaTemporada(dados, temporadaAtiva);
        }

        salvarRodadas(rodadasDados);
        dadosService.salvarDados(dados);
    }

    private Temporada buscarTemporadaAtiva(List<Temporada> temporadas) {
        for (Temporada temporada : temporadas) {
            if (temporada.isAtiva()) {
                return temporada;
            }
        }
        return null;
    }

    private RodadasDados lerRodadas() {
        try {
            RodadasDados dados = arquivoRodadas.ler(RodadasDados.class);
            return dados == null ? new RodadasDados() : dados;
        } catch (IOException e) {
            throw new RuntimeException("Erro ao ler rodadas.json", e);
        }
    }

    private void salvarRodadas(RodadasDados rodadasDados) {
//        new Classificacao01().setVisible(true);
        try {
            arquivoRodadas.salvar(rodadasDados);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar rodadas.json", e);
        }
    }

    private TemporadaRodadas obterOuCriarTemporadaRodadas(RodadasDados rodadasDados, Temporada temporada) {
        for (TemporadaRodadas tr : rodadasDados.getTemporadas()) {
            if (Objects.equals(tr.getTemporadaId(), temporada.getId())) {
                return tr;
            }
        }
        TemporadaRodadas nova = new TemporadaRodadas();
        nova.setTemporadaId(temporada.getId());
        nova.setAno(temporada.getAno());
        rodadasDados.getTemporadas().add(nova);
        return nova;
    }

    private void gerarRodadasSeNecessario(Temporada temporada, TemporadaRodadas temporadaRodadas) {
        if (!temporadaRodadas.getPartidas().isEmpty()) {
            return;
        }

        if (temporada.getGrupos() == null || temporada.getGrupos().isEmpty()) {
            throw new IllegalStateException("A temporada ativa não possui grupos.");
        }

        for (Grupo grupo : temporada.getGrupos()) {
            List<Time> times = grupo.getTimes();
            if (times == null || times.size() < 2 || times.size() % 2 != 0) {
                throw new IllegalStateException("Cada grupo deve ter quantidade par de times.");
            }

            List<Time> rotacao = new ArrayList<>(times);
            int totalRodadas = rotacao.size() - 1;
            for (int rodada = 1; rodada <= totalRodadas; rodada++) {
                int metade = rotacao.size() / 2;
                for (int i = 0; i < metade; i++) {
                    Time timeA = rotacao.get(i);
                    Time timeB = rotacao.get(rotacao.size() - 1 - i);

                    String confronto = chaveConfronto(timeA.getId(), timeB.getId());
                    if (temporadaRodadas.getConfrontosGerados().contains(confronto)) {
                        continue;
                    }

                    PartidaRodada partida = new PartidaRodada();
                    partida.setTemporadaId(temporada.getId());
                    partida.setGrupoId(grupo.getId());
                    partida.setGrupoNome(grupo.getNome());
                    partida.setRodada(rodada);
                    partida.setTimeAId(timeA.getId());
                    partida.setTimeANome(timeA.getNome());
                    partida.setTimeBId(timeB.getId());
                    partida.setTimeBNome(timeB.getNome());
                    partida.setStatus(STATUS_PENDENTE);

                    temporadaRodadas.getPartidas().add(partida);
                    temporadaRodadas.getConfrontosGerados().add(confronto);
                }

                rotacionarTimes(rotacao);
            }
        }
    }

    private void rotacionarTimes(List<Time> times) {
        Time ultimo = times.remove(times.size() - 1);
        times.add(1, ultimo);
    }

    private String chaveConfronto(int timeAId, int timeBId) {
        int menor = Math.min(timeAId, timeBId);
        int maior = Math.max(timeAId, timeBId);
        return menor + "-" + maior;
    }

    private Map<Integer, Time> indexarTimesPorId(Temporada temporada) {
        Map<Integer, Time> timesPorId = new HashMap<>();
        for (Grupo grupo : temporada.getGrupos()) {
            for (Time time : grupo.getTimes()) {
                timesPorId.put(time.getId(), time);
            }
        }
        return timesPorId;
    }

    private boolean envolveMeuTime(Time timeA, Time timeB, String meuTime) {
        if (meuTime == null || meuTime.trim().isEmpty()) {
            return false;
        }
        String nomeMeuTime = meuTime.trim();
        return timeA.getNome() != null && timeA.getNome().trim().equalsIgnoreCase(nomeMeuTime)
                || timeB.getNome() != null && timeB.getNome().trim().equalsIgnoreCase(nomeMeuTime);
    }

    private void simularConfronto(Time timeA, Time timeB, PartidaRodada partida) {
        int forcaA = Math.max(1, timeA.getForca());
        int forcaB = Math.max(1, timeB.getForca());
        double chanceEmpate = 0.15d;
        double sorteio = random.nextDouble();

        timeA.setJogos(timeA.getJogos() + 1);
        timeB.setJogos(timeB.getJogos() + 1);

        if (sorteio < chanceEmpate) {
            registrarEmpate(timeA, timeB, partida);
            return;
        }

        double chanceVitoriaA = (double) forcaA / (forcaA + forcaB);
        double sorteioSemEmpate = (sorteio - chanceEmpate) / (1.0d - chanceEmpate);

        Time vencedor = sorteioSemEmpate <= chanceVitoriaA ? timeA : timeB;
        Time derrotado = vencedor == timeA ? timeB : timeA;

        registrarVitoria(vencedor, derrotado, partida);
    }

    private void jogarPartidaMeuTime(Time timeA, Time timeB, PartidaRodada partida) {
        ResultadoPenaltis resultado = disputaPenaltisUI.disputar(timeA.getNome(), timeB.getNome());
        aplicarResultadoPorGols(timeA, timeB, partida, resultado.getGolsTimeA(), resultado.getGolsTimeB());
    }

    private void aplicarResultadoPorGols(Time timeA, Time timeB, PartidaRodada partida, int golsTimeA, int golsTimeB) {
        timeA.setJogos(timeA.getJogos() + 1);
        timeB.setJogos(timeB.getJogos() + 1);

        if (golsTimeA == golsTimeB) {
            registrarEmpate(timeA, timeB, partida);
            return;
        }

        Time vencedor = golsTimeA > golsTimeB ? timeA : timeB;
        Time derrotado = vencedor == timeA ? timeB : timeA;
        registrarVitoria(vencedor, derrotado, partida);
    }

    private void registrarEmpate(Time timeA, Time timeB, PartidaRodada partida) {
        timeA.setEmpates(timeA.getEmpates() + 1);
        timeB.setEmpates(timeB.getEmpates() + 1);
        timeA.setPontos(timeA.getPontos() + PONTOS_EMPATE);
        timeB.setPontos(timeB.getPontos() + PONTOS_EMPATE);
        partida.setVencedorId(null);
        partida.setStatus(STATUS_FINALIZADO);
    }

    private void registrarVitoria(Time vencedor, Time derrotado, PartidaRodada partida) {
        vencedor.setVitorias(vencedor.getVitorias() + 1);
        vencedor.setPontos(vencedor.getPontos() + PONTOS_VITORIA);
        vencedor.setForca(vencedor.getForca() + 1);

        derrotado.setDerrotas(derrotado.getDerrotas() + 1);
        derrotado.setForca(Math.max(1, derrotado.getForca() - 1));

        partida.setVencedorId(vencedor.getId());
        partida.setStatus(STATUS_FINALIZADO);
    }

    private boolean temporadaEncerrada(TemporadaRodadas temporadaRodadas) {
        List<PartidaRodada> partidas = temporadaRodadas.getPartidas();
        if (partidas == null || partidas.isEmpty()) {
            return false;
        }
        for (PartidaRodada partida : partidas) {
            if (!STATUS_FINALIZADO.equals(partida.getStatus())) {
                return false;
            }
        }
        return true;
    }

    private void abrirNovaTemporada(Dados dados, Temporada temporadaAtual) {
        temporadaAtual.setAtiva(false);

        Temporada novaTemporada = new Temporada();
        novaTemporada.setId(proximoIdTemporada(dados.getTemporadas()));
        novaTemporada.setAno(temporadaAtual.getAno() + 1);
        novaTemporada.setNome("Campeonato " + novaTemporada.getAno());
        novaTemporada.setRodada(0);
        novaTemporada.setAtiva(true);
        novaTemporada.setGrupos(clonarGruposParaNovaTemporada(temporadaAtual.getGrupos()));

        dados.getTemporadas().add(novaTemporada);
    }

    private Long proximoIdTemporada(List<Temporada> temporadas) {
        long maiorId = 0L;
        for (Temporada temporada : temporadas) {
            if (temporada.getId() != null && temporada.getId() > maiorId) {
                maiorId = temporada.getId();
            }
        }
        return maiorId + 1L;
    }

    private List<Grupo> clonarGruposParaNovaTemporada(List<Grupo> gruposOriginais) {
        List<Grupo> gruposClonados = new ArrayList<>();
        if (gruposOriginais == null) {
            return gruposClonados;
        }

        for (Grupo grupoOriginal : gruposOriginais) {
            Grupo grupoNovo = new Grupo();
            grupoNovo.setId(grupoOriginal.getId());
            grupoNovo.setNome(grupoOriginal.getNome());
            grupoNovo.setTimes(clonarTimesParaNovaTemporada(grupoOriginal.getTimes()));
            gruposClonados.add(grupoNovo);
        }
        return gruposClonados;
    }

    private List<Time> clonarTimesParaNovaTemporada(List<Time> timesOriginais) {
        List<Time> timesClonados = new ArrayList<>();
        if (timesOriginais == null) {
            return timesClonados;
        }

        for (Time timeOriginal : timesOriginais) {
            Time timeNovo = new Time();
            timeNovo.setId(timeOriginal.getId());
            timeNovo.setNome(timeOriginal.getNome());
            timeNovo.setForca(Math.max(1, timeOriginal.getForca()));
            timeNovo.setPontos(0);
            timeNovo.setJogos(0);
            timeNovo.setVitorias(0);
            timeNovo.setEmpates(0);
            timeNovo.setDerrotas(0);
            timesClonados.add(timeNovo);
        }
        return timesClonados;
    }
}
