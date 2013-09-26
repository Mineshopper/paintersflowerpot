package paintersflowerpot;

import net.minecraft.block.Block;
import net.minecraft.block.StepSound;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.Property;
import paintersflowerpot.block.BlockPaintersFlowerPot;
import paintersflowerpot.item.ItemPaintersFlowerPot;
import paintersflowerpot.item.ItemUnfiredPaintersFlowerPot;
import paintersflowerpot.proxy.CommonProxy;
import paintersflowerpot.tileentity.TEPaintersFlowerPot;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

@Mod(
        modid = "PaintersFlowerPot",
        name = "Painter's Flower Pot",
        version = "v1.59",
        dependencies = "after:BiomesOPlenty"
)
@NetworkMod(
        clientSideRequired = true,
        serverSideRequired = false
)
public class PaintersFlowerPot
{
    @Instance("PaintersFlowerPot")
    public static PaintersFlowerPot instance;
    @SidedProxy(
            clientSide = "paintersflowerpot.proxy.ClientProxy",
            serverSide = "paintersflowerpot.proxy.CommonProxy"
    )
    
    public static CommonProxy proxy;
    
    public static Block	blockPaintersFlowerPot;
    
    public static Item	itemUnfiredPaintersFlowerPot,
    					itemPaintersFlowerPot;
    
    public static int	paintersFlowerPotRenderID,
    					blockPaintersFlowerPotID,
    					itemUnfiredPaintersFlowerPotID;
    
    public static boolean	overrideColor;
    
    public static int	baseBlockID = 2400,
    					baseItemID = 4700;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {  	
        Configuration config = new Configuration(event.getSuggestedConfigurationFile());
        config.load();
        
        blockPaintersFlowerPotID = config.getBlock("Painter's Flower Pot", baseBlockID++).getInt(baseBlockID);
        itemUnfiredPaintersFlowerPotID = config.getItem("Unfired Painter's Flower Pot", baseItemID++).getInt(baseItemID);
        
        Property overrideColorProp = config.get(Configuration.CATEGORY_GENERAL, "Override Biome Coloring", true);
        overrideColorProp.comment = "This overrides Minecraft's default behavior where wild grasses are colored according to biome.  Set to true to override coloring to a lively green.";
        overrideColor = overrideColorProp.getBoolean(true);
        
        config.save();
    }

    @EventHandler
    public void load(FMLInitializationEvent event)
    {
    	GameRegistry.registerTileEntity(TEPaintersFlowerPot.class, "TileEntityPaintersFlowerPot");
    	final StepSound soundPowderFootstep = new StepSound("stone", 1.0F, 1.0F);
        blockPaintersFlowerPot = new BlockPaintersFlowerPot(blockPaintersFlowerPotID);
        itemUnfiredPaintersFlowerPot = new ItemUnfiredPaintersFlowerPot(itemUnfiredPaintersFlowerPotID - 256);
        itemPaintersFlowerPot = new ItemPaintersFlowerPot(blockPaintersFlowerPotID - 256);
        LanguageRegistry.addName(blockPaintersFlowerPot, "Painter's Flower Pot");
        LanguageRegistry.addName(itemUnfiredPaintersFlowerPot, "Unfired Painter's Flower Pot");
        LanguageRegistry.addName(itemPaintersFlowerPot, "Painter's Flower Pot");
        GameRegistry.addRecipe(new ItemStack(itemUnfiredPaintersFlowerPot, 1), new Object[] {"X X", " X ", 'X', Item.clay});
        GameRegistry.addRecipe(new ItemStack(itemUnfiredPaintersFlowerPot, 1), new Object[] {"XXX", " X ", 'X', Item.clay});
        GameRegistry.addSmelting(itemUnfiredPaintersFlowerPotID, new ItemStack(itemPaintersFlowerPot, 1), 0.9F);
    	proxy.registerRenderInformation();
    }

}
