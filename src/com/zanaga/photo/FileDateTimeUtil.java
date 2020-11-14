package com.zanaga.photo;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.mp4.Mp4MetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import com.drew.metadata.file.FileSystemDirectory;
import com.drew.metadata.mp4.Mp4Directory;

public class FileDateTimeUtil {

	private Set<AParser> parserSet;

	public FileDateTimeUtil() {
		this.parserSet = new HashSet<>();
		Arrays.asList(

				getRegexDigit(1),
				
				getRegexDigit(1) + "_" + getRegexDigit(1),
				"foto" + getRegexDigit(1),

				"N" + getRegexDigit(2),

				getRegexDigit(3),

				"Picture " + getRegexDigit(3),

				getRegexDigit(4),
				
				getRegexDigit(4) + getRegexWords(",", ";", "\\.", "\\.\\.", " " + getRegexParentheses(1), "; " + getRegexParentheses(1)),

				getRegexWords("CIMG", "Copia di CIMG", "DSC_", "dscn", "DSCN", "HPIM", "IMAG", "IMG_", "RSCN", "SAM_", "VIDEO") + getRegexDigit(4),

				"IMAG" + getRegexDigit(4) + " " + getRegexParentheses(1),
				"DSC_" + getRegexDigit(4) + getRegexWords(" " + getRegexParentheses(1), " - Copia"),
				"DSC_" + getRegexDigit(4) + "_edited",
				"SAM_" + getRegexDigit(4) + "\\." + getRegexDigit(1),

				getRegexWords("DSC", "MAH", "SDC") + getRegexDigit(5),

				"DSC" + getRegexDigit(5) + getRegexWords(" " + getRegexParentheses(1), " - Copia", "-" + getRegexDigit(1)),

				"IM" + getRegexDigit(6),

				"P" + getRegexDigit(7),

				getRegexDigit(8),

				"P" + getRegexDigit(8)

		).forEach(regex -> this.parserSet.add(createParserExif(regex)));

		Arrays.asList(
				getRegexWords(
						getRegexDigit(1) + " - " + getRegexWords(
								"Incontro", 
								"Passeggiata", 
								"Matrimonio", 
								"A casa"
						),
						"Rimini 2005 " + getRegexDigit(3),	// 001
						"NADIA",
						"claudia e patrick", 
						"fine", 
						"fiori", 
						"New Image", 
						"pag1", 
						"pag2", 
						"sfondo arrivo", 
						"sfondo chiesa", 
						"sfondo palestra", 
						"sfondo sala", 
						"sfondopapà",
						"sfono riagiorgio",
						"foto",
						"Mone",
						"Patty",
						"PattyMone",
						"Tombola b",
						"Canyon b",
						"Festa della mamma",
						"comica",
						"comica" + getRegexDigit(13),
						"Rebecca " + getRegexDigit(2), 
						"4. giro",
						"BeviBevi",
						"Cacca",
						"Distruggitorre",
						"Draghetto",
						"Giacco-specchio",
						"Giacco",
						"Giaccoliere",
						"Giacopazzo",
						"Pappa",
						"Pernacchiotto",
						"Biscotti natalizi 2009 " + getRegexParentheses(1),
						"Biscotti natalizi 2009 " + getRegexParentheses(2),
						"NuovoPromemoria_" + getRegexDigit(8) + "_" + getRegexDigit(6) + "_" + getRegexDigit(1)
				)
		).forEach(regex -> this.parserSet.add(createParserExif(regex)));
		
		Arrays.asList(
				"IMG-" + getRegexDigit(8) + "-WA" + getRegexDigit(4),
				"IMG-" + getRegexDigit(8) + "-WA" + getRegexDigit(4) + " " + getRegexParentheses(1),
				"IMG-" + getRegexDigit(8) + "-WA" + getRegexDigit(4) + " - Copy",
				"IMG-" + getRegexDigit(8) + "-WA" + getRegexDigit(4) + "-" + getRegexDigit(1),
				"VID-" + getRegexDigit(8) + "-WA" + getRegexDigit(4),
				"VID-" + getRegexDigit(8) + "-WA" + getRegexDigit(4) + " " + getRegexParentheses(1),
				"VID-" + getRegexDigit(8) + "-WA" + getRegexDigit(4) + " - Copia"
		).forEach(regex -> this.parserSet.add(createParserExifORDateTime(regex, 4, 8, "yyyyMMdd")));

		Arrays.asList(
				getRegexDigit(2) + "-" + getRegexDigit(2) + "-" + getRegexDigit(4)
		).forEach(regex -> this.parserSet.add(createParserExifORDateTime(regex, 0, 10, "dd-MM-yyyy")));

		Arrays.asList(
				getRegexDigit(8) + "_" + getRegexDigit(6),
				getRegexDigit(8) + "_" + getRegexDigit(6) + " " + getRegexParentheses(1),
				getRegexDigit(8) + "_" + getRegexDigit(6) + "_LLS",
				getRegexDigit(8) + "_" + getRegexDigit(6) + "-" + getRegexDigit(1),
				getRegexDigit(8) + "_" + getRegexDigit(6) + "_" + getRegexDigit(1),
				getRegexDigit(8) + "_" + getRegexDigit(6) + "~" + getRegexDigit(1),
				getRegexDigit(8) + "_" + getRegexDigit(6) + "_" + getRegexDigit(3),
				getRegexDigit(8) + "_" + getRegexDigit(6) + "_" + getRegexDigit(3) + "_" + getRegexDigit(2),
				getRegexDigit(8) + "_" + getRegexDigit(6) + "_" + getRegexDigit(3) + "_" + getRegexDigit(3),
				getRegexDigit(8) + "_" + getRegexDigit(6) + getRegexParentheses(1)
		).forEach(regex -> this.parserSet.add(createParserExifORDateTime(regex, 0, 15, "yyyyMMdd_HHmmss")));

		Arrays.asList(
				"WIN_" + getRegexDigit(8) + "_" + getRegexDigit(6)
		).forEach(regex -> this.parserSet.add(createParserExifORDateTime(regex, 4, 15, "yyyyMMdd_HHmmss")));

		Arrays.asList(
				"WIN_" + getRegexDigit(8) + "_" + getRegexDigit(2) + "_" + getRegexDigit(2) + "_" + getRegexDigit(2) + "_Pro"
		).forEach(regex -> this.parserSet.add(createParserExifORDateTime(regex, 4, 17, "yyyyMMdd_HH_mm_ss")));

		Arrays.asList(
				"big_" + getRegexDigit(4) + "-" + getRegexDigit(2) + "-" + getRegexDigit(2) + "-" + getRegexDigit(4) + "_" + getRegexDigit(1)
		).forEach(regex -> this.parserSet.add(createParserExifORDateTime(regex, 4, 15, "yyyy-MM-dd-HHmm")));

		Arrays.asList(
				getRegexDigit(14)
		).forEach(regex -> this.parserSet.add(createParserExifORDateTime(regex, 0, 14, "yyyyMMddHHmmss")));
	}
	
	
	public FileStructure getFileStructure(File file, String name) {
		return this.parserSet.stream().filter(parser -> parser.matches(name)).map(parser -> parser.parse(file)).findFirst().orElse(null);
	}

	
	/*
	 * REGEX
	 */
	
