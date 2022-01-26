package sample;

import javafx.scene.control.Alert;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Obfuscation implements Items,Garbage{

    private String code;
    private static List<String> variables = new ArrayList<>();

    public Obfuscation(String code) {
        this.code = code;
    }

    public String getCode() {
       return code;
    }

    public static List<String> getVariables() {
        return variables;
    }

    public void delComments(){
        while (code.indexOf("//")>0){
            int index1 = code.indexOf("//");
            int index2 = code.indexOf("\n");
            if (index1>index2) {
                code = code.replaceFirst("\n","");
            }
            else {
                char[] buffCh = new char[index2-index1];
                code.getChars(index1, index2, buffCh, 0);
                String buffStr = new String(buffCh);
                code = code.replace(buffStr, "");
            }
        }
        while (code.indexOf("/*")>0){
            int index1 = code.indexOf("/*");
            int index2 = code.indexOf("*/");
            if (index1<index2) {
                char[] buffCh = new char[index2-index1+2];
                code.getChars(index1, index2+2, buffCh, 0);
                String buffStr = new String(buffCh);
                code = code.replace(buffStr, "");
            }
        }
    }

    public void delSpace(){
        for (int x=0;x<signs.length;x+=2){
            code = code.replaceAll(signs[x],signs[x+1]);
        }
    }

    public void getItemsToReplace(){
        for(int x = 0; x<items.length;x++){
            code = findAndReplaceItem(items[x], code);
        }
    }

    private String findAndReplaceItem(String item, String text) {
        int counter = 0;
        Pattern pattern = Pattern.compile(item);
        Matcher matcher = pattern.matcher(text);
        int [] indexesEnd = new int[50];
        while (matcher.find()) {
            indexesEnd[counter] = matcher.end();
            counter++;
        }
        String [] names = new String[counter];
        int indexOfItemEnd;
        int indexOfNameStart;
        int indexOfNameEnd;
        for (int i = 0; i < counter; i++) {
            indexOfItemEnd = indexesEnd[i];
            indexOfNameStart = indexOfItemEnd;
            indexOfNameEnd = text.indexOf(" ", indexOfNameStart);
            if (text.substring(indexOfNameStart, indexOfNameEnd).contains(";")){
                indexOfNameEnd = text.indexOf(";", indexOfNameStart);
            }
            if (text.substring(indexOfNameStart, indexOfNameEnd).contains(",")){
                indexOfNameEnd = text.indexOf(",", indexOfNameStart);
            }
            if (text.substring(indexOfNameStart, indexOfNameEnd).contains(")")){
                indexOfNameEnd = text.indexOf(")", indexOfNameStart);
            }
            if (text.substring(indexOfNameStart, indexOfNameEnd).contains("(")){
                indexOfNameEnd = text.indexOf("(", indexOfNameStart);
            }
            if (text.substring(indexOfNameStart, indexOfNameEnd).contains("=")){
                indexOfNameEnd = text.indexOf("=", indexOfNameStart);
            }
            if (text.substring(indexOfNameStart, indexOfNameEnd).contains("<")){
                indexOfNameEnd = text.indexOf("<", indexOfNameStart);
            }
            int sizeOfName = indexOfNameEnd - indexOfNameStart;
            if (sizeOfName != 1) {

                names[i] = text.substring(indexOfNameStart, indexOfNameEnd);
            } else {
                errorPrint("Name of variable is too short!");
            }
        }
        for (int i= 0; i<counter; i++) {
            if (names[i]!=null&&!names[i].contains("[]")&&!names[i].contains("(")&&!names[i].contains("[")&&!names[i].equals("")&&!names[i].equals("main")
                    &&!names[i].equals("args")&&!names[i].equals("")&&!names[i].equals("clone")&&!names[i].equals("toString")
                    &&!names[i].equals("equals")&&!names[i].equals("hashCode")&&!names[i].contains("{")&&!names[i].contains("\"")
                    &&!names[i].equals("length")) {
                String newName = genItemName();
                String buff = "";
                if (item.contains("class")){buff+="class ";}
                if (names[i].length()<5){
                    variables.add(buff + names[i]+"\t\t\t|  " + newName + "\n");
                } else if (names[i].length()>=5&&names[i].length()<11){
                    variables.add(buff + names[i]+"\t\t|  " + newName + "\n");
                }else if (names[i].length()>=11&&names[i].length()<16){
                    variables.add(buff + names[i]+"\t|  " + newName + "\n");
                } else variables.add(buff + names[i]+" |  " + newName + "\n");
                text = text.replaceAll(names[i], newName);
            }
        }
        return text;
    }

    private String genItemName(){
        String name = "";
        String randCh = "qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM";
        Random r = new Random();
        int size = 2 + r.nextInt(10);
        for (int i = 0; i<size;i++){
            int randNum = r.nextInt(randCh.length());
            name+=randCh.charAt(randNum);
        }
        return name;
    }

    protected void addGarbage(){
        Random r = new Random();
        int num = r.nextInt(3);
        List<Integer> repeats = new ArrayList<>();
        for (int i = 0;i<=num;i++){
            int index = r.nextInt(garbage.length-1);
            if (!repeats.contains(index)) {
                repeats.add(index);
                String garbageStr = garbage[index];
                int codeLastIndex = code.lastIndexOf("}");
                code = code.substring(0, codeLastIndex) + garbageStr + code.substring(codeLastIndex);
            } else i--;
        }
    }

    public static void errorPrint(String error){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("ERROR");
        alert.setContentText(error);
        alert.showAndWait();
    }
}