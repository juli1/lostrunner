package org.gunnm.lostrunner.graphics;

import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import org.gunnm.lostrunner.model.Game;

import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLU;
import android.util.Log;

public class LostRenderer implements Renderer {
	private static int mapWidth   	= 5;
	private static int mapHeight  	= 10;
	private static int nbCubes      = 5;
	private static FloatBuffer[][] gamePlate;
	private static FloatBuffer[][] cubes;
	private static boolean[] cubeActive;
	private float rotation = 0.0f;
	public static float camX = mapWidth;
	public static float camY = 4;
	public static float camZ = 3;
	public static float toviewX = mapWidth / 2;
	public static float toviewY = 0;
	public static float toviewZ = -1 * mapHeight / 2;
	
	
	private Game currentGame;
	
	public LostRenderer (Game g)
	{
		super ();
		this.currentGame = g;
	}
	private static float[][] cubeCoords;
	/*
	private static float[][] cubeCoords = new float[][] {
		new float[] { // top
			 1, 1,-1,
			-1, 1,-1,
			-1, 1, 1,
			 1, 1, 1
		},
		new float[] { // bottom
			 1,-1, 1,
			-1,-1, 1,
			-1,-1,-1,
			 1,-1,-1
		},
		new float[] { // front
			 1, 1, 1,
			-1, 1, 1,
			-1,-1, 1,
			 1,-1, 1
		},
		new float[] { // back
			 1,-1,-1,
			-1,-1,-1,
			-1, 1,-1,
			 1, 1,-1
		},
		new float[] { // left
			-1, 1, 1,
			-1, 1,-1,
			-1,-1,-1,
			-1,-1, 1
		},
		new float[] { // right
			 1, 1,-1,
			 1, 1, 1,
			 1,-1, 1,
			 1,-1,-1
		},
	};
	*/
	/*
	private static float[] cubeColors = new float[] {
		0,1,0,1,
		1,0.5f,0,1,
		1,0,0,1,
		1,1,0,1,
		0,0,1,1,
		1,0,1,1		
	};
	
	private static FloatBuffer pyramidVertexBfr;
	private static FloatBuffer pyramidColorBfr;
	private static FloatBuffer[] cubeVertexBfr;
	
	private static float pyramidRot;
	private static float cubeRot;
	*/
	private static FloatBuffer[] cubeVertexBfr;

	static
	{
		/*
		pyramidVertexBfr = FloatBuffer.wrap(pyramidCoords);
		pyramidColorBfr = FloatBuffer.wrap(pyramidColors);
		
		cubeVertexBfr = new FloatBuffer[6];
		for (int i = 0; i < 6; i++)
		{
			cubeVertexBfr[i] = FloatBuffer.wrap(cubeCoords[i]);
		}*/
	}

	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		gl.glShadeModel(GL10.GL_SMOOTH);
		gl.glClearColor(0, 0, 0, 0);

		gl.glClearDepthf(1.0f);
		gl.glEnable(GL10.GL_DEPTH_TEST);
		gl.glDepthFunc(GL10.GL_LEQUAL);
		
