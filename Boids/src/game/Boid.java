package game;
import engine.graphics.mathObjects.*;
import engine.graphics.*;

public class Boid {
	
	static int mapWidth = 300;
	private Form boidForm = new Form();
	public Point position = new Point(Math.random()*mapWidth-mapWidth/2,Math.random()*mapWidth-mapWidth/2,Math.random()*mapWidth-mapWidth/2);
	public Vector direction = new Vector((Math.random()-0.5)*2,(Math.random()-0.5)*2,(Math.random()-0.5)*2);
	
	public Boid() {
		boidForm.setColor((int)(Math.random()*256.0),128,128);
		//bottom
		boidForm.clockDirection((byte)(-1));
		boidForm.setTintWeight(0.75);
		boidForm.addTriangle(-1, -1, -1, 0, 0, 2, 1, -1, -1);
		//top
		boidForm.clockDirection((byte)(1));
		boidForm.setTintWeight(1.25);
		boidForm.addTriangle(-1, 1, -1, 0, 0, 2, 1, 1, -1);
		//left
		boidForm.clockDirection((byte)(-1));
		boidForm.setTintWeight(1.0);
		boidForm.addTriangle(-1, -1, -1, -1, 1, -1, 0, 0, 2);
		//right
		boidForm.clockDirection((byte)(1));
		boidForm.addTriangle(1, -1, -1, 1, 1, -1, 0, 0, 2);
		//front
		boidForm.setTintWeight(0.8);
		boidForm.addTriangle(-1, -1, -1, -1, 1, -1, 1, -1, -1);
		boidForm.addTriangle(-1, 1, -1, 1, 1, -1, 1, -1, -1);
		
	}
	double a = 0;
	public void render(Camera c) {
		double mag = Math.sqrt(direction.getXTrans()*direction.getXTrans()+direction.getYTrans()*direction.getYTrans()+direction.getZTrans()*direction.getZTrans());
		double yDir = direction.getYTrans()/mag;
		a+=0.02;
		boidForm.addToScene(new Vector(position.x,position.y,position.z),new Angle(Math.atan2(direction.getZTrans(),direction.getXTrans())-Math.PI/2,-Math.asin(yDir),a), c);
	}
	
	public void update(Boid[] boids) {
		position.x+=direction.getXTrans();
		position.y+=direction.getYTrans();
		position.z+=direction.getZTrans();
		
		sep(boids);
		ali(boids);
		join(boids);
		
		if(position.x>mapWidth/2) {
			position.x = -mapWidth/2;
			
		}
		if(position.x<-mapWidth/2) {
			position.x = mapWidth/2;
			
		}
		if(position.z>mapWidth/2) {
			position.z = -mapWidth/2;
		}
		if(position.z<-mapWidth/2) {
			position.z = mapWidth/2;
		}
		if(position.y>mapWidth/2) {
			position.y=-mapWidth/2;
		}
		if(position.y<-mapWidth/2) {
			position.y=mapWidth/2;
		}
		
		direction.add(new Vector((Math.random()*2.0-1.0)*0.05,(Math.random()*2.0-1.0)*0.05,(Math.random()*2.0-1.0)*0.05));
		
		direction.scaler(1.01);
		
		double speed = direction.getMagnitude();
		if(speed>1.5) {
			direction.scaler(1.0/speed);
		}
		
		
	}
	
	void ali(Boid[] boids) {
		
		double radius = 40;
		int sum = 0;
		Vector result = new Vector(0,0,0);
		
		for(Boid b : boids) {
			if(b==this) {
				continue;
			}
			
			double dist = Point.dist(b.position, this.position);
			if(dist<radius) {
				sum++;
				result.add(b.direction);
			}
			
		}
		
		if(sum!=0) {
			
			double speed = direction.getMagnitude();
			double mag = result.getMagnitude();
			result.scaler((1.0/mag)*speed);
			result.scaler(3.0);
			
			Vector r = Vector.add(new Vector(-direction.getXTrans(),-direction.getYTrans(),-direction.getZTrans()), result);
			r.scaler(1.0/(60.0));
			direction.add(r);
			
		}
		
	}
	
	
	void join(Boid[] boids) {
		double radius = 45;
		int sum = 0;
		Point result = new Point(0,0,0);
		
		for(Boid b : boids) {
			if(b==this) {
				continue;
			}
			
			double dist = Point.dist(b.position, this.position);
			if(dist<radius&&dist>7) {
				sum++;
				result.x+=(b.position.x-position.x);
				result.y+=(b.position.y-position.y);
				result.z+=(b.position.z-position.z);
			}
			
		}
		
		if(sum!=0) {
			result.x/=sum;
			result.y/=sum;
			result.z/=sum;
			
			Vector res = new Vector(result.x,result.y,result.z);
			Vector r = Vector.add(new Vector(-direction.getXTrans(),-direction.getYTrans(),-direction.getZTrans()), res);
			r.scaler(1.0/(60*10));
			direction.add(r);
			
		}
	}
	
	void sep(Boid[] boids) {
		double radius = 15;
		int sum = 0;
		Point result = new Point(0,0,0);
		
		for(Boid b : boids) {
			if(b==this) {
				continue;
			}
			
			double dist = Point.dist(b.position, this.position);
			if(dist<radius) {
				sum++;
				result.x-=10.0/(b.position.x-position.x);
				result.y-=10.0/(b.position.y-position.y);
				result.z-=10.0/(b.position.z-position.z);
			}
			
		}
		
		if(sum!=0) {
			result.x/=sum;
			result.y/=sum;
			result.z/=sum;
			
			Vector res = new Vector(result.x,result.y,result.z);
			Vector r = Vector.add(new Vector(-direction.getXTrans(),-direction.getYTrans(),-direction.getZTrans()), res);
			r.scaler(1.0/(60*5));
			direction.add(r);
			
		}
	}
	
}
