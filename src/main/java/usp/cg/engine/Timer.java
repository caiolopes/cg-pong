package usp.cg.engine;

class Timer {

    private double lastLoopTime;
    
    void init() {
        lastLoopTime = getTime();
    }

    double getTime() {
        return System.nanoTime() / 1000_000_000.0;
    }

    float getEllapsedTime() {
        double time = getTime();
        float ellapsedTime = (float) (time - lastLoopTime);
        lastLoopTime = time;
        return ellapsedTime;
    }

    double getLastLoopTime() {
        return lastLoopTime;
    }
}