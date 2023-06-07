package module3;

import processing.core.PApplet;
import processing.core.PImage;

public class week3 extends PApplet {


    private PImage pic;
    public void setup(){
        size(600, 600);
        pic = loadImage("palmTrees.jpg");

    }


    public void draw(){
        pic.resize(0, height);
        image(pic, 0, 0);

//         sunny yellow 255, 209, 0
//        if(hour() >= 6 && hour() < 12){
//            fill(255, 255, 255);
//        }else if(hour() > 12 && hour() <= 15){
//            fill(255, 228, 132);
//        }else if(hour() > 15 && hour() <= 18){
//            fill(255, 204, 51);
//        }else if(hour() > 18 && hour() <= 21){
//            fill(252, 150, 1);
//        }else if(hour() == 22){
//            fill(209, 64, 9);
//        }else if(hour() >= 22 || hour() < 6){
//            fill(9, 48, 46);
//        }

        float ratio = Math.abs(30-second());
        fill(255 * ratio/30f,  255 * ratio/30f, 0);
        stroke(255 * ratio/30f,  255 * ratio/30f, 0);

        ellipse(width/4, height/6, width/6, height/6);
    }

    public static void main(String[] args) {
        PApplet.main(new String[] {"module3.week3"});
    }
}
