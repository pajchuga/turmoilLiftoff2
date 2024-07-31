package com.turmoillift2.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.github.tommyettinger.textra.Font;
import com.github.tommyettinger.textra.TypingLabel;
import com.turmoillift2.handlers.GameStateManager;
import com.turmoillift2.handlers.MyInput;
import com.turmoillift2.main.TurmoilLiftoff2;

public class Init extends GameState {
    private Skin skin;
    private Stage stage;

    public Init(GameStateManager gsm) {
        super(gsm);
        skin = new Skin(Gdx.files.internal("ui/test/uiskin.json"));

        stage = new Stage(game.getViewport());
        //Gdx.input.setInputProcessor(stage);
        game.getInputMultiplexer().addProcessor(stage);
        Table root = new Table();
        root.setSkin(skin);
        root.setFillParent(true);
        skin.get(Label.LabelStyle.class).font.getData().markupEnabled = true;
        TypingLabel typingLabel = new TypingLabel("[@IBM 8x16][%150][CHARTREUSE]{EASE=10.0;4.0;false}Wizards Turmoil",new Font());
        Label label = new Label("[YELLOW] * * * []" + TurmoilLiftoff2.TITLE + "[YELLOW] * * * []", skin);
        Label instructionLabel1 = new Label("Press [YELLOW]R[] or click on [YELLOW][Start][]", skin);
        Label instructionLabel2 = new Label("[YELLOW] TO START THE GAME[]", skin);

        stage.addActor(root);
        ImageTextButton textButton = new ImageTextButton("Start", skin);
        textButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                gsm.setState(GameStateType.PLAY);
            }
        });
        root.padTop(50);
        root.align(Align.top);
        root.add(typingLabel).expand();
        root.row().spaceTop(200);
        root.add(textButton);
        root.row().spaceTop(60);
        root.add(instructionLabel1);
        root.row().spaceTop(10);
        root.add(instructionLabel2);
        root.row();
        root.add(new Label("created by [YELLOW]Pavle[]", skin)).align(Align.bottomRight).padRight(20);
        root.row();


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
        ScreenUtils.clear(0.56f, 0.56f, 0.30f, 1);
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 144f));
        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }
}
