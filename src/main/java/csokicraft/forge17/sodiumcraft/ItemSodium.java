package csokicraft.forge17.sodiumcraft;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.*;
import net.minecraft.util.IIcon;

public class ItemSodium extends Item{
	public static int META_COUNT=8;
	
	protected IIcon[] icons;
	
	public ItemSodium(){
		hasSubtypes = true;
		icons = new IIcon[META_COUNT];
		setUnlocalizedName(getName());
	}
	
	private String getName() {
		return "itemSodium";
	}
	
	@Override
	public void registerIcons(IIconRegister reg) {
		for(int i=0;i<icons.length;i++)
			icons[i] = reg.registerIcon(SodiumCraft.MODID+":"+getName()+"."+i);
	}
	
	@Override
	public String getUnlocalizedName(ItemStack is){
		return getUnlocalizedName()+"."+is.getItemDamage();
	}
	
	@Override
	public IIcon getIconFromDamage(int i) {
		return icons[i];
	}
	
	@Override
	public void getSubItems(Item item, CreativeTabs tab, List l) {
		for(int i=0;i<META_COUNT;i++){
			l.add(new ItemStack(item, 1, i));
		}
	}
}
