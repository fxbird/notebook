package test;


public class Main {
    public static void main(String[] args) {
        int[] distance = { 20, 10, 5};
        int[] counts = {4, 6, 3, 5};


        for (int count : counts) {
            int sum = 0;
            for (int i = 0; i <count; i++) {
                sum += distance[i>2?i-3:i];
            }

            System.out.println(sum);
        }

    }


}
