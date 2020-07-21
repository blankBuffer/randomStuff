int frameCount = 0;

int step;

int bp;
int mapWid;
int mapHei;
double tx, ty;
double[][] mapHeight;
double[][] trees;
boolean loaded = false;

void setup() {
  fullScreen();
  noStroke();

  step = 10;


  mapWid = width/step;
  mapHei = height/step+30;
  tx = Integer.MAX_VALUE/2;
  ty = Integer.MAX_VALUE/2;
  mapHeight = new double[mapWid][mapHei];
  trees = new double[mapWid][mapHei];

  loadMap();
}
RandomMap landHeightMap = new RandomMap();
RandomMap riverHeightMap = new RandomMap();
RandomMap detMap = new RandomMap();
RandomMap treeMap = new RandomMap();

double ctx, cty;

void draw() {
  if (!loaded) return;
  tx+=((double)(mouseX-width/2)/width)*8.0;
  ty+=((double)(mouseY-height/2)/height)*8.0;
  if (frameCount%20==0)loadMap();
  background(0);
  pushMatrix();
  translate((int)Math.ceil((ctx-tx)*step), (int)((cty-ty)*step));
  for (int i = 0; i<step*mapWid; i+=step) {
    for (int j = 0; j<step*mapHei; j+=step) {
      double val = mapHeight[i/step][j/step];
      double tree = trees[i/step][j/step];
      if (val>0.53) {
        if (tree>0.5) {
          fill(0, (int)(val*255.0), 0);
        } else {
          fill(0, (int)(val*150.0), 0);
        }
      } else if (val>0.5) {
        fill((int)(val*255.0), (int)(val*255.0), 0);
      } else {
        fill(0, 0, 64);
      }
      rect(i, (int)((double)j-val*step*bp)+bp*(step/2), step, step);
      if (bp!=0) {
        if (val>0.53) {
          if (tree>0.5) {
            fill(0, (int)(val*128.0), 0);
          } else {
            fill(0, (int)(val*70.0), 0);
          }
        } else if (val>0.5) {
          fill((int)(val*128.0), (int)(val*128.0), 0);
        } else {
          fill(0, 0,32);
        }
        rect(i, (int)((double)j-val*step*bp)+step+bp*(step/2), step, step*5);
      }
    }
  }
  popMatrix();

  if (mousePressed) {
    bp = 0;
  } else {
    bp = 100;
  }
  if (keyPressed) {
    if (key=='a') tx-=1;
    if (key=='d') tx+=1;

    if (key=='w') ty-=1;
    if (key=='s') ty+=1;
  }
  frameCount++;
  fill(255);
  text((int)tx+" "+(int)ty,100,100);
}
public void loadMap() {

  for (int i = -mapWid/2; i<mapWid/2; i++) {

    for (int j = -mapHei/2; j<mapHei/2; j++) {
      double val = landHeightMap.cloudRand(new int[]{i+(int)tx, j+(int)ty}, 128, 0);
      val-=amp((1.0-riverHeightMap.riverRand(new int[]{i+(int)tx, j+(int)ty}, 32, 2))-0.43, 16)
        *amp(detMap.cloudRand(new int[]{i+(int)tx, j+(int)ty}, 512, 0)*0.6, 16);
      double tree = treeMap.cloudRand(new int[]{i+(int)tx, j+(int)ty}, 32, 0);
      if (val>1.0) val = 1.0;
      if (val<0.5) val = 0.5;
      trees[i+mapWid/2][j+mapHei/2] = tree;
      mapHeight[i+mapWid/2][j+mapHei/2] = val;
    }
  }
  ctx = (int)tx;
  cty = (int)ty;
  loaded = true;
}

double amp(double x, double a) {
  return Math.atan((x-0.5)*a)/Math.PI+0.5;
}

class RandomMap {
  private int seed;
  public RandomMap() {
    setSeed((int)random(0, Integer.MAX_VALUE));
  }
  public void setSeed(int s) {
    seed = s;
    println(s);
  }

  public double rand(int[] pos) {
    int sum = 0;
    for (int i = 0; i<pos.length; i++) sum+=seedDepth(seed, i)*intPow(pos[i], 3);

    return Math.abs(sum%4095)/4096.0;
  }
  private int seedDepth(int seed, int n) {
    int c = seed;
    for (int i = 0; i<n; i++) c=(c*Integer.MAX_VALUE/2+11)%Integer.MAX_VALUE/2;
    return c;
  }
  private int intPow(int v, int n) {
    int ans = 1;
    for (int i = 0; i<n; i++) ans*=v;
    return ans;
  }
  public double smoothRand(int[] pos, int size) {
    int[] posR = new int[pos.length];
    double [] posP = new double[pos.length];
    for (int i = 0; i<pos.length; i++) {
      posR[i] = pos[i]/size;
      posP[i] = (1.0-(pos[i]%size)/((double)size));
    }
    double [] neerList = new double[intPow(2, pos.length)];
    for (int i = 0; i<neerList.length; i++) {
      int[] set = new int[pos.length];
      for (int j = 0; j<pos.length; j++) {
        int adder = 1;
        if (i%intPow(2, j+1)<intPow(2, j)) {
          adder=0;
        }
        set[j] = adder+posR[j];
      }
      neerList[i] = rand(set);
    }
    int c = 0;
    while (neerList.length!=1) {
      double[] nextList = new double[neerList.length/2];
      for (int i = 0; i<neerList.length-1; i+=2) {
        nextList[i/2] = neerList[i]*posP[c]+ neerList[i+1]*(1.0-posP[c]);
      }
      neerList = nextList;
      c++;
    }
    return neerList[0];
  }

  public double cloudRand(int[] pos, int size, int limit) {
    double sum = 0;
    double range = 0;
    int c = 1;
    while (size>limit) {
      sum+=smoothRand(pos, size)/c;
      range+=1.0/c;
      c*=2;
      size/=2;
    }
    return sum/range;
  }
  public double riverRand(int[] pos, int size, int limit) {
    return Math.abs(cloudRand(pos, size, limit)-0.5)*2.0;
  }
}
