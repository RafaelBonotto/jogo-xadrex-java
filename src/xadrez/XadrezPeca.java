package xadrez;

import jogo.Peca;
import jogo.Posicao;
import jogo.Tabuleiro;

public abstract class XadrezPeca extends Peca {


    private Cor cor;
    private int contagemDeMovimentos;

    public XadrezPeca(Tabuleiro tabuleiro, Cor cor) {
        super(tabuleiro);
        this.cor = cor;
    }

    public Cor getCor() {
        return cor;
    }

    public int getContagemDeMovimentos() {
        return contagemDeMovimentos;
    }

    public void aumentarContagemDeMovimentos(){
        contagemDeMovimentos ++;
    }

    public void diminuirContagemDeMovimentos(){
        contagemDeMovimentos --;
    }

    public XadrezPosicao getXadrezPosicao(){
        return XadrezPosicao.dePosicao(posicao);
    }

    protected boolean existePecaDoOponente(Posicao posicao){
        XadrezPeca p = (XadrezPeca)getTabuleiro().peca(posicao);
        return p != null && p.getCor() != cor;
    }
}
