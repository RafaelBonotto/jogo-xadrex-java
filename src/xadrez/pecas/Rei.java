package xadrez.pecas;

import jogo.Posicao;
import jogo.Tabuleiro;
import xadrez.Cor;
import xadrez.Partida;
import xadrez.XadrezPeca;

public class Rei extends XadrezPeca {

    private Partida partida;

    public Rei(Tabuleiro tabuleiro, Cor cor, Partida partida) {
        super(tabuleiro, cor);
        this.partida = partida;
    }

    @Override
    public String toString(){
        return "R";
    }
    
    private boolean podeMover(Posicao posicao){
        XadrezPeca p = (XadrezPeca)getTabuleiro().peca(posicao);
        return p == null || p.getCor() != getCor();
    }

    private boolean testTorreParaRoque(Posicao posicao){
        XadrezPeca p = (XadrezPeca)getTabuleiro().peca(posicao);
        return p != null && p instanceof Torre && p.getCor() == getCor() && p.getContagemDeMovimentos() == 0;
    }

    @Override
    public boolean[][] movimentosPossiveis() {
        boolean[][] mat = new boolean[getTabuleiro().getLinhas()][getTabuleiro().getColunas()];

        Posicao p = new Posicao(0,0);
        // Acima:
        p.setValores(posicao.getLinha() - 1, posicao.getColuna());
        if(getTabuleiro().posicaoExiste(p) && podeMover(p)){
            mat[p.getLinha()][p.getColuna()] = true;
        }

        // Abaixo:
        p.setValores(posicao.getLinha() + 1, posicao.getColuna());
        if(getTabuleiro().posicaoExiste(p) && podeMover(p)){
            mat[p.getLinha()][p.getColuna()] = true;
        }

        // Esquerda:
        p.setValores(posicao.getLinha(), posicao.getColuna() - 1);
        if(getTabuleiro().posicaoExiste(p) && podeMover(p)){
            mat[p.getLinha()][p.getColuna()] = true;
        }

        // Direita:
        p.setValores(posicao.getLinha(), posicao.getColuna() + 1);
        if(getTabuleiro().posicaoExiste(p) && podeMover(p)){
            mat[p.getLinha()][p.getColuna()] = true;
        }

        // Noroeste:
        p.setValores(posicao.getLinha() - 1, posicao.getColuna() - 1);
        if(getTabuleiro().posicaoExiste(p) && podeMover(p)){
            mat[p.getLinha()][p.getColuna()] = true;
        }

        // Nordeste:
        p.setValores(posicao.getLinha() - 1, posicao.getColuna() + 1);
        if(getTabuleiro().posicaoExiste(p) && podeMover(p)){
            mat[p.getLinha()][p.getColuna()] = true;
        }

        // Sudoeste:
        p.setValores(posicao.getLinha() + 1, posicao.getColuna() - 1);
        if(getTabuleiro().posicaoExiste(p) && podeMover(p)){
            mat[p.getLinha()][p.getColuna()] = true;
        }

        // Sudeste:
        p.setValores(posicao.getLinha() + 1, posicao.getColuna() + 1);
        if(getTabuleiro().posicaoExiste(p) && podeMover(p)){
            mat[p.getLinha()][p.getColuna()] = true;
        }

        // #Movimento especial 'Roque'
        if(getContagemDeMovimentos() == 0 && !partida.getCheck()){
            //#Movimeto especial Roque pequeno (Lado da Torre)
            Posicao posT1 = new Posicao(posicao.getLinha(), posicao.getColuna() + 3);
            if(testTorreParaRoque(posT1)){
                Posicao p1 =  new Posicao(posicao.getLinha(), posicao.getColuna() + 1);
                Posicao p2 =  new Posicao(posicao.getLinha(), posicao.getColuna() + 2);
                if(getTabuleiro().peca(p1) == null && getTabuleiro().peca(p2) == null){
                    mat[posicao.getLinha()][posicao.getColuna() + 2] = true;
                }
            }
            //#Movimeto especial Roque grande (Lado da Rainha)
            Posicao posT2 = new Posicao(posicao.getLinha(), posicao.getColuna() - 4);
            if(testTorreParaRoque(posT2)){
                Posicao p1 =  new Posicao(posicao.getLinha(), posicao.getColuna() - 1);
                Posicao p2 =  new Posicao(posicao.getLinha(), posicao.getColuna() - 2);
                Posicao p3 =  new Posicao(posicao.getLinha(), posicao.getColuna() - 3);
                if(getTabuleiro().peca(p1) == null && getTabuleiro().peca(p2) == null && getTabuleiro().peca(p3) == null){
                    mat[posicao.getLinha()][posicao.getColuna() - 2] = true;
                }
            }
        }

        return mat;
    }
}
