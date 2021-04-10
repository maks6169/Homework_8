package geekbrains.lesson_8v2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

/**
 * Project java_core_1_0321
 *
 * @Author Alexander Grigorev
 * Created 03.04.2021
 * v1.0
 */
public class GameMap extends JPanel {
    public static final int MODE_VS_AI = 0;
    public static final int MODE_VS_HUMAN = 1;
    private static final Random RANDOM = new Random();
    private static final int DOT_EMPTY = 0;
    private static final int DOT_HUMAN = 1;
    private static final int DOT_AI = 2;
    private static final int DOT_PADDING = 7;
    private static final int STATE_DRAW = 0;
    private static final int STATE_WIN_HUMAN = 1;
    private static final int STATE_WIN_AI = 2;

    private int stateGameOver;

    private int[][] field;
    private int fieldSizeX;
    private int fieldSizeY;
    private int winLength;
    private int cellWidth;
    private int cellHeight;
    private boolean isGameOver;
    private boolean initialized;
    private int gameMode;
    private int playerNoTurn;
//    private String playerOneName = "";
//
//    private static int scoreHuman = 0;
//    private static int scoreAI = 0;

    public GameMap() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                update(e);
            }
        });
        initialized = false;
    }

    private void update(MouseEvent e) {
        if (isGameOver || !initialized) return;
        int dot = gameMode == 0 ? DOT_HUMAN : playerNoTurn == 1 ? DOT_HUMAN : DOT_AI;

        if (!playerTurn(e, dot)) return;
        if (gameCheck(dot, STATE_WIN_HUMAN)) return;
        if (gameMode == 0) {
            aiTurn();
            repaint();
            if (gameCheck(DOT_AI, STATE_WIN_AI)) return;
        }
    }

    private boolean playerTurn(MouseEvent e, int dot) {
        int cellX = e.getX() / cellWidth;
        int cellY = e.getY() / cellWidth;
        if (!isCellValid(cellX, cellY) || !isCellEmpty(cellX, cellY)) return false;
        field[cellY][cellX] = dot;
        repaint();
        playerNoTurn = playerNoTurn == 1 ? 2 : 1;
        return true;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        render(g);
    }

    private void render(Graphics g) {
        if (!initialized) return;
        int width = getWidth();
        int height = getHeight();
        cellWidth = width / fieldSizeX;
        cellHeight = height / fieldSizeY;
        g.setColor(Color.BLACK);

        for (int i = 0; i < fieldSizeY; i++) {
            int y = i * cellHeight;
            g.drawLine(0, y, width, y);
        }
        for (int i = 0; i < fieldSizeX; i++) {
            int x = i * cellWidth;
            g.drawLine(x, 0, x, height);
        }

        for (int y = 0; y < fieldSizeY; y++) {
            for (int x = 0; x < fieldSizeX; x++) {
                if (isCellEmpty(x, y)) continue;
                if (field[y][x] == DOT_HUMAN) {
                    g.setColor(new Color(1, 1, 255));
                    g.fillOval(x * cellWidth + DOT_PADDING,
                            y * cellHeight + DOT_PADDING,
                            cellWidth - DOT_PADDING * 2,
                            cellHeight - DOT_PADDING * 2);
                } else if (field[y][x] == DOT_AI) {
                    g.setColor(Color.RED);
                    g.fillRect(x * cellWidth + DOT_PADDING,
                            y * cellHeight + DOT_PADDING,
                            cellWidth - DOT_PADDING * 2,
                            cellHeight - DOT_PADDING * 2);
                } else {
                    throw new RuntimeException("Something wrong with coordinates");
                }
            }
        }

        if (isGameOver) {
            showMessageGameOver(g);
        }
    }

    public void startNewGame(int gameMode, int fieldSize, int winLength) {
        this.gameMode = gameMode;
        this.fieldSizeX = fieldSize;
        this.fieldSizeY = fieldSize;
        this.winLength = winLength;
        this.playerNoTurn = 1;
        field = new int[fieldSizeY][fieldSizeX];
        initialized = true;
        isGameOver = false;
        repaint();
        System.out.printf("New game with: %dx%d sized field, mode: %d and win length %d", fieldSize, fieldSize, gameMode, winLength);
    }

    private void showMessageGameOver(Graphics g) {
        g.setColor(Color.DARK_GRAY);
        g.fillRect(0, 250, getWidth(), 100);
        g.setColor(Color.YELLOW);
        g.setFont(new Font("TimesNewRoman", Font.BOLD, 60));
        switch (stateGameOver) {
            case STATE_DRAW:
                g.drawString("DRAW", 200, getHeight() / 2 + 35);
                break;
            case STATE_WIN_HUMAN:
                g.drawString("HUMAN Wins!", 100, getHeight() / 2 + 35);
                break;
            case STATE_WIN_AI:
                g.drawString("AI Wins!", 200, getHeight() / 2 + 35);
                break;
        }
    }

    private boolean gameCheck(int dot, int stateGameOver) {
        if (checkWin(dot, winLength)) {
            this.stateGameOver = stateGameOver;
            isGameOver = true;
            repaint();
//            if (dot == DOT_HUMAN) {
//                scoreHuman++;
//            } else {
//                scoreAI++;
//            }
//            System.out.println(s);
            return true;
        }
        if (checkDraw()) {
            this.stateGameOver = STATE_DRAW;
            isGameOver = true;
            repaint();
            return true;
        }
        return false;
    }

    private void aiTurn() {
        if (scanField(DOT_AI, winLength)) return;
        if (scanField(DOT_HUMAN, winLength)) return;
        if (scanField(DOT_AI, winLength - 1)) return;
        if (scanField(DOT_HUMAN, winLength - 1)) return;
        if (scanField(DOT_AI, winLength - 2)) return;
        if (scanField(DOT_HUMAN, winLength - 2)) return;

        int x, y;
        do {
            x = RANDOM.nextInt(fieldSizeX);
            y = RANDOM.nextInt(fieldSizeY);
        } while (!isCellEmpty(x, y));

        field[y][x] = DOT_AI;
    }

    private boolean scanField(int dot, int len) {
        for (int y = 0; y < fieldSizeY; y++) {
            for (int x = 0; x < fieldSizeX; x++) {
                if (isCellEmpty(x, y)) { //Проверим, что смотрим на пустую ячейку
                    field[y][x] = dot;
                    if (checkWin(dot, len)) {
                        if (dot == DOT_AI) return true;
                        if (dot == DOT_HUMAN) {
                            field[y][x] = DOT_AI;
                            return true;
                        }
                    }
                    field[y][x] = DOT_EMPTY;
                }
            }
        }
        return false;
    }

    private boolean checkWin(int c, int len) {
        for (int y = 0; y < fieldSizeY; y++) {
            for (int x = 0; x < fieldSizeX; x++) {
                if (checkLine(x, y, 1, 0, len, c)) return true; //Проверка горизонталь +х
                if (checkLine(x, y, 1, 1, len, c)) return true; //проверка диагонали +х +у
                if (checkLine(x, y, 0, 1, len, c)) return true; //проверка вертикали +у
                if (checkLine(x, y, 1, -1, len, c)) return true; //проверка диагонали +х -у
            }
        }
        return false;
    }

    private boolean checkLine(int x, int y, int incrementX, int incrementY, int len, int dot) {
        int endXLine = x + (len - 1) * incrementX; //Конец линии по x
        int endYLine = y + (len - 1) * incrementY; //Конец линии по y
        if (!isCellValid(endXLine, endYLine)) return false;
        for (int i = 0; i < len; i++) {
            if (field[y + i * incrementY][x + i * incrementX] != dot) return false;
        }
        return true;
    }

    private boolean checkDraw() {
        for (int y = 0; y < fieldSizeY; y++) {
            for (int x = 0; x < fieldSizeX; x++) {
                if (isCellEmpty(x, y)) return false;
            }
        }
        return true;
    }

    private boolean isCellValid(int x, int y) {
        return x >= 0 && y >= 0 && x < fieldSizeX && y < fieldSizeY;
    }

    private boolean isCellEmpty(int x, int y) {
        return field[y][x] == DOT_EMPTY;
    }
}
