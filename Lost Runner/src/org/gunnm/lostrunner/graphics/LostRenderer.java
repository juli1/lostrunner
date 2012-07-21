package org.gunnm.lostrunner.graphics;

import java.nio.FloatBuffer;
import java.util.Calendar;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import org.gunnm.lostrunner.R;
import org.gunnm.lostrunner.model.Game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLU;
import android.opengl.GLUtils;
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
	private int textures[];
	private static final int NB_TEXTURES = 8;
	private static FloatBuffer[] cubeVertexBfr;
	private static FloatBuffer doorVertexBfr;
	private static FloatBuffer heroVertexBfr;
	private Context context;
	
	private boolean showFPS = true;
	private long startTime;
	private long nbFrames;
	
	
	private Game currentGame;
	private GLText glText;
	
	
	public LostRenderer (Context c, Game g)
	{
		super ();
		this.textures = new int[NB_TEXTURES];
		this.currentGame = g;
		this.context = c;
		this.startTime = Calendar.getInstance().getTimeInMillis();
		this.nbFrames = 0;
		
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
		doorVertexBfr = FloatBuffer.wrap(doorCoords);
		heroVertexBfr = FloatBuffer.wrap(heroCoords);
	}
	
	public void enableShowFPS ()
	{
		this.showFPS = true;
		this.startTime = Calendar.getInstance().getTimeInMillis();
		this.nbFrames = 0;
	}
	
	public void disableShowFPS ()
	{
		this.showFPS = false;
	}
	
	private static float [] doorCoords = new float[] {1, 2, 0, 
													  0, 2, 0, 
													  0,0, 0, 
													  1,0, 0};
	private static float [] heroCoords = new float[] {0.75f, 2, 0, 
													  0.25f, 2, 0, 
													  0.25f,0, 0, 
													  0.75f,0, 0};
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
		
	      // Set the background frame color
	      gl.glClearColor( 0.5f, 0.5f, 0.5f, 1.0f );

	      // Create the GLText
	      glText = new GLText( gl, context.getAssets() );

	      // Load the font from file (set size + padding), creates the texture
	      // NOTE: after a successful call to this the font is ready for rendering!
	      glText.load( "Roboto-Regular.ttf", 14, 2, 2 );  // Create Font (Height: 14 Pixels / X+Y Padding 2 Pixels)
		
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

		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		
	      gl.glMatrixMode( GL10.GL_MODELVIEW );       
	      gl.glLoadIdentity();              

	      currentGame.update();
		
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
		
		gl.glPushMatrix();
		gl.glTranslatef(currentGame.getCurrentMap().getExitPositionX() , 0, currentGame.getCurrentMap().getExitPositionZ());
		gl.glColor4f(0,0,1,0.5f);
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, doorVertexBfr);
		gl.glDrawArrays(GL10.GL_TRIANGLE_FAN, 0, 4);
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glPopMatrix();
		
		gl.glPushMatrix();
		gl.glTranslatef(currentGame.getHero().getX() , 0, currentGame.getHero().getZ());
		gl.glColor4f(0,1,1,0.5f);
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, heroVertexBfr);
		gl.glDrawArrays(GL10.GL_TRIANGLE_FAN, 0, 4);
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glPopMatrix();
		 
		  /* For text import
	      gl.glEnable( GL10.GL_TEXTURE_2D );              // Enable Texture Mapping
	      gl.glEnable( GL10.GL_BLEND );                   // Enable Alpha Blend
	      gl.glBlendFunc( GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA );  // Set Alpha Blend Function
	      // TEST: render the entire font texture
	      gl.glColor4f( 1.0f, 1.0f, 1.0f, 1.0f );         // Set Color to Use

	      // TEST: render some strings with the font
	      glText.begin( 1.0f, 1.0f, 1.0f, 1.0f );         // Begin Text Rendering (Set Color WHITE)
	      glText.draw( "Test String :)", 0, 0 );          // Draw Test String
	      glText.draw( "Line 1", 50, 50 );                // Draw Test String
	      glText.draw( "Line 2", 100, 100 );              // Draw Test String
	      glText.end();
	      gl.glDisable( GL10.GL_TEXTURE_2D );              // Enable Texture Mapping
	      gl.glDisable( GL10.GL_BLEND ); 
	      */
		
		if (this.showFPS)
		{
			this.nbFrames = this.nbFrames + 1;
			long currentTime = Calendar.getInstance().getTimeInMillis();
			long nbsec;
			nbsec = 1;
			nbsec = ((currentTime - startTime) / 1000 );
			if (nbsec == 0)
			{
				nbsec = 1;
			}
			long fps =  nbFrames / nbsec;
			Log.i("LostRenderer", "FPS=" + fps);
		}
	}  

	public void onSurfaceChanged(GL10 gl, int width, int height) {
		if (height == 0)
		{
			height = 1;
		}
	    // Setup orthographic projection
	    gl.glMatrixMode( GL10.GL_PROJECTION );          // Activate Projection Matrix
		gl.glViewport(0, 0, width, height);
		
		gl.glLoadIdentity();
		GLU.gluPerspective(gl,90.0f, (float)width / (float)height , 0.1f, 100.0f);
	}
	

}
