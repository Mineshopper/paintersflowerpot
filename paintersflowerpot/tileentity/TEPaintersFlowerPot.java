package paintersflowerpot.tileentity;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;

public class TEPaintersFlowerPot extends TileEntity
{
	public byte potDyeColor = -1;
	public byte potSpecialColor = -1;
	public short potCover = (short) Block.flowerPot.blockID;
	public byte potCoverMetadata = 0;
	public short potSoil = 0;
	public byte potSoilMetadata = 0;
	public short potPlant = 0;
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);		
		potDyeColor = nbt.getByte("potDyeColor");
		potSpecialColor = nbt.getByte("potSpecialColor");
		potCover = nbt.getShort("potCover");
		potCoverMetadata = nbt.getByte("potCoverMetadata");
		potSoil = nbt.getShort("potSoil");
		potSoilMetadata = nbt.getByte("potSoilMetadata");
		potPlant = nbt.getShort("potPlant");
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		nbt.setByte("potDyeColor", potDyeColor);
		nbt.setByte("potSpecialColor", potSpecialColor);
		nbt.setShort("potCover", potCover);
		nbt.setByte("potCoverMetadata", potCoverMetadata);
		nbt.setShort("potSoil", potSoil);
		nbt.setByte("potSoilMetadata", potSoilMetadata);
		nbt.setShort("potPlant", potPlant);
	}
	
    /**
     * Overridden in a sign to provide the text.
     */
    @Override
	public Packet getDescriptionPacket()
    {    	
        NBTTagCompound nbt = new NBTTagCompound();
        this.writeToNBT(nbt);
        return new Packet132TileEntityData(this.xCoord, this.yCoord, this.zCoord, 1, nbt);
    }
    
    /**
     * Called when you receive a TileEntityData packet for the location this
     * TileEntity is currently in. On the client, the NetworkManager will always
     * be the remote server. On the server, it will be whomever is responsible for 
     * sending the packet.
     * 
     * @param net The NetworkManager the packet originated from 
     * @param pkt The data packet
     */
    @Override
	public void onDataPacket(INetworkManager net, Packet132TileEntityData pkt)
    {
    	readFromNBT(pkt.customParam1);

		if (this.worldObj.isRemote)
		{
			Minecraft.getMinecraft().renderGlobal.markBlockForRenderUpdate(this.xCoord, this.yCoord, this.zCoord);
			this.worldObj.updateAllLightTypes(this.xCoord, this.yCoord, this.zCoord);
		}
    }
}
