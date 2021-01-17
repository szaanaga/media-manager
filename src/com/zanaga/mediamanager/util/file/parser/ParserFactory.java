package com.zanaga.mediamanager.util.file.parser;

import com.zanaga.mediamanager.util.file.structure.FileStructure;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ParserFactory {

	private final Set<Parser> parserSet = new HashSet<>();

	public ParserFactory() {
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
						"sfondopap�",
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
		buffer.append("\\d".repeat(count));
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
}
