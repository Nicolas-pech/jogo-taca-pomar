
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JLabel;
import service.Time;
import java.util.Comparator;
import java.util.stream.Collectors;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import service.IniciarDisputa;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Escola João XXIII
 */
public class Classificacao01 extends javax.swing.JFrame {

    JLabel jlTime1 = new JLabel();
    JLabel jl8 = new JLabel();
    JLabel jl9 = new JLabel();
    JLabel jl6 = new JLabel();
    JLabel jl4 = new JLabel();
    JLabel jl3 = new JLabel();
    JLabel jl7 = new JLabel();
    JLabel jl5 = new JLabel();
    JLabel jl2 = new JLabel();
    List<Time> timesGrupoA = new ArrayList();
    List<Time> timesGrupoB = new ArrayList();

    JLabel jlEscudoTime1 = new JLabel();
    JLabel jlEscudoTime2 = new JLabel();
    JLabel jlEscudoTime3 = new JLabel();
    JLabel jlEscudoTime4 = new JLabel();
    JLabel jlEscudoTime5 = new JLabel();
    JLabel jlEscudoTime6 = new JLabel();
    JLabel jlEscudoTime7 = new JLabel();
    JLabel jlEscudoTime8 = new JLabel();
    JLabel jlEscudoTime9 = new JLabel();

    ImageIcon godoiense = new ImageIcon(getClass().getResource("/imagens/godoiense.png"));
    ImageIcon pumas = new ImageIcon(getClass().getResource("/imagens/pumas.png"));
    ImageIcon atlantico = new ImageIcon(getClass().getResource("/imagens/atlantico.png"));
    ImageIcon caxias = new ImageIcon(getClass().getResource("/imagens/caxias.png"));
    ImageIcon operario = new ImageIcon(getClass().getResource("/imagens/operario.png"));
    ImageIcon cruzeiro = new ImageIcon(getClass().getResource("/imagens/cruzeiro.png"));
    ImageIcon vira = new ImageIcon(getClass().getResource("/imagens/vira.png"));
    ImageIcon xucros = new ImageIcon(getClass().getResource("/imagens/xucros.png"));
    
    String meuTime = new String();

    public Classificacao01(List<Time> timesGrupoA, List<Time> timesGrupoB, String meuTime) {
        this.meuTime = meuTime;
        
        iniciarLabeltime(225, 140, jlTime1, "jogar", "");
        this.add(jlTime1);
        iniciarLabeltime(225, 220, jl2, "jogar", "");
        this.add(jl2);
        iniciarLabeltime(225, 305, jl3, "jogar", "");
        this.add(jl3);
        iniciarLabeltime(225, 380, jl4, "jogar", "");
        this.add(jl4);
        iniciarLabeltime(595, 140, jl5, "jogar", "");
        this.add(jl5);
        iniciarLabeltime(595, 220, jl6, "jogar", "");
        this.add(jl6);
        iniciarLabeltime(595, 305, jl7, "jogar", "");
        this.add(jl7);
        iniciarLabeltime(595, 380, jl8, "jogar", "");
        this.add(jl8);
        iniciarLabelAvancar(720, 455, jl9, "Avançar", "");
        this.add(jl9);

        iniciarLabelEscudo(165, 140, jlEscudoTime1, "jogar", "");
        this.add(jlEscudoTime1);
        iniciarLabelEscudo(165, 220, jlEscudoTime2, "jogar", "");
        this.add(jlEscudoTime2);
        iniciarLabelEscudo(165, 305, jlEscudoTime3, "jogar", "");
        this.add(jlEscudoTime3);
        iniciarLabelEscudo(165, 380, jlEscudoTime4, "jogar", "");
        this.add(jlEscudoTime4);
        iniciarLabelEscudo(535, 140, jlEscudoTime5, "jogar", "");
        this.add(jlEscudoTime5);
        iniciarLabelEscudo(535, 220, jlEscudoTime6, "jogar", "");
        this.add(jlEscudoTime6);
        iniciarLabelEscudo(535, 305, jlEscudoTime7, "jogar", "");
        this.add(jlEscudoTime7);
        iniciarLabelEscudo(535, 380, jlEscudoTime8, "jogar", "");
        this.add(jlEscudoTime8);
        iniciarLabelEscudo(660, 455, jlEscudoTime9, "jogar", "");
        this.add(jlEscudoTime9);

        this.timesGrupoA = timesGrupoA;
        this.timesGrupoB = timesGrupoB;

        initComponents();
        this.setLocationRelativeTo(null);
        preencherclass();

    }

    public void iniciarLabelEscudo(int x, int y, JLabel label, String op, String texto) {
        label.setText(texto);
        label.setBorder(BorderFactory.createLineBorder(Color.yellow, 2));
        label.setOpaque(false);
        label.setBounds(x, y, 60, 60);
        label.setFont(new Font("Segoe UI", Font.BOLD, 20));
        label.setForeground(new Color(255, 16, 84));
    }

    public void iniciarLabeltime(int x, int y, JLabel label, String op, String texto) {
        label.setText(texto);
        label.setBorder(BorderFactory.createLineBorder(Color.yellow, 2));
        label.setOpaque(false);
        label.setBounds(x, y, 200, 60);
        label.setFont(new Font("Segoe UI", Font.BOLD, 20));
        label.setForeground(new Color(6, 16, 84));
    }

    public void iniciarLabelAvancar(int x, int y, JLabel label, String op, String texto) {
        label.setText(texto);
        //label.setBorder(BorderFactory.createLineBorder(Color.yellow, 2));
        label.setOpaque(false);
        label.setBounds(x, y, 260, 60);
        label.setFont(new Font("Segoe UI", Font.BOLD, 20));
        label.setForeground(new Color(6, 16, 84));
        label.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent e) {
                setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                dispose();
                new IniciarDisputa(meuTime);
            }
        });
    }

    public void preencherclass() {
        timesGrupoA = timesGrupoA
                .stream()
                .sorted(Comparator.comparingInt(Time::getPontos).reversed())
                .collect(Collectors.toList());
        timesGrupoB = timesGrupoB
                .stream()
                .sorted(Comparator.comparingInt(Time::getPontos).reversed())
                .collect(Collectors.toList());
        jlTime1.setText(" " + timesGrupoA.get(0).getNome() + " | PTS: " + timesGrupoA.get(0).getPontos());
        jl2.setText(" " + timesGrupoA.get(1).getNome() + " | PTS: " + timesGrupoA.get(1).getPontos());
        jl3.setText(" " + timesGrupoA.get(2).getNome() + " | PTS: " + timesGrupoA.get(2).getPontos());
        jl4.setText(" " + timesGrupoA.get(3).getNome() + " | PTS: " + timesGrupoA.get(3).getPontos());
        jl5.setText(" " + timesGrupoB.get(0).getNome() + " | PTS: " + timesGrupoB.get(0).getPontos());
        jl6.setText(" " + timesGrupoB.get(1).getNome() + " | PTS: " + timesGrupoB.get(1).getPontos());
        jl7.setText(" " + timesGrupoB.get(2).getNome() + " | PTS: " + timesGrupoB.get(2).getPontos());
        jl8.setText(" " + timesGrupoB.get(3).getNome() + " | PTS: " + timesGrupoB.get(3).getPontos());

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/Taça pomar - Jogo.png"))); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    // End of variables declaration//GEN-END:variables
}
