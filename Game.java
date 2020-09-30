import java.awt.Dimension;
import java.awt.EventQueue;
import javax.swing.JFrame;

public class Game extends JFrame {

public Game() {
	this.getContentPane().setPreferredSize(new Dimension(30*19, 30*16));
	this.pack();
	initUI();
}

private void initUI() {

	add(new Board());

	setTitle("Thin Ice");
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	setLocationRelativeTo(null);
}

public static void main(String[] args) {
	//System.setProperty("sun.java2d.uiScale","2"); // scale everything

	EventQueue.invokeLater(new Runnable() {

			@Override
			public void run() {
				Game ex = new Game();
				ex.setVisible(true);
			}
		});
}
}
