package csokicraft.forge.sodiumcraft.battery;

import java.util.List;

import cofh.redstoneflux.impl.ItemEnergyContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

public class ItemSodiumPotatoBattery extends ItemEnergyContainer{
	public ItemSodiumPotatoBattery(){
		super(10000, 0, 1000);
		setUnlocalizedName("itemPotatoBattery");
		setMaxStackSize(1);
	}
	
	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected){
		if(getEnergyStored(stack)==0){
			entityIn.replaceItemInInventory(itemSlot, new ItemStack(Items.BAKED_POTATO));
		}
		super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
	}
	
	@Override
	public void onCreated(ItemStack stack, World worldIn, EntityPlayer playerIn){
		if (!stack.hasTagCompound()){
			stack.setTagCompound(new NBTTagCompound());
		}
		stack.getTagCompound().setInteger("Energy", capacity);
		super.onCreated(stack, worldIn, playerIn);
	}
	
	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items){
		if(isInCreativeTab(tab)){
			ItemStack stack=new ItemStack(this);
			onCreated(stack, null, null);
			items.add(stack);
		}
	}
	
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn){
		int nrg=getEnergyStored(stack);
		int max=getMaxEnergyStored(stack);
		tooltip.add(I18n.format("tooltip.battery.energy", nrg, max));
		super.addInformation(stack, worldIn, tooltip, flagIn);
	}
}
