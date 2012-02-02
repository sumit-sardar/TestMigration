/**
 * Custom Worker. 
 *	Note: Custom Worker is a arrayProcessing worker class which takes in an array,arguments object, the runnable function 
 *  and a callback function. 
 * 	Don't use more than three worker class simultaneously cause it will lead to high CPU Utilization
 * @author: Santhana R S
 */
 
function CustomWorker(array,args,process,callback) {
	/* TODO
	 Need to Implement Singleton
	this.instance = this;
	this.instances = [];
	*/
	this.name="";
	this.stream = (function(){	
			var processArray = array.concat();
			var timer;
				return{				
				start:function(){
						timer = setTimeout(function(){
						var start = +new Date();
						do {
							 process.call(null, processArray.shift(),args);
						} while (processArray.length > 0 && (+new Date() - start < 50));
						if (processArray.length > 0){
						
							setTimeout(arguments.callee, 25);
						} else {
							callback(array);
						}
					}, 25);
				},
				kill:function(){
					processArray = [];	
					clearTimeout(timer);
				}
			}
	})();
}

CustomWorker.prototype.name = function(name){
	this.name = name;
}

CustomWorker.prototype.start = function() {
	this.stream.start();
};
CustomWorker.prototype.kill = function() {
	this.stream.kill();
};

/*TODO:
 A seperate Singelton Class should be created to manage this part
CustomWorker.prototype.noOfInstances = function() {

	return this.instances.length;
};

CustomWorker.prototype.killAll = function() {

};

CustomWorker.prototype.killInstance = function() {

    
};
*/

