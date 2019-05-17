package ru.dsstaroverov.lift;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class LiftApplication implements CommandLineRunner {
    private List<Passenger> passengers;
    private List<Passenger> queue;

    private long liftSpeed=10000;

    private long stopTime=2000;

    private boolean power;

    private final Object isMove = new Object();

    private final Object isStop = new Object();

    private Lift lift;

    private LiftMine mine;


    public static void main(String[] args) {
        SpringApplication.run(LiftApplication.class, args);
    }

    @Override
    public void run(String... args) {

        mine = new LiftMine(7);
        lift = new Lift(0,0);
        lift.draw(mine);
        mine.print();
        power = true;
        passengers = new ArrayList<>();
        queue = new ArrayList<>();


        //Test passengers
        System.out.println("||| Add to queue 5 test passengers |||");
        System.out.println(queue.add(new Passenger(0,1)));
        System.out.println(queue.add(new Passenger(4,6)));
        System.out.println(queue.add(new Passenger(4,0)));
        System.out.println(queue.add(new Passenger(1,3)));
        System.out.println(queue.add(new Passenger(6,0)));


        //For input new passenger need enter 2 int "X Y", where X-enter floor and Y-leave floor.
        Thread console = new Thread(() -> {

            try(BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
                while (power) {
                    try {
                        String input = br.readLine();
                        if(input.equals("power")){
                            power=false;
                            return;
                        }
                        String[] floors = input.split(" ");
                        int enter = Integer.parseInt(floors[0]);
                        int leave = Integer.parseInt(floors[1]);
                        if(enter>=0&&enter<mine.getFloorsNum()&&
                            leave>=0&&leave<mine.getFloorsNum()) {
                            queue.add(new Passenger(enter, leave));
                        }else {
                            System.out.println("floor range 0 to "+mine.getFloorsNum());
                        }
                    }catch (Exception e){
                        System.out.println("data error");
                    }
                }
            }catch (Exception e){
                System.out.println("Console close");
            }
        });



        //Lift logic thread
        Thread main = new Thread(()->{
            while (power){

                synchronized (isMove) {
                    if(checkQueue()||checkLiftPassengers()) {
                        System.out.println("pass " + passengers.size());
                        System.out.println("que " + queue.size());
                        System.out.println("floor " + lift.getPosition());
                        mine.print();

                        synchronized (isStop) {
                            boolean needEnter = false;
                            boolean needLeave = false;
                            if (checkLiftPassengers()) {
                                needLeave = checkLeavePassenger(lift.getPosition());
                            }

                            if (checkQueue()) {
                                needEnter = checkEnterPassenger(lift.getPosition());
                                if (!checkLiftPassengers()) {
                                    moveToQueue();
                                }
                            }

                            if (needEnter || needLeave) {
                                try {
                                    isStop.wait(stopTime);
                                    System.out.println("lift stop");
                                    if(needLeave){System.out.println("pass leave");}
                                    if(needEnter){System.out.println("pass enter");}
                                    isStop.wait(stopTime);

                                } catch (InterruptedException e) {
                                    System.out.println("isStop error");
                                }
                            }


                            if (lift.getDirection() != 0) {
                                move();
                            }

                            System.out.println();
                        }
                    }
                }
            }
        });
        console.start();
        main.start();
    }

    private void move(){
        try {
            isMove.wait(liftSpeed);
        } catch (InterruptedException e) {
            System.out.println("move interrupted");
        }
        mine.clear();
        if(checkPosition()) {
            lift.move();
        }
        lift.draw(mine);
    }

    private void moveToQueue(){
        if(checkQueue()&&!checkLiftPassengers()&&lift.getDirection()==0){
            if(queue.get(0).getEnterFloor()>=lift.getPosition()){
                lift.setDirection(1);
            }else if(queue.get(0).getEnterFloor()<=lift.getPosition()) {
                lift.setDirection(-1);
            }
        }
    }

    private boolean checkLeavePassenger(int floor){
        List<Passenger> leavers= new ArrayList<>();
        if(checkLiftPassengers()) {
            for (Passenger passenger : passengers) {
                if (passenger.getLeaveFloor() == floor) {
                    leavers.add(passenger);
                }
            }
            passengers.removeAll(leavers);

        }
        if(passengers.size()==0&&queue.size()==0){
            lift.setDirection(0);
        }
        return leavers.size()>0;
    }

    private boolean checkEnterPassenger(int floor){
        List<Passenger> enters= new ArrayList<>();
        if(checkQueue()) {
            for (Passenger passenger : queue) {
                if (passenger.getEnterFloor() == floor
                        &&(passenger.getDirection()==lift.getDirection()||lift.getDirection()==0||!checkLiftPassengers()))
                {
                    passengers.add(passenger);
                    enters.add(passenger);
                    if(checkLiftPassengers()){
                        lift.setDirection(passenger.getDirection());
                    }
                }
            }
            queue.removeAll(passengers);
        }
        return enters.size()>0;
    }

    private boolean checkQueue(){
        return queue.size()>0;
    }

    private boolean checkLiftPassengers(){
        return passengers.size()>0;
    }

    private boolean checkPosition(){
        if((lift.getPosition()+lift.getDirection()<mine.getFloorsNum())&&(lift.getPosition()+lift.getDirection()>-1)){
            return true;
        }else {
            lift.setDirection(0);
            return false;
        }
    }
}
