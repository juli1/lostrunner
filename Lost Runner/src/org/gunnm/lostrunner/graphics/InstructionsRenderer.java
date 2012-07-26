package org.gunnm.lostrunner.graphics;

import java.util.Calendar;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLU;
import android.opengl.GLSurfaceView.Renderer;
import android.util.Log;

public class InstructionsRenderer implements Renderer 
{
   private GLText glText;                             // A GLText Instance

	private long startTime;
	private long lastTime;
	private Context context;
	private int screenWidth = 100;
	private int screenHeight = 100;

	private int currentPage;
	public final static int PAGE_MOVE 	= 1;
	public final static int PAGE_GUN 	= 2;
	public final static int PAGE_BOMB 	= 3;
	public final static int PAGE_UTILS = 4;
	private LostIcon iconLogo;
	private LostIcon bitmapInstructions1;
	
	public InstructionsRenderer (Context c)
	{
		super ();

		this.startTime = Calendar.getInstance().getTimeInMillis();
		this.lastTime = this.startTime;
		this.context  = c;

		this.currentPage = PAGE_MOVE;
		
		iconLogo = new LostIcon (c, "logo.png", -1.5f, 3.5f, -3.0f, LostIcon.ICON_BIG);
		bitmapInstructions1 = new LostIcon (c, "instructions1.png", 0.8f, -1.0f, -1.8f, LostIcon.ICON_BIG, LostIcon.FORM_RECTANGLE);
		
	}

	public int getPage ()
	{
		return this.currentPage;
	}
	
	public void setPage (int p)
	{
		this.currentPage = p;
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
	      bitmapInstructions1.loadGLTexture(gl, context);
	}
	
	public void onDrawFrame(GL10 gl) {
		  long currentTime;
		  long deltaTime;
		  
		  currentTime = Calendar.getInstance().getTimeInMillis();
		  deltaTime = currentTime - lastTime;
		  
		  //updateAngles (deltaTime);
		  lastTime = currentTime;
		  //Log.i ("TitleRenderer", "Angle instructions=" + angleInstructions + ";anglerecords=" + angleOnlineScores + ";angleNewGame = " + angleNewGame);
	      // Redraw background color
		  
			gl.glViewport( 0, 0, screenWidth, screenHeight );

			gl.glMatrixMode( GL10.GL_PROJECTION );       
			gl.glViewport(0, 0, screenWidth, screenHeight);

			gl.glLoadIdentity();
			GLU.gluPerspective(gl,120.0f, (float)screenWidth / (float)screenHeight , 0.1f, 200.0f);
		  
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
	      
	      
	      gl.glPopMatrix();

	      bitmapInstructions1.draw(gl);
	      
	      // disable texture + alpha
	      gl.glDisable( GL10.GL_BLEND );                  // Disable Alpha Blend
	      gl.glDisable( GL10.GL_TEXTURE_2D );             // Disable Texture Mapping

	      
	      gl.glMatrixMode( GL10.GL_PROJECTION );          // Activate Projection Matrix
	      gl.glLoadIdentity();                            // Load Identity Matrix
	      gl.glOrthof(                                    // Set Ortho Projection (Left,Right,Bottom,Top,Front,Back)
	         0, screenWidth,
	         0, screenHeight,
	         1.0f, -1.0f
	      );
	      
	      gl.glEnable( GL10.GL_TEXTURE_2D );              // Enable Texture Mapping
	      gl.glEnable( GL10.GL_BLEND );                   // Enable Alpha Blend
	      gl.glBlendFunc( GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA );  // Set Alpha Blend Function

	      // TEST: render the entire font texture
	      gl.glColor4f( 1.0f, 1.0f, 1.0f, 1.0f );         // Set Color to Use

	      glText.begin( 1.0f, 1.0f, 1.0f, 1.0f );         // Begin Text Rendering (Set Color WHITE)
	      int base = screenHeight - (screenHeight / 3 );
	      int lineHeight = 20;
	      glText.draw( "The goal of the game is to ", 0, base);
	      glText.draw( "escape the level and go to", 0, base - lineHeight); 
	      glText.draw( "the door as quick as you can.", 0, base - ( 2 * lineHeight));
	      
	      glText.draw( "You move the hero using the ", 0, base - ( 4 * lineHeight));
	      glText.begin( 0.0f, 1.0f, 0.0f, 1.0f );   
	      glText.draw( "directional keys on the bottom.", 0, base - ( 5 * lineHeight));
	      glText.end();
	      glText.draw( "The hero must avoid the cubes.", 0, base - ( 6 * lineHeight));

	      glText.draw( "Be hitten by a cube will remove", 0, base - ( 8 * lineHeight));
	      glText.draw( "one life. Life counter is shown", 0, base - ( 9 * lineHeight));
	      glText.end();
	      glText.begin( 1.0f, 0.0f, 0.0f, 1.0f );         
	      glText.draw( "on the left side.", 0, base - ( 10 * lineHeight));
	      glText.end();  
	      
	      
	      glText.begin( 1.0f, 1.0f, 1.0f, 1.0f );         
	      glText.draw( "Page 1/4", (screenWidth / 2) - 30, 0);
	      glText.end(); 
	       
	}  

	public void onSurfaceChanged(GL10 gl, int width, int height) {
		this.screenWidth = width;
	    this.screenHeight = height;
	}

	
}
