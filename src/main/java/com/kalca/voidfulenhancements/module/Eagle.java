package com.kalca.voidfulenhancements.module;

import com.kalca.voidfulenhancements.settings.SliderSetting;
import com.kalca.voidfulenhancements.util.PlayerUtil;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class Eagle extends Module {

    private final SliderSetting distanceSetting = new SliderSetting("Distance", 0.3, 0.1, 1.0, 0.05);
    private final SliderSetting delaySetting = new SliderSetting("Delay", 200, 0, 1000, 10);

    private final Minecraft mc = Minecraft.getMinecraft();
    private long edgeSince = -1;
    private boolean autoSneaking;

    public Eagle() {
        super("Eagle", Category.MOVEMENT);
        settings.add(distanceSetting);
        settings.add(delaySetting);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.START) return;
        if (!isEnabled()) return;
        if (mc.thePlayer == null || mc.theWorld == null) return;

        if (atEdge()) {
            if (edgeSince == -1) edgeSince = System.currentTimeMillis();
            if (System.currentTimeMillis() - edgeSince >= (long) delaySetting.getValue()) {
                setSneak(true);
            }
        } else {
            edgeSince = -1;
            setSneak(false);
        }
    }

    private boolean atEdge() {
        EntityPlayer player = mc.thePlayer;
        if (!player.onGround || !PlayerUtil.isMoving(player)) return false;
        if (mc.thePlayer.movementInput.moveForward != 0 && mc.thePlayer.movementInput.moveStrafe != 0) return false;

        float yaw = player.rotationYaw;
        double dirX = -MathHelper.sin((float) Math.toRadians(yaw));
        double dirZ = MathHelper.cos((float) Math.toRadians(yaw));
        double d = distanceSetting.getValue();
        double px = player.posX + dirX * d;
        double pz = player.posZ + dirZ * d;
        double py = player.posY - 0.05;

        BlockPos feet = new BlockPos(px, py, pz);
        Block blockAtFeet = mc.theWorld.getBlockState(feet).getBlock();
        if (!blockAtFeet.isAir(mc.theWorld, feet)) return false;

        BlockPos below = feet.down();
        Block blockBelow = mc.theWorld.getBlockState(below).getBlock();
        return !blockBelow.isAir(mc.theWorld, below);
    }

    private void setSneak(boolean state) {
        if (autoSneaking == state) return;
        autoSneaking = state;
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindSneak.getKeyCode(), state);
    }

    @Override
    public void onEnable() {
        edgeSince = -1;
    }

    @Override
    public void onDisable() {
        setSneak(false);
        edgeSince = -1;
    }
}