package br.com.gympass.test.kartRace;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class KartRaceApplication {

	public static void main(String[] args) {
		try {
			Process.Process("kartRace.txt");
		} catch (
				IOException e) {
			e.printStackTrace();
		}
	}

}
