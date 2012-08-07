package org.gunnm.lostrunner.graphics;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import org.gunnm.lostrunner.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;
import android.util.Log;

public class LostIcon {

	private String 		filename;
	private Context     context;
	private FloatBuffer vertexBuffer;   // buffer holding the vertices
	private float 		translationX;
	private float 		translationY;
	private float 		translationZ;
	private int[] 		textures 	= new int[1];
	private int 		type;
	private int			form;
	private float		fade;
	
	public final static int ICON_SMALL 		= 1;
	public final static int ICON_MEDIUM 	= 2;
	public final static int ICON_BIG 		= 3;
	public final static int FORM_SQUARE     = 1;
	public final static int FORM_RECTANGLE  = 2;
	
	private float verticesSquare[] = {
			 1, 1, 0,
			-1, 1, 0,
			-1,-1, 0,
			 1,-1, 0
	};	
	

	private float verticesRectangle[] = {
			1   ,  2, 0,
			-1  ,  2, 0,
			-1  , -1, 0,
			1   , -1, 0
	};	

	private FloatBuffer textureBuffer;  // buffer holding the texture coordinates

	private float texture[] = {
			1.0f, 0.0f,     // bottom right (V3)
			0.0f, 0.0f,     // top right    (V4)
			0.0f, 1.0f,     // bottom left  (V1)
			1.0f, 1.0f,     // top left     (V2)	
	};
	
	public LostIcon (Context c, String f, float tx, float ty, float tz, int t)
	{
		this (c,f,tx,ty,tz,t,FORM_SQUARE);
	}
	
	public LostIcon (Context c, String f, float tx, float ty, float tz, int t, int fo)
	{
		int divider;
		
		float[] vertices;
		ByteBuffer vertexByteBuffer;
		ByteBuffer byteBuffer;

		this.fade 		= 1;
		this.context 	= c;
		this.filename 	= f;
		
		this.form 		= fo;
		this.type 		= t;
		
		vertices		= null;
		
		switch (this.type)
		{
			case ICON_SMALL:
			{
				divider = 3;
				break;
			}
			case ICON_MEDIUM:
			{
				divider = 2;
				break;
			}
			default:
			{
				divider = 1;
				break;
			}
		}
		
		switch (fo)
		{
			case FORM_RECTANGLE:
			{
				vertices = verticesRectangle;
				break;
			}
			
			default:
			{
				vertices = verticesSquare;
				break;
			}
		}
		
		for (int i = 0 ; i < vertices.length ; i++)
		{
			vertices[i] = vertices[i] / divider;
		}

		vertexByteBuffer= ByteBuffer.allocateDirect(vertices.length * 4);
		vertexByteBuffer.order(ByteOrder.nativeOrder());
		vertexBuffer = vertexByteBuffer.asFloatBuffer();
		vertexBuffer.put(vertices);

		byteBuffer = ByteBuffer.allocateDirect(texture.length * 4);
		byteBuffer.order(ByteOrder.nativeOrder());
		textureBuffer = byteBuffer.asFloatBuffer();
		textureBuffer.put(texture);
		textureBuffer.position(0);

		this.translationX = tx;
		this.translationY = ty;
		this.translationZ = tz;
		vertexBuffer.position(0);
		
		
	}
	
	public LostIcon (Context c, String f, float tx, float ty, float tz) 
	{

		this (c, f, tx, ty, tz, ICON_BIG);
	}

	public void loadGLTexture(GL10 gl, Context context) 
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

	public void setFade (float f)
	{
		this.fade = f;
	}

	public void draw(GL10 gl) 
	{
		gl.glPushMatrix();
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]);
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

	    gl.glColor4f( 1.0f, 1.0f, 1.0f, this.fade );
		
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
		
		gl.glFrontFace(GL10.GL_CW);
		gl.glTranslatef(translationX, translationY, translationZ);

		// Point to our vertex buffer
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);

		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBuffer);
		// Draw the vertices as triangle strip
		gl.glDrawArrays(GL10.GL_TRIANGLE_FAN, 0, 4);


		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glPopMatrix();
	}
}
