package Controllers;

import MYSHOWS.Manage;
import MYSHOWS.Shows;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Paint;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import main.Getter;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;


/**
 * Created by symph on 09.07.2017.
 */
public class mainController {

    private int duration, position;
    private long percent;
    public static boolean correction=false;
    private String episode, season, show;
    private static Logger logger = Logger.getLogger(mainController.class.getName());
    private static FileHandler fh;

    @FXML
    private TextField shownameField,seasonField, episodeField;
    @FXML
    private MenuBar menubar;
    @FXML
    private ImageView showImage;
    @FXML
    private Image img;
    @FXML
    private WebView showInfo;
    @FXML
    private ToggleGroup userRating;
    @FXML
    private MenuItem menuButton_settings, menuButton_login, menuButton_close, menuButton_about;
    @FXML
    private Button correctionButton, correctionConfirm, correctionCancel;
    @FXML
    private Label sendStatus, labelSeries;


    @FXML
    private void clickCorrectionButton(){
        correction = true;
        shownameField.setEditable(true);
        seasonField.setEditable(true);
        episodeField.setEditable(true);
        correctionConfirm.setVisible(true);
        correctionCancel.setVisible(true);
        correctionButton.setVisible(false);
    }
    @FXML
    private void clickOnCorrectionConfirm(){
        correction = true;
        new Shows().searchByFile(formSearchString(shownameField.getText().replace(" ", "."), seasonField.getText(), episodeField.getText()));
        getShowData();
        shownameField.setEditable(false);
        seasonField.setEditable(false);
        episodeField.setEditable(false);
        correctionConfirm.setVisible(false);
        correctionCancel.setVisible(false);
        correctionButton.setVisible(true);
    }
    @FXML
    private void clickOnCorrectionCancel(){
        correction = false;
        shownameField.setEditable(false);
        seasonField.setEditable(false);
        episodeField.setEditable(false);
        shownameField.setText(show);
        seasonField.setText(season);
        episodeField.setText(episode);
        correctionConfirm.setVisible(false);
        correctionCancel.setVisible(false);
        correctionButton.setVisible(true);
    }
    @FXML
    private void clickSendButton(){
        String rating, showName, episodeId, season, episode;
        showName = shownameField.getText().replace(" ", ".");
        season = seasonField.getText();
        episode = episodeField.getText();
        /*Get user rating*/
        RadioButton chk = (RadioButton)userRating.getSelectedToggle();
        rating = chk.getText();
        /*Form MyShows query*/
        new Shows().searchByFile(formSearchString(showName, season, episode));
        episodeId = new Shows().getEpisodeId();

        System.out.println(episodeId);
        String rateResponce = new Manage().rateEpisode(episodeId,rating);
        switch(rateResponce){
            case "ok":
                sendStatus.setTextFill(Paint.valueOf("GREEN"));
                sendStatus.setText("Отправлено!");
                break;
            case "fail":
                sendStatus.setText("Ошибка");
                sendStatus.setTextFill(Paint.valueOf("RED"));
                break;
        }
        //System.out.println(episodeId);
        new Manage().rateEpisode(episodeId,rating);
    }

    ////////////////////////////
    public void updateShowData(){
        //new Getter().getData();

        show = new Getter().getShowname();
        season = new Getter().getSeason();
        episode = new Getter().getEpisode();

        shownameField.setText(show);
        seasonField.setText(season);
        episodeField.setText(episode);
        getShowData();
    }

    public void getShowData(){
        new Shows().getById(Shows.showId);
        System.out.println(Shows.showId);
        WebEngine webEngine = showInfo.getEngine();
        //String desc = new Shows().getDescription().replace("\\r","").replace("\\n","").replace("\"","");
        String poster = new Shows().getPoster();
        try {
            img = new Image(poster);
        } catch (RuntimeException e){logger(logger,"error",e.getStackTrace().toString());}
        //webEngine.loadContent(desc);
        showImage.setImage(img);
        centerImage();
    }

    private void centerImage() {
        Image img = showImage.getImage();
        if (img != null) {
            double w = 0;
            double h = 0;

            double ratioX = showImage.getFitWidth() / img.getWidth();
            double ratioY = showImage.getFitHeight() / img.getHeight();

            double reducCoeff = 0;
            if(ratioX >= ratioY) {
                reducCoeff = ratioY;
            } else {
                reducCoeff = ratioX;
            }

            w = img.getWidth() * reducCoeff;
            h = img.getHeight() * reducCoeff;

            showImage.setX((showImage.getFitWidth() - w) / 2);
            showImage.setY((showImage.getFitHeight() - h) / 2);

        }
    }

    /*Menubar actions*/
    @FXML
    private void menuButtonClose(){
        Stage stage = (Stage) menubar.getScene().getWindow();
        stage.close();
    }
    @FXML
    private void menuButtonAbout(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/aboutWindow.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            //Stage stageOld = (Stage) exit.getScene().getWindow();
            //stageOld.hide();
            Stage stage = new Stage();
            stage.setScene(new Scene(root1));
            stage.setResizable(false);
            stage.getIcons().add(new Image("images/star-icon.png"));
            stage.setTitle("О программе");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void menuButtonLogin(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/loginWindow.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            //Stage stageOld = (Stage) exit.getScene().getWindow();
            //stageOld.hide();
            Stage stage = new Stage();
            stage.setScene(new Scene(root1));
            stage.setResizable(false);
            stage.getIcons().add(new Image("images/star-icon.png"));
            stage.setTitle("Настройки входа");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void menuButtonSettings(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/settingsWindow.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            //Stage stageOld = (Stage) exit.getScene().getWindow();
            //stageOld.hide();
            Stage stage = new Stage();
            stage.setScene(new Scene(root1));
            stage.setResizable(false);
            stage.getIcons().add(new Image("images/star-icon.png"));
            stage.setTitle("Настройки");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String formSearchString(String showName, String seasonNumber, String episodeNumber){
        String searchQuery = showName;
        /*String searchQuery = showName.toLowerCase();
        searchQuery = Character.toUpperCase(searchQuery.charAt(0)) + searchQuery.substring(1);*/
        if (seasonNumber.length()>1 && !seasonNumber.startsWith("0")){
            searchQuery = searchQuery+".S"+seasonNumber;
        }else if (seasonNumber.length()==1){
            searchQuery = searchQuery+".S0"+seasonNumber;
        } /*else if(season.startsWith("0")){
            season.replaceFirst("0","");
            searchQuery = searchQuery+".S"+season;
        }*/
        if (episodeNumber.length()>1 && !episodeNumber.startsWith("0")){
            searchQuery = searchQuery+"E"+episodeNumber;
        }else if (episodeNumber.length()==1){
            searchQuery = searchQuery+"E0"+episodeNumber;
        } /*else if(season.startsWith("0")){
        season.replaceFirst("0","");
        searchQuery = searchQuery+"E"+season;
        }*/

        logger(logger,"info","Search query is: "+searchQuery);
        return searchQuery;
    }

    public static void logger(Logger logger, String level, String message) {
        //Logger logger = Logger.getLogger();
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

}
