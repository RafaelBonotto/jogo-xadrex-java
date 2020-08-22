package xadrez;

import jogo.Peca;
import jogo.Posicao;
import jogo.Tabuleiro;
import xadrez.pecas.*;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Partida {

    private int turno;
    private Cor jogadorAtual;
    private Tabuleiro tabuleiro;
    private boolean check;
    private boolean checkMate;
    private XadrezPeca vuneravelEnPassant;
    private XadrezPeca promovido;

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

    public boolean getCheckMate(){ return checkMate; }

    public XadrezPeca getVuneravelEnPassant() {
        return vuneravelEnPassant;
    }

    public XadrezPeca getPromovido() { return promovido; }

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

        XadrezPeca pecaMovida = (XadrezPeca)tabuleiro.peca(destino);

        // #Movimento Especial Promoção
        promovido = null;
        if(pecaMovida instanceof Peao){
            if((pecaMovida.getCor() == Cor.BRANCO && destino.getLinha() == 0) || (pecaMovida.getCor() == Cor.PRETO && destino.getLinha() == 7)){
                promovido = (XadrezPeca)tabuleiro.peca(destino);
                promovido = substituirPecaPromovida ("D");
            }
        }

        check = (testarCheck(oponente(jogadorAtual))) ? true : false;

        if(testarCheckMate(oponente(jogadorAtual))){
            checkMate = true;
        }
        else {
            proximoTurno();
        }

        // # Movimento Especial En Passant
        if(pecaMovida instanceof Peao && (destino.getLinha() == origem.getLinha() - 2 || destino.getLinha() == origem.getLinha() + 2)){
            vuneravelEnPassant = pecaMovida;
        }
        else{
            vuneravelEnPassant = null;
        }

        return (XadrezPeca)pecaCapturada;
    }

    public XadrezPeca substituirPecaPromovida(String tipo){
        if(promovido == null){
            throw new IllegalStateException("Não há peça para ser promovida!");
        }
        if(!tipo.equals("B") && !tipo.equals("C") && !tipo.equals("T") &&!tipo.equals("D")){
            throw new InvalidParameterException("Tipo Inválido para Promoção!");
        }
        Posicao pos = promovido.getXadrezPosicao().posicionar();
        Peca p = tabuleiro.removerPeca(pos);
        pecasNoTabuleiro.remove(p);

        XadrezPeca novaPeca = novaPeca(tipo, promovido.getCor());
        tabuleiro.colocarPeca(novaPeca, pos);
        pecasNoTabuleiro.add(novaPeca);

        return novaPeca;
    }

    private XadrezPeca novaPeca(String tipo, Cor cor){
        if(tipo.equals("B")) return new Bispo(tabuleiro, cor);
        if(tipo.equals("C")) return new Cavalo(tabuleiro, cor);
        if(tipo.equals("T")) return new Torre(tabuleiro, cor);
        return new Rainha(tabuleiro, cor);
    }


    private Peca movimento (Posicao origem, Posicao destino){
        XadrezPeca p = (XadrezPeca)tabuleiro.removerPeca(origem);
        p.aumentarContagemDeMovimentos();
        Peca pecaCapturada = tabuleiro.removerPeca(destino);
        tabuleiro.colocarPeca(p, destino);

        if(pecaCapturada != null){
            pecasNoTabuleiro.remove(pecaCapturada);
            pecasCapturadas.add(pecaCapturada);
        }
        // #Movimento Especial (Roque Pequeno)
        if(p instanceof Rei && destino.getColuna() == origem.getColuna() +  2){
            Posicao origemTorre = new Posicao(origem.getLinha(), origem.getColuna() + 3);
            Posicao destinoTorre = new Posicao(origem.getLinha(), origem.getColuna() + 1);
            XadrezPeca torre = (XadrezPeca)tabuleiro.removerPeca(origemTorre);
            tabuleiro.colocarPeca(torre, destinoTorre);
            torre.aumentarContagemDeMovimentos();
        }
        // #Movimento Especial (Roque Grande)
        if(p instanceof Rei && destino.getColuna() == origem.getColuna() -  2){
            Posicao origemTorre = new Posicao(origem.getLinha(), origem.getColuna() - 4);
            Posicao destinoTorre = new Posicao(origem.getLinha(), origem.getColuna() - 1);
            XadrezPeca torre = (XadrezPeca)tabuleiro.removerPeca(origemTorre);
            tabuleiro.colocarPeca(torre, destinoTorre);
            torre.aumentarContagemDeMovimentos();
        }
        // #Movimetno Especial En Passant
        if(p instanceof Peao){
            if(origem.getColuna() != destino.getColuna() && pecaCapturada == null){
                Posicao peaoPosicao;
                if(p.getCor() == Cor.BRANCO){
                    peaoPosicao = new Posicao(destino.getLinha() + 1, destino.getColuna());
                }
                else{
                    peaoPosicao = new Posicao(destino.getLinha() - 1, destino.getColuna());
                }
                pecaCapturada = tabuleiro.removerPeca(peaoPosicao);
                pecasCapturadas.add(pecaCapturada);
                pecasNoTabuleiro.remove(pecaCapturada);
            }
        }

        return pecaCapturada;
    }

    private void desfazerMovimento(Posicao origem, Posicao destino, Peca pecaCapturada){
        XadrezPeca p = (XadrezPeca)tabuleiro.removerPeca(destino);
        p.diminuirContagemDeMovimentos();
        tabuleiro.colocarPeca(p, origem);

        if(pecaCapturada != null){
            tabuleiro.colocarPeca(pecaCapturada,destino);
            pecasCapturadas.remove(pecaCapturada);
            pecasNoTabuleiro.add(pecaCapturada);
        }

        // #Movimento Especial (Roque Pequeno)
        if(p instanceof Rei && destino.getColuna() == origem.getColuna() +  2){
            Posicao origemTorre = new Posicao(origem.getLinha(), origem.getColuna() + 3);
            Posicao destinoTorre = new Posicao(origem.getLinha(), origem.getColuna() + 1);
            XadrezPeca torre = (XadrezPeca)tabuleiro.removerPeca(destinoTorre);
            tabuleiro.colocarPeca(torre, origemTorre);
            torre.diminuirContagemDeMovimentos();
        }
        // #Movimento Especial (Roque Grande)
        if(p instanceof Rei && destino.getColuna() == origem.getColuna() -  2){
            Posicao origemTorre = new Posicao(origem.getLinha(), origem.getColuna() - 4);
            Posicao destinoTorre = new Posicao(origem.getLinha(), origem.getColuna() - 1);
            XadrezPeca torre = (XadrezPeca)tabuleiro.removerPeca(destinoTorre);
            tabuleiro.colocarPeca(torre, origemTorre);
            torre.diminuirContagemDeMovimentos();
        }
        if(p instanceof Peao){
            if(origem.getColuna() != destino.getColuna() && pecaCapturada == vuneravelEnPassant){
                XadrezPeca peao = (XadrezPeca)tabuleiro.removerPeca(destino);
                Posicao peaoPosicao;
                if(p.getCor() == Cor.BRANCO){
                    peaoPosicao = new Posicao(3, destino.getColuna());
                }
                else{
                    peaoPosicao = new Posicao(4, destino.getColuna());
                }
                tabuleiro.colocarPeca(peao, peaoPosicao);
            }
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

    private boolean testarCheckMate(Cor cor){
        if(!testarCheck(cor)){
            return false;
        }
        List<Peca> lista = pecasNoTabuleiro.stream().filter(x -> ((XadrezPeca)x).getCor() == cor).collect(Collectors.toList());
        for(Peca p : lista){
            boolean [][] mat = p.movimentosPossiveis();
            for(int i = 0; i < tabuleiro.getLinhas(); i++){
                for(int j = 0; j < tabuleiro.getColunas(); j++){
                    if(mat[i][j]) {
                        Posicao origem = ((XadrezPeca)p).getXadrezPosicao().posicionar();
                        Posicao destino = new Posicao(i, j);
                        Peca pecaCapturada = movimento(origem, destino);
                        boolean testarCheck = testarCheck(cor);
                        desfazerMovimento(origem,destino,pecaCapturada);
                        if(!testarCheck){
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }


        private void colocarNovaPeca(char coluna, int linha, XadrezPeca peca){
        tabuleiro.colocarPeca(peca,new XadrezPosicao(coluna, linha).posicionar());
        pecasNoTabuleiro.add(peca);
    }

    private void configInicial(){
        colocarNovaPeca('a', 1, new Torre(tabuleiro, Cor.BRANCO));
        colocarNovaPeca('b', 1, new Cavalo(tabuleiro, Cor.BRANCO));
        colocarNovaPeca('c', 1, new Bispo(tabuleiro, Cor.BRANCO));
        colocarNovaPeca('d', 1, new Rainha(tabuleiro, Cor.BRANCO));
        colocarNovaPeca('e', 1, new Rei(tabuleiro, Cor.BRANCO, this));
        colocarNovaPeca('f', 1, new Bispo(tabuleiro, Cor.BRANCO));
        colocarNovaPeca('g', 1, new Cavalo(tabuleiro, Cor.BRANCO));
        colocarNovaPeca('h', 1, new Torre(tabuleiro, Cor.BRANCO));
        colocarNovaPeca('a', 2, new Peao(tabuleiro, Cor.BRANCO, this));
        colocarNovaPeca('b', 2, new Peao(tabuleiro, Cor.BRANCO, this));
        colocarNovaPeca('c', 2, new Peao(tabuleiro, Cor.BRANCO, this));
        colocarNovaPeca('d', 2, new Peao(tabuleiro, Cor.BRANCO, this));
        colocarNovaPeca('e', 2, new Peao(tabuleiro, Cor.BRANCO, this));
        colocarNovaPeca('f', 2, new Peao(tabuleiro, Cor.BRANCO, this));
        colocarNovaPeca('g', 2, new Peao(tabuleiro, Cor.BRANCO, this));
        colocarNovaPeca('h', 2, new Peao(tabuleiro, Cor.BRANCO, this));



        colocarNovaPeca('a', 8, new Torre(tabuleiro, Cor.PRETO));
        colocarNovaPeca('b', 8, new Cavalo(tabuleiro, Cor.PRETO));
        colocarNovaPeca('c', 8, new Bispo(tabuleiro, Cor.PRETO));
        colocarNovaPeca('d', 8, new Rainha(tabuleiro, Cor.PRETO));
        colocarNovaPeca('e', 8, new Rei(tabuleiro, Cor.PRETO, this));
        colocarNovaPeca('f', 8, new Bispo(tabuleiro, Cor.PRETO));
        colocarNovaPeca('g', 8, new Cavalo(tabuleiro, Cor.PRETO));
        colocarNovaPeca('h', 8, new Torre(tabuleiro, Cor.PRETO));
        colocarNovaPeca('a', 7, new Peao(tabuleiro, Cor.PRETO, this));
        colocarNovaPeca('b', 7, new Peao(tabuleiro, Cor.PRETO, this));
        colocarNovaPeca('c', 7, new Peao(tabuleiro, Cor.PRETO, this));
        colocarNovaPeca('d', 7, new Peao(tabuleiro, Cor.PRETO, this));
        colocarNovaPeca('e', 7, new Peao(tabuleiro, Cor.PRETO, this));
        colocarNovaPeca('f', 7, new Peao(tabuleiro, Cor.PRETO, this));
        colocarNovaPeca('g', 7, new Peao(tabuleiro, Cor.PRETO, this));
        colocarNovaPeca('h', 7, new Peao(tabuleiro, Cor.PRETO, this));

    }
}
