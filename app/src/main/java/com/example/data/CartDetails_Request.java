package com.example.data;

import java.util.ArrayList;

public class CartDetails_Request 
{
	private static ArrayList<String> ArrayProduct_Id;
	private static ArrayList<String> ArrayProductName;
	private static ArrayList<String> ArrayImagePath;
	private static ArrayList<String> ArrayQuantity;
	private static ArrayList<String> ArrayTotal_Cost;
	private static ArrayList<String> ArrayOverallCost;
	
	public static ArrayList<String> GetProduct_Ids()
	{
		return ArrayProduct_Id;
	}
	public static void SetProduct_Ids(ArrayList<String> RArrayProduct_Id)
	{
		ArrayProduct_Id=RArrayProduct_Id;
	}
	
	public static ArrayList<String> GetProductNames()
	{
		return ArrayProductName;
	}
	public static void SetProductNames(ArrayList<String> RArrayProductName)
	{
		ArrayProductName=RArrayProductName;
	}
	
	public static ArrayList<String> GetImagePaths()
	{
		return ArrayImagePath;
	}
	public static void SetImagePaths(ArrayList<String> RArrayImagePath)
	{
		ArrayImagePath=RArrayImagePath;
	}
	
	public static ArrayList<String> GetQuantitys()
	{
		return ArrayQuantity;
	}
	public static void SetQuantitys(ArrayList<String> RArrayQuantity)
	{
		ArrayQuantity=RArrayQuantity;
	}
	
	public static ArrayList<String> GetTotal_Costs()
	{
		return ArrayTotal_Cost;
	}
	public static void SetTotal_Costs(ArrayList<String> RArrayTotal_Cost)
	{
		ArrayTotal_Cost=RArrayTotal_Cost;
	}
	
	public static ArrayList<String> GetOverallCost()
	{
		return ArrayOverallCost;
	}
	public static void SetOverallCost(ArrayList<String> RArrayOverallCost)
	{
		ArrayOverallCost=RArrayOverallCost;
	}
	
	
	
}
