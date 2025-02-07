package com.example.twobirdwithonestone.Activity;

import android.os.Parcel;
import android.os.Parcelable;

public class ParcelableItems implements Parcelable {
    public byte[] image;
    public String category;
    public String name;
    public String price;
    public String explanation;
    public String brand;



    public ParcelableItems(byte[] image, String category, String name, String price, String explanation,String brand) {

        this.image = image;
        this.category = category;
        this.name = name;
        this.price = price;
        this.explanation = explanation;
        this.brand = brand;
    }

    public ParcelableItems(Parcel in) {
        readFromParcel(in);

    }

    public void readFromParcel(Parcel in){
        // readByteArray가 받는 인자를 readInt형으로 바꾸어 줌
        image = new byte[in.readInt()];
        in.readByteArray(image);
        category = in.readString();
        name = in.readString();
        price = in.readString();
        explanation = in.readString();
        brand = in.readString();
    }

    public static final Creator<ParcelableItems> CREATOR = new Creator<ParcelableItems>() {
        @Override
        public ParcelableItems createFromParcel(Parcel in) {
            return new ParcelableItems(in);

        }

        @Override
        public ParcelableItems[] newArray(int size) {
            return new ParcelableItems[size];
        }
    };
    public String getcategory() {
        return category;
    }
    public String getname() {
        return name;
    }
    public String getexplanation() {
        return explanation;
    }
    public String getprice() {
        return price;
    }
    public String getbrand() {
        return brand;
    }
    public byte[] getBytes() {
        return image;
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // read 형식과 동일하게 int형 써주고 bytearray, readfromparcel 와 같이 순서 동일하게 적어주어야함
        dest.writeInt(image.length);
        dest.writeByteArray(image);
        dest.writeString(category);
        dest.writeString(name);
        dest.writeString(price);
        dest.writeString(explanation);
        dest.writeString(brand);
    }


    };
