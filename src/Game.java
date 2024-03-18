import javax.swing.*;

public class Game {
  public static void main(String args[]) {

    JFrame frame = new JFrame();
    GamePanel panel = new GamePanel();

    panel.setSize(GameData.BOARD_WIDTH, GameData.BOARD_HEIGHT);
    panel.setLocation(400, 50);
    panel.setVisible(true);
    frame.setTitle("Fly You Fools");
    frame.addKeyListener(new KeyChecker(panel));

    frame.add(panel);
    frame.setSize(GameData.BOARD_WIDTH, GameData.BOARD_HEIGHT);
    frame.setLocation(400, 50);
    frame.setResizable(false);
    frame.setVisible(true);


    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


  }
}
