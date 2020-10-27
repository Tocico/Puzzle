import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
    private int blankPosition;
    private JButton[][] tile = new JButton[DIMENSION][DIMENSION];
    private JButton resetBtn = new JButton("RESET");
    private JPanel panel = new JPanel();
    private JLabel msg;
    private Border line = new LineBorder(Color.WHITE);
    private Border margin = new EmptyBorder(10, 15, 10, 15);
    private Border compound = new CompoundBorder(line, margin);



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

    private void printTiles() {
        for (int i = 0; i < tilesList.size(); i++) {
            int positionY = i / DIMENSION;
            int positionX = i % DIMENSION;


            tile[positionY][positionX] = new JButton(String.valueOf(tilesList.get(i)));

            tile[positionY][positionX].setBackground(new Color(0x194B0E));
            tile[positionY][positionX].setForeground(new Color(0xEBEFEB));
            tile[positionY][positionX].setFont(new Font("SansSerif", Font.BOLD, 60));
            tile[positionY][positionX].setBorder(compound);
            tile[positionY][positionX].addMouseListener(new MouseAdapterEvent());
            tile[positionY][positionX].setOpaque(true);
            panel.add(tile[positionY][positionX]);


            if (tilesList.get(i) == 0) {
                blankPosition = i;
                tile[positionY][positionX].setVisible(false);
            }
        }


        panel.setLayout(new GridLayout(DIMENSION, DIMENSION));

        add(panel, BorderLayout.CENTER);
        add(resetBtn, BorderLayout.SOUTH);

        resetBtn.setFont(new Font("SansSerif", Font.BOLD, 20));
        resetBtn.setForeground(new Color(0xFFFFFF));
        resetBtn.setBackground(new Color(0x9A3B4A));
        resetBtn.addMouseListener(new MouseAdapterEvent());
        resetBtn.setOpaque(true);
        resetBtn.setBorder(compound);


        pack();
        setTitle("15 Puzzle");
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private class MouseAdapterEvent extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            String pressedBtn = ((JButton) e.getSource()).getText();

            if (pressedBtn.equals("RESET")) {
                reStartGame();
            } else {
                if (!getXYPosition(pressedBtn)) {
                    msg = new JLabel(
                            "<html><h1>Det gick något fel</h1></html>", SwingConstants.CENTER);
                    msg.setFont(new Font("SansSerif", Font.BOLD, 40));
                    msg.setForeground(Color.red);
                    add(msg, BorderLayout.NORTH);
                } else {
                    if (isFinishedGame()) {
                        msg = new JLabel(
                                "<html><h1>GRATTIS!!</h1></html>", SwingConstants.CENTER);
                        msg.setFont(new Font("SansSerif", Font.BOLD, 40));
                        msg.setForeground(new Color(0x2779AD));
                        add(msg, BorderLayout.NORTH);
                    }
                }
            }
        }
    }

    private boolean getXYPosition(String pressedBtn) {
        for (int y = 0; y < tile.length; y++) {
            for (int x = 0; x < tile[y].length; x++) {
                if (tile[y][x].getText().equals(pressedBtn)) {
                    return moveTiles(y, x);
                }
            }
        }
        return false;
    }

    public boolean moveTiles(int y, int x) {
        int blankPositionY = blankPosition / DIMENSION;
        int blankPositionX = blankPosition % DIMENSION;
        //Hur långt ifrån blanka position
        int xDiff = blankPositionX - x;
        int yDiff = blankPositionY - y;


        if (x == blankPositionX || y == blankPositionY) {

            if (xDiff < 0 && y == blankPositionY) {
                for (int i = 0; i < Math.abs(xDiff); i++) {
                    tile[blankPositionY][blankPositionX + i].setText(
                            tile[blankPositionY][blankPositionX + (i + 1)].getText()
                    );
                }
            } else if (xDiff > 0 && y == blankPositionY) {
                for (int i = 0; i < Math.abs(xDiff); i++) {
                    tile[blankPositionY][blankPositionX - i].setText(
                            tile[blankPositionY][blankPositionX - (i + 1)].getText()
                    );
                }
            } else if (yDiff < 0 && x == blankPositionX) {
                for (int i = 0; i < Math.abs(yDiff); i++) {
                    tile[blankPositionY + i][blankPositionX].setText(
                            tile[blankPositionY + (i + 1)][blankPositionX].getText()
                    );
                }
            } else if (yDiff > 0 && x == blankPositionX) {
                for (int i = 0; i < Math.abs(yDiff); i++) {
                    tile[blankPositionY - i][blankPositionX].setText(
                            tile[blankPositionY - (i + 1)][blankPositionX].getText()
                    );
                }
            }

            tile[blankPositionY][blankPositionX].setVisible(true);
            tile[y][x].setText(Integer.toString(0));
            tile[y][x].setVisible(false);
            blankPosition = y * DIMENSION + x;
        }
        return true;
    }



    public static void main(String[] args) {

        Puzzle puzzle = new Puzzle();
    }
}
