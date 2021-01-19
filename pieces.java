public class pieces {
  private int xCor;
  private int yCor;
  private boolean king;
  private String color;

  public pieces() {
    this.xCor = 0;
    this.yCor = 0;
    this.king = false;
    this.color = "";
  }

  public pieces(int x, int y, boolean b, String c) {
    this.xCor = x;
    this.yCor = y;
    this.king = b;
    this.color = c;
  }

  public int getX() {
    return this.xCor;
  }

  public int getY() {
    return this.yCor;
  }

  public String getColor() {
    return this.color;
  }

  public boolean kingStatus() {
    return this.king;
  }

  public void setX(int x) {
    this.xCor = x;
  }

  public void setY(int y) {
    this.yCor = y;
  }

  public void setColor(String c) {
    this.color = c;
  }

  public void makeKing() {
    this.king = true;
  }

  public String toString() {
    if (this.color == "white") {
      if (this.king)
        return "W";
      else
        return "w";
    } else {
      if (this.king)
        return "B";
      else
        return "b";
    }
  }
}