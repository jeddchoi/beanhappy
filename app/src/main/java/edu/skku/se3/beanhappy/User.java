package edu.skku.se3.beanhappy;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.UUID;

//type 0: IDLE
//type 1: 이용중
//type 2: 예약중
//type 3: 자리비움

@IgnoreExtraProperties
public class User {

    public String email;
    private static int status;
    private static int seatNum;
    protected volatile static UUID uuid;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String email, UUID uuid) {
        this.email = email;
        this.status = 0;
        this.uuid = uuid;
    }


    public void setType(int type){
        this.status = type;
    }

    public int getType(){
        return this.status;
    }

    public void setSeatNum(int seatNum){
        this.seatNum = seatNum;
    }
    public int getSeatNum(){
        return this.seatNum;
    }

}