package csokicraft.forge17.sodiumcraft;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.block.model.*;

public class ClientProxy extends CommonProxy{
	
	@Override
	public void registerModels(){
		ItemModelMesher imm=Minecraft.getMinecraft().getRenderItem().getItemModelMesher();
		for(int meta=0;meta<ItemSodium.META_COUNT;meta++){
			ModelResourceLocation res_loc=new ModelResourceLocation(SodiumCraft.MODID+":itemSodium."+meta, "inventory");
			imm.register(SodiumCraft.itemSodium, meta, res_loc);
			ModelBakery.registerItemVariants(SodiumCraft.itemSodium, res_loc);
		}
	}
}
