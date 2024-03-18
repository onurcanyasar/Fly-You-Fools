import java.awt.event.*;

public class KeyChecker extends KeyAdapter {
  private GamePanel panel;
  public KeyChecker(GamePanel panel) {
    this.panel = panel;

  }

  @Override
  public void keyPressed(KeyEvent e) {
    panel.keyPressed(e);

  }

  @Override
  public void keyReleased(KeyEvent e) {
    panel.keyReleased(e);

  }

  @Override
  public void keyTyped(KeyEvent e) {
    panel.keyTyped(e);
  }
}
