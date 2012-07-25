package org.gunnm.lostrunner.graphics;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Calendar;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import org.gunnm.lostrunner.R;
import org.gunnm.lostrunner.model.Game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
	public float camX = 0;
	public float camY = 0;
	public float camZ = 0;
	public float camAngle;

	
	LostIcon iconDirectionLeft;
	LostIcon iconDirectionRight;
	LostIcon iconDirectionUp;
	LostIcon iconDirectionDown;
	LostIcon iconGunSmall;
	LostIcon iconBombSmall;
	LostIcon iconBigBombSmall;
	LostIcon iconLifeSmall;
	LostIcon iconGun;
	LostIcon iconBomb;
	LostIcon iconBigBomb;
	LostIcon iconZoomIn;
	LostIcon iconZoomOut;
	LostIcon iconCameraLeft;
	LostIcon iconCameraRight;
	private static FloatBuffer[] cubeVertexBfr;
	private static FloatBuffer doorVertexBfr;
	private static FloatBuffer heroVertexBfr;
	private Context context;

	private boolean showFPS = true;
	private long startTime;
	private long nbFrames;


	private Game currentGame;
	private GLText glText;

	private final static int CAMERA_ZOOM_IN   = 1;
	private final static int CAMERA_ZOOM_OUT  = 2;
	private final static int CAMERA_ZOOM_NONE = 3;

	private final static int CAMERA_MOVE_LEFT   = 1;
	private final static int CAMERA_MOVE_RIGHT  = 2;
	private final static int CAMERA_MOVE_NONE   = 3;

	private int currentCameraZoom;
	private int currentCameraMove;

	private int screenWidth;
	private int screenHeight;
	
	private int[] 		textureGround 	= new int[1];
	private int[] 		textureDoor 	= new int[1];
	private int[] 		textureCube 	= new int[1];
	
	private FloatBuffer textureBuffer;  // buffer holding the texture coordinates
	private FloatBuffer[] textureCubeBuffer;  // buffer holding the texture coordinates
	
	private float texture[] = {
	// Mapping coordinates for the vertices
			1.0f, 0.0f,      // bottom right (V3)
			0.0f, 0.0f,     // top right    (V4)
			0.0f, 1.0f,     // bottom left  (V1)
			1.0f, 1.0f,     // top left     (V2)	
	};
	
	private static float textureCubeCoords[][] = new float[][] {
			new float[] 
					{1.0f, 0.0f,   
					0.0f, 0.0f,   
					0.0f, 1.0f,     
					1.0f, 1.0f,     
			},
			new float[] 
					{1.0f, 0.0f,     
					0.0f, 0.0f,     
					0.0f, 1.0f,     
					1.0f, 1.0f,    
			},
			new float[] 
					{1.0f, 0.0f,      
					0.0f, 0.0f,     
					0.0f, 1.0f,     
					1.0f, 1.0f,    
			},
			new float[]
					{1.0f, 0.0f,   
					0.0f, 0.0f,   
					0.0f, 1.0f,     
					1.0f, 1.0f,     
			},
			new float[] 
					{1.0f, 0.0f,  
					0.0f, 0.0f, 
					0.0f, 1.0f,  
					1.0f, 1.0f,  
			},
			new float[]
					{1.0f, 0.0f,  
					0.0f, 0.0f,    
					0.0f, 1.0f,    
					1.0f, 1.0f,    
			},
	};

	private static float [] doorCoords = new float[] {1, 2, 0, 
		0, 2, 0, 
		0,0, 0, 
		1,0, 0};
	private static float [] heroCoords = new float[] {0.75f, 2, 0, 
		0.25f, 2, 0, 
		0.25f,0, 0, 
		0.75f,0, 0};
	private static float[][] cubeCoords = new float[][] {
		new float[] { 
				0.5f, 0.5f,-0.5f,
				-0.5f, 0.5f,-0.5f,
				-0.5f, 0.5f, 0.5f,
				0.5f, 0.5f, 0.5f
		},
		new float[] { 
				0.5f,-0.5f, 0.5f,
				-0.5f,-0.5f, 0.5f,
				-0.5f,-0.5f,-0.5f,
				0.5f,-0.5f,-0.5f
		},
		new float[] { 
				0.5f, 0.5f, 0.5f,
				-0.5f, 0.5f, 0.5f,
				-0.5f,-0.5f, 0.5f,
				0.5f,-0.5f, 0.5f
		},
		new float[] { 
				0.5f,-0.5f,-0.5f,
				-0.5f,-0.5f,-0.5f,
				-0.5f, 0.5f,-0.5f,
				0.5f, 0.5f,-0.5f
		},
		new float[] { 
				-0.5f, 0.5f, 0.5f,
				-0.5f, 0.5f,-0.5f,
				-0.5f,-0.5f,-0.5f,
				-0.5f,-0.5f, 0.5f
		},
		new float[] {
				0.5f, 0.5f,-0.5f,
				0.5f, 0.5f, 0.5f,
				0.5f,-0.5f, 0.5f,
				0.5f,-0.5f,-0.5f
		},
	};
	

	
	
	public void disableCameraMove ()
	{
		this.currentCameraMove 	= CAMERA_MOVE_NONE;
	}

	public void disableCameraZoom ()
	{
		this.currentCameraZoom 	= CAMERA_ZOOM_NONE;
	}




	public LostRenderer (Context c, Game g)
	{
		super ();
		this.currentCameraMove 	= CAMERA_MOVE_NONE;
		this.currentCameraZoom	= CAMERA_ZOOM_NONE;
		this.currentGame 		= g;
		this.context 			= c;
		this.startTime 			= Calendar.getInstance().getTimeInMillis();
		this.nbFrames 			= 0;

		camX = 7;
		camY = 4.5f;
		camZ = 2;
		camAngle = -45;

		cubeVertexBfr = new FloatBuffer[6];
		textureCubeBuffer = new FloatBuffer[6];
		for (int i = 0 ; i < 6 ; i++)
		{
			cubeVertexBfr[i] = makeFloatBuffer(cubeCoords[i]);
			textureCubeBuffer[i] = makeFloatBuffer(textureCubeCoords[i]);
		}
		doorVertexBfr = makeFloatBuffer(doorCoords);
		heroVertexBfr = makeFloatBuffer(heroCoords);
		
		textureBuffer = ByteBuffer.allocateDirect(texture.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
		textureBuffer.put(texture);
		textureBuffer.position(0);

		iconDirectionLeft = new LostIcon (c, "direction-left.png", -5.5f, -7.5f, -10.0f);
		iconDirectionDown = new LostIcon (c, "direction-down.png", -3.5f, -9.0f, -10.0f);
		iconDirectionUp = new LostIcon (c, "direction-up.png", -3.5f, -5.5f, -10.0f);
		iconDirectionRight = new LostIcon (c, "direction-right.png", -1.5f, -7.5f, -10.0f);
		iconGun = new LostIcon (c, "gun.png", 0.5f, -9.0f, -10.0f);
		iconBomb = new LostIcon (c, "bomb1.png", 3.0f, -9.0f, -10.0f);
		iconBigBomb = new LostIcon (c, "bomb2.png", 5.5f, -9.0f, -10.0f);
		
		iconGunSmall = new LostIcon (c, "gun.png", -6f, 7.0f, -10.0f, LostIcon.ICON_SMALL);
		iconBombSmall = new LostIcon (c, "bomb1.png", -6f, 6.0f, -10.0f, LostIcon.ICON_SMALL);
		iconBigBombSmall = new LostIcon (c, "bomb2.png", -6f, 5.0f, -10.0f, LostIcon.ICON_SMALL);
		iconLifeSmall = new LostIcon (c, "life.png", -6f, 4.0f, -10.0f, LostIcon.ICON_SMALL);
		
		
		iconZoomIn = new LostIcon (c, "zoomin.png",5.5f, 8.5f, -10.0f, LostIcon.ICON_MEDIUM);
		iconZoomOut = new LostIcon (c, "zoomout.png",3.0f, 8.5f, -10.0f , LostIcon.ICON_MEDIUM);
		iconCameraLeft = new LostIcon (c, "camleft.png", -5.5f, 8.5f, -10.0f, LostIcon.ICON_MEDIUM);
		iconCameraRight = new LostIcon (c, "camright.png",  -3.0f, 8.5f, -10.0f, LostIcon.ICON_MEDIUM);
	}

	public void loadGLTexture(GL10 gl, String filename, int[] textures) 
	{
		
		Bitmap bitmap;
		try 
		{
			bitmap = BitmapFactory.decodeStream(context.getAssets().open(filename));
		} 
		catch (IOException e) 
		{
			Log.i("LostIcon", "Error when trying to load texture" + filename);
			return;
		}
		
		gl.glGenTextures(1, textures, 0);
		
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]);

		// create nearest filtered texture
		gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
		gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
		gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_CLAMP_TO_EDGE);
		gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_CLAMP_TO_EDGE);
		
		// Use Android GLUtils to specify a two-dimensional texture image from our bitmap
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);

		bitmap.recycle();
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


	private FloatBuffer makeFloatBuffer(float[] arr) {
		ByteBuffer bb = ByteBuffer.allocateDirect(arr.length*4);
		bb.order(ByteOrder.nativeOrder());
		FloatBuffer fb = bb.asFloatBuffer();
		fb.put(arr);
		fb.position(0);
		return fb;
	}


	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		iconDirectionLeft.loadGLTexture(gl, context);
		iconDirectionDown.loadGLTexture(gl, context);
		iconDirectionUp.loadGLTexture(gl, context);
		iconDirectionRight.loadGLTexture(gl, context);
		iconGun.loadGLTexture(gl, context);
		iconBomb.loadGLTexture(gl, context);
		iconBigBomb.loadGLTexture(gl, context);
		iconZoomIn.loadGLTexture(gl, context);
		iconZoomOut.loadGLTexture(gl, context);
		iconCameraLeft.loadGLTexture(gl, context);
		iconCameraRight.loadGLTexture(gl, context);
		iconGunSmall.loadGLTexture(gl, context);
		iconBombSmall.loadGLTexture(gl, context);
		iconLifeSmall.loadGLTexture(gl, context);
		iconBigBombSmall.loadGLTexture(gl, context);
		
		loadGLTexture(gl, "metal2.png", textureCube);
		loadGLTexture(gl, "ground4.png", textureGround);
		loadGLTexture(gl, "door2.png", textureDoor);
		
		// Create the GLText
		glText = new GLText( gl, context.getAssets() );
		// Load the font from file (set size + padding), creates the texture
		// NOTE: after a successful call to this the font is ready for rendering!
		glText.load( "Roboto-Regular.ttf", 14, 2, 2 );  // Create Font (Height: 14 Pixels / X+Y Padding 2 Pixels)
		
		
	    gl.glEnable(GL10.GL_BLEND);
        gl.glBlendFunc(gl.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		gl.glEnable(GL10.GL_TEXTURE_2D);
		gl.glShadeModel(GL10.GL_SMOOTH);
		gl.glClearColor(0, 0, 0, 0);

		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
		
		gamePlate = new FloatBuffer[currentGame.getCurrentMap().getMapWidth()][currentGame.getCurrentMap().getMapDepth()];

		for (int i = 0 ; i < currentGame.getCurrentMap().getMapWidth() ; i++)
		{
			for (int j = 0 ; j < currentGame.getCurrentMap().getMapDepth() ; j++)
			{
				gamePlate[i][j] = makeFloatBuffer(new float[]{i    , 0 , -j,
						i+1  ,  0 , -j,
						i+1, 0, -j-1,
						i, 0 , -j-1});
			}
		}
	}

	public void onDrawFrame(GL10 gl) {
		long fps = 0;

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
			fps =  nbFrames / nbsec;
		}

		if (this.currentCameraMove != CAMERA_MOVE_NONE)
		{
			this.updateMove();
		}

		if (this.currentCameraZoom != CAMERA_ZOOM_NONE)
		{
			this.updateZoom();
		}


		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);



		gl.glMatrixMode( GL10.GL_MODELVIEW );       
		gl.glLoadIdentity();   
		
		if (currentGame.isCompleted())
		{
			/* Start text stuff */
			gl.glPushMatrix();
			gl.glTranslatef(-50, 80, -200);
			gl.glEnable( GL10.GL_TEXTURE_2D );              // Enable Texture Mapping
			gl.glEnable( GL10.GL_BLEND );                    // Enable Alpha Blend
			gl.glBlendFunc( GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA );  // Set Alpha Blend Function
			// TEST: render the entire font texture
			gl.glColor4f( 1.0f, 1.0f, 1.0f, 1.0f );         // Set Color to Use
	
			// TEST: render some strings with the font
			glText.begin( 1.0f, 1.0f, 1.0f, 1.0f );         // Begin Text Rendering (Set Color WHITE)
			glText.draw( "Congratulations !", 0, 0 );          // Draw Test String
			glText.end();
			gl.glPopMatrix();
		}
		
		/* Camera stuff */
		gl.glPushMatrix();
		currentGame.update();

		gl.glRotatef(20, 1, 0, 0);
		gl.glRotatef(camAngle, 0, 1, 0);
		gl.glTranslatef(-camX, -camY, -camZ);


		/* Start to draw the scene */
		gl.glColor4f(1, 0, 0, 0.5f);
		for (int i = 0 ; i < currentGame.getCurrentMap().getMapWidth() ; i++)
		{
			for (int j = 0 ; j < currentGame.getCurrentMap().getMapDepth() ; j++)
			{
				if (currentGame.isDestroyed(i, j))
				{
					continue;
				}

				gl.glColor4f(1, 1, 1, 1);

				if (currentGame.hasBigBomb(i, j))
				{
					gl.glColor4f(1, 0.5f, 0.5f, 0.5f);	
				}

				if (currentGame.hasBomb(i, j))
				{
					gl.glColor4f(1, 0.2f, 0.2f, 0.5f);	
				}
				gl.glEnable( GL10.GL_TEXTURE_2D );              // Enable Texture Mapping
				gl.glEnable( GL10.GL_BLEND );                    // Enable Alpha Blend
				gl.glBindTexture(GL10.GL_TEXTURE_2D, textureGround[0]);
				gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
				gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

				gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
				
				gl.glFrontFace(GL10.GL_CW);
				
				gl.glVertexPointer(3, GL10.GL_FLOAT, 0, gamePlate[i][j]);
				gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBuffer);

				gl.glDrawArrays(GL10.GL_TRIANGLE_FAN, 0, 4);

				gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
				gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
				gl.glDisable( GL10.GL_BLEND );  
				gl.glDisable( GL10.GL_TEXTURE_2D );
			}
		}

		for (int i = 0 ; i < currentGame.getCurrentMap().getNbCubes() ; i++)
		{
			if (currentGame.getCube(i).isVisible() == false)
			{
				continue;
			}

			gl.glPushMatrix();

			gl.glTranslatef(currentGame.getCube(i).getX() + 0.5f, 0.5f, currentGame.getCube(i).getZ());

			gl.glRotatef(currentGame.getCube(i).getRotation(), 1, 0, 0);

			for (int k = 0; k < 6; k++)
			{
				/*
				gl.glColor4f(0,1,0,0.5f);
				gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
				gl.glVertexPointer(3, GL10.GL_FLOAT, 0, cubeVertexBfr[k]);
				gl.glDrawArrays(GL10.GL_TRIANGLE_FAN, 0, 4);
				gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
				*/
				gl.glEnable( GL10.GL_TEXTURE_2D );              // Enable Texture Mapping
				gl.glEnable( GL10.GL_BLEND );                    // Enable Alpha Blend

				gl.glBindTexture(GL10.GL_TEXTURE_2D, textureCube[0]);

				gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
				gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
				gl.glEnable(GL10.GL_DEPTH_TEST);
				gl.glDepthFunc(GL10.GL_LEQUAL);
				gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
				
				gl.glFrontFace(GL10.GL_CW);
				
				gl.glVertexPointer(3, GL10.GL_FLOAT, 0, cubeVertexBfr[k]);
				gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureCubeBuffer[k]);

				gl.glDrawArrays(GL10.GL_TRIANGLE_FAN, 0, 4);
				gl.glDisable(GL10.GL_DEPTH_TEST);
				gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
				gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
				gl.glDisable( GL10.GL_BLEND );  
				gl.glDisable( GL10.GL_TEXTURE_2D );
			}

			gl.glPopMatrix();

		}

		gl.glPushMatrix();
		gl.glTranslatef(currentGame.getCurrentMap().getExitPositionX() , 0, currentGame.getCurrentMap().getExitPositionZ());
		
		gl.glEnable( GL10.GL_TEXTURE_2D );              // Enable Texture Mapping
		gl.glEnable( GL10.GL_BLEND );                    // Enable Alpha Blend
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textureDoor[0]);
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glEnable(GL10.GL_DEPTH_TEST);
		gl.glDepthFunc(GL10.GL_LEQUAL);

		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
		
		gl.glFrontFace(GL10.GL_CW);
		
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, doorVertexBfr);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBuffer);

		gl.glDrawArrays(GL10.GL_TRIANGLE_FAN, 0, 4);
		gl.glDisable(GL10.GL_DEPTH_TEST);
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glDisable( GL10.GL_BLEND );  
		gl.glDisable( GL10.GL_TEXTURE_2D );
		gl.glPopMatrix();
		
		
		

		gl.glPushMatrix();
		gl.glTranslatef(currentGame.getHero().getX() , 0, currentGame.getHero().getZ());
		gl.glColor4f(0,1,1,0.5f);
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, heroVertexBfr);
		gl.glDrawArrays(GL10.GL_TRIANGLE_FAN, 0, 4);
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glPopMatrix();
		gl.glPopMatrix();
		/* Finish to draw the scene */
		
		
		
		
		
		
		/* Start text stuff */
		gl.glPushMatrix();
		gl.glTranslatef(-110, 110, -200);
		gl.glEnable( GL10.GL_TEXTURE_2D );              // Enable Texture Mapping
		gl.glEnable( GL10.GL_BLEND );                    // Enable Alpha Blend
		gl.glBlendFunc( GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA );  // Set Alpha Blend Function
		// TEST: render the entire font texture
		gl.glColor4f( 1.0f, 1.0f, 1.0f, 1.0f );         // Set Color to Use

		// TEST: render some strings with the font
		glText.begin( 1.0f, 1.0f, 1.0f, 1.0f );         // Begin Text Rendering (Set Color WHITE)
		glText.draw( "" + currentGame.getHero().getNbBullets(), 0, 20 );          // Draw Test String
		glText.draw( "" + currentGame.getHero().getNbBombs(), 0, 0 );          // Draw Test String
		glText.draw( "" + currentGame.getHero().getNbBombs(), 0, -20 );          // Draw Test String
		glText.draw( "" + currentGame.getHero().getNbLifes(), 0, -40 );          // Draw Test String
		if (this.showFPS)
		{
			glText.draw( fps + "fps", 190, 20 );          // Draw Test String
		}
		glText.end();
		
		
		gl.glPopMatrix();

		iconDirectionLeft.draw(gl);
		
		iconDirectionDown.draw(gl);
		iconDirectionRight.draw(gl);
		iconDirectionUp.draw(gl);
		iconGun.draw(gl);
		iconBomb.draw(gl);
		iconBigBomb.draw(gl);
		iconCameraLeft.draw(gl);
		iconCameraRight.draw(gl);
		iconZoomIn.draw(gl);
		iconZoomOut.draw(gl);
		iconGunSmall.draw(gl);
		iconBombSmall.draw(gl);
		iconLifeSmall.draw(gl);
		iconBigBombSmall.draw(gl);
		
		
		gl.glDisable( GL10.GL_BLEND);
		gl.glDisable( GL10.GL_TEXTURE_2D);
	}  

	public void onSurfaceChanged(GL10 gl, int width, int height) {
		this.screenWidth  = width;
		this.screenHeight = height;

		if (height == 0)
		{
			height = 1;
		}
		// Setup orthographic projection

		gl.glMatrixMode( GL10.GL_PROJECTION );       
		gl.glViewport(0, 0, width, height);

		gl.glLoadIdentity();
		GLU.gluPerspective(gl,90.0f, (float)width / (float)height , 0.1f, 200.0f);


	}


	public void moveLeft ()
	{

		this.currentCameraMove = CAMERA_MOVE_LEFT;
	}

	public void moveRight ()
	{

		this.currentCameraMove = CAMERA_MOVE_RIGHT;
	} 

	public void zoomIn ()
	{
		this.currentCameraZoom = CAMERA_ZOOM_IN;


	}


	public void zoomOut ()
	{
		this.currentCameraZoom = CAMERA_ZOOM_OUT;
		this.camZ = this.camZ + 0.2f; 
	}


	public void updateMove ()
	{
		switch (this.currentCameraMove)
		{
		case CAMERA_MOVE_LEFT:
		{
			if (this.camAngle >= 45)
			{
				return;
			}
			if (camX < this.currentGame.getCurrentMap().getMapWidth() / 2)
			{
				//Log.i("CAM", "first case");
				this.camAngle = this.camAngle + 2;
				this.camX     = this.camX - 0.2f;
				this.camZ     = this.camZ - 0.1f;
			}
			else
			{
				//Log.i("CAM", "second case");
				this.camAngle = this.camAngle + 2;
				this.camX     = this.camX - 0.2f;
				this.camZ     = this.camZ + 0.1f;
			}
			break;
		}

		case CAMERA_MOVE_RIGHT:
		{
			if (this.camAngle <= -45)
			{
				return;
			}

			if (camX < this.currentGame.getCurrentMap().getMapWidth() / 2)
			{
				//Log.i("CAM", "first case");
				this.camAngle = this.camAngle - 2;
				this.camX     = this.camX + 0.2f;
				this.camZ     = this.camZ + 0.1f;
			}
			else
			{
				//Log.i("CAM", "second case");
				this.camAngle = this.camAngle - 2;
				this.camX     = this.camX + 0.2f;
				this.camZ     = this.camZ - 0.1f;
			}
			break;
		}
		}
	}


	public void updateZoom ()
	{
		switch (this.currentCameraZoom)
		{
		case CAMERA_ZOOM_IN:
		{
			this.camZ = this.camZ - 0.2f;
			break;
		}

		case CAMERA_ZOOM_OUT:
		{
			this.camZ = this.camZ + 0.2f;
			break;
		}
		}
	}

}
