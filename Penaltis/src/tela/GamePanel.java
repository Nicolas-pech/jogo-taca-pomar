package tela;

import controle.ControleTeclado;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import jogo.GameLoop;
import jogo.Jogo;

public class GamePanel extends JPanel {

    private GameLoop gameLoop;
    private Jogo jogo;
    private ResultadoListener resultadoListener;
    private final boolean modoDisputaComRetorno;
    private boolean resultadoNotificado;

    public interface ResultadoListener {
        void aoFinalizar(int golsTimeA, int golsTimeB);
    }

    public GamePanel() {
        this("Time A", "Time B", null);
    }

    public GamePanel(String nomeTimeA, String nomeTimeB, ResultadoListener resultadoListener) {
        setFocusable(true);
        this.resultadoListener = resultadoListener;
        this.modoDisputaComRetorno = resultadoListener != null;
        jogo = new Jogo(1200, 700, nomeTimeA, nomeTimeB);
        addKeyListener(new ControleTeclado(jogo, modoDisputaComRetorno));
    }

    public void iniciarJogo() {
        requestFocusInWindow();
        gameLoop = new GameLoop(this);
        gameLoop.iniciar();
    }

    public void atualizar() {
        jogo.atualizar();
        if (!jogo.isDisputaFinalizada()) {
            resultadoNotificado = false;
        } else if (!resultadoNotificado && jogo.isResultadoFinalConfirmado() && resultadoListener != null) {
            resultadoNotificado = true;
            resultadoListener.aoFinalizar(jogo.getGolsTimeA(), jogo.getGolsTimeB());
        }
    }

    @Override
    public void removeNotify() {
        if (gameLoop != null) {
            gameLoop.parar();
        }
        super.removeNotify();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        jogo.desenhar(g2);
        g2.setColor(Color.WHITE);
        g2.drawString("Placar: " + jogo.getNomeTimeA() + " " + jogo.getGolsTimeA() + " x " + jogo.getGolsTimeB() + " " + jogo.getNomeTimeB(), 20, 30);
        g2.drawString("Batidas: " + jogo.getCobrancasTimeA() + "/" + jogo.getTotalCobrancasPorTime() + " x " + jogo.getCobrancasTimeB() + "/" + jogo.getTotalCobrancasPorTime(), 20, 50);
        g2.drawString("Vez: " + (jogo.isVezTimeA() ? jogo.getNomeTimeA() : jogo.getNomeTimeB()), 20, 70);
        g2.drawString("Chute selecionado: " + jogo.getTextoDirecao(), 20, 90);
        g2.drawString("Defesa do goleiro: " + jogo.getTextoDefesa(), 20, 110);
        g2.drawString("Controles canto: Q=ESQ SUP, E=DIR SUP, Z=ESQ INF, C=DIR INF | ESPACO=CHUTAR", 20, 130);
        g2.drawString(jogo.getMensagem(), 20, 150);
        if (jogo.isExibindoResultadoFinal()) {
            if (modoDisputaComRetorno) {
                g2.drawString("Pressione ENTER para ver a classificação.", 20, 170);
            } else {
                g2.drawString("Pressione ENTER para jogar novamente.", 20, 170);
            }
            String titulo = jogo.getTextoResultadoFinal();
            String placar = jogo.getPlacarFinal();
            Font fonteAnterior = g2.getFont();
            Font fonteTitulo = new Font("Arial", Font.BOLD, 64);
            Font fontePlacar = new Font("Arial", Font.BOLD, 54);
            g2.setFont(fonteTitulo);
            FontMetrics metricsTitulo = g2.getFontMetrics(fonteTitulo);
            int xTitulo = (getWidth() - metricsTitulo.stringWidth(titulo)) / 2;
            int yTitulo = getHeight() / 2 - 10;
            g2.setColor(Color.BLACK);
            g2.drawString(titulo, xTitulo + 3, yTitulo + 3);
            g2.setColor(Color.YELLOW);
            g2.drawString(titulo, xTitulo, yTitulo);

            g2.setFont(fontePlacar);
            FontMetrics metricsPlacar = g2.getFontMetrics(fontePlacar);
            int xPlacar = (getWidth() - metricsPlacar.stringWidth(placar)) / 2;
            int yPlacar = yTitulo + 70;
            g2.setColor(Color.BLACK);
            g2.drawString(placar, xPlacar + 3, yPlacar + 3);
            g2.setColor(Color.WHITE);
            g2.drawString(placar, xPlacar, yPlacar);
            g2.setFont(fonteAnterior);
            g2.setColor(Color.WHITE);
        } else if (!jogo.getResultadoUltimaBatida().isEmpty()) {
            String textoResultado = jogo.getResultadoUltimaBatida();
            Font fonteAnterior = g2.getFont();
            Font fonteResultado = new Font("Arial", Font.BOLD, 64);
            g2.setFont(fonteResultado);
            FontMetrics metrics = g2.getFontMetrics(fonteResultado);
            int x = (getWidth() - metrics.stringWidth(textoResultado)) / 2;
            int y = getHeight() / 2;
            g2.setColor(Color.BLACK);
            g2.drawString(textoResultado, x + 3, y + 3);
            g2.setColor("GOL".equals(textoResultado) ? Color.GREEN : Color.RED);
            g2.drawString(textoResultado, x, y);
            g2.setFont(fonteAnterior);
            g2.setColor(Color.WHITE);
        }
    }
}
