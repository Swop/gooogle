package eit.searchengine;

import eit.searchengine.controller.Controller;
import eit.searchengine.core.Model;
import eit.searchengine.view.MainWindow;

public class SearchEngine {

    public static void main(String[] args) {
        Controller c = Controller.buildController(new Model());

		MainWindow w = MainWindow.getInstance();
		w.init();

		c.setView(w);
    }

}
