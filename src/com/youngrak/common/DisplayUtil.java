package com.youngrak.common;

import com.youngrak.ApplicationEx;

import android.content.Context;

public class DisplayUtil
{
	private static final float DEFAULT_HDIP_DENSITY_SCALE = 1.5f;
	/**
	 * �ȼ������� ���� ���÷��� ȭ�鿡 ����� ũ��� ��ȯ�մϴ�.
	 * 
	 * @param pixel �ȼ�
	 * @return ��ȯ�� �� (DP)
	 */
	public static int DPFromPixel(int pixel)
	{
		Context context = ApplicationEx.getAppContext();
		float scale = context.getResources().getDisplayMetrics().density;
    
		return (int)(pixel / DEFAULT_HDIP_DENSITY_SCALE * scale);
	}
  
	/**
	 * ���� ���÷��� ȭ�鿡 ����� DP������ �ȼ� ũ��� ��ȯ�մϴ�.
	 * 
	 * @param  DP �ȼ�
	 * @return ��ȯ�� �� (pixel)
	 */
	public static int PixelFromDP(int DP)
	{
		Context context = ApplicationEx.getAppContext();
		float scale = context.getResources().getDisplayMetrics().density;
    
		return (int)(DP / scale * DEFAULT_HDIP_DENSITY_SCALE);
	}
}
