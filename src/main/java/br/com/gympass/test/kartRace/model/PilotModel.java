package br.com.gympass.test.kartRace.model;

import org.joda.time.LocalTime;

import java.util.List;

public class PilotModel implements Comparable<PilotModel>{
    private String name;
    private String code;
    private Double mediumRaceVelocity;
    private boolean isWinner = false;
    private Integer lapCount = 0;
    private LocalTime raceTime;
    private LocalTime bestLap;
    private LocalTime timeAfterWinner;

    public LocalTime getTimeAfterWinner() {
        return timeAfterWinner;
    }

    public void setTimeAfterWinner(LocalTime timeAfterWinner) {
        this.timeAfterWinner = timeAfterWinner;
    }

    public LocalTime getBestLap() {
        return bestLap;
    }

    public void setBestLap(LocalTime bestLap) {
        this.bestLap = bestLap;
    }

    public Integer getLapCount() {
        return lapCount;
    }

    public void setLapCount(Integer lapCount) {
        this.lapCount = lapCount;
    }

    public LocalTime getRaceTime() {
        return raceTime;
    }

    public void setRaceTime(LocalTime raceTime) {
        this.raceTime = raceTime;
    }

    public boolean isWinner() {
        return isWinner;
    }

    public void setWinner(boolean winner) {
        isWinner = winner;
    }

    public static void setWinner(boolean winner, List<PilotModel> pilotModelList, PilotModel pilotModel) {
        for (PilotModel pm : pilotModelList){
            if (pm.getCode().equals(pilotModel.getCode()))
                pm.isWinner = winner;
        }
    }

    public PilotModel(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Double getMediumRaceVelocity() {
        return mediumRaceVelocity;
    }

    public void setMediumRaceVelocity(Double mediumRaceVelocity) {
        this.mediumRaceVelocity = mediumRaceVelocity;
    }

    @Override
    public int compareTo(PilotModel p) {
        return this.getRaceTime().compareTo(p.getRaceTime());
    }

}
