package jogo;

public class Tabuleiro {

    private int linhas;
    private int colunas;
    private Peca[][] pecas;

    public Tabuleiro(int linhas, int colunas) {
        if (linhas < 1  || colunas < 1 ){
            throw  new TabuleiroExcepition("Erro ao criar o Tabuleiro: É necessário pelo menos uma linha e uma coluna!");
        }
        this.linhas = linhas;
        this.colunas = colunas;
        pecas = new Peca[linhas][colunas];
    }

    public int getLinhas() {
        return linhas;
    }

    public int getColunas() {
        return colunas;
    }

    public Peca peca(int linha, int coluna){
        if(!posicaoExiste(linha, coluna)){
            throw new TabuleiroExcepition("Posição Inválida no tabuleiro!");
        }
        return pecas[linha][coluna];
    }

    public Peca peca(Posicao posicao){
        if(!posicaoExiste(posicao)){
            throw new TabuleiroExcepition("Posição Inválida no tabuleiro!");
        }
        return pecas[posicao.getLinha()][posicao.getColuna()];
    }

    public void colocarPeca(Peca peca, Posicao posicao){
        if (pecaExiste(posicao)){
            throw new TabuleiroExcepition("Já existe uma peça nessa posição! " + posicao);
        }
        pecas[posicao.getLinha()][posicao.getColuna()] = peca;
        peca.posicao = posicao;
    }

    private boolean posicaoExiste(int linha, int coluna){
        return linha >= 0 && linha < linhas && coluna >= 0 && coluna < colunas;
    }

    public boolean posicaoExiste(Posicao posicao){
        return posicaoExiste(posicao.getLinha(), posicao.getColuna());
    }

    public boolean pecaExiste(Posicao posicao){
        if(!posicaoExiste(posicao)){
            throw new TabuleiroExcepition("Posição Inválida no tabuleiro!");
        }
        return peca(posicao) != null;
    }
}
