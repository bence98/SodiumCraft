package csokicraft.forge.sodiumcraft;

import static csokicraft.forge.sodiumcraft.SodiumCraft.MODID;

import csokicraft.util.FormatterUtil;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.item.*;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.*;
import net.minecraftforge.registries.IForgeRegistry;

public class SodiumItems{
	public static ItemStack Na    =new ItemStack(SodiumCraft.itemSodium, 1, 0),
							NaOH  =new ItemStack(SodiumCraft.itemSodium, 1, 1),
							NaS   =new ItemStack(SodiumCraft.itemSodium, 1, 2),
							NaHCO3=new ItemStack(SodiumCraft.itemSodium, 1, 3),
							Na2CO3=new ItemStack(SodiumCraft.itemSodium, 1, 4),
							aqWood=new ItemStack(SodiumCraft.itemSodium, 1, 5),
							NaNO3 =new ItemStack(SodiumCraft.itemSodium, 1, 6),
							HNO3  =new ItemStack(SodiumCraft.itemSodium, 1, 7),
							AlOH3 =new ItemStack(SodiumCraft.itemSodium, 1, 8),
							Al_SO4=new ItemStack(SodiumCraft.itemSodium, 1, 9),
							firePE=new ItemStack(SodiumCraft.itemSodium, 1, 10);

	public static void addConcreteRecipes(IForgeRegistry<IRecipe> reg){
		for(EnumDyeColor color:EnumDyeColor.values()){
			ItemStack concrete =new ItemStack(Blocks.CONCRETE_POWDER, 8, color.getMetadata());
			ShapelessOreRecipe rec=new ShapelessOreRecipe(new ResourceLocation(MODID, "concrete"),
					concrete,
					Blocks.GRAVEL, Blocks.SAND, Al_SO4, "dye"+FormatterUtil.capitalise(color.getUnlocalizedName()));
			rec.setRegistryName(new ResourceLocation(MODID, "concrete_"+color.getName()));
			reg.register(rec);
		}
	}
	
	public static void addFiremanSuitRecipes(IForgeRegistry<IRecipe> reg){
		ItemStack helmStack=new ItemStack(Items.IRON_HELMET);
		helmStack.addEnchantment(Enchantments.FIRE_PROTECTION, 5);
		helmStack.addEnchantment(Enchantments.UNBREAKING, 3);
		helmStack.setStackDisplayName("§rFireman's helmet");
		ShapedOreRecipe recHelm=new ShapedOreRecipe(new ResourceLocation(MODID, "fireman_helm"),
				helmStack,
				"...", ".*.",
				'.', firePE,
				'*', Items.IRON_HELMET);
		recHelm.setRegistryName(recHelm.getGroup());
		reg.register(recHelm);

		ItemStack chestStack=new ItemStack(Items.IRON_CHESTPLATE);
		chestStack.addEnchantment(Enchantments.FIRE_PROTECTION, 5);
		chestStack.addEnchantment(Enchantments.UNBREAKING, 3);
		chestStack.setStackDisplayName("§rFireman's vest");
		ShapedOreRecipe recChest=new ShapedOreRecipe(new ResourceLocation(MODID, "fireman_chest"),
				chestStack,
				".*.","...", "...",
				'.', firePE,
				'*', Items.IRON_CHESTPLATE);
		recChest.setRegistryName(recChest.getGroup());
		reg.register(recChest);

		ItemStack legsStack=new ItemStack(Items.IRON_LEGGINGS);
		legsStack.addEnchantment(Enchantments.FIRE_PROTECTION, 5);
		legsStack.addEnchantment(Enchantments.UNBREAKING, 3);
		legsStack.setStackDisplayName("§rFireman's leggings");
		ShapedOreRecipe recLegs=new ShapedOreRecipe(new ResourceLocation(MODID, "fireman_legs"),
				legsStack,
				"...", ". .", ".*.",
				'.', firePE,
				'*', Items.IRON_LEGGINGS);
		recLegs.setRegistryName(recLegs.getGroup());
		reg.register(recLegs);

		ItemStack bootStack=new ItemStack(Items.IRON_BOOTS);
		bootStack.addEnchantment(Enchantments.FIRE_PROTECTION, 5);
		bootStack.addEnchantment(Enchantments.UNBREAKING, 3);
		bootStack.setStackDisplayName("§rFireman's boots");
		ShapedOreRecipe recBoot=new ShapedOreRecipe(new ResourceLocation(MODID, "fireman_boots"),
				bootStack,
				". .", ".*.",
				'.', firePE,
				'*', Items.IRON_BOOTS);
		recBoot.setRegistryName(recBoot.getGroup());
		reg.register(recBoot);
	}
}
