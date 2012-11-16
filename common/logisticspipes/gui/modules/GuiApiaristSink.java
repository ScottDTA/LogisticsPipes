package logisticspipes.gui.modules;

import logisticspipes.modules.ModuleApiaristSink;
import logisticspipes.modules.ModuleApiaristSink.FilterType;
import logisticspipes.modules.ModuleApiaristSink.SinkSetting;
import logisticspipes.network.GuiIDs;
import logisticspipes.network.NetworkConstants;
import logisticspipes.network.packets.PacketPipeBeePacket;
import logisticspipes.proxy.MainProxy;
import logisticspipes.proxy.SimpleServiceLocator;
import logisticspipes.utils.gui.BasicGuiHelper;
import logisticspipes.utils.gui.DummyContainer;
import logisticspipes.utils.gui.IItemTextureRenderSlot;
import logisticspipes.utils.gui.ISmallColorRenderSlot;
import net.minecraft.client.Minecraft;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.GuiScreen;
import buildcraft.transport.Pipe;

public class GuiApiaristSink extends GuiWithPreviousGuiContainer {

	private final ModuleApiaristSink module;
	private final EntityPlayer player;
	private int slot;
	
	public GuiApiaristSink(ModuleApiaristSink module, EntityPlayer player, Pipe pipe, GuiScreen previousGui, int slot) {
		super(new DummyContainer(player.inventory,null), pipe, previousGui);
		this.module = module;
		this.player = player;
		this.slot = slot;
		for(int i=0; i < 6; i++) {
			SinkSetting filter = module.filter[i];
			this.addRenderSlot(new TypeSlot(20, 20 + (i*18), filter, i, this));
			this.addRenderSlot(new GroupSlot(guiLeft + 45, guiTop + 25 + (i*18), filter, i, this));
			this.addRenderSlot(new BeeSlot(60, 20 + (i*18),filter,0, i, this));
			this.addRenderSlot(new BeeSlot(78, 20 + (i*18),filter,1, i, this));
		}
		xSize = 120;
		ySize = 150;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3) {
		BasicGuiHelper.drawGuiBackGround(mc, guiLeft, guiTop, right, bottom, zLevel, true);
	}
	
	@Override
	public int getGuiID() {
		return GuiIDs.GUI_Module_Apiarist_Sink_ID;
	}

	private class TypeSlot extends IItemTextureRenderSlot {

		final private int xPos;
		final private int yPos;
		final private SinkSetting setting;
		final private int row;
		final private GuiApiaristSink gui;
		
		private TypeSlot(int xPos, int yPos, SinkSetting setting, int row, GuiApiaristSink guiApiaristSink) {
			this.xPos = xPos;
			this.yPos = yPos;
			this.setting = setting;
			this.row = row;
			this.gui = guiApiaristSink;
		}
		
		@Override
		public int getTextureId() {
			if(setting.filterType == null) return 0;
			return setting.filterType.icon;
		}

		@Override
		public String getTextureFile() {
			return "/gfx/forestry/gui/analyzer_icons.png";
		}

		@Override
		public void mouseClicked(int button) {
			if(button == 2) {
				setting.FilterTypeReset();
			}
			if(button == 0) {
				setting.FilterTypeUp();
			}
			if(button == 1) {
				setting.FilterTypeDown();
			}
			PacketPipeBeePacket packet = new PacketPipeBeePacket(NetworkConstants.BEE_MODULE_SET_BEE, pipe.xCoord, pipe.yCoord, pipe.zCoord, gui.slot, row, 3, setting.filterType.ordinal());
			MainProxy.sendPacketToServer(packet.getPacket());
		}

		@Override
		public boolean drawSlotBackground() {
			return true;
		}

		@Override
		public int getXPos() {
			return xPos;
		}

		@Override
		public int getYPos() {
			return yPos;
		}

		@Override
		public boolean drawSlotIcon() {
			return true;
		}

		@Override
		public String getToolTipText() {
			if(setting.filterType == null) return "";
			return SimpleServiceLocator.forestryProxy.getForestryTranslation(setting.filterType.path);
		}

		@Override
		public boolean displayToolTip() {
			return setting.filterType != FilterType.Null;
		}

		@Override
		public boolean customRender(Minecraft mc, float zLevel) {
			return false;
		}
		
	}
	private class GroupSlot extends ISmallColorRenderSlot {

		final private int xPos;
		final private int yPos;
		final private SinkSetting setting;
		final private int row;
		final private GuiApiaristSink gui;

		private GroupSlot(int xPos, int yPos, SinkSetting setting, int row, GuiApiaristSink guiApiaristSink) {
			this.xPos = xPos;
			this.yPos = yPos;
			this.setting = setting;
			this.row = row;
			this.gui = guiApiaristSink;
		}
		
