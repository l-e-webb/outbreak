package com.udacity.game_dev.outbreak;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.udacity.game_dev.outbreak.Game.GameManager;
import com.udacity.game_dev.outbreak.Game.GameScreen;


public class OutbreakGame extends Game {
	
	@Override
	public void create () {
		setStartScreen();
	}

	public void setStartScreen() {
        setScreen(new StartScreen(this));
    }

    public void setGameScreen(GameManager.Difficulty difficulty) {
        setScreen(new GameScreen(this, difficulty));
    }

}
