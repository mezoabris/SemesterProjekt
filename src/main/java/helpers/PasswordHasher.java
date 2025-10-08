package helpers;
import com.password4j.BcryptFunction;
import com.password4j.Hash;
import com.password4j.Password;
import com.password4j.types.Bcrypt;

public class PasswordHasher {

    public String hashPassword(String password) {
        BcryptFunction bcrypt = BcryptFunction.getInstance(Bcrypt.B, 12);

        Hash hash = Password.hash(password)
                .with(bcrypt);

        return hash.getResult(); // store this in DB
    }

    public boolean verifyPassword(String rawPassword, String hashedPassword) {
        BcryptFunction bcrypt = BcryptFunction.getInstance(Bcrypt.B, 12);

        return Password.check(rawPassword, hashedPassword)
                .with(bcrypt);
    }
}
