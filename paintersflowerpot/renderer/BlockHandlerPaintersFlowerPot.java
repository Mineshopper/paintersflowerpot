package paintersflowerpot.renderer;

import net.minecraft.block.Block;
import net.minecraft.block.BlockGrass;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.Item;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.MinecraftForgeClient;
import paintersflowerpot.PaintersFlowerPot;
import paintersflowerpot.block.BlockPaintersFlowerPot;
import paintersflowerpot.tileentity.TEPaintersFlowerPot;
import paintersflowerpot.util.BlockProperties;
import paintersflowerpot.util.RenderHelper;
import biomesoplenty.api.Blocks;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

public class BlockHandlerPaintersFlowerPot implements ISimpleBlockRenderingHandler
{
	
	protected boolean hasMetadataOverride = false;
	protected int metadataOverride = 0;
	
	protected Icon[] iconOverrideIndex =
	{
		Block.flowerPot.getBlockTextureFromSide(0),
		Block.flowerPot.getBlockTextureFromSide(1),
		Block.flowerPot.getBlockTextureFromSide(2),
		Block.flowerPot.getBlockTextureFromSide(3),
		Block.flowerPot.getBlockTextureFromSide(4),
		Block.flowerPot.getBlockTextureFromSide(5)
	};
	
	protected boolean	isPotBottom = false,
						isPot = false,
						isSoil = false;
	
	/**
	 * Clears icon override index.
	 */
	protected void clearIconOverrideIndex()
	{
		iconOverrideIndex = new Icon[] {
				Block.flowerPot.getBlockTextureFromSide(0),
				Block.flowerPot.getBlockTextureFromSide(1),
				Block.flowerPot.getBlockTextureFromSide(2),
				Block.flowerPot.getBlockTextureFromSide(3),
				Block.flowerPot.getBlockTextureFromSide(4),
				Block.flowerPot.getBlockTextureFromSide(5)
		};
	}
	
	/**
	 * Sets metadata override.
	 */
	protected void setMetadataOverride(int metadata)
	{
		hasMetadataOverride = true;
		metadataOverride = metadata;
	}
	
	/**
	 * Clears metadata override.
	 */
	protected void clearMetadataOverride()
	{
		hasMetadataOverride = false;
	}
	
	/**
	 * Stores uncolored, shaded AO values for each corner of face (AO)
	 */
	protected float[] ao_no_color = { 0.0F, 0.0F, 0.0F, 0.0F };
    
	/**
	 * Stores uncolored light value for face (NON-AO)
	 */
	protected float[] rgb_no_color = { 0.0F, 0.0F, 0.0F };
	
    @Override
	public void renderInventoryBlock(Block var1, int var2, int var3, RenderBlocks var4) {  }
    
    @Override
	public boolean renderWorldBlock(IBlockAccess blockAccess, int x, int y, int z, Block block, int modelId, RenderBlocks renderBlocks)
    {
    	TEPaintersFlowerPot TE = (TEPaintersFlowerPot)blockAccess.getBlockTileEntity(x, y, z);
    	
    	int renderPass = MinecraftForgeClient.getRenderPass();

    	renderBlocks.renderAllFaces = true;
    	
    	if (BlockProperties.getCoverBlock(TE).getRenderBlockPass() == renderPass || renderBlocks.hasOverrideBlockTexture()) {
    		isPot = true;
    		renderPot(TE, renderBlocks, BlockProperties.getCoverBlock(TE), x, y, z);
    		isPot = false;
    	}
        
        if (renderPass == 0)
        {
	        if (BlockProperties.hasSoil(TE)) {
	        	isSoil = true;
	        	renderSoil(TE, renderBlocks, BlockProperties.getSoilBlock(TE), x, y, z);
	        	isSoil = false;
	        }
	
	        if (BlockProperties.hasPlant(TE))
	        	renderPlant(TE, renderBlocks, BlockProperties.getPlantBlock(TE), x, y, z);
        }
        
        renderBlocks.renderAllFaces = false;

    	return true;
    }

    @Override
	public boolean shouldRender3DInInventory() {
        return false;
    }

    @Override
	public int getRenderId() {
        return 0;
    }
    
    /**
     * Renders flower pot
     */
    public boolean renderPot(TEPaintersFlowerPot TE, RenderBlocks renderBlocks, Block block, int x, int y, int z)
    {
    	setMetadataOverride(TE.potCoverMetadata);

    	isPotBottom = true;
    	renderBlocks.setRenderBounds(0.375D, 0.0D, 0.375D, 0.625D, 0.0625D, 0.625D);
		renderStandardBlock(TE, renderBlocks, block, x, y, z);
		isPotBottom = false;
		
		isPot = true;
		
		clearIconOverrideIndex();

		if (BlockProperties.hasDyeColor(TE)) {
			iconOverrideIndex[1] = BlockPaintersFlowerPot.dyeIcon[TE.potDyeColor];
			iconOverrideIndex[2] = BlockPaintersFlowerPot.dyeIcon[TE.potDyeColor];
			iconOverrideIndex[3] = BlockPaintersFlowerPot.dyeIcon[TE.potDyeColor];
			iconOverrideIndex[4] = BlockPaintersFlowerPot.dyeIcon[TE.potDyeColor];
		} else if (BlockProperties.hasSpecialColor(TE)) {
			iconOverrideIndex[1] = BlockPaintersFlowerPot.specialIcon[TE.potSpecialColor];
			iconOverrideIndex[2] = BlockPaintersFlowerPot.specialIcon[TE.potSpecialColor];
			iconOverrideIndex[3] = BlockPaintersFlowerPot.specialIcon[TE.potSpecialColor];
			iconOverrideIndex[4] = BlockPaintersFlowerPot.specialIcon[TE.potSpecialColor];
		}
    	renderBlocks.setRenderBounds(0.3125D, 0.0D, 0.3125D, 0.375D, 0.375D, 0.6875D);
		renderStandardBlock(TE, renderBlocks, block, x, y, z);
		clearIconOverrideIndex();
		
		if (BlockProperties.hasDyeColor(TE)) {
			iconOverrideIndex[1] = BlockPaintersFlowerPot.dyeIcon[TE.potDyeColor];
			iconOverrideIndex[2] = BlockPaintersFlowerPot.dyeIcon[TE.potDyeColor];
			iconOverrideIndex[3] = BlockPaintersFlowerPot.dyeIcon[TE.potDyeColor];
			iconOverrideIndex[5] = BlockPaintersFlowerPot.dyeIcon[TE.potDyeColor];
		} else if (BlockProperties.hasSpecialColor(TE)) {
			iconOverrideIndex[1] = BlockPaintersFlowerPot.specialIcon[TE.potSpecialColor];
			iconOverrideIndex[2] = BlockPaintersFlowerPot.specialIcon[TE.potSpecialColor];
			iconOverrideIndex[3] = BlockPaintersFlowerPot.specialIcon[TE.potSpecialColor];
			iconOverrideIndex[5] = BlockPaintersFlowerPot.specialIcon[TE.potSpecialColor];
		}
    	renderBlocks.setRenderBounds(0.625D, 0.0D, 0.3125D, 0.6875D, 0.375D, 0.6875D);
		renderStandardBlock(TE, renderBlocks, block, x, y, z);
		clearIconOverrideIndex();

		if (BlockProperties.hasDyeColor(TE)) {
			iconOverrideIndex[1] = BlockPaintersFlowerPot.dyeIcon[TE.potDyeColor];
			iconOverrideIndex[2] = BlockPaintersFlowerPot.dyeIcon[TE.potDyeColor];
		} else if (BlockProperties.hasSpecialColor(TE)) {
			iconOverrideIndex[1] = BlockPaintersFlowerPot.specialIcon[TE.potSpecialColor];
			iconOverrideIndex[2] = BlockPaintersFlowerPot.specialIcon[TE.potSpecialColor];
		}
    	renderBlocks.setRenderBounds(0.375D, 0.0D, 0.3125D, 0.625D, 0.375D, 0.375D);
		renderStandardBlock(TE, renderBlocks, block, x, y, z);
		clearIconOverrideIndex();
		
		if (BlockProperties.hasDyeColor(TE)) {
			iconOverrideIndex[1] = BlockPaintersFlowerPot.dyeIcon[TE.potDyeColor];
			iconOverrideIndex[3] = BlockPaintersFlowerPot.dyeIcon[TE.potDyeColor];
		} else if (BlockProperties.hasSpecialColor(TE)) {
			iconOverrideIndex[1] = BlockPaintersFlowerPot.specialIcon[TE.potSpecialColor];
			iconOverrideIndex[3] = BlockPaintersFlowerPot.specialIcon[TE.potSpecialColor];
		}
    	renderBlocks.setRenderBounds(0.375D, 0.0D, 0.625D, 0.625D, 0.375D, 0.6875D);
		renderStandardBlock(TE, renderBlocks, block, x, y, z);
		clearIconOverrideIndex();
		
		isPot = false;
		
		clearMetadataOverride();
    	
    	return true;
    }
    
    /**
     * Renders soil
     */
    public boolean renderSoil(TEPaintersFlowerPot TE, RenderBlocks renderBlocks, Block block, int x, int y, int z)
    {
    	setMetadataOverride(TE.potSoilMetadata);
    	
    	isSoil = true;
    	
    	renderBlocks.setRenderBounds(0.375D, 0.0625D, 0.375D, 0.625D, 0.25D, 0.625D);
		renderStandardBlock(TE, renderBlocks, block, x, y, z);
		
		isSoil = false;
		
    	clearMetadataOverride();
    	
    	return true;
    }
    
    /**
     * Renders plant
     */
    public boolean renderPlant(TEPaintersFlowerPot TE, RenderBlocks renderBlocks, Block block, int x, int y, int z)
    {
    	Tessellator tessellator = Tessellator.instance;
		tessellator.addTranslation(0.0F, 0.25F, 0.0F);
    	
		int metadata = renderBlocks.blockAccess.getBlockMetadata(x, y, z);
		
		// Render leaf blocks
		if (block.blockMaterial == Material.leaves || (block.blockMaterial == Material.cactus && block != Block.cactus))
		{
			float renderWidth = 0.125F;
			renderBlocks.setRenderBounds(0.5F - renderWidth, 0.0D, 0.5F - renderWidth, 0.5F + renderWidth, 0.25D, 0.5F + renderWidth);
			renderBlocks.renderStandardBlock(block, x, y, z);
			renderBlocks.setRenderBounds(0.5F - renderWidth, 0.25D, 0.5F - renderWidth, 0.5F + renderWidth, 0.50D, 0.5F + renderWidth);
			renderBlocks.renderStandardBlock(block, x, y, z);
			renderBlocks.setRenderBounds(0.5F - renderWidth, 0.50D, 0.5F - renderWidth, 0.5F + renderWidth, 0.75D, 0.5F + renderWidth);
			renderBlocks.renderStandardBlock(block, x, y, z);
			renderBlocks.setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
		}

		// Render vine-type plants and sugarcane using thin-profile crossed squares
		else if (block == Block.reed || block.blockMaterial == Material.vine) {
			if (isThinPlantException(block, metadata))
				renderPlantCrossedSquares(renderBlocks, block, metadata, x, y, z);
			else
				renderPlantThinCrossedSquares(renderBlocks, block, x, y, z, getPlantTexture(TE, metadata));
		}

		// Render cactus
		else if (block == Block.cactus)
			drawPlantCactus(renderBlocks, block, x, y, z);

		// By default, render plant as crossed squares
		else {
			renderBlocks.overrideBlockTexture = getPlantTexture(TE, 0);
			renderPlantCrossedSquares(renderBlocks, block, metadata, x, y, z);
			renderBlocks.clearOverrideBlockTexture();
		}
		
		tessellator.addTranslation(0.0F, -0.25F, 0.0F);
    	
    	return true;
    }
    	
    /**
     * Retrieves the plant block texture to use. Args: tileEntity, side
     */
    public Icon getPlantTexture(TEPaintersFlowerPot TE, int side)
    {
    	Block block = BlockProperties.getPlantBlock(TE);
    	int metadata = TE.worldObj.getBlockMetadata(TE.xCoord, TE.yCoord, TE.zCoord);

    	return usesCustomRenderer(block.getRenderType()) ? getAlternateIcon(block, side, metadata) : block.getIcon(side, metadata);
    }
    
    private boolean usesCustomRenderer(int renderType) {
    	return renderType > 39;
    }
    
    /*
     * Retrieve alternate icon for rendering plant.
     */
    private Icon getAlternateIcon(Block block, int side, int metadata) {
    	if (Blocks.flowers.isPresent() && block == Blocks.flowers.get() && metadata == 13) // BoP exception for Sunflower Top
    		return block.getIcon(2, 14);
    	if (Blocks.plants.isPresent() && block == Blocks.plants.get()) // BoP exception for all plants
    		return block.getIcon(side, metadata);
    	else
    		return Item.itemsList[block.blockID].getIconFromDamage(metadata);
    }
    
    /*
     * For thin plants (mostly vine), returns whether plant should render as normal crossed squares
     */
    private boolean isThinPlantException(Block block, int metadata) {
    	if (block == Block.tallGrass && metadata != 1)
    		return true;
    	else if (block == Block.deadBush)
    		return true;
    	else if (Blocks.plants.isPresent() && block == Blocks.plants.get() && metadata == 5) // BoP exception for Thorns
    		return true;
    	else if (Blocks.foliage.isPresent() && block == Blocks.foliage.get() && metadata == 7) // BoP exception for Poison Ivy
    		return true;
    	else
    		return false;
    }
            
    /**
     * Renders any block requiring crossed squares such as reeds, flowers, and mushrooms
     */
    public boolean renderPlantCrossedSquares(RenderBlocks renderBlocks, Block block, int blockMetadata, int x, int y, int z)
    {
        Tessellator tessellator = Tessellator.instance;
        tessellator.setBrightness(block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y, z));

        int hexColor = block.colorMultiplier(renderBlocks.blockAccess, x, y, z);
        float red = (hexColor >> 16 & 255) / 255.0F;
        float green = (hexColor >> 8 & 255) / 255.0F;
        float blue = (hexColor & 255) / 255.0F;
        tessellator.setColorOpaque_F(red, green, blue);
        
