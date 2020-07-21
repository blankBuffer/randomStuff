package engine.main;
import java.io.*;
import javax.sound.sampled.*;
public class Sound{
    private File soundFile = null;
    private Clip clip = null;
    private boolean loaded = false;
    public Sound(String file){
        try{
            soundFile = new File("Resources/"+file);
        }catch(Exception e){
            System.err.println(e);
            System.exit(0);
        }
    }
    public void play(){
        if(!loaded)
            return;
        clip.start();
    }
    public void pause(){
        if(!loaded)
            return;
        clip.stop();
    }
    public void stop(){
        if(!loaded)
            return;
        clip.drain();
        try{
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
            clip = AudioSystem.getClip();
        }catch(Exception e){
            System.err.println(e);
            System.exit(0);
        }
    }
    public boolean isPlaying(){
        if(loaded){
            return clip.isActive();
        }else{
            return false;
        }
    }
    public void load(){
        try{
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
            clip = AudioSystem.getClip();
            clip.open(audioIn);
        }catch(Exception e){
            System.err.println(e);
            System.exit(0);
        }
        loaded = true;
    }
}