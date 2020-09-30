import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import java.util.Collections;

public class Board extends JPanel {

    private Image block[];
    private Image breakFrames[];
    private Image waterFrames[];
    private String start = "images/startScreen.JPG" , playButton = "images/playButton.png", loadButton = "images/loadButton.png", saveButton = "images/saveButton.png";
    public static String[] level = new String[11];
    public static int field[][];//stores what block each space is
    private static Puffle puffle;
    private static dragImage dragPuffle, dragFinish, dragTeleport1, dragTeleport2, dragKey, dragLock; //icons dragged in sandbox
    public static String status = new String("menu");
    private static String dragging = "";
    public static String custom = String.join("", Collections.nCopies(15*19, new String("0")));
    public static int Level = 0;
    public static boolean hasKey = false;
    public static boolean sandboxPlay = false;

    public Board() {
    	initBoard();
    }

    private void initBoard() {
    	addKeyListener(new TAdapter());
    	addMouseListener(new MAdapter());
    	addMouseMotionListener(new MotionAdapter());
    	setFocusable(true);
    	puffle = new Puffle();
    	initLevels();
        loadImage();
        setField();

        dragPuffle = new dragImage("images/puffle.gif");
    	dragFinish = new dragImage("images/finish.JPG");
    	dragTeleport1 = new dragImage("images/teleport.gif");
    	dragTeleport2 = new dragImage("images/teleport.gif");
    	dragKey = new dragImage("images/keyTransparent.png");
    	dragLock = new dragImage("images/lock.png");
        dragPuffle.setX(30*6);
    	dragPuffle.setY(30*15);
    	dragFinish.setX(30*7);
    	dragFinish.setY(30*15);
    	dragTeleport1.setX(30*8);
    	dragTeleport1.setY(30*15);
    	dragTeleport2.setX(30*9);
    	dragTeleport2.setY(30*15);
    	dragKey.setX(30*10);
    	dragKey.setY(30*15);
    	dragLock.setX(30*11);
    	dragLock.setY(30*15);
    }

    public static void setField() {
    	hasKey = false;
    	field = new int[15][19];
    	if (status.equals("game")) {
    		for (int row = 0; row < 15; row++) {
        		for (int col = 0; col < 19; col++) {
        			if(level[Level].charAt(row * 19 + col) == 'P') {
        				field[row][col] = 1;
        				puffle.setX(30 * col);
        				puffle.setY(30 * row);
        			}
        			else {
        				field[row][col] = Character.getNumericValue(level[Level].charAt(row * 19 + col));
        			}
        		}
        	}
    	}
    	else if (status.equals("sandbox")) {
    		for (int row = 0; row < 15; row++) {
        		for (int col = 0; col < 19; col++) {
        			if (custom.charAt(row * 19 + col) == 'P') {
        				field[row][col] = 1;
        				puffle.setX(30 * col);
        				puffle.setY(30 * row);
        				dragPuffle.setX(30 * col);
        				dragPuffle.setY(30 * row);
        			} else {
        				field[row][col] = Character.getNumericValue(custom.charAt(row * 19 + col));
        			}

        			if (custom.charAt(row * 19 + col) == '3') {
        				if (dragTeleport1.getX() == 30*7 && dragTeleport1.getY() == 30*15) {
        					dragTeleport1.setX(30 * col);
        					dragTeleport1.setY(30 * row);
        				} else {
        					dragTeleport2.setX(30 * col);
        					dragTeleport2.setY(30 * row);
        				}
        			} else if (custom.charAt(row * 19 + col) == '4') {
    					dragFinish.setX(30 * col);
    					dragFinish.setY(30 * row);
        			} else if (custom.charAt(row * 19 + col) == '5') {
    					dragKey.setX(30 * col);
    					dragKey.setY(30 * row);
        			} else if (custom.charAt(row * 19 + col) == '6') {
    					dragLock.setX(30 * col);
    					dragLock.setY(30 * row);
        			}


        		}
        	}
    	}



    }

