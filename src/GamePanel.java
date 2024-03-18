import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.*;


public class GamePanel extends javax.swing.JPanel {


  private TimeHandler timeHandler;

  private boolean state;
  private boolean start;


  private int score;
  private int cycle;

  private int phase;

  private boolean isReleased;
  private Timer gameTimer;

  private boolean end;
  private String playerName;

  private Player player;
  private ArrayList<Enemy> enemies;
  private BufferedImage backgroundImage;

  private Font pixelMplus;


  public GamePanel() {

    phase = 1;
    isReleased = true;
    player = new Player("src/resources/eagle_animate/e", "src/resources/eagle_animate/spark.png", 15);
    enemies = new ArrayList<>();
    playerName = "";
    end = false;
    score = 0;
    cycle = 0;
    state = true;
    timeHandler = new TimeHandler();
    start = false;
    try {
      backgroundImage = ImageIO.read(new File("src/resources/bg.jpg"));
    } catch (IOException e1) {
      e1.printStackTrace();
    }

    try {
      pixelMplus = Font.createFont(Font.TRUETYPE_FONT, new File("src/resources/PixelMplus10-Regular.ttf")).deriveFont(40f);
    } catch (FontFormatException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }


    gameTimer = new Timer();
    gameTimer.schedule(new TimerTask() {
      @Override
      public void run() {


        player.move();
        if (cycle == 800) {
          cycle = 0;
        }
        cycle += 5;
        repaint();


      }
    }, 0, GameData.DELAY);

  }


  @Override
  public void paint(Graphics g) {

    super.paint(g);
    Graphics2D gtd = (Graphics2D) g;
    gtd.drawImage(backgroundImage, 0, cycle - 800, backgroundImage.getWidth(), backgroundImage.getHeight(), null);
    gtd.drawImage(backgroundImage, 0, cycle, backgroundImage.getWidth(), backgroundImage.getHeight(), null);
    gtd.setFont(pixelMplus);
    if (!player.isDead()) {
      if (start) {
        gtd.drawString(Integer.toString(score), 1200, 50);
        gtd.drawString(Integer.toString(player.getHealth()), 50, 50);

        player.drawImage(gtd);

        addEnemies();

        collisionCheck();

        updateEnemies(gtd);

        updateMissiles(gtd);

      } else {
        //gtd.setFont(pixelMplus);

        if (timeHandler.timeBomb(0.5)) {
          state = !state;

        }

        gtd.drawString("USE LEFT, RIGHT ARROW TO MOVE - SPACEBAR TO SHOOT", 160, 450);
        if (state) {
          gtd.drawString("PRESS ENTER TO START", 440, 350);
        }

        player.drawImage(gtd);
      }

    } else if (end) {
      gtd.drawString("HIGH SCORES", 500, 250);
      int limit = readScores().size() > 5 ? 5: readScores().size();
      for (int i = 0; i < limit; i++) {
        gtd.drawString(i+ 1 + " - " + readScores().get(i), 500, 300 + (i * 50));
      }


    } else {
      gtd.drawString("GAME OVER!", 450, 250);
      gtd.drawString("POINT:", 450, 350);
      gtd.drawString("ENTER NAME:", 450, 450);
      gtd.drawString(playerName, 450, 550);
      gtd.drawString(Integer.toString(score), 650, 350);
    }

  }

  public static ArrayList<String> sortScores(ArrayList<String> scores){
    boolean sorted;

    while (true){
      sorted = true;

      for(int i = 0; i < scores.size() - 1; i++){
        int nextIndex = Integer.parseInt(scores.get(i + 1).replaceAll("[^\\d]", ""));
        int currentIndex = Integer.parseInt(scores.get(i).replaceAll("[^\\d]", ""));
        if(currentIndex < nextIndex){
          sorted = false;
          Collections.swap(scores, i, i + 1);

        }
      }
      if (sorted){
        break;
      }
    }

    return scores;
  }

  public static ArrayList<String> readScores() {
    ArrayList<String>  scores = new ArrayList<>();
    Path path = Paths.get("src/resources/highscores.txt");
    Scanner in = null;
    try {
      in = new Scanner(path);
    } catch (IOException e) {
      e.printStackTrace();
    }
    while (in.hasNext()) {
      String str = in.nextLine();
      scores.add(str);
    }
    scores = sortScores(scores);

    return scores;
  }

  public void writeNewScore(String name, int score) throws IOException {
    Path path = Paths.get("src/resources/highscores.txt");
    Scanner in = new Scanner(path);
    String old = "";
    while (in.hasNext()) {
      String str = in.nextLine();
      if(str.contains(name)){

      }
      old += str + "\n";
    }
    name = name == "" ? "NONE": name;


    name = name.toUpperCase();
    name = name.replaceAll(" ", "");

    old += String.format("%s: %d", name, score);

    Files.write(path, old.getBytes());

  }

  public void keyTyped(KeyEvent e) {
    if (player.isDead() && !end) {
      if (e.getKeyChar() == (char) KeyEvent.VK_ENTER) {
        try {
          writeNewScore(playerName, score);
        } catch (IOException e1) {
          e1.printStackTrace();
        }
        end = true;
      }
      if (e.getKeyChar() == (char) KeyEvent.VK_BACK_SPACE) {
        try {
          playerName = playerName.substring(0, playerName.length() - 1);
        } catch (Exception e1) {
          System.out.println("Please enter a name!");
        }

      } else {
        playerName += e.getKeyChar();
      }
    }
  }

