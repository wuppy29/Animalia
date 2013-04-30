package animalia.mod.src.client;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;
import animalia.mod.src.Animalia;
import animalia.mod.src.CommonProxy;
import animalia.mod.src.Constants;
import animalia.mod.src.machine.extractor.BlockExtractorItemRenderer;
import animalia.mod.src.machine.extractor.GuiExtractor;
import animalia.mod.src.machine.extractor.TileEntityExtractor;
import animalia.mod.src.machine.extractor.TileEntityExtractorRenderer;
import cpw.mods.fml.client.registry.ClientRegistry;

public class ClientProxy extends CommonProxy
{
	public Animalia modInstance = Animalia.instance;
	
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) 
	{
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) 
	{
		TileEntity te = world.getBlockTileEntity(x, y, z);
		if(te != null)
		{
			switch(ID)
			{
			case Constants.EXTRACTOR_GUI_ID: return new GuiExtractor(player.inventory, (TileEntityExtractor)te); 
			}
		}
		return null;
	}

	public void registerRenders() 
	{
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityExtractor.class, new TileEntityExtractorRenderer());
		MinecraftForgeClient.registerItemRenderer(modInstance.extractorOff.blockID, new BlockExtractorItemRenderer());
	}
}
