package org.gunnm.lostrunner.graphics;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;

public class LostIcon {

	private String 		filename;
	private Context     context;
	private FloatBuffer vertexBuffer;   // buffer holding the vertices

	private float vertices[] = {
			1, 1, 0,
			-1, 1, 0,
			-1,-1, 0,
			1,-1, 0
	};	
	
	public LostIcon (Context c, String f) 
	{
		context = c;
		filename = f;
		ByteBuffer vertexByteBuffer = ByteBuffer.allocateDirect(vertices.length * 4);
		vertexByteBuffer.order(ByteOrder.nativeOrder());
		vertexBuffer = vertexByteBuffer.asFloatBuffer();
		vertexBuffer.put(vertices);

		vertexBuffer.position(0);
	}

	public void draw(GL10 gl) 
	{
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);

		// set the colour for the square
		gl.glColor4f(0.0f, 1.0f, 0.0f, 0.5f);

		// Point to our vertex buffer
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);

		// Draw the vertices as triangle strip
		gl.glDrawArrays(GL10.GL_TRIANGLE_FAN, 0, 4);

		//Disable the client state before leaving
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
	}
}
