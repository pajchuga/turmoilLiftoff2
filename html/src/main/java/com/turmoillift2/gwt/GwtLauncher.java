package com.turmoillift2.gwt;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.turmoillift2.main.TurmoilLiftoff2;

/** Launches the GWT application. */
public class GwtLauncher extends GwtApplication {
        @Override
        public GwtApplicationConfiguration getConfig () {
            // Resizable application, uses available space in browser with no padding:
            GwtApplicationConfiguration cfg = new GwtApplicationConfiguration(true);
            cfg.padVertical = 0;
            cfg.padHorizontal = 0;
            //`return cfg;`
            // If you want a fixed size application, comment out the above resizable section,
            // and uncomment below:
            return new GwtApplicationConfiguration(Math.round(TurmoilLiftoff2.WORLD_WIDTH * 1.25f), Math.round(TurmoilLiftoff2.WORLD_HEIGHT * 1.25f));
        }

        @Override
        public ApplicationListener createApplicationListener () {
            return new TurmoilLiftoff2();
        }
}
