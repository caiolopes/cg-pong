package usp.cg.graphics;

public class ShaderManager {

    public static Shader shader1;
    public static Shader shader2;
    public static Shader shader3;

    public ShaderManager(){
        System.out.println("Shader Manager Started");
    }

    public void loadAll(){
        shader1 = new Shader("src/main/java/usp/cg/shaders/vertexShader", "src/main/java/usp/cg/shaders/fragShader");
        shader2 = new Shader("src/main/java/usp/cg/shaders/vertexShader1", "src/main/java/usp/cg/shaders/fragShader1");
        shader3 = new Shader("src/main/java/usp/cg/shaders/vertexShader2", "src/main/java/usp/cg/shaders/fragShader2");
    }
}