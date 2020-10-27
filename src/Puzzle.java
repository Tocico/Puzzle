import javax.swing.*;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Toshiko Kuno
 * Date: 2020-10-27
 * Time: 13:11
 * Project: IntelliJ IDEA
 * Copyright: MIT
 */


public class Puzzle extends JFrame {
    private final int DIMENSION = 4;
    private final int TOTALTILES = DIMENSION * DIMENSION;
    ArrayList<Integer> tilesList = new ArrayList<>(TOTALTILES);



    public Puzzle() {
        startGame();

    }
    private void startGame() {
        do {
            tilesList.clear();
            initialTiles();
            shuffleTiles();
        } while (!isSolvablePuzzle());
    }

    //Skapa 1-15
    private void initialTiles() {
        for (int i = 0; i < TOTALTILES - 1; i++) {
            tilesList.add(i, i + 1);
        }
    }

    private void shuffleTiles() {
        Collections.shuffle(tilesList);
        //Lägg 0 i slutet av arraylist
        tilesList.add(tilesList.size(), 0);
    }

    private boolean isSolvablePuzzle() {//Matematiskt logik
        int countInversions = 0;// 0 är altid index 15 det betyder kottaste väg till 0 =jämn
        for (int i = 0; i < tilesList.size() - 1; i++) { //tileList.size()=16 måste vara 15(1-15)
            for (int j = 0; j < i; j++) {
                if (tilesList.get(j) > tilesList.get(i)) {
                    countInversions++;
                }
            }
        }// antalet av inversioner måste vara jämnt för att bli lösbar puzzel eftersom kottaste väg till 0(jämn)
        return countInversions % 2 == 0;
    }


    public static void main(String[] args) {

        Puzzle puzzle = new Puzzle();
    }
}
