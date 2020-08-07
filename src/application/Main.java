package application;


import xadrez.Partida;
import xadrez.XadrezExcepition;
import xadrez.XadrezPeca;
import xadrez.XadrezPosicao;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        Partida partida = new Partida();

        while(true) {
            try {
                UI.clearScreen();
                UI.printTabuleiro(partida.getPecas());
                System.out.println();
                System.out.print("Origem: ");
                XadrezPosicao origem = UI.lerXadrezPosicao(sc);

                System.out.println();
                System.out.print("Destino: ");
                XadrezPosicao destino = UI.lerXadrezPosicao(sc);

                XadrezPeca pecaCapturada = partida.executarMovimento(origem, destino);
            }
            catch (XadrezExcepition e){
                System.out.println(e.getMessage());
                sc.nextLine();
            }
            catch (InputMismatchException e){
                System.out.println(e.getMessage());
                sc.nextLine();
            }

        }
    }
}
