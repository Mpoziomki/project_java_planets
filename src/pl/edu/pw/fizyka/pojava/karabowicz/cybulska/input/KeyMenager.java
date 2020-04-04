package pl.edu.pw.fizyka.pojava.karabowicz.cybulska.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.SwingUtilities;

//Cybulska
public class KeyMenager implements KeyListener
{
    private boolean[] keys;
    public boolean up, down, left, right;
    public boolean holding, released;

    public KeyManager()
    {
        keys = new boolean[256];
    }

    public void keyTyped(KeyEvent e)
    {

    }
    public void tick()
    {
        up = keys[KeyEvent.VK_W];
        down = keys[KeyEvent.VK_S];
        left = keys[KeyEvent.VK_A];
        right = keys[KeyEvent.VK_D];
    }
    public void keyPressed(KeyEvent e)
    {
        keys[e.getKeyCode()]=true;
    }

    public void keyReleased(KeyEvent e)
    {
        keys[e.getKeyCode()]=false;
    }
}
