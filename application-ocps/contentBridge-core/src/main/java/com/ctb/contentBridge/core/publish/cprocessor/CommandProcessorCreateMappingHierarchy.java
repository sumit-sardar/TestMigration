package com.ctb.contentBridge.core.publish.cprocessor;

import java.util.Iterator;

import org.apache.log4j.Logger;
import org.jdom.Element;

import com.ctb.contentBridge.core.publish.mapping.ItemMap;
import com.ctb.contentBridge.core.publish.mapping.Mapper;
import com.ctb.contentBridge.core.publish.mapping.MapperFactory;
import com.ctb.contentBridge.core.publish.mapping.Objectives;
import com.ctb.contentBridge.core.publish.report.HierarchyReport;
import com.ctb.contentBridge.core.publish.report.ItemProcessorReport;
import com.ctb.contentBridge.core.publish.report.ItemSetReport;
import com.ctb.contentBridge.core.publish.report.Report;
import com.ctb.contentBridge.core.publish.report.ReportFactory;
import com.ctb.contentBridge.core.publish.xml.item.ItemProcessor;
import com.ctb.contentBridge.core.publish.xml.item.ItemProcessorFactory;

public class CommandProcessorCreateMappingHierarchy implements CommandProcessor {
	private static Logger logger = Logger
			.getLogger(CommandProcessorCreateMappingHierarchy.class);
	private final ItemProcessor itemProcessor;
	private final ItemMap itemMap;
	private final Objectives objectives;

	public CommandProcessorCreateMappingHierarchy(Objectives objectives,
			ItemMap itemMap) {
		this.objectives = objectives;
		this.itemMap = itemMap;
		this.itemProcessor = ItemProcessorFactory
				.getItemProcessorGenerateMappingHierarchy();
	}

	public Report process() {
		ItemSetReport itemSetReport = ReportFactory.createItemSetReport(true);

		if (MapperFactory.getItemMap() != null) {
			for (Iterator iter = MapperFactory.getItemMap().getAllItemIDs(); iter
					.hasNext();) {
				Element element = new Element("Item");
				element.setAttribute("ID", (String) iter.next());
				try {
					itemProcessor.process(element);
					addProjectedMappedId();
				} catch (Exception e) {
					CommandProcessorUtility.handleItemException(e, logger);
				} finally {
					itemSetReport.addSubReport(ItemProcessorReport
							.getCurrentReport());
				}
			} // end for
		}

		HierarchyReport report = new HierarchyReport(this.objectives,
				this.itemMap);
		report.addSubReport(itemSetReport);
		report.setSuccess(true);
		return report;
	}

	private void addProjectedMappedId() {
		if (MapperFactory.getItemMap() != null) {
			Mapper mapper = MapperFactory.newMapper(this.objectives,
					this.itemMap);
			ItemProcessorReport report = ItemProcessorReport.getCurrentReport();
			report.setNewID(mapper.mappedItemId(report.getID()));
		}
	}
}