    private void loadImage() {

    	block = new Image[11];
    	ImageIcon ii = new ImageIcon("images/background.png");
        block[0] = ii.getImage();
        ii = new ImageIcon("images/thinIce.png");
        block[1] = ii.getImage(); block[10] = ii.getImage();//prevent sandbox overlap
        ii = new ImageIcon("images/thickIce.png");
        block[2] = ii.getImage();
        ii = new ImageIcon("images/teleport.gif");
        block[3] = ii.getImage();
        ii = new ImageIcon("images/finish.JPG");
        block[4] = ii.getImage();
        ii = new ImageIcon("images/key.png");
        block[5] = ii.getImage();
        ii = new ImageIcon("images/lock.png");
        block[6] = ii.getImage();
        ii = new ImageIcon("images/usedTeleport.JPG");
        block[7] = ii.getImage();
        ii = new ImageIcon("images/swirl.gif");
        block[8] = ii.getImage();
        ii = new ImageIcon("images/ice.png");
        block[9] = ii.getImage();

        waterFrames = new Image[17];
        for (int i = 0; i < 17; i++) {
        	ii = new ImageIcon("images/waterFrames/frame_" + i + "_delay-0.1s.gif");
        	waterFrames[i] = ii.getImage();
        }

        breakFrames = new Image[20];
        for (int i = 0; i < 20; i++) {
        	ii = new ImageIcon("images/breakFrames/frame_" + i + "_delay-0.1s.gif");
        	breakFrames[i] = ii.getImage();
        }

    }


    @Override
    public void paintComponent(Graphics g) {
    	String s;
    	super.paintComponent(g);
    	if (status.equals("game") || status.equals("sandbox")) {

        	for (int row = 0; row < 15; row++) {
        		for (int col = 0; col < 19; col++) {
        			if(field[row][col] < 100) {
        				g.drawImage(block[field[row][col]], 30*col, 30*row, 30, 30, this);
        			}
        			else if(field[row][col] < 200) {
        				g.drawImage(breakFrames[(field[row][col]-100)/3], 30*col, 30*row, 30, 30, this);
        				field[row][col]++;
        				if (field[row][col] == 139) {
        					field[row][col] = 200;
        				}
        			}
        			else {
        				g.drawImage(waterFrames[(field[row][col]-200)/3], 30*col, 30*row, 30, 30, this);
        				field[row][col]++;
        				if (field[row][col] == 251) {
        					field[row][col] = 200;
        				}
        			}
        		}
        	}
        	if (status.equals("game")) {
        		s = "Level: " + Level + " | Q - quit | R - reset | , - previous level | . - next level" +
            			"         Try to melt all the ice!";
            	g.setFont(new Font("Arial", Font.BOLD, 11));
            	g.drawString(s, 5, 470);
        	}
        	if (status.equals("game") || sandboxPlay) {
        		if (sandboxPlay) {
        			s = "Q - quit to menu | R - reset level | D - done playing level | C - clear entire level |";
                	g.setFont(new Font("Arial", Font.BOLD, 11));
                	g.drawString(s, 5, 470);
                	if (!drown()) {
                		g.drawImage(puffle.getImage(), puffle.getX(), puffle.getY(), 30, 30, this);
                	}

        		}
        		if (drown()) {
            		field[puffle.getY()/30][puffle.getX()/30] = 8;
                  if (sandboxPlay) {
                      setField();
                  		sandboxPlay = false;
                  }
            	}
            	else if(field[puffle.getY()/30][puffle.getX()/30] == 4) {
            		if (!sandboxPlay) {
            			Level++;
            			if (Level == 11) {
            				status = "menu";
                    		sandboxPlay = false;
                    		Level = 0;
                    		setField();
            			}
            			setField();
            		} else {
            			setField();
                		sandboxPlay = false;
            		}

            	}
            	else {
            		g.drawImage(puffle.getImage(), puffle.getX(), puffle.getY(), 30, 30, this);
            	}
        	} else if (status.equals("sandbox") && !sandboxPlay) {
        		s = "Q - Quit | C - Clear | Click/Drag";
            	g.setFont(new Font("Arial", Font.BOLD, 11));
            	g.drawString(s, 5, 470);
        		g.drawImage(dragPuffle.getImage(), dragPuffle.getX(), dragPuffle.getY(), 30, 30, this);
        		g.drawImage(dragFinish.getImage(), dragFinish.getX(), dragFinish.getY(), 30, 30, this);
        		g.drawImage(dragTeleport1.getImage(), dragTeleport1.getX(), dragTeleport1.getY(), 30, 30, this);
        		g.drawImage(dragTeleport2.getImage(), dragTeleport2.getX(), dragTeleport2.getY(), 30, 30, this);
        		g.drawImage(dragLock.getImage(), dragLock.getX(), dragLock.getY(), 30, 30, this);
        		g.drawImage(dragKey.getImage(), dragKey.getX(), dragKey.getY(), 30, 30, this);

        		ImageIcon ii = new ImageIcon(playButton);
                Image image = ii.getImage();
        		g.drawImage(image, 365, 456, 60, 20, this);
        		ii = new ImageIcon(loadButton);
                image = ii.getImage();
        		g.drawImage(image, 430, 456, 60, 20, this);
        		ii = new ImageIcon(saveButton);
                image = ii.getImage();
        		g.drawImage(image, 495, 456, 60, 20, this);
        	}
    	}
    	else {
    		ImageIcon ii = new ImageIcon(start);
            Image image = ii.getImage();
    		g.drawImage(image, 0, 0, 30*19, 30*16, this);
    		s = "Press S for sandbox mode!";
        	g.setFont(new Font("Arial", Font.BOLD, 13));
        	g.drawString(s, 190, 420);
    	}

    }

