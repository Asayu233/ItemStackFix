package xyz.asayu233.itemstackfix.mixin;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
    @Shadow @Nullable private CompoundTag tag;

    @Inject(
        method = "isSameItemSameTags",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/Objects;equals(Ljava/lang/Object;Ljava/lang/Object;)Z"
        ),
        cancellable = true
    )
    private static void istfix$isSameItemSameTags$hook(ItemStack p_150943_, ItemStack p_150944_, CallbackInfoReturnable<Boolean> cir) {
        if (!p_150943_.hasTag() && !p_150944_.hasTag())
            cir.setReturnValue(true);
    }

    @Redirect(
        method = "<init>(Lnet/minecraft/nbt/CompoundTag;)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/nbt/CompoundTag;contains(Ljava/lang/String;I)Z",
            ordinal = 0
        )
    )
    private boolean istfix$init$hook(CompoundTag instance, String p_128426_, int p_128427_) {
        return instance.get(p_128426_) instanceof CompoundTag c && !c.isEmpty();
    }

    @Inject(method = "save", at = @At("HEAD"))
    private void istfix$save$hook(CompoundTag p_41740_, CallbackInfoReturnable<CompoundTag> cir) {
        if (this.tag != null && this.tag.isEmpty())
            this.tag = null;
    }
}
