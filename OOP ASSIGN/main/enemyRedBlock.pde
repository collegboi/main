class enemyRedBlocks { 
  PImage img;
  float enemyX;
  float enemyY;
  float speed = 5;

  enemyRedBlocks(float speed_, float ranX_, float ranY_) {
    enemyX = ranX_;
    enemyY = ranY_;
    speed = speed_;
    img = loadImage("box.png");
  }

  void display() {
    image(img, enemyX, enemyY);
  }

  void move() {
    //speed = speed_;
    enemyY += speed;
    if (enemyY >= height) {
      enemyY = -40;
    }
  }
}

