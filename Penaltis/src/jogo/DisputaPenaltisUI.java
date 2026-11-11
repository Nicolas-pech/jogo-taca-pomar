package jogo;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import tela.GamePanel;

public class DisputaPenaltisUI {

    public ResultadoPenaltis disputar(String nomeTimeA, String nomeTimeB) {
        if (SwingUtilities.isEventDispatchThread()) {
            throw new IllegalStateException("A disputa nao pode iniciar na thread de UI.");
        }

        final CountDownLatch latch = new CountDownLatch(1);
        final AtomicReference<ResultadoPenaltis> resultadoRef = new AtomicReference<>();

        SwingUtilities.invokeLater(() -> {
            JFrame janela = new JFrame("Disputa de Penaltis");
            janela.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            janela.setSize(1200, 700);
            janela.setLocationRelativeTo(null);
            janela.setResizable(false);

            GamePanel gamePanel = new GamePanel(nomeTimeA, nomeTimeB, (golsTimeA, golsTimeB) -> {
                resultadoRef.set(new ResultadoPenaltis(golsTimeA, golsTimeB));
                janela.dispose();
                latch.countDown();
            });

            janela.add(gamePanel);
            janela.setVisible(true);
            gamePanel.iniciarJogo();
        });

        try {
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Interrompido ao aguardar resultado da disputa.", e);
        }

        ResultadoPenaltis resultado = resultadoRef.get();
        if (resultado == null) {
            throw new IllegalStateException("A disputa foi encerrada sem resultado.");
        }
        return resultado;
    }
}
