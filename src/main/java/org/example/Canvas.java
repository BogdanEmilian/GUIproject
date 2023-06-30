package org.example;

import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureData;
import com.jogamp.opengl.util.texture.TextureIO;

import javax.media.opengl.*;
import javax.media.opengl.awt.GLCanvas;
import javax.media.opengl.fixedfunc.GLPointerFunc;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.GLUquadric;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.util.ArrayList;

public class Canvas extends GLCanvas implements GLEventListener, KeyListener {

	private static final float SUN_RADIUS = 30f;
	private FPSAnimator animator;
	private GLU glu;
	private Texture earthTexture;
	private Texture skyTexture;
	private Texture cloudsTexture;
	private Texture moonTexture;
	private ArrayList<Planet> planets;

	private float Angle = 0;
	private float earthAngle = 0;
	private float systemAngle = 0;
	private Sun sun;

	public Canvas(int width, int height, GLCapabilities capabilities) {
		super(capabilities);
		setSize(width, height);
		addGLEventListener(this);
	}

	/*
	* Textures provided by:
	* https://www.freepik.com/free-photo/beautiful-shining-stars-night-sky_7631083.htm#query=stars&position=1&from_view=search&track=sph
	* https://www.solarsystemscope.com/textures/
	*/
	@Override
	public void init(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();
		glu = new GLU();
		planets = new ArrayList<>();
		gl.glDisable(GL.GL_DEPTH_TEST);
		gl.glShadeModel(GL2.GL_SMOOTH);
		gl.glClearColor(0f, 0f, 0f, 0f);
		gl.glHint(GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST);
		this.addKeyListener(this);
		animator = new FPSAnimator(this, 60);
		animator.start();

		// Earth
		String textureFile = "F://Projects//GUIproject//src//main//resources//textures//earth.jpg";
		earthTexture = getObjectTexture(gl, textureFile);
		textureFile = "F://Projects//GUIproject//src//main//resources//textures//galaxy.jpg";
		skyTexture = getObjectTexture(gl, textureFile);
		textureFile = "F://Projects//GUIproject//src//main//resources//textures//clouds.png";
		cloudsTexture = getObjectTexture(gl, textureFile);

		// Sun
		textureFile = "F://Projects//GUIproject//src//main//resources//textures//sun.jpg";
		this.sun = new Sun(gl, glu, getObjectTexture(gl, textureFile));

		// Mercury
		textureFile = "F://Projects//GUIproject//src//main//resources//textures//mercury.jpg";
		Planet mercury = new Planet(getObjectTexture(gl, textureFile), gl, glu, SUN_RADIUS + 2f, 0.15f, 2.50f);

		// Venus
		textureFile = "F://Projects//GUIproject//src//main//resources//textures//venus.jpg";
		Planet venus = new Planet(getObjectTexture(gl, textureFile), gl, glu, SUN_RADIUS + 12f, 0.25f, 4.00f);

		// Jupiter
		textureFile = "F://Projects//GUIproject//src//main//resources//textures//jupiter.jpg";
		Planet jupiter = new Planet(getObjectTexture(gl, textureFile), gl, glu, SUN_RADIUS + 65f, 0.35f, 11.00f);

		// Mars
		textureFile = "F://Projects//GUIproject//src//main//resources//textures//mars.jpg";
		Planet mars = new Planet(getObjectTexture(gl, textureFile), gl, glu, SUN_RADIUS + 50f, 0.45f, 3.00f);

		// Moon
		textureFile = "F://Projects//GUIproject//src//main//resources//textures//moon.png";
		moonTexture = getObjectTexture(gl, textureFile);

		// Saturn
		textureFile = "F://Projects//GUIproject//src//main//resources//textures//saturn.jpg";
		Planet saturn = new Planet(getObjectTexture(gl, textureFile), gl, glu, SUN_RADIUS + 90f, 0.55f, 10.00f);

		// Uranus
		textureFile = "F://Projects//GUIproject//src//main//resources//textures//uranus.jpg";
		Planet uranus = new Planet(getObjectTexture(gl, textureFile), gl, glu, SUN_RADIUS + 105f, 0.65f, 8.00f);

		// Neptune
		textureFile = "F://Projects//GUIproject//src//main//resources//textures//neptune.jpg";
		Planet neptune = new Planet(getObjectTexture(gl, textureFile), gl, glu, SUN_RADIUS + 120f, 0.75f, 7.50f);

		planets.add(mercury);
		planets.add(venus);
		planets.add(mars);
		planets.add(jupiter);
		planets.add(saturn);
		planets.add(uranus);
		planets.add(neptune);
	}

	@Override
	public void dispose(GLAutoDrawable glAutoDrawable) {
	}

	@Override
	public void display(GLAutoDrawable glAutoDrawable) {
		if (!animator.isAnimating()) {
			return;
		}
		final GL2 gl = glAutoDrawable.getGL().getGL2();
		setCamera(gl, 250);
		setLights(gl);
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

		gl.glEnable(GL2.GL_BLEND);
		gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);

