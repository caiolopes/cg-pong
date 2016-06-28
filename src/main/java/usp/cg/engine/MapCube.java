package usp.cg.engine;

import usp.cg.engine.graphics.Material;
import usp.cg.engine.graphics.Mesh;
import usp.cg.engine.graphics.OBJLoader;
import usp.cg.engine.graphics.Texture;

public class MapCube extends GameItem {

    public MapCube(String objModel, String textureFile) throws Exception {
        super();
        Mesh skyBoxMesh = OBJLoader.loadMesh(objModel);
        Texture skyBoxtexture = new Texture(textureFile);
        skyBoxMesh.setMaterial(new Material(skyBoxtexture, 0.0f));
        setMesh(skyBoxMesh);
        setPosition(0, 0, 0);
    }
}
