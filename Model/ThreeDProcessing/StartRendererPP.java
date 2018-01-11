package Polygen.Model.ThreeDProcessing; /**
 * Copyright 2012-2013 JogAmp Community. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are
 * permitted provided that the following conditions are met:
 *
 *    1. Redistributions of source code must retain the above copyright notice, this list of
 *       conditions and the following disclaimer.
 *
 *    2. Redistributions in binary form must reproduce the above copyright notice, this list
 *       of conditions and the following disclaimer in the documentation and/or other materials
 *       provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY JogAmp Community ``AS IS'' AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL JogAmp Community OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * The views and conclusions contained in the software and documentation are those of the
 * authors and should not be interpreted as representing official policies, either expressed
 * or implied, of JogAmp Community.
 */

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.nio.FloatBuffer;

import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.PMVMatrix;

/**
 * Performs the OpenGL graphics processing using the Programmable Pipeline and the
 * OpenGL Core profile
 *
 * Starts an animation loop.
 * Zooming and rotation of the Camera is included.
 * 	Use: left/right/up/down-keys and +/-Keys
 * Draws a simple triangle.
 * Serves as a template (start code) for setting up an OpenGL/Jogl application
 * which is using a vertex and fragment shader.
 *
 * Please make sure setting the file path and names of the shader correctly (see below).
 *
 *
 * Based on a tutorial by Chua Hock-Chuan
 * http://www3.ntu.edu.sg/home/ehchua/programming/opengl/JOGL2.0.html
 *
 * and on an example by Xerxes Rånby
 * http://jogamp.org/git/?p=jogl-demos.git;a=blob;f=src/demos/es2/RawGL2ES2demo.java;hb=HEAD
 *
 * @author Karsten Lehn
 * @version 3.9.2015, 15.9.2015, 18.9.2015, 10.9.2017
 *
 */
public class StartRendererPP extends GLCanvas implements GLEventListener {

    private static final long serialVersionUID = 1L;

    // taking shader source code files from relative path
    final String shaderPath = "Polygen.\\Resources\\";
    final String vertexShaderFileName = "Basic.vert";
    final String fragmentShaderFileName = "Basic.frag";
    private ShaderProgram shaderProgram;

    // Pointers for data transfer and handling on GPU
    int[] vaoName;  // Name of vertex array object
    int[] vboName;	// Name of vertex buffer object

    // Object for handling keyboard and mouse interaction
    InteractionHandler interactionHandler;
    // Projection model view matrix tool
    PMVMatrix pmvMatrix;

    /**
     * Standard constructor for object creation.
     */
    public StartRendererPP() {
        // Create the canvas with default capabilities
        super();
        // Add this object as OpenGL event listener
        this.addGLEventListener(this);
        createAndRegisterInteractionHandler();
    }

    /**
     * Create the canvas with the requested OpenGL capabilities
     * @param capabilities The capabilities of the canvas, including the OpenGL profile
     */
    public StartRendererPP(GLCapabilities capabilities) {
        // Create the canvas with the requested OpenGL capabilities
        super(capabilities);
        // Add this object as an OpenGL event listener
        this.addGLEventListener(this);
        createAndRegisterInteractionHandler();
    }

    /**
     * Helper method for creating an interaction handler object and registering it
     * for key press and mouse interaction call backs.
     */
    private void createAndRegisterInteractionHandler() {
        // The constructor call of the interaction handler generates meaningful default values
        // Nevertheless the start parameters can be set via setters
        // (see class definition of the interaction handler)
        interactionHandler = new InteractionHandler();
        this.addKeyListener(interactionHandler);
        this.addMouseListener(interactionHandler);
        this.addMouseMotionListener(interactionHandler);
        this.addMouseWheelListener(interactionHandler);
    }

