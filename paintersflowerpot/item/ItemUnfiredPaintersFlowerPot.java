package paintersflowerpot.item;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemUnfiredPaintersFlowerPot extends Item {

	public ItemUnfiredPaintersFlowerPot(int id) {
		super(id);
		this.setMaxStackSize(64);
		this.setCreativeTab(CreativeTabs.tabDecorations);
		this.setUnlocalizedName("itemUnfiredPaintersFlowerPot");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister iconRegister)
	{
		itemIcon = iconRegister.registerIcon("paintersflowerpot:unFiredPaintersFlowerPot");
	}

}