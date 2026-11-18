/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import java.io.IOException;


public class DadosService {

    private static final String CAMINHO_TEMPORADAS = "src/service/temporadas.json";
    private static final String CAMINHO_TIME_USUARIO = "src/service/time-usuario.json";
    
    public Dados lerDados() {
        ArquivoJson arquivo = new ArquivoJson(CAMINHO_TEMPORADAS);
        try {
            Dados dados = arquivo.ler(Dados.class);
            if (dados == null) {
                dados = new Dados();
            }
            return dados;
        } catch (IOException e) {
            System.err.println(
                    "Erro ao acessar o arquivo: "
                    + e.getMessage()
            );
            return null;
        }
    }

    public void salvarDados(Dados dados) {
        ArquivoJson arquivo = new ArquivoJson(CAMINHO_TEMPORADAS);
        try {
            arquivo.salvar(dados);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar temporadas.json", e);
        }
    }

    public String buscarNomeTimeEscolhido() {
        ArquivoJson arquivo = new ArquivoJson(CAMINHO_TIME_USUARIO);
        try {
            TimeEscolhido timeEscolhido = arquivo.ler(TimeEscolhido.class);
            if (timeEscolhido == null || timeEscolhido.getNomeTime() == null) {
                return null;
            }
            String nomeTime = timeEscolhido.getNomeTime().trim();
            if (nomeTime.isEmpty()) {
                return null;
            }
            return nomeTime;
        } catch (IOException e) {
            System.err.println(
                    "Erro ao acessar o arquivo: "
                    + e.getMessage()
            );
            return null;
        }
    }

    public String salvarTimeEscolhidoSeAusente(String nomeTime) {
        String timeJaEscolhido = buscarNomeTimeEscolhido();
        if (timeJaEscolhido != null) {
            return timeJaEscolhido;
        }

        if (nomeTime == null || nomeTime.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do time inválido para salvar.");
        }

        String nomeNormalizado = nomeTime.trim();
        TimeEscolhido timeEscolhido = new TimeEscolhido();
        timeEscolhido.setNomeTime(nomeNormalizado);

        ArquivoJson arquivo = new ArquivoJson(CAMINHO_TIME_USUARIO);
        try {
            arquivo.salvar(timeEscolhido);
            return nomeNormalizado;
        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar time-usuario.json", e);
        }
    }

}
