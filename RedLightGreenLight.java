import java.util.Random;
import java.util.Scanner;

public class RedLightGreenLight {

    private static final String RED_LIGHT = "Red Light 🔴 👧";
    private static final String GREEN_LIGHT = "Green Light 🟢 👧";
    private static final String GAME_OVER = "Game Over 🤯🔫 (press Enter)";

    private static final int TIME_LIMIT = 1000 * 60; // 1 Minute
    private static final int BREATHER = 500;
    private static final int GREEN_LIGHT_MAX_TIME = 4000;

    private static int pressCount = 0;
    private static boolean gameOver = false;

    private static Scanner scanner;

    public static void main(String[] args) throws InterruptedException {
        scanner = new Scanner(System.in);
        boolean continuePlaying = false;
        do {
            pressCount = 0;
            gameOver = false;
            System.out.println("\n =============================");
            System.out.printf("Starting the Squid Game #1: %s / %s", RED_LIGHT, GREEN_LIGHT);
            System.out.println();
            System.out.println("When Green Light, Please press the Enter button continuously.");
            System.out.println("Stop pressing once you see Red light");

            Thread frontMan = new Thread(RedLightGreenLight::frontMan);
            Thread player = new Thread(RedLightGreenLight::player);

            frontMan.start();
            player.start();

            frontMan.join();
            player.join();

            System.out.println("============================");
            System.out.println("Do you want to continue?: [Y/N] : ");
            String next = scanner.next();
            continuePlaying = next.equalsIgnoreCase("Y");
        } while (continuePlaying); // if pressed Y, restart the game
    }

    private static void frontMan() {
        int timeRemain = TIME_LIMIT;
        Random random = new Random();
        do {
            int dollCountingTime = random.nextInt(Math.min(timeRemain, GREEN_LIGHT_MAX_TIME)) + 1000;
            System.out.printf("%s Time remain: %d seconds (current score %d ): %n", GREEN_LIGHT, timeRemain / 1000,
                    pressCount);
            System.out.println();

            timeRemain -= dollCountingTime;

            sleep(dollCountingTime);

            System.out.println(RED_LIGHT);

            int lastPressedInGreenLight = pressCount;
            int scanningForMovements = random.nextInt(2000) + BREATHER;
            sleep(scanningForMovements); // Scanning for any movement
            timeRemain -= scanningForMovements;

            if (pressCount != lastPressedInGreenLight) {
                gameOver = true;
                pressCount = lastPressedInGreenLight;
                break;
            }
        } while (timeRemain >= 0);

        System.out.printf("%s: Your score is %d", GAME_OVER, pressCount);
    }

    private static void player() {
        while (!gameOver) {
            scanner.nextLine();
            pressCount++;
        }
    }

    public static void sleep(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
