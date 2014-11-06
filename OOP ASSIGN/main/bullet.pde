//class for bullets
//after a while user can user bullets

class bullet {

  PImage bullet; 
  PImage ammoPic;
  float bulletY = 0;
  float bulletX = 0;
  float ammoX = 0;
  float ammoY = 0;

  bullet() {
    bullet = loadImage("bullet.png");
    ammoPic = loadImage("ammo.png");
  }

  void bulletUpate(float posX, float posY) {
    bulletX = posX + 25;
    bulletY = posY;
  }

  //function to set new co-ordinates for ammo
  void updateAmmo() {
    ammoX = random(100, 1000);
    ammoY = 0;
  }
  void bulletDraw() {
    image(bullet, bulletX, bulletY);
  } 

  void bulletMove() {
    bulletY -= 10;
  }

  void drawAmmo() {
    image(ammoPic, ammoX, ammoY);
  }
  //function to move ammo and if go to end
  //of screen set boolean to false to stop checking if hit
  void moveAmmo(float speed) {
    ammoY += speed;
    if (ammoY > height) {
      ammo = false;
      ammoY = 0;
    }
  }
}
