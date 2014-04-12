package net.wohlfart.photon.shader;

import javax.media.opengl.GL2;
import javax.media.opengl.GL2ES2;
import javax.media.opengl.GL3;
import javax.media.opengl.GL4;

//
// handle for a single shader attribute in a specific shader program
//
public class AttributeHandle {

    private final int shaderProgramId;

    private final String name;
    private final int location;

    private final int typeSize;
    @SuppressWarnings("unused")
    private final int typeId;

    @SuppressWarnings("unused")
    private String type;
    private int attributeSize; // in floats


    AttributeHandle(int shaderProgramId, String name, int typeSize, int typeId, int location) {
        this.shaderProgramId = shaderProgramId;
        this.name = name;
        this.typeSize = typeSize;
        this.typeId = typeId;
        this.location = location;
        setupTypeInfo(typeId, typeSize);
    }

    public int getAttributeSize() {
        return attributeSize;
    }

    public void enable(GL2ES2 gl, int size, int stride, int offset) {
        gl.glEnableVertexAttribArray(location);
        gl.glVertexAttribPointer(location, size, GL2.GL_FLOAT, false, stride, offset);
    }

    /**
     * disable the vertex attribute and set a default null value
     */
    public void disable(GL2ES2 gl) {
        gl.glDisableVertexAttribArray(location);
        switch (attributeSize) {
        case 1:
            gl.glVertexAttrib1f(location, 0f);
            break;
        case 2:
            gl.glVertexAttrib2f(location, 0f, 0f);
            break;
        case 3:
            gl.glVertexAttrib3f(location, 0f, 0f, 1f);
            break;
        case 4:
            gl.glVertexAttrib4f(location, 0f, 0f, 1f, 0);
            break;
        default:
            throw new IllegalStateException("unknowns size: '" + typeSize + "'");
        }
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " [shaderProgramId=" + shaderProgramId
                + ", name=" + name
                + ", location=" + location + "]";
    }

    // TODO: move this into a subclass and reuse for uniforms
    private final void setupTypeInfo(int typeId, int typeSize) {
        switch (typeId) {
        case GL2.GL_FLOAT:
            type = "GL_FLOAT";
            attributeSize = 1;
            break;
        case GL2.GL_FLOAT_VEC2:
            type = "GL_FLOAT_VEC2";
            attributeSize = 2;
            break;
        case GL2.GL_FLOAT_VEC3:
            type = "GL_FLOAT_VEC3";
            attributeSize = 3;
            break;
        case GL2.GL_FLOAT_VEC4:
            type = "GL_FLOAT_VEC4";
            attributeSize = 4;
            break;
        case GL2.GL_FLOAT_MAT2:
            type = "GL_FLOAT_MAT2";
            attributeSize = 2;
            break;
        case GL2.GL_FLOAT_MAT3:
            type = "GL_FLOAT_MAT3";
            attributeSize = 3;
            break;
        case GL2.GL_FLOAT_MAT4:
            type = "GL_FLOAT_MAT4";
            attributeSize = 4;
            break;
        case GL2.GL_FLOAT_MAT2x3:
            type = "GL_FLOAT_MAT2x3";
            attributeSize = 6;
            break;
        case GL2.GL_FLOAT_MAT2x4:
            type = "GL_FLOAT_MAT2x4";
            attributeSize = 8;
            break;
        case GL2.GL_FLOAT_MAT3x2:
            type = "GL_FLOAT_MAT3x2";
            attributeSize = 6;
            break;
        case GL2.GL_FLOAT_MAT3x4:
            type = "GL_FLOAT_MAT3x4";
            attributeSize = 12;
            break;
        case GL2.GL_FLOAT_MAT4x2:
            type = "GL_FLOAT_MAT4x2";
            attributeSize = 8;
            break;
        case GL2.GL_FLOAT_MAT4x3:
            type = "GL_FLOAT_MAT4x3";
            attributeSize = 12;
            break;
        case GL2.GL_INT:
            type = "GL_INT";
            attributeSize = 1;
            break;
        case GL2.GL_INT_VEC2:
            type = "GL_INT_VEC2";
            attributeSize = 2;
            break;
        case GL2.GL_INT_VEC3:
            type = "GL_INT_VEC3";
            attributeSize = 3;
            break;
        case GL2.GL_INT_VEC4:
            type = "GL_INT_VEC4";
            attributeSize = 4;
            break;
        case GL2.GL_UNSIGNED_INT:
            type = "GL_UNSIGNED_INT";
            attributeSize = 1;
            break;
        case GL3.GL_UNSIGNED_INT_VEC2:
            type = "GL_UNSIGNED_INT_VEC2";
            attributeSize = 2;
            break;
        case GL3.GL_UNSIGNED_INT_VEC3:
            type = "GL_UNSIGNED_INT_VEC3";
            attributeSize = 3;
            break;
        case GL3.GL_UNSIGNED_INT_VEC4:
            type = "GL_UNSIGNED_INT_VEC4";
            attributeSize = 4;
            break;
        case GL2.GL_DOUBLE:
            type = "GL_DOUBLE";
            attributeSize = 2;
            break;
        case GL4.GL_DOUBLE_VEC2:
            type = "GL_DOUBLE_VEC2";
            attributeSize = 4;
            break;
        case GL4.GL_DOUBLE_VEC3:
            type = "GL_DOUBLE_VEC3";
            attributeSize = 6;
            break;
        case GL4.GL_DOUBLE_VEC4:
            type = "GL_DOUBLE_VEC4";
            attributeSize = 8;
            break;
        case GL4.GL_DOUBLE_MAT2:
            type = "GL_DOUBLE_MAT2";
            attributeSize = 4;
            break;
        case GL4.GL_DOUBLE_MAT3:
            type = "GL_DOUBLE_MAT3";
            attributeSize = 6;
            break;
        case GL4.GL_DOUBLE_MAT4:
            type = "GL_DOUBLE_MAT4";
            attributeSize = 8;
            break;
        case GL4.GL_DOUBLE_MAT2x3:
            type = "GL_DOUBLE_MAT2x3";
            attributeSize = 12;
            break;
        case GL4.GL_DOUBLE_MAT2x4:
            type = "GL_DOUBLE_MAT2x4";
            attributeSize = 16;
            break;
        case GL4.GL_DOUBLE_MAT3x2:
            type = "GL_DOUBLE_MAT3x2";
            attributeSize = 12;
            break;
        case GL4.GL_DOUBLE_MAT3x4:
            type = "GL_DOUBLE_MAT3x4";
            attributeSize = 24;
            break;
        case GL4.GL_DOUBLE_MAT4x2:
            type = "GL_DOUBLE_MAT4x2";
            attributeSize = 16;
            break;
        case GL4.GL_DOUBLE_MAT4x3:
            type = "GL_DOUBLE_MAT4x3";
            attributeSize = 24;
            break;
        default:
            type = "unknown";
            attributeSize = 0;
            break;
        }
    }

}
