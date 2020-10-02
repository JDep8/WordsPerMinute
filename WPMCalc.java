import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class WPMCalc {

    private String[] pastResults;
    private String[] dictionary;
    int currrentNumResults;
    int currentNumWordsInDiCtionary;
    Scanner userInput = new Scanner(System.in);

    public void WPMcalc(int numResults) {
	
	int selection = 0;
	this.pastResults = new String[numResults];
	this.dictionary = new String[1000];
	this.currrentNumResults = 0;
	this.currentNumWordsInDiCtionary = 0;

	// populate past results and dictionary
	readResultsFile();
	readDictionaryFile();

	// Menu
	System.out.println("Welcome to the WPM calculator game");
	do {

	    System.out.println("------------------");
	    System.out.println("1. New Game");
	    System.out.println("2. Past Results");
	    System.out.println("3. Exit");
	    System.out.println("------------------");

	    selection = Integer.parseInt(userInput.nextLine());

	    if (selection == 1) {
		newGame();
	    } else if (selection == 2) {
		pastResults();
	    } else if (selection == 3) {
		writeResultsToFile();

	    }
	} while (selection != 3);
    }

    public void newGame() {

	try {

	    // Count down
	    System.out.println("Get ready");
	    TimeUnit.SECONDS.sleep(1);
	    System.out.println("3");
	    TimeUnit.SECONDS.sleep(1);
	    System.out.println("2");
	    TimeUnit.SECONDS.sleep(1);
	    System.out.println("1");
	    TimeUnit.SECONDS.sleep(1);

	    // Generate random sentence
	    Random r1 = new Random();
	    String sentence = null;
	    for (int i = 0; i < 10; i++) {
		if (i == 0) {
		    sentence = this.dictionary[r1.nextInt(this.currentNumWordsInDiCtionary)] + " ";
		} else if (i < 9) {

		    sentence = sentence + this.dictionary[r1.nextInt(this.currentNumWordsInDiCtionary)] + " ";
		} else {
		    sentence = sentence + this.dictionary[r1.nextInt(this.currentNumWordsInDiCtionary)];
		}

	    }

	    // Output sentence
	    System.out.println(sentence);
	    double startTime = LocalTime.now().toNanoOfDay();

	    // User answer

	    String userAnswer = userInput.nextLine();

	    // Acc. calc:
	    int correctLetters = 0;
	    for (int i = 0; i < sentence.length(); i++) {
		// if the user answer doesn't match the length of the sentence, we need to stop
		// looping as it will be out of bounds on the char index
		if (i == sentence.length() || i == userAnswer.length()) {
		    i = sentence.length() + userAnswer.length();
		}

		// Compare sentence letter to user input
		else if (sentence.charAt(i) == userAnswer.charAt(i)) {
		    correctLetters++;
		}
	    }

	    double acc = ((double) correctLetters / sentence.length()) * 100;

	    if (acc >= 85.0) {
		// WPM Calc.
		double endTime = LocalTime.now().toNanoOfDay();
		double secElapsed = ((endTime - startTime) / 1000000000.0);
		double avgWordLength = ((double) userAnswer.replaceAll("\\s+", "").length() / 10);

		int WPM = (int) ((((double) userAnswer.length() / avgWordLength) / secElapsed) * 60);

		System.out.println("\nWords per minute: " + WPM + "!");

		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
		LocalDateTime now = LocalDateTime.now();

		// extend array if needed
		if (this.currrentNumResults == this.pastResults.length) {
		    extendArray();

		}

		// Populate results
		this.pastResults[this.currrentNumResults] = dtf.format(now) + "," + WPM + "," + acc;
		this.currrentNumResults++;

	    } else {

		System.out.println("Invalid result, Accuracy below 85%");
	    }

	    System.out.printf("Accuracy: %.2f%%\n\n", acc);

	} catch (InterruptedException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

    public void pastResults() {
	if (this.currrentNumResults != 0) {

	    System.out.println("----------------------------------");
	    System.out.println("Time and Date    | WPM | Accuracy ");
	    System.out.println("----------------------------------");

	    for (int i = 0; i < this.currrentNumResults; i++) {

		String result[] = this.pastResults[i].split(",");
		String date = result[0];
		String WPM = result[1];
		Double ACC = Double.parseDouble(result[2]);

		System.out.printf("%s | %-3s | %.2f%%\n", date, WPM, ACC);

	    }
	    System.out.println("----------------------------------\n");
	} else {

	    System.out.println("No past results, Try playing a new game!\n");
	}
    }

    public void extendArray() {

	if (this.currrentNumResults == this.pastResults.length) {
	    String[] tempPastResults = new String[this.pastResults.length + 50];

	    for (int i = 0; i < this.currrentNumResults; i++) {

		tempPastResults[i] = this.pastResults[i];
	    }

	    this.pastResults = new String[tempPastResults.length];

	    for (int i = 0; i < this.currrentNumResults; i++) {

		this.pastResults[i] = tempPastResults[i];

	    }
	} else if (this.currentNumWordsInDiCtionary == this.dictionary.length) {
	    String[] tempDictionary = new String[this.dictionary.length + 100];

	    for (int i = 0; i < this.currentNumWordsInDiCtionary; i++) {

		tempDictionary[i] = this.dictionary[i];
	    }

	    this.dictionary = new String[tempDictionary.length];

	    for (int i = 0; i < this.currentNumWordsInDiCtionary; i++) {

		this.dictionary[i] = tempDictionary[i];
	    }

	}
    }

    public void readResultsFile() {

	try {
	    Scanner fileScanner = new Scanner(new FileReader("pastResults.txt"));

	    while (fileScanner.hasNextLine()) {

		// extend array if needed
		if (this.currrentNumResults == this.pastResults.length) {
		    extendArray();

		}

		this.pastResults[this.currrentNumResults] = fileScanner.nextLine();
		this.currrentNumResults++;

	    }

	} catch (FileNotFoundException e) {
	    // TODO Auto-generated catch block
	 
	}

    }

    public void readDictionaryFile() {

   	try {
   	    Scanner fileScanner = new Scanner(new FileReader("dictionary.txt"));

   	    while (fileScanner.hasNextLine()) {

   		// extend array if needed
   		if (this.currentNumWordsInDiCtionary == this.dictionary.length) {
   		    extendArray();

   		}

   		this.dictionary[this.currentNumWordsInDiCtionary] = fileScanner.nextLine().toLowerCase();
   		this.currentNumWordsInDiCtionary++;

   	    }

   	} catch (FileNotFoundException e) {
   	    // TODO Auto-generated catch block
   	
  
   	}

       }
    
    public void writeResultsToFile() {

	// write array to data file
	try {
	    PrintWriter pw = new PrintWriter(new FileWriter("pastResults.txt"));

	    for (int i = 0; i < this.currrentNumResults; i++) {

		pw.println(this.pastResults[i]);
	    }
	    pw.close();

	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

    };

    public static void main(String[] args) {

	WPMCalc objName = new WPMCalc();
	objName.WPMcalc(50);

    }

}
