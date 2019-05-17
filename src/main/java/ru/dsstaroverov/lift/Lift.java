package ru.dsstaroverov.lift;


public class Lift {
    private int position;
    private int direction;


    public Lift(int position, int direction) {
        this.position = position;
        this.direction = direction;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public void draw(LiftMine mine){
        mine.setPosition(position,'X');
    }
    public void move(){
        if(direction>0){
            position++;
        }else if(direction<0){
            position--;
        }
    }
}
