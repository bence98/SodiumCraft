package csokicraft.forge.sodiumcraft;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.*;
import net.minecraft.item.*;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.*;
import net.minecraftforge.registries.IForgeRegistry;

import static csokicraft.forge.sodiumcraft.SodiumItems.*;

import java.util.*;

import com.pam.harvestcraft.item.ItemRegistry;

import cofh.core.util.helpers.ItemHelper;
import cofh.thermalexpansion.util.managers.machine.SmelterManager;
import cofh.thermalfoundation.init.TFItems;
import csokicraft.forge.sodiumcraft.battery.ItemSodiumPotatoBattery;
import csokicraft.forge.sodiumcraft.battery.ItemSodiumSilverBattery;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import mekanism.api.gas.*;

@Mod(modid = SodiumCraft.MODID, version = SodiumCraft.VERSION, dependencies="required-after:mekanism@[1.12.2-9.4,);required-after:thermalexpansion@[5.3,)")
@EventBusSubscriber
public class SodiumCraft
{
	public static final String MODID = "sodiumcraft";
	public static final String VERSION = "1.3.2";

	@SidedProxy(serverSide="csokicraft.forge.sodiumcraft.CommonProxy", clientSide="csokicraft.forge.sodiumcraft.ClientProxy")
	public static CommonProxy proxy;
	public static Item itemSodium = new ItemSodium().setCreativeTab(CreativeTabs.MATERIALS).setRegistryName("sodium");
	public static Item itemPotatoBattery=new ItemSodiumPotatoBattery().setRegistryName("potato_battery").setCreativeTab(CreativeTabs.TOOLS);
	public static Item itemSilverBattery=new ItemSodiumSilverBattery().setRegistryName("silver_battery").setCreativeTab(CreativeTabs.TOOLS);

	@EventHandler
	public void preinit(FMLPreInitializationEvent e){
		
	}

	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		NBTTagCompound imcTag;
		
		//Sodium crystallization
		imcTag = new NBTTagCompound();
		imcTag.setTag("input", new GasStack(GasRegistry.getGas("sodium"), 100).write(new NBTTagCompound()));
		imcTag.setTag("output", Na.writeToNBT(new NBTTagCompound()));
		FMLInterModComms.sendMessage("mekanism", "ChemicalCrystallizerRecipe", imcTag);
		
		//Sodium hydration
		imcTag = new NBTTagCompound();
		imcTag.setTag("input", Na.writeToNBT(new NBTTagCompound()));
		imcTag.setTag("gasType", new GasStack(GasRegistry.getGas("water"), 100).write(new NBTTagCompound()));
		imcTag.setTag("output", NaOH.writeToNBT(new NBTTagCompound()));
		FMLInterModComms.sendMessage("mekanism", "ChemicalInjectionChamberRecipe", imcTag);
		
		//wood steaming
		for(int i=0;i<4;i++){
			imcTag = new NBTTagCompound();
			imcTag.setTag("input", new ItemStack(Blocks.LOG, 1, i).writeToNBT(new NBTTagCompound()));
			imcTag.setTag("gasType", new GasStack(GasRegistry.getGas("water"), 100).write(new NBTTagCompound()));
			imcTag.setTag("output", aqWood.writeToNBT(new NBTTagCompound()));
			FMLInterModComms.sendMessage("mekanism", "ChemicalInjectionChamberRecipe", imcTag);
		}
		
		for(int i=0;i<2;i++){
			imcTag = new NBTTagCompound();
			imcTag.setTag("input", new ItemStack(Blocks.LOG2, 1, i).writeToNBT(new NBTTagCompound()));
			imcTag.setTag("gasType", new GasStack(GasRegistry.getGas("water"), 100).write(new NBTTagCompound()));
			imcTag.setTag("output", aqWood.writeToNBT(new NBTTagCompound()));
			FMLInterModComms.sendMessage("mekanism", "ChemicalInjectionChamberRecipe", imcTag);
		}
		
		//Sodium bicarbonate
		imcTag = new NBTTagCompound();
		imcTag.setTag("input", Na2CO3.writeToNBT(new NBTTagCompound()));
		imcTag.setTag("gasType", new GasStack(GasRegistry.getGas("water"), 100).write(new NBTTagCompound()));
		imcTag.setTag("output", NaHCO3.writeToNBT(new NBTTagCompound()));
		FMLInterModComms.sendMessage("mekanism", "ChemicalInjectionChamberRecipe", imcTag);
		
		//Nitric acid production
		imcTag = new NBTTagCompound();
		imcTag.setTag("input", PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.WATER).writeToNBT(new NBTTagCompound()));
		imcTag.setTag("output", HNO3.writeToNBT(new NBTTagCompound()));
		FMLInterModComms.sendMessage("mekanism", "EnrichmentChamberRecipe", imcTag);
		
		//Sodium nitrate oredict
		OreDictionary.registerOre("dustSaltpeter", NaNO3);
		
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
	}
	
	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> evt){
		IForgeRegistry<Item> reg=evt.getRegistry();
		reg.register(itemSodium);
		reg.register(itemPotatoBattery);
		reg.register(itemSilverBattery);
	}
	
	@SubscribeEvent
	public static void registerRecipes(RegistryEvent.Register<IRecipe> evt){
		IForgeRegistry<IRecipe> reg=evt.getRegistry();
		ResourceLocation locSilverBattery=new ResourceLocation(MODID, "battery_silver");
		ShapedOreRecipe recSilverBattery1=new ShapedOreRecipe(locSilverBattery,
				new ItemStack(itemSilverBattery),
				"isa", "isa", "ttt",
				'i', "ingotSilver",
				's', NaOH,
				'a', "ingotAluminum",
				't', "stone");
		recSilverBattery1.setRegistryName(new ResourceLocation(MODID, "battery_silver_aluminum"));
		recSilverBattery1.setMirrored(true);
		reg.register(recSilverBattery1);
		
		ShapedOreRecipe recSilverBattery2=new ShapedOreRecipe(locSilverBattery,
				new ItemStack(itemSilverBattery),
				"isa", "isa", "ttt",
				'i', "ingotSilver",
				's', NaOH,
				'a', "ingotAluminium",
				't', "stone");
		recSilverBattery2.setRegistryName(new ResourceLocation(MODID, "battery_silver_aluminium"));
		recSilverBattery2.setMirrored(true);
		reg.register(recSilverBattery2);
		
		if(Loader.isModLoaded("harvestcraft")){
			
			ShapelessOreRecipe recFoodFlour=new ShapelessOreRecipe(new ResourceLocation(MODID, "dough_foodflour"),
					new ItemStack(ItemRegistry.doughItem, 2),
					NaHCO3, "listAllwater", "toolMixingbowl", "foodFlour");
			recFoodFlour.setRegistryName(recFoodFlour.getGroup());
			reg.register(recFoodFlour);

			ShapelessOreRecipe recDustWheat=new ShapelessOreRecipe(new ResourceLocation(MODID, "dough_dustwheat"),
					new ItemStack(ItemRegistry.doughItem, 2),
					NaHCO3, "listAllwater", "toolMixingbowl", "dustWheat");
			recDustWheat.setRegistryName(recDustWheat.getGroup());
			reg.register(recDustWheat);
		}
	}
	
	@SubscribeEvent
	public static void loadModels(ModelRegistryEvent evt){
		proxy.registerModels();
	}
}
