package animalia.mod.src.core;

import animalia.mod.src.api.AnimaliaAccess;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class MachineInfo 
{
	public static int getExtractorFuelRuntime(ItemStack itemstack)
	{
		if(itemstack.itemID == Item.coal.itemID)
			return 400;
		else
			return AnimaliaAccess.getExternalFuelRuntime(itemstack);
	}
}
