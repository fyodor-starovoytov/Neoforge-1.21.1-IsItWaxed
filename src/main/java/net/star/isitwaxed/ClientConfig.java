package net.star.isitwaxed;

import net.neoforged.neoforge.common.ModConfigSpec;

public class ClientConfig {

    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.IntValue OVERLAY_TYPE = BUILDER
            .comment("0. Disable\n1. Small flower\n2. Yellow outline\n3. Yellow frame")
            .translation("isitwaxed.config.overlay_type")
            .defineInRange("overlay_type", 1, 0, 3);

    public static final ModConfigSpec.IntValue OVERLAY_CORNER = BUILDER
            .comment("1. Upper-left\n2. Upper-right\n3. Bottom-left\n4. Bottom-right")
            .translation("isitwaxed.config.overlay_corner")
            .defineInRange("overlay_corner", 2, 1, 4);

    public static final ModConfigSpec SPEC = BUILDER.build();
}
