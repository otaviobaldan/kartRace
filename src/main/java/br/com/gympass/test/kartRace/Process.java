package br.com.gympass.test.kartRace;

import br.com.gympass.test.kartRace.model.LapModel;
import br.com.gympass.test.kartRace.model.PilotModel;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Process {
    static String datePattern = "mm:ss.SSS";
    static DateTimeFormatter formatter = DateTimeFormat.forPattern(datePattern);
    static LocalTime bestRaceLap;

    public static void Process(String filename){
        List<PilotModel> pilotModelList = new ArrayList<>();
        List<LapModel> lapModelList = new ArrayList<>();
        Integer lapNumber;
        BufferedReader reader;
        ClassLoader classLoader = Process.class.getClassLoader();
        // load file
        File file = new File(classLoader.getResource(filename).getFile());

        try {
            // read the file
            reader = new BufferedReader(new FileReader(file));
            String line;

            // while i have lines to read, do the process
            while ((line = reader.readLine()) != null) {
                // split every line with tabulation char
                String[] data = line.split("\t");
                LapModel lapModel = new LapModel();
                // column controls what data I need to insert and where
                int column = 0;
                for(int i = 0; i<= data.length-1; i++)
                    // verify if have null data, if not null process data
                    if (!data[i].trim().isEmpty()) {
                        switch (column) {
                            // Pilot informations
                            case 1: {
                                setPilotInformation(data[i], lapModel, pilotModelList);
                                break;
                            }
                            // lap number
                            case 2: {
                                lapNumber = Integer.parseInt(data[i].trim());
                                lapModel.setLapNumber(lapNumber);
                                if (lapNumber == 4 && !hasWinner(pilotModelList))
                                    PilotModel.setWinner(true, pilotModelList, lapModel.getPilot());
                                break;
                            }
                            // lap time
                            case 3: {
                                lapModel.setLapTime(LocalTime.parse(data[i].trim(), formatter));
                                break;
                            }
                            // medium velocity for lap
                            case 4: {
                                lapModel.setMediumVelocity(Double.parseDouble(data[i].replace(",",".").trim()));
                                break;
                            }
                        }
                        column++;
                    }
                lapModelList.add(lapModel);
            }
            reader.close();

            oraganizeRanking(pilotModelList, lapModelList);
            calculateTimeAfterWinner(pilotModelList);
            printRanking(pilotModelList);


        } catch (
                IOException e) {
            e.printStackTrace();
        }
    }

    private static void setPilotInformation(String data, LapModel lapModel, List<PilotModel> pilotModelList) {
        String pilotName = data.substring(6).trim();
        String pilotNumber = data.substring(0, 3).trim();
        PilotModel pilotModel = new PilotModel(pilotName, pilotNumber);
        lapModel.setPilot(pilotModel);

        if (!canAddPilot(pilotModelList, pilotModel)) pilotModelList.add(pilotModel);
    }

    private static boolean canAddPilot(List<PilotModel> pilotModelList, PilotModel pilotModel){
        for (PilotModel pm : pilotModelList) {
            if (pm.getCode().equals(pilotModel.getCode()) && pm.getName().equals(pilotModel.getName())) return true;
        }
        return false;
    }

    private static List<LapModel> filterPilotLap(List<LapModel> lapModelList, PilotModel pilotModel){
        List<LapModel> returnLapModel = new ArrayList<>();
        Double mediumRaceVelocity = 0.00;
        for (LapModel lm : lapModelList) {
            if (lm.getPilot().getCode().equals(pilotModel.getCode()) && lm.getPilot().getName().equals(pilotModel.getName())) {
                returnLapModel.add(lm);

                mediumRaceVelocity = mediumRaceVelocity + lm.getMediumVelocity();
                if (pilotModel.getBestLap() == null || lm.getLapTime().isBefore(pilotModel.getBestLap()))
                    pilotModel.setBestLap(lm.getLapTime());

                if (bestRaceLap == null || lm.getLapTime().isBefore(bestRaceLap))
                    bestRaceLap = lm.getLapTime();
            }
        }
        mediumRaceVelocity = mediumRaceVelocity / returnLapModel.size();
        pilotModel.setMediumRaceVelocity(mediumRaceVelocity);
        pilotModel.setRaceTime(getRaceTime(returnLapModel));
        pilotModel.setLapCount(returnLapModel.size());
        return returnLapModel;
    }

    private static void oraganizeRanking(List<PilotModel> pilotModelList, List<LapModel> lapModelList){
        for (PilotModel pm : pilotModelList){
            List<LapModel> newLapModelList = filterPilotLap(lapModelList, pm);
            Collections.sort(newLapModelList);
        }
        Collections.sort(pilotModelList);
    }

    private static boolean hasWinner(List<PilotModel> pilotModelList){
        for (PilotModel pm : pilotModelList){
            if (pm.isWinner()) return true;
        }
        return false;
    }

    private static LocalTime getRaceTime(List<LapModel> lapModelList){
        int millis = 0;
        int seconds = 0;
        int minutes = 0;
        int hours = 0;
        for ( LapModel lm : lapModelList){
            millis = millis + lm.getLapTime().getMillisOfSecond();
            seconds = seconds + lm.getLapTime().getSecondOfMinute();
            minutes = minutes + lm.getLapTime().getMinuteOfHour();
        }

        if (millis > 999) {
            seconds = seconds + (millis/999);
            millis = millis % 999;
        }

        if (seconds > 59) {
            minutes = minutes + (seconds / 60);
            seconds = seconds % 60;
        }

        return new LocalTime(hours ,minutes,seconds, millis);
    }

    private static void calculateTimeAfterWinner(List<PilotModel> pilotModelList){
        LocalTime winnerTime = pilotModelList.get(0).getRaceTime();

        for(PilotModel pm : pilotModelList){
            pm.setTimeAfterWinner(differenceBetweenTimes(winnerTime, pm.getRaceTime()));
        }
    }

    private static LocalTime differenceBetweenTimes(LocalTime bestTime, LocalTime worstTime){
        int minutesWorst = worstTime.getMinuteOfHour();
        int secondsWorst = worstTime.getSecondOfMinute();
        int milliWorst = worstTime.getMillisOfSecond();

        int minutesBest = bestTime.getMinuteOfHour();
        int secondsBest= bestTime.getSecondOfMinute();
        int milliBest = bestTime.getMillisOfSecond();

        int newMinutes =  (minutesWorst - minutesBest < 0) ? (minutesBest - minutesWorst) : (minutesWorst - minutesBest);
        int newSeconds = (secondsWorst - secondsBest < 0) ? (secondsBest - secondsWorst) : (secondsWorst - secondsBest);
        int newMilli = (milliWorst - milliBest < 0) ? (milliBest - milliWorst) : (milliWorst - milliBest);

        return new LocalTime(0, newMinutes, newSeconds, newMilli);

    }

    private static void printRanking(List<PilotModel> pilotModelList){
        System.out.println(String.format("Melhor volta da corrida: %s \n\n", bestRaceLap));
        pilotModelList.forEach(pilotModel ->
            System.out.println(String.format("Posição: %d \n"+
                " Código: %s \t\t" +
                " Nome: %s \n" +
                " Voltas: %d \t\t" +
                " Velocidade Média: %.3f \t\t" +
                " Melhor Volta: %s \t\t" +
                " Tempo para o vencedor: %s \t\t" +
                " Tempo Total: %s \n\n",
                pilotModelList.indexOf(pilotModel) + 1,
                    pilotModel.getCode(),
                    pilotModel.getName(),
                    pilotModel.getLapCount(),
                    pilotModel.getMediumRaceVelocity(),
                    pilotModel.getBestLap(),
                    pilotModel.getTimeAfterWinner(),
                    pilotModel.getRaceTime()
                )
            )
        );
    }
}
