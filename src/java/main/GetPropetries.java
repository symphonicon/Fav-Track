package main;


import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Created by symph on 20.07.2017.
 */
public class GetPropetries {

    private String MyShowsClientId, MyShowsSecret;
    private String user_pref_player, user_login, user_password;
    private static int user_showtime;
    private boolean user_rememberMe,user_autologin;
    private String MPC_host, MPC_port;
    private String VLC_host, VLC_port, VLC_password, VLC_login;
    private static String propFileName = "user.properties", extFolder="./";
    private static Logger logger = Logger.getLogger(GetPropetries.class.getName());
    private static FileHandler fh;

    private static void copyFileUsingStream(File source, File dest) throws IOException {
        InputStream is = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(source);
            os = new FileOutputStream(dest);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        } finally {
            is.close();
            os.close();
        }
    }
    private static void copyFileUsingChannel(File source, File dest) throws IOException {
        FileChannel sourceChannel = null;
        FileChannel destChannel = null;
        try {
            sourceChannel = new FileInputStream(source).getChannel();
            destChannel = new FileOutputStream(dest).getChannel();
            destChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
        }finally{
            sourceChannel.close();
            destChannel.close();
        }
    }
    private static void copyFileUsingJava7Files(File source, File dest) throws IOException {
        Files.copy(source.toPath(), dest.toPath());
    }
    private static void copyFileUsingApacheCommonsIO(File source, File dest) throws IOException {
        FileUtils.copyFile(source, dest);
    }

    private void getSecretValues(){
        String secretConfigFile = "config.properties";
        try {
            PropertiesConfiguration secretConfig = new PropertiesConfiguration(secretConfigFile);
            if (secretConfig != null) {
                //Extract secret credentials
                MyShowsClientId = secretConfig.getString("MyShowsClientId");
                MyShowsSecret = secretConfig.getString("MyShowsSecret");
            } else {
                throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
            }
        } catch (Exception e) {
            logger(logger,"error","Exception: " + e);
        }
    }

    private void getUserValues(){
        try {
            PropertiesConfiguration config = new PropertiesConfiguration(extFolder+propFileName);
            user_pref_player = config.getString("user_pref_player");
            user_login = config.getString("user_login");
            user_password = config.getString("user_password");
            user_rememberMe = config.getBoolean("user_rememberMe");
            user_autologin = config.getBoolean("user_autologin");
            user_showtime = config.getInt("user_showtime");
            /*logger.info(config.getPath());*/
        } catch (Exception e) {
            System.out.println("Exception: " + e);
        }
    }
    private void getPlayerValues(){
        try {
            //getUserValues();
            PropertiesConfiguration config = new PropertiesConfiguration(extFolder+propFileName);
            MPC_host = config.getString("MPC_host");
            MPC_port = config.getString("MPC_port");
            VLC_host = config.getString("VLC_host");
            VLC_port = config.getString("VLC_port");
            VLC_password = config.getString("VLC_password");
            VLC_login = config.getString("VLC_login");
            /*//Extract player setup
            switch (user_pref_player){
                case "MPC":
                    MPC_host = config.getString("MPC_host");
                    MPC_port = config.getString("MPC_port");
                    break;
                case "VLC":
                    VLC_host = config.getString("VLC_host");
                    VLC_port = config.getString("VLC_port");
                    VLC_password = config.getString("VLC_password");
                    VLC_login = config.getString("VLC_login");

                    break;
            }*/

        } catch (Exception e) {
            System.out.println("Exception: " + e);
        }
    }
    private void setUserValues(String propName, String propValue){
        try {
            PropertiesConfiguration config = new PropertiesConfiguration(extFolder+propFileName);
            config.setProperty(propName, propValue);
            config.save();
            //logger.info(config.getPath());
        } catch (Exception e) {
            logger(logger,"error", e.getMessage());
        }
    }

    public static void logger(Logger logger, String level, String message) {
        try {
            fh = new FileHandler("./logging.log", true);
            logger.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        switch (level) {
            case "info":
                logger.info("\n"+message+"\n");
                fh.close();
                break;
            case "error":
                logger.severe("\n"+message+"\n");
                fh.close();
                break;
        }
    }

    //SECRET GETTERS
    public String getMyShowsClientId(){
        getSecretValues();
        return this.MyShowsClientId;
    }
    public String getMyShowsSecret(){
        getSecretValues();
        return this.MyShowsSecret;
    }

    //User prefs getters
    public String getUserPref_Player(){
        getUserValues();
        return this.user_pref_player;
    }
    public int getUserShowtime(){
        getUserValues();
        return this.user_showtime;
    }

    //Players setup getters
    public String getMPC_host(){
        getPlayerValues();
        return this.MPC_host;
    }
    public String getMPC_port(){
        getPlayerValues();
        return this.MPC_port;
    }
    public String getVLC_host(){
        getPlayerValues();
        return this.VLC_host;
    }
    public String getVLC_port(){
        getPlayerValues();
        return this.VLC_port;
    }
    public String getVLC_password(){
        getPlayerValues();
        return this.VLC_password;
    }
    public String getVLC_login(){
        getPlayerValues();
        return this.VLC_login;
    }

    //User login getters
    public String getUserLogin(){
        getUserValues();
        return this.user_login;
    }
    public String getUserPassword(){
        getUserValues();
        return this.user_password;
    }
    public boolean getRememberMe(){
        getUserValues();
        return this.user_rememberMe;
    }
    public boolean getAutoLogin(){
        getUserValues();
        return this.user_autologin;
    }


    /**
     USER PREFS SETTERS
     **/
    public void setUserLogin(String propValue){
        setUserValues("user_login", propValue);
    }
    public void setUserPassword(String propValue){
        setUserValues("user_password", propValue);
    }
    public boolean setRememberMe(String propValue){
        setUserValues("user_rememberMe", propValue);
        return this.user_rememberMe;
    }
    public boolean setAutoLogin(String propValue){
        setUserValues("user_autologin", propValue);
        return this.user_autologin;
    }
    public void setUserShowtime(String propValue){
        setUserValues("user_showtime", propValue);
    }

    /*Set player settings*/
    public void setUserPlayer(String propValue){
        setUserValues("user_pref_player", propValue);
    }
    public void setMPC_host(String propValue){
        setUserValues("MPC_host", propValue);
    }
    public void setMPC_port(String propValue){
        setUserValues("MPC_port", propValue);
    }
    public void setVLC_host(String propValue){
        setUserValues("VLC_host", propValue);
    }
    public void setVLC_port(String propValue){
        setUserValues("VLC_port", propValue);
    }
    public void setVLC_password(String propValue){
        setUserValues("VLC_password", propValue);
    }
    public void setVLC_login(String propValue){
        setUserValues("VLC_login", propValue);
    }

}
