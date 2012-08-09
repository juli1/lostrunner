package org.gunnm.lostrunner.graphics;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Calendar;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import org.gunnm.lostrunner.Main;
import org.gunnm.lostrunner.model.Cube;
import org.gunnm.lostrunner.model.Game;
import org.gunnm.lostrunner.model.Hero;
import org.gunnm.lostrunner.model.Warp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLU;
import android.opengl.GLUtils;

public class LostRenderer implements Renderer 
{
	private FloatBuffer ground;
	private FloatBuffer warpBuffer;
	public float camX = 0;
	public float camY = 0;
	public float camZ = 0;
	public float camAngle;
	private int screenWidth;
	private int screenHeight;
	
	LostIcon iconDirectionLeft;
	LostIcon iconDirectionRight;
	LostIcon iconDirectionUp;
	LostIcon iconDirectionDown;
	LostIcon iconLifeSmall;

	LostIcon iconZoomIn;
	LostIcon iconZoomOut;
	LostIcon iconCameraLeft;
	LostIcon iconCameraRight;
	
	/*
	LostIcon iconGun;
	LostIcon iconBomb;
	LostIcon iconBigBomb;
	LostIcon iconGunSmall;
	LostIcon iconBombSmall;
	LostIcon iconBigBombSmall;
	*/
	
	private static FloatBuffer[] 	heroBodyVertexBfr;
	private static FloatBuffer[] 	heroMemberVertexBfr;
	private static FloatBuffer[] 	cubeVertexBfr;
	private static FloatBuffer 		doorVertexBfr;
	private Main mainActivity;

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

	
	private boolean scoreNotified;
	
	private int[] 		textureGround 	= new int[1];
	private int[] 		textureDoor 	= new int[1];
	private int[] 		textureCube 	= new int[1];
	private int[] 		textureWarp 	= new int[1];
	private int[] 		textureFace 	= new int[1];
	
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
	
	/*
	 * This is the cube coordinate, we specify
	 * the vertices for each face of the cube.
	 */
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
	
	
	/*
	 * Coordinates for each vertices of the body of the hero.
	 */
	private static float[][] heroBodyCoords = new float[][] {
		new float[] { //top
				0.1f, 0.5f,-0.1f,
				-0.1f, 0.5f,-0.1f,
				-0.1f, 0.5f, 0.1f,
				0.1f, 0.5f, 0.1f
		},
		new float[] { //bottom
				0.1f,-0.5f, 0.1f,
				-0.1f,-0.5f, 0.1f,
				-0.1f,-0.5f,-0.1f,
				0.1f,-0.5f,-0.1f
		},
		new float[] { //front
				0.1f, 0.5f, 0.1f,
				-0.1f, 0.5f, 0.1f,
				-0.1f,-0.5f, 0.1f,
				0.1f,-0.5f, 0.1f
		},
		new float[] { //back
				0.1f,-0.5f,-0.1f,
				-0.1f,-0.5f,-0.1f,
				-0.1f, 0.5f,-0.1f,
				0.1f, 0.5f,-0.1f
		},
		new float[] { //left
				-0.1f, 0.5f, 0.1f,
				-0.1f, 0.5f,-0.1f,
				-0.1f,-0.5f,-0.1f,
				-0.1f,-0.5f, 0.1f
		},
		new float[] {//right
				0.1f, 0.5f,-0.1f,
				0.1f, 0.5f, 0.1f,
				0.1f,-0.5f, 0.1f,
				0.1f,-0.5f,-0.1f
		},
	};
	
