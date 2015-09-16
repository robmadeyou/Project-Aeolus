package com.gmail.robmadeyou;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import core.game.util.JsonLoader;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Created by roger on 16/09/15.
 */
public class ItemEditorGUI extends JFrame
{

	public ItemEditorGUI() throws HeadlessException
	{
		super();


	}

	private static ArrayList<Item> items;
	private static ItemEditorGUI gui;
	public static void main( String[] args )
	{
		/*
		 * Setting the system default look and feel, meaning that what ever the
		 * system default theme is - java will use it and adapt their gui to that
		 */
		try{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}catch(Exception e){}

		gui = new ItemEditorGUI();

		gui.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
		gui.setLocation( 400, 400 );
		gui.setSize( 280, 200 );
		gui.setVisible( true );

		JsonLoader loader = new JsonLoader( "./Data/json/item_definitions.json" )
		{
			@Override
			public void load( JsonObject reader, Gson builder )
			{
				System.out.println( "stuff" );
			}
		};

		loader.load();

	}

	private class Item
	{
		private int id;
		private String name;
		private String description;
		private boolean tradeable;
		private boolean destroyable;
		private boolean stackable;
		private int value;
		private int[] specialPrice;
		
		public Item( int id, String texture )
		{
			super();
		}

		public void loadItem()
		{

		}
	}
}

