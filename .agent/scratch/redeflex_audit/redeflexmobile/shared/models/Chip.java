package com.axys.redeflexmobile.shared.models;

/**
 * Created by joao.viana on 20/07/2016.
 */
public class Chip {
    private int Position;
    private String ThisChar;
    private Integer Doubled;
    private Integer Summed;

    public Integer getDoubled() {
        return Doubled;
    }

    public void setDoubled(Integer doubled) {
        Doubled = doubled;
    }

    public Integer getSummed() {
        return Summed;
    }

    public void setSummed(Integer summed) {
        Summed = summed;
    }

    public int getPosition() {
        return Position;
    }

    public void setPosition(int position) {
        Position = position;
    }

    public String getThisChar() {
        return ThisChar;
    }

    public void setThisChar(String thisChar) {
        ThisChar = thisChar;
    }
}