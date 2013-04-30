package animalia.mod.src.core;

import animalia.mod.src.machine.extractor.ContainerExtractor;
import animalia.mod.src.machine.extractor.TileEntityExtractor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;

public class CommonProxy implements IGuiHandler
{
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) 
	{
		TileEntity te = world.getBlockTileEntity(x, y, z);
		if(te != null)
		{
			switch(ID)
			{
				case Constants.EXTRACTOR_GUI_ID: return new ContainerExtractor(player.inventory, (TileEntityExtractor)world.getBlockTileEntity(x, y, z));
			}
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) 
	{
		return null;
	}

	public void registerRenders() 
	{
		
	}
}
