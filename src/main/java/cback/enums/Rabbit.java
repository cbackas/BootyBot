package cback.enums;

public enum Rabbit {
	ZAC("zac", "http://rabb.it/zgibson"),
	TROY("troy","http://rabb.it/troyhayes"),
	JEREMY("jeremy","http://rabb.it/zannyhip"),
	JOSH("josh","http://rabb.it/impervious"),
	GIBS("gibs","http://rabb.it/gibsorya"),
	NAYR("nayr","https://rabb.it/nayr");

	public String roomname;
	public String url;

	Rabbit(String roomname, String url)
	{
		this.roomname = roomname;
		this.url = url;
	}

	public static Rabbit getURLFromCommand(String roomname)
	{
		for(Rabbit room : values())
		{
			if(room.roomname.equalsIgnoreCase(roomname))
			{
				return room;
			}
		}
		return null;
	}
}