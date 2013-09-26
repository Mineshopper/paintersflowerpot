package paintersflowerpot.util;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBreakable;
import net.minecraft.block.BlockHalfSlab;
import net.minecraft.block.BlockPane;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.IShearable;
import paintersflowerpot.tileentity.TEPaintersFlowerPot;

public class BlockProperties {

	/**
	 * Ejects an item at given coordinates.
	 */
	private static void ejectEntity(TEPaintersFlowerPot TE, ItemStack itemStack)
	{
		if (!TE.worldObj.isRemote)
		{
			float offset = 0.7F;
			double rand1 = TE.worldObj.rand.nextFloat() * offset + (1.0F - offset) * 0.5D;
			double rand2 = TE.worldObj.rand.nextFloat() * offset + (1.0F - offset) * 0.2D + 0.6D;
			double rand3 = TE.worldObj.rand.nextFloat() * offset + (1.0F - offset) * 0.5D;
			
			EntityItem entityItem = new EntityItem(TE.worldObj, TE.xCoord + rand1, TE.yCoord + rand2, TE.zCoord + rand3, itemStack);

			entityItem.delayBeforeCanPickup = 10;
			TE.worldObj.spawnEntityInWorld(entityItem);
		}
	}
	
	/**
	 * Plays block placement sound when set as a cover.
	 */
	private static void playBlockPlacementSound(TEPaintersFlowerPot TE, int blockID)
	{
		if (!TE.worldObj.isRemote && blockID > 0)
		{
			Block block = Block.blocksList[blockID];
			TE.worldObj.playSoundEffect(TE.xCoord + 0.5F, TE.yCoord + 0.5F, TE.zCoord + 0.5F, block.stepSound.getPlaceSound(), block.stepSound.getVolume() + 1.0F / 2.0F, block.stepSound.getPitch() * 0.8F);
		}
	}
		
	/**
	 * Returns cover block.
	 */
	public final static Block getCoverBlock(TEPaintersFlowerPot TE)
	{
		return hasCover(TE) ? Block.blocksList[TE.potCover] : Block.flowerPot;
	}
	
	/**
	 * Returns soil block.
	 */
	public final static Block getSoilBlock(TEPaintersFlowerPot TE)
	{
		return Block.blocksList[TE.potSoil];
	}
	
	/**
	 * Returns plant block.
	 */
	public final static Block getPlantBlock(TEPaintersFlowerPot TE)
	{
		return Block.blocksList[TE.potPlant];
	}
	
	/**
	 * Returns whether pot has cover.
	 */
	public final static boolean hasCover(TEPaintersFlowerPot TE)
	{
		if (Block.blocksList[TE.potCover] == null)
			TE.potCover = (short) Block.flowerPot.blockID;
		
		return TE.potCover != Block.flowerPot.blockID;
	}
	
	/**
	 * Returns whether pot has cover.
	 */
	public final static boolean hasSoil(TEPaintersFlowerPot TE)
	{
		if (Block.blocksList[TE.potSoil] == null)
			TE.potSoil = 0;
		
		return TE.potSoil > 0;
	}
    
    /**
     * Returns whether pot has plant.
     */
    public static boolean hasPlant(TEPaintersFlowerPot TE)
    {
		if (Block.blocksList[TE.potPlant] == null)
			TE.potPlant = 0;
    	
    	return TE.potPlant != 0;
    }
    
	/**
	 * Returns whether block is a cover
	 */
	public final static boolean isCover(ItemStack itemStack)
	{
		if (itemStack.getItem() instanceof ItemBlock && !isSoil(itemStack) && !isPlant(itemStack))
		{
			Block block = Block.blocksList[itemStack.getItem().itemID];
			
			return	!block.hasTileEntity(itemStack.getItemDamage()) &&
					(
						block.renderAsNormalBlock() ||
						block instanceof BlockHalfSlab ||
						block instanceof BlockPane ||
						block instanceof BlockBreakable
					);
		}

		return false;
	}
	
