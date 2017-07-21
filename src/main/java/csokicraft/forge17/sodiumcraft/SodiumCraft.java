package csokicraft.forge17.sodiumcraft;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.*;
import net.minecraft.item.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.*;
import net.minecraftforge.oredict.*;

import java.util.*;

import com.pam.harvestcraft.item.ItemRegistry;

import cofh.lib.util.helpers.ItemHelper;
import cofh.thermalexpansion.ThermalExpansion;
import cofh.thermalexpansion.init.TEItems;
import cofh.thermalexpansion.util.managers.machine.SmelterManager;
import cofh.thermalfoundation.init.TFItems;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.LoaderException;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.registry.GameRegistry;
import mekanism.api.gas.*;
import mekanism.common.MekanismItems;

import static csokicraft.forge17.sodiumcraft.SodiumItems.*;

@Mod(modid = SodiumCraft.MODID, version = SodiumCraft.VERSION, dependencies="required-after:Mekanism;after:thermalexpansion")
public class SodiumCraft
{
	public static final String MODID = "sodiumcraft";
	public static final String VERSION = "1.2";

	@SidedProxy(serverSide="csokicraft.forge17.sodiumcraft.CommonProxy", clientSide="csokicraft.forge17.sodiumcraft.ClientProxy")
	public static CommonProxy proxy;
	public static Item itemSodium = new ItemSodium().setCreativeTab(CreativeTabs.MATERIALS);

	@EventHandler
	public void preinit(FMLPreInitializationEvent e){
		if(Loader.isModLoaded("thermalexpansion")){
			if(!ThermalExpansion.VERSION.startsWith("5.1")){
				throw new LoaderException("ThermalExpansion 5.1 is required!");
			}
		}
	}

	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		GameRegistry.registerItem(itemSodium, "itemSodium");
		proxy.registerModels();
		
		NBTTagCompound imcTag;
		
		//Sodium crystallization
		imcTag = new NBTTagCompound();
		imcTag.setTag("input", new GasStack(GasRegistry.getGas("sodium"), 100).write(new NBTTagCompound()));
		imcTag.setTag("output", Na.writeToNBT(new NBTTagCompound()));
		FMLInterModComms.sendMessage("Mekanism", "ChemicalCrystallizerRecipe", imcTag);
		
		//Sodium hydration
		imcTag = new NBTTagCompound();
		imcTag.setTag("input", Na.writeToNBT(new NBTTagCompound()));
		imcTag.setTag("gasType", new GasStack(GasRegistry.getGas("water"), 100).write(new NBTTagCompound()));
		imcTag.setTag("output", NaOH.writeToNBT(new NBTTagCompound()));
		FMLInterModComms.sendMessage("Mekanism", "ChemicalInjectionChamberRecipe", imcTag);
		
		//Sodium sulfurization
		GameRegistry.addRecipe(new ShapelessOreRecipe(NaS, Na, "dustSulfur"));
		
		//wood steaming
		for(int i=0;i<4;i++){
			imcTag = new NBTTagCompound();
			imcTag.setTag("input", new ItemStack(Blocks.LOG, 1, i).writeToNBT(new NBTTagCompound()));
			imcTag.setTag("gasType", new GasStack(GasRegistry.getGas("water"), 100).write(new NBTTagCompound()));
			imcTag.setTag("output", aqWood.writeToNBT(new NBTTagCompound()));
			FMLInterModComms.sendMessage("Mekanism", "ChemicalInjectionChamberRecipe", imcTag);
		}
		
		for(int i=0;i<2;i++){
			imcTag = new NBTTagCompound();
			imcTag.setTag("input", new ItemStack(Blocks.LOG2, 1, i).writeToNBT(new NBTTagCompound()));
			imcTag.setTag("gasType", new GasStack(GasRegistry.getGas("water"), 100).write(new NBTTagCompound()));
			imcTag.setTag("output", aqWood.writeToNBT(new NBTTagCompound()));
			FMLInterModComms.sendMessage("Mekanism", "ChemicalInjectionChamberRecipe", imcTag);
		}
		
		//Sodium bicarbonate
		imcTag = new NBTTagCompound();
		imcTag.setTag("input", Na2CO3.writeToNBT(new NBTTagCompound()));
		imcTag.setTag("gasType", new GasStack(GasRegistry.getGas("water"), 100).write(new NBTTagCompound()));
		imcTag.setTag("output", NaHCO3.writeToNBT(new NBTTagCompound()));
		FMLInterModComms.sendMessage("Mekanism", "ChemicalInjectionChamberRecipe", imcTag);
		
