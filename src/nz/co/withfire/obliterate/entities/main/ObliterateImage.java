package nz.co.withfire.obliterate.entities.main;

import java.util.ArrayList;

import nz.co.withfire.obliterate.entities.Entity;
import nz.co.withfire.obliterate.graphics.drawable.shape2d.Quad2d;

public class ObliterateImage implements Entity {
	
	//VARIABLES
	//the image to obliterate
	private Quad2d image;
	
	//CONSTRUCTOR
	public ObliterateImage() {
		
		//TODO: texture image
		float[] imageCoord = {
			-0.5f,  0.5f, 0.0f,
			-0.5f, -0.5f, 0.0f,
			 0.5f, -0.5f, 0.0f,
			 0.5f,  0.5f, 0.0f
		};
		float[] imageColour = {
			0.6f, 0.05f, 0.15f, 1.0f,
			0.6f, 0.05f, 0.15f, 1.0f,
			0.6f, 0.05f, 0.15f, 1.0f,
			0.6f, 0.05f, 0.15f, 1.0f
		};
		
		image = new Quad2d(imageCoord, imageColour);
	}

	@Override
	public void collisionCheck(Entity other) {
		
		//TODO:
	}

	@Override
	public ArrayList<Entity> update() {
		
		//TODO:
		
		return null;
	}

	@Override
	public void draw(float[] mvpMatrix) {
		
		image.draw(mvpMatrix);
	}
}