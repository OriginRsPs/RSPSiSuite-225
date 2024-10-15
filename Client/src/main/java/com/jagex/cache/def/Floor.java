package com.jagex.cache.def;

public class Floor {

	private final static int hsl24to16(int h, int s, int l) {
		if (l > 179) {
			s /= 2;
		}
		if (l > 192) {
			s /= 2;
		}
		if (l > 217) {
			s /= 2;
		}
		if (l > 243) {
			s /= 2;
		}
		return (h / 4 << 10) + (s / 32 << 7) + l / 2;
	}

	private int texture;
	private int rgb;

	private boolean shadowed;
	private int anotherRgb;
	private int hue;

	private int saturation;
	private int luminance;
	private int anotherHue;

	private int anotherSaturation;
	private int anotherLuminance;
	private int weightedHue;

	private int chroma;

	private int colour;

	public Floor() {
		texture = -1;
		shadowed = true;
	}

	public void generateHsl() {
		if (anotherRgb != -1) {
			rgbToHsl(anotherRgb);
			anotherHue = hue;
			anotherSaturation = saturation;
			anotherLuminance = luminance;
		}
		rgbToHsl(rgb);
	}

	public int getAnotherHue() {
		return anotherHue;
	}

	public int getAnotherLuminance() {
		return anotherLuminance;
	}

	public int getAnotherRgb() {
		return anotherRgb;
	}

	public int getAnotherSaturation() {
		return anotherSaturation;
	}

	public int getChroma() {
		return chroma;
	}

	public int getColour() {
		return colour;
	}

	public int getHue() {
		return hue;
	}

	public int getLuminance() {
		return luminance;
	}

	public int getRgb() {
		return rgb;
	}

	public int getSaturation() {
		return saturation;
	}

	public int getTexture() {
		return texture;
	}

	public int getWeightedHue() {
		return weightedHue;
	}

	public boolean isShadowed() {
		return shadowed;
	}

	public void calculateHsl()
	{
		int var1 = colour;
		double var2 = (double) (var1 >> 16 & 255) / 256.0D;
		double var4 = (double) (var1 >> 8 & 255) / 256.0D;
		double var6 = (double) (var1 & 255) / 256.0D;
		double var8 = var2;
		if (var4 < var2)
		{
			var8 = var4;
		}

		if (var6 < var8)
		{
			var8 = var6;
		}

		double var10 = var2;
		if (var4 > var2)
		{
			var10 = var4;
		}

		if (var6 > var10)
		{
			var10 = var6;
		}

		double var12 = 0.0D;
		double var14 = 0.0D;
		double var16 = (var10 + var8) / 2.0D;
		if (var8 != var10)
		{
			if (var16 < 0.5D)
			{
				var14 = (var10 - var8) / (var8 + var10);
			}

			if (var16 >= 0.5D)
			{
				var14 = (var10 - var8) / (2.0D - var10 - var8);
			}

			if (var2 == var10)
			{
				var12 = (var4 - var6) / (var10 - var8);
			}
			else if (var10 == var4)
			{
				var12 = 2.0D + (var6 - var2) / (var10 - var8);
			}
			else if (var10 == var6)
			{
				var12 = 4.0D + (var2 - var4) / (var10 - var8);
			}
		}

		var12 /= 6.0D;
		this.saturation = (int) (var14 * 256.0D);
		this.luminance = (int) (var16 * 256.0D);
		if (this.saturation < 0)
		{
			this.saturation = 0;
		}
		else if (this.saturation > 255)
		{
			this.saturation = 255;
		}

		if (this.luminance < 0)
		{
			this.luminance = 0;
		}
		else if (this.luminance > 255)
		{
			this.luminance = 255;
		}

		if (var16 > 0.5D)
		{
			this.chroma = (int) (var14 * (1.0D - var16) * 512.0D);
		}
		else
		{
			this.chroma = (int) (var14 * var16 * 512.0D);
		}

		if (this.chroma < 1)
		{
			this.chroma = 1;
		}

		this.weightedHue = (int) (var12 * chroma);
		this.hue = (int) ((double) this.chroma * var12);
		this.colour = hsl24to16(hue, saturation, luminance);
	}

	private void rgbToHsl(int rgb) {
		double r = (rgb >> 16 & 0xff) / 256.0;
		double g = (rgb >> 8 & 0xff) / 256.0;
		double b = (rgb & 0xff) / 256.0;
		double min = r;
		if (g < min) {
			min = g;
		}
		if (b < min) {
			min = b;
		}
		double max = r;
		if (g > max) {
			max = g;
		}
		if (b > max) {
			max = b;
		}
		double h = 0.0;
		double s = 0.0;
		double l = (min + max) / 2.0;
		if (min != max) {
			if (l < 0.5) {
				s = (max - min) / (max + min);
			}
			if (l >= 0.5) {
				s = (max - min) / (2.0 - max - min);
			}
			if (r == max) {
				h = (g - b) / (max - min);
			} else if (g == max) {
				h = 2.0 + (b - r) / (max - min);
			} else if (b == max) {
				h = 4.0 + (r - g) / (max - min);
			}
		}
		h /= 6.0;
		hue = (int) (h * 256.0);
		saturation = (int) (s * 256.0);
		luminance = (int) (l * 256.0);
		if (saturation < 0) {
			saturation = 0;
		} else if (saturation > 255) {
			saturation = 255;
		}
		if (luminance < 0) {
			luminance = 0;
		} else if (luminance > 255) {
			luminance = 255;
		}
		if (l > 0.5) {
			chroma = (int) ((1.0 - l) * s * 512.0);
		} else {
			chroma = (int) (l * s * 512.0);
		}
		if (chroma < 1) {
			chroma = 1;
		}
		weightedHue = (int) (h * chroma);
		colour = hsl24to16(hue, saturation, luminance);
	}

	public void setTexture(int texture) {
		this.texture = texture;
	}

	public void setRgb(int rgb) {
		this.rgb = rgb;
	}

	public void setShadowed(boolean shadowed) {
		this.shadowed = shadowed;
	}

	public void setAnotherRgb(int anotherRgb) {
		this.anotherRgb = anotherRgb;
	}

	public void setHue(int hue) {
		this.hue = hue;
	}

	public void setSaturation(int saturation) {
		this.saturation = saturation;
	}

	public void setLuminance(int luminance) {
		this.luminance = luminance;
	}

	public void setAnotherHue(int anotherHue) {
		this.anotherHue = anotherHue;
	}

	public void setAnotherSaturation(int anotherSaturation) {
		this.anotherSaturation = anotherSaturation;
	}

	public void setAnotherLuminance(int anotherLuminance) {
		this.anotherLuminance = anotherLuminance;
	}

	public void setWeightedHue(int weightedHue) {
		this.weightedHue = weightedHue;
	}

	public void setChroma(int chroma) {
		this.chroma = chroma;
	}

	public void setColour(int colour) {
		this.colour = colour;
	}
	
	

}