		//Nitric acid production
		imcTag = new NBTTagCompound();
		imcTag.setTag("input", PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.WATER).writeToNBT(new NBTTagCompound()));
		imcTag.setTag("output", HNO3.writeToNBT(new NBTTagCompound()));
		FMLInterModComms.sendMessage("Mekanism", "EnrichmentChamberRecipe", imcTag);
		//And usage
		GameRegistry.addShapelessRecipe(NaNO3, NaOH, HNO3);
		
		//Cellulose extraction, loosely based on the "Kraft process"
		GameRegistry.addShapelessRecipe(new ItemStack(Items.PAPER, 3), NaOH, NaS, aqWood);
		
		//Sodium nitrate uses
		OreDictionary.registerOre("dustSaltpeter", NaNO3);
		GameRegistry.addShapelessRecipe(new ItemStack(Items.GUNPOWDER), NaNO3, Items.COAL);
		GameRegistry.addShapelessRecipe(new ItemStack(Items.GUNPOWDER, 2), NaNO3, MekanismItems.Substrate);
		
		if(Loader.isModLoaded("thermalexpansion")){
			//Aluminum processing
			List<ItemStack> aluOres = new ArrayList<ItemStack>(),
							aluBars = new ArrayList<ItemStack>();
			aluOres.addAll(OreDictionary.getOres("oreAluminum"));
			aluOres.addAll(OreDictionary.getOres("oreAluminium"));
			aluBars.addAll(OreDictionary.getOres("ingotAluminum"));
			aluBars.addAll(OreDictionary.getOres("ingotAluminium"));
			
			if(!aluBars.isEmpty())
				for(ItemStack aluOre:aluOres)
					SmelterManager.addRecipe(2400, NaOH, aluOre, ItemHelper.cloneStack(aluBars.get(0), 3), new ItemStack(TFItems.itemMaterial, 1, 865), 50);
			
			//Sodium carbonation
			SmelterManager.addRecipe(800, NaOH, OreDictionary.getOres("dustCoal").get(0), Na2CO3);
			
			//Baking with soda
			SmelterManager.addRecipe(800, NaHCO3, new ItemStack(Items.WHEAT), new ItemStack(Items.BREAD, 2));
			
			//Sodium carbonate glass
			SmelterManager.addRecipe(800, Na2CO3, new ItemStack(Blocks.SAND), new ItemStack(Blocks.GLASS, 2), NaNO3, 25);
			SmelterManager.addRecipe(800, Na2CO3, new ItemStack(TFItems.itemMaterial, 1, 864), new ItemStack(Blocks.GLASS), new ItemStack(Blocks.GLASS), 50);
			
			//Flesh processing
			for(int i=0;i<5;i++)
				SmelterManager.addRecipe(800, new ItemStack(itemSodium, 16, 1), new ItemStack(Items.SKULL, 1, i), new ItemStack(Items.ROTTEN_FLESH, 16));
			SmelterManager.addRecipe(800, new ItemStack(itemSodium, 8, 1), new ItemStack(Items.SPIDER_EYE), new ItemStack(Items.ROTTEN_FLESH, 8));
			SmelterManager.addRecipe(800, new ItemStack(itemSodium, 12, 1), new ItemStack(Items.FERMENTED_SPIDER_EYE), new ItemStack(Items.ROTTEN_FLESH, 12));
			/*
			//Potato battery Overhauled Edition
			GameRegistry.addRecipe(new ShapedOreRecipe(TEItems.capacitorPotato,
					"scs", "sps", "scs",
					's', NaOH,
					'p', Items.POTATO,
					'c', "nuggetCopper"
				));
			
			GameRegistry.addRecipe(new ShapedOreRecipe(TEItems.capacitorPotato,
					"scs", "sps", "scs",
					's', NaOH,
					'p', Items.POISONOUS_POTATO,
					'c', "nuggetCopper"
				));
			*/
		}else{
			//fallback vanilla recipes
			GameRegistry.addShapelessRecipe(Na2CO3, Items.COAL, Items.COAL, NaOH);
			GameRegistry.addShapelessRecipe(new ItemStack(Items.BREAD, 2), NaHCO3, NaHCO3, new ItemStack(Items.WHEAT));
			GameRegistry.addShapelessRecipe(new ItemStack(Blocks.GLASS, 1), Na2CO3, Na2CO3, new ItemStack(Blocks.SAND));
		}
		
		if(Loader.isModLoaded("harvestcraft")){
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ItemRegistry.doughItem, 2), NaHCO3, "foodFlour", "toolMixingbowl", "listAllwater"));
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ItemRegistry.doughItem, 2), NaHCO3, "dustWheat", "toolMixingbowl", "listAllwater"));
		}
	}
}
