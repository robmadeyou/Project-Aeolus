package com.gmail.robmadeyou;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
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

	private static ArrayList<Item> items = new ArrayList<>();

	public ItemEditorGUI() throws HeadlessException
	{
		super();


		JsonLoader loader = new JsonLoader( "./Data/json/item_definitions.json" )
		{
			@Override
			public void load( JsonObject reader, Gson builder )
			{
				Item i = new Item();
				i.loadItem( reader );
				items.add( i );
			}
		};

		loader.load();
	}

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

		gui.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE );
		gui.setLocation( 400, 400);
		gui.setSize( 280, 200 );
		gui.setVisible( true );



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
		private ArrayList<Integer> specialPrice = new ArrayList<>();
		private int lowAlchemy;
		private int highAlchemy;
		private double weight;
		private boolean noted;
		private boolean noteable;
		private int childId;
		private int parentId;
		private boolean isTwoHanded;
		private String equipmentType;
		private boolean fullBody;
		private boolean fullHat;
		private boolean fullMask;
		private ArrayList<Double> bonus = new ArrayList<>();
		private ArrayList<Integer> requirement = new ArrayList<>();
		private ArrayList<String> actions = new ArrayList<>();

		public Item()
		{
			super();
		}

		public void loadItem( JsonObject reader )
		{
			this.id = reader.get("id").getAsInt();
			this.name = reader.get( "name" ).getAsString();
			this.description = reader.get( "description" ).getAsString();
			this.tradeable = reader.get( "tradeable" ).getAsBoolean();
			this.destroyable = reader.get( "destroyable" ).getAsBoolean();
			this.stackable = reader.get( "stackable" ).getAsBoolean();
			this.value = reader.get( "value" ).getAsInt();
			JsonArray specialItems = reader.get( "specialPrice" ).getAsJsonArray();
			for( JsonElement spec : specialItems )
			{
				this.specialPrice.add( spec.getAsInt() );
			}
			this.specialPrice = reader.get( "specialPrice" ).getAsJsonArray();
		}
	}
}

