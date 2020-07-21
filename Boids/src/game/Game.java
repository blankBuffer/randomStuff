package game;
import engine.main.*;
import engine.graphics.*;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import engine.graphics.mathObjects.Point;
import engine.graphics.mathObjects.Vector;
import engine.graphics.mathObjects.Angle;

public class Game extends Main{
	private static final long serialVersionUID = 1524306043384221391L;
	Graphics3d g3d = new Graphics3d();
    Camera camera = new Camera();
    BufferedImage imageForScene;
    
    
    Angle cameraAngle = new Angle(0,0,0);
    Point cameraPosition = new Point(0,0,0);
    
    Boid boids[] = new Boid[100];
    Form box = new Form();
    

    public Game(){
    	size(1000,700);
        updateRate(60);
		frameRate(60);
        imageForScene = Graphics3d.createBlankBufferedImage((int)(WIDTH/2),(int)(HEIGHT/2));
		//enableInterpolation();
		init();
		name("Boids 3d");
        run();
    }
    
    
    
    public void init() {
    	for(int i = 0;i<boids.length;i++) {
    		boids[i] = new Boid();
    	}
    	int w = Boid.mapWidth/2;
    	box.setColor(0, 200, 255);
    	box.setTintWeight(0.5);
    	box.addTriangle(-w, -w, w, -w, w, w, w, -w, w);
    	box.addTriangle(-w, w, w, w, w, w, w, -w, w);
    	
    	box.addTriangle(-w, -w, -w, -w, w, -w, w, -w, -w);
    	box.addTriangle(-w, w, -w, w, w, -w, w, -w, -w);
    	box.setTintWeight(0.25);
    	box.addTriangle(-w, -w, -w, -w, -w, w, w, -w, -w);
    	box.addTriangle(-w, -w, w, w, -w, w, w, -w, -w);
    	box.setTintWeight(0.8);
    	box.addTriangle(-w, w, -w, -w, w, w, w, w, -w);
    	box.addTriangle(-w, w, w, w, w, w, w, w, -w);
    	
    	
    }
	double r = 0;
	public void update(long currentTime){
		r+=0.02;
		
		camera.setAngle(cameraAngle);
		camera.setPosition(cameraPosition);
        for(Boid boid : boids) {
        	boid.update(boids);
        }
	}
	public void draw(Graphics2D g){
		g3d.backgroundColor(0, 50, 70);
		for(Boid b : boids) {
			b.render(camera);
		}
		box.addToScene(new Vector(0,0,0), camera);
        g.drawImage(g3d.render(camera,imageForScene),0,HEIGHT,WIDTH,-HEIGHT,null);

	}
	
	double step = 0.03;
	double limit = 0.5;
	Vector velocity = new Vector(0,0,0);
    
	public void input(KeyBoard keyBoard,Mouse mouse){
		
		
		Vector dir = new Vector(Math.sin(cameraAngle.getXRotation()),0,Math.cos(cameraAngle.getXRotation()) );
        if(keyBoard.W) {
        	dir.scaler(step);
        	velocity.add(dir);
        }
        if(keyBoard.S) {
        	dir.scaler(step);
        	dir.rotate(new Angle(Math.PI, 0, 0));
        	velocity.add(dir);
        }
        if(keyBoard.A) {
        	dir.scaler(step);
        	dir.rotate(new Angle(Math.PI/2, 0, 0));
        	velocity.add(dir);
        }
        if(keyBoard.D) {
        	dir.scaler(step);
        	dir.rotate(new Angle(-Math.PI/2, 0, 0));
        	velocity.add(dir);
        }
        if(keyBoard.E) velocity.scaler(0.80);
        
        cameraPosition.translate(velocity);
        
        if(keyBoard.LEFT) cameraAngle.shiftAngle(-0.1, 0, 0);
        if(keyBoard.RIGHT) cameraAngle.shiftAngle(0.1, 0, 0);
        
        if(keyBoard.UP) cameraAngle.shiftAngle(0,0.1, 0);
        if(keyBoard.DOWN) cameraAngle.shiftAngle(0,-0.1, 0);
        
        if(keyBoard.SPACE) cameraPosition.translate(new Vector(0,limit,0));
        if(keyBoard.SHIFT) cameraPosition.translate(new Vector(0,-limit,0));

	}
	
	public static void main(String[] args) {
		new Game();
	}
	
}