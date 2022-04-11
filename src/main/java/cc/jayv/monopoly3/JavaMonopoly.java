package cc.jayv.monopoly3;


import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author jay
 */
public class JavaMonopoly {

	DynamicView dynamicView;

	public JavaMonopoly() {
		dynamicView = new DynamicView();
	}

	public static void main(String args[]) throws IOException {
		new JavaMonopoly();
	}
}
