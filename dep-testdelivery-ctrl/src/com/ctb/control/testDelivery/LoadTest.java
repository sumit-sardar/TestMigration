package com.ctb.control.testDelivery;

import org.apache.beehive.controls.api.bean.ControlInterface;

import com.bea.control.annotations.TransactionAttribute;
import com.bea.control.annotations.TransactionAttributeType;

@ControlInterface
public interface LoadTest {

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	noNamespace.TmssvcResponseDocument getLoadTestConfig(noNamespace.TmssvcRequestDocument document);
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	noNamespace.TmssvcResponseDocument setLoadTestStatistics(noNamespace.TmssvcRequestDocument document);
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	noNamespace.TmssvcResponseDocument uploadSystemInfo(noNamespace.TmssvcRequestDocument document);
}