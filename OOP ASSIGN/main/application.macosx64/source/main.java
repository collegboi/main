import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import javax.swing.JFrame; 
import processing.video.*; 
import ddf.minim.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class main extends PApplet {

//Processing assignment
//By Timothy Barnard
//20/10/2014

PFrame newFrame;
secondApplet newApplet;


//video library

PImage videoFrame;
Capture video;

//Sound in the game

Minim minim;
AudioPlayer player;



//-------------------------------//
//setting variables for class
enemyRedBlocks[] enemy; 
player player1;
lives newLive;
bullet newBullet;
splashScreen newScreen;
Score[] newScore;


//global boolean variables
boolean check;
boolean lives;
boolean checkHeart;
boolean bulletCheck;
boolean startGame;
boolean checkAmmo;
boolean checkScore;
boolean ammo;
boolean powerUP;
int powerUPTime;
boolean picTake;


//setting global variables
int score = 0;
float speed = 0;
int count = 0;

//number of lives
int no_lives;
//no of bullets lefts
int no_bullets;


//image for game over
PImage gameOver;

//=============================================================================
//setup function which only happens once

public void setup() {
  //scren Size
  size(1020, 1020);

  //set number of lives = 5
  no_lives = 5;
  //set number of bullets to 3
  no_bullets = 1;

  enemy = new enemyRedBlocks[25];
  newScore = new Score[3];
  //for loop to set random places for enemy blocks

  for (int i = 0; i < enemy.length; i ++ ) { 
    float ranX = random(10, width - 50);
    float ranY = random(-200, 0);
    speed = random(1, 6);
    enemy[i] = new enemyRedBlocks(speed, ranX, ranY);
  } 
  for (int i = 0; i < newScore.length; i++) {
    float ranX = random(0, width - 50);
    float ranY = random(0, height - 600);
    newScore[i] = new Score(ranX, ranY, random(1, 5));
  }
  //i = 0;

  //declaring vaiables for classes
  player1 = new player(width/2, height - 300);
  newLive = new lives();
  newBullet = new bullet();
  newScreen = new splashScreen();

  //initiliasing boolean variables
  check = false;
  checkHeart = true;
  bulletCheck = false;
  startGame = false;
  checkScore = false;
  ammo = false;
  powerUP = false;
  score = 0;
  picTake = false;

  minim = new Minim (this);
  player = minim.loadFile ("gameover.mp3");
  gameOver = loadImage("GameOver.png");
}//end setup 


//=============================================================================
//draw function every 60 times a seconds
public void draw() {

  //on startup, into splash screen
  if (!startGame) {

    /*if (picTake) {
     } else { */
    newScreen.backgroundSplashScreen();
    newScreen.drawSplashScreen();
    // }
  }//end if game started
  //if users starts games
  else { 

    //
    if (videoFrame == null) {
      background(255);
    } else {
      image(videoFrame, 0, 0);
    }
    //game code
    String text = "Lives = " + no_lives;
    String text1 = "Bullets = " + no_bullets;
    String text2 = "Score = " + score;
    textSize(22);
    fill(0, 102, 153);
    text(text, 20, 20);
    text(text1, 20, 50);
    text(text2, 20, 80);

    //display blocks
    for (int i = 0; i < enemy.length; i ++ ) {
      enemy[i].move();
      enemy[i].display();
    }

    for (int i = 0; i < newScore.length; i ++) {
      newScore[i].drawScore();
      newScore[i].moveScore();
    }

    //------------------GAME OVER ---------------------//
    //if no lives left, game over
    if (no_lives == 0 ) {
      noLoop();
      player.play ();
      lives = false;
      background(0);
      image(gameOver, 250, 100);
      fill(255);
      rect(250, 550, 560, 80, 7);
      rect(250, 680, 560, 80, 7);
      textSize(32);
      fill(255);
      text("Score: " + score, 430, 400);
      fill(0);
      text("Try Again", 460, 600);
      text("Exit", 480, 740);
    } else {
      //draws player
      player1.drawPlayer();
    }

    //newBullet.bulletMove();
    if (bulletCheck) {
      newBullet.bulletDraw();
      newBullet.bulletMove();
    }

    //function to check if contact
    check = enemyHit();
    //check to check if we user hit enemy
    //and reduces lives
    if (check) {
      no_lives--;
      check = false;
    }

    //a counter so a live gets given every 100
    count++;
    if ((count % 1000) == 0) {
      if (newLive.heartY == 0) {
        lives = true;
        newLive.update();
      }
      //---Power up = true ----//
      if ((count % 100) == 0) {
        powerUP = true;
        speed += 1;
      }

      if (no_bullets == 0) {
        ammo = true;
        newBullet.updateAmmo();
      }
    }
    //set new ammo come down
    if (ammo) {
      checkAmmo = hitHeart();
      if (!checkAmmo) {
        newBullet.drawAmmo();
        newBullet.moveAmmo(speed);
      }
    }
    //-----------scores amount-----------------//
    checkScore = hitHeart();
    if (checkScore) {
      score += 2;
      checkScore = false;
    }

    if (lives) {
      //check if player hit hear
      checkHeart = hitHeart();
      if (!checkHeart) {
        newLive.liveDraw();
        newLive.liveMove(speed);
      } else {
        no_lives++;
        lives = false;
      }
    }  //end check if game started
  }//end if checki if game started
}//end draw function

//=============================================================================
//Function uses dis to check if contact between player and enemy is made
public boolean enemyHit() {

  float enemyL = 0;
  float enemyT = 0;
  float enemyR = 40;//width of enemy
  float enemyB = 40; // height of enemy

  float playerL = player1.playerX;
  float playerT = player1.playerY;
  float playerR = 40 + playerL; //width of player
  float playerB = 40 + playerT;

  float bulletL = newBullet.bulletX;
  float bulletT = newBullet.bulletY;
  float bulletR = 10 + bulletL;
  float bulletB = 40 + bulletT;


  boolean check = false;


  for (int i = 0; i < enemy.length; i++) {
    enemyL = enemy[i].enemyX;
    enemyT = enemy[i].enemyY;
    enemyR += enemyL;
    enemyB += enemyT;


    if (!(enemyL > playerR || enemyR < playerL || enemyT > playerB || enemyB  <  playerT)) {
      //power up enables invincibility of player
      if (powerUP) {
        enemy[i].enemyY = 0;
      } else {
        check = true;
        enemy[i].enemyY = 0;
      }
    }
    if (!(enemyL > bulletR || enemyR < bulletL || enemyT > bulletB || enemyB  <  bulletT)) {
      enemy[i].enemyY = 0;
      score += 1;
      bulletCheck = false;
    }
    enemyR = 40;
    enemyB = 40;
  }

  return check;
}
//=============================================================================
public boolean hitHeart() {

  //declaring variables for heart position
  float heartL = newLive.heartX;
  float heartT = newLive.heartY;
  float heartR = newLive.heartX + 25;
  float heartB = newLive.heartY + 25;

  //declaring vairables for player position
  float playerL = player1.playerX;
  float playerT = player1.playerY;
  float playerR = 40 + playerL; //width of player
  float playerB = 40 + playerT;

  float ammoL = newBullet.ammoX;
  float ammoT = newBullet.ammoY;
  float ammoR = newBullet.ammoX + 40;
  float ammoB = newBullet.ammoY + 40;

  boolean checkMate = false;


  for (int i = 0; i < newScore.length; i++) {
    float scoreL = newScore[i].scoreX;
    float scoreT = newScore[i].scoreY;
    float scoreR = newScore[i].scoreX + 30;
    float scoreB = newScore[i].scoreY + 30;

    if (!(scoreL > playerR || scoreR < playerL || scoreT > playerB || scoreB < playerT)) {
      checkMate = true;
      checkScore = false;
      newScore[i].scoreY = height + 20;
    }
  }


  //return! 
  if (!(heartL > playerR || heartR < playerL || heartT > playerB || heartB < playerT)) {
    checkMate = true;
    lives = false;
    newLive.heartY = 0;
  }

  if (!(ammoL > playerR || ammoR < playerL || ammoT > playerB || ammoB < playerT)) {
    checkMate = true;
    ammo = false;
    if (no_bullets == 0) {
      no_bullets = 1;
    }
  }

  return checkMate;
}
//=============================================================================
//mouse pressed function
public void mousePressed() {
  //checking for start up screen
  if (mouseX >= newScreen.startX && mouseX <= (newScreen.startX2 + newScreen.startX) && 
    mouseY >= newScreen.startY && mouseY <= (newScreen.startY2 + newScreen.startY)) {
    startGame = true;
  }
  //take picture for background
  if (mouseX >= newScreen.bckgdX && mouseX <= (newScreen.bckgdX2 + newScreen.bckgdX) && 
    mouseY >= newScreen.bckgdY && mouseY <= (newScreen.bckgdY2 + newScreen.bckgdY)) {
    //picTake= true;
    if (newFrame == null) {
      newFrame = new PFrame();
    }
  }
  //pick picture from computer
  if (mouseX >= newScreen.levelX && mouseX <= (newScreen.levelX2 + newScreen.levelX) && 
    mouseY >= newScreen.levelY && mouseY <= (newScreen.levelY2 + newScreen.levelY)) {
    selectFolder("Select a folder to process:", "folderSelected"); 
    
    
  }

  if (no_lives == 0) {
    if (mouseX >= 250 && mouseX <= (250 + 560) && mouseY >= 550 && mouseY <= (550 + 80)) {
      loop();
      no_lives = 5;
      no_bullets = 1;
      count = 0;
      for (int i = 0; i < enemy.length; i ++) {
        enemy[i].enemyY = random(0, (height-400));
        enemy[i].enemyX = random(0, width);
      }
    }
    if (mouseX >= 250 && mouseX <= (250 + 560) && mouseY >= 680 && mouseY <= (680 + 80)) {
      loop();
      startGame = false;
      no_lives = 5;
      no_bullets = 1;
      count = 0;
      for (int i = 0; i < enemy.length; i ++) {
        enemy[i].enemyY = random(0, (height-400));
        enemy[i].enemyX = random(0, width);
      }
    }
  }
} 

//=================folder selection method====================================//
public void folderSelected(File selection) {
        if (selection != null) {
            videoFrame = loadImage(selection.getAbsolutePath());
          videoFrame.resize(1020,1020);
        }
    }
//======================keyPressed method======================================
public void keyPressed() {
  if (key == 's') {

    if (no_bullets > 0) {
      bulletCheck = true;
      newBullet.bulletUpate(player1.playerX, player1.playerY);
      no_bullets--;
    }
  }

  if (key == CODED) {
    if (keyCode == LEFT)
    {
      player1.movePlayer(-20, 0);
    } else if (keyCode == RIGHT) {
      player1.movePlayer(20, 0);
    } else if (keyCode == UP) {
      player1.movePlayer(0, -20);
    } else if (keyCode == DOWN) {
      player1.movePlayer(0, 20);
    }
  }//end if key == coded
}

//========================secondApplet second screen==============================//

public class secondApplet extends PApplet {

  public void setup() {
    size(960, 640);
    // Using the default capture device
    video = new Capture(this, width, height, 15);
    // Create an empty image the same size as the video
    videoFrame = createImage(video.width, video.height, RGB);
    video.start();
  }
  public void draw() {
    // fill(255,0,0);
    background(0);
    // You don't need to display it to analyze it!
    image(video, 0, 0);
    // Capture video
    if (video.available()) {
      // Save previous frame for motion detection!!
      videoFrame.copy(video, 0, 0, video.width, video.height, 0, 0, video.width, video.height);
      videoFrame.updatePixels();
      video.read();
    }
    loadPixels();
    video.loadPixels();
    videoFrame.loadPixels();
  }

  public void mousePressed() {
    videoFrame.resize(1020, 1020);
    destroy();
    newFrame.setVisible(false);
  }
}


public class PFrame extends JFrame {

  public PFrame() {
    setBounds(0, 0, 940, 640);
    newApplet = new secondApplet();
    add(newApplet);
    newApplet.init();
    show();
  }
} 

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
  
  public void update(float speed) {
    this.speed = speed;
    //randomize a number for live block
    float rand = random(350);
    scoreX = rand;
    scoreY = 0;
  }
  
  public void drawScore() {
    image(score, scoreX, scoreY);
  }
  
  public void moveScore() {
    scoreY += speed;

    if (scoreY > height) {
      this.update(random(0,10));
      
    }
  }
  
  
  
}
class bckgdVideo {
  /*
  
   
   setup {
   video = new Capture(this, width, height, 30);
   // Create an empty image the same size as the video
   prevFrame = createImage(video.width,video.height,RGB);
   //start video background function
   
   }
   // Capture video background code
   if (video.available()) {
   // Save previous frame for motion detection!!
   prevFrame.copy(video,0,0,video.width,video.height,0,0,video.width,video.height); // Before we read the new frame, we always save the previous frame for comparison!
   prevFrame.updatePixels();
   video.read();
   }
   
   loadPixels();
   video.loadPixels();
   prevFrame.loadPixels();
   
   // Begin loop to walk through every pixel
   for (int x = 0; x < video.width; x ++ ) {
   for (int y = 0; y < video.height; y ++ ) {
   
   int loc = x + y*video.width;            // Step 1, what is the 1D pixel location
   color current = video.pixels[loc];      // Step 2, what is the current color
   color previous = prevFrame.pixels[loc]; // Step 3, what is the previous color
   
   // Step 4, compare colors (previous vs. current)
   float r1 = red(current); float g1 = green(current); float b1 = blue(current);
   float r2 = red(previous); float g2 = green(previous); float b2 = blue(previous);
   float diff = dist(r1,g1,b1,r2,g2,b2);
   
   // Step 5, How different are the colors?
   // If the color at that pixel has changed, then there is motion at that pixel.
   if (diff > threshold) { 
   // If motion, display black
   pixels[loc] = color(0);
   } else {
   // If not, display white
   pixels[loc] = color(255);
   }
   }
   }
   updatePixels(); */
}

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

  public void bulletUpate(float posX, float posY) {
    bulletX = posX + 25;
    bulletY = posY;
  }

  //function to set new co-ordinates for ammo
  public void updateAmmo() {
    ammoX = random(100, 1000);
    ammoY = 0;
  }
  public void bulletDraw() {
    image(bullet, bulletX, bulletY);
  } 

  public void bulletMove() {
    bulletY -= 10;
  }

  public void drawAmmo() {
    image(ammoPic, ammoX, ammoY);
  }
  //function to move ammo and if go to end
  //of screen set boolean to false to stop checking if hit
  public void moveAmmo(float speed) {
    ammoY += speed;
    if (ammoY > height) {
      ammo = false;
      ammoY = 0;
    }
  }
}
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

  public void display() {
    image(img, enemyX, enemyY);
  }

  public void move() {
    //speed = speed_;
    enemyY += speed;
    if (enemyY >= height) {
      enemyY = -40;
    }
  }
}

