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
public class Railroad extends Property implements Serializable {
	public Railroad(Map<String, Integer> propertyAttributes, int inputSpaceID, String inputFriendlyName) {
		super(null);
	}
	
	@Override
	public int calculateRent() {
		return 0;
	}
}
