package edu.skku.se3.beanhappy;
//type 0: 예약전
//type 1: 이용중
//type 2: 예약중
//type 3: 자리비움
public class Beanbag {
    private String Bid; // 빈백 자리 번호
    private int Btype; // 빈백 상태
    public static int Bcount = 27; // 이용가능 빈백 수

    public Beanbag(String Bid){
        this.Bid = Bid;
        this.Btype = 1;
    }

    public Beanbag(){

    }

    public void setBtype(int Btype){
        this.Btype = Btype;
    }
    public int getBtype(){
        return this.Btype;
    }
    public int getBcount(){
        return Bcount;
    }
    public String getBid() { return Bid; }

    public Beanbag A1 = new Beanbag("A1");
}

