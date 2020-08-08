package xadrez;

import jogo.Peca;
import jogo.Posicao;
import jogo.Tabuleiro;
import xadrez.pecas.Rei;
import xadrez.pecas.Torre;

public class Partida {

    private Tabuleiro tabuleiro;

    public Partida(){
        tabuleiro = new Tabuleiro(8,8);
        configInicial();
    }

    public XadrezPeca[][] getPecas(){
        XadrezPeca[][] mat = new XadrezPeca[tabuleiro.getLinhas()][tabuleiro.getColunas()];
        for(int i = 0; i<tabuleiro.getLinhas(); i ++){
            for(int j = 0; j<tabuleiro.getColunas(); j ++){
                mat[i][j] = (XadrezPeca) tabuleiro.peca(i,j);
            }
        }
        return mat;
    }

    public XadrezPeca executarMovimento(XadrezPosicao posicaoOrigem, XadrezPosicao posicaoDestino){
        Posicao origem = posicaoOrigem.posicionar();
        Posicao destino = posicaoDestino.posicionar();
        validarPosicaoOrigem(origem);
        validarPosicaoDestino(origem, destino);
        Peca pecaCapturada = movimento(origem, destino);
        return (XadrezPeca)pecaCapturada;
    }

    private Peca movimento (Posicao origem, Posicao destino){
        Peca p = tabuleiro.removerPeca(origem);
        Peca pecaCapturada = tabuleiro.removerPeca(destino);
        tabuleiro.colocarPeca(p, destino);
        return pecaCapturada;
    }

    private void validarPosicaoOrigem(Posicao posicao){
        if(!tabuleiro.pecaExiste(posicao)){
            throw new XadrezExcepition("Não existe peça na posição de origem");
        }
        if(!tabuleiro.peca(posicao).existeAlgumMovimentoPossivel()){
            throw new XadrezExcepition("Não existe movimentos possíveis para a peça escolhida");
        }
    }

    private void validarPosicaoDestino(Posicao origem, Posicao destino){
        if(!tabuleiro.peca(origem).movimetoPossivel(destino)){
            throw new XadrezExcepition("A peça escolhida não pode se mover para posição de destino!");
        }
    }

    private void colocarNovaPeca(char coluna, int linha, XadrezPeca peca){
        tabuleiro.colocarPeca(peca,new XadrezPosicao(coluna, linha).posicionar());

    }

    private void configInicial(){
        colocarNovaPeca('c', 1, new Torre(tabuleiro, Cor.BRANCO));
        colocarNovaPeca('c', 2, new Torre(tabuleiro, Cor.BRANCO));
        colocarNovaPeca('d', 2, new Torre(tabuleiro, Cor.BRANCO));
        colocarNovaPeca('e', 2, new Torre(tabuleiro, Cor.BRANCO));
        colocarNovaPeca('e', 1, new Torre(tabuleiro, Cor.BRANCO));
        colocarNovaPeca('d', 1, new Rei(tabuleiro, Cor.BRANCO));

        colocarNovaPeca('c', 7, new Torre(tabuleiro, Cor.PRETO));
        colocarNovaPeca('c', 8, new Torre(tabuleiro, Cor.PRETO));
        colocarNovaPeca('d', 7, new Torre(tabuleiro, Cor.PRETO));
        colocarNovaPeca('e', 7, new Torre(tabuleiro, Cor.PRETO));
        colocarNovaPeca('e', 8, new Torre(tabuleiro, Cor.PRETO));
        colocarNovaPeca('d', 8, new Rei(tabuleiro, Cor.PRETO));
    }
}
