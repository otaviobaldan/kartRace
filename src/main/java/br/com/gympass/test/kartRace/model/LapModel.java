package br.com.gympass.test.kartRace.model;

import org.joda.time.LocalTime;

public class LapModel implements Comparable<LapModel>{
    private PilotModel pilot;
    private Integer lapNumber;
    private Double mediumVelocity;
    private LocalTime lapTime;
    private Boolean bestLap;

    public PilotModel getPilot() {
        return pilot;
    }

    public void setPilot(PilotModel pilot) {
        this.pilot = pilot;
    }

    public Integer getLapNumber() {
        return lapNumber;
    }

    public void setLapNumber(Integer lapNumber) {
        this.lapNumber = lapNumber;
    }

    public Double getMediumVelocity() {
        return mediumVelocity;
    }

    public void setMediumVelocity(Double mediumVelocity) {
        this.mediumVelocity = mediumVelocity;
    }

    public LocalTime getLapTime() {
        return lapTime;
    }

    public void setLapTime(LocalTime lapTime) {
        this.lapTime = lapTime;
    }

    public Boolean getBestLap() {
        return bestLap;
    }

    public void setBestLap(Boolean bestLap) {
        this.bestLap = bestLap;
    }

    @Override
    public int compareTo(LapModel p) {
        return this.getLapNumber().compareTo(p.getLapNumber());
    }

}
