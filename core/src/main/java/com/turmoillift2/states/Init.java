package com.turmoillift2.states;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.github.tommyettinger.textra.KnownFonts;
import com.github.tommyettinger.textra.TypingLabel;
import com.turmoillift2.handlers.GameStateManager;
import com.turmoillift2.handlers.HoverEvent;
import com.turmoillift2.handlers.MyInput;
import com.turmoillift2.main.TurmoilLiftoff2;

public class Init extends GameState {
    private static final String TITLE_EFFECT = "[%200][#f1dd38]{ROTATE=10.0}{EASE=15 .0;3.0;false}{JOLT=1.0;1.0;inf;0.1;#f1dd38;ffff88ff}";
    private static final String INSTURCTION1_PART1_EFFECT = "{FADE=f1b209ff;f1dd38;1.0}";
    private static final String INSTURCTION1_PART2_EFFECT = "{JOLT=1.0;1.0;inf;0.45;#f1dd38;ffff88ff}R{ENDJOLT}";
    private static final String INSTURCTION1_PART3_EFFECT = "[#f1dd38]{JOLT=1.0;1.0;inf;0.3;#f1dd38;ffff88ff}";
    private static final String INSTURCTION2_PART1_EFFECT = "{FADE=f1b209ff;ffff88ff;1.0}[#ffff88ff]";
    private static final String SIGNATURE_EFFECT = "created by {JOLT=1.0;1.0;inf;0.2;#f1dd38;ffff88ff}[#f1dd38]Pavle[]";

    private Skin skin;
    private Stage stage;
    private Sprite background;
    private Texture backgroundTexture;
    private Sound buttonHoverSound;
    private Sound mainLobbyLoop;


    public Init(GameStateManager gsm) {
        super(gsm);
        skin = new Skin(Gdx.files.internal("ui/test2/uiskin.json"));
        backgroundTexture = new Texture(Gdx.files.internal("background.png"));
        backgroundTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        background = new Sprite(backgroundTexture);
        background.setSize(TurmoilLiftoff2.WORLD_WIDTH, TurmoilLiftoff2.WORLD_HEIGHT);
        buttonHoverSound = TurmoilLiftoff2.resource.getSound("buttonHoverSound");
        HoverEvent buttonHoverListener = new HoverEvent(buttonHoverSound);
        mainLobbyLoop = TurmoilLiftoff2.resource.getSound("titleThemeSound");

        // background = new Sprite(backgroundTexture);

        stage = new Stage(game.getViewport());
        game.getInputMultiplexer().addProcessor(stage);

        if (Gdx.app.getType() == Application.ApplicationType.Android) {
            initAndroid(buttonHoverListener);
        } else {
            initPC(buttonHoverListener);
        }
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
        mainLobbyLoop.stop();
        //TODO Manage disposal for now everything loads at the start and stays there it should
        // most stuff should load on creating this state and stay only while this state is active
    }

    private void initAndroid(EventListener buttonHoverListener) {
        Table root = new Table();
        root.setSkin(skin);
        root.setFillParent(true);
        skin.get(Label.LabelStyle.class).font.getData().markupEnabled = true;
        stage.addActor(root);
        TypingLabel typingLabel = new TypingLabel(TITLE_EFFECT + "Wizards Turmoil", KnownFonts.getIBM8x16());
        TypingLabel instructionLabel1 = new TypingLabel("Press on " + INSTURCTION1_PART3_EFFECT + " START", KnownFonts.getIBM8x16());
        TypingLabel instructionLabel2 = new TypingLabel(INSTURCTION2_PART1_EFFECT + " TO START THE GAME[]", KnownFonts.getIBM8x16());
        TypingLabel signature = new TypingLabel(SIGNATURE_EFFECT, KnownFonts.getIBM8x16());
        ImageTextButton startButton = new ImageTextButton("Start", skin);
        startButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gsm.setState(GameStateType.PLAY);
            }
        });
        TextButton exitButton = new TextButton("EXIT", skin);
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });
        startButton.addListener(buttonHoverListener);
        exitButton.addListener(buttonHoverListener);
        root.padTop(50);
        root.align(Align.top);
        root.add(typingLabel).expand().minWidth(250).align(Align.center);
        root.row().padTop(160);
        root.add(startButton).minHeight(50).growX().align(Align.center).fill(0.45f, 0.45f);
        root.row().padTop(16);
        root.add(exitButton).minHeight(50).growX().align(Align.center).fill(0.45f, 0.45f);
        root.row().padTop(60);
        root.add(instructionLabel1);
        root.row().padTop(10);
        root.add(instructionLabel2);
        root.row();
        root.add(signature).align(Align.bottomRight).padRight(20).padBottom(20);
        root.row();
//        root.debug();
        mainLobbyLoop.loop(0.15f);
    }


    private void initPC(EventListener buttonHoverListener) {
        Table root = new Table();
        root.setSkin(skin);
        root.setFillParent(true);
        skin.get(Label.LabelStyle.class).font.getData().markupEnabled = true;
        stage.addActor(root);
        TypingLabel typingLabel = new TypingLabel(TITLE_EFFECT + "Wizards Turmoil", KnownFonts.getIBM8x16());
        TypingLabel instructionLabel1 = new TypingLabel(INSTURCTION1_PART1_EFFECT + "Press " + INSTURCTION1_PART2_EFFECT + " or click on " + INSTURCTION1_PART3_EFFECT + " START", KnownFonts.getIBM8x16());
        TypingLabel instructionLabel2 = new TypingLabel(INSTURCTION2_PART1_EFFECT + " TO START THE GAME[]", KnownFonts.getIBM8x16());
        TypingLabel signature = new TypingLabel(SIGNATURE_EFFECT, KnownFonts.getIBM8x16());
        ImageTextButton startButton = new ImageTextButton("Start", skin);
        startButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                gsm.setState(GameStateType.PLAY);
            }
        });
        TextButton exitButton = new TextButton("EXIT", skin);
        exitButton.addListener(new ChangeListener() {

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });
        startButton.addListener(buttonHoverListener);
        exitButton.addListener(buttonHoverListener);
        root.padTop(50);
        root.align(Align.top);
        root.add(typingLabel).expand().minWidth(250);
        root.row().padTop(160);
        root.add(startButton).minHeight(50);
        if (Gdx.app.getType() != Application.ApplicationType.WebGL) {
            root.row().padTop(8);
            root.add(exitButton).minHeight(50);
        }
        root.row().padTop(60);
        root.add(instructionLabel1);
        root.row().padTop(10);
        root.add(instructionLabel2);
        root.row();
        root.add(signature).align(Align.bottomRight).padRight(20).padBottom(20);
        root.row();
//        root.debug();
        mainLobbyLoop.loop(0.15f);
    }
}
