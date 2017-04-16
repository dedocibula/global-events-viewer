package edu.vt.dlrl.domain;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import java.util.List;

/**
 * Author: dedocibula
 * Created on: 15.4.2017.
 */
public class TrendSelection {
    @JsonFormat(pattern = "yyyy")
    private Date from;
    @JsonFormat(pattern = "yyyy")
    private Date to;
    private List<Trend> trends;

    public TrendSelection(Date from, Date to) {
        this.from = from;
        this.to = to;
    }

    public Date getFrom() {
        return from;
    }

    public void setFrom(Date from) {
        this.from = from;
    }

    public Date getTo() {
        return to;
    }

    public void setTo(Date to) {
        this.to = to;
    }

    public List<Trend> getTrends() {
        return trends;
    }

    public void setTrends(List<Trend> trends) {
        this.trends = trends;
    }
}
