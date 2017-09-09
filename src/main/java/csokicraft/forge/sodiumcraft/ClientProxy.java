package csokicraft.forge.sodiumcraft;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraftforge.client.model.ModelLoader;

public class ClientProxy extends CommonProxy{
	
	@Override
	public void registerModels(){
		//ItemModelMesher imm=Minecraft.getMinecraft().getRenderItem().getItemModelMesher();
		for(int meta=0;meta<ItemSodium.META_COUNT;meta++){
			ModelResourceLocation res_loc=new ModelResourceLocation(SodiumCraft.MODID+":sodium."+meta, "inventory");
			//imm.register(SodiumCraft.itemSodium, meta, res_loc);
			ModelLoader.setCustomModelResourceLocation(SodiumCraft.itemSodium, meta, res_loc);
		}
	}
}