  public void keyPressed(KeyEvent e) {
    int keyInput = e.getKeyCode();
    int RIGHT = KeyEvent.VK_RIGHT;
    int LEFT = KeyEvent.VK_LEFT;
    int SHOOT = KeyEvent.VK_SPACE;
    int ENTER = KeyEvent.VK_ENTER;
    if (keyInput == ENTER) {
      start = true;
    }
    if (keyInput == RIGHT) {
      player.setGoRight(true);
    }
    if (keyInput == LEFT) {
      player.setGoLeft(true);
    }
    if (keyInput == SHOOT) {
      if (isReleased) {
        player.attack();
        isReleased = false;
      }

    }
  }

  public void keyReleased(KeyEvent e) {
    int keyInput = e.getKeyCode();
    int RIGHT = KeyEvent.VK_RIGHT;
    int LEFT = KeyEvent.VK_LEFT;
    int SHOOT = KeyEvent.VK_SPACE;
    int start = KeyEvent.VK_ENTER;

    if (keyInput == RIGHT) {
      player.setGoRight(false);
    }
    if (keyInput == LEFT) {
      player.setGoLeft(false);
    }
    if (keyInput == SHOOT) {
      isReleased = true;
    }
  }

  public void addEnemies() {
    int x;
    int random_sign;

      if (phase == 1 && enemies.size() < 1) {
        phase = 2;
        for (int k = 0; k < score / 100 + 10; k++) {

          SecureRandom random = new SecureRandom();
          x =  2000 + random.nextInt(1000);

          if (random.nextInt(2) == 1) {
            x *= -1;
          }
          createTier3Enemies(x, 50);
        }
      }
      if (phase == 2 && enemies.size() < 1) {
        phase = 1;
        for (int k = 0; k < score / 100 + 10; k++) {
          x = 2000;
          random_sign = (int) Math.round((Math.random()));
          if (random_sign == 1) {
            x *= -1;
          }
          createTier2Enemies(x, 50);
        }
        createBoss(-1000, 50);

      }




  }


  public void collisionCheck() {
    for (Enemy enemy : enemies) {
      for (Projectile projectile_enemy : enemy.getProjectiles()) {
        /* collision check for player */
        if (player.getHitbox().intersects(projectile_enemy.getHitbox()) && !projectile_enemy.isDead()) {
          projectile_enemy.setDead(true);
          player.setHealth(player.getHealth() - 1);

        }
      }

      for (Projectile projectile_player : player.getProjectiles()) {

        /* collision check for enemy */
        if (enemy.getHitbox().intersects(projectile_player.getHitbox()) && !projectile_player.isDead() && !enemy.isDead()) {
          enemy.setHealth(enemy.getHealth() - 1);
          if (enemy.getHealth() < 1) {
            score += 10;
            enemy.setDead(true);
          }
          projectile_player.setDead(true);
        }

        /* collision check for enemy projectile and player projectile */
        for (Projectile projectile_enemy : enemy.getProjectiles()) {
          if (projectile_player.getHitbox().intersects(projectile_enemy.getHitbox()) && !projectile_player.isDead()) {
            projectile_enemy.setDead(true);
            projectile_player.setDead(true);

          }


        }
      }

    }

  }

  public void updateEnemies(Graphics2D gtd) {
    for (int i = 0; i < enemies.size(); i++) {
      if (enemies.get(i).isDead()) {
        if (enemies.get(i).getProjectiles().size() == 0) {
          enemies.remove(i);
        }
      } else {
        enemies.get(i).drawImage(gtd);
        enemies.get(i).move();
      }
    }
  }

  public void updateMissiles(Graphics2D gtd) {
    for (int i = 0; i < player.getProjectiles().size(); i++) {

      if (!player.getProjectiles().get(i).isDead()) {
        player.getProjectiles().get(i).drawImage(gtd);
        player.getProjectiles().get(i).move();
      } else {
        player.getProjectiles().get(i).setImage("src/resources/explosion3.png");
        player.getProjectiles().get(i).drawImage(gtd);
        if (player.getProjectiles().get(i).getTimeHandler().timeBomb(0.5)) {
          player.getProjectiles().remove(i);
        }


      }

    }

    for (int j = 0; j < enemies.size(); j++) {
      for (int i = 0; i < enemies.get(j).getProjectiles().size(); i++) {
        if (!enemies.get(j).getProjectiles().get(i).isDead()) {
          enemies.get(j).getProjectiles().get(i).drawImage(gtd);
          enemies.get(j).getProjectiles().get(i).move_e();
        } else {
          enemies.get(j).getProjectiles().remove(i);
        }
      }
    }

  }

  public void createTier3Enemies(int x, int y) {
    enemies.add(new Enemy(x, y, "src/resources/enemy_tier3/crow", "src/resources/enemy_tier3/e_bullet.png", 7, 1.5, 1));
  }

  public void createTier2Enemies(int x, int y) {
    enemies.add(new Enemy(x, y, "src/resources/enemy_tier2/Dragon", "src/resources/enemy_tier2/fireball.png", 7, 0.5, 3));
  }

  public void createBoss(int x, int y) {
    enemies.add(new Enemy(x, y, "src/resources/enemy_boss/dragon_boss", "src/resources/enemy_boss/fireball.png", 8, 0.2, 50));

  }


}
