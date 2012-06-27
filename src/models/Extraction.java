package models;

import java.sql.Timestamp;

import db.Resources.TextType;

public class Extraction
{
	protected TextType 	textType;
	protected String		text;
	protected Timestamp	date;
	
	public Extraction()
	{
		super();
	}

	public Extraction(TextType textType, String text)
	{
		super();
		this.textType = textType;
		this.text = text;
	}
	
	public Extraction(TextType textType, String text, Timestamp date)
	{
		super();
		this.textType = textType;
		this.text = text;
		this.date = date;
	}

	public TextType getTextType()
	{
		return textType;
	}

	public void setTextType(TextType textType)
	{
		this.textType = textType;
	}

	public String getText()
	{
		return text;
	}

	public void setText(String text)
	{
		this.text = text;
	}

	public Timestamp getDate()
	{
		return date;
	}

	public void setDate(Timestamp date)
	{
		this.date = date;
	}
}
