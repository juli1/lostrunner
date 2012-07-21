package org.gunnm.lostrunner.graphics;

import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import org.gunnm.lostrunner.model.Game;

import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLU;
import android.util.Log;

public class LostRenderer implements Renderer 
{
	private static FloatBuffer[][] gamePlate;
	public static float camX = 0;
	public static float camY = 0;
	public static float camZ = 0;
	public static float toviewX = 0;
	public static float toviewY = 0;
	public static float toviewZ = 0;

	private static FloatBuffer[] cubeVertexBfr;

	
	private Game currentGame;
	
	public LostRenderer (Game g)
	{
		super ();
		
		this.currentGame = g;
		
		camX = g.getCurrentMap().getMapWidth();
		camY = 4;
		camZ = 3;
		
		toviewX = g.getCurrentMap().getMapWidth() / 2;
		toviewY = 0;
		toviewZ = -1 * g.getCurrentMap().getMapDepth() / 2;
		
		cubeVertexBfr = new FloatBuffer[6];
		
		for (int i = 0 ; i < 6 ; i++)
		{
			cubeVertexBfr[i] = FloatBuffer.wrap(cubeCoords[i]);
		}
	}
	private static float[][] cubeCoords = new float[][] {
			new float[] { // top
					 0.5f, 0.5f,-0.5f,
					-0.5f, 0.5f,-0.5f,
					-0.5f, 0.5f, 0.5f,
					 0.5f, 0.5f, 0.5f
				},
				new float[] { // bottom
					 0.5f,-0.5f, 0.5f,
					-0.5f,-0.5f, 0.5f,
					-0.5f,-0.5f,-0.5f,
					 0.5f,-0.5f,-0.5f
				},
				new float[] { // front
					 0.5f, 0.5f, 0.5f,
					-0.5f, 0.5f, 0.5f,
					-0.5f,-0.5f, 0.5f,
					 0.5f,-0.5f, 0.5f
				},
				new float[] { // back
					 0.5f,-0.5f,-0.5f,
					-0.5f,-0.5f,-0.5f,
					-0.5f, 0.5f,-0.5f,
					 0.5f, 0.5f,-0.5f
				},
				new float[] { // left
					-0.5f, 0.5f, 0.5f,
					-0.5f, 0.5f,-0.5f,
					-0.5f,-0.5f,-0.5f,
					-0.5f,-0.5f, 0.5f
				},
				new float[] { // right
					 0.5f, 0.5f,-0.5f,
					 0.5f, 0.5f, 0.5f,
					 0.5f,-0.5f, 0.5f,
					 0.5f,-0.5f,-0.5f
				},
			};
	
	
	

	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		gl.glShadeModel(GL10.GL_SMOOTH);
		gl.glClearColor(0, 0, 0, 0);

		gl.glClearDepthf(1.0f);
		gl.glEnable(GL10.GL_DEPTH_TEST);
		gl.glDepthFunc(GL10.GL_LEQUAL);
		
		gl.glEnable(GL10.GL_CULL_FACE);
		gl.glCullFace(GL10.GL_BACK);
		
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
		gamePlate = new FloatBuffer[currentGame.getCurrentMap().getMapWidth()][currentGame.getCurrentMap().getMapDepth()];
		
		for (int i = 0 ; i < currentGame.getCurrentMap().getMapWidth() ; i++)
		{
			for (int j = 0 ; j < currentGame.getCurrentMap().getMapDepth() ; j++)
			{
				gamePlate[i][j] = FloatBuffer.wrap(new float[]{i    , 0 , -j,
						                                      i+1  ,  0 , -j,
						                                      i+1, 0, -j-1,
						                                      i, 0 , -j-1});
			}
		}
	}

	public void onDrawFrame(GL10 gl) {
		currentGame.update();
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
		GLU.gluLookAt(gl, camX , camY, camZ , 
						  toviewX, toviewY, toviewZ, 
						  0, 1, 0);

		gl.glColor4f(1, 0, 0, 0.5f);
		for (int i = 0 ; i < currentGame.getCurrentMap().getMapWidth() ; i++)
		{
			for (int j = 0 ; j < currentGame.getCurrentMap().getMapDepth() ; j++)
			{
				gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
				gl.glVertexPointer(3, GL10.GL_FLOAT, 0, gamePlate[i][j]);
				gl.glDrawArrays(GL10.GL_TRIANGLE_FAN, 0, 4);
				gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
			}
		}
		
		for (int i = 0 ; i < currentGame.getCurrentMap().getNbCubes() ; i++)
		{		
			gl.glPushMatrix();
			
			gl.glTranslatef(currentGame.getCube(i).getX() + 0.5f, 0.5f, currentGame.getCube(i).getZ());

			gl.glRotatef(currentGame.getCube(i).getRotation(), 1, 0, 0);
			
			for (int k = 0; k < 6; k++)
			{
				gl.glColor4f(0,1,0,0.5f);
				gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
				gl.glVertexPointer(3, GL10.GL_FLOAT, 0, cubeVertexBfr[k]);
				gl.glDrawArrays(GL10.GL_TRIANGLE_FAN, 0, 4);
				gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
			}
			
			gl.glPopMatrix();
		}
	
	}  

	public void onSurfaceChanged(GL10 gl, int width, int height) {
		if (height == 0) height = 1;
		gl.glViewport(0, 0, width, height);
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		GLU.gluPerspective(gl,90.0f, (float)width / (float)height, 0.1f, 100.0f);

	}
	

}