	private static float[][] heroMemberCoords = new float[][] {
		new float[] { 
				0.1f  , 0 ,-0.1f,
				-0.1f , 0 ,-0.1f,
				-0.1f , 0 , 0.1f,
				0.1f  , 0 , 0.1f
		},
		new float[] { 
				0.1f  ,-0.4f ,  0.1f,
				-0.1f ,-0.4f ,  0.1f,
				-0.1f ,-0.4f , -0.1f,
				0.1f  ,-0.4f , -0.1f
		},
		new float[] { 
				0.1f  ,     0, 0.1f,
				-0.1f ,     0, 0.1f,
				-0.1f , -0.4f, 0.1f,
				0.1f  , -0.4f, 0.1f
		},
		new float[]
		{ 
				0.1f  ,-0.4f ,-0.1f,
				-0.1f ,-0.4f ,-0.1f,
				-0.1f , 0    ,-0.1f,
				0.1f  , 0    ,-0.1f
		},
		new float[]
		{ 
				-0.1f  , 0     , 0.1f,
				-0.1f  , 0     ,-0.1f,
				-0.f   ,-0.4f  ,-0.1f,
				-0.1f  ,-0.4f  , 0.1f
		},
		new float[]
		{
				0.1f , 0    ,-0.1f,
				0.1f , 0    , 0.1f,
				0.1f ,-0.4f , 0.1f,
				0.1f ,-0.4f ,-0.1f
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




	public LostRenderer (Main c, Game g)
	{
		super ();
		this.currentCameraMove 	= CAMERA_MOVE_NONE;
		this.currentCameraZoom	= CAMERA_ZOOM_NONE;
		this.currentGame 		= g;
		this.mainActivity		= c;
		this.startTime 			= Calendar.getInstance().getTimeInMillis();
		this.nbFrames 			= 0;
		this.scoreNotified 		= false;
		
		
		screenWidth				= 0;
		screenHeight			= 0;
		
		camX = 0;
		camY = 4.5f;
		camZ = 2;
		camAngle = 25;

		cubeVertexBfr 		= new FloatBuffer[6];
		textureCubeBuffer 	= new FloatBuffer[6];
		heroBodyVertexBfr 	= new FloatBuffer[6];
		heroMemberVertexBfr = new FloatBuffer[6];
		
		for (int i = 0 ; i < 6 ; i++)
		{
			cubeVertexBfr[i] 		= makeFloatBuffer (cubeCoords[i]);
			textureCubeBuffer[i] 	= makeFloatBuffer (textureCubeCoords[i]);
			heroMemberVertexBfr[i]	= makeFloatBuffer (heroMemberCoords[i]);
			heroBodyVertexBfr[i] 	= makeFloatBuffer (heroBodyCoords[i]);
		}
		doorVertexBfr = makeFloatBuffer(doorCoords);

		warpBuffer = makeFloatBuffer(new float[]{0 , 1 , 0,
												 1 , 1 , 0,
												 1 , 0 , 0,
												 0 , 0 , 0});
		
		textureBuffer = ByteBuffer.allocateDirect(texture.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
		textureBuffer.put(texture);
		textureBuffer.position(0);

		iconDirectionLeft = new LostIcon 	(c, "direction-left.png"	, -4.5f	, -6.5f, -10.0f);
		iconDirectionDown = new LostIcon 	(c, "direction-down.png"	, -1.5f	, -9.0f, -10.0f);
		iconDirectionUp = new LostIcon 		(c, "direction-up.png"		, -1.5f	, -4.0f, -10.0f);
		iconDirectionRight = new LostIcon 	(c, "direction-right.png"	,  1.5f	, -6.5f, -10.0f);

		iconLifeSmall = new LostIcon 	(c, "life.png"		, -4.5f, 7.0f, -10.0f, LostIcon.ICON_SMALL);
		
		
		iconZoomIn = new LostIcon 		(c, "zoomin.png"		,  4.5f, 8.5f, -10.0f , LostIcon.ICON_MEDIUM);
		iconZoomOut = new LostIcon 		(c, "zoomout.png"		,  2.0f, 8.5f, -10.0f , LostIcon.ICON_MEDIUM);
		iconCameraLeft = new LostIcon 	(c, "camleft.png"		, -4.5f, 8.5f, -10.0f , LostIcon.ICON_MEDIUM);
		iconCameraRight = new LostIcon 	(c, "camright.png"		, -2.0f, 8.5f, -10.0f , LostIcon.ICON_MEDIUM);
	}

	public void loadGLTexture(GL10 gl, String filename, int[] textures) 
	{
		
		Bitmap bitmap;
		try 
		{
			bitmap = BitmapFactory.decodeStream(mainActivity.getAssets().open(filename));
		} 
		catch (IOException e) 
		{
//			Log.i("LostIcon", "Error when trying to load texture" + filename);
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
		iconDirectionLeft.loadGLTexture(gl, mainActivity);
		iconDirectionDown.loadGLTexture(gl, mainActivity);
		iconDirectionUp.loadGLTexture(gl, mainActivity);
		iconDirectionRight.loadGLTexture(gl, mainActivity);

		iconZoomIn.loadGLTexture(gl, mainActivity);
		iconZoomOut.loadGLTexture(gl, mainActivity);
		iconCameraLeft.loadGLTexture(gl, mainActivity);
		iconCameraRight.loadGLTexture(gl, mainActivity);
		iconLifeSmall.loadGLTexture(gl, mainActivity);
		
		/*
		iconGun.loadGLTexture(gl, mainActivity);
		iconBomb.loadGLTexture(gl, mainActivity);
		iconBigBomb.loadGLTexture(gl, mainActivity);
		iconGunSmall.loadGLTexture(gl, mainActivity);
		iconBombSmall.loadGLTexture(gl, mainActivity);
		iconBigBombSmall.loadGLTexture(gl, mainActivity);
		*/
		
		
		loadGLTexture(gl, "warp.png", textureWarp);
		loadGLTexture(gl, "metal2.png", textureCube);
		loadGLTexture(gl, "ground4.png", textureGround);
		loadGLTexture(gl, "door2.png", textureDoor);
		loadGLTexture(gl, "face.png", textureFace);
		// Create the GLText
		glText = new GLText( gl, mainActivity.getAssets() );
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

		ground = makeFloatBuffer(new float[]
				{	0	,	0	,	0,
					1	,	0	,	0,
					1	,	0	,	-1,
					0	,	0	,	-1});
	}

	public void onDrawFrame(GL10 gl) {
		long fps = 0;
		long currentTime;
		
		currentTime = Calendar.getInstance().getTimeInMillis();
		
		if (currentGame.isCompleted() && ( ! scoreNotified ))
		{
			if ( (currentTime - currentGame.getCompletedTime()) > 5000)
			{
				mainActivity.finish();
			}
		}
		
		if (this.showFPS)
		{
			this.nbFrames = this.nbFrames + 1;
			
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

		gl.glMatrixMode( GL10.GL_PROJECTION );       
		gl.glViewport(0, 0, screenWidth, screenHeight);

		gl.glLoadIdentity();
		GLU.gluPerspective(gl,90.0f, (float)screenWidth / (float)screenHeight , 0.1f, 200.0f);

		gl.glMatrixMode( GL10.GL_MODELVIEW );       
		gl.glLoadIdentity();   
		
		if (currentGame.isCompleted())
		{
			gl.glPushMatrix();
			gl.glTranslatef(-50, 80, -200);
			gl.glEnable( GL10.GL_TEXTURE_2D );
			gl.glEnable( GL10.GL_BLEND );
			gl.glBlendFunc( GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA );
			gl.glColor4f( 1.0f, 1.0f, 1.0f, 1.0f );
			glText.begin( 1.0f, 1.0f, 1.0f, 1.0f );
			glText.draw( "Congratulations !", 0, 0 );
			glText.end();
			gl.glPopMatrix();
		}
		
		
		currentGame.update();

		gl.glPushMatrix();

		/* Camera management */
		gl.glRotatef(20, 1, 0, 0);
		gl.glRotatef(camAngle, 0, 1, 0);
		gl.glTranslatef(-camX, -camY, -camZ);
		/* End of camera management */
		
		
		/* Start to display the ground */
		gl.glColor4f(1, 1, 1, 1);
		gl.glEnable( GL10.GL_TEXTURE_2D );
		gl.glEnable( GL10.GL_BLEND );  
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textureGround[0]);
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);
		
		gl.glFrontFace(GL10.GL_CW);
		
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, ground);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBuffer);

		for (int i = 0 ; i < currentGame.getCurrentMap().getMapWidth() ; i++)
		{
			for (int j = 0 ; j < currentGame.getCurrentMap().getMapDepth() ; j++)
			{
				gl.glPushMatrix();
				gl.glTranslatef(i, 0, -1 * j);
				
				gl.glDrawArrays(GL10.GL_TRIANGLE_FAN, 0, 4);


				gl.glPopMatrix();
			}
		}
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glDisable( GL10.GL_BLEND );  
		gl.glDisable( GL10.GL_TEXTURE_2D );
		/* Display the ground completed */
		
		/* Start to display the Warp zones */
		gl.glColor4f(1,1 ,1, 1);	

		gl.glEnable( GL10.GL_TEXTURE_2D );         
		gl.glEnable( GL10.GL_BLEND ); 
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textureWarp[0]);
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);
		
