package LavaLampJenkinsTest;

public class CommandControllerStub implements LampController {
    private String name;
    private boolean state;

    public CommandControllerStub(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void turnLamp(boolean on) {
        state = on;
    }

    public boolean getState() {
        return state;
    }
}
