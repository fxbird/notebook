public class Main {
    public static void main(String[] args) {
       int[] distance={0,20,10,5};
       int count=6;//this is count to hop

        int sum=0;
        for (int i = 1  ; i<=count; i++) {
           sum+=distance[i%3];
       }

        System.out.println("total distance = "+sum);
    }


}
