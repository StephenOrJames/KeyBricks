package code.gui;

import code.model.Model;
import code.model.Tile;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

/**
 * Created by Stephen on 3/27/2016.
 */
public class GUI implements Runnable {

    private final Model _model;
    private final JFrame _window;
    private final JPanel _boardPanel;
    private final JPanel _savePanel;
    private final JPanel _scorePanel;
    // TODO: replace existing command-line restoration with a restore button?

    public GUI(Model model) {
        // Use the Nimbus Look and Feel if available
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception ignored) {
        }

        _model = model;
        _window = new JFrame("KeyBricks");

        _boardPanel = new JPanel(new GridBagLayout());
        _savePanel = new JPanel();
        _scorePanel = new JPanel();
        JPanel topPanel = new JPanel(new GridLayout());

        refreshBoardPanel();
        refreshSavePanel();
        refreshScorePanel();

        topPanel.add(_savePanel);
        topPanel.add(_scorePanel);
        _window.add(topPanel, BorderLayout.PAGE_START);
        _window.add(_boardPanel, BorderLayout.CENTER);

        _window.addKeyListener(new KeyPressHandler(this));
        _window.pack();
        _window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        _window.setExtendedState(JFrame.MAXIMIZED_BOTH);
        _window.setLocationRelativeTo(null);  // position in the center of the screen
        _window.setVisible(true);
    }

    /**
     * Refresh the window after the state of its components are changed.
     */
    private void refresh() {
        refreshBoardPanel();
        refreshScorePanel();

        _window.repaint();
        _window.revalidate();
    }

    /**
     * Populate or repopulate the board panel with the necessary tiles based on the model.
     */
    private void refreshBoardPanel() {
        _boardPanel.removeAll();  // ensure the panel is clear
        GridBagConstraints c = new GridBagConstraints();

        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.gridx = 0;
        c.gridy = Model.ROWS - 1;

        for (ArrayList<Tile> column: _model.getBoard()) {
            for (Tile t: column) {
                JButton b = new JButton(String.valueOf(t.getLetter()));
                b.setBackground(t.getColor());
                if (!t.getColor().equals(Color.GREEN) && !t.getColor().equals(Color.YELLOW)) {
                    b.setForeground(Color.WHITE);
                }
                b.setFocusable(false);
                b.setFont(b.getFont().deriveFont(50f));
                _boardPanel.add(b, c);
                c.gridy--;
            }
            c.gridx++;
            c.gridy = Model.ROWS - 1;
        }
    }

    /**
     * Add the necessary JComponents to the save panel.
     */
    private void refreshSavePanel() {
        JButton button = new JButton("Save");
        _savePanel.setFocusable(false);
        button.setFocusable(false);
        button.addActionListener(new SaveClickHandler(_model));
        _savePanel.add(button);
    }

    /**
     * Populate or repopulate the board panel with the necessary tiles based on the model.
     */
    private void refreshScorePanel() {
        _scorePanel.removeAll();
        JLabel scoreLabel = new JLabel("Score: " + _model.getScore());
        scoreLabel.setFont(scoreLabel.getFont().deriveFont(20f));
        _scorePanel.add(scoreLabel);
    }

    private Model getModel() {
        return _model;
    }

    @Override public void run() {}

    private class KeyPressHandler implements KeyListener {

        private GUI _gui;

        KeyPressHandler(GUI gui) {
            _gui = gui;
        }

        @Override
        public void keyTyped(KeyEvent e) {
            Character keyChar = e.getKeyChar();
            keyChar = Character.toUpperCase(keyChar);
            if (!keyChar.equals(' ')) {
                if (_gui.getModel().removeLetterGroup(keyChar)) {
                    _gui.refresh();
                }
            }
        }

        @Override
        public void keyPressed(KeyEvent e) {}

        @Override
        public void keyReleased(KeyEvent e) {}

    }

    private class SaveClickHandler implements ActionListener {

        private Model _model;

        public SaveClickHandler(Model model) {
            _model = model;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            _model.save();
        }

    }

}
