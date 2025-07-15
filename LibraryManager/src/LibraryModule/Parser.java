package LibraryModule;

/* I was trying to import Gson (after failing with Jackson) to parse Json representations to objects,
 * however, the library and maven both decided it would be impossible for me to install and/or use the library
 * for reasons unknown, and since: 
 * 1) this little problem (installing a library) as of now has stolen more time than the Requisite Analysis,
 * 	  Architectural design choices, UML design and coding of the system combined,
 * 2) I am in severe burnout, complete with chronic fatigue, restlessness, diminished attention
 * 	  and nearly complete emotional numbness,
 * 3) Aside from that my general mental health is at an alarmingly low level and
 * 4) The project is due in 3 days
 * I have decided to take the (counterintuitively) easier path: Making the parser myself. 
 * 
 * I am honestly unsure of how good of an engineer I am, given the Odyssey I find myself in for installing a library,
 * task everyone succeeds at, but I cannot afford to care at the moment. I need a parser and if the gods of programming
 * are not going to hand me one, I'm going to make one out of hate, spite and increased risks of a heart attack since
 * those run in my family. */

import java.util.StringTokenizer;
import java.util.List;
import java.util.LinkedList;
import java.lang.StringBuilder;
import Utility.Package;

/* This is a parser made specifically for Volumes */
public class Parser 
{
	public static final char TAB = '\t';
	public static final char NLN = '\n';
	public static final char QTM = '\"';
	public static final char SPC = '\s';
	public static final char COL = ':';
	
	public static final String ENDL = ",\n";
	public static final String JSON_ENTRY_TAB = "\t\t";
	
	public static final int ENTRY_PREDICTED_SIZE = 127;
	public static final int LIBRARY_PREDICTED_SIZE = 2048; // 2kB
	
	private String toJson(Volume v, boolean formatted)
	{
		if (v == null)
		{
			return null;
		}
		
		String format = formatted ? JSON_ENTRY_TAB : "";
		String ret = format + "{" + NLN;
		
		ret += format + "\t\"title\": " 	+ QTM + v.GetTitle() 	+ QTM + ENDL;
		ret += format + "\t\"author\": " 	+ QTM + v.GetAuthor() 	+ QTM + ENDL;
		ret += format + "\t\"genre\": "		+ QTM + v.GetGenre()	+ QTM + ENDL;
		ret += format + "\t\"ISBN\": "		+ QTM + v.GetISBN()		+ QTM + ENDL;
		ret += format + "\t\"rating\": " 	+ QTM + v.GetRating() 	+ QTM + ENDL;
		ret += format + "\t\"state\": "		+ QTM + v.GetState()	+ QTM + NLN;
		
		ret += format + "}";
		
		return ret;
	}
	
	public String ParseLibrary(List<Volume> lib)
	{
		StringBuilder result = new StringBuilder(LIBRARY_PREDICTED_SIZE);
		
		result.append("{\n\t\"library\": [\n");
		
		int i = 0;
		for (Volume v : lib)
		{
			result.append(toJson(v, true) + (i != lib.size() - 1 ? ",\n" : "\n"));
			i++;
		}
		
		result.append("\t]\n}");
		
		return result.toString();
	}
	
	@Package static String trim(String s) { return s.replaceAll("^\\s+|\\s+$", ""); }
	
