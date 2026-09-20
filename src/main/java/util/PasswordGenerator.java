package util;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordGenerator {

    public static void main(String[] args) {

        String password = "Admin@12345";

        String hash = BCrypt.hashpw(
                password,
                BCrypt.gensalt()
        );

        System.out.println("Password: " + password);
        System.out.println("BCrypt Hash: " + hash);
    }
}
