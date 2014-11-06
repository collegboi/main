//class for lives

class lives {

  PImage live;
  float heartX = 0;
  float heartY = 0;

  lives() {
    live = loadImage("heart.png");
  }

  void update() {
    //randomize a number for live block
    float rand = random(350);
    heartX = rand;
    heartY = 0;
  }

  void liveDraw() {
    image(live, heartX, heartY);
    //fill(0,255,0);
    // rect(heartX, heartY, 40,40);
  }


  void liveMove(float speed) {
    heartY += speed;

    if (heartY > height) {
      lives = false;
      println(lives);
      heartY = 0;
    }
  }
}

