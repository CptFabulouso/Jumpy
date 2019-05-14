package com.jerry.framework;

public interface Game {

	public Input getInput();
	
	public FileIO getFileIO();
	
	public Graphics getGraphics();
	
	public Audio getAudio();
	
	/*The Game.setScreen() method allows us to set the current Screen of the Game. These
methods will be implemented once, along with all the internal thread creation, window
management, and main loop logic that will constantly ask the current screen to present
and update itself*/
	public void setScreen(Screen screen);
	
	//The Game.getCurrentScreen() method returns the currently active Screen.
	public Screen getCurrentScreen();
	
	public Screen getStartScreen();
	
}
