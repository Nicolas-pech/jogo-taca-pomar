package entidades;

import java.awt.Graphics2D;
import java.awt.Image;
import javax.swing.ImageIcon;

public class Campo {

    private int x;
    private int y;

    private int largura;
    private int altura;

    private Image imagem;

    public Campo(int larguraTela, int alturaTela) {
        this.x = 0;
        this.y = 0;
        this.largura = larguraTela;
        this.altura = alturaTela;
        imagem = new ImageIcon("src/imagens/campo/campo.png").getImage();
    }

    public void desenhar(Graphics2D g2) {
        g2.drawImage(imagem, x, y, largura, altura, null);
    }

    public int getPenaltiX() {
        return x + largura / 2;
    }

    public int getPenaltiY() {
        return y + altura - 180;
    }

    public int getGolX() {
        int larguraGol = 260;
        return x + largura / 2 - larguraGol / 2;
    }

    public int getGolY() {
        return y;
    }

    public int getGolLargura() {
        return 260;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getLargura() {
        return largura;
    }

    public int getAltura() {
        return altura;
    }
}
