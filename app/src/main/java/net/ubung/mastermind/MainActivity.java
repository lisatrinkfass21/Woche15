package net.ubung.mastermind;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
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
    String randomCode = null;

    private List<String> items = new ArrayList<>();
    private ListView list;
    private ArrayAdapter<String> adap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        start();
        list = findViewById(R.id.myList);
        bindAdapterToListView(list);


    }

    private void bindAdapterToListView(ListView lv){
        adap = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        lv.setAdapter(adap);
    }

    public void start(){
        InputStream in = getInputStreamForAsset("config.conf.txt");
        int counter = 0;
        BufferedReader bin = new BufferedReader(new InputStreamReader(in));
        String line;
        try{
            while((line = bin.readLine()) != null ){
                line = line.replace(" ", ""); //Leerzeichen entfernen
                String[] line2 = line.split("=");
                switch (line2[0]){
                    case "alphabet": getAlphabet(line2[1]); counter++;
                         break;
                    case "codeLength": getCodeLength(line2[1]); counter++; break;
                    case "doubleAllowed": getdoubleAllowed(line2[1]); counter++; break;
                    case "guessRounds": getGuessRounds(line2[1]); counter++; break;
                    case "correctPositionSign": getCorrecPositionSign(line2[1]); counter++; break;
                    case  "correctCodeElementSign": getCorrectElementSign(line2[1]); counter++; break;
                    default: break;
                }
                System.out.println("readLine");

            }
            if(counter != 6){
                Toast.makeText(getApplicationContext(), "Konfigurationsdatei nicht komplett", Toast.LENGTH_LONG).show();
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }




        //eigl logik:



        items.clear();
        bindAdapterToListView(list);
        StringBuilder sb = new StringBuilder();
        char x ;
        int num;
        for(int i = 0; i< this.codeLength; i++){
            num = (int) ((Math.random()+1)*alphabet.size());
            x = alphabet.get(num-1);
            sb.append(x);
        }
        this.randomCode = sb.toString();
    }

    private void getCorrectElementSign(String s) {
        this.correctCodeElementSign=s;
        System.out.println("correctalements");
    }

    private void getCorrecPositionSign(String s) {
            this.correctPositionSign=s ;
        System.out.println("correctposition");
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
            System.out.println("double");
        }catch (Exception e){
            Toast.makeText(getApplicationContext(), "doubleAllowed kein boolean", Toast.LENGTH_LONG).show();
        }
    }

    private void getCodeLength(String s) {
        try{
            this.codeLength = Integer.parseInt(s);
            System.out.println("codelength");
        }catch(Exception e){
            Toast.makeText(getApplicationContext(), "CodeLength keine Zahl", Toast.LENGTH_LONG).show();
        }
    }

    private void getAlphabet(String line) {
        System.out.println("alphabet");
        if(line != null && line.contains(",")){
            String[] alpha = line.split(",");
            for (String al : alpha) {
                if(al.length() == 1){
                    alphabet.add(al.charAt(0));
                }else{
                Toast.makeText(getApplicationContext(), "Konfigurationsdatei nicht korrekt /1", Toast.LENGTH_LONG).show();}
            }
        }else {
            Toast.makeText(getApplicationContext(), "Konfigurationsdatei nicht korrekt / 2", Toast.LENGTH_LONG).show();
        }
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
   //zusatz
    public void onclickScore(View view){

    }

    public void onclickShow(View view){
        items.clear();
      items.add("alphabet");
      items.add(this.alphabet.toString());
      items.add("codeLength");
      items.add(String.valueOf(this.codeLength));
      items.add("doubleAllowed");
      items.add(String.valueOf(this.doubleAllowed));
      items.add("guessRounds");
      items.add(String.valueOf(this.guessRounds));
      items.add("correctPositionSign");
      items.add(this.correctPositionSign);
      items.add("correctCodeElementSign");
      items.add(this.correctCodeElementSign);
      bindAdapterToListView(list);
    }


    public void onclickNewGame(View view){
        start();

    }
}