//class for lives

class lives {

  PImage live;
  float heartX = 0;
  float heartY = 0;

  lives() {
    live = loadImage("heart.png");
  }

  public void update() {
    //randomize a number for live block
    float rand = random(350);
    heartX = rand;
    heartY = 0;
  }

  public void liveDraw() {
    image(live, heartX, heartY);
    //fill(0,255,0);
    // rect(heartX, heartY, 40,40);
  }


  public void liveMove(float speed) {
    heartY += speed;

    if (heartY > height) {
      lives = false;
      println(lives);
      heartY = 0;
    }
  }
}


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

  public void drawPlayer() {
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

  public void movePlayer(int directionLR, int directionUD) {
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

//class for main screen of game 
class splashScreen {

  int startX = ((1020/2) - 250);
  int startY = 200;
  int startX2 = 500;
  int startY2 = 75;

  int bckgdX = startX;
  int bckgdY = 400;
  int bckgdX2 = startX2;
  int bckgdY2 = startY2;

  int levelX =startX;
  int levelY = 600;
  int levelX2 = startX2;
  int levelY2 = startY2;

  public void backgroundSplashScreen() {
    smooth();
    float r = 0;
    float g = 0;
    float b = 255;
    //set background color of splash screen 
    for (int j = 0; j < 1020; j++) {
      if (j % 2 == 0)
      {
        if (j > 1020/2) {
          r--;
          g--;
          stroke(r, g, b);
          line(0, j, 1020, j);
        } else {
          stroke(r, g, b);
          line(0, j, 1020, j);
          r++; 
          g++;
        }
      }//if modular 2
      else {
        stroke(r, g, b);
        line(0, j, 1020, j);
      }//else if modular
    }//end for loop for drawing line
  }//draw splash screen upon startup of game

  //function to draw buttons
  public void drawSplashScreen() {

    //start game
    fill(222, 156, 250);
    noStroke();
    rect(startX, startY, startX2, startY2, 7);
    //click for background type
    rect(bckgdX, bckgdY, bckgdX2, bckgdY2, 7);
    //click to change level
    rect(levelX, levelY, levelX2, levelY2, 7);

    fill(255);
    textSize(32);
    text("Start Game", (startX + 160), (startY + 50));
    text("Click to take Pic", (bckgdX + 110), (bckgdY + 50));
    text("Open Image ", (levelX + 160), (levelY + 50));
  }//end function to draw
}  //end of class  


  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "main" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
