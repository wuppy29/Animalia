package animalia.mod.src.machine.extractor;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

public class GuiExtractor extends GuiContainer 
{
	private final TileEntityExtractor extractorInventory;
	
	public GuiExtractor(InventoryPlayer inventory, TileEntityExtractor tileEntity)
    {
        super(new ContainerExtractor(inventory, tileEntity));
        this.extractorInventory = tileEntity;
    }

	protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        String s = this.extractorInventory.isInvNameLocalized() ? this.extractorInventory.getInvName() : StatCollector.translateToLocal(this.extractorInventory.getInvName());
        this.fontRenderer.drawString(s, this.xSize / 2 - this.fontRenderer.getStringWidth(s) / 2, 6, 4210752);
        this.fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, this.ySize - 96 + 2, 4210752);
    }

    /**
     * Draw the background layer for the GuiContainer (everything behind the items)
     */
	@Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture("/gui/furnace.png");
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
        int i1;

        if (this.extractorInventory.isRunning())
        {
            i1 = this.extractorInventory.getTimeRemainingScaled(12);
            this.drawTexturedModalRect(k + 56, l + 36 + 12 - i1, 176, 12 - i1, 14, i1 + 2);
        }

        i1 = this.extractorInventory.getProgressScaled(24);
        this.drawTexturedModalRect(k + 79, l + 34, 176, 14, i1 + 1, 16);
    }
}
