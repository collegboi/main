class Score {
  
  PImage score;
  float scoreX;
  float scoreY;
  float speed = 0;
  
  Score(float ranX_, float ranY_, float speed_) {
    scoreX = ranX_;
    scoreY = ranY_;
    speed = speed_;
    score = loadImage("boxScore.png");
  }
  
  void update(float speed) {
    this.speed = speed;
    //randomize a number for live block
    float rand = random(350);
    scoreX = rand;
    scoreY = 0;
  }
  
  void drawScore() {
    image(score, scoreX, scoreY);
  }
  
  void moveScore() {
    scoreY += speed;

    if (scoreY > height) {
      this.update(random(0,10));
      
    }
  }
  
  
  
}