		gl.glEnable(GL10.GL_CULL_FACE);
		gl.glCullFace(GL10.GL_BACK);
		
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
		gamePlate = new FloatBuffer[mapWidth][mapHeight];
		cubes = new FloatBuffer[nbCubes][6];
		for (int i = 0 ; i < mapWidth ; i++)
		{
			for (int j = 0 ; j < mapHeight ; j++)
			{
				gamePlate[i][j] = FloatBuffer.wrap(new float[]{i    , 0 , -j,
						                                      i+1  ,  0 , -j,
						                                      i+1, 0, -j-1,
						                                      i, 0 , -j-1});
			}
		}
		for (int i = 0 ; i < nbCubes ; i++)
		{
			int xpos = i % mapWidth;
			int zpos = (int) Math.ceil( i / mapWidth);
			
			//bottom
			cubes[i][0] = FloatBuffer.wrap(new float[] {xpos    , 0 , -1 * mapHeight,
														xpos  ,  0 ,-1 *  mapHeight + 1,
														xpos + 1, 0,-1 *  mapHeight + 1 ,
														xpos + 1 , 0 , -1 * mapHeight});	
			//top
			cubes[i][1] = FloatBuffer.wrap(new float[] {xpos + 1   , 1 , -1 * mapHeight,
														xpos , 1, -1 * mapHeight,
														xpos   ,  1 ,-1 *  mapHeight + 1,
														xpos +1 , 1 , -1 * mapHeight + 1});
			//left
			cubes[i][2] = FloatBuffer.wrap(new float[] {xpos , 0 ,-1 *  mapHeight,
														xpos ,  0 , -1 * mapHeight + 1,
														xpos ,    1  , -1 *  mapHeight + 1,
														xpos ,  1  , -1 * mapHeight});
			//right
			cubes[i][3] = FloatBuffer.wrap(new float[] {xpos + 1  ,  0 , -1 * mapHeight,
														xpos + 1  ,  0 , -1 * mapHeight + 1,
														xpos + 1  ,  1 , -1 *  mapHeight + 1,
														xpos + 1  ,  1 , -1 *  mapHeight });
			//front
			cubes[i][4] = FloatBuffer.wrap(new float[] {xpos    , 0 ,-1 *  mapHeight + 1,
														xpos  +1 ,  0 , -1 * mapHeight + 1,
														xpos +1, 1, -1 * mapHeight + 1,
														xpos , 1 , -1 * mapHeight + 1});
			//rear
			cubes[i][5] = FloatBuffer.wrap(new float[] {xpos      , 0 , -1 *  mapHeight,
														xpos + 1  , 0 , -1 * mapHeight ,
														xpos + 1  , 1 , -1 * mapHeight ,
														xpos + 1  , 1 , -1 * mapHeight });		
		}
	}

	public void onDrawFrame(GL10 gl) {
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
		/*
		// draw pyramid
		gl.glTranslatef(-1.5f, 0, -6);
		gl.glRotatef(pyramidRot, 0, 1, 0);
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, pyramidVertexBfr);
		gl.glColorPointer(4, GL10.GL_FLOAT, 0, pyramidColorBfr);
		gl.glDrawArrays(GL10.GL_TRIANGLE_FAN, 0, 6);
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
		*/
		// draw cube
		GLU.gluLookAt(gl, camX , camY, camZ , 
						  toviewX, toviewY, toviewZ, 
						  0, 1, 0);
/*		
		gl.glTranslatef(0, -2, -20);
		gl.glRotatef(-20, 1, 0, 0);
		gl.glRotatef(-20, 0, 1, 0);
*/
		Log.d( "ANGLE" , "rotation" + rotation);
		//gl.glRotatef(cubeRot, 1, 1, 1);
		
		gl.glColor4f(1, 0, 0, 0.5f);
		for (int i = 0 ; i < mapWidth ; i++)
		{
			for (int j = 0 ; j < mapHeight ; j++)
			{
				gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
				gl.glVertexPointer(3, GL10.GL_FLOAT, 0, gamePlate[i][j]);
				gl.glDrawArrays(GL10.GL_TRIANGLE_FAN, 0, 4);
				gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
			}
		}
		
		for (int i = 0 ; i < currentGame.getCurrentMap().getNbCubes() ; i++)
		{
			cubeCoords = new float[][] {
					new float[] { // top
						currentGame.getCube(i).getX() + 1, 1, currentGame.getCube(i).getZ() + 0,
						currentGame.getCube(i).getX() + 0, 1, currentGame.getCube(i).getZ() + 0,
						currentGame.getCube(i).getX() + 0, 1, currentGame.getCube(i).getZ() + 1,
						currentGame.getCube(i).getX() + 1, 1, currentGame.getCube(i).getZ() + 1
					},
					new float[] { // bottom
							currentGame.getCube(i).getX() +1,0, currentGame.getCube(i).getZ() + 1,
							currentGame.getCube(i).getX() +0,0, currentGame.getCube(i).getZ() + 1,
							currentGame.getCube(i).getX() +0,0,currentGame.getCube(i).getZ() + 0,
							currentGame.getCube(i).getX() +1,0,currentGame.getCube(i).getZ() + 0
					},
					new float[] { // front
							currentGame.getCube(i).getX() + 1, 1, currentGame.getCube(i).getZ() + 1,
							currentGame.getCube(i).getX() +0, 1, currentGame.getCube(i).getZ() + 1,
							currentGame.getCube(i).getX() +0,0, currentGame.getCube(i).getZ() + 1,
							currentGame.getCube(i).getX() +1,0, currentGame.getCube(i).getZ() + 1
					},
					new float[] { // back
							currentGame.getCube(i).getX() +1,0,currentGame.getCube(i).getZ() + 0,
							currentGame.getCube(i).getX() +0,0,currentGame.getCube(i).getZ() + 0,
							currentGame.getCube(i).getX() +0, 1,currentGame.getCube(i).getZ() + 0,
							currentGame.getCube(i).getX() +1, 1,currentGame.getCube(i).getZ() + 0
					},
					new float[] { // left
							currentGame.getCube(i).getX() +0, 1, currentGame.getCube(i).getZ() + 1,
							currentGame.getCube(i).getX() +0, 1,currentGame.getCube(i).getZ() + 0,
							currentGame.getCube(i).getX() +0,0,currentGame.getCube(i).getZ() + 0,
							currentGame.getCube(i).getX() +0,-1, currentGame.getCube(i).getZ() + 1
					},
					new float[] { // right
							currentGame.getCube(i).getX() +1, 1,currentGame.getCube(i).getZ() + 0,
							currentGame.getCube(i).getX() +1, 1, currentGame.getCube(i).getZ() + 1,
							currentGame.getCube(i).getX() +1,0, currentGame.getCube(i).getZ() + 1,
							currentGame.getCube(i).getX() +1,0,currentGame.getCube(i).getZ() + 0
					},
				};
			cubeVertexBfr = new FloatBuffer[6];

			for (int k = 0; k < 6; k++)
			{
				cubeVertexBfr[k] = FloatBuffer.wrap(cubeCoords[k]);

				gl.glColor4f(0,1,0,0.5f);
				gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
				gl.glVertexPointer(3, GL10.GL_FLOAT, 0, cubeVertexBfr[k]);
				gl.glDrawArrays(GL10.GL_TRIANGLE_FAN, 0, 4);
				gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
			}
		}
		/*
		for (int i = 0 ; i < nbCubes ; i++)
		{
			
		
			for (int k = 0; k < 6; k++)
			{
				gl.glColor4f(0,1,0,0.5f);
				gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
				gl.glVertexPointer(3, GL10.GL_FLOAT, 0, cubes[i][k]);
				gl.glDrawArrays(GL10.GL_TRIANGLE_FAN, 0, 4);
				gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
			}
		}
		*/
		/*
		for (int i = 0; i < 6; i++)
		{
			gl.glColor4f(cubeColors[4*i+0], cubeColors[4*i+1], cubeColors[4*i+2], cubeColors[4*i+3]);
			gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
			gl.glVertexPointer(3, GL10.GL_FLOAT, 0, cubeVertexBfr[i]);
			gl.glDrawArrays(GL10.GL_TRIANGLE_FAN, 0, 4);
			gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		}
		
		// update rotations
		pyramidRot += 0.8f;
		cubeRot -= 0.5f;
		*/
		//rotation += 2.8f;

	}  

	public void onSurfaceChanged(GL10 gl, int width, int height) {
		// avoid division by zero
		if (height == 0) height = 1;
		// draw on the entire screen
		gl.glViewport(0, 0, width, height);
		// setup projection matrix
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		GLU.gluPerspective(gl,90.0f, (float)width / (float)height, 0.1f, 100.0f);
		//GLU.gluLookAt(gl, 5, 1, -1, 5, 0, 3, 0, 1, 0);
		//GLU.gluLookAt(gl, 0, 0, 20, 0, 0, 0, 0, 1, 0);
	}
	

}
