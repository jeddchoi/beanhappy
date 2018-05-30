package edu.skku.se3.beanhappy;
//type 1: 예약전
//type 2: 예약중
//type 3: 이용중
//type 4: 자리비움
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

    public void setBid(int Btype){
        this.Btype = Btype;
    }
    public int getBid(){
        return this.Btype;
    }
    public int getBcount(){
        return Bcount;
    }

    Beanbag A1 = new Beanbag("A1");
    Beanbag A2 = new Beanbag("A2");
    Beanbag A3 = new Beanbag("A3");
    Beanbag A4 = new Beanbag("A4");
    Beanbag A5 = new Beanbag("A5");
    Beanbag A6 = new Beanbag("A6");
}

