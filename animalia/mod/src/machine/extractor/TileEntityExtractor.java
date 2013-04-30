package animalia.mod.src.machine.extractor;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

import net.minecraft.block.BlockFurnace;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import animalia.mod.src.MachineInfo;
import animalia.mod.src.network.IHandlePacket;
import animalia.mod.src.network.PacketHandler;

public class TileEntityExtractor extends TileEntity implements ISidedInventory, IHandlePacket, net.minecraftforge.common.ISidedInventory
{
	/*
	*I'm honestly not sure at the moment what these are used for. 
	*Once I get some time to look around in what getSizeInventorySide()
	*is used for, i'll rename these accordingly.
	*/
    private static final int[] field_102010_d = new int[] {0};
    private static final int[] field_102011_e = new int[] {2, 1};
    private static final int[] field_102009_f = new int[] {1};
	
	public ItemStack[] extractorStacks = new ItemStack[3];
	
	/**
	 * The number of ticks the Extractor will keep extracting
	 **/
	public int extractorRunTime = 0;
	
	/**
	 * The number of ticks that a fresh copy of the item currently undergoing extraction would keep the Extractor extracting for
	 **/
	public int totalItemRunTime = 0;
	
	/**
	 * The number of ticks the current item has been being extracted for
	 **/
	public int currentItemRunTime = 0;
	
	/**
	 * Localized String. Does not need to be Initialized, but will be used as the Inventory Name if it is.
	 **/
	public String customString;

	@Override
	public int getSizeInventory() 
	{
		return this.extractorStacks.length;
	}

	@Override
	public ItemStack getStackInSlot(int i) 
	{
		return this.extractorStacks[i];
	}

	@Override
	public ItemStack decrStackSize(int stackIndex, int j) 
	{
		if (this.extractorStacks[stackIndex] != null)
        {
            ItemStack itemstack;

            if (this.extractorStacks[stackIndex].stackSize <= j)
            {
                itemstack = this.extractorStacks[stackIndex];
                this.extractorStacks[stackIndex] = null;
                return itemstack;
            }
            else
            {
                itemstack = this.extractorStacks[stackIndex].splitStack(j);

                if (this.extractorStacks[stackIndex].stackSize == 0)
                {
                    this.extractorStacks[stackIndex] = null;
                }

                return itemstack;
            }
        }
        else
        {
            return null;
        }
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slot) 
	{
		 if (this.extractorStacks[slot] != null)
	        {
	            ItemStack itemstack = this.extractorStacks[slot];
	            this.extractorStacks[slot] = null;
	            return itemstack;
	        }
	        else
	        {
	            return null;
	        }
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack itemstack) 
	{
		this.extractorStacks[slot] = itemstack;
		
		if (itemstack != null && itemstack.stackSize > this.getInventoryStackLimit())
        {
			itemstack.stackSize = this.getInventoryStackLimit();
        }
	}

	@Override
	public String getInvName() 
	{
		return this.isInvNameLocalized() ? this.customString : "container.furnace";
	}