	private Volume toVolume(String json) throws InvalidJsonEntryException, JsonParsingError
	{
		if (json == null)
		{
			return null;
		}
		
		Volume ret = new Volume();
		StringTokenizer tokenizer = new StringTokenizer(json, ":\"\t\n", false);
		
		boolean beganEntry 		= false;
		boolean awaitFieldType 	= true;
		boolean metEnd			= false;
		boolean skipTkn			= false;
		//boolean awaitColon 		= false;		// MIGHT be unnecessary
		String field = "";
		String token = "";
		while (tokenizer.hasMoreTokens())
		{
			if (skipTkn)
			{
				skipTkn = false;
			}
			else
			{
				token = tokenizer.nextToken().trim();
			}
			
			//System.out.println(token);
			
			// Entry begin character
			if (token.equals("{"))
			{
				if (beganEntry)
				{
					throw new InvalidJsonEntryException("Unexpected \'{\' found");
				}
				beganEntry = true;
				continue;
			}
			
			// We don't care about spaces
			if (token.equals(" ") || token.equals(""))
			{
				continue;
			}
			
			// Met the end of the Json entry
			if (token.replace(" ", "").equals("}"))
			{
				break;
			}
			
			// Met the comma = analyzing next field
			if (token.equals(","))
			{
				awaitFieldType = true;
				//awaitColon = false;
				continue;
			}
			
			// If we're awaiting a field type, we flag that we expect a colon next and 
			if (awaitFieldType)
			{
				field = token;
				awaitFieldType = false;
				continue;
			}
			
			/*// Met a colon
			if (awaitColon && token.replace(" ", "").equals(":"))
			{
				awaitColon = false;
				continue;
			}*/
			
			// Memorize value
			field = field.toLowerCase();
			if (token.endsWith("}"))
			{
				System.out.println("token includes closed bracket");
				metEnd = true;
				token = token.substring(0, token.length() - 1).trim();
			}
			else if (token.startsWith("}"))
			{
				System.out.println("Token starts with closed bracket");
				token = token.substring(1);
				skipTkn = true;
			}
			switch (field)
			{
			case "title":
				if (ret.GetTitle() == null)
				{
					ret.SetTitle(token);
				}
				else
				{
					throw new InvalidJsonEntryException("Json entry defined the same field (title) more than once");
				}
				break;
			case "author":
				if (ret.GetAuthor() == null)
				{
					ret.SetAuthor(token);
				}
				else
				{
					throw new InvalidJsonEntryException("Json entry defined the same field (author) more than once");
				}
				break;
			case "genre":
				if (ret.GetGenre() == null)
				{
					ret.SetGenre(token);
				}
				else
				{
					throw new InvalidJsonEntryException("Json entry defined the same field (genre) more than once");
				}
				break;
			case "isbn":
				if (ret.GetISBN() == null)
				{
					ret.SetISBN(token);
				}
				else
				{
					throw new InvalidJsonEntryException("Json entry defined the same field (ISBN) more than once");
				}
				break;
			case "rating":
				if (ret.GetRating() == Volume.UNINITIALIZED_RATING)
				{
					if (token.length() >= 2)
					{
						throw new InvalidJsonEntryException("Invalid rating value found!");
					}
					
					if (!Character.isDigit(token.charAt(0)))
					{
						throw new InvalidJsonEntryException("Rating value should be a number");
					}
					
					byte val = (byte) Character.getNumericValue(token.charAt(0));
					if (val < 0 || val > 5)
					{
						throw new InvalidJsonEntryException("Rating must be a value between 0 and 5 (inclusive)");
					}
					
					ret.SetRating(val);
				}
				else
				{
					throw new InvalidJsonEntryException("Json entry defined the same field (rating) more than once");
				}
				break;
			case "state":
				if (ret.GetState() == null)
				{
					token = trim(token.toLowerCase());
					
					switch (token)
					{
					case "read":
						ret.SetState(ReadingState.READ);
						break;
					case "reading":
						ret.SetState(ReadingState.READING);
						break;
					case "to_be_read": case "to be read":
						ret.SetState(ReadingState.TO_BE_READ);
						break;
					default:
						// Reading state is not a "volume defining" information, and it'e easy to modify,
						// so it's not a huge deal if it doesn't initialize properly sometimes
						ret.SetState(ReadingState.TO_BE_READ);
						System.out.println("ILLEGAL READING STATE READ: " + token);
						break;
					}
				}
				else
				{
					throw new InvalidJsonEntryException("Json entry defined the same field (state) more than once");
				}
				break;
			default:
				throw new JsonParsingError("Unexpected field found->" + field);
			}
			
			if (metEnd)
			{
				break;
			}
		}
		
		if (ret.GetState() == null)
		{
			System.out.println("NO STATE FOUND, DEFAULTING TO TO_BE_READ.");
			ret.SetState(ReadingState.TO_BE_READ);
		}
		
		return ret;
	}
	
