package paintersflowerpot.block;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.player.BonemealEvent;
import paintersflowerpot.PaintersFlowerPot;
import paintersflowerpot.tileentity.TEPaintersFlowerPot;
import paintersflowerpot.util.BlockProperties;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockPaintersFlowerPot extends BlockContainer
{
	
    public BlockPaintersFlowerPot(int par1)
    {
        super(par1, Material.circuits);
		this.setCreativeTab(CreativeTabs.tabDecorations);
    	this.setBlockBounds(0.3125F, 0.0F, 0.3125F, 0.6875F, 0.375F, 0.6875F);
    	this.setHardness(0.5F);
    	this.setStepSound(soundPowderFootstep);
    	this.setUnlocalizedName("blockPaintersFlowerPot");
    }
    
    public static final String[] dyeIcons = new String[]
    {
    	"blackPot", "redPot", "greenPot", "brownPot", "bluePot", "purplePot",
    	"cyanPot", "lightGrayPot",  "grayPot", "pinkPot", "limePot", "yellowPot",
    	"lightBluePot", "magentaPot", "orangePot", "whitePot"
    };
    public static final String[] specialIcons = new String[]
    {
    	"originalPot", "rainbowPot", "glassPot"
    };
	public static Icon[] dyeIcon = new Icon[dyeIcons.length];
	public static Icon[] specialIcon = new Icon[specialIcons.length];
    
	@Override
	@SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconRegister)
    {
    	this.blockIcon = iconRegister.registerIcon("paintersflowerpot:potIcon");
    	
        // Register dye icons
        for (int count = 0; count < dyeIcons.length; count++)
        	dyeIcon[count] = iconRegister.registerIcon("paintersflowerpot:" + dyeIcons[count]);

        // Register special icons
        for (int count = 0; count < specialIcons.length; count++)
        	specialIcon[count] = iconRegister.registerIcon("paintersflowerpot:" + specialIcons[count]);
    }
	
    @Override
	@SideOnly(Side.CLIENT)
    /**
     * Returns true only if block is flowerPot
     */
    public boolean isFlowerPot()
    {
        return true;
    }
    
    /**
     * Triggered whenever an entity collides with this block (enters into the block). Args: world, x, y, z, entity
     */
    @Override
	public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity)
    {
    	TEPaintersFlowerPot TE = (TEPaintersFlowerPot) world.getBlockTileEntity(x, y, z);

    	if (TE != null)
    		if (BlockProperties.hasPlant(TE))
    			BlockProperties.getPlantBlock(TE).onEntityCollidedWithBlock(world, x, y, z, entity);
    }
    
    /**
     * Ray traces through the blocks collision from start vector to end vector returning a ray trace hit. Args: world,
     * x, y, z, startVec, endVec
     */
    @Override
	public MovingObjectPosition collisionRayTrace(World world, int x, int y, int z, Vec3 startVec, Vec3 endVec)
    {
    	TEPaintersFlowerPot TE = (TEPaintersFlowerPot) world.getBlockTileEntity(x, y, z);

    	// Determine if ray trace is a hit on flower pot
    	if (TE != null) {
    		if (BlockProperties.hasPlant(TE)) {
    			Block plantBlock = BlockProperties.getPlantBlock(TE);
    			if (plantBlock.blockMaterial == Material.cactus || plantBlock.blockMaterial == Material.leaves) {
    				this.setBlockBounds(0.3125F, 0.0F, 0.3125F, 0.6875F, 0.99F, 0.6875F);
    				MovingObjectPosition traceResult = super.collisionRayTrace(world, x, y, z, startVec, endVec);
    				if (traceResult != null) {
    			    	this.setBlockBounds(0.3125F, 0.0F, 0.3125F, 0.6875F, 0.375F, 0.6875F);
    					return traceResult;
    				}
    			}
    		}
    	}
    	
    	this.setBlockBounds(0.3125F, 0.0F, 0.3125F, 0.6875F, 0.375F, 0.6875F);
		MovingObjectPosition traceResult = super.collisionRayTrace(world, x, y, z, startVec, endVec);
		if (traceResult != null)
			return traceResult;
		
    	this.setBlockBounds(0.3125F, 0.0F, 0.3125F, 0.6875F, 0.375F, 0.6875F);
		return null;
    }
    
    /**
     * Adds all intersecting collision boxes to a list. (Be sure to only add boxes to the list if they intersect the
     * mask.) Parameters: World, X, Y, Z, mask, list, colliding entity
     */
    @Override
	public void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB axisAlignedBB, List list, Entity entity)
    {
    	AxisAlignedBB box1 = null;
    	AxisAlignedBB box2 = null;
    	
    	if (world.getBlockId(x, y, z) == this.blockID)
    	{
    		box1 = AxisAlignedBB.getAABBPool().getAABB(x + 0.3125D, y, z + 0.3125D, x + 0.6875D, y + 0.375D, z + 0.6875D);

    		TEPaintersFlowerPot TE = (TEPaintersFlowerPot) world.getBlockTileEntity(x, y, z);
    		
    		if (TE != null) {
        		if (BlockProperties.hasPlant(TE)) {
        			Block plantBlock = BlockProperties.getPlantBlock(TE);
        			if (plantBlock.blockMaterial == Material.cactus || plantBlock.blockMaterial == Material.leaves)
        				box2 = AxisAlignedBB.getAABBPool().getAABB(x + 0.375D, y + 0.3125D, z + 0.375D, x + 0.625D, y + 0.99D, z + 0.625D);
        		}
    		}
    	}
    	
		if (axisAlignedBB.intersectsWith(box1))
			list.add(box1);
		if (box2 != null && axisAlignedBB.intersectsWith(box2))
			list.add(box2);
    }
    
    /**
     * Returns light value based on plant in pot
     */
    @Override
	public int getLightValue(IBlockAccess blockAccess, int x, int y, int z)
    {
		int temp_lightValue = lightValue[this.blockID];
    	
		if (blockAccess.getBlockId(x, y, z) == this.blockID)
		{
			TEPaintersFlowerPot TE = (TEPaintersFlowerPot) blockAccess.getBlockTileEntity(x, y, z);

			if (BlockProperties.hasCover(TE))
			{
				int cover_lightValue = lightValue[BlockProperties.getCoverBlock(TE).blockID];
				
				if (cover_lightValue > temp_lightValue)
					temp_lightValue = cover_lightValue;
			}
			
			if (BlockProperties.hasSoil(TE))
			{
				int soil_lightValue = lightValue[BlockProperties.getSoilBlock(TE).blockID];
				
				if (soil_lightValue > temp_lightValue)
					temp_lightValue = soil_lightValue;
			}				
			
			if (BlockProperties.hasPlant(TE))
			{
				int plant_lightValue = lightValue[BlockProperties.getPlantBlock(TE).blockID];
				
				if (plant_lightValue > temp_lightValue)
					temp_lightValue = plant_lightValue;
			}
		}

		return temp_lightValue;
    }

    /**
     * Returns the ID of the items to drop on destruction.
     */
    @Override
	public int idDropped(int var1, Random var2, int var3)
    {	
        return PaintersFlowerPot.itemPaintersFlowerPot.itemID;
    }
    
    @Override
	@SideOnly(Side.CLIENT)
    /**
     * only called by clickMiddleMouseButton , and passed to inventory.setCurrentItem (along with isCreative)
     */
    public int idPicked(World world, int x, int y, int z) {
    	
    	return PaintersFlowerPot.itemPaintersFlowerPot.itemID;
    }

    /**
     * Disable default bonemeal behavior to enable use as dye
     */
    @ForgeSubscribe
    public void onUseBonemeal(BonemealEvent event) {  }
    
    /**
     * Called when the block is clicked by a player. Args: x, y, z, entityPlayer
     */
    @Override
	public void onBlockClicked(World world, int x, int y, int z, EntityPlayer entityPlayer)
    {
    	TEPaintersFlowerPot TE = (TEPaintersFlowerPot) world.getBlockTileEntity(x, y, z);

    	if (BlockProperties.hasPlant(TE)) {
    		BlockProperties.setPlant(TE, (ItemStack)null);
    	} else if (BlockProperties.hasSoil(TE)) {
    		BlockProperties.setSoil(TE, (ItemStack)null);
    	} else {
    		BlockProperties.ejectCoverAttributes(TE);
    	}
    }
    
    /**
     * Called upon block activation (right click on the block.)
     */
    @Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer entityPlayer, int side, float hitX, float hitY, float hitZ)
    {
    	TEPaintersFlowerPot TE = (TEPaintersFlowerPot) world.getBlockTileEntity(x, y, z);
		ItemStack itemStack = entityPlayer.getCurrentEquippedItem();
		boolean actionPerformed = false;
		boolean decrementInventory = false;

    	if (itemStack != null)
    	{
        	// Check held item for dye color properties
        	if (itemStack.itemID == Item.dyePowder.itemID)
        	{
    			if (BlockProperties.doDyeColorsMatch(TE, itemStack)) {
    				actionPerformed = true;
    			} else {
    				BlockProperties.setDyeColor(TE, itemStack);
    				actionPerformed = true;
    				decrementInventory = true;
    			}
        	}
        	
        	// Check held item for special color properties
        	if (itemStack.itemID == Item.speckledMelon.itemID || itemStack.itemID == Item.enderPearl.itemID)
        	{
    			if (BlockProperties.doSpecialColorsMatch(TE, itemStack)) {
    				actionPerformed = true;
    			} else {
    				BlockProperties.setSpecialColor(TE, itemStack);
    				actionPerformed = true;
    				decrementInventory = true;
    			}
        	}
    		
    		if (itemStack.getItem() instanceof ItemBlock || itemStack.getItem() == Item.reed)
    		{
    			Block block;

    			if (itemStack.getItem() == Item.reed) {
    				block = Block.reed;
    			} else {
    				block = Block.blocksList[itemStack.itemID];
    			}

    			if (BlockProperties.isCover(itemStack))
    			{
    				if (BlockProperties.doCoversMatch(TE, itemStack)) {
        				actionPerformed = true;
    				} else {
    					BlockProperties.setCover(TE, itemStack);
        				actionPerformed = true;
    					decrementInventory = true;
    				}
    			}

    			if (BlockProperties.isSoil(itemStack))
    			{
    				if (BlockProperties.doSoilsMatch(TE, itemStack)) {
        				actionPerformed = true;
    				} else {
    					BlockProperties.setSoil(TE, itemStack);
        				actionPerformed = true;
        				decrementInventory = true;
    				}
    			}

    			if (BlockProperties.hasSoil(TE))
    			{
    				if (BlockProperties.isPlant(itemStack))
    				{
    					Block soilBlock = BlockProperties.getSoilBlock(TE);

    					if (BlockProperties.doPlantsMatch(TE, itemStack)) {
    	    				actionPerformed = true;
    					} else {
    						BlockProperties.setPlant(TE, itemStack);
    	    				actionPerformed = true;
    	    				decrementInventory = true;
    					}
    				}
    			}
    		}
    	}
    	
    	if (!world.isRemote && decrementInventory)
    		if (!entityPlayer.capabilities.isCreativeMode && --itemStack.stackSize <= 0)
    			entityPlayer.inventory.setInventorySlotContents(entityPlayer.inventory.currentItem, (ItemStack)null);

    	return actionPerformed;
    }
    
    /**
     * Ejects contained items into the world, and notifies neighbors of an update, as appropriate
     */
    @Override
	public void breakBlock(World world, int x, int y, int z, int var5, int metadata)
    {
    	TEPaintersFlowerPot TE = (TEPaintersFlowerPot)world.getBlockTileEntity(x, y, z);
    	
    	if (TE != null) {
    		if (BlockProperties.hasPlant(TE))
    			BlockProperties.setPlant(TE, (ItemStack)null);
    		if (BlockProperties.hasSoil(TE))
    			BlockProperties.setSoil(TE, (ItemStack)null);
    		
    		BlockProperties.ejectCoverAttributes(TE);
    	}
    	
    	super.breakBlock(world, x, y, z, var5, metadata);
    }
    
    /**
     * Lets the block know when one of its neighbor changes. Doesn't know which neighbor changed (coordinates passed are
     * their own) Args: x, y, z, neighbor blockID
     */
    @Override
	public void onNeighborBlockChange(World world, int x, int y, int z, int par5)
    {
        if (!canPlaceBlockAt(world, x, y, z))
        {
            this.dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
            world.setBlockToAir(x, y, z);
        }
    }
    
    /**
     * Checks to see if its valid to put this block at the specified coordinates. Args: world, x, y, z
     */
    @Override
	public boolean canPlaceBlockAt(World world, int x, int y, int z)
    {
    	int blockID = world.getBlockId(x, y - 1, z);
    	
    	if (blockID > 0)
    		return Block.blocksList[blockID].isBlockSolidOnSide(world, x, y - 1, z, ForgeDirection.UP) || Block.blocksList[blockID].canPlaceTorchOnTop(world, x, y - 1, z);
    	
        return false;
    }
    
	/**
	 * Determines if this block should render in this pass.
	 */
	@Override
	public boolean canRenderInPass(int pass)
	{
		ForgeHooksClient.setRenderPass(pass);
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	/**
	 * Returns which pass this block be rendered on. 0 for solids and 1 for alpha.
	 */
	public int getRenderBlockPass()
	{
		return 1;
	}

    /**
     * Is this block (a) opaque and (b) a full 1m cube?  This determines whether or not to render the shared face of two
     * adjacent blocks and also whether the player can attach torches, redstone wire, etc to this block.
     */
    @Override
	public boolean isOpaqueCube()
    {
        return false;
    }

    /**
     * If this block doesn't render as an ordinary block it will return False (examples: signs, buttons, stairs, etc)
     */
    @Override
	public boolean renderAsNormalBlock()
    {
        return false;
    }

    /**
     * Called whenever the block is added into the world. Args: world, x, y, z
     */
    @Override
	public void onBlockAdded(World world, int x, int y, int z)
    {	
    	world.setBlockTileEntity(x, y, z, this.createNewTileEntity(world));
    }
    
	@Override
	public TileEntity createNewTileEntity(World var1)
	{
		return new TEPaintersFlowerPot();
	}
    
    @Override
	public boolean hasTileEntity(int metadata)
    {
    	return true;
    }
    
    @Override
	@SideOnly(Side.CLIENT)
    /**
     * A randomly called display update to be able to add particles or other items for display
     */
    public void randomDisplayTick(World world, int x, int y, int z, Random random)
    {
    	TEPaintersFlowerPot TE = (TEPaintersFlowerPot) world.getBlockTileEntity(x, y, z);
    	
    	if (BlockProperties.hasPlant(TE))
    		BlockProperties.getPlantBlock(TE).randomDisplayTick(world, x, y, z, random);
    }
    
    /**
     * The type of render function that is called for this block
     */
    @Override
	public int getRenderType()
    {
        return PaintersFlowerPot.paintersFlowerPotRenderID;
    }
    	
}
