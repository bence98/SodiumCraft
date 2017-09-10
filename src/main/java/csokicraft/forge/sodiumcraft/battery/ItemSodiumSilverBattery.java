package csokicraft.forge.sodiumcraft.battery;

import java.util.List;

import cofh.redstoneflux.impl.ItemEnergyContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemSodiumSilverBattery extends ItemEnergyContainer{
	public ItemSodiumSilverBattery(){
		super(100000, 1000);
		setUnlocalizedName("itemSilverBattery");
		setMaxStackSize(1);
	}
	
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn){
		int nrg=getEnergyStored(stack);
		int max=getMaxEnergyStored(stack);
		tooltip.add(I18n.format("tooltip.battery.energy", nrg, max));
		super.addInformation(stack, worldIn, tooltip, flagIn);
	}
}