		gl.glFrontFace(GL10.GL_CW);
		
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, warpBuffer);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBuffer);

		for (int i = 0 ; i < currentGame.getCurrentMap().getNbWarps() ; i++)
		{
			Warp warp = currentGame.getWarp(i);
			gl.glPushMatrix();
			gl.glTranslatef(warp.getX(), 0, warp.getZ());
			if (warp.getDirection() == Warp.WARP_TYPE_HORIZONTAL)
			{
				gl.glRotatef(90, 0, 1, 0);
			}
			gl.glDrawArrays(GL10.GL_TRIANGLE_FAN, 0, 4);
			gl.glPopMatrix();
		}

		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);

		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glDisable( GL10.GL_BLEND );  
		gl.glDisable( GL10.GL_TEXTURE_2D );
		
		/* End displaying Warp zones */
		
		
		/* Display the cubes */
		gl.glEnable( GL10.GL_TEXTURE_2D );              // Enable Texture Mapping
		gl.glEnable( GL10.GL_BLEND );                    // Enable Alpha Blend

		gl.glBindTexture(GL10.GL_TEXTURE_2D, textureCube[0]);

		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glEnable(GL10.GL_DEPTH_TEST);
		gl.glDepthFunc(GL10.GL_LEQUAL);
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);
		
		gl.glFrontFace(GL10.GL_CW);
		
		for (int i = 0 ; i < currentGame.getCurrentMap().getNbCubes() ; i++)
		{
			if (currentGame.getCube(i).isVisible() == false)
			{
				continue;
			}

			gl.glPushMatrix();

			gl.glTranslatef(currentGame.getCube(i).getX() + 0.5f, 0.5f, currentGame.getCube(i).getZ());

			if (currentGame.getCube(i).getType() == Cube.TYPE_VERTICAL)
			{
				gl.glRotatef(currentGame.getCube(i).getRotation(), 1, 0, 0);
			}
			if (currentGame.getCube(i).getType() == Cube.TYPE_HORIZONTAL)
			{
				gl.glRotatef(currentGame.getCube(i).getRotation(), 0, 0, 1);
			}
			
			for (int k = 0; k < 6; k++)
			{
				
				gl.glVertexPointer(3, GL10.GL_FLOAT, 0, cubeVertexBfr[k]);
				gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureCubeBuffer[k]);

				gl.glDrawArrays(GL10.GL_TRIANGLE_FAN, 0, 4);

			}

			gl.glPopMatrix();

		}
		gl.glDisable(GL10.GL_DEPTH_TEST);
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glDisable( GL10.GL_BLEND );  
		gl.glDisable( GL10.GL_TEXTURE_2D );
		/* end of displaying cubes */
		
		
		/* Display the door */
		gl.glPushMatrix();
		gl.glTranslatef(currentGame.getCurrentMap().getExitPositionX() , 0, currentGame.getCurrentMap().getExitPositionZ());
		
		gl.glEnable( GL10.GL_TEXTURE_2D );
		gl.glEnable( GL10.GL_BLEND );
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
		/* end of door display */
		
		
		
		/* Draw the hero */
		gl.glPushMatrix();
		gl.glTranslatef(currentGame.getHero().getX() , 0, currentGame.getHero().getZ());
		drawHero (gl);
		gl.glPopMatrix();
		/* Finish to draw the hero */
		gl.glPopMatrix();
		/* Finish to draw the scene */
		

		/* Start text stuff */
		gl.glPushMatrix();
		gl.glTranslatef(-110, 110, -200);
		gl.glEnable( GL10.GL_TEXTURE_2D );              // Enable Texture Mapping
		gl.glEnable( GL10.GL_BLEND );                    // Enable Alpha Blend
		gl.glBlendFunc( GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA );  // Set Alpha Blend Function
		// TEST: render the entire font texture

		// TEST: render some strings with the font
		glText.begin( 1.0f, 1.0f, 1.0f, 1.0f );
		glText.draw( "" + currentGame.getHero().getNbLifes(), 35, 20 );


		glText.draw( currentGame.getElapsedSec() + " s", 190, 20 );
		
		if (this.showFPS)
		{
			glText.draw( fps + "fps", 190, 0 );
		}
		glText.end();
		gl.glPopMatrix();
		/* End of text stuff */

        /* Display icons */
        gl.glShadeModel(GL10.GL_FLAT);
        gl.glEnable(GL10.GL_BLEND);
        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
        gl.glColor4x(0x10000, 0x10000, 0x10000, 0x10000);
        gl.glEnable(GL10.GL_TEXTURE_2D);
		iconDirectionLeft.draw(gl);
		iconDirectionDown.draw(gl);
		iconDirectionRight.draw(gl);
		iconDirectionUp.draw(gl);
		iconCameraLeft.draw(gl);
		iconCameraRight.draw(gl);
		iconZoomIn.draw(gl);
		iconZoomOut.draw(gl);
		iconLifeSmall.draw(gl);
		
		gl.glDisable( GL10.GL_BLEND);
		gl.glDisable( GL10.GL_TEXTURE_2D);
		/* End of icons display */
	}  

	public void onSurfaceChanged(GL10 gl, int width, int height) 
	{
		if (height == 0)
		{
			height = 1;
		}
		screenWidth = width;
		screenHeight = height;
		
//		Log.i("Renderer", "onSurfaceChanged, width=" + width + "; height=" + height);
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

	
	public void drawHero (GL10 gl)
	{
		gl.glColor4f(1,1,1,1.0f);
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnable(GL10.GL_DEPTH_TEST);
		gl.glDepthFunc(GL10.GL_LEQUAL);
		switch (currentGame.getHero().getDirection())
		{
			case Hero.DIRECTION_LEFT:
			{
				gl.glRotatef(90, 0, 1, 0);
				break;
			}
			case Hero.DIRECTION_RIGHT:
			{
				gl.glRotatef(-90, 0, 1, 0);
				break;
			}
			case Hero.DIRECTION_DOWN:
			{
				gl.glRotatef(-180, 0, 1, 0);
				break;
			}
		}
		
		gl.glTranslatef(0, 0.5f, 0);
		
		
		for (int k = 0; k < 6; k++)
		{
			if (k == 3)
			{
				gl.glEnable( GL10.GL_TEXTURE_2D );              // Enable Texture Mapping
				gl.glEnable( GL10.GL_BLEND );                    // Enable Alpha Blend

				gl.glBindTexture(GL10.GL_TEXTURE_2D, textureFace[0]);

				gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

				gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
				
				gl.glFrontFace(GL10.GL_CW);
				
				gl.glVertexPointer(3, GL10.GL_FLOAT, 0, heroBodyVertexBfr[k]);

				gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBuffer);

				gl.glDrawArrays(GL10.GL_TRIANGLE_FAN, 0, 4);
				gl.glDisable( GL10.GL_BLEND ); 
				gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
 
				gl.glDisable( GL10.GL_TEXTURE_2D );
			}
			else
			{
				gl.glVertexPointer(3, GL10.GL_FLOAT, 0, heroBodyVertexBfr[k]);
				gl.glDrawArrays(GL10.GL_TRIANGLE_FAN, 0, 4);
			}
		}
		
		// Draw Right leg
		gl.glPushMatrix();
		gl.glTranslatef(0.2f, -0.5f, 0);
		gl.glRotatef(currentGame.getHero().getLegsAngles() , 1, 0, 0);
		for (int k = 0; k < 6; k++)
		{
			gl.glVertexPointer(3, GL10.GL_FLOAT, 0, heroMemberVertexBfr[k]);
			gl.glDrawArrays(GL10.GL_TRIANGLE_FAN, 0, 4);
		}
		gl.glPopMatrix();
		// Draw Left leg
		gl.glPushMatrix();
		gl.glTranslatef(-0.2f, -0.5f, 0);
		gl.glRotatef(currentGame.getHero().getLegsAngles() * -1, 1, 0, 0);
		for (int k = 0; k < 6; k++)
		{
			gl.glVertexPointer(3, GL10.GL_FLOAT, 0, heroMemberVertexBfr[k]);
			gl.glDrawArrays(GL10.GL_TRIANGLE_FAN, 0, 4);
		}
		gl.glPopMatrix();
		
		
		// Draw Left arm
		gl.glPushMatrix();
		gl.glTranslatef(-0.2f, 0.3f, 0);
		gl.glRotatef(currentGame.getHero().getArmsAngles() * -1, 1, 0, 0);
		for (int k = 0; k < 6; k++)
		{
			gl.glVertexPointer(3, GL10.GL_FLOAT, 0, heroMemberVertexBfr[k]);
			gl.glDrawArrays(GL10.GL_TRIANGLE_FAN, 0, 4);
		}
		gl.glPopMatrix();
		
		
		// Draw Right arm
		gl.glPushMatrix();
		gl.glTranslatef(0.2f, 0.3f, 0);
		gl.glRotatef(currentGame.getHero().getArmsAngles(), 1, 0, 0);
		for (int k = 0; k < 6; k++)
		{
			gl.glVertexPointer(3, GL10.GL_FLOAT, 0, heroMemberVertexBfr[k]);
			gl.glDrawArrays(GL10.GL_TRIANGLE_FAN, 0, 4);
		}
		gl.glPopMatrix();
		
		gl.glDrawArrays(GL10.GL_TRIANGLE_FAN, 0, 4);
		gl.glDisable(GL10.GL_DEPTH_TEST);	
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
	}
	
}
