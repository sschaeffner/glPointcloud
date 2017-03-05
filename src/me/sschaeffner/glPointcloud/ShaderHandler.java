package me.sschaeffner.glPointcloud;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

/**
 * @author sschaeffner
 */
public class ShaderHandler {

    private final String vertex;
    private final String fragment;

    private int programId;
    private int vertexShaderId;
    private int fragmentShaderId;

    public ShaderHandler() throws IOException {
        vertex = readFile("res/shader/vertex.vert");
        fragment = readFile("res/shader/fragment.frag");

        /*System.out.println("--- Vertex Shader ---");
        System.out.println(vertex);
        System.out.println("---------------------");
        System.out.println("--- Fragment Shader ---");
        System.out.println(fragment);
        System.out.println("-----------------------");*/

        System.out.println("creating program...");
        programId = glCreateProgram();
        vertexShaderId = createShader(vertex, GL_VERTEX_SHADER);
        fragmentShaderId = createShader(fragment, GL_FRAGMENT_SHADER);

        System.out.println("ShaderHandler: attaching...");
        glAttachShader(programId, vertexShaderId);
        glAttachShader(programId, fragmentShaderId);

        System.out.println("ShaderHandler: bind attribute location...");
        glBindAttribLocation(programId, 0, "Position");

        System.out.println("ShaderHandler: linking...");
        glLinkProgram(programId);
        checkProgramErrors(programId, GL_LINK_STATUS);

        System.out.println("ShaderHandler: validating...");
        glValidateProgram(programId);
        checkProgramErrors(programId, GL_VALIDATE_STATUS);
    }

    public void bind() {
        glUseProgram(programId);
    }

    public void destroy() {
        glDetachShader(programId, fragmentShaderId);
        glDetachShader(programId, vertexShaderId);
        glDeleteShader(vertexShaderId);
        glDeleteShader(fragmentShaderId);
        glDeleteProgram(programId);
    }

    private int createShader(String content, int shaderType) {
        int shaderId = glCreateShader(shaderType);

        if (shaderId == 0) {
            throw new IllegalStateException("Shader creation failed");
        }

        glShaderSource(shaderId, content);

        System.out.println("ShaderHandler: compiling shader...");
        glCompileShader(shaderId);
        checkShaderErrors(shaderId, GL_COMPILE_STATUS);

        return shaderId;
    }

    private boolean checkShaderErrors(int shaderId, int flag) {
        int success = glGetShaderi(shaderId, flag);
        if (success == GL_FALSE) {
            System.out.println("error in shader (" + shaderId + "): " + glGetShaderInfoLog(shaderId));
            return false;
        }
        return true;
    }

    private boolean checkProgramErrors(int programId, int flag) {
        int success = glGetProgrami(programId, flag);
        if (success == GL_FALSE) {
            System.out.println("error in program (" + programId + "): " + glGetProgramInfoLog(programId));
            return false;
        }
        return true;
    }

    private static String readFile(String path) throws IOException {
        return new String(Files.readAllBytes(Paths.get(path)));
    }
}
