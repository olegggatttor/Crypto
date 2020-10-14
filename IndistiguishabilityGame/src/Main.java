import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

    public static boolean findPeriod(int period, int[] counts) {
        for (int cur = 0; cur + period < counts.length; cur++) {
            if(counts[cur] != counts[cur + period]) {
                return false;
            }
        }
        return true;
    }
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int[] counts = new int[2000];
        for (int i = 0; i < 2000; i++) {
            int in = Integer.parseInt(br.readLine());
            int coolBit = 0;
            System.out.println(in ^ coolBit);
            String ans = br.readLine();
            counts[i] = ans.equals("YES") ? coolBit : 1 - coolBit;
        }
        int per = 2;
        for (int period = 2; period <= 1000; period++) {
            if(findPeriod(period, counts)) {
                per = period;
                break;
            }
        }
        for (int i = 2000; i < 10000; i++) {
            int in = Integer.parseInt(br.readLine());
            int coolBit = counts[i % per];
            System.out.println(in ^ coolBit);
            br.readLine();
        }
    }
}