package usp.cg.engine.graphics;

import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.utils.PNGDecoder.Format;
import java.io.InputStream;
import java.nio.ByteBuffer;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;

public class Texture {

    private final int id;

    public Texture(String fileName) throws Exception {
        this(Texture.class.getResourceAsStream(fileName));
    }

    private Texture(InputStream is) throws Exception {
        // Carrega o arquivo da textura
        PNGDecoder decoder = new PNGDecoder(is);

        int width = decoder.getWidth();
        int height = decoder.getHeight();

        // Carrega o conteudo textura no byte buffer
        ByteBuffer buf = ByteBuffer.allocateDirect(
                4 * decoder.getWidth() * decoder.getHeight());
        decoder.decode(buf, decoder.getWidth() * 4, Format.RGBA);
        buf.flip();

        // Cria uma nova textura OpenGL
        this.id = glGenTextures();
        // Faz o bind da textura
        glBindTexture(GL_TEXTURE_2D, this.id);

        // Fala pra OpenGL retirar os bytes do RGBA. Cada componente eh 1 byte
        glPixelStorei(GL_UNPACK_ALIGNMENT, 1);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        // Atualiza os dados da textura
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, buf);
        // Gera o Mip Map
        glGenerateMipmap(GL_TEXTURE_2D);
    }

    int getId() {
        return id;
    }

    void cleanup() {
        glDeleteTextures(id);
    }
}