    /**
     * Implementation of the OpenGL EventListener (GLEventListener) method
     * that is called when the OpenGL renderer is started for the first time.
     * @param drawable The OpenGL drawable
     */
    @Override
    public void init(GLAutoDrawable drawable) {
        GL3 gl = drawable.getGL().getGL3();

        shaderProgram = new ShaderProgram(gl);
        shaderProgram.loadShaderAndCreateProgram(shaderPath,
                vertexShaderFileName, fragmentShaderFileName);

        // Preparation for drawing a triangle with VAOs and VBOs
        // vertex array of triangle  (corner points only)
        // interleaved data: position, color
        float[] vertices = {
                -0.5f,  0.5f,   0.5f, 	 // 0 position
                0.0f,  0.68f,  0.85f, // 0 color
                0.5f, -0.5f,   0.5f, 	 // 1 position
                0.0f,  0.68f,  0.85f, // 1 color
                0.5f,  0.5f,   0.5f, 	 // 2 position
                0.0f,  0.68f,  0.85f, // 2 color
                -0.5f,  0.5f,   0.5f, 	 // 3 position
                0.0f,  0.68f,  0.85f, // 3 color
                0.5f, -0.5f,   0.5f, 	 // 4 position
                0.0f,  0.68f,  0.85f, // 4 color
                -0.5f,  -0.5f,   0.5f,  // 5 position
                0.0f,  0.68f,  0.85f, // 5 color

                -0.5f,  0.5f,   -0.5f, 	 // 0 position
                0.0f,  0.68f,  0.85f, // 0 color
                0.5f, -0.5f,   -0.5f, 	 // 1 position
                0.0f,  0.68f,  0.85f, // 1 color
                0.5f,  0.5f,   -0.5f, 	 // 2 position
                0.0f,  0.68f,  0.85f, // 2 color
                -0.5f,  0.5f,  -0.5f, 	 // 3 position
                0.0f,  0.68f,  0.85f, // 3 color
                0.5f, -0.5f,   -0.5f, 	 // 4 position
                0.0f,  0.68f,  0.85f, // 4 color
                -0.5f,  -0.5f, -0.5f,  // 5 position
                0.0f,  0.68f,  0.85f, // 5 color

                -0.5f,  -0.5f,   0.5f, 	 // 0 position
                0.0f,  0.68f,  0.85f, // 0 color
                -0.5f, 0.5f,   -0.5f, 	 // 1 position
                0.0f,  0.68f,  0.85f, // 1 color
                -0.5f,  0.5f,   0.5f, 	 // 2 position
                0.0f,  0.68f,  0.85f, // 2 color
                -0.5f,  -0.5f,  0.5f, 	 // 3 position
                0.0f,  0.68f,  0.85f, // 3 color
                -0.5f, 0.5f,   -0.5f, 	 // 4 position
                0.0f,  0.68f,  0.85f, // 4 color
                -0.5f,  -0.5f, -0.5f,  // 5 position
                0.0f,  0.68f,  0.85f, // 5 color
        };

        // create and activate a vertex array object (VAO)
        vaoName = new int[1];
        gl.glGenVertexArrays(1, vaoName, 0);
        if (vaoName[0] < 1)
            System.err.println("Error allocating vertex array object (VAO).");
        gl.glBindVertexArray(vaoName[0]);

        // create, activate and initialize vertex buffer object (VBO)
        vboName = new int[1];
        gl.glGenBuffers(1, vboName, 0);
        if (vboName[0] < 1)
            System.err.println("Error allocating vertex buffer object (VBO).");
        gl.glBindBuffer(GL.GL_ARRAY_BUFFER, vboName[0]);
        // floats use 4 bytes in Java
        gl.glBufferData(GL.GL_ARRAY_BUFFER, vertices.length * 4,
                FloatBuffer.wrap(vertices), GL.GL_STATIC_DRAW);
        // Activate and order vertex buffer object data for the vertex shader
        // Defining input for vertex shader
        gl.glEnableVertexAttribArray(0);
        // Pointer for the vertex shader to the position information per vertex
        gl.glVertexAttribPointer(0, 3, GL.GL_FLOAT, false, 6*4, 0);
        // (0, GL.GL_FLOAT, GL.GL_FALSE, 0, 0);
        gl.glEnableVertexAttribArray(1);
        // Pointer for the vertex shader to the color information per vertex
        gl.glVertexAttribPointer(1, 3, GL.GL_FLOAT, false, 6*4, 3*4);

        // Create projection-model-view matrix
        pmvMatrix = new PMVMatrix();

        // Start parameter settings for the interaction handler might be called here
        interactionHandler.setEyeZ(2);

        // Switch on back face culling
        // gl.glEnable(GL.GL_CULL_FACE);
        //gl.glCullFace(GL.GL_BACK);

        // Switch on depth test
        gl.glEnable(GL.GL_DEPTH_TEST);

        GL3 gl2 = drawable.getGL().getGL3();

        shaderProgram = new ShaderProgram(gl2);
        shaderProgram.loadShaderAndCreateProgram(shaderPath,
                vertexShaderFileName, fragmentShaderFileName);

        // Preparation for drawing a triangle with VAOs and VBOs
        // vertex array of triangle  (corner points only)
        // interleaved data: position, color
        float[] vertices2 = {
                -0.5f,  0.5f,   5.5f, 	 // 0 position
                0.0f,  0.68f,  0.85f, // 0 color
                0.5f, -0.5f,   5.5f, 	 // 1 position
                0.0f,  0.68f,  0.85f, // 1 color
                0.5f,  0.5f,   5.5f, 	 // 2 position
                0.0f,  0.68f,  0.85f, // 2 color
                -0.5f,  0.5f,   5.5f, 	 // 3 position
                0.0f,  0.68f,  0.85f, // 3 color
                0.5f, -0.5f,   5.5f, 	 // 4 position
                0.0f,  0.68f,  0.85f, // 4 color
                -0.5f,  -0.5f,   5.5f,  // 5 position
                0.0f,  0.68f,  0.85f, // 5 color

                -0.5f,  0.5f,   4.5f, 	 // 0 position
                0.0f,  0.68f,  0.85f, // 0 color
                0.5f, -0.5f,   4.5f, 	 // 1 position
                0.0f,  0.68f,  0.85f, // 1 color
                0.5f,  0.5f,   4.5f, 	 // 2 position
                0.0f,  0.68f,  0.85f, // 2 color
                -0.5f,  0.5f,  4.5f, 	 // 3 position
                0.0f,  0.68f,  0.85f, // 3 color
                0.5f, -0.5f,   4.5f, 	 // 4 position
                0.0f,  0.68f,  0.85f, // 4 color
                -0.5f,  -0.5f, 4.5f,  // 5 position
                0.0f,  0.68f,  0.85f, // 5 color

                -0.5f,  -0.5f,   5.5f, 	 // 0 position
                0.0f,  0.68f,  0.85f, // 0 color
                -0.5f, 0.5f,   4.5f, 	 // 1 position
                0.0f,  0.68f,  0.85f, // 1 color
                -0.5f,  0.5f,   5.5f, 	 // 2 position
                0.0f,  0.68f,  0.85f, // 2 color
                -0.5f,  -0.5f,  5.5f, 	 // 3 position
                0.0f,  0.68f,  0.85f, // 3 color
                -0.5f, 0.5f,   4.5f, 	 // 4 position
                0.0f,  0.68f,  0.85f, // 4 color
                -0.5f,  -0.5f, 4.5f,  // 5 position
                0.0f,  0.68f,  0.85f, // 5 color
        };

        // create and activate a vertex array object (VAO)
        vaoName = new int[1];
        gl2.glGenVertexArrays(1, vaoName, 0);
        if (vaoName[0] < 1)
            System.err.println("Error allocating vertex array object (VAO).");
        gl2.glBindVertexArray(vaoName[0]);

        // create, activate and initialize vertex buffer object (VBO)
        vboName = new int[1];
        gl2.glGenBuffers(1, vboName, 0);
        if (vboName[0] < 1)
            System.err.println("Error allocating vertex buffer object (VBO).");
        gl2.glBindBuffer(GL.GL_ARRAY_BUFFER, vboName[0]);
        // floats use 4 bytes in Java
        gl2.glBufferData(GL.GL_ARRAY_BUFFER, vertices2.length * 4,
                FloatBuffer.wrap(vertices), GL.GL_STATIC_DRAW);
        // Activate and order vertex buffer object data for the vertex shader
        // Defining input for vertex shader
        gl2.glEnableVertexAttribArray(0);
        // Pointer for the vertex shader to the position information per vertex
        gl2.glVertexAttribPointer(0, 3, GL.GL_FLOAT, false, 6*4, 0);
        // (0, GL.GL_FLOAT, GL.GL_FALSE, 0, 0);
        gl2.glEnableVertexAttribArray(1);
        // Pointer for the vertex shader to the color information per vertex
        gl2.glVertexAttribPointer(1, 3, GL.GL_FLOAT, false, 6*4, 3*4);

        // Create projection-model-view matrix
        pmvMatrix = new PMVMatrix();

        // Start parameter settings for the interaction handler might be called here
        interactionHandler.setEyeZ(2);

        // Switch on back face culling
        // gl.glEnable(GL.GL_CULL_FACE);
        //gl.glCullFace(GL.GL_BACK);

        // Switch on depth test
        gl2.glEnable(GL.GL_DEPTH_TEST);
    }

