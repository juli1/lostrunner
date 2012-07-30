package org.gunnm.lostrunner.graphics;

import java.util.Calendar;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLU;
import android.opengl.GLSurfaceView.Renderer;
import android.util.Log;

public class TitleRenderer implements Renderer 
{
   private GLText glText;                             // A GLText Instance

	private long startTime;
	private long lastTime;
	private Context context;
	private int screenWidth = 100;
	private int screenHeight = 100;
	private float angleNewGame;
	private float angleOnlineScores;
	private float angleInstructions;
	private int angleNewGameDirection;
	private int angleOnlineScoresDirection;
	private int angleInstructionsDirection;
	
	private final int ANGLE_INCREASE = 1;
	private final int ANGLE_DECREASE = 2;
	private final int ANGLE_INCREMENT = 1;
	private final int ANGLE_MAX = 10;
	private LostIcon iconLogo;
	
	public TitleRenderer (Context c)
	{
		super ();

		this.startTime = Calendar.getInstance().getTimeInMillis();
		this.lastTime = this.startTime;
		this.context  = c;
		this.angleNewGame = 5;
		this.angleOnlineScores = -5;
		this.angleInstructions = 0;
		this.angleNewGameDirection = ANGLE_INCREASE;
		this.angleOnlineScoresDirection = ANGLE_DECREASE;
		this.angleInstructionsDirection = ANGLE_INCREASE;
		
		iconLogo = new LostIcon (c, "logo.png", -1.5f, 3.5f, -3.0f, LostIcon.ICON_BIG);
	}

	


	public void onSurfaceCreated(GL10 gl, EGLConfig config) 
	{
	      // Set the background frame color
	      gl.glClearColor( 0.0f, 0.0f, 0.0f, 1.0f );

	      // Create the GLText
	      glText = new GLText( gl, context.getAssets() );

	      // Load the font from file (set size + padding), creates the texture
	      // NOTE: after a successful call to this the font is ready for rendering!
	      glText.load( "Roboto-Regular.ttf", 14, 2, 2 );  // Create Font (Height: 14 Pixels / X+Y Padding 2 Pixels)
	      
	      iconLogo.loadGLTexture(gl, context);

	}

	private void updateAngles (long delta)
	{
		if ((delta % 3) == 0)
		{
			return;
		}
		if ((delta % 6) == 0)
		{
			return;
		}
		if ((delta % 5) == 0)
		{
			return;
		}
		
		if (this.angleInstructionsDirection == ANGLE_INCREASE)
		{
			this.angleInstructions = this.angleInstructions + ANGLE_INCREMENT;
			if (this.angleInstructions > ANGLE_MAX)
			{
				this.angleInstructions = ANGLE_MAX;
				this.angleInstructionsDirection = ANGLE_DECREASE;
			}
		}
		else
		{
			this.angleInstructions = this.angleInstructions - ANGLE_INCREMENT;
			if (this.angleInstructions < -ANGLE_MAX)
			{
				this.angleInstructions = -ANGLE_MAX;
				this.angleInstructionsDirection = ANGLE_INCREASE;
			}
		}
		
		if (this.angleNewGameDirection == ANGLE_INCREASE)
		{
			//Log.i("TitleRenderer", "Should increase the nesgame angle, old value="+ this.angleNewGame+ "increment=" + ANGLE_INCREMENT + "d=" + delta);
			this.angleNewGame = this.angleNewGame + ANGLE_INCREMENT;
			if (this.angleNewGame > ANGLE_MAX)
			{
				this.angleNewGame = ANGLE_MAX;
				this.angleNewGameDirection = ANGLE_DECREASE;
			}
			//Log.i("TitleRenderer", "New value="+ this.angleNewGame);
		}
		else
		{
			//Log.i("TitleRenderer", "Should decrease the nesgame angle, old value="+ this.angleNewGame + "increment=" + ANGLE_INCREMENT + "d=" + delta);

			this.angleNewGame = this.angleNewGame - ANGLE_INCREMENT;
			if (this.angleNewGame < -ANGLE_MAX)
			{
				this.angleNewGame = -ANGLE_MAX;
				this.angleNewGameDirection = ANGLE_INCREASE;
			}
			//Log.i("TitleRenderer", "New value="+ this.angleNewGame);

		}
		
		if (this.angleOnlineScoresDirection == ANGLE_INCREASE)
		{
			this.angleOnlineScores = this.angleOnlineScores +ANGLE_INCREMENT;
			if (this.angleOnlineScores > ANGLE_MAX)
			{
				this.angleOnlineScores = ANGLE_MAX;
				this.angleOnlineScoresDirection = ANGLE_DECREASE;
			}
		}
		else
		{
			this.angleOnlineScores = this.angleOnlineScores -ANGLE_INCREMENT;
			if (this.angleOnlineScores < -ANGLE_MAX)
			{
				this.angleOnlineScores = -ANGLE_MAX;
				this.angleOnlineScoresDirection = ANGLE_INCREASE;
			}
		}
		
	}
	
