package pl.wroc.pwr.student.acteditor;

import pl.wroc.pwr.student.acteditor.view.MainWindow;


public final class Application {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Application.run();
	}

	private static void run() {
		new MainWindow();
	}
}