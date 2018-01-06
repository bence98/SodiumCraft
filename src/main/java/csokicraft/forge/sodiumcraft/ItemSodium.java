package csokicraft.forge.sodiumcraft;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.*;
import net.minecraft.util.NonNullList;

public class ItemSodium extends Item{
	public static int META_COUNT=11;
	
	public ItemSodium(){
		hasSubtypes = true;
		setUnlocalizedName("itemSodium");
	}
	
	@Override
	public String getUnlocalizedName(ItemStack is){
		return getUnlocalizedName()+"."+is.getItemDamage();
	}
	
	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> l){
		if(isInCreativeTab(tab))
			for(int i=0;i<META_COUNT;i++){
				l.add(new ItemStack(this, 1, i));
			}
	}
}
