package br.com.gympass.test.kartRace;

import br.com.gympass.test.kartRace.model.LapModel;
import br.com.gympass.test.kartRace.model.PilotModel;
import org.joda.time.LocalTime;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.IsCollectionContaining.hasItems;
import static org.junit.Assert.*;

public class ProcessTest {

    @Test(expected = FileNotFoundException.class)
    public void invalidFile() throws IOException {
        Process.Process("xxxx.txt");
    }

    @Test
    public void setPilotInformation() {
        List<PilotModel> pilotModelList = mock2Pilots();
        String data = "038 – F.MASSA";
        Process.setPilotInformation(data, new LapModel(), pilotModelList);

        assertTrue(pilotModelList.size() == 3);
    }

    @Test
    public void cannotInsertDuplicatedPilot() {
        List<PilotModel> pilotModelList = mock2Pilots();
        pilotModelList.add(new PilotModel("F.MASSA", "038"));

        String data = "038 – F.MASSA";
        Process.setPilotInformation(data, new LapModel(), pilotModelList);

        assertTrue(pilotModelList.size() == 2);
    }

    @Test
    public void organizeRanking(){
        List<PilotModel> pilotModelList = mock2Pilots();
        List<LapModel> lapModelList = mock2LapsPerPilot(pilotModelList);

        Process.oraganizeRanking(pilotModelList, lapModelList);

        assertTrue(pilotModelList.get(0).getName().equals("F.ALONSO"));
    }

    @Test
    public void organizeRankingFail(){
        List<PilotModel> pilotModelList = mock2Pilots();
        List<LapModel> lapModelList = mock2LapsPerPilot(pilotModelList);

        Process.oraganizeRanking(pilotModelList, lapModelList);

        assertFalse(pilotModelList.get(1).getName().equals("F.ALONSO"));
    }

    @Test
    public void getRaceTime(){
        List<PilotModel> pilotModelList = mock1Pilot();
        List<LapModel> lapModelList = mock2LapsFor1Pilot(pilotModelList);

        pilotModelList.get(0).setRaceTime(Process.getRaceTime(lapModelList));

        assertTrue(pilotModelList.get(0).getRaceTime() != null);
    }

    @Test
    public void calculateTimeAfterWinner(){
        List<PilotModel> pilotModelList = mock2Pilots();
        List<LapModel> lapModelList = mock2LapsPerPilot(pilotModelList);
        LocalTime winnerTime = new LocalTime(0,0,0,0);

        Process.oraganizeRanking(pilotModelList, lapModelList);

        Process.calculateTimeAfterWinner(pilotModelList);

        assertTrue(winnerTime.isEqual(pilotModelList.get(0).getTimeAfterWinner()));
        assertTrue(winnerTime.isBefore(pilotModelList.get(1).getTimeAfterWinner()));
    }

    private List<PilotModel> mock2Pilots() {
        List<PilotModel> pilotModelList = new ArrayList<>();
        pilotModelList.add(new PilotModel("M.WEBBER", "023"));
        pilotModelList.add(new PilotModel("F.ALONSO", "015"));

        return pilotModelList;
    }

    private List<LapModel> mock2LapsPerPilot(List<PilotModel> pilotModelList){
        List<LapModel> lapModelList = new ArrayList<>();

        LapModel lapModel = new LapModel();
        lapModel.setPilot(new PilotModel("M.WEBBER", "023"));
        lapModel.setLapNumber(1);
        lapModel.setLapTime(new LocalTime(0,1,5,720));
        lapModel.setAvarageSpeed(42.030);
        lapModelList.add(lapModel);

        LapModel lapModel2 = new LapModel();
        lapModel2.setPilot(new PilotModel("M.WEBBER", "023"));
        lapModel2.setLapNumber(2);
        lapModel2.setLapTime(new LocalTime(0,1,4,432));
        lapModel2.setAvarageSpeed(43.111);
        lapModelList.add(lapModel2);

        LapModel lapModel3 = new LapModel();
        lapModel3.setPilot(new PilotModel("F.ALONSO", "015"));
        lapModel3.setLapNumber(1);
        lapModel3.setLapTime(new LocalTime(0,1,2,852));
        lapModel3.setAvarageSpeed(44.275);
        lapModelList.add(lapModel3);

        LapModel lapModel4 = new LapModel();
        lapModel4.setPilot(new PilotModel("F.ALONSO", "015"));
        lapModel4.setLapNumber(2);
        lapModel4.setLapTime(new LocalTime(0,1,3,170));
        lapModel4.setAvarageSpeed(44.053);
        lapModelList.add(lapModel4);

        return lapModelList;
    }

    private List<PilotModel> mock1Pilot() {
        List<PilotModel> pilotModelList = new ArrayList<>();
        pilotModelList.add(new PilotModel("M.WEBBER", "023"));
        pilotModelList.add(new PilotModel("F.ALONSO", "015"));

        return pilotModelList;
    }

    private List<LapModel> mock2LapsFor1Pilot(List<PilotModel> pilotModelList){
        List<LapModel> lapModelList = new ArrayList<>();

        LapModel lapModel = new LapModel();
        lapModel.setPilot(new PilotModel("M.WEBBER", "023"));
        lapModel.setLapNumber(1);
        lapModel.setLapTime(new LocalTime(0,1,5,720));
        lapModel.setAvarageSpeed(42.030);
        lapModelList.add(lapModel);

        LapModel lapModel2 = new LapModel();
        lapModel2.setPilot(new PilotModel("M.WEBBER", "023"));
        lapModel2.setLapNumber(2);
        lapModel2.setLapTime(new LocalTime(0,1,4,432));
        lapModel2.setAvarageSpeed(43.111);
        lapModelList.add(lapModel2);


        return lapModelList;
    }




}