		if (block.getBlockColor() != 16777215 && PaintersFlowerPot.overrideColor)
			tessellator.setColorOpaque_F(0.45F, 0.80F, 0.30F);
		
		float renderScale = 0.75F;
		
		if (block == Block.plantRed || block == Block.plantYellow || block == Block.mushroomRed || block == Block.mushroomBrown)
			renderScale = 1.0F;

        renderBlocks.drawCrossedSquares(block, blockMetadata, x, y, z, renderScale);
        return true;
    }
	
	/**
	 * Renders thin-profile crossed squares (reeds, vine-type blocks).
	 */
	public void renderPlantThinCrossedSquares(RenderBlocks renderBlocks, Block block, double x, double y, double z, Icon icon)
	{
		Tessellator tessellator = Tessellator.instance;
		
    	tessellator.setBrightness(block.getMixedBrightnessForBlock(renderBlocks.blockAccess, (int)x, (int)y, (int)z));

        int hexColor = block.colorMultiplier(renderBlocks.blockAccess, (int)x, (int)y, (int)z);
        float red = (hexColor >> 16 & 255) / 255.0F;
        float green = (hexColor >> 8 & 255) / 255.0F;
        float blue = (hexColor & 255) / 255.0F;
        tessellator.setColorOpaque_F(red, green, blue);
        
		if (block.getBlockColor() != 16777215 && PaintersFlowerPot.overrideColor)
			tessellator.setColorOpaque_F(0.45F, 0.80F, 0.30F);

    	double xCoordLow = icon.getInterpolatedU(0.0D);
    	double xCoordHigh = icon.getInterpolatedU(4.0D);
    	double yCoordHigh = icon.getInterpolatedV(0.0D);
    	double yCoordLow = icon.getInterpolatedV(16.0D);
		double rotatedScaleFactor = 0.45D * 0.375F;
		double xMin = x + 0.5D - rotatedScaleFactor;
		double xMax = x + 0.5D + rotatedScaleFactor;
		double zMin = z + 0.5D - rotatedScaleFactor;
		double zMax = z + 0.5D + rotatedScaleFactor;

		tessellator.addVertexWithUV(xMin, y + 0.75F, zMin, xCoordLow, yCoordHigh);
		tessellator.addVertexWithUV(xMin, y + 0.0D, zMin, xCoordLow, yCoordLow);
		tessellator.addVertexWithUV(x + 0.5F, y + 0.0D, z + 0.5F, xCoordHigh, yCoordLow);
		tessellator.addVertexWithUV(x + 0.5F, y + 0.75F, z + 0.5F, xCoordHigh, yCoordHigh);
		
		tessellator.addVertexWithUV(xMax, y + 0.75F, zMin, xCoordLow, yCoordHigh);
		tessellator.addVertexWithUV(xMax, y + 0.0D, zMin, xCoordLow, yCoordLow);
		tessellator.addVertexWithUV(x + 0.5F, y + 0.0D, z + 0.5F, xCoordHigh, yCoordLow);
		tessellator.addVertexWithUV(x + 0.5F, y + 0.75F, z + 0.5F, xCoordHigh, yCoordHigh);
		
		tessellator.addVertexWithUV(xMax, y + 0.75F, zMax, xCoordLow, yCoordHigh);
		tessellator.addVertexWithUV(xMax, y + 0.0D, zMax, xCoordLow, yCoordLow);
		tessellator.addVertexWithUV(x + 0.5F, y + 0.0D, z + 0.5F, xCoordHigh, yCoordLow);
		tessellator.addVertexWithUV(x + 0.5F, y + 0.75F, z + 0.5F, xCoordHigh, yCoordHigh);
		
		tessellator.addVertexWithUV(xMin, y + 0.75F, zMax, xCoordLow, yCoordHigh);
		tessellator.addVertexWithUV(xMin, y + 0.0D, zMax, xCoordLow, yCoordLow);
		tessellator.addVertexWithUV(x + 0.5F, y + 0.0D, z + 0.5F, xCoordHigh, yCoordLow);
		tessellator.addVertexWithUV(x + 0.5F, y + 0.75F, z + 0.5F, xCoordHigh, yCoordHigh);
		
    	xCoordLow = icon.getInterpolatedU(12.0D);
    	xCoordHigh = icon.getInterpolatedU(16.0D);

		tessellator.addVertexWithUV(x + 0.5F, y + 0.75F, z + 0.5F, xCoordLow, yCoordHigh);
		tessellator.addVertexWithUV(x + 0.5F, y + 0.0D, z + 0.5F, xCoordLow, yCoordLow);
		tessellator.addVertexWithUV(xMin, y + 0.0D, zMin, xCoordHigh, yCoordLow);
		tessellator.addVertexWithUV(xMin, y + 0.75F, zMin, xCoordHigh, yCoordHigh);
		
		tessellator.addVertexWithUV(x + 0.5F, y + 0.75F, z + 0.5F, xCoordLow, yCoordHigh);
		tessellator.addVertexWithUV(x + 0.5F, y + 0.0D, z + 0.5F, xCoordLow, yCoordLow);
		tessellator.addVertexWithUV(xMax, y + 0.0D, zMin, xCoordHigh, yCoordLow);
		tessellator.addVertexWithUV(xMax, y + 0.75F, zMin, xCoordHigh, yCoordHigh);
		
		tessellator.addVertexWithUV(x + 0.5F, y + 0.75F, z + 0.5F, xCoordLow, yCoordHigh);
		tessellator.addVertexWithUV(x + 0.5F, y + 0.0D, z + 0.5F, xCoordLow, yCoordLow);
		tessellator.addVertexWithUV(xMax, y + 0.0D, zMax, xCoordHigh, yCoordLow);
		tessellator.addVertexWithUV(xMax, y + 0.75F, zMax, xCoordHigh, yCoordHigh);
		
		tessellator.addVertexWithUV(x + 0.5F, y + 0.75F, z + 0.5F, xCoordLow, yCoordHigh);
		tessellator.addVertexWithUV(x + 0.5F, y + 0.0D, z + 0.5F, xCoordLow, yCoordLow);
		tessellator.addVertexWithUV(xMin, y + 0.0D, zMax, xCoordHigh, yCoordLow);
		tessellator.addVertexWithUV(xMin, y + 0.75F, zMax, xCoordHigh, yCoordHigh);
	}

	/**
	 * 	Renders cactus.
	 */
	private void drawPlantCactus(RenderBlocks renderBlocks, Block block, double x, double y, double z)
	{
		Tessellator tessellator = Tessellator.instance;
		
    	tessellator.setBrightness(block.getMixedBrightnessForBlock(renderBlocks.blockAccess, (int)x, (int)y, (int)z));

        int hexColor = block.colorMultiplier(renderBlocks.blockAccess, (int)x, (int)y, (int)z);
        float red = (hexColor >> 16 & 255) / 255.0F;
        float green = (hexColor >> 8 & 255) / 255.0F;
        float blue = (hexColor & 255) / 255.0F;
        tessellator.setColorOpaque_F(red, green, blue);
		
		Icon icon;
		
		icon = block.getBlockTextureFromSide(2);

    	double xCoordLow = icon.getInterpolatedU(0.0D);
    	double xCoordHigh = icon.getInterpolatedU(3.0D);
    	double yCoordHigh = icon.getInterpolatedV(0.0D);
    	double yCoordLow = icon.getInterpolatedV(16.0D);

		tessellator.addVertexWithUV(x+0.3125F, y, z+0.375F, xCoordLow, yCoordLow);
		tessellator.addVertexWithUV(x+0.3125F, y+0.75F, z+0.375F, xCoordLow, yCoordHigh);
		tessellator.addVertexWithUV(x+0.5F, y+0.75F, z+0.375F, xCoordHigh, yCoordHigh);
		tessellator.addVertexWithUV(x+0.5F, y, z+0.375F, xCoordHigh, yCoordLow);

		tessellator.addVertexWithUV(x+0.5F, y, z+0.625F, xCoordHigh, yCoordLow);
		tessellator.addVertexWithUV(x+0.5F, y+0.75F, z+0.625F, xCoordHigh, yCoordHigh);
		tessellator.addVertexWithUV(x+0.3125F, y+0.75F, z+0.625F, xCoordLow, yCoordHigh);
		tessellator.addVertexWithUV(x+0.3125F, y, z+0.625F, xCoordLow, yCoordLow);

		tessellator.addVertexWithUV(x+0.625F, y, z+0.3125F, xCoordLow, yCoordLow);
		tessellator.addVertexWithUV(x+0.625F, y+0.75F, z+0.3125F, xCoordLow, yCoordHigh);
		tessellator.addVertexWithUV(x+0.625F, y+0.75F, z+0.5F, xCoordHigh, yCoordHigh);
		tessellator.addVertexWithUV(x+0.625F, y, z+0.5F, xCoordHigh, yCoordLow);

		tessellator.addVertexWithUV(x+0.375F, y, z+0.5F, xCoordHigh, yCoordLow);
		tessellator.addVertexWithUV(x+0.375F, y+0.75F, z+0.5F, xCoordHigh, yCoordHigh);
		tessellator.addVertexWithUV(x+0.375F, y+0.75F, z+0.3125F, xCoordLow, yCoordHigh);
		tessellator.addVertexWithUV(x+0.375F, y, z+0.3125F, xCoordLow, yCoordLow);

    	xCoordLow = icon.getInterpolatedU(13.0D);
    	xCoordHigh = icon.getInterpolatedU(16.0D);

		tessellator.addVertexWithUV(x+0.5F, y, z+0.375F, xCoordLow, yCoordLow);
		tessellator.addVertexWithUV(x+0.5F, y+0.75F, z+0.375F, xCoordLow, yCoordHigh);
		tessellator.addVertexWithUV(x+0.6875F, y+0.75F, z+0.375F, xCoordHigh, yCoordHigh);
		tessellator.addVertexWithUV(x+0.6875F, y, z+0.375F, xCoordHigh, yCoordLow);

		tessellator.addVertexWithUV(x+0.6875F, y, z+0.625F, xCoordHigh, yCoordLow);
		tessellator.addVertexWithUV(x+0.6875F, y+0.75F, z+0.625F, xCoordHigh, yCoordHigh);
		tessellator.addVertexWithUV(x+0.5F, y+0.75F, z+0.625F, xCoordLow, yCoordHigh);
		tessellator.addVertexWithUV(x+0.5F, y, z+0.625F, xCoordLow, yCoordLow);

		tessellator.addVertexWithUV(x+0.625F, y, z+0.5F, xCoordLow, yCoordLow);
		tessellator.addVertexWithUV(x+0.625F, y+0.75F, z+0.5F, xCoordLow, yCoordHigh);
		tessellator.addVertexWithUV(x+0.625F, y+0.75F, z+0.6875F, xCoordHigh, yCoordHigh);
		tessellator.addVertexWithUV(x+0.625F, y, z+0.6875F, xCoordHigh, yCoordLow);

		tessellator.addVertexWithUV(x+0.375F, y, z+0.6875F, xCoordHigh, yCoordLow);
		tessellator.addVertexWithUV(x+0.375F, y+0.75F, z+0.6875F, xCoordHigh, yCoordHigh);
		tessellator.addVertexWithUV(x+0.375F, y+0.75F, z+0.5F, xCoordLow, yCoordHigh);
		tessellator.addVertexWithUV(x+0.375F, y, z+0.5F, xCoordLow, yCoordLow);

		/**
		 * 	Draw top of cactus
		 */
		icon = block.getBlockTextureFromSide(1);
		
    	xCoordLow = icon.getInterpolatedU(6.0D);
    	xCoordHigh = icon.getInterpolatedU(10.0D);
    	yCoordHigh = icon.getInterpolatedV(6.0D);
    	yCoordLow = icon.getInterpolatedV(10.0D);
		
		tessellator.addVertexWithUV(x+0.375F, y+0.75F, z+0.625F, xCoordLow, yCoordLow);
		tessellator.addVertexWithUV(x+0.625F, y+0.75F, z+0.625F, xCoordLow, yCoordHigh);
		tessellator.addVertexWithUV(x+0.625F, y+0.75F, z+0.375F, xCoordHigh, yCoordHigh);
		tessellator.addVertexWithUV(x+0.375F, y+0.75F, z+0.375F, xCoordHigh, yCoordLow);
	}
	
    /**
     * Renders the vanilla pot bottom for uncovered flower pot and glass covered flower pot.
     */
    public void renderOffsetTextureForPotBottom(RenderBlocks renderBlocks, double x, double y, double z, Icon icon)
    {
    	Tessellator tessellator = Tessellator.instance;
    	
    	if (renderBlocks.hasOverrideBlockTexture())
    		icon = renderBlocks.overrideBlockTexture;

	    double uMin = icon.getInterpolatedU(6.0D);
	    double uMax = icon.getInterpolatedU(10.0D);
	    double vMax = icon.getInterpolatedV(16.0D);
	    double vMin = icon.getInterpolatedV(12.0D);

    	double xMin = x + renderBlocks.renderMinX;
    	double xMax = x + renderBlocks.renderMaxX;
    	double yMax = y + renderBlocks.renderMaxY;
    	double zMin = z + renderBlocks.renderMinZ;
    	double zMax = z + renderBlocks.renderMaxZ;

    	if (renderBlocks.enableAO) {
	    	tessellator.setColorOpaque_F(renderBlocks.colorRedTopLeft, renderBlocks.colorGreenTopLeft, renderBlocks.colorBlueTopLeft);
	    	tessellator.setBrightness(renderBlocks.brightnessTopLeft);
	    	tessellator.addVertexWithUV(xMax, yMax, zMax, uMax, vMax);
	    	tessellator.setColorOpaque_F(renderBlocks.colorRedBottomLeft, renderBlocks.colorGreenBottomLeft, renderBlocks.colorBlueBottomLeft);
	    	tessellator.setBrightness(renderBlocks.brightnessBottomLeft);
	    	tessellator.addVertexWithUV(xMax, yMax, zMin, uMax, vMin);
	    	tessellator.setColorOpaque_F(renderBlocks.colorRedBottomRight, renderBlocks.colorGreenBottomRight, renderBlocks.colorBlueBottomRight);
	    	tessellator.setBrightness(renderBlocks.brightnessBottomRight);
	    	tessellator.addVertexWithUV(xMin, yMax, zMin, uMin, vMin);
	    	tessellator.setColorOpaque_F(renderBlocks.colorRedTopRight, renderBlocks.colorGreenTopRight, renderBlocks.colorBlueTopRight);
	    	tessellator.setBrightness(renderBlocks.brightnessTopRight);
	    	tessellator.addVertexWithUV(xMin, yMax, zMax, uMin, vMax);
    	} else {
	    	tessellator.addVertexWithUV(xMax, yMax, zMax, uMax, vMax);
	    	tessellator.addVertexWithUV(xMax, yMax, zMin, uMax, vMin);
	    	tessellator.addVertexWithUV(xMin, yMax, zMin, uMin, vMin);
	    	tessellator.addVertexWithUV(xMin, yMax, zMax, uMin, vMax);
    	}
    }
	
    /**
     * Multiplies AO by a color.
     */
    protected void aoMultiplyByColor(RenderBlocks renderBlocks, float red, float green, float blue)
    {
    	renderBlocks.colorRedTopLeft *= red;
    	renderBlocks.colorGreenTopLeft *= green;
    	renderBlocks.colorBlueTopLeft *= blue;
    	renderBlocks.colorRedTopRight *= red;
    	renderBlocks.colorGreenTopRight *= green;
    	renderBlocks.colorBlueTopRight *= blue;
    	renderBlocks.colorRedBottomRight *= red;
    	renderBlocks.colorGreenBottomRight *= green;
    	renderBlocks.colorBlueBottomRight *= blue;
    	renderBlocks.colorRedBottomLeft *= red;
    	renderBlocks.colorGreenBottomLeft *= green;
    	renderBlocks.colorBlueBottomLeft *= blue;
    }

    /**
     * Sets AO color.
     */
    protected void aoSetColor(TEPaintersFlowerPot TE, RenderBlocks renderBlocks, Block block, int side, float red, float green, float blue, float lightness)
    {    	
		/*
		 * Set base light values against color multiplier
		 */
		renderBlocks.colorRedTopLeft = renderBlocks.colorRedBottomLeft = renderBlocks.colorRedBottomRight = renderBlocks.colorRedTopRight = red * lightness;
    	renderBlocks.colorGreenTopLeft = renderBlocks.colorGreenBottomLeft = renderBlocks.colorGreenBottomRight = renderBlocks.colorGreenTopRight = green * lightness;
    	renderBlocks.colorBlueTopLeft = renderBlocks.colorBlueBottomLeft = renderBlocks.colorBlueBottomRight = renderBlocks.colorBlueTopRight = blue * lightness;

        /*
         * Shade AO values.
         */
    	renderBlocks.colorRedTopLeft *= ao_no_color[0];
    	renderBlocks.colorGreenTopLeft *= ao_no_color[0];
    	renderBlocks.colorBlueTopLeft *= ao_no_color[0];
    	renderBlocks.colorRedTopRight *= ao_no_color[1];
    	renderBlocks.colorGreenTopRight *= ao_no_color[1];
    	renderBlocks.colorBlueTopRight *= ao_no_color[1];
    	renderBlocks.colorRedBottomRight *= ao_no_color[2];
    	renderBlocks.colorGreenBottomRight *= ao_no_color[2];
    	renderBlocks.colorBlueBottomRight *= ao_no_color[2];
    	renderBlocks.colorRedBottomLeft *= ao_no_color[3];
    	renderBlocks.colorGreenBottomLeft *= ao_no_color[3];
    	renderBlocks.colorBlueBottomLeft *= ao_no_color[3];
    }
    
    /**
     * Resets AO color.
     */
    protected void aoResetColor(RenderBlocks renderBlocks)
    {
        /*
         * Shade AO values.
         */
    	renderBlocks.colorRedTopLeft = ao_no_color[0];
    	renderBlocks.colorGreenTopLeft = ao_no_color[0];
    	renderBlocks.colorBlueTopLeft = ao_no_color[0];
    	renderBlocks.colorRedTopRight = ao_no_color[1];
    	renderBlocks.colorGreenTopRight = ao_no_color[1];
    	renderBlocks.colorBlueTopRight = ao_no_color[1];
    	renderBlocks.colorRedBottomRight = ao_no_color[2];
    	renderBlocks.colorGreenBottomRight = ao_no_color[2];
    	renderBlocks.colorBlueBottomRight = ao_no_color[2];
    	renderBlocks.colorRedBottomLeft = ao_no_color[3];
    	renderBlocks.colorGreenBottomLeft = ao_no_color[3];
    	renderBlocks.colorBlueBottomLeft = ao_no_color[3];
    }
    
    /**
     * Sets color if necessary, and renders given side.
     * RGB passed in only used when enableAO = false.
     */
    protected void setupColorAndRender(TEPaintersFlowerPot TE, RenderBlocks renderBlocks, Block block, int side, int x, int y, int z, float lightness)
    {
		Icon icon;
		if (hasMetadataOverride)
			icon = block.getIcon(side, metadataOverride);
		else
			icon = block.getBlockTextureFromSide(side);
		
		// Override glass to use custom texture
		if (block == Block.glass)
			icon = BlockPaintersFlowerPot.specialIcon[BlockProperties.convertItemIDtoSpecialColorID(Block.glass.blockID)];
		
		// Apply icon overrides for dyed sides
		if (iconOverrideIndex[side] != Block.flowerPot.getBlockTextureFromSide(side))
			icon = iconOverrideIndex[side];
		
    	/*
    	 * A texture override indicates the breaking animation is being
    	 * drawn.  If this is the case, only draw this for current pass.
    	 */
    	if (renderBlocks.hasOverrideBlockTexture())
    	{
    		icon = renderBlocks.overrideBlockTexture;
    		renderSide(TE, renderBlocks, side, x, y, z, icon);
    	}
    	
    	else
    	{
    		if (isPot)
    		{
    			if (isPotBottom) {        		
                	colorSide(TE, renderBlocks, block, side, x, y, z, icon, lightness);
        			if (side < 2 && (block == Block.glass || block == Block.flowerPot))
        				renderOffsetTextureForPotBottom(renderBlocks, x, y, z, icon);
        			else
        	           	renderSide(TE, renderBlocks, side, x, y, z, icon);
    			} else {
                	colorSide(TE, renderBlocks, block, side, x, y, z, icon, lightness);
    	           	renderSide(TE, renderBlocks, side, x, y, z, icon);
    			}
    		}
    		
    		if (isSoil)
    		{
            	colorSide(TE, renderBlocks, block, side, x, y, z, icon, lightness);
            	renderSide(TE, renderBlocks, side, x, y, z, icon);
    			
    			if (block == Block.grass)
    			{
                	icon = BlockGrass.getIconSideOverlay();
                	colorSide(TE, renderBlocks, block, side, x, y, z, icon, lightness);
                	renderSide(TE, renderBlocks, side, x, y, z, icon);
                }
    		}
    	}
    }
    
    /**
     * Renders side.
     */
    protected void renderSide(TEPaintersFlowerPot TE, RenderBlocks renderBlocks, int side, int x, int y, int z, Icon icon)
    {
    	switch (side) {
	    	case 0:
	        	RenderHelper.renderFaceYNeg(renderBlocks, x, y, z, icon);
	        	break;
	    	case 1:
	    		RenderHelper.renderFaceYPos(renderBlocks, x, y, z, icon);
	    		break;
	    	case 2:
	    		RenderHelper.renderFaceZNeg(renderBlocks, x, y, z, icon);
	    		break;
	    	case 3:
	    		RenderHelper.renderFaceZPos(renderBlocks, x, y, z, icon);
	    		break;
	    	case 4:
	    		RenderHelper.renderFaceXNeg(renderBlocks, x, y, z, icon);
	    		break;
	    	case 5:
	    		RenderHelper.renderFaceXPos(renderBlocks, x, y, z, icon);
	    		break;
    	}
    }
    
    /**
     * Returns float array with RGB values for block.
     */
    protected float[] getRGB(Block block, IBlockAccess blockAccess, int x, int y, int z)
    {
    	float[] rgb = { 0, 0, 0 };
    	
    	int color = block.colorMultiplier(blockAccess, x, y, z);
    	
        rgb[0] = (color >> 16 & 255) / 255.0F;
        rgb[1] = (color >> 8 & 255) / 255.0F;
        rgb[2] = (color & 255) / 255.0F;
        
        if (EntityRenderer.anaglyphEnable)
        {
            rgb[0] = (rgb[0] * 30.0F + rgb[1] * 59.0F + rgb[2] * 11.0F) / 100.0F;
            rgb[1] = (rgb[0] * 30.0F + rgb[1] * 70.0F) / 100.0F;
            rgb[2] = (rgb[0] * 30.0F + rgb[2] * 70.0F) / 100.0F;
        }

        return rgb;
    }
    
    /**
     * Apply color to AO or tessellator
     */
    protected void colorSide(TEPaintersFlowerPot TE, RenderBlocks renderBlocks, Block block, int side, int x, int y, int z, Icon icon, float lightness)
    {    	
    	Tessellator tessellator = Tessellator.instance;
    	float baseRGB[] = getRGB(block, renderBlocks.blockAccess, x, y, z);
    	float colorRGB[] = { 1.0F, 1.0F, 1.0F };
    	    	
    	if (renderBlocks.enableAO) {
    		
    		aoResetColor(renderBlocks);
    		if (block == Block.grass) {
    			if (side == 1 || icon == BlockGrass.getIconSideOverlay())
    				aoSetColor(TE, renderBlocks, block, side, baseRGB[0] * colorRGB[0], baseRGB[1] * colorRGB[1], baseRGB[2] * colorRGB[2], lightness);
    			else
    				aoSetColor(TE, renderBlocks, block, side, colorRGB[0], colorRGB[1], colorRGB[2], lightness);
    		} else {
				aoSetColor(TE, renderBlocks, block, side, baseRGB[0] * colorRGB[0], baseRGB[1] * colorRGB[1], baseRGB[2] * colorRGB[2], lightness);
    		}    
    		
    	} else {
    		
    		if (block == Block.grass) {
    			if (side == 1 || icon == BlockGrass.getIconSideOverlay())
    				tessellator.setColorOpaque_F(rgb_no_color[0] * baseRGB[0] * colorRGB[0], rgb_no_color[1] * baseRGB[1] * colorRGB[1], rgb_no_color[2] * baseRGB[2] * colorRGB[2]);
    			else
    				tessellator.setColorOpaque_F(rgb_no_color[0] * colorRGB[0], rgb_no_color[1] * colorRGB[1], rgb_no_color[2] * colorRGB[2]);
    		} else {
    			tessellator.setColorOpaque_F(rgb_no_color[0] * baseRGB[0] * colorRGB[0], rgb_no_color[1] * baseRGB[1] * colorRGB[1], rgb_no_color[2] * baseRGB[2] * colorRGB[2]);
    		}
    		
    	}
    }
    
    /**
     * Renders a standard cube block at the given coordinates
     */
    public boolean renderStandardBlock(TEPaintersFlowerPot TE, RenderBlocks renderBlocks, Block block, int x, int y, int z)
    {
    	float[] rgb = getRGB(block, renderBlocks.blockAccess, x, y, z);

        if (Minecraft.isAmbientOcclusionEnabled())
        	return renderStandardBlockWithAmbientOcclusion(TE, renderBlocks, block, x, y, z, rgb[0], rgb[1], rgb[2], renderBlocks.partialRenderBounds);
        else
        	return renderStandardBlockWithColorMultiplier(TE, renderBlocks, block, x, y, z, rgb[0], rgb[1], rgb[2]);
    }
    
    public boolean renderStandardBlockWithAmbientOcclusion(TEPaintersFlowerPot TE, RenderBlocks renderBlocks, Block block, int x, int y, int z, float red, float green, float blue, boolean maxSmoothLighting)
    {
    	renderBlocks.enableAO = true;
        boolean side_rendered = false;
        int mixedBrightness = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y, z);
        Tessellator tessellator = Tessellator.instance;
        tessellator.setBrightness(983055);

        boolean canBlockGrassXYNN;
        boolean canBlockGrassXYPN;
        boolean canBlockGrassYZNN;
        boolean canBlockGrassYZNP;
        float aoLightValue;
        int offsetBrightness;
        
        /*
         * Used to determine what block to get the ambient
         * occlusion value from for side rendering.  These
         * are defaults pulled out from below.
         */
        int[] aoBlockOffset =
        {
        	renderBlocks.renderMinY <= 0.0D ? 1 : 0,
        	renderBlocks.renderMaxY >= 1.0D ? 1 : 0,
            renderBlocks.renderMinZ <= 0.0D ? 1 : 0,
            renderBlocks.renderMaxZ >= 1.0D ? 1 : 0,
            renderBlocks.renderMinX <= 0.0D ? 1 : 0,
            renderBlocks.renderMaxX >= 1.0D ? 1 : 0
        };

        /*
         * Used for Maximum Smooth Lighting calculations
         */
        float aoBottomLeftTemp;
        float aoTopLeftTemp;
        float aoTopRightTemp;
        float aoBottomRightTemp;
        int brightnessBottomLeftTemp;
        int brightnessTopLeftTemp;
        int brightnessTopRightTemp;
        int brightnessBottomRightTemp;
        
        if (renderBlocks.renderAllFaces || block.shouldSideBeRendered(renderBlocks.blockAccess, x, y - 1, z, 0))
        {
        	y -= aoBlockOffset[0];

            renderBlocks.aoBrightnessXYNN = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x - 1, y, z);
            renderBlocks.aoBrightnessYZNN = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y, z - 1);
            renderBlocks.aoBrightnessYZNP = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y, z + 1);
            renderBlocks.aoBrightnessXYPN = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x + 1, y, z);
            renderBlocks.aoLightValueScratchXYNN = block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x - 1, y, z);
            renderBlocks.aoLightValueScratchYZNN = block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x, y, z - 1);
            renderBlocks.aoLightValueScratchYZNP = block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x, y, z + 1);
            renderBlocks.aoLightValueScratchXYPN = block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x + 1, y, z);
            canBlockGrassXYPN = Block.canBlockGrass[renderBlocks.blockAccess.getBlockId(x + 1, y - 1, z)];
            canBlockGrassXYNN = Block.canBlockGrass[renderBlocks.blockAccess.getBlockId(x - 1, y - 1, z)];
            canBlockGrassYZNP = Block.canBlockGrass[renderBlocks.blockAccess.getBlockId(x, y - 1, z + 1)];
            canBlockGrassYZNN = Block.canBlockGrass[renderBlocks.blockAccess.getBlockId(x, y - 1, z - 1)];

            if (!canBlockGrassYZNN && !canBlockGrassXYNN) {
                renderBlocks.aoLightValueScratchXYZNNN = renderBlocks.aoLightValueScratchXYNN;
                renderBlocks.aoBrightnessXYZNNN = renderBlocks.aoBrightnessXYNN;
            } else {
                renderBlocks.aoLightValueScratchXYZNNN = block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x - 1, y, z - 1);
                renderBlocks.aoBrightnessXYZNNN = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x - 1, y, z - 1);
            }

            if (!canBlockGrassYZNP && !canBlockGrassXYNN) {
                renderBlocks.aoLightValueScratchXYZNNP = renderBlocks.aoLightValueScratchXYNN;
                renderBlocks.aoBrightnessXYZNNP = renderBlocks.aoBrightnessXYNN;
            } else {
                renderBlocks.aoLightValueScratchXYZNNP = block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x - 1, y, z + 1);
                renderBlocks.aoBrightnessXYZNNP = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x - 1, y, z + 1);
            }

            if (!canBlockGrassYZNN && !canBlockGrassXYPN) {
                renderBlocks.aoLightValueScratchXYZPNN = renderBlocks.aoLightValueScratchXYPN;
                renderBlocks.aoBrightnessXYZPNN = renderBlocks.aoBrightnessXYPN;
            } else {
                renderBlocks.aoLightValueScratchXYZPNN = block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x + 1, y, z - 1);
                renderBlocks.aoBrightnessXYZPNN = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x + 1, y, z - 1);
            }

            if (!canBlockGrassYZNP && !canBlockGrassXYPN) {
                renderBlocks.aoLightValueScratchXYZPNP = renderBlocks.aoLightValueScratchXYPN;
                renderBlocks.aoBrightnessXYZPNP = renderBlocks.aoBrightnessXYPN;
            } else {
                renderBlocks.aoLightValueScratchXYZPNP = block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x + 1, y, z + 1);
                renderBlocks.aoBrightnessXYZPNP = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x + 1, y, z + 1);
            }
            
        	y += aoBlockOffset[0];

            offsetBrightness = mixedBrightness;

            if (renderBlocks.renderMinY <= 0.0D || !renderBlocks.blockAccess.isBlockOpaqueCube(x, y - 1, z))
                offsetBrightness = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y - 1, z);

            aoLightValue = block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x, y - 1, z);
            
		    if (maxSmoothLighting) {
				aoTopLeftTemp = (renderBlocks.aoLightValueScratchXYZNNP + renderBlocks.aoLightValueScratchXYNN + renderBlocks.aoLightValueScratchYZNP + aoLightValue) / 4.0F;
				aoBottomLeftTemp = (renderBlocks.aoLightValueScratchYZNP + aoLightValue + renderBlocks.aoLightValueScratchXYZPNP + renderBlocks.aoLightValueScratchXYPN) / 4.0F;
				aoBottomRightTemp = (aoLightValue + renderBlocks.aoLightValueScratchYZNN + renderBlocks.aoLightValueScratchXYPN + renderBlocks.aoLightValueScratchXYZPNN) / 4.0F;
				aoTopRightTemp = (renderBlocks.aoLightValueScratchXYNN + renderBlocks.aoLightValueScratchXYZNNN + aoLightValue + renderBlocks.aoLightValueScratchYZNN) / 4.0F;
                ao_no_color[0] = (float)(aoTopLeftTemp * renderBlocks.renderMaxZ * (1.0D - renderBlocks.renderMinX) + aoBottomLeftTemp * renderBlocks.renderMaxZ * renderBlocks.renderMinX + aoBottomRightTemp * (1.0D - renderBlocks.renderMaxZ) * renderBlocks.renderMinX + aoTopRightTemp * (1.0D - renderBlocks.renderMaxZ) * (1.0D - renderBlocks.renderMinX));
                ao_no_color[1] = (float)(aoTopLeftTemp * renderBlocks.renderMaxZ * (1.0D - renderBlocks.renderMaxX) + aoBottomLeftTemp * renderBlocks.renderMaxZ * renderBlocks.renderMaxX + aoBottomRightTemp * (1.0D - renderBlocks.renderMaxZ) * renderBlocks.renderMaxX + aoTopRightTemp * (1.0D - renderBlocks.renderMaxZ) * (1.0D - renderBlocks.renderMaxX));
                ao_no_color[2] = (float)(aoTopLeftTemp * renderBlocks.renderMinZ * (1.0D - renderBlocks.renderMaxX) + aoBottomLeftTemp * renderBlocks.renderMinZ * renderBlocks.renderMaxX + aoBottomRightTemp * (1.0D - renderBlocks.renderMinZ) * renderBlocks.renderMaxX + aoTopRightTemp * (1.0D - renderBlocks.renderMinZ) * (1.0D - renderBlocks.renderMaxX));
                ao_no_color[3] = (float)(aoTopLeftTemp * renderBlocks.renderMinZ * (1.0D - renderBlocks.renderMinX) + aoBottomLeftTemp * renderBlocks.renderMinZ * renderBlocks.renderMinX + aoBottomRightTemp * (1.0D - renderBlocks.renderMinZ) * renderBlocks.renderMinX + aoTopRightTemp * (1.0D - renderBlocks.renderMinZ) * (1.0D - renderBlocks.renderMinX));
				brightnessTopLeftTemp = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessXYZNNP, renderBlocks.aoBrightnessXYNN, renderBlocks.aoBrightnessYZNP, offsetBrightness);
				brightnessTopRightTemp = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessYZNP, renderBlocks.aoBrightnessXYZPNP, renderBlocks.aoBrightnessXYPN, offsetBrightness);
				brightnessBottomRightTemp = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessYZNN, renderBlocks.aoBrightnessXYPN, renderBlocks.aoBrightnessXYZPNN, offsetBrightness);
				brightnessBottomLeftTemp = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessXYNN, renderBlocks.aoBrightnessXYZNNN, renderBlocks.aoBrightnessYZNN, offsetBrightness);
				renderBlocks.brightnessTopLeft = renderBlocks.mixAoBrightness(brightnessTopLeftTemp, brightnessBottomLeftTemp, brightnessBottomRightTemp, brightnessTopRightTemp, renderBlocks.renderMaxZ * (1.0D - renderBlocks.renderMinX), renderBlocks.renderMaxZ * renderBlocks.renderMinX, (1.0D - renderBlocks.renderMaxZ) * renderBlocks.renderMinX, (1.0D - renderBlocks.renderMaxZ) * (1.0D - renderBlocks.renderMinX));
                renderBlocks.brightnessBottomLeft = renderBlocks.mixAoBrightness(brightnessTopLeftTemp, brightnessBottomLeftTemp, brightnessBottomRightTemp, brightnessTopRightTemp, renderBlocks.renderMaxZ * (1.0D - renderBlocks.renderMaxX), renderBlocks.renderMaxZ * renderBlocks.renderMaxX, (1.0D - renderBlocks.renderMaxZ) * renderBlocks.renderMaxX, (1.0D - renderBlocks.renderMaxZ) * (1.0D - renderBlocks.renderMaxX));
                renderBlocks.brightnessBottomRight = renderBlocks.mixAoBrightness(brightnessTopLeftTemp, brightnessBottomLeftTemp, brightnessBottomRightTemp, brightnessTopRightTemp, renderBlocks.renderMinZ * (1.0D - renderBlocks.renderMaxX), renderBlocks.renderMinZ * renderBlocks.renderMaxX, (1.0D - renderBlocks.renderMinZ) * renderBlocks.renderMaxX, (1.0D - renderBlocks.renderMinZ) * (1.0D - renderBlocks.renderMaxX));
                renderBlocks.brightnessTopRight = renderBlocks.mixAoBrightness(brightnessTopLeftTemp, brightnessBottomLeftTemp, brightnessBottomRightTemp, brightnessTopRightTemp, renderBlocks.renderMinZ * (1.0D - renderBlocks.renderMinX), renderBlocks.renderMinZ * renderBlocks.renderMinX, (1.0D - renderBlocks.renderMinZ) * renderBlocks.renderMinX, (1.0D - renderBlocks.renderMinZ) * (1.0D - renderBlocks.renderMinX));
            } else {
				ao_no_color[0] = (renderBlocks.aoLightValueScratchXYZNNP + renderBlocks.aoLightValueScratchXYNN + renderBlocks.aoLightValueScratchYZNP + aoLightValue) / 4.0F;
				ao_no_color[1] = (renderBlocks.aoLightValueScratchYZNP + aoLightValue + renderBlocks.aoLightValueScratchXYZPNP + renderBlocks.aoLightValueScratchXYPN) / 4.0F;
				ao_no_color[2] = (aoLightValue + renderBlocks.aoLightValueScratchYZNN + renderBlocks.aoLightValueScratchXYPN + renderBlocks.aoLightValueScratchXYZPNN) / 4.0F;
				ao_no_color[3] = (renderBlocks.aoLightValueScratchXYNN + renderBlocks.aoLightValueScratchXYZNNN + aoLightValue + renderBlocks.aoLightValueScratchYZNN) / 4.0F;
				renderBlocks.brightnessTopLeft = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessXYZNNP, renderBlocks.aoBrightnessXYNN, renderBlocks.aoBrightnessYZNP, offsetBrightness);
				renderBlocks.brightnessTopRight = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessYZNP, renderBlocks.aoBrightnessXYZPNP, renderBlocks.aoBrightnessXYPN, offsetBrightness);
				renderBlocks.brightnessBottomRight = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessYZNN, renderBlocks.aoBrightnessXYPN, renderBlocks.aoBrightnessXYZPNN, offsetBrightness);
				renderBlocks.brightnessBottomLeft = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessXYNN, renderBlocks.aoBrightnessXYZNNN, renderBlocks.aoBrightnessYZNN, offsetBrightness);
			}
		    
		    setupColorAndRender(TE, renderBlocks, block, 0, x, y, z, 0.5F);
		    
            side_rendered = true;
        }

        if (renderBlocks.renderAllFaces || block.shouldSideBeRendered(renderBlocks.blockAccess, x, y + 1, z, 1))
        {
        	y += aoBlockOffset[1];

            renderBlocks.aoBrightnessXYNP = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x - 1, y, z);
            renderBlocks.aoBrightnessXYPP = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x + 1, y, z);
            renderBlocks.aoBrightnessYZPN = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y, z - 1);
            renderBlocks.aoBrightnessYZPP = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y, z + 1);
            renderBlocks.aoLightValueScratchXYNP = block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x - 1, y, z);
            renderBlocks.aoLightValueScratchXYPP = block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x + 1, y, z);
            renderBlocks.aoLightValueScratchYZPN = block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x, y, z - 1);
            renderBlocks.aoLightValueScratchYZPP = block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x, y, z + 1);
            canBlockGrassXYPN = Block.canBlockGrass[renderBlocks.blockAccess.getBlockId(x + 1, y + 1, z)];
            canBlockGrassXYNN = Block.canBlockGrass[renderBlocks.blockAccess.getBlockId(x - 1, y + 1, z)];
            canBlockGrassYZNP = Block.canBlockGrass[renderBlocks.blockAccess.getBlockId(x, y + 1, z + 1)];
            canBlockGrassYZNN = Block.canBlockGrass[renderBlocks.blockAccess.getBlockId(x, y + 1, z - 1)];

            if (!canBlockGrassYZNN && !canBlockGrassXYNN) {
                renderBlocks.aoLightValueScratchXYZNPN = renderBlocks.aoLightValueScratchXYNP;
                renderBlocks.aoBrightnessXYZNPN = renderBlocks.aoBrightnessXYNP;
            } else {
                renderBlocks.aoLightValueScratchXYZNPN = block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x - 1, y, z - 1);
                renderBlocks.aoBrightnessXYZNPN = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x - 1, y, z - 1);
            }

            if (!canBlockGrassYZNN && !canBlockGrassXYPN) {
                renderBlocks.aoLightValueScratchXYZPPN = renderBlocks.aoLightValueScratchXYPP;
                renderBlocks.aoBrightnessXYZPPN = renderBlocks.aoBrightnessXYPP;
            } else {
                renderBlocks.aoLightValueScratchXYZPPN = block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x + 1, y, z - 1);
                renderBlocks.aoBrightnessXYZPPN = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x + 1, y, z - 1);
            }

            if (!canBlockGrassYZNP && !canBlockGrassXYNN) {
                renderBlocks.aoLightValueScratchXYZNPP = renderBlocks.aoLightValueScratchXYNP;
                renderBlocks.aoBrightnessXYZNPP = renderBlocks.aoBrightnessXYNP;
            } else {
                renderBlocks.aoLightValueScratchXYZNPP = block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x - 1, y, z + 1);
                renderBlocks.aoBrightnessXYZNPP = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x - 1, y, z + 1);
            }

            if (!canBlockGrassYZNP && !canBlockGrassXYPN) {
                renderBlocks.aoLightValueScratchXYZPPP = renderBlocks.aoLightValueScratchXYPP;
                renderBlocks.aoBrightnessXYZPPP = renderBlocks.aoBrightnessXYPP;
            } else {
                renderBlocks.aoLightValueScratchXYZPPP = block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x + 1, y, z + 1);
                renderBlocks.aoBrightnessXYZPPP = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x + 1, y, z + 1);
            }
            
        	y -= aoBlockOffset[1];

            offsetBrightness = mixedBrightness;

            if (renderBlocks.renderMaxY >= 1.0D || !renderBlocks.blockAccess.isBlockOpaqueCube(x, y + 1, z))
                offsetBrightness = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y + 1, z);

            aoLightValue = block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x, y + 1, z);
            
            if (maxSmoothLighting) {
	            aoTopLeftTemp = (renderBlocks.aoLightValueScratchXYZNPP + renderBlocks.aoLightValueScratchXYNP + renderBlocks.aoLightValueScratchYZPP + aoLightValue) / 4.0F;
	            aoBottomLeftTemp = (renderBlocks.aoLightValueScratchYZPP + aoLightValue + renderBlocks.aoLightValueScratchXYZPPP + renderBlocks.aoLightValueScratchXYPP) / 4.0F;
	            aoBottomRightTemp = (aoLightValue + renderBlocks.aoLightValueScratchYZPN + renderBlocks.aoLightValueScratchXYPP + renderBlocks.aoLightValueScratchXYZPPN) / 4.0F;
	            aoTopRightTemp = (renderBlocks.aoLightValueScratchXYNP + renderBlocks.aoLightValueScratchXYZNPN + aoLightValue + renderBlocks.aoLightValueScratchYZPN) / 4.0F;
                ao_no_color[1] = (float)(aoTopLeftTemp * renderBlocks.renderMaxZ * (1.0D - renderBlocks.renderMinX) + aoBottomLeftTemp * renderBlocks.renderMaxZ * renderBlocks.renderMinX + aoBottomRightTemp * (1.0D - renderBlocks.renderMaxZ) * renderBlocks.renderMinX + aoTopRightTemp * (1.0D - renderBlocks.renderMaxZ) * (1.0D - renderBlocks.renderMinX));
                ao_no_color[0] = (float)(aoTopLeftTemp * renderBlocks.renderMaxZ * (1.0D - renderBlocks.renderMaxX) + aoBottomLeftTemp * renderBlocks.renderMaxZ * renderBlocks.renderMaxX + aoBottomRightTemp * (1.0D - renderBlocks.renderMaxZ) * renderBlocks.renderMaxX + aoTopRightTemp * (1.0D - renderBlocks.renderMaxZ) * (1.0D - renderBlocks.renderMaxX));
                ao_no_color[3] = (float)(aoTopLeftTemp * renderBlocks.renderMinZ * (1.0D - renderBlocks.renderMaxX) + aoBottomLeftTemp * renderBlocks.renderMinZ * renderBlocks.renderMaxX + aoBottomRightTemp * (1.0D - renderBlocks.renderMinZ) * renderBlocks.renderMaxX + aoTopRightTemp * (1.0D - renderBlocks.renderMinZ) * (1.0D - renderBlocks.renderMaxX));
                ao_no_color[2] = (float)(aoTopLeftTemp * renderBlocks.renderMinZ * (1.0D - renderBlocks.renderMinX) + aoBottomLeftTemp * renderBlocks.renderMinZ * renderBlocks.renderMinX + aoBottomRightTemp * (1.0D - renderBlocks.renderMinZ) * renderBlocks.renderMinX + aoTopRightTemp * (1.0D - renderBlocks.renderMinZ) * (1.0D - renderBlocks.renderMinX));
	            brightnessTopRightTemp = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessXYZNPP, renderBlocks.aoBrightnessXYNP, renderBlocks.aoBrightnessYZPP, offsetBrightness);
	            brightnessTopLeftTemp = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessYZPP, renderBlocks.aoBrightnessXYZPPP, renderBlocks.aoBrightnessXYPP, offsetBrightness);
	            brightnessBottomLeftTemp = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessYZPN, renderBlocks.aoBrightnessXYPP, renderBlocks.aoBrightnessXYZPPN, offsetBrightness);
	            brightnessBottomRightTemp = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessXYNP, renderBlocks.aoBrightnessXYZNPN, renderBlocks.aoBrightnessYZPN, offsetBrightness);
                renderBlocks.brightnessTopLeft = renderBlocks.mixAoBrightness(brightnessTopLeftTemp, brightnessBottomLeftTemp, brightnessBottomRightTemp, brightnessTopRightTemp, renderBlocks.renderMaxZ * (1.0D - renderBlocks.renderMinX), renderBlocks.renderMaxZ * renderBlocks.renderMinX, (1.0D - renderBlocks.renderMaxZ) * renderBlocks.renderMinX, (1.0D - renderBlocks.renderMaxZ) * (1.0D - renderBlocks.renderMinX));
                renderBlocks.brightnessBottomLeft = renderBlocks.mixAoBrightness(brightnessTopLeftTemp, brightnessBottomLeftTemp, brightnessBottomRightTemp, brightnessTopRightTemp, renderBlocks.renderMaxZ * (1.0D - renderBlocks.renderMaxX), renderBlocks.renderMaxZ * renderBlocks.renderMaxX, (1.0D - renderBlocks.renderMaxZ) * renderBlocks.renderMaxX, (1.0D - renderBlocks.renderMaxZ) * (1.0D - renderBlocks.renderMaxX));
                renderBlocks.brightnessBottomRight = renderBlocks.mixAoBrightness(brightnessTopLeftTemp, brightnessBottomLeftTemp, brightnessBottomRightTemp, brightnessTopRightTemp, renderBlocks.renderMinZ * (1.0D - renderBlocks.renderMaxX), renderBlocks.renderMinZ * renderBlocks.renderMaxX, (1.0D - renderBlocks.renderMinZ) * renderBlocks.renderMaxX, (1.0D - renderBlocks.renderMinZ) * (1.0D - renderBlocks.renderMaxX));
                renderBlocks.brightnessTopRight = renderBlocks.mixAoBrightness(brightnessTopLeftTemp, brightnessBottomLeftTemp, brightnessBottomRightTemp, brightnessTopRightTemp, renderBlocks.renderMinZ * (1.0D - renderBlocks.renderMinX), renderBlocks.renderMinZ * renderBlocks.renderMinX, (1.0D - renderBlocks.renderMinZ) * renderBlocks.renderMinX, (1.0D - renderBlocks.renderMinZ) * (1.0D - renderBlocks.renderMinX));
            } else {
	            ao_no_color[1] = (renderBlocks.aoLightValueScratchXYZNPP + renderBlocks.aoLightValueScratchXYNP + renderBlocks.aoLightValueScratchYZPP + aoLightValue) / 4.0F;
	            ao_no_color[0] = (renderBlocks.aoLightValueScratchYZPP + aoLightValue + renderBlocks.aoLightValueScratchXYZPPP + renderBlocks.aoLightValueScratchXYPP) / 4.0F;
	            ao_no_color[3] = (aoLightValue + renderBlocks.aoLightValueScratchYZPN + renderBlocks.aoLightValueScratchXYPP + renderBlocks.aoLightValueScratchXYZPPN) / 4.0F;
	            ao_no_color[2] = (renderBlocks.aoLightValueScratchXYNP + renderBlocks.aoLightValueScratchXYZNPN + aoLightValue + renderBlocks.aoLightValueScratchYZPN) / 4.0F;
	            renderBlocks.brightnessTopRight = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessXYZNPP, renderBlocks.aoBrightnessXYNP, renderBlocks.aoBrightnessYZPP, offsetBrightness);
	            renderBlocks.brightnessTopLeft = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessYZPP, renderBlocks.aoBrightnessXYZPPP, renderBlocks.aoBrightnessXYPP, offsetBrightness);
	            renderBlocks.brightnessBottomLeft = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessYZPN, renderBlocks.aoBrightnessXYPP, renderBlocks.aoBrightnessXYZPPN, offsetBrightness);
	            renderBlocks.brightnessBottomRight = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessXYNP, renderBlocks.aoBrightnessXYZNPN, renderBlocks.aoBrightnessYZPN, offsetBrightness);
            }
            
		    setupColorAndRender(TE, renderBlocks, block, 1, x, y, z, 1.0F);

            side_rendered = true;
        }

        if (renderBlocks.renderAllFaces || block.shouldSideBeRendered(renderBlocks.blockAccess, x, y, z - 1, 2))
        {
        	z -= aoBlockOffset[2];

            renderBlocks.aoLightValueScratchXZNN = block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x - 1, y, z);
            renderBlocks.aoLightValueScratchYZNN = block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x, y - 1, z);
            renderBlocks.aoLightValueScratchYZPN = block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x, y + 1, z);
            renderBlocks.aoLightValueScratchXZPN = block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x + 1, y, z);
            renderBlocks.aoBrightnessXZNN = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x - 1, y, z);
            renderBlocks.aoBrightnessYZNN = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y - 1, z);
            renderBlocks.aoBrightnessYZPN = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y + 1, z);
            renderBlocks.aoBrightnessXZPN = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x + 1, y, z);
            canBlockGrassXYPN = Block.canBlockGrass[renderBlocks.blockAccess.getBlockId(x + 1, y, z - 1)];
            canBlockGrassXYNN = Block.canBlockGrass[renderBlocks.blockAccess.getBlockId(x - 1, y, z - 1)];
            canBlockGrassYZNP = Block.canBlockGrass[renderBlocks.blockAccess.getBlockId(x, y + 1, z - 1)];
            canBlockGrassYZNN = Block.canBlockGrass[renderBlocks.blockAccess.getBlockId(x, y - 1, z - 1)];

            if (!canBlockGrassXYNN && !canBlockGrassYZNN) {
                renderBlocks.aoLightValueScratchXYZNNN = renderBlocks.aoLightValueScratchXZNN;
                renderBlocks.aoBrightnessXYZNNN = renderBlocks.aoBrightnessXZNN;
            } else {
                renderBlocks.aoLightValueScratchXYZNNN = block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x - 1, y - 1, z);
                renderBlocks.aoBrightnessXYZNNN = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x - 1, y - 1, z);
            }

            if (!canBlockGrassXYNN && !canBlockGrassYZNP) {
                renderBlocks.aoLightValueScratchXYZNPN = renderBlocks.aoLightValueScratchXZNN;
                renderBlocks.aoBrightnessXYZNPN = renderBlocks.aoBrightnessXZNN;
            } else {
                renderBlocks.aoLightValueScratchXYZNPN = block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x - 1, y + 1, z);
                renderBlocks.aoBrightnessXYZNPN = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x - 1, y + 1, z);
            }

            if (!canBlockGrassXYPN && !canBlockGrassYZNN) {
                renderBlocks.aoLightValueScratchXYZPNN = renderBlocks.aoLightValueScratchXZPN;
                renderBlocks.aoBrightnessXYZPNN = renderBlocks.aoBrightnessXZPN;
            } else {
                renderBlocks.aoLightValueScratchXYZPNN = block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x + 1, y - 1, z);
                renderBlocks.aoBrightnessXYZPNN = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x + 1, y - 1, z);
            }

            if (!canBlockGrassXYPN && !canBlockGrassYZNP) {
                renderBlocks.aoLightValueScratchXYZPPN = renderBlocks.aoLightValueScratchXZPN;
                renderBlocks.aoBrightnessXYZPPN = renderBlocks.aoBrightnessXZPN;
            } else {
                renderBlocks.aoLightValueScratchXYZPPN = block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x + 1, y + 1, z);
                renderBlocks.aoBrightnessXYZPPN = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x + 1, y + 1, z);
            }
            
        	z += aoBlockOffset[2];

            offsetBrightness = mixedBrightness;

            if (renderBlocks.renderMinZ <= 0.0D || !renderBlocks.blockAccess.isBlockOpaqueCube(x, y, z - 1))
                offsetBrightness = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y, z - 1);

            aoLightValue = block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x, y, z - 1);
            
            if (maxSmoothLighting) {
                aoTopLeftTemp = (renderBlocks.aoLightValueScratchXZNN + renderBlocks.aoLightValueScratchXYZNPN + aoLightValue + renderBlocks.aoLightValueScratchYZPN) / 4.0F;
                aoBottomLeftTemp = (aoLightValue + renderBlocks.aoLightValueScratchYZPN + renderBlocks.aoLightValueScratchXZPN + renderBlocks.aoLightValueScratchXYZPPN) / 4.0F;
                aoBottomRightTemp = (renderBlocks.aoLightValueScratchYZNN + aoLightValue + renderBlocks.aoLightValueScratchXYZPNN + renderBlocks.aoLightValueScratchXZPN) / 4.0F;
                aoTopRightTemp = (renderBlocks.aoLightValueScratchXYZNNN + renderBlocks.aoLightValueScratchXZNN + renderBlocks.aoLightValueScratchYZNN + aoLightValue) / 4.0F;
                ao_no_color[0] = (float)(aoTopLeftTemp * renderBlocks.renderMaxY * (1.0D - renderBlocks.renderMinX) + aoBottomLeftTemp * renderBlocks.renderMaxY * renderBlocks.renderMinX + aoBottomRightTemp * (1.0D - renderBlocks.renderMaxY) * renderBlocks.renderMinX + aoTopRightTemp * (1.0D - renderBlocks.renderMaxY) * (1.0D - renderBlocks.renderMinX));
                ao_no_color[3] = (float)(aoTopLeftTemp * renderBlocks.renderMaxY * (1.0D - renderBlocks.renderMaxX) + aoBottomLeftTemp * renderBlocks.renderMaxY * renderBlocks.renderMaxX + aoBottomRightTemp * (1.0D - renderBlocks.renderMaxY) * renderBlocks.renderMaxX + aoTopRightTemp * (1.0D - renderBlocks.renderMaxY) * (1.0D - renderBlocks.renderMaxX));
                ao_no_color[2] = (float)(aoTopLeftTemp * renderBlocks.renderMinY * (1.0D - renderBlocks.renderMaxX) + aoBottomLeftTemp * renderBlocks.renderMinY * renderBlocks.renderMaxX + aoBottomRightTemp * (1.0D - renderBlocks.renderMinY) * renderBlocks.renderMaxX + aoTopRightTemp * (1.0D - renderBlocks.renderMinY) * (1.0D - renderBlocks.renderMaxX));
                ao_no_color[1] = (float)(aoTopLeftTemp * renderBlocks.renderMinY * (1.0D - renderBlocks.renderMinX) + aoBottomLeftTemp * renderBlocks.renderMinY * renderBlocks.renderMinX + aoBottomRightTemp * (1.0D - renderBlocks.renderMinY) * renderBlocks.renderMinX + aoTopRightTemp * (1.0D - renderBlocks.renderMinY) * (1.0D - renderBlocks.renderMinX));
                brightnessTopLeftTemp = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessXZNN, renderBlocks.aoBrightnessXYZNPN, renderBlocks.aoBrightnessYZPN, offsetBrightness);
                brightnessBottomLeftTemp = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessYZPN, renderBlocks.aoBrightnessXZPN, renderBlocks.aoBrightnessXYZPPN, offsetBrightness);
                brightnessBottomRightTemp = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessYZNN, renderBlocks.aoBrightnessXYZPNN, renderBlocks.aoBrightnessXZPN, offsetBrightness);
                brightnessTopRightTemp = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessXYZNNN, renderBlocks.aoBrightnessXZNN, renderBlocks.aoBrightnessYZNN, offsetBrightness);
                renderBlocks.brightnessTopLeft = renderBlocks.mixAoBrightness(brightnessTopLeftTemp, brightnessBottomLeftTemp, brightnessBottomRightTemp, brightnessTopRightTemp, renderBlocks.renderMaxY * (1.0D - renderBlocks.renderMinX), renderBlocks.renderMaxY * renderBlocks.renderMinX, (1.0D - renderBlocks.renderMaxY) * renderBlocks.renderMinX, (1.0D - renderBlocks.renderMaxY) * (1.0D - renderBlocks.renderMinX));
                renderBlocks.brightnessBottomLeft = renderBlocks.mixAoBrightness(brightnessTopLeftTemp, brightnessBottomLeftTemp, brightnessBottomRightTemp, brightnessTopRightTemp, renderBlocks.renderMaxY * (1.0D - renderBlocks.renderMaxX), renderBlocks.renderMaxY * renderBlocks.renderMaxX, (1.0D - renderBlocks.renderMaxY) * renderBlocks.renderMaxX, (1.0D - renderBlocks.renderMaxY) * (1.0D - renderBlocks.renderMaxX));
                renderBlocks.brightnessBottomRight = renderBlocks.mixAoBrightness(brightnessTopLeftTemp, brightnessBottomLeftTemp, brightnessBottomRightTemp, brightnessTopRightTemp, renderBlocks.renderMinY * (1.0D - renderBlocks.renderMaxX), renderBlocks.renderMinY * renderBlocks.renderMaxX, (1.0D - renderBlocks.renderMinY) * renderBlocks.renderMaxX, (1.0D - renderBlocks.renderMinY) * (1.0D - renderBlocks.renderMaxX));
                renderBlocks.brightnessTopRight = renderBlocks.mixAoBrightness(brightnessTopLeftTemp, brightnessBottomLeftTemp, brightnessBottomRightTemp, brightnessTopRightTemp, renderBlocks.renderMinY * (1.0D - renderBlocks.renderMinX), renderBlocks.renderMinY * renderBlocks.renderMinX, (1.0D - renderBlocks.renderMinY) * renderBlocks.renderMinX, (1.0D - renderBlocks.renderMinY) * (1.0D - renderBlocks.renderMinX));
            } else {
	            ao_no_color[0] = (renderBlocks.aoLightValueScratchXZNN + renderBlocks.aoLightValueScratchXYZNPN + aoLightValue + renderBlocks.aoLightValueScratchYZPN) / 4.0F;
	            ao_no_color[3] = (aoLightValue + renderBlocks.aoLightValueScratchYZPN + renderBlocks.aoLightValueScratchXZPN + renderBlocks.aoLightValueScratchXYZPPN) / 4.0F;
	            ao_no_color[2] = (renderBlocks.aoLightValueScratchYZNN + aoLightValue + renderBlocks.aoLightValueScratchXYZPNN + renderBlocks.aoLightValueScratchXZPN) / 4.0F;
	            ao_no_color[1] = (renderBlocks.aoLightValueScratchXYZNNN + renderBlocks.aoLightValueScratchXZNN + renderBlocks.aoLightValueScratchYZNN + aoLightValue) / 4.0F;
	            renderBlocks.brightnessTopLeft = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessXZNN, renderBlocks.aoBrightnessXYZNPN, renderBlocks.aoBrightnessYZPN, offsetBrightness);
	            renderBlocks.brightnessBottomLeft = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessYZPN, renderBlocks.aoBrightnessXZPN, renderBlocks.aoBrightnessXYZPPN, offsetBrightness);
	            renderBlocks.brightnessBottomRight = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessYZNN, renderBlocks.aoBrightnessXYZPNN, renderBlocks.aoBrightnessXZPN, offsetBrightness);
	            renderBlocks.brightnessTopRight = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessXYZNNN, renderBlocks.aoBrightnessXZNN, renderBlocks.aoBrightnessYZNN, offsetBrightness);
	        }
            
		    setupColorAndRender(TE, renderBlocks, block, 2, x, y, z, 0.8F);
            
            side_rendered = true;
        }

        if (renderBlocks.renderAllFaces || block.shouldSideBeRendered(renderBlocks.blockAccess, x, y, z + 1, 3))
        {
        	z += aoBlockOffset[3];

            renderBlocks.aoLightValueScratchXZNP = block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x - 1, y, z);
            renderBlocks.aoLightValueScratchXZPP = block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x + 1, y, z);
            renderBlocks.aoLightValueScratchYZNP = block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x, y - 1, z);
            renderBlocks.aoLightValueScratchYZPP = block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x, y + 1, z);
            renderBlocks.aoBrightnessXZNP = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x - 1, y, z);
            renderBlocks.aoBrightnessXZPP = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x + 1, y, z);
            renderBlocks.aoBrightnessYZNP = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y - 1, z);
            renderBlocks.aoBrightnessYZPP = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y + 1, z);
            canBlockGrassXYPN = Block.canBlockGrass[renderBlocks.blockAccess.getBlockId(x + 1, y, z + 1)];
            canBlockGrassXYNN = Block.canBlockGrass[renderBlocks.blockAccess.getBlockId(x - 1, y, z + 1)];
            canBlockGrassYZNP = Block.canBlockGrass[renderBlocks.blockAccess.getBlockId(x, y + 1, z + 1)];
            canBlockGrassYZNN = Block.canBlockGrass[renderBlocks.blockAccess.getBlockId(x, y - 1, z + 1)];

            if (!canBlockGrassXYNN && !canBlockGrassYZNN) {
                renderBlocks.aoLightValueScratchXYZNNP = renderBlocks.aoLightValueScratchXZNP;
                renderBlocks.aoBrightnessXYZNNP = renderBlocks.aoBrightnessXZNP;
            } else {
                renderBlocks.aoLightValueScratchXYZNNP = block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x - 1, y - 1, z);
                renderBlocks.aoBrightnessXYZNNP = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x - 1, y - 1, z);
            }

            if (!canBlockGrassXYNN && !canBlockGrassYZNP) {
                renderBlocks.aoLightValueScratchXYZNPP = renderBlocks.aoLightValueScratchXZNP;
                renderBlocks.aoBrightnessXYZNPP = renderBlocks.aoBrightnessXZNP;
            } else {
                renderBlocks.aoLightValueScratchXYZNPP = block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x - 1, y + 1, z);
                renderBlocks.aoBrightnessXYZNPP = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x - 1, y + 1, z);
            }

            if (!canBlockGrassXYPN && !canBlockGrassYZNN) {
                renderBlocks.aoLightValueScratchXYZPNP = renderBlocks.aoLightValueScratchXZPP;
                renderBlocks.aoBrightnessXYZPNP = renderBlocks.aoBrightnessXZPP;
            } else {
                renderBlocks.aoLightValueScratchXYZPNP = block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x + 1, y - 1, z);
                renderBlocks.aoBrightnessXYZPNP = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x + 1, y - 1, z);
            }

            if (!canBlockGrassXYPN && !canBlockGrassYZNP) {
                renderBlocks.aoLightValueScratchXYZPPP = renderBlocks.aoLightValueScratchXZPP;
                renderBlocks.aoBrightnessXYZPPP = renderBlocks.aoBrightnessXZPP;
            } else {
                renderBlocks.aoLightValueScratchXYZPPP = block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x + 1, y + 1, z);
                renderBlocks.aoBrightnessXYZPPP = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x + 1, y + 1, z);
            }

        	z -= aoBlockOffset[3];

            offsetBrightness = mixedBrightness;

            if (renderBlocks.renderMaxZ >= 1.0D || !renderBlocks.blockAccess.isBlockOpaqueCube(x, y, z + 1))
                offsetBrightness = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y, z + 1);

            aoLightValue = block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x, y, z + 1);
            
            if (maxSmoothLighting) {
                aoTopLeftTemp = (renderBlocks.aoLightValueScratchXZNP + renderBlocks.aoLightValueScratchXYZNPP + aoLightValue + renderBlocks.aoLightValueScratchYZPP) / 4.0F;
                aoBottomLeftTemp = (aoLightValue + renderBlocks.aoLightValueScratchYZPP + renderBlocks.aoLightValueScratchXZPP + renderBlocks.aoLightValueScratchXYZPPP) / 4.0F;
                aoBottomRightTemp = (renderBlocks.aoLightValueScratchYZNP + aoLightValue + renderBlocks.aoLightValueScratchXYZPNP + renderBlocks.aoLightValueScratchXZPP) / 4.0F;
                aoTopRightTemp = (renderBlocks.aoLightValueScratchXYZNNP + renderBlocks.aoLightValueScratchXZNP + renderBlocks.aoLightValueScratchYZNP + aoLightValue) / 4.0F;
                ao_no_color[0] = (float)(aoTopLeftTemp * renderBlocks.renderMaxY * (1.0D - renderBlocks.renderMinX) + aoBottomLeftTemp * renderBlocks.renderMaxY * renderBlocks.renderMinX + aoBottomRightTemp * (1.0D - renderBlocks.renderMaxY) * renderBlocks.renderMinX + aoTopRightTemp * (1.0D - renderBlocks.renderMaxY) * (1.0D - renderBlocks.renderMinX));
                ao_no_color[3] = (float)(aoTopLeftTemp * renderBlocks.renderMinY * (1.0D - renderBlocks.renderMinX) + aoBottomLeftTemp * renderBlocks.renderMinY * renderBlocks.renderMinX + aoBottomRightTemp * (1.0D - renderBlocks.renderMinY) * renderBlocks.renderMinX + aoTopRightTemp * (1.0D - renderBlocks.renderMinY) * (1.0D - renderBlocks.renderMinX));
                ao_no_color[2] = (float)(aoTopLeftTemp * renderBlocks.renderMinY * (1.0D - renderBlocks.renderMaxX) + aoBottomLeftTemp * renderBlocks.renderMinY * renderBlocks.renderMaxX + aoBottomRightTemp * (1.0D - renderBlocks.renderMinY) * renderBlocks.renderMaxX + aoTopRightTemp * (1.0D - renderBlocks.renderMinY) * (1.0D - renderBlocks.renderMaxX));
                ao_no_color[1] = (float)(aoTopLeftTemp * renderBlocks.renderMaxY * (1.0D - renderBlocks.renderMaxX) + aoBottomLeftTemp * renderBlocks.renderMaxY * renderBlocks.renderMaxX + aoBottomRightTemp * (1.0D - renderBlocks.renderMaxY) * renderBlocks.renderMaxX + aoTopRightTemp * (1.0D - renderBlocks.renderMaxY) * (1.0D - renderBlocks.renderMaxX));
                brightnessTopLeftTemp = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessXZNP, renderBlocks.aoBrightnessXYZNPP, renderBlocks.aoBrightnessYZPP, offsetBrightness);
                brightnessBottomLeftTemp = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessYZPP, renderBlocks.aoBrightnessXZPP, renderBlocks.aoBrightnessXYZPPP, offsetBrightness);
                brightnessBottomRightTemp = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessYZNP, renderBlocks.aoBrightnessXYZPNP, renderBlocks.aoBrightnessXZPP, offsetBrightness);
                brightnessTopRightTemp = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessXYZNNP, renderBlocks.aoBrightnessXZNP, renderBlocks.aoBrightnessYZNP, offsetBrightness);
                renderBlocks.brightnessTopLeft = renderBlocks.mixAoBrightness(brightnessTopLeftTemp, brightnessTopRightTemp, brightnessBottomRightTemp, brightnessBottomLeftTemp, renderBlocks.renderMaxY * (1.0D - renderBlocks.renderMinX), (1.0D - renderBlocks.renderMaxY) * (1.0D - renderBlocks.renderMinX), (1.0D - renderBlocks.renderMaxY) * renderBlocks.renderMinX, renderBlocks.renderMaxY * renderBlocks.renderMinX);
                renderBlocks.brightnessBottomLeft = renderBlocks.mixAoBrightness(brightnessTopLeftTemp, brightnessTopRightTemp, brightnessBottomRightTemp, brightnessBottomLeftTemp, renderBlocks.renderMinY * (1.0D - renderBlocks.renderMinX), (1.0D - renderBlocks.renderMinY) * (1.0D - renderBlocks.renderMinX), (1.0D - renderBlocks.renderMinY) * renderBlocks.renderMinX, renderBlocks.renderMinY * renderBlocks.renderMinX);
                renderBlocks.brightnessBottomRight = renderBlocks.mixAoBrightness(brightnessTopLeftTemp, brightnessTopRightTemp, brightnessBottomRightTemp, brightnessBottomLeftTemp, renderBlocks.renderMinY * (1.0D - renderBlocks.renderMaxX), (1.0D - renderBlocks.renderMinY) * (1.0D - renderBlocks.renderMaxX), (1.0D - renderBlocks.renderMinY) * renderBlocks.renderMaxX, renderBlocks.renderMinY * renderBlocks.renderMaxX);
                renderBlocks.brightnessTopRight = renderBlocks.mixAoBrightness(brightnessTopLeftTemp, brightnessTopRightTemp, brightnessBottomRightTemp, brightnessBottomLeftTemp, renderBlocks.renderMaxY * (1.0D - renderBlocks.renderMaxX), (1.0D - renderBlocks.renderMaxY) * (1.0D - renderBlocks.renderMaxX), (1.0D - renderBlocks.renderMaxY) * renderBlocks.renderMaxX, renderBlocks.renderMaxY * renderBlocks.renderMaxX);
            } else {
	            ao_no_color[0] = (renderBlocks.aoLightValueScratchXZNP + renderBlocks.aoLightValueScratchXYZNPP + aoLightValue + renderBlocks.aoLightValueScratchYZPP) / 4.0F;
	            ao_no_color[1] = (aoLightValue + renderBlocks.aoLightValueScratchYZPP + renderBlocks.aoLightValueScratchXZPP + renderBlocks.aoLightValueScratchXYZPPP) / 4.0F;
	            ao_no_color[2] = (renderBlocks.aoLightValueScratchYZNP + aoLightValue + renderBlocks.aoLightValueScratchXYZPNP + renderBlocks.aoLightValueScratchXZPP) / 4.0F;
	            ao_no_color[3] = (renderBlocks.aoLightValueScratchXYZNNP + renderBlocks.aoLightValueScratchXZNP + renderBlocks.aoLightValueScratchYZNP + aoLightValue) / 4.0F;
	            renderBlocks.brightnessTopLeft = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessXZNP, renderBlocks.aoBrightnessXYZNPP, renderBlocks.aoBrightnessYZPP, offsetBrightness);
	            renderBlocks.brightnessTopRight = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessYZPP, renderBlocks.aoBrightnessXZPP, renderBlocks.aoBrightnessXYZPPP, offsetBrightness);
	            renderBlocks.brightnessBottomRight = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessYZNP, renderBlocks.aoBrightnessXYZPNP, renderBlocks.aoBrightnessXZPP, offsetBrightness);
	            renderBlocks.brightnessBottomLeft = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessXYZNNP, renderBlocks.aoBrightnessXZNP, renderBlocks.aoBrightnessYZNP, offsetBrightness);
            }
            
		    setupColorAndRender(TE, renderBlocks, block, 3, x, y, z, 0.8F);
            
            side_rendered = true;
        }

        if (renderBlocks.renderAllFaces || block.shouldSideBeRendered(renderBlocks.blockAccess, x - 1, y, z, 4))
        {
        	x -= aoBlockOffset[4];

            renderBlocks.aoLightValueScratchXYNN = block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x, y - 1, z);
            renderBlocks.aoLightValueScratchXZNN = block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x, y, z - 1);
            renderBlocks.aoLightValueScratchXZNP = block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x, y, z + 1);
            renderBlocks.aoLightValueScratchXYNP = block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x, y + 1, z);
            renderBlocks.aoBrightnessXYNN = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y - 1, z);
            renderBlocks.aoBrightnessXZNN = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y, z - 1);
            renderBlocks.aoBrightnessXZNP = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y, z + 1);
            renderBlocks.aoBrightnessXYNP = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y + 1, z);
            canBlockGrassXYPN = Block.canBlockGrass[renderBlocks.blockAccess.getBlockId(x - 1, y + 1, z)];
            canBlockGrassXYNN = Block.canBlockGrass[renderBlocks.blockAccess.getBlockId(x - 1, y - 1, z)];
            canBlockGrassYZNP = Block.canBlockGrass[renderBlocks.blockAccess.getBlockId(x - 1, y, z - 1)];
            canBlockGrassYZNN = Block.canBlockGrass[renderBlocks.blockAccess.getBlockId(x - 1, y, z + 1)];

            if (!canBlockGrassYZNP && !canBlockGrassXYNN) {
                renderBlocks.aoLightValueScratchXYZNNN = renderBlocks.aoLightValueScratchXZNN;
                renderBlocks.aoBrightnessXYZNNN = renderBlocks.aoBrightnessXZNN;
            } else {
                renderBlocks.aoLightValueScratchXYZNNN = block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x, y - 1, z - 1);
                renderBlocks.aoBrightnessXYZNNN = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y - 1, z - 1);
            }

            if (!canBlockGrassYZNN && !canBlockGrassXYNN) {
                renderBlocks.aoLightValueScratchXYZNNP = renderBlocks.aoLightValueScratchXZNP;
                renderBlocks.aoBrightnessXYZNNP = renderBlocks.aoBrightnessXZNP;
            } else {
                renderBlocks.aoLightValueScratchXYZNNP = block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x, y - 1, z + 1);
                renderBlocks.aoBrightnessXYZNNP = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y - 1, z + 1);
            }

            if (!canBlockGrassYZNP && !canBlockGrassXYPN) {
                renderBlocks.aoLightValueScratchXYZNPN = renderBlocks.aoLightValueScratchXZNN;
                renderBlocks.aoBrightnessXYZNPN = renderBlocks.aoBrightnessXZNN;
            } else {
                renderBlocks.aoLightValueScratchXYZNPN = block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x, y + 1, z - 1);
                renderBlocks.aoBrightnessXYZNPN = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y + 1, z - 1);
            }

            if (!canBlockGrassYZNN && !canBlockGrassXYPN) {
                renderBlocks.aoLightValueScratchXYZNPP = renderBlocks.aoLightValueScratchXZNP;
                renderBlocks.aoBrightnessXYZNPP = renderBlocks.aoBrightnessXZNP;
            } else {
                renderBlocks.aoLightValueScratchXYZNPP = block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x, y + 1, z + 1);
                renderBlocks.aoBrightnessXYZNPP = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y + 1, z + 1);
            }

        	x += aoBlockOffset[4];

            offsetBrightness = mixedBrightness;

            if (renderBlocks.renderMinX <= 0.0D || !renderBlocks.blockAccess.isBlockOpaqueCube(x - 1, y, z))
                offsetBrightness = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x - 1, y, z);

            aoLightValue = block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x - 1, y, z);
            
            if (maxSmoothLighting) {
                aoTopLeftTemp = (renderBlocks.aoLightValueScratchXYNN + renderBlocks.aoLightValueScratchXYZNNP + aoLightValue + renderBlocks.aoLightValueScratchXZNP) / 4.0F;
                aoBottomLeftTemp = (aoLightValue + renderBlocks.aoLightValueScratchXZNP + renderBlocks.aoLightValueScratchXYNP + renderBlocks.aoLightValueScratchXYZNPP) / 4.0F;
                aoBottomRightTemp = (renderBlocks.aoLightValueScratchXZNN + aoLightValue + renderBlocks.aoLightValueScratchXYZNPN + renderBlocks.aoLightValueScratchXYNP) / 4.0F;
                aoTopRightTemp = (renderBlocks.aoLightValueScratchXYZNNN + renderBlocks.aoLightValueScratchXYNN + renderBlocks.aoLightValueScratchXZNN + aoLightValue) / 4.0F;
                ao_no_color[0] = (float)(aoBottomLeftTemp * renderBlocks.renderMaxY * renderBlocks.renderMaxZ + aoBottomRightTemp * renderBlocks.renderMaxY * (1.0D - renderBlocks.renderMaxZ) + aoTopRightTemp * (1.0D - renderBlocks.renderMaxY) * (1.0D - renderBlocks.renderMaxZ) + aoTopLeftTemp * (1.0D - renderBlocks.renderMaxY) * renderBlocks.renderMaxZ);
                ao_no_color[3] = (float)(aoBottomLeftTemp * renderBlocks.renderMaxY * renderBlocks.renderMinZ + aoBottomRightTemp * renderBlocks.renderMaxY * (1.0D - renderBlocks.renderMinZ) + aoTopRightTemp * (1.0D - renderBlocks.renderMaxY) * (1.0D - renderBlocks.renderMinZ) + aoTopLeftTemp * (1.0D - renderBlocks.renderMaxY) * renderBlocks.renderMinZ);
                ao_no_color[2] = (float)(aoBottomLeftTemp * renderBlocks.renderMinY * renderBlocks.renderMinZ + aoBottomRightTemp * renderBlocks.renderMinY * (1.0D - renderBlocks.renderMinZ) + aoTopRightTemp * (1.0D - renderBlocks.renderMinY) * (1.0D - renderBlocks.renderMinZ) + aoTopLeftTemp * (1.0D - renderBlocks.renderMinY) * renderBlocks.renderMinZ);
                ao_no_color[1] = (float)(aoBottomLeftTemp * renderBlocks.renderMinY * renderBlocks.renderMaxZ + aoBottomRightTemp * renderBlocks.renderMinY * (1.0D - renderBlocks.renderMaxZ) + aoTopRightTemp * (1.0D - renderBlocks.renderMinY) * (1.0D - renderBlocks.renderMaxZ) + aoTopLeftTemp * (1.0D - renderBlocks.renderMinY) * renderBlocks.renderMaxZ);
                brightnessTopLeftTemp = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessXYNN, renderBlocks.aoBrightnessXYZNNP, renderBlocks.aoBrightnessXZNP, offsetBrightness);
                brightnessBottomLeftTemp = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessXZNP, renderBlocks.aoBrightnessXYNP, renderBlocks.aoBrightnessXYZNPP, offsetBrightness);
                brightnessBottomRightTemp = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessXZNN, renderBlocks.aoBrightnessXYZNPN, renderBlocks.aoBrightnessXYNP, offsetBrightness);
                brightnessTopRightTemp = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessXYZNNN, renderBlocks.aoBrightnessXYNN, renderBlocks.aoBrightnessXZNN, offsetBrightness);
                renderBlocks.brightnessTopLeft = renderBlocks.mixAoBrightness(brightnessBottomLeftTemp, brightnessBottomRightTemp, brightnessTopRightTemp, brightnessTopLeftTemp, renderBlocks.renderMaxY * renderBlocks.renderMaxZ, renderBlocks.renderMaxY * (1.0D - renderBlocks.renderMaxZ), (1.0D - renderBlocks.renderMaxY) * (1.0D - renderBlocks.renderMaxZ), (1.0D - renderBlocks.renderMaxY) * renderBlocks.renderMaxZ);
                renderBlocks.brightnessBottomLeft = renderBlocks.mixAoBrightness(brightnessBottomLeftTemp, brightnessBottomRightTemp, brightnessTopRightTemp, brightnessTopLeftTemp, renderBlocks.renderMaxY * renderBlocks.renderMinZ, renderBlocks.renderMaxY * (1.0D - renderBlocks.renderMinZ), (1.0D - renderBlocks.renderMaxY) * (1.0D - renderBlocks.renderMinZ), (1.0D - renderBlocks.renderMaxY) * renderBlocks.renderMinZ);
                renderBlocks.brightnessBottomRight = renderBlocks.mixAoBrightness(brightnessBottomLeftTemp, brightnessBottomRightTemp, brightnessTopRightTemp, brightnessTopLeftTemp, renderBlocks.renderMinY * renderBlocks.renderMinZ, renderBlocks.renderMinY * (1.0D - renderBlocks.renderMinZ), (1.0D - renderBlocks.renderMinY) * (1.0D - renderBlocks.renderMinZ), (1.0D - renderBlocks.renderMinY) * renderBlocks.renderMinZ);
                renderBlocks.brightnessTopRight = renderBlocks.mixAoBrightness(brightnessBottomLeftTemp, brightnessBottomRightTemp, brightnessTopRightTemp, brightnessTopLeftTemp, renderBlocks.renderMinY * renderBlocks.renderMaxZ, renderBlocks.renderMinY * (1.0D - renderBlocks.renderMaxZ), (1.0D - renderBlocks.renderMinY) * (1.0D - renderBlocks.renderMaxZ), (1.0D - renderBlocks.renderMinY) * renderBlocks.renderMaxZ);
            } else {
	            ao_no_color[1] = (renderBlocks.aoLightValueScratchXYNN + renderBlocks.aoLightValueScratchXYZNNP + aoLightValue + renderBlocks.aoLightValueScratchXZNP) / 4.0F;
	            ao_no_color[0] = (aoLightValue + renderBlocks.aoLightValueScratchXZNP + renderBlocks.aoLightValueScratchXYNP + renderBlocks.aoLightValueScratchXYZNPP) / 4.0F;
	            ao_no_color[3] = (renderBlocks.aoLightValueScratchXZNN + aoLightValue + renderBlocks.aoLightValueScratchXYZNPN + renderBlocks.aoLightValueScratchXYNP) / 4.0F;
	            ao_no_color[2] = (renderBlocks.aoLightValueScratchXYZNNN + renderBlocks.aoLightValueScratchXYNN + renderBlocks.aoLightValueScratchXZNN + aoLightValue) / 4.0F;
	            renderBlocks.brightnessTopRight = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessXYNN, renderBlocks.aoBrightnessXYZNNP, renderBlocks.aoBrightnessXZNP, offsetBrightness);
	            renderBlocks.brightnessTopLeft = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessXZNP, renderBlocks.aoBrightnessXYNP, renderBlocks.aoBrightnessXYZNPP, offsetBrightness);
	            renderBlocks.brightnessBottomLeft = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessXZNN, renderBlocks.aoBrightnessXYZNPN, renderBlocks.aoBrightnessXYNP, offsetBrightness);
	            renderBlocks.brightnessBottomRight = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessXYZNNN, renderBlocks.aoBrightnessXYNN, renderBlocks.aoBrightnessXZNN, offsetBrightness);
            }
            
		    setupColorAndRender(TE, renderBlocks, block, 4, x, y, z, 0.6F);

            side_rendered = true;
        }

        if (renderBlocks.renderAllFaces || block.shouldSideBeRendered(renderBlocks.blockAccess, x + 1, y, z, 5))
        {
        	x += aoBlockOffset[5];

            renderBlocks.aoLightValueScratchXYPN = block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x, y - 1, z);
            renderBlocks.aoLightValueScratchXZPN = block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x, y, z - 1);
            renderBlocks.aoLightValueScratchXZPP = block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x, y, z + 1);
            renderBlocks.aoLightValueScratchXYPP = block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x, y + 1, z);
            renderBlocks.aoBrightnessXYPN = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y - 1, z);
            renderBlocks.aoBrightnessXZPN = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y, z - 1);
            renderBlocks.aoBrightnessXZPP = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y, z + 1);
            renderBlocks.aoBrightnessXYPP = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y + 1, z);
            canBlockGrassXYPN = Block.canBlockGrass[renderBlocks.blockAccess.getBlockId(x + 1, y + 1, z)];
            canBlockGrassXYNN = Block.canBlockGrass[renderBlocks.blockAccess.getBlockId(x + 1, y - 1, z)];
            canBlockGrassYZNP = Block.canBlockGrass[renderBlocks.blockAccess.getBlockId(x + 1, y, z + 1)];
            canBlockGrassYZNN = Block.canBlockGrass[renderBlocks.blockAccess.getBlockId(x + 1, y, z - 1)];

            if (!canBlockGrassXYNN && !canBlockGrassYZNN) {
                renderBlocks.aoLightValueScratchXYZPNN = renderBlocks.aoLightValueScratchXZPN;
                renderBlocks.aoBrightnessXYZPNN = renderBlocks.aoBrightnessXZPN;
            } else {
                renderBlocks.aoLightValueScratchXYZPNN = block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x, y - 1, z - 1);
                renderBlocks.aoBrightnessXYZPNN = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y - 1, z - 1);
            }

            if (!canBlockGrassXYNN && !canBlockGrassYZNP) {
                renderBlocks.aoLightValueScratchXYZPNP = renderBlocks.aoLightValueScratchXZPP;
                renderBlocks.aoBrightnessXYZPNP = renderBlocks.aoBrightnessXZPP;
            } else {
                renderBlocks.aoLightValueScratchXYZPNP = block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x, y - 1, z + 1);
                renderBlocks.aoBrightnessXYZPNP = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y - 1, z + 1);
            }

            if (!canBlockGrassXYPN && !canBlockGrassYZNN) {
                renderBlocks.aoLightValueScratchXYZPPN = renderBlocks.aoLightValueScratchXZPN;
                renderBlocks.aoBrightnessXYZPPN = renderBlocks.aoBrightnessXZPN;
            } else {
                renderBlocks.aoLightValueScratchXYZPPN = block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x, y + 1, z - 1);
                renderBlocks.aoBrightnessXYZPPN = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y + 1, z - 1);
            }

            if (!canBlockGrassXYPN && !canBlockGrassYZNP) {
                renderBlocks.aoLightValueScratchXYZPPP = renderBlocks.aoLightValueScratchXZPP;
                renderBlocks.aoBrightnessXYZPPP = renderBlocks.aoBrightnessXZPP;
            } else {
                renderBlocks.aoLightValueScratchXYZPPP = block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x, y + 1, z + 1);
                renderBlocks.aoBrightnessXYZPPP = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y + 1, z + 1);
            }

        	x -= aoBlockOffset[5];

            offsetBrightness = mixedBrightness;

            if (renderBlocks.renderMaxX >= 1.0D || !renderBlocks.blockAccess.isBlockOpaqueCube(x + 1, y, z))
                offsetBrightness = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x + 1, y, z);

            aoLightValue = block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x + 1, y, z);
            
            if (maxSmoothLighting) {
                aoTopLeftTemp = (renderBlocks.aoLightValueScratchXYPN + renderBlocks.aoLightValueScratchXYZPNP + aoLightValue + renderBlocks.aoLightValueScratchXZPP) / 4.0F;
                aoBottomLeftTemp = (renderBlocks.aoLightValueScratchXYZPNN + renderBlocks.aoLightValueScratchXYPN + renderBlocks.aoLightValueScratchXZPN + aoLightValue) / 4.0F;
                aoBottomRightTemp = (renderBlocks.aoLightValueScratchXZPN + aoLightValue + renderBlocks.aoLightValueScratchXYZPPN + renderBlocks.aoLightValueScratchXYPP) / 4.0F;
                aoTopRightTemp = (aoLightValue + renderBlocks.aoLightValueScratchXZPP + renderBlocks.aoLightValueScratchXYPP + renderBlocks.aoLightValueScratchXYZPPP) / 4.0F;
                ao_no_color[0] = (float)(aoTopLeftTemp * (1.0D - renderBlocks.renderMinY) * renderBlocks.renderMaxZ + aoBottomLeftTemp * (1.0D - renderBlocks.renderMinY) * (1.0D - renderBlocks.renderMaxZ) + aoBottomRightTemp * renderBlocks.renderMinY * (1.0D - renderBlocks.renderMaxZ) + aoTopRightTemp * renderBlocks.renderMinY * renderBlocks.renderMaxZ);
                ao_no_color[3] = (float)(aoTopLeftTemp * (1.0D - renderBlocks.renderMinY) * renderBlocks.renderMinZ + aoBottomLeftTemp * (1.0D - renderBlocks.renderMinY) * (1.0D - renderBlocks.renderMinZ) + aoBottomRightTemp * renderBlocks.renderMinY * (1.0D - renderBlocks.renderMinZ) + aoTopRightTemp * renderBlocks.renderMinY * renderBlocks.renderMinZ);
                ao_no_color[2] = (float)(aoTopLeftTemp * (1.0D - renderBlocks.renderMaxY) * renderBlocks.renderMinZ + aoBottomLeftTemp * (1.0D - renderBlocks.renderMaxY) * (1.0D - renderBlocks.renderMinZ) + aoBottomRightTemp * renderBlocks.renderMaxY * (1.0D - renderBlocks.renderMinZ) + aoTopRightTemp * renderBlocks.renderMaxY * renderBlocks.renderMinZ);
                ao_no_color[1] = (float)(aoTopLeftTemp * (1.0D - renderBlocks.renderMaxY) * renderBlocks.renderMaxZ + aoBottomLeftTemp * (1.0D - renderBlocks.renderMaxY) * (1.0D - renderBlocks.renderMaxZ) + aoBottomRightTemp * renderBlocks.renderMaxY * (1.0D - renderBlocks.renderMaxZ) + aoTopRightTemp * renderBlocks.renderMaxY * renderBlocks.renderMaxZ);
                brightnessTopLeftTemp = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessXYPN, renderBlocks.aoBrightnessXYZPNP, renderBlocks.aoBrightnessXZPP, offsetBrightness);
                brightnessBottomLeftTemp = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessXZPP, renderBlocks.aoBrightnessXYPP, renderBlocks.aoBrightnessXYZPPP, offsetBrightness);
                brightnessBottomRightTemp = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessXZPN, renderBlocks.aoBrightnessXYZPPN, renderBlocks.aoBrightnessXYPP, offsetBrightness);
                brightnessTopRightTemp = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessXYZPNN, renderBlocks.aoBrightnessXYPN, renderBlocks.aoBrightnessXZPN, offsetBrightness);
                renderBlocks.brightnessTopLeft = renderBlocks.mixAoBrightness(brightnessTopLeftTemp, brightnessTopRightTemp, brightnessBottomRightTemp, brightnessBottomLeftTemp, (1.0D - renderBlocks.renderMinY) * renderBlocks.renderMaxZ, (1.0D - renderBlocks.renderMinY) * (1.0D - renderBlocks.renderMaxZ), renderBlocks.renderMinY * (1.0D - renderBlocks.renderMaxZ), renderBlocks.renderMinY * renderBlocks.renderMaxZ);
                renderBlocks.brightnessBottomLeft = renderBlocks.mixAoBrightness(brightnessTopLeftTemp, brightnessTopRightTemp, brightnessBottomRightTemp, brightnessBottomLeftTemp, (1.0D - renderBlocks.renderMinY) * renderBlocks.renderMinZ, (1.0D - renderBlocks.renderMinY) * (1.0D - renderBlocks.renderMinZ), renderBlocks.renderMinY * (1.0D - renderBlocks.renderMinZ), renderBlocks.renderMinY * renderBlocks.renderMinZ);
                renderBlocks.brightnessBottomRight = renderBlocks.mixAoBrightness(brightnessTopLeftTemp, brightnessTopRightTemp, brightnessBottomRightTemp, brightnessBottomLeftTemp, (1.0D - renderBlocks.renderMaxY) * renderBlocks.renderMinZ, (1.0D - renderBlocks.renderMaxY) * (1.0D - renderBlocks.renderMinZ), renderBlocks.renderMaxY * (1.0D - renderBlocks.renderMinZ), renderBlocks.renderMaxY * renderBlocks.renderMinZ);
                renderBlocks.brightnessTopRight = renderBlocks.mixAoBrightness(brightnessTopLeftTemp, brightnessTopRightTemp, brightnessBottomRightTemp, brightnessBottomLeftTemp, (1.0D - renderBlocks.renderMaxY) * renderBlocks.renderMaxZ, (1.0D - renderBlocks.renderMaxY) * (1.0D - renderBlocks.renderMaxZ), renderBlocks.renderMaxY * (1.0D - renderBlocks.renderMaxZ), renderBlocks.renderMaxY * renderBlocks.renderMaxZ);
            } else {
	            ao_no_color[0] = (renderBlocks.aoLightValueScratchXYPN + renderBlocks.aoLightValueScratchXYZPNP + aoLightValue + renderBlocks.aoLightValueScratchXZPP) / 4.0F;
	            ao_no_color[3] = (renderBlocks.aoLightValueScratchXYZPNN + renderBlocks.aoLightValueScratchXYPN + renderBlocks.aoLightValueScratchXZPN + aoLightValue) / 4.0F;
	            ao_no_color[2] = (renderBlocks.aoLightValueScratchXZPN + aoLightValue + renderBlocks.aoLightValueScratchXYZPPN + renderBlocks.aoLightValueScratchXYPP) / 4.0F;
	            ao_no_color[1] = (aoLightValue + renderBlocks.aoLightValueScratchXZPP + renderBlocks.aoLightValueScratchXYPP + renderBlocks.aoLightValueScratchXYZPPP) / 4.0F;
	            renderBlocks.brightnessTopLeft = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessXYPN, renderBlocks.aoBrightnessXYZPNP, renderBlocks.aoBrightnessXZPP, offsetBrightness);
	            renderBlocks.brightnessTopRight = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessXZPP, renderBlocks.aoBrightnessXYPP, renderBlocks.aoBrightnessXYZPPP, offsetBrightness);
	            renderBlocks.brightnessBottomRight = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessXZPN, renderBlocks.aoBrightnessXYZPPN, renderBlocks.aoBrightnessXYPP, offsetBrightness);
	            renderBlocks.brightnessBottomLeft = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessXYZPNN, renderBlocks.aoBrightnessXYPN, renderBlocks.aoBrightnessXZPN, offsetBrightness);
            }
            
		    setupColorAndRender(TE, renderBlocks, block, 5, x, y, z, 0.6F);
            
            side_rendered = true;
        }

        renderBlocks.enableAO = false;
        return side_rendered;
    }
    
    /**
     * Renders a standard cube block at the given coordinates, with a given color ratio.  Args: block, x, y, z, r, g, b
     */
    protected boolean renderStandardBlockWithColorMultiplier(TEPaintersFlowerPot TE, RenderBlocks renderBlocks, Block block, int x, int y, int z, float red, float green, float blue)
    {
        renderBlocks.enableAO = false;
        Tessellator tessellator = Tessellator.instance;
        boolean side_rendered = false;
        float[] rgb_YP = { 1.0F, 1.0F, 1.0F };
        float[] rgb_YN = { 0.5F, 0.5F, 0.5F };
        float[] rgb_X = { 0.6F, 0.6F, 0.6F };
        float[] rgb_Z = { 0.8F, 0.8F, 0.8F };

        int mixedBrightness = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y, z);

        if (renderBlocks.renderAllFaces || block.shouldSideBeRendered(renderBlocks.blockAccess, x, y - 1, z, 0))
        {
            tessellator.setBrightness(renderBlocks.renderMinY > 0.0D ? mixedBrightness : block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y - 1, z));
    		rgb_no_color = rgb_YN;
        	setupColorAndRender(TE, renderBlocks, block, 0, x, y, z, 0.0F);

            side_rendered = true;
        }

        if (renderBlocks.renderAllFaces || block.shouldSideBeRendered(renderBlocks.blockAccess, x, y + 1, z, 1))
        {
            tessellator.setBrightness(renderBlocks.renderMaxY < 1.0D ? mixedBrightness : block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y + 1, z));
    		rgb_no_color = rgb_YP;
        	setupColorAndRender(TE, renderBlocks, block, 1, x, y, z, 0.0F);
            
            side_rendered = true;
        }

        if (renderBlocks.renderAllFaces || block.shouldSideBeRendered(renderBlocks.blockAccess, x, y, z - 1, 2))
        {
            tessellator.setBrightness(renderBlocks.renderMinZ > 0.0D ? mixedBrightness : block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y, z - 1));
    		rgb_no_color = rgb_Z;
        	setupColorAndRender(TE, renderBlocks, block, 2, x, y, z, 0.0F);

            side_rendered = true;
        }

        if (renderBlocks.renderAllFaces || block.shouldSideBeRendered(renderBlocks.blockAccess, x, y, z + 1, 3))
        {
            tessellator.setBrightness(renderBlocks.renderMaxZ < 1.0D ? mixedBrightness : block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y, z + 1));
    		rgb_no_color = rgb_Z;
        	setupColorAndRender(TE, renderBlocks, block, 3, x, y, z, 0.0F);

            side_rendered = true;
        }

        if (renderBlocks.renderAllFaces || block.shouldSideBeRendered(renderBlocks.blockAccess, x - 1, y, z, 4))
        {
            tessellator.setBrightness(renderBlocks.renderMinX > 0.0D ? mixedBrightness : block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x - 1, y, z));
    		rgb_no_color = rgb_X;
        	setupColorAndRender(TE, renderBlocks, block, 4, x, y, z, 0.0F);

            side_rendered = true;
        }

        if (renderBlocks.renderAllFaces || block.shouldSideBeRendered(renderBlocks.blockAccess, x + 1, y, z, 5))
        {
            tessellator.setBrightness(renderBlocks.renderMaxX < 1.0D ? mixedBrightness : block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x + 1, y, z));
    		rgb_no_color = rgb_X;
        	setupColorAndRender(TE, renderBlocks, block, 5, x, y, z, 0.0F);

            side_rendered = true;
        }

        return side_rendered;
    }
    
}