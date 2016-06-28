package usp.cg.engine;

public class GameEngine implements Runnable {

    private static final int TARGET_FPS = 75;

    private static final int TARGET_UPS = 30;

    private final Window window;

    private final Thread gameLoopThread;

    private final Timer timer;

    private final GameInterface gameLogic;

    private final MouseInput mouseInput;

    public GameEngine(String windowTitle, int width, int height, boolean vSync, GameInterface gameLogic) throws Exception {
        gameLoopThread = new Thread(this, "GAME_LOOP_THREAD");
        window = new Window(windowTitle, width, height, vSync);
        mouseInput = new MouseInput();
        this.gameLogic = gameLogic;
        timer = new Timer();
    }

    public void start() {
        String osName = System.getProperty("os.name");
        if ( osName.contains("Mac") ) {
            gameLoopThread.run();
        } else {
            gameLoopThread.start();
        }
    }

    @Override
    public void run() {
        try {
            init();
            gameLoop();
        } catch (Exception excp) {
            excp.printStackTrace();
        } finally {
            cleanup();
        }
    }

    private void init() throws Exception {
        window.init();
        timer.init();
        mouseInput.init(window);
        gameLogic.init(window);
    }

    private void gameLoop() {
        float ellapsedTime;
        float accumulator = 0f;
        float interval = 1f / TARGET_UPS;

        while (window.windowShouldClose() != 1) {
            ellapsedTime = timer.getEllapsedTime();
            accumulator += ellapsedTime;

            input();

            while (accumulator >= interval) {
                update(interval);
                accumulator -= interval;
            }

            render();

            if ( !window.isvSync() ) {
                sync();
            }
        }
    }

    private void cleanup() {
        gameLogic.cleanup();                
    }
    
    private void sync() {
        float loopSlot = 1f / TARGET_FPS;
        double endTime = timer.getLastLoopTime() + loopSlot;
        while (timer.getTime() < endTime) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException ignored) {
            }
        }
    }

    private void input() {
        mouseInput.input(window);
        gameLogic.input(window, mouseInput);
    }

    private void update(float interval) {
        gameLogic.update(interval, mouseInput);
    }

    private void render() {
        gameLogic.render(window);
        window.update();
    }
}
