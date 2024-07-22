package com.turmoillift2.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.turmoillift2.handlers.GameStateManager;
import com.turmoillift2.handlers.MyInput;
import com.turmoillift2.main.TurmoilLiftoff2;


public class Init extends GameState {
    private Skin skin;
    private Stage stage;

    public Init(GameStateManager gsm) {
        super(gsm);
        skin = new Skin(Gdx.files.internal("ui/uiskin.json"));


        stage = new Stage(game.getViewport());
        //Gdx.input.setInputProcessor(stage);
        game.getInputMultiplexer().addProcessor(stage);
        Table root = new Table();
        root.setSkin(skin);
        root.setFillParent(true);
        skin.get(Label.LabelStyle.class).font.getData().markupEnabled = true;
        Label label = new Label("* * * " + TurmoilLiftoff2.TITLE + " * * *", skin);
        Label instructionLabel1 = new Label("Press [YELLOW]R[] or click on [YELLOW][Start][]", skin);
        Label instructionLabel2 = new Label("[YELLOW] TO START THE GAME[]", skin);

        stage.addActor(root);
        TextButton textButton = new TextButton("Start", skin);
        textButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                gsm.setState(GameStateType.PLAY);
            }
        });
        root.background("window");
        root.add(label).colspan(6);
        root.row();
        Window window = new Window("", skin);
        root.add(window).expand().growX();
        window.row();
        window.add(textButton).padTop(20);
        window.row();
        window.add(instructionLabel1).padTop(50);
        window.row();
        window.add(instructionLabel2);
        root.row();
        root.add(new Label("created by [YELLOW]Pavle[]", skin)).align(Align.right);


    }

    @Override
    public void handleInput() {
        if (MyInput.isPressed(MyInput.PLAY_BUTTON)) {
            gsm.setState(GameStateType.PLAY);
        }
    }

    @Override
    public void update(float dt) {
        handleInput();
    }

    @Override
    public void render() {
        ScreenUtils.clear(0.26f, 0.26f, 0.90f, 1);
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 144f));
        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }
}
