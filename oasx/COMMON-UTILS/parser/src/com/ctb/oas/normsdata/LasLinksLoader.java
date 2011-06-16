package com.ctb.oas.normsdata;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Writer;
import java.util.List;

/**
 * @author TCS
 *
 */
public class LasLinksLoader {
	private File dataFile;
	protected ParsedData parsedData = ParsedData.INSTANCE;

	public LasLinksLoader(File dataFile) {
		this.dataFile = dataFile;
	}

	public LasLinksLoader() {
	};

	public ParsedData load() {
		try {
			LineNumberReader reader = new LineNumberReader(new FileReader(
					dataFile));
			return load(reader);
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	public ParsedData load(LineNumberReader reader) {
		try {
			String line;
			LasLinksScorer lasLinksScorer = null;
			TableType type = null;
			while ((line = reader.readLine()) != null) {
				if (ScorerUtil.containsHeaderCode(line)
						&& (type = TableType.getLasLinksTableType(line)) != null) {
					if (lasLinksScorer != null) {
						addScaleScore(lasLinksScorer.getNormsData());
					}

					lasLinksScorer = (LasLinksScorer) ScorerFactory
							.getLasLinksScorer(type.getDestScoreType());
					if (lasLinksScorer == null) {
						line = reader.readLine();
						continue;
					} else
						lasLinksScorer.handleHeader(line);
				}

				else if (ScorerUtil.containsInstructionCode(line)) {
					lasLinksScorer.handleInstruction(line);
				}

				else if (ScorerUtil.containsScoreCode(line)) {
					lasLinksScorer.handleScoreLine(line);
				} /*else {
					System.out.println("Skipped Line");
					System.out.println(line);
				}*/
			}
			addScaleScore(lasLinksScorer.getNormsData());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return parsedData;
	}

	public static void main(String args[]) {
		ScorerUtil.validateArgs(args);
		File inputFile = ScorerUtil.getInputFileFromArgs(args);
		File outFile = ScorerUtil.getOutputFileFromArgs(args);

		LasLinksLoader loader = new LasLinksLoader(inputFile);
		loader.load();
		loader.write(outFile);
	}

	protected void addScaleScore(NormsData data) {
		if (data == null)
			return;

		List dataArray = (List) parsedData
				.getScoreList(data.getDestScoreType());
		dataArray.add(data);
	}

	public void write(File outFile) {
		ScoreType[] scoreTypes = ScoreType.LASLINKS_TYPES;
		FileWriter writer = null;
		try {
			writer = new FileWriter(outFile);
		} catch (IOException e) {
			throw new RuntimeException("cannot writer for outfile", e);
		}
		for (int i = 0; i < scoreTypes.length; i++) {
			ScoreType scoreType = scoreTypes[i];
			List normsList = ParsedData.INSTANCE.getScoreList(scoreType);
			if (!normsList.isEmpty())
				write(scoreType, normsList, writer);
		}

		try {
			writer.flush();
			writer.close();
		} catch (IOException e) {
			throw new RuntimeException("cannot flush or close file writer ", e);
		}
	}

	public void write(ScoreType scoreType, List normsDataList, Writer writer) {
		ScoreRecordWriter recordWriter = ScoreRecordWriterFactory
				.getScoreRecordWriter(scoreType);
		for (int i = 0; i < normsDataList.size(); i++) {
			NormsData normsData = (NormsData) normsDataList.get(i);
			recordWriter.writeScoreRecord(writer, normsData);
		}
	}

}
