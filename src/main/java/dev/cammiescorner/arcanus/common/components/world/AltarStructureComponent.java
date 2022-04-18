package dev.cammiescorner.arcanus.common.components.world;

import dev.cammiescorner.arcanus.Arcanus;
import dev.cammiescorner.arcanus.api.ArcanusHelper;
import dev.cammiescorner.arcanus.common.registry.ArcanusComponents;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.Structure;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class AltarStructureComponent implements AutoSyncedComponent {
	private final HashMap<BlockPos, BlockState> structureMap = new HashMap<>();
	private final World world;
	private int offsetX, offsetZ;
	
	public AltarStructureComponent(World world) {
		this.world = world;
	}
	
	@Override
	public void readFromNbt(NbtCompound tag) {
		NbtList nbtList = tag.getList("StructureMap", NbtElement.COMPOUND_TYPE);

		structureMap.clear();
		for(int i = 0; i < nbtList.size(); i++) {
			NbtCompound map = nbtList.getCompound(i);
			structureMap.put(
					NbtHelper.toBlockPos(map.getCompound("BlockPos")),
					NbtHelper.toBlockState(map.getCompound("BlockState"))
			);
		}

		offsetX = tag.getInt("OffsetX");
		offsetZ = tag.getInt("OffsetZ");
	}

	@Override
	public void writeToNbt(NbtCompound tag) {
		NbtList nbtList = new NbtList();

		structureMap.forEach((pos, state) -> {
			NbtCompound map = new NbtCompound();
			map.put("BlockPos", NbtHelper.fromBlockPos(pos));
			map.put("BlockState", NbtHelper.fromBlockState(state));
			nbtList.add(map);
		});

		tag.put("StructureMap", nbtList);
		tag.putInt("OffsetX", offsetX);
		tag.putInt("OffsetZ", offsetZ);
	}

	public HashMap<BlockPos, BlockState> getStructureMap() {
		return structureMap;
	}

	public BlockPos getOffset() {
		return new BlockPos(-offsetX, 0, -offsetZ);
	}

	public void constructStructureMap() {
		if(world instanceof ServerWorld serverWorld) {
			structureMap.clear();
			Optional<Structure> optional = serverWorld.getStructureManager().getStructure(Arcanus.id("altar"));
			BlockPos pos = BlockPos.ORIGIN;

			if(optional.isPresent()) {
				Structure structure = optional.get();
				StructurePlacementData placementData = new StructurePlacementData();

				List<Structure.StructureBlockInfo> randInfoList = placementData.getRandomBlockInfos(structure.blockInfoLists, pos).getAll();
				List<Structure.StructureBlockInfo> infoList = Structure.process(serverWorld, pos, pos, placementData, randInfoList);

				offsetX = (int) Math.floor(structure.getSize().getX() / 2D);
				offsetZ = (int) Math.floor(structure.getSize().getZ() / 2D);

				for(Structure.StructureBlockInfo info : infoList)
					if(ArcanusHelper.isValidAltarBlock(info.state))
						structureMap.put(info.pos.subtract(pos), info.state);

				ArcanusComponents.ALTAR_STRUCTURE_COMPONENT.sync(world);
			}
		}
	}
}
