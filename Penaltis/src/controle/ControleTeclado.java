/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controle;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import jogo.Jogo;

public class ControleTeclado implements KeyListener {

    private final Jogo jogo;
    private final boolean modoDisputaComRetorno;

    public ControleTeclado(Jogo jogo) {
        this(jogo, false);
    }

    public ControleTeclado(Jogo jogo, boolean modoDisputaComRetorno) {
        this.jogo = jogo;
        this.modoDisputaComRetorno = modoDisputaComRetorno;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            jogo.getBatedor().moverEsquerda(true);
        }

        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            jogo.getBatedor().moverDireita(true);
        }
        
        if (e.getKeyCode() == KeyEvent.VK_Q) {
            jogo.escolherEsquerdaSuperior();
        }

        if (e.getKeyCode() == KeyEvent.VK_E) {
            jogo.escolherDireitaSuperior();
        }

        if (e.getKeyCode() == KeyEvent.VK_Z) {
            jogo.escolherEsquerdaInferior();
        }

        if (e.getKeyCode() == KeyEvent.VK_C) {
            jogo.escolherDireitaInferior();
        }

        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            jogo.chutar();
        }

        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            if (modoDisputaComRetorno) {
                jogo.confirmarResultadoFinal();
            } else {
                jogo.iniciarNovaPartida();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

        if (e.getKeyCode() == KeyEvent.VK_LEFT) {

            jogo.getBatedor().moverEsquerda(false);
        }

        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {

            jogo.getBatedor().moverDireita(false);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }
}
