package paintersflowerpot.proxy;

import paintersflowerpot.PaintersFlowerPot;
import paintersflowerpot.renderer.BlockHandlerPaintersFlowerPot;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy
{    
	@Override
    public void registerRenderInformation()
    {
        PaintersFlowerPot.paintersFlowerPotRenderID = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(PaintersFlowerPot.paintersFlowerPotRenderID, new BlockHandlerPaintersFlowerPot()); 
    }
}