    private class TAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
        	if (status.equals("game") || sandboxPlay) {
        		puffle.keyPressed(e);
        	}
        	if(e.getKeyChar() == 'r') {
        		setField();
        	}
        	if(e.getKeyChar() == 'q') {
        		status = "menu";
        		sandboxPlay = false;
        		Level = 0;
        		setField();
        	}
        	if(e.getKeyChar() == 's') {
        		status = "sandbox";

        		setField();
        		repaint();
        	}
        	if(e.getKeyChar() == 'c' && status.equals("sandbox")) {
        		sandboxPlay = false;
        		custom = String.join("", Collections.nCopies(15*19, new String("0")));
        		dragPuffle.setX(30*6);
            	dragPuffle.setY(30*15);
            	dragFinish.setX(30*7);
            	dragFinish.setY(30*15);
            	dragTeleport1.setX(30*8);
            	dragTeleport1.setY(30*15);
            	dragTeleport2.setX(30*9);
            	dragTeleport2.setY(30*15);
            	dragKey.setX(30*10);
            	dragKey.setY(30*15);
            	dragLock.setX(30*11);
            	dragLock.setY(30*15);
        		setField();
        		repaint();
        	}
        	if(e.getKeyChar() == 'p' && status.equals("sandbox") && custom.contains("P") && custom.contains("4")) {
    			sandboxPlay = true;
    			setField();
        		repaint();
        	}

