
import java.io.Serializable;
import java.util.Map;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author jay
 */
public class Color extends Property implements Serializable {
	public enum colorGroupKeys {
        unspecified,
        brown,
        lightBlue,
        magenta,
        orange,
        red,
        yellow,
        green,
        darkBlue
	}
	colorGroupKeys colorGroup;
	
	int houseCount, hotelCount;
	
	Color(Map<String, Integer> propertyAttributes, int inputSpaceID, String inputFriendlyName) {
		colorGroup = colorGroupKeys.unspecified;
		id = inputSpaceID;
		
		houseCount = 0;
		hotelCount = 0;
	}
	
	// <editor-fold defaultstate="collapsed" desc="Setters and getters">
	public colorGroupKeys getColorGroup() {
		return colorGroup;
	}
	
	public void setColorGroup(colorGroupKeys inputColorGroup) {
        colorGroup = inputColorGroup;
    }
    
    public int getHouseCount() {
        return houseCount;
    }
    
    public void setHouseCount(int inputHouseCount) {
        houseCount = inputHouseCount;
    }
	// </editor-fold>
	
	@Override
	public int calculateRent() {
		return 0;
	}
}
