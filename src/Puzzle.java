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
    private ArrayList<Integer> tilesList = new ArrayList<>(TOTALTILES);
    private int blankPosition;
    private JButton[][] tile = new JButton[DIMENSION][DIMENSION];
    private JButton resetBtn = new JButton("RESET");
    private JButton exitBtn = new JButton("EXIT");
    private JPanel panel = new JPanel();
    private JPanel footerP = new JPanel();
    private JLabel msg = new JLabel("",SwingConstants.CENTER);
    private Border line = new LineBorder(Color.WHITE);
    private Border margin = new EmptyBorder(10, 15, 10, 15);
    private Border compound = new CompoundBorder(line, margin);


    public Puzzle() {
        startGame();
        printTiles();

        //En specialinställning för att avsluta spelet snabbt.
        //cheatInitialTiles();
    }

    private void startGame() {
        //Generera och blanda siffror tills skapar lösbar pussel
        do {
            initialTiles();
            shuffleTiles();
        } while (!isSolvablePuzzle());
    }

    //Skapa 1-15
    private void initialTiles() {
        tilesList.clear(); //Ta bort den gamla listan
        for (int i = 0; i < TOTALTILES - 1; i++) {
            tilesList.add(i, i + 1);
        }
    }

    //Blanda siffror
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

    /**
     * Tilldelar slumptal till varje bricka
     */
    private void printTiles() {
        for (int i = 0; i < tilesList.size(); i++) {
            int positionY = i / DIMENSION;
            int positionX = i % DIMENSION;

            tile[positionY][positionX] = new JButton(String.valueOf(tilesList.get(i)));

            //Styling för varje bricka
            tile[positionY][positionX].setBackground(new Color(0x194B0E));
            tile[positionY][positionX].setForeground(new Color(0xEBEFEB));
            tile[positionY][positionX].setFont(new Font("SansSerif", Font.BOLD, 60));
            tile[positionY][positionX].setBorder(compound);
            tile[positionY][positionX].addMouseListener(mouseAdapterEvent);
            tile[positionY][positionX].setOpaque(true);
            panel.add(tile[positionY][positionX]);

            //Sätta 0 osynligt
            if (tilesList.get(i) == 0) {
                blankPosition = i;
                tile[positionY][positionX].setVisible(false);
            }
        }


        /** ---------------GUI------------------ **/

        //Layout
        panel.setLayout(new GridLayout(DIMENSION, DIMENSION));
        add(panel, BorderLayout.CENTER);
        footerP.add(resetBtn);
        footerP.add(exitBtn);
        add(footerP, BorderLayout.SOUTH);

        //Styling av avslut knappen
        exitBtn.setFont(new Font("SansSerif", Font.BOLD, 20));
        exitBtn.setForeground(new Color(0xFFFFFF));
        exitBtn.setBackground(new Color(0x7D6E1E));
        exitBtn.addMouseListener(mouseAdapterEvent);
        exitBtn.setOpaque(true);
        exitBtn.setBorder(compound);

        //Styling av reset knappen
        resetBtn.setFont(new Font("SansSerif", Font.BOLD, 20));
        resetBtn.setForeground(new Color(0xFFFFFF));
        resetBtn.setBackground(new Color(0x9A3B4A));
        resetBtn.addMouseListener(mouseAdapterEvent);
        resetBtn.setOpaque(true);
        resetBtn.setBorder(compound);


        pack();
        setTitle("15 Puzzle");
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    /**
     * Adapterklass
     */
    MouseAdapter mouseAdapterEvent = new MouseAdapter() {
        @Override
        public void mousePressed(MouseEvent e) {
            //Hämta värdet som användare tryckte på
            String pressedBtn = ((JButton) e.getSource()).getText();

            if (pressedBtn.equals("RESET")) {
                if (!msg.getText().equals("")) msg.setVisible(false);  // Ta bort den gamla meddelande
                reStartGame();
            } else if(pressedBtn.equals("EXIT")) {
                System.exit(0);
            } else {
                if (!msg.getText().equals("")) msg.setVisible(false); // Ta bort den gamla meddelande
                //Om användare klickar på fel stället då visas felmeddelande.
                if (!getXYPosition(pressedBtn)) {
                    msg.setText("Det gick något fel");
                    msg.setFont(new Font("SansSerif", Font.BOLD, 40));
                    msg.setForeground(Color.red);
                    add(msg, BorderLayout.NORTH);
                    msg.setVisible(true);
                } else {
                    if (!msg.getText().equals("")) msg.setVisible(false); // Ta bort den gamla meddelande
                    //Om spelet är klar då visas Grattis medelande
                    if (isFinishedGame()) {
                        msg.setText("GRATTIS!!");
                        msg.setFont(new Font("SansSerif", Font.BOLD, 40));
                        msg.setForeground(new Color(0x2779AD));
                        add(msg, BorderLayout.NORTH);
                        msg.setVisible(true);
                    }
                }
            }
        }
    };

    /**
     * Hämta positionen där användare tryckt på
     * Om man hittar positionen då anropar moveTiles().
     *
     * @param pressedBtn String värdet som tryckt på
     */
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

    //Funktion att flytta brickor
    public boolean moveTiles(int y, int x) {
        //Hämta nuvarande blank position
        int blankPositionY = blankPosition / DIMENSION;
        int blankPositionX = blankPosition % DIMENSION;
        //Hur långt ifrån blank position
        int xDiff = blankPositionX - x;
        int yDiff = blankPositionY - y;

        //Kontrollera om den klickade positionen ligger i samma kolumn eller rad som den blanka positionen
        if (x == blankPositionX || y == blankPositionY) {

            //Flytta brickor åt vänster
            if (xDiff < 0 && y == blankPositionY) {
                for (int i = 0; i < Math.abs(xDiff); i++) {
                    tile[blankPositionY][blankPositionX + i].setText(
                            tile[blankPositionY][blankPositionX + (i + 1)].getText()
                    );
                }
                //Flytta brickor åt höger
            } else if (xDiff > 0 && y == blankPositionY) {
                for (int i = 0; i < Math.abs(xDiff); i++) {
                    tile[blankPositionY][blankPositionX - i].setText(
                            tile[blankPositionY][blankPositionX - (i + 1)].getText()
                    );
                }
                //Flytta blickor uppåt
            } else if (yDiff < 0 && x == blankPositionX) {
                for (int i = 0; i < Math.abs(yDiff); i++) {
                    tile[blankPositionY + i][blankPositionX].setText(
                            tile[blankPositionY + (i + 1)][blankPositionX].getText()
                    );
                }
                //Flytta blickor nedåt
            } else if (yDiff > 0 && x == blankPositionX) {
                for (int i = 0; i < Math.abs(yDiff); i++) {
                    tile[blankPositionY - i][blankPositionX].setText(
                            tile[blankPositionY - (i + 1)][blankPositionX].getText()
                    );
                }
            }

            tile[blankPositionY][blankPositionX].setVisible(true);
            //Sätt 0 värdet där användare klickat
            tile[y][x].setText(Integer.toString(0));
            tile[y][x].setVisible(false);
            blankPosition = y * DIMENSION + x;
        }
        return true;
    }

    private boolean isFinishedGame() {
        //Kolla om 0 ligger på längst ner till höger
        if (!tile[DIMENSION - 1][DIMENSION - 1].getText().equals(String.valueOf(0))) {
            return false;
        }
        //Kolla om det finns 1 - 15 i brickor.
        for (int i = 0; i < TOTALTILES - 1; i++) {
            if (!tile[i / DIMENSION][i % DIMENSION].getText().equals(String.valueOf(i + 1))) {
                return false;
            }
        }
        return true;
    }

    private void reStartGame() {
        startGame();

        for (int i = 0; i < tilesList.size(); i++) {
            int positionY = i / DIMENSION;
            int positionX = i % DIMENSION;

            //Sätt ett nytt värde i brickan
            tile[positionY][positionX].setText(String.valueOf(tilesList.get(i)));
            tile[positionY][positionX].setVisible(true);

            if (tilesList.get(i) == 0) {
                blankPosition = i;
                tile[positionY][positionX].setVisible(false);
            }
        }
    }

    //En specialinställning för att avsluta spelet snabbt.
    private void cheatInitialTiles() {
        tilesList.clear();
        for (int i = 0; i < TOTALTILES; i++) {
            if (i == TOTALTILES - 2) tilesList.add(i, 0);
            else if (i == TOTALTILES - 1) tilesList.add(i, 15);
            else tilesList.add(i, i + 1);
        }
        printTiles();
    }


    public static void main(String[] args) {
        Puzzle puzzle = new Puzzle();
    }
}
