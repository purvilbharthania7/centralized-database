package User;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
public class UserRegistration {
    
    public static String encryptPassword(String input)
    {
        try {

            MessageDigest getMessageDigestInstance = MessageDigest.getInstance("SHA-1");
            byte[] messageDigest = getMessageDigestInstance.digest(input.getBytes());
            BigInteger bigInteger = new BigInteger(1, messageDigest);
            String hashtext = bigInteger.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
  
    
}