    /**
     * Returns whether block is soil.
     */
    public static boolean isSoil(ItemStack itemStack)
    {    	
		if (itemStack.getItem() instanceof ItemBlock && !isPlant(itemStack))
		{
	    	Block block = Block.blocksList[itemStack.itemID];

	    	return	block.blockMaterial == Material.grass ||
	    			block.blockMaterial == Material.ground ||
	    			block.blockMaterial == Material.sand;
		}
    	
    	return false;
    }
    
    /**
     * Returns whether block is plant.
     */
    public static boolean isPlant(ItemStack itemStack)
    {
		if (itemStack.getItem() instanceof ItemBlock)
		{
			Block block = Block.blocksList[itemStack.getItem().itemID];
			
			return block instanceof IPlantable || block instanceof IShearable;
		}

		return false;
    }
    
    /**
     * Check if dye being applied matches existing dye on pot.
     */
    public static boolean doDyeColorsMatch(TEPaintersFlowerPot TE, ItemStack itemStack)
    {
    	return TE.potDyeColor == itemStack.getItemDamage();
    }
    
    /**
     * Returns whether pot has dye color.
     */
    public static boolean hasDyeColor(TEPaintersFlowerPot TE)
    {
    	return TE.potDyeColor >= 0;
    }
    
    /**
     * Ejects dye colors, special colors, or cover blocks from pot.
     */
    public static void ejectCoverAttributes(TEPaintersFlowerPot TE)
    {
    	boolean alteredState = false;
    	
    	if (hasDyeColor(TE))
    	{    		
    		ejectEntity(TE, new ItemStack(Item.dyePowder, 1, TE.potDyeColor));
    		TE.potDyeColor = -1;
    		alteredState = true;
    	}
    	if (hasSpecialColor(TE))
    	{
    		ejectEntity(TE, new ItemStack(Item.itemsList[convertSpecialColorIDtoItemID(TE.potSpecialColor)]));
    		TE.potSpecialColor = -1;
    		alteredState = true;
    	}
    	if (hasCover(TE))
    	{
    		ejectEntity(TE, new ItemStack(TE.potCover, 1, TE.potCoverMetadata));
    		TE.potCover = 0;
    		TE.potCoverMetadata = 0;
    		alteredState = true;
    	}
    	
    	if (alteredState)
    		TE.worldObj.markBlockForUpdate(TE.xCoord, TE.yCoord, TE.zCoord);
    }
    
    /**
     * Sets dye color on pot.
     */
    public static void setDyeColor(TEPaintersFlowerPot TE, ItemStack itemStack)
    {
		ejectCoverAttributes(TE);
    	
		TE.potDyeColor = (byte) itemStack.getItemDamage();
		TE.potSpecialColor = -1;
		TE.worldObj.markBlockForUpdate(TE.xCoord, TE.yCoord, TE.zCoord);
    }
    
    /**
     * Check if special color being applied matches existing special color on pot.
     */
    public static boolean doSpecialColorsMatch(TEPaintersFlowerPot TE, ItemStack itemStack)
    {
    	return convertItemIDtoSpecialColorID(itemStack.itemID) == TE.potSpecialColor;
    }
    
    /**
     * Returns whether pot has special color.
     */
    public static boolean hasSpecialColor(TEPaintersFlowerPot TE)
    {
    	return TE.potSpecialColor >= 0;
    }
    
    /**
     * Sets special paint color on pot.
     */
    public static void setSpecialColor(TEPaintersFlowerPot TE, ItemStack itemStack)
    {
		ejectCoverAttributes(TE);
		
		TE.potSpecialColor = (byte) convertItemIDtoSpecialColorID(itemStack.itemID);
		TE.potDyeColor = -1;
		TE.worldObj.markBlockForUpdate(TE.xCoord, TE.yCoord, TE.zCoord);
    }

    /**
     * Will convert item to its special color ID.
     */
    public static int convertItemIDtoSpecialColorID(int itemID)
    {
    	if (itemID == Item.enderPearl.itemID)
    		return 0;
    	else if (itemID == Item.speckledMelon.itemID)
    		return 1;
    	else // itemID == Block.glass.blockID
    		return 2;
    }
    
