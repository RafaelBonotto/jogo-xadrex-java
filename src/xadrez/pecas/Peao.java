package xadrez.pecas;

import jogo.Posicao;
import jogo.Tabuleiro;
import xadrez.Cor;
import xadrez.Partida;
import xadrez.XadrezPeca;

public class Peao extends XadrezPeca {

    private Partida partida;

    public Peao(Tabuleiro tabuleiro, Cor cor, Partida partida) {
        super(tabuleiro, cor);
        this.partida = partida;
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
            // #Movimento Especial En Passant Branca
            if(posicao.getLinha() == 3){
                Posicao esquerda = new Posicao(posicao.getLinha(), posicao.getColuna() - 1);
                if(getTabuleiro().posicaoExiste(esquerda) && existePecaDoOponente(esquerda) && getTabuleiro().peca(esquerda) == partida.getVuneravelEnPassant()){
                    mat[esquerda.getLinha() - 1][esquerda.getColuna()] = true;
                }
                Posicao direita = new Posicao(posicao.getLinha(), posicao.getColuna() + 1);
                if(getTabuleiro().posicaoExiste(direita) && existePecaDoOponente(direita) && getTabuleiro().peca(direita) == partida.getVuneravelEnPassant()){
                    mat[direita.getLinha() - 1][direita.getColuna()] = true;
                }
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
            // #Movimento Especial En Passant Preta
            if(posicao.getLinha() == 4){
                Posicao esquerda = new Posicao(posicao.getLinha(), posicao.getColuna() - 1);
                if(getTabuleiro().posicaoExiste(esquerda) && existePecaDoOponente(esquerda) && getTabuleiro().peca(esquerda) == partida.getVuneravelEnPassant()){
                    mat[esquerda.getLinha() + 1][esquerda.getColuna()] = true;
                }
                Posicao direita = new Posicao(posicao.getLinha(), posicao.getColuna() + 1);
                if(getTabuleiro().posicaoExiste(direita) && existePecaDoOponente(direita) && getTabuleiro().peca(direita) == partida.getVuneravelEnPassant()){
                    mat[direita.getLinha() + 1][direita.getColuna()] = true;
                }
            }
        }
        return mat;
    }

    @Override
    public String toString(){
        return "P";
    }
}
