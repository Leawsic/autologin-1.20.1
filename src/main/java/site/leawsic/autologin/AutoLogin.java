package site.leawsic.autologin;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AutoLogin implements ModInitializer {
    public static final String MOD_ID = "autologin";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("Hello " + MOD_ID);
    }
}