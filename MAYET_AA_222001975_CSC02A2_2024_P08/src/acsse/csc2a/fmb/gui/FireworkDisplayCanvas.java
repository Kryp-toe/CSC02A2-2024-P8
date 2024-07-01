/**
 * @author Mr J Orfao
 * @version P07
 * Custom FireworkDisplayCanvas that will Draw the FireworkLayout
 */
package acsse.csc2a.fmb.gui;

import java.util.ArrayList;

import acsse.csc2a.fmb.model.Entity;
import acsse.csc2a.fmb.model.Firework;
import acsse.csc2a.fmb.model.FireworkEntity;
import acsse.csc2a.fmb.model.FountainFirework;
import acsse.csc2a.fmb.model.RocketFirework;
import acsse.csc2a.fmb.particles.FireworkParticleSystem;
import acsse.csc2a.fmb.particles.FountainParticleSystem;
import acsse.csc2a.fmb.pattern.EntityVisitor;
import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class FireworkDisplayCanvas extends Canvas{
	// Attributes
	private ArrayList<FireworkEntity> entities = null;
	private static final int NUM_ROWS = 15;
	private static final int NUM_COLS = 15;
	private static final int CELL_SIZE = 50;
	private static final int SIZE = NUM_ROWS * CELL_SIZE;
	private EntityVisitor visitor = null;
	private FireworkParticleSystem fireworkParticleSystem = null;
	private FountainParticleSystem fountainParticleSystem = null;

	/**
	 * Default constructor to set the size of the canvas
	 */
	public FireworkDisplayCanvas() {
		setWidth(SIZE);
		setHeight(SIZE);

		this.fireworkParticleSystem = new FireworkParticleSystem();
		this.fountainParticleSystem = new FountainParticleSystem();

		// Create a Visitor to draw the FireworkEntities
		visitor = new EntityVisitor(SIZE, CELL_SIZE);
	}

	/**
	 * 
	 * Method to set the FireworkEntity array for drawing
	 * 
	 * @param fireworkEntities ArrayList of FireworkEntity objects
	 */
	public void setFireworkEntities(ArrayList<FireworkEntity> fireworkEntities) {
		this.entities = fireworkEntities;
		for (FireworkEntity fireworkEntity : fireworkEntities) {
			Firework firework = fireworkEntity.getFirework();
			if(firework instanceof RocketFirework) {
				this.fireworkParticleSystem.addFirework(fireworkEntity.getXLocation()*CELL_SIZE, SIZE - CELL_SIZE, (RocketFirework) firework);
			}else if (firework instanceof FountainFirework) {
				this.fountainParticleSystem.addFirework(fireworkEntity.getXLocation()*CELL_SIZE, SIZE - CELL_SIZE, (FountainFirework) firework);
			}
		}
		// Redraw the canvas to show the FireworkDisplayCanvas objects
		redrawCanvas();
	}

	/**
	 * Method to draw FireworkEntity objects on the canvas
	 */
	public void redrawCanvas() {
		// GraphicsContext
		GraphicsContext gc = getGraphicsContext2D();

		// provide the visitor with the context
		visitor.setGraphicsContext(gc);

		//fill a rect (size of the grid)
		gc.clearRect(0, 0, CELL_SIZE*15, CELL_SIZE*15);

		// Draw the grid
		gc.setStroke(Color.BLACK);
		// Begin drawing Grid
		for (int r = 0; r < 15; r++) {
			for (int c = 0; c < 15; c++) {
				// Draw Rectangle
				// you need to multiply the row and column by cell_size to get to the correct
				// pixel location
				gc.strokeRect(c * CELL_SIZE, r * CELL_SIZE, CELL_SIZE, CELL_SIZE);
			}
		}

		// Draw each FireworkEntity using the visitor
		for (Entity entity : entities) {
			entity.accept(visitor);
		}
	}

	public void startSimulation() {
		//color of fill
		getGraphicsContext2D().setFill(Color.BLACK);
		//fill a rect (size of the grid)
		getGraphicsContext2D().fillRect(0, 0, CELL_SIZE*15, CELL_SIZE*15);

		AnimationTimer animationTimer = new AnimationTimer() {

			@Override
			public void handle(long now) {
				double sec = now /1e9;
				fountainParticleSystem.updateAndShow(getGraphicsContext2D(), sec);
				fireworkParticleSystem.updateAndShow(getGraphicsContext2D(), sec);
			}
		};
		animationTimer.start();
	}
}