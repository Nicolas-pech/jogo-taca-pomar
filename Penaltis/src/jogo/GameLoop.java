/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jogo;

import tela.GamePanel;


public class GameLoop implements Runnable {

    private final GamePanel gamePanel;

    private boolean rodando;

    public GameLoop(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    public void iniciar() {

        if (rodando) {
            return;
        }

        rodando = true;

        Thread thread = new Thread(this);
        thread.setDaemon(true);
        thread.start();
    }

    public void parar() {
        rodando = false;
    }

    @Override
    public void run() {
        final int FPS = 60;
        final long TEMPO_FRAME = 1_000_000_000 / FPS;
        long ultimoTempo = System.nanoTime();
        while (rodando) {
            long tempoAtual = System.nanoTime();
            if (tempoAtual - ultimoTempo >= TEMPO_FRAME) {
                atualizar();
                gamePanel.repaint();
                ultimoTempo = tempoAtual;
            }
        }
    }

    private void atualizar() {
         gamePanel.atualizar();
    }
}