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
	private LostIcon bitmapInstructions2;
	private LostIcon bitmapInstructions3;
	private LostIcon bitmapInstructions4;
	
	public InstructionsRenderer (Context c)
	{
		super ();

		this.startTime = Calendar.getInstance().getTimeInMillis();
		this.lastTime = this.startTime;
		this.context  = c;

		this.currentPage = PAGE_MOVE;
		
		iconLogo = new LostIcon (c, "logo.png", -1.5f, 3.5f, -3.0f, LostIcon.ICON_BIG);
		bitmapInstructions1 = new LostIcon (c, "instructions1.png", 0.8f, -1.0f, -1.8f, LostIcon.ICON_BIG, LostIcon.FORM_RECTANGLE);
		bitmapInstructions2 = new LostIcon (c, "instructionsgun.png", 0.8f, -1.0f, -1.8f, LostIcon.ICON_BIG, LostIcon.FORM_RECTANGLE);
		bitmapInstructions3 = new LostIcon (c, "bomb.png", 0.8f, -1.0f, -1.8f, LostIcon.ICON_BIG, LostIcon.FORM_RECTANGLE);
		bitmapInstructions4 = new LostIcon (c, "utils.png", 0.8f, -1.0f, -1.8f, LostIcon.ICON_BIG, LostIcon.FORM_RECTANGLE);
		
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
	      bitmapInstructions2.loadGLTexture(gl, context);
	      bitmapInstructions3.loadGLTexture(gl, context);
	      bitmapInstructions4.loadGLTexture(gl, context);
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

	      switch (currentPage)
		  {
		      case PAGE_MOVE:
		      {
		    	  displayPage1(gl);
		    	  break;
		      }
		      
		      case PAGE_GUN:
		      {
		    	  displayPage2(gl);
		    	  break;
		      }
		      
		      case PAGE_BOMB:
		      {
		    	  displayPage3(gl);
		    	  break;
		      }
		      
		      case PAGE_UTILS:
		      {
		    	  displayPage4(gl);
		    	  break;
		      }
	      }
	       
	}
	
	
	public void displayPage4(GL10 gl)
	{
	      bitmapInstructions4.draw(gl);
	      
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
	      glText.draw( "To ease the use of the game", 0, base);
	      glText.draw( "some functions have been", 0, base - lineHeight); 
	      glText.draw( "introduced to assist you.", 0, base - ( 2 * lineHeight));
	      glText.end();
	      
	      glText.begin( 0.0f, 1.0f, 0.0f, 1.0f );   
	      glText.draw( "Camera position can be set", 0, base - ( 5 * lineHeight));
	      glText.draw( "by using the icon on the", 0, base - ( 6 * lineHeight));
	      glText.draw( "top left. You can either", 0, base - ( 7 * lineHeight));
	      glText.draw( "turn left or right.", 0, base - ( 8 * lineHeight));
	      glText.end();
	      
	      glText.begin( 1.0f, 0.0f, 0.0f, 1.0f );   
	      glText.draw( "Zoom can be adjusted by", 0, base - ( 12 * lineHeight));
	      glText.draw( "by pressing the icons on", 0, base - ( 13 * lineHeight));
	      glText.draw( "the top right. You can ", 0, base - ( 14 * lineHeight));
	      glText.draw( "either zoom in or out.", 0, base - ( 15 * lineHeight));
	      glText.end();  
	      
	      
	      glText.begin( 1.0f, 1.0f, 1.0f, 1.0f );         
	      glText.draw( "Page 4/4", (screenWidth / 2) - 30, 0);
	      glText.end(); 
	      gl.glDisable( GL10.GL_TEXTURE_2D );              // Enable Texture Mapping
	      gl.glDisable( GL10.GL_BLEND );                   // Enable Alpha Blend
	}
	
	
	public void displayPage3(GL10 gl)
	{
	      bitmapInstructions3.draw(gl);
	      
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
	      glText.draw( "Cube can also be destroyed", 0, base);
	      glText.draw( "using bombs. Two different", 0, base - lineHeight); 
	      glText.draw( "bombs are available: the .", 0, base - ( 2 * lineHeight));
	      glText.draw( "normal and the big.       ", 0, base - ( 3 * lineHeight));
	      glText.end();
	      
	      glText.begin( 0.0f, 1.0f, 0.0f, 1.0f );   
	      glText.draw( "Big bombs will explode when", 0, base - ( 5 * lineHeight));
	      glText.draw( "a cube hit their area but", 0, base - ( 6 * lineHeight));
	      glText.draw( "will also destroy the ground", 0, base - ( 7 * lineHeight));
	      glText.draw( "so that the hero might fall", 0, base - ( 8 * lineHeight));
	      glText.draw( "and die. They can be put on", 0, base - ( 9 * lineHeight));
	      glText.draw( "ground by pressing the icon", 0, base - ( 10 * lineHeight));
	      glText.draw( "at the bottom.", 0, base - ( 11 * lineHeight));
	      glText.end();
	      

	      glText.begin( 0.0f, 0.0f, 1.0f, 1.0f );   
	      glText.draw( "Normal bombs will explode when", 0, base - ( 14 * lineHeight));
	      glText.draw( "a cube hit their area but", 0, base - ( 15 * lineHeight));
	      glText.draw( "does not destroy the ground", 0, base - ( 16 * lineHeight));
	      glText.draw( "so that the hero can still", 0, base - ( 17 * lineHeight));
	      glText.draw( "move in that area. They are", 0, base - ( 18 * lineHeight));
	      glText.draw( "disposed by pressing the icon", 0, base - ( 19 * lineHeight));
	      glText.draw( "at the bottom.", 0, base - ( 20 * lineHeight));
	      glText.end();
	
	      
	      glText.begin( 1.0f, 1.0f, 1.0f, 1.0f );         
	      glText.draw( "Page 3/4", (screenWidth / 2) - 30, 0);
	      glText.end(); 
	      gl.glDisable( GL10.GL_TEXTURE_2D );              // Enable Texture Mapping
	      gl.glDisable( GL10.GL_BLEND );                   // Enable Alpha Blend
	}
	
	
	public void displayPage2(GL10 gl)
	{
	      bitmapInstructions2.draw(gl);
	      
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
	      glText.draw( "Cube can disappear by shooting ", 0, base);
	      glText.draw( "at them. Cube are hitted when", 0, base - lineHeight); 
	      glText.draw( "you shoot in their direction.", 0, base - ( 2 * lineHeight));
	      
	      glText.draw( "Number of remaining bullet are", 0, base - ( 4 * lineHeight));
	      glText.end();
	      glText.begin( 1.0f, 0.0f, 0.0f, 1.0f );   
	      glText.draw( "displayed on top with red.", 0, base - ( 5 * lineHeight));
	      glText.end();
	      
	      glText.begin( 1.0f, 1.0f, 1.0f, 1.0f );
	      glText.draw( "To shoot a bullet, you have to ", 0, base - ( 8 * lineHeight));
	      glText.end();
	      glText.begin( 1.0f, 0.0f, 0.0f, 1.0f );         
	      glText.draw( "press the icon at the bottom.", 0, base - ( 9 * lineHeight));
	      glText.end();  
	      
	      
	      glText.begin( 1.0f, 1.0f, 1.0f, 1.0f );         
	      glText.draw( "Page 2/4", (screenWidth / 2) - 30, 0);
	      glText.end(); 
	      gl.glDisable( GL10.GL_TEXTURE_2D );              // Enable Texture Mapping
	      gl.glDisable( GL10.GL_BLEND );                   // Enable Alpha Blend
	}
	
	public void displayPage1(GL10 gl)
	{
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
	      gl.glDisable( GL10.GL_TEXTURE_2D );              // Enable Texture Mapping
	      gl.glDisable( GL10.GL_BLEND );                   // Enable Alpha Blend
	}

	public void onSurfaceChanged(GL10 gl, int width, int height) {
		this.screenWidth = width;
	    this.screenHeight = height;
	}

	
}
