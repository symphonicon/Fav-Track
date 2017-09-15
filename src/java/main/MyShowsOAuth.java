package main;

import com.google.api.client.auth.oauth2.PasswordTokenRequest;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.auth.oauth2.TokenResponseException;
import com.google.api.client.http.BasicAuthentication;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;

import java.io.IOException;
import java.net.UnknownHostException;

/**
 * Created by symph on 09.07.2017.
 */
public class MyShowsOAuth {

    private static final String token = "https://myshows.me/oauth/token";
    private static String responseToken, error, errorResponse;


    public static String getToken(String username, String password)throws UnknownHostException{
        String clientId = new GetPropetries().getMyShowsClientId();
        String secret = new GetPropetries().getMyShowsSecret();
        String responceState=null;
        try {
            TokenResponse response =
                    new PasswordTokenRequest(new NetHttpTransport(), new JacksonFactory(),
                            new GenericUrl(token), username, password)
                            .setClientAuthentication(
                                    new BasicAuthentication(clientId, secret)).execute();
            System.out.println("Access token granted");
            responceState="ok";
            responseToken = response.getAccessToken();
        } catch (TokenResponseException e) {
            if (e.getDetails() != null) {
                System.err.println("Error: " + e.getDetails().getError());
                error = e.getDetails().getErrorDescription();
                if (e.getDetails().getErrorDescription() != null) {
                    System.err.println("Description: "+e.getDetails().getErrorDescription());
                    error = e.getDetails().getErrorDescription();
                }
                if (e.getDetails().getErrorUri() != null) {
                    System.err.println("ErrorURI "+e.getDetails().getErrorUri());
                    error = e.getDetails().getErrorUri();
                }
            } else {
                System.err.println("GetMessage: "+e.getMessage());
                error = e.getMessage();
            }
            responceState = "fail";
        } catch (IOException e) {
            responceState = "fail";
            e.printStackTrace();
        }
        return responceState;
    }

    public static String getResponse(){
        return responseToken;
    }

    public String getResponseNew(){
        return this.responseToken;
    }

    public static String errorHandle(){
        if (error != null) {
            switch (error) {
                case "401 Unauthorized":
                    errorResponse = "Неверный логин или пароль";
                    break;
                case "Missing parameters: \"username\" and \"password\" required":
                    errorResponse = "Неверный логин или пароль";
                    break;
/*                case "":
                    errorResponse = "OK";
                    break;*/
                default:
                    errorResponse = "Ошибка";
                    break;
            }
        } else {return errorResponse = "OK";}

        return errorResponse;
    }

}
