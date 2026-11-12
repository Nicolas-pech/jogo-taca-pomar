package inicio;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import tela.GamePanel;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame janela = new JFrame("Jogo de Pênaltis");
            janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            janela.setSize(1200, 700);
            janela.setLocationRelativeTo(null);
            janela.setResizable(false);

            GamePanel gamePanel = new GamePanel();
            janela.add(gamePanel);
            janela.setVisible(true);
            gamePanel.iniciarJogo();
        });
    }
}