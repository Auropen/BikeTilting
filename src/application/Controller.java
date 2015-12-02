package application;

import technical.DBHandler;

public class Controller implements IController {
	private static IController instance;
	private DBHandler dbHandler;

	private Controller() {
		this.dbHandler = DBHandler.getInstance();
	}

	public static IController getInstance() {
		if (instance == null) instance = new Controller();
		return instance;
	}
}
