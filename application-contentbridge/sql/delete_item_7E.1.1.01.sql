delete datapoint_condition_code where datapoint_id in (
	   		  select datapoint_id from datapoint where item_id = '7E.1.1.01' );
			  
delete datapoint where item_id = '7E.1.1.01';

delete item_set_item where item_id = '7E.1.1.01';		  
			  
delete item where item_id = '7E.1.1.01';

