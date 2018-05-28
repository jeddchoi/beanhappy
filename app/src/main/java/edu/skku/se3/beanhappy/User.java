package edu.skku.se3.beanhappy;
//type 1: 예약전
//type 2: 예약중
//type 3: 이용중
//type 4: 자리비움
public class User {
    String name;
    private static int type, seatNum;

    public User(String name) {
        this.name = name;
        this.type = 1;
    }

    public void setType(int type){
        this.type = type;
    }

    public int getType(){
        return this.type;
    }

    public void setSeatNum(int seatNum){
        this.seatNum = seatNum;
    }
    public int getSeatNum(){
        return this.seatNum;
    }
}
