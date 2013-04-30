package animalia.mod.src.machine.extractor;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import animalia.mod.src.Animalia;
import animalia.mod.src.Constants;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockExtractor extends BlockContainer
{
    private final Random extractorRand = new Random();

    private final boolean isActive;

    private static boolean keepInventory = false;
    
    //Make sure you Annotate these as Client Side Only
    @SideOnly(Side.CLIENT)
    private Icon extractorTopIcon;
    @SideOnly(Side.CLIENT)
    private Icon extractorFrontIcon;
	
	public BlockExtractor(int blockID, boolean b) 
	{
		super(blockID, Material.rock);
		this.isActive = b;
	}

	public static void updateExtractorBlockState(boolean b, World world, int xCoord, int yCoord, int zCoord) 
	{
		int l = world.getBlockMetadata(xCoord, yCoord, zCoord);
        TileEntity tileentity = world.getBlockTileEntity(xCoord, yCoord, zCoord);
        keepInventory = true;

        if (b)
        {
        	world.setBlock(xCoord, yCoord, zCoord, Animalia.extractorOn.blockID);
        }
        else
        {
        	world.setBlock(xCoord, yCoord, zCoord, Animalia.extractorOff.blockID);
        }

        keepInventory = false;
        world.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, l, 2);

        if (tileentity != null)
        {
            tileentity.validate();
            world.setBlockTileEntity(xCoord, yCoord, zCoord, tileentity);
        }
	}

	@Override
	public TileEntity createNewTileEntity(World world) 
	{
		return new TileEntityExtractor();
	}	
	
    public void onBlockAdded(World par1World, int par2, int par3, int par4)
    {
        super.onBlockAdded(par1World, par2, par3, par4);
        this.setDefaultDirection(par1World, par2, par3, par4);
    }

    private void setDefaultDirection(World par1World, int par2, int par3, int par4)
    {
        if (!par1World.isRemote)
        {
            int l = par1World.getBlockId(par2, par3, par4 - 1);
            int i1 = par1World.getBlockId(par2, par3, par4 + 1);
            int j1 = par1World.getBlockId(par2 - 1, par3, par4);
            int k1 = par1World.getBlockId(par2 + 1, par3, par4);
            byte b0 = 3;

            if (Block.opaqueCubeLookup[l] && !Block.opaqueCubeLookup[i1])
            {
                b0 = 3;
            }

            if (Block.opaqueCubeLookup[i1] && !Block.opaqueCubeLookup[l])
            {
                b0 = 2;
            }

            if (Block.opaqueCubeLookup[j1] && !Block.opaqueCubeLookup[k1])
            {
                b0 = 5;
            }

            if (Block.opaqueCubeLookup[k1] && !Block.opaqueCubeLookup[j1])
            {
                b0 = 4;
            }

            par1World.setBlockMetadataWithNotify(par2, par3, par4, b0, 2);
        }
    }

    //Make sure you Annotate this as Client Side Only
    @SideOnly(Side.CLIENT)
    public Icon getBlockTextureFromSideAndMetadata(int par1, int par2)
    {
        return par1 == 1 ? this.extractorTopIcon : (par1 == 0 ? this.extractorTopIcon : (par1 != par2 ? this.blockIcon : this.extractorFrontIcon));
    }

    //Make sure you Annotate this as Client Side Only
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconRegister)
    {
        this.blockIcon = iconRegister.registerIcon("animalia:machine/extractor_side");
        this.extractorFrontIcon = iconRegister.registerIcon(this.isActive ? "animalia:machine/extractor_front_on" : "animalia:machine/extractor_front");
        this.extractorTopIcon = iconRegister.registerIcon("animalia:machine/extractor_top");
    }

    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9)
    {
    	TileEntity te = world.getBlockTileEntity(x, y, z);

        if (te == null || !(te instanceof TileEntityExtractor))
        {
            return false;
        }

        player.openGui(Animalia.instance, Constants.EXTRACTOR_GUI_ID, world, x, y, z);
        return true;
    }
    
    public void onBlockPlacedBy(World world, int xCoord, int yCoord, int zCoord, EntityLiving entityLiving, ItemStack itemstack)
    {
        int l = MathHelper.floor_double((double)(entityLiving.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;

        if (l == 0)
        {
        	world.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, 2, 2);
        }

        if (l == 1)
        {
        	world.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, 5, 2);
        }

        if (l == 2)
        {
        	world.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, 3, 2);
        }

        if (l == 3)
        {
        	world.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, 4, 2);
        }

        if (itemstack.hasDisplayName())
        {
            ((TileEntityFurnace)world.getBlockTileEntity(xCoord, yCoord, zCoord)).func_94129_a(itemstack.getDisplayName());
        }
    }
    
    public int idDropped(int par1, Random par2Random, int par3)
    {
        return Block.furnaceIdle.blockID;
    }
    
    public int idPicked(World world, int i, int j, int k)
    {
    	return Animalia.extractorOff.blockID;
    }
}
