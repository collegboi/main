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

  void backgroundSplashScreen() {
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
  void drawSplashScreen() {

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


