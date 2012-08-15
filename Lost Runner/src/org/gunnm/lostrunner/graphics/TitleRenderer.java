package org.gunnm.lostrunner.graphics;

import java.util.Calendar;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLU;
import android.opengl.GLSurfaceView.Renderer;

public class TitleRenderer implements Renderer 
{
   private GLText glText;                             // A GLText Instance

	private long startTime;
	private long lastTime;
	private Context context;
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
		
		iconLogo = new LostIcon (c, "logo.png", -0.0f, 0.0f, -1.0f, LostIcon.ICON_BIG);
		iconLogo.setFade (0.3f);
	}

	


	public void onSurfaceCreated(GL10 gl, EGLConfig config) 
	{
	      gl.glClearColor( 0.0f, 0.0f, 0.0f, 1.0f );

	      glText = new GLText( gl, context.getAssets() );

	      glText.load( "Roboto-Regular.ttf", 14, 2, 2 );  
	      
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
			this.angleNewGame = this.angleNewGame - ANGLE_INCREMENT;
			if (this.angleNewGame < -ANGLE_MAX)
			{
				this.angleNewGame = -ANGLE_MAX;
				this.angleNewGameDirection = ANGLE_INCREASE;
			}
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


	      gl.glClear( GL10.GL_COLOR_BUFFER_BIT );


	      gl.glMatrixMode( GL10.GL_MODELVIEW );      
	      gl.glLoadIdentity(); 
	      
	      gl.glPushMatrix();
	      

	      gl.glEnable( GL10.GL_TEXTURE_2D );
	      gl.glEnable( GL10.GL_BLEND );
	      gl.glBlendFunc( GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA );  

	      gl.glColor4f( 1.0f, 1.0f, 1.0f, 1.0f );
	      
	      iconLogo.draw(gl);
	      
	      gl.glPushMatrix(); 
	      gl.glTranslatef (-22,40,-40);

	      glText.begin( 1.0f, 1.0f, 1.0f, 1.0f );
	      glText.draw( "Lost", -10, 0 );     
	      glText.end();  
	      gl.glPopMatrix();
	      
	      gl.glPushMatrix(); 
	      gl.glTranslatef (0,105,-60);
	      glText.begin( 1.0f, 1.0f, 1.0f, 1.0f );
	      glText.draw( "Runner", 10, -50 ); 
	      glText.end(); 
	      gl.glPopMatrix();
	      
	      
	  
	      gl.glPushMatrix(); 

	      gl.glRotatef(angleInstructions, 0, 1, 0);
	      gl.glTranslatef (-50,-20,-100);

	      glText.begin( 1.0f, 1.0f, 1.0f, 1.0f );        
	      glText.draw( "Game Instructions", 0, -100 );       
	      glText.end();                         
	      gl.glPopMatrix();
	       
	      gl.glPushMatrix();
	      gl.glRotatef(angleOnlineScores, 0, 1, 0);
	      gl.glTranslatef (-50,-20,-100);
	      glText.begin( 1.0f, 1.0f, 1.0f, 1.0f );         
	      glText.draw( "Online Records", 4, -20 );             
	      glText.end();                                   
	      gl.glPopMatrix();
	      
	      gl.glPushMatrix();
	      gl.glRotatef(angleNewGame, 0, 1, 0);
	      gl.glTranslatef (-50,-20,-100);
	      glText.begin( 1.0f, 1.0f, 1.0f, 1.0f );    
	      glText.draw( "Start or Continue", 2, 60 );           
	      glText.end();                              
	      gl.glPopMatrix();
	      
	      gl.glPopMatrix();

	      
	      gl.glDisable( GL10.GL_BLEND );             
	      gl.glDisable( GL10.GL_TEXTURE_2D );         

	}  

	public void onSurfaceChanged(GL10 gl, int width, int height) {
	      gl.glViewport( 0, 0, width, height );

	      gl.glMatrixMode( GL10.GL_PROJECTION );          
	      gl.glLoadIdentity();                   
	                        
			gl.glMatrixMode( GL10.GL_PROJECTION );       
			gl.glViewport(0, 0, width, height);

			gl.glLoadIdentity();
			GLU.gluPerspective(gl,120.0f, (float)width / (float)height , 0.1f, 200.0f);
	}

	
}
