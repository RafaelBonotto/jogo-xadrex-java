package xadrez.pecas;

import jogo.Tabuleiro;
import xadrez.Cor;
import xadrez.XadrezPeca;

public class Rei extends XadrezPeca {


    public Rei(Tabuleiro tabuleiro, Cor cor) {
        super(tabuleiro, cor);
    }

    @Override
    public String toString(){
        return "R";
    }

    @Override
    public boolean[][] movimentosPossiveis() {
        boolean[][] mat = new boolean[getTabuleiro().getLinhas()][getTabuleiro().getColunas()];
        return mat;
    }
}
