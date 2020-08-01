package application;


import xadrez.Partida;

public class Main {

    public static void main(String[] args) {

        Partida partida = new Partida();
        UI.printTabuleiro(partida.getPecas());
    }
}
