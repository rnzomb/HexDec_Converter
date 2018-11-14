package com.zomb;

import java.io.*;
import java.util.regex.Pattern;

public class Main {
    static String filename = "values.txt";
    static String readFilePath = "";
    static String writeFilePath = "";
    static String inputString;
    static String[] inputValues;
    static String[] outputValues;


    public static void main(String[] args) {

        // check our arguments with file names
        checkFileNames(args);


        // if there no input file or wrong file path, type values from console
        if (readFilePath != "") {
            readFromFile(readFilePath);
        }
        if (inputString == null) {
            System.out.println("Enter your values :");
            inputString = readString();
        }


        // convert our values
        convertValues(inputString);


        //check write file path and if read and write path are the same then use console output

        if (writeFilePath != "") {

            readFilePath = readFilePath.toUpperCase();
            writeFilePath = writeFilePath.toUpperCase();

            if (writeFilePath.equals(readFilePath)) {

                System.out.println("The read and write file paths are same");

                // output to console
                String textTemp = "";
                String sep = System.getProperty("line.separator");

                for (int i = 0; i < inputValues.length; i++) {
                    textTemp += (inputValues[i] + " " + outputValues[i]) + sep;
                }
                System.out.println(textTemp);
            }
        }

        saveFile(writeFilePath);

        System.out.println("Converted");

    }

    private static void convertValues(String inputString) {

        //split our input string using regular expressions
        Pattern pat = Pattern.compile("[\\W+\\s]+");
        inputValues = pat.split(inputString);


        //set size of our output values array
        outputValues = new String[inputValues.length];


        //convert values from input string array to output string array
        for (int i = 0; i < inputValues.length; i++) {
            if (inputValues[i].contains("0x")) {
                try {
                    outputValues[i] = String.valueOf(Integer.parseInt(inputValues[i].substring(2), 16));      //hex to dec

                } catch (NumberFormatException ne) {
                    outputValues[i] = "invalid hexadecimal value";
                }

            } else {
                try {
                    outputValues[i] = "0x" + Integer.toHexString(Integer.parseInt(inputValues[i]));          //dec to hex

                } catch (NumberFormatException ne) {
                    outputValues[i] = "invalid decimal value";
                }
            }

        }
    }

    private static void checkFileNames(String args[]) {

        //check our arguments for read and write path
        try {
            if (args.length != 0) {
                if (args[0].contains("-i<")) {
                    readFilePath = args[0].substring(args[0].indexOf("<") + 1, args[0].indexOf(">"));
                } else if (args[0].contains("-o<")) {
                    writeFilePath = args[0].substring(args[0].indexOf("<") + 1, args[0].indexOf(">"));
                }
                if (args.length > 1) {
                    if (args[1].contains("-i<")) {
                        readFilePath = args[1].substring(args[1].indexOf("<") + 1, args[1].indexOf(">"));
                    } else if (args[1].contains("-o<")) {
                        writeFilePath = args[1].substring(args[1].indexOf("<") + 1, args[1].indexOf(">"));
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("incorrect arguments");
        }
    }

    private static void saveFile(String writeFilePath) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(writeFilePath + "\\" + filename))) {

            String text = "";
            String sep = System.getProperty("line.separator");
            for (int i = 0; i < inputValues.length; i++) {
                text += inputValues[i] + " " + outputValues[i] + sep;
            }

            bw.write(text);
        } catch (IOException ex) {

            System.out.println(ex.getMessage());
        }
    }


    private static void readFromFile(String readFilePath) {
        try (FileInputStream fin = new FileInputStream(readFilePath + "\\" + filename)) {

            BufferedReader br = new BufferedReader(new InputStreamReader(fin));

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
                sb.append('\n');
            }
            inputString = sb.toString();

        } catch (IOException ex) {

            System.out.println(ex.getMessage());
        }
    }


    public static String readString() {
        String s = "";
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        boolean flag;

        do {
            flag = true;
            try {

                s = in.readLine();

            } catch (IOException ex) {
                System.out.println(ex.getMessage());
                System.exit(1);                 // exit if some error
            }
            //    System.out.println(s);
        } while (!flag);
        return s;
    }
}