	public List<Volume> ParseJson(String json) throws 
													InvalidJsonSequenceException, 
													InvalidJsonEntryException, 
													JsonParsingError
	{
		LinkedList<Volume> ret = new LinkedList<Volume>();
		
		boolean inSequence 	= false;
		boolean inEntry		= false;
		StringBuilder entryBuilder = new StringBuilder(ENTRY_PREDICTED_SIZE);
		String entryJson;
		for (char c : json.toCharArray())
		{
			// Sequence begin character
			if (c == '[')
			{
				if (inSequence)
				{
					throw new InvalidJsonSequenceException("Found multiple begin sequence characters.");
				}
				inSequence = true;
				continue;
			}
			
			// Sequence end character
			if (c == ']')
			{
				if (!inSequence)
				{
					throw new InvalidJsonSequenceException("Met an end of sequence character when the sequence was never opened");
				}
				if (inEntry)
				{
					throw new InvalidJsonSequenceException("Sequence ended in the middle of an entry.");
				}
				inSequence = false;
				break;
			}
			
			// Entry begin character
			if (c == '{')
			{
				if (!inSequence)
				{
					System.out.println("We're not in a sequence, entry begin character can be ignored.");
					continue;
				}
				inEntry = true;
			}
			
			if (inEntry)
			{
				entryBuilder.append(c);
			}
			
			// Entry end character
			if (c == '}')
			{
				if (!inSequence)
				{
					System.out.println("We're not in a sequence, entry end character can be ignored.");
					continue;
				}
				inEntry = false;
				
				// This part could -and should- be done with multithreading,
				// but because of a lack of time I'll add it last if I can
				entryJson = entryBuilder.toString();
				//System.out.println("Json entry: " + entryJson);
				ret.add(toVolume(entryJson));
				entryBuilder.delete(0, entryBuilder.length());
			}
		}
		
		return ret;
	}
	
	public static void main(String... args) throws 	InvalidJsonEntryException, JsonParsingError	// Parser testing
												,	InvalidJsonSequenceException
	{
		/*
		 * TEST 1 - Single Json entry - translate from entry to Volume and vice versa
		String json = "\t{\"title\": \"1984\", \"author\" : \"George Orwell\", \"ISBN\": \"0000000002048\", \t\"genre\" :\"dystopian\", \"rating\": \"5\",\n\t\"StAtE\": \"to_BE_read\"\t}";
		
		Parser p = new Parser();
		Volume v = p.toVolume(json);
		
		if (v != null)
		{
			System.out.println(v);
			
			json = p.toJson(v);
			
			System.out.println(json);
		}
		else
		{
			System.out.println("Null volume");
		}
		*/
		
		/*
		 * TEST 2 - convert Json list to Volumes list
		String json = 	"SEQUENCE:" + 
						"[\t{\"title\": \"1984\", \"author\" : \"George Orwell\", \"ISBN\": \"0000000002048\", \t\"genre\" :\"dystopian\", \"rating\": \"5\",\n\t\"StAtE\": \"to_BE_read\"\t}" +
						"\n {\"title\": \"Ultrakill\", \"author\": \"Hakita\", \"ISBN\": \"0000000001024\", \"genre\": \"LGBT robots killing God\", \"rating\": \"5\", \"state\": \"read\"}]";
		
		Parser parser = new Parser();
		List<Volume> list;
		list = parser.parseJson(json);
		
		for (Volume v : list)
		{
			System.out.println(v);
		}
		*/
		
		/*
		 * TEST 3 - convert Volume list to Json list
		 */
		
		Volume literally1984 = new Volume(
										"1984", 
										"George Orwell", 
										"dystopian", 
										"0000000002048", 
										(byte)5, 
										ReadingState.READING);
		Volume wheresNumber3 = new Volume(
										"Half-Life 2",	// As it turns out, this system works well for games as well
										"Valve",
										"shooter",
										"3333333333333",
										(byte)5,
										ReadingState.READ);
		
		LinkedList<Volume> volumes = new LinkedList<Volume>();
		volumes.add(literally1984);
		volumes.add(wheresNumber3);
		
		Parser p = new Parser();
		
		System.out.println(p.ParseLibrary(volumes));
	}
}

class InvalidJsonEntryException extends Exception
{
	public InvalidJsonEntryException(String msg)
	{
		super(msg);
	}
}

class JsonParsingError extends Exception
{
	public JsonParsingError(String msg)
	{
		super(msg);
	}
}

class InvalidJsonSequenceException extends Exception
{
	public InvalidJsonSequenceException(String msg)
	{
		super(msg);
	}
}