        	if(e.getKeyChar() == 'd') {
        		if (sandboxPlay) {
        			setField();
            		sandboxPlay = false;
        		}
        	}
        }
    }

    private class MAdapter extends MouseAdapter {
    	@Override
    	public void mouseClicked(MouseEvent e) {
    		//System.out.println("x " + e.getX() + " y " + e.getY());
		    if (status.equals("menu") && e.getX() > 226 && e.getX() < 343 && e.getY() > 426 && e.getY() < 457) {
		    	status = "game";
		    	start = "images/startScreen.JPG";
		    	setField();
    			repaint();
			}
		    if (status.equals("sandbox") && !sandboxPlay) {
		    	if (e.getY()/30 != 0 && e.getY()/30 < 14 && e.getX()/30 != 0 && e.getX()/30 < 18) {
		    		if (field[e.getY()/30][e.getX()/30] == 1) {
		    			field[e.getY()/30][e.getX()/30] = 2;
		    		}
		    		else if (field[e.getY()/30][e.getX()/30] == 2) {
		    			field[e.getY()/30][e.getX()/30] = 9;
		    		}
		    		else {
		    			field[e.getY()/30][e.getX()/30] = 1;
		    		}
			    	for (int y = -1; y <= 1; y++) {
			    		for (int x = -1; x <= 1; x++) {
			    			if (field[e.getY()/30 + y][e.getX()/30 + x] == 0 && !(x == 0 && y == 0)) {
					    		field[e.getY()/30 + y][e.getX()/30 + x] = 9;
					    	}
			    		}
			    	}
			    	repaint();
			    	setCustom();
		    	}

		    }
    	}

    	@Override
    	public void mousePressed(MouseEvent e) {
    		if (status.equals("sandbox") && !sandboxPlay) {
    			if (e.getX() > dragPuffle.getX() && e.getX() < dragPuffle.getX() + 30
    					&& e.getY() > dragPuffle.getY() && e.getY() < dragPuffle.getY() + 30) {
    				dragging = "puffle";
    				if (e.getY() / 30 < 15 && e.getX() / 30 < 19) {
        				field[e.getY() / 30][e.getX() / 30] = 1;
        			}
    			} else if (e.getX() > dragFinish.getX() && e.getX() < dragFinish.getX() + 30
    					&& e.getY() > dragFinish.getY() && e.getY() < dragFinish.getY() + 30) {
    				dragging = "finish";
    				if (e.getY() / 30 < 15 && e.getX() / 30 < 19) {
        				field[e.getY() / 30][e.getX() / 30] = 1;
        			}
    			} else if (e.getX() > dragTeleport1.getX() && e.getX() < dragTeleport1.getX() + 30
    					&& e.getY() > dragTeleport1.getY() && e.getY() < dragTeleport1.getY() + 30) {
    				dragging = "teleport1";
    				if (e.getY() / 30 < 15 && e.getX() / 30 < 19) {
        				field[e.getY() / 30][e.getX() / 30] = 1;
        			}
    			} else if (e.getX() > dragTeleport2.getX() && e.getX() < dragTeleport2.getX() + 30
    					&& e.getY() > dragTeleport2.getY() && e.getY() < dragTeleport2.getY() + 30) {
    				dragging = "teleport2";
    				if (e.getY() / 30 < 15 && e.getX() / 30 < 19) {
        				field[e.getY() / 30][e.getX() / 30] = 1;
        			}
    			} else if (e.getX() > dragKey.getX() && e.getX() < dragKey.getX() + 30
    					&& e.getY() > dragKey.getY() && e.getY() < dragKey.getY() + 30) {
    				dragging = "key";
    				if (e.getY() / 30 < 15 && e.getX() / 30 < 19) {
        				field[e.getY() / 30][e.getX() / 30] = 1;
        			}
    			} else if (e.getX() > dragLock.getX() && e.getX() < dragLock.getX() + 30
    					&& e.getY() > dragLock.getY() && e.getY() < dragLock.getY() + 30) {
    				dragging = "lock";
    				if (e.getY() / 30 < 15 && e.getX() / 30 < 19) {
        				field[e.getY() / 30][e.getX() / 30] = 1;
        			}
    			}

    			if (e.getX() > 366 && e.getX() < 424 && e.getY() > 457 && e.getY() < 476
    					&& status.equals("sandbox") && custom.contains("P") && custom.contains("4")) {
    				sandboxPlay = true;
        			setField();
            		repaint();
    			}

    			if (e.getX() > 431 && e.getX() < 489 && e.getY() > 457 && e.getY() < 476
    					&& status.equals("sandbox")) {
    				try {
    	        		String data = (String) Toolkit.getDefaultToolkit()
    	        				.getSystemClipboard().getData(DataFlavor.stringFlavor);
    		        	//System.out.println(data);
    		        	if (data.length() == 15*19) {
    		        		dragPuffle.setX(30*6);
    		            	dragPuffle.setY(30*15);
    		            	dragFinish.setX(30*7);
    		            	dragFinish.setY(30*15);
    		            	dragTeleport1.setX(30*8);
    		            	dragTeleport1.setY(30*15);
    		            	dragTeleport2.setX(30*9);
    		            	dragTeleport2.setY(30*15);
    		            	dragKey.setX(30*10);
    		            	dragKey.setY(30*15);
    		            	dragLock.setX(30*11);
    		            	dragLock.setY(30*15);
    		        		custom = data;
    		        		setField();
    		        		repaint();
    		        	}
    	        	}
    	        	catch (Exception ex) {
    	        		//to stop the errors
    	        	}
    			}
    			if (e.getX() > 496 && e.getX() < 554 && e.getY() > 457 && e.getY() < 476
    					&& status.equals("sandbox")) {
    				try {
    		        	StringSelection stringSelection = new StringSelection(custom);
    		        	Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
    		        	clpbrd.setContents(stringSelection, null);
    		        	//System.out.println(custom);
    	        	}
    	        	catch (Exception ex) {
    	        		//to stop the errors
    	        	}
    			}

    		}
    	}



    	@Override
    	public void mouseReleased(MouseEvent e) {
    		if (status.equals("sandbox")) {
    			if (dragging.equals("puffle")) {
    				if (e.getY()/30 > 0 && e.getY()/30 < 15 &&
    						e.getX()/30 > 0 && e.getX()/30 < 19 &&
    						field[e.getY()/30][e.getX()/30] == 1) {
    					dragPuffle.setX(e.getX()/30*30);
    					dragPuffle.setY(e.getY()/30*30);
    					field[e.getY()/30][e.getX()/30] = 10;
        			} else {
        				dragPuffle.setX(30*6);
        				dragPuffle.setY(30*15);
    				}
    			} else if (dragging.equals("finish")) {
    				if (e.getY()/30 > 0 && e.getY()/30 < 15 &&
    						e.getX()/30 > 0 && e.getX()/30 < 19 &&
    						field[e.getY()/30][e.getX()/30] == 1) {
    					dragFinish.setX(e.getX()/30*30);
    					dragFinish.setY(e.getY()/30*30);
    					field[e.getY()/30][e.getX()/30] = 10;
        			} else {
        				dragFinish.setX(30*7);
        				dragFinish.setY(30*15);
    				}
    			} else if (dragging.equals("teleport1")) {
    				if (e.getY()/30 > 0 && e.getY()/30 < 15 &&
    						e.getX()/30 > 0 && e.getX()/30 < 19 &&
    						field[e.getY()/30][e.getX()/30] == 1) {
    					dragTeleport1.setX(e.getX()/30*30);
    					dragTeleport1.setY(e.getY()/30*30);
    					field[e.getY()/30][e.getX()/30] = 10;
        			} else {
        				dragTeleport1.setX(30*8);
        				dragTeleport1.setY(30*15);
    				}
    			} else if (dragging.equals("teleport2")) {
    				if (e.getY()/30 > 0 && e.getY()/30 < 15 &&
    						e.getX()/30 > 0 && e.getX()/30 < 19 &&
    						field[e.getY()/30][e.getX()/30] == 1) {
    					dragTeleport2.setX(e.getX()/30*30);
    					dragTeleport2.setY(e.getY()/30*30);
    					field[e.getY()/30][e.getX()/30] = 10;
        			} else {
        				dragTeleport2.setX(30*9);
        				dragTeleport2.setY(30*15);
    				}
    			} else if (dragging.equals("key")) {
    				if (e.getY()/30 > 0 && e.getY()/30 < 15 &&
    						e.getX()/30 > 0 && e.getX()/30 < 19 &&
    						field[e.getY()/30][e.getX()/30] == 1) {
    					dragKey.setX(e.getX()/30*30);
    					dragKey.setY(e.getY()/30*30);
    					field[e.getY()/30][e.getX()/30] = 10;
        			} else {
        				dragKey.setX(30*10);
        				dragKey.setY(30*15);
    				}
    			} else if (dragging.equals("lock")) {
    				if (e.getY()/30 > 0 && e.getY()/30 < 15 &&
    						e.getX()/30 > 0 && e.getX()/30 < 19 &&
    						field[e.getY()/30][e.getX()/30] == 1) {
    					dragLock.setX(e.getX()/30*30);
    					dragLock.setY(e.getY()/30*30);
    					field[e.getY()/30][e.getX()/30] = 10;
        			} else {
        				dragLock.setX(30*11);
        				dragLock.setY(30*15);
    				}
    			}
    			setCustom();
    		}
    		dragging = "";
    	}
    }

    private class MotionAdapter extends MouseAdapter {
    	@Override
    	public void mouseMoved(MouseEvent e) {
    		if (status.equals("menu")) {
    			if (e.getX() > 226 && e.getX() < 343 && e.getY() > 426 && e.getY() < 457) {
    				start = "images/startScreenHover.JPG";
        			repaint();
    			}
    			else {
        			start = "images/startScreen.JPG";
        			repaint();
        		}
    		}
    		if (status.equals("sandbox")) {
    			if (e.getX() > 366 && e.getX() < 424 && e.getY() > 457 && e.getY() < 476) {
    		        playButton = "images/playButtonHover.png";
        			repaint();
    			}
    			else {
    		        playButton = "images/playButton.png";
        			repaint();
        		}

    			if (e.getX() > 431 && e.getX() < 489 && e.getY() > 457 && e.getY() < 476) {
    		        loadButton = "images/loadButtonHover.png";
        			repaint();
    			}
    			else {
    		        loadButton = "images/loadButton.png";
        			repaint();
        		}

    			if (e.getX() > 496 && e.getX() < 554 && e.getY() > 457 && e.getY() < 476) {
    		        saveButton = "images/saveButtonHover.png";
        			repaint();
    			}
    			else {
    		        saveButton = "images/saveButton.png";
        			repaint();
        		}
    		}
        }

    	@Override
    	public void mouseDragged(java.awt.event.MouseEvent e) {
            if (status.equals("sandbox")) {
            	dragImage temp = null;
            	if (dragging.equals("puffle")) {
            		temp = dragPuffle;
            	} else if (dragging.equals("finish")) {
            		temp = dragFinish;
            	} else if (dragging.equals("teleport1")) {
            		temp = dragTeleport1;
            	} else if (dragging.equals("teleport2")) {
            		temp = dragTeleport2;
            	} else if (dragging.equals("key")) {
            		temp = dragKey;
            	} else if (dragging.equals("lock")) {
            		temp = dragLock;
            	}
            	if(temp != null) {
            		temp.setX(e.getX() - 15);
                	temp.setY(e.getY() - 15);
            	}


            }
        }
    }

	private boolean drown() {
		if(field[puffle.getY()/30][puffle.getX()/30] != 4 &&
			((field[puffle.getY()/30+1][puffle.getX()/30] >= 6 &&
			field[puffle.getY()/30-1][puffle.getX()/30] >= 6 &&
			field[puffle.getY()/30][puffle.getX()/30+1] >= 6 &&
			field[puffle.getY()/30][puffle.getX()/30-1] >= 6 && !hasKey
			&& field[puffle.getY()/30][puffle.getX()/30] != 5) ||
			(field[puffle.getY()/30+1][puffle.getX()/30] > 6 &&
			field[puffle.getY()/30-1][puffle.getX()/30] > 6 &&
			field[puffle.getY()/30][puffle.getX()/30+1] > 6 &&
			field[puffle.getY()/30][puffle.getX()/30-1] > 6))) {
			return true;
		}
		return false;
	}

	private void setCustom() {
		custom = "";
		for (int row = 0; row < 15; row++) {
			for (int col = 0; col < 19; col++) {
				if (field[row][col] < 11) {
					if(dragPuffle.getY()/30 == row && dragPuffle.getX()/30 == col) {
						custom += 'P';
					} else if(dragFinish.getY()/30 == row && dragFinish.getX()/30 == col) {
						custom += '4';
					} else if(dragTeleport1.getY()/30 == row && dragTeleport1.getX()/30 == col) {
						custom += '3';
					} else if(dragTeleport2.getY()/30 == row && dragTeleport2.getX()/30 == col) {
						custom += '3';
					} else if(dragKey.getY()/30 == row && dragKey.getX()/30 == col) {
						custom += '5';
					} else if(dragLock.getY()/30 == row && dragLock.getX()/30 == col) {
						custom += '6';
					} else {
						custom += field[row][col];
					}
				} else {
					custom += '1';
				}

			}
		}
	}

	 private void initLevels() {
		 	level[0] = "0000000000000000000"
		   			 + "0000000000000000000"
		   			 + "0000000000000000000"
		   			 + "0000000000000000000"
		   			 + "0000000000000000000"
		   			 + "0000000000000000000"
		   			 + "0099900000000000000"
		   			 + "009P900000000000000"
		   			 + "0091900000000000000"
		   			 + "0094900000000000000"
		   			 + "0099900000000000000"
		   			 + "0000000000000000000"
		   			 + "0000000000000000000"
		   			 + "0000000000000000000"
		   			 + "0000000000000000000";

	    	level[1] = "0000000000000000000"
		   			 + "0000000000000000000"
		   			 + "0000000000000000000"
		   			 + "0000000000000000000"
		   			 + "0000000000000000000"
		   			 + "0000000000000000000"
		   			 + "0099900000000000000"
		   			 + "009P900000000000000"
		   			 + "0091909999990999000"
		   			 + "0091999111190949000"
		   			 + "0091111199199919000"
		   			 + "0099999999111119000"
		   			 + "0000000009999999000"
		   			 + "0000000000000000000"
		   			 + "0000000000000000000";

	    	level[2] = "0000000000000000000"
		   			 + "0000000000000000000"
		   			 + "0000000000000000000"
		   			 + "0000000000000000000"
		   			 + "0000000000000000000"
		   			 + "0000000000000000000"
		   			 + "0099900000000000000"
		   			 + "0094900000000999000"
		   			 + "00919099990009P9000"
		   			 + "0091999119999919000"
		   			 + "0091111111111119000"
		   			 + "0091199991199119000"
		   			 + "0099990099999999000"
		   			 + "0000000000000000000"
		   			 + "0000000000000000000";

	    	level[3] = "0000000000000000000"
		   			 + "0999990000009999900"
		   			 + "0911199999999111900"
		   			 + "0919199111199191900"
		   			 + "0911111111111111900"
		   			 + "0991999911999919900"
		   			 + "0091909111190919000"
		   			 + "009P909111190949000"
		   			 + "0099909999990999000"
		   			 + "0000000000000000000"
		   			 + "0000000000000000000"
		   			 + "0000000000000000000"
		   			 + "0000000000000000000"
		   			 + "0000000000000000000"
		   			 + "0000000000000000000";

	    	level[4] = "0000000000000000000"
		   			 + "0000000000000000000"
		   			 + "0999999999999999000"
		   			 + "0911111111111119000"
		   			 + "0919999999999919999"
		   			 + "0919994111111121119"
		   			 + "0919999999999919919"
		   			 + "09199999999999P9919"
		   			 + "0919999999999999919"
		   			 + "0919999119911119919"
		   			 + "0911111111111111119"
		   			 + "0999991111119999999"
		   			 + "0000099999999000000"
		   			 + "0000000000000000000"
		   			 + "0000000000000000000";

	    	level[5] = "000000000000000000000000000000000000000009999999999990000000911111111159000099991999999991999999P11261111111112149999929999999999111900092222222221111190009999999999999999000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000";
	    	level[6] = "0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000099999900000999900009P1139000009359000099119900000991900000911999999999190000091194611111119000009999999999999900000000000000000000000000000000000000000000000000000000000";
	    	level[7] = "99999999999999999909511111122222111P909199991919999919990911113222900091900099999999190009190000000000919000919000000000091900091900000000009190009190000000000919000919000000000091900091900000000009190009190000000000919000919000999999991999991999994111116222221111399999999999999999999";
	    	level[8] = "00000000000000000000000000000000000000000000000000000000000000000000000000000099999999999900000009P112221111900000009999191999190000000911119199919000000091999919991900000009199991119190000000919119312119000000091111564111900000009111913911190000000911199999119000000099999999999900000";
	    	level[9] = "09999999999999999990951111111111111119091111111111111311909999111211111111190099919321111111919009P119121111111119009999911111111911900941191111111191190099969111111111119999992211191111191991112111119111111199199219111111111119919922111111111191991112199119911911199999999999999999999";
	    	level[10] = "99999999999999999999511111111111111119911122222222111121991111111111111112199111111999999111219911111199P992211219911111199199221111991111111111111111199111111111111111119911111199911119991991111111112222619199119911999111194919911992211911119991991111111111111111199999999999999999999";
	 	}

}
