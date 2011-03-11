package eit.searchengine.controller;

import eit.searchengine.core.Model;
import eit.searchengine.view.MainWindow;

public class Controller {
	private static Controller _instance = null;

	public static Controller getInstance() {
		return _instance;
	}

	public static Controller buildController(Model model) {
		if(_instance == null)
			_instance = new Controller(model);
		return _instance;
	}

	private Model _model = null;
	private MainWindow _view = null;

	private Controller(Model model) {
		_model = model;
	}

	public void setView(MainWindow mw) {
		_view = mw;
	}

	public MainWindow getView() {
		return _view;
	}

	public Model getModel() {
		return _model;
	}

	public void exitProgram() {
		boolean ok = true;

		if(_view != null)
			ok = closeView();
		if(ok && _model != null)
			ok = closeModel();
		if(ok)
			System.exit(0);
	}

	private boolean closeView() {
		return _view.closeView();
	}

	private boolean closeModel() {
		return _model.closeModel();
	}
}