	@Override
	public int getInventoryStackLimit() 
	{
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer) 
	{
		return this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord, this.zCoord) != this ? false : entityplayer.getDistanceSq((double)this.xCoord + 0.5D, (double)this.yCoord + 0.5D, (double)this.zCoord + 0.5D) <= 64.0D;
	}
	
	public void readFromNBT(NBTTagCompound nbtTagCompound)
    {
        super.readFromNBT(nbtTagCompound);
        NBTTagList nbttaglist = nbtTagCompound.getTagList("Items");
        this.extractorStacks = new ItemStack[this.getSizeInventory()];

        for (int i = 0; i < nbttaglist.tagCount(); ++i)
        {
            NBTTagCompound nbttagcompound1 = (NBTTagCompound)nbttaglist.tagAt(i);
            byte b0 = nbttagcompound1.getByte("Slot");

            if (b0 >= 0 && b0 < this.extractorStacks.length)
            {
                this.extractorStacks[b0] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
            }
        }

        this.extractorRunTime = nbtTagCompound.getShort("RunTime");
        this.totalItemRunTime = nbtTagCompound.getShort("ItemRunTime");
        this.currentItemRunTime = getItemRunTime(this.extractorStacks[1]);

        if (nbtTagCompound.hasKey("CustomName"))
        {
            this.customString = nbtTagCompound.getString("CustomName");
        }
    }

    public void writeToNBT(NBTTagCompound nbtTagCompound)
    {
        super.writeToNBT(nbtTagCompound);
        nbtTagCompound.setShort("RunTime", (short)this.extractorRunTime);
        nbtTagCompound.setShort("ItemRunTime", (short)this.currentItemRunTime);
        NBTTagList nbttaglist = new NBTTagList();

        for (int i = 0; i < this.extractorStacks.length; ++i)
        {
            if (this.extractorStacks[i] != null)
            {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte("Slot", (byte)i);
                this.extractorStacks[i].writeToNBT(nbttagcompound1);
                nbttaglist.appendTag(nbttagcompound1);
            }
        }

        nbtTagCompound.setTag("Items", nbttaglist);

        if (this.isInvNameLocalized())
        {
        	nbtTagCompound.setString("CustomName", this.customString);
        }
    }
    
    public static int getItemRunTime(ItemStack itemstack)
    {	
    	if(!(itemstack == null))
    		return MachineInfo.getExtractorFuelRuntime(itemstack);
    	else
    		return 0;
    }
    
    @Override
    public void updateEntity()
    {
        boolean isRunning = this.extractorRunTime > 0;
        boolean flag1 = false;

        if (this.extractorRunTime > 0)
        {
            --this.extractorRunTime;
        }

        if (!this.worldObj.isRemote)
        {
            if (this.extractorRunTime == 0 && this.canExtract())
            {
                this.totalItemRunTime = this.extractorRunTime = getItemRunTime(this.extractorStacks[1]);

                if (this.extractorRunTime > 0)
                {
                    flag1 = true;

                    if (this.extractorStacks[1] != null)
                    {
                        --this.extractorStacks[1].stackSize;

                        if (this.extractorStacks[1].stackSize == 0)
                        {
                            this.extractorStacks[1] = this.extractorStacks[1].getItem().getContainerItemStack(extractorStacks[1]);
                        }
                    }
                }
            }

            if (this.isRunning() && this.canExtract())
            {
                ++this.currentItemRunTime;

                if (this.currentItemRunTime == 200)
                {
                    this.currentItemRunTime = 0;
                    this.extractItem();
                    flag1 = true;
                }
            }
            else
            {
                this.currentItemRunTime = 0;
            }

            if (isRunning != this.extractorRunTime > 0)
            {
                flag1 = true;
                BlockExtractor.updateExtractorBlockState(this.extractorRunTime > 0, this.worldObj, this.xCoord, this.yCoord, this.zCoord);
            }
        }

        if (flag1)
        {
            this.onInventoryChanged();
        }
        
        PacketHandler.sendTileEntityPacket(this, "Animalia", this.currentItemRunTime, this.extractorRunTime);
    }

	public boolean canExtract() 
	{
		return false;
	}

	public void extractItem() 
	{
		
	}

	public boolean isRunning() 
	{
		return this.extractorRunTime > 0;
	}

	@Override
	public void openChest() {}

	@Override
	public void closeChest() {}

	@Override
	public void handlePacketData(INetworkManager manager, Packet250CustomPayload packet, EntityPlayer player) 
	{
		ByteArrayDataInput bads = ByteStreams.newDataInput(packet.data);

		this.currentItemRunTime = bads.readInt();
		this.extractorRunTime = bads.readInt();
	}

	@Override
 	public int getStartInventorySide(ForgeDirection side) 
	{
		 if (side == ForgeDirection.DOWN) return 1;
         if (side == ForgeDirection.UP) return 0;
         return 2;
	}

	@Override
	public int getSizeInventorySide(ForgeDirection side) 
	{
		return 1;
	}

	@Override
	public boolean isInvNameLocalized() 
	{
		return this.customString != null && this.customString.length() > 0;
	}

	@Override
	public boolean isStackValidForSlot(int slot, ItemStack itemstack) 
	{
		return slot == 2 ? false : (slot == 1 ? isItemFuel(itemstack) : true);
	}

	public static boolean isItemFuel(ItemStack itemstack) 
	{
		return getItemRunTime(itemstack) > 0;
	}

	@Override
	public int[] getSizeInventorySide(int par1) 
	{
		return par1 == 0 ? field_102011_e : (par1 == 1 ? field_102010_d : field_102009_f);
	}

	@Override
	public boolean func_102007_a(int i, ItemStack itemstack, int j) 
	{
		return this.isStackValidForSlot(i, itemstack);
	}

	@Override
	public boolean func_102008_b(int i, ItemStack itemstack, int j) 
	{
		return j != 0 || i != 1 || itemstack.itemID == Item.bucketEmpty.itemID;
	}

	public int getTimeRemainingScaled(int i) 
	{
		 if (this.totalItemRunTime == 0)
	     {
	        this.totalItemRunTime = 200;
	     }

	     return this.currentItemRunTime * i / this.totalItemRunTime;
	}

	public int getProgressScaled(int i) 
	{
		return this.extractorRunTime * i / 200;
	}

}
