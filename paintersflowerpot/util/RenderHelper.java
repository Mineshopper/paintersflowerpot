package paintersflowerpot.util;

import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.util.Icon;

public class RenderHelper extends DrawHelper {

	/**
	 * Renders the given texture to the bottom face of the block. Args: slope, x, y, z, texture
	 */
	public static void renderFaceYNeg(RenderBlocks renderBlocks, final double x, final double y, final double z, Icon icon)
	{
        double uMin = icon.getInterpolatedU(renderBlocks.renderMinX * 16.0D);
        double uMax = icon.getInterpolatedU(renderBlocks.renderMaxX * 16.0D);
        double vMin = icon.getInterpolatedV(renderBlocks.renderMinZ * 16.0D);
        double vMax = icon.getInterpolatedV(renderBlocks.renderMaxZ * 16.0D);

        double xMin = x + renderBlocks.renderMinX;
        double xMax = x + renderBlocks.renderMaxX;
        double yMin = y + renderBlocks.renderMinY;
        double zMin = z + renderBlocks.renderMinZ;
        double zMax = z + renderBlocks.renderMaxZ;

        setupVertex(renderBlocks, xMin, yMin, zMax, uMin, vMax, 0);
        setupVertex(renderBlocks, xMin, yMin, zMin, uMin, vMin, 1);
        setupVertex(renderBlocks, xMax, yMin, zMin, uMax, vMin, 2);
        setupVertex(renderBlocks, xMax, yMin, zMax, uMax, vMax, 3);
	}

	/**
	 * Renders the given texture to the top face of the block. Args: slope, x, y, z, texture
	 */
	public static void renderFaceYPos(RenderBlocks renderBlocks, double x, double y, double z, Icon icon)
	{
        double uMin = icon.getInterpolatedU(renderBlocks.renderMinX * 16.0D);
        double uMax = icon.getInterpolatedU(renderBlocks.renderMaxX * 16.0D);
        double vMin = icon.getInterpolatedV(renderBlocks.renderMinZ * 16.0D);
        double vMax = icon.getInterpolatedV(renderBlocks.renderMaxZ * 16.0D);

        double xMin = x + renderBlocks.renderMinX;
        double xMax = x + renderBlocks.renderMaxX;
        double yMax = y + renderBlocks.renderMaxY;
        double zMin = z + renderBlocks.renderMinZ;
        double zMax = z + renderBlocks.renderMaxZ;

        setupVertex(renderBlocks, xMax, yMax, zMax, uMax, vMax, 0);
        setupVertex(renderBlocks, xMax, yMax, zMin, uMax, vMin, 1);
        setupVertex(renderBlocks, xMin, yMax, zMin, uMin, vMin, 2);
        setupVertex(renderBlocks, xMin, yMax, zMax, uMin, vMax, 3);
	}

	/**
	 * Renders the given texture to the east (z-negative) face of the block.  Args: slope, x, y, z, texture
	 */
	public static void renderFaceZNeg(RenderBlocks renderBlocks, double x, double y, double z, Icon icon)
	{
        double uMin = icon.getInterpolatedU(renderBlocks.renderMinX * 16.0D);
        double uMax = icon.getInterpolatedU(renderBlocks.renderMaxX * 16.0D);
        double vMax = icon.getInterpolatedV(16.0D - (isDrapingIcon(icon) ? 1.0D : renderBlocks.renderMaxY) * 16.0D);
        double vMin = icon.getInterpolatedV(16.0D - (isDrapingIcon(icon) ? (1.0D - (renderBlocks.renderMaxY - renderBlocks.renderMinY)) : renderBlocks.renderMinY) * 16.0D);

        // Corrects issue where some boxes have flipped or incorrectly offset textures.
        if (vMin > vMax)
        {
        	uMin = icon.getInterpolatedU((1.0D - renderBlocks.renderMaxX) * 16.0D);
        	uMax = icon.getInterpolatedU((1.0D - renderBlocks.renderMinX) * 16.0D);
        }
        
        double xMin = x + renderBlocks.renderMinX;
        double xMax = x + renderBlocks.renderMaxX;
        double yMin = y + renderBlocks.renderMinY;
        double yMax = y + renderBlocks.renderMaxY;
        double zMin = z + renderBlocks.renderMinZ;

        setupVertex(renderBlocks, xMin, yMax, zMin, uMax, vMax, 0);
        setupVertex(renderBlocks, xMax, yMax, zMin, uMin, vMax, 1);
        setupVertex(renderBlocks, xMax, yMin, zMin, uMin, vMin, 2);
        setupVertex(renderBlocks, xMin, yMin, zMin, uMax, vMin, 3);
	}

