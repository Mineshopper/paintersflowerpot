package paintersflowerpot.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;

public class ItemPaintersFlowerPot extends ItemBlock {

        public ItemPaintersFlowerPot(int id) {
    		super(id);
    		this.setMaxStackSize(16);
    		this.setCreativeTab(CreativeTabs.tabDecorations);
    		this.setUnlocalizedName("itemPaintersFlowerPot");
        }
}