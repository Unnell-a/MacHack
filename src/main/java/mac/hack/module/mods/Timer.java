package mac.hack.module.mods;

import mac.hack.module.Category;
import mac.hack.module.Module;
import mac.hack.setting.base.SettingSlider;

public class Timer extends Module {

    public Timer() {
        super("Timer", KEY_UNBOUND, Category.WORLD, "more speeds",
                new SettingSlider("Speed: ", 0.01, 20, 1, 2));
    }

    // See MixinRenderTickCounter for code

}
