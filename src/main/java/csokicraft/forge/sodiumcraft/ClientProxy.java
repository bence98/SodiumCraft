package csokicraft.forge.sodiumcraft;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraftforge.client.model.ModelLoader;

public class ClientProxy extends CommonProxy{
	
	@Override
	public void registerModels(){
		for(int meta=0;meta<SodiumCraft.itemSodium.META_COUNT;meta++){
			ModelResourceLocation res_loc=new ModelResourceLocation(SodiumCraft.MODID+":sodium."+meta, "inventory");
			ModelLoader.setCustomModelResourceLocation(SodiumCraft.itemSodium, meta, res_loc);
		}
		
		for(int meta=0;meta<SodiumCraft.itemNaFood.META_COUNT;meta++){
			ModelResourceLocation res_loc=new ModelResourceLocation(SodiumCraft.MODID+":food."+meta, "inventory");
			ModelLoader.setCustomModelResourceLocation(SodiumCraft.itemNaFood, meta, res_loc);
		}
		
		ModelResourceLocation res_loc=new ModelResourceLocation(SodiumCraft.MODID+":potato_battery", "inventory");
		ModelLoader.setCustomModelResourceLocation(SodiumCraft.itemPotatoBattery, 0, res_loc);
		res_loc=new ModelResourceLocation(SodiumCraft.MODID+":silver_battery", "inventory");
		ModelLoader.setCustomModelResourceLocation(SodiumCraft.itemSilverBattery, 0, res_loc);
	}
}
