package io.github.alxtray.groundclimber.services;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import io.github.alxtray.groundclimber.enums.InputAction;

public class InputService {

    public InputAction getInputAction() {
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            return InputAction.LEFT;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            return InputAction.RIGHT;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            return InputAction.JUMP;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            return InputAction.PAUSE;
        }

        return InputAction.NO_ACTION;
    }
}