		@Override
		public void mouseClicked(int button) {
			if(button == 2) {
				setting.filterGroupReset();
			}
			if(button == 0) {
				setting.filterGroupUp();
			}
			if(button == 1) {
				setting.filterGroupDown();
			}
			PacketPipeBeePacket packet = new PacketPipeBeePacket(NetworkConstants.BEE_MODULE_SET_BEE, pipe.xCoord, pipe.yCoord, pipe.zCoord, gui.slot, row, 2, setting.filterGroup);
			MainProxy.sendPacketToServer(packet.getPacket());
		}

		@Override
		public boolean drawSlotBackground() {
			return setting.filterType != FilterType.Null;
		}

		@Override
		public int getXPos() {
			return xPos;
		}

		@Override
		public int getYPos() {
			return yPos;
		}

		@Override
		public String getToolTipText() {
			switch(setting.filterGroup) {
			case 1:
				return "GroupColor: Red";
			case 2:
				return "GroupColor: Green";
			case 3:
				return "GroupColor: Blue";
			case 4:
				return "GroupColor: Yellow";
			case 5:
				return "GroupColor: Cyan";
			case 6:
				return "GroupColor: Purple";
			default:
				return "No Group";
			}
		}

		@Override
		public boolean displayToolTip() {
			return drawSlotBackground();
		}

		@Override
		public int getColor() {
			switch(setting.filterGroup) {
			case 1:
				return 0xFFFF0000;
			case 2:
				return 0xFF00FF00;
			case 3:
				return 0xFF0000FF;
			case 4:
				return 0xFFFFFF00;
			case 5:
				return 0xFF00FFFF;
			case 6:
				return 0xFFFF00FF;
			default:
				return 0;
			}
		}

		@Override
		public boolean drawColor() {
			return drawSlotBackground();
		}
		
	}
	private class BeeSlot extends IItemTextureRenderSlot {

		final private int xPos;
		final private int yPos;
		final private SinkSetting setting;
		final private int slotNumber;
		final private int row;
		final private GuiApiaristSink gui;
		
		private BeeSlot(int xPos, int yPos, SinkSetting setting, int slotNumber, int row, GuiApiaristSink guiApiaristSink) {
			this.xPos = xPos;
			this.yPos = yPos;
			this.setting = setting;
			this.slotNumber = slotNumber;
			this.row = row;
			this.gui = guiApiaristSink;
		}
		
		@Override
		public int getTextureId() {
			return 0;
		}

		@Override
		public String getTextureFile() {
			return "/gfx/forestry/items/bees.png";
		}

		@Override
		public void mouseClicked(int button) {
			if(button == 2) {
				if(slotNumber == 0) {
					setting.firstBeeReset();
				} else {
					setting.secondBeeReset();
				}
			}
			if(button == 0) {
				if(slotNumber == 0) {
					setting.firstBeeUp();
				} else {
					setting.secondBeeUp();
				}
			}
			if(button == 1) {
				if(slotNumber == 0) {
					setting.firstBeeDown();
				} else {
					setting.secondBeeDown();
				}
			}
			PacketPipeBeePacket packet = new PacketPipeBeePacket(NetworkConstants.BEE_MODULE_SET_BEE, pipe.xCoord, pipe.yCoord, pipe.zCoord, gui.slot, row, slotNumber, slotNumber == 0 ? setting.firstBee : setting.secondBee);
			MainProxy.sendPacketToServer(packet.getPacket());
		}

		@Override
		public boolean drawSlotBackground() {
			return setting.filterType.secondSlots > slotNumber;
		}

		@Override
		public int getXPos() {
			return xPos;
		}

		@Override
		public int getYPos() {
			return yPos;
		}

		@Override
		public boolean drawSlotIcon() {
			return drawSlotBackground() && (slotNumber == 0 ? setting.firstBee : setting.secondBee) != "";
		}

		@Override
		public String getToolTipText() {
			return SimpleServiceLocator.forestryProxy.getAlleleName(slotNumber == 0 ? setting.firstBee : setting.secondBee);
		}

		@Override
		public boolean displayToolTip() {
			if(slotNumber == 0) {
				return setting.firstBee != "" && drawSlotBackground();
			} else {
				return setting.secondBee != "" && drawSlotBackground();
			}
		}

		@Override
		public boolean customRender(Minecraft mc, float zLevel) {
			if(slotNumber == 0) {
				BasicGuiHelper.renderForestryBeeAt(mc, xPos + 1, yPos + 1, zLevel, setting.firstBee);
			} else {
				BasicGuiHelper.renderForestryBeeAt(mc, xPos + 1, yPos + 1, zLevel, setting.secondBee);
			}
			return true;
		}
		
	}
}
