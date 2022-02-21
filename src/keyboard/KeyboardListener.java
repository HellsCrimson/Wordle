package keyboard;


import wordle.TLController;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyboardListener extends KeyAdapter {
    private final TLController controller;

    public void keyPressed(KeyEvent e) {
        // 10 = enter; 8 = delete
        if (e.getKeyCode() == 10 || e.getKeyCode() == 8 || (e.getKeyCode() >= 65 && e.getKeyCode() <= 90)) {
            controller.keyKeyboardPressed(e.getKeyCode());
        }
    }

    public KeyboardListener(TLController controller) { this.controller = controller; }
}
