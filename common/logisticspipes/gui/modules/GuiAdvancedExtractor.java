/** 
 * Copyright (c) Krapht, 2011
 * 
 * "LogisticsPipes" is distributed under the terms of the Minecraft Mod Public 
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */

package logisticspipes.gui.modules;

import logisticspipes.modules.ModuleAdvancedExtractor;
import logisticspipes.network.GuiIDs;
import logisticspipes.network.NetworkConstants;
import logisticspipes.network.packets.PacketPipeInteger;
import logisticspipes.proxy.MainProxy;
import logisticspipes.utils.gui.DummyContainer;
import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.IInventory;

import org.lwjgl.opengl.GL11;

import buildcraft.transport.Pipe;

public class GuiAdvancedExtractor extends GuiWithPreviousGuiContainer {

	private final IInventory _playerInventory;
	private final ModuleAdvancedExtractor _advancedExtractor;
	private final int slot;
	
	@Override
	public void initGui() {
		super.initGui();
       //Default item toggle:
       controlList.clear();
       controlList.add(new GuiButton(0, width / 2 + 20, height / 2 - 34, 60, 20, _advancedExtractor.areItemsIncluded() ? "Included" : "Excluded"));
       if(_advancedExtractor.connectedToSidedInventory()) {
    	   controlList.add(new GuiButton(1, width / 2 - 25, height / 2 - 34, 40, 20, "Sneaky"));
       }
	}
	
	@Override
	protected void actionPerformed(GuiButton guibutton) {
		switch(guibutton.id)
		{
			case 0:
				_advancedExtractor.setItemsIncluded(!_advancedExtractor.areItemsIncluded());
				((GuiButton)controlList.get(0)).displayString = _advancedExtractor.areItemsIncluded() ? "Included" : "Excluded";
				MainProxy.sendPacketToServer(new PacketPipeInteger(NetworkConstants.ADVANCED_EXTRACTOR_MODULE_INCLUDED_SET, pipe.xCoord, pipe.yCoord, pipe.zCoord, (_advancedExtractor.areItemsIncluded() ? 1 : 0) + (slot * 10)).getPacket());
				break;
			case 1:
				if(!_advancedExtractor.connectedToSidedInventory()) {
					controlList.remove(1);
				}
				MainProxy.sendPacketToServer(new PacketPipeInteger(NetworkConstants.ADVANCED_EXTRACTOR_MODULE_SNEAKY_GUI, pipe.xCoord, pipe.yCoord, pipe.zCoord, slot).getPacket());
				break;
		}
		
	}
	
	public GuiAdvancedExtractor(IInventory playerInventory, Pipe pipe, ModuleAdvancedExtractor advancedExtractor, GuiScreen previousGui, int slot) {
		super(null,pipe,previousGui);
		_advancedExtractor = advancedExtractor;
		this.slot = slot;
		DummyContainer dummy = new DummyContainer(playerInventory, _advancedExtractor.getFilterInventory());
		dummy.addNormalSlotsForPlayerInventory(8, 60);

		//Pipe slots
	    for(int pipeSlot = 0; pipeSlot < 9; pipeSlot++){
	    	dummy.addDummySlot(pipeSlot, 8 + pipeSlot * 18, 18);
	    }
	    
	    this.inventorySlots = dummy;
		this._playerInventory = playerInventory;
		xSize = 175;
		ySize = 142;
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2) {
		fontRenderer.drawString(_advancedExtractor.getFilterInventory().getInvName(), 8, 6, 0x404040);
		fontRenderer.drawString("Inventory", 8, ySize - 92, 0x404040);
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
		int i = mc.renderEngine.getTexture("/logisticspipes/gui/itemsink.png");
				
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.renderEngine.bindTexture(i);
		int j = guiLeft;
		int k = guiTop;
		drawTexturedModalRect(j, k, 0, 0, xSize, ySize);
	}

	@Override
	public int getGuiID() {
		return GuiIDs.GUI_Module_Advanced_Extractor_ID + (slot * 100);
	}
}
