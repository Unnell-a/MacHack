/*
 * This file is part of the MacHack distribution (https://github.com/BleachDrinker420/bleachhack-1.14/).
 * Copyright (c) 2019 Bleach.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package mac.hack.module.mods;

import com.google.common.collect.Streams;
import com.google.common.eventbus.Subscribe;
import mac.hack.event.events.EventTick;
import mac.hack.module.Category;
import mac.hack.module.Module;
import mac.hack.setting.base.SettingSlider;
import mac.hack.setting.base.SettingToggle;
import mac.hack.utils.WorldUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket.Action;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import java.util.List;
import java.util.stream.Collectors;

public class BowBot extends Module {

    public BowBot() {
        super("BowBot", KEY_UNBOUND, Category.COMBAT, "Automatically aims and shoots at entities",
                new SettingToggle("Shoot", true),
                new SettingSlider("Charge: ", 0.1, 1, 0.5, 2),
                new SettingToggle("Aim", false));
    }

    @Subscribe
    public void onTick(EventTick event) {
        if (!(mc.player.getMainHandStack().getItem() instanceof RangedWeaponItem) || !mc.player.isUsingItem()) return;

        if (getSettings().get(0).asToggle().state && BowItem.getPullProgress(mc.player.getItemUseTime()) >= getSettings().get(1).asSlider().getValue()) {
            mc.player.stopUsingItem();
            mc.player.networkHandler.sendPacket(new PlayerActionC2SPacket(Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, Direction.UP));
        }

        // skidded from wurst no bully pls
        if (getSettings().get(2).asToggle().state) {
            List<Entity> targets = Streams.stream(mc.world.getEntities())
                    .filter(e -> e instanceof LivingEntity && e != mc.player)
                    .sorted((a, b) -> Float.compare(a.distanceTo(mc.player), b.distanceTo(mc.player))).collect(Collectors.toList());

            if (targets.isEmpty()) return;

            LivingEntity target = (LivingEntity) targets.get(0);

            // set velocity
            float velocity = (72000 - mc.player.getItemUseTimeLeft()) / 20F;
            velocity = (velocity * velocity + velocity * 2) / 3;

            if (velocity > 1) velocity = 1;

            // set position to aim at
            double d = mc.player.getPos().add(0, mc.player.getEyeHeight(mc.player.getPose()), 0)
                    .distanceTo(target.getBoundingBox().getCenter());
            double x = target.getX() + (target.getX() - target.prevX) * d
                    - mc.player.getX();
            double y = target.getY() + (target.getY() - target.prevY) * d
                    + target.getHeight() * 0.5 - mc.player.getY()
                    - mc.player.getEyeHeight(mc.player.getPose());
            double z = target.getZ() + (target.getZ() - target.prevZ) * d
                    - mc.player.getZ();

            // set yaw
            mc.player.yaw = (float) Math.toDegrees(Math.atan2(z, x)) - 90;

            // calculate needed pitch
            double hDistance = Math.sqrt(x * x + z * z);
            double hDistanceSq = hDistance * hDistance;
            float g = 0.006F;
            float velocitySq = velocity * velocity;
            float velocityPow4 = velocitySq * velocitySq;
            float neededPitch = (float) -Math.toDegrees(Math.atan((velocitySq - Math
                    .sqrt(velocityPow4 - g * (g * hDistanceSq + 2 * y * velocitySq)))
                    / (g * hDistance)));

            // set pitch
            if (Float.isNaN(neededPitch))
                WorldUtils.facePos(target.getX(), target.getY() + target.getHeight() / 2, target.getZ());
            else mc.player.pitch = neededPitch;
        }
    }

}
