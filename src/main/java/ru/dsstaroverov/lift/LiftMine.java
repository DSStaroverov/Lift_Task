package ru.dsstaroverov.lift;

public class LiftMine {
    private int floorsNum;
    private char [] floors;

    public LiftMine(int num) {
        floorsNum = num;
        this.floors = new char[num];
        clear();
    }

    public int getFloorsNum() {
        return floorsNum;
    }

    public void setFloorsNum(int floorsNum) {
        this.floorsNum = floorsNum;
    }

    public char[] getFloors() {
        return floors;
    }

    public void setFloors(char[] floors) {
        this.floors = floors;
    }

    public void setPosition(int position, char c){
        floors[position] = c;
    }

    public void print(){
        System.out.print("-----------");
        System.out.println();
        for(int i = 0; i<floorsNum; i++){
            System.out.print("|"+floors[i]);
        }
        System.out.print("|");
        System.out.println();
    }
    public void clear(){
        for(int i =0;i<floorsNum;i++){
            setPosition(i,' ');
        }
    }

}
