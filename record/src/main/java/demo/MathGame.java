package demo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class MathGame {
    private static Random random = new Random();

    public int illegalArgumentCount = 0;

    public static void main(String[] args) throws Exception{
        MathGame game = new MathGame();
        while (true) {
            game.run();
            TimeUnit.SECONDS.sleep(1);
        }
    }

    public void run() {
        try{
            int number = random.nextInt() / 10000;
            List<Integer> primeFactors = primeFactors(number);
            print(number, primeFactors);
        }catch (Exception e){
            e.printStackTrace();
        }


    }

    public static void print(int number, List<Integer> primeFactors) {
        StringBuilder sb = new StringBuilder(number + "=");
        for (int factor : primeFactors) {
            sb.append(factor).append('*');
        }
        if (sb.charAt(sb.length() - 1) == '*') {
            sb.deleteCharAt(sb.length() - 1);
        }

    }

    public List<Integer> primeFactors(int number) {
        try {
            TimeUnit.SECONDS.sleep(number & 3 + 4);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (number < 2) {
            illegalArgumentCount++;
            throw new IllegalArgumentException("number is: " + number + ", need >= 2");
        }

        List<Integer> result = new ArrayList<Integer>();
        int i = 2;
        while (i <= number) {
            if (number % i == 0) {
                result.add(i);
                number = number / i;
                i = 2;
            } else {
                i++;
            }
        }

        return result;
    }
}
