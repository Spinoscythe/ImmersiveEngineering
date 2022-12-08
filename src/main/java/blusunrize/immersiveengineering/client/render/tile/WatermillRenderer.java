/*
 * BluSunrize
 * Copyright (c) 2017
 *
 * This code is licensed under "Blu's License of Common Sense"
 * Details can be found in the license file in the root folder of this project
 */

package blusunrize.immersiveengineering.client.render.tile;

import blusunrize.immersiveengineering.api.ApiUtils;
import blusunrize.immersiveengineering.api.IEProperties;
import blusunrize.immersiveengineering.api.client.IVertexBufferHolder;
import blusunrize.immersiveengineering.api.utils.SafeChunkUtils;
import blusunrize.immersiveengineering.common.blocks.wooden.WatermillBlockEntity;
import blusunrize.immersiveengineering.common.register.IEBlocks.WoodenDevices;
import com.mojang.blaze3d.vertex.PoseStack;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelData;

public class WatermillRenderer extends IEBlockEntityRenderer<WatermillBlockEntity>
{
	public static final String NAME = "watermill";
	public static DynamicModel MODEL;
	private static final IVertexBufferHolder MODEL_BUFFER = IVertexBufferHolder.create(() -> {
		BlockState state = WoodenDevices.WATERMILL.defaultBlockState()
				.setValue(IEProperties.FACING_HORIZONTAL, Direction.NORTH);
		return MODEL.get().getQuads(state, null, ApiUtils.RANDOM_SOURCE, ModelData.EMPTY, null);
	});

	@Override
	public void render(WatermillBlockEntity tile, float partialTicks, PoseStack transform, MultiBufferSource bufferIn,
					   int combinedLightIn, int combinedOverlayIn)
	{
		if(!SafeChunkUtils.isChunkSafe(tile.getLevelNonnull(), tile.getBlockPos()))
			return;
		transform.pushPose();
		transform.translate(.5, .5, .5);
		final float dir = (tile.getFacing().toYRot()+180)%180;
		float wheelRotation = 360*(tile.rotation+partialTicks*(float)tile.perTick);
		transform.mulPose(new Quaternionf(new Vector3f(0, 1, 0), dir, true));
		transform.mulPose(new Quaternionf(new Vector3f(0, 0, 1), wheelRotation, true));
		transform.translate(-.5, -.5, -.5);
		MODEL_BUFFER.render(RenderType.cutoutMipped(), combinedLightIn, combinedOverlayIn, bufferIn, transform);
		transform.popPose();
	}

	public static void reset()
	{
		MODEL_BUFFER.reset();
	}
}