/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import java.io.IOException;


public class DadosService {
    
    public Dados lerDados() {
        ArquivoJson arquivo = new ArquivoJson("src/service/temporadas.json");
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
        ArquivoJson arquivo = new ArquivoJson("src/service/temporadas.json");
        try {
            arquivo.salvar(dados);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar temporadas.json", e);
        }
    }

}
