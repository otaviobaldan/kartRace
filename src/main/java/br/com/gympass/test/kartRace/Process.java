package br.com.gympass.test.kartRace;

import br.com.gympass.test.kartRace.Utils.DateUtils;
import br.com.gympass.test.kartRace.model.LapModel;
import br.com.gympass.test.kartRace.model.PilotModel;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public class Process {
    // pattern used to format the lap time
    static String datePattern = "mm:ss.SSS";
    static DateTimeFormatter formatter = DateTimeFormat.forPattern(datePattern);
    // variable used to get the best lap of the race
    static LocalTime bestRaceLap;

    public static void Process(String filename) {
        // the model that I'll use to put the pilot
        List<PilotModel> pilotModelList = new ArrayList<>();
        // the model that I'll use to put the laps of one pilot
        List<LapModel> lapModelList = new ArrayList<>();
        // variable that will used to compare the lap number
        // I'll use one varible to do the conversion to integer one time only
        Integer lapNumber;

        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(filename);
        // Use inputStream to check if the file exists on the resource
        if (inputStream != null){
            try {
                BufferedReader reader = new  BufferedReader(new InputStreamReader(inputStream));

                // variable to receive the lines readed
                String line;

                // while i have lines to read, do the process
                while ((line = reader.readLine()) != null) {

                    // replace the 2 spaces characters to tabulation, that way I can read the file
                    line = line.replace("  ", "\t");
                    // split every line with tabulation char
                    String[] data = line.split("\t");
                    // create a LapModel in every line readed to store a lap
                    // after I'll put it on the lapModelList
                    LapModel lapModel = new LapModel();
                    // column controls what data I need to insert and where
                    int column = 0;
                    // the for are used to read all data that was splitted previouslly
                    for (int i = 0; i <= data.length - 1; i++)
                        // verify if have null data, if not null process data
                        if (!data[i].trim().isEmpty()) {
                            switch (column) {
                                // set Pilot informations
                                case 1: {
                                    setPilotInformation(data[i], lapModel, pilotModelList);
                                    break;
                                }
                                // set lap number
                                case 2: {
                                    // convert the lap number on data to Integer
                                    lapNumber = Integer.parseInt(data[i].trim());
                                    // store on lapmodel
                                    lapModel.setLapNumber(lapNumber);
                                    // if the lapNumber is equal 4 and doesn't have any pilot setted as a winner
                                    // I'll set the pilot as a winner, because he was the first to complete 4 laps
                                    if (lapNumber == 4 && !PilotModel.hasWinner(pilotModelList))
                                        PilotModel.setWinner(true, pilotModelList, lapModel.getPilot());
                                    break;
                                }
                                // set lap time
                                case 3: {
                                    lapModel.setLapTime(LocalTime.parse(data[i].trim(), formatter));
                                    break;
                                }
                                // medium velocity for lap
                                case 4: {
                                    lapModel.setAvarageSpeed(Double.parseDouble(data[i].replace(",", ".").trim()));
                                    break;
                                }
                            }
                            // every time that I process a data I add 1 on column to control the flow and know what is the next data
                            column++;
                        }
                    // after all the lapModel be filled, I add it on lapModelList
                    lapModelList.add(lapModel);
                }
                // after I read all the file, I close the reader
                reader.close();

                // Organize the ranking order
                oraganizeRanking(pilotModelList, lapModelList);
                // Calculate the time that each pilot arrive after winner
                calculateTimeAfterWinner(pilotModelList);
                // And finally print the ranking
                printRanking(pilotModelList);


            } catch (
                    IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Arquivo não encontrado");
        }

    }

    // Method used to set Pilot Name and Number
    private static void setPilotInformation(String data, LapModel lapModel, List<PilotModel> pilotModelList) {
        // Use the substring to take the pilot number on first 3 positions ...
        String pilotNumber = data.substring(0, 3).trim();
        // ... and to take the Pilot name after the "-" char
        String pilotName = data.substring(6).trim();

        // create a new pilot model with the informations filled
        PilotModel pilotModel = new PilotModel(pilotName, pilotNumber);
        // add the pilot model to lapModel, that help to know the laps of this pilot
        lapModel.setPilot(pilotModel);

        // Here I need to verify if the pilot are able to be included on Pilot List, avoiding duplicated pilots in this list
        if (!canAddPilot(pilotModelList, pilotModel)) pilotModelList.add(pilotModel);
    }

    // Method to verify if the pilot are able to be included on Pilot List
    private static boolean canAddPilot(List<PilotModel> pilotModelList, PilotModel pilotModel){
        // I'll run through the pilot list verifying if exists one pilot with the same number
        // If I found any pilot with the same information, return true, in that way this pilot not will
        //   be added again on the list
        // I don't use the name to check, because can have any typing error on name
        for (PilotModel pm : pilotModelList) {
            if (pm.getCode().equals(pilotModel.getCode())) return true;
        }
        // if I not found, only return false and the pilot will be added
        return false;
    }

    // oraganizeRanking is a method that will sort the pilots and laps
    private static void oraganizeRanking(List<PilotModel> pilotModelList, List<LapModel> lapModelList){
        // Run through all pillots
        for (PilotModel pm : pilotModelList){
            // filter the laps of this pilot and set some informations like best lap, medium velocity
            List<LapModel> newLapModelList = filterPilotLapAndSetRaceInformation(lapModelList, pm);
            // After all, I sort that, sorting by lap number
            Collections.sort(newLapModelList);
        }
        // Sort the pilots using the raceTime
        Collections.sort(pilotModelList);
    }

    // In this method, I'll filter all the pilot laps, sort then, and set some informations
    //   like race time, avarage speed and lapCount.
    // I receive a pilot in parameter to know who do I need to filter the laps
    private static List<LapModel> filterPilotLapAndSetRaceInformation(List<LapModel> lapModelList, PilotModel pilotModel){
        // create a new lapList to receive the pilot laps filtered
        List<LapModel> returnLapModel = new ArrayList<>();
        // variable used to know what is the avarage speed in the race for each pilot
        Double raceAvarageSpeed = 0.00;
        // for every lap that I have in lapModelList, I execute the same processes
        for (LapModel lm : lapModelList) {
            // If the pilot of the lap is the same that I have in the parameters, I'll add the lap to new list
            if (lm.getPilot().getCode().equals(pilotModel.getCode())) {
                returnLapModel.add(lm);

                // I add the avarage speed of the lap into a variable
                raceAvarageSpeed = raceAvarageSpeed + lm.getAvarageSpeed();

                // Verifying if the current lap is the best lap of the pilot
                // if the best lap is null I assume that is the best pilot lap
                if (pilotModel.getBestLap() == null || lm.getLapTime().isBefore(pilotModel.getBestLap()))
                    pilotModel.setBestLap(lm.getLapTime());

                // Do the same with the previous validation, but here I set the best lap of the race
                if (bestRaceLap == null || lm.getLapTime().isBefore(bestRaceLap))
                    bestRaceLap = lm.getLapTime();
            }
        }
        // after all the laps covered, I use the size of new lapList to know what is the pilot avarage speed
        raceAvarageSpeed = raceAvarageSpeed / returnLapModel.size();
        pilotModel.setRaceAvarageSpeed(raceAvarageSpeed);
        // I use the method getRaceTime to know what is the pilot race time
        pilotModel.setRaceTime(getRaceTime(returnLapModel));
        // and set how many laps the pilot runned
        pilotModel.setLapCount(returnLapModel.size());
        return returnLapModel;
    }

    // In this method I'l manipulate the time, to sum all the lap times
    private static LocalTime getRaceTime(List<LapModel> lapModelList){
        // create a list of LocalTime to calculate the total race time
        List<LocalTime> localTimeList = new ArrayList<>();
        lapModelList.forEach(lapModel -> localTimeList.add(lapModel.getLapTime()));
        return DateUtils.sumTimes(localTimeList);
    }

    // calculateTimeAfterWinner, like the name says, calculate the time that one pilot arrived in the end of the race
    //   after the first pilot
    private static void calculateTimeAfterWinner(List<PilotModel> pilotModelList){
        // As the pilot list is already sorted, I take the first element on it and yout race time
        LocalTime winnerTime = pilotModelList.get(0).getRaceTime();

        // For every pilot in list, I set the difference between the current pilot and the winner.
        for(PilotModel pm : pilotModelList){
            pm.setTimeAfterWinner(DateUtils.differenceBetweenTimes(winnerTime, pm.getRaceTime()));
        }
    }

    // After all the sorts and variables set, I print the right order of ranking
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
                    pilotModel.getAvarageSpeed(),
                    pilotModel.getBestLap(),
                    pilotModel.getTimeAfterWinner(),
                    pilotModel.getRaceTime()
                )
            )
        );
    }
}
