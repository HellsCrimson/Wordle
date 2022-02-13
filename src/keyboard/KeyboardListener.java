package src.keyboard;


import src.wordle.TLController;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyboardListener implements KeyListener {
    private TLController controller;

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // 10 = enter; 8 = delete
        if (e.getKeyCode() == 10 || e.getKeyCode() == 8 || (e.getKeyCode() >= 65 && e.getKeyCode() <= 90)) {
            controller.keyKeyboardPressed(e.getKeyCode());
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    public KeyboardListener(TLController controller) {
        this.controller = controller;
    }
}
