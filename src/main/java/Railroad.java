
import java.io.Serializable;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author jay
 */
public class Railroad extends Property implements Serializable {
	public Railroad() {
		super(null);
	}
	
	@Override
	public int calculateRent() {
		return 0;
	}
}
