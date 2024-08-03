package com.turmoillift2.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.github.tommyettinger.textra.KnownFonts;
import com.github.tommyettinger.textra.TypingLabel;
import com.turmoillift2.handlers.GameStateManager;
import com.turmoillift2.handlers.MyInput;
import com.turmoillift2.main.TurmoilLiftoff2;

public class Init extends GameState {
    private final Skin skin;
    private final Stage stage;
    private final Sprite background;
    private final Texture backgroundTexture;

    public Init(GameStateManager gsm) {
        super(gsm);
        skin = new Skin(Gdx.files.internal("ui/test2/uiskin.json"));
        backgroundTexture = new Texture(Gdx.files.internal("background.png"));
        backgroundTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        background = new Sprite(backgroundTexture);
        background.setSize(TurmoilLiftoff2.WORLD_WIDTH, TurmoilLiftoff2.WORLD_HEIGHT);

       // background = new Sprite(backgroundTexture);

        stage = new Stage(game.getViewport());
        //Gdx.input.setInputProcessor(stage);
        game.getInputMultiplexer().addProcessor(stage);
        Table root = new Table();
        root.setSkin(skin);
        root.setFillParent(true);
        skin.get(Label.LabelStyle.class).font.getData().markupEnabled = true;
        stage.addActor(root);
        TypingLabel typingLabel = new TypingLabel("[%200][#f1dd38]{ROTATE=10.0}{EASE=15 .0;3.0;false}{JOLT=1.0;1.0;inf;0.1;#f1dd38;ffff88ff}Wizards Turmoil", KnownFonts.getIBM8x16());
        TypingLabel instructionLabel1 = new TypingLabel("{FADE=f1b209ff;f1dd38;1.0}Press {JOLT=1.0;1.0;inf;0.45;#f1dd38;ffff88ff}R{ENDJOLT} or click on [#f1dd38]{JOLT=1.0;1.0;inf;0.3;#f1dd38;ffff88ff} START", KnownFonts.getIBM8x16());
        TypingLabel instructionLabel2 = new TypingLabel("{FADE=f1b209ff;ffff88ff;1.0}[#ffff88ff] TO START THE GAME[]", KnownFonts.getIBM8x16());
        TypingLabel signature = new TypingLabel("created by {JOLT=1.0;1.0;inf;0.2;#f1dd38;ffff88ff}[#f1dd38]Pavle[]", KnownFonts.getIBM8x16());
        ImageTextButton textButton = new ImageTextButton("Start", skin);
        textButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                gsm.setState(GameStateType.PLAY);
            }
        });
        root.padTop(50);
        root.align(Align.top);
        root.add(typingLabel).expand().minWidth(250);
        root.row().padTop(200);
        root.add(textButton).minHeight(50);
        root.row().padTop(60);
        root.add(instructionLabel1);
        root.row().padTop(10);
        root.add(instructionLabel2);
        root.row();
        root.add(signature).align(Align.bottomRight).padRight(20).padBottom(20);
        root.row();
        //root.debug();
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
        ScreenUtils.clear(0.29f, 0.46f, 0.72f, 1);
        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();
        background.draw(spriteBatch);
        spriteBatch.end();
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 144f));
        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
        backgroundTexture.dispose();
    }
}
