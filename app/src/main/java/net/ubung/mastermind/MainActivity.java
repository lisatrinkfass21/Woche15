package net.ubung.mastermind;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;


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

    private final String FILE = "Spielstand.txt";

    private Set<String> winGames = new TreeSet<>(new Comparator<String>() {
        @Override
        public int compare(String s1, String s2){


            s1 = s1.replace(" ", "");
            s2 = s2.replace(" ", "");

            String[] StringS1 = s1.split("\\|");
            String[] StringS2 = s2.split("\\|");


            if(StringS1[1].equals(StringS2[1])){
                try{
                SimpleDateFormat sdf = new SimpleDateFormat("mm:ss", Locale.GERMAN);
                Date date1 = sdf.parse(StringS1[2]);
                Date date2 = sdf.parse(StringS2[2]);

                return date1.compareTo(date2);
            } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            return StringS1[1].compareTo(StringS2[1]);
    }});
    long start = 0;
    long finish = 0;

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

        list = findViewById(R.id.myList);
        items.clear();
        bindAdapterToListView(list);
        StringBuilder sb = new StringBuilder();
        char x ;
        int num;
        for(int i = 0; i< this.codeLength; i++){
            num = (int) (Math.random()*alphabet.size()+1);
            x = alphabet.get(num-1);
            sb.append(x);
            if(!this.doubleAllowed){//wenn Elemente im Code nur einmal vorkommen soll
                alphabet.remove(num-1);
            }
        }
        this.randomCode = sb.toString();
        System.out.println(this.randomCode);

        start = System.currentTimeMillis();
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
        alphabet.clear();
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

            items.clear();
            try{
                FileInputStream fis = openFileInput(this.FILE);
                BufferedReader in = new BufferedReader(new InputStreamReader(fis));
                String line = in.readLine();
                while(line  != null){
                    if(line.contains("code")){
                       line = line.substring(6,(6+this.codeLength));
                        this.randomCode = line;
                    }else if(line.contains("offeneTipps")){
                        int pos = line.indexOf("/");
                        line = line.substring(13,( pos-1));
                        this.guessRounds = Integer.parseInt(line);
                    }else if(line.contains("guess")){
                        String input = in.readLine();
                        int pos = input.indexOf("/");
                        input = input.substring(11, (pos-1));


                        String res = in.readLine();
                         pos = res.indexOf("/");
                         res = res.substring(8, (pos-1));
                        items.add(input+" | "+res);
                        line = in.readLine();
                    }
                    line = in.readLine();

                }
                list = findViewById(R.id.myList);
                bindAdapterToListView(list);
                in.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        if(items.isEmpty()){
            Toast.makeText(getApplicationContext(), "kein offener Spielstand", Toast.LENGTH_LONG).show();
        }

    }

    public void onclickSave(View view){
        if(!items.isEmpty()){
            try{
                FileOutputStream fos = openFileOutput(FILE, MODE_PRIVATE );
                PrintWriter out = new PrintWriter(new OutputStreamWriter(fos));
                    out.println("<saveState>");
                    out.flush();
                    out.println("<code>"+this.randomCode+"</code");
                    out.flush();
                    out.println("<offeneTipps>"+guessRounds+"</offeneTipps>");
                    for(int i = 0; i< items.size(); i++){
                        String item = items.get(i).replace(" ","");
                        String[] it = item.split("\\|");
                        out.println("<guess"+(i+1)+">");
                        out.flush();
                        out.println("<userInput>"+it[0]+"</userInput>");
                        out.flush();
                        out.println("<result>"+it[1]+"</result>");
                        out.flush();
                        out.println("</guess"+(i+1)+">");
                        out.flush();
                    }
                out.println("</saveState>");
                out.flush();
                out.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
                start();
        }else{
            Toast.makeText(getApplicationContext(), "kein offener Spielstand", Toast.LENGTH_LONG).show();
        }

    }

    public void onclickSubmit(View view){
        EditText et = findViewById(R.id.nextguess);
        String text;
        if(this.guessRounds != 0){
            if(et.getText().toString()!=null && et.getText().toString().length()== this.codeLength){
                text = et.getText().toString();
                String correct = vergleiche(text);
                items.add(text + " | " + correct);
                list = findViewById(R.id.myList);
                bindAdapterToListView(list);
            }else{
                Toast.makeText(getApplicationContext(), "falsche / keine Eingabe", Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(getApplicationContext(), "keine freien Tipps mehr", Toast.LENGTH_LONG).show();
            items.add("NOT SOLVED: "+this.randomCode);
            list = findViewById(R.id.myList);
            bindAdapterToListView(list);
        }
        et.setText("");

    }



    private String vergleiche(String text){
        String perfect = this.correctPositionSign+this.correctPositionSign+this.correctPositionSign+this.correctPositionSign;
        String sb = "";
        for(int i = 0; i< this.codeLength; i++){
            if(text.charAt(i)==this.randomCode.charAt(i)){
                sb = this.correctPositionSign + sb;
            }else if(this.randomCode.contains(String.valueOf(text.charAt(i)))){
                sb= sb+this.correctCodeElementSign;
            }else if(!alphabet.contains(text.charAt(i))){
                Toast.makeText(getApplicationContext(), "1 Element gehört nicht zum Alphabet", Toast.LENGTH_LONG).show();
            }
        }
        this.guessRounds--;
        if(sb.equals(perfect)){
            finish = System.currentTimeMillis();
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy - HH:mm");
            String date = sdf.format(new Date());
            sdf = new SimpleDateFormat("mm:ss");
            String time = sdf.format(new Date(finish-start));
            String te = (date +" | "+ (12-guessRounds) + "Rounds | "+ time);
           // winGames.add(te);
            writeFile(te);
            this.guessRounds=0;
            Toast.makeText(getApplicationContext(), "Code erraten - Spiel zu Ende", Toast.LENGTH_LONG).show();
            return "SOLVED";

        }
        return sb;
    }

    private void writeFile(String text){
        String filename = "score.sc.txt";
        try{
            FileOutputStream fos = openFileOutput(filename, MODE_PRIVATE | MODE_APPEND);
            PrintWriter out = new PrintWriter(new OutputStreamWriter(fos));
            out.println(text);
            out.flush();
            out.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void readFile(){
        winGames.clear();
        try{
            FileInputStream fis = openFileInput("score.sc.txt");
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));
            String line;
            while((line = in.readLine()) != null){
                winGames.add(line);
            }
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

   //zusatz
    public void onclickScore(View view){
        list = findViewById(R.id.myList);
        readFile();
        if(!winGames.isEmpty()){
            items.clear();
            for (String game : winGames) {
                items.add(game);
            }
            bindAdapterToListView(list);
        }

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