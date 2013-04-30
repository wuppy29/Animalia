package animalia.mod.src.block;

import net.minecraft.item.ItemStack;
import animalia.mod.src.item.ItemFossil;

public interface IExtractable
{
	public ItemStack[] getExtractionPossiblities();
}