	public void onDrawFrame(GL10 gl) {
		  long currentTime;
		  long deltaTime;
		  
		  currentTime = Calendar.getInstance().getTimeInMillis();
		  deltaTime = currentTime - lastTime;
		  
		  updateAngles (deltaTime);
		  lastTime = currentTime;
		  //Log.i ("TitleRenderer", "Angle instructions=" + angleInstructions + ";anglerecords=" + angleOnlineScores + ";angleNewGame = " + angleNewGame);
	      // Redraw background color
	      gl.glClear( GL10.GL_COLOR_BUFFER_BIT );

	      // Set to ModelView mode
	      gl.glMatrixMode( GL10.GL_MODELVIEW );           // Activate Model View Matrix
	      gl.glLoadIdentity(); 
	      
	      gl.glPushMatrix();
	      
	      // enable texture + alpha blending
	      // NOTE: this is required for text rendering! we could incorporate it into
	      // the GLText class, but then it would be called multiple times (which impacts performance).
	      gl.glEnable( GL10.GL_TEXTURE_2D );              // Enable Texture Mapping
	      gl.glEnable( GL10.GL_BLEND );                   // Enable Alpha Blend
	      gl.glBlendFunc( GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA );  // Set Alpha Blend Function

	      // TEST: render the entire font texture
	      gl.glColor4f( 1.0f, 1.0f, 1.0f, 1.0f );         // Set Color to Use
	      //glText.drawTexture( screenWidth, screenHeight );            // Draw the Entire Texture
	      
	      iconLogo.draw(gl);
	      
	      gl.glPushMatrix(); 
	      gl.glTranslatef (20,100,-50);

	      glText.begin( 1.0f, 1.0f, 1.0f, 1.0f );         // Begin Text Rendering (Set Color WHITE)
	      glText.draw( "Lost", -11, -50 );          // Draw Test String
	      glText.end();                                   // End Text Rendering
	      gl.glPopMatrix();
	      
	      gl.glPushMatrix(); 
	      gl.glTranslatef (0,100,-60);
	      glText.begin( 1.0f, 1.0f, 1.0f, 1.0f );         // Begin Text Rendering (Set Color WHITE)
	      glText.draw( "Runner", 0, -50 );          // Draw Test String
	      glText.end();                                   // End Text Rendering
	      gl.glPopMatrix();
	      
	      
	      // TEST: render some strings with the font
	      gl.glPushMatrix(); 

	      gl.glRotatef(angleInstructions, 0, 1, 0);
	      gl.glTranslatef (-50,-20,-100);

	      glText.begin( 1.0f, 1.0f, 1.0f, 1.0f );         // Begin Text Rendering (Set Color WHITE)
	      glText.draw( "Game Instructions", 0, -100 );          // Draw Test String
	      glText.end();                                   // End Text Rendering
	      gl.glPopMatrix();
	       
	      gl.glPushMatrix();
	      gl.glRotatef(angleOnlineScores, 0, 1, 0);
	      gl.glTranslatef (-50,-20,-100);
	      glText.begin( 1.0f, 1.0f, 1.0f, 1.0f );         // Begin Text Rendering (Set Color WHITE)
	      glText.draw( "Online Records", 4, -20 );                // Draw Test String
	      glText.end();                                   // End Text Rendering
	      gl.glPopMatrix();
	      
	      gl.glPushMatrix();
	      gl.glRotatef(angleNewGame, 0, 1, 0);
	      gl.glTranslatef (-50,-20,-100);
	      glText.begin( 1.0f, 1.0f, 1.0f, 1.0f );         // Begin Text Rendering (Set Color WHITE)
	      glText.draw( "Start or Continue", 2, 60 );              // Draw Test String
	      glText.end();                                   // End Text Rendering
	      gl.glPopMatrix();
	      
	                                       // End Text Rendering
	      gl.glPopMatrix();

	      
	      // disable texture + alpha
	      gl.glDisable( GL10.GL_BLEND );                  // Disable Alpha Blend
	      gl.glDisable( GL10.GL_TEXTURE_2D );             // Disable Texture Mapping

	}  

	public void onSurfaceChanged(GL10 gl, int width, int height) {
	      gl.glViewport( 0, 0, width, height );

	      // Setup orthographic projection
	      gl.glMatrixMode( GL10.GL_PROJECTION );          // Activate Projection Matrix
	      gl.glLoadIdentity();                            // Load Identity Matrix
	     
	      // Save width and height
	      this.screenWidth = width;                             // Save Current Width
	      this.screenHeight = height;                           // Save Current Height
			gl.glMatrixMode( GL10.GL_PROJECTION );       
			gl.glViewport(0, 0, width, height);

			gl.glLoadIdentity();
			GLU.gluPerspective(gl,120.0f, (float)width / (float)height , 0.1f, 200.0f);
	}

	
}
