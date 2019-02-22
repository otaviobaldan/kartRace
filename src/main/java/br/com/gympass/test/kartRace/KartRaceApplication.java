package br.com.gympass.test.kartRace;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

@SpringBootApplication
public class KartRaceApplication {

	public static void main(String[] args) {
		Process.Process("kartRace.txt");
	}

}
