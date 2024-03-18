public class TimeHandler {

  private double currentTime;
  private double initTime;


  public TimeHandler() {

    currentTime = System.currentTimeMillis() / 1000.0;;
    initTime = System.currentTimeMillis() / 1000.0;
  }

  public boolean timeBomb(double x) {

    currentTime = System.currentTimeMillis() / 1000.0 - initTime;
    if (currentTime > x) {
      initTime = System.currentTimeMillis() / 1000.0;
      return true;
    }
    return false;
  }





}
