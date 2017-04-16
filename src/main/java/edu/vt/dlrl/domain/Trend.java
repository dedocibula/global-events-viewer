package edu.vt.dlrl.domain;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * Author: dedocibula
 * Created on: 15.4.2017.
 */
public class Trend {
    @JsonFormat(pattern = "yyyy")
    private Date date;
    private int assailantAge;
    private int victimCount;

    public Trend(Date date, int assailantAge, int victimCount) {
        this.date = date;
        this.assailantAge = assailantAge;
        this.victimCount = victimCount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getAssailantAge() {
        return assailantAge;
    }

    public void setAssailantAge(int assailantAge) {
        this.assailantAge = assailantAge;
    }

    public int getVictimCount() {
        return victimCount;
    }

    public void setVictimCount(int victimCount) {
        this.victimCount = victimCount;
    }
}
