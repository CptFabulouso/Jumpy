package com.jerry.jumpy;

import android.util.Log;

import com.jerry.framework.FileIO;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Created by Pavel on 13. 12. 2014.
 */
public class Settings {
    public static boolean soundEnabled;
    public final static int[] highscores = new int[] {100,80,60,50,40,10};
    public final static String file = ".jumpy";

    public static void load(FileIO files){
        BufferedReader in = null;
        try{
            in = new BufferedReader(new InputStreamReader(files.readFile(file)));
            soundEnabled = Boolean.parseBoolean(in.readLine());
            for(int i = 0; i< 5;i++){
                highscores[i] = Integer.parseInt(in.readLine());
            }
        } catch (IOException e ){
            e.printStackTrace();
            Log.e("Jumpy", "Couldn't load Settings file ");
        } catch (NumberFormatException e){
            e.printStackTrace();
            Log.e("Jumpy", "Couldn't parse int while loading settings");
        } finally {
            try{
                if( in != null){
                    in.close();
                }
            } catch (IOException e){
                e.printStackTrace();
                Log.e("Jumpy","Couldn't close input stream loading Settings file");
            }
        }
    }

    public static void save(FileIO files){
        BufferedWriter out = null;
        try{
            out = new BufferedWriter(new OutputStreamWriter(files.writeFile(file)));
            out.write(Boolean.toString(soundEnabled));
            out.write("\n");
            for(int i = 0; i< 5;i++){
                out.write(Integer.toString(highscores[i]));
                out.write("\n");
            }
        } catch (IOException e){
            e.printStackTrace();
            Log.e("Jumpy", "Couldn't save Settings file");
        } finally {
                try {
                    if(out != null) {
                        out.close();
                    }
                } catch (IOException e){
                    e.printStackTrace();
                    Log.e("Jumpy", "Couldn't close output stream saving Settings file");
                }
            }
        }


    public static void addScore(int score) {
            for(int i = 0; i < 5; i++){
                if(highscores[i] < score){
                    for(int j = 4; j>i; j--){
                        highscores[j] = highscores[j-1];
                    }
                    highscores[i] = score;
                    break;
                }
            }


    }


}
