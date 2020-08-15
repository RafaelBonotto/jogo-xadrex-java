package xadrez.pecas;

import jogo.Posicao;
import jogo.Tabuleiro;
import xadrez.Cor;
import xadrez.XadrezPeca;

public class Peao extends XadrezPeca {

    public Peao(Tabuleiro tabuleiro, Cor cor) {
        super(tabuleiro, cor);
    }

    @Override
    public boolean[][] movimentosPossiveis() {
        boolean[][] mat = new boolean[getTabuleiro().getLinhas()][getTabuleiro().getColunas()];

        Posicao p = new Posicao(0, 0);

        if(getCor() == Cor.BRANCO){
            p.setValores(posicao.getLinha() - 1, posicao.getColuna());
            if(getTabuleiro().posicaoExiste(p) && !getTabuleiro().pecaExiste(p)){
                mat [p.getLinha()][p.getColuna()] = true;
            }
            p.setValores(posicao.getLinha() - 2, posicao.getColuna());
            Posicao p2 = new Posicao(posicao.getLinha() - 1, posicao.getColuna());
            if(getTabuleiro().posicaoExiste(p) && !getTabuleiro().pecaExiste(p) && getTabuleiro().posicaoExiste(p2) && !getTabuleiro().pecaExiste(p2) && getContagemDeMovimentos() == 0){
                mat [p.getLinha()][p.getColuna()] = true;
            }
            p.setValores(posicao.getLinha() - 1, posicao.getColuna() - 1);
            if(getTabuleiro().posicaoExiste(p) && existePecaDoOponente(p)){
                mat [p.getLinha()][p.getColuna()] = true;
            }
            p.setValores(posicao.getLinha() - 1, posicao.getColuna() + 1);
            if(getTabuleiro().posicaoExiste(p) && existePecaDoOponente(p)){
                mat [p.getLinha()][p.getColuna()] = true;
            }
        }
        else{
            p.setValores(posicao.getLinha() + 1, posicao.getColuna());
            if(getTabuleiro().posicaoExiste(p) && !getTabuleiro().pecaExiste(p)){
                mat [p.getLinha()][p.getColuna()] = true;
            }
            p.setValores(posicao.getLinha() + 2, posicao.getColuna());
            Posicao p2 = new Posicao(posicao.getLinha() + 1, posicao.getColuna());
            if(getTabuleiro().posicaoExiste(p) && !getTabuleiro().pecaExiste(p) && getTabuleiro().posicaoExiste(p2) && !getTabuleiro().pecaExiste(p2) && getContagemDeMovimentos() == 0){
                mat [p.getLinha()][p.getColuna()] = true;
            }
            p.setValores(posicao.getLinha() + 1, posicao.getColuna() - 1);
            if(getTabuleiro().posicaoExiste(p) && existePecaDoOponente(p)){
                mat [p.getLinha()][p.getColuna()] = true;
            }
            p.setValores(posicao.getLinha() + 1, posicao.getColuna() + 1);
            if(getTabuleiro().posicaoExiste(p) && existePecaDoOponente(p)){
                mat [p.getLinha()][p.getColuna()] = true;
            }
        }
        return mat;
    }

    @Override
    public String toString(){
        return "P";
    }
}
