
//class for player
class player {

  float playerX;
  float playerY;
  PImage player;
  int i = 0;
  int seconds = 10;

  player(float playerX_, float playerY_) {

    playerX = playerX_;
    playerY = playerY_;
    player = loadImage("player.png");
 
  }

  void drawPlayer() {
    if(powerUP) {
      powerUPTime++;
      if((powerUPTime % 60) == 0) {
          seconds --;
          if(seconds == 0) {
            powerUP = false;
            seconds = 10;
          }
      }
      textSize(40);
      text(seconds, 500, 50);
      if(i >= 255) {
        i = 0;
      }
      fill(255,255,i);
      rect(playerX, playerY, 50, 50);
      i += 40;
    }
    else {
      image(player, playerX, playerY);
    }
  }

  void movePlayer(int directionLR, int directionUD) {
    //small checks to make sure player stays inside game
    if (playerX < 0) {
      playerX = 0;
    } else if (playerX > width) {
      playerX = (width - 50);
    } else if (playerY > (height - 50)) {
      playerY = (height - 50);
    } else if (playerY < 0) {
      playerY = 0;
    }


    playerX += directionLR;
    playerY += directionUD;
  }
}

