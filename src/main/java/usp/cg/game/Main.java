package usp.cg.game;

import usp.cg.engine.GameEngine;
import usp.cg.engine.IGameLogic;

/**
 * Classe que inicializa o jogo
 */
public class Main {
    public static void main(String[] args) {
        try {
            boolean vSync = true;
            IGameLogic gameLogic = new Game();
            GameEngine gameEng = new GameEngine("Pong - 0 x 0 - Pressione espaço para começar!", 900, 600, vSync, gameLogic);
            gameEng.start();
        } catch (Exception excp) {
            excp.printStackTrace();
            System.exit(-1);
        }
    }
}