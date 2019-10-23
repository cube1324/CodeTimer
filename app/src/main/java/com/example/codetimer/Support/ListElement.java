package com.example.codetimer.Support;

import java.util.concurrent.TimeUnit;

public class ListElement {
    private String name;
    private ItemType type;
    private long number = 0;
    private ListElement relatedElement = null;
    private int depth = 1;

    public ListElement(String name, ItemType type){
        this.name = name;
        this.type = type;
    }

    public String getNumber(){
        String r;
        switch (this.getType()){
            case TIMER:
                long min = TimeUnit.MINUTES.convert(number, TimeUnit.MILLISECONDS) % 60;
                long sec = TimeUnit.SECONDS.convert(number, TimeUnit.MILLISECONDS) % 60;
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
}
