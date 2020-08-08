package xadrez;

import jogo.Peca;
import jogo.Posicao;
import jogo.Tabuleiro;

public abstract class XadrezPeca extends Peca {


    private Cor cor;

    public XadrezPeca(Tabuleiro tabuleiro, Cor cor) {
        super(tabuleiro);
        this.cor = cor;
    }

    public Cor getCor() {
        return cor;
    }

    protected boolean existePecaDoOponente(Posicao posicao){
        XadrezPeca p = (XadrezPeca)getTabuleiro().peca(posicao);
        return p != null && p.getCor() != cor;
    }
}
