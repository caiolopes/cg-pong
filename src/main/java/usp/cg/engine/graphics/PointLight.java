package usp.cg.engine.graphics;

import org.joml.Vector3f;

public class PointLight {

    private Vector3f color;

    private Vector3f position;

    private float intensity;

    private Attenuation attenuation;
    
    public PointLight(Vector3f color, Vector3f position, float intensity) {
        attenuation = new Attenuation(1, 0, 0);
        this.color = color;
        this.position = position;
        this.intensity = intensity;
    }

    public PointLight(Vector3f color, Vector3f position, float intensity, Attenuation attenuation) {
        this(color, position, intensity);
        this.attenuation = attenuation;
    }

    public PointLight(PointLight pointLight) {
        this(new Vector3f(pointLight.getColor()), new Vector3f(pointLight.getPosition()),
                pointLight.getIntensity(), pointLight.getAttenuation());
    }

    Vector3f getColor() {
        return color;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    float getIntensity() {
        return intensity;
    }

    Attenuation getAttenuation() {
        return attenuation;
    }

    public static class Attenuation {

        private float constant;

        private float linear;

        private float exponent;

        public Attenuation(float constant, float linear, float exponent) {
            this.constant = constant;
            this.linear = linear;
            this.exponent = exponent;
        }

        float getConstant() {
            return constant;
        }

        float getLinear() {
            return linear;
        }

        float getExponent() {
            return exponent;
        }
    }
}