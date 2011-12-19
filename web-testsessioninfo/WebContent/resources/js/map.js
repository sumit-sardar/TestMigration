/**
 * Cache jstree objects. 
 * @author: Santhana R S
 */
function Map() {
    this.items = {};
    this.count = 0;
    this.keys = [];
    this.isDurty = false;
}

Map.prototype.get = function(key) {
    var item = this.items[key];
    var returnVal = null;
    if (item != null) {
        returnVal = item.value;       
    } 
    return returnVal;
};

Map.prototype.getKeys = function() {
    var i = 0;
    if(this.isDurty){
	    for (var k in this.items) {
	   	 	if(this.items[k].value !=null && this.items[k].value!= undefined) {
		   	 	this.keys[i] = k;
		   	 	 ++i;
	   	 	}
	   	 }
	   	this.isDurty = false;
	   
    }
     return this.keys;
   	 
};

Map.prototype.put = function(key, value) {
    function map(k, v) {
        if (k!=null){            
        this.key = k;
        this.value = v;
		}
    }
	//Update map
    if (this.items[key] != null){
        this._removeItem(key);
	}
    this._addItem(new map(key, value));
    this.isDurty = true;
};

Map.prototype.remove = function(key) {
	 if(this.get(key) !=null) {
	  	this._removeItem(key);
	  	this.isDurty = true;
	  	return true;
	 } else {
	   return false;
	 }
};


Map.prototype.length = function() {
	return this.count;
};

Map.prototype.clear = function() {
	this.items = {};
	this.count = 0;
};

Map.prototype._addItem = function(item) {
    this.items[item.key] = item;
    this.count++;
	var x = item;
	
};

Map.prototype._removeItem = function(key) {
    var item = this.items[key];
    delete this.items[key];
    this.count--;    
};