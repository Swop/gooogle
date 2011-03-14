package eit.searchengine;

import eit.searchengine.controller.Controller;
import eit.searchengine.core.Model;
import eit.searchengine.view.MainWindow;

public class SearchEngine {

    public static void main(String[] args) {
		if(args.length != 1) {
			System.out.println("Usage : SearchEngine path_to_treetager_install_dir");
			System.exit(1);
		}
        System.setProperty("treetagger.home", args[0]);

		Controller c = Controller.buildController(new Model());

		MainWindow w = MainWindow.getInstance();
		w.init();

		c.setView(w);
    }

}