    /**
     * Will convert special color to its item ID.
     */
    private static int convertSpecialColorIDtoItemID(int specialColorID)
    {
    	if (specialColorID == 0)
    		return Item.enderPearl.itemID;
    	else
    		return Item.speckledMelon.itemID;
    }
    
    /**
     * Check if cover being applied matches existing cover on pot.
     */
    public static boolean doCoversMatch(TEPaintersFlowerPot TE, ItemStack itemStack)
    {
    	return TE.potCover == itemStack.itemID && TE.potCoverMetadata == itemStack.getItemDamage();
    }
    
    /**
     * Check if soil being placed matches existing soil on pot.
     */
    public static boolean doSoilsMatch(TEPaintersFlowerPot TE, ItemStack itemStack)
    {
    	return TE.potSoil == itemStack.itemID && TE.potSoilMetadata == itemStack.getItemDamage();
    }
    
    /**
     * Check if plant being placed matches existing plant in pot.
     */
    public static boolean doPlantsMatch(TEPaintersFlowerPot TE, ItemStack itemStack)
    {
    	return TE.potPlant == itemStack.itemID && TE.worldObj.getBlockMetadata(TE.xCoord, TE.yCoord, TE.zCoord) == itemStack.getItemDamage();
    }
	
	/**
	 * Sets cover block.
	 */
	public final static void setCover(TEPaintersFlowerPot TE, ItemStack itemStack)
	{
		playBlockPlacementSound(TE, itemStack == null ? getCoverBlock(TE).blockID : itemStack.itemID);
		
		int blockID, metadata;
		if (itemStack == null) {
			blockID = Block.flowerPot.blockID;
			metadata = 0;
		} else {
			blockID = itemStack.itemID;
			metadata = itemStack.getItemDamage();
		}
		
		ejectCoverAttributes(TE);

		TE.potCover = (short) blockID;
		TE.potCoverMetadata = (byte) metadata;
		TE.worldObj.markBlockForUpdate(TE.xCoord, TE.yCoord, TE.zCoord);
	}
	
	/**
	 * Sets soil block.
	 */
	public final static void setSoil(TEPaintersFlowerPot TE, ItemStack itemStack)
	{
		playBlockPlacementSound(TE, itemStack == null ? getSoilBlock(TE).blockID : itemStack.itemID);
		
		int blockID, metadata;
		if (itemStack == null) {
			blockID = 0;
			metadata = 0;
		} else {
			blockID = itemStack.itemID;
			metadata = itemStack.getItemDamage();
		}
		
		if (hasSoil(TE)) {
			if (hasPlant(TE))
				setPlant(TE, (ItemStack)null);
			ejectEntity(TE, new ItemStack(TE.potSoil, 1, TE.potSoilMetadata));
		}

		TE.potSoil = (short) blockID;
		TE.potSoilMetadata = (byte) metadata;
		TE.worldObj.markBlockForUpdate(TE.xCoord, TE.yCoord, TE.zCoord);
	}
	
	/**
	 * Sets plant block.
	 */
	public final static void setPlant(TEPaintersFlowerPot TE, ItemStack itemStack)
	{
		playBlockPlacementSound(TE, itemStack == null ? getPlantBlock(TE).blockID : itemStack.itemID);
		
		int blockID, metadata;
		if (itemStack == null) {
			blockID = 0;
			metadata = 0;
		} else {
			blockID = itemStack.itemID;
			metadata = itemStack.getItemDamage();
		}
		
		if (hasPlant(TE))
			ejectEntity(TE, new ItemStack(TE.potPlant, 1, TE.worldObj.getBlockMetadata(TE.xCoord, TE.yCoord, TE.zCoord)));

		TE.potPlant = (short) blockID;
		TE.worldObj.setBlockMetadataWithNotify(TE.xCoord, TE.yCoord, TE.zCoord, metadata, 2);
		TE.worldObj.markBlockForUpdate(TE.xCoord, TE.yCoord, TE.zCoord);
	}
		
}
