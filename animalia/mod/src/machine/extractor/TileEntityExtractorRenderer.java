package animalia.mod.src.machine.extractor;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

import org.lwjgl.opengl.GL11;

import animalia.mod.src.client.model.ModelExtractor;

public class TileEntityExtractorRenderer extends TileEntitySpecialRenderer
{
	ModelExtractor model;
	
	public TileEntityExtractorRenderer()
	{
		model = new ModelExtractor();
	}
	
	public void renderAModelAt(TileEntityExtractor tileEntity, double d, double d1, double d2, float f) 
	{
		int rotation = 0;
		if(tileEntity.worldObj != null)
		{
			rotation = tileEntity.getBlockMetadata();
		}
		if(tileEntity.isRunning())
			bindTextureByName("extractor_in_use.png");
		else
			bindTextureByName("extractor.png"); 
		
		GL11.glPushMatrix();
		GL11.glTranslatef((float)d + 0.5F, (float)d1 + 1.5F, (float)d2 + 0.5F);
		GL11.glScalef(1.0F, -1F, -1F);
		GL11.glRotatef(rotation*90, 0.0F, 1.0F, 0.0F);
		model.renderTileEntity();
		GL11.glPopMatrix();
	}

	@Override
	public void renderTileEntityAt(TileEntity tileEntity, double d0, double d1, double d2, float f) 
	{
		this.renderAModelAt((TileEntityExtractor)tileEntity, d0, d1, d2, f);
	}
}