	private String getRegexDigit(int count) {
		StringBuffer buffer = new StringBuffer();
		for(int index=0; index<count; index++) {
			buffer.append("\\d");
		}
		return buffer.toString();
	}
	
	private String getRegexWords(String... items) {
		StringBuffer buffer = new StringBuffer();
		for(String item : items) {
			if(buffer.length() > 0) {
				buffer.append("|");
			}
			buffer.append(item);
		}
		return "\\b(" + buffer.toString() + ")";
	}
	
	private String getRegexParentheses(int digitsCount) {
		return "\\(" + getRegexDigit(digitsCount) + "\\)";
	}

	
	/*
	 * PARSER
	 */
	
	private ParserExif createParserExif(String regex) {
		return new ParserExif(regex);
	}

	private ParserExifORDateTime createParserExifORDateTime(String regex, int beginIndex, int length, String pattern) {
		return new ParserExifORDateTime(regex, beginIndex, length, pattern);
	}

	private abstract class AParser {
		
		private Pattern pattern;
		
		private AParser(String regex) {
			this.pattern = Pattern.compile(regex);
		}
		
		private boolean matches(String name) {
			return this.pattern.matcher(name).matches();
		}
		
		protected abstract FileStructure parse(File file);
	}

	private class ParserExif extends AParser {
		
		private ParserExif(String regex) {
			super(regex);
		}
		
		@Override
		protected FileStructure parse(File file) {
			FileStructure fileStructure = new FileStructure(file);
			Date date;
			switch(Util.getSuffix(file)) {
			case "MOV":
			case "mov":
			case "MP4":
			case "mp4":
				date = getDateMP4(file);
				break;
			case "3gp":
				date = getDateFS(file);
				break;
			default:
				date = getDateDefault(file);
				break;
			}
			if(date != null) {
				fileStructure.setExifCreatedTime(date);
			}
			return fileStructure;
		}
		
		private Date getDateFS(File file) {
			try {
				for(Directory directory : Mp4MetadataReader.readMetadata(file).getDirectoriesOfType(FileSystemDirectory.class)) {
					Date date = directory.getDate(FileSystemDirectory.TAG_FILE_MODIFIED_DATE);
					if(date != null) {
						return date;
					}
				}
			}
			catch(Exception exception) {
				
			}
			return null;
		}
		
		private Date getDateMP4(File file) {
			try {
				for(Directory directory : Mp4MetadataReader.readMetadata(file).getDirectoriesOfType(Mp4Directory.class)) {
					Date date = directory.getDate(Mp4Directory.TAG_CREATION_TIME);
					if(date != null) {
						return date;
					}
				}
			}
			catch(Exception exception) {
				
			}
			return null;
		}
		
		private Date getDateDefault(File file) {
			try {
				for(Directory directory : ImageMetadataReader.readMetadata(file).getDirectoriesOfType(ExifSubIFDDirectory.class)) {
					Date date = directory.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL);
					if(date != null) {
						return date;
					}
				}
			}
			catch(Exception exception) {
				
			}
			return null;
		}
	}
	
	private class ParserExifORDateTime extends ParserExif {
		
		private int beginIndex;
		private int length;
		private SimpleDateFormat dateFormat;
		
		private ParserExifORDateTime(String regex, int beginIndex, int length, String pattern) {
			super(regex);
			this.beginIndex = beginIndex;
			this.length = length;
			this.dateFormat = (SimpleDateFormat)SimpleDateFormat.getInstance();
			this.dateFormat.applyPattern(pattern);
		}
		
		@Override
		protected FileStructure parse(File file) {
			FileStructure fileStructure = super.parse(file);
			try {
				fileStructure.setParsedTimestamp(this.dateFormat.parse(file.getName().substring(this.beginIndex, beginIndex+this.length)));
			}
			catch(ParseException exception) {
				
			}
			return fileStructure;
		}
	}
}
