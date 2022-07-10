package cn.bminor7.nfc_demo;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class User implements Parcelable{
    //id 姓名  车牌号 卡id 入场时间 余额
    private int       id;
    private String 	  name;
    private String    carNum;
    private String    uid;
    private String    inTime;
    private int       money;

    public User(){
        super();
    }

    public User(int id,String name,String carNum,String uid,String inTime,int money){
        super();
        this.id=id;
        this.name=name;
        this.carNum=carNum;
        this.uid=uid;
        this.inTime=inTime;
        this.money=money;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCarNum() {
        return carNum;
    }

    public void setCarNum(String carNum) {
        this.carNum = carNum;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getInTime() {
        return inTime;
    }

    public void setInTime(String inTime) {
        this.inTime = inTime;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public String toString() {
        return "Player [id=" + id + ", name=" + name + ", carNum=" + carNum+",uid="+uid
                + ",inTime="+inTime+",money="+money+"]";
    }
    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }
    @Override
    public void writeToParcel(Parcel arg0, int arg1) {
        // TODO Auto-generated method stub
        arg0.writeInt(id);
        arg0.writeString(name);
        arg0.writeString(carNum);
        arg0.writeString(uid);
        arg0.writeString(inTime);
        arg0.writeInt(money);
    }

    public static final Parcelable.Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source.readInt(),source.readString(),source.readString(),source.readString(),source.readString(),source.readInt());
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}