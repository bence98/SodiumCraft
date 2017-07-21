package csokicraft.forge17.sodiumcraft;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.*;
import net.minecraft.item.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.oredict.*;

import java.util.*;

import com.pam.harvestcraft.ItemRegistry;

import cofh.lib.util.helpers.ItemHelper;
import cofh.thermalexpansion.item.TEItems;
import cofh.thermalexpansion.util.crafting.SmelterManager;
import cofh.thermalfoundation.item.TFItems;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.*;
import cpw.mods.fml.common.registry.GameRegistry;
import mekanism.api.gas.*;
import mekanism.common.MekanismItems;

import static csokicraft.forge17.sodiumcraft.SodiumItems.*;

@Mod(modid = SodiumCraft.MODID, version = SodiumCraft.VERSION, dependencies="required-after:Mekanism;after:ThermalExpansion")
public class SodiumCraft
{
    public static final String MODID = "sodiumcraft";
    public static final String VERSION = "1.2";
    
    public static Item itemSodium = new ItemSodium().setCreativeTab(CreativeTabs.tabMaterials);
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
		GameRegistry.registerItem(itemSodium, "itemSodium");
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
			imcTag.setTag("input", new ItemStack(Blocks.log, 1, i).writeToNBT(new NBTTagCompound()));
			imcTag.setTag("gasType", new GasStack(GasRegistry.getGas("water"), 100).write(new NBTTagCompound()));
			imcTag.setTag("output", aqWood.writeToNBT(new NBTTagCompound()));
			FMLInterModComms.sendMessage("Mekanism", "ChemicalInjectionChamberRecipe", imcTag);
		}
		
		for(int i=0;i<2;i++){
			imcTag = new NBTTagCompound();
			imcTag.setTag("input", new ItemStack(Blocks.log2, 1, i).writeToNBT(new NBTTagCompound()));
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
		imcTag.setTag("input", new ItemStack(Items.potionitem).writeToNBT(new NBTTagCompound()));
		imcTag.setTag("output", HNO3.writeToNBT(new NBTTagCompound()));
		FMLInterModComms.sendMessage("Mekanism", "EnrichmentChamberRecipe", imcTag);
		//And usage
		GameRegistry.addShapelessRecipe(NaNO3, NaOH, HNO3);
		
		//Cellulose extraction, loosely based on the "Kraft process"
		GameRegistry.addShapelessRecipe(new ItemStack(Items.paper, 3), NaOH, NaS, aqWood);
		
		//Sodium nitrate uses
		OreDictionary.registerOre("dustSaltpeter", NaNO3);
		GameRegistry.addShapelessRecipe(new ItemStack(Items.gunpowder), NaNO3, Items.coal);
		GameRegistry.addShapelessRecipe(new ItemStack(Items.gunpowder, 2), NaNO3, MekanismItems.Substrate);
		
		if(Loader.isModLoaded("ThermalExpansion")){
			//Aluminum processing
			List<ItemStack> aluOres = new ArrayList<ItemStack>(),
							aluBars = new ArrayList<ItemStack>();
			aluOres.addAll(OreDictionary.getOres("oreAluminum"));
			aluOres.addAll(OreDictionary.getOres("oreAluminium"));
			aluBars.addAll(OreDictionary.getOres("ingotAluminum"));
			aluBars.addAll(OreDictionary.getOres("ingotAluminium"));
			
			if(!aluBars.isEmpty())
				for(ItemStack aluOre:aluOres)
					SmelterManager.addRecipe(2400, NaOH, aluOre, ItemHelper.cloneStack(aluBars.get(0), 3), TEItems.slagRich, 50);
			
			//Sodium carbonation
			SmelterManager.addRecipe(800, NaOH, OreDictionary.getOres("dustCoal").get(0), Na2CO3);
			
			//Baking with soda
			SmelterManager.addRecipe(800, NaHCO3, new ItemStack(Items.wheat), new ItemStack(Items.bread, 2));
			
			//Sodium carbonate glass
			SmelterManager.addRecipe(800, Na2CO3, new ItemStack(Blocks.sand), new ItemStack(Blocks.glass, 2), NaNO3, 25);
			SmelterManager.addRecipe(800, Na2CO3, TEItems.slag, new ItemStack(Blocks.glass), new ItemStack(Blocks.glass), 50);
			
			//Flesh processing
			for(int i=0;i<5;i++)
				SmelterManager.addRecipe(800, new ItemStack(itemSodium, 16, 1), new ItemStack(Items.skull, 1, i), new ItemStack(Items.rotten_flesh, 16));
			SmelterManager.addRecipe(800, new ItemStack(itemSodium, 8, 1), new ItemStack(Items.spider_eye), new ItemStack(Items.rotten_flesh, 8));
			SmelterManager.addRecipe(800, new ItemStack(itemSodium, 12, 1), new ItemStack(Items.fermented_spider_eye), new ItemStack(Items.rotten_flesh, 12));
			
			//Potato battery Overhauled Edition
			GameRegistry.addRecipe(new ShapedOreRecipe(TEItems.capacitorPotato,
					"scs", "sps", "scs",
					's', NaOH,
					'p', Items.potato,
					'c', "nuggetCopper"
				));
			
			GameRegistry.addRecipe(new ShapedOreRecipe(TEItems.capacitorPotato,
					"scs", "sps", "scs",
					's', NaOH,
					'p', Items.poisonous_potato,
					'c', "nuggetCopper"
				));
		}else{
			//fallback vanilla recipes
			GameRegistry.addShapelessRecipe(Na2CO3, Items.coal, Items.coal, NaOH);
			GameRegistry.addShapelessRecipe(new ItemStack(Items.bread, 2), NaHCO3, NaHCO3, new ItemStack(Items.wheat));
			GameRegistry.addShapelessRecipe(new ItemStack(Blocks.glass, 1), Na2CO3, Na2CO3, new ItemStack(Blocks.sand));
		}
		
		if(Loader.isModLoaded("harvestcraft")){
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ItemRegistry.doughItem, 2), NaHCO3, "foodFlour", "toolMixingbowl", "listAllwater"));
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ItemRegistry.doughItem, 2), NaHCO3, "dustWheat", "toolMixingbowl", "listAllwater"));
		}
    }
}
