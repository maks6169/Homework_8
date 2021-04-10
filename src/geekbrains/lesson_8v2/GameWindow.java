package geekbrains.lesson_8v2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Project java_core_1_0321
 **
 * @Author Alexander Grigorev
 * Created 03.04.2021
 * v1.0
 */
public class GameWindow extends JFrame {

    private static final int WINDOW_WIDTH = 600;
    private static final int WINDOW_HEIGHT = 640;
    private static final int WINDOW_POS_X = 640;
    private static final int WINDOW_POS_Y = 230;

    private GameMap map;

    GameWindow() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setLocation(WINDOW_POS_X, WINDOW_POS_Y);
//        setLocationRelativeTo(null);
        setTitle("Tic Tac Toe");
        setResizable(false);

        JButton btnStart = new JButton("<html><body><b>Start</b></body></html>");
        JButton btnStop = new JButton("<html><body><b>EXIT</b></body></html>");

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 2));
        buttonPanel.add(btnStart);
        buttonPanel.add(btnStop);

        map = new GameMap();
        SettingsWindow settings = new SettingsWindow(this);


        btnStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                settings.setVisible(true);
            }
        });
        btnStop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        add(map, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        setVisible(true);
    }

    public void startGame(int gameMode, int fieldSize, int winLength) {
        map.startNewGame(gameMode, fieldSize, winLength);
    }
}
