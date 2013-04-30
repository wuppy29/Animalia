package animalia.mod.src.core;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import animalia.mod.src.Animalia;
import cpw.mods.fml.common.registry.GameRegistry;

public class RecipeHandler
{
	public static void addRecipes()
	{
		GameRegistry.addRecipe(new ItemStack(Animalia.olivineAxe), new Object[]
		{
			"XX ", "XS ", " S ", Character.valueOf('X'), Animalia.olivineGem, Character.valueOf('S'), Item.stick
		});
		GameRegistry.addRecipe(new ItemStack(Animalia.olivineAxe), new Object[]
		{
			" XX", " SX", " S ", Character.valueOf('X'), Animalia.olivineGem, Character.valueOf('S'), Item.stick
		});
		GameRegistry.addRecipe(new ItemStack(Animalia.olivineHoe), new Object[]
		{
			"XX", " S", " S", Character.valueOf('X'), Animalia.olivineGem, Character.valueOf('S'), Item.stick
		});
		GameRegistry.addRecipe(new ItemStack(Animalia.olivineHoe), new Object[]
		{
			"XX", "S ", "S ", Character.valueOf('X'), Animalia.olivineGem, Character.valueOf('S'), Item.stick
		});
		GameRegistry.addRecipe(new ItemStack(Animalia.olivinePickaxe), new Object[]
		{
			"XXX", " S ", " S ", Character.valueOf('X'), Animalia.olivineGem, Character.valueOf('S'), Item.stick
		});
		GameRegistry.addRecipe(new ItemStack(Animalia.olivineShovel), new Object[]
		{
			"X", "S", "S", Character.valueOf('X'), Animalia.olivineGem, Character.valueOf('S'), Item.stick
		});
		GameRegistry.addRecipe(new ItemStack(Animalia.olivineSword), new Object[]
		{
			"X", "X", "S", Character.valueOf('X'), Animalia.olivineGem, Character.valueOf('S'), Item.stick
		});
	}
}