package datatransfer;

public class RegisterRequest {
    private String username;
    private String password;
    private String email;
    private String vorname;
    private String nachname;
    private String passwordConfirmation;
    public RegisterRequest() {
    }

    public RegisterRequest(String vorname, String nachname, String username, String email, String password) {
        this.vorname = vorname;
        this.nachname = nachname;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    // Getters & setters
    public String getVorname() { return vorname; }
    public void setVorname(String vorname) { this.vorname = vorname; }

    public String getNachname() { return nachname; }
    public void setNachname(String nachname) { this.nachname = nachname; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
