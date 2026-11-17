package net.star.isitwaxed;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.ModConfigSpec;

public class ClientConfig {

    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.IntValue OVERLAY_CORNER = BUILDER
            .comment("1. Upper-left\n2. Upper-right\n3. Bottom-left\n4. Bottom-right")
            .translation("isitwaxed.config.overlay_corner")
            .defineInRange("overlay_corner", 2, 1, 4);

    public static final ModConfigSpec.IntValue OVERLAY_TYPE = BUILDER
            .comment("1. Small flower\n2. Yellow outline")
            .translation("isitwaxed.config.overlay_type")
            .defineInRange("overlay_type", 2, 1, 2);


    public static final ModConfigSpec SPEC = BUILDER.build();
}
