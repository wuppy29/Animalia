package animalia.mod.src.api;

import java.util.Set;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import animalia.mod.src.Animalia;
import animalia.mod.src.block.BlockExtractable;
import animalia.mod.src.item.ItemExtractable;
import animalia.mod.src.machine.extractor.ExtractorRecipes;

import com.google.common.collect.Sets;

public class AnimaliaAccess
{	
	private static Set<IExtractorFuelHandler> extractorFuelHandlers = Sets.newHashSet();
	private static Set<IExtractorResultHandler> extractorResultHandlers = Sets.newHashSet();

	public static Animalia getAnimalia()
	{
		return Animalia.instance;
	}
	
	public static void addExtractorRecipe(int inputItemOrBlockID, ItemStack outputItemstack, float expOnExtract)
	{
		ExtractorRecipes.extracting().addRecipe(inputItemOrBlockID, outputItemstack, expOnExtract);
	}
	
	public static void addExtractorRecipe(int inputItemOrBlockID, int metadata, ItemStack outputItemstack, float expOnExtract)
	{
		ExtractorRecipes.extracting().addRecipe(inputItemOrBlockID, metadata, outputItemstack, expOnExtract);
	}
	
	public static void registerExtractorFuelHandler(IExtractorFuelHandler handler)
	{
		if(!extractorFuelHandlers.contains(handler))
			extractorFuelHandlers.add(handler);
		else
			APILog.info("The External Extractor Fuel Handler " + handler + " has already been registered.");
	}
	
	public static int getExternalFuelRuntime(ItemStack itemstack) 
	{
		int fuelRuntime = 0;
		for(IExtractorFuelHandler handler : extractorFuelHandlers)
		{
			if(handler.getExtractorFuelRuntime(itemstack) > 0)
				if(!(fuelRuntime < 1) && handler.getExtractorFuelRuntime(itemstack) < fuelRuntime)
					fuelRuntime = handler.getExtractorFuelRuntime(itemstack);
		}
		return fuelRuntime;
	}
	
	public static void registerExtractionHandler(IExtractorResultHandler handler)
	{
		extractorResultHandlers.add(handler);
	}
	
	public static void onExtract(EntityPlayer player, ItemStack itemstack)
	{
		for(IExtractorResultHandler handler : extractorResultHandlers)
		{
			handler.onExtraction(player, itemstack);
		}
	}
	
	public static BlockExtractable newBlockExtractable(int blockID, Material material, ItemStack[] extractableItems)
	{
		return new BlockExtractable(blockID, material, extractableItems);
	}
	
	public static ItemExtractable newItemExtractable(int itemID, ItemStack[] items)
	{
		return new ItemExtractable(itemID, items);
	}
}
