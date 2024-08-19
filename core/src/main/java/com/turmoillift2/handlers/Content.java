package com.turmoillift2.handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

import java.util.HashMap;

public class Content {
    private HashMap<String, Texture> textures;
    private HashMap<String, Sound> sounds;

    public Content() {
        this.textures = new HashMap<>();
        this.sounds = new HashMap<>();
    }

    public void loadTexture(String path, String key) {
        Texture tex = new Texture(Gdx.files.internal(path));
        textures.put(key, tex);
    }

    public void loadSound(String path, String key) {
        Sound sound = Gdx.audio.newSound(Gdx.files.internal(path));
        sounds.put(key,sound);
    }

    public Texture getTexture(String key) {
        return textures.get(key);
    }

    public Sound getSound(String key) {
        return sounds.get(key);
    }

    public void disposeTexture(String key) {
        Texture tex = textures.get(key);
        if (tex != null) {
            tex.dispose();
        }
    }
    public void disposeSound(String key) {
        Sound sound = sounds.get(key);
        if (sound != null) {
            sound.dispose();
        }
    }
}
