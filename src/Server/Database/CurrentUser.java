package Server.Database;

import java.io.Serializable;

public class CurrentUser implements Serializable {

    private Credentials credentials;

    public CurrentUser(Credentials credentials) {
        this.credentials = credentials;
    }

    public void setCredentials(Credentials credentials) {
        this.credentials = credentials;
    }

    public Credentials getCredentials() {
        return credentials;
    }
}