package usp.cg.engine.graphics;

import org.joml.Vector3f;

public class SpotLight {

    private PointLight pointLight;

    private Vector3f coneDirection;

    private float cutOff;

    public SpotLight(PointLight pointLight, Vector3f coneDirection, float cutOffAngle) {
        this.pointLight = pointLight;
        this.coneDirection = coneDirection;
        setCutOffAngle(cutOffAngle);
    }

    public SpotLight(SpotLight spotLight) {
        this(new PointLight(spotLight.getPointLight()),
                new Vector3f(spotLight.getConeDirection()),
                spotLight.getCutOff());
    }

    PointLight getPointLight() {
        return pointLight;
    }

    Vector3f getConeDirection() {
        return coneDirection;
    }

    void setConeDirection(Vector3f coneDirection) {
        this.coneDirection = coneDirection;
    }

    float getCutOff() {
        return cutOff;
    }

    private void setCutOff(float cutOff) {
        this.cutOff = cutOff;
    }
    
    private void setCutOffAngle(float cutOffAngle) {
        this.setCutOff((float)Math.cos(Math.toRadians(cutOffAngle)));
    }

}
