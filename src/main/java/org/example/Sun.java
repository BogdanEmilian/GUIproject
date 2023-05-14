package org.example;


import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.GLUquadric;

import com.jogamp.opengl.util.texture.Texture;

public class Sun {
	private GL2 gl;
	private GLU glu;
	private Texture sunTexture;
	private float angle = 0;

	public Sun(GL2 gl, GLU glu, Texture sunTexture) {
		this.gl = gl;
		this.glu = glu;
		this.sunTexture = sunTexture;
	}

	public void display() {
		float[] rgba = { 1f, 1f, 1f };
		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT, rgba, 0);
		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, rgba, 0);
		gl.glMaterialf(GL2.GL_FRONT, GL2.GL_SHININESS, 1f);
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT);
		gl.glClear(GL2.GL_DEPTH_BUFFER_BIT);
		sunTexture.enable(gl);
		sunTexture.bind(gl);
		gl.glPushName(5);
		angle = (angle + 0.5f) % 360f;
		gl.glPushMatrix();
		gl.glRotatef(angle, 0.8f, 0.1f, 0);
		GLUquadric sun = glu.gluNewQuadric();
		glu.gluQuadricTexture(sun, true);
		glu.gluQuadricDrawStyle(sun, GLU.GLU_FILL);
		glu.gluQuadricNormals(sun, GLU.GLU_FLAT);
		glu.gluQuadricOrientation(sun, GLU.GLU_OUTSIDE);
		final float radius = 20f;
		final int slices = 26;
		final int stacks = 26;
		glu.gluSphere(sun, radius, slices, stacks);
		glu.gluDeleteQuadric(sun);
		gl.glPopMatrix();
		gl.glPopName();
	}

}
