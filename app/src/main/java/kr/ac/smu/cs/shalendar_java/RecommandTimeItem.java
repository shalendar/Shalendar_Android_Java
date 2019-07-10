package kr.ac.smu.cs.shalendar_java;

import android.widget.RadioButton;

//추천시간 recyclerview의 item들
public class RecommandTimeItem {
    private String reco_timeNumber;
    private String reco_time;
    private RadioButton reco_radioButton;

    public RecommandTimeItem() {
    }

    public RecommandTimeItem(String reco_timeNumber, String reco_time) {
        this.reco_timeNumber = reco_timeNumber;
        this.reco_time = reco_time;
    }

    public String getReco_timeNumber() {
        return reco_timeNumber;
    }

    public void setReco_timeNumber(String reco_timeNumber) {
        this.reco_timeNumber = reco_timeNumber;
    }

    public String getReco_time() {
        return reco_time;
    }

    public void setReco_time(String reco_time) {
        this.reco_time = reco_time;
    }

}
