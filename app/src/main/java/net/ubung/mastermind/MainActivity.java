package net.ubung.mastermind;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<Character> alphabet = new ArrayList<>();
    int codeLength = 0;
    boolean doubleAllowed = true;
    int guessRounds = 0;
    String correctPositionSign = null;
    String correctCodeElementSign = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        start();


    }

    public void start(){
        InputStream in = getInputStreamForAsset("config.conf");
        int counter = 0;
        BufferedReader bin = new BufferedReader(new InputStreamReader(in));
        String line;
        try{
            while((line = bin.readLine()) != null && line.contains("=")){
                line = line.trim(); //Leerzeichen entfernen
                String[] line2 = line.split("=");
                switch (line2[0]){
                    case "alphabet": getAlphabet(line2[1]); counter++; break;
                    case "codeLength": getCodeLength(line2[1]); counter++; break;
                    case "doubleAllowed": getdoubleAllowed(line2[1]); counter++; break;
                    case "guessRounds": getGuessRounds(line2[1]); counter++; break;
                    case "correctPositionSign": getCorrecPositionSign(line2[1]); counter++; break;
                    case  "correctCodeElementSign": getCorrectElementSign(line2[1]); counter++; break;
                    default: break;
                }
            }
            if(counter != 6){
                Toast.makeText(getApplicationContext(), "Konfigurationsdatei nicht komplett", Toast.LENGTH_LONG).show();
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }




        //eigl logik:
    }

    private void getCorrectElementSign(String s) {
        this.correctCodeElementSign=s;
    }

    private void getCorrecPositionSign(String s) {
            this.correctPositionSign=s ;
    }

    private void getGuessRounds(String s) {try{
        this.guessRounds = Integer.parseInt(s);
    }catch(Exception e){
        Toast.makeText(getApplicationContext(), "GuessRounds keine Zahl", Toast.LENGTH_LONG).show();
    }
    }

    private void getdoubleAllowed(String s) {
        try{
            this.doubleAllowed = Boolean.parseBoolean(s);
        }catch (Exception e){
            Toast.makeText(getApplicationContext(), "doubleAllowed kein boolean", Toast.LENGTH_LONG).show();
        }
    }

    private void getCodeLength(String s) {
        try{
            this.codeLength = Integer.parseInt(s);
        }catch(Exception e){
            Toast.makeText(getApplicationContext(), "CodeLength keine Zahl", Toast.LENGTH_LONG).show();
        }
    }

    private void getAlphabet(String line) {
        if(line != null && line.contains(",")){
            String[] alpha = line.split(",");
            for (String al : alpha) {
                if(al.length() == 1){
                    alphabet.add(al.charAt(0));
                }
                Toast.makeText(getApplicationContext(), "Konfigurationsdatei nicht korrekt", Toast.LENGTH_LONG).show();
            }
        }
        Toast.makeText(getApplicationContext(), "Konfigurationsdatei nicht korrekt", Toast.LENGTH_LONG).show();
    }

    private InputStream getInputStreamForAsset(String filename){
        AssetManager assets = getAssets();

        try{
            return assets.open(filename);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    public void onclickLoad(View view){

    }

    public void onclickSave(View view){

    }

    public void onclickSubmit(View view){

    }

    public void onclickScore(View view){

    }

    public void onclickShow(View view){

    }
}