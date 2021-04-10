package geekbrains.lesson_8v2;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Project java_core_1_0321
 *
 * @Author Alexander Grigorev
 * Created 03.04.2021
 * v1.0
 */
public class SettingsWindow extends JFrame {
    private static final int WINDOW_WIDTH = 350;
    private static final int WINDOW_HEIGHT = 300;
    private static final int MIN_WIN_LENGTH = 3;
    private static final int MIN_FIELD_SIZE = 3;
    private static final int MAX_FIELD_SIZE = 10;
    private static final String FIELD_SIZE_PREF = "Field size: ";
    private static final String WIN_LENGTH_PREF = "Win length: ";

    private GameWindow gameWindow;
    private JSlider sliderWinLength;
    private JSlider sliderFieldSize;
    private JRadioButton humanVsAI;
    private JRadioButton humanVsHuman;

    SettingsWindow(GameWindow gameWindow) {
        this.gameWindow = gameWindow;
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setLocationRelativeTo(gameWindow);
        setResizable(false);
        setTitle("Create new Game");
        setLayout(new GridLayout(10, 1));

        addGameMode();
        addFieldSize();
        JButton buttonStartGame = new JButton("Start new game");
        buttonStartGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveSettings();
            }
        });
        add(buttonStartGame);
    }

    private void saveSettings() {
        int gameMode;
        if (humanVsAI.isSelected()) {
            gameMode = GameMap.MODE_VS_AI;
        } else if (humanVsHuman.isSelected()) {
            gameMode = GameMap.MODE_VS_HUMAN;
        } else {
            throw new RuntimeException("INVALID GAME MODE!");
        }
        int fieldSize = sliderFieldSize.getValue();
        int winLength = sliderWinLength.getValue();

        gameWindow.startGame(gameMode, fieldSize, winLength);

        setVisible(false);
    }

    private void addFieldSize() {
        JLabel labelFieldSize = new JLabel(FIELD_SIZE_PREF + MIN_FIELD_SIZE);
        JLabel labelWinLength = new JLabel(WIN_LENGTH_PREF + MIN_WIN_LENGTH);

        sliderFieldSize = new JSlider(MIN_FIELD_SIZE, MAX_FIELD_SIZE, MIN_FIELD_SIZE);
        sliderWinLength = new JSlider(MIN_WIN_LENGTH, MAX_FIELD_SIZE, MIN_FIELD_SIZE);

        sliderFieldSize.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int currentValue = sliderFieldSize.getValue();
                labelFieldSize.setText(FIELD_SIZE_PREF + currentValue);
                sliderWinLength.setMaximum(currentValue);
            }
        });

        sliderWinLength.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                labelWinLength.setText(WIN_LENGTH_PREF + sliderWinLength.getValue());
            }
        });

        add(new JLabel("Choose field size:"));
        add(labelFieldSize);
        add(sliderFieldSize);
        add(new JLabel("Choose win length:"));
        add(labelWinLength);
        add(sliderWinLength);
    }

    private void addGameMode() {
        add(new JLabel("Choose game mode:"));
        humanVsAI = new JRadioButton("Human versus AI", true);
        humanVsHuman = new JRadioButton("Human versus human");
        ButtonGroup gameMode = new ButtonGroup();
        gameMode.add(humanVsAI);
        gameMode.add(humanVsHuman);
        add(humanVsAI);
        add(humanVsHuman);
    }
}
