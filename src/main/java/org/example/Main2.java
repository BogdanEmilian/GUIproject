package org.example;

import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLProfile;
import javax.swing.*;
import java.awt.*;

public class Main2 {
	public static final int SCREEN_WIDTH = 1000;
	public static final int SCREEN_HEIGHT = 1000;

	public static void main(String[] args) {
		try{
			final GLProfile profile = GLProfile.get(GLProfile.GL2);
			GLCapabilities capabilities = new GLCapabilities(profile);
			Canvas canvas = new Canvas(SCREEN_WIDTH, SCREEN_HEIGHT, capabilities);
			JFrame frame = new JFrame("Project");
			frame.getContentPane().add(canvas, BorderLayout.CENTER);
			frame.setSize(frame.getContentPane().getPreferredSize());
			frame.setResizable(true);
			frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
			frame.setVisible(true);

			canvas.requestFocus();
		}catch (Exception e){
			e.printStackTrace();
		}
	}
}