    /**
     * Implementation of the OpenGL EventListener (GLEventListener) method
     * called by the OpenGL animator for every frame.
     * @param drawable The OpenGL drawable
     */
    @Override
    public void display(GLAutoDrawable drawable) {
        GL3 gl = drawable.getGL().getGL3();
        gl.glClear(GL3.GL_COLOR_BUFFER_BIT | GL3.GL_DEPTH_BUFFER_BIT);

        // Background color of the canvas
        gl.glClearColor(0.2f, 0.2f, 0.2f, 1.0f);
        // using the compiles shader program
        gl.glUseProgram(shaderProgram.getShaderProgramID());

        // Using the PMV-Tool
        pmvMatrix.glMatrixMode(PMVMatrix.GL_MODELVIEW);
        pmvMatrix.glLoadIdentity();
        pmvMatrix.gluLookAt(0f, 0f, interactionHandler.getEyeZ(),
                0f, 0f, 0f,
                0f, 1.0f, 0f);
        pmvMatrix.glTranslatef(interactionHandler.getxPosition(), interactionHandler.getyPosition(), 0f);
        pmvMatrix.glRotatef(interactionHandler.getAngleXaxis(), 1f, 0f, 0f);
        pmvMatrix.glRotatef(interactionHandler.getAngleYaxis(), 0f, 1f, 0f);

        // Transfer the PVM-Matrix (model-view and projection matrix)
        // to the vertex shader
        gl.glUniformMatrix4fv(0, 1, false, pmvMatrix.glGetPMatrixf());
        gl.glUniformMatrix4fv(1, 1, false, pmvMatrix.glGetMvMatrixf());

        // Draws the contents of the vertex buffer object (VBO)
        // Draws the first 3 vertices
        gl.glDrawArrays(GL.GL_TRIANGLES, 0, 36); //TODO
    }

    /**
     * Implementation of the OpenGL EventListener (GLEventListener) method
     * called when the OpenGL window is resized.
     * @param drawable The OpenGL drawable
     * @param x
     * @param y
     * @param width
     * @param height
     */
    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL3 gl = drawable.getGL().getGL3();

        pmvMatrix.glMatrixMode(PMVMatrix.GL_PROJECTION);
        pmvMatrix.glLoadIdentity();
        pmvMatrix.gluPerspective(45f, (float) width/ (float) height, 0.1f, 100f);
    }

    /**
     * Implementation of the OpenGL EventListener (GLEventListener) method
     * called when OpenGL canvas ist destroyed.
     * @param drawable
     */
    @Override
    public void dispose(GLAutoDrawable drawable) {
        GL3 gl = drawable.getGL().getGL3();

        // Detach and delete shader program
        gl.glUseProgram(0);
        shaderProgram.deleteShaderProgram();

        // deactivate VAO and VBO
        gl.glBindVertexArray(0);
        gl.glDisableVertexAttribArray(0);
        gl.glDisableVertexAttribArray(1);

        System.exit(0);
    }
}
