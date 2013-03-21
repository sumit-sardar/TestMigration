package com.ctb.contentBridge.core.publish.command;

import java.io.PrintStream;
import java.io.PrintWriter;
import org.apache.log4j.Logger;

import com.ctb.contentBridge.core.exception.BusinessException;
import com.ctb.contentBridge.core.publish.report.AssessmentProcessorReport;
import com.ctb.contentBridge.core.publish.report.CommandReport;
import com.ctb.contentBridge.core.publish.report.CommandReportFormatter;
import com.ctb.contentBridge.core.publish.report.DummyReport;
import com.ctb.contentBridge.core.publish.report.Formatter;
import com.ctb.contentBridge.core.publish.report.FormatterFactory;
import com.ctb.contentBridge.core.publish.report.Report;

/**
 * EXAMPLE ARGUMENTS [as of 12/03/2003]
 * <p>
 * mappingHierarchy frameworkfile=../mappingdata/CA/levels.txt
 * objectivesfile=../mappingdata/CA/objectives.txt
 * mappingfile=../mappingdata/CA/item_map.txt objectivesfileformat=short
 * <p>
 * importitemsvalidate itemfile=testdata/parse_pipe/3rracoonbusy.xml env=user
 * <p>
 * importitems localimagearea=testdata/parse_pipe imagearea=.
 * itemfile=testdata/parse_pipe/3rracoonbusy.xml env=user
 * <p>
 * mapitemsvalidate itemfile=testdata/parse_pipe/3rracoonbusy.xml env=user
 * frameworkfile=content/mapping/WVA/wv_levels.txt
 * objectivesfile=content/mapping/WVA/objectives.txt.wva2
 * mappingfile=testdata/parse_pipe/raccons_map_wva_complete.txt
 * objectivesfileformat=short env=user
 * <p>
 * mapitems localimagearea=testdata/parse_pipe imagearea=.
 * itemfile=testdata/parse_pipe/3rracoonbusy.xml env=user
 * frameworkfile=content/mapping/WVA/wv_levels.txt
 * objectivesfile=content/mapping/WVA/objectives.txt.wva2
 * mappingfile=testdata/parse_pipe/raccons_map_wva.txt
 * objectivesfileformat=short env=user
 * <p>
 * importandmapitems env=user mapping.dir=testdata/mapping mapping.list=FL,WV
 * localimagearea=testdata/parse_pipe imagearea=.
 * itemfile=testdata/parse_pipe/3rracoonbusy.xml
 * <p>
 * buildtest itemfile=testdata/sofa/sofatest.xml imagearea=testdata/sofa
 * env=user
 * <p>
 * testmap_initial env=user frameworkCode=CO inputCsv=initial_mapping.csv
 * outputCsv=CO_GRADE3_MATH.csv
 * <p>
 * testmap_objective_update env=user frameworkCode=CO
 * inputCsv=CO_GRADE3_MATH.csv outputCsv=CO_GRADE3_MATH_V1.csv
 * <p>
 * testmap_merge_validate env=user frameworkCode=CO
 * inputCsv=CO_GRADE3_MATH_V1.csv outputItemMap=item_map_out2.txt
 * <p>
 * testmap_merge env=user frameworkCode=CO inputCsv=CO_GRADE3_MATH_V1.csv
 * outputItemMap=item_map_out2.txt
 * <p>
 * testmap_compare env=user frameworkCode=CO srcCsv=CO_GRADE3_MATH_V1.csv
 * tgtCsv=CO_GRADE3_MATH_V2.csv\
 * <p>
 * testmap_answerkey env=user frameworkCode=CO itemfile=itemfile
 * inputCsv=CO_GRADE3_MATH.csv outputCsv=CO_GRADE3_MATH_V1.csv
 * <p>
 * createpdfthroughfile infile=infile outfile=outfile type=type
 * <p>
 * generatemediaforeditorreview infile=testdata/3ec02.xml rootdirectory=build/
 * directory=test/ pdffilename=generateMediaForEditorPreviewPDF
 * flashfilename=generateMediaForEditorPreviewFlash
 * <p>
 * generatepdf infile=testdata/3ec02.xml rootdirectory=build/ directory=test/
 * pdffilename=generatePDF
 * <p>
 * flattenedmappinghierarchy frameworkfile=/mappingdata/cqaLevels.txt
 * objectivesFile=/mappingdata/cqaObjectives.txt
 * mappingFile=/mappingdata/cqaMappings.txt
 * <p>
 * validateitemxml env=env logfile=logfile
 */
public class ContentImportModule implements InterfaceHandlesUsageException {
	protected static Logger logger = Logger
			.getLogger(ContentImportModule.class);

	/*
	 * public static void main(String[] args) {
	 * PropertyConfigurator.configure("conf/log4j.properties");
	 * ContentImportModule cim = new ContentImportModule(); cim.begin(args); }
	 */

	public Report begin(CommandLine commandLine) {
		Report report = null;
		try {
			report = ContentImportModuleUtils.run(commandLine, this);
		} 
		catch(BusinessException ex){
			CommandReport r = new CommandReport("","");
			r.setSuccess(false);
			r.setException(ex);
			return r;
		}
		
		
		catch (Exception e) {
			System.out.print(e);
			CommandReport r = new CommandReport("","");
			r.setSuccess(false);
			r.setException(new Exception(e.getLocalizedMessage()));
			return r;
			/*PrintWriter writer = new PrintWriter(System.err);
			System.err.println("\nError while processing Command:\n"
					+ e.toString() + "\n");
			e.printStackTrace(writer);
			exitCode = 2;
			writer.flush();*/
		} /*finally {
			if (report != null) {
				boolean longReportFormat = getReportFormat(commandLine);
				PrintWriter writer = new PrintWriter(System.out);
				Formatter formatter = FormatterFactory.create(report,
						longReportFormat);
				formatter.print(writer, false);
				if (formatter instanceof CommandReportFormatter)
					((CommandReportFormatter) formatter).print(true);
				writer.flush();
			}
		}*/
		/*System.exit(exitCode);*/
		return report;
	}

	public void handleUsageException(Exception e) {
		PrintStream s = System.err;
		String output = ContentImportModuleUtils.getUsageString(e);
		s.println(output);
		System.exit(2);
	}

	protected static boolean getReportFormat(CommandLine commandLine) {
		boolean longReportFormat = true;
		final String longFormat = "longformat";
		final String reportFormat = "reportformat";
		final String shortFormat = "shortformat";
		if (longFormat.equals(commandLine.getOptionalParameterValue(
				reportFormat, shortFormat)))
			return longReportFormat;
		return !longReportFormat;
	}
}
