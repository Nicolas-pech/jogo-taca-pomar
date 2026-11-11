package service;

public class PartidaRodada {

    private Long temporadaId;
    private Long grupoId;
    private String grupoNome;
    private int rodada;
    private int timeAId;
    private String timeANome;
    private int timeBId;
    private String timeBNome;
    private String status;
    private Integer vencedorId;

    public Long getTemporadaId() {
        return temporadaId;
    }

    public void setTemporadaId(Long temporadaId) {
        this.temporadaId = temporadaId;
    }

    public Long getGrupoId() {
        return grupoId;
    }

    public void setGrupoId(Long grupoId) {
        this.grupoId = grupoId;
    }

    public String getGrupoNome() {
        return grupoNome;
    }

    public void setGrupoNome(String grupoNome) {
        this.grupoNome = grupoNome;
    }

    public int getRodada() {
        return rodada;
    }

    public void setRodada(int rodada) {
        this.rodada = rodada;
    }

    public int getTimeAId() {
        return timeAId;
    }

    public void setTimeAId(int timeAId) {
        this.timeAId = timeAId;
    }

    public String getTimeANome() {
        return timeANome;
    }

    public void setTimeANome(String timeANome) {
        this.timeANome = timeANome;
    }

    public int getTimeBId() {
        return timeBId;
    }

    public void setTimeBId(int timeBId) {
        this.timeBId = timeBId;
    }

    public String getTimeBNome() {
        return timeBNome;
    }

    public void setTimeBNome(String timeBNome) {
        this.timeBNome = timeBNome;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getVencedorId() {
        return vencedorId;
    }

    public void setVencedorId(Integer vencedorId) {
        this.vencedorId = vencedorId;
    }
}
