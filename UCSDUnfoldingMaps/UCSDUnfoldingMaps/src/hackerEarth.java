import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigInteger;

public class hackerEarth {



    public static void main(String[] args) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        BigInteger res;
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                res = new BigInteger(0+"");

                String[] d = line.split(" ");
                for(String s: d){
                    BigInteger e = new BigInteger(s+"");
                    res = res.add(e);
                }
                System.out.println(res);
            }
        }catch (Exception er){

        }
    }
}
