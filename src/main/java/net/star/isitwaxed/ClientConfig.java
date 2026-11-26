package net.star.isitwaxed;


import net.minecraftforge.common.ForgeConfigSpec;

public class ClientConfig {

    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    public static final ForgeConfigSpec.IntValue OVERLAY_TYPE = BUILDER
            .comment("0. Disable\n1. Small flower\n2. Yellow outline\n3. Yellow frame")
            .translation("isitwaxed.config.overlay_type")
            .defineInRange("overlay_type", 1, 0, 3);

    public static final ForgeConfigSpec.IntValue OVERLAY_CORNER = BUILDER
            .comment("1. Upper-left\n2. Upper-right\n3. Bottom-left\n4. Bottom-right")
            .translation("isitwaxed.config.overlay_corner")
            .defineInRange("overlay_corner", 2, 1, 4);

    public static final ForgeConfigSpec SPEC = BUILDER.build();
}
