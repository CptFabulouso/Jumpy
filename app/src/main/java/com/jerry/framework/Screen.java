package com.jerry.framework;

/*The last piece in the puzzle is the abstract class Screen. We make it an abstract class
 instead of an interface so that we can implement some bookkeeping. This way, we have
 to write less boilerplate code in the actual implementations of the abstract Screen class.
 Listing 3�9 shows the abstract Screen class.*/
public abstract class Screen {

	/*
	 * The constructor receives the Game instance and stores it in a final
	 * member that's accessible to all subclasses. Via this mechanism, we can
	 * achieve two things: 
	 * 
	 * We can get access to the low-level modules of the
	 * Game to play back audio, draw to the screen, get user input, and read and
	 * write files.
	 * 
	 * We can set a new current Screen by invoking Game.setScreen() when
	 * appropriate (for example, when a button is pressed that triggers a
	 * transition to a new screen).
	 * 
	 * The first point is pretty much obvious: our
	 * Screen implementation needs access to these modules so that it can
	 * actually do something meaningful, like rendering huge numbers of unicorns
	 * with rabies. 
	 * 
	 * The second point allows us to implement our screen
	 * transitions easily within the Screen instances themselves. Each Screen
	 * can decide when to transition to which other Screen based on its state
	 * (for example, when a menu button is pressed).
	 */
	protected final Game game;

	public Screen(Game game) {
		this.game = game;
	}

	//update the screen state and present it accordingly. The Game instance will call
	//them once in every iteration of the main loop.
	public abstract void update(float deltaTime);

	public abstract void present(float deltaTime);

	/*methods will be called when the game is
	*paused or resumed. This is again done by the Game instance and applied to the currently
	*active Screen.
	*/
	public abstract void pause();

	public abstract void backButton();
	
	public abstract void resume();

	/*The Screen.dispose() method will be called by the Game instance in case
	*Game.setScreen() is called. The Game instance will dispose of the current Screen via this
	*method and thereby give the Screen an opportunity to release all its system resources
	*(for example, graphical assets stored in Pixmaps) to make room for the new screen's resource
	*in memory. The call to the Screen.dispose() method is also the last opportunity
	*for a screen to make sure that any information that needs persistence is saved.
	*/
	public abstract void dispose();
}
