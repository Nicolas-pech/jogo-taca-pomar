/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import java.awt.Graphics2D;
import java.awt.Image;
import javax.swing.ImageIcon;

public class Bola {

    private double x;
    private double y;
    private double velocidadeX;
    private double velocidadeY;
    private final int tamanho = 60;
    private boolean emMovimento;
    private Image imagem;

    public Bola(double x, double y) {
        this.x = x;
        this.y = y;
        this.velocidadeX = 0;
        this.velocidadeY = 0;
        this.emMovimento = false;
        imagem = new ImageIcon("src/imagens/bola/bola.png").getImage();
    }

    public void atualizar() {
        if (!emMovimento) {
            return;
        }
        x += velocidadeX;
        y += velocidadeY;
    }

    public void chutar(double velocidadeX, double velocidadeY) {
        this.velocidadeX = velocidadeX;
        this.velocidadeY = velocidadeY;
        this.emMovimento = true;
    }

    public void parar() {
        velocidadeX = 0;
        velocidadeY = 0;
        emMovimento = false;
    }

    public void desenhar(Graphics2D g2) {
        g2.drawImage(imagem, (int) x, (int) y, tamanho, tamanho, null);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public int getTamanho() {
        return tamanho;
    }

    public boolean isEmMovimento() {
        return emMovimento;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getVelocidadeX() {
        return velocidadeX;
    }

    public double getVelocidadeY() {
        return velocidadeY;
    }
}
