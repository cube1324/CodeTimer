package com.example.codetimer.Support;

import android.os.Parcelable;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

public class ListElement implements Serializable {
    private String name;
    private ItemType type;
    private long number = 0;
    private long save_number = 0;
    private ListElement relatedElement = null;
    private int depth = 1;
    private int relatedIndex;

    public ListElement(String name, ItemType type, int number, ListElement relatedElement, int depth, int relatedIndex, int save_number){
        this.name = name;
        this.type = type;
        this.number = number;
        this.relatedElement = relatedElement;
        this.depth = depth;
        this.relatedIndex = relatedIndex;
        this.save_number = save_number;
    }

    public ListElement(String name, ItemType type){
        this.name = name;
        this.type = type;
    }

    public String getNumber(){
        String r;
        switch (this.getType()){
            case TIMER:
                int min = (int) number / 1000 / 60;
                int sec = (int) number / 1000 % 60;
                r =  String.format("%02d : %02d", min, sec);
                break;
            case LOOPSTART:
                r = Long.toString(number);
                break;
            case LOOPEND:
                r = "LOOPEND";
                break;
            default:
                r = super.toString();
        }
        return r;
    }

    public void setSave_number(long save_number) {
        this.save_number = save_number;
    }

    public long getSave_number() {
        return save_number;
    }

    public int getRelatedIndex() {
        return relatedIndex;
    }

    public void setRelatedIndex(int relatedIndex) {
        this.relatedIndex = relatedIndex;
    }

    public int getDepth() {
        return depth;
    }

    public void incDepthBy(int depth){
        this.depth += depth;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNumber(long number) {
        this.number = number;
    }

    public void incNumberBy(long x){
        if (this.number + x < 0){
            this.number = 0;
        }else {
            this.number += x;
        }
    }

    public void setrelatedElement(ListElement endElement) {
        this.relatedElement = endElement;
    }

    public String getName() {
        return name;
    }

    public ItemType getType() {
        return type;
    }

    public ListElement getrelatedElement() {
        return relatedElement;
    }

    public long getRepetition(){
        return this.number;
    }
}
