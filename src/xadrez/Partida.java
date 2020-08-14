package xadrez;

import jogo.Peca;
import jogo.Posicao;
import jogo.Tabuleiro;
import xadrez.pecas.Rei;
import xadrez.pecas.Torre;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Partida {

    private int turno;
    private Cor jogadorAtual;
    private Tabuleiro tabuleiro;
    private boolean check;

    private List<Peca> pecasNoTabuleiro = new ArrayList<>();
    private List<Peca> pecasCapturadas = new ArrayList<>();

    public Partida(){
        tabuleiro = new Tabuleiro(8,8);
        turno = 1;
        jogadorAtual = Cor.BRANCO;
        configInicial();
    }

    public int getTurno() {
        return turno;
    }

    public Cor getJogadorAtual() {
        return jogadorAtual;
    }

    public boolean getCheck(){ return check; }


    public XadrezPeca[][] getPecas(){
        XadrezPeca[][] mat = new XadrezPeca[tabuleiro.getLinhas()][tabuleiro.getColunas()];
        for(int i = 0; i<tabuleiro.getLinhas(); i ++){
            for(int j = 0; j<tabuleiro.getColunas(); j ++){
                mat[i][j] = (XadrezPeca) tabuleiro.peca(i,j);
            }
        }
        return mat;
    }

    public boolean [][] movimetosPossiveis(XadrezPosicao origemPosicao){
        Posicao posicao = origemPosicao.posicionar();
        validarPosicaoOrigem(posicao);
        return tabuleiro.peca(posicao).movimentosPossiveis();
    }

    public XadrezPeca executarMovimento(XadrezPosicao posicaoOrigem, XadrezPosicao posicaoDestino){
        Posicao origem = posicaoOrigem.posicionar();
        Posicao destino = posicaoDestino.posicionar();
        validarPosicaoOrigem(origem);
        validarPosicaoDestino(origem, destino);
        Peca pecaCapturada = movimento(origem, destino);
        if(testarCheck(jogadorAtual)){
            desfazerMovimento(origem, destino, pecaCapturada);
            throw new XadrezExcepition("Você não pode se colocar em check!!");
        }
        check = (testarCheck(oponente(jogadorAtual))) ? true : false;

        proximoTurno();
        return (XadrezPeca)pecaCapturada;
    }

    private Peca movimento (Posicao origem, Posicao destino){
        Peca p = tabuleiro.removerPeca(origem);
        Peca pecaCapturada = tabuleiro.removerPeca(destino);
        tabuleiro.colocarPeca(p, destino);

        if(pecaCapturada != null){
            pecasNoTabuleiro.remove(pecaCapturada);
            pecasCapturadas.add(pecaCapturada);
        }

        return pecaCapturada;
    }

    private void desfazerMovimento(Posicao origem, Posicao destino, Peca pecaCapturada){
        Peca p = tabuleiro.removerPeca(destino);
        tabuleiro.colocarPeca(p, origem);

        if(pecaCapturada != null){
            tabuleiro.colocarPeca(pecaCapturada,destino);
            pecasCapturadas.remove(pecaCapturada);
            pecasNoTabuleiro.add(pecaCapturada);
        }
    }

    private void validarPosicaoOrigem(Posicao posicao){
        if(!tabuleiro.pecaExiste(posicao)){
            throw new XadrezExcepition("Não existe peça na posição de origem");
        }
        if(jogadorAtual != ((XadrezPeca)tabuleiro.peca(posicao)).getCor()){
            throw new XadrezExcepition("A peça escolhida não é sua!!");
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

    private void proximoTurno(){
        turno ++;
        jogadorAtual = (jogadorAtual == Cor.BRANCO) ? Cor.PRETO : Cor.BRANCO;
    }

    private Cor oponente(Cor cor){
        return (cor == Cor.BRANCO) ? Cor.PRETO : Cor.BRANCO;
    }

    private XadrezPeca rei (Cor cor){
        List<Peca> lista = pecasNoTabuleiro.stream().filter(x -> ((XadrezPeca)x).getCor() == cor).collect(Collectors.toList());
        for (Peca p : lista){
            if (p instanceof Rei){
                return (XadrezPeca)p;
            }
        }
        throw new IllegalStateException("Não existe o Rei " + cor + " no tabuleiro!");
    }

    private boolean testarCheck(Cor cor){
        Posicao reiPosicao = rei(cor).getXadrezPosicao().posicionar();
        List<Peca> pecaOponente = pecasNoTabuleiro.stream().filter(x -> ((XadrezPeca)x).getCor() == oponente(cor)).collect(Collectors.toList());
        for(Peca p : pecaOponente){
            boolean [][] mat = p.movimentosPossiveis();
            if(mat[reiPosicao.getLinha()][reiPosicao.getColuna()]){
                return true;
            }

        }
        return false;
    }

    private void colocarNovaPeca(char coluna, int linha, XadrezPeca peca){
        tabuleiro.colocarPeca(peca,new XadrezPosicao(coluna, linha).posicionar());
        pecasNoTabuleiro.add(peca);
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
