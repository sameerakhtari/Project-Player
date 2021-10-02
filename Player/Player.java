/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Sameer Akhtari
 */
import java.io.*;
import java.util.*;
import jaco.mp3.player.MP3Player;

public class Player {
    
        static String path1="";
         static MP3Player player = new MP3Player();
         ListFrame lf=new ListFrame();
         
         File file;
Player()
{
   
    
   // playSet();
    
}

    private static ArrayList<String> pList = new ArrayList<>(); 
  
public void retrive(String path)
{
    path1=path;
    File folder = new File(path);
    
    File[] listOfFiles = folder.listFiles();
   
    
  
    
    for(int i = 0; i < listOfFiles.length; i++) 
    {
        if (listOfFiles[i].isFile()) 
        {
            String name=listOfFiles[i].getName();
                if(name.contains(".mp3"))
                {
                    
    		addList(listOfFiles[i].getName());
                
                }
        }
    }
    
    
    
      
    
 
   playSet(); 
 
}





public void playSet()
{
   int i=0;
  PlayerGUI p =new PlayerGUI(); */
 player=new MP3Player(new File(path1+"\\"+pList.get(7)));
  play();
    static String path2="";
         static MP3Player player1 = new MP3Player();
         ListFrame lff=new ListFrame();
    for(; i<pList.size();){
      
  
  
  
  while(!player.isStopped())
  {
      //do nothing
  }
  i++;
  } */
    lf.clearList();
   for(int i=0; i<pList.size(); i++){
       player.addToPlayList(new File(path1+"\\"+pList.get(i)));
       lf.setList(pList.get(i));
   }
   
   
  
    
    
    
}


public void addList(String name)
{
    pList.add(name);
}
public String getList(int index)
{
    
   return pList.get(index);
}
public void removeList(int index )
{
    pList.remove(index);
}

public void setAt(int index,int number)
{
    String temp=getList(index);
    removeList(index);
    String temp3;
    String temp2= getList(number-1);
        pList.set(number-1, temp);
    for (int i =number; i < pList.size(); i++) {
        temp3=getList(i);
         pList.set(i, temp2);
         temp2=temp3;
        
    }
    
//loop completed
  reload();
    
}
public void reload()
{
      PlayerGUI pg=new PlayerGUI();
    stop();
    if(player.isPaused())
    pg.pauseSet();
    MP3Player tempp=new MP3Player();
    player=tempp;
    playSet();
    pg.playSet();
    player.play();
}
public void delete()
{
     
     reload();
}

public void next()
{
   player.skipForward();
}
public void prev()
{
    player.skipBackward();
}
public void play()
{
    
    System.out.println("play-clicked");
   
   
    
         
          player.play();
          
    
    
    }
public void setRep(boolean x)
{
    player.setRepeat(x);
}
public void pause()
{
    player.pause();
    
}

 public void stop()
 {
     player.stop();
 }
 


















}
























    
