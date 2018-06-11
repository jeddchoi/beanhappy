package edu.skku.se3.beanhappy;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.UUID;

//type 0: 비어있음
//type 1: 이용중
//type 2: 예약중
//type 3: 자리비움

@IgnoreExtraProperties
public class User {

    public String email;
    public int status;
    public String seatNum;
    public Long last_idle_time;
    public Long last_reserve_time;
    public Long last_start_time;
    public Long last_login_time;
    public boolean isExtended;
    public String uuid;


    public User(String email, String uuid, long time) {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    User(String email, String uuid, Long last_login_time) {
        this.email = email;
        this.status = 0;
        this.seatNum = null;
        this.last_idle_time = 0L;
        this.last_reserve_time = 0L;
        this.last_start_time = 0L;
        this.last_login_time = last_login_time;
        this.isExtended = false;
        this.uuid = uuid;
    }
}