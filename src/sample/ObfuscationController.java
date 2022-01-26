package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;

public class ObfuscationController {

    @FXML
    private TextArea codeArea, varsArea;
    @FXML
    private TextField passTextField;
    @FXML
    private Button showVarButton;

    @FXML
    void tangle(MouseEvent event) {
        String str = codeArea.getText();
        if (!str.equals("")) {
            Obfuscation obf = new Obfuscation(str);
            obf.delComments();
            obf.addGarbage();
            obf.getItemsToReplace();
            obf.delSpace();
            codeArea.setText(obf.getCode());
            showVarButton.setDisable(false);
        } else Obfuscation.errorPrint("Choose file");
    }

    @FXML
    void openFile(ActionEvent event) {
        String str = read();
        codeArea.setText(str);
    }

    @FXML
    void saveFile() {
        if (!codeArea.getText().equals("")) {
            saveFile(this.codeArea);
        } else Obfuscation.errorPrint("There is nothing to save");
    }

    @FXML
    void saveVars(MouseEvent event) {
        saveFile(varsArea);
    }

    @FXML
    void showVar() {
            Stage mainStage = new Stage();
            mainStage.setTitle("Ling");
            mainStage.getIcons().add(new Image("logo10.png"));
            mainStage.setScene(MainController.getSceneWin("variables.fxml"));
            mainStage.setResizable(false);
            mainStage.show();
            showVarButton.setDisable(true);
            mainStage.setOnCloseRequest(event ->{
                showVarButton.setDisable(false);
            });

    }

    @FXML
    void showList(MouseEvent event) {
        List<String> vars = Obfuscation.getVariables();
        System.out.println(vars);
        for (int i = 0; i<vars.size();i++) {
            varsArea.appendText(vars.get(i));
        }
    }


    @FXML
    void editCode(MouseEvent event) {
        codeArea.setEditable(true);
    }
    public static Scene getScene() {
        Parent root = loadFXML();
        assert root != null;
        return new Scene(root);
    }

    private static Parent loadFXML() {
        try {
            return FXMLLoader.load(MainController.class.getResource("obfuscationwin.fxml"));
        }
        catch (IOException e) {
            Obfuscation.errorPrint(e.getMessage());
            return new Pane();
        }
    }


    public String read() {
        String list = new String();
        FileChooser choose = new FileChooser();
        FileChooser.ExtensionFilter extFilter =
                new FileChooser.ExtensionFilter("IntelliJ IDEA Community Edition (*.java)", "*.java");
        choose.getExtensionFilters().add(extFilter);
        FileChooser.ExtensionFilter extFilter2 =
                new FileChooser.ExtensionFilter("TXT", "*.txt");
        choose.getExtensionFilters().add(extFilter2);
        File file = choose.showOpenDialog(null);
        Path path = file.toPath();
        passTextField.setText(path.toString());
        try (Scanner in = new Scanner (file)){
            while(in.hasNext()){
                list +=in.nextLine() + " " + "\n";
            }
        }catch(IOException ex){
            Obfuscation.errorPrint(ex.getMessage());
        }

        return list;
    }

    public void saveFile(TextArea textArea){
        FileChooser fileChooser = new FileChooser();

        FileChooser.ExtensionFilter extFilter =
                new FileChooser.ExtensionFilter("IntelliJ IDEA Community Edition (*.java)", "*.java");
        fileChooser.getExtensionFilters().add(extFilter);
        FileChooser.ExtensionFilter extFilter2 =
                new FileChooser.ExtensionFilter("TXT", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter2);

        File file = fileChooser.showSaveDialog(null);

        if(file != null){
            try{
                FileWriter fileWriter;
                fileWriter = new FileWriter(file);
                fileWriter.write(textArea.getText());
                fileWriter.close();
            } catch (IOException e)
            {
                Obfuscation.errorPrint(e.getMessage());
            }
        }
    }
}