		sun.display();
		drawEarthAndMoon(gl);
		for (Planet p : planets)
			p.display();
		skyTexture.bind(gl);
		skyTexture.enable(gl);
		drawCube(gl);
	}

	private void drawEarthAndMoon(GL2 gl) {
		gl.glPushMatrix();
		systemAngle = (systemAngle + 0.5f) % 360f;
		final float distance = SUN_RADIUS + 40f;
		final float x = (float) Math.sin(Math.toRadians(systemAngle)) * distance;
		final float y = (float) Math.cos(Math.toRadians(systemAngle)) * distance;
		final float z = 0;
		gl.glTranslatef(x, y, z);
		drawEarth(gl);
		drawMoon(gl);
		gl.glPopMatrix();
	}

	private void drawMoon(GL2 gl) {
		gl.glPushMatrix();
		moonTexture.enable(gl);
		moonTexture.bind(gl);
		gl.glPushName(5);
		Angle = (Angle + 1f) % 360f;
		final float distance = 12.000f;
		final float x = (float) Math.sin(Math.toRadians(Angle)) * distance;
		final int y = (int) ((float) Math.cos(Math.toRadians(Angle)) * distance);
		final float z = 0;
		gl.glTranslatef(x, y, z);
		gl.glRotatef(Angle, 0, 0, -1);
		gl.glRotatef(45f, 0, 1, 0);

		final float radius = 3.378f;
		final int slices = 16;
		final int stacks = 16;
		GLUquadric moon = glu.gluNewQuadric();
		glu.gluQuadricTexture(moon, true);
		glu.gluQuadricDrawStyle(moon, GLU.GLU_FILL);
		glu.gluQuadricNormals(moon, GLU.GLU_FLAT);
		glu.gluQuadricOrientation(moon, GLU.GLU_INSIDE);
		glu.gluSphere(moon, radius, slices, stacks);
		gl.glPopMatrix();
		gl.glPopName();
	}

	private void drawEarth(GL2 gl) {
		float[] rgba = { 1f, 1f, 1f, 1f }; // Set alpha to 0
		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT, rgba, 1);
		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_DIFFUSE, rgba, 0);
		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, rgba, 1);


		gl.glPushName(4);
		earthAngle = (earthAngle + 0.1f) % 360f;
		cloudsTexture.enable(gl);
		cloudsTexture.bind(gl);
		gl.glPushMatrix();
		gl.glRotatef(earthAngle, 0.2f, 0.1f, 0);
		final float radius = 6.378f;
		final int slices = 16;
		final int stacks = 16;
		gl.glEnable(GL.GL_BLEND);
		gl.glBlendFunc(GL.GL_SRC_COLOR, GL.GL_DST_ALPHA);

		GLUquadric clouds = glu.gluNewQuadric();
		glu.gluQuadricOrientation(clouds, GLU.GLU_OUTSIDE);
		glu.gluQuadricTexture(clouds, true);
		glu.gluSphere(clouds, 7, slices, stacks);
		earthTexture.enable(gl);
		earthTexture.bind(gl);
		gl.glDisable(GL.GL_BLEND);

		GLUquadric earth = glu.gluNewQuadric();
		glu.gluQuadricTexture(earth, true);
		glu.gluQuadricDrawStyle(earth, GLU.GLU_FILL);
		glu.gluQuadricNormals(earth, GLU.GLU_FLAT);
		glu.gluQuadricOrientation(earth, GLU.GLU_OUTSIDE);
		glu.gluSphere(earth, radius, slices, stacks);
		gl.glPopName();
		glu.gluDeleteQuadric(earth);
		glu.gluDeleteQuadric(clouds);
		gl.glPopMatrix();
	}

	private void drawCube(GL gl) {
		skyTexture.enable(gl);
		skyTexture.bind(gl);
		((GLPointerFunc) gl).glDisableClientState(GL2.GL_VERTEX_ARRAY);
		final float radius = 200f;
		final int slices = 15;
		final int stacks = 15;
		gl.glEnable(GL.GL_BLEND);
		gl.glBlendFunc(GL.GL_SRC_COLOR, GL.GL_DST_ALPHA);
		GLUquadric sky = glu.gluNewQuadric();
		glu.gluQuadricTexture(sky, true);
		glu.gluQuadricDrawStyle(sky, GLU.GLU_FILL);
		glu.gluQuadricNormals(sky, GLU.GLU_FLAT);
		glu.gluQuadricOrientation(sky, GLU.GLU_INSIDE);
		glu.gluSphere(sky, radius, slices, stacks);

		gl.glEnable(GL.GL_BLEND);
		gl.glBlendFunc(GL.GL_SRC_COLOR, GL.GL_DST_ALPHA);
	}


	private Texture getObjectTexture(GL2 gl, String fileName) {
		InputStream stream = null;
		Texture tex = null;
		String extension = fileName.substring(fileName.lastIndexOf('.'));
		try {
			stream = new FileInputStream(new File(fileName));
			TextureData data = TextureIO.newTextureData(gl.getGLProfile(), stream, false, extension);
			tex = TextureIO.newTexture(data);
		} catch (FileNotFoundException e) {
			System.err.println("Error loading");
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("IO Exception");
			e.printStackTrace();
		}
		return tex;
	}

	//TODO 3 models o light
	private void setLights(GL2 gl) {
		float SHINE_ALL_DIRECTIONS = 1;
		float[] lightPos = { 1f, 1f, 1f, SHINE_ALL_DIRECTIONS };
		float[] lightColorAmbient = { 1f, 1f, 1f, 0f };
		float[] lightColorSpecular = { 0.5f, 0.5f, 0.5f, 0f };
		gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_POSITION, lightPos, 0);
		gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_AMBIENT, lightColorAmbient, 0);
		gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_SPECULAR, lightColorSpecular, 0);
		gl.glEnable(GL2.GL_LIGHT1);
		gl.glEnable(GL2.GL_LIGHTING);
	}

	private void setCamera(GL2 gl, float distance) {
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();
		float widthHeightRatio = (float) getWidth() / (float) getHeight();
		glu.gluPerspective(50, widthHeightRatio, 1, 1000);
		glu.gluLookAt(0, 0, distance, 0, 0, 0, 0, 1, 0);
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity();
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		GL gl = drawable.getGL();
		gl.glViewport(0, 0, width, height);
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}
	}