	/**
	 * Renders the given texture to the west (z-positive) face of the block.  Args: slope, x, y, z, texture
	 */
	public static void renderFaceZPos(RenderBlocks renderBlocks, double x, double y, double z, Icon icon)
	{
        double uMin = icon.getInterpolatedU(renderBlocks.renderMinX * 16.0D);
        double uMax = icon.getInterpolatedU(renderBlocks.renderMaxX * 16.0D);
        double vMax = icon.getInterpolatedV(16.0D - (isDrapingIcon(icon) ? 1.0D : renderBlocks.renderMaxY) * 16.0D);
        double vMin = icon.getInterpolatedV(16.0D - (isDrapingIcon(icon) ? (1.0D - (renderBlocks.renderMaxY - renderBlocks.renderMinY)) : renderBlocks.renderMinY) * 16.0D);

        double xMin = x + renderBlocks.renderMinX;
        double xMax = x + renderBlocks.renderMaxX;
        double yMin = y + renderBlocks.renderMinY;
        double yMax = y + renderBlocks.renderMaxY;
        double zMax = z + renderBlocks.renderMaxZ;

        setupVertex(renderBlocks, xMin, yMax, zMax, uMin, vMax, 0);
        setupVertex(renderBlocks, xMin, yMin, zMax, uMin, vMin, 1);
        setupVertex(renderBlocks, xMax, yMin, zMax, uMax, vMin, 2);
        setupVertex(renderBlocks, xMax, yMax, zMax, uMax, vMax, 3);
	}

	/**
	 * Renders the given texture to the north (x-negative) face of the block.  Args: slope, x, y, z, texture
	 */
	public static void renderFaceXNeg(RenderBlocks renderBlocks, double x, double y, double z, Icon icon)
	{
        double uMin = icon.getInterpolatedU(renderBlocks.renderMinZ * 16.0D);
        double uMax = icon.getInterpolatedU(renderBlocks.renderMaxZ * 16.0D);
        double vMax = icon.getInterpolatedV(16.0D - (isDrapingIcon(icon) ? 1.0D : renderBlocks.renderMaxY) * 16.0D);
        double vMin = icon.getInterpolatedV(16.0D - (isDrapingIcon(icon) ? (1.0D - (renderBlocks.renderMaxY - renderBlocks.renderMinY)) : renderBlocks.renderMinY) * 16.0D);

        double xMin = x + renderBlocks.renderMinX;
        double yMin = y + renderBlocks.renderMinY;
        double yMax = y + renderBlocks.renderMaxY;
        double zMin = z + renderBlocks.renderMinZ;
        double zMax = z + renderBlocks.renderMaxZ;

        setupVertex(renderBlocks, xMin, yMax, zMax, uMax, vMax, 0);
        setupVertex(renderBlocks, xMin, yMax, zMin, uMin, vMax, 1);
        setupVertex(renderBlocks, xMin, yMin, zMin, uMin, vMin, 2);
        setupVertex(renderBlocks, xMin, yMin, zMax, uMax, vMin, 3);
	}

	/**
	 * Renders the given texture to the south (x-positive) face of the block.  Args: slope, x, y, z, texture
	 */
	public static void renderFaceXPos(RenderBlocks renderBlocks, double x, double y, double z, Icon icon)
	{
        double uMin = icon.getInterpolatedU(renderBlocks.renderMinZ * 16.0D);
        double uMax = icon.getInterpolatedU(renderBlocks.renderMaxZ * 16.0D);
        double vMax = icon.getInterpolatedV(16.0D - (isDrapingIcon(icon) ? 1.0D : renderBlocks.renderMaxY) * 16.0D);
        double vMin = icon.getInterpolatedV(16.0D - (isDrapingIcon(icon) ? (1.0D - (renderBlocks.renderMaxY - renderBlocks.renderMinY)) : renderBlocks.renderMinY) * 16.0D);

        // Corrects issue where some boxes have flipped or incorrectly offset textures.
        if (vMin > vMax)
        {
        	uMin = icon.getInterpolatedU((1.0D - renderBlocks.renderMaxZ) * 16.0D);
        	uMax = icon.getInterpolatedU((1.0D - renderBlocks.renderMinZ) * 16.0D);
        }

        double xMax = x + renderBlocks.renderMaxX;
        double yMin = y + renderBlocks.renderMinY;
        double yMax = y + renderBlocks.renderMaxY;
        double zMin = z + renderBlocks.renderMinZ;
        double zMax = z + renderBlocks.renderMaxZ;

        setupVertex(renderBlocks, xMax, yMin, zMax, uMin, vMin, 0);
        setupVertex(renderBlocks, xMax, yMin, zMin, uMax, vMin, 1);
        setupVertex(renderBlocks, xMax, yMax, zMin, uMax, vMax, 2);
        setupVertex(renderBlocks, xMax, yMax, zMax, uMin, vMax, 3);
	}
}