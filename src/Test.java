import java.util.*;

//import com.sun.java.util.jar.pack.Package.Class.Method;

import java.lang.*;

public class Test {
    private static ArrayList<Integer> method1;

    public static void RecPermute(String soFar, String rest) {
        if (rest.equals("")) {
            //System.out.println(soFar);
            return;
        } else {
            for (int i = 0; i < rest.length(); i++) {
                String next = soFar + rest.charAt(i);
                String remaining = rest.substring(0, i) + rest.substring(i+1);
                RecPermute(next, remaining);
            }
        }
    }

    // Attempt 1 : Doesn't ignore dupe permutations.
    public static int Permute15(ArrayList<Integer> soFar, ArrayList<Integer> rest) {
        int total = 0;
        int current = 0;
        int last = 10000;
        for (Integer i : soFar) {
            current = i;
            if (current > last) {
                return 0;
            } else {
                total += i;
            }
            last = current;
        }
        if (total == 15){
            System.out.println("Fifteen!: " + soFar.toString());
            return 2;
        } else if (total > 15) {
            System.out.println("Too much: " + soFar.toString());
            return 0;
        } else {
            if (rest.size() == 0) {
                System.out.println("No 15s: " + soFar.toString());
                return 0;
            } else {
                int score = 0;
                for (int i = 0; i < rest.size(); i++) {
                    ArrayList<Integer> next = (ArrayList<Integer>) soFar.clone();
                    next.add(rest.get(i));
                    ArrayList<Integer> remaining = new ArrayList<Integer>();
                    for (int j = 0; j < i; j++) {
                        remaining.add(rest.get(j));
                    }
                    for (int j = i+1; j < rest.size(); j++) {
                        remaining.add(rest.get(j));
                    }
                    score += Permute15(next, remaining);
                }
                return score;
            }
        }
    }

    // Attempt 2 : Works for many situations, but repeats calculation branches multiple times.
    public static int FindFifteens(int idx, int value, int[] ints) {
        int score = 0;
        int lastAdd = value;
        int prev = 0;

        for (int i = idx; i < ints.length; i++) {

            int curr = ints[i];
            int newAdd = lastAdd + curr;

            if (newAdd == 15) {
                System.out.println("Fifteen!");
                score += 2;
            }

            if (curr == prev && lastAdd < 15) {
                System.out.println("........New Fork........" + i);
                score += FindFifteens(i, lastAdd-curr, ints);
                System.out.println(".......Fork Ended......." + i);
            }
            if (newAdd < 15) {
                lastAdd = newAdd;
            }
            prev = curr;
            
        }
        return score;
    }

    // Attempt 3 : Courtesy of Sam Meldrum, 9/17/08 "Algorithm find which numbers from a list sum to another number"
    public static void Method1(int goal, int currentSum, ArrayList<Integer> included, 
        ArrayList<Integer> notIncluded, int startIndex) {
        
        for (int index = startIndex; index < notIncluded.size(); index++) {
            Integer nextValue = notIncluded.get(index);
            if (currentSum + nextValue == goal) {
                ArrayList<Integer> newResult = new ArrayList<Integer>(included);
                newResult.add(nextValue);
                method1.addAll(newResult);
            } else if (currentSum + nextValue < goal) {
                ArrayList<Integer> nextIncluded = new ArrayList<Integer>(included);
                nextIncluded.add(nextValue);
                ArrayList<Integer> nextNotIncluded = new ArrayList<Integer>(notIncluded);
                nextNotIncluded.remove(nextValue);
                Method1(goal, currentSum+nextValue, nextIncluded, nextNotIncluded, startIndex++);
            }
        }

    }

    public static ArrayList<Integer> RunMethod1(int goal, ArrayList<Integer> elements) {
        method1 = new ArrayList<Integer>();
        ArrayList<Integer> included = new ArrayList<Integer>();
        ArrayList<Integer> notIncluded = new ArrayList<Integer>(elements);
        Method1(goal, 0, included, notIncluded, 0);
        return method1;
    }

    public static int Fifteens(int[] ints) {
        int score = 0;
        for (int i = 0; i < ints.length; i++) {
            System.out.println("New check!");
            score += FindFifteens(i, 0, ints);
            System.out.println("Score="+score);
        }
        return score;
    }

    public static void main(String[] args) {
        ArrayList<Card> cards = new ArrayList<Card>();
        cards.add(new Card());
        Hand hand = new Hand();
        hand.DisplayCards();
        hand.Score();
        //System.out.println(FindFifteens(0, 0, hand1)); // 12
        //System.out.println(FindFifteens(0, 0, hand2)); // 16
        //System.out.println(FindFifteens(0, 0, hand3)); // 32
        /*int[] hand = {10,10,10,10,
                        10,10,10,10,
                        10,10,10,10,
                        10,10,10,10,
                        9,9,9,9,
                        8,8,8,8,
                        7,7,7,7,
                        6,6,6,6,
                        5,5,5,5,
                        4,4,4,4,
                        3,3,3,3,
                        2,2,2,2,
                        1,1,1,1};*/
    }
}