package ru.dsstaroverov.lift;

public class Passenger {
    private int enterFloor;
    private int leaveFloor;
    private int direction = 0;

    public Passenger() {
    }

    public Passenger(int enter, int leave) {
        this.enterFloor = enter;
        this.leaveFloor = leave;
        if(leave-enter>0){
            direction = 1;
        }else {
            direction = -1;
        }
    }

    public int getEnterFloor() {
        return enterFloor;
    }

    public void setEnterFloor(int enterFloor) {
        this.enterFloor = enterFloor;
    }

    public int getLeaveFloor() {
        return leaveFloor;
    }

    public void setLeaveFloor(int leaveFloor) {
        this.leaveFloor = leaveFloor;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }
}
