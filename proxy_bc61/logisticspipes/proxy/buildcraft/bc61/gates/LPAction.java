/**
 * Copyright (c) SpaceToad, 2011
 * http://www.mod-buildcraft.com
 *
 * BuildCraft is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */

package logisticspipes.proxy.buildcraft.bc61.gates;


import logisticspipes.textures.Textures;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import buildcraft.api.gates.IAction;
import buildcraft.api.gates.IStatementParameter;
import buildcraft.api.gates.StatementManager;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract  class LPAction implements IAction {

	protected final String uniqueTag;

	public LPAction(String uniqueTag) {
		this.uniqueTag = uniqueTag;
		StatementManager.registerStatement(this);
	}

	@Override
	public IStatementParameter createParameter(int arg0) {
		return null;
	}

	@Override
	public int maxParameters() {
		return 0;
	}

	@Override
	public int minParameters() {
		return 0;
	}

	@Override
	public String getDescription() {
		return "";
	}
	
	@Override
	public void registerIcons(IIconRegister iconRegister) {
		Textures.LPactionIconProvider.registerIcons(iconRegister);
	}

	@Override
	public String getUniqueTag() {
		return this.uniqueTag;
	}

	public int getIconIndex() {
		return 0;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon() {
		return Textures.LPactionIconProvider.getIcon(getIconIndex());
	}

	@Override
	public buildcraft.api.gates.IAction rotateLeft() {
		return this;
	}